package controller.admin;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import controller.porder.FindAllPorder;
import service.PorderService;
import service.impl.PorderServiceImpl;
import util.ChartUI;
import util.Convert;
import util.Dialogs;
import util.Ticket;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import javax.swing.RepaintManager;

public class ReportCharts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel chartsContainer;
	private PorderService porderService = new PorderServiceImpl();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReportCharts frame = new ReportCharts();
					frame.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ReportCharts() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 911, 743);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(248, 254, 226));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(120, 183, 240));
		panel_1.setBounds(0, 42, 910, 58);
		contentPane.add(panel_1);
		

		
		JLabel lblMonaMuseum_1 = new JLabel("Mona Museum 報表");
		lblMonaMuseum_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonaMuseum_1.setForeground(new Color(249, 255, 228));
		lblMonaMuseum_1.setFont(new Font("Dialog", Font.BOLD, 20));
		lblMonaMuseum_1.setBackground(new Color(120, 183, 240));
		lblMonaMuseum_1.setBounds(6, 6, 914, 46);
		panel_1.add(lblMonaMuseum_1);
		
		// 建框裝四張圖
		chartsContainer = new JPanel(new GridLayout(2, 2, 16, 16));  // <-- 用欄位
		chartsContainer.setBackground(Color.WHITE);
		chartsContainer.setBorder(new EmptyBorder(16,16,16,16));
		chartsContainer.setBounds(20, 120, 870, 520);
		contentPane.add(chartsContainer);


		// 套用統一圖表 UI 主題
        ChartUI.applyTheme();

        // 建立與放置圖表
        try {
            buildCharts();
        } catch (Exception ex) {
            ex.printStackTrace();
            Dialogs.error(this, "建立圖表失敗：" + ex.getMessage());
        }
    }

    //取訂單明細並建四個圖表
    private void buildCharts() {
        // 取得資料：欄位順序
        // 0:porder_item_id, 1:created_at, 2:order_no, 3:member_id, 4:order_date,
        // 5:ticket_type_id, 6:ticket_period_id, 7:quantity, 8:unit_price
        List<Object[]> rows = porderService.getAllOrderDetails();

        // 彙總容器
        Map<String, Integer> qtyByProduct   = new HashMap<>(); // 產品(票種x票期)->銷量
        Map<String, Integer> amtByProduct   = new HashMap<>(); // 產品(票種x票期)->銷售額
        int[] typeQty   = new int[4]; // 1..3：全票/學生票/愛心票
        int[] periodQty = new int[4]; // 1..3：單日票/季票/年票
        
        

        for (Object[] r : rows) {
        	int tType   = Convert.toInt(r[5]);
            int tPeriod = Convert.toInt(r[6]);
            int qty     = Convert.toInt(r[7]);
            int price   = Convert.toInt(r[8]);
            int amount  = qty * price;

            
            
            String productName = Ticket.productName(tType, tPeriod);

            qtyByProduct.put(productName, qtyByProduct.getOrDefault(productName, 0) + qty);
            amtByProduct.put(productName, amtByProduct.getOrDefault(productName, 0) + amount);

            if (tType >= 1 && tType <= 3)   typeQty[tType]   += qty;
            if (tPeriod >= 1 && tPeriod <= 3) periodQty[tPeriod] += qty;
        }

        
        // 先清空 2x2 容器（框）
        chartsContainer.removeAll();
        
        //前五熱賣產品（依銷量）
        DefaultCategoryDataset top5Dataset = new DefaultCategoryDataset();
        List<Entry<String, Integer>> list = new ArrayList<>(qtyByProduct.entrySet());
        list.sort(Comparator.comparingInt(Entry<String, Integer>::getValue).reversed());
        int topN = Math.min(5, list.size());
        for (int i = 0; i < topN; i++) {
            Entry<String, Integer> e = list.get(i);
            top5Dataset.addValue(e.getValue(), "銷量", e.getKey());
        }
        JFreeChart chartTop5 = ChartUI.createBar("前五熱賣產品", "產品", "銷量", top5Dataset);
        ChartPanel panelTop5 = new ChartPanel(chartTop5);
        chartsContainer.add(panelTop5); 


        // 各產品銷售額佔比
        DefaultPieDataset pie = new DefaultPieDataset();
        for (Map.Entry<String, Integer> e : amtByProduct.entrySet()) {
            if (e.getValue() != null && e.getValue() > 0) {
                pie.setValue(e.getKey(), e.getValue());
            }
        }
        JFreeChart chartPie = ChartUI.createPie("產品銷售額佔比", pie);
        ChartPanel panelPie = new ChartPanel(chartPie);
        chartsContainer.add(panelPie); 
  

        // 單看票種的銷量表現
        DefaultCategoryDataset typeDataset = new DefaultCategoryDataset();
        typeDataset.addValue(typeQty[1], "銷量", Ticket.typeName(1)); // 全票
        typeDataset.addValue(typeQty[2], "銷量", Ticket.typeName(2)); // 學生票
        typeDataset.addValue(typeQty[3], "銷量", Ticket.typeName(3)); // 愛心票
        JFreeChart chartType = ChartUI.createBar("票種銷售表現", "票種", "銷量", typeDataset);
        ChartPanel panelType = new ChartPanel(chartType);
        chartsContainer.add(panelType); 
        

        // 單看票期的銷量表現
        DefaultCategoryDataset periodDataset = new DefaultCategoryDataset();
        periodDataset.addValue(periodQty[1], "銷量", Ticket.periodName(1)); // 單日票
        periodDataset.addValue(periodQty[2], "銷量", Ticket.periodName(2)); // 季票
        periodDataset.addValue(periodQty[3], "銷量", Ticket.periodName(3)); // 年票
        JFreeChart chartPeriod = ChartUI.createBar("票期銷售表現", "票期", "銷量", periodDataset);
        ChartPanel panelPeriod = new ChartPanel(chartPeriod);
        chartsContainer.add(panelPeriod); 
        
        // 讓容器（框）重畫
        chartsContainer.revalidate();
        chartsContainer.repaint();
        
        JButton exportButton = new JButton("匯出圖表");
        exportButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		PrinterJob job = PrinterJob.getPrinterJob();
                job.setJobName("Mona Museum Charts");

                job.setPrintable(new Printable() {
                    @Override
                    public int print(java.awt.Graphics graphics, PageFormat pf, int pageIndex) throws PrinterException {
                        if (pageIndex > 0) return NO_SUCH_PAGE;

                        Graphics2D g2 = (Graphics2D) graphics;

                        // 關閉雙緩衝避免列印黑塊
                        RepaintManager rm = RepaintManager.currentManager(chartsContainer);
                        boolean wasDB = rm.isDoubleBufferingEnabled();
                        rm.setDoubleBufferingEnabled(false);
                        try {
                            // 移到可列印區
                            g2.translate(pf.getImageableX(), pf.getImageableY());

                            // 縮放以塞進單一頁面
                            double w = chartsContainer.getWidth();
                            double h = chartsContainer.getHeight();
                            double pw = pf.getImageableWidth();
                            double ph = pf.getImageableHeight();
                            double scale = Math.min(pw / w, ph / h);
                            g2.scale(scale, scale);

                            chartsContainer.printAll(g2); // 把 2x2 圖表容器畫到頁面
                        } finally {
                            rm.setDoubleBufferingEnabled(wasDB);
                        }
                        return PAGE_EXISTS;
                    }
                });

                if (job.printDialog()) { // 系統列印對話框
                    try {
                        job.print();
                    } catch (PrinterException ex) {
                        ex.printStackTrace();
                        Dialogs.error(ReportCharts.this, "列印失敗：" + ex.getMessage());
                    }
                }
            }
        });
        
        
        
        exportButton.setFont(new Font("Dialog", Font.BOLD, 16));
        exportButton.setBackground(new Color(120, 183, 237));
        exportButton.setBounds(772, 652, 120, 48);
        contentPane.add(exportButton);
        
        JButton FindAllPorderButton = new JButton("回訂單管理");
        FindAllPorderButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		FindAllPorder findAllPorder=new FindAllPorder();
        		findAllPorder.setVisible(true);
        		dispose();
        	}
        });
        FindAllPorderButton.setFont(new Font("Dialog", Font.BOLD, 16));
        FindAllPorderButton.setBackground(new Color(120, 183, 237));
        FindAllPorderButton.setBounds(640, 652, 120, 48);
        contentPane.add(FindAllPorderButton);
    }

    

    private String toProductName(int typeId, int periodId) {
        return Ticket.typeName(typeId) + "-" + Ticket.periodName(periodId);
    }
}
