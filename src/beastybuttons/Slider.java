package beastybuttons;

import processing.core.*;

@NotImplementedYet
@Experimental
public class Slider extends Widget
{
	
	//******VARIABLES******
	
	//Instances of the Inputfield
	protected static int INSTANCES = 0;
	
	//PApplet ref
	private final PApplet REF;
	
	// 0 = value, 1 = lowerBoundary, 2 = upperBoundary, 3 = incrementvalue, 4 = increment in pixels
	private Number[] slider_stats = new Number[5];
	
	private float textSize;
	
	private int hotkey;
	
	enum Slidertype{
		IntegerSlider,
		LongSlider,
		FloatSlider,
		DoubleSlider
	}
	
	//the general type of the slider all values will be converted to
	private Slidertype SLIDERTYPE;
	
	
	//******CONSTRUCTORS******
	
	public Slider(PApplet ref, float xsize, float ysize) {
		this.REF = ref;
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		this.slider_stats[1] = 0.0f;
		this.slider_stats[2] = 1.0f;
		this.slider_stats[0] = 0.0f;
		this.slider_stats[3] = 0.1f;
		this.slider_stats[4] = xsize/((float)this.slider_stats[2]/(float)this.slider_stats[3]);
		this.SLIDERTYPE = Slidertype.FloatSlider;
	}
	
	public Slider(PApplet ref, float xsize, float ysize, Number lower, Number upper, Number increment) {
		this.REF = ref;
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		this.slider_stats[1] = lower;
		this.slider_stats[2] = upper;
		this.slider_stats[0] = lower;
		this.slider_stats[3] = increment;
		this.slider_stats[4] = xsize/((float)this.slider_stats[2]/(float)this.slider_stats[3]);
		
		if(upper instanceof Integer && lower instanceof Integer && increment instanceof Integer) {
			this.SLIDERTYPE = Slidertype.IntegerSlider;
		}
		else if(upper instanceof Long && lower instanceof Long && increment instanceof Long) {
			this.SLIDERTYPE = Slidertype.LongSlider;
		}
		else if(upper instanceof Float && lower instanceof Float && increment instanceof Float) {
			this.SLIDERTYPE = Slidertype.FloatSlider;
		}
		else if(upper instanceof Double && lower instanceof Double && increment instanceof Double) {
			this.SLIDERTYPE = Slidertype.DoubleSlider;
		}
		else {
			throw new RuntimeException("Slider input values are of different types");
		}
		
		if((float)increment > (float)upper/2) {
			throw new RuntimeException("Increment size is to high");
		}
	}
	
	//import constructor
	protected Slider(PApplet ref) {
		this.REF = ref;
	}
	
	
	
	//******CALLED FROM CONSTRUCTOR******
	
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
        String convert = String.valueOf(Slider.INSTANCES);
        this.ID = "Slider" + convert;
        Slider.INSTANCES+=1;
        this.SOURCEPATH = this.ID;
    }
    
    
    
    //******SET METHODS******
    
    public Slider setForegroundColor(int fg) {
    	this.foreground = fg;
    	return this;
    }
    
    public Slider setBackgroundColor(int bg) {
    	this.background = bg;
    	return this;
    }
    
    public Slider resetColors() {
    	this.generateColors();
    	return this;
    }
    
    
    public Slider setSize(float xsize, float ysize) {
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		
		//update the increment size
		//..
		
		return this;
	}
    
    public Slider setTextsize(float size) {
    	this.textSize = size;
    	return this;
    }
    
	public Slider setSliderValue(Number value) {
		
		
		
		return this;
	}
	
	public Slider setSliderLower(Number value) {
		
		
		return this;
	}
	
	public Slider setSliderUpper(Number value) {
		
		
		
		return this;
	}
	
	//set the position of the button -> if tooltip is enabled you will need to re-set its position
	public Slider setPosition(float x, float y) {
		this.positions[0] = x;
		this.positions[1] = y;
		this.calc_tt_auto_pos();
		return this;
	}
	
	public Slider setPosition(String a, String b) {
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
	
	public Slider setPosition(String a, float y) {
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
	
	public Slider setPosition(float x, String b) {
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
	public Slider hide(boolean visible) {
		this.visible = !visible;
		return this;
	}
	
	//disables interaction and tooltip displaying
	public Slider setActive(boolean active) {
		this.active = active;
		return this;
	}
	
		
	public Slider setHotkey(int key) {
		if(key != PConstants.TAB && key != PConstants.ENTER) {
			this.hotkey = key;
		}
		else {
			throw new RuntimeException("TAB or ENTER as hotkey is not available");
		}
		return this;
	}
	
	@Override
	public Slider setLayer(int layer) {
		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
			this.LAYER = layer;
		}
		else {
			PApplet.println("WARNING(BB): UNKNOWN LAYER SET -> CONTINUEING WITHOUT SETTING THE LAYER");
		}
		return this;
	}
	
	@Override
	public Slider setID(String id) {
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
	
	public Slider copySettings(Slider s) {
		
		
		
		return this;
	}

	
	
	//******GET METHODS******
	
	public float getTextSize() {
		return this.textSize;
	}
	
	public Number getValue() {
		return this.slider_stats[0];
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
    public Slider createTooltip(String text, float size, int fg, int bg) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.ttforeground = fg;
    	this.ttbackground = bg;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public Slider createTooltip(String text, float size) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public Slider createTooltip(String text, float xsize, float ysize, int fg, int bg) {
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
    
    public Slider createTooltip(String text, float xsize, float ysize) {
    	this.tooltiptext = text;
    	this.ttsizes[0] = xsize;
    	this.ttsizes[1] = ysize;
    	this.calculatemaxttsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    
    //configure the tooltip further
    public Slider configureTooltip(boolean roundtt) {
    	if(this.tooltiptext != null) {
    		this.roundtt = roundtt;
    		this.enable_intervall = false;
    	}
    	else {
    		throw new RuntimeException("there is no tooltip to configure, it needs to be created first");
    	}
    	return this;
    }
    
    public Slider configureTooltip(boolean roundtt, float intervall) {
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
    public Slider setTooltipPosition(float x, float y) {
    	this.ttpositions[0] = x;
    	this.ttpositions[1] = y;
    	return this;
    }
    
    public Slider setTooltipPosition(String pos) {
    	if(pos.equals("auto")) {
    		this.calc_tt_auto_pos();
    	}
    	else {
    		throw new RuntimeException("no valid input for setting the position of the tooltip");
    	}
    	return this;
    }
    
    public Slider setTooltipSize(float x, float y) {
    	this.ttsizes[0] = x;
    	this.ttsizes[1] = y;
    	return this;
    }
    
    public Slider setTooltipTextsize(float s) {
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
    public Slider setTooltiptextoffset(float x, float y) {
    	this.tttextoffset[0] = x;
    	this.tttextoffset[1] = y;
    	return this;
    }
    
    //enable or disable the tooltip(tooltip is automatically enabled on creation)
    public Slider enableTooltip(boolean enable) {
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
    
    
    //******RENDER METHODS******
     @Override
     protected void render() {
    	 
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
