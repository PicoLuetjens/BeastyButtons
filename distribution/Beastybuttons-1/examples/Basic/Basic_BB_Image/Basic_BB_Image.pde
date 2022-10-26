import beastybuttons.*;

BeastyWorld world;
BB_Image img;

void setup(){
  size(500, 500);
  world = new BeastyWorld(this);
  
  //IMPORTANT: Set the Size before the Position
  img = new BB_Image(this, "data/tree.jpg").setScaleFactor(0.7, 0.6)
  .setPosition(width/2, height/2);
  world.addWidget(img);
}

void draw(){
  
}

void mousePressed(){
  if(mouseButton == LEFT){
    img.cropImage("left", 20);
  }
  else if(mouseButton == RIGHT){
    img.cropImage("bottom", 10);
  }
}
