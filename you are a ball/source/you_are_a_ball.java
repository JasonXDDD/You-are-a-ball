import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class you_are_a_ball extends PApplet {

final static String ICON  = "MyIcon.png";
final static String TITLE = "You are a ball!";

Minim minim;
AudioPlayer sound1,sound2,sound3,sound4,sound5,dead,shadow,bonusound,open,close;
PrintWriter output;
BufferedReader reader;
boolean[]kP = new boolean[128];
boolean fall = false;
boolean inBonus = false;
boolean colorUp = true;
boolean pressUp = false;
boolean showUp = true;
boolean save = true;
boolean shadow2 = false;
boolean shadow3 = false;
boolean fDown = true;
boolean shadowPlay = false;
boolean deadPlay = false;
boolean bonusPlaying = false;
boolean iAddSlow = false;
boolean iMute = false;
float x,y;
float xSpeed;
float ySpeed;
float g = 0.7f;
float xx,yy;
float t=0,t2=0,t3=0,tb,twb=0;
float xBonus,yBonus;
float xv,yv = 0;//shock xy
int tv,tr = 0;//shock time
float rectLength = 150;
float xRect = 250;//platform x
int score = 0,best = 0;
int bonus;
int b,c,d,e = 73;
int f=300 ;
int cRandom=130;
int textLenght10;
int bonusRate;
float frameT = 60,frameAddT;
float stopA=460,stopB=480,stopC=0,stopD=0;
float strokeT = 0,strokeWT = 0;
int bestMid = 0;
float shadowX;
int upT = 0;
int soundPlay = 0;
String line;
float sx1,sx2,sx3,sy1,sy2,sy3;
int p1 = 128,p2,p3;
int shadowT = -1;
int bonusLast = 1;
int bonusAddSlow = 280;
float stopSave;
int muteT = 0;

public void setup(){
  size(500,500);
  x = width/2;
  y = 405;
  ySpeed = -10;
  minim = new Minim(this);
  sound1 = minim.loadFile("1.wav");
  sound2 = minim.loadFile("2.wav");
  sound3 = minim.loadFile("3.wav");
  sound4 = minim.loadFile("4.wav");
  sound5 = minim.loadFile("5.wav");
  dead = minim.loadFile("dead.wav"); 
  shadow = minim.loadFile("shadow.wav");
  bonusound =  minim.loadFile("bonus.wav");
  open = minim.loadFile("open.wav");
  close = minim.loadFile("close.wav");
  noStroke();
  //output = createWriter("bestData.txt");
  //reader = createReader("bestData.txt");
  changeAppIcon( loadImage(ICON) );
  changeAppTitle(TITLE);
}

public void draw(){
  //setting
  frameRate(frameT);
  colorMode(HSB);
  b = color(cRandom,30,50);
  c = color(cRandom,100,100);
  d = color(cRandom,100,50);
  background(c);
  fill(0xffC7CEAD);
  xSpeed=0;
  xx = 50;
  yy = 50;
  tv -= 1; 
  strokeT -= 125/frameT;
  strokeWT += 16;
  upT++;
  
  if(e<=360 && y<500){
    e++;
  }else if(e >= 360 && y<500){
    e = 0;
  }
  
  //Setting key
  if(kP[RIGHT]){
  xSpeed=9;
  }else if(kP[LEFT]){
  xSpeed=-9;
  }
  
  //Mute
  if(kP[77]&&iMute == false){
    iMute = true;
    kP[77] = false;
    close.rewind();
    close.play();
    shadow.pause();
    dead.pause();
  }else if(kP[77]&&iMute == true){
    iMute = false;
    kP[77] = false;
    open.rewind();
    open.play();
  }
  muteT -= 3;
  fill(0xffFFFFFF,muteT);
  textSize(25);
  if(iMute == false)text("'M' close the music",142+xv,468+yv);
  if(iMute == true)text("'M' open the music",143+xv,468+yv);
  

  //On floor give speed
  if(y>=405&&fall==false){
  ySpeed=-20;
  }
  
  //set speed
  if(pressUp==false){
  ySpeed+=g;
  y+=ySpeed;
  x+=xSpeed;
  }
  
   if((kP[UP] || kP[32]) && stopD<=500 && y<500  && shadowPlay == false){
    shadowPlay = true;
    shadow.rewind();
  }
  
  //slow
  if((kP[UP] || kP[32]) && stopD<=500 && y<500){
    if(iMute == false){
       shadow.play();
    }
    frameRate(frameT-40);
    twb++;
    if(twb%17<=8&&stopD<=495){
    background(b);
    }
      stopA-=1800/frameT;
    if(stopA<=20){
      stopA=0;
      stopB-=1800/frameT;
      if(stopB<=20){
        stopB=0;
        stopC+=1800/frameT;
        if(stopC>=500){
          stopC=500;
          stopD+=1800/frameT;
          if(stopD>=500){
            kP[UP] = false;
          }
        }
      }
    }
  }else if((kP[UP] || kP[32]) == false){
    twb = 0;
    shadow.pause();
    shadowPlay = false;
  }
  if(stopD>=490){
  shadow.pause();
  }
  
  //recover slow Mp
  if(stopA<460&&stopB>=480&&stopC<=0&&stopD<=0&&y<600){
    stopA+=1.25f;
  }
  if(stopB<480&&stopC<=0&&stopD<=0&&y<600){
    stopB+=1.25f;
    }
  if(stopC>0&&stopD<=0&&y<600){
    stopC-=1.25f;
  }else if(stopC<=0){
    stopC = 0;
  }
  if(stopD>0&&y<600){
    stopD-=1.25f;
  }
  
  //show slow
  fill(d);
  noStroke();
  rect(20,480,stopA,20); 
  rect(0,20,20,stopB);
  rect(stopC,0,480,20);
  rect(480,stopD,20,500);
  
  //X Border
  if(x>=475){
  x=475;
  }else if(x<=25){
  x=25;
  }
  
  //exchange platform
  if(y>=405&&y<=450&&x>=xRect-15-rectLength/2&&x<=xRect+rectLength+15-rectLength/2){
  if(rectLength<=500){
  xRect=random(0+rectLength/2,500-rectLength+rectLength/2);
  }else if(rectLength>500){
    xRect = rectLength/2;
  }
    fall = false;
  }else if((y>=405&&x<=xRect-15-rectLength/2)||(y>=405&&x>=xRect+rectLength+15-rectLength/2)){
    fall = true;
    if(save == true){
    line = "hello";
    //output.println(best);
    //output.flush(); // Writes the remaining data to the file
    //save = false;
    }
  } 
  
  if(y>=500&&deadPlay==false){
  dead.rewind();
  if(iMute == false){
    dead.play();
  }
  deadPlay = true;
  shadow.pause();
  }  
  
  //show reset
  if(y>=500){
  frameT=35;
  tr++;
    if(tr%21>=10){
  fill(0xffFFFFFF);
  textSize(25);
  text("Please Press '\u2193'",width/2-85,80);
  
  }
  }
  
  //Touch platform
  if(y>=405&&fall==false){
    bonusPlaying = true;
    if(iMute == false){
    soundPlay = PApplet.parseInt(random(1,5));
    switch(soundPlay){
      case 1:
        sound1.rewind();
        sound1.play();
        break;
      case 2:
        sound2.rewind();
        sound2.play();
        break;
      case 3:
        sound3.rewind();
        sound3.play();
        break;
      case 4:
        sound4.rewind();
        sound4.play();
        break;
      case 5:
        sound5.rewind();
        sound5.play();
        break;
    }
    }
    y = 405;
    tv = 17;
    score++;
    frameT+=frameAddT;
    strokeT = 60;
    strokeWT = 0;
    cRandom = PApplet.parseInt(random(360));
    shadowX = x+xv;
    if(rectLength>250){
      rectLength=rectLength*0.886f;
    }else if(rectLength<=250&&rectLength>130)
      {rectLength = rectLength*0.907f;
    }else if(rectLength<=130&&rectLength>80)
      {rectLength = rectLength*0.938f; 
    }else if(rectLength<=80&&rectLength>=0)
      {rectLength = rectLength*0.965f;
    }
    //println(score,frameT);
  }
  
  //more fast
  frameAddT = pow(2,-0.018f*score+10.8f)/pow(2,11);

  //the best score
  if(score >= best){
    best = score;
  }
  if(best>=10 && best<100){
    bestMid=6;
  }else if(best>=100 && best<1000){
    bestMid=15;
  }else if(best>=1000){
    bestMid=25;
  }
  
  //shock
  if(tv%2 == 1&&tv>12){
    xv = random(-14,14);
    yv = random(-14,14);
  }else if(tv%2 == 1&&tv<=12&&tv>6){
    xv = random(-10,10);
    yv = random(-10,10);
  }else if(tv%2 == 1&&tv<=6&&tv>0){
    xv = random(-6,6);
    yv = random(-6,6);
  }else if(tv==0){
  xv = 0;
  yv = 0;
  }

  //score2 3 4
  if(score>=10&&score<=99){
  textLenght10=70;
  }else if(score>=100&&score<=999){
  textLenght10=130;
  }else if(score>=1000){
  textLenght10=200;
  }


  //show inmage
  noStroke();
  fill(d);
  textSize(200); 
  text(score,width/2-60-textLenght10+xv,height/2+75+yv);
  textSize(30); 
  text("BEST",200-bestMid+xv,140-bestMid+yv);
  text(best,280-bestMid+xv,140-bestMid+yv);
  fill(e,45,250);
  rect(xRect+xv-rectLength/2,405+yv,rectLength,30);
  
  strokeWeight(strokeWT);
  stroke(e,45,250,strokeT);
  noFill();
  rect(shadowX+xv-rectLength/2,405+yv,rectLength,30);
  
  //show Up
  if(upT>=31 && (kP[UP] || kP[32])==false && showUp==true){
    pressUp = true;
    fill(d,100);
    rect(width/2-250,height/2-22.5f,500,45);
    if(fDown==true){
      f -= 9;
    }
    if(f <= 50)fDown = false;
    if(fDown==false){
      f += 9;
    }
    if(f >= 300)fDown = true;
    fill(0xffffffff,f);
    textSize(26);
    noStroke();
    text("Hold '\u2191' or 'SPACE' Freeze Time",width/2-195,height/2+11);
  }if(upT>=30 && (kP[UP] || kP[32])){
    showUp = false;
    pressUp = false;
  }

  //bonus
  if(y>=405&&fall==false&&inBonus==false){
    bonus = PApplet.parseInt(random(1,bonusRate));
    xBonus = random(480);
    yBonus = random(100,220);
    tb = frameT*2.6f;
    bonusLast++;
    if(bonusLast>=12&&rectLength<=125){
      bonusLast = 1;
      bonus = 1;
    }
  }
  if(bonus == 1 && tb > 0){
    bonusLast = 1;
    inBonus = true;
    iAddSlow = true;
    fill(0xffffffff);
    strokeWeight(20);
    stroke(0xffffffff,70);
    rect(xBonus+xv,yBonus+yv,20,20);
    tb--;
    if(x>=xBonus-20&&x<=xBonus+40&&y-yy/2>=yBonus-20&&y-yy/2<=yBonus+40&&rectLength<=110){
      rectLength=rectLength*1.9f;
      tb=0;
    }
    else if(x>=xBonus-20&&x<=xBonus+40&&y-yy/2>=yBonus-20&&y-yy/2<=yBonus+40&&rectLength>110&&rectLength<=280){
      rectLength=rectLength*1.6f;
      tb=0;
    }
    else if(x>=xBonus-20&&x<=xBonus+40&&y-yy/2>=yBonus-20&&y-yy/2<=yBonus+40&&rectLength>280){
      rectLength=rectLength*1.4f;
      tb=0;
    }
    if(x>=xBonus-20&&x<=xBonus+40&&y-yy/2>=yBonus-20&&y-yy/2<=yBonus+40&&bonusPlaying == true){
    bonusound.rewind();
    if(iMute == false){
      bonusound.play();
    }
    bonusPlaying = false;
    }
    if(x>=xBonus-20&&x<=xBonus+40&&y-yy/2>=yBonus-20&&y-yy/2<=yBonus+40&&iAddSlow == true){
        if(stopA<460){
            if(stopB<480){
                if(stopC>0){
                    if(stopD>0){
                        stopSave = stopD;
                        stopD -= bonusAddSlow;
                        if(stopD <= 0){
                            stopD = 0;
                            stopC -= (bonusAddSlow - stopSave);
                        }
                        iAddSlow = false;
                    }
                    if(iAddSlow == true){
                    stopSave = stopC;
                    stopC -= bonusAddSlow;
                    if(stopC <= 0){
                        stopC = 0;
                        stopB += (bonusAddSlow - stopSave);
                    }
                    iAddSlow = false;
                    }
                }
                if(iAddSlow == true){
                stopSave = stopB;
                stopB += bonusAddSlow;
                if(stopB >= 500){
                    stopB = 500;
                    stopA += stopSave + bonusAddSlow - 500;
                    }
                iAddSlow = false;
                }
            }
            if(iAddSlow == true){
            stopA += bonusAddSlow;
            if(stopA >= 460){
                stopA = 460;
            }
            iAddSlow = false;
            }
        }
    }
  }else if (tb <= 0){
    inBonus = false;
  }

  //bonus rate
  if(rectLength<=60){
      bonusRate = 6; 
    }else if(rectLength>60&&rectLength<=110){
      bonusRate = 8;   
    }
    else if(rectLength>110&&rectLength<=280){
      bonusRate = 10;
    }
    else if(rectLength>280){
      bonusRate = 12;
    }
    
  //Shadow 
  if((kP[UP] || kP[32])&&stopD<=490&&y<500){
    shadowT++;
    if(shadowT>12){
      shadowT = 0;
    }
    switch(shadowT){
      case 0:
        sx1 = x;
        sy1 = y-yy/2;
        p1 = 110;
        p3 = 65;
        p2 = 20;
        break;
      case 4:
        sx2 = x;
        sy2 = y-yy/2;
        shadow2 = true;
        p2 = 110;
        p1 = 65;
        p3 = 20;
        break;
      case 8:
        sx3 = x;
        sy3 = y-yy/2;
        shadow3 = true;
        p3 = 110;
        p2 = 65;
        p1 = 20;
        break;
    }
    noStroke();
    fill(e,45,250,p1);
    ellipse(sx1+xv,sy1+yv,xx,yy);
    if(shadow2==true){
    fill(e,45,250,p2);
    ellipse(sx2+xv,sy2+yv,xx,yy);}
    if(shadow3==true){
    fill(e,45,250,p3);
    ellipse(sx3+xv,sy3+yv,xx,yy);}
  }else if(kP[UP]==false){
    shadowT = -1;
    shadow2 = false;
    shadow3 = false;
  }
  noStroke();
  fill(e,45,250);
  ellipse(x+xv,y-yy/2+yv,xx,yy);
    
  
  //reset
  if(kP[DOWN]&&y>500){
  y = 410;
  x = xRect ;
  fall = false;
  rectLength = 150;
  score = 0;
  bonus = 0;
  c = 0xff3C492A;
  textLenght10 = 0;
  cRandom = PApplet.parseInt(random(360));
  frameT=60;
  inBonus = false;
  stopA=460;stopB=480;stopC=0;stopD=0;
  e=73;
  strokeT=0;
  save = true;
  shadowT = -1;
  shadow2 = false;
  shadow3 = false;
  bonusLast = 1;
  deadPlay = false;
  muteT = 500;
  }

  
  if(kP[27]){
  output.close(); // Finishes the file
  exit();
  }
}
  
public void keyPressed(){
  kP[keyCode] = true;
}

public void keyReleased(){
  kP[keyCode] = false;
}

public void changeAppIcon(PImage img) {
  final PGraphics pg = createGraphics(16, 16, JAVA2D);

  pg.beginDraw();
  pg.image(img, 0, 0, 16, 16);
  pg.endDraw();

  frame.setIconImage(pg.image);
}

public void changeAppTitle(String title) {
  frame.setTitle(title);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "you_are_a_ball" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
