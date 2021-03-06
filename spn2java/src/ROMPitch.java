import org.andrewkilpatrick.elmGen.ElmProgram;
// ;PITCH TRANSPOSER, STEREO
// 
// mem	ldel	4096	;left delay
// mem	rdel	4096	;right delay
// mem	dtemp	1	;temporary DRAM data location
// 
// equ	potfil	reg0	;pot0 filter for smoothing
// 
// ; Initialization, only run on first executuion of code
// ; Skip the following two instructions if NOT the first time
// 
// skp	RUN, LOOP
// wldr 	0,	0, 	4096	; Load up a ramp LFO to shift up 0 octaves, A=0x0 (4096 range)
// LOOP:
// 
// rdax 	ADCL,	1.0		;read left input
// wra 	ldel, 	0.0		;write to delay start
// rdax 	ADCR,	1.0		;read right input
// wra 	rdel, 	0.0		;write to delay start
// 
// ; We use the base of the sample memory block as the
// ; address since we are using a positive only ramp
// ; that ranges 0 to 1.0 (4095 in this case)
// ;do left chan:
// 
// cho 	rda,	rmp0,reg|compc,ldel	; (1-k)*sample[addr]
// cho 	rda,	rmp0,0,ldel+1		; k*sample[addr+1] + ACC
// wra 	dtemp,	0			; Save it off to memory and clear ACC
// cho 	rda,	rmp0,rptr2|compc, ldel	; (1-k)*sample[addr+ half ramp]
// cho 	rda,	rmp0,rptr2,ldel+1		; k*sample[addr+ half ramp + 1] + ACC
// cho 	sof, 	rmp0,na|compc,0		; Result in ACC, multiply it by (1-XFADE) coefficient
// cho 	rda,	rmp0,na,dtemp		; Add in earlier value saved in memory, multiply saved value by XFADE coefficient
// wrax 	dacl,	0 			; Write it to DACL and clear ACC
// 
// ;now do right chan:
// cho 	rda,	rmp0,compc,rdel		; (1-k)*sample[addr]
// cho 	rda,	rmp0,0,rdel+1		; k*sample[addr+1] + ACC
// wra 	dtemp,	0			; Save it off to memory and clear ACC
// cho 	rda,	rmp0,rptr2|compc, rdel	; (1-k)*sample[addr+ half ramp]
// cho 	rda,	rmp0,rptr2,rdel+1		; k*sample[addr+ half ramp + 1] + ACC
// cho 	sof, 	rmp0,na|compc,0		; Result in ACC, multiply it by (1-XFADE) coefficient
// cho 	rda,	rmp0,na,dtemp		; Add in earlier value saved in memory, multiply saved value by XFADE coefficient
// wrax 	dacr,	0 			; Write it to DACL and clear ACC
// 
// 
// rdax 	pot0,1.0
// sof 	0.25,-0.125			;control ranges +/-0.125
// rdfx 	potfil,0.02			;filter result to smooth pot control
// wrax  	potfil,1.0
// wrax 	rmp0_rate,0			;write to rate of lfo2, clear accumulator
// 
// ;FT 5/05
// 
public class ROMPitch extends ElmProgram {
  public ROMPitch() {
    super("ROMPitch");
    setSamplerate(48000);
    allocDelayMem("ldel", 4096);
    allocDelayMem("rdel", 4096);
    allocDelayMem("dtemp", 1);
    int potfil = REG0;
    skip(SKP_RUN, 1);
    loadRampLFO(0, 0, 4096);
    readRegister(ADCL, 1.0);
    writeDelay("ldel", 0, 0.0);
    readRegister(ADCR, 1.0);
    writeDelay("rdel", 0, 0.0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_REG|CHO_COMPC, "ldel", 0);
    chorusReadDelay(CHO_LFO_RMP0, 0, "ldel", (int)(1));
    writeDelay("dtemp", 0, 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_RPTR2|CHO_COMPC, "ldel", 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_RPTR2, "ldel", (int)(1));
    chorusScaleOffset(CHO_LFO_RMP0, CHO_NA|CHO_COMPC, 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_NA, "dtemp", 0);
    writeRegister(DACL, 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_COMPC, "rdel", 0);
    chorusReadDelay(CHO_LFO_RMP0, 0, "rdel", (int)(1));
    writeDelay("dtemp", 0, 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_RPTR2|CHO_COMPC, "rdel", 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_RPTR2, "rdel", (int)(1));
    chorusScaleOffset(CHO_LFO_RMP0, CHO_NA|CHO_COMPC, 0);
    chorusReadDelay(CHO_LFO_RMP0, CHO_NA, "dtemp", 0);
    writeRegister(DACR, 0);
    readRegister(POT0, 1.0);
    scaleOffset(0.25, -0.125);
    readRegisterFilter(potfil, 0.02);
    writeRegister(potfil, 1.0);
    writeRegister(RMP0_RATE, 0);
  }
}
