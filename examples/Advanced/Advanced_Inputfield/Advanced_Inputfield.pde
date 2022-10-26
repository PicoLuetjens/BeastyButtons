import beastybuttons.*;

BeastyWorld world;
Inputfield input;
Inputfield input2;

void setup(){
  size(700, 700);
  world = new BeastyWorld(this);
  input = new Inputfield(this, 10, 30).setPosition(399, 399).setHotkey(ALT)
  .createTooltip("Tooltip", 10).configureTooltip(false, 3000).setTooltipPosition(400, 300);
  input2 = new Inputfield(this, 10, 300, 100).setPosition(199, 199).setHotkey(CONTROL);
  world.addWidget(input);
  world.addWidget(input2);
  input.setInputLimit(50);
  input2.setInputLimit(50);
  world.registerHandler("hotkeys");
}

void draw(){
  input.setSize(input.getSize()[0], input.getSize()[1], true);
  input2.setTextsize(input2.getTextSize(), true);
}
