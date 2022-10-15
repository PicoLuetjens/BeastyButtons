package beastybuttons;

import processing.core.*;

public class Label extends Widget
{
	
	//******VARIABLES******
	
	//instances of the label
	protected static int INSTANCES = 0;
	
	//PApplet ref
	private final PApplet REF;
	
	//round label or not?
	protected boolean round;
	
	//text of the label
	protected String text;
	
	//textsize of the label
	protected float textSize;
	
	//offset the text in the label
  	protected float[] textoffset = {0f, 0f};
  	
  	
  	
  	
  	//******CONSTRUCTORS******
  	
	//old Label_asot constructor
	public Label(PApplet ref, String text, float size) {
		this.REF = ref;
		this.text = text;
		this.textSize = size;
		this.calculateSize();
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
	}
	
	//old Label_asot constructor
	public Label(PApplet ref, String text, float size, boolean round) {
		this.REF = ref;
		this.text = text;
		this.textSize = size;
		this.round = round;
		this.calculateSize();
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
	}
	
	//old Label_asos constructor
	public Label(PApplet ref, float xsize, float ysize, String text) {
		this.REF = ref;
		this.text = text;
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		this.calculatemaxsize();
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
	}
	
	//old Label_asos contructor
	public Label(PApplet ref, float xsize, float ysize, String text, boolean round) {
		this.REF = ref;
		this.text = text;
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		this.round = round;
		this.calculatemaxsize();
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
	}
	
	//import constructor
	public Label(PApplet ref) {
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
        String convert = String.valueOf(Label.INSTANCES);
        this.ID = "Label" + convert;
        Label.INSTANCES+=1;
        this.SOURCEPATH = this.ID;
    }
    
    
    
    
    //******SET METHODS******
    
    public Label setForegroundColor(int fg) {
    	this.foreground = fg;
    	return this;
    }
    
    public Label setBackgroundColor(int bg) {
    	this.background = bg;
    	return this;
    }
    
    
    public Label resetColors() {
    	this.generateColors();
    	return this;
    }
    
	public Label setText(String text, boolean update) {
		this.text = text;
		
		if(update) {
			//update the size of the button to fit
			this.calculateSize();
		}
		return this;
	}
	
	public Label setTextsize(float textSize, boolean update) {
		this.textSize = textSize;
		
		if(update) {
			//update the size of the button to fit
			this.calculateSize();
		}
		return this;
	}
	
	public Label setSize(float xsize, float ysize, boolean update) {
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		
		if(update) {
			//update the textsize for the text to fit
			this.calculatemaxsize();
		}
		return this;
	}
	
	
	//set the position of the button -> if tooltip is enabled you will need to re-set its position
	public Label setPosition(float x, float y) {
		this.positions[0] = x;
		this.positions[1] = y;
		//this.calc_tt_auto_pos();
		return this;
	}
	
	public Label setPosition(String a, String b) {
		if(a.equals("left")) {
			this.positions[0] = this.sizes[0]/2;
		}
		else if(a.equals("right")) {
			this.positions[0] = this.REF.width - this.sizes[0]/2;
		}
		else {
			throw new RuntimeException("not a valid input for setting the position of the label");
		}
		
		if(b.equals("top")) {
			this.positions[1] = this.sizes[1]/2;
		}
		else if(b.equals("bottom")) {
			this.positions[1] = this.REF.height - this.sizes[1]/2;
		}
		else {
			throw new RuntimeException("not a valid input for setting the position of the label");
		}
		//this.calc_tt_auto_pos();
		return this;
	}
	
	public Label setPosition(String a, float y) {
		if(a.equals("left")) {
			this.positions[0] = this.sizes[0]/2;
		}
		else if(a.equals("right")) {
			this.positions[0] = this.REF.width - this.sizes[0]/2;
		}
		else {
			throw new RuntimeException("not a valid input for setting the position of the label");
		}
		this.positions[1] = y;
		//this.calc_tt_auto_pos();
		return this;
	}
	
	public Label setPosition(float x, String b) {
		if(b.equals("top")) {
			this.positions[1] = this.sizes[1]/2;
		}
		else if(b.equals("bottom")) {
			this.positions[1] = this.REF.height - this.sizes[1]/2;
		}
		else {
			throw new RuntimeException("not a valid input for setting the position of the label");
		}
		this.positions[0] = x;
		//this.calc_tt_auto_pos();
		return this;
	}
	
	//does not display the widget anymore
	public Label hide(boolean visible) {
		this.visible = !visible;
		return this;
	}
	
	@Override
	public Label setLayer(int layer) {
		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
			this.LAYER = layer;
		}
		else {
			PApplet.println("WARNING(BB): UNKNOWN LAYER SET -> CONTINUEING WITHOUT SETTING THE LAYER");
		}
		return this;
	}
	
	@Override
	public Label setID(String id) {
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
    public Label setTextoffset(float x, float y) {
    	this.textoffset[0] = x;
    	this.textoffset[1] = y;
    	return this;
    }
    
    
    public Label copySettings(Label l) {
    	
    	this.text = l.text;
		this.textSize = l.textSize;
		this.round = l.round;
		this.textoffset = l.textoffset;
		this.visible = l.visible;
		this.LAYER = l.LAYER;
		this.positions = l.positions;
		this.sizes = l.sizes;
		this.rendercolor = l.rendercolor;
		this.background = l.background;
		this.foreground = l.foreground;
    	
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
    
    //not used but needs to be overridden
    @Override
    protected void calc_tt_auto_pos() {
    	
    }
    
    
    //******RENDER METHODS******
    
    @Override
    protected void render() {
    	this.REF.rectMode(PConstants.CENTER);
    	
    	this.REF.fill(this.background);
    	this.REF.noStroke();
    
    	if(this.visible) {
    		if(this.round) {
    			this.REF.rect(this.positions[0], this.positions[1], this.sizes[0], this.sizes[1], 10);
    		}
    		else {
    			this.REF.rect(this.positions[0], this.positions[1], this.sizes[0], this.sizes[1]);
    		}
    		this.REF.fill(this.foreground);
        	//this.REF.textAlign(PConstants.CENTER);
    		this.REF.textAlign(PConstants.CENTER, PConstants.CENTER);
        	this.REF.textSize(this.textSize);
        	this.REF.text(this.text, this.positions[0]+this.textoffset[0], (this.positions[1] - this.sizes[1]/20)+this.textoffset[1]);
    	}
    	
    	
    	this.REF.rectMode(PConstants.CORNER);
    }
    
    @Override
    protected void rendertooltip() {
    	//do nothing in here since Labels have no tooltip to it available.
    	//this is just declared here to not cause an error because it needs to override
    	//the widget abstract top class one
    	return;
    }
}
