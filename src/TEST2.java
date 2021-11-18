import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;

public class TEST2 {
    public static void main(String[] args) {
        JOGL_1 scene_test = new JOGL_1();
        GLCanvas canvas = new GLCanvas();
        JFrame jf = new JFrame("TESt");

        canvas.setSize(500,500);
        jf.setSize(500,500);

        canvas.addGLEventListener(scene_test);
        jf.getContentPane().add(canvas);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jf.setVisible(true);
    }
}
