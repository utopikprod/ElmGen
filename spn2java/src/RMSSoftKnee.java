import org.andrewkilpatrick.elmGen.ElmProgram;
// ;rms limiter, approx 10dB limiting range,
// ;soft knee, variable comp ratio.
// 
// equ	sigin	reg0
// equ	avg	reg1
// equ	gain	reg2
// 
// rdax	adcl,0.5
// rdax	adcr,0.5
// wrax	sigin,1		;write mono input signal
// mulx	sigin		;square input
// rdfx	avg,0.001	;average squared result
// wrax	avg,0		;avg stored, ACC cleared
// 
// sof	0,0.125		;put 1/8 in acc
// rdax	avg,1		;add our averaged input level
// log	-0.4,-0.25	;square root and 1/x combined
// exp	1,0		;
// wrax	gain,1
// mulx	adcl
// sof	-2,0		;restore gain, to avoid output clipping
// sof	-2,0
// sof	1.5,0
// wrax	dacl,0
// rdax	gain,1
// mulx	adcr
// sof	1.5,0
// 
// clr
// rdax	sigin,1
// wrax	dacr,0		;write outputs, zero ACC
public class RMSSoftKnee extends ElmProgram {
  public RMSSoftKnee() {
    super("RMSSoftKnee");
    setSamplerate(48000);
    int sigin = REG0;
    int avg = REG1;
    int gain = REG2;
    readRegister(ADCL, 0.5);
    readRegister(ADCR, 0.5);
    writeRegister(sigin, 1);
    mulx(sigin);
    readRegisterFilter(avg, 0.001);
    writeRegister(avg, 0);
    scaleOffset(0, 0.125);
    readRegister(avg, 1);
    log(-0.4, -0.25);
    exp(1, 0);
    writeRegister(gain, 1);
    mulx(ADCL);
    scaleOffset(-2, 0);
    scaleOffset(-2, 0);
    scaleOffset(1.5, 0);
    writeRegister(DACL, 0);
    readRegister(gain, 1);
    mulx(ADCR);
    scaleOffset(1.5, 0);
    clear();
    readRegister(sigin, 1);
    writeRegister(DACR, 0);
  }
}
