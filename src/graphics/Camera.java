
package graphics;

import component.animation.Animation;
import component.animation.Animator;
import object.OthelloObject;

import java.awt.geom.AffineTransform;

public class Camera {

    private static OthelloObject obj = new OthelloObject("camera_obj") ;

    private static Animator rotate = new Animator(0);
    private static Animator px = new Animator(0);
    private static Animator py = new Animator(0);
    private static Animator scale = new Animator(1);


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
