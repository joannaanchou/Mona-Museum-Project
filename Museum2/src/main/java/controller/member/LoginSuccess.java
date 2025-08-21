package controller.member;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import controller.porder.AddPorder;
import model.Member;
import util.Session;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;

public class LoginSuccess extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginSuccess frame = new LoginSuccess();
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
	public LoginSuccess() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 595, 485);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 244, 218));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(238, 244, 218));
		panel.setBounds(62, 147, 471, 106);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel WelcomeMessage = new JLabel("<dynamic>, 歡迎回來！");
		WelcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
		WelcomeMessage.setFont(new Font("Dialog", Font.BOLD, 14));
		WelcomeMessage.setBackground(new Color(238, 244, 218));
		WelcomeMessage.setBounds(6, 6, 459, 94);
		panel.add(WelcomeMessage);
		
		Member member = Session.getCurrentMember();
		String show;
		if (member != null && member.getName() != null && !member.getName().isEmpty()) {
		    show = member.getName() + "，歡迎回來！";
		} else {
		    show = "歡迎回來！";
		}
		WelcomeMessage.setText(show);
		
		//BUTTON//
		JButton btnNewButton = new JButton("立即購票");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AddPorder addPorder=new AddPorder();
				addPorder.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		btnNewButton.setBackground(new Color(228, 181, 159));
		btnNewButton.setBounds(320, 297, 117, 51);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("會員中心");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MemberCenter memberCenter=new MemberCenter();
				memberCenter.setVisible(true);
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		btnNewButton_1.setBackground(new Color(228, 181, 159));
		btnNewButton_1.setBounds(158, 297, 117, 51);
		contentPane.add(btnNewButton_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(120, 183, 240));
		panel_1.setBounds(0, 27, 595, 58);
		contentPane.add(panel_1);
		
		JLabel lblMonaMuseum = new JLabel("Mona Museum");
		lblMonaMuseum.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonaMuseum.setForeground(new Color(249, 255, 228));
		lblMonaMuseum.setFont(new Font("Dialog", Font.BOLD, 20));
		lblMonaMuseum.setBackground(new Color(120, 183, 240));
		lblMonaMuseum.setBounds(6, 6, 583, 46);
		panel_1.add(lblMonaMuseum);

	}
}
