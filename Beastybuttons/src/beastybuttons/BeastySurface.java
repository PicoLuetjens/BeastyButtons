package beastybuttons;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import processing.core.*;


public class BeastySurface 
{
	//PApplet ref
	private final PApplet REF;
	
	protected String ID = "BeastySurface";
	
	protected String SOURCEPATH = "BeastySurface";
	
	private static int INSTANCES = 0;
	
	private PImage backgroundimg;
	
	//should the backgroundimage be stretched to fit in frame?
	private boolean stretch_backgroundimg = false;
	
	//the sizes for the backgroundimage
	private float max_imgsize_x = 0f, max_imgsize_y = 0f;
	
	//to eliminate surfaces from being added multiple times
	protected boolean already_added = false;
	
	//widget list ofthe surface
	protected ArrayList<Widget> widgets = new ArrayList<>();
	
	//for capturing active states and restore them(for surface change animations)
	protected ArrayList<Boolean> active_states = new ArrayList<>();
	
	//cycle through with tab
	protected ArrayList<Widget> tabswitchlist = new ArrayList<>();
	
	//backgroundcolor
	private int backgroundcolor;
	
	//position variables
	protected float posx, posy;
	
	//if the background image is offset
	private float offsetx = 0f, offsety = 0f;
	
	//the index in the tabswitchlist
	protected int tabswitchindex = 0;
	
	
	
	//******CONSTRUCTOR******
	
	public BeastySurface(PApplet ref) {
		this.REF = ref;
		this.generateID();
		this.generate_backgroundimg();
		
		//default background color is grey and 100% transparent so its showing through
		this.backgroundcolor = this.REF.color(100, 100, 100, 0);
		this.posx = this.REF.width/2f;
		this.posy = this.REF.height/2f;
	}
	
	
	
	//******CALLED FROM CONSTRUCTOR******
	
	//generate the backgroundimg as transparent image
	private void generate_backgroundimg() {
		this.backgroundimg = this.REF.createImage(this.REF.width, this.REF.height, PConstants.ARGB);
		for(int i = 0; i < this.backgroundimg.pixels.length; i++) {
			this.backgroundimg.pixels[i] = this.REF.color(255, 255, 255, 0);
		}
	}
	
	//generate the default id
	private void generateID() {
		String convert = String.valueOf(BeastySurface.INSTANCES);
		this.ID = "BeastySurface" + convert;
		BeastySurface.INSTANCES+=1;
		this.SOURCEPATH = this.ID;
	}
	
	
	//******SET METHODS******
	
	//copy the settings from another surface(without specific like sourcepath, id, widgets etc.)
	@Experimental
	public BeastySurface copySettings(BeastySurface source) {
		PApplet.println("WARNING(BB): copySettings() IS STILL HIGHLY EXPERIMENTAL, AVOID USE IF POSSIBLE");
		this.setSurfaceImage(source.backgroundimg, source.stretch_backgroundimg);
		this.setSurfaceColor(source.backgroundcolor);
		return this;
	}
	
	//edits the SOURCEPATH of this instance(called from top class)
	protected void editSourcePath(String action, String id) {
		String[] splits = this.SOURCEPATH.split("/");
		if(action.equals("delete")) {
			this.SOURCEPATH = splits[1];
		}
		else if(action.equals("add")) {
			this.SOURCEPATH = id + "/" + this.SOURCEPATH;
		}
		else {
			throw new RuntimeException("Error while editing source path");
		}
	}
	
	
	//set the id manually
	public BeastySurface setID(String id) {	
		this.SOURCEPATH.replace(this.ID, id);
		this.ID = id;
		return this;
	}
	
	
	public BeastySurface setSurfaceColor(int col) {
		this.backgroundcolor = col;
		return this;
	}
	
	
	public BeastySurface setSurfaceImage(PImage img, boolean stretch) {
		this.stretch_backgroundimg = stretch;
		this.backgroundimg = img;
		
		//calculate the max scale of the image
		if(!this.stretch_backgroundimg) {
			
			float img_scale = 0f;
			
			if(this.backgroundimg.width > this.backgroundimg.height) {
				img_scale = (float)this.REF.width/(float)this.backgroundimg.width;
				this.max_imgsize_x = (float)this.REF.width;
				this.max_imgsize_y = (float)this.backgroundimg.height*img_scale;
				PApplet.println("width > height");
			}
			else if(this.backgroundimg.width < this.backgroundimg.height) {
				img_scale = (float)this.REF.height/(float)this.backgroundimg.height;
				this.max_imgsize_x = (float)this.backgroundimg.width*img_scale;
				this.max_imgsize_y = (float)this.REF.height;
				PApplet.println("width < height");
				PApplet.println(this.backgroundimg.width);
				PApplet.println(this.backgroundimg.height);
				PApplet.println(this.REF.height);
			}
			else {
				img_scale = (float)this.REF.width/(float)this.backgroundimg.width;
				this.max_imgsize_x = (float)this.REF.width;
				this.max_imgsize_y = (float)this.backgroundimg.height*img_scale;
				PApplet.println("width == height");
			}
			PApplet.println("IN");
			PApplet.println(String.valueOf(img_scale));
			PApplet.println(String.valueOf(max_imgsize_x));
			PApplet.println(String.valueOf(max_imgsize_y));
		}
		else {
			this.max_imgsize_x = (float)this.REF.width;
			this.max_imgsize_y = (float)this.REF.height;
		}
		
		return this;
	}
	
	public BeastySurface offsetSurfaceImage(float x, float y) {
		if(this.backgroundimg != null) {
			this.offsetx = x;
			this.offsety = y;
		}
		else {
			PApplet.println("WARNING(BB): COULD NOT OFFSET THE SURFACE IMAGE -> CONTINUEING WITHOUT OFFSETTING");
		}
		return this;
	}
	
	
	@Experimental
	public BeastySurface enableonLayer(int layer) {
		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
			throw new RuntimeException("given layer is out of bounds");
		}
		if(this.widgets.size() > 0) {
			for(Widget w : this.widgets) {
				if(w.LAYER == layer) {
					w.active = true;
				}
			}
		}
		
		return this;
	}
	
	@Experimental
	public BeastySurface disableonLayer(int layer) {
		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
			throw new RuntimeException("given layer is out of bounds");
		}
		if(this.widgets.size() > 0) {
			for(Widget w : this.widgets) {
				if(w.LAYER == layer) {
					w.active = false;
				}
			}
		}
		
		return this;
	}
	
	
	public BeastySurface addWidget(Widget w) {
		if(!w.already_added) {
			if(!w.ID.equals(this.ID)) {
				w.already_added = true;
				this.widgets.add(w);
				this.tabswitchlist.clear();
				this.generate_tabswitchlist();
				
				w.editSourcepath("add", this.ID);
			}
			else {
				PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID AS THE SURFACE OBJECT -> CONTINUEING WITHOUT ADDING");
			}
		}
		else {
			PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES -> CONTINUEING WITHOUT ADDING");
		}	
		return this;
	}
	
	@Experimental
	public BeastySurface removeWidget(Widget w) {
		if(this.widgets.size() > 0) {
			this.widgets.remove(w);
			if(this.widgets.size() > 0) {
				this.tabswitchlist.clear();
				this.generate_tabswitchlist();
			}
		}
		else {
			PApplet.println("WARNING(BB): NO WIDGET FOUND TO REMOVE");
		}
		return this;
	}
	
	//******GET METHODS******
	
	public String getID() {
		return this.ID;
	}
	
	public String getSourcepath() {
		return this.SOURCEPATH;
	}
	
	//get the position of the surface(from the middle of the surface) -> used for animating things with it while whipe transition
	public float[] getPosition() {
		float [] temp = {this.posx, this.posy};
		return temp;
	}
	
	public ArrayList<Widget> getWidgetList(){
		return this.widgets;
	}
	
	
	//******STATES METHODS******
	
	//capture all widgets active states to restore them later
	protected void capture_active_states() {
		if(this.widgets.size() > 0) {
			for(Widget w : this.widgets) {
				this.active_states.add(w.active);
			}
		}
	}
	
	
	protected void reset_active_states() {
		if(this.widgets.size() > 0) {
			for(int i = 0; i < this.widgets.size(); i++) {
				this.widgets.get(i).active = this.active_states.get(i);
			}
		}
	}
	
	
	
	
	//******RENDER METHODS******
	
	//does not render widgets only renders the background for the surface
	protected void render() {
		
		//render from the middle
		this.REF.rectMode(PConstants.CENTER);
		
		//fill color
		this.REF.fill(this.backgroundcolor);
		this.REF.noStroke();
		
		//render backgroundimg(transparent by default)
		this.REF.imageMode(PConstants.CENTER);
		this.REF.image(this.backgroundimg, this.posx+this.offsetx, this.posy+this.offsety, this.max_imgsize_x, this.max_imgsize_y);
		
		//render backgroundcolor(transparent by default)
		this.REF.rect(this.posx, this.posy, this.REF.width, this.REF.height);
		
		//set back to CORNER which is default
		this.REF.rectMode(PConstants.CORNER);
	}



	
	//******TABSWITCH METHODS******
	
	//generates an order list based of the y-position to cycle through on tab switch
	//using java stream syntax might not work
	public void generate_tabswitchlist() {
		this.tabswitchlist = (ArrayList<Widget>) this.widgets.stream()
				.sorted(Comparator.comparing(widget -> widget.positions[1]))
				.collect(Collectors.toList());
		
	}
}
