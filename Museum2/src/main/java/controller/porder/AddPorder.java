package controller.porder;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import model.Member;
import model.Porder;
import service.PorderService;
import service.ProductService;
import service.impl.PorderServiceImpl;
import service.impl.ProductServiceImpl;
import util.DateLabelFormatter;
import util.Ticket;
import util.Session;
import util.Tables;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import model.PorderItem;
import model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.DbConnection;
import util.Dialogs;
import util.Formatters;

import java.util.ArrayList;

import javax.swing.JScrollPane;

public class AddPorder extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Member currentUser;
	
	
	private PorderService porderService = new PorderServiceImpl();
	private ProductService productService = new ProductServiceImpl();//當下拉選單被選擇後，即時顯示對產品價格
	
	
	
	// 日期 / 顯示
	private LocalDateTime orderDateValue = null;
	private static final DateTimeFormatter DATE_UI_FMT =DateTimeFormatter.ofPattern("yyyy-MM-dd");

	// 票種固定對應
	private static int TYPE_ADULT = 1;       // 全票
	private static int TYPE_STUDENT = 2;     // 學生票
	private static int TYPE_CONCESSION = 3;  // 愛心票

	
	private JTable CartTable;
	private DefaultTableModel cartModel;
	private static String TOTAL_KEY = "__TOTAL_ROW__"; // 辨識加總列
	
	
		// 儲存格：加深表格線、數值欄靠右、加總列粗體＆底色
		private class CartCellRenderer extends DefaultTableCellRenderer {
	    private  Color grid = new Color(120,120,120);
	    private Color totalBg = new Color(255, 248, 210); // 淡黃
	    private javax.swing.border.Border cellBorder =BorderFactory.createMatteBorder(0, 0, 1, 1, grid);

	    	@Override
	    	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	    	{
	    		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    		setBorder(cellBorder);


	        // 判斷最後一列是否加總（用隱藏的 key）
	        int modelRow = table.convertRowIndexToModel(row);
	        Object k = cartModel.getValueAt(modelRow, 5); // 第6欄是隱藏key
	        boolean isTotal = TOTAL_KEY.equals(k);

	        if (isTotal) 
	        {
	            setFont(getFont().deriveFont(Font.BOLD));
	            if (!isSelected) setBackground(totalBg);
	        } else {
	            // 還原一般底色（避免選取後背景還有底色）
	            if (!isSelected) setBackground(Color.WHITE);
	        }
	        return c;
	    }
	}

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddPorder frame = new AddPorder();
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
	// 兩個建構相容 + 可擴充
    public AddPorder() {
        this.currentUser = (Member) Session.getCurrentMember(); // 
        repairCurrentUserIdIfMissing();//避免呼叫端傳進來的物件沒有 id
        initUI();
    }

    public AddPorder(Member currentUser) {
        this.currentUser = currentUser;
        repairCurrentUserIdIfMissing(); //避免呼叫端傳進來的物件沒有 id
        initUI();
    }

    
    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 910, 631);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(238, 244, 218));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 顯示會員名稱＆ID
        JLabel showMember = new JLabel("");
        showMember.setHorizontalAlignment(SwingConstants.CENTER);
        showMember.setForeground(new Color(0, 1, 1));
        showMember.setFont(new Font("Dialog", Font.BOLD, 12));
        showMember.setBackground(new Color(237, 243, 219));
        showMember.setBounds(551, 89, 156, 32);
        contentPane.add(showMember);
        
        
        String show;
        if (currentUser != null && currentUser.getName() != null) {
            String idStr = (currentUser.getId() != null && currentUser.getId() > 0)
                    ? "（ID: " + currentUser.getId() + "）" : "（ID 未載入）";
            show = "會員：" + currentUser.getName() + " " + idStr;
        } else {
            show = "會員：未登入";
        }
        showMember.setText(show);
        
		
		//時鐘
		JLabel showTime = new JLabel("");
		showTime.setBounds(708, 89, 185, 32);
		contentPane.add(showTime);
		showTime.setHorizontalAlignment(SwingConstants.CENTER);
		showTime.setForeground(new Color(0, 1, 1));
		showTime.setFont(new Font("Dialog", Font.BOLD, 12));
		showTime.setBackground(new Color(237, 243, 219));
		
		Timer clockTimer = new Timer(1000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		        String currentTime = sdf.format(new Date());
		        showTime.setText(currentTime);
		    }
		});
		clockTimer.start();
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(120, 183, 240));
		panel_1.setBounds(0, 30, 910, 58);
		contentPane.add(panel_1);
		
		JLabel lblMonaMuseum_1 = new JLabel("Mona Museum 票券購買");
		lblMonaMuseum_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonaMuseum_1.setForeground(new Color(249, 255, 228));
		lblMonaMuseum_1.setFont(new Font("Dialog", Font.BOLD, 20));
		lblMonaMuseum_1.setBackground(new Color(120, 183, 240));
		lblMonaMuseum_1.setBounds(6, 6, 914, 46);
		panel_1.add(lblMonaMuseum_1);
		
		//票券啟用日選擇區域//
		JPanel panel = new JPanel();
		panel.setBackground(new Color(242, 193, 172));
		panel.setBounds(22, 133, 870, 212);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_2_3 = new JLabel("票券啟用日:");
		lblNewLabel_2_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_3.setForeground(new Color(0, 1, 1));
		lblNewLabel_2_3.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_2_3.setBackground(new Color(119, 183, 240));
		lblNewLabel_2_3.setBounds(0, 0, 112, 37);
		panel.add(lblNewLabel_2_3);
		
		// 日期選擇器元件（要先初始化datePanel，再建立 datePicker）
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "今天");
		p.put("text.month", "月份");
		p.put("text.year", "年份");
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.setBounds(104, 6, 250, 29);
		panel.add(datePicker);
		
		JLabel lblNewLabel_2_3_1 = new JLabel("票種");
		lblNewLabel_2_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_3_1.setForeground(new Color(0, 1, 1));
		lblNewLabel_2_3_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_2_3_1.setBackground(new Color(119, 183, 240));
		lblNewLabel_2_3_1.setBounds(0, 47, 112, 37);
		panel.add(lblNewLabel_2_3_1);
		
		JLabel lblNewLabel_2_3_1_1 = new JLabel("票期");
		lblNewLabel_2_3_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_3_1_1.setForeground(new Color(0, 1, 1));
		lblNewLabel_2_3_1_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_2_3_1_1.setBackground(new Color(119, 183, 240));
		lblNewLabel_2_3_1_1.setBounds(134, 47, 112, 37);
		panel.add(lblNewLabel_2_3_1_1);
		
		JLabel lblNewLabel_2_3_1_2 = new JLabel("票價");
		lblNewLabel_2_3_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_3_1_2.setForeground(new Color(0, 1, 1));
		lblNewLabel_2_3_1_2.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_2_3_1_2.setBackground(new Color(119, 183, 240));
		lblNewLabel_2_3_1_2.setBounds(317, 47, 112, 37);
		panel.add(lblNewLabel_2_3_1_2);
		
		JLabel lblNewLabel_2_3_1_3 = new JLabel("數量");
		lblNewLabel_2_3_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_3_1_3.setForeground(new Color(0, 1, 1));
		lblNewLabel_2_3_1_3.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_2_3_1_3.setBackground(new Color(119, 183, 240));
		lblNewLabel_2_3_1_3.setBounds(489, 47, 112, 37);
		panel.add(lblNewLabel_2_3_1_3);
		
		JLabel lblNewLabel_2_3_1_4 = new JLabel("全票");
		lblNewLabel_2_3_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_3_1_4.setForeground(new Color(98, 99, 93));
		lblNewLabel_2_3_1_4.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_2_3_1_4.setBackground(new Color(119, 183, 240));
		lblNewLabel_2_3_1_4.setBounds(0, 84, 112, 37);
		panel.add(lblNewLabel_2_3_1_4);
		
		JLabel lblNewLabel_2_3_1_4_1 = new JLabel("學生票");
		lblNewLabel_2_3_1_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_3_1_4_1.setForeground(new Color(98, 99, 93));
		lblNewLabel_2_3_1_4_1.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_2_3_1_4_1.setBackground(new Color(119, 183, 240));
		lblNewLabel_2_3_1_4_1.setBounds(0, 125, 112, 37);
		panel.add(lblNewLabel_2_3_1_4_1);
		
		JLabel lblNewLabel_2_3_1_4_1_1 = new JLabel("愛心票");
		lblNewLabel_2_3_1_4_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_3_1_4_1_1.setForeground(new Color(98, 99, 93));
		lblNewLabel_2_3_1_4_1_1.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_2_3_1_4_1_1.setBackground(new Color(119, 183, 240));
		lblNewLabel_2_3_1_4_1_1.setBounds(0, 169, 112, 37);
		panel.add(lblNewLabel_2_3_1_4_1_1);
		
		JLabel price1 = new JLabel("");
		price1.setHorizontalAlignment(SwingConstants.CENTER);
		price1.setForeground(new Color(98, 99, 93));
		price1.setFont(new Font("Dialog", Font.BOLD, 12));
		price1.setBackground(new Color(119, 183, 240));
		price1.setBounds(317, 84, 112, 37);
		panel.add(price1);
		
		JLabel price2 = new JLabel("");
		price2.setHorizontalAlignment(SwingConstants.CENTER);
		price2.setForeground(new Color(98, 99, 93));
		price2.setFont(new Font("Dialog", Font.BOLD, 12));
		price2.setBackground(new Color(119, 183, 240));
		price2.setBounds(317, 125, 112, 37);
		panel.add(price2);
		
		JLabel price3 = new JLabel("");
		price3.setHorizontalAlignment(SwingConstants.CENTER);
		price3.setForeground(new Color(98, 99, 93));
		price3.setFont(new Font("Dialog", Font.BOLD, 12));
		price3.setBackground(new Color(119, 183, 240));
		price3.setBounds(317, 169, 112, 37);
		panel.add(price3);
		
		
		// 票期下拉選單區//
		String[] period = {"單日票", "季票", "年票" };
		
		JComboBox TT01 = new JComboBox(period);
		TT01.setBounds(134, 84, 134, 45);
		TT01.setSelectedIndex(0);
		panel.add(TT01);
		
		JComboBox TT02 = new JComboBox(period);
		TT02.setBounds(134, 125, 134, 45);
		TT02.setSelectedIndex(0);
		panel.add(TT02);
		
		JComboBox TT03 = new JComboBox(period);
		TT03.setBounds(134, 169, 134, 45);
		TT03.setSelectedIndex(0);
		panel.add(TT03);
		
		// 根據下拉選單即時顯示對應價格 
		bindPrice(TT01, TYPE_ADULT,      price1);
		bindPrice(TT02, TYPE_STUDENT,    price2);
		bindPrice(TT03, TYPE_CONCESSION, price3);
		
		// 價格預設顯示單日票價格
		updatePrice(TT01, TYPE_ADULT,      price1);
		updatePrice(TT02, TYPE_STUDENT,    price2);
		updatePrice(TT03, TYPE_CONCESSION, price3);
		
		JComboBox quantity1 = new JComboBox();
		quantity1.setBounds(499, 84, 85, 45);
		panel.add(quantity1);
		
		JComboBox quantity2 = new JComboBox();
		quantity2.setBounds(499, 125, 85, 45);
		panel.add(quantity2);
		
		JComboBox quantity3 = new JComboBox();
		quantity3.setBounds(499, 169, 85, 45);
		panel.add(quantity3);
		
		for (int i = 0; i <= 10; i++) {
			quantity1.addItem(i);
			quantity2.addItem(i);
			quantity3.addItem(i);
		}
		
		
		//選購內容區域//
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(22, 357, 870, 186);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel_2_3_2 = new JLabel("你的選購內容：");
		lblNewLabel_2_3_2.setBounds(0, -11, 175, 37);
		panel_2.add(lblNewLabel_2_3_2);
		lblNewLabel_2_3_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2_3_2.setForeground(new Color(0, 1, 1));
		lblNewLabel_2_3_2.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_2_3_2.setBackground(new Color(119, 183, 240));
		
		JLabel ticketDate = new JLabel("");
		ticketDate.setHorizontalAlignment(SwingConstants.LEFT);
		ticketDate.setForeground(new Color(0, 1, 1));
		ticketDate.setFont(new Font("Dialog", Font.BOLD, 14));
		ticketDate.setBackground(new Color(119, 183, 240));
		ticketDate.setBounds(178, -11, 175, 37);
		panel_2.add(ticketDate);

		// 啟用日（格式 yyyy-MM-dd）
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		// 先把目前 model 的值顯示一次
		Date d0 = (Date) model.getValue();
		if (d0 != null) {
		    LocalDate ld = d0.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		    orderDateValue = ld.atStartOfDay(); // 存成 LocalDateTime（00:00）
		    ticketDate.setText("啟用日：" + DATE_UI_FMT.format(ld));
		} else {
		    orderDateValue = null;
		    ticketDate.setText("票券啟用日：—");
		}
		
		// 當日期變更就即時更新 + 禁止今天/過去
		model.addChangeListener(new ChangeListener() {
		    @Override
		    public void stateChanged(ChangeEvent e) {
		        Date d = (Date) model.getValue(); // UtilDateModel 回傳 java.util.Date
		        if (d == null) {
		            orderDateValue = null;
		            ticketDate.setText("票券啟用日：—");
		            return;
		        }

		        LocalDate picked = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		        LocalDate today  = LocalDate.now();

		        if (!picked.isAfter(today)) { // 不能選今天或更早
		        	Dialogs.info(AddPorder.this, "請選擇未來日期！");
		            model.setValue(null);
		            orderDateValue = null;
		            ticketDate.setText("票券啟用日：—");
		            return;
		        }

		        orderDateValue = picked.atStartOfDay();
		        ticketDate.setText("票券啟用日：" + DATE_UI_FMT.format(picked));
		    }
		});
		
		// 建立表格的資料
		cartModel = new DefaultTableModel(new Object[]{"票種", "票期", "單價", "數量", "金額", "_key"}, 0) 
		{
			    @Override public boolean isCellEditable(int row, int col) { return false; }
		};
		
		CartTable = new JTable(cartModel);
		CartTable.setFillsViewportHeight(true);
		CartTable.setRowHeight(28);
		
		// 顯示格線 + 顏色
		CartTable.setShowGrid(true);
		CartTable.setGridColor(new Color(120,120,120));
		// 使用自訂儲存格 renderer 讓格線更清楚（粗一點的邊）
		CartTable.setDefaultRenderer(Object.class, new CartCellRenderer());
		
		//ScrollPane包起來
		JScrollPane cartScroll = new JScrollPane(CartTable);
		cartScroll.setBounds(0, 38, 870, 148);
		panel_2.add(cartScroll);
		
		
		
		// 表頭底色&粗體統一 util.Tables）
		Tables.styleHeader(CartTable);
		Tables.centerAlign(CartTable, 0, 1);   // 0:票種、1:票期 置中
		Tables.rightAlign(CartTable, 2, 3, 4); // 2:單價、3:數量、4:金額 靠右

		// 隱藏key欄(第6欄)
		CartTable.getColumnModel().removeColumn(CartTable.getColumnModel().getColumn(5));
		
		
		
		//Button//
		JButton AddCartButton = new JButton("加入購物車");
		AddCartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// 先移除舊總金額列（避免中途合併時把它當成一般列計算）
		        removeTotalRowIfAny();

		        
				// 全票
		        processRow(TYPE_ADULT, TT01, quantity1);
		        // 學生票
		        processRow(TYPE_STUDENT, TT02, quantity2);
		        // 愛心票
		        processRow(TYPE_CONCESSION, TT03, quantity3);

		        //統一重算並追加「總金額」列（永遠放最底）
		        refreshTotalRow();
		        
		        // 重置數量為 0（可選）
		        quantity1.setSelectedItem(0);
		        quantity2.setSelectedItem(0);
		        quantity3.setSelectedItem(0);
				
			}
		});
		AddCartButton.setFont(new Font("Dialog", Font.BOLD, 14));
		AddCartButton.setBackground(new Color(228, 181, 159));
		AddCartButton.setBounds(730, 169, 134, 37);
		panel.add(AddCartButton);
		
		JButton ConfirmButton = new JButton("確認訂單");
		ConfirmButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			    
				repairCurrentUserIdIfMissing();
				
				if (orderDateValue == null) {
			        Dialogs.info(AddPorder.this, "請先選擇票券啟用日（需為未來日期）！");
			        return;
			    }

			    // 檢查購物車是否為空
			    if (cartModel.getRowCount() == 0) {
			    	Dialogs.info(AddPorder.this, "購物車沒有任何票券！");
			        return;
			    }

			    if (currentUser == null || currentUser.getId() == null || currentUser.getId() <= 0) {
			    	Dialogs.error(AddPorder.this, "會員資訊有誤，請重新登入。");
			        return;
			    }
			
			    
			    // 建立 Porder 物件
                Porder p = new Porder();
                p.setMemberId(currentUser.getId());
                p.setOrderDate(orderDateValue);
                p.setCreatedAt(LocalDateTime.now());

			    // 建立多筆 PorderItem
			    List<PorderItem> items = new ArrayList<>();
			    for (int i = 0; i < cartModel.getRowCount(); i++) {
			        Object k = cartModel.getValueAt(i, 5);
			        if (TOTAL_KEY.equals(k)) continue; // 跳過總金額列

			        String typeName = (String) cartModel.getValueAt(i, 0);
			        String periodName = (String) cartModel.getValueAt(i, 1);
			        int unitPrice = (int) cartModel.getValueAt(i, 2);
			        int qty = (int) cartModel.getValueAt(i, 3);
			        int lineTotal = (int) cartModel.getValueAt(i, 4);

			        int ticketTypeId = Ticket.getTicketTypeIdFromName(typeName);
			        int ticketPeriodId = Ticket.getTicketPeriodIdFromName(periodName);
			        

			        Product product = productService.getProductByTypeAndPeriod(ticketTypeId, ticketPeriodId);
			        Integer productId = (product != null) ? product.getId() : null;
			        
			        if (productId == null) {
			        	Dialogs.error(AddPorder.this, "找不到對應商品：" + typeName + "/" + periodName);
			            return;
			        }

			        PorderItem item = new PorderItem();
			        item.setProductId(productId);
			        item.setUnitPrice(unitPrice);
			        item.setQuantity(qty);
			        item.setLineTotal(lineTotal);
			        items.add(item);
			    }

			    // 寫入 DB
			    try {
			        long porderId = porderService.placeOrder(p, items);
			        p.setId((int) porderId); // 寫入 ID 讓後面顯示用
			        Session.setCurrentPorder(p);

			        Finish finish = new Finish();
			        finish.setVisible(true);
			        dispose();
			    } catch (Exception ex) {
			        ex.printStackTrace();
			        Dialogs.error(AddPorder.this, "訂單建立失敗：" + ex.getMessage());
			    }
			}
		});

		ConfirmButton.setFont(new Font("Dialog", Font.BOLD, 14));
		ConfirmButton.setBackground(new Color(120, 183, 237));
		ConfirmButton.setBounds(758, 549, 134, 37);
		contentPane.add(ConfirmButton);
		
		JButton ClearButton = new JButton("清除重選");
		ClearButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String[] options = { "返回", "清除" };
		        int choice = JOptionPane.showOptionDialog(
		                AddPorder.this,                 // 用視窗本身做父層，讓彈窗能置中顯示
		                "確定要清除訂單內容？",             // 訊息
		                "提醒",                           // 標題
		                JOptionPane.DEFAULT_OPTION,
		                JOptionPane.WARNING_MESSAGE,
		                null,
		                options,
		                options[0]                      // 預設選「返回」
		        );

		        // 關閉或選「返回」都不動作
		        if (choice == JOptionPane.CLOSED_OPTION || choice == 0) {
		            return;
		        }else {
		        	// 選「清除」：把 JTable 內容清空
			        cartModel.setRowCount(0);
		        }

			}
		});
		ClearButton.setFont(new Font("Dialog", Font.BOLD, 14));
		ClearButton.setBackground(new Color(120, 183, 237));
		ClearButton.setBounds(610, 549, 134, 37);
		contentPane.add(ClearButton);
		
	}
	
    //工具方法區
	private void bindPrice(JComboBox periodCombo, int ticketTypeId, JLabel priceLabel) {
	    periodCombo.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent evt) {
	            updatePrice(periodCombo, ticketTypeId, priceLabel);
	        }
	    });
	}

	private void updatePrice(JComboBox periodCombo, int ticketTypeId, JLabel priceLabel)
	{
		
	            String selected = (String) periodCombo.getSelectedItem();
	            if (selected == null) 
	            {
	                priceLabel.setText("");
	                return;
	            }

            
            int periodId = Ticket.getTicketPeriodIdFromName(selected);
            Integer price = productService.getPriceByTypeAndPeriod(ticketTypeId, periodId);
            if (price != null) {
                priceLabel.setText(Formatters.currency(price));
            } else {
                priceLabel.setText("－");
            }
	      }
	
	
	// 逐列處理：讀取選到的票期與數量，查單價，加入/合併到表格
	private void processRow(int ticketTypeId,
	                        JComboBox<String> periodCombo,
	                        JComboBox<Integer> qtyCombo) {
	    Integer qty = (Integer) qtyCombo.getSelectedItem();
	    if (qty == null || qty <= 0) return; // 沒買就跳過

	    String periodName = (String) periodCombo.getSelectedItem();
	    if (periodName == null) return;
	    
	    int periodId = Ticket.getTicketPeriodIdFromName(periodName);
	    if (periodId < 0) return; // -1 表示查無此票期

	    // 取得單價
	    Integer unitPrice = productService.getPriceByTypeAndPeriod(ticketTypeId, periodId);
	    if (unitPrice == null) 
	    {
	    	Dialogs.error(
	            AddPorder.this,
	            "找不到對應票價（" + Ticket.typeName(ticketTypeId) + " / " + periodName + "）"
	        );
	        return;
	    }

	    addOrUpdateCartRow(ticketTypeId, Ticket.typeName(ticketTypeId), periodId, periodName, unitPrice, qty);
	}

	// 如果同票種+同票期已存在，就合併數量；否則新增一列
	private void addOrUpdateCartRow(int typeId,String typeName,int periodId,String periodName,int unitPrice,int qty) 
	{
	    if (qty <= 0) return;
	    
	    String key = typeId + "-" + periodId; // 合併鍵

	    // 尋找是否已有相同(票種,票期)的列，若有就合併
	    for (int i = 0; i < cartModel.getRowCount(); i++) 
	    {
	        Object k = cartModel.getValueAt(i, 5); // 第 6 欄是隱藏的key
	        if (TOTAL_KEY.equals(k)) continue; // 跳過加總列
	        
	        
	        if (key.equals(k)) {
	            int oldQty = (Integer) cartModel.getValueAt(i, 3);
	            int newQty = oldQty + qty;
	            cartModel.setValueAt(newQty, i, 3);                 // 更新數量
	            cartModel.setValueAt(unitPrice * newQty, i, 4);     // 更新金額
	            return;
	     }
	   }

	    // 沒有同類項目，就新增一列
	    cartModel.addRow(new Object[]{
	        typeName,                 // 票種
	        periodName,               // 票期
	        unitPrice,                // 單價
	        qty,                      // 數量
	        unitPrice * qty,          // 金額
	        key                       // key（隱藏）
	    });
	   
	}
	
	
	private void removeTotalRowIfAny() 
	{
	    for (int i = cartModel.getRowCount() - 1; i >= 0; i--) 
	    {
	        Object k = cartModel.getValueAt(i, 5);
	        if (TOTAL_KEY.equals(k)) {
	            cartModel.removeRow(i);
	        }
	    }
	}
	
	
	private void refreshTotalRow() 
	{
	    // 先確定沒有舊的總金額列
	    removeTotalRowIfAny();
	    
	    int sum = 0;
	    for (int i = 0; i<cartModel.getRowCount(); i++) 
	    {
	        Object k = cartModel.getValueAt(i, 5);
	        if (TOTAL_KEY.equals(k)) continue; // 跳過加總列
	        Number amount = (Number) cartModel.getValueAt(i, 4);
	        if (amount != null) sum += amount.intValue();
	    }
	    // 追加新的總金額列（放最後）
	    if (sum > 0) 
	    {
	    	cartModel.addRow(new Object[]{
	        "總金額",  // 票種欄顯示粗體的「總金額」（加粗體＆底色）
	        "", "", "",  // 票期/單價/數量空白
	        sum,         // 金額欄顯示加總
	        TOTAL_KEY    // 識別用 key
	    	});
	    }
	}
	
	/** 若 currentUser 沒有有效 id，嘗試用可辨識欄位從 DB 補回 id，並反寫 member.txt。 */
	private void repairCurrentUserIdIfMissing() {
	    if (currentUser == null) return;
	    Integer id = currentUser.getId();
	    if (id != null && id > 0) return; // 已經有 id 就不處理

	    String sql = null;
	    String val = null;

	    // 依你 Member 物件實際有的欄位來挑選一個最能代表唯一性的
	    if (currentUser.getUsername() != null && !currentUser.getUsername().isEmpty()) {
	        sql = "SELECT id FROM member WHERE username = ? ORDER BY id DESC LIMIT 1";
	        val = currentUser.getUsername();
	    }

	    if (sql == null) return; // 沒有足夠欄位可查就放棄，交給 UI 邏輯攔下

	    try (Connection conn = DbConnection.getDb();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, val);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                int fixedId = rs.getInt(1);
	                if (fixedId > 0) {
	                    currentUser.setId(fixedId);
	                    // 反寫回檔案，避免之後又失效
	                    Session.setCurrentMember(currentUser);
	                }
	            }
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace(); // 方便在 Console 看原因
	    }
	}
}




