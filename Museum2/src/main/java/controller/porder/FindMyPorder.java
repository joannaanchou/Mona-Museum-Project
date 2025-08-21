package controller.porder;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import controller.member.MemberCenter;
import model.Member;
import model.Porder;
import model.PorderItem;
import model.Product;
import service.PorderItemService;
import service.PorderService;
import service.ProductService;
import service.impl.PorderItemServiceImpl;
import service.impl.PorderServiceImpl;
import service.impl.ProductServiceImpl;
import util.Dialogs;
import util.Session;
import util.Tables;
import util.Ticket;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

public class FindMyPorder extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel tableModel;
	
	
	private PorderService porderService = new PorderServiceImpl();
    private PorderItemService porderItemService = new PorderItemServiceImpl();
    private ProductService productService = new ProductServiceImpl();

    

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FindMyPorder frame = new FindMyPorder();
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
	public FindMyPorder() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 690);
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
		
		JLabel lblMonaMuseum_1 = new JLabel("Mona Museum 我的訂單");
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
		showMember.setBounds(0, 113, 142, 50);
		contentPane.add(showMember);
		
		JLabel showTime = new JLabel("");
		showTime.setHorizontalAlignment(SwingConstants.CENTER);
		showTime.setForeground(new Color(0, 1, 1));
		showTime.setFont(new Font("Dialog", Font.BOLD, 14));
		showTime.setBackground(new Color(237, 243, 219));
		showTime.setBounds(701, 113, 185, 50);
		contentPane.add(showTime);
		
		Timer clockTimer = new Timer(1000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		        String currentTime = sdf.format(new Date());
		        showTime.setText(currentTime);
		    }
		});
		clockTimer.start();
		
		
		Member m = Session.getCurrentMember();
		String show = (m != null && m.getName() != null) ? m.getName() : "未登入";
		showMember.setText("會員：" + show);
		
		
		JScrollPane myOrder = new JScrollPane();
		myOrder.setBounds(21, 175, 870, 339);
		contentPane.add(myOrder);
		
		String[] columnNames = {"訂單編號", "啟用日", "票種", "票期", "單價", "數量", "金額"};
		tableModel = new DefaultTableModel(columnNames, 0) {
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};

		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		table.setRowHeight(28);
		table.setShowGrid(true);
		table.setGridColor(new Color(120, 120, 120));
		table.setFont(new Font("Dialog", Font.PLAIN, 13));


		myOrder.setViewportView(table);
		loadMyOrders();
		
		// 表格樣式統一 util.Tables
		Tables.styleHeader(table);
		// 欄位：0 訂單編號, 1 啟用日, 2 票種, 3 票期, 4 單價, 5 數量, 6 金額
		Tables.centerAlign(table, 0, 1, 2, 3);
		Tables.rightAlign(table, 4, 5, 6);
		Tables.zebra(table);
		table.setAutoCreateRowSorter(true); // 可排序
		
		
		//Button//
		JButton MemberCenterButton = new JButton("回會員中心");
		MemberCenterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MemberCenter memberCenter = new MemberCenter();
				memberCenter.setVisible(true);
		        dispose();
			}
		});
		MemberCenterButton.setFont(new Font("Dialog", Font.BOLD, 14));
		MemberCenterButton.setBackground(new Color(120, 183, 237));
		MemberCenterButton.setBounds(729, 572, 162, 58);
		contentPane.add(MemberCenterButton);
		
	}
	
	
	//loadMyOrders方法
	private void loadMyOrders() {
	    Member m = (Member) Session.getCurrentMember();
	    if (m == null) {
	    	Dialogs.info(this, "請先登入會員！");
	        return;
	    }

	    List<Porder> orders = porderService.getOrdersByMemberId(m.getId());
	    DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    for (Porder p : orders) {
	        List<PorderItem> items = porderItemService.getItemsByPorderId(p.getId());

	        for (PorderItem item : items) {
	            Product prod = productService.getProductById(item.getProductId());

	            String ticketType   = Ticket.typeName(prod.getTicketTypeId());
	            String ticketPeriod = Ticket.periodName(prod.getTicketPeriodId());
	            int unitPrice = prod.getPrice();
	            int qty = item.getQuantity();
	            int total = unitPrice * qty;

	            tableModel.addRow(new Object[]{
	                    p.getOrderNo(),
	                    (p.getOrderDate() != null) ? p.getOrderDate().format(dateFmt) : "—",
	                    ticketType,
	                    ticketPeriod,
	                    unitPrice,
	                    qty,
	                    total
	            });
	        }
	    }
	}
}
