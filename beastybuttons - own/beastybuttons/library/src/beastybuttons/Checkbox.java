package beastybuttons;

import processing.core.*;

public class Checkbox extends Widget
{
    //position of the center of the Button
    protected float posx = 0, posy = 0;

    //is the widget active or not?
    protected boolean active = true;

    //is the widget visible or not?
    protected boolean visible = true;

    //holding text of the button
    private String text;

    //colors
    private int background, foreground;

    //shape of the widget
    private boolean round = false;

    //size of the widget
    private float sizes[] = new float[2];

    //textsize of the holding text
    private float textSize;

    //colors
    private int overcolor, clickcolor;

    //widget clicke variables
    private boolean clicked = false, click = false;

    //click type(right, left, middle)
    private int type = 99;

    //tooltip
    private boolean tooltip_enabled = false;
    private String tooltiptext = "tooltip";
    private float tooltiptextsize = 1;
    private float[] ttsizes = {0, 0};

    //colors
    private int ttbackground, ttforeground;

    //tooltip position
    private float ttxpos = 0, ttypos = 0;

    //amount of milliseconds after the tooltip disappears
    private float ttintervall = 2000;

    //shape of the tooltip
    private boolean roundtt = false;

    //does the mouse stay on the widget in one position?
    private boolean pos_locked = false;

    //used for the tooltip dissaper time measure
    private long time;

    //auto positioning of the tooltip
    private boolean tt_autopos = true;

    //enable disappear for the tooltip?
    private boolean enable_intervall = false;

    //used for the information on the buttonpress
    private boolean lastpress = false;

    private boolean press = false;

    private boolean mouseReleased = false;

    //counts the clicks on the Widget since runtime
    private int clickssincestart = 0;

    //id of the Widget
    protected String id = "id";

    //the layer on which the widget is rendered
    protected int layer = 0;

    //the amount of instances to generate unique id
    private static int c_instances = 0;

    //the sourcepath of the widget
    protected String sourcePath;

    //executing on Click
    private String olc, omc, orc;

    //is it checked or not?
    private boolean checked = false;

    //ckecker types
    private int checktype = 0;

    //is the widget already added to a surface or the world
    protected boolean already_added = false;

    //PApplet reference
    private final PApplet sketch;



    Checkbox(PApplet p, float size)
    {
        this.sketch = p;
        this.sizes[0] = size;
        this.sizes[1] = size;
        this.generateColors();
        this.generateID();
    }

    Checkbox(PApplet p, float size, boolean round)
    {
        this.sketch = p;
        this.sizes[0] = size;
        this.sizes[1] = size;
        this.round = round;
        this.generateColors();
        this.generateID();
    }

    //**** CONFIGURATION METHODS - CALLED FROM THE CONSTRUCTOR ****

    //calculates the size of the Tooltip
    private void calculatettsize()
    {
        float current_size = this.sketch.g.textSize;
        this.sketch.textSize(this.tooltiptextsize);
        float xsize = 0, ysize = 0;
        for(int i = 0; i < this.tooltiptext.length(); i++)
        {
            xsize += this.sketch.textWidth(this.tooltiptext.charAt(i));
        }
        ysize = this.sketch.textAscent()+this.sketch.textDescent();
        xsize = (float)(xsize*1.15);
        ysize = (float)(ysize*1.15);
        this.ttsizes[0] = xsize;
        this.ttsizes[1] = ysize;
        this.sketch.textSize(current_size);
    }

    //generate the colors after sketch can be referenced
    private void generateColors()
    {
        this.foreground = this.sketch.color(100, 100, 100);
        this.background = this.sketch.color(0, 0, 0);
        this.overcolor = this.sketch.color(250, 0, 0);
        this.clickcolor = this.sketch.color(60, 0, 0);
        this.ttbackground = this.sketch.color(255, 255, 255);
        this.ttforeground = this.sketch.color(0, 0, 0);
    }

    private void generateID()
    {
        String convert = String.valueOf(Checkbox.c_instances);
        this.id = "checkbox" + convert;
        Checkbox.c_instances+=1;
        this.sourcePath = this.id;
    }


    //**** SET AND GET METHODS****

    //set the Foreground Color
    public void setForegroundColor(int fg)
    {
        if(!BeastyWorld.running)
        {
            this.foreground = fg;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set the Background Color
    public void setBackgroundColor(int bg)
    {
        if(!BeastyWorld.running)
        {
            this.background = bg;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //creates a tooltip
    //if you create a second one the old one gets overwritten, you can only have one tooltip
    public void createTooltip(String text, float size, int fg, int bg)
    {
        if (!BeastyWorld.running)
        {
            this.tooltiptext = text;
            this.tooltiptextsize = size;
            this.ttforeground = fg;
            this.ttbackground = bg;
            this.calculatettsize();
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void createTooltip(String text, float size)
    {
        if(!BeastyWorld.running)
        {
            this.tooltiptext = text;
            this.tooltiptextsize = size;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //configure the tooltip further
    public void configureTooltip(boolean roundtt)
    {
        if(!BeastyWorld.running)
        {
            this.roundtt = roundtt;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void configureTooltip(boolean roundtt, float intervall)
    {
        if(!BeastyWorld.running)
        {
            this.roundtt = roundtt;
            this.ttintervall = intervall;
            this.enable_intervall = true;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //position the tooltip
    public void setTooltipPosition(float x, float y)
    {
        if(!BeastyWorld.running)
        {
            this.tt_autopos = false;
            this.ttxpos = x;
            this.ttypos = y;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void setTooltipPosition(char c)
    {
        if(c == 'A')
        {
            if(!BeastyWorld.running)
            {
                this.tt_autopos = true;
            }
            else
            {
                PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
            }
        }
        else
        {
            //throw error
            throw new CostumRuntimeError("Not a valid input for setTooltipPosition()");
        }
    }

    //enabling or disabling the tooltip for display, it still exists when created before
    public void enableTooltip(boolean en)
    {
        if(!BeastyWorld.running)
        {
            this.tooltip_enabled = en;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set the Position of the Widget
    public void setPosition(float x, float y)
    {
        if(!BeastyWorld.running)
        {
            this.posx = x;
            this.posy = y;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void setPosition(char a, char b)
    {
        if(!BeastyWorld.running) {
            if (a == 'L') {
                this.posx = this.sizes[0] / 2;
            } else if (a == 'R') {
                this.posx = this.sketch.width - this.sizes[0] / 2;
            } else {
                //throw error
                throw new CostumRuntimeError("Not a valid input for setPosition()");
            }
            if (b == 'T') {
                this.posy = this.sizes[1] / 2;
            } else if (b == 'B') {
                this.posy = this.sketch.height - this.sizes[1] / 2;
            } else {
                //throw error
                throw new CostumRuntimeError("Not a valid input for setPosition()");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void setPosition(char a, float y)
    {
        if(!BeastyWorld.running) {
            if (a == 'L') {
                this.posx = this.sizes[0] / 2;
            } else if (a == 'R') {
                this.posx = this.sketch.width - this.sizes[0] / 2;
            } else {
                //throw error
                throw new CostumRuntimeError("Not a valid input for setPosition()");
            }
            this.posy = y;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void setPosition(float x, char b)
    {
        if(!BeastyWorld.running) {
            if (b == 'T') {
                this.posy = this.sizes[1] / 2;
            } else if (b == 'B') {
                this.posy = this.sketch.height - this.sizes[1] / 2;
            } else {
                //throw error
                throw new CostumRuntimeError("Not a valid input for setPosition()");
            }
            this.posx = x;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public float[] getPosition()
    {
        if(!BeastyWorld.running) {
            //pass
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
        float[] temp = {this.posx, this.posy};
        return temp;
    }

    //set widget unvisible or visible
    public void hide(boolean vis)
    {
        this.visible = vis;
    }

    //set the widget active(listens to clicks, is clickable..)
    public void setActive(boolean act)
    {
        this.active = act;
    }

    //set the color when the mouse hovers over
    public void setOvercolor(int c)
    {
        if(!BeastyWorld.running)
        {
            this.overcolor = c;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set the color when the button is pressed and hold down
    public void setClickcolor(int c)
    {
        if(!BeastyWorld.running)
        {
            this.clickcolor = c;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set the Layer
    public void setLayer(int layer)
    {
        if(!BeastyWorld.running)
        {
            this.layer = layer;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //override the id
    public void setID(String id)
    {
        if(!BeastyWorld.running)
        {
            this.id = id;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    protected void editSourcepath(String action, String id)
    {
        String[] splits = this.sourcePath.split("/");
        if(action.equals("delete"))
        {
            this.sourcePath = splits[1];
        }
        else if(action.equals("add"))
        {
            //this.sourcePath = id + "/" + this.sourcePath;
            this.sourcePath = id.concat("/"+this.sourcePath);
        }
        else
        {
            PApplet.println("WARNING(BB): SOURCEPATH ERROR - WIDGET MIGHT BE LINKED WRONG");
        }
    }

    //copy all Settings of another Button
    public void copySettings(Checkbox c)
    {
        if(!BeastyWorld.running)
        {
            //go through every parameter and copy the actual Parameters(not by reference)
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set the function thats executed on left click
    public void setonLeftCheck(String method)
    {
        if(!BeastyWorld.running)
        {
            this.olc = method;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set the function thats executed on middle click
    public void setonMiddleCheck(String method)
    {
        if(!BeastyWorld.running)
        {
            this.omc = method;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set the function thats executed on right click
    public void setonRightCheck(String method)
    {
        if(!BeastyWorld.running)
        {
            this.orc = method;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //sets the Checkbox checked or not checked(can be used externally, but is also used internally to set state when clicked)
    public void setState(boolean state)
    {
        if(!BeastyWorld.running)
        {
            this.checked = state;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //sets the type of checker: 0 -> check, 1 -> cross, 2 -> point, 3 -> circle
    public void setChecktype(int type)
    {
        if(type >= 0 && type <= 3)
        {
            this.checktype = type;
        }
        else
        {
            throw new CostumRuntimeError("Unknown Checkertype for Checkbox!");
        }
    }

    //returns the current check state
    public boolean getState()
    {
        if(this.active)
        {
            if(this.checked)
            {
                return true;
            }
        }
        return false;
    }


    //****  INTERNAL CALLED METHODS  ****

    //gets executed on Left Check once its checked
    protected void onLeftClick()
    {
        if(this.olc != null)
        {
            this.sketch.method(olc);
        }
    }

    //gets executed on Middle Check once its checked
    protected void onMiddleClick()
    {
        if(this.omc != null)
        {
            this.sketch.method(omc);
        }
    }

    //gets executed on Right Check once its checked
    protected void onRightClick()
    {
        if(this.orc != null)
        {
            this.sketch.method(orc);
        }
    }

    //takes coordinates and calculates if the cursor is over the element
    protected boolean over(float mx, float my)
    {
        if(mx >= this.posx-this.sizes[1]/2 && mx <= this.posx+this.sizes[1]/2)
        {
            if(my >= this.posy-this.sizes[1]/2 && my <= this.posy+this.sizes[1]/2)
            {
                return true;
            }
        }
        return false;
    }

    //render queue for the tooltip
    protected void rendertooltip()
    {
        //render with rectMode(CENTER)!!!
    }

    //render queue for the Widget
    protected void render()
    {
        //render with rectMode(CENTER)!!!
    }
}