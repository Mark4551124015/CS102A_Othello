package graphics;
import object.OthelloObject;
import newData.Vct;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Shape extends OthelloObject {
    public enum Type {
        Rectangle, Circle, Others
    }

    private Color color;
    private Type type;

    public Shape(String id, Color color, Type type, Vct size) {
        super(id,null);
        this.color = color;
        this.type = type;
        this.setSize(size);
        this.updateImage();
    }

    public void setColor(Color color) {
        this.color = color;
        this.updateImage();
    }



    public void setType(Type type) {
        this.type = type;
        this.updateImage();
    }

    private void updateImage() {
        int w = (int)Math.round(this.size.x);
        int h = (int)Math.round(this.size.y);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(this.color);
        switch(this.type) {
            case Rectangle:
                g.fillRect(0, 0, w, h);
                break;
            case Circle:
                g.fillOval(0, 0, w, h);
        }
        g.dispose();
        this.image = new Image(img);
    }
}
