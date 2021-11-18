import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class JOGL_1 implements GLEventListener {
        private GL2 gl;
        @Override
        public void init(GLAutoDrawable glAutoDrawable) {
            gl=glAutoDrawable.getGL().getGL2();
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable) {}

        @Override
        public void display(GLAutoDrawable glAutoDrawable) {
            gl.glBegin(GL2.GL_POLYGON);
            gl.glVertex2f(0f,1f);
            gl.glVertex2f(1f,0f);
            gl.glVertex2f(0f,0f);
            gl.glEnd();
        }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }
}

