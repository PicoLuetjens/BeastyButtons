package beastybuttons;


abstract class Widget
{		
	protected String ID;
	
	protected boolean already_added = false;
	
	protected boolean active = true;
	
	protected boolean visible = true;
	
	//this is true when the mouse is pressed over an element(not released)
	public boolean mouse_pressed_down_over = false;
	
	protected String SOURCEPATH;
	
	//protected String[] logline;
	
	protected int LAYER = 0;
	
	//the position of the widget
	protected float[] positions = {0f, 0f};
	
	//the size of the widget
	protected float[] sizes = {0f, 0f};
	
	//the size of the tooltip
	protected float[] ttsizes = {0f, 0f};
		
	//the position of the tooltip
	protected float[] ttpositions = {0f, 0f};
	
	//the rendercolor of the background (changes when mouseover or clicked)
	protected int rendercolor;
		
	//the background color
	protected int background;
		
	//the foreground color
	protected int foreground;
		
	//the color when mouseover or selected with tab
	protected int overcolor;
		
	//the color when mouseclicked or selected with enter
	protected int clickcolor;
	
	//the background color of the tooltip
	protected int ttbackground = 100;
		
	//the foreground color of the tooltip
	protected int ttforeground = 100;
		
	//the text of the tooltip
	protected String tooltiptext = null;
		
	//the textsize of the tooltip
	protected float tooltiptextsize = 0f;
	
	//offset the text in the tooltip
  	protected float[] tttextoffset = {0f, 0f};
	
	//is the tooltip enabled?
	protected boolean tooltip_enabled = false;
	
	//enable disappear for the tooltip over time?
    protected boolean enable_intervall = false;
    
    //the time after the tooltip disappears when enable interval is selected and mouse is still over or widget is still selected
	protected float intervall = 2000f;
	
	//saves the timestep of millis for the tooltip disappearance
	protected long timeStep;
	
	//reset for the tooltip interval animation
	protected boolean selector_off_element = true;
    
	//is the tooltip round?
	protected boolean roundtt;
	
	//counts the clicks on the Widget since runtime
    protected int clicks_since_start = 0;
    
    //selected by tab when tab handler is active
    protected boolean tab_selected = false;
    
    //assumption because on windows the java system line separator returns nothing
  	protected String line_separator = "\n";
	
	
	protected abstract void editSourcepath(String operation, String id_other);
	
	public abstract Widget setLayer(int layer);

    public abstract Widget setID(String id);
    
    public abstract String getID();
    
    public abstract String getSourcePath();
    
    //public abstract Widget copySettings(Widget w);
    
    //render method for rendering objects with this type
    protected abstract void render();

    //rendertooltip method for rendering objects with this type
    protected abstract void rendertooltip();
    
    //for mouse over
    public abstract boolean over(float x, float y);
    
    protected abstract void calc_tt_auto_pos();
}
