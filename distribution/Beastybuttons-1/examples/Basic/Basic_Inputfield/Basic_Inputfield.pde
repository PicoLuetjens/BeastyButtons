//NOTE: Inputfield also supports multiline however this input here is meant to be single line so there is no update from the inputfield when a new line is created.

import beastybuttons.*;

BeastyWorld world;
Label l;
Inputfield i;
Button b;
String question;

void setup(){
  size(500, 500);
  world = new BeastyWorld(this);
  question = "Guess the Answer:\nThe one who does it does not say it.\nThe one who says it does not do it\nThe one who takes it does not know it\nThe one who knows it does not take it";
  l = new Label(this, question, 20).setPosition(width/2, height/4);
  
  b = new Button(this, "Submit", 20).setPosition(width/2, (height/8)*7).onLeftClick("submit");
  
  //This constructor calculates the size based of the longest input with textsize 10
  i = new Inputfield(this, 17, 20).setPosition(width/2, (height/4)*3).setGreyedText("Click to write input");
  world.addWidget(i);
  world.addWidget(l);
  world.addWidget(b);
}

void draw(){
  
}

void submit(){
  if(i.getText().equals("counterfeit money") || i.getText().equals("Counterfeit Money") || i.getText().equals("Counterfeit money")){
    
    //this updates the size of the Label to fit with the new text
    l.setText("Congrats,\nright answer!", true);
  }
}
