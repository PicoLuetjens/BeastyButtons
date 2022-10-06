package beastybuttons;

import processing.core.*;

import java.util.ArrayList;

public class BeastySurface
{
    //PApplet reference
    private final PApplet ref;

    //Background Image for the surface
    private PImage backgroundimg;

    //stretches the backgroundimg to the borders
    private boolean stretch_backgroundimg = false;

    //the sizes for the backgroundimg
    private float max_imgsize_x = 0f, max_imgsize_y = 0f;

    //background color
    private int backgroundcolor;

    //ID
    protected String id;

    //the amount of instances to generate unique id
    private static int bs_instances = 0;

    //beasty buttons source path
    protected String sourcePath;

    //Widget List
    protected ArrayList<Widget> widgets = new ArrayList<>();

    //the active states stored for animation
    protected ArrayList<ArrayList<Boolean>>active_states = new ArrayList();

    //is the surface already added to the world
    protected boolean already_added = false;

    //the list of ids of the widgets based on position(up to down) for tabswitch handler
    protected ArrayList<String> tabswitch_list = new ArrayList();

    //position variables
    protected float posx, posy;

    public BeastySurface(PApplet p)
    {
        if(!BeastyWorld.running)
        {
            this.ref = p;
            generateID();
            //default background is grey
            this.backgroundcolor = this.ref.color(100, 100, 100, 0);
            this.posx = this.ref.width / 2f;
            this.posy = this.ref.height / 2f;
        }
        else
        {
            throw new CostumRuntimeError("BeastySurface cannot be instanced while runtime, try to instance before draw()");
        }
    }

    //**** CONFIGURATION METHODS - CALLED FROM THE CONSTRUCTOR ****
    //generate the default id
    private void generateID()
    {
        String convert = String.valueOf(BeastySurface.bs_instances);
        this.id = "beastysurface" + convert;
        BeastySurface.bs_instances+=1;
        this.sourcePath = this.id;
    }


    //**** SET METHODS ****
    //copy the settings of another surface for time saving
    //does not copy sourcepath, id, already_added status and added widgets
    public void copySettings(BeastySurface surface)
    {
        if(!BeastyWorld.running)
        {
            PApplet.println("WARNING(BB): copySettings() IS STILL HIGHLY EXPERIMENTAL, AVOID USE WHEN POSSIBLE");
            //copy all the settings(but not id and path)
            this.setSurfaceimage(surface.backgroundimg, surface.stretch_backgroundimg);
            this.setSurfacecolor(surface.backgroundcolor);
            //this.backgroundcolor = surface.backgroundcolor;
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME");
        }
    }

    //edits the sourcepath of this instance(called from top level class -> Beastyworld instance)
    // for delete just pass null as second argument
    protected void editSourcepath(String action, String id)
    {
        String[] splits = this.sourcePath.split("/");
        if(action.equals("delete"))
        {
            this.sourcePath = splits[1];
        }
        else if(action.equals("add"))
        {
            this.sourcePath = id + "/" + this.sourcePath;
        }
        else
        {
            PApplet.println("WARNING(BB): SOURCEPATH ERROR - WIDGET MIGHT BE LINKED WRONG");
        }
    }

    public void addWidget(Button_asot button)
    {
        if(!BeastyWorld.running)
        {
            if(!button.already_added)
            {
                if(!button.id.equals(this.id))
                {
                    button.already_added = true;
                    this.button_asots.add(button);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Button_asos button)
    {
        if(!BeastyWorld.running)
        {
            if(!button.already_added)
            {
                if(!button.id.equals(this.id))
                {
                    button.already_added = true;
                    this.button_asoss.add(button);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Label_asot label)
    {
        if(!BeastyWorld.running)
        {
            if(!label.already_added)
            {
                if(!label.id.equals(this.id))
                {
                    label.already_added = true;
                    this.label_asots.add(label);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Label_asos label)
    {
        if(!BeastyWorld.running)
        {
            if(!label.already_added)
            {
                if(!label.id.equals(this.id))
                {
                    label.already_added = true;
                    this.label_asoss.add(label);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Checkbox box)
    {
        if(!BeastyWorld.running)
        {
            if(!box.already_added)
            {
                if(!box.id.equals(this.id))
                {
                    box.already_added = true;
                    this.checkboxes.add(box);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Inputfield_asot inputfield)
    {
        if(!BeastyWorld.running)
        {
            if(!inputfield.already_added)
            {
                if(!inputfield.id.equals(this.id))
                {
                    inputfield.already_added = true;
                    this.inputfield_asots.add(inputfield);

                    //edit the sourcePath
                    inputfield.editSourcepath("add", this.id);
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
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Inputfield_asos inputfield)
    {
        if(!BeastyWorld.running)
        {
            if(!inputfield.already_added)
            {
                if(!inputfield.id.equals(this.id))
                {
                    inputfield.already_added = true;
                    this.inputfield_asoss.add(inputfield);

                    //edit the sourcePath
                    inputfield.editSourcepath("add", this.id);
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
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Table_BB table)
    {
        if(!BeastyWorld.running)
        {
            if(!table.already_added)
            {
                if(!table.id.equals(this.id))
                {
                    table.already_added = true;
                    this.table_bbs.add(table);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Radiobutton_asot radiobutton)
    {
        if(!BeastyWorld.running)
        {
            if(!radiobutton.already_added)
            {
                if(!radiobutton.id.equals(this.id))
                {
                    radiobutton.already_added = true;
                    this.radiobutton_asots.add(radiobutton);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Radiobutton_asos radiobutton)
    {
        if(!BeastyWorld.running)
        {
            if(!radiobutton.already_added)
            {
                if(!radiobutton.id.equals(this.id))
                {
                    radiobutton.already_added = true;
                    this.radiobutton_asoss.add(radiobutton);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Dropdownlist_asot dropdownlist)
    {
        if(!BeastyWorld.running)
        {
            if(!dropdownlist.already_added)
            {
                if(!dropdownlist.id.equals(this.id))
                {
                    dropdownlist.already_added = true;
                    this.dropdownlist_asots.add(dropdownlist);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Dropdownlist_asos dropdownlist)
    {
        if(!BeastyWorld.running)
        {
            if(!dropdownlist.already_added)
            {
                if(!dropdownlist.id.equals(this.id))
                {
                    dropdownlist.already_added = true;
                    this.dropdownlist_asoss.add(dropdownlist);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    public void addWidget(Slider slider)
    {
        if(!BeastyWorld.running)
        {
            if(!slider.already_added)
            {
                if(!slider.id.equals(this.id))
                {
                    slider.already_added = true;
                    this.sliders.add(slider);

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
                PApplet.println("WARNING(BB): WIDGETS CANNOT BE ADDED TO MULTIPLE SURFACES");
            }
        }
        else
        {
            PApplet.println("WARNING(BB): PARAMETERS CANNOT BE CHANGED AT RUNTIME TO ENSURE EXPORT FUNCTION");
        }
    }

    //sets the id manually
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

    //this writes the active state of each Widget to a boolean
    //this is called from BeastyWorld when a wipe or opacity Animation is done because during that time all Widgets need
    //to be disabled. Afterwords they will be set back to their before state
    protected void capture_active_states()
    {
        /* save order:
        Button_asot
        Button_asos
        Checkbox
        Label_asot
        Label_asos
        Inputfield_asot
        Inputfield_asos
        Radiobutton_asot
        Radiobutton_asos
        Table_BB
        Dropdownlist_asot
        Dropdownlist_asos
        Slider
         */

        if(this.button_asots.size() > 0)
        {
            ArrayList<Boolean> temp1 = new ArrayList();
            for (Button_asot b : this.button_asots) {
                temp1.add(b.active);
            }
            this.active_states.add(temp1);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash1 = new ArrayList();
            trash1.add(false);
            this.active_states.add(trash1);
        }

        if(this.button_asoss.size() > 0)
        {
            ArrayList<Boolean> temp2 = new ArrayList();
            for (Button_asos b : this.button_asoss) {
                temp2.add(b.active);
            }
            this.active_states.add(temp2);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash2 = new ArrayList();
            trash2.add(false);
            this.active_states.add(trash2);
        }

        if(this.checkboxes.size() > 0)
        {
            ArrayList<Boolean> temp3 = new ArrayList();
            for (Checkbox b : this.checkboxes) {
                temp3.add(b.active);
            }
            this.active_states.add(temp3);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash3 = new ArrayList();
            trash3.add(false);
            this.active_states.add(trash3);
        }

        if(this.label_asots.size() > 0)
        {
            ArrayList<Boolean> temp4 = new ArrayList();
            for (Label_asot b : this.label_asots) {
                temp4.add(b.active);
            }
            this.active_states.add(temp4);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash4 = new ArrayList();
            trash4.add(false);
            this.active_states.add(trash4);
        }

        if(this.label_asoss.size() > 0)
        {
            ArrayList<Boolean> temp5 = new ArrayList();
            for (Label_asos b : this.label_asoss) {
                temp5.add(b.active);
            }
            this.active_states.add(temp5);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash5 = new ArrayList();
            trash5.add(false);
            this.active_states.add(trash5);
        }

        if(this.inputfield_asots.size() > 0)
        {
            ArrayList<Boolean> temp6 = new ArrayList();
            for (Inputfield_asot b : this.inputfield_asots) {
                temp6.add(b.active);
            }
            this.active_states.add(temp6);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash6 = new ArrayList();
            trash6.add(false);
            this.active_states.add(trash6);
        }

        if(this.inputfield_asoss.size() > 0)
        {
            ArrayList<Boolean> temp7 = new ArrayList();
            for (Inputfield_asos b : this.inputfield_asoss) {
                temp7.add(b.active);
            }
            this.active_states.add(temp7);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash7 = new ArrayList();
            trash7.add(false);
            this.active_states.add(trash7);
        }

        if(this.radiobutton_asots.size() > 0)
        {
            ArrayList<Boolean> temp8 = new ArrayList();
            for (Radiobutton_asot b : this.radiobutton_asots) {
                temp8.add(b.active);
            }
            this.active_states.add(temp8);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash8 = new ArrayList();
            trash8.add(false);
            this.active_states.add(trash8);
        }

        if(this.radiobutton_asoss.size() > 0)
        {
            ArrayList<Boolean> temp9 = new ArrayList();
            for (Radiobutton_asos b : this.radiobutton_asoss) {
                temp9.add(b.active);
            }
            this.active_states.add(temp9);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash9 = new ArrayList();
            trash9.add(false);
            this.active_states.add(trash9);
        }

        if(this.table_bbs.size() > 0)
        {
            ArrayList<Boolean> temp10 = new ArrayList();
            for (Table_BB b : this.table_bbs) {
                temp10.add(b.active);
            }
            this.active_states.add(temp10);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash10 = new ArrayList();
            trash10.add(false);
            this.active_states.add(trash10);
        }

        if(this.dropdownlist_asots.size() > 0)
        {
            ArrayList<Boolean> temp11 = new ArrayList();
            for (Dropdownlist_asot b : this.dropdownlist_asots) {
                temp11.add(b.active);
            }
            this.active_states.add(temp11);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash11 = new ArrayList();
            trash11.add(false);
            this.active_states.add(trash11);
        }

        if(this.dropdownlist_asoss.size() > 0)
        {
            ArrayList<Boolean> temp12 = new ArrayList();
            for (Dropdownlist_asos b : this.dropdownlist_asoss) {
                temp12.add(b.active);
            }
            this.active_states.add(temp12);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash12 = new ArrayList();
            trash12.add(false);
            this.active_states.add(trash12);
        }

        if(this.sliders.size() > 0)
        {
            ArrayList<Boolean> temp13 = new ArrayList();
            for (Slider b : this.sliders) {
                temp13.add(b.active);
            }
            this.active_states.add(temp13);
        }
        else
        {
            //trash list to ensure right index
            ArrayList<Boolean> trash13 = new ArrayList();
            trash13.add(false);
            this.active_states.add(trash13);
        }
    }

    protected void reset_active_states()
    {
        /* save order:
        Button_asot
        Button_asos
        Checkbox
        Label_asot
        Label_asos
        Inputfield_asot
        Inputfield_asos
        Radiobutton_asot
        Radiobutton_asos
        Table_BB
        Dropdownlist_asot
        Dropdownlist_asos
        Slider
         */

        if(this.button_asots.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(0).size(); i++) {
                this.button_asots.get(i).active = this.active_states.get(0).get(i);
            }
        }

        if(this.button_asoss.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(1).size(); i++) {
                this.button_asoss.get(i).active = this.active_states.get(1).get(i);
            }
        }

        if(this.checkboxes.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(2).size(); i++) {
                this.checkboxes.get(i).active = this.active_states.get(2).get(i);
            }
        }

        if(this.label_asots.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(3).size(); i++) {
                this.label_asots.get(i).active = this.active_states.get(3).get(i);
            }
        }

        if(this.label_asoss.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(4).size(); i++) {
                this.label_asoss.get(i).active = this.active_states.get(4).get(i);
            }
        }

        if(this.inputfield_asots.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(5).size(); i++) {
                this.inputfield_asots.get(i).active = this.active_states.get(5).get(i);
            }
        }

        if(this.inputfield_asoss.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(6).size(); i++) {
                this.inputfield_asoss.get(i).active = this.active_states.get(6).get(i);
            }
        }

        if(this.radiobutton_asots.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(7).size(); i++) {
                this.radiobutton_asots.get(i).active = this.active_states.get(7).get(i);
            }
        }

        if(this.radiobutton_asoss.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(8).size(); i++) {
                this.radiobutton_asoss.get(i).active = this.active_states.get(8).get(i);
            }
        }

        if(this.table_bbs.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(9).size(); i++) {
                this.table_bbs.get(i).active = this.active_states.get(9).get(i);
            }
        }

        if(this.dropdownlist_asots.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(10).size(); i++) {
                this.dropdownlist_asots.get(i).active = this.active_states.get(10).get(i);
            }
        }

        if(this.dropdownlist_asoss.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(11).size(); i++) {
                this.dropdownlist_asoss.get(i).active = this.active_states.get(11).get(i);
            }
        }

        if(this.sliders.size() > 0)
        {
            for (int i = 0; i < this.active_states.get(12).size(); i++) {
                this.sliders.get(i).active = this.active_states.get(12).get(i);
            }
        }
    }

    //sets the background color(may cause issue with color transforming)
    public void setSurfacecolor(int col)
    {
        this.backgroundcolor = col;
    }

    //sets a background image(always strechted to sketch bounds, so make sure image has the same size) -> always rendered
    //above the surfacecolor so if there is alpha then the backgroundcolor sees through
    public void setSurfaceimage(PImage img, boolean stretch)
    {
        this.stretch_backgroundimg = stretch;
        this.backgroundimg = img;

        //calculate the max scale size of the image
        if(!this.stretch_backgroundimg)
        {
            if(this.backgroundimg.width > this.backgroundimg.height)
            {
                float imgfactor = (float)this.backgroundimg.width/(float)this.backgroundimg.height;
                this.max_imgsize_y = (float)this.ref.height;
                this.max_imgsize_x = (this.ref.width*imgfactor);
            }
            //quadratic also covered
            else
            {
                float imgfactor = (float)this.backgroundimg.height/(float)this.backgroundimg.width;
                this.max_imgsize_x = (float)this.ref.width;
                this.max_imgsize_y = (this.ref.height*imgfactor);
            }


            //if stretch is activated it is stretched to the sketch size
            if(this.stretch_backgroundimg)
            {
               this.max_imgsize_x = (float)this.ref.width;
               this.max_imgsize_y = (float)this.ref.height;
            }
        }
    }

    //get the position -> useful for animating things along the surface animation while transitioning, which are not part
    //of beastybuttons
    public float[] getPosition()
    {
        float[] temp = {this.posx, this.posy};
        return temp;
    }

    //**** INTERNAL CALLED METHODS ****

    //render queue
    //does not render widgets only renders the background for the surface(backgroundcolor and backgroundimg)
    protected void render()
    {
        //render the from the middle
        this.ref.rectMode(PConstants.CENTER);

        //fill color
        this.ref.fill(this.backgroundcolor);
        this.ref.noStroke();

        //render backgroundcolor(transparent by default)
        this.ref.image(this.backgroundimg, this.posx, this.posy, this.max_imgsize_x, this.max_imgsize_y);
        this.ref.rect(this.posx, this.posy, this.ref.width, this.ref.height);



        //set back to CORNER which is default
        this.ref.rectMode(PConstants.CORNER);
    }
}