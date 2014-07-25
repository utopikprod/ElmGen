import org.andrewkilpatrick.elmGen.ElmProgram;
// ;rms limiter, approx 10dB limiting range.
// 
// equ	sigin	reg0
// equ	avg	reg1
// equ	gain	reg2
// equ	expg	reg3
// equ	expavg	reg4
// equ	test	reg5
// 
// rdax	adcl,0.5
// rdax	adcr,0.5
// wrax	sigin,1		;write mono input signal
// mulx	sigin		;square input
// rdfx	avg,0.001	;average squared result
// wrax	avg,1		;avg stored and in ACC
// log	-0.5,-0.125	;square root and 1/x combined
// exp	1,0
// wrax	gain,0
// ;now create an expansion value to
// ;modify the gain value
// rdax	sigin,1		;read input common input signal
// sof	-2,0		;and provide lots of gain
// sof	-2,0
// sof	-2,0
// sof	-2,0		;add more gain and insert
// sof	-2,0		;pot function here to vary
// sof	-2,0		;expansion threshold
// sof	-2,0
// sof	-2,0
// absa			;get abs value
// rdfx	expavg,0.001	;average result
// wrax	expavg,1	;avg stored and in ACC
// log	0.5,0		;take square root; this will
// exp	1,0		;cause gain to be reduced more gracelfully
// mulx	gain		;multiply by gain
// wrax	gain,1
// 
// mulx	adcl		;use final gain value to affect output
// sof	1.5,0		;restore gain, to avoid output clipping
// wrax	dacl,0
// 
// rdax	gain,1
// mulx	adcr
// sof	1.5,0
// wrax	dacr,0		;write outputs, zero ACC
public class RMSLimExp extends ElmProgram {
  public RMSLimExp() {
    super("RMSLimExp");
    setSamplerate(48000);
    int sigin = REG0;
    int avg = REG1;
    int gain = REG2;
    int expg = REG3;
    int expavg = REG4;
    int test = REG5;
    readRegister(ADCL, 0.5);
    readRegister(ADCR, 0.5);
    writeRegister(sigin, 1);
    mulx(sigin);
    readRegisterFilter(avg, 0.001);
    writeRegister(avg, 1);
    log(-0.5, -0.125);
    exp(1, 0);
    writeRegister(gain, 0);
    readRegister(sigin, 1);
    scaleOffset(-2, 0);
    scaleOffset(-2, 0);
    scaleOffset(-2, 0);
    scaleOffset(-2, 0);
    scaleOffset(-2, 0);
    scaleOffset(-2, 0);
    scaleOffset(-2, 0);
    scaleOffset(-2, 0);
    absa();
    readRegisterFilter(expavg, 0.001);
    writeRegister(expavg, 1);
    log(0.5, 0);
    exp(1, 0);
    mulx(gain);
    writeRegister(gain, 1);
    mulx(ADCL);
    scaleOffset(1.5, 0);
    writeRegister(DACL, 0);
    readRegister(gain, 1);
    mulx(ADCR);
    scaleOffset(1.5, 0);
    writeRegister(DACR, 0);
  }
}
