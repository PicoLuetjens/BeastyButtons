import beastybuttons.*;

BeastyWorld world;
BeastySurface surface1, surface2, surface3;
Button button1, button2, button3;
Checkbox checkbox1, checkbox2, checkbox3;
Inputfield inputfield1, inputfield2, inputfield3;
Label label1, label2, label3;
BB_Image img1, img2, img3;

void setup(){
  size(500, 500);
  GUIsetup();
}

void draw(){
  
}

void GUIsetup(){
  world = new BeastyWorld(this);
  world.registerHandler("hotkeys");
  world.registerHandler("tabswitch");
  surface1 = new BeastySurface(this);
  surface2 = new BeastySurface(this);
  surface3 = new BeastySurface(this);
  button1 = new Button(this, 100, 30, "Button1").setPosition(width/2, height/2).setHotkey(SHIFT);
  button2 = new Button(this, 100, 30, "Button2").setPosition(width/2, height/2);
  button3 = new Button(this, 100, 30, "Button3").setPosition(width/2, height/2).setHotkey(CONTROL);
  surface1.addWidget(button1);
  surface2.addWidget(button2);
  surface3.addWidget(button3);
  checkbox1 = new Checkbox(this, 30, false).setPosition("right", "bottom");
  checkbox2 = new Checkbox(this, 30, false).setPosition("right", "bottom");
  checkbox3 = new Checkbox(this, 30, true).setPosition("right", "bottom");
  surface1.addWidget(checkbox1);
  surface2.addWidget(checkbox2);
  surface3.addWidget(checkbox3);
  inputfield1 = new Inputfield(this, 10, 300, 100).setPosition("left", "bottom");
  inputfield2 = new Inputfield(this, 10, 300, 100).setPosition("left", "bottom");
  inputfield3 = new Inputfield(this, 10, 300, 100).setPosition("left", "bottom");
  surface1.addWidget(inputfield1);
  surface2.addWidget(inputfield2);
  surface3.addWidget(inputfield3);
  label1 = new Label(this, 300, 100, "label1").setPosition("left", "top");
  label2 = new Label(this, 300, 100, "label2").setPosition("left", "top");
  label3 = new Label(this, 300, 100, "label3").setPosition("left", "top");
  surface1.addWidget(label1);
  surface2.addWidget(label2);
  surface3.addWidget(label3);
  img1 = new BB_Image(this, "Java-Logo.jpg").setPosition("right", "top").setScaleFactor(0.1, 0.1).cropImage("top", 20).cropImage("right", 10);
  img2 = new BB_Image(this, "JavaScript-Logo.jpg").setPosition("right", "top").setScaleFactor(0.3, 0.3);
  img3 = new BB_Image(this, "Python-Logo.jpg").setScaleFactor(0.1, 0.1).setPosition("right", "top").cropImage("top", 20).cropImage("right", 500);
  surface1.addWidget(img1);
  surface2.addWidget(img2);
  surface3.addWidget(img3);
  world.addSurface(surface1);
  world.addSurface(surface2);
  world.addSurface(surface3);
  world.exportLayout("export.beasty");
}
