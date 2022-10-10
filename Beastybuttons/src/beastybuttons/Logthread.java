package beastybuttons;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Logthread implements Runnable{
	private String path;
	private ArrayList<String> loglist;
	
	Logthread(String path, ArrayList<String> loglist){
		this.path = path;
		this.loglist = loglist;
	}
	
	@Override
	public void run() {
		try {
			FileWriter writer = new FileWriter("output.txt"); 
			for(String str: this.loglist) {
			  writer.write(str + System.lineSeparator());
			}
			writer.close();
		}
		catch(IOException e) {
			throw new RuntimeException("Error while writing logfile to file system");
		}
	}
}
