package beastybuttons;

import processing.core.*;

public class Checkbox extends Widget 
{
	//******VARIABLES******
	
	protected enum Checktype{
		CHECK,
		CROSS,
		POINT,
		CIRCLE
	}
	
	//instances of the checkbox
	protected static int INSTANCES = 0;
	
	//the defined checktype
	protected Checktype CHECKTYPE = Checktype.CHECK;
	
	//PApplet ref
	private final PApplet REF;
	
	//the checked or not state
	protected boolean state; 
	
	//round checkbox or not?
	protected boolean round;
		
	//hotkey combination
	protected int hotkey = -1;
	
	//methods on check(on check, on uncheck)
  	protected String oc, ou;
	
  	//the stroke width of the outline
  	protected float outline = 2f;
  	
  	
  	//******CONSTRUCTORS******
	
	public Checkbox(PApplet ref, float size, boolean checked) {
		this.REF = ref;
		this.sizes[0] = size;
		this.sizes[1] = size;
		this.state = checked;
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
	}
	
	public Checkbox(PApplet ref, float size, boolean round, boolean checked) {
		this.REF = ref;
		this.sizes[0] = size;
		this.sizes[1] = size;
		this.round = round;
		this.state = checked;
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
	}
	
	//constructor for import
	protected Checkbox(PApplet ref) {
		this.REF = ref;
	}
	
	
	
	//******CALLED FROM CONSTRUCTOR******
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
	
	private void generateColors() {
		this.foreground = this.REF.color(0, 0, 0, 255);
        this.background = this.REF.color(100, 100, 100, 255);
        this.overcolor = this.REF.color(250, 0, 0, 255);
        this.clickcolor = this.REF.color(150, 0, 150, 255);
        this.ttbackground = this.REF.color(200, 200, 200, 255);
        this.ttforeground = this.REF.color(0, 0, 0, 255);
	}
	
	private void generateID() {
		String convert = String.valueOf(Checkbox.INSTANCES);
        this.ID = "Checkbox" + convert;
        Checkbox.INSTANCES+=1;
        this.SOURCEPATH = this.ID;
	}
	
	
	
	//******SET METHODS******
	public Checkbox copySettings(Checkbox b) {
    	this.positions[0] = b.positions[0];
    	this.positions[1] = b.positions[1];
    	
    	this.sizes[0] = b.sizes[0];
    	this.sizes[1] = b.sizes[1];
    	
    	this.round = b.round;
    	
    	this.hotkey = b.hotkey;
    	
    	this.CHECKTYPE = b.CHECKTYPE;
    	
    	this.background = b.background;
    	this.foreground = b.foreground;
    	this.overcolor = b.overcolor;
    	this.clickcolor = b.clickcolor;
    	
    	this.active = b.active;
    	this.visible = b.visible;
    	
    	this.LAYER = b.LAYER;
    	
    	this.oc = b.oc;
    	this.ou = b.ou;
    	
    	this.outline = b.outline;
    	
    	return this;
    }
	
	public Checkbox setOutlineWidth(float o_width) {
    	this.outline = o_width;
    	return this;
    }
	
    public Checkbox setForegroundColor(int fg) {
    	this.foreground = fg;
    	return this;
    }
    
    public Checkbox setBackgroundColor(int bg) {
    	this.background = bg;
    	return this;
    }
    
    public Checkbox setOvercolor(int c) {
    	this.overcolor = c;
    	return this;
    }
    
    public Checkbox setClickcolor(int c) {
    	this.clickcolor = c;
    	return this;
    }
    
    public Checkbox resetColors() {
    	this.generateColors();
    	return this;
    }
    
    public Checkbox setSize(float xsize, float ysize) {
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		
		return this;
	}
    
    
  //set the position of the checkbox -> if tooltip is enabled you will need to re-set its position
  	public Checkbox setPosition(float x, float y) {
  		this.positions[0] = x;
  		this.positions[1] = y;
  		this.calc_tt_auto_pos();
  		return this;
  	}
  	
  	public Checkbox setPosition(String a, String b) {
  		if(a.equals("left")) {
  			this.positions[0] = this.sizes[0]/2;
  		}
  		else if(a.equals("right")) {
  			this.positions[0] = this.REF.width - this.sizes[0]/2;
  		}
  		else {
  			throw new RuntimeException("not a valid input for setting the position of the checkbox");
  		}
  		
  		if(b.equals("top")) {
  			this.positions[1] = this.sizes[1]/2;
  		}
  		else if(b.equals("bottom")) {
  			this.positions[1] = this.REF.height - this.sizes[1]/2;
  		}
  		else {
  			throw new RuntimeException("not a valid input for setting the position of the checkbox");
  		}
  		this.calc_tt_auto_pos();
  		return this;
  	}
  	
  	public Checkbox setPosition(String a, float y) {
  		if(a.equals("left")) {
  			this.positions[0] = this.sizes[0]/2;
  		}
  		else if(a.equals("right")) {
  			this.positions[0] = this.REF.width - this.sizes[0]/2;
  		}
  		else {
  			throw new RuntimeException("not a valid input for setting the position of the checkbox");
  		}
  		this.positions[1] = y;
  		this.calc_tt_auto_pos();
  		return this;
  	}
  	
  	public Checkbox setPosition(float x, String b) {
  		if(b.equals("top")) {
  			this.positions[1] = this.sizes[1]/2;
  		}
  		else if(b.equals("bottom")) {
  			this.positions[1] = this.REF.height - this.sizes[1]/2;
  		}
  		else {
  			throw new RuntimeException("not a valid input for setting the position of the checkbox");
  		}
  		this.positions[0] = x;
  		this.calc_tt_auto_pos();
  		return this;
  	}
  	
  	//does not display the widget anymore
  	public Checkbox hide(boolean visible) {
  		this.visible = !visible;
  		return this;
  	}
  	
  	//disables interaction and tooltip displaying
  	public Checkbox setActive(boolean active) {
  		this.active = active;
  		return this;
  	}
  	
  	public Checkbox setHotkey(int key) {
  		if(key != PConstants.TAB && key != PConstants.ENTER) {
  			this.hotkey = key;
  		}
  		else {
  			throw new RuntimeException("TAB or ENTER as hotkey is not available");
  		}
  		return this;
  	}
	
	
  	@Override
	public Checkbox setLayer(int layer) {
		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
			this.LAYER = layer;
		}
		else {
			PApplet.println("WARNING(BB): UNKNOWN LAYER SET -> CONTINUEING WITHOUT SETTING THE LAYER");
		}
		return this;
	}
	
	@Override
	public Checkbox setID(String id) {
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
	
	public Checkbox setChecktype(String s) {
		if(s.equals("check")) {
			this.CHECKTYPE = Checktype.CHECK;
		}
		else if(s.equals("cross")) {
			this.CHECKTYPE = Checktype.CROSS;
		}
		else if(s.equals("point")) {
			this.CHECKTYPE = Checktype.POINT;
		}
		else if(s.equals("circle")) {
			this.CHECKTYPE = Checktype.CIRCLE;
		}
		else {
			throw new RuntimeException("not a valid input for the checktype");
		}
		return this;
	}
	
	
	//******GET METHODS******

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
	
	public boolean getState() {
		return this.state;
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
    public Checkbox createTooltip(String text, float size, int fg, int bg) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.ttforeground = fg;
    	this.ttbackground = bg;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public Checkbox createTooltip(String text, float size) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public Checkbox createTooltip(String text, float xsize, float ysize, int fg, int bg) {
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
    
    public Checkbox createTooltip(String text, float xsize, float ysize) {
    	this.tooltiptext = text;
    	this.ttsizes[0] = xsize;
    	this.ttsizes[1] = ysize;
    	this.calculatemaxttsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    
    //configure the tooltip further
    public Checkbox configureTooltip(boolean roundtt) {
    	if(this.tooltiptext != null) {
    		this.roundtt = roundtt;
    		this.enable_intervall = false;
    	}
    	else {
    		throw new RuntimeException("there is no tooltip to configure, it needs to be created first");
    	}
    	return this;
    }
    
    public Checkbox configureTooltip(boolean roundtt, float intervall) {
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
    public Checkbox setTooltipPosition(float x, float y) {
    	this.ttpositions[0] = x;
    	this.ttpositions[1] = y;
    	return this;
    }
    
    public Checkbox setTooltipPosition(String pos) {
    	if(pos.equals("auto")) {
    		this.calc_tt_auto_pos();
    	}
    	else {
    		throw new RuntimeException("no valid input for setting the position of the tooltip");
    	}
    	return this;
    }
    
    public Checkbox setTooltipSize(float x, float y) {
    	this.ttsizes[0] = x;
    	this.ttsizes[1] = y;
    	return this;
    }
    
    public Checkbox setTooltipTextsize(float s) {
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
    public Checkbox setTooltiptextoffset(float x, float y) {
    	this.tttextoffset[0] = x;
    	this.tttextoffset[1] = y;
    	return this;
    }
    
    //enable or disable the tooltip(tooltip is automatically enabled on creation)
    public Checkbox enableTooltip(boolean enable) {
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
	
	public Checkbox onCheck(String method) {
		this.oc = method;
		return this;
	}
	
	public Checkbox onUncheck(String method) {
		this.ou = method;
		return this;
	}
	
	
	//******SELECT METHODS******
    
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
    	
    	if(this.visible) {
    		if(this.round) {
    			if(this.outline > 0) {
    				this.REF.rect(this.positions[0], this.positions[1], this.sizes[0], this.sizes[1], 10);
    			}
    			this.REF.fill(this.rendercolor);
    			this.REF.rect(this.positions[0], this.positions[1], this.sizes[0]-(this.outline*2), this.sizes[1]-(this.outline*2), 5);
    		}
    		else {
    			if(this.outline > 0) {
    				this.REF.rect(this.positions[0], this.positions[1], this.sizes[0], this.sizes[1]);
    			}
    			this.REF.fill(this.rendercolor);
    			this.REF.rect(this.positions[0], this.positions[1], this.sizes[0]-(this.outline*2), this.sizes[1]-(this.outline*2));
    			
    		}
    		
    		if(state) {
	    		this.REF.stroke(this.foreground);
	    		this.REF.fill(this.foreground);
	    		this.REF.strokeWeight(this.sizes[0]/30);
	    		
	    		if(this.CHECKTYPE == Checktype.CHECK) {
	    			this.REF.line(this.positions[0]-this.sizes[0]/3, this.positions[1], this.positions[0], this.positions[1]+this.sizes[1]/3);
	    			this.REF.line(this.positions[0], this.positions[1]+this.sizes[1]/3, this.positions[0]+this.sizes[0]/3, this.positions[1]-this.sizes[1]/3);
	    		}
	    		else if(this.CHECKTYPE == Checktype.CROSS) {
	    			this.REF.line(this.positions[0]-this.sizes[0]/3, this.positions[1]-this.sizes[1]/3, this.positions[0]+this.sizes[0]/3, this.positions[1]+this.sizes[1]/3);
	    			this.REF.line(this.positions[0]-this.sizes[0]/3, this.positions[1]+this.sizes[1]/3, this.positions[0]+this.sizes[0]/3, this.positions[1]-this.sizes[1]/3);
	    		}
	    		else if(this.CHECKTYPE == Checktype.POINT) {
	    			this.REF.ellipse(this.positions[0], this.positions[1], this.sizes[0]/3, this.sizes[1]/3);
	    		}
	    		else if(this.CHECKTYPE == Checktype.CIRCLE) {
	    			this.REF.noFill();
	    			this.REF.ellipse(this.positions[0], this.positions[1], this.sizes[0]/2, this.sizes[1]/2);
	    		}
    		}
    	}
    	this.REF.fill(this.foreground);
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
