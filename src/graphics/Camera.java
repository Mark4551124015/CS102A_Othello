
package graphics;

import component.animation.Animation;
import component.animation.Animator;
import object.OthelloObject;

import java.awt.geom.AffineTransform;

public class Camera {

    private static final OthelloObject obj = new OthelloObject("camera_obj") ;

    private static final Animator rotate = new Animator(0);
    private static final Animator px = new Animator(0);
    private static final Animator py = new Animator(0);
    private static final Animator scale = new Animator(1);


    public static void update(double dt) {
        rotate.update(dt);
        px.update(dt);
        py.update(dt);
        scale.update(dt);

        obj.setAngle(rotate.val());
        obj.setPosition(px.val(), py.val());
        obj.setScale(scale.val());
    }

    public static AffineTransform getView() {
        return obj.getAbsoluteTransform();
    }

}
