package util;

import javax.swing.*;

public class ErrorMsg {

    public static void error(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
