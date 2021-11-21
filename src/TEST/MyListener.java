package TEST;

import com.jogamp.opengl.*;
import com.jogamp.opengl.util.GLBuffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MyListener implements GLEventListener {
    private GL3 gl;
    private final IntBuffer ArrayName = GLBuffers.newDirectIntBuffer(1); //存VAO的buffer
    private final IntBuffer BufferName = GLBuffers.newDirectIntBuffer(1); //存VBO的buffer
    private final float vertices[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };


    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        gl=glAutoDrawable.getGL().getGL3();

        initBuffer(gl);


    }



    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        gl=glAutoDrawable.getGL().getGL3();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        gl=glAutoDrawable.getGL().getGL3();
    }
    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        gl=glAutoDrawable.getGL().getGL3();
    }

    private void initBuffer(GL3 gl) {
        //VAO
        gl.glGenVertexArrays(1, ArrayName);
        gl.glBindVertexArray(ArrayName.get(0));
        //VBO
        FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(vertices);
        gl.glGenBuffers(1, BufferName);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, BufferName.get(0));
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL.GL_STATIC_DRAW );
        //设置指针读取数据的方式
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3*Float.BYTES,0);
        /*
         * 参数1 读取数据传入GPU的通道编号， 0-15可用
         * 参数2 每组数据包包含的数据个数
         * 参数3 数据类型（OPENGL预设类型）
         * 参数4 是否标准化，一般用默认false
         * 参数5 步长
         * 参数6 偏移量
         * */
        //unbind
        gl.glBindVertexArray(0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }
}

