package beastybuttons;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class BeastyWorld 
{
	//******VARIABLES******
	
	//PApplet ref
	private final PApplet REF;
	
	private final String ID = "world";
	
	private final String SOURCEPATH = "world";
	
	private static int INSTANCES = 0;
	
	//true while BeastyButtons is in RUNTIME Mode(after first call of draw())
	protected static boolean RUNNING = false;
	
	//true when a transition is happening
	protected static boolean ISANIMATING = false;
	
	//the operating mode
	private WidgetMode MODE = WidgetMode.WIDGET;
	
	//the amount of render layers for z dimension
	protected static final int LAYERS = 5;
	
	//for files on windows \ , for unix /
	private String separator = "\"";
	
	//variable to set RUNNING on first iteration of draw
	private boolean runtime_set = false;
	
	//the actual page to render
	private BeastySurface renderpage;
	
	//the page that is animated to when using surface animation
	@NotImplementedYet
	@Experimental
	private BeastySurface animateToPage = null;
	
	//logging enabled/disabled
	@NotImplementedYet
	@Experimental
	private boolean logging = false;
	
	@NotImplementedYet
	@Experimental
	private int loglevel;
	
	//The Loglist to write to at runtime
	@NotImplementedYet
	@Experimental
	private ArrayList<String> loglist = new ArrayList<>();
	
	@NotImplementedYet
	@Experimental
	private String logfilepath;
	
	//handlers for interaction
	protected static boolean[] HANDLERS = {false, false, false};
	
	//all widgets
	private ArrayList<Widget> widgets = new ArrayList<>();
	
	//the index in the tabswitchlist
	protected int tabswitchindex = 0;
	
	//if the tabswitch handler is disabled by the user, otherwise it will be automatically disabled when interacting with other handlers
	protected boolean tabswitch_user_disabled = true;
	
	protected boolean tabswitch_active = false;
	
	//cycle through with tab
	protected ArrayList<Widget> tabswitchlist = new ArrayList<>();
		
	//all surfaces
	private ArrayList<BeastySurface> surfaces = new ArrayList<>();
	
	
	//render font
	private PFont font;
	
	//variables before and after render loop
    private int beforecolor;
    private float beforestrokeweight;
    private PFont beforefont;
    private float beforetextsize;
    private int beforecolormode;
    private int beforeimagemode;
    private int beforeellipsemode;
    private int beforerectmode;
    private int beforetextalign;
    private boolean beforestroke;
    private int beforestrokecolor;
    
    private boolean disable_background = false;
    
    //change the timestep again when true -> for animating disappearance of tooltip
    private boolean new_timestep = true;
    
    private int BACKGROUNDCOLOR;
	
	
	//******CONSTRUCTOR******
	
	public BeastyWorld(PApplet ref) {
		if(!BeastyWorld.RUNNING) {
			this.REF = ref;
			
			this.checkInstance();
			this.setWindowsetup();
			//this.getSketchParameters();
			this.registermethods();
			this.checksystemseparator();
			this.BACKGROUNDCOLOR = this.REF.g.backgroundColor;
			
			//mouse handler is enabled by default
			this.registerHandler("mouse");
		}
		else {
			throw new RuntimeException("BeastyWorld cannot be instanced while runtime, try to instance before draw() method");
		}
		
	}
	
	
	//******CALLED FROM CONSTRUCTOR******
	
	//sets up the Window(Name, icon, etc.)
	private void setWindowsetup() {
		Info info = new Info(this.REF);
		this.REF.getSurface().setTitle("Beastybuttons Project Window");
		this.REF.getSurface().setIcon(info.icon());
	}
	
	//checks for separator
	private void checksystemseparator() {
		/*
		String system = System.getProperty("os.name");
		if(system.contains("Windows") || system.contains("windows")) {
			this.separator = "\"";
		}
		else if(system.contains("Linux") || system.contains("linux")) {
			this.separator = "/";
		}
		else if(system.contains("Mac") || system.contains("mac")) {
			this.separator = "/";
		}
		else {
			PApplet.println("WARNING(BB): COULD NOT RECOGNIZE SYSTEM - FILESYSTEM INTERACTION MIGHT NOT WORK");
		}*/
		
		this.separator = System.getProperty("file.separator");
	}
	
	//check the amount of instances of this class 
	private void checkInstance() {
		BeastyWorld.INSTANCES+=1;
		if(BeastyWorld.INSTANCES > 1) {
			throw new RuntimeException("There can only be one Instance of BeastyWorld");
		}
	}
	
	
	//registers methods for Processing PApplet
	private void registermethods() {
		try {
			this.REF.registerMethod("dispose", this);
			this.REF.registerMethod("draw", this);
			this.REF.registerMethod("mouseEvent", this);
			this.REF.registerMethod("keyEvent", this);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("could not register needed methods on PApplet of Processing");
		}
	}
	
	
	//checks if a surface was added
	private void checkforsurface() {
		if(this.surfaces.size() > 0) {
			throw new RuntimeException("Single Widgets and Surfaces cannot be added at the same time");
		}
	}
	
	//checks if a widget was added
	private void checkforwidget() {
		if(this.widgets.size() > 0) {
			throw new RuntimeException("Single Widgets and Surfaces cannot be added at the same time");
		}
	}
	
	
	
	//******SET METHODS******
	
	//register a handler for triggering events("mouse", "hotkeys", "tabsnap") - multiple can be registered(hotkey input and tab from virtualkeyboard also valid)
	public BeastyWorld registerHandler(String handler) {
		if(handler.equals("mouse")) {
			BeastyWorld.HANDLERS[0] = true;
		}
		else if(handler.equals("hotkeys")) {
			BeastyWorld.HANDLERS[1] = true;
		}
		else if(handler.equals("tabswitch")) {
			BeastyWorld.HANDLERS[2] = true;
		}
		else {
			PApplet.println("WARNING(BB): UNKNOWN HANDLER - COULD NOT REGISTER THE HANDLER");
		}
		return this;
	}
	
	
	//unregister a handler for triggering events("mouse", "hotkeys", "tabsnap") - multiple can be registered(hotkey input and tab from virtualkeyboard also valid)
	public BeastyWorld unregisterHandler(String handler) {
		if(handler.equals("mouse")) {
			BeastyWorld.HANDLERS[0] = false;
		}
		else if(handler.equals("hotkeys")) {
			BeastyWorld.HANDLERS[1] = false;
		}
		else if(handler.equals("tabswitch")) {
			BeastyWorld.HANDLERS[2] = false;
		}
		else {
			PApplet.println("WARNING(BB): UNKNOWN HANDLER - COULD NOT UNREGISTER THE HANDLER");
		}
		return this;
	}
	
	
	//set the window name
	public BeastyWorld setWindowName(String name) {
		try {
			this.REF.getSurface().setTitle(name);
		}
		catch(Exception e) {
			PApplet.println("WARNING(BEASTYBUTTONS): ERROR SETTING WINDOW TITLE -> CONTINUEING WITH OLD TITLE");
		}
		return this;
	}
	
	//set the window icon
	public BeastyWorld setWindowIcon(PImage icon) {
		try {
			this.REF.getSurface().setIcon(icon);
		}
		catch(Exception e) {
			PApplet.println("WARNING(BEASTYBUTTONS): ERROR SETTING WINDOW ICON -> CONTINUEING WITH OLD ICON");
		}
		return this;
	}
	
	
	//set the render font
	/*
	@Experimental
	public BeastyWorld setFont(PFont f) {
		PApplet.println("WARNING(BB): setFont() IS STILL HIHGLY EXPERIMENTAL, AVOID USE IF POSSIBLE");
		this.font = f;
		return this;
	}
	*/
	
	//the background that is drawn at the end of Processing IDEs draw and before drawing BB Graphics
	public void setBackgroundColor(int c) {
		this.BACKGROUNDCOLOR = c;
	}
	
	//use this to disable background update by BB.
	//disadvantage -> background has to be called from inside processing IDE in draw() now, otherwise no screen update
	//advantage -> you can draw own elements to the screen without being overdrawn by BB, adding to the BB GUI and animating with the GUI etc.
	//the registermethod("pre", this) is not used to give more freedom to the user
	public void disableBackground(boolean disable) {
		this.disable_background = disable;
	}
	
	
	
	//******ADD METHODS******
	
	public BeastyWorld addWidget(Widget w) {
		if(!w.already_added) {
			if(!w.ID.equals(this.ID)) {
				this.widgets.add(w);
				this.checkforsurface();
				w.already_added = true;
				this.MODE = WidgetMode.WIDGET;
				//edit the sourcePath
				w.editSourcepath("add", this.ID);
				this.tabswitchlist.clear();
				this.generate_tabswitchlist();
			}
			else {
				PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID AS THE BEASTYWORLD OBJECT -> CONTINUEING WITHOUT ADDING");
			}
		}
		else {
			PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
		}
		return this;
	}
	
	
	public BeastyWorld addSurface(BeastySurface s) {
		if(!s.already_added) {
			if(!s.ID.equals(this.ID)) {
				this.surfaces.add(s);
				this.checkforwidget();
				s.already_added = true;
				this.MODE = WidgetMode.SURFACE;
				this.renderpage = s;
				
				//edit the sourcePath
				s.editSourcePath("add", this.ID);
				//s.tabswitchlist.clear();
				//s.generate_tabswitchlist();
			}
			else {
				PApplet.println("WARNING(BB): SURFACES CANNOT HAVE THE SAME ID AS THE BEASTYWORLD OBJECT -> CONTINUEING WITHOUT ADDING");
			}
		}
		else {
			PApplet.println("WARNING(BB): SURFACES CANNOT BE ADDED MULTIPLE TIMES -> CONTINUEING WITHOUT ADDING");
		}
		return this;
	}
	
	
	@Experimental
	public BeastyWorld removeWidget(Widget w) {
		if(this.MODE == WidgetMode.WIDGET) {
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
		}
		else {
			PApplet.println("WARNING(BB): COULD NOT REMOVE WIDGET BECAUSE THE BEASTYBUTTONS IS CURRENTLY IN SURFACEMODE");
		}
		return this;
	}
	
	@Experimental
	public BeastyWorld removeSurface(BeastySurface s) {
		if(this.MODE == WidgetMode.SURFACE) {
			if(this.surfaces.size() > 0) {
				this.surfaces.remove(s);
			}
			else {
				PApplet.println("WARNING(BB): NO SURFACE FOUND TO REMOVE");
			}
		}
		else {
			PApplet.println("WARNING(BB): COULD NOT REMOVE SURFACE BECAUSE THE BEASTYBUTTONS IS CURRENTLY IN WIDGETMODE");
		}
		return this;
	}
	
	
	@Experimental
	public BeastyWorld enableonLayer(int layer) {
		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
			throw new RuntimeException("given layer is out of bounds");
		}
		if(this.MODE == WidgetMode.WIDGET) {
			if(this.widgets.size() > 0) {
				for(Widget w : this.widgets) {
					if(w.LAYER == layer) {
						w.active = true;
					}
				}
			}
		}
		else if(this.MODE == WidgetMode.SURFACE) {
			PApplet.println("WARNING(BB): WHEN USING SURFACES YOU HAVE TO USE THE enableonlayer() METHOD ON SURFACE LEVEL -> CONTINUEING WITHOUT ENABLING");
		}
		
		return this;
	}
	
	
	@Experimental
	public BeastyWorld disableonLayer(int layer) {
		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
			throw new RuntimeException("given layer is out of bounds");
		}
		if(this.MODE == WidgetMode.WIDGET) {
			if(this.widgets.size() > 0) {
				for(Widget w : this.widgets) {
					if(w.LAYER == layer) {
						w.active = false;
					}
				}
			}
		}
		else if(this.MODE == WidgetMode.SURFACE) {
			PApplet.println("WARNING(BB): WHEN USING SURFACES YOU HAVE TO USE THE disableonLayer() METHOD ON SURFACE LEVEL -> CONTINUEING WITHOUT DISABLING");
		}
		
		return this;
	}
	
	
	//******GET METHODS******
	/*
	public Widget get_Widget_by_path(String path) {
		
		//located in a surface instance?("worldID/surfaceID/widgetID")
		if(path.split("/").length == 3){
			for(BeastySurface s : this.surfaces) {
				for(Widget w : s.widgets) {
					if(w.SOURCEPATH.equals(path)) {
						if(w instanceof Button) {
							return (Button)w;
						}
						else if(w instanceof Checkbox) {
							return (Checkbox)w;
						}
						//else if(w instanceof Dropdownlist) {
							//return (Dropdownlist)w;
						}
						//else if(w instanceof Coordinatesystem) {
							//return (Coordinatesystem)w;
						}
						else if(w instanceof BB_Image) {
							return (BB_Image)w;
						}
						else if(w instanceof Inputfield) {
							return (Inputfield)w;
						}
						else if(w instanceof Label) {
							return (Label)w;
						}
						else if(w instanceof Radiobutton) {
							return (Radiobutton)w;
						}
						else if(w instanceof Slider) {
							return (Slider)w;
						}
						else if(w instanceof BB_Table) {
							return (BB_Table)w;
						}
						else {
							throw new RuntimeException("Could not cast the Widget to a specific type");
						}
					}
				}
				throw new RuntimeException("No match for the given Path");
			}
		}
		else if(path.split("/").length == 2){
			for(Widget w : this.widgets) {
				if(w.SOURCEPATH.equals(path)) {
					if(w instanceof Button) {
						return (Button)w;
					}
					else if(w instanceof Checkbox) {
						return (Checkbox)w;
					}
					else if(w instanceof Dropdownlist) {
						return (Dropdownlist)w;
					}
					else if(w instanceof Coordinatesystem) {
						return (Coordinatesystem)w;
					}
					else if(w instanceof BB_Image) {
						return (BB_Image)w;
					}
					else if(w instanceof Inputfield) {
						return (Inputfield)w;
					}
					else if(w instanceof Label) {
						return (Label)w;
					}
					else if(w instanceof Radiobutton) {
						return (Radiobutton)w;
					}
					else if(w instanceof Slider){
						return (Slider)w;
					}
					else if(w instanceof BB_Table) {
						return (BB_Table)w;
					}
					else {
						throw new RuntimeException("Could not cast the Widget to a specific type");
					}
				}
			}
			throw new RuntimeException("No match for the given Path");
		}
		
		@Experimental
		public Widget get_widget_by_id(String id){
			for(Widget w : this.widgets) {
				if(w.ID.equals(id)) {
					if(w instanceof Button) {
						return (Button)w;
					}
					else if(w instanceof Checkbox) {
						return (Checkbox)w;
					}
					else if(w instanceof Dropdownlist) {
						return (Dropdownlist)w;
					}
					else if(w instanceof Coordinatesystem) {
						return (Coordinatesystem)w;
					}
					else if(w instanceof BB_Image) {
						return (BB_Image)w;
					}
					else if(w instanceof Inputfield) {
						return (Inputfield)w;
					}
					else if(w instanceof Label) {
						return (Label)w;
					}
					else if(w instanceof Radiobutton) {
						return (Radiobutton)w;
					}
					else if(w instanceof Slider){
						return (Slider)w;
					}
					else if(w instanceof BB_Table) {
						return (BB_Table)w;
					}
					else {
						throw new RuntimeException("Could not cast the Widget to a specific type");
					}
				}
			}
			for(BeastySurface s : this.surfaces) {
				for(Widget w : s.widgets) {
					if(w instanceof Button) {
						return (Button)w;
					}
					else if(w instanceof Checkbox) {
						return (Checkbox)w;
					}
					else if(w instanceof Dropdownlist) {
						return (Dropdownlist)w;
					}
					else if(w instanceof Coordinatesystem) {
						return (Coordinatesystem)w;
					}
					else if(w instanceof BB_Image) {
						return (BB_Image)w;
					}
					else if(w instanceof Inputfield) {
						return (Inputfield)w;
					}
					else if(w instanceof Label) {
						return (Label)w;
					}
					else if(w instanceof Radiobutton) {
						return (Radiobutton)w;
					}
					else if(w instanceof Slider){
						return (Slider)w;
					}
					else if(w instanceof BB_Table) {
						return (BB_Table)w;
					}
					else {
						throw new RuntimeException("Could not cast the Widget to a specific type");
					}
				}
			}
			throw new RuntimeException("No match for the given ID");
		}
	}
	
	
	
	@Experimental
	public BeastyWorld get_surface_by_path(String path){
		return this;
	}
	
	@Experimental
	public BeastyWorld get_surface_by_id(String id){
		return this;
	}
	
	*/
	
	public ArrayList<Widget> getWidgetList(){
		return this.widgets;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public String getSourcepath() {
		return this.SOURCEPATH;
	}
	
	
	
	
	//******LOGGING METHODS******
	
	//enables logging(logfilepath needs to be a indirect path, cannot be direct)
	public BeastyWorld enableLogging(int loglevel, String logfilepath) {
		PApplet.println("WARNING(BB): LOGGING CAN SLOW DOWN FPS PERFORMANCE ON SLOW HARDWARE");
		if(!this.logging) {
			if(loglevel == 0 || loglevel == 1) {
				if(logfilepath.endsWith(".txt")) {
					this.logging = true;
					this.loglevel = loglevel;
					this.logfilepath = this.REF.sketchPath() + this.separator + logfilepath;
					
					LocalDate date = LocalDate.now();
					LocalTime time = LocalTime.now();
					
					this.loglist.add("****** LOGGING STARTED - " + date + " " + time + " ******");
					this.loglist.add("");
					this.loglist.add("");
				}
				else {
					PApplet.println("WARNING(BB): UNKNOWN LOGFILETYPE, MUST BE .txt -> CONTINUEING WITHOUT LOGGING");
				}
			}
			else{
				PApplet.println("WARNING(BB): UNKNOWN LOGLEVEL -> CONTINUEING WITHOUT LOGGING");
			}
		}
		else {
			PApplet.println("WARNING(BB): LOGGING WAS ALREADY ENABLED BEFORE CALLING");
		}
		return this;
	}
	
	
	//disables logging manually(otherwise it is auto disabled on window close)
	public BeastyWorld disableLogging() {
		if(this.logging) {
			this.logging = false;
			
			//get the Date and Time
			LocalDate date = LocalDate.now();
			LocalTime time = LocalTime.now();
			
			this.loglist.add("****** LOGGING FINISHED - " + date + " " + time + " ******");
			this.loglist.add("");
			this.loglist.add("");
			
			//save the file in another Thread
			@SuppressWarnings("unused")
			Logthread logthread = new Logthread(this.logfilepath, this.loglist);
		}
		return this;
	}
	
	
	//called to make a new log entry
	private void writelogline(String id, String classtype, String action) {
		
		if(this.loglevel == 0) {
			this.loglist.add(id + " -> " + action);
		}
		else if(this.loglevel == 1) {
			LocalTime time = LocalTime.now();
			LocalDate date = LocalDate.now();
			
			this.loglist.add(date + " - " + time + " -> " + id + "[" + classtype + "] -> " + action);
		}
	}
	
	
	
	//******IMPORT - EXPORT METHODS******
	
	//exports a .beasty file stored in JSON format
	@NotImplementedYet
	@Experimental
	public void exportLayout(String path, String alignment) {
		try {
			if(alignment.equals("fixed")) {
				//store position in fixed values
			}
			else if(alignment.equals("relative")) {
				//store position values in relative to window size
			}
		}
		catch(Exception e){
			PApplet.println("WARNING(BB): COULD NOT EXPORT LAYOUT -> CONTINUEING WITHOUT EXPORT");
		}
	}
	
	
	//imports a .beasty file stored in JSON format
	@NotImplementedYet
	@Experimental
	public void importLayout(String path) {
		try {
			if(path.split(".")[1].equals("beasty")) {
				if(this.widgets.size() > 0) {
					this.widgets.clear();
				}
				if(this.surfaces.size() > 0) {
					this.surfaces.clear();
				}
				
				//import all of the Layout
				//here
				
				
			}
			else {
				throw new RuntimeException("unknown filetype to import, can only import .beasty files");
			}
		}
		catch (Exception e) {
			throw new RuntimeException("an error occured while importing");
		}
	}
	
	
	
	//******TRANSITION METHODS******
	
	//jump cut to another surface
	public void surface_jump_transition(String targetsurfaceID) {
		if(this.MODE == WidgetMode.SURFACE) {
			boolean found = false;
			for(BeastySurface surface : this.surfaces) {
				if(surface.ID.equals(targetsurfaceID)) {
					this.renderpage = surface;
					found = true;
				}
			}
			if(!found) {
				PApplet.println("WARNING(BB): NO ID MATCHING SURFACE FOUND -> COULD NOT CHANGE SURFACE");
			}
		}
		else {
			throw new RuntimeException("transitions are only available through surfaces");
		}
	}
	
	
	//opacity cut to another surface
	@NotImplementedYet
	@Experimental
	public void surface_opacity_transition(String targetsurfaceID, float operatingtime) {
		//test if surface mode

        //set isanimating = true

        //disable all widgets

        //disable all tooltips

        //do transition in time

        //enable all widgets

        //set isanimating = false
	}
	
	
	//swipe cut to another surface
	@NotImplementedYet
	@Experimental
	public void surface_swipe_transition(String targetsurfaceID, float operatingtime, String direction){
		//test if surface mode

        //set isanimating = true

        //disable all widgets

        //disable all tooltips

        //do transition in time

        //enable all widgets

        //set isanimating = false
	}
	
	
	
	//******RENDER METHODS******
	
	//renders all widgets from render methods
	private void render() {
		//get all values to be stored aside
        this.beforetextsize = this.REF.g.textSize;
        this.beforestrokeweight = this.REF.g.strokeWeight;
        this.beforecolor = this.REF.g.fillColor;
        //this.beforefont = this.REF.g.textFont;
        this.beforecolormode = this.REF.g.colorMode;
        this.beforeimagemode = this.REF.g.imageMode;
        this.beforeellipsemode = this.REF.g.ellipseMode;
        this.beforerectmode = this.REF.g.rectMode;
        this.beforetextalign = this.REF.g.textAlign;
        this.beforestroke = this.REF.g.stroke;
        this.beforestrokecolor = this.REF.g.strokeColor;
        
        
        //override values for rendering
        if(!this.disable_background) {
        	this.REF.background(this.BACKGROUNDCOLOR);
        }
        this.REF.noStroke();
        this.REF.colorMode(PConstants.ARGB, 255, 255, 255, 255);
        this.REF.rectMode(PConstants.CENTER);
        this.REF.imageMode(PConstants.CENTER);
        //this.REF.textFont(this.font);
        this.REF.ellipseMode(PConstants.CENTER);
        this.REF.textAlign(PConstants.CENTER);
        
        
        if(this.MODE == WidgetMode.WIDGET) {
        	for(int i = 0; i <= BeastyWorld.LAYERS; i++) {
        		if(this.widgets.size() > 0) {
        			for(Widget w : this.widgets) {
        				if(w.LAYER == i) {
        					w.render();
        				}
        			}
        		}
        	}
        }
        else if(this.MODE == WidgetMode.SURFACE) {
        	this.renderpage.render();
        	
        	for(int i = 0; i <= BeastyWorld.LAYERS; i++) {
        		if(this.renderpage.widgets.size() > 0) {
        			for(Widget w : this.renderpage.widgets) {
        				if(w.LAYER == i) {
        					w.render();
        				}
        			}
        		}
        	}
        	
        	if(this.animateToPage != null) {
        		
        		//render the other page too
        		this.animateToPage.render();
        		
        		for(int i = 0; i <= BeastyWorld.LAYERS; i++) {
        			if(this.animateToPage.widgets.size() > 0) {
        				for(Widget w : this.animateToPage.widgets) {
        					if(w.LAYER == i) {
        						w.render();
        					}
        				}
        			}
        		}
        	}
        }
	}
	
	
	
	//renders all the tooltips from render methods
	private void rendertooltip() {
		
		if(this.MODE == WidgetMode.WIDGET) {
			for(int i = 0; i <= BeastyWorld.LAYERS; i++) {
				if(this.widgets.size() > 0) {
					for(Widget w : this.widgets) {
						if(w.LAYER == i) {
							w.rendertooltip();
						}
					}
				}
			}
		}
		else if(this.MODE == WidgetMode.SURFACE) {
			for(int i = 0; i <= BeastyWorld.LAYERS; i++) {
				if(this.renderpage.widgets.size() > 0) {
					for(Widget w : this.renderpage.widgets) {
						if(w.LAYER == i) {
							w.rendertooltip();
						}
					}
				}
			}
		}
		
		//set everything back to sketch settings
        if(this.beforestroke){
            this.REF.stroke(this.beforestrokecolor);
        } else{
            this.REF.noStroke();
        }
        
        this.REF.textSize(this.beforetextsize);
        this.REF.rectMode(this.beforerectmode);
        this.REF.ellipseMode(this.beforeellipsemode);
        this.REF.imageMode(this.beforeimagemode);
        this.REF.colorMode(this.beforecolormode);
        this.REF.strokeWeight(this.beforestrokeweight);
        this.REF.textAlign(this.beforetextalign);
        this.REF.fill(this.beforecolor);
        //this.REF.textFont(this.beforefont);
	}
	
	//generates a new timeStep when mouse over element -> for animating tooltip disappearance
	private void reset_timeStep_in(Widget w) {
		if(this.new_timestep) {
			this.new_timestep = false;
			
			w.timeStep = System.currentTimeMillis();
		}
	}
	
	//does the same the other way around
	private void reset_timeStep_out(Widget w) {
		if(!this.new_timestep) {
			
			this.new_timestep = true;
		}
	}
	
	//******PROCESSING METHODS******
	
	//called on Processing exit
	public void dispose() {
		if(this.logging) {
			
			this.logging = false;
			
			LocalTime time = LocalTime.now();
			LocalDate date = LocalDate.now();
			
			this.loglist.add("****** LOGGING FINISHED - " + date + " " + time + " ******");
			this.loglist.add("");
			this.loglist.add("");
			
			//save the file in another Thread
			@SuppressWarnings("unused")
			Logthread logthread = new Logthread(this.logfilepath, this.loglist);
		}
	}
	
	//main draw in Processing
	public void draw() {
		//set running true in first iteration of draw
		if(!this.runtime_set) {
			BeastyWorld.RUNNING = true;
			this.runtime_set = true;
		}
		this.render();
		this.rendertooltip();
	}
	
	//mouse events from Processing
	public void mouseEvent(MouseEvent e) {
		if(!BeastyWorld.ISANIMATING) {
			if(BeastyWorld.HANDLERS[0]) {
				
				//check and update over color
				if(this.MODE == WidgetMode.WIDGET) {
					for(Widget w : this.widgets) {
						if(w instanceof Button || w instanceof Checkbox || w instanceof Inputfield || w instanceof BB_Image) {
							if(w.over(e.getX(), e.getY())) {
								if(w.active) {
									w.rendercolor = w.overcolor;
									this.reset_timeStep_in(w);
								}
							}
							else {
								if(w.active) {
									w.rendercolor = w.background;
									this.reset_timeStep_out(w);
								}
							}
						}
					}
				}
				else if(this.MODE == WidgetMode.SURFACE) {
					for(Widget w : this.renderpage.widgets) {
						if(w instanceof Button || w instanceof Checkbox || w instanceof Inputfield || w instanceof BB_Image) {
							if(w.over(e.getX(), e.getY())) {
								if(w.active) {
									w.rendercolor = w.overcolor;
									this.reset_timeStep_in(w);
								}
							}
							else {
								if(w.active) {
									w.rendercolor = w.background;
									this.reset_timeStep_out(w);
								}
							}
						}
					}
				}
				
				//check and update click color
				if(e.getAction() == MouseEvent.PRESS) {
					if(this.MODE == WidgetMode.WIDGET) {
						for(Widget w : this.widgets) {
							if(w instanceof Button || w instanceof Checkbox) {
								if(w.over(e.getX(), e.getY())) {
									if(w.active) {
										w.rendercolor = w.clickcolor;
										//w.trueClick = true;
									}
								}
								else {
									if(w.active) {
										w.rendercolor = w.background;
										//w.trueClick = false;
									}
								}
							}
						}
					}
					else if(this.MODE == WidgetMode.SURFACE) {
						for(Widget w : this.renderpage.widgets) {
							if(w instanceof Button || w instanceof Checkbox) {
								if(w.over(e.getX(), e.getY())) {
									if(w.active) {
										w.rendercolor = w.clickcolor;
										//w.trueClick = true;
									}
								}
								else {
									if(w.active) {
										w.rendercolor = w.background;
										//w.trueClick = false;
									}
								}
							}
						}
					}
				}
				
				//check and update drag events
				else if(e.getAction() == MouseEvent.DRAG) {
					if(this.MODE == WidgetMode.WIDGET) {
						for(Widget w : this.widgets) {
							if(w instanceof Button || w instanceof Checkbox) {
								if(w.over(e.getX(), e.getY())) {
									if(w.active) {
										w.rendercolor = w.clickcolor;
										//w.trueClick = true;
									}
								}
								else {
									if(w.active) {
										w.rendercolor = w.background;
										//w.trueClick = false;
									}
								}
							}
						}
					}
					else if(this.MODE == WidgetMode.SURFACE) {
						for(Widget w : this.renderpage.widgets) {
							if(w instanceof Button || w instanceof Checkbox) {
								if(w.over(e.getX(), e.getY())) {
									if(w.active) {
										w.rendercolor = w.clickcolor;
										//w.trueClick = true;
									}
								}
								else {
									if(w.active) {
										w.rendercolor = w.background;
										//w.trueClick = false;
									}
								}
							}
						}
					}
				}
				
				//check and update onclick events
				else if(e.getAction() == MouseEvent.RELEASE) {
					if(this.MODE == WidgetMode.WIDGET) {
						for(Widget w : this.widgets) {
							if(w.over(e.getX(), e.getY())) {
								if(w.active) {
									
									//left button
									if(e.getButton() == PConstants.LEFT) {	
										if(w instanceof Button) {
											Button b = (Button)w;
											b.clicks_since_start+=1;
											if(b.olc != null) {
												this.REF.method(b.olc);
												//PApplet.println("Widget one");
											}
										}
										else if(w instanceof Checkbox) {
											Checkbox c = (Checkbox)w;
											c.clicks_since_start+=1;
											if(c.state) {
												c.state = !c.state;
												if(c.ou != null) {
													this.REF.method(c.ou);
												}
											}
											else {
												c.state = !c.state;
												if(c.oc != null) {
													this.REF.method(c.oc);
												}
											}
										}
										else if(w instanceof Inputfield) {
											Inputfield i = (Inputfield)w;
											i.clicks_since_start++;
											i.input = true;
										}
									}
									
									//middle button
									else if(e.getButton() == PConstants.CENTER) {
										if(w instanceof Button) {
											Button b = (Button)w;
											b.clicks_since_start+=1;
											if(b.omc != null) {
												this.REF.method(b.omc);
											}
										}
										else if(w instanceof Checkbox) {
											Checkbox c = (Checkbox)w;
											c.clicks_since_start+=1;
											if(c.state) {
												c.state = !c.state;
												if(c.ou != null) {
													this.REF.method(c.ou);
												}
											}
											else {
												c.state = !c.state;
												if(c.oc != null) {
													this.REF.method(c.oc);
												}
											}	
										}
										else if(w instanceof Inputfield) {
											Inputfield i = (Inputfield)w;
											i.clicks_since_start++;
											i.input = true;
										}
									}
									
									//right button
									else if(e.getButton() == PConstants.RIGHT) {
										if(w instanceof Button) {
											Button b = (Button)w;
											b.clicks_since_start+=1;
											if(b.orc != null) {
												this.REF.method(b.orc);
											}
										}
										else if (w instanceof Checkbox) {
											Checkbox c = (Checkbox)w;
											c.clicks_since_start+=1;
											if(c.state) {
												c.state = !c.state;
												if(c.ou != null) {
													this.REF.method(c.ou);
												}
											}
											else {
												c.state = !c.state;
												if(c.oc != null) {
													this.REF.method(c.oc);
												}
											}
										}
										else if(w instanceof Inputfield) {
											Inputfield i = (Inputfield)w;
											i.clicks_since_start++;
											i.input = true;
										}
									}
								}
							}
							else {
								if(w instanceof Inputfield) {
									if(w.active) {
										Inputfield i = (Inputfield)w;
										i.input = false;
									}
								}
							}
						}
					}
					else if(this.MODE == WidgetMode.SURFACE) {
						for(Widget w : this.renderpage.widgets) {
							if(w.over(e.getX(), e.getY())) {
								if(w.active) {
								
									//left button
									if(e.getButton() == PConstants.LEFT) {	
										if(w instanceof Button) {
											Button b = (Button)w;
											b.clicks_since_start+=1;
											if(b.olc != null) {
												this.REF.method(b.olc);
												//PApplet.println("Surface one");
											}
										}
										else if(w instanceof Checkbox) {
											Checkbox c = (Checkbox)w;
											c.clicks_since_start+=1;
											if(c.state) {
												c.state = !c.state;
												if(c.ou != null) {
													this.REF.method(c.ou);
												}
											}
											else {
												c.state = !c.state;
												if(c.oc != null) {
													this.REF.method(c.oc);
												}
											}
										}
										else if(w instanceof Inputfield) {
											Inputfield i = (Inputfield)w;
											i.clicks_since_start++;
											i.input = true;
										}
									}
									
									//middle button
									else if(e.getButton() == PConstants.CENTER) {
										if(w instanceof Button) {
											Button b = (Button)w;
											b.clicks_since_start+=1;
											if(b.omc != null) {
												this.REF.method(b.omc);
											}
										}
										else if(w instanceof Checkbox) {
											Checkbox c = (Checkbox)w;
											c.clicks_since_start+=1;
											if(c.state) {
												c.state = !c.state;
												if(c.ou != null) {
													this.REF.method(c.ou);
												}
											}
											else {
												c.state = !c.state;
												if(c.oc != null) {
													this.REF.method(c.oc);
												}
											}	
										}
										else if(w instanceof Inputfield) {
											Inputfield i = (Inputfield)w;
											i.clicks_since_start++;
											i.input = true;
										}
									}
									
									//right button
									else if(e.getButton() == PConstants.RIGHT) {
										if(w instanceof Button) {
											Button b = (Button)w;
											b.clicks_since_start+=1;
											if(b.orc != null) {
												this.REF.method(b.orc);
											}
										}
										else if (w instanceof Checkbox) {
											Checkbox c = (Checkbox)w;
											c.clicks_since_start+=1;
											if(c.state) {
												c.state = !c.state;
												if(c.ou != null) {
													this.REF.method(c.ou);
												}
											}
											else {
												c.state = !c.state;
												if(c.oc != null) {
													this.REF.method(c.oc);
												}
											}
										}
										else if(w instanceof Inputfield) {
											Inputfield i = (Inputfield)w;
											i.clicks_since_start++;
											i.input = true;
										}
									}
								}
							}
							else {
								if(w instanceof Inputfield) {
									if(w.active) {
										Inputfield i = (Inputfield)w;
										i.input = false;
									}
								}
							}
						}
					}
					if(this.tabswitch_active) {
						for(Widget w : this.widgets) {
							w.rendercolor = w.background;
						}
						this.tabswitch_active = false;
					}
				}
			}
			//this.tabswitch_displaying = false;
		}
	}
	
	//key events from Processing
	public void keyEvent(KeyEvent e) {
		if(!BeastyWorld.ISANIMATING) {
			
			//handle Inputfield input as separate, because it is special from the other widgets
			if(e.getAction() == KeyEvent.PRESS) {
				if(this.MODE == WidgetMode.WIDGET) {
					for(Widget w : this.widgets) {
						if(w instanceof Inputfield) {
							if(w.active) {
								Inputfield i = (Inputfield)w;
								if(i.input) {
									i.writeinput(e);
								}
								else {
									if(BeastyWorld.HANDLERS[1]) {
										if(e.getKeyCode() == i.hotkey){
											i.input = true;
										}
									}
								}
							}
						}
					}
				}
				else if(this.MODE == WidgetMode.SURFACE) {
					for(Widget w : this.renderpage.widgets) {
						if(w instanceof Inputfield) {
							if(w.active) {
								Inputfield i = (Inputfield)w;
								if(i.input) {
									i.writeinput(e);
								}
								else {
									if(BeastyWorld.HANDLERS[1]) {
										if(e.getKeyCode() == i.hotkey){
											i.input = true;
										}
									}
								}
							}
						}
					}
				}
			}
			
			
			
			//Hotkeys
			if(BeastyWorld.HANDLERS[1]) {
				if(this.MODE == WidgetMode.WIDGET) {
					if(e.getAction() == KeyEvent.RELEASE) {
						for(Widget w : this.widgets) {
							if(w.active) {
								if(w instanceof Button) {
									Button b = (Button)w;
									if(e.getKeyCode() == b.hotkey) {
										//b.clicks_since_start+=1;
										if(b.olc != null) {
											this.REF.method(b.olc);
										}
									}
								}
								if(w instanceof Checkbox) {
									Checkbox c = (Checkbox)w;
									if(e.getKeyCode() == c.hotkey) {
										//c.clicks_since_start+=1;
										if(c.state) {
											c.state = !c.state;
											if(c.ou != null) {
												this.REF.method(c.ou);
											}
										}
										else {
											c.state = !c.state;
											if(c.oc != null) {
												this.REF.method(c.oc);
											}
										}
									}
								}
							}
						}
						if(e.getKeyCode() != PApplet.TAB || e.getKeyCode() != PApplet.ENTER) {
							if(this.tabswitch_active) {
								for(Widget w : this.widgets) {
									w.rendercolor = w.background;
								}
								this.tabswitch_active = false;
							}
						}
					}
				}
				else if(this.MODE == WidgetMode.SURFACE) {
					if(e.getAction() == KeyEvent.RELEASE) {
						for(Widget w : this.renderpage.widgets) {
							if(w.active) {
								if(w instanceof Button) {
									Button b = (Button)w;
									if(e.getKeyCode() == b.hotkey) {
										//b.clicks_since_start+=1;
										if(b.olc != null) {
											this.REF.method(b.olc);
										}
									}
								}
								if(w instanceof Checkbox) {
									Checkbox c = (Checkbox)w;
									if(e.getKeyCode() == c.hotkey) {
										//c.clicks_since_start+=1;
										if(c.state) {
											c.state = !c.state;
											if(c.ou != null) {
												this.REF.method(c.ou);
											}
										}
										else {
											c.state = !c.state;
											if(c.oc != null) {
												this.REF.method(c.oc);
											}
										}
									}
								}
							}
						}
						if(e.getKeyCode() != PApplet.TAB || e.getKeyCode() != PApplet.ENTER) {
							if(this.tabswitch_active) {
								for(Widget w : this.widgets) {
									w.rendercolor = w.background;
								}
								this.tabswitch_active = false;
							}
						}
					}
				}
			}
			
			//Tabswitch
			if(BeastyWorld.HANDLERS[2]) {
				if(this.MODE == WidgetMode.WIDGET) {
					if(e.getAction() == KeyEvent.RELEASE) {
						if(e.getKeyCode() == PApplet.TAB) {
							this.tabswitch_active = true;
							for(Widget w : this.widgets) {
								if(w instanceof Inputfield) {
									Inputfield i = (Inputfield)w;
									i.input = false;
								}
							}
							if(this.tabswitchindex+1 < this.tabswitchlist.size()) {
								this.tabswitchlist.get(this.tabswitchindex).tab_selected = false;
								this.tabswitchlist.get(this.tabswitchindex).rendercolor = this.tabswitchlist.get(this.tabswitchindex).background;
								this.tabswitchindex+=1;
								
								this.tabswitchlist.get(this.tabswitchindex).tab_selected = true;
								
								//?
								if(this.tabswitchlist.get(this.tabswitchindex).active) {
									if(this.tabswitchlist.get(this.tabswitchindex) instanceof Button || this.tabswitchlist.get(this.tabswitchindex) instanceof Checkbox) {
										this.tabswitchlist.get(this.tabswitchindex).rendercolor = this.tabswitchlist.get(this.tabswitchindex).overcolor;
									}
									else if(this.tabswitchlist.get(this.tabswitchindex) instanceof Inputfield) {
										Inputfield i = (Inputfield)this.tabswitchlist.get(this.tabswitchindex);
										i.rendercolor = i.input_color;
										i.input = true;
									}
								}
								
							}
							else {
								this.tabswitchlist.get(this.tabswitchindex).tab_selected = false;
								this.tabswitchlist.get(this.tabswitchindex).rendercolor = this.tabswitchlist.get(this.tabswitchindex).background;
								this.tabswitchindex = 0;
								
								this.tabswitchlist.get(this.tabswitchindex).tab_selected = true;
								
								
								//?
								if(this.tabswitchlist.get(this.tabswitchindex).active) {
									if(this.tabswitchlist.get(this.tabswitchindex) instanceof Button || this.tabswitchlist.get(this.tabswitchindex) instanceof Checkbox) {
										this.tabswitchlist.get(this.tabswitchindex).rendercolor = this.tabswitchlist.get(this.tabswitchindex).overcolor;
									}
									else if(this.tabswitchlist.get(this.tabswitchindex) instanceof Inputfield) {
										Inputfield i = (Inputfield)this.tabswitchlist.get(this.tabswitchindex);
										i.rendercolor = i.input_color;
										i.input = true;
									}
								}
							}
						}
						else if(e.getKeyCode() == PApplet.ENTER) {
							if(this.tabswitch_active) {
							
								for(Widget w : this.widgets) {
									if(w.active) {
										if(w instanceof Button) {
											if(w.tab_selected) {
												Button b = (Button)w;
												//b.clicks_since_start++;
												if(b.olc != null) {
													this.REF.method(b.olc);
												}
											}
										}
										else if(w instanceof Checkbox) {
											if(w.tab_selected) {
												Checkbox c = (Checkbox)w;
												//b.clicks_since_start++;
												if(c.state) {
													c.state = !c.state;
													if(c.ou != null) {
														this.REF.method(c.ou);
													}
												}
												else {
													c.state = !c.state;
													if(c.oc != null) {
														this.REF.method(c.oc);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				else if(this.MODE == WidgetMode.SURFACE) {
					if(e.getAction() == KeyEvent.RELEASE) {
						if(e.getKeyCode() == PApplet.TAB) {
							this.tabswitch_active = true;
							PApplet.println("surfaceindex: " + this.renderpage.tabswitchindex);
							PApplet.println("worldindex: " + this.tabswitchindex);
							for(Widget w : this.renderpage.widgets) {
								if(w instanceof Inputfield) {
									Inputfield i = (Inputfield)w;
									i.input = false;
								}
							}
							if(this.renderpage.tabswitchindex+1 < this.renderpage.tabswitchlist.size()) {
								this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).tab_selected = false;
								this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).rendercolor = this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).background;
								this.renderpage.tabswitchindex+=1;
								
								this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).tab_selected = true;
								
								//?
								if(this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).active) {
									if(this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex) instanceof Button || this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex) instanceof Checkbox) {
										this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).rendercolor = this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).overcolor;
									}
									else if(this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex) instanceof Inputfield) {
										Inputfield i = (Inputfield)this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex);
										i.rendercolor = i.input_color;
										i.input = true;
									}
								}
								
							}
							else {
								this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).tab_selected = false;
								this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).rendercolor = this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).background;
								this.tabswitchindex = 0;
								
								this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).tab_selected = true;
								
								
								//?
								if(this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).active) {
									if(this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex) instanceof Button || this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex) instanceof Checkbox) {
										this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).rendercolor = this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex).overcolor;
									}
									else if(this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex) instanceof Inputfield) {
										Inputfield i = (Inputfield)this.renderpage.tabswitchlist.get(this.renderpage.tabswitchindex);
										i.rendercolor = i.input_color;
										i.input = true;
									}
								}
							}
						}
						else if(e.getKeyCode() == PApplet.ENTER) {
							if(this.tabswitch_active) {
							
								for(Widget w : this.renderpage.widgets) {
									if(w.active) {
										if(w instanceof Button) {
											if(w.tab_selected) {
												Button b = (Button)w;
												//b.clicks_since_start++;
												if(b.olc != null) {
													this.REF.method(b.olc);
												}
											}
										}
										else if(w instanceof Checkbox) {
											if(w.tab_selected) {
												Checkbox c = (Checkbox)w;
												//b.clicks_since_start++;
												if(c.state) {
													c.state = !c.state;
													if(c.ou != null) {
														this.REF.method(c.ou);
													}
												}
												else {
													c.state = !c.state;
													if(c.oc != null) {
														this.REF.method(c.oc);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
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
