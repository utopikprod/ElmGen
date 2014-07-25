import org.andrewkilpatrick.elmGen.ElmProgram;
// ;reverb program
// ;moderate sized reverb, stereo in and out
// ;for mixers
// ;pot0=reverb time
// ;pot1=low freq response
// ;pot2=high freq response
// ;
// mem	lap1	156
// mem	lap2	223
// mem	lap3	332
// mem	lap4	548
// mem	rap1	186
// mem	rap2	253
// mem	rap3	302
// mem	rap4	498
// ;
// mem	ap1	1251
// mem	ap1b	1551
// mem	ap2	943
// mem	ap2b	1343
// mem	ap3	1282
// mem	ap3b	1381
// mem	ap4	1174
// mem	ap4b	1382
// ;
// mem	del1	2859
// mem	del2	3145
// mem	del3	2476
// mem	del4	3568
// ;
// mem	ldel	3000
// mem	rdel	3000
// ;
// equ	rout1	del1+2420
// equ	rout2	del2+1631
// equ	rout3	del3+1163
// equ	rout4	del4+2330
// ;
// equ	lout1	del1+2630
// equ	lout2	del2+1943
// equ	lout3	del3+1200
// equ	lout4	del4+2016
// ;
// ;set up lfo, ~0.5Hz:
// skp	run,start
// wlds	sin0,12,	160
// 
// start:
// ;do ap smearing to ap1 and ap3:
// cho 	RDA,	sin0,	0x06,	ap1+50
// cho	RDA,	sin0,	0,	ap1+51
// wra	ap1+100,	0
// cho 	RDA,	sin0,	0x07,	ap3+50
// cho	RDA,	sin0,	01,	ap3+51
// wra	ap3+100,	0
// 
// 
// ;prepare pots for control:
// rdax	POT0,	1.0
// sof 	0.6, 	0.2
// wrax	reg0,	0
// ;
// ;shelving controls are negative:
// rdax	POT1,	1.0
// sof	0.8, 	-0.8
// wrax 	reg1,	0
// ;
// rdax	POT2,	1.0
// sof 	0.8,	-0.8
// wrax 	reg2,0
// ;
// ;get inputs and process with three APs each
// ;
// rdax	adcl,	0.6
// rda	lap1#,	-0.6
// wrap	lap1,	0.6
// rda	lap2#,	-0.6
// wrap	lap2,	0.6
// wra	ldel,	1.0
// rda	lap3#,	-0.6
// wrap	lap3,	0.6
// rda	lap4#,	-0.6
// wrap	lap4,	0.6
// wrax	reg10,	0
// ;
// rdax	adcr,	0.6
// rda	rap1#,	-0.6
// wrap	rap1,	0.6
// rda	rap2#,	-0.6
// wrap	rap2,	0.6
// wra	rdel,	1.0
// rda	rap3#,	-0.6
// wrap	rap3,	0.6
// rda	rap4#,	-0.6
// wrap	rap4,	0.6
// wrax	reg11,	0
// ;
// ;now do loop, use reg9 as temp reg for filtering:
// ;
// ;delay ap into 1:
// 
// rda	del4#,	1.0
// mulx	reg0
// rdax	reg10,	1.0
// rda	ap1#,	-0.6
// wrap	ap1,	0.6
// rda	ap1b#,	-0.5
// wrap	ap1b,	0.5
// wrax	reg9,	1.0
// rdfx	reg12,	0.5
// wrhx	reg12,	-1.0
// mulx	reg1
// rdax	reg9,	1.0
// wrax	reg9,	1.0
// rdfx	reg13,	0.05
// wrlx	reg13,	-1.0
// mulx	reg2
// rdax	reg9,	1.0
// wra	del1,	0.0
// ;
// ;delay ap into 2:
// rda	del1#,	1.0
// mulx	reg0
// rdax	reg11,	1.0
// rda	ap2#,	-0.6
// wrap	ap2,	0.6
// rda	ap2b#,	-0.5
// wrap	ap2b,	0.5
// wrax	reg9,	1.0
// rdfx	reg14,	0.5
// wrhx	reg14,	-1.0
// mulx	reg1
// rdax	reg9,	1.0
// wrax	reg9,	1.0
// rdfx	reg15,	0.05
// wrlx	reg15,	-1.0
// mulx	reg2
// rdax	reg9,	1.0
// wra	del2,	0.0
// ;
// ;delay ap into 3:
// rda	del2#,	1.0
// mulx	reg0
// rdax	reg10,	1.0
// rda	ap3#,	-0.6
// wrap	ap3,	0.6
// rda	ap3b#,	-0.5
// wrap	ap3b,	0.5
// wrax	reg9,	1.0
// rdfx	reg16,	0.5
// wrhx	reg16,	-1.0
// mulx	reg1
// rdax	reg9,	1.0
// wrax	reg9,	1.0
// rdfx	reg17,	0.05
// wrlx	reg17,	-1.0
// mulx	reg2
// rdax	reg9,	1.0
// wra	del3,	0.0
// ;
// ;delay ap into 4:
// rda	del3#,	1.0
// mulx	reg0
// rdax	reg11,	1.0
// rda	ap4#,	-0.6
// wrap	ap4,	0.6
// rda	ap4b#,	-0.5
// wrap	ap4b,	0.5
// wrax	reg9,	1.0
// rdfx	reg18,	0.5
// wrhx	reg18,	-1.0
// mulx	reg1
// rdax	reg9,	1.0
// wrax	reg9,	1.0
// rdfx	reg19,	0.05
// wrlx	reg19,	-1.0
// mulx	reg2
// rdax	reg9,	1.0
// wra	del4,	0.0
// ;
// rda	lout1,	0.8
// rda	lout2,	0.7
// rda	lout3,	0.6
// rda	lout4,	0.5
// rda	ldel+180,	0.8
// rda	ldel+1194,	0.7
// rda	ldel+2567,	0.6
// rda	ldel+2945,	0.5
// wrax	dacl, 0.0
// ;
// rda	rout3,	0.8
// rda	rout4,	0.7
// rda	rout1,	0.6
// rda	rout2,	0.5
// rda	rdel+265,	0.8
// rda	rdel+1265,	0.7
// rda	rdel+2265,	0.6
// wrax	dacr, 0.0
// ;
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 
public class ROMRev2 extends ElmProgram {
  public ROMRev2() {
    super("ROMRev2");
    setSamplerate(48000);
    allocDelayMem("lap1", 156);
    allocDelayMem("lap2", 223);
    allocDelayMem("lap3", 332);
    allocDelayMem("lap4", 548);
    allocDelayMem("rap1", 186);
    allocDelayMem("rap2", 253);
    allocDelayMem("rap3", 302);
    allocDelayMem("rap4", 498);
    allocDelayMem("ap1", 1251);
    allocDelayMem("ap1b", 1551);
    allocDelayMem("ap2", 943);
    allocDelayMem("ap2b", 1343);
    allocDelayMem("ap3", 1282);
    allocDelayMem("ap3b", 1381);
    allocDelayMem("ap4", 1174);
    allocDelayMem("ap4b", 1382);
    allocDelayMem("del1", 2859);
    allocDelayMem("del2", 3145);
    allocDelayMem("del3", 2476);
    allocDelayMem("del4", 3568);
    allocDelayMem("ldel", 3000);
    allocDelayMem("rdel", 3000);
    
    
    
    
    
    
    
    
    skip(SKP_RUN, 1);
    loadSinLFO(0, 12, 160);
    chorusReadDelay(CHO_LFO_SIN0, 0x06, "ap1", (int)(50));
    chorusReadDelay(CHO_LFO_SIN0, 0, "ap1", (int)(51));
    writeDelay("ap1", (int)(100), 0);
    chorusReadDelay(CHO_LFO_SIN0, 0x07, "ap3", (int)(50));
    chorusReadDelay(CHO_LFO_SIN0, 01, "ap3", (int)(51));
    writeDelay("ap3", (int)(100), 0);
    readRegister(POT0, 1.0);
    scaleOffset(0.6, 0.2);
    writeRegister(REG0, 0);
    readRegister(POT1, 1.0);
    scaleOffset(0.8, -0.8);
    writeRegister(REG1, 0);
    readRegister(POT2, 1.0);
    scaleOffset(0.8, -0.8);
    writeRegister(REG2, 0);
    readRegister(ADCL, 0.6);
    readDelay("lap1", 1.0, -0.6);
    writeAllpass("lap1", 0, 0.6);
    readDelay("lap2", 1.0, -0.6);
    writeAllpass("lap2", 0, 0.6);
    writeDelay("ldel", 0, 1.0);
    readDelay("lap3", 1.0, -0.6);
    writeAllpass("lap3", 0, 0.6);
    readDelay("lap4", 1.0, -0.6);
    writeAllpass("lap4", 0, 0.6);
    writeRegister(REG10, 0);
    readRegister(ADCR, 0.6);
    readDelay("rap1", 1.0, -0.6);
    writeAllpass("rap1", 0, 0.6);
    readDelay("rap2", 1.0, -0.6);
    writeAllpass("rap2", 0, 0.6);
    writeDelay("rdel", 0, 1.0);
    readDelay("rap3", 1.0, -0.6);
    writeAllpass("rap3", 0, 0.6);
    readDelay("rap4", 1.0, -0.6);
    writeAllpass("rap4", 0, 0.6);
    writeRegister(REG11, 0);
    readDelay("del4", 1.0, 1.0);
    mulx(REG0);
    readRegister(REG10, 1.0);
    readDelay("ap1", 1.0, -0.6);
    writeAllpass("ap1", 0, 0.6);
    readDelay("ap1b", 1.0, -0.5);
    writeAllpass("ap1b", 0, 0.5);
    writeRegister(REG9, 1.0);
    readRegisterFilter(REG12, 0.5);
    writeRegisterHighshelf(REG12, -1.0);
    mulx(REG1);
    readRegister(REG9, 1.0);
    writeRegister(REG9, 1.0);
    readRegisterFilter(REG13, 0.05);
    writeRegisterLowshelf(REG13, -1.0);
    mulx(REG2);
    readRegister(REG9, 1.0);
    writeDelay("del1", 0, 0.0);
    readDelay("del1", 1.0, 1.0);
    mulx(REG0);
    readRegister(REG11, 1.0);
    readDelay("ap2", 1.0, -0.6);
    writeAllpass("ap2", 0, 0.6);
    readDelay("ap2b", 1.0, -0.5);
    writeAllpass("ap2b", 0, 0.5);
    writeRegister(REG9, 1.0);
    readRegisterFilter(REG14, 0.5);
    writeRegisterHighshelf(REG14, -1.0);
    mulx(REG1);
    readRegister(REG9, 1.0);
    writeRegister(REG9, 1.0);
    readRegisterFilter(REG15, 0.05);
    writeRegisterLowshelf(REG15, -1.0);
    mulx(REG2);
    readRegister(REG9, 1.0);
    writeDelay("del2", 0, 0.0);
    readDelay("del2", 1.0, 1.0);
    mulx(REG0);
    readRegister(REG10, 1.0);
    readDelay("ap3", 1.0, -0.6);
    writeAllpass("ap3", 0, 0.6);
    readDelay("ap3b", 1.0, -0.5);
    writeAllpass("ap3b", 0, 0.5);
    writeRegister(REG9, 1.0);
    readRegisterFilter(REG16, 0.5);
    writeRegisterHighshelf(REG16, -1.0);
    mulx(REG1);
    readRegister(REG9, 1.0);
    writeRegister(REG9, 1.0);
    readRegisterFilter(REG17, 0.05);
    writeRegisterLowshelf(REG17, -1.0);
    mulx(REG2);
    readRegister(REG9, 1.0);
    writeDelay("del3", 0, 0.0);
    readDelay("del3", 1.0, 1.0);
    mulx(REG0);
    readRegister(REG11, 1.0);
    readDelay("ap4", 1.0, -0.6);
    writeAllpass("ap4", 0, 0.6);
    readDelay("ap4b", 1.0, -0.5);
    writeAllpass("ap4b", 0, 0.5);
    writeRegister(REG9, 1.0);
    readRegisterFilter(REG18, 0.5);
    writeRegisterHighshelf(REG18, -1.0);
    mulx(REG1);
    readRegister(REG9, 1.0);
    writeRegister(REG9, 1.0);
    readRegisterFilter(REG19, 0.05);
    writeRegisterLowshelf(REG19, -1.0);
    mulx(REG2);
    readRegister(REG9, 1.0);
    writeDelay("del4", 0, 0.0);
    readDelay("del1", 2630, 0.8);
    readDelay("del2", 1943, 0.7);
    readDelay("del3", 1200, 0.6);
    readDelay("del4", 2016, 0.5);
    readDelay("ldel", (int)(180), 0.8);
    readDelay("ldel", (int)(1194), 0.7);
    readDelay("ldel", (int)(2567), 0.6);
    readDelay("ldel", (int)(2945), 0.5);
    writeRegister(DACL, 0.0);
    readDelay("del3", 1163, 0.8);
    readDelay("del4", 2330, 0.7);
    readDelay("del1", 2420, 0.6);
    readDelay("del2", 1631, 0.5);
    readDelay("rdel", (int)(265), 0.8);
    readDelay("rdel", (int)(1265), 0.7);
    readDelay("rdel", (int)(2265), 0.6);
    writeRegister(DACR, 0.0);
  }
}
