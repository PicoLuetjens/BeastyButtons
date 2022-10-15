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
	
	
	public static class Presets{
		//Presets to import and start building the UI that come with BeastyButtons Info Class
		public static String TOPBAR_PRESET = "Topbar_preset";
		public static String SIDEBAR_PRESET = "Sidebar_preset";
		public static String ALL_ELEMENTS_PRESET = "All_Elements_preset";
		public static String ANIMATING_WIPE_PRESET = "Animating_Wipe_preset";
		public static String ANIMATING_OPACITY_PRESET = "Animating_Opacity_preset";
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
