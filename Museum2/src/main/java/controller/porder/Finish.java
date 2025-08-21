package controller.porder;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.member.MemberCenter;
import model.Member;
import model.Porder;
import model.PorderItem;
import service.ProductService;
import service.impl.PorderItemServiceImpl;
import service.impl.ProductServiceImpl;
import util.CreateExcel;
import util.Dialogs;
import util.Formatters;
import util.Session;
import util.Ticket;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.List;


import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.time.format.DateTimeFormatter;
import service.PorderItemService;
import model.Product;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Finish extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	
	// 票種/票期對應名稱
   /* private static Map<Integer, String> TYPE_NAME = new HashMap<>();
    private static Map<Integer, String> PERIOD_NAME = new HashMap<>();
    static {
        // ticket_type: 1=全票、2=學生票、3=愛心票
        TYPE_NAME.put(1, "全票");
        TYPE_NAME.put(2, "學生票");
        TYPE_NAME.put(3, "愛心票");

        // ticket_period: 1=單日票、2=季票、3=年票
        PERIOD_NAME.put(1, "單日票");
        PERIOD_NAME.put(2, "季票");
        PERIOD_NAME.put(3, "年票");
    }*/

    // 依 productId 查商品（拿單價、票種/票期）
    private final ProductService productService = new ProductServiceImpl();
    
    private static String nz(String s) {return (s == null || s.isBlank()) ? "—" : s;}
    
    private PorderItemService porderItemService = new PorderItemServiceImpl();

    
    

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Finish frame = new Finish();
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
	public Finish() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 911, 639);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(237, 243, 219));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(120, 183, 240));
		panel_1.setBounds(0, 37, 910, 58);
		contentPane.add(panel_1);
		
		JLabel lblMonaMuseum_1 = new JLabel("Mona Museum 訂單完成");
		lblMonaMuseum_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonaMuseum_1.setForeground(new Color(249, 255, 228));
		lblMonaMuseum_1.setFont(new Font("Dialog", Font.BOLD, 20));
		lblMonaMuseum_1.setBackground(new Color(120, 183, 240));
		lblMonaMuseum_1.setBounds(6, 6, 914, 46);
		panel_1.add(lblMonaMuseum_1);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(228, 181, 159));
		panel.setBounds(24, 139, 855, 362);
		contentPane.add(panel);
		panel.setLayout(null);
		
		Member m=(Member)Session.getCurrentMember();
		Porder p=(Porder)Session.getCurrentPorder();
		
		JLabel showMessage = new JLabel("");
		showMessage.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		showMessage.setBounds(24, 107, 306, 32);
		contentPane.add(showMessage);
		
		
		String memberName = (m != null && m.getName() != null && !m.getName().isBlank()) ? m.getName() : "訪客";
		showMessage.setText("訂單已完成！" + memberName + "，以下為訂單資訊");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 855, 362);
		panel.add(scrollPane);
		
		//Reporter 內容區//
		JTextArea showReporter = new JTextArea();
		scrollPane.setViewportView(showReporter);
		showReporter.setBackground(new Color(228, 181, 159));
		
		
		String show = "";

		//會員資料
		show += "【會員資料】";
		show += "\n姓名：" + nz(m != null ? m.getName()   : null);
		show += "\n地址：" + nz(m != null ? m.getAddress(): null);
		show += "\n聯絡電話：" + nz(m != null ? m.getPhone()  : null);
		show += "\n";

		show += "\n================================================";
		show += "\n【訂單內容】";

		// 2) 訂單編號、下單時間、票券啟用日
		String orderNo = (p != null && p.getOrderNo() != null) ? p.getOrderNo() : "—";

		// 下單時間（createdAt）
		String placedAt = (p != null && p.getCreatedAt() != null)
		        ? p.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
		        : "—";

		// 票券啟用日（orderDate）
		String activateDate = (p != null && p.getOrderDate() != null)
		        ? p.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
		        : "—";
		
		show += "\n訂單編號：" + orderNo;
		show += "\n下單時間：" + placedAt;      
		show += "\n票券啟用日：" + activateDate;  
		
		// 3) 單筆商品明細（依 productId 取 Product）
		show += "\n\n項目明細：";
		int grandTotal = 0;
		
		if (p != null && p.getId() != null && p.getId() > 0) {
		    List<PorderItem> items = porderItemService.getItemsByPorderId(p.getId());

		    if (items != null && !items.isEmpty()) {
		        for (PorderItem item : items) {
		            Product prod = (item.getProductId() != null)
		                ? productService.getProductById(item.getProductId())
		                : null;

		            String typeName = "未知票種";
		            String periodName = "未知票期";
		            Integer unitPrice = null;

		            if (prod != null) {
		                typeName = Ticket.typeName(prod.getTicketTypeId());
		                periodName = Ticket.periodName(prod.getTicketPeriodId());
		                unitPrice = prod.getPrice();
		            }

		            int qty = Math.max(0, item.getQuantity());

		            if (unitPrice != null) {
		                int lineTotal = unitPrice * qty;
		                grandTotal += lineTotal;
		                show += "\n- " + typeName + " / " + periodName +
		                        "：" + qty + " 張 x" + Formatters.currency(unitPrice) + " =" + Formatters.currency(lineTotal);
		            } else {
		                show += "\n- " + typeName + " / " + periodName +
		                        "：" + qty + " 張 x $— = $—（未設定價錢或商品不存在）";
		            }
		        }
		    } else {
		        show += "\n（找不到任何明細）";
		    }
		} else {
		    show += "\n（找不到訂單資料）";
		}

		
		// 4) 金額總結
		if (grandTotal == 0 && p != null && p.getTotalAmount() > 0) {
		    grandTotal = p.getTotalAmount();
		}

		show += "\n";
		show += "\n================================================";
		show += "\n【金額計算】";
		show += "\n總金額：" + Formatters.currency(grandTotal);
		
		showReporter.setText(show);
		showReporter.setCaretPosition(0);
		
		
		//Button//
		JButton btnNewButton_1_1 = new JButton("回會員中心");
		btnNewButton_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MemberCenter memberCenter=new MemberCenter();
				memberCenter.setVisible(true);
				dispose();
			}
		});
		btnNewButton_1_1.setFont(new Font("Dialog", Font.BOLD, 14));
		btnNewButton_1_1.setBackground(new Color(120, 183, 240));
		btnNewButton_1_1.setBounds(24, 512, 117, 51);
		contentPane.add(btnNewButton_1_1);
		
		
		
		
		JButton btnNewButton_1 = new JButton("列印");
		btnNewButton_1.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		            // 基本列印
		            showReporter.print(null, null, true, null, null, true);
		        } catch (PrinterException e1) {
		            e1.printStackTrace();
		        }
		    }
		});
		
		btnNewButton_1.setFont(new Font("Dialog", Font.BOLD, 14));
		btnNewButton_1.setBackground(new Color(120, 183, 240));
		btnNewButton_1.setBounds(632, 524, 117, 51);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("匯出");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				// 讀取目前showreporter內容
			    String receipt = showReporter.getText();
			    if (receipt == null || receipt.isBlank()) {
			    	Dialogs.error(
			            Finish.this, "目前沒有可匯出的內容！"
			        );
			        return;
			    }
				
				// 選擇檔案位置&名稱
				JFileChooser fileChooser = new JFileChooser();
		        fileChooser.setDialogTitle("請選擇匯出Excel檔案的位置");
		        fileChooser.setSelectedFile(new File("OrderExport.xlsx")); //預設檔名

		        int userSelection = fileChooser.showSaveDialog(Finish.this);
		        if (userSelection == JFileChooser.APPROVE_OPTION) {
		            File fileToSave = fileChooser.getSelectedFile();
		            String filePath = fileToSave.getAbsolutePath();

		            // 若使用者沒加副檔名，自動補上.xlsx
		            if (!filePath.toLowerCase().endsWith(".xlsx")) {
		                filePath += ".xlsx";
		                fileToSave = new File(filePath);
		            }

		            try {
		                CreateExcel excel = new CreateExcel();
		                excel.exportReceipt(filePath, "訂單明細",receipt);

		                Dialogs.info(
		                    Finish.this, "Excel 匯出成功：\n"+filePath
		                );
		            } catch (Exception ex) {
		                ex.printStackTrace();
		                Dialogs.error(
		                    Finish.this, "Excel 匯出失敗：" + ex.getMessage()
		                );
		            }
		        } 
		    }
		});
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 14));
		btnNewButton.setBackground(new Color(120, 183, 240));
		btnNewButton.setBounds(762, 524, 117, 51);
		contentPane.add(btnNewButton);

	}
}
