package beastybuttons;

import java.util.ArrayList;

import processing.core.*;

public class BB_Image extends Widget
{
	//******VARIABLES******

	//instances of the table
	protected static int INSTANCES = 0;
	
	//PApplet ref
	private final PApplet REF;
	
	//image ref
	protected PImage IMG;
	
	//saved crops for exporting
	ArrayList<Crop> crops = new ArrayList<>();
	
	
	//******CONSTRUCTORS******
	
	public BB_Image(PApplet ref, String imgpath) {
		this.REF = ref;
		this.IMG = this.REF.loadImage(imgpath);
		this.sizes[0] = this.IMG.width;
		this.sizes[1] = this.IMG.height;
		this.generateColors();
		this.generateID();
	}
	
	//import constructor
	protected BB_Image(PApplet ref) {
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
    
    //generate the ID
    private void generateID() {
        String convert = String.valueOf(BB_Image.INSTANCES);
        this.ID = "BB_Image" + convert;
        BB_Image.INSTANCES+=1;
        this.SOURCEPATH = this.ID;
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
    
    
    //******SET METHODS******
    
    public BB_Image setImage(PImage img) {
    	this.IMG = img;
    	this.sizes[0] = img.width;
    	this.sizes[1] = img.height;
    	return this;
    }
    
    public BB_Image setSize(float x, float y) {
    	this.sizes[0] = x;
    	this.sizes[1] = y;
    	return this;
    }
    
    //default is 1
    public BB_Image setScaleFactor(float factorx, float factory) {
    	this.sizes[0] = this.sizes[0]*factorx;
    	this.sizes[1] = this.sizes[1]*factory;
    	return this;
    }
    
    //crops from one edge(pixel_amount is from the original size of the image)
    //in some cases this will result in a position change, so you will have to update the position again afterwords!
    public BB_Image cropImage(String edge, int pixel_amount) {
    	this.crops.add(new Crop(edge, pixel_amount));
    	PImage newimg;
    	float pixel_size;
    	float pixel_amount_size;
    	float new_size;
    	
    	if(edge.equals("left")) {
    		if(pixel_amount > 0 && pixel_amount < this.IMG.width) {
    			newimg = this.REF.createImage(this.IMG.width-pixel_amount, this.IMG.height, PConstants.RGB);
    			
    			pixel_size = this.sizes[0]/(float)this.IMG.width;
    			pixel_amount_size = pixel_size*(float)pixel_amount;
    			new_size = this.sizes[0]-pixel_amount_size;
    			this.IMG = this.IMG.get(pixel_amount, 0, this.IMG.width, this.IMG.height);
    			
    			
    			this.REF.loadPixels();
    			int skip_amount = 0;
    			for(int i = 0; i < this.IMG.pixels.length; i++) {
    				if(this.REF.alpha(this.IMG.pixels[i]) > 0) {
    					newimg.pixels[i-skip_amount] = this.IMG.pixels[i];
    				}
    				else {
    					skip_amount+=1;
    				}
    			}
    			this.IMG = newimg;
    			this.REF.updatePixels();
    			this.sizes[0] = new_size;
    		}
    		else {
    			throw new RuntimeException("Crop amount out of bounds");
    		}
    	}
    	else if(edge.equals("right")) {
    		if(pixel_amount > 0 && pixel_amount < this.IMG.width) {
    			newimg = this.REF.createImage(this.IMG.width-pixel_amount, this.IMG.height, PConstants.RGB);
    			
    			pixel_size = this.sizes[0]/(float)this.IMG.width;
    			pixel_amount_size = pixel_size*(float)pixel_amount;
    			new_size = this.sizes[0]-pixel_amount_size;
    			this.IMG = this.IMG.get(0, 0, this.IMG.width-pixel_amount, this.IMG.height);
    			
    			
    			this.REF.loadPixels();
    			int skip_amount = 0;
    			for(int i = 0; i < this.IMG.pixels.length; i++) {
    				if(this.REF.alpha(this.IMG.pixels[i]) > 0) {
    					newimg.pixels[i-skip_amount] = this.IMG.pixels[i];
    				}
    				else {
    					skip_amount+=1;
    				}
    			}
    			this.IMG = newimg;
    			this.REF.updatePixels();
    			this.sizes[0] = new_size;
    		}
    		else {
    			throw new RuntimeException("Crop amount out of bounds");
    		}
    	}
    	else if(edge.equals("top")) {
    		if(pixel_amount > 0 && pixel_amount < this.IMG.height) {
    			newimg = this.REF.createImage(this.IMG.width, this.IMG.height-pixel_amount, PConstants.RGB);
    			
    			pixel_size = this.sizes[1]/(float)this.IMG.height;
    			pixel_amount_size = pixel_size*(float)pixel_amount;
    			new_size = this.sizes[1]-pixel_amount_size;
    			this.IMG = this.IMG.get(0, pixel_amount, this.IMG.width, this.IMG.height);
    			
    			
    			this.REF.loadPixels();
    			int skip_amount = 0;
    			for(int i = 0; i < this.IMG.pixels.length; i++) {
    				if(this.REF.alpha(this.IMG.pixels[i]) > 0) {
    					newimg.pixels[i-skip_amount] = this.IMG.pixels[i];
    				}
    				else {
    					skip_amount+=1;
    				}
    			}
    			this.IMG = newimg;
    			this.REF.updatePixels();
    			this.sizes[1] = new_size;
    		}
    		else {
    			throw new RuntimeException("Crop amount out of bounds");
    		}
    	}
    	else if(edge.equals("bottom")) {
    		if(pixel_amount > 0 && pixel_amount < this.IMG.height) {
    			newimg = this.REF.createImage(this.IMG.width, this.IMG.height-pixel_amount, PConstants.RGB);
    			
    			pixel_size = this.sizes[1]/(float)this.IMG.height;
    			pixel_amount_size = pixel_size*(float)pixel_amount;
    			new_size = this.sizes[1]-pixel_amount_size;
    			this.IMG = this.IMG.get(0, 0, this.IMG.width, this.IMG.height-pixel_amount);
    			
    			
    			this.REF.loadPixels();
    			int skip_amount = 0;
    			for(int i = 0; i < this.IMG.pixels.length; i++) {
    				if(this.REF.alpha(this.IMG.pixels[i]) > 0) {
    					newimg.pixels[i-skip_amount] = this.IMG.pixels[i];
    				}
    				else {
    					skip_amount+=1;
    				}
    			}
    			this.IMG = newimg;
    			this.REF.updatePixels();
    			this.sizes[1] = new_size;
    		}
    		else {
    			throw new RuntimeException("Crop amount out of bounds");
    		}
    	}
    	else {
    		PApplet.println("WARNING(BB): UNKNOWN INPUT FOR CROPIMAGE() -> CONTINUEING WITHOUT CROPPING THE IMAGE");
    	}
    	return this;
    }
    
    
  
  	public BB_Image setPosition(float x, float y) {
  		this.positions[0] = x;
  		this.positions[1] = y;
  		this.calc_tt_auto_pos();
  		return this;
  	}
  	
  	public BB_Image setPosition(String a, String b) {
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
  	
  	public BB_Image setPosition(String a, float y) {
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
  	
  	public BB_Image setPosition(float x, String b) {
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
  	public BB_Image hide(boolean visible) {
  		this.visible = !visible;
  		return this;
  	}

  //disables interaction and tooltip displaying
  	public BB_Image setActive(boolean active) {
  		this.active = active;
  		return this;
  	}
  	
  	@Override
  	public BB_Image setLayer(int layer) {
  		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
  			this.LAYER = layer;
  		}
  		else {
  			PApplet.println("WARNING(BB): UNKNOWN LAYER SET -> CONTINUEING WITHOUT SETTING THE LAYER");
  		}
  		return this;
  	}
  	
  	@Override
  	public BB_Image setID(String id) {
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
  	
  	//called when imported to execute the saved crops to get the resulting export image
  	protected void executeCrops() {
  		for(Crop c : this.crops) {
  			this.cropImage(c.edge, c.pixel_amount);
  		}
  	}
  	
  	protected void addCrop(String edge, int pixel_amount) { 
  		this.crops.add(new Crop(edge, pixel_amount));
  	}
  	
  	protected ArrayList<Crop> getCrops(){
  		return this.crops;
  	}
  	
  	public BB_Image copySettings(BB_Image img) {
  		
  		
  		return this;
  	}
  	
  	
  	
  	//******GET METHODS******
  	
  	public float[] getSize() {
		return this.sizes;
	}
  	
  	public PImage getImage() {
  		return this.IMG;
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
    public BB_Image createTooltip(String text, float size, int fg, int bg) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.ttforeground = fg;
    	this.ttbackground = bg;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public BB_Image createTooltip(String text, float size) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public BB_Image createTooltip(String text, float xsize, float ysize, int fg, int bg) {
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
    
    public BB_Image createTooltip(String text, float xsize, float ysize) {
    	this.tooltiptext = text;
    	this.ttsizes[0] = xsize;
    	this.ttsizes[1] = ysize;
    	this.calculatemaxttsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    
    //configure the tooltip further
    public BB_Image configureTooltip(boolean roundtt) {
    	if(this.tooltiptext != null) {
    		this.roundtt = roundtt;
    		this.enable_intervall = false;
    	}
    	else {
    		throw new RuntimeException("there is no tooltip to configure, it needs to be created first");
    	}
    	return this;
    }
    
    public BB_Image configureTooltip(boolean roundtt, float intervall) {
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
    public BB_Image setTooltipPosition(float x, float y) {
    	this.ttpositions[0] = x;
    	this.ttpositions[1] = y;
    	return this;
    }
    
    public BB_Image setTooltipPosition(String pos) {
    	if(pos.equals("auto")) {
    		this.calc_tt_auto_pos();
    	}
    	else {
    		throw new RuntimeException("no valid input for setting the position of the tooltip");
    	}
    	return this;
    }
    
    public BB_Image setTooltipSize(float x, float y) {
    	this.ttsizes[0] = x;
    	this.ttsizes[1] = y;
    	return this;
    }
    
    public BB_Image setTooltipTextsize(float s) {
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
    public BB_Image setTooltiptextoffset(float x, float y) {
    	this.tttextoffset[0] = x;
    	this.tttextoffset[1] = y;
    	return this;
    }
    
    //enable or disable the tooltip(tooltip is automatically enabled on creation)
    public BB_Image enableTooltip(boolean enable) {
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
    	this.REF.imageMode(PConstants.CENTER);
    	
    	this.REF.image(this.IMG, this.positions[0], this.positions[1], this.sizes[0], this.sizes[1]);
    	
    	/*this.REF.fill(255, 0, 0);
    	this.REF.ellipse(this.positions[0], this.positions[1], 30, 30);
    	this.REF.strokeWeight(10);
    	this.REF.stroke(255, 255, 0);
    	this.REF.line(this.positions[0]-this.sizes[0]/2f, this.positions[1], this.positions[0]+this.sizes[0]/2f, this.positions[1]);
    	*/
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
    
    //protected inner class for logs of image crops(used for export/import)
    class Crop{
    	String edge;
    	int pixel_amount;
    	
    	Crop(String edge, int pixel_amount){
    		this.edge = edge;
    		this.pixel_amount = pixel_amount;
    	}
    }
}
