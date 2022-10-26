import beastybuttons.*;

BeastyWorld world;
BeastySurface surface1, surface2, surface3;

void setup(){
    size(1280, 720);
    world = new BeastyWorld(this);
    world.registerHandler("hotkeys");
    surface1 = new BeastySurface(this);
    surface2 = new BeastySurface(this);
    surface3 = new BeastySurface(this);
    surface1.addWidget(new Label(this, "This is Surface 1", 30)
    .setPosition("left", "top"));
    surface2.addWidget(new Label(this, "This is Surface 2", 30)
    .setPosition("left", "top"));
    surface3.addWidget(new Label(this, "This is Surface 3", 30)
    .setPosition("left", "top"));
    surface1.addWidget(new Button(this, "Switch to Surface 2", 30)
    .setPosition(width/4, height/2).onLeftClick("switchto2"));
    surface1.addWidget(new Button(this, "Switch to Surface 3", 30)
    .setPosition((width/4)*3, height/2).onLeftClick("switchto3"));
    surface2.addWidget(new Button(this, "Switch to Surface 1", 30)
    .setPosition(width/4, height/2).onLeftClick("switchto1"));
    surface2.addWidget(new Button(this, "Switch to Surface 3", 30)
    .setPosition((width/4)*3, height/2).onLeftClick("switchto3"));
    surface3.addWidget(new Button(this, "Switch to Surface 1", 30)
    .setPosition(width/4, height/2).onLeftClick("switchto1"));
    surface3.addWidget(new Button(this, "Switch to Surface 2", 30)
    .setPosition((width/4)*3, height/2).onLeftClick("switchto2"));
    surface1.setSurfaceColor(color(255, 100, 100));
    surface2.setSurfaceColor(color(150, 50, 50));
    surface3.setSurfaceColor(color(0, 100, 200));
    
    world.addSurface(surface1);
    world.addSurface(surface2);
    world.addSurface(surface3);
    world.surface_jump_transition("BeastySurface0");
}

void draw(){
  
}

void switchto1(){
  world.surface_jump_transition("BeastySurface0");
}

void switchto2(){
  world.surface_jump_transition("BeastySurface1");
}

void switchto3(){
  world.surface_jump_transition("BeastySurface2");
}
