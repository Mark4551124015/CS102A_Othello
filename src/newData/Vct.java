package newData;

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



}
