package controller.member;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.porder.AddPorder;
import controller.porder.FindMyPorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.Member;
import util.Session;
import util.Dialogs;

public class MemberCenter extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MemberCenter frame = new MemberCenter();
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
	public MemberCenter() {
		
		// 先檢查是否登入，未登入就導回登入頁
	    Member current = Session.getCurrentMember();
	    if (current == null) {
	        Dialogs.info(this, "請先登入");
	        Login login = new Login();
	        login.setVisible(true);
	        dispose();
	        return;
	    }
	    
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 595, 485);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(228, 181, 159));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(120, 183, 240));
		panel_1.setBounds(0, 46, 595, 58);
		contentPane.add(panel_1);
		
		JLabel lblMonaMuseum_1 = new JLabel("Mona Museum 會員中心");
		lblMonaMuseum_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonaMuseum_1.setForeground(new Color(249, 255, 228));
		lblMonaMuseum_1.setFont(new Font("Dialog", Font.BOLD, 20));
		lblMonaMuseum_1.setBackground(new Color(120, 183, 240));
		lblMonaMuseum_1.setBounds(6, 6, 583, 46);
		panel_1.add(lblMonaMuseum_1);
		
		//BUTTON//
		JButton btnNewButton = new JButton("前往購票");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AddPorder addPorder = new AddPorder(Session.getCurrentMember());  //建立AddPorder時，可以直接傳入目前會員物件
				addPorder.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setBackground(new Color(120, 183, 240));
		btnNewButton.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		btnNewButton.setBounds(162, 183, 267, 59);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("訂單查詢");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FindMyPorder findMyPorder=new FindMyPorder();
				findMyPorder.setVisible(true);
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		btnNewButton_1.setBackground(new Color(120, 183, 240));
		btnNewButton_1.setBounds(162, 248, 267, 59);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_1_1 = new JButton("會員登出");
		btnNewButton_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Session.setCurrentMember(null);     // 先清掉目前登入會員
				Dialogs.info(MemberCenter.this, "您已登出");
				Login login = new Login();
		        login.setVisible(true);
		        dispose();
			}
		});
		btnNewButton_1_1.setFont(new Font("Dialog", Font.BOLD, 14));
		btnNewButton_1_1.setBackground(new Color(120, 183, 240));
		btnNewButton_1_1.setBounds(162, 319, 267, 59);
		contentPane.add(btnNewButton_1_1);
		
		

	}

}
