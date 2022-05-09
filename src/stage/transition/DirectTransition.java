
package stage.transition;

import java.awt.*;

public class DirectTransition implements Transition {

    private boolean active;


    public DirectTransition() {
        this.active = false;
    }

    public void init() {
        active = true;
    }

    public void update(double dt) {
    }

    public void render(Graphics2D g2d) {
    }

    public boolean isFinished() {
        return this.active;
    }
}
