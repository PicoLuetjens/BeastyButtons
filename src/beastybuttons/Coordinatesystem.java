package beastybuttons;

import java.util.ArrayList;

import processing.core.*;

@NotImplementedYet
@Experimental
public class Coordinatesystem {
	
	//******VARIABLES******
	
	//Instances of the Inputfield
	protected static int INSTANCES = 0;
	
	//PApplet ref
	private final PApplet REF;
	
	
	
	public Coordinatesystem(PApplet ref) {
		this.REF = ref;
	}
	
	private class Graph {
		ArrayList <PVector> points = new ArrayList<>();
		
		
		Graph(){
			
		}
	}
}
