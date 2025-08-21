package controller.member;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.porder.AddPorder;
import model.Member;
import util.Session;
import util.Dialogs;


import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddMemberSuccess extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddMemberSuccess frame = new AddMemberSuccess();
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
	public AddMemberSuccess() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 595, 485);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 244, 218));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(238, 244, 218));
		panel.setBounds(70, 144, 447, 139);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel showMessage = new JLabel("");
		showMessage.setBackground(new Color(238, 244, 218));
		showMessage.setHorizontalAlignment(SwingConstants.CENTER);
		showMessage.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		showMessage.setBounds(6, 6, 435, 127);
		panel.add(showMessage);
		

		Member member = Session.getCurrentMember();
		if (member == null || member.getName() == null || member.getName().isEmpty()) {
		    Dialogs.info(this, "會員資料未載入，將以『新會員』顯示。");
		}
		String name = (member != null && member.getName() != null && !member.getName().isEmpty())
		        ? member.getName()
		        : "新會員";
		showMessage.setText(name + "，歡迎加入！");
		
		//Button//
		JButton btnNewButton = new JButton("立即購票");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AddPorder addPorder=new AddPorder();
				addPorder.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 14));
		btnNewButton.setBackground(new Color(228, 181, 159));
		btnNewButton.setBounds(237, 305, 117, 51);
		contentPane.add(btnNewButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(120, 183, 240));
		panel_1.setBounds(0, 40, 595, 58);
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
