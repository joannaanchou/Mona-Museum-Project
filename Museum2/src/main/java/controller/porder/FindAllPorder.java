package controller.porder;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import controller.admin.AdminCenter;
import controller.admin.ReportCharts;
import controller.member.Login;
import model.Member;
import model.PorderItem;
import model.Product;
import service.PorderItemService;
import service.PorderService;
import service.ProductService;
import service.impl.PorderItemServiceImpl;
import service.impl.PorderServiceImpl;
import service.impl.ProductServiceImpl;
import util.Tool;
import util.Convert;
import util.CreateExcel;
import util.Dialogs;
import util.Formatters;
import util.Ticket;
import util.Session;
import util.Tables;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import java.io.File;

public class FindAllPorder extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	//adminPanel欄位宣告
	private JComboBox<String> adminList;        
    private JTextField porderItemId;       
    private JLabel porderItemIdLabel;  

    private JLabel ticketTypeLabel;
    private JComboBox<String> ticketType;

    private JLabel ticketPeriodLabel;
    private JComboBox<String> ticketPeriod;

    private JLabel quantityLabel;
    private JComboBox<Integer> quantity;       // 數量（注意：使用泛型避免 raw type）
    
    // exportPanel 欄位宣告
    private JPanel exportPanel;
    private JLabel exportExcelLabel;
    private JComboBox<String> exportExcel;
    private JButton exportButton;
    
    
	private PorderService porderService = new PorderServiceImpl();
    private PorderItemService porderItemService = new PorderItemServiceImpl();
    private ProductService productService = new ProductServiceImpl();
    private JButton exportButton_1;
    

    
    // ===== id -> 中文名稱（只用來顯示在 JTable）=====
    /*private String toTypeName(int id) {
        switch (id) {
            case 1: return "全票";
            case 2: return "學生票";
            case 3: return "愛心票";
            default: return String.valueOf(id);
        }
    }
    private String toPeriodName(int id) {
        switch (id) {
            case 1: return "單日票";
            case 2: return "季票";
            case 3: return "年票";
            default: return String.valueOf(id);
        }
    }*/
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FindAllPorder frame = new FindAllPorder();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FindAllPorder() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 872);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(248, 254, 226));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(120, 183, 240));
		panel_1.setBounds(0, 38, 910, 58);
		contentPane.add(panel_1);
		
		JLabel lblMonaMuseum_1 = new JLabel("Mona Museum 訂單管理");
		lblMonaMuseum_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonaMuseum_1.setForeground(new Color(249, 255, 228));
		lblMonaMuseum_1.setFont(new Font("Dialog", Font.BOLD, 20));
		lblMonaMuseum_1.setBackground(new Color(120, 183, 240));
		lblMonaMuseum_1.setBounds(6, 6, 914, 46);
		panel_1.add(lblMonaMuseum_1);
		
		JLabel showMember = new JLabel("");
		showMember.setHorizontalAlignment(SwingConstants.CENTER);
		showMember.setForeground(new Color(0, 1, 1));
		showMember.setFont(new Font("Dialog", Font.BOLD, 14));
		showMember.setBackground(new Color(237, 243, 219));
		showMember.setBounds(631, 113, 142, 32);
		contentPane.add(showMember);
		
		
		Member m = Session.getCurrentMember();
	    String show = (m != null && m.getName() != null) ? m.getName() : "（未載入）";
	    showMember.setText("員工：" + show);
		
		
		
		JScrollPane allPorder = new JScrollPane();
		allPorder.setBounds(24, 174, 867, 264);
		contentPane.add(allPorder);

		table = new JTable();
		allPorder.setViewportView(table);

		// 呼叫載入所有訂單資料
		loadAllOrders();
		
		//管理員介面區
		JPanel adminPanel = new JPanel();
		adminPanel.setBounds(24, 490, 867, 203);
		adminPanel.setBackground(new Color(228, 181, 159));
		adminPanel.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
		contentPane.add(adminPanel);
		adminPanel.setLayout(null);
		
		
		
		JLabel lblNewLabel_2_3_1_2 = new JLabel("選擇管理項目：");
		lblNewLabel_2_3_1_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2_3_1_2.setForeground(new Color(0, 1, 1));
		lblNewLabel_2_3_1_2.setFont(new Font("Dialog", Font.BOLD, 16));
		lblNewLabel_2_3_1_2.setBackground(new Color(119, 183, 240));
		lblNewLabel_2_3_1_2.setBounds(271, 26, 112, 37);
		adminPanel.add(lblNewLabel_2_3_1_2);
		
		//管理選項下拉選單
		String[] list = {"刪除訂單", "修改訂單" };
		
		adminList = new JComboBox<>(list);
		adminList.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		adminList.setSelectedIndex(0);
		adminList.setBounds(395, 23, 134, 45);
		adminPanel.add(adminList);
		
		//輸入訂單編號(porder_item_id)
		porderItemIdLabel = new JLabel("請輸入訂單編號：");
		porderItemIdLabel.setHorizontalAlignment(SwingConstants.LEFT);
		porderItemIdLabel.setForeground(new Color(0, 1, 1));
		porderItemIdLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		porderItemIdLabel.setBackground(new Color(119, 183, 240));
		porderItemIdLabel.setBounds(21, 26, 161, 37);
		adminPanel.add(porderItemIdLabel);
		
		porderItemId = new JTextField();
		porderItemId.setBounds(151, 26, 86, 37);
		adminPanel.add(porderItemId);
		porderItemId.setColumns(10);
		
		
		// 票種//
		ticketTypeLabel = new JLabel("票種：");
		ticketTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		ticketTypeLabel.setForeground(new Color(0, 1, 1));
		ticketTypeLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		ticketTypeLabel.setBackground(new Color(119, 183, 240));
		ticketTypeLabel.setBounds(271, 77, 112, 37);
		adminPanel.add(ticketTypeLabel);
		
		String[] type = {"全票", "學生票", "愛心票" };
		ticketType = new JComboBox<>(type);
		ticketType.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		ticketType.setSelectedIndex(0);
		ticketType.setBounds(314, 75, 134, 45);
		adminPanel.add(ticketType);
		
		
		// 票期//
		ticketPeriodLabel = new JLabel("票期：");
		ticketPeriodLabel.setHorizontalAlignment(SwingConstants.LEFT);
		ticketPeriodLabel.setForeground(new Color(0, 1, 1));
		ticketPeriodLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		ticketPeriodLabel.setBackground(new Color(119, 183, 240));
		ticketPeriodLabel.setBounds(473, 77, 112, 37);
		adminPanel.add(ticketPeriodLabel);
		
		String[] period = {"單日票", "季票", "年票" };
		ticketPeriod = new JComboBox<>(period);
		ticketPeriod.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		ticketPeriod.setSelectedIndex(0);
		ticketPeriod.setBounds(519, 75, 134, 45);
		adminPanel.add(ticketPeriod);
		
		//票券數量
		quantityLabel = new JLabel("數量：");
		quantityLabel.setHorizontalAlignment(SwingConstants.LEFT);
		quantityLabel.setForeground(new Color(0, 1, 1));
		quantityLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		quantityLabel.setBackground(new Color(119, 183, 240));
		quantityLabel.setBounds(676, 77, 112, 37);
		adminPanel.add(quantityLabel);
		
		quantity = new JComboBox<>();
		quantity.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		quantity.setBounds(723, 75, 134, 45);
		adminPanel.add(quantity);
		
		for (int i = 0; i <= 10; i++) {
			quantity.addItem(i);
		}
		
		
		
		
		//Button//
        
        JButton ConfirmButton_1 = new JButton("確認");
		ConfirmButton_1.setFont(new Font("Dialog", Font.BOLD, 14));
		ConfirmButton_1.setBackground(new Color(120, 183, 237));
		ConfirmButton_1.setBounds(751, 134, 106, 51);
		adminPanel.add(ConfirmButton_1);
		
        // 事件：切換模式（刪除 / 修改）
        this.adminList.addActionListener(e -> applyAdminModeUI());
        
		// 事件：確認
        ConfirmButton_1.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                onAdminConfirm();
            }
        });
		
		
		// 初始：依模式顯示/隱藏欄位 
        applyAdminModeUI();
        
        // 最後再載入表格資料
        loadAllOrders();
        
        JButton AdminCenterButton = new JButton("管理員後台");
		AdminCenterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				AdminCenter adminCenter = new AdminCenter();
				adminCenter.setVisible(true);
		        dispose();
			}
		});
		AdminCenterButton.setFont(new Font("Dialog", Font.BOLD, 14));
		AdminCenterButton.setBackground(new Color(120, 183, 237));
		AdminCenterButton.setBounds(785, 112, 106, 37);
		contentPane.add(AdminCenterButton);
		
		
		//匯出區
		exportPanel = new JPanel();
		exportPanel.setBackground(new Color(227, 181, 159));
		exportPanel.setBounds(24, 705, 867, 107);
		contentPane.add(exportPanel);
		exportPanel.setLayout(null);
		
		exportExcelLabel = new JLabel("報表內容：");
		exportExcelLabel.setHorizontalAlignment(SwingConstants.LEFT);
		exportExcelLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		exportExcelLabel.setBounds(17, 36, 123, 45);
		exportPanel.add(exportExcelLabel);
		
		String[] excel = {"所有訂單", "總產品銷售","票種銷售","票期銷售"};
		exportExcel = new JComboBox(excel);
		exportExcel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		exportExcel.setBounds(102, 35, 136, 46);
		exportPanel.add(exportExcel);
		
		exportButton = new JButton("匯出Excel");
		exportButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onExportClicked();
			}
		});
		exportButton.setFont(new Font("Dialog", Font.BOLD, 16));
		exportButton.setBackground(new Color(120, 183, 237));
		exportButton.setBounds(250, 33, 106, 48);
		exportPanel.add(exportButton);	
		
		exportButton_1 = new JButton("查看銷售報表");
		exportButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ReportCharts reportCharts=new ReportCharts();
				reportCharts.setVisible(true);
				dispose();
			}
		});
		exportButton_1.setFont(new Font("Dialog", Font.BOLD, 16));
		exportButton_1.setBackground(new Color(120, 183, 237));
		exportButton_1.setBounds(368, 33, 120, 48);
		exportPanel.add(exportButton_1);
		
}


		// 載入所有訂單資料到 jtable
		private void loadAllOrders() {
			List<Object[]> orderList = porderService.getAllOrderDetails();
			String[] columnNames = {
					"訂單編號", "下單時間", "訂單序號", "會員編號", "票券啟用日",
			        "票種", "票期", "數量", "金額"};
		
			DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
				 @Override public boolean isCellEditable(int r, int c) { return false; } 
			};
			
			for (Object[] row : orderList) {
				
		        // 0:porder_item_id, 1:created_at, 2:order_no, 3:member_id, 4:order_date,
		        // 5:ticket_type_id, 6:ticket_period_id, 7:quantity, 8:unit_price
		        int typeId   = Convert.toInt(row[5]);
		        int periodId = Convert.toInt(row[6]);
		        int qty      = Convert.toInt(row[7]);
		        int unit     = Convert.toInt(row[8]);
		        int lineAmt  = unit * qty;
		        
		        Object[] display = new Object[9];
		        display[0] = row[0];                         // 訂單編號（porder_item.id）
		        display[1] = row[1];                         // 下單時間
		        display[2] = row[2];                         // 訂單序號（order_no）
		        display[3] = row[3];                         // 會員編號
		        display[4] = row[4];                         // 票券啟用日
		        display[5] = Ticket.typeName(typeId);        // 票種 (中文)
		        display[6] = Ticket.periodName(periodId);    // 票期 (中文)
		        display[7] = qty;                            // 數量
		        display[8] = Formatters.currency(lineAmt);   // 金額（單價×數量）

		        model.addRow(display);
		    }
			
			table.setModel(model);
			
			// ---- 表格樣式統一 util.Tables ----
		    Tables.styleHeader(table);
		    // 依欄位：0 編號, 1 下單時間, 2 訂單序號, 3 會員編號, 4 票券啟用日, 5 票種, 6 票期, 7 數量, 8 金額
		    Tables.centerAlign(table, 0, 2, 3, 5, 6);
		    Tables.rightAlign(table, 7, 8);
		    Tables.zebra(table);
		    table.setAutoCreateRowSorter(true); // 可排序（方便管理用）
		}
		
		

		// 依下拉選單模式，切換 UI 顯示
	    private void applyAdminModeUI() {
	        boolean isDelete = adminList.getSelectedIndex() == 0; // 0=刪除, 1=修改

	        // 刪除：只需要輸入訂單編號
	        porderItemId.setVisible(true);

	        ticketTypeLabel.setVisible(!isDelete);
	        ticketType.setVisible(!isDelete);

	        ticketPeriodLabel.setVisible(!isDelete);
	        ticketPeriod.setVisible(!isDelete);

	        quantityLabel.setVisible(!isDelete);
	        quantity.setVisible(!isDelete);
	    }

		//點「匯出確認」按鈕後的邏輯
	    private void onAdminConfirm() {
	        String idText = porderItemId.getText();
	        if (idText == null || idText.trim().isEmpty()) {
	        	Dialogs.info(this, "請輸入訂單編號");
	            return;
	        }

	        int itemId;
	        try {
	            itemId = Integer.parseInt(idText.trim());
	        } catch (NumberFormatException ex) {
	        	Dialogs.info(this, "訂單編號必須是數字！");
	            return;
	        }

	        boolean isDelete = adminList.getSelectedIndex() == 0;

	        if (isDelete) {
	            // === 刪除 ===
	            int ok = JOptionPane.showConfirmDialog(this,
	                    "確定要刪除此筆訂單明細？(ID=" + itemId + ")",
	                    "確認刪除", JOptionPane.YES_NO_OPTION);
	            if (ok != JOptionPane.YES_OPTION) return;

	            try {
	                // 若要連動回寫 porder.total_amount，記得在刪除前先找 porderId
	                // Integer porderId = porderItemService.findPorderIdByItemId(itemId);

	                porderItemService.deleteById(itemId);

	                // if (porderId != null) {
	                //     int newTotal = porderItemService.sumLineTotalByPorderId(porderId);
	                //     porderService.updateTotal(porderId, newTotal); // 若有這個 service 方法
	                // }

	                Dialogs.info(this, "刪除完成");
	                loadAllOrders();
	                porderItemId.setText("");

	            } catch (Exception ex) {
	                ex.printStackTrace();
	                Dialogs.error(this, "刪除失敗：" + ex.getMessage());
	            }
	        } else {
	            // === 修改 ===
	            String typeName = (String) ticketType.getSelectedItem();
	            String periodName = (String) ticketPeriod.getSelectedItem();
	            Integer qty = (Integer) quantity.getSelectedItem();

	            if (qty == null || qty <= 0) {
	            	Dialogs.error(this, "數量需大於 0");
	                return;
	            }

	            // 雖然 JTable 顯示中文，但要更新資料庫仍需把中文轉成對應 id
	            int ticketTypeId = Ticket.getTicketTypeIdFromName(typeName);
	            int ticketPeriodId = Ticket.getTicketPeriodIdFromName(periodName);

	            try {
	                // 找對應商品（拿 productId / 單價）
	                Product prod = productService.getProductByTypeAndPeriod(ticketTypeId, ticketPeriodId);
	                if (prod == null || prod.getId() == null) {
	                	Dialogs.error(this, "找不到對應商品（" + typeName + "／" + periodName + "）");
	                    return;
	                }

	                int unitPrice =prod.getPrice();

	                // 更新這筆明細
	                PorderItem item = new PorderItem();
	                item.setId(itemId);                    // 重要：更新哪一筆
	                item.setProductId(prod.getId());       // 對應商品
	                item.setQuantity(qty);
	                item.setUnitPrice(unitPrice);
	                item.setLineTotal(unitPrice * qty);    // 若你的 update 會自算也可省略

	                // 若要連動回寫 porder.total_amount，更新前先記住它的 porderId
	                // Integer porderId = porderItemService.findPorderIdByItemId(itemId);

	                porderItemService.update(item);

	                // if (porderId != null) {
	                //     int newTotal = porderItemService.sumLineTotalByPorderId(porderId);
	                //     porderService.updateTotal(porderId, newTotal); // 若有這個 service 方法
	                // }

	                Dialogs.info(this, "更新完成");
	                loadAllOrders();

	            } catch (Exception ex) {
	                ex.printStackTrace();
	                Dialogs.error(this, "更新失敗：" + ex.getMessage());
	            }
	        }
	    }
	    
	    // 按下匯出鈕
	    private void onExportClicked() {
	        int which = exportExcel.getSelectedIndex();

	        JFileChooser fc = new JFileChooser();
	        switch (which) {
	            case 0: fc.setSelectedFile(new File("AllOrders.xlsx")); break;
	            case 1: fc.setSelectedFile(new File("TypePeriodDetail.xlsx")); break; // 「總產品銷受」
	            case 2: fc.setSelectedFile(new File("TicketTypeSales.xlsx")); break;
	            case 3: fc.setSelectedFile(new File("TicketPeriodSales.xlsx")); break;
	        }
	        
	        
	        fc.setDialogTitle("請選擇匯出 Excel 檔案的位置");
	        int ans = fc.showSaveDialog(this);
	        if (ans != JFileChooser.APPROVE_OPTION) return;

	        File file = fc.getSelectedFile();
	        String path = file.getAbsolutePath();
	        if (!path.toLowerCase().endsWith(".xlsx")) {
	            path += ".xlsx";
	        }
	        

	        try {
	            CreateExcel excel = new CreateExcel();

	            if (which == 0) {
	                // 直接把 JTable 的內容匯出
	                DefaultTableModel m = (DefaultTableModel) table.getModel();
	                int cols = m.getColumnCount();
	                int rows = m.getRowCount();

	                String[] headers = new String[cols];
	                for (int c = 0; c < cols; c++) headers[c] = m.getColumnName(c);

	                List<Object[]> data = new ArrayList<>();
	                for (int r = 0; r < rows; r++) {
	                    Object[] row = new Object[cols];
	                    for (int c = 0; c < cols; c++) {
	                        row[c] = m.getValueAt(r, c);
	                    }
	                    data.add(row);
	                }

	                // 需在 CreateExcel 實作 exportTable(path, sheetName, headers, data)
	                excel.exportTable(path, "全部訂單", headers, data);
	                Dialogs.info(this, "匯出成功：\n" + path);
	                return;
	            }

	            // 其餘報表：先拿全部明細
	            List<Object[]> rows = porderService.getAllOrderDetails();
	            if (rows == null || rows.isEmpty()) {
	            	Dialogs.error(this, "目前沒有資料可匯出。");
	                return;
	            }

	            if (which == 2) {
	                // 票種銷售（兩欄：票種、銷量）
	                Object[][] data = buildTypeSalesData(rows);
	                excel.exportTicketTypeSalesReport(data, path, "票種銷售報表");
	                Dialogs.info(this, "匯出成功：\n" + path);
	            } else if (which == 3) {
	                // 票期銷售（兩欄：票期、銷量）
	                Object[][] data = buildPeriodSalesData(rows);
	                excel.exportTicketPeriodSalesReport(data, path, "票期銷售報表");
	                Dialogs.info(this, "匯出成功：\n" + path);
	            } else if (which == 1) {
	                // 總產品銷受（＝票種×票期 9 列明細，含銷量與銷售總額、最底列總計）
	                Object[][] data = buildTypePeriodDetailData(rows);
	                excel.exportTicketTypePeriodSalesReport(data, path, "票種×票期明細");
	                Dialogs.info(this, "匯出成功：\n" + path);
	            }

	        } catch (Exception ex) {
	            ex.printStackTrace();
	            Dialogs.error(this, "匯出失敗：" + ex.getMessage());
	        }
	    }
	    
	    
	    
	 // 彙總工具：票種銷售（只回傳銷量）
	    // getAllOrderDetails() 欄位順序：
	    // 0:porder_item_id, 1:created_at, 2:order_no, 3:member_id, 4:order_date,
	    // 5:ticket_type_id, 6:ticket_period_id, 7:quantity, 8:unit_price
	    private Object[][] buildTypeSalesData(List<Object[]> rows) {
	        int[] countByType = new int[4]; // 1..3
	        for (Object[] r : rows) {
	            int typeId = Convert.toInt(r[5]);
	            int qty    = Convert.toInt(r[7]);
	            if (typeId >= 1 && typeId <= 3) countByType[typeId] += qty;
	        }
	        return new Object[][] {
	            {"全票",   countByType[1]},
	            {"學生票", countByType[2]},
	            {"愛心票", countByType[3]}
	        };
	    }

	    //彙總工具：票期銷售（只回傳銷量）
	    private Object[][] buildPeriodSalesData(List<Object[]> rows) {
	        int[] countByPeriod = new int[4]; // 1..3
	        for (Object[] r : rows) {
	            int periodId = Convert.toInt(r[6]);
	            int qty      = Convert.toInt(r[7]);
	            if (periodId >= 1 && periodId <= 3) countByPeriod[periodId] += qty;
	        }
	        return new Object[][] {
	            {"單日票", countByPeriod[1]},
	            {"季票",   countByPeriod[2]},
	            {"年票",   countByPeriod[3]}
	        };
	    }

	    // === 彙總工具：票種×票期 9 列明細（含銷量與總額）===
	    private Object[][] buildTypePeriodDetailData(List<Object[]> rows) {
	        int[][] qtySum = new int[4][4];   // [type][period]
	        int[][] amtSum = new int[4][4];

	        for (Object[] r : rows) {
	            int typeId   = Convert.toInt(r[5]); // 1..3
	            int periodId = Convert.toInt(r[6]); // 1..3
	            int qty      = Convert.toInt(r[7]);
	            int price    = Convert.toInt(r[8]);
	            if (typeId < 1 || typeId > 3 || periodId < 1 || periodId > 3) continue;
	            qtySum[typeId][periodId] += qty;
	            amtSum[typeId][periodId] += qty * price;
	        }

	        Object[][] data = new Object[9][4]; // 票種、票期、銷量、銷售總額
	        int idx = 0;
	        for (int t = 1; t <= 3; t++) {
	            for (int p = 1; p <= 3; p++) {
	                data[idx][0] = Ticket.typeName(t);
	                data[idx][1] = Ticket.periodName(p);
	                data[idx][2] = qtySum[t][p];
	                data[idx][3] = amtSum[t][p];
	                idx++;
	            }
	        }
	        return data;
	    }
	    
}
