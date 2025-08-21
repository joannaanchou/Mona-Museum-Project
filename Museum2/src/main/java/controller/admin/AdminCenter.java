
package controller.admin;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.member.Login;
import controller.porder.FindAllPorder;
import model.Member;
import util.Dialogs;
import util.Session;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.Font;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminCenter extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Member currentUser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminCenter frame = new AdminCenter();
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
	public AdminCenter() {
		
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
		
		JLabel lblMonaMuseum_1 = new JLabel("Mona Museum 管理員後台");
		lblMonaMuseum_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonaMuseum_1.setForeground(new Color(249, 255, 228));
		lblMonaMuseum_1.setFont(new Font("Dialog", Font.BOLD, 20));
		lblMonaMuseum_1.setBackground(new Color(120, 183, 240));
		lblMonaMuseum_1.setBounds(6, 6, 583, 46);
		panel_1.add(lblMonaMuseum_1);
		
		
		
		JLabel showAdmin = new JLabel("<dynamic>");
		showAdmin.setHorizontalAlignment(SwingConstants.RIGHT);
		showAdmin.setForeground(new Color(0, 1, 1));
		showAdmin.setFont(new Font("Dialog", Font.BOLD, 12));
		showAdmin.setBackground(new Color(237, 243, 219));
		showAdmin.setBounds(366, 116, 223, 32);
		contentPane.add(showAdmin);
		
		this.currentUser = Session.getCurrentMember();
		
		String show;
        if (currentUser != null && currentUser.getName() != null) {
            String idStr = (currentUser.getId() != null && currentUser.getId() > 0)
                    ? "（ID: " + currentUser.getId() + "）" : "（ID 未載入）";
            show = "管理員：" + currentUser.getName() + " " + idStr;
        } else {
            show = "管理員：未登入";
        }
        showAdmin.setText(show);
				
		
		
				
				//Button
				
				JButton btnNewButton_1 = new JButton("訂單管理");
				btnNewButton_1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FindAllPorder findAllPorder=new FindAllPorder();
						findAllPorder.setVisible(true);
						dispose();
					}
				});
				btnNewButton_1.setFont(new Font("微軟正黑體", Font.BOLD, 14));
				btnNewButton_1.setBackground(new Color(120, 183, 240));
				btnNewButton_1.setBounds(161, 232, 267, 59);
				contentPane.add(btnNewButton_1);
				
				
		JButton btnNewButton_1_1 = new JButton("管理員登出");
		btnNewButton_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Session.setCurrentMember(null);     // 先清掉目前登入會員
				Dialogs.info(AdminCenter.this, "您已登出");
				Login login = new Login();
		        login.setVisible(true);
		        dispose();
			}
		});
		btnNewButton_1_1.setFont(new Font("Dialog", Font.BOLD, 14));
		btnNewButton_1_1.setBackground(new Color(120, 183, 240));
		btnNewButton_1_1.setBounds(161, 303, 267, 59);
		contentPane.add(btnNewButton_1_1);
		

		
		
		
				
				
				
		
		

	}
}
