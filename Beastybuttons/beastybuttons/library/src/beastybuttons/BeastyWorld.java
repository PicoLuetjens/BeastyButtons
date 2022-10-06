package beastybuttons;


import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

//imported for java color type -> issues because it has some same datatypes such as Checkbox(maybe better store color as int value)
//import java.awt.*;
//import java.io.File;
//import java.io.PrintWriter;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

//Top level Class that handles all structures of the project
public class BeastyWorld
{
    //sketch reference(protected because of Logthread access)
    protected final PApplet theparent;

    //instances of BeastyWorld
    private static int bw_instances = 0;

    //id
    private final String id = "world";


    //is BeastyButtons in run mode or in setup mode? used because setMethods should not be useble in at runtime
    protected static boolean running = false;

    //is running already active?
    private boolean runtime_set = false;

    //logging enabled or not
    private boolean logging = false;

    //the loglevel used when logging 0 -> log clicks on clickable widgets, 1 -> also include timestamps
    private int loglevel = 0;

    //the directory where to safe the logfile(protected because of Logthread access)
    protected String logfilepath;

    //holds the log lines(protected because of Logthread access)
    protected ArrayList<String>loglist = new ArrayList();

    //the directory where to safe the exportfile
    private String exportpath;

    //the directory of the sketch
    private String sketchpath;

    //if a layout was already imported
    private boolean already_imported = false;

    //the registered handlers that are active(mouse, hotkeys, tabswitch) -> mouse is enabled by default
    protected static boolean[] handlers = {true, false, false};

    //is the tabswitch handler displaying -> display is disabled when mouse is moved(if both are registered)
    protected static boolean tabswitch_displaying = false;

    //the font that is used for rendering with beastyButtons(experimental feature -> cause some Widgets like Inputfield rely
    //on information from the font like textWidth() and textHeight()
    private PFont font;

    //holds all ArrayLists of Widgets
    //private ArrayList<ArrayList>allWidgets = new ArrayList();

    //holds all Button_asots
    private ArrayList<Button_asot>button_asots = new ArrayList();

    //holds all Button_asoss
    private ArrayList<Button_asos>button_asoss = new ArrayList();

    //holds all Checkboxes
    private ArrayList<Checkbox>checkboxes = new ArrayList();

    //holds all label_asots
    private ArrayList<Label_asot>label_asots = new ArrayList();

    //holds all label_asoss
    private ArrayList<Label_asos>label_asoss = new ArrayList();

    //holds all Inputfields_asots
    private ArrayList<Inputfield_asot>inputfield_asots = new ArrayList();

    //holds all Inputfield_asoss
    private ArrayList<Inputfield_asos>inputfield_asoss = new ArrayList();

    //holds all Radiobutton_asots
    private ArrayList<Radiobutton_asot>radiobutton_asots = new ArrayList();

    //holds all Radiobutton_asoss
    private ArrayList<Radiobutton_asos>radiobutton_asoss = new ArrayList();

    //holds all Table_BBs
    private ArrayList<Table_BB>table_bbs = new ArrayList();

    //holds all Dropdownlist_asots
    private ArrayList<Dropdownlist_asot>dropdownlist_asots = new ArrayList();

    //holds all Dropdownlist_asoss
    private ArrayList<Dropdownlist_asos>dropdownlist_asoss = new ArrayList();

    //holds all Sliders
    private ArrayList<Slider>sliders = new ArrayList();

    //holds all surfaces
    private ArrayList<BeastySurface>surfaces = new ArrayList();

    //used in importing to compare against sketch resolution
    private int import_res_x, import_res_y;
    private int sketch_start_res_x, sketch_start_res_y;

    //the page that is currently displaying -> if mode is 1(using surfaces)
    private BeastySurface renderpage;

    //the page that is animated to -> if mode is 1
    private BeastySurface animateToPage = null;

    //the way the Widgets are added. 0 -> single objects, 1 -> surfaces. you cannot add surfaces and single widgets at the same time
    private int mode = 0;

    //virtual keyboard instance(only one)
    private VirtualKeyboard keyboard;

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

    //global sketch mouse variables
    protected int MOUSE_POS_X = 0, MOUSE_POS_Y = 0;

    //amount of available RenderLayers(layer 0 is on the bottom, layer 5 is on the top)
    private final int LAYERS = 5;

    //is an animation transition happening?
    protected static boolean isanimating = false;

    //the list of ids of the widgets based on position(up to down) for tabswitch handler
    protected ArrayList<String> tabswitch_list = new ArrayList();

    public BeastyWorld(PApplet parent)
    {
        if(!BeastyWorld.running)
        {
            this.theparent = parent;
            this.checkInstance();
            this.getSketchParameters();
            this.registermethods();
            //this.createDirectories();
            this.keyboard = new VirtualKeyboard(this.theparent);
        }
        else
        {
            throw new CostumRuntimeError("BeastyWorld cannot be instanced while runtime, try to instance before draw()");
        }
    }


    //**** CONFIGURATION METHODS - CALLED FROM THE CONSTRUCTOR ****

    //makes sure there is only one instance of BeastyWorld, else throws error
    private void checkInstance()
    {
        BeastyWorld.bw_instances+=1;

        if(BeastyWorld.bw_instances > 1)
        {
            //throw error -> there can only be one instance of BeastyWorld
            throw new CostumRuntimeError("There can only be one Instance of BeastyWorld");
        }
    }

    //gets the Sketchsize in x and y in pixels(saved for exports -> later compared against import sketch size, might throw Error)
    private void getSketchParameters()
    {
        this.sketchpath = this.theparent.sketchPath();
        this.sketch_start_res_x = this.theparent.width;
        this.sketch_start_res_y = this.theparent.height;
    }

    //creates directories for logging(BBLog) and export(BBExport) if they do not already exist
    /*private void createDirectories()
    {
        File sketchdir = new File(this.sketchpath);
        String[]files = sketchdir.list();
        boolean log = false, export = false;
        if(files.length > 0) {
            for (String s : files) {
                if (s.equals("BBLog")) {
                    log = true;
                }
                if (s.equals("BBExport")) {
                    export = true;
                }
            }
            if (!log) {
                File f = new File(sketchdir, "BBLog");
                if (!f.mkdir()) {
                    PApplet.println("WARNING(BB): COULD NOT CREATE LOG DIRECTORY");
                }
            }
            if (!export) {
                File ff = new File(sketchdir, "BBExport");
                if (!ff.mkdir()) {
                    PApplet.println("WARNING(BB): COULD NOT CREATE EXPORT DIRECTORY");
                }
            }
            File templog = new File(sketchdir, "BBLog");
            File tempexp = new File(sketchdir, "BBExport");
            this.logpath = templog.getPath();
            this.exportpath = tempexp.getPath();
        }
        else
        {
            File templog = new File(sketchdir, "BBLog");
            File tempexp = new File(sketchdir, "BBExport");
            this.logpath = templog.getPath();
            this.exportpath = tempexp.getPath();
        }
    }*/

    //this registers any used methods which are dispose() for logging, draw() for rendering on top, as well as keyEvent() and mouseEvent()
    private void registermethods()
    {
        this.theparent.registerMethod("dispose", this);
        this.theparent.registerMethod("draw", this);
        this.theparent.registerMethod("mouseEvent", this);
        this.theparent.registerMethod("keyEvent", this);
    }

    //called when a single Widget is added to make sure no surface is already added
    private void checkforsurface()
    {
        if(this.surfaces.size() > 0)
        {
            throw new CostumRuntimeError("Single Widgets and Surfaces cannot be added at the same time");
        }
        this.mode = 0;
    }




    //**** ADD METHODS ****

    //register a handler for triggering events("mouse", "hotkeys", "tabsnap") - multiple can be registered(hotkey input and tab from virtualkeyboard also valid)
    public void registerHandler(String handler)
    {
        PApplet.println("WARNING(BB): registerHandler() IS STILL HIGHLY EXPERIMENTAL, AVOID USE WHEN POSSIBLE");
        if(!BeastyWorld.running)
        {
            //register the handler
            if(handler.equals("mouse"))
            {
                BeastyWorld.handlers[0] = true;
            }
            else if(handler.equals("hotkeys"))
            {
                BeastyWorld.handlers[1] = true;
            }
            else if(handler.equals("tabswitch"))
            {
                BeastyWorld.handlers[2] = true;
            }
            else
            {
                PApplet.println("WARNING(BB): UNKNOWN HANDLER - COULD NOT REGISTER THE HANDLER");
            }
        }
    }

    //the same for unregistering
    public void unregisterHandler(String handler)
    {
        PApplet.println("WARNING(BB): unregisterHandler() IS STILL HIGHLY EXPERIMENTAL, AVOID USE WHEN POSSIBLE");
        if(!BeastyWorld.running)
        {
            //unregister the handler
            if(handler.equals("mouse"))
            {
                BeastyWorld.handlers[0] = false;
            }
            else if(handler.equals("hotkeys"))
            {
                BeastyWorld.handlers[1] = false;
            }
            else if(handler.equals("tabswitch"))
            {
                BeastyWorld.handlers[2] = false;
            }
            else
            {
                PApplet.println("WARNING(BB): UNKNOWN HANDLER - COULD NOT UNREGISTER THE HANDLER");
            }
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Button_asot button)
    {
        if(!BeastyWorld.running)
        {
            if(!button.already_added)
            {
                if(!button.id.equals(this.id))
                {
                    this.button_asots.add(button);
                    this.checkforsurface();
                    button.already_added = true;

                    //edit the sourcePath
                    button.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Button_asos button)
    {
        if(!BeastyWorld.running)
        {
            if(!button.already_added)
            {
                if(!button.id.equals(this.id))
                {
                    this.button_asoss.add(button);
                    this.checkforsurface();
                    button.already_added = true;

                    //edit the sourcePath
                    button.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Checkbox box)
    {
        if(!BeastyWorld.running)
        {
            if(!box.already_added)
            {
                if(!box.id.equals(this.id))
                {
                    this.checkboxes.add(box);
                    this.checkforsurface();
                    box.already_added = true;

                    //edit the sourcePath
                    box.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Label_asot label)
    {
        if(!BeastyWorld.running)
        {
            if(!label.already_added)
            {
                if(!label.id.equals(this.id))
                {
                    this.label_asots.add(label);
                    this.checkforsurface();
                    label.already_added = true;

                    //edit the sourcePath
                    label.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Label_asos label)
    {
        if(!BeastyWorld.running)
        {
            if(!label.already_added)
            {
                if(!label.id.equals(this.id))
                {
                    this.label_asoss.add(label);
                    this.checkforsurface();
                    label.already_added = true;

                    //edit the sourcePath
                    label.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Inputfield_asot field)
    {
        if(!BeastyWorld.running)
        {
            if(!field.already_added)
            {
                if(!field.id.equals(this.id))
                {
                    this.inputfield_asots.add(field);
                    this.checkforsurface();
                    field.already_added = true;

                    //edit the sourcePath
                    field.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Inputfield_asos field)
    {
        if(!BeastyWorld.running)
        {
            if(!field.already_added)
            {
                if(!field.id.equals(this.id))
                {
                    this.inputfield_asoss.add(field);
                    this.checkforsurface();
                    field.already_added = true;

                    //edit the sourcePath
                    field.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Radiobutton_asot radiobutton)
    {
        if(!BeastyWorld.running)
        {
            if(!radiobutton.already_added)
            {
                if(!radiobutton.id.equals(this.id))
                {
                    this.radiobutton_asots.add(radiobutton);
                    this.checkforsurface();
                    radiobutton.already_added = true;

                    //edit the sourcePath
                    radiobutton.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Radiobutton_asos radiobutton)
    {
        if(!BeastyWorld.running)
        {
            if(!radiobutton.already_added)
            {
                if(!radiobutton.id.equals(this.id))
                {
                    this.radiobutton_asoss.add(radiobutton);
                    this.checkforsurface();
                    radiobutton.already_added = true;

                    //edit the sourcePath
                    radiobutton.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Table_BB table)
    {
        if(!BeastyWorld.running)
        {
            if(!table.already_added)
            {
                if(!table.id.equals(this.id))
                {
                    this.table_bbs.add(table);
                    this.checkforsurface();
                    table.already_added = true;

                    //edit the sourcePath
                    table.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Dropdownlist_asot dropdownlist)
    {
        if(!BeastyWorld.running)
        {
            if(!dropdownlist.already_added)
            {
                if(!dropdownlist.id.equals(this.id))
                {
                    this.dropdownlist_asots.add(dropdownlist);
                    this.checkforsurface();
                    dropdownlist.already_added = true;

                    //edit the sourcePath
                    dropdownlist.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Dropdownlist_asos dropdownlist)
    {
        if(!BeastyWorld.running)
        {
            if(!dropdownlist.already_added)
            {
                if(!dropdownlist.id.equals(this.id))
                {
                    this.dropdownlist_asoss.add(dropdownlist);
                    this.checkforsurface();
                    dropdownlist.already_added = true;

                    //edit the sourcePath
                    dropdownlist.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addWidget() is for single Widgets only -> if you have Surfaces add them to Surfaces and use the addSurface()
    public void addWidget(Slider slider)
    {
        if(!BeastyWorld.running)
        {
            if(!slider.already_added)
            {
                if(!slider.id.equals(this.id))
                {
                    this.sliders.add(slider);
                    this.checkforsurface();
                    slider.already_added = true;

                    //edit the sourcePath
                    slider.editSourcepath("add", this.id);
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                }
            }
            else
            {
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED MULTIPLE TIMES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //addSurface is for surfaces and not for single Widgets
    public void addSurface(BeastySurface surface)
    {
        if(!BeastyWorld.running)
        {
            //check if there are already single Widgets added
            if(this.button_asots.size() == 0 && this.button_asoss.size() == 0 && this.checkboxes.size() == 0 && this.label_asots.size() == 0 && this.label_asoss.size()
            == 0 && this.inputfield_asots.size() == 0 && this.inputfield_asoss.size() == 0 && this.radiobutton_asots.size() == 0 && this.radiobutton_asoss.size() == 0 &&
            this.dropdownlist_asots.size() == 0 && this.dropdownlist_asoss.size() == 0 && this.table_bbs.size() == 0 && this.sliders.size() == 0)
            {

                if (!surface.already_added) {
                    if (!surface.id.equals(this.id)) {
                        this.surfaces.add(surface);

                        //set the mode to surface objects
                        this.mode = 1;

                        //set the renderpage to this by default
                        this.renderpage = surface;

                        //edit the sourcepath of the surface to be "world/surfaceid"
                        surface.editSourcepath("add", this.id);
                    }
                    else
                    {
                        PApplet.println("WARNING(BB): WIDGETS CANNOT HAVE THE SAME ID. THIS WILL CAUSE IMPORT ISSUES");
                    }
                }
                else
                {
                    PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
                }
            }
            else
            {
                throw new CostumRuntimeError("Single Widgets and Surfaces cannot be added at the same time");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }




    //**** SET METHODS ****

    //sets the Font that is used for rendering with BeastyButtons(experimental)
    //when you set the font after instanzing widgets the alignment of text might be off and you have to manualy re adjust
    public void setFont(PFont f)
    {
        PApplet.println("WARNING(BB): setFont() IS STILL HIGHLY EXPERIMENTAL, AVOID USE WHEN POSSIBLE");
        if(!BeastyWorld.running)
        {
            this.font = f;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME");
        }
    }

    //this searches for the Widget and returns it(for linking after import if callback is needed)
    //if the path exists but it is a different class type its not found so make sure to use the right one!!!
    public Button_asot get_Button_asot_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Button_asot b : surface.button_asots)
                {
                    if (b.sourcePath.equals(path))
                    {
                        return b;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Button_asot b : this.button_asots)
            {
                if(b.sourcePath.equals(path))
                {
                    return b;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Button_asos get_Button_asos_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Button_asos b : surface.button_asoss)
                {
                    if (b.sourcePath.equals(path))
                    {
                        return b;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Button_asos b : this.button_asoss)
            {
                if(b.sourcePath.equals(path))
                {
                    return b;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Checkbox get_Checkbox_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Checkbox c : surface.checkboxes)
                {
                    if (c.sourcePath.equals(path))
                    {
                        return c;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Checkbox c : this.checkboxes)
            {
                if(c.sourcePath.equals(path))
                {
                    return c;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Label_asot get_Label_asot_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Label_asot l : surface.label_asots)
                {
                    if (l.sourcePath.equals(path))
                    {
                        return l;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Label_asot l : this.label_asots)
            {
                if(l.sourcePath.equals(path))
                {
                    return l;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Label_asos get_Label_asos_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Label_asos l : surface.label_asoss)
                {
                    if (l.sourcePath.equals(path))
                    {
                        return l;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Label_asos l : this.label_asoss)
            {
                if(l.sourcePath.equals(path))
                {
                    return l;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Inputfield_asot get_Inputfield_asot_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Inputfield_asot i : surface.inputfield_asots)
                {
                    if (i.sourcePath.equals(path))
                    {
                        return i;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Inputfield_asot i : this.inputfield_asots)
            {
                if(i.sourcePath.equals(path))
                {
                    return i;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Inputfield_asos get_Inputfield_asos_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Inputfield_asos i : surface.inputfield_asoss)
                {
                    if (i.sourcePath.equals(path))
                    {
                        return i;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Inputfield_asos i : this.inputfield_asoss)
            {
                if(i.sourcePath.equals(path))
                {
                    return i;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Radiobutton_asot get_Radiobutton_asot_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Radiobutton_asot r : surface.radiobutton_asots)
                {
                    if (r.sourcePath.equals(path))
                    {
                        return r;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Radiobutton_asot r : this.radiobutton_asots)
            {
                if(r.sourcePath.equals(path))
                {
                    return r;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Radiobutton_asos get_Radiobutton_asos_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Radiobutton_asos r : surface.radiobutton_asoss)
                {
                    if (r.sourcePath.equals(path))
                    {
                        return r;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Radiobutton_asos r : this.radiobutton_asoss)
            {
                if(r.sourcePath.equals(path))
                {
                    return r;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Dropdownlist_asot get_Dropdownlist_asot_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Dropdownlist_asot d : surface.dropdownlist_asots)
                {
                    if (d.sourcePath.equals(path))
                    {
                        return d;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Dropdownlist_asot d : this.dropdownlist_asots)
            {
                if(d.sourcePath.equals(path))
                {
                    return d;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Dropdownlist_asos get_Dropdownlist_asos_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Dropdownlist_asos d : surface.dropdownlist_asoss)
                {
                    if (d.sourcePath.equals(path))
                    {
                        return d;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Dropdownlist_asos d : this.dropdownlist_asoss)
            {
                if(d.sourcePath.equals(path))
                {
                    return d;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }
    
    public Table_BB get_Table_BB_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Table_BB t : surface.table_bbs)
                {
                    if (t.sourcePath.equals(path))
                    {
                        return t;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Table_BB t : this.table_bbs)
            {
                if(t.sourcePath.equals(path))
                {
                    return t;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public Slider get_Slider_BB_by_path(String path)
    {
        //is it located in a surface instance?("world/surface/widget")
        if(path.split("/").length == 3)
        {
            for (BeastySurface surface : this.surfaces)
            {
                for (Slider s : surface.sliders)
                {
                    if (s.sourcePath.equals(path))
                    {
                        return s;
                    }
                }
            }
        }
        //located in world instance?("world/widget")
        else if(path.split("/").length == 2)
        {
            for(Slider s : this.sliders)
            {
                if(s.sourcePath.equals(path))
                {
                    return s;
                }
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }

    public BeastySurface get_BeastySurface_by_path(String path)
    {
        for(BeastySurface s : this.surfaces)
        {
            if(s.sourcePath.equals(path))
            {
                return s;
            }
        }
        PApplet.println("WARNING(BB): NO WIDGET FOUND FOR{"+path+"}. WIDGET MAY BE A DIFFERENT CLASS TYPE");
        return null;
    }


    //**** LOGGING METHODS ****

    //enables logging
    public void enableLogging(int loglevel, String logfilepath)
    {
        if(!this.logging)
        {
            if(loglevel == 0 || loglevel == 1)
            {
                this.logging = true;
                this.loglevel = loglevel;
                this.logfilepath = logfilepath + ".beasty";

                LocalDate date = LocalDate.now();
                LocalTime time = LocalTime.now();

                this.loglist.add("***** LOGGING STARTED - " + date + " " + time + " *****");
                this.loglist.add("");
                this.loglist.add("");
            }
            else
            {
                PApplet.println("WARNING(BB): UNKNOWN LOGLEVEL - LOGGING WAS DISABLED AUTOMATICALLY");
                this.logging = false;
            }
        }
        else
        {
            PApplet.println("WARNING(BB): LOGGING IS ALREADY ENABLED");
        }
    }

    //disables logging manually
    public void disableLogging()
    {
        if(this.logging)
        {
            this.logging = false;

            //get the Date and the Time
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();

            this.loglist.add("***** LOGGING FINISHED - " + date + " " + time + " *****");
            this.loglist.add("");
            this.loglist.add("");

            //save file in another Thread because this is mostly called at runtime -> performance
            Logthread filesave = new Logthread(this);
            filesave.start();
        }
        else
        {
            PApplet.println("WARNING(BB): LOGGING IS ALREADY DISABLED");
        }
    }

    //called internally when a new log entry has to be made
    //since this is called at "runtime" this method has to be called from another thread to ensure performance speed
    private void writelogline(String id, String classtype, String action)
    {
        try
        {
            //write a logfile line in this style
            //type here means like "Button_asot" or "Inputfield_asos"
            // loglevel 0: id -> "clicked" or for inputfield: id ->
            // loglevel 1: 12-12-30 -> id[classtype] -> "clicked" or for dropdownlist: 12-12-30 -> id[classtype] -> option -> "selected"
            //..
            //..
            //..
            if(this.loglevel == 0)
            {
                this.loglist.add(id + " -> " + action);
            }
            else if(this.loglevel == 1)
            {
                //get the Date and the Time
                LocalTime time = LocalTime.now();
                LocalDate date = LocalDate.now();

                this.loglist.add(date + " - " + time + " -> " + id + "[" + classtype + "] -> " + action);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            PApplet.println("WARNING(BB): COULD NOT WRITE LOG LINE TO FILE");
        }
    }


    //writes an export file in another thread and raises a Warning when called from draw, must be called before draw
    /*public void exportLayout(String filename, String alignment)
    {
        if(!BeastyWorld.running)
        {
            if(alignment.equals("fixed"))
            {
                //store position in fixed values
            }
            else if(alignment.equals("relative"))
            {
                //store position values in relative to window size
            }
        }
        else
        {
            PApplet.println("WARNING(BB): COULD NOT EXPORT AT RUNTIME, TRY EXPORT BEFORE DRAW()");
        }
    }*/

    //imports a .beasty file and creates the structure in the BeastyWorld instance
    // to reference Widgets and Surfaces use the get_Element_by_path() method
    /*public void importLayout(String filename)
    {
        //set this to a new instance and completely erase everything assigned with this instance before(if import is called twice or something)
        if(!this.already_imported)
        {
            this.already_imported = true;

            //test if it is a .beasty file
            if (!filename.split(".")[1].equals("beasty"))
            {
                PApplet.println("WARNING(BB): IMPORT ERROR - UNKNOWN FILETYPE");
            }
            else
            {
                //import all
            }
        }
        else
        {
            PApplet.println("WARNING(BB): CANNOT IMPORT A SECOND LAYOUT");
        }
    }*/



    //**** TRANSITION METHODS -> ONLY WORKS IN SURFACES MODE WHEN SURFACES ARE ADDED ****

    //jump cut to another surface
    /*public void set_Surface_cut_transition(String surfaceID)
    {
        if(this.mode == 1)
        {
            boolean found = false;
            for (BeastySurface surface : this.surfaces) {
                if (surface.id.equals(surfaceID)) {
                    this.renderpage = surface;
                    found = true;
                }
            }
            if (!found) {
                PApplet.println("WARNING(BB): NO ID MATCHING SURFACE FOUND - COULD NOT CHANGE SURFACE");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): RUNNING IN SINGLE WIDGET MODE - COULD NOT CHANGE SURFACES IN THIS MODE");
        }
    }*/

    //opacity cut to another surface -> do this in another thread to ensure draw() running while transition happens
    /*public void set_Surface_opacity_transition(String surfaceID, float operatingtime)
    {
        //test if surface mode

        //set isanimating = true

        //disable all widgets

        //disable all tooltips

        //do transition in time

        //enable all widgets

        //set isanimating = false
    }*/


    //swipe cut to another surface -> do this in another thread to ensure draw() running while transition happens
    /*public void set_Surface_swipe_transition_up(String surfaceID, float operatingtime, String direction)
    {
        //test if surface mode

        //set isanimating = true

        //disable all widgets

        //disable all tooltips

        //do transition in time

        //enable all widgets

        //set isanimating = false
    }*/


    //**** REGISTERED METHODS -> AUTOMATICALLY CALLED BY PROCESSING ****

    //called before closing(if logging is enabled close the logfile in here)
    public void dispose()
    {
        if(this.logging)
        {
            //not necessary but for logic reasons keep here
            this.logging = false;

            //get the Date and the Time
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();

            this.loglist.add("***** LOGGING FINISHED - " + date + " " + time + " *****");
            this.loglist.add("");
            this.loglist.add("");

            //dont need the Logthread here, because this is called on closing the Application
            String[]save = new String[this.loglist.size()];
            for(int i = 0; i < this.loglist.size(); i++)
            {
                save[i] = this.loglist.get(i);
            }
            this.theparent.saveStrings(this.logfilepath, save);
        }
    }

    //checks if any loglines are available and passes them to this logline array
    private void checkforlog()
    {
        //single widgets
        if(this.mode == 0)
        {
            for(Button_asot b : this.button_asots){
               if(b.logline[0] != null){
                   this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
               }
            }

            for(Button_asos b : this.button_asoss){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Checkbox b : this.checkboxes){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Dropdownlist_asot b : this.dropdownlist_asots){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Dropdownlist_asos b : this.dropdownlist_asoss){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Inputfield_asot b : this.inputfield_asots){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Inputfield_asos b : this.inputfield_asoss){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Label_asot b : this.label_asots){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Label_asos b : this.label_asoss){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Radiobutton_asot b : this.radiobutton_asots){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Radiobutton_asos b : this.radiobutton_asoss){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Slider b : this.sliders){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }

            for(Table_BB b : this.table_bbs){
                if(b.logline[0] != null){
                    this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                }
            }
        }

        //surfaces
        else if(this.mode == 1)
        {
            if(!BeastyWorld.isanimating)
            {
                for(Button_asot b : this.renderpage.button_asots){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Button_asos b : this.renderpage.button_asoss){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Checkbox b : this.renderpage.checkboxes){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Dropdownlist_asot b : this.renderpage.dropdownlist_asots){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Dropdownlist_asos b : this.renderpage.dropdownlist_asoss){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Inputfield_asot b : this.renderpage.inputfield_asots){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Inputfield_asos b : this.renderpage.inputfield_asoss){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Label_asot b : this.renderpage.label_asots){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Label_asos b : this.renderpage.label_asoss){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Radiobutton_asot b : this.renderpage.radiobutton_asots){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Radiobutton_asos b : this.renderpage.radiobutton_asoss){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Slider b : this.renderpage.sliders){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }

                for(Table_BB b : this.renderpage.table_bbs){
                    if(b.logline[0] != null){
                        this.writelogline(b.logline[0], b.logline[1], b.logline[2]);
                    }
                }
            }
        }
    }

    //generates the tabswitchlist for every surface
    private void generate_tabswitchlist()
    {
        if(this.mode == 0){
            float highest_pos = 10000.0f;
            if(this.button_asots.size() > 0){
                for(Button_asot b : this.button_asots){

                }
            }
        }
    }

    //render function for all widgets(called from draw) take care of the animateTOsurface to render too if this is not null
    private void render()
    {
        //get all values to be stored aside
        this.beforetextsize = this.theparent.g.textSize;
        this.beforestrokeweight = this.theparent.g.strokeWeight;
        this.beforecolor = this.theparent.g.fillColor;
        this.beforefont = this.theparent.g.textFont;
        this.beforecolormode = this.theparent.g.colorMode;
        this.beforeimagemode = this.theparent.g.imageMode;
        this.beforeellipsemode = this.theparent.g.ellipseMode;
        this.beforerectmode = this.theparent.g.rectMode;
        this.beforetextalign = this.theparent.g.textAlign;
        this.beforestroke = this.theparent.g.stroke;
        this.beforestrokecolor = this.theparent.g.strokeColor;

        //override values for rendering
        this.theparent.noStroke();
        this.theparent.colorMode(PConstants.ARGB, 255, 255, 255, 255);
        this.theparent.rectMode(PConstants.CENTER);
        this.theparent.imageMode(PConstants.CENTER);
        //this.theparent.textFont(this.font);
        this.theparent.ellipseMode(PConstants.CENTER);
        this.theparent.textAlign(PConstants.CENTER);


        //when tab handler is displayed then disable the cursor
        //MOVE TO MOUSEPRESSED AND KEYPRESSED
        if(BeastyWorld.tabswitch_displaying)
        {
            this.theparent.noCursor();
        }
        else
        {
            this.theparent.cursor();
        }


        if(this.mode == 0)
        {
            for(int i = 0; i < this.LAYERS; i++)
            {
                if(this.button_asots.size() > 0){
                    for(Button_asot b : this.button_asots){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.button_asoss.size() > 0){
                    for(Button_asos b : this.button_asoss){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.checkboxes.size() > 0){
                    for(Checkbox b : this.checkboxes){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.dropdownlist_asots.size() > 0){
                    for(Dropdownlist_asot b : this.dropdownlist_asots){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.dropdownlist_asoss.size() > 0){
                    for(Dropdownlist_asos b : this.dropdownlist_asoss){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.inputfield_asots.size() > 0){
                    for(Inputfield_asot b : this.inputfield_asots){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.inputfield_asoss.size() > 0){
                    for(Inputfield_asos b : this.inputfield_asoss){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.label_asots.size() > 0){
                    for(Label_asot b : this.label_asots){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.label_asoss.size() > 0){
                    for(Label_asos b : this.label_asoss){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.radiobutton_asots.size() > 0){
                    for(Radiobutton_asot b : this.radiobutton_asots){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.radiobutton_asoss.size() > 0){
                    for(Radiobutton_asos b : this.radiobutton_asoss){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.sliders.size() > 0){
                    for(Slider b : this.sliders){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.table_bbs.size() > 0){
                    for(Table_BB b : this.table_bbs){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }
            }
        }
        else if(this.mode == 1)
        {
            this.renderpage.render();

            for(int i = 0; i < this.LAYERS; i++)
            {
                if(this.renderpage.button_asots.size() > 0){
                    for(Button_asot b : this.renderpage.button_asots){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.button_asoss.size() > 0){
                    for(Button_asos b : this.renderpage.button_asoss){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.checkboxes.size() > 0){
                    for(Checkbox b : this.renderpage.checkboxes){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.dropdownlist_asots.size() > 0){
                    for(Dropdownlist_asot b : this.renderpage.dropdownlist_asots){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.dropdownlist_asoss.size() > 0){
                    for(Dropdownlist_asos b : this.renderpage.dropdownlist_asoss){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.inputfield_asots.size() > 0){
                    for(Inputfield_asot b : this.renderpage.inputfield_asots){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.inputfield_asoss.size() > 0){
                    for(Inputfield_asos b : this.renderpage.inputfield_asoss){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.label_asots.size() > 0){
                    for(Label_asot b : this.renderpage.label_asots){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.label_asoss.size() > 0){
                    for(Label_asos b : this.renderpage.label_asoss){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.radiobutton_asots.size() > 0){
                    for(Radiobutton_asot b : this.renderpage.radiobutton_asots){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.radiobutton_asoss.size() > 0){
                    for(Radiobutton_asos b : this.renderpage.radiobutton_asoss){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.sliders.size() > 0){
                    for(Slider b : this.renderpage.sliders){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

                if(this.renderpage.table_bbs.size() > 0){
                    for(Table_BB b : this.renderpage.table_bbs){
                        if(b.layer == i){
                            b.render();
                        }
                    }
                }

            }

            if(this.animateToPage != null)
            {
                //render the Widgets of the other page too
                this.animateToPage.render();

                for(int i = 0; i < this.LAYERS; i++)
                {
                    if(this.animateToPage.button_asots.size() > 0){
                        for(Button_asot b : this.animateToPage.button_asots){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.button_asoss.size() > 0){
                        for(Button_asos b : this.animateToPage.button_asoss){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.checkboxes.size() > 0){
                        for(Checkbox b : this.animateToPage.checkboxes){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.dropdownlist_asots.size() > 0){
                        for(Dropdownlist_asot b : this.animateToPage.dropdownlist_asots){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.dropdownlist_asoss.size() > 0){
                        for(Dropdownlist_asos b : this.animateToPage.dropdownlist_asoss){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.inputfield_asots.size() > 0){
                        for(Inputfield_asot b : this.animateToPage.inputfield_asots){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.inputfield_asoss.size() > 0){
                        for(Inputfield_asos b : this.animateToPage.inputfield_asoss){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.label_asots.size() > 0){
                        for(Label_asot b : this.animateToPage.label_asots){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.label_asoss.size() > 0){
                        for(Label_asos b : this.animateToPage.label_asoss){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.radiobutton_asots.size() > 0){
                        for(Radiobutton_asot b : this.animateToPage.radiobutton_asots){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.radiobutton_asoss.size() > 0){
                        for(Radiobutton_asos b : this.animateToPage.radiobutton_asoss){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.sliders.size() > 0){
                        for(Slider b : this.animateToPage.sliders){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }

                    if(this.animateToPage.table_bbs.size() > 0){
                        for(Table_BB b : this.animateToPage.table_bbs){
                            if(b.layer == i){
                                b.render();
                            }
                        }
                    }
                }
            }

        }
    }


    //render function for all tooltips(called from draw)
    private void rendertooltip()
    {
        if(this.mode == 0)
        {
            for(int i = 0; i < this.LAYERS; i++){
                if(this.button_asots.size() > 0){
                    for(Button_asot b : this.button_asots){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.button_asoss.size() > 0){
                    for(Button_asos b : this.button_asoss){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.checkboxes.size() > 0){
                    for(Checkbox b : this.checkboxes){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.dropdownlist_asots.size() > 0){
                    for(Dropdownlist_asot b : this.dropdownlist_asots){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.dropdownlist_asoss.size() > 0){
                    for(Dropdownlist_asos b : this.dropdownlist_asoss){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.inputfield_asots.size() > 0){
                    for(Inputfield_asot b : this.inputfield_asots){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.inputfield_asoss.size() > 0){
                    for(Inputfield_asos b : this.inputfield_asoss){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.label_asots.size() > 0){
                    for(Label_asot b : this.label_asots){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.label_asoss.size() > 0){
                    for(Label_asos b : this.label_asoss){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.radiobutton_asots.size() > 0){
                    for(Radiobutton_asot b : this.radiobutton_asots){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.radiobutton_asoss.size() > 0){
                    for(Radiobutton_asos b : this.radiobutton_asoss){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.sliders.size() > 0){
                    for(Slider b : this.sliders){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.table_bbs.size() > 0){
                    for(Table_BB b : this.table_bbs){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }
            }
        }
        else if(this.mode == 1)
        {
            for(int i = 0; i < this.LAYERS; i++){
                if(this.renderpage.button_asots.size() > 0){
                    for(Button_asot b : this.renderpage.button_asots){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.button_asoss.size() > 0){
                    for(Button_asos b : this.renderpage.button_asoss){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.checkboxes.size() > 0){
                    for(Checkbox b : this.renderpage.checkboxes){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.dropdownlist_asots.size() > 0){
                    for(Dropdownlist_asot b : this.renderpage.dropdownlist_asots){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.dropdownlist_asoss.size() > 0){
                    for(Dropdownlist_asos b : this.renderpage.dropdownlist_asoss){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.inputfield_asots.size() > 0){
                    for(Inputfield_asot b : this.renderpage.inputfield_asots){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.inputfield_asoss.size() > 0){
                    for(Inputfield_asos b : this.renderpage.inputfield_asoss){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.label_asots.size() > 0){
                    for(Label_asot b : this.renderpage.label_asots){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.label_asoss.size() > 0){
                    for(Label_asos b : this.renderpage.label_asoss){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.radiobutton_asots.size() > 0){
                    for(Radiobutton_asot b : this.renderpage.radiobutton_asots){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.radiobutton_asoss.size() > 0){
                    for(Radiobutton_asos b : this.renderpage.radiobutton_asoss){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.sliders.size() > 0){
                    for(Slider b : this.renderpage.sliders){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }

                if(this.renderpage.table_bbs.size() > 0){
                    for(Table_BB b : this.renderpage.table_bbs){
                        if(b.layer == i){
                            b.rendertooltip();
                        }
                    }
                }
            }
        }

        //set everything back to sketch settings
        if(this.beforestroke){
            this.theparent.stroke(this.beforestrokecolor);
        } else{
            this.theparent.noStroke();
        }
        this.theparent.textSize(this.beforetextsize);
        this.theparent.rectMode(this.beforerectmode);
        this.theparent.ellipseMode(this.beforeellipsemode);
        this.theparent.imageMode(this.beforeimagemode);
        this.theparent.colorMode(this.beforecolormode);
        this.theparent.strokeWeight(this.beforestrokeweight);
        this.theparent.textAlign(this.beforetextalign);
        this.theparent.fill(this.beforecolor);
        //this.theparent.textFont(this.beforefont);
    }


    //called at the end of draw(draw all UI Stuff in here to make sure nothing in drawn above it from inside the sketch)
    public void draw()
    {
        //set running true in first iteration of draw
        if(!this.runtime_set)
        {
            BeastyWorld.running = true;
            this.runtime_set = true;

            if(mode > 1)
            {
                //mode is not known
                throw new CostumRuntimeError("unknown target mode. make sure not to add widgets and surfaces at the same time");
            }

            //if handler[2] is enabled -> generate tabswitch list for each surface if mode == 1 else for the elements from the position top to bottom
            this.generate_tabswitchlist();
        }

        this.render();
        this.rendertooltip();
        //this.checkforlog();
    }

    //listens for mouseEvents in a seperate thread
    //public void mouseEvent(processing.event.MouseEvent e)
    public void mouseEvent(MouseEvent e)
    {
        //if mouse handler is registered
        if(BeastyWorld.handlers[0])
        {
            //no animation transition happening
            if (!BeastyWorld.isanimating)
            {
                BeastyWorld.MOUSE_POS_X = e.getX();
                BeastyWorld.MOUSE_POS_Y = e.getY();
                BeastyWorld.tabswitch_displaying = false;


                switch (e.getAction()) {
                    case MouseEvent.PRESS:
                        //mouse is pressed constantly
                        BeastyWorld.MOUSE_PRESSED_DOWN = true;

                        break;
                    case MouseEvent.RELEASE:
                        //mouse is released
                        BeastyWorld.MOUSE_RELEASED = true;
                        BeastyWorld.MOUSE_PRESSED_DOWN = false;

                        //returns LEFT, CENTER or RIGHT
                        BeastyWorld.MOUSE_RELEASE_BUTTON = e.getButton();

                        break;
                    case MouseEvent.CLICK:
                        //mouse is clicked

                        break;
                    case MouseEvent.DRAG:
                        //mouse is moved while pressing down

                        break;

                    case MouseEvent.MOVE:
                        //mouse is moved, without press

                        break;

                    case MouseEvent.WHEEL:
                        //mouse Wheel is moved
                        break;
                }
            }
        }
    }

    //listens for keyEvents in a seperate thread
    //public void keyEvent(processing.event.KeyEvent e)
    public void keyEvent(KeyEvent e)
    {
        //if hotkeys handler is registered
        if(BeastyWorld.handlers[1])
        {
            //no animation transition happening
            if(!BeastyWorld.isanimating)
            {
                if(e.getAction() == KeyEvent.RELEASE)
                {
                    BeastyWorld.KEY_RELEASED = true;
                    BeastyWorld.KEY_RELEASED_BUTTON = e.getKeyCode();
                }
            }
        }

        //if tabswitch handler is registered
        else if(BeastyWorld.handlers[2])
        {
            //no animation transition happening
            if(!BeastyWorld.isanimating)
            {
                BeastyWorld.tabswitch_displaying = true;

                //on release
                if (e.getAction() == KeyEvent.RELEASE)
                {
                    BeastyWorld.KEY_RELEASED_BUTTON = e.getKeyCode();

                    if (BeastyWorld.KEY_RELEASED_BUTTON == PConstants.TAB)
                    {
                        if(this.mode == 0)
                        {
                            //snap to the next element
                            //this.tabswitch_next();
                        }
                        else if(this.mode == 2)
                        {
                            //snap to the next element on the page
                            //this.renderpage.tabswitch_next();
                        }
                    }
                }
            }
        }
    }



    //**** VIRTUAL KEYBOARD ****
    public void showVK()
    {

    }

    public void hideVK()
    {

    }
}


//TODO: IMPORTANT!!! IMPORT/EXPORT METHODS
//TODO: IMPORTANT!!! MOUSE/KEYEVENTS AND CONNECT WITH OVER AND LOGGING - TESTING STATE
//TODO: SMARTER RENDERING OF SURFACES WHEN TRANSITION BETWEEN THEM(just render the active surface elements and when transition the other one before transition
//TODO: begins too)
//TODO: CHANGING AND ANIMATING BETWEEN SURFACES
//TODO: IMPORTANT!!! VIRTUAL KEYBOARD
//TODO: HANDLERS - TESTING STATE
//TODO: IMPORTANT!!! UPDATE THE SOURCEPATH WHEN SETID() IS CALLED IN EACH WIDGET - TESTING STATE

//RIGHT NOW THERE IS NO SUPPORT FOR REACTING TO THE RESIZE OF THE WINDOW OTHER THAN CALLING SETPOSITION() IN DRAW
//THREAD EXECUTION SHOULD WORK BUT YOU CANNOT DRAW IN THERE TO PGRAPHICS INSTANCE OF PAPPLET AND YOU CANNOT RUN A METHOD WHICH
//HAS PARAMETERS PASSED TO IT - IT HAS TO BE PARAMETERLESS