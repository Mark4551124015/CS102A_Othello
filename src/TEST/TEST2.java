package TEST;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TEST2 {
    public static void main(String[] args) {
        // Run the GUI codes in the event-dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 创建OpenGL渲染画布
                GLCanvas canvas = new GLCanvas();
                canvas.setPreferredSize(new Dimension(800   , 600));

                // 创建一个Animator以一个指定的帧率驱动画布display()方法
                final FPSAnimator animator = new FPSAnimator(canvas, 144, true);

                // 创建顶级容器，并将画布添加进去
                final JFrame frame = new JFrame();
                frame.getContentPane().add(canvas);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        // 监听窗口关闭事件，程序关闭时执行
                        new Thread(() -> {
                                if (animator.isStarted()) animator.stop();
                                System.exit(0);
                        }).start();
                    }
                });
                frame.setTitle("TITLE");
                frame.pack();
                frame.setVisible(true);
                animator.start(); // 开始循环动画
            }
        });
    }
}
