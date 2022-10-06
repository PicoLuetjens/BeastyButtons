package beastybuttons;

abstract class Widget {

    public abstract void setLayer(int layer);

    public abstract void setID(String id);

    //render method for rendering objects with this type
    public abstract void render();

    //rendertooltip method for rendering objects with this type
    public abstract void rendertooltip();
}
