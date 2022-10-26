import beastybuttons.*;

BeastyWorld world;
Label l, l2, l3, l4, l5, l6;
Button change_to_red, change_to_blue;
color red_layout = color(255, 0, 10), blue_layout = color(10, 0, 255);

void setup(){
  size(500, 500);
  world = new BeastyWorld(this);
  l = new Label(this, "Label1", 30).setPosition(width/4, (height/5)*2)
  .setBackgroundColor(red_layout);
  l2 = new Label(this, "Label2", 30).setPosition(width/4, (height/5)*3)
  .setBackgroundColor(red_layout);
  l3 = new Label(this, "Label3", 30).setPosition(width/4, (height/5)*4)
  .setBackgroundColor(red_layout);
  l4 = new Label(this, "Label4", 30).setPosition((width/4)*3, (height/5)*2)
  .setBackgroundColor(blue_layout);
  l5 = new Label(this, "Label5", 30).setPosition((width/4)*3, (height/5)*3)
  .setBackgroundColor(blue_layout);
  l6 = new Label(this, "Label6", 30).setPosition((width/4)*3, (height/5)*4)
  .setBackgroundColor(blue_layout);
  world.addWidget(l);
  world.addWidget(l2);
  world.addWidget(l3);
  world.addWidget(l4);
  world.addWidget(l5);
  world.addWidget(l6);
  change_to_blue = new Button(this, "change to blue Layout", 20).setPosition(width/4, height/8)
  .onLeftClick("changetoblue").setOvercolor(color(50, 200, 100));
   change_to_red = new Button(this, "change to red Layout", 20).setPosition((width/4)*3, height/8)
  .onLeftClick("changetored").setOvercolor(color(50, 200, 100));
  world.setBackgroundColor(color(10, 100, 100));
  world.addWidget(change_to_blue);
  world.addWidget(change_to_red);
  l.hide(true);
  l2.hide(true);
  l3.hide(true);
  change_to_blue.setActive(false);
  change_to_blue.hide(true);
}

void draw(){
  
}

void changetoblue(){
  l.hide(true);
  l2.hide(true);
  l3.hide(true);
  l4.hide(false);
  l5.hide(false);
  l6.hide(false);
  change_to_red.setActive(true);
  change_to_red.hide(false);
  change_to_blue.setActive(false);
  change_to_blue.hide(true);
}

void changetored(){
  l.hide(false);
  l2.hide(false);
  l3.hide(false);
  l4.hide(true);
  l5.hide(true);
  l6.hide(true);
  change_to_red.setActive(false);
  change_to_red.hide(true);
  change_to_blue.setActive(true);
  change_to_blue.hide(false);
}
