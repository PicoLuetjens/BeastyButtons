package beastybuttons;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import beastybuttons.Checkbox.Checktype;
import processing.core.*;
import processing.data.XML;
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
	
	private String iconpath = "default";
	
	private String windowtitle = "default";
	
	//operating system
	private String operating_system = "none";
	
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
	private boolean logging = false;
	
	private int loglevel;
	
	//The Loglist to write to at runtime
	private ArrayList<String> loglist = new ArrayList<>();
	
	
	private String logfilepath = null, rel_path = "none";
	
	//handlers for interaction
	protected static boolean[] HANDLERS = {false, false, false};
	
	//all widgets
	private ArrayList<Widget> widgets = new ArrayList<>();
	
	//the index in the tabswitchlist
	protected int tabswitchindex = 0;
	
	//if the tabswitch handler is enabled/disabled by the user, otherwise it will be automatically disabled when interacting with other handlers
	protected boolean tabswitch_user_enabled = true;
	
	//cycle through with tab
	protected ArrayList<Widget> tabswitchlist = new ArrayList<>();
		
	//all surfaces
	private ArrayList<BeastySurface> surfaces = new ArrayList<>();
	
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
	
	
    private String fontpath = "default";
    
    private String ttfontpath = "default";
    
    // used to print out ids and user info when importing a layout
    private ArrayList<String>import_printouts = new ArrayList<>();
    
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
		
		String system = System.getProperty("os.name");
		if(system.contains("Windows") || system.contains("windows")) {
			this.operating_system = "Windows";
		}
		else if(system.contains("Linux") || system.contains("linux")) {
			this.operating_system = "Linux";
		}
		else if(system.contains("Mac") || system.contains("mac")) {
			this.operating_system = "Mac";
		}
		else {
			PApplet.println("WARNING(BB): COULD NOT RECOGNIZE SYSTEM - FILESYSTEM INTERACTION MIGHT NOT WORK");
			this.operating_system = "unknown";
		}
		
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
			this.tabswitch_user_enabled = true;
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
			this.tabswitch_user_enabled = false;
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
		this.windowtitle = name;
		return this;
	}
	
	//set the window icon(path must be relative)
	public BeastyWorld setWindowIcon(String iconpath) {
		try {
			this.iconpath = iconpath;
			PImage icon = this.REF.loadImage(iconpath);
			this.REF.getSurface().setIcon(icon);
		}
		catch(Exception e) {
			PApplet.println("WARNING(BEASTYBUTTONS): ERROR SETTING WINDOW ICON -> CONTINUEING WITH OLD ICON");
		}
		return this;
	}
	
	
	//set the render font
	@NoDocumentation
	@Experimental
	@NotImplementedYet
	public BeastyWorld setFont(String fontpath) {
		this.fontpath = fontpath;
		if(this.MODE == WidgetMode.WIDGET) {
			for(Widget w : this.widgets) {
				if(w instanceof Button) {
					Button b = (Button)w;
					b.setFont(fontpath);
				}
				else if(w instanceof Label) {
					Label l = (Label)w;
					l.setFont(fontpath);
				}
				else if(w instanceof Inputfield) {
					Inputfield i = (Inputfield)w;
					i.setFont(fontpath);
				}
			}
		}
		else if(this.MODE == WidgetMode.SURFACE) {
			for(BeastySurface s : this.surfaces) {
				for(Widget w : s.widgets) {
					if(w instanceof Button) {
						Button b = (Button)w;
						b.setFont(fontpath);
					}
					else if(w instanceof Label) {
						Label l = (Label)w;
						l.setFont(fontpath);
					}
					else if(w instanceof Inputfield) {
						Inputfield i = (Inputfield)w;
						i.setFont(fontpath);
					}
				}
			}
		}
		return this;
	}
	
	@NoDocumentation
	@Experimental
	@NotImplementedYet
	public BeastyWorld setTooltipFont(String fontpath) {
		this.ttfontpath = fontpath;
		if(this.MODE == WidgetMode.WIDGET) {
			for(Widget w : this.widgets) {
				if(w instanceof Button) {
					Button b = (Button)w;
					b.setTooltipFont(fontpath);
				}
				else if(w instanceof Checkbox) {
					Checkbox c = (Checkbox)w;
					c.setTooltipFont(fontpath);
				}
				else if(w instanceof Inputfield) {
					Inputfield i = (Inputfield)w;
					i.setFont(fontpath);
				}
				else if(w instanceof BB_Image) {
					BB_Image bi = (BB_Image)w;
					bi.setTooltipFont(fontpath);
				}
			}
		}
		else if(this.MODE == WidgetMode.SURFACE) {
			for(BeastySurface s : this.surfaces) {
				for(Widget w : s.widgets) {
					if(w instanceof Button) {
						Button b = (Button)w;
						b.setTooltipFont(fontpath);
					}
					else if(w instanceof Checkbox) {
						Checkbox c = (Checkbox)w;
						c.setTooltipFont(fontpath);
					}
					else if(w instanceof Inputfield) {
						Inputfield i = (Inputfield)w;
						i.setFont(fontpath);
					}
					else if(w instanceof BB_Image) {
						BB_Image bi = (BB_Image)w;
						bi.setTooltipFont(fontpath);
					}
				}
			}
		}
		return this;
	}
	
	//the background that is drawn at the end of Processing IDEs draw and before drawing BB Graphics
	public BeastyWorld setBackgroundColor(int c) {
		this.BACKGROUNDCOLOR = c;
		return this;
	}
	
	//use this to disable background update by BB.
	//disadvantage -> background has to be called from inside processing IDE in draw() now, otherwise no screen update
	//advantage -> you can draw own elements to the screen without being overdrawn by BB, adding to the BB GUI and animating with the GUI etc.
	//the registermethod("pre", this) is not used to give more freedom to the user
	public BeastyWorld disableBackground(boolean disable) {
		this.disable_background = disable;
		return this;
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
				for(Widget w : s.widgets) {
					w.editSourcepath("add", this.ID);
				}
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
	
	@NoDocumentation
	public BeastyWorld removeWidget(Widget w) {
		if(this.MODE == WidgetMode.WIDGET) {
			if(this.widgets.size() > 0) {
				this.widgets.remove(w);
				w.already_added = false;
				//edit the sourcePath
				w.editSourcepath("delete", this.ID);
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
	
	
	//when the renderpage is the same as the removed surface, the last surface in the list becomes the new renderpage
	@NoDocumentation
	public BeastyWorld removeSurface(BeastySurface s) {
		if(this.MODE == WidgetMode.SURFACE) {
			if(this.surfaces.size() > 0) {
				this.surfaces.remove(s);
				for(Widget w : s.widgets) {
					w.SOURCEPATH = w.SOURCEPATH.replace(this.ID + "/", "");
				}
				s.editSourcePath("delete", this.ID);
				if(this.surfaces.size() > 0) {
					this.renderpage = this.surfaces.get(this.surfaces.size()-1);
				}
				else {
					this.renderpage = null;
					throw new RuntimeException("there are no more surfaces to render after surface was removed, there has to be at least one in surface mode");
				}
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
	
	@NoDocumentation
	public BeastyWorld enableonLayer(int layer) {
		if(layer < 0 || layer > BeastyWorld.LAYERS) {
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
	
	@NoDocumentation
	public BeastyWorld disableonLayer(int layer) {
		if(layer < 0 || layer > BeastyWorld.LAYERS) {
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
	
	
	public Widget get_widget_by_path(String path) {	
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
						//}
						//else if(w instanceof Coordinatesystem) {
							//return (Coordinatesystem)w;
						//}
						else if(w instanceof BB_Image) {
							return (BB_Image)w;
						}
						else if(w instanceof Inputfield) {
							return (Inputfield)w;
						}
						else if(w instanceof Label) {
							return (Label)w;
						}
						//else if(w instanceof Radiobutton) {
							//return (Radiobutton)w;
						//}
						//else if(w instanceof Slider) {
							//return (Slider)w;
						//}
						//else if(w instanceof BB_Table) {
							//return (BB_Table)w;
						//}
					}
				}
				//throw new RuntimeException("No match for the given Path");
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
					//else if(w instanceof Dropdownlist) {
						//return (Dropdownlist)w;
					//}
					//else if(w instanceof Coordinatesystem) {
						//return (Coordinatesystem)w;
					//}
					else if(w instanceof BB_Image) {
						return (BB_Image)w;
					}
					else if(w instanceof Inputfield) {
						return (Inputfield)w;
					}
					else if(w instanceof Label) {
						return (Label)w;
					}
					//else if(w instanceof Radiobutton) {
						//return (Radiobutton)w;
					//}
					//else if(w instanceof Slider){
						//return (Slider)w;
					//}
					//else if(w instanceof BB_Table) {
						//return (BB_Table)w;
					//}
				}
			}
		}
		throw new RuntimeException("No match for the given Path");
	}
		
		
	public BeastySurface get_surface_by_path(String path){
		for(BeastySurface s : this.surfaces) {
			if(s.SOURCEPATH.equals(path)) {
				return s;
			}
		}
		throw new RuntimeException("No match for the given Path");
	}
	
		
	public BeastySurface get_surface_by_id(String id) {
		for(BeastySurface s : this.surfaces) {
			if(s.ID.equals(id)) {
				return s;
			}
		}
		throw new RuntimeException("No match for the given ID");
	}

	
	public Widget get_widget_by_id(String id){
		for(Widget w : this.widgets) {
			if(w.ID.equals(id)) {
				if(w instanceof Button) {
					return (Button)w;
				}
				else if(w instanceof Checkbox) {
					return (Checkbox)w;
				}
				//else if(w instanceof Dropdownlist) {
					//return (Dropdownlist)w;
				//}
				//else if(w instanceof Coordinatesystem) {
					//return (Coordinatesystem)w;
				//}
				else if(w instanceof BB_Image) {
					return (BB_Image)w;
				}
				else if(w instanceof Inputfield) {
					return (Inputfield)w;
				}
				else if(w instanceof Label) {
					return (Label)w;
				}
				//else if(w instanceof Radiobutton) {
					//return (Radiobutton)w;
				//}
				//else if(w instanceof Slider){
					//return (Slider)w;
				//}
				//else if(w instanceof BB_Table) {
					//return (BB_Table)w;
				//}
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
				//else if(w instanceof Dropdownlist) {
					//return (Dropdownlist)w;
				//}
				//else if(w instanceof Coordinatesystem) {
					//return (Coordinatesystem)w;
				//}
				else if(w instanceof BB_Image) {
					return (BB_Image)w;
				}
				else if(w instanceof Inputfield) {
					return (Inputfield)w;
				}
				else if(w instanceof Label) {
					return (Label)w;
				}
				//else if(w instanceof Radiobutton) {
					//return (Radiobutton)w;
				//}
				//else if(w instanceof Slider){
					//return (Slider)w;
				//}
				//else if(w instanceof BB_Table) {
					//return (BB_Table)w;
				//}
			}
		}
		throw new RuntimeException("No match for the given ID");
	}
	
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
					this.rel_path = logfilepath;
					this.loadlog();
					
					LocalDate date = LocalDate.now();
					LocalTime time = LocalTime.now();
					
					this.loglist.add("****** LOGGING STARTED - " + date + " " + time + " ******");
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
		if(logfilepath.contains(":")){
			throw new RuntimeException("the given logfilepath must be relative");
		}
		return this;
	}
	
	//if an old file with a log already exists it does not get overwritten but added to, else it will be created
	private void loadlog() {
		File f = new File(this.logfilepath);
		if(f.exists()) {
			String[] logs = this.REF.loadStrings(this.rel_path);
			for(String line : logs) {
				this.loglist.add(line);
			}
		}
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
			//@SuppressWarnings("unused")
			//Logthread logthread = new Logthread(this.REF, this.rel_path, this.loglist);
			this.savelog();
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
	
	private void savelog() {
		try {
			String[] list = new String[this.loglist.size()];
			for(int i = 0; i < this.loglist.size(); i++) {
				list[i] = this.loglist.get(i);
			}
			this.REF.saveStrings(this.rel_path, list);
		}
		catch(Exception e) {
			throw new RuntimeException("Error while writing logfile to file system");
		}
	}
	
	//returns full logfile path
	public String getLogfilePath() {
		if(this.logfilepath != null) {
			return this.logfilepath;
		}
		else {
			PApplet.println("WARNING(BB): COULD NOT RETURN LOGFILEPATH SINCE NOTHING WAS SET BEFORE");
		}
		return null;
	}
	
	
	//******IMPORT - EXPORT METHODS******
	
	//set additional information for the user to be print when importing
	public void setPrintouts(String[] prts) {
		for(String s : prts) {
			this.import_printouts.add(s);
		}
	}
	
	//exports a .beasty file stored in XML format
	public void exportLayout(String path) {
		try {
				XML exportxml = new XML("BeastyWorld");
				
				String bbversion_exp = Info.version();
				String processingversion_exp = "unknown";
				
				XML system_exp_tag = exportxml.addChild("System");
				system_exp_tag.addChild("type").setContent(this.operating_system);
				system_exp_tag.addChild("bbversion").setContent(bbversion_exp);
				system_exp_tag.addChild("processingversion").setContent(processingversion_exp);
				
				XML sketch_exp_tag = exportxml.addChild("Sketch");
				sketch_exp_tag.addChild("exportresx").setContent(Float.toString(this.REF.width));
				sketch_exp_tag.addChild("exportresy").setContent(Float.toString(this.REF.height));
				
				String widgetmode_exp = "widget";
				if(this.MODE == WidgetMode.WIDGET) {
					widgetmode_exp = "widget";
				}
				else if(this.MODE == WidgetMode.SURFACE) {
					widgetmode_exp = "surface";
				}
				String logging_exp = "false";
				if(this.logging) {
					logging_exp = "true";
				}
				else {
					logging_exp = "false";
				}
				
				String handlers_exp = "true,false,false", h1 = "true", h2 = "false", h3 = "false";
				if(BeastyWorld.HANDLERS[0]) {
					h1 = "true";
				}
				else {
					h1 = "false";
				}
				if(BeastyWorld.HANDLERS[1]) {
					h2 = "true";
				}
				else {
					h2 = "false";
				}
				if(BeastyWorld.HANDLERS[2]) {
					h3 = "true";
				}
				else {
					h3 = "false";
				}
				handlers_exp = h1+","+h2+","+h3;
				String backgrounddisable_exp = "false";
				if(this.disable_background) {
					backgrounddisable_exp = "true";
				}
				else {
					backgrounddisable_exp = "false";
				}
				
				
				XML settings_exp_tag = exportxml.addChild("Settings");
				settings_exp_tag.addChild("id").setContent(this.ID);
				settings_exp_tag.addChild("sourcepath").setContent(this.SOURCEPATH);
				settings_exp_tag.addChild("widgetmode").setContent(widgetmode_exp);
				if(this.MODE == WidgetMode.SURFACE) {
					settings_exp_tag.addChild("renderpage").setContent(this.renderpage.ID);
				}
				settings_exp_tag.addChild("logging").setContent(logging_exp);
				settings_exp_tag.addChild("loglevel").setContent(Integer.toString(this.loglevel));
				settings_exp_tag.addChild("rellogpath").setContent(this.rel_path);
				settings_exp_tag.addChild("handlers").setContent(handlers_exp);
				settings_exp_tag.addChild("backgrounddisable").setContent(backgrounddisable_exp);
				settings_exp_tag.addChild("backgroundcolor").setContent(Integer.toString(this.BACKGROUNDCOLOR));
				settings_exp_tag.addChild("windowtitle").setContent(this.windowtitle);
				XML import_printouts_exp_tag = settings_exp_tag.addChild("printouts");
				if(this.import_printouts.size() > 0) {
					for(String s : this.import_printouts) {
						import_printouts_exp_tag.addChild("printout").setContent(s);
					}
				}
				XML dependencies_exp_tag = settings_exp_tag.addChild("dependencies");
				dependencies_exp_tag.addChild("iconpath").setContent(this.iconpath);
				dependencies_exp_tag.addChild("fontpath").setContent(this.fontpath);
				dependencies_exp_tag.addChild("ttfontpath").setContent(this.ttfontpath);
			
				
				//layout from here
				XML layout_exp_tag = exportxml.addChild("Layout");
				
				if(this.MODE == WidgetMode.WIDGET) {
					for(Widget w : this.widgets) {
						XML widget_exp_tag = layout_exp_tag.addChild("Widget");
						if(w instanceof Button) {
							widget_exp_tag.setString("type", "Button");
							Button bu = (Button)w;
							widget_exp_tag.addChild("id").setContent(bu.ID);
							widget_exp_tag.addChild("sourcepath").setContent(bu.SOURCEPATH);
							widget_exp_tag.addChild("xpos").setContent(Float.toString(bu.positions[0]));
							widget_exp_tag.addChild("ypos").setContent(Float.toString(bu.positions[1]));
							widget_exp_tag.addChild("xsize").setContent(Float.toString(bu.sizes[0]));
							widget_exp_tag.addChild("ysize").setContent(Float.toString(bu.sizes[1]));
							String round = "false";
							if(bu.round) {
								round = "true";
							}
							else {
								round = "false";
							}
							widget_exp_tag.addChild("round").setContent(round);
							widget_exp_tag.addChild("text").setContent(bu.text);
							widget_exp_tag.addChild("textsize").setContent(Float.toString(bu.textSize));
							widget_exp_tag.addChild("outline").setContent(Float.toString(bu.outline));
							String hotkey = "none";
							if(bu.hotkey > -1) {
								hotkey = Integer.toString(bu.hotkey);
							}
							else {
								hotkey = "none";
							}
							widget_exp_tag.addChild("hotkey").setContent(hotkey);
							widget_exp_tag.addChild("textoffsetx").setContent(Float.toString(bu.textoffset[0]));
							widget_exp_tag.addChild("textoffsety").setContent(Float.toString(bu.textoffset[1]));
							widget_exp_tag.addChild("backgroundcolor").setContent(Integer.toString(bu.background));
							widget_exp_tag.addChild("foregroundcolor").setContent(Integer.toString(bu.foreground));
							widget_exp_tag.addChild("overcolor").setContent(Integer.toString(bu.overcolor));
							widget_exp_tag.addChild("clickcolor").setContent(Integer.toString(bu.clickcolor));
							String active = "true";
							if(bu.active) {
								active = "true";
							}
							else {
								active = "false";
							}
							widget_exp_tag.addChild("active").setContent(active);
							String visible = "true";
							if(bu.visible) {
								visible = "true";
							}
							else {
								visible = "false";
							}
							widget_exp_tag.addChild("visible").setContent(visible);
							widget_exp_tag.addChild("layer").setContent(Integer.toString(bu.LAYER));
							XML tooltip = widget_exp_tag.addChild("tooltip");
							String tttext = "none";
							if(bu.tooltiptext != null) {
								tttext = bu.tooltiptext;
							}
							else {
								tttext = "none";
							}
							tooltip.addChild("tttext").setContent(tttext);
							String tttextsize = "none";
							if(bu.tooltiptextsize > -1) {
								tttextsize = Float.toString(bu.tooltiptextsize);
							}
							else {
								tttextsize = "none";
							}
							tooltip.addChild("tttextsize").setContent(tttextsize);
							tooltip.addChild("ttposx").setContent(Float.toString(bu.ttpositions[0]));
							tooltip.addChild("ttposy").setContent(Float.toString(bu.ttpositions[1]));
							tooltip.addChild("ttsizex").setContent(Float.toString(bu.ttsizes[0]));
							tooltip.addChild("ttsizey").setContent(Float.toString(bu.ttsizes[1]));
							tooltip.addChild("ttforeground").setContent(Integer.toString(bu.ttforeground));
							tooltip.addChild("ttbackground").setContent(Integer.toString(bu.ttbackground));
							String roundtt = "false";
							if(bu.roundtt) {
								roundtt = "true";
							}
							else {
								roundtt = "false";
							}
							tooltip.addChild("roundtt").setContent(roundtt);
							tooltip.addChild("intervall").setContent(Float.toString(bu.intervall));
							tooltip.addChild("tttextoffsetx").setContent(Float.toString(bu.tttextoffset[0]));
							tooltip.addChild("tttextoffsety").setContent(Float.toString(bu.tttextoffset[1]));
							String enabled = "false";
							if(bu.tooltip_enabled) {
								enabled = "true";
							}
							else {
								enabled = "false";
							}
							tooltip.addChild("enabled").setContent(enabled);
							XML dependencies = widget_exp_tag.addChild("dependencies");
							String olc = "none", omc = "none", orc = "none";
							if(bu.olc != null) {
								olc = bu.olc;
							}
							else {
								olc = "none";
							}
							if(bu.omc != null) {
								omc = bu.omc;
							}
							else {
								omc = "none";
							}
							if(bu.orc != null) {
								orc = bu.orc;
							}
							else {
								orc = "none";
							}
							dependencies.addChild("onleftclick").setContent(olc);
							dependencies.addChild("onmiddleclick").setContent(omc);
							dependencies.addChild("onrightclick").setContent(orc);
							dependencies.addChild("fontpath").setContent(bu.fontpath);
							dependencies.addChild("ttfontpath").setContent(bu.ttfontpath);
						}
						else if(w instanceof Checkbox) {
							widget_exp_tag.setString("type", "Checkbox");
							Checkbox c = (Checkbox)w;
							widget_exp_tag.addChild("id").setContent(c.ID);
							widget_exp_tag.addChild("sourcepath").setContent(c.SOURCEPATH);
							widget_exp_tag.addChild("xpos").setContent(Float.toString(c.positions[0]));
							widget_exp_tag.addChild("ypos").setContent(Float.toString(c.positions[1]));
							widget_exp_tag.addChild("xsize").setContent(Float.toString(c.sizes[0]));
							widget_exp_tag.addChild("ysize").setContent(Float.toString(c.sizes[1]));
							String round = "false";
							if(c.round) {
								round = "true";
							}
							else {
								round = "false";
							}
							widget_exp_tag.addChild("round").setContent(round);
							widget_exp_tag.addChild("outline").setContent(Float.toString(c.outline));
							String hotkey = "none";
							if(c.hotkey > -1) {
								hotkey = Integer.toString(c.hotkey);
							}
							else {
								hotkey = "none";
							}
							widget_exp_tag.addChild("hotkey").setContent(hotkey);
							String checktype = "check";
							if(c.CHECKTYPE == Checktype.CHECK) {
								checktype = "check";
							}
							else if(c.CHECKTYPE == Checktype.CROSS) {
								checktype = "cross";
							}
							else if(c.CHECKTYPE == Checktype.POINT) {
								checktype = "point";
							}
							else if(c.CHECKTYPE == Checktype.CIRCLE) {
								checktype = "circle";
							}
							widget_exp_tag.addChild("checktype").setContent(checktype);
							String state = "false";
							if(c.state) {
								state = "true";
							}
							else {
								state = "false";
							}
							widget_exp_tag.addChild("state").setContent(state);
							widget_exp_tag.addChild("backgroundcolor").setContent(Integer.toString(c.background));
							widget_exp_tag.addChild("foregroundcolor").setContent(Integer.toString(c.foreground));
							widget_exp_tag.addChild("overcolor").setContent(Integer.toString(c.overcolor));
							widget_exp_tag.addChild("clickcolor").setContent(Integer.toString(c.clickcolor));
							String active = "true";
							if(c.active) {
								active = "true";
							}
							else {
								active = "false";
							}
							widget_exp_tag.addChild("active").setContent(active);
							String visible = "true";
							if(c.visible) {
								visible = "true";
							}
							else {
								visible = "false";
							}
							widget_exp_tag.addChild("visible").setContent(visible);
							widget_exp_tag.addChild("layer").setContent(Integer.toString(c.LAYER));
							XML tooltip = widget_exp_tag.addChild("tooltip");
							String tttext = "none";
							if(c.tooltiptext != null) {
								tttext = c.tooltiptext;
							}
							else {
								tttext = "none";
							}
							tooltip.addChild("tttext").setContent(tttext);
							String tttextsize = "none";
							if(c.tooltiptextsize > -1) {
								tttextsize = Float.toString(c.tooltiptextsize);
							}
							else {
								tttextsize = "none";
							}
							tooltip.addChild("tttextsize").setContent(tttextsize);
							tooltip.addChild("ttposx").setContent(Float.toString(c.ttpositions[0]));
							tooltip.addChild("ttposy").setContent(Float.toString(c.ttpositions[1]));
							tooltip.addChild("ttsizex").setContent(Float.toString(c.ttsizes[0]));
							tooltip.addChild("ttsizey").setContent(Float.toString(c.ttsizes[1]));
							tooltip.addChild("ttforeground").setContent(Integer.toString(c.ttforeground));
							tooltip.addChild("ttbackground").setContent(Integer.toString(c.ttbackground));
							String roundtt = "false";
							if(c.roundtt) {
								roundtt = "true";
							}
							else {
								roundtt = "false";
							}
							tooltip.addChild("roundtt").setContent(roundtt);
							tooltip.addChild("intervall").setContent(Float.toString(c.intervall));
							tooltip.addChild("tttextoffsetx").setContent(Float.toString(c.tttextoffset[0]));
							tooltip.addChild("tttextoffsety").setContent(Float.toString(c.tttextoffset[1]));
							String enabled = "false";
							if(c.tooltip_enabled) {
								enabled = "true";
							}
							else {
								enabled = "false";
							}
							tooltip.addChild("enabled").setContent(enabled);
							XML dependencies = widget_exp_tag.addChild("dependencies");
							String oc = "none", ou = "none";
							if(c.oc != null) {
								oc = c.oc;
							}
							else {
								oc = "none";
							}
							if(c.ou != null) {
								ou = c.ou;
							}
							else {
								ou = "none";
							}
							dependencies.addChild("oncheck").setContent(oc);
							dependencies.addChild("onuncheck").setContent(ou);
							dependencies.addChild("ttfontpath").setContent(c.ttfontpath);
						}
						else if(w instanceof Label) {
							widget_exp_tag.setString("type", "Label");
							Label l = (Label)w;
							widget_exp_tag.addChild("id").setContent(l.ID);
							widget_exp_tag.addChild("sourcepath").setContent(l.SOURCEPATH);
							widget_exp_tag.addChild("xpos").setContent(Float.toString(l.positions[0]));
							widget_exp_tag.addChild("ypos").setContent(Float.toString(l.positions[1]));
							widget_exp_tag.addChild("xsize").setContent(Float.toString(l.sizes[0]));
							widget_exp_tag.addChild("ysize").setContent(Float.toString(l.sizes[1]));
							String round = "false";
							if(l.round) {
								round = "true";
							}
							else {
								round = "false";
							}
							widget_exp_tag.addChild("round").setContent(round);
							widget_exp_tag.addChild("text").setContent(l.text);
							widget_exp_tag.addChild("textsize").setContent(Float.toString(l.textSize));
							widget_exp_tag.addChild("textoffsetx").setContent(Float.toString(l.textoffset[0]));
							widget_exp_tag.addChild("textoffsety").setContent(Float.toString(l.textoffset[1]));
							widget_exp_tag.addChild("backgroundcolor").setContent(Integer.toString(l.background));
							widget_exp_tag.addChild("foregroundcolor").setContent(Integer.toString(l.foreground));
							
							String visible = "true";
							if(l.visible) {
								visible = "true";
							}
							else {
								visible = "false";
							}
							widget_exp_tag.addChild("visible").setContent(visible);
							widget_exp_tag.addChild("layer").setContent(Integer.toString(l.LAYER));
							
							XML dependencies = widget_exp_tag.addChild("dependencies");
							dependencies.addChild("fontpath").setContent(l.fontpath);
						}
						else if(w instanceof Inputfield) {
							widget_exp_tag.setString("type", "Inputfield");
							Inputfield i = (Inputfield)w;
							widget_exp_tag.addChild("id").setContent(i.ID);
							widget_exp_tag.addChild("sourcepath").setContent(i.SOURCEPATH);
							widget_exp_tag.addChild("xpos").setContent(Float.toString(i.positions[0]));
							widget_exp_tag.addChild("ypos").setContent(Float.toString(i.positions[1]));
							widget_exp_tag.addChild("xsize").setContent(Float.toString(i.sizes[0]));
							widget_exp_tag.addChild("ysize").setContent(Float.toString(i.sizes[1]));
							String round = "false";
							if(i.round) {
								round = "true";
							}
							else {
								round = "false";
							}
							widget_exp_tag.addChild("round").setContent(round);
							widget_exp_tag.addChild("inputtext").setContent(i.input_text);
							widget_exp_tag.addChild("greyedtext").setContent(i.greyText);
							widget_exp_tag.addChild("textsize").setContent(Float.toString(i.textSize));
							widget_exp_tag.addChild("outline").setContent(Float.toString(i.outline));
							String hotkey = "-1";
							if(i.hotkey > -1) {
								hotkey = Integer.toString(i.hotkey);
							}
							else {
								hotkey = "-1";
							}
							widget_exp_tag.addChild("hotkey").setContent(hotkey);
							widget_exp_tag.addChild("textoffsetx").setContent(Float.toString(i.textoffset[0]));
							widget_exp_tag.addChild("textoffsety").setContent(Float.toString(i.textoffset[1]));
							widget_exp_tag.addChild("backgroundcolor").setContent(Integer.toString(i.background));
							widget_exp_tag.addChild("foregroundcolor").setContent(Integer.toString(i.foreground));
							widget_exp_tag.addChild("overcolor").setContent(Integer.toString(i.overcolor));
							widget_exp_tag.addChild("inputcolor").setContent(Integer.toString(i.input_color));
							widget_exp_tag.addChild("limitinputtext").setContent(Integer.toString(i.limit_input_length));
							String active = "true";
							if(i.active) {
								active = "true";
							}
							else {
								active = "false";
							}
							widget_exp_tag.addChild("active").setContent(active);
							String visible = "true";
							if(i.visible) {
								visible = "true";
							}
							else {
								visible = "false";
							}
							widget_exp_tag.addChild("visible").setContent(visible);
							widget_exp_tag.addChild("layer").setContent(Integer.toString(i.LAYER));
							XML tooltip = widget_exp_tag.addChild("tooltip");
							String tttext = "none";
							if(i.tooltiptext != null) {
								tttext = i.tooltiptext;
							}
							else {
								tttext = "none";
							}
							tooltip.addChild("tttext").setContent(tttext);
							String tttextsize = "none";
							if(i.tooltiptextsize > -1) {
								tttextsize = Float.toString(i.tooltiptextsize);
							}
							else {
								tttextsize = "none";
							}
							tooltip.addChild("tttextsize").setContent(tttextsize);
							tooltip.addChild("ttposx").setContent(Float.toString(i.ttpositions[0]));
							tooltip.addChild("ttposy").setContent(Float.toString(i.ttpositions[1]));
							tooltip.addChild("ttsizex").setContent(Float.toString(i.ttsizes[0]));
							tooltip.addChild("ttsizey").setContent(Float.toString(i.ttsizes[1]));
							tooltip.addChild("ttforeground").setContent(Integer.toString(i.ttforeground));
							tooltip.addChild("ttbackground").setContent(Integer.toString(i.ttbackground));
							String roundtt = "false";
							if(i.roundtt) {
								roundtt = "true";
							}
							else {
								roundtt = "false";
							}
							tooltip.addChild("roundtt").setContent(roundtt);
							tooltip.addChild("intervall").setContent(Float.toString(i.intervall));
							tooltip.addChild("tttextoffsetx").setContent(Float.toString(i.tttextoffset[0]));
							tooltip.addChild("tttextoffsety").setContent(Float.toString(i.tttextoffset[1]));
							String enabled = "false";
							if(i.tooltip_enabled) {
								enabled = "true";
							}
							else {
								enabled = "false";
							}
							tooltip.addChild("enabled").setContent(enabled);
							XML dependencies = widget_exp_tag.addChild("dependencies");
							dependencies.addChild("fontpath").setContent(i.fontpath);
							dependencies.addChild("ttfontpath").setContent(i.ttfontpath);
						}
						else if(w instanceof BB_Image) {
							widget_exp_tag.setString("type", "BB_Image");
							BB_Image bbi = (BB_Image)w;
							widget_exp_tag.addChild("id").setContent(bbi.ID);
							widget_exp_tag.addChild("sourcepath").setContent(bbi.SOURCEPATH);
							widget_exp_tag.addChild("xpos").setContent(Float.toString(bbi.positions[0]));
							widget_exp_tag.addChild("ypos").setContent(Float.toString(bbi.positions[1]));
							widget_exp_tag.addChild("xsize").setContent(Float.toString(bbi.sizes[0]));
							widget_exp_tag.addChild("ysize").setContent(Float.toString(bbi.sizes[1]));
							
							String active = "true";
							if(bbi.active) {
								active = "true";
							}
							else {
								active = "false";
							}
							widget_exp_tag.addChild("active").setContent(active);
							String visible = "true";
							if(bbi.visible) {
								visible = "true";
							}
							else {
								visible = "false";
							}
							widget_exp_tag.addChild("visible").setContent(visible);
							widget_exp_tag.addChild("layer").setContent(Integer.toString(bbi.LAYER));
							if(bbi.crops.size() > 0) {
								XML crops = widget_exp_tag.addChild("crops");
								for(BB_Image.Crop crop: bbi.crops) {
									String crop_str = crop.edge + "," + Integer.toString(crop.pixel_amount);
									crops.addChild("crop").setContent(crop_str);
								}
							}
							
							XML tooltip = widget_exp_tag.addChild("tooltip");
							String tttext = "none";
							if(bbi.tooltiptext != null) {
								tttext = bbi.tooltiptext;
							}
							else {
								tttext = "none";
							}
							tooltip.addChild("tttext").setContent(tttext);
							String tttextsize = "none";
							if(bbi.tooltiptextsize > -1) {
								tttextsize = Float.toString(bbi.tooltiptextsize);
							}
							else {
								tttextsize = "none";
							}
							tooltip.addChild("tttextsize").setContent(tttextsize);
							tooltip.addChild("ttposx").setContent(Float.toString(bbi.ttpositions[0]));
							tooltip.addChild("ttposy").setContent(Float.toString(bbi.ttpositions[1]));
							tooltip.addChild("ttsizex").setContent(Float.toString(bbi.ttsizes[0]));
							tooltip.addChild("ttsizey").setContent(Float.toString(bbi.ttsizes[1]));
							tooltip.addChild("ttforeground").setContent(Integer.toString(bbi.ttforeground));
							tooltip.addChild("ttbackground").setContent(Integer.toString(bbi.ttbackground));
							String roundtt = "false";
							if(bbi.roundtt) {
								roundtt = "true";
							}
							else {
								roundtt = "false";
							}
							tooltip.addChild("roundtt").setContent(roundtt);
							tooltip.addChild("intervall").setContent(Float.toString(bbi.intervall));
							tooltip.addChild("tttextoffsetx").setContent(Float.toString(bbi.tttextoffset[0]));
							tooltip.addChild("tttextoffsety").setContent(Float.toString(bbi.tttextoffset[1]));
							String enabled = "false";
							if(bbi.tooltip_enabled) {
								enabled = "true";
							}
							else {
								enabled = "false";
							}
							tooltip.addChild("enabled").setContent(enabled);
							XML dependencies = widget_exp_tag.addChild("dependencies");
							dependencies.addChild("sourceimg").setContent(bbi.imgpath);
							dependencies.addChild("ttfontpath").setContent(bbi.ttfontpath);
						}
					}
				}
				else if(this.MODE == WidgetMode.SURFACE) {
					for(BeastySurface b : this.surfaces) {
						XML surface_exp_tag = layout_exp_tag.addChild("BeastySurface");
						XML settings_surface_exp_tag = surface_exp_tag.addChild("Settings");
						settings_surface_exp_tag.addChild("id").setContent(b.ID);
						settings_surface_exp_tag.addChild("sourcepath").setContent(b.SOURCEPATH);
						settings_surface_exp_tag.addChild("xpos").setContent(Float.toString(b.posx));
						settings_surface_exp_tag.addChild("ypos").setContent(Float.toString(b.posy));
						String strbgsurface = "false";
						if(b.stretch_backgroundimg) {
							strbgsurface = "true";
						}
						else {
							strbgsurface = "false";
						}
						settings_surface_exp_tag.addChild("stretchbackgroundimg").setContent(strbgsurface);
						settings_surface_exp_tag.addChild("backgroundcolor").setContent(Integer.toString(b.backgroundcolor));
						settings_surface_exp_tag.addChild("imgoffsetx").setContent(Float.toString(b.offsetx));
						settings_surface_exp_tag.addChild("imgoffsety").setContent(Float.toString(b.offsety));
						XML dependenciessurface_exp_tag = settings_surface_exp_tag.addChild("dependencies");
						dependenciessurface_exp_tag.addChild("backgroundimgpath").setContent(b.imgpath);
						dependenciessurface_exp_tag.addChild("fontpath").setContent(b.fontpath);
						dependenciessurface_exp_tag.addChild("ttfontpath").setContent(b.ttfontpath);
						
						XML widgets_surface_exp_tag = surface_exp_tag.addChild("Widgets");
						
						for(Widget w : b.widgets) {
							XML widget_surface_exp_tag = widgets_surface_exp_tag.addChild("Widget");
							
							if(w instanceof Button) {
								widget_surface_exp_tag.setString("type", "Button");
								Button bu = (Button)w;
								widget_surface_exp_tag.addChild("id").setContent(bu.ID);
								widget_surface_exp_tag.addChild("sourcepath").setContent(bu.SOURCEPATH);
								widget_surface_exp_tag.addChild("xpos").setContent(Float.toString(bu.positions[0]));
								widget_surface_exp_tag.addChild("ypos").setContent(Float.toString(bu.positions[1]));
								widget_surface_exp_tag.addChild("xsize").setContent(Float.toString(bu.sizes[0]));
								widget_surface_exp_tag.addChild("ysize").setContent(Float.toString(bu.sizes[1]));
								String round = "false";
								if(bu.round) {
									round = "true";
								}
								else {
									round = "false";
								}
								widget_surface_exp_tag.addChild("round").setContent(round);
								widget_surface_exp_tag.addChild("text").setContent(bu.text);
								widget_surface_exp_tag.addChild("textsize").setContent(Float.toString(bu.textSize));
								widget_surface_exp_tag.addChild("outline").setContent(Float.toString(bu.outline));
								String hotkey = "none";
								if(bu.hotkey > -1) {
									hotkey = Integer.toString(bu.hotkey);
								}
								else {
									hotkey = "none";
								}
								widget_surface_exp_tag.addChild("hotkey").setContent(hotkey);
								widget_surface_exp_tag.addChild("textoffsetx").setContent(Float.toString(bu.textoffset[0]));
								widget_surface_exp_tag.addChild("textoffsety").setContent(Float.toString(bu.textoffset[1]));
								widget_surface_exp_tag.addChild("backgroundcolor").setContent(Integer.toString(bu.background));
								widget_surface_exp_tag.addChild("foregroundcolor").setContent(Integer.toString(bu.foreground));
								widget_surface_exp_tag.addChild("overcolor").setContent(Integer.toString(bu.overcolor));
								widget_surface_exp_tag.addChild("clickcolor").setContent(Integer.toString(bu.clickcolor));
								String active = "true";
								if(bu.active) {
									active = "true";
								}
								else {
									active = "false";
								}
								widget_surface_exp_tag.addChild("active").setContent(active);
								String visible = "true";
								if(bu.visible) {
									visible = "true";
								}
								else {
									visible = "false";
								}
								widget_surface_exp_tag.addChild("visible").setContent(visible);
								widget_surface_exp_tag.addChild("layer").setContent(Integer.toString(bu.LAYER));
								XML tooltip = widget_surface_exp_tag.addChild("tooltip");
								String tttext = "none";
								if(bu.tooltiptext != null) {
									tttext = bu.tooltiptext;
								}
								else {
									tttext = "none";
								}
								tooltip.addChild("tttext").setContent(tttext);
								String tttextsize = "none";
								if(bu.tooltiptextsize > -1) {
									tttextsize = Float.toString(bu.tooltiptextsize);
								}
								else {
									tttextsize = "none";
								}
								tooltip.addChild("tttextsize").setContent(tttextsize);
								tooltip.addChild("ttposx").setContent(Float.toString(bu.ttpositions[0]));
								tooltip.addChild("ttposy").setContent(Float.toString(bu.ttpositions[1]));
								tooltip.addChild("ttsizex").setContent(Float.toString(bu.ttsizes[0]));
								tooltip.addChild("ttsizey").setContent(Float.toString(bu.ttsizes[1]));
								tooltip.addChild("ttforeground").setContent(Integer.toString(bu.ttforeground));
								tooltip.addChild("ttbackground").setContent(Integer.toString(bu.ttbackground));
								String roundtt = "false";
								if(bu.roundtt) {
									roundtt = "true";
								}
								else {
									roundtt = "false";
								}
								tooltip.addChild("roundtt").setContent(roundtt);
								tooltip.addChild("intervall").setContent(Float.toString(bu.intervall));
								tooltip.addChild("tttextoffsetx").setContent(Float.toString(bu.tttextoffset[0]));
								tooltip.addChild("tttextoffsety").setContent(Float.toString(bu.tttextoffset[1]));
								String enabled = "false";
								if(bu.tooltip_enabled) {
									enabled = "true";
								}
								else {
									enabled = "false";
								}
								tooltip.addChild("enabled").setContent(enabled);
								XML dependencies = widget_surface_exp_tag.addChild("dependencies");
								String olc = "none", omc = "none", orc = "none";
								if(bu.olc != null) {
									olc = bu.olc;
								}
								else {
									olc = "none";
								}
								if(bu.omc != null) {
									omc = bu.omc;
								}
								else {
									omc = "none";
								}
								if(bu.orc != null) {
									orc = bu.orc;
								}
								else {
									orc = "none";
								}
								dependencies.addChild("onleftclick").setContent(olc);
								dependencies.addChild("onmiddleclick").setContent(omc);
								dependencies.addChild("onrightclick").setContent(orc);
								dependencies.addChild("fontpath").setContent(bu.fontpath);
								dependencies.addChild("ttfontpath").setContent(bu.ttfontpath);
							}
							else if(w instanceof Checkbox) {
								widget_surface_exp_tag.setString("type", "Checkbox");
								Checkbox c = (Checkbox)w;
								widget_surface_exp_tag.addChild("id").setContent(c.ID);
								widget_surface_exp_tag.addChild("sourcepath").setContent(c.SOURCEPATH);
								widget_surface_exp_tag.addChild("xpos").setContent(Float.toString(c.positions[0]));
								widget_surface_exp_tag.addChild("ypos").setContent(Float.toString(c.positions[1]));
								widget_surface_exp_tag.addChild("xsize").setContent(Float.toString(c.sizes[0]));
								widget_surface_exp_tag.addChild("ysize").setContent(Float.toString(c.sizes[1]));
								String round = "false";
								if(c.round) {
									round = "true";
								}
								else {
									round = "false";
								}
								widget_surface_exp_tag.addChild("round").setContent(round);
								widget_surface_exp_tag.addChild("outline").setContent(Float.toString(c.outline));
								String hotkey = "none";
								if(c.hotkey > -1) {
									hotkey = Integer.toString(c.hotkey);
								}
								else {
									hotkey = "none";
								}
								widget_surface_exp_tag.addChild("hotkey").setContent(hotkey);
								String checktype = "check";
								if(c.CHECKTYPE == Checktype.CHECK) {
									checktype = "check";
								}
								else if(c.CHECKTYPE == Checktype.CROSS) {
									checktype = "cross";
								}
								else if(c.CHECKTYPE == Checktype.POINT) {
									checktype = "point";
								}
								else if(c.CHECKTYPE == Checktype.CIRCLE) {
									checktype = "circle";
								}
								widget_surface_exp_tag.addChild("checktype").setContent(checktype);
								String state = "false";
								if(c.state) {
									state = "true";
								}
								else {
									state = "false";
								}
								widget_surface_exp_tag.addChild("state").setContent(state);
								widget_surface_exp_tag.addChild("backgroundcolor").setContent(Integer.toString(c.background));
								widget_surface_exp_tag.addChild("foregroundcolor").setContent(Integer.toString(c.foreground));
								widget_surface_exp_tag.addChild("overcolor").setContent(Integer.toString(c.overcolor));
								widget_surface_exp_tag.addChild("clickcolor").setContent(Integer.toString(c.clickcolor));
								String active = "true";
								if(c.active) {
									active = "true";
								}
								else {
									active = "false";
								}
								widget_surface_exp_tag.addChild("active").setContent(active);
								String visible = "true";
								if(c.visible) {
									visible = "true";
								}
								else {
									visible = "false";
								}
								widget_surface_exp_tag.addChild("visible").setContent(visible);
								widget_surface_exp_tag.addChild("layer").setContent(Integer.toString(c.LAYER));
								XML tooltip = widget_surface_exp_tag.addChild("tooltip");
								String tttext = "none";
								if(c.tooltiptext != null) {
									tttext = c.tooltiptext;
								}
								else {
									tttext = "none";
								}
								tooltip.addChild("tttext").setContent(tttext);
								String tttextsize = "none";
								if(c.tooltiptextsize > -1) {
									tttextsize = Float.toString(c.tooltiptextsize);
								}
								else {
									tttextsize = "none";
								}
								tooltip.addChild("tttextsize").setContent(tttextsize);
								tooltip.addChild("ttposx").setContent(Float.toString(c.ttpositions[0]));
								tooltip.addChild("ttposy").setContent(Float.toString(c.ttpositions[1]));
								tooltip.addChild("ttsizex").setContent(Float.toString(c.ttsizes[0]));
								tooltip.addChild("ttsizey").setContent(Float.toString(c.ttsizes[1]));
								tooltip.addChild("ttforeground").setContent(Integer.toString(c.ttforeground));
								tooltip.addChild("ttbackground").setContent(Integer.toString(c.ttbackground));
								String roundtt = "false";
								if(c.roundtt) {
									roundtt = "true";
								}
								else {
									roundtt = "false";
								}
								tooltip.addChild("roundtt").setContent(roundtt);
								tooltip.addChild("intervall").setContent(Float.toString(c.intervall));
								tooltip.addChild("tttextoffsetx").setContent(Float.toString(c.tttextoffset[0]));
								tooltip.addChild("tttextoffsety").setContent(Float.toString(c.tttextoffset[1]));
								String enabled = "false";
								if(c.tooltip_enabled) {
									enabled = "true";
								}
								else {
									enabled = "false";
								}
								tooltip.addChild("enabled").setContent(enabled);
								XML dependencies = widget_surface_exp_tag.addChild("dependencies");
								String oc = "none", ou = "none";
								if(c.oc != null) {
									oc = c.oc;
								}
								else {
									oc = "none";
								}
								if(c.ou != null) {
									ou = c.ou;
								}
								else {
									ou = "none";
								}
								dependencies.addChild("oncheck").setContent(oc);
								dependencies.addChild("onuncheck").setContent(ou);
								dependencies.addChild("ttfontpath").setContent(c.ttfontpath);
							}
							else if(w instanceof Label) {
								widget_surface_exp_tag.setString("type", "Label");
								Label l = (Label)w;
								widget_surface_exp_tag.addChild("id").setContent(l.ID);
								widget_surface_exp_tag.addChild("sourcepath").setContent(l.SOURCEPATH);
								widget_surface_exp_tag.addChild("xpos").setContent(Float.toString(l.positions[0]));
								widget_surface_exp_tag.addChild("ypos").setContent(Float.toString(l.positions[1]));
								widget_surface_exp_tag.addChild("xsize").setContent(Float.toString(l.sizes[0]));
								widget_surface_exp_tag.addChild("ysize").setContent(Float.toString(l.sizes[1]));
								String round = "false";
								if(l.round) {
									round = "true";
								}
								else {
									round = "false";
								}
								widget_surface_exp_tag.addChild("round").setContent(round);
								widget_surface_exp_tag.addChild("text").setContent(l.text);
								widget_surface_exp_tag.addChild("textsize").setContent(Float.toString(l.textSize));
								widget_surface_exp_tag.addChild("textoffsetx").setContent(Float.toString(l.textoffset[0]));
								widget_surface_exp_tag.addChild("textoffsety").setContent(Float.toString(l.textoffset[1]));
								widget_surface_exp_tag.addChild("backgroundcolor").setContent(Integer.toString(l.background));
								widget_surface_exp_tag.addChild("foregroundcolor").setContent(Integer.toString(l.foreground));
								
								String visible = "true";
								if(l.visible) {
									visible = "true";
								}
								else {
									visible = "false";
								}
								widget_surface_exp_tag.addChild("visible").setContent(visible);
								widget_surface_exp_tag.addChild("layer").setContent(Integer.toString(l.LAYER));
								
								XML dependencies = widget_surface_exp_tag.addChild("dependencies");
								dependencies.addChild("fontpath").setContent(l.fontpath);
							}
							else if(w instanceof Inputfield) {
								widget_surface_exp_tag.setString("type", "Inputfield");
								Inputfield i = (Inputfield)w;
								widget_surface_exp_tag.addChild("id").setContent(i.ID);
								widget_surface_exp_tag.addChild("sourcepath").setContent(i.SOURCEPATH);
								widget_surface_exp_tag.addChild("xpos").setContent(Float.toString(i.positions[0]));
								widget_surface_exp_tag.addChild("ypos").setContent(Float.toString(i.positions[1]));
								widget_surface_exp_tag.addChild("xsize").setContent(Float.toString(i.sizes[0]));
								widget_surface_exp_tag.addChild("ysize").setContent(Float.toString(i.sizes[1]));
								String round = "false";
								if(i.round) {
									round = "true";
								}
								else {
									round = "false";
								}
								widget_surface_exp_tag.addChild("round").setContent(round);
								widget_surface_exp_tag.addChild("inputtext").setContent(i.input_text);
								widget_surface_exp_tag.addChild("greyedtext").setContent(i.greyText);
								widget_surface_exp_tag.addChild("textsize").setContent(Float.toString(i.textSize));
								widget_surface_exp_tag.addChild("outline").setContent(Float.toString(i.outline));
								String hotkey = "-1";
								if(i.hotkey > -1) {
									hotkey = Integer.toString(i.hotkey);
								}
								else {
									hotkey = "-1";
								}
								widget_surface_exp_tag.addChild("hotkey").setContent(hotkey);
								widget_surface_exp_tag.addChild("textoffsetx").setContent(Float.toString(i.textoffset[0]));
								widget_surface_exp_tag.addChild("textoffsety").setContent(Float.toString(i.textoffset[1]));
								widget_surface_exp_tag.addChild("backgroundcolor").setContent(Integer.toString(i.background));
								widget_surface_exp_tag.addChild("foregroundcolor").setContent(Integer.toString(i.foreground));
								widget_surface_exp_tag.addChild("overcolor").setContent(Integer.toString(i.overcolor));
								widget_surface_exp_tag.addChild("inputcolor").setContent(Integer.toString(i.input_color));
								widget_surface_exp_tag.addChild("limitinputtext").setContent(Integer.toString(i.limit_input_length));
								String active = "true";
								if(i.active) {
									active = "true";
								}
								else {
									active = "false";
								}
								widget_surface_exp_tag.addChild("active").setContent(active);
								String visible = "true";
								if(i.visible) {
									visible = "true";
								}
								else {
									visible = "false";
								}
								widget_surface_exp_tag.addChild("visible").setContent(visible);
								widget_surface_exp_tag.addChild("layer").setContent(Integer.toString(i.LAYER));
								XML tooltip = widget_surface_exp_tag.addChild("tooltip");
								String tttext = "none";
								if(i.tooltiptext != null) {
									tttext = i.tooltiptext;
								}
								else {
									tttext = "none";
								}
								tooltip.addChild("tttext").setContent(tttext);
								String tttextsize = "none";
								if(i.tooltiptextsize > -1) {
									tttextsize = Float.toString(i.tooltiptextsize);
								}
								else {
									tttextsize = "none";
								}
								tooltip.addChild("tttextsize").setContent(tttextsize);
								tooltip.addChild("ttposx").setContent(Float.toString(i.ttpositions[0]));
								tooltip.addChild("ttposy").setContent(Float.toString(i.ttpositions[1]));
								tooltip.addChild("ttsizex").setContent(Float.toString(i.ttsizes[0]));
								tooltip.addChild("ttsizey").setContent(Float.toString(i.ttsizes[1]));
								tooltip.addChild("ttforeground").setContent(Integer.toString(i.ttforeground));
								tooltip.addChild("ttbackground").setContent(Integer.toString(i.ttbackground));
								String roundtt = "false";
								if(i.roundtt) {
									roundtt = "true";
								}
								else {
									roundtt = "false";
								}
								tooltip.addChild("roundtt").setContent(roundtt);
								tooltip.addChild("intervall").setContent(Float.toString(i.intervall));
								tooltip.addChild("tttextoffsetx").setContent(Float.toString(i.tttextoffset[0]));
								tooltip.addChild("tttextoffsety").setContent(Float.toString(i.tttextoffset[1]));
								String enabled = "false";
								if(i.tooltip_enabled) {
									enabled = "true";
								}
								else {
									enabled = "false";
								}
								tooltip.addChild("enabled").setContent(enabled);
								XML dependencies = widget_surface_exp_tag.addChild("dependencies");
								dependencies.addChild("fontpath").setContent(i.fontpath);
								dependencies.addChild("ttfontpath").setContent(i.ttfontpath);
							}
							else if(w instanceof BB_Image) {
								widget_surface_exp_tag.setString("type", "BB_Image");
								BB_Image bbi = (BB_Image)w;
								widget_surface_exp_tag.addChild("id").setContent(bbi.ID);
								widget_surface_exp_tag.addChild("sourcepath").setContent(bbi.SOURCEPATH);
								widget_surface_exp_tag.addChild("xpos").setContent(Float.toString(bbi.positions[0]));
								widget_surface_exp_tag.addChild("ypos").setContent(Float.toString(bbi.positions[1]));
								widget_surface_exp_tag.addChild("xsize").setContent(Float.toString(bbi.sizes[0]));
								widget_surface_exp_tag.addChild("ysize").setContent(Float.toString(bbi.sizes[1]));
								
								String active = "true";
								if(bbi.active) {
									active = "true";
								}
								else {
									active = "false";
								}
								widget_surface_exp_tag.addChild("active").setContent(active);
								String visible = "true";
								if(bbi.visible) {
									visible = "true";
								}
								else {
									visible = "false";
								}
								widget_surface_exp_tag.addChild("visible").setContent(visible);
								widget_surface_exp_tag.addChild("layer").setContent(Integer.toString(bbi.LAYER));
								if(bbi.crops.size() > 0) {
									XML crops = widget_surface_exp_tag.addChild("crops");
									for(BB_Image.Crop crop: bbi.crops) {
										String crop_str = crop.edge + "," + Integer.toString(crop.pixel_amount);
										crops.addChild("crop").setContent(crop_str);
									}
								}
								
								XML tooltip = widget_surface_exp_tag.addChild("tooltip");
								String tttext = "none";
								if(bbi.tooltiptext != null) {
									tttext = bbi.tooltiptext;
								}
								else {
									tttext = "none";
								}
								tooltip.addChild("tttext").setContent(tttext);
								String tttextsize = "none";
								if(bbi.tooltiptextsize > -1) {
									tttextsize = Float.toString(bbi.tooltiptextsize);
								}
								else {
									tttextsize = "none";
								}
								tooltip.addChild("tttextsize").setContent(tttextsize);
								tooltip.addChild("ttposx").setContent(Float.toString(bbi.ttpositions[0]));
								tooltip.addChild("ttposy").setContent(Float.toString(bbi.ttpositions[1]));
								tooltip.addChild("ttsizex").setContent(Float.toString(bbi.ttsizes[0]));
								tooltip.addChild("ttsizey").setContent(Float.toString(bbi.ttsizes[1]));
								tooltip.addChild("ttforeground").setContent(Integer.toString(bbi.ttforeground));
								tooltip.addChild("ttbackground").setContent(Integer.toString(bbi.ttbackground));
								String roundtt = "false";
								if(bbi.roundtt) {
									roundtt = "true";
								}
								else {
									roundtt = "false";
								}
								tooltip.addChild("roundtt").setContent(roundtt);
								tooltip.addChild("intervall").setContent(Float.toString(bbi.intervall));
								tooltip.addChild("tttextoffsetx").setContent(Float.toString(bbi.tttextoffset[0]));
								tooltip.addChild("tttextoffsety").setContent(Float.toString(bbi.tttextoffset[1]));
								String enabled = "false";
								if(bbi.tooltip_enabled) {
									enabled = "true";
								}
								else {
									enabled = "false";
								}
								tooltip.addChild("enabled").setContent(enabled);
								XML dependencies = widget_surface_exp_tag.addChild("dependencies");
								dependencies.addChild("sourceimg").setContent(bbi.imgpath);
								dependencies.addChild("ttfontpath").setContent(bbi.ttfontpath);
							}
						}
					}
				}
				
				this.REF.saveXML(exportxml, path);
		}
		catch(Exception e){
			PApplet.println("WARNING(BB): COULD NOT EXPORT LAYOUT -> CONTINUEING WITHOUT EXPORTING");
		}
	}
	
	
	//imports a .beasty file stored in JSON format
	@NotImplementedYet
	@Experimental
	public void importLayout(String path, boolean dependencies) {
		try {
			if(!path.split(".")[1].equals("beasty")) {
				throw new RuntimeException("unknown import file type");
			}
			
			XML importxml = this.REF.loadXML(path);
			
			String bb_version = importxml.getChild("System").getChild("bbversion").getContent();
			if(!Info.version().equals(bb_version)) {
				PApplet.println("WARNING(BB): BEASTYBUTTONS VERSION DOES NOT MATCH THE VERSION THE FILE WAS EXPORTED WITH, THIS MIGHT CAUSE ERRORS");
			}
			
			int sketchimpx = importxml.getChild("Sketch").getChild("exportresx").getIntContent();
			int sketchimpy = importxml.getChild("Sketch").getChild("exportresy").getIntContent();
			if(this.REF.width != sketchimpx || this.REF.height != sketchimpy) {
				throw new RuntimeException("Import Error: Import file does not match the Sketch Resolution");
			}
			
			String w_mode = importxml.getChild("Settings").getChild("widgetmode").getContent();
			if(this.MODE == WidgetMode.WIDGET && w_mode.equals("surface")) {
				PApplet.println("WARNING(BB): IMPORT FILE DOES NOT MATCH CURRENT WIDGETMODE, WIDGETS MIGHT BE IGNORED");
			}
			else if(this.MODE == WidgetMode.SURFACE && w_mode.equals("widget")) {
				PApplet.println("WARNING(BB): IMPORT FILE DOES NOT MATCH CURRENT WIDGETMODE, WIDGETS MIGHT BE IGNORED");
			}
			else {
				if(w_mode.equals("widget")) {
					this.MODE = WidgetMode.WIDGET;
				}
				else if(w_mode.equals("surface")) {
					this.MODE = WidgetMode.SURFACE;
				}
			}
			
			if(importxml.getChild("Settings").getChild("logging").getContent().equals("true")) {
				this.logging = true;
			}
			else if(importxml.getChild("Settings").getChild("logging").getContent().equals("false")) {
				this.logging = false;
			}
			
			this.loglevel = Integer.parseInt(importxml.getChild("Settings").getChild("loglevel").getContent());
			
			this.rel_path = importxml.getChild("Settings").getChild("rellogpath").getContent();
			
			String h1 = "false", h2 = "false", h3 = "false";
			String h_str = importxml.getChild("Settings").getChild("handlers").getContent();
			h1 = h_str.split(",")[0];
			h2 = h_str.split(",")[1];
			h3 = h_str.split(",")[2];
			
			if(h1.equals("true")) {
				BeastyWorld.HANDLERS[0] = true;
			}
			else if(h1.equals("false")) {
				BeastyWorld.HANDLERS[0] = false;
			}
			
			if(h2.equals("true")) {
				BeastyWorld.HANDLERS[1] = true;
			}
			else if(h2.equals("false")) {
				BeastyWorld.HANDLERS[1] = false;
			}
			
			if(h3.equals("true")) {
				BeastyWorld.HANDLERS[2] = true;
			}
			else if(h3.equals("false")) {
				BeastyWorld.HANDLERS[2] = false;
			}
			
			if(importxml.getChild("Settings").getChild("backgrounddisable").getContent().equals("true")) {
				this.disable_background = true;
			}
			else if(importxml.getChild("Settings").getChild("backgrounddisable").getContent().equals("false")) {
				this.disable_background = false;
			}
			
			this.BACKGROUNDCOLOR = importxml.getChild("Settings").getChild("backgroundcolor").getIntContent();
			
			if(!importxml.getChild("Settings").getChild("windowtitle").getContent().equals("default")) {
				String wt = importxml.getChild("Settings").getChild("windowtitle").getContent();
				this.setWindowName(wt);
			}
			
			XML printout_tag = importxml.getChild("Settings").getChild("printouts");
			XML[] po_children = printout_tag.getChildren();
			
			PApplet.println("");
			PApplet.println("********Import-Details********");
			for(XML c : po_children) {
				PApplet.println(c.getContent());
			}
			PApplet.println("******************************");
			PApplet.println("");
			
			if(dependencies) {
				XML dependencies_tag = importxml.getChild("Settings").getChild("dependencies");
				if(!dependencies_tag.getChild("iconpath").getContent().equals("default")) {
					String ip = dependencies_tag.getChild("iconpath").getContent();
					this.setWindowIcon(ip);
				}
				
				if(!dependencies_tag.getChild("ttfontpath").getContent().equals("default")) {
					String ttfp = dependencies_tag.getChild("ttfontpath").getContent();
					this.setTooltipFont(ttfp);
				}
				
				if(!dependencies_tag.getChild("fontpath").getContent().equals("default")) {
					String fp = dependencies_tag.getChild("fontpath").getContent();
					this.setFont(fp);
				}
			}
			
			if(w_mode.equals("widget")) {
				XML imp_widgets_tag = importxml.getChild("Layout").getChild("Settings");
				XML[] imp_widget_tags = imp_widgets_tag.getChildren();
				
				for(XML widget_xml : imp_widget_tags) {
					if(widget_xml.getString("type").equals("Button")) {
						Button b = new Button(this.REF);
						
						b.ID = widget_xml.getChild("id").getContent();
						b.positions[0] = widget_xml.getChild("xpos").getFloatContent();
						b.positions[1] = widget_xml.getChild("ypos").getFloatContent();
						b.sizes[0] = widget_xml.getChild("xsize").getFloatContent();
						b.sizes[1] = widget_xml.getChild("ysize").getFloatContent();
						
						if(widget_xml.getChild("round").getContent().equals("true")) {
							b.round = true;
						}
						else if(widget_xml.getChild("round").getContent().equals("false")) {
							b.round = false;
						}
						
						b.text = widget_xml.getChild("text").getContent();
						b.textSize = widget_xml.getChild("textsize").getFloatContent();
						b.outline = widget_xml.getChild("outline").getFloatContent();
						if(!widget_xml.getChild("hotkey").getContent().equals("none")) {
							b.hotkey = widget_xml.getChild("round").getIntContent();
						}
						
						b.tttextoffset[0] = widget_xml.getChild("textoffsetx").getFloatContent();
						b.tttextoffset[1] = widget_xml.getChild("textoffsety").getFloatContent();
						
						b.background = widget_xml.getChild("backgroundcolor").getIntContent();
						b.foreground = widget_xml.getChild("foregroundcolor").getIntContent();
						b.overcolor = widget_xml.getChild("overcolor").getIntContent();
						b.clickcolor = widget_xml.getChild("clickcolor").getIntContent();
						
						if(widget_xml.getChild("active").getContent().equals("true")) {
							b.active = true;
						}
						else if(widget_xml.getChild("active").getContent().equals("false")) {
							b.active = false;
						}
						
						if(widget_xml.getChild("visible").getContent().equals("true")) {
							b.visible = true;
						}
						else if(widget_xml.getChild("visible").getContent().equals("false")) {
							b.visible = false;
						}
						if(!widget_xml.getChild("tooltip").getChild("tttext").getContent().equals("none")) {
							b.tooltiptext = widget_xml.getChild("tooltip").getChild("tttext").getContent();
						}
						if(!widget_xml.getChild("tooltip").getChild("tttextsize").getContent().equals("none")) {
							b.tooltiptextsize = widget_xml.getChild("tooltip").getChild("tttextsize").getFloatContent();
						}
						b.ttpositions[0] = widget_xml.getChild("tooltip").getChild("ttposx").getFloatContent();
						b.ttpositions[1] = widget_xml.getChild("tooltip").getChild("ttposy").getFloatContent();
						b.ttsizes[0] = widget_xml.getChild("tooltip").getChild("ttsizex").getFloatContent();
						b.ttsizes[1] = widget_xml.getChild("tooltip").getChild("ttsizey").getFloatContent();
						b.ttforeground = widget_xml.getChild("tooltip").getChild("ttforeground").getIntContent();
						b.ttbackground = widget_xml.getChild("tooltip").getChild("ttbackground").getIntContent();
						
						if(widget_xml.getChild("tooltip").getChild("roundtt").getContent().equals("true")) {
							b.roundtt = true;
						}
						else if(widget_xml.getChild("tooltip").getChild("roundtt").getContent().equals("false")) {
							b.roundtt = false;
						}
						
						b.intervall = widget_xml.getChild("tooltip").getChild("intervall").getFloatContent();
						b.tttextoffset[0] = widget_xml.getChild("tooltip").getChild("tttextoffsetx").getFloatContent();
						b.tttextoffset[1] = widget_xml.getChild("tooltip").getChild("tttextoffsety").getFloatContent();
						
						if(widget_xml.getChild("tooltip").getChild("enabled").getContent().equals("true")) {
							b.tooltip_enabled = true;
						}
						else if(widget_xml.getChild("tooltip").getChild("enabled").getContent().equals("false")) {
							b.tooltip_enabled = false;
						}
						
						if(dependencies) {
							if(!widget_xml.getChild("dependencies").getChild("onleftclick").getContent().equals("none")) {
								b.olc = widget_xml.getChild("dependencies").getChild("onleftclick").getContent();
							}
							
							if(!widget_xml.getChild("dependencies").getChild("onmiddleclick").getContent().equals("none")) {
								b.omc = widget_xml.getChild("dependencies").getChild("onmiddleclick").getContent();
							}
							
							if(!widget_xml.getChild("dependencies").getChild("onrightclick").getContent().equals("none")) {
								b.orc = widget_xml.getChild("dependencies").getChild("onrightclick").getContent();
							}
							
							if(!widget_xml.getChild("dependencies").getChild("fontpath").getContent().equals("default")) {
								String fpath = widget_xml.getChild("dependencies").getChild("fontpath").getContent();
								b.setFont(fpath);
							}
							
							if(!widget_xml.getChild("dependencies").getChild("ttfontpath").getContent().equals("default")) {
								String ttfpath = widget_xml.getChild("dependencies").getChild("ttfontpath").getContent();
								b.setTooltipFont(ttfpath);
							}
						}
					}
					else if(widget_xml.getString("type").equals("Checkbox")) {
						
					}
				}
			}
			else if(w_mode.equals("surface")) {
				String imp_renderpage = importxml.getChild("Settings").getChild("renderpage").getContent();
				
				
				
				//set renderpage to imp_renderpage
				this.renderpage = this.get_surface_by_id(imp_renderpage);
			}
			
		}
		catch(Exception e) {
			PApplet.println("WARNING(BB): COULD NOT IMPORT LAYOUT -> CONTINUEING WITHOUT IMPORTING");
		}
	}
	
	
	
	//******TRANSITION METHODS******
	
	//get the id of the renderpage at the moment of calling this method
	@Experimental
	public String get_renderpage_id() {
		if(this.MODE == WidgetMode.SURFACE) {
			return this.renderpage.ID;
		}
		throw new RuntimeException("renderpage is not supported in Widgetmode");
	}
	
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
	@NoDocumentation
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
	@NoDocumentation
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
			//@SuppressWarnings("unused")
			//Logthread logthread = new Logthread(this.REF, this.rel_path, this.loglist);
			this.savelog();
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
							if(w instanceof Button || w instanceof Checkbox || w instanceof Inputfield) {
								if(w.over(e.getX(), e.getY())) {
									if(w.active) {
										w.rendercolor = w.clickcolor;
										//w.trueClick = true;
										w.mouse_pressed_down_over = true;
									}
								}
								else {
									if(w.active) {
										w.rendercolor = w.background;
										//w.trueClick = false;
										w.mouse_pressed_down_over = false;
									}
								}
							}
						}
					}
					else if(this.MODE == WidgetMode.SURFACE) {
						for(Widget w : this.renderpage.widgets) {
							if(w instanceof Button || w instanceof Checkbox || w instanceof Inputfield) {
								if(w.over(e.getX(), e.getY())) {
									if(w.active) {
										w.rendercolor = w.clickcolor;
										//w.trueClick = true;
										w.mouse_pressed_down_over = true;
									}
								}
								else {
									if(w.active) {
										w.rendercolor = w.background;
										//w.trueClick = false;
										w.mouse_pressed_down_over = false;
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
										//w.mouse_pressed_down_over = true;
									}
								}
								else {
									if(w.active) {
										w.rendercolor = w.background;
										//w.trueClick = false;
										//w.mouse_pressed_down_over = false;
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
										//w.mouse_pressed_down_over = true;
									}
								}
								else {
									if(w.active) {
										w.rendercolor = w.background;
										//w.trueClick = false;
										//w.mouse_pressed_down_over = false;
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
											if(w.mouse_pressed_down_over) {
												Button b = (Button)w;
												b.clicks_since_start+=1;
												if(this.logging) {
													this.writelogline(b.ID, "Button", "(mousehandler)left clicked");
												}
												if(b.olc != null) {
													this.REF.method(b.olc);
													//PApplet.println("Widget one");
												}
											}
										}
										else if(w instanceof Checkbox) {
											if(w.mouse_pressed_down_over) {
												Checkbox c = (Checkbox)w;
												c.clicks_since_start+=1;
												if(c.state) {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)unchecked");
													}
													if(c.ou != null) {
														this.REF.method(c.ou);
													}
												}
												else {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)checked");
													}
													if(c.oc != null) {
														this.REF.method(c.oc);
													}
												}
											}
										}
										else if(w instanceof Inputfield) {
											if(w.mouse_pressed_down_over) {
												Inputfield i = (Inputfield)w;
												i.clicks_since_start++;
												if(this.logging) {
													this.writelogline(i.ID, "Inputfield", "(mousehandler)input activated");
												}
												i.input = true;
											}
										}
									}
									
									//middle button
									else if(e.getButton() == PConstants.CENTER) {
										if(w instanceof Button) {
											if(w.mouse_pressed_down_over) {
												Button b = (Button)w;
												b.clicks_since_start+=1;
												if(this.logging) {
													this.writelogline(b.ID, "Button", "(mousehandler)middle clicked");
												}
												if(b.omc != null) {
													this.REF.method(b.omc);
												}
											}
										}
										else if(w instanceof Checkbox) {
											if(w.mouse_pressed_down_over) {
												Checkbox c = (Checkbox)w;
												c.clicks_since_start+=1;
												if(c.state) {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)unchecked");
													}
													if(c.ou != null) {
														this.REF.method(c.ou);
													}
												}
												else {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)checked");
													}
													if(c.oc != null) {
														this.REF.method(c.oc);
													}
												}
											}
										}
										else if(w instanceof Inputfield) {
											if(w.mouse_pressed_down_over) {
												Inputfield i = (Inputfield)w;
												i.clicks_since_start++;
												if(this.logging) {
													this.writelogline(i.ID, "Inputfield", "(mousehandler)input activated");
												}
												i.input = true;
											}
										}
									}
									
									//right button
									else if(e.getButton() == PConstants.RIGHT) {
										if(w instanceof Button) {
											if(w.mouse_pressed_down_over) {
												Button b = (Button)w;
												b.clicks_since_start+=1;
												if(this.logging) {
													this.writelogline(b.ID, "Button", "(mousehandler)right clicked");
												}
												if(b.orc != null) {
													this.REF.method(b.orc);
												}
											}
										}
										else if (w instanceof Checkbox) {
											if(w.mouse_pressed_down_over) {
												Checkbox c = (Checkbox)w;
												c.clicks_since_start+=1;
												if(c.state) {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)unchecked");
													}
													if(c.ou != null) {
														this.REF.method(c.ou);
													}
												}
												else {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)checked");
													}
													if(c.oc != null) {
														this.REF.method(c.oc);
													}
												}
											}
										}
										else if(w instanceof Inputfield) {
											if(w.mouse_pressed_down_over) {
												Inputfield i = (Inputfield)w;
												i.clicks_since_start++;
												if(this.logging) {
													this.writelogline(i.ID, "Inputfield", "(mousehandler)input activated");
												}
												i.input = true;
											}
										}
									}
									w.mouse_pressed_down_over = false;
								}
							}
							else {
								if(w instanceof Inputfield) {
									if(w.active) {
										Inputfield i = (Inputfield)w;
										if(this.logging) {
											if(i.input) {
												this.writelogline(i.ID, "Inputfield", "(mousehandler)input deactivated");
											}
										}
										i.input = false;
									}
								}
								if(w.active) {
									w.mouse_pressed_down_over = false;
								}
							}
						}
						if(this.tabswitch_user_enabled) {
							this.unregisterHandler("tabswitch");
							for(Widget w : this.widgets) {
								w.rendercolor = w.background;
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
											if(w.mouse_pressed_down_over) {
												Button b = (Button)w;
												b.clicks_since_start+=1;
												if(this.logging) {
													this.writelogline(b.ID, "Button", "(mousehandler)left clicked");
												}
												if(b.olc != null) {
													this.REF.method(b.olc);
													//PApplet.println("Surface one");
												}
											}
										}
										else if(w instanceof Checkbox) {
											if(w.mouse_pressed_down_over) {
												Checkbox c = (Checkbox)w;
												c.clicks_since_start+=1;
												if(c.state) {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)unchecked");
													}
													if(c.ou != null) {
														this.REF.method(c.ou);
													}
												}
												else {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)checked");
													}
													if(c.oc != null) {
														this.REF.method(c.oc);
													}
												}
											}
										}
										else if(w instanceof Inputfield) {
											if(w.mouse_pressed_down_over) {
												Inputfield i = (Inputfield)w;
												i.clicks_since_start++;
												if(this.logging) {
													this.writelogline(i.ID, "Inputfield", "(mousehandler)input activated");
												}
												i.input = true;
											}
										}
									}
									
									//middle button
									else if(e.getButton() == PConstants.CENTER) {
										if(w instanceof Button) {
											if(w.mouse_pressed_down_over) {
												Button b = (Button)w;
												b.clicks_since_start+=1;
												if(this.logging) {
													this.writelogline(b.ID, "Button", "(mousehandler)middle clicked");
												}
												if(b.omc != null) {
													this.REF.method(b.omc);
												}
											}
										}
										else if(w instanceof Checkbox) {
											if(w.mouse_pressed_down_over) {
												Checkbox c = (Checkbox)w;
												c.clicks_since_start+=1;
												if(c.state) {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)unchecked");
													}
													if(c.ou != null) {
														this.REF.method(c.ou);
													}
												}
												else {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)checked");
													}
													if(c.oc != null) {
														this.REF.method(c.oc);
													}
												}
											}
										}
										else if(w instanceof Inputfield) {
											if(w.mouse_pressed_down_over) {
												Inputfield i = (Inputfield)w;
												i.clicks_since_start++;
												if(this.logging) {
													this.writelogline(i.ID, "Inputfield", "(mousehandler)input activated");
												}
												i.input = true;
											}
										}
									}
									
									//right button
									else if(e.getButton() == PConstants.RIGHT) {
										if(w instanceof Button) {
											if(w.mouse_pressed_down_over) {
												Button b = (Button)w;
												b.clicks_since_start+=1;
												if(this.logging) {
													this.writelogline(b.ID, "Button", "(mousehandler)right clicked");
												}
												if(b.orc != null) {
													this.REF.method(b.orc);
												}
											}
										}
										else if (w instanceof Checkbox) {
											if(w.mouse_pressed_down_over) {
												Checkbox c = (Checkbox)w;
												c.clicks_since_start+=1;
												if(c.state) {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)unchecked");
													}
													if(c.ou != null) {
														this.REF.method(c.ou);
													}
												}
												else {
													c.state = !c.state;
													if(this.logging) {
														this.writelogline(c.ID, "Checkbox", "(mousehandler)checked");
													}
													if(c.oc != null) {
														this.REF.method(c.oc);
													}
												}
											}
										}
										else if(w instanceof Inputfield) {
											if(w.mouse_pressed_down_over) {
												Inputfield i = (Inputfield)w;
												i.clicks_since_start++;
												if(this.logging) {
													this.writelogline(i.ID, "Inputfield", "(mousehandler)input activated");
												}
												i.input = true;
											}
										}
									}
									w.mouse_pressed_down_over = false;
								}
							}
							else {
								if(w instanceof Inputfield) {
									if(w.active) {
										Inputfield i = (Inputfield)w;
										if(this.logging) {
											if(i.input) {
												this.writelogline(i.ID, "Inputfield", "(mousehandler)input deactivated");
											}
										}
										i.input = false;
										w.mouse_pressed_down_over = false;
									}
								}
							}
						}
						if(this.tabswitch_user_enabled) {
							this.unregisterHandler("tabswitch");
							for(Widget w : this.renderpage.widgets) {
								w.rendercolor = w.background;
							}
						}
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
									if(i.input_text.length() < i.limit_input_length) {
										this.writelogline(i.ID, "Inputfield", "input: " + e.getKey());
									}
								}
								else {
									if(BeastyWorld.HANDLERS[1]) {
										if(e.getKeyCode() == i.hotkey){
											i.input = true;
											this.writelogline(i.ID, "Inputfield", "(hotkeyhandler)input activated");
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
									if(i.input_text.length() < i.limit_input_length) {
										this.writelogline(i.ID, "Inputfield", "input: " + e.getKey());
									}
								}
								else {
									if(BeastyWorld.HANDLERS[1]) {
										if(e.getKeyCode() == i.hotkey){
											i.input = true;
											this.writelogline(i.ID, "Inputfield", "(hotkeyhandler)input activated");
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
										if(this.logging) {
											this.writelogline(b.ID, "Button", "(hotkeyhandler)pressed");
										}
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
											if(this.logging) {
												this.writelogline(c.ID, "Checkbox", "(hotkeyhandler)unchecked");
											}
											if(c.ou != null) {
												this.REF.method(c.ou);
											}
										}
										else {
											c.state = !c.state;
											if(this.logging) {
												this.writelogline(c.ID, "Checkbox", "(hotkeyhandler)checked");
											}
											if(c.oc != null) {
												this.REF.method(c.oc);
											}
										}
									}
								}
							}
						}
						if(e.getKeyCode() != PApplet.TAB || e.getKeyCode() != PApplet.ENTER) {
							if(this.tabswitch_user_enabled) {
								this.unregisterHandler("tabswitch");
								for(Widget w : this.widgets) {
									w.rendercolor = w.background;
								}
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
										if(this.logging) {
											this.writelogline(b.ID, "Button", "(hotkeyhandler)pressed");
										}
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
											if(this.logging) {
												this.writelogline(c.ID, "Checkbox", "(hotkeyhandler)unchecked");
											}
											if(c.ou != null) {
												this.REF.method(c.ou);
											}
										}
										else {
											c.state = !c.state;
											if(this.logging) {
												this.writelogline(c.ID, "Checkbox", "(hotkeyhandler)checked");
											}
											if(c.oc != null) {
												this.REF.method(c.oc);
											}
										}
									}
								}
							}
						}
						if(e.getKeyCode() != PApplet.TAB || e.getKeyCode() != PApplet.ENTER) {
							if(this.tabswitch_user_enabled) {
								this.unregisterHandler("tabswitch");
								for(Widget w : this.renderpage.widgets) {
									w.rendercolor = w.background;
								}
							}
						}
					}
				}
			}
			
			//Tabswitch
			if(this.tabswitch_user_enabled) {
				if(e.getAction() == KeyEvent.RELEASE) {
					PApplet.println("tab en");
					if(e.getKeyCode() == PApplet.TAB) {
						this.registerHandler("tabswitch");
						PApplet.println("tabswitch active");
					}
				}
			}
			
			if(BeastyWorld.HANDLERS[2]) {
				if(this.MODE == WidgetMode.WIDGET) {
					if(e.getAction() == KeyEvent.RELEASE) {
						if(e.getKeyCode() == PApplet.TAB) {
							this.registerHandler("tabswitch");
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
							
							for(Widget w : this.widgets) {
								if(w.active) {
									if(w instanceof Button) {
										if(w.tab_selected) {
											Button b = (Button)w;
											//b.clicks_since_start++;
											if(b.olc != null) {
												this.REF.method(b.olc);
											}
											this.unregisterHandler("tabswitch");
											for(Widget wi : this.widgets) {
												wi.rendercolor = wi.background;
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
												this.unregisterHandler("tabswitch");
												for(Widget wi : this.renderpage.widgets) {
													wi.rendercolor = wi.background;
												}
											}
											else {
												c.state = !c.state;
												if(c.oc != null) {
													this.REF.method(c.oc);
												}
												this.unregisterHandler("tabswitch");
												for(Widget wi : this.renderpage.widgets) {
													wi.rendercolor = wi.background;
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
							this.registerHandler("tabswitch");
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
							
							for(Widget w : this.renderpage.widgets) {
								if(w.active) {
									if(w instanceof Button) {
										if(w.tab_selected) {
											Button b = (Button)w;
											//b.clicks_since_start++;
											if(b.olc != null) {
												this.REF.method(b.olc);
											}
											this.unregisterHandler("tabswitch");
											for(Widget wi : this.renderpage.widgets) {
												wi.rendercolor = wi.background;
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
												this.unregisterHandler("tabswitch");
												for(Widget wi : this.renderpage.widgets) {
													wi.rendercolor = wi.background;
												}
											}
											else {
												c.state = !c.state;
												if(c.oc != null) {
													this.REF.method(c.oc);
												}
												this.unregisterHandler("tabswitch");
												for(Widget wi : this.renderpage.widgets) {
													wi.rendercolor = wi.background;
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
	protected void generate_tabswitchlist() {
		this.tabswitchlist = (ArrayList<Widget>) this.widgets.stream()
				.sorted(Comparator.comparing(widget -> widget.positions[1]))
				.collect(Collectors.toList());
		
	}
}
