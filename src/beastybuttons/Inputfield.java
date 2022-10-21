package beastybuttons;

import processing.core.*;
import processing.event.KeyEvent;

public class Inputfield extends Widget 
{
	
	//******VARIABLES******
	
	//Instances of the Inputfield
	protected static int INSTANCES = 0;
	
	//PApplet ref
	private final PApplet REF;
	
	//longest input used for size calculation with two constructors
	private int longest_input;
	
	protected float textSize;
	
	protected boolean round;
	
	//the text shown before box is active and has no input
	protected String greyText = "input...";
	
	//the color of the greyeed text
	protected int gt_color;
	
	//is input happening?
	protected boolean input = false;
	
	//holds the inputted text
	protected String input_text = "";
	
	//hotkey
	protected int hotkey = -1;
	
	//offset the text in the inputfield
  	protected float[] textoffset = {0f, 0f};
  	
  	//color when inputting
  	protected int input_color;
  	
  	protected int limit_input_length = 1000;
  	
  	//was the default set size or textsize from constructor overwritten? If so do not display any greyText anymore since the Widgets
  	//appereance has been completely changed
  	private boolean changed = false;
	
	//******CONSTRUCTORS******
	
	//old Inputfield_asot constructor
	public Inputfield(PApplet ref, int li, float size) {
		this.REF = ref;
		this.longest_input = li;
		String li_str = "";
		for(int i = 0; i < li; i++) {
			li_str += "M";
		}
		this.textSize = size;
		this.calculateSize(li_str);
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
	}
	
	//old Inputfield_asot constructor
	public Inputfield(PApplet ref, int li, float size, boolean round) {
		this.REF = ref;
		this.longest_input = li;
		String li_str = "";
		for(int i = 0; i < li; i++) {
			li_str += "M";
		}
		this.textSize = size;
		this.round = round;
		this.calculateSize(li_str);
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
	}
	
	//old Inputfield_asos constructor
	public Inputfield(PApplet ref, int li, float xsize, float ysize) {
		this.REF = ref;
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		this.longest_input = li;
		String li_str = "";
		for(int i = 0; i < li; i++) {
			li_str += "M";
		}
		this.calculatemaxsize(li_str);
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
	}
	
	//old Inputfield_asos constructor
	public Inputfield(PApplet ref, int li, float xsize, float ysize, boolean round) {
		this.REF = ref;
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		this.round = round;
		this.longest_input = li;
		String li_str = "";
		for(int i = 0; i < li; i++) {
			li_str += "M";
		}
		this.calculatemaxsize(li_str);
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
	}
	
	//import constructor
  	protected Inputfield(PApplet ref) {
  		this.REF = ref;
  	}
	
	
	//******CALLED FROM CONSTRUCTOR******
	
	//calculates the size of the Button(with multiple line support)
    private void calculateSize(String text) {
        float current_size = this.REF.g.textSize;
        String[] splits = text.split(this.line_separator);
        int lines = splits.length;
        String longest_line = "";
        int longest_length = 0;
        for(String s : splits) {
        	if(s.length() > longest_length) {
        		longest_length = s.length();
        		longest_line = s;
        	}
        }
        
        this.REF.textSize(this.textSize);
        float xsize = 0f, ysize = 0f;

        for(int i = 0; i < longest_length; i++)
        {
            xsize += this.REF.textWidth(longest_line.charAt(i));
        }
        
        for(String s : splits) {
        	ysize += (this.REF.textAscent()+this.REF.textDescent())*1.4f;
        }
        xsize = xsize*1.15f;
        ysize = ysize*1.15f;
        this.sizes[0] = xsize;
        this.sizes[1] = ysize;
        this.REF.textSize(current_size);
    }
	
    
    
    //calculates the size of the Tooltip(with multiple line support)
    private void calculatettsize() {
        float current_size = this.REF.g.textSize;
        String[] splits = this.tooltiptext.split(this.line_separator);
        int lines = splits.length;
        String longest_line = "";
        int longest_length = 0;
        for(String s : splits) {
        	if(s.length() > longest_length) {
        		longest_length = s.length();
        		longest_line = s;
        	}
        }
        
        this.REF.textSize(this.tooltiptextsize);
        float xsize = 0f, ysize = 0f;

        for(int i = 0; i < longest_length; i++)
        {
            xsize += this.REF.textWidth(longest_line.charAt(i));
        }
        
        for(String s : splits) {
        	ysize += (this.REF.textAscent()+this.REF.textDescent())*1.4f;
        }
        xsize = xsize*1.15f;
        ysize = ysize*1.15f;
        this.ttsizes[0] = xsize;
        this.ttsizes[1] = ysize;
        this.REF.textSize(current_size);
    }
    
    
    //calculates the textsize of the Button
    private void calculatemaxsize(String text) {
    	float current_size = this.REF.g.textSize;
    	String[] splits = text.split(this.line_separator);
        int lines = splits.length;
        String longest_line = "";
        int longest_length = 0;
        for(String s : splits) {
        	if(s.length() > longest_length) {
        		longest_length = s.length();
        		longest_line = s;
        	}
        }
        
        
        int temp_text_size = 0;
        float xsize = 0f, ysize = 0f;
        
        
        //test for the max possible size in x direction
        for(int i = 1; i < 500; i++) {
        	this.REF.textSize(i);
        	for(int j = 0; j < longest_length; j++) {
        		xsize += this.REF.textWidth(longest_line.charAt(j));
        	}
        	if(xsize > this.sizes[0]) {
        		temp_text_size = i;
        		break;
        	}
        	else {
        		xsize = 0f;
        	}
        }
        
        
        //take the calculated testsize and test if it works with y direction too
        //if not test downwards from that size until a possible size in y direction is reached
        for(int k = temp_text_size; k > 0; k--) {
        	this.REF.textSize(k);
        	ysize = (this.REF.textAscent()+this.REF.textDescent())*(splits.length*1.2f);//*1.2 extension of the ysize for each line
        	if(ysize < this.sizes[1]) {
        		temp_text_size= k;
        		break;
        	}
    
        }
        this.textSize = temp_text_size;
        this.textSize = this.textSize/1.15f;
        //this.textSize = this.textSize/1.2f;
        this.REF.textSize(current_size);
    }
    
    
    //calculates the textsize of the tooltip
    private void calculatemaxttsize() {
    	float current_size = this.REF.g.textSize;
    	String[] splits = this.tooltiptext.split(this.line_separator);
        int lines = splits.length;
        String longest_line = "";
        int longest_length = 0;
        for(String s : splits) {
        	if(s.length() > longest_length) {
        		longest_length = s.length();
        		longest_line = s;
        	}
        }
        
        
        int temp_text_size = 0;
        float xsize = 0f, ysize = 0f;
        
        
        //test for the max possible size in x direction
        for(int i = 1; i < 500; i++) {
        	this.REF.textSize(i);
        	for(int j = 0; j < longest_length; j++) {
        		xsize += this.REF.textWidth(longest_line.charAt(j));
        	}
        	if(xsize > this.ttsizes[0]) {
        		temp_text_size = i;
        		break;
        	}
        	else {
        		xsize = 0f;
        	}
        }
        
        
        //take the calculated testsize and test if it works with y direction too
        //if not test downwards from that size until a possible size in y direction is reached
        for(int k = temp_text_size; k > 0; k--) {
        	this.REF.textSize(k);
        	ysize = (this.REF.textAscent()+this.REF.textDescent())*splits.length;
        	if(ysize < this.ttsizes[1]) {
        		temp_text_size= k;
        		break;
        	}
    
        }
        this.tooltiptextsize = temp_text_size;
        this.tooltiptextsize = this.tooltiptextsize/1.15f;
        this.REF.textSize(current_size);
    }
    
    
    //generate the colors after sketch can be referenced
    private void generateColors() {
        this.foreground = this.REF.color(0, 0, 0, 255);
        this.background = this.REF.color(100, 100, 100, 255);
        this.overcolor = this.REF.color(250, 0, 0, 255);
        this.clickcolor = this.REF.color(150, 0, 150, 255);
        this.ttbackground = this.REF.color(200, 200, 200, 255);
        this.ttforeground = this.REF.color(0, 0, 0, 255);
        this.gt_color = this.REF.color(200, 200, 200, 255);
        this.input_color = this.REF.color(100, 0, 0, 255);
    }
	
    
    //generate the ID
    private void generateID() {
        String convert = String.valueOf(Inputfield.INSTANCES);
        this.ID = "Inputfield" + convert;
        Inputfield.INSTANCES+=1;
        this.SOURCEPATH = this.ID;
    }
    
	
    
    //******SET METHODS******
    
    public Inputfield setForegroundColor(int fg) {
    	this.foreground = fg;
    	return this;
    }
    
    public Inputfield setBackgroundColor(int bg) {
    	this.background = bg;
    	return this;
    }
    
    public Inputfield setInputColor(int ic) {
    	this.input_color = ic;
    	return this;
    }
    
    
    public Inputfield setOvercolor(int c) {
    	this.overcolor = c;
    	return this;
    }
    /*
    public Inputfield setClickcolor(int c) {
    	this.clickcolor = c;
    	return this;
    }*/
    
    public Inputfield setGreyTextColor(int c) {
    	this.gt_color = c;
    	return this;
    }
    
    public Inputfield resetColors() {
    	this.generateColors();
    	return this;
    }
	
    public Inputfield clearInput() {
    	this.input_text = "";
    	return this;
    }
    
    public Inputfield setTextsize(float textSize, boolean update) {
    	if(this.input_text.length() > 0) {
			this.textSize = textSize;
			this.changed = true;
			
			if(update) {
				//update the size of the button to fit
				this.calculateSize(this.input_text);
			}
    	}
		return this;
	}
	
    
	public Inputfield setSize(float xsize, float ysize, boolean update) {
    	if(this.input_text.length() > 0) {
			this.sizes[0] = xsize;
			this.sizes[1] = ysize;
			this.changed = true;
			
			if(update) {
				//update the textsize for the text to fit
				this.calculatemaxsize(this.input_text);
			}
    	}
		return this;
	}
	
	public Inputfield setGreyedText(String text) {
		this.greyText = text;
		return this;
	}
	
	
	//set the position of the button -> if tooltip is enabled you will need to re-set its position
	public Inputfield setPosition(float x, float y) {
		this.positions[0] = x;
		this.positions[1] = y;
		this.calc_tt_auto_pos();
		return this;
	}
	
	public Inputfield setPosition(String a, String b) {
		if(a.equals("left")) {
			this.positions[0] = this.sizes[0]/2;
		}
		else if(a.equals("right")) {
			this.positions[0] = this.REF.width - this.sizes[0]/2;
		}
		else {
			throw new RuntimeException("not a valid input for setting the position of the button");
		}
		
		if(b.equals("top")) {
			this.positions[1] = this.sizes[1]/2;
		}
		else if(b.equals("bottom")) {
			this.positions[1] = this.REF.height - this.sizes[1]/2;
		}
		else {
			throw new RuntimeException("not a valid input for setting the position of the button");
		}
		this.calc_tt_auto_pos();
		return this;
	}
	
	public Inputfield setPosition(String a, float y) {
		if(a.equals("left")) {
			this.positions[0] = this.sizes[0]/2;
		}
		else if(a.equals("right")) {
			this.positions[0] = this.REF.width - this.sizes[0]/2;
		}
		else {
			throw new RuntimeException("not a valid input for setting the position of the button");
		}
		this.positions[1] = y;
		this.calc_tt_auto_pos();
		return this;
	}
	
	public Inputfield setPosition(float x, String b) {
		if(b.equals("top")) {
			this.positions[1] = this.sizes[1]/2;
		}
		else if(b.equals("bottom")) {
			this.positions[1] = this.REF.height - this.sizes[1]/2;
		}
		else {
			throw new RuntimeException("not a valid input for setting the position of the button");
		}
		this.positions[0] = x;
		this.calc_tt_auto_pos();
		return this;
	}
	
	//does not display the widget anymore
	public Inputfield hide(boolean visible) {
		this.visible = !visible;
		return this;
	}
	
	//disables interaction and tooltip displaying
	public Inputfield setActive(boolean active) {
		this.active = active;
		return this;
	}
	
	//set an input limit amount
	public Inputfield setInputLimit(int limit) {
		this.limit_input_length = limit;
		return this;
	}
	
	//A REGISTERED HOTKEY WILL BE BLOCKED FOR INPUT!!!
	//THE HOTKEY HAS TO BE A CHAR VALUE AND IN UPPER CASE TO WORK!!!
	public Inputfield setHotkey(int key) {
		if(key != PConstants.TAB && key != PConstants.ENTER) {
			this.hotkey = key;
		}
		else {
			throw new RuntimeException("TAB or ENTER as hotkey is not available");
		}
		return this;
	}
	
	@Override
	public Inputfield setLayer(int layer) {
		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
			this.LAYER = layer;
		}
		else {
			PApplet.println("WARNING(BB): UNKNOWN LAYER SET -> CONTINUEING WITHOUT SETTING THE LAYER");
		}
		return this;
	}
	
	@Override
	public Inputfield setID(String id) {
		this.ID = id;
		this.SOURCEPATH = this.SOURCEPATH.replace(this.ID, id);
		return this;
	}
	
	@Override
	protected void editSourcepath(String action, String id) {
		String[] splits = this.SOURCEPATH.split("/");
		if(action.equals("delete")) {
			this.SOURCEPATH = splits[1];
		}
		else if(action.equals("add")) {
			this.SOURCEPATH = id + "/" + this.SOURCEPATH;
		}
		else {
			throw new RuntimeException("sourcepath error - could not update the sourcepath");
		}
	}
	
	//offset the text in the button
    public Inputfield setTextoffset(float x, float y) {
    	this.textoffset[0] = x;
    	this.textoffset[1] = y;
    	return this;
    }
    
    public Inputfield copySettings(Inputfield field) {
    	
    	return this;
    }
    
    
  //******GET METHODS******
	
  	public String getText() {
  		return this.input_text;
  	}
  	
  	public float getTextSize() {
  		return this.textSize;
  	}
  	
  	public float[] getSize() {
  		return this.sizes;
  	}
  	
  	public int getForegroundColor() {
  		return this.foreground;
  	}
  	
  	public int getBackgroundColor() {
  		return this.background;
  	}
  	
  	public int getInputColor() {
  		return this.input_color;
  	}
  	
  	/*
  	public int getOverColor() {
  		return this.overcolor;
  	}
  	
  	public int getClickColor() {
  		return this.clickcolor;
  	}
  	*/
  	
  	@Override
  	public String getID() {
  		return this.ID;
  	}
  	
  	@Override
  	public String getSourcePath() {
  		return this.SOURCEPATH;
  	}
  	
  	public float[] getPosition() {
  		return this.positions;
  	}
  	
  	//return if Inputfield is currently selected for input
  	public boolean getSelection() {
  		return this.input;
  	}
  	
  	
  //******TOOLTIP METHODS******
	
  	//creates a new tooltip overrriding old ones
    public Inputfield createTooltip(String text, float size, int fg, int bg) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.ttforeground = fg;
    	this.ttbackground = bg;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public Inputfield createTooltip(String text, float size) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public Inputfield createTooltip(String text, float xsize, float ysize, int fg, int bg) {
    	this.tooltiptext = text;
    	this.ttsizes[0] = xsize;
    	this.ttsizes[1] = ysize;
    	this.ttforeground = fg;
    	this.ttbackground = bg;
    	this.calculatemaxttsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public Inputfield createTooltip(String text, float xsize, float ysize) {
    	this.tooltiptext = text;
    	this.ttsizes[0] = xsize;
    	this.ttsizes[1] = ysize;
    	this.calculatemaxttsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    
    //configure the tooltip further
    public Inputfield configureTooltip(boolean roundtt) {
    	if(this.tooltiptext != null) {
    		this.roundtt = roundtt;
    		this.enable_intervall = false;
    	}
    	else {
    		throw new RuntimeException("there is no tooltip to configure, it needs to be created first");
    	}
    	return this;
    }
    
    public Inputfield configureTooltip(boolean roundtt, float intervall) {
    	if(this.tooltiptext != null) {
    		this.roundtt = roundtt;
    		this.intervall = intervall;
    		this.enable_intervall = true;
    	}
    	else {
    		throw new RuntimeException("there is no tooltip to configure, it needs to be created first");
    	}
    	return this;
    }
    
    //sets the tooltip position manually
    public Inputfield setTooltipPosition(float x, float y) {
    	this.ttpositions[0] = x;
    	this.ttpositions[1] = y;
    	return this;
    }
    
    public Inputfield setTooltipPosition(String pos) {
    	if(pos.equals("auto")) {
    		this.calc_tt_auto_pos();
    	}
    	else {
    		throw new RuntimeException("no valid input for setting the position of the tooltip");
    	}
    	return this;
    }
    
    public Inputfield setTooltipSize(float x, float y) {
    	this.ttsizes[0] = x;
    	this.ttsizes[1] = y;
    	return this;
    }
    
    public Inputfield setTooltipTextsize(float s) {
    	this.tooltiptextsize = s;
    	return this;
    }
    
    public float[] getTooltipSize() {
    	return this.ttsizes;
    }
    
    public float getTooltipTextsize() {
    	return this.tooltiptextsize;
    }
    
    public float[] getTooltipPosition() {
    	return this.ttpositions;
    }
    
    //offset the text in the tooltip
    public Inputfield setTooltiptextoffset(float x, float y) {
    	this.tttextoffset[0] = x;
    	this.tttextoffset[1] = y;
    	return this;
    }
    
    //enable or disable the tooltip(tooltip is automatically enabled on creation)
    public Inputfield enableTooltip(boolean enable) {
    	this.tooltip_enabled = enable;
    	return this;
    }
    
    //positions the tooltip automatically
    @Override
    protected void calc_tt_auto_pos()
    {
        if(this.positions[0] <= this.REF.width/2f)
        {
            this.ttpositions[0] = this.positions[0] + this.sizes[0] - (this.sizes[0]/5);
        }
        else if(this.positions[0] > this.REF.width/2f)
        {
            this.ttpositions[0] = (this.positions[0] + (this.sizes[0]/5)) - this.ttsizes[0];
        }
        if(this.positions[1] <= this.REF.height/2f)
        {
            this.ttpositions[1] = (this.positions[1] + this.sizes[1]) - (this.sizes[1]/5);
        }
        else if(this.positions[1] > this.REF.height/2f)
        {
            this.ttpositions[1] = this.positions[1] - this.sizes[1]/5;
        }
    }
    
    
    
    //******SELECT METHODS******
    
    //make this publicly accessible to get information about over position of the mouse even when the mouse handler is not registered, if you want
    //to use this information in processing. Otherwise this is information is not accessible to the user -> works only with mouse
    
    @Override
    public boolean over(float mx, float my) {
    	if(mx >= this.positions[0]-this.sizes[0]/2 && mx <= this.positions[0]+this.sizes[0]/2)
        {
            if(my >= this.positions[1]-this.sizes[1]/2 && my <= this.positions[1]+this.sizes[1]/2)
            {
                return true;
            }
        }
        return false;
    }
    
    //write the input to the text variable
    protected void writeinput(KeyEvent e) {
    	if(e.getKeyCode() != this.hotkey) {
		    if(e.getKeyCode() >= 'A' && e.getKeyCode() <= 'Z') {
		    	if(this.input_text.length() < this.limit_input_length) {
		    		this.input_text = this.input_text + e.getKey();
		    	}
		    }
    	}
    	if(e.getKeyCode() == PApplet.BACKSPACE) {
	    //else if(this.REF.keyCode == PConstants.BACKSPACE) {
    	//else if(e.getKeyCode() == KeyCodes.KEYCODES.get("delete")) {
    	//else if(e.getKey() == '') {
	      this.input_text = removeLastChar(this.input_text);
	    }
		else if(e.getKeyCode() == PApplet.ENTER) {
	    //else if(e.getKeyCode() == PConstants.ENTER) {
	    //else if(this.REF.key == PConstants.ENTER) {
	    //else if(e.getKeyCode() == PConstants.ENTER) {
	    	if(this.input_text.length() < this.limit_input_length) {
	    		this.input_text = this.input_text + "\n";
	    	}
	    }
	    else if(e.getKeyCode() == ' ') {
	    	if(this.input_text.length() < this.limit_input_length) {
	    		this.input_text = this.input_text + " ";
	    	}
	    }
	    else if(e.getKeyCode() == this.hotkey) {
	    	if(BeastyWorld.HANDLERS[1]) {
	    		this.input = false;
	    	}
	    }
    }
    
    
    private String removeLastChar(String s) {
        return (s == null || s.length() == 0)
          ? ""
          : (s.substring(0, s.length() - 1));
    }
    
    //******RENDER METHODS******
    
    @Override
    protected void render() {
    	this.REF.rectMode(PConstants.CENTER);
    	
    	this.REF.fill(this.foreground);
    	//this.REF.fill(this.rendercolor);
    	
    	this.REF.noStroke();
    	
    	
    	if(this.visible) {
    		if(this.round) {
    			this.REF.rect(this.positions[0], this.positions[1], this.sizes[0], this.sizes[1], 10);
    			if(this.input) {
    				this.rendercolor = this.input_color;
    			}
    			else {
    				this.rendercolor = this.background;
    			}
    			this.REF.fill(this.rendercolor);
    			this.REF.rect(this.positions[0], this.positions[1], this.sizes[0]-this.sizes[1]/10, this.sizes[1]-this.sizes[1]/10,10);
    		}
    		else {
    			this.REF.rect(this.positions[0], this.positions[1], this.sizes[0], this.sizes[1]);
    			if(this.input) {
    				this.rendercolor = this.input_color;
    			}
    			else {
    				this.rendercolor = this.background;
    			}
    			this.REF.fill(this.rendercolor);
    			this.REF.rect(this.positions[0], this.positions[1], this.sizes[0]-this.sizes[1]/10, this.sizes[1]-this.sizes[1]/10);
    		}
    		
    		this.REF.fill(this.foreground);
        	//this.REF.textAlign(PConstants.CENTER);
    		this.REF.textAlign(PConstants.CENTER, PConstants.CENTER);
    		if(this.textSize > 0) {
    			this.REF.textSize(this.textSize);
    		}
    		
        	//input text is given
        	if(this.input_text.length() > 0) {
        		this.REF.text(this.input_text, this.positions[0]+this.textoffset[0], (this.positions[1] - this.sizes[1]/20)+this.textoffset[1]);
        	}
        	
        	
        	
        	//no input is given but greyText
        	else {
        		if(!this.changed) {
        			this.REF.fill(this.gt_color);
        			this.REF.text(this.greyText, this.positions[0]+this.textoffset[0], (this.positions[1] - this.sizes[1]/20)+this.textoffset[1]);
        		}
        	}
        	
    	}
    	
    	this.REF.rectMode(PConstants.CORNER);
    }
    
    @Override
    protected void rendertooltip() {
    	
    	//no transition animation is happening
    	if(!BeastyWorld.ISANIMATING) {
    		
    		
    		if(this.active) {
    			this.REF.rectMode(PConstants.CENTER);
    			
    			if(this.tooltip_enabled) {
    				
    				if(this.over(this.REF.mouseX, this.REF.mouseY)) {
    				
	    				//normal render loop without depending on interval
	    				if(!this.enable_intervall) {
		    				this.REF.noStroke();
		    				this.REF.fill(this.ttbackground);
		    				
		    				if(this.roundtt) {
		    					this.REF.rect(this.ttpositions[0], this.ttpositions[1], this.ttsizes[0], this.ttsizes[1], 10);
		    				}
		    				else {
		    					this.REF.rect(this.ttpositions[0], this.ttpositions[1], this.ttsizes[0], this.ttsizes[1]);
		    				}
		    				
		    				this.REF.fill(this.ttforeground);
		    				this.REF.textAlign(PConstants.CENTER, PConstants.CENTER);
		    				this.REF.textSize(this.tooltiptextsize);
		    				this.REF.text(this.tooltiptext, this.ttpositions[0]+this.tttextoffset[0], (this.ttpositions[1] - this.ttsizes[1]/20)+this.tttextoffset[1]);
	    				}
	    				
	    				//render loop depending on interval
	        			else {
	        				if(System.currentTimeMillis() <= (this.timeStep + (double)this.intervall)) {
	        					
	    	    				this.REF.noStroke();
	    	    				this.REF.fill(this.ttbackground);
	    	    				
	    	    				if(this.roundtt) {
	    	    					this.REF.rect(this.ttpositions[0], this.ttpositions[1], this.ttsizes[0], this.ttsizes[1], 10);
	    	    				}
	    	    				else {
	    	    					this.REF.rect(this.ttpositions[0], this.ttpositions[1], this.ttsizes[0], this.ttsizes[1]);
	    	    				}
	    	    				
	    	    				this.REF.fill(this.ttforeground);
	    	    				this.REF.textAlign(PConstants.CENTER, PConstants.CENTER);
	    	    				this.REF.textSize(this.tooltiptextsize);
	    	    				this.REF.text(this.tooltiptext, this.ttpositions[0]+this.tttextoffset[0], (this.ttpositions[1] - this.ttsizes[1]/20)+this.tttextoffset[1]);
	        				}
	        			}
    				}
    			}
    			this.REF.rectMode(PConstants.CORNER);
    		}
    	}
    }
    
}