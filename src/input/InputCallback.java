/**
 * @Author: RogerDTZ
 * @FileName: InputCallback.java
 */

package input;

import newData.Vct;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public interface InputCallback {

    void onMousePressed(MouseEvent e);

    void onMouseReleased(MouseEvent e);

    void onMouseMoved(Vct mousePos);

    void onKeyPressed(KeyEvent e);

    void onKeyReleased(KeyEvent e);

    void onKeyTyped(KeyEvent e);

    void onMouseWheelMoved(MouseWheelEvent e);

}
