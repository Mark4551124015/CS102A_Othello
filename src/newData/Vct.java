package newData;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Vct {
    public double x,y;

    //初始化
    public Vct() {
        this.x = this.y = 0.0;
    }
    public Vct(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //取模
    public double getModulus() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    //缩放
    public Vct scalar(double c) {
        this.x *= c;
        this.y *= c;
        return this;
    }

    //相对加减
    public Vct add(Vct rhs) {
        this.x += rhs.x;
        this.y += rhs.y;
        return this;
    }

    //变形
    public Vct transform(AffineTransform at) {
        Point2D.Double src = new Point2D.Double(this.x, this.y);
        Point2D.Double dst = null;
        dst = (Point2D.Double)(at.transform(src, dst));
        this.x = dst.x;
        this.y = dst.y;
        return this;
    }

    public static Vct scalar(Vct v, double c) {
        return new Vct(v.x * c, v.y * c);
    }



}
