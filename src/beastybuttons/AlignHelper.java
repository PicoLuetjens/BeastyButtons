package beastybuttons;


import java.util.ArrayList;

import processing.core.*;


//this is a class that contains helper methods for alignment 
//NOTE: When using AlignHelper to set the position the tooltip position will be updated automatically once

public class AlignHelper {
	
	
	//******VARIABLES******
	
	//PApplet ref
	private final PApplet REF;
	
	public AlignHelper(PApplet ref) {
		this.REF = ref;
	}
	
	
	
	
	//******ALIGN METHODS******
	
	//aligns horizontal by calculating the positions, not considering the amount of pixels between each widget
	public void alignHorizontalByFrame(float yposition, ArrayList<Widget> widgets) {
		int widget_amount = widgets.size();
		float total_size = 0f;
		
		for(Widget w : widgets) {
			total_size += w.sizes[0];
		}
		
		if(total_size >= this.REF.width) {
			PApplet.println("WARNING(BB): COULD NOT ALIGN THE ITEMS HORIZONTAL BECAUSE THE WINDOW IS TOO SHORT -> CONTINUEING WITHOUT ALIGNING");
			return;
		}
		
		
		//float gap = total_size/((float)widget_amount+2f);
		float gap = this.REF.width/((float)widget_amount+1f);
		
		
		int index = 1;
		for(Widget w : widgets) {
			w.positions[0] = gap*(float)index;
			w.positions[1] = yposition;
			w.calc_tt_auto_pos();
			index++;
		}
		
	}
	
	
	//aligns vertical by calculating the positions, not considering the amount of pixels between each widget
	public void alignVertikalByFrame(float xposition, ArrayList<Widget> widgets) {
		int widget_amount = widgets.size();
		float total_size = 0f;
		
		for(Widget w : widgets) {
			total_size += w.sizes[1];
		}
		
		if(total_size >= this.REF.height) {
			PApplet.println("WARNING(BB): COULD NOT ALIGN THE ITEMS VERICAL BECAUSE THE WINDOW IS TOO SHORT -> CONTINUEING WITHOUT ALIGNING");
			return;
		}
		
		
		//float gap = total_size/((float)widget_amount+2f);
		float gap = this.REF.height/((float)widget_amount+1f);
		
		
		int index = 1;
		for(Widget w : widgets) {
			w.positions[1] = gap*(float)index;
			w.positions[0] = xposition;
			w.calc_tt_auto_pos();
			index++;
		}
		
	}
	
	//aligns Widgets in a circle with an angle based on the given widget amount
	public void alignCirclebyRadius(float xposition, float yposition, float radius, ArrayList<Widget> widgets) {
		float angle_gap = 360f/(float)widgets.size();
		
		if(radius > this.REF.width || radius > this.REF.height) {
			PApplet.println("WARNING(BB): COULD NOT ALIGN THE ITEMS IN CIRCLE BECAUSE THE WINDOW IS TOO SHORT -> CONTINUEING WITHOUT ALIGNING");
			return;
		}
		
		if(widgets.size() < 3) {
			PApplet.println("WARNING(BB): COULD NOT ALIGN THE ITEMS IN CIRCLE BECAUSE THE GIVEN AMOUNT IS LESS THEN 3 -> CONTINUEING WITHOUT ALIGNING");
			return;
		}
		
		for(int i = 1; i <= widgets.size(); i++) {
			float x = radius*PApplet.cos(((angle_gap/180)*PConstants.PI)*(float)i) + xposition;
			float y = radius*PApplet.sin(((angle_gap/180)*PConstants.PI)*(float)i) + yposition;
			
			widgets.get(i-1).positions[0] = x;
			widgets.get(i-1).positions[1] = y;
			widgets.get(i-1).calc_tt_auto_pos();
		}
	}
	
	
	
	//******DEPRECATED VERSIONS OR SYNTAX NOT SUPPORTED BY PROCESSING******
	
	/*
	//aligns horizontal by calculating the positions, not considering the amount of pixels between each widget
	@Experimental
	public void alignHorizontalByFrame(float yposition, Widget... widgets) {
		int widget_amount = widgets.length;
		float total_size = 0f;
		
		for(Widget w : widgets) {
			total_size += w.sizes[0];
		}
		
		if(total_size >= this.REF.width) {
			PApplet.println("WARNING(BB): COULD NOT ALIGN THE ITEMS HORIZONTAL BECAUSE THE WINDOW IS TOO SHORT -> CONTINUEING WITHOUT ALIGNING");
			return;
		}
		
		
		float gap = total_size/((float)widget_amount+2f);
		
		int index = 1;
		for(Widget w : widgets) {
			w.positions[0] = gap*(float)index;
			w.positions[1] = yposition;
			w.calc_tt_auto_pos();
			index++;
		}
		
	}
	
	
	
	//aligns horizontal by considering the given gap 
	@Experimental
	public void alignHorizontalByGap(float yposition, float gap, Widget... widgets) {
		float total_size = (float)(widgets.length-1)*gap;
		
		if(total_size >= this.REF.width) {
			PApplet.println("WARNING(BB): COULD NOT ALIGN THE ITEMS HORIZONTAL BECAUSE THE WINDOW IS TOO SHORT -> CONTINUEING WITHOUT ALIGNING");
			return;
		}
		
		float rest_size = this.REF.width-total_size;
		
		for(int i = 1; i >= widgets.length; i++) {
			if(i == 1) {
				widgets[i-1].positions[0] = rest_size/2f;
			}
			else if(i == widgets.length) {
				widgets[i-1].positions[0] = this.REF.width-(rest_size/2f);
			}
			else {
				widgets[i-1].positions[0] = (rest_size/2) + (gap*((float)i-1));
			}
			widgets[i].positions[1] = yposition;
		}
	}
	
	
	//aligns vertical by calculating the positions, not considering the amount of pixels between each widget
	@Experimental
	public void alignVertikalByFrame(float xposition, Widget... widgets) {
		int widget_amount = widgets.length;
		float total_size = 0f;
		
		for(Widget w : widgets) {
			total_size += w.sizes[1];
		}
		
		if(total_size >= this.REF.height) {
			PApplet.println("WARNING(BB): COULD NOT ALIGN THE ITEMS VERICAL BECAUSE THE WINDOW IS TOO SHORT -> CONTINUEING WITHOUT ALIGNING");
			return;
		}
		
		
		float gap = total_size/((float)widget_amount+2f);
		
		int index = 1;
		for(Widget w : widgets) {
			w.positions[1] = gap*(float)index;
			w.positions[0] = xposition;
			w.calc_tt_auto_pos();
			index++;
		}
		
	}
	
	
	//aligns horizontal by considering the given gap 
	@Experimental
	public void alignVerticalByGap(float xposition, float gap, Widget... widgets) {
		float total_size = (float)(widgets.length-1)*gap;
		
		if(total_size >= this.REF.height) {
			PApplet.println("WARNING(BB): COULD NOT ALIGN THE ITEMS HORIZONTAL BECAUSE THE WINDOW IS TOO SHORT -> CONTINUEING WITHOUT ALIGNING");
			return;
		}
		
		float rest_size = this.REF.height-total_size;
		
		for(int i = 1; i >= widgets.length; i++) {
			if(i == 1) {
				widgets[i-1].positions[1] = rest_size/2f;
			}
			else if(i == widgets.length) {
				widgets[i-1].positions[1] = this.REF.height-(rest_size/2f);
			}
			else {
				widgets[i-1].positions[1] = (rest_size/2) + (gap*((float)i-1));
			}
			widgets[i].positions[0] = xposition;
		}
	}
	
	
	@Experimental
	public void alignCirclebyRadius(float xposition, float yposition, float radius, Widget...widgets) {
		float angle_gap = 360f/(float)widgets.length;
		
		if(radius > this.REF.width || radius > this.REF.height) {
			PApplet.println("WARNING(BB): COULD NOT ALIGN THE ITEMS IN CIRCLE BECAUSE THE WINDOW IS TOO SHORT -> CONTINUEING WITHOUT ALIGNING");
		}
		
		if(widgets.length < 3) {
			PApplet.println("WARNING(BB): COULD NOT ALIGN THE ITEMS IN CIRCLE BECAUSE THE GIVEN AMOUNT IS LESS THEN 3 -> CONTINUEING WITHOUT ALIGNING");
		}
		
		for(int i = 1; i <= widgets.length; i++) {
			float x = radius*PApplet.cos(angle_gap*(float)i) + xposition;
			float y = radius*PApplet.sin(angle_gap*(float)i) + yposition;
			
			widgets[i-1].positions[0] = x;
			widgets[i-1].positions[1] = y;
			widgets[i-1].calc_tt_auto_pos();
		}
		
	}
	*/
}
