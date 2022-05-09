package newData;

import java.awt.geom.AffineTransform;

public class Trans {

    public Vct position;
    public double angle;
    public Vct scale;

    public Trans(Vct position, double angle, Vct scale) {
        this.angle = angle;
        this.position = position;
        this.scale = scale;
    }

    public Trans(){
        this(new Vct(0.0,0.0), 0.0 ,new Vct(1,1));
    }

    public AffineTransform concatenate(AffineTransform inputAt) {
        AffineTransform at = (AffineTransform) inputAt.clone();
        at.translate(this.position.x, this.position.y);
        at.rotate(-Math.toRadians(this.angle));
        at.scale(this.scale.x, this.scale.y);
        return at;
    }

    public void rotate(double angle) {
        this.angle += angle;
    }

    public void translate(Vct delta) {
        this.position.add(delta);
    }

}

