package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;  

public final class Tables {
    private Tables() {}

    public static void styleHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));
        DefaultTableCellRenderer r = (DefaultTableCellRenderer) header.getDefaultRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static void rightAlign(JTable table, int... cols) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int c : cols) {
            table.getColumnModel().getColumn(c).setCellRenderer(r);
        }
    }

    public static void centerAlign(JTable table, int... cols) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        for (int c : cols) {
            table.getColumnModel().getColumn(c).setCellRenderer(r);
        }
    }

    //一般欄位斑馬紋
    public static void zebra(JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : new Color(238, 238, 238));
                return c;
            }
        });
    }
}
