
package component.animation;

public class Animation {

    public enum CurveType {
        Tan,
        Smooth
    }

    private CurveType type;
    private boolean active;
    private boolean positive;
    private double value;
    private double startValue;
    private double targetValue;
    private double delay;
    private double time;
    private double duration;
    private double pa, pb, pc;
    private double highVal;


    public static Animation GetTanh(double st, double ed, double duration, boolean positive, double delay) {
        Animation res = new Animation();
        res.setTanh(st, ed, duration, positive);
        res.setDelay(delay);
        return res;
    }

    public static Animation GetTanh(double st, double ed, double duration, boolean positive) {
        return GetTanh(st, ed, duration, positive, 0);
    }


    public static Animation GetSmooth(double st, double ed, double duration, double delay) {
        Animation res = new Animation();
        res.setSmooth(st, ed, duration);
        res.setDelay(delay);
        return res;
    }

    public static Animation GetSmooth(double st, double ed, double duration) {
        return GetSmooth(st, ed, duration, 0);
    }

    public double getDuration() {
        return this.delay + this.duration;
    }

    public double getRest() {
        return Math.max(0, this.duration - this.time);
    }


    public void setTanh(double st, double ed, double duration, boolean positive) {
        this.type = CurveType.Tan;
        this.time = 0;
        this.positive = positive;
        this.duration = duration;
        this.value = this.startValue = st;
        this.targetValue = ed;

        if (positive) {
            this.pa = ed - st;
            this.pb = 1 / duration;
            this.pc = st;
        } else {
            this.pa = ed - st;
            this.pb = 1 / duration;
            this.pc = ed;
        }

        this.active = true;
    }


    public void setSmooth(double st, double ed, double duration) {
        this.type = CurveType.Smooth;
        this.time = 0;
        this.duration = duration;
        this.value = this.startValue = st;
        this.targetValue = ed;
        this.pa = ed - st;
        this.pb = 2 / duration;
        this.pc = st;
        this.active = true;
    }

    public void update(double dt) {
        if (!this.active)
            return;
        this.time += dt;
        if (this.time < 0)
            this.value = startValue;
        else if (this.time <= this.duration)
            this.value = getValue(this.time);
        else
            this.value = targetValue;
    }

    private double getValue(double x) {
        switch (this.type) {

            case Tan:
                return this.positive ? this.pc + this.pa * Math.tanh(3 * this.pb * x) : this.pc + this.pa * Math.tanh(3 * (this.pb * x - 1));

            case Smooth:
                return this.pa * (Math.tanh(3 * (-1 + x * this.pb)) * 0.5 + 0.5) + this.pc;
        }
        return 0;
    }

    public void reset() {
        switch (this.type) {
            case Tan:
                this.setTanh(this.startValue, this.targetValue, this.duration, this.positive);
                this.setDelay(this.delay);
                break;
            case Smooth:
                this.setSmooth(this.startValue, this.highVal, this.duration);
                this.setDelay(this.delay);
                break;
        }
    }

    public double val() {
        return this.value;
    }

    public void setActive(boolean flag) {
        this.active = flag;
    }

    public void setDelay(double delay) {
        this.delay = delay;
        this.time = -delay;
    }

    public boolean isFinished() {
        return this.time >= duration;
    }

    public double getEndVal() {
        return this.targetValue;
    }

    public double getDelay() {
        return this.delay;
    }

}
