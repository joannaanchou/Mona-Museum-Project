package util;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;


//列印＆匯出pdf工具
public final class Printing {
    private Printing() {}

    public static void printComponent(Component comp, String jobName) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName(jobName == null ? comp.getClass().getSimpleName() : jobName);

        job.setPrintable(new Printable() {
            @Override
            public int print(java.awt.Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
                Graphics2D g2 = (Graphics2D) g.create();

                double iw = pf.getImageableWidth();
                double ih = pf.getImageableHeight();
                double sx = iw / comp.getWidth();
                double sy = ih / comp.getHeight();
                double scale = Math.min(sx, sy);

                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(scale, scale);
                comp.printAll(g2);
                g2.dispose();
                return Printable.PAGE_EXISTS;
            }
        });

        if (job.printDialog()) job.printDialog();
        try { if (job.printDialog()) job.print(); } catch (PrinterException e) { throw new RuntimeException(e); }
    }
}
