package beastybuttons;

import processing.core.*;

public class Button_asot extends Widget
{
    //Class Constant
    protected String CLS = "BUTTON_ASOT";

    //position of the center of the Button
    protected float posx = 0, posy = 0;

    //is the widget active or not?
    protected boolean active = true;

    //is the widget visible or not?
    protected boolean visible = true;

    //holding text of the button
    protected String text;

    //colors
    protected int background, foreground;

    //shape of the widget
    protected boolean round = false;

    //size of the widget
    protected float sizes[] = new float[2];

    //textsize of the holding text
    protected float textSize;

    //colors
    protected int overcolor, clickcolor;

    //this is the background of the button -> either assigned to backgroundcolor, overcolor or clickcolor
    protected int rendercolor;

    //tooltip
    protected boolean tooltip_enabled = false;
    protected String tooltiptext = "tooltip";
    protected float tooltiptextsize = 1;
    protected float[] ttsizes = {0, 0};

    //colors
    protected int ttbackground, ttforeground;

    //tooltip position
    protected float ttxpos = 0, ttypos = 0;

    //amount of milliseconds after the tooltip disappears
    protected float ttintervall = 2000;

    //shape of the tooltip
    protected boolean roundtt = false;

    //used for the tooltip dissaper time measure
    private long time;

    //auto positioning of the tooltip
    protected boolean tt_autopos = true;

    //enable disappear for the tooltip?
    protected boolean enable_intervall = false;

    //counts the clicks on the Widget since runtime
    private int clickssincestart = 0;

    //id of the Widget
    protected String id = "id";

    //the layer on which the widget is rendered
    protected int layer = 0;

    //the amount of instances to generate unique id
    private static int b_instances = 0;

    //the sourcepath of the widget
    protected String sourcePath;

    //executing on Click(on left click, on middle click, on right click, on mouse release)
    protected String olc, omc, orc;

    //on hotkey
    protected String ohk;

    //on tab enter
    protected String ote;

    //should the method be executed in a seperate thread or no?
    protected boolean olc_thread, omc_thread, orc_thread;

    //on hotkey thread
    protected boolean ohk_thread;

    //on tab enter thread
    protected boolean ote_thread;

    //is the widget already added to a surface or the world
    protected boolean already_added = false;

    //last loop over() check -> compared at loop begin to ensure a true click(clicked while over and released while over)
    private boolean lastcheck = false;

    //hotkey combination
    protected int hotkey;

    //tabswitch selected
    protected boolean tab_selected = false;

    //PApplet reference
    private final PApplet sketch;

    public Button_asot(PApplet p, String text, float size)
    {
        if(!BeastyWorld.running)
        {
            this.sketch = p;
            this.text = text;
            this.textSize = size;
            this.calculateSize();
            this.generateColors();
            this.generateID();
            this.rendercolor = this.background;
        }
        else
        {
            throw new CostumRuntimeError("Widgets cannot be instanced while runtime, try to instance before draw()");
        }
    }

    public Button_asot(PApplet p, String text, float size, boolean round)
    {
        if(!BeastyWorld.running)
        {
            this.sketch = p;
            this.text = text;
            this.textSize = size;
            this.round = round;
            this.calculateSize();
            this.generateColors();
            this.generateID();
            this.rendercolor = this.background;
        }
        else
        {
            throw new CostumRuntimeError("Widgets cannot be instanced while runtime, try to instance before draw()");
        }
    }

    //import Contructor - called from the BeastyWorld Class when it is importing/creating a Button_asot Widget
    protected Button_asot(PApplet p)
    {
        this.sketch = p;
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

    //calculates the size of the Button
    private void calculateSize()
    {
        float current_size = this.sketch.g.textSize;
        this.sketch.textSize(this.textSize);
        float xsize = 0, ysize = 0;
        for(int i = 0; i < this.text.length(); i++)
        {
            xsize += this.sketch.textWidth(this.text.charAt(i));
        }
        ysize = this.sketch.textAscent()+sketch.textDescent();
        xsize = (float)(xsize*1.15);
        ysize = (float)(ysize*1.15);
        this.sizes[0] = xsize;
        this.sizes[1] = ysize;
        this.sketch.textSize(current_size);
    }

    //generate the colors after sketch can be referenced
    private void generateColors()
    {
        this.foreground = this.sketch.color(0, 0, 0);
        this.background = this.sketch.color(100, 100, 100);
        this.overcolor = this.sketch.color(250, 0, 0);
        this.clickcolor = this.sketch.color(150, 0, 150);
        this.ttbackground = this.sketch.color(100, 100, 100);
        this.ttforeground = this.sketch.color(0, 0, 0);
    }

    private void generateID()
    {
        String convert = String.valueOf(Button_asot.b_instances);
        this.id = "button_asot" + convert;
        Button_asot.b_instances+=1;
        this.sourcePath = this.id;
    }



    //**** SET AND GET METHODS****

    //set the Foreground Color
    public void setForegroundColor(int fg)
    {
        this.foreground = fg;
    }

    //set the Background Color
    public void setBackgroundColor(int bg)
    {
        this.background = bg;
        this.rendercolor = this.background;
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
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME");
        }
    }

    public void createTooltip(String text, float size)
    {
        if(!BeastyWorld.running)
        {
            this.tooltiptext = text;
            this.tooltiptextsize = size;
            this.calculatettsize();
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME");
        }
    }

    //configure the tooltip further
    public void configureTooltip(boolean roundtt)
    {
        if(!BeastyWorld.running)
        {
            this.roundtt = roundtt;
            this.enable_intervall = false;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME");
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
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME");
        }
    }

    //position the tooltip
    public void setTooltipPosition(float x, float y)
    {
        this.tt_autopos = false;
        this.ttxpos = x;
        this.ttypos = y;
    }

    public void setTooltipPosition(String pos)
    {
        if(pos.equals("auto"))
        {
            this.tt_autopos = true;
            this.calc_tt_auto_pos();
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
        this.tooltip_enabled = en;
    }


    //sets the Buttonsize - this may reposition the text inside the widget(does not update position and textsize)
    public void setButtonsize(float x, float y)
    {
        this.sizes[0] = x;
        this.sizes[1] = y;
    }

    //get the Button size - may return the wrong size unsafe to use
    public float[] getButtonsize()
    {
        return this.sizes;
    }

    //set the Textsize
    public void setTextsize(float s)
    {
       this.textSize = s;
    }

    //get the Textsize
    public float getTextsize()
    {
        return this.textSize;
    }

    //set the Position of the Widget(during runtime only float, float input is accepted, because window may be resized
    public void setPosition(float x, float y)
    {
        this.posx = x;
        this.posy = y;
        if(this.tt_autopos)
        {
            this.calc_tt_auto_pos();
        }
    }

    public void setPosition(String a, String b)
    {
        if (a.equals("left")) {
            this.posx = this.sizes[0] / 2;
        } else if (a.equals("right")) {
            this.posx = this.sketch.width - this.sizes[0] / 2;
        } else {
            //throw error
            throw new CostumRuntimeError("Not a valid input for setPosition()");
        }
        if (b.equals("top")) {
            this.posy = this.sizes[1] / 2;
        } else if (b.equals("bottom")) {
            this.posy = this.sketch.height - this.sizes[1] / 2;
        } else {
            //throw error
            throw new CostumRuntimeError("Not a valid input for setPosition()");
        }
        if(this.tt_autopos)
        {
            this.calc_tt_auto_pos();
        }
    }

    public void setPosition(String a, float y)
    {
        if (a.equals("left")) {
            this.posx = this.sizes[0] / 2;
        } else if (a.equals("right")) {
            this.posx = this.sketch.width - this.sizes[0] / 2;
        } else {
            //throw error
            throw new CostumRuntimeError("Not a valid input for setPosition()");
        }
        this.posy = y;
        if(this.tt_autopos)
        {
            this.calc_tt_auto_pos();
        }
    }

    public void setPosition(float x, String b)
    {
        if (b .equals("top")) {
            this.posy = this.sizes[1] / 2;
        } else if (b.equals("bottom")) {
            this.posy = this.sketch.height - this.sizes[1] / 2;
        } else {
            //throw error
            throw new CostumRuntimeError("Not a valid input for setPosition()");
        }
        this.posx = x;
        if(this.tt_autopos)
        {
            this.calc_tt_auto_pos();
        }
    }

    //position is always from Center!!!
    public float[] getPosition()
    {
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
        this.overcolor = c;
    }

    //set the color when the button is pressed and hold down
    public void setClickcolor(int c)
    {
        this.clickcolor = c;
    }

    //set the hotkey key
    public void setHotkey(int key)
    {
        if(!BeastyWorld.running)
        {
            if(key != PConstants.TAB)
            {
                this.hotkey = key;
            }
            else
            {
                throw new CostumRuntimeError("TAB is already registered by BeastyButtons");
            }
        }
    }

    //set the Layer
    public void setLayer(int layer)
    {
        this.layer = layer;
    }

    //override the id
    public void setID(String id)
    {
        if(!BeastyWorld.running)
        {
            this.id = id;
            this.sourcePath.replace(this.id, id);
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME");
        }
    }

    //edits the sourcepath of this instance(called from top level class -> Beastyworld instance)
    // for delete just pass null as second argument
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

    //copy all Settings of another Button except the linked on click methods and the text and the position and size of button
    //Tooltip will be copied. You can either use disableTooltip() afterwords or just call createTooltip() again to change.
    //IF THE SOURCE WAS ALREADY ADDED TO A SURFACE OR WORLD, THIS WILL NOT APPLY TO THE COPIED ONE. HOWEVER IT WILL COPY THE LAYER.
    public void copySettings(Button_asot b)
    {
        if(!BeastyWorld.running)
        {
            PApplet.println("WARNING(BB): copySettings() IS STILL HIGHLY EXPERIMENTAL, AVOID USE WHEN POSSIBLE");
            //go through every parameter and copy the actual Parameters(not by reference)
            //no constructor settings will be influenced
            this.setPosition(b.posx, b.posy);
            //this.posx = b.posx;
            //this.posy = b.posy;
            this.setActive(b.active);
            //this.active = b.active;
            this.hide(b.visible);
            //this.visible = b.visible;
            //this.text = b.text;
            this.setBackgroundColor(b.background);
            this.setForegroundColor(b.foreground);
            //this.foreground = b.foreground;
            //this.background = b.background;
            //this.round = b.round;
            //this.sizes = b.sizes;
            //this.textSize = b.textSize;
            if(b.tooltip_enabled)
            {
                this.createTooltip(b.tooltiptext, b.tooltiptextsize, b.ttforeground, b.ttbackground);
                if(b.enable_intervall)
                {
                    this.configureTooltip(b.roundtt, b.ttintervall);
                }
                else
                {
                    this.configureTooltip(b.roundtt);
                }
                if(b.tt_autopos)
                {
                    this.setTooltipPosition("auto");
                }
                else
                {
                    this.setTooltipPosition(b.ttxpos, b.ttypos);
                }
            }
            this.setOvercolor(b.overcolor);
            this.setClickcolor(b.clickcolor);
            //this.overcolor = b.overcolor;
            //this.clickcolor = b.clickcolor;
            this.rendercolor = b.rendercolor;
            //this.tooltip_enabled = b.tooltip_enabled;
            //this.tooltiptext = b.tooltiptext;
            //this.tooltiptextsize = b.tooltiptextsize;
            //this.ttsizes = b.ttsizes;
            //this.ttbackground = b.ttbackground;
            //this.ttforeground = b.ttforeground;
            //this.ttxpos = b.ttxpos;
            //this.ttypos = b.ttypos;
            //this.ttintervall = b.ttintervall;
            //this.roundtt = b.roundtt;
            //this.time = b.time;
            //this.tt_autopos = b.tt_autopos;
            //this.enable_intervall = b.enable_intervall;
            //this.clickssincestart = b.clickssincestart;
            //this.id = b.id;
            this.layer = b.layer;
            //this.sourcePath = b.sourcePath;

            this.rendercolor = this.background;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME");
        }
    }

    //set the function thats executed on left click
    public void onLeftClick(String method, boolean thr)
    {
        if(!BeastyWorld.running)
        {
            this.olc = method;
            this.olc_thread = thr;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set the function thats executed on middle click
    public void onMiddleClick(String method, boolean thr)
    {
        if(!BeastyWorld.running)
        {
            this.omc = method;
            this.omc_thread = thr;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set the function thats executed on right click
    public void onRightClick(String method, boolean thr)
    {
        if(!BeastyWorld.running)
        {
            this.orc = method;
            this.orc_thread = thr;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set execution method on Hotkey press
    public void onHotkey(String method, boolean thr)
    {
        if(!BeastyWorld.running)
        {
            this.ohk = method;
            this.ohk_thread = thr;
        }
    }

    //set execution method on Enter -> only works with the tabswitch handler
    public void onTabEnter(String method, boolean thr)
    {
        if(!BeastyWorld.running)
        {
            this.ote = method;
            this.ote_thread = thr;
        }
    }


    //****  INTERNAL CALLED METHODS  ****


    //calculates the tooltipposition, only called when autopos is enabled
    private void calc_tt_auto_pos()
    {
        if(this.posx <= this.sketch.width/2f)
        {
            this.ttxpos = this.posx + this.sizes[0]-(this.sizes[0]/10);
        }
        else if(this.posx > this.sketch.width/2f)
        {
            this.ttxpos = (this.posx+(this.sizes[0]/10))-this.ttsizes[0];
        }
        if(this.posy <= this.sketch.height/2f)
        {
            this.ttypos = (this.posy+this.sizes[1])-(this.sizes[1]/10);
        }
        else if(this.posy > this.sketch.height/2f)
        {
            this.ttypos = this.posy-this.sizes[1]/10;
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
        if(!BeastyWorld.isanimating)
        {
            if(this.active)
            {
                //only show tooltip when mouse handler is enabled
                if(BeastyWorld.handlers[0]) {
                    //render with rectMode(CENTER)!!!
                    this.sketch.rectMode(PConstants.CENTER);

                    //enabled?
                    if (this.tooltip_enabled) {

                        this.sketch.noStroke();
                        this.sketch.fill(this.ttbackground);
                        //round or not?
                        if (this.roundtt) {
                            this.sketch.rect(this.ttxpos, this.ttypos, this.ttsizes[0], this.ttsizes[1], 10);
                        } else {
                            this.sketch.rect(this.ttxpos, this.ttypos, this.ttsizes[0], this.ttsizes[1]);
                        }
                        this.sketch.fill(this.ttforeground);
                        this.sketch.textAlign(PConstants.CENTER);
                        this.sketch.textSize(this.tooltiptextsize);
                        this.sketch.text(this.tooltiptext, this.ttxpos, this.ttypos);

                    }
                    //set rectMode back to CORNER which is default
                    this.sketch.rectMode(PConstants.CORNER);
                }
            }
        }
    }

    //render queue for the Widget
    protected void render()
    {
        //render with rectMode(CENTER)!!!
        this.sketch.rectMode(PConstants.CENTER);

        //render the rect of the button with round corners or straight
        this.sketch.fill(this.rendercolor);
        this.sketch.noStroke();
        if(this.visible)
        {
            if (this.round)
            {
                this.sketch.rect(this.posx, this.posy, this.sizes[0], this.sizes[1], 10);
            }
            else
            {
                this.sketch.rect(this.posx, this.posy, this.sizes[0], this.sizes[1]);
            }
        }

        //render the font
        this.sketch.fill(this.foreground);
        this.sketch.textAlign(PConstants.CENTER);
        this.sketch.textSize(this.textSize);
        this.sketch.text(this.text, this.posx, this.posy);

        //set rectMode back to CORNER which is default
        this.sketch.rectMode(PConstants.CORNER);
    }
}

//TODO: Copy gen_colors over to other widgets -> new default color values