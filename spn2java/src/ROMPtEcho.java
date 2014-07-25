import org.andrewkilpatrick.elmGen.ElmProgram;
// ;pitch transopse in left channel, echo in right chan
// ;pot0 adjusts pitch in left channel
// ;pot1 adjusts echo delay time
// ;pot2 adjusts echo mix
// 
// mem	pdel	4096	;delay for pitch transposing
// mem	pmix	1
// mem	echo	20000	;echo delay
// 
// equ	potfil	reg0	;adjusts pitch
// equ	temp	reg1
// equ	efil	reg2
// 
// ; Initialization, only run on first executuion of code
// ; Skip the following two instructions if NOT the first time
// 
// skp	RUN, 	loop
// wldr	0,	0,	4096 		; Load up ramp0 LFO to shift up 0 octaves, A=0x0 (4096 range)
// loop:
// 
// ;set up pot0 to control pitch shift:
// 
// rdax 	pot0,	1.0			; Read in pot 0
// sof 	0.25,	 -0.125			; POT0 in ACC, -0.125 to +0.125
// rdfx 	potfil,	0.02			; Smooth the result to avoid jumping
// wrax 	potfil,	1.0
// wrax 	rmp0_rate,	0		; Write to LFO control register
// 
// ; Do the pitch shift:
// 
// rdax 	adcl,1.0 			; Read in a value, ADCL * 1.0 -> ACC
// wra 	pdel,0 				; Write it to memory address 0
// cho 	rda,rmp0,reg|compc,pdel		; (1-k)*sample[addr]
// cho 	rda,rmp0,0,pdel+1			; k*sample[addr+1] + ACC
// wra 	pmix,0 				; Save it off to memory and clear ACC
// 
// ; Do other transposer tap:
// 
// cho 	rda,rmp0,rptr2|compc,pdel		; (1-k)*sample[addr+ half ramp]
// cho 	rda,rmp0,rptr2,pdel+1		; k*sample[addr+ half ramp + 1] + ACC
// 
// ;crossfade between taps:
// 
// cho 	sof,rmp0,na|compc,0		; Result in ACC, multiply it by (1-XFADE) coefficient
// cho 	rda,rmp0,na, pmix			; Add value saved in memory, multiply value by XFADE coefficient
// wrax 	dacl,0				; Write it to DACL and clear ACC
// 
// 
// ; Now do an echo for right
// ; Select tap from delay based on pot, should range 4915 to 17640
// ; Since pot only has 512 states, want to filter pot to avoid jumping
// 
// or 	0x31b500		; Put 12725 (17640-4915) into ACC alligned to ACC[22:8]
// mulx 	pot1			; Multiply by POT1
// wrax 	temp,	0		; save to a temp reg
// or 	0x233500		; load in 4915+4098 (4098 is base of delay) to add
// rdax 	temp,	1.0		; Add the temp reg to it
// rdfx 	efil,	0.02		; Smooth it
// wrax 	efil,	1.0
// wrax 	addr_ptr,0		; Write it to the address pointer register
// rmpa 	1			; Read from memory
// mulx 	pot2			; Value in ACC, multiply by POT2
// rdax 	adcr,	1.0		;add input
// wrax 	dacr,	0		; ACC-> DAC
// rmpa 	0.25			; Read the delay again, multiply by feed back coeff
// rdax 	adcr,	1		; Add the input
// wra 	echo,	0		; Write it to the delay
// 
// 
public class ROMPtEcho extends ElmProgram {
  public ROMPtEcho() {
    super("ROMPtEcho");
    setSamplerate(48000);
    allocDelayMem("pdel", 4096);
    allocDelayMem("pmix", 1);
    allocDelayMem("echo", 20000);
    int potfil = REG0;
    int temp = REG1;
    int efil = REG2;
    skip(SKP_RUN, 1);
    loadRampLFO(0, 0, 4096);
    readRegister(POT0, 1.0);
    scaleOffset(0.25, -0.125);
    readRegisterFilter(potfil, 0.02);
    writeRegister(potfil, 1.0);
    writeRegister(RMP0_RATE, 0);
    readRegister(ADCL, 1.0);
    writeDelay("pdel", 0, 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_REG|CHO_COMPC, "pdel", 0);
    chorusReadDelay(CHO_LFO_RMP0, 0, "pdel", (int)(1));
    writeDelay("pmix", 0, 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_RPTR2|CHO_COMPC, "pdel", 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_RPTR2, "pdel", (int)(1));
    chorusScaleOffset(CHO_LFO_RMP0, CHO_NA|CHO_COMPC, 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_NA, "pmix", 0);
    writeRegister(DACL, 0);
    or(0x31b500);
    mulx(POT1);
    writeRegister(temp, 0);
    or(0x233500);
    readRegister(temp, 1.0);
    readRegisterFilter(efil, 0.02);
    writeRegister(efil, 1.0);
    writeRegister(ADDR_PTR, 0);
    readDelayPointer(1);
    mulx(POT2);
    readRegister(ADCR, 1.0);
    writeRegister(DACR, 0);
    readDelayPointer(0.25);
    readRegister(ADCR, 1);
    writeDelay("echo", 0, 0);
  }
}
