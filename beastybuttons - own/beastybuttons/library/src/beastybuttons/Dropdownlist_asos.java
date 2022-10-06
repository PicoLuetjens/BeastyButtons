package beastybuttons;

import processing.core.PApplet;

public class Dropdownlist_asos extends Widget
{
    //is the widget already added to a surface or the world
    protected boolean already_added = false;

    protected String sourcePath;

    protected int layer = 0;

    protected String id = "id";

    Dropdownlist_asos()
    {

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


    protected void render()
    {

    }

    protected void rendertooltip()
    {

    }
}
