package beastybuttons;

import processing.core.*;
import processing.data.Table;
import processing.data.TableRow;

import java.util.ArrayList;

@NotImplementedYet
@Experimental
public class BB_Table extends Widget 
{
	
	//******VARIABLES******

	//instances of the table
	protected static int INSTANCES = 0;
	
	//PApplet ref
	private final PApplet REF;
	
	//textsize of the table entrys
	private float textSize;
	
	//offset the text in the table for all entrys
  	protected float[] textoffset = {0f, 0f};

  	//highlight entrys
  	protected ArrayList<Highlight> highlight = new ArrayList<>();
  	
  	//highlight color
  	protected int highlightcolor;
  	
  	//holds the data of the table
  	protected ArrayList<BB_TableRow> data = new ArrayList<>();
  	
  	//rowcount of this table
  	private int rowcount = 0;
  	
  	//column count of this table
  	private int colcount = 0;
  	
 
  	//******CONSTRUCTORS******
  	
  	//this is the size of the whole table in pixels, not the amount of rows or cols!
  	public BB_Table(PApplet ref, float xsize, float ysize) {
  		this.REF = ref;
  		this.sizes[0] = xsize;
  		this.sizes[1] = ysize;
		this.generateColors();
		this.generateID();
		this.rendercolor = this.background;
		this.timeStep = System.currentTimeMillis();
  	}
  	
  	//import constructor
  	protected BB_Table(PApplet ref) {
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
        this.highlightcolor = this.REF.color(255, 255, 100);
    }
	
    
    //generate the ID
    private void generateID() {
        String convert = String.valueOf(BB_Table.INSTANCES);
        this.ID = "BB_Table" + convert;
        BB_Table.INSTANCES+=1;
        this.SOURCEPATH = this.ID;
    }
    
    //calculates the size of the Button(with multiple line support)
    @NotImplementedYet
    private float[] calculateSize(String text) {
        float current_size = this.REF.g.textSize;
        String[] splits = text.split(this.line_separator);
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
        //this.sizes[0] = xsize;
        //this.sizes[1] = ysize;
        
        this.REF.textSize(current_size);
        
        float[] temp = {xsize, ysize};
        
        return temp;
    }
    
    
    //******SET METHODS******
    
    public BB_Table setForegroundColor(int fg) {
    	this.foreground = fg;
    	return this;
    }
    
    public BB_Table setBackgroundColor(int bg) {
    	this.background = bg;
    	return this;
    }
    
    public BB_Table setHighlightColor(int hc) {
    	this.highlightcolor = hc;
    	return this;
    }
    
    public BB_Table setOvercolor(int c) {
    	this.overcolor = c;
    	return this;
    }
    
    public BB_Table setClickcolor(int c) {
    	this.clickcolor = c;
    	return this;
    }
    
    
    public BB_Table resetColors() {
    	this.generateColors();
    	return this;
    }
    
    @NotImplementedYet
    public BB_Table setTextsize(float textSize, boolean update) {
		this.textSize = textSize;
		
		if(update) {
			//update the size of the tablecells to fit
			if(this.data.size() > 0) {
				String longest = "";
				for(BB_TableRow row : this.data) {
					for(String s : row.rowdata) {
						if(s.length() > longest.length()) {
							longest = s;
						}
					}
				}
				float[] temp = this.calculateSize(longest);
				
				
				//add an extra border for the block splitter
				this.sizes[0] = (float)(this.rowcount+1)*(temp[0]*1.15f);
				this.sizes[1] = (float)(this.colcount+1)*(temp[1]*1.15f);
				
			}
		}
		return this;
	}
    
    @NotImplementedYet
    public BB_Table setSize(float xsize, float ysize, boolean update) {
    	this.sizes[0] = xsize;
    	this.sizes[1] = ysize;
    	
    	if(update) {
    		//update the textsize of the text in the tablecells to fit
    		
    	}
    	return this;
    }
    
    //set the position of the table -> if tooltip is enabled you will need to re-set its position
  	public BB_Table setPosition(float x, float y) {
  		this.positions[0] = x;
  		this.positions[1] = y;
  		this.calc_tt_auto_pos();
  		return this;
  	}
  	
  	public BB_Table setPosition(String a, String b) {
  		if(a.equals("left")) {
  			this.positions[0] = this.sizes[0]/2;
  		}
  		else if(a.equals("right")) {
  			this.positions[0] = this.REF.width - this.sizes[0]/2;
  		}
  		else {
  			throw new RuntimeException("not a valid input for setting the position of the table");
  		}
  		
  		if(b.equals("top")) {
  			this.positions[1] = this.sizes[1]/2;
  		}
  		else if(b.equals("bottom")) {
  			this.positions[1] = this.REF.height - this.sizes[1]/2;
  		}
  		else {
  			throw new RuntimeException("not a valid input for setting the position of the table");
  		}
  		this.calc_tt_auto_pos();
  		return this;
  	}
  	
  	public BB_Table setPosition(String a, float y) {
  		if(a.equals("left")) {
  			this.positions[0] = this.sizes[0]/2;
  		}
  		else if(a.equals("right")) {
  			this.positions[0] = this.REF.width - this.sizes[0]/2;
  		}
  		else {
  			throw new RuntimeException("not a valid input for setting the position of the table");
  		}
  		this.positions[1] = y;
  		this.calc_tt_auto_pos();
  		return this;
  	}
  	
  	public BB_Table setPosition(float x, String b) {
  		if(b.equals("top")) {
  			this.positions[1] = this.sizes[1]/2;
  		}
  		else if(b.equals("bottom")) {
  			this.positions[1] = this.REF.height - this.sizes[1]/2;
  		}
  		else {
  			throw new RuntimeException("not a valid input for setting the position of the table");
  		}
  		this.positions[0] = x;
  		this.calc_tt_auto_pos();
  		return this;
  	}
  	
  	//does not display the widget anymore
  	public BB_Table hide(boolean visible) {
  		this.visible = !visible;
  		return this;
  	}
  	
  //disables interaction and tooltip displaying
  	public BB_Table setActive(boolean active) {
  		this.active = active;
  		return this;
  	}
  	
  	@Override
  	public BB_Table setLayer(int layer) {
  		if(layer <= BeastyWorld.LAYERS && layer >= 0) {
  			this.LAYER = layer;
  		}
  		else {
  			PApplet.println("WARNING(BB): UNKNOWN LAYER SET -> CONTINUEING WITHOUT SETTING THE LAYER");
  		}
  		return this;
  	}
  	
  	@Override
  	public BB_Table setID(String id) {
  		this.ID = id;
  		this.SOURCEPATH.replace(this.ID, id);
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
     public BB_Table setTextoffset(float x, float y) {
      	this.textoffset[0] = x;
      	this.textoffset[1] = y;
      	return this;
     }
     
     @NotImplementedYet
     public BB_Table copySettings(BB_Table t) {
 		
 		//copy the settings by value except the sourcepath, id, onclick variables, hotkey variable, already_added, line_separator, PApplet ref,
 		//timeStep, selector_of_element, clickssincestart, tab_selected, and instance value
 		
 		
 		return this;
 	}
 	
 	
     //highlight entry in a specific color with a rect
     @NotImplementedYet
     public BB_Table highlightEntry(int x, int y)
     {
         //this.highlight = true;
         this.highlight.add(new Highlight(String.valueOf(x)+String.valueOf(y), x, y));
         return this;
     }

     //end highlighting with no parameters given
     @NotImplementedYet
     public BB_Table endhighlightEntry(int x, int y)
     {
         if(this.highlight.size() > 0) {
        	 for(Highlight h : this.highlight) {
        		 if(h.getH_ID().equals(String.valueOf(x)+String.valueOf(y))) {
        			 this.highlight.remove(h);
        		 }
        	 }
         }
         return this;
     }
     
     
     //add empty row at the end
     public void addRow()
     {
         BB_TableRow r = new BB_TableRow(this);
         this.data.add(r);
         this.rowcount+=1;
     }

     //add empty column at the end
     public void addCol()
     {
         for(BB_TableRow row : this.data)
         {
             row.addCol();
         }
         this.colcount+=1;
     }

     //remove last row
     public void removeLastRow()
     {
    	 if(this.data.size() > 0) {
    		 this.data.remove(this.data.size()-1);
    	 }
     }

     //remove last column
     public void removeLastCol()
     {
         for(BB_TableRow row : this.data)
         {
             row.removeCol();
         }
     }

     //overwrite data at
     @NotImplementedYet
     public void overwriteDataAt(int x, int y, String data, boolean update)
     {
         this.data.get(y).replaceAt(x, data);
     }

     //add to data at
     @NotImplementedYet
     public void appendDataAt(int x, int y, String data, boolean update)
     {
         this.data.get(y).addAt(x, data);
     }

 	
     
     //******GET METHODS******
 	
 	
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
 	
 	public String getID() {
 		return this.ID;
 	}
 	
 	public float[] getPosition() {
 		return this.positions;
 	}
 	
 	
 	
 	//******TOOLTIP METHODS******
	
	//creates a new tooltip overrriding old ones
    public BB_Table createTooltip(String text, float size, int fg, int bg) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.ttforeground = fg;
    	this.ttbackground = bg;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public BB_Table createTooltip(String text, float size) {
    	this.tooltiptext = text;
    	this.tooltiptextsize = size;
    	this.calculatettsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    public BB_Table createTooltip(String text, float xsize, float ysize, int fg, int bg) {
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
    
    public BB_Table createTooltip(String text, float xsize, float ysize) {
    	this.tooltiptext = text;
    	this.ttsizes[0] = xsize;
    	this.ttsizes[1] = ysize;
    	this.calculatemaxttsize();
    	this.tooltip_enabled = true;
    	this.setTooltipPosition("auto");
    	return this;
    }
    
    
    //configure the tooltip further
    public BB_Table configureTooltip(boolean roundtt) {
    	if(this.tooltiptext != null) {
    		this.roundtt = roundtt;
    		this.enable_intervall = false;
    	}
    	else {
    		throw new RuntimeException("there is no tooltip to configure, it needs to be created first");
    	}
    	return this;
    }
    
    public BB_Table configureTooltip(boolean roundtt, float intervall) {
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
    public BB_Table setTooltipPosition(float x, float y) {
    	this.ttpositions[0] = x;
    	this.ttpositions[1] = y;
    	return this;
    }
    
    public BB_Table setTooltipPosition(String pos) {
    	if(pos.equals("auto")) {
    		this.calc_tt_auto_pos();
    	}
    	else {
    		throw new RuntimeException("no valid input for setting the position of the tooltip");
    	}
    	return this;
    }
    
    public BB_Table setTooltipSize(float x, float y) {
    	this.ttsizes[0] = x;
    	this.ttsizes[1] = y;
    	return this;
    }
    
    public BB_Table setTooltipTextsize(float s) {
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
    public BB_Table setTooltiptextoffset(float x, float y) {
    	this.tttextoffset[0] = x;
    	this.tttextoffset[1] = y;
    	return this;
    }
    
    //enable or disable the tooltip(tooltip is automatically enabled on creation)
    public BB_Table enableTooltip(boolean enable) {
    	this.tooltip_enabled = enable;
    	return this;
    }
    
    //positions the tooltip automatically
    
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
    
    
    
    //******IMPORT METHODS******
    
    //import a csv table
    @Experimental
    public void importCSV(String path)
    {
        String[] dataarr = this.REF.loadStrings(path);
        for(int i = 0; i < dataarr.length; i++)
        {
            BB_TableRow nrow = new BB_TableRow(this);
            String st = dataarr[i];
            String[] spl = st.split(",");
            for(int j = 0; j < spl.length; j++)
            {
                nrow.addAt(j, spl[j]);
            }
            this.data.add(nrow);
        }
    }
    
    
    //import a processing table object
    @Experimental
    public void importProcessingTable(Table table)
    {
        //does this include header???
        for(int i = 0; i < table.getRowCount(); i++)
        {
            TableRow row = table.getRow(i);
            BB_TableRow row_BB = new BB_TableRow(this);
            for(int j = 0; j < row.getColumnCount(); j++)
            {
                //try for different data types
                try
                {
                    //try String
                    String s = row.getString(j);
                    row_BB.addAt(j, s);
                }
                catch (Exception e)
                {
                    try
                    {
                        //try int
                        int t = row.getInt(j);
                        String temp = String.valueOf(t);
                        row_BB.addAt(j, temp);
                    }
                    catch (Exception f)
                    {
                        //try float
                        try
                        {
                            float fl = row.getFloat(j);
                            String temp2 = String.valueOf(f);
                            row_BB.addAt(j, temp2);
                        }
                        catch (Exception g)
                        {
                            //throw Error
                            throw new RuntimeException("importing from Processing Table Object failed!");
                        }
                    }
                }
            }
            //add the row to this data list
            this.data.add(row_BB);
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
    
    
    
    //******TABLEROW INNER CLASS******
    private class BB_TableRow
    {
    	//data holder
        private ArrayList<String> rowdata = new ArrayList();


        BB_TableRow(BB_Table table)
        {
            //table.rowcount+=1;
        }

        //add last
        private void addCol()
        {
            this.rowdata.add("");
        }

        //remove last
        private void removeCol()
        {
            this.rowdata.remove(this.rowdata.size()-1);
        }

        //replace at
        protected void replaceAt(int pos, String data)
        {
            this.rowdata.set(pos, data);
        }

        //add at
        private void addAt(int pos, String data)
        {
            String temp = this.rowdata.get(pos);
            temp += data;
            this.rowdata.set(pos, temp);
        }
    }
    
    private class Highlight {
    	private String h_id;
    	private int h_x, h_y;
    	
    	Highlight(String id, int x, int y){
    		this.h_id = id;
    		this.h_x = x;
    		this.h_y = y;
    	}
    	
    	private String getH_ID() {
    		return this.h_id;
    	}
    	
    	private int getH_X() {
    		return this.h_x;
    	}
    	
    	private int getH_Y() {
    		return this.h_y;
    	}
    }
}