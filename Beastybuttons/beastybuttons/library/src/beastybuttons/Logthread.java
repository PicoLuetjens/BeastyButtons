package beastybuttons;

import processing.core.PApplet;

//saves the file in another Thread
class Logthread extends Thread
{
    BeastyWorld world;

    Logthread(BeastyWorld world)
    {
        this.world = world;
    }
    //gets called automatically when the thread is started
    public void run()
    {
        //save logfile in here
        String[]save = new String[this.world.loglist.size()];
        for(int i = 0; i < this.world.loglist.size(); i++)
        {
            save[i] = this.world.loglist.get(i);
        }
        this.world.theparent.saveStrings(this.world.logfilepath, save);
    }
}
