package beastybuttons;

import java.util.ArrayList;

import processing.core.*;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class Info {
// myParent is a reference to the parent sketch
	PApplet myParent;

	//int myVariable = 0;
	
	public final static String VERSION = "##library.prettyVersion##";
	
	private static PShape LOGO;
	
	private static PImage ICON;
	
	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param theParent the parent PApplet
	 */
	public Info(PApplet theParent) {
		myParent = theParent;
		LOGO = myParent.loadShape("Beastybuttons Logo.svg");
		ICON = myParent.loadImage("Beastybuttons Icon.png");
	}
	
	
	public void info() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}
	
	/*
	public String sayHello() {
		return "hello library.";
	}
	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	
	
	public static String version() {
		return VERSION;
	}
	
	
	public PShape logo() {
		return LOGO;
	}
	
	public PImage icon() {
		return ICON;
	}
	
	
	public class Presets{
		//Presets to import and start building the UI that come with BeastyButtons Info Class
		public final static String PRESET_1280x720_2SURFACE_3TOPBUTTONBAR = "Presets/surface/Preset_2surface_3topbuttonbar/Preset_1280x720_2surface_3topbuttonbar.beasty";
		public final static String PRESET_1920x1080_2SURFACE_3TOPBUTTONBAR = "Presets/surface/Preset_2surface_3topbuttonbar/Preset_1920x1080_2surface_3topbuttonbar.beasty";
		public final static String PRESET_3480x2160_2SURFACE_3TOPBUTTONBAR = "Presets/surface/Preset_2surface_3topbuttonbar/Preset_3840x2160_2surface_3topbuttonbar.beasty";
		public final static String PRESET_640x480_2SURFACE_3TOPBUTTONBAR = "Presets/surface/Preset_2surface_3topbuttonbar/Preset_640x480_2surface_3topbuttonbar.beasty";
		public final static String PRESET_800x480_2SURFACE_3TOPBUTTONBAR = "Presets/surface/Preset_2surface_3topbuttonbar/Preset_800x480_2surface_3topbuttonbar.beasty";
		public final static String PRESET_800x600_2SURFACE_3TOPBUTTONBAR = "Presets/surface/Preset_2surface_3topbuttonbar/Preset_800x600_2surface_3topbuttonbar.beasty";
		public final static String PRESET_1280x720_2SURFACE_4TOPBUTTONBAR = "Presets/surface/Preset_2surface_4topbuttonbar/Preset_1280x720_2surface_4topbuttonbar.beasty";
		public final static String PRESET_1920x1080_2SURFACE_4TOPBUTTONBAR = "Presets/surface/Preset_2surface_4topbuttonbar/Preset_1920x1080_2surface_4topbuttonbar.beasty";
		public final static String PRESET_3840x2160_2SURFACE_4TOPBUTTONBAR = "Presets/surface/Preset_2surface_4topbuttonbar/Preset_3840x2160_2surface_4topbuttonbar.beasty";
		public final static String PRESET_640x480_2SURFACE_4TOPBUTTONBAR = "Presets/surface/Preset_2surface_4topbuttonbar/Preset_640x480_2surface_4topbuttonbar.beasty";
		public final static String PRESET_800x480_2SURFACE_4TOPBUTTONBAR = "Presets/surface/Preset_2surface_4topbuttonbar/Preset_800x480_2surface_4topbuttonbar.beasty";
		public final static String PRESET_800x600_2SURFACE_4TOPBUTTONBAR = "Presets/surface/Preset_2surface_4topbuttonbar/Preset_800x600_2surface_4topbuttonbar.beasty";
		public final static String PRESET_1280x720_2SURFACE_5BUTTONPIECHART = "Presets/surface/Preset_2surface_5buttonpiechart/Preset_1280x720_2surface_5buttonpiechart.beasty";
		public final static String PRESET_1920x1080_2SURFACE_5BUTTONPIECHART = "Presets/surface/Preset_2surface_5buttonpiechart/Preset_1920x1080_2surface_5buttonpiechart.beasty";
		public final static String PRESET_3840x2160_2SURFACE_5BUTTONPIECHART = "Presets/surface/Preset_2surface_5buttonpiechart/Preset_3840x2160_2surface_5buttonpiechart.beasty";
		public final static String PRESET_640x480_2SURFACE_5BUTTONPIECHART = "Presets/surface/Preset_2surface_5buttonpiechart/Preset_640x480_2surface_5buttonpiechart.beasty";
		public final static String PRESET_800x480_2SURFACE_5BUTTONPIECHART = "Presets/surface/Preset_2surface_5buttonpiechart/Preset_800x480_2surface_5buttonpiechart.beasty";
		public final static String PRESET_800x600_2SURFACE_5BUTTONPIECHART = "Presets/surface/Preset_2surface_5buttonpiechart/Preset_800x600_2surface_5buttonpiechart.beasty";
		public final static String PRESET_1280x720_2SURFACE_6BUTTONPIECHART = "Presets/surface/Preset_2surface_6buttonpiechart/Preset_1280x720_2surface_6buttonpiechart.beasty";
		public final static String PRESET_1920x1080_2SURFACE_6BUTTONPIECHART = "Presets/surface/Preset_2surface_6buttonpiechart/Preset_1920x1080_2surface_6buttonpiechart.beasty";
		public final static String PRESET_3840x2160_2SURFACE_6BUTTONPIECHART = "Presets/surface/Preset_2surface_6buttonpiechart/Preset_3840x2160_2surface_6buttonpiechart.beasty";
		public final static String PRESET_640x480_2SURFACE_6BUTTONPIECHART = "Presets/surface/Preset_2surface_6buttonpiechart/Preset_640x480_2surface_6buttonpiechart.beasty";
		public final static String PRESET_800x480_2SURFACE_6BUTTONPIECHART = "Presets/surface/Preset_2surface_6buttonpiechart/Preset_800x480_2surface_6buttonpiechart.beasty";
		public final static String PRESET_800x600_2SURFACE_6BUTTONPIECHART = "Presets/surface/Preset_2surface_6buttonpiechart/Preset_800x600_2surface_6buttonpiechart.beasty";
	}
	
	

	/**
	 * 
	 * @param theA the width of test
	 * @param theB the height of test
	 */
	/*
	public void setVariable(int theA, int theB) {
		myVariable = theA + theB;
	}

	/**
	 * 
	 * @return int
	 */
	
	/*
	public int getVariable() {
		return myVariable;
	}
	*/
}
