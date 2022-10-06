package beastybuttons;

import processing.core.*;
import processing.data.Table;
import processing.data.TableRow;

import java.util.ArrayList;

public class Table_BB extends Widget
{
    //position of the center of the Button
    protected float posx = 0, posy = 0;

    //is the widget active or not?
    protected boolean active = true;

    //is the widget visible or not?
    protected boolean visible = true;

    //holding text of the button
    private String text;

    //colors
    private int background, foreground;

    //shape of the widget
    private boolean round = false;

    //size of the widget
    private float sizes[] = new float[2];

    //textsize of the holding text
    private float textSize;

    //colors
    private int overcolor, clickcolor;

    //widget clicke variables
    private boolean clicked = false, click = false;

    //click type(right, left, middle)
    private int type = 99;

    //tooltip
    private boolean tooltip_enabled = false;
    private String tooltiptext = "tooltip";
    private float tooltiptextsize = 1;
    private float[] ttsizes = {0, 0};

    //colors
    private int ttbackground, ttforeground;

    //tooltip position
    private float ttxpos = 0, ttypos = 0;

    //amount of milliseconds after the tooltip disappears
    private float ttintervall = 2000;

    //shape of the tooltip
    private boolean roundtt = false;

    //does the mouse stay on the widget in one position?
    private boolean pos_locked = false;

    //used for the tooltip dissaper time measure
    private long time;

    //auto positioning of the tooltip
    private boolean tt_autopos = true;

    //enable disappear for the tooltip?
    private boolean enable_intervall = false;

    //used for the information on the buttonpress
    private boolean lastpress = false;

    private boolean press = false;

    private boolean mouseReleased = false;

    //counts the clicks on the Widget since runtime
    private int clickssincestart = 0;

    //id of the Widget
    protected String id = "id";

    //the layer on which the widget is rendered
    protected int layer = 0;

    //the amount of instances to generate unique id
    private static int t_instances = 0;

    //the sourcepath of the widget
    protected String sourcePath;

    //executing on Click
    private String olc, omc, orc;

    //highlighting active or not?
    private boolean highlight = false;

    //highlight position
    private int highlightx = 0, highlighty = 0;

    //row count of this table
    private int rowcount = 0;

    //is the widget already added to a surface or the world
    protected boolean already_added = false;

    //PApplet reference
    private final PApplet sketch;

    //holds the table data(in rows)
    private ArrayList<TableRow_BB> data = new ArrayList();

    Table_BB(PApplet p, int sizex, int sizey)
    {
        this.sketch = p;
        this.sizes[0] = sizex;
        this.sizes[1] = sizey;
    }

    //**** CONFIGURATION METHODS - CALLED FROM THE CONSTRUCTOR ****

    //calculates the size of the Tooltip
    private void calculatettsize()
    {
        float current_size = this.sketch.g.textSize;
        this.sketch.textSize(this.tooltiptextsize);
        float xsize = 0, ysize = 0;
        for(int i = 0; i < this.tooltiptext.length(); i++)
        {
            xsize += this.sketch.textWidth(this.tooltiptext.charAt(i));
        }
        ysize = this.sketch.textAscent()+this.sketch.textDescent();
        xsize = (float)(xsize*1.15);
        ysize = (float)(ysize*1.15);
        this.ttsizes[0] = xsize;
        this.ttsizes[1] = ysize;
        this.sketch.textSize(current_size);
    }

    private void calculatemaxsize(String text)
    {
        float current_size = this.sketch.g.textSize;
        float size, lastsize = 0;
        float ysize, xsize = 0;
        for(size = 1; size < 500; size++)
        {
            this.sketch.textSize(size);
            ysize = this.sketch.textAscent()+this.sketch.textDescent();
            xsize = 0;

            for(int i = 0; i < text.length(); i++)
            {
                xsize += this.sketch.textWidth(text.charAt(i));
            }

            lastsize = size;

        }
        this.textSize = lastsize;
        //print(this.textSize);
        this.sketch.textSize(current_size);
    }

    //generate the colors after sketch can be referenced
    private void generateColors()
    {
        this.foreground = this.sketch.color(100, 100, 100);
        this.background = this.sketch.color(0, 0, 0);
        this.overcolor = this.sketch.color(250, 0, 0);
        this.clickcolor = this.sketch.color(60, 0, 0);
        this.ttbackground = this.sketch.color(255, 255, 255);
        this.ttforeground = this.sketch.color(0, 0, 0);
    }

    private void generateID()
    {
        String convert = String.valueOf(Table_BB.t_instances);
        this.id = "Table_BB" + convert;
        Table_BB.t_instances+=1;
        this.sourcePath = this.id;
    }

    //**** SET AND GET METHODS****

    //set the Foreground Color
    public void setForegroundColor(int fg)
    {
        if(!BeastyWorld.running)
        {
            this.foreground = fg;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //set the textsize because we don't know at which time the table is full so which is the longest input to calculate
    //the textsize
    public void setTextsize(float size)
    {
        this.textSize = size;
    }

    //automatically calculates the textSize at the time it is called(so it needs to be called by the user when the table is full)
    public void setTextsize(char s)
    {
        if(s == 'A')
        {
            //get the longest input in the table
            String longestinput;
            int li_count = 0;
            for(int i = 0; i < this.data.size(); i++)
            {
                for(int j = 0; j < this.data.get(i).rowdata.size(); j++)
                {
                    if(this.data.get(i).rowdata.get(j).length() > li_count)
                    {
                        li_count = this.data.get(i).rowdata.get(j).length();
                        longestinput = this.data.get(i).rowdata.get(j);
                    }
                }
            }
        }
        else
        {
            throw new CostumRuntimeError("Not a valid input for settextsize()");
        }

        //calculate the textsize from the longest input

    }

    //set the Background Color
    public void setBackgroundColor(int bg)
    {
        if(!BeastyWorld.running)
        {
            this.background = bg;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }


    //import a .csv table
    public void importCSV(String path)
    {
        String[] dataarr = sketch.loadStrings(path);
        for(int i = 0; i < dataarr.length; i++)
        {
            TableRow_BB nrow = new TableRow_BB(this);
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
    public void importProcessingTable(Table table)
    {
        //does this include header???
        for(int i = 0; i < table.getRowCount(); i++)
        {
            TableRow row = table.getRow(i);
            TableRow_BB row_BB = new TableRow_BB(this);
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
                            throw new CostumRuntimeError("Conversion from Processing Table Object failed!");
                        }
                    }
                }
            }
            //add the row to this data list
            this.data.add(row_BB);
        }
    }

    //highlight entry in a specific color with a rect
    public void highlightEntry(int x, int y)
    {
        this.highlight = true;
        this.highlightx = x;
        this.highlighty = y;
    }

    //end highlighting with no parameters given
    public void endhighlightEntry(int x, int y)
    {
        this.highlight = false;
    }

    //add empty row at the end
    public void addRow()
    {
        TableRow_BB r = new TableRow_BB(this);
        this.data.add(r);
    }

    //add empty column at the end
    public void addCol()
    {
        for(TableRow_BB row : this.data)
        {
            row.addCol();
        }
    }

    //remove last row
    public void removeLastRow()
    {
        this.data.remove(this.data.size()-1);
    }

    //remove last column
    public void removeLastCol()
    {
        for(TableRow_BB row : this.data)
        {
            row.removeCol();
        }
    }

    //overwrite data at
    public void overwriteDataAt(int x, int y, String message)
    {
        this.data.get(y).replaceAt(x, message);
    }

    //add to data at
    public void appendDataAt(int x, int y, String message)
    {
        this.data.get(y).addAt(x, message);
    }

    //set the Position of the Widget
    public void setPosition(float x, float y)
    {
        if(!BeastyWorld.running)
        {
            this.posx = x;
            this.posy = y;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void setPosition(char a, char b)
    {
        if(!BeastyWorld.running) {
            if (a == 'L') {
                this.posx = this.sizes[0] / 2;
            } else if (a == 'R') {
                this.posx = this.sketch.width - this.sizes[0] / 2;
            } else {
                //throw error
                throw new CostumRuntimeError("Not a valid input for setPosition()");
            }
            if (b == 'T') {
                this.posy = this.sizes[1] / 2;
            } else if (b == 'B') {
                this.posy = this.sketch.height - this.sizes[1] / 2;
            } else {
                //throw error
                throw new CostumRuntimeError("Not a valid input for setPosition()");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void setPosition(char a, float y)
    {
        if(!BeastyWorld.running) {
            if (a == 'L') {
                this.posx = this.sizes[0] / 2;
            } else if (a == 'R') {
                this.posx = this.sketch.width - this.sizes[0] / 2;
            } else {
                //throw error
                throw new CostumRuntimeError("Not a valid input for setPosition()");
            }
            this.posy = y;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void setPosition(float x, char b)
    {
        if(!BeastyWorld.running) {
            if (b == 'T') {
                this.posy = this.sizes[1] / 2;
            } else if (b == 'B') {
                this.posy = this.sketch.height - this.sizes[1] / 2;
            } else {
                //throw error
                throw new CostumRuntimeError("Not a valid input for setPosition()");
            }
            this.posx = x;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public float[] getPosition()
    {
        if(!BeastyWorld.running) {
            //pass
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
        float[] temp = {this.posx, this.posy};
        return temp;
    }

    //set widget unvisible or visible
    public void hide(boolean vis)
    {
        this.visible = vis;
    }

    //set the Layer
    public void setLayer(int layer)
    {
        if(!BeastyWorld.running)
        {
            this.layer = layer;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //override the id
    public void setID(String id)
    {
        if(!BeastyWorld.running)
        {
            this.sourcePath.replace(this.id, id);
            this.id = id;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    protected void editSourcepath(String action, String id)
    {
        String[] splits = this.sourcePath.split("/");
        if(action.equals("delete"))
        {
            this.sourcePath = splits[1];
        }
        else if(action.equals("add"))
        {
            //this.sourcePath = id + "/" + this.sourcePath;
            this.sourcePath = id.concat("/"+this.sourcePath);
        }
        else
        {
            PApplet.println("WARNING(BB): SOURCEPATH ERROR - WIDGET MIGHT BE LINKED WRONG");
        }
    }

    //copy all Settings of another Table
    public void copySettings(Table_BB t)
    {
        if(!BeastyWorld.running)
        {
            //go through every parameter and copy the actual Parameters(not by reference)
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }


    //****  INTERNAL CALLED METHODS  ****

    //takes coordinates and calculates if the cursor is over the element
    protected boolean over(float mx, float my)
    {
        if(mx >= this.posx-this.sizes[1]/2 && mx <= this.posx+this.sizes[1]/2)
        {
            if(my >= this.posy-this.sizes[1]/2 && my <= this.posy+this.sizes[1]/2)
            {
                return true;
            }
        }
        return false;
    }

    //render queue for the tooltip
    protected void rendertooltip()
    {
        //render with rectMode(CENTER)!!!
    }

    //render queue for the Widget
    protected void render()
    {
        //render with rectMode(CENTER)!!!
    }

    //inner TableRow Class
    private class TableRow_BB
    {
        //data holder
        private ArrayList<String> rowdata = new ArrayList();


        TableRow_BB(Table_BB table)
        {
            table.rowcount+=1;
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
        protected void replaceAt(int pos, String message)
        {
            this.rowdata.set(pos, message);
        }

        //add at
        private void addAt(int pos, String message)
        {
            String temp = this.rowdata.get(pos);
            temp += message;
            this.rowdata.set(pos, temp);
        }


    }
}
