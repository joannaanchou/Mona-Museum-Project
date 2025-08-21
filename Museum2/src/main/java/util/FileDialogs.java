package util;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;

//另存檔案的彈窗、自動補副檔名
public final class FileDialogs {
    private FileDialogs() {}

    //開啟另存對話框；requiredExt 例：".xlsx" 
    public static File chooseSave(Component parent, String defaultName, String requiredExt) {
        JFileChooser fc = new JFileChooser();
        if (defaultName != null && !defaultName.isBlank()) {
            fc.setSelectedFile(new File(defaultName));
        }
        fc.setDialogTitle("請選擇匯出檔案的位置");
        int ans = fc.showSaveDialog(parent);
        if (ans != JFileChooser.APPROVE_OPTION) return null;

        File f = fc.getSelectedFile();
        if (requiredExt != null && !requiredExt.isBlank()) {
            String lower = f.getName().toLowerCase();
            if (!lower.endsWith(requiredExt.toLowerCase())) {
                f = new File(f.getParentFile(), f.getName() + requiredExt);
            }
        }
        return f;
    }
}
