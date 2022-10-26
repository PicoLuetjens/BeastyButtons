import beastybuttons.*;

BeastyWorld world;
Button change1, change2, change3, change4;
PFont font_ink, font_notosans, font_segoe, font_segoescript;

void setup(){
  size(700, 700);
  font_ink = loadFont("InkFree-48.vlw");
  font_notosans = loadFont("NotoSans-CondensedItalic-48.vlw");
  font_segoe = loadFont("SegoePrint-48.vlw");
  font_segoescript = loadFont("SegoeScript-Bold-48.vlw");
  world = new BeastyWorld(this);
  change1 = new Button(this, "change font to InkFree", 20).setPosition(width/4, height/4).onLeftClick("change1");
  change2 = new Button(this, "change font to NotoSans", 20).setPosition((width/4)*3, height/4).onLeftClick("change2");
  change3 = new Button(this, "change font to SegoePrint", 20).setPosition(width/4, (height/4)*3).onLeftClick("change3");
  change4 = new Button(this, "change font to SegoeScript", 20).setPosition((width/4)*3, (height/4)*3).onLeftClick("change4");
  world.addWidget(change1);
  world.addWidget(change2);
  world.addWidget(change3);
  world.addWidget(change4);
}

void draw(){
  
}

void change1(){
  textFont(font_ink);
  change1.setText(change1.getText(), true);
  change2.setText(change2.getText(), true);
  change3.setText(change3.getText(), true);
  change4.setText(change4.getText(), true);
}

void change2(){
  textFont(font_notosans);
  change1.setText(change1.getText(), true);
  change2.setText(change2.getText(), true);
  change3.setText(change3.getText(), true);
  change4.setText(change4.getText(), true);
}

void change3(){
  textFont(font_segoe);
  change1.setText(change1.getText(), true);
  change2.setText(change2.getText(), true);
  change3.setText(change3.getText(), true);
  change4.setText(change4.getText(), true);
}

void change4(){
  textFont(font_segoescript);
  change1.setText(change1.getText(), true);
  change2.setText(change2.getText(), true);
  change3.setText(change3.getText(), true);
  change4.setText(change4.getText(), true);
}
