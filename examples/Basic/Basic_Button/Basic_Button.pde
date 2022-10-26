import beastybuttons.*;

BeastyWorld world;
String button = "nothing";

void setup(){
  size(500, 500);
  world = new BeastyWorld(this);
  world.addWidget(new Button(this, "Click me", 40)
  .setPosition(width/2, height/3)
  .onLeftClick("left").onMiddleClick("middle").onRightClick("right"));
  world.disableBackground(true);
}

void draw(){
  background(100);
  fill(0);
  textAlign(CENTER);
  text(button + " was clicked", width/2, (height/3)*2);
}

void left(){
  button = "left";
}

void middle(){
  button = "middle";
}

void right(){
  button = "right";
}
