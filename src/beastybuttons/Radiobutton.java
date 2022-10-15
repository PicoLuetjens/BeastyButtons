package beastybuttons;

import java.util.ArrayList;

import processing.core.*;
/*
@NotImplementedYet
@Experimental
public class Radiobutton extends Widget
{
	//******VARIABLES******
	
	//Instances of the Inputfield
	protected static int INSTANCES = 0;
	
	//PApplet ref
	private final PApplet REF;
	
	private boolean round;
	
	private float[] sizes = {0f, 0f};
	
	private ArrayList<Field> fields = new ArrayList<>();
	
	private float textSize;
	
	
	
	//******CONSTRUCTORS******
	
	public Radiobutton(PApplet ref, float xsize, float ysize, String text) {
		this.REF = ref;
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		this.fields.add(new Field(text, true));
	}
	
	public Radiobutton(PApplet ref, float xsize, float ysize, String text, boolean round) {
		this.REF = ref;
		this.sizes[0] = xsize;
		this.sizes[1] = ysize;
		this.fields.add(new Field(text, true));
		this.round = round;
	}
	
	public Radiobutton(PApplet ref, float size, String text) {
		this.REF = ref;
		this.textSize = size;
		this.fields.add(new Field(text, true));
	}
	
	public Radiobutton(PApplet ref, float size, String text, boolean round) {
		this.REF = ref;
		this.textSize = size;
		this.fields.add(new Field(text, true));
		this.round = round;
	}
	
	
	//import constructor
	protected Radiobutton(PApplet ref) {
		this.REF = ref;
	}
	
	
	
	//******CALLED FROM CONSTRUCTOR******
	
	
	//generate the colors after sketch can be referenced
    private void generateColors() {
        this.foreground = this.REF.color(0, 0, 0, 255);
        this.background = this.REF.color(100, 100, 100, 255);
        this.overcolor = this.REF.color(250, 0, 0, 255);
        this.clickcolor = this.REF.color(150, 0, 150, 255);
        this.ttbackground = this.REF.color(200, 200, 200, 255);
        this.ttforeground = this.REF.color(0, 0, 0, 255);
    }
	
    
    //generate the ID
    private void generateID() {
        String convert = String.valueOf(Radiobutton.INSTANCES);
        this.ID = "Radiobutton" + convert;
        Radiobutton.INSTANCES+=1;
        this.SOURCEPATH = this.ID;
    }
	
	
    public Radiobutton addField(String text, boolean update) {
    	this.fields.add(new Field(text, false));
    	
    	return this;
    }
    
	private class Field
	{
		public String text;
		public boolean selected = false;
		public float sizex, sizey;
		public float posx, posy;
		
		public Field(String text, boolean selected) {
			this.text = text;
			this.selected = selected;
			this.calculateSize();
		}
		
		public boolean overfield(float mx, float my) {
	    	if(mx >= this.posx-this.sizex/2 && mx <= this.posx+this.sizex/2)
	        {
	            if(my >= this.posx-this.posy/2 && my <= this.posy+this.sizey/2)
	            {
	                return true;
	            }
	        }
	        return false;
	    }
	}
}*/
