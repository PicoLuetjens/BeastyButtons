import beastybuttons.*;

BeastyWorld world;
Label l, l2, l3, l4, l5;
Button change;
Inputfield input_layer, input_label;

void setup(){
  size(500, 500);
  world = new BeastyWorld(this);
  
  l = new Label(this, "I am on Layer 0", 30).setPosition(width/2, height/2-(height/20)*2).setLayer(0).setBackgroundColor(color(15, 255, 1));
  l2 = new Label(this, "I am on Layer 1", 30).setPosition(width/2, height/2-height/20).setLayer(1).setBackgroundColor(color(15*2, 255/2, 4));
  l3 = new Label(this, "I am on Layer 2", 30).setPosition(width/2, height/2).setLayer(2).setBackgroundColor(color(15*3, 255/3, 9));
  l4 = new Label(this, "I am on Layer 3", 30).setPosition(width/2, height/2+height/20).setLayer(3).setBackgroundColor(color(15*4, 255/4, 16));
  l5 = new Label(this, "I am on Layer 4", 30).setPosition(width/2, height/2+(height/20)*2).setLayer(4).setBackgroundColor(color(15*5, 255/5, 25));
  
  world.addWidget(l);
  world.addWidget(l2);
  world.addWidget(l3);
  world.addWidget(l4);
  world.addWidget(l5);
  
  change = new Button(this, "set", 30).setPosition(width/2, "bottom").onLeftClick("change");
  input_layer = new Inputfield(this, 6, 30).setPosition("right", "bottom").setGreyedText("layer");
  world.addWidget(input_layer);
  input_label = new Inputfield(this, 6, 30).setPosition("left", "bottom").setGreyedText("label");
  world.addWidget(input_label);
  world.addWidget(change);
}

void draw(){
  
}

void change(){
  String layer_str = input_layer.getText();
  String label = input_label.getText();
  int layer = 0;
  
  if(layer_str.equals("one")){
    layer = 1;
  }
  else if(layer_str.equals("two")){
    layer = 2;
  }
  else if(layer_str.equals("three")){
    layer = 3;
  }
  else if(layer_str.equals("four")){
    layer = 4;
  }
  else if(layer_str.equals("five")){
    layer = 5;
  }
  else if(layer_str.equals("zero")){
    layer = 0;
  }
  
  if(label.equals("one")){
    l.setLayer(layer);
    l.setText("I am on Layer " + str(layer), false);
  }
  if(label.equals("two")){
    l2.setLayer(layer);
    l2.setText("I am on Layer " + str(layer), false);
  }
  if(label.equals("three")){
    l3.setLayer(layer);
    l3.setText("I am on Layer " + str(layer), false);
  }
  if(label.equals("four")){
    l4.setLayer(layer);
    l4.setText("I am on Layer " + str(layer), false);
  }
  if(label.equals("five")){
    l5.setLayer(layer);
    l5.setText("I am on Layer " + str(layer), false);
  }
  input_layer.clearInput();
  input_label.clearInput();
}
