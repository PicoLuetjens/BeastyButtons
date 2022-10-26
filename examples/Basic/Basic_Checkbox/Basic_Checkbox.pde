import beastybuttons.*;

BeastyWorld world;
color color1 = color(200, 200, 50);
color color2 = color(200, 30, 100);

void setup(){
  size(500, 500);
  world = new BeastyWorld(this);
  world.addWidget(new Button(this, "Click me", 40)
  .setPosition(width/2, height/3)
  .onLeftClick("left"));
  world.addWidget(new Checkbox(this, 30, false).setPosition(width/2, height/2));
  world.setBackgroundColor(color1);
}

void draw(){
  
}

void left(){
  //the id is automatically generated when not overriden
  Checkbox b = (Checkbox)world.get_widget_by_id("Checkbox0");
  if(b.getState()){
    world.setBackgroundColor(color2);
  }
  else{
    world.setBackgroundColor(color1);
  }
}
