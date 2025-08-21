package util;

import java.awt.Component;
import javax.swing.JOptionPane;

//統一訊息彈窗
public final class Dialogs {
    private Dialogs() {}

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "錯誤", JOptionPane.ERROR_MESSAGE);
    }

    /** OK= true；Cancel/Close = false */
    public static boolean confirm(Component parent, String message) {
        int ans = JOptionPane.showConfirmDialog(parent, message, "確認", JOptionPane.OK_CANCEL_OPTION);
        return ans == JOptionPane.OK_OPTION;
    }
}
