import beastybuttons.*;

BeastyWorld world;
Button b;
Inputfield input1, input2;
Checkbox loggingbox;
Label logginglabel;
boolean dark_mode = false;

void setup() {
  size(500, 500);
  world = new BeastyWorld(this);
  b = new Button(this, "Dark Background", 20, true).setPosition("left", "top")
  .setHotkey('Q').onLeftClick("changebackground");
  loggingbox = new Checkbox(this, 30, true, false).setPosition(width/4, height/2)
    .setChecktype("point").setHotkey('Y').onCheck("enableLogging").onUncheck("disableLogging");
  logginglabel = new Label(this, "logging", 30, true).setPosition(width/2, height/2);
  input1 = new Inputfield(this, 10, 20, true).setPosition("left", "bottom").setGreyedText("loginput")
    .setHotkey(CONTROL);
  input2 = new Inputfield(this, 10, 20, true).setPosition("right", "bottom").setGreyedText("loginput")
    .setHotkey(ALT);
  world.registerHandler("hotkeys");
  world.addWidget(b);
  world.addWidget(loggingbox);
  world.addWidget(logginglabel);
  world.addWidget(input1);
  world.addWidget(input2);
}

void draw() {
  
}

void changebackground(){
  if(!dark_mode){
    world.setBackgroundColor(color(10, 10, 10));
  }
  else{
    world.setBackgroundColor(color(200, 200, 200));
  }
  dark_mode = !dark_mode;
}

void enableLogging(){
  world.enableLogging(1, "data/log.txt");
}

void disableLogging(){
  world.disableLogging();
}
