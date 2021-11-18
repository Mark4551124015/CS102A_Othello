package newData;

public class Trans {
    public Vct position;
    public Vct onboard;
    public double angle;
    public Trans(Vct position, double angle) {
        this.angle = angle;
        this.position = position;
    }

    public Trans(){
        this(new Vct(0.0,0.0), 0.0 );
    }

    public void angle(double angle) {
        this.angle = angle;
    }

    public void rotate(double angle) {
        this.angle += angle;
    }

    public void translate(Vct derta) {
        this.position.add(derta);
    }

}

