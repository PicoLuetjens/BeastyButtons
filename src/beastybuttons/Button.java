package beastybuttons;

import processing.core.*;

public class Button extends Widget
{
	//******VARIABLES******
	
	//instances of the button
	protected static int INSTANCES = 0;
	
	//PApplet ref
	private final PApplet REF;
	
	//text of the button
	protected String text;
	
	//textsize of the button
	protected float textSize;
	
	//round button or not?
	protected boolean round;
	
	//hotkey
	protected int hotkey = -1;
	
	//methods on click(on left click, on middle click, on right click)
  	protected String olc, omc, orc;
	
  	//offset the text in the button
  	protected float[] textoffset = {0f, 0f};
	
  	//the stroke width of the outline
  	protected float outline = 2f;
  	
  	
  	//******CONSTRUCTORS******
	
	//old Button_asot constructor
	public Button(PApplet ref, String text, float size) {
		this.REF = ref;
		this.text = text;
		this.textSize = size;
		this.calculateSize();
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
		//this.line_separator = System.getProperty("line.separator");
	}
	
	//old Button_asot constructor
	public Button(PApplet ref, String text, float size, boolean round) {
		this.REF = ref;
		this.text = text;
		this.textSize = size;
		this.round = round;
		this.calculateSize();
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
		//this.line_separator = System.getProperty("line.separator");
	}
	
	//old Button_asos constructor
	public Button(PApplet ref, float xsize, float ysize, String text) {
		this.REF = ref;
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		this.text = text;
		this.calculatemaxsize();
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
		//this.line_separator = System.getProperty("line.separator");
	}
	
	//old Button_asos constructor
	public Button(PApplet ref, float xsize, float ysize, String text, boolean round) {
		this.REF = ref;
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		this.text = text;
		this.round = round;
		this.calculatemaxsize();
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
		//this.line_separator = System.getProperty("line.separator");
	}
	
	//constructor for import that is used internally
	protected Button(PApplet ref) {
		//the rest is read from import file
		this.REF = ref;
	}
	
	
	
	
	
	//******CALLED FROM CONSTRUCTOR******
	
	//calculates the size of the Button(with multiple line support)
    private void calculateSize() {
        float current_size = this.REF.g.textSize;
        String[] splits = this.text.split(this.line_separator);
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
    private void calculatemaxsize() {
    	float current_size = this.REF.g.textSize;
    	String[] splits = this.text.split(this.line_separator);
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
    }
	
    
    //generate the ID
    private void generateID() {
        String convert = String.valueOf(Button.INSTANCES);
        this.ID = "Button" + convert;
        Button.INSTANCES+=1;
        this.SOURCEPATH = this.ID;
    }
    
    
    
    //******SET METHODS******
    public Button copySettings(Button b) {
    	this.positions[0] = b.positions[0];
    	this.positions[1] = b.positions[1];
    	
    	this.sizes[0] = b.sizes[0];
    	this.sizes[1] = b.sizes[1];
    	
    	this.round = b.round;
    	
    	this.text = b.text;
    	this.textSize = b.textSize;
    	
    	this.hotkey = b.hotkey;
    	
    	this.textoffset[0] = b.textoffset[0];
    	this.textoffset[1] = b.textoffset[1];
    	
    	this.background = b.background;
    	this.foreground = b.foreground;
    	this.overcolor = b.overcolor;
    	this.clickcolor = b.clickcolor;
    	
    	this.active = b.active;
    	this.visible = b.visible;
    	
    	this.LAYER = b.LAYER;
    	
    	this.olc = b.olc;
    	this.omc = b.omc;
    	this.orc = b.orc;
    	
    	this.outline = b.outline;
    	
    	return this;
    }
    
    public Button setOutlineWidth(float o_width) {
    	this.outline = o_width;
    	return this;
    }
    
    
    public Button setForegroundColor(int fg) {
    	this.foreground = fg;
    	return this;
    }
    
    public Button setBackgroundColor(int bg) {
    	this.background = bg;
    	return this;
    }
    
    public Button setOvercolor(int c) {
    	this.overcolor = c;
    	return this;
    }
    
    public Button setClickcolor(int c) {
    	this.clickcolor = c;
    	return this;
    }
    
    public Button resetColors() {
    	this.generateColors();
    	return this;
    }
    
	public Button setText(String text, boolean update) {
		this.text = text;
		
		if(update) {
			//update the size of the button to fit
			this.calculateSize();
		}
		return this;
	}
	
	public Button setTextsize(float textSize, boolean update) {
		this.textSize = textSize;
		
		if(update) {
			//update the size of the button to fit
			this.calculateSize();
		}
		return this;
	}
	
	public Button setSize(float xsize, float ysize, boolean update) {
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		
		if(update) {
			//update the textsize for the text to fit
			this.calculatemaxsize();
		}
		return this;
	}
	
	
	//set the position of the button -> if tooltip is enabled you will need to re-set its position
	public Button setPosition(float x, float y) {
		this.positions[0] = x;
		this.positions[1] = y;
		this.calc_tt_auto_pos();
		return this;
	}
	
	public Button setPosition(String a, String b) {
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
	
	public Button setPosition(String a, float y) {
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
	
	public Button setPosition(float x, String b) {
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
	public Button hide(boolean visible) {
		this.visible = !visible;
		return this;
	}
	
	//disables interaction and tooltip displaying
	public Button setActive(boolean active) {
		this.active = active;
		return this;
	}
	
	//for button the hotkey will always execute the method assigned to the left click!!!
	public Button setHotkey(int key) {
		if(key != PConstants.TAB && key != PConstants.ENTER) {
			this.hotkey = key;
		}
		else {
			throw new RuntimeException("TAB or ENTER as hotkey is not available");
		}
		return this;
	}
	
	@Override
	public Button setLayer(int layer) {
		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
			this.LAYER = layer;
		}
		else {
			PApplet.println("WARNING(BB): UNKNOWN LAYER SET -> CONTINUEING WITHOUT SETTING THE LAYER");
		}
		return this;
	}
	
	@Override
	public Button setID(String id) {
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
    public Button setTextoffset(float x, float y) {
    	this.textoffset[0] = x;
    	this.textoffset[1] = y;
    	return this;
    }
	
	
	//******GET METHODS******
	
	public String getText() {
		return this.text;
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
	
	public int getOverColor() {
		return this.overcolor;
	}
	
	public int getClickColor() {
		return this.clickcolor;
	}
	
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
	
	
	
	
	//******TOOLTIP METHODS******
	
	//creates a new tooltip overrriding old ones
    public Button createTooltip(String text, float size, int fg, int bg) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.ttforeground = fg;
    	this.ttbackground = bg;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public Button createTooltip(String text, float size) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public Button createTooltip(String text, float xsize, float ysize, int fg, int bg) {
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
    
    public Button createTooltip(String text, float xsize, float ysize) {
    	this.tooltiptext = text;
    	this.ttsizes[0] = xsize;
    	this.ttsizes[1] = ysize;
    	this.calculatemaxttsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    
    //configure the tooltip further
    public Button configureTooltip(boolean roundtt) {
    	if(this.tooltiptext != null) {
    		this.roundtt = roundtt;
    		this.enable_intervall = false;
    	}
    	else {
    		throw new RuntimeException("there is no tooltip to configure, it needs to be created first");
    	}
    	return this;
    }
    
    public Button configureTooltip(boolean roundtt, float intervall) {
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
    public Button setTooltipPosition(float x, float y) {
    	this.ttpositions[0] = x;
    	this.ttpositions[1] = y;
    	return this;
    }
    
    public Button setTooltipPosition(String pos) {
    	if(pos.equals("auto")) {
    		this.calc_tt_auto_pos();
    	}
    	else {
    		throw new RuntimeException("no valid input for setting the position of the tooltip");
    	}
    	return this;
    }
    
    public Button setTooltipSize(float x, float y) {
    	this.ttsizes[0] = x;
    	this.ttsizes[1] = y;
    	return this;
    }
    
    public Button setTooltipTextsize(float s) {
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
    public Button setTooltiptextoffset(float x, float y) {
    	this.tttextoffset[0] = x;
    	this.tttextoffset[1] = y;
    	return this;
    }
    
    //enable or disable the tooltip(tooltip is automatically enabled on creation)
    public Button enableTooltip(boolean enable) {
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
    
    
    
   //******CLICK METHODS******
   
    public Button onLeftClick(String method) {
    	this.olc = method;
    	return this;
    }
    
    public Button onMiddleClick(String method) {
    	this.omc = method;
    	return this;
    }
    
    public Button onRightClick(String method) {
    	this.orc = method;
    	return this;
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
    
    
    //******RENDER METHODS******
    
    @Override
    protected void render() {
    	this.REF.rectMode(PConstants.CENTER);
    	
    	this.REF.fill(this.foreground);
    	//this.REF.fill(this.rendercolor);
    	
    	this.REF.noStroke();
    
    	if(this.visible) {
    		if(this.round) {
    			if(this.outline > 0) {
    				this.REF.rect(this.positions[0], this.positions[1], this.sizes[0], this.sizes[1], 10);
    			}	
    			this.REF.fill(this.rendercolor);
    			this.REF.rect(this.positions[0], this.positions[1], this.sizes[0]-(this.outline*2), this.sizes[1]-(this.outline*2) , 5);
    		}
    		else {
    			if(this.outline > 0) {
    				this.REF.rect(this.positions[0], this.positions[1], this.sizes[0], this.sizes[1]);
    			}
    			this.REF.fill(this.rendercolor);
    			this.REF.rect(this.positions[0], this.positions[1], this.sizes[0]-(this.outline*2), this.sizes[1]-(this.outline*2));
    		}
    		this.REF.fill(this.foreground);
        	//this.REF.textAlign(PConstants.CENTER);
    		this.REF.textAlign(PConstants.CENTER, PConstants.CENTER);
        	this.REF.textSize(this.textSize);
        	this.REF.text(this.text, this.positions[0]+this.textoffset[0], (this.positions[1] - this.sizes[1]/12)+this.textoffset[1]);
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
	    	    				this.REF.text(this.tooltiptext, this.ttpositions[0]+this.tttextoffset[0], (this.ttpositions[1] - this.ttsizes[1]/12)+this.tttextoffset[1]);
	        				}
	        			}
    				}
    			}
    			this.REF.rectMode(PConstants.CORNER);
    		}
    	}
    }
}

