import beastybuttons.*;

BeastyWorld world;
Label label1, label2;

void setup(){
  size(500, 500);
  world = new BeastyWorld(this);
  
  //this constructor calculates the labelsize
  label1 = new Label(this, "click left\nto change position", 30).setPosition("right", "bottom");
  
  //this constructor calculates the labeltextsize
  label2 = new Label(this, 300, 100, "click right\nto change color").setPosition("left", "top");
  world.addWidget(label1);
  world.addWidget(label2);
}

void draw(){
  
}

void mousePressed(){
  if(mouseButton == LEFT){
    int choice = int(random(0, 4));
    if(choice == 0){
      label1.setPosition("right", "top");
      label2.setPosition("left", "bottom");
    }
    else if(choice == 1){
      label1.setPosition("left", "top");
      label2.setPosition("right", "bottom");
    }
    else if(choice == 2){
      label1.setPosition("right", "bottom");
      label2.setPosition("left", "top");
    }
    else if(choice == 3){
      label1.setPosition("left", "bottom");
      label2.setPosition("right", "top");
    }
  }
  else if(mouseButton == RIGHT){
    label1.setBackgroundColor(color(random(0, 255), random(0, 255), random(0, 255)));
    label2.setBackgroundColor(color(random(0, 255), random(0, 255), random(0, 255)));
  }
}
