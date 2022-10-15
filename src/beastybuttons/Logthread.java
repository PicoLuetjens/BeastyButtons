package beastybuttons;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import processing.core.*;

class Logthread implements Runnable{
	private String path;
	private ArrayList<String> loglist;
	private PApplet REF;
	
	Logthread(PApplet ref, String path, ArrayList<String> loglist){
		this.path = path;
		this.loglist = loglist;
		this.REF = ref;
	}
	
	
	@Override
	public void run() {
		try {
			String[] list = new String[this.loglist.size()];
			for(int i = 0; i < this.loglist.size(); i++) {
				list[i] = this.loglist.get(i);
			}
			PApplet.println(this.path);
			this.REF.saveStrings(this.path, list);
		}
		catch(Exception e) {
			throw new RuntimeException("Error while writing logfile to file system");
		}
	}
	
}
