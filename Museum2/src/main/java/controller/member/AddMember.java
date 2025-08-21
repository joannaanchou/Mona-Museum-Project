package controller.member;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Member;
import service.impl.MemberServiceImpl;
import util.Dialogs;
import util.Session;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AddMember extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField name;
	private JTextField username;
	private JTextField password;
	private JTextField address;
	private JTextField phone;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddMember frame = new AddMember();
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
	public AddMember() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 595, 485);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 244, 218));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("姓名:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_1.setBounds(172, 242, 75, 33);
		contentPane.add(lblNewLabel_1);
		
		name = new JTextField();
		name.setBounds(240, 242, 178, 37);
		contentPane.add(name);
		name.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("帳號:");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(172, 120, 75, 33);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("密碼:");
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_1_2.setBounds(172, 178, 75, 33);
		contentPane.add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("地址:");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_1_3.setBounds(172, 306, 75, 33);
		contentPane.add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("電話:");
		lblNewLabel_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_4.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_1_4.setBounds(172, 362, 75, 33);
		contentPane.add(lblNewLabel_1_4);
		
		
		//對應訊息顯示處
		JLabel usernameNotice = new JLabel("");
		usernameNotice.setFont(new Font("Dialog", Font.PLAIN, 12));
		usernameNotice.setBounds(240, 150, 264, 33);
		contentPane.add(usernameNotice);
		
		//離開帳號欄位就檢查，並提供對應訊息
		username = new JTextField();
		username.addFocusListener(new FocusAdapter() {
		    @Override
		    public void focusLost(FocusEvent e) {
		        String u = username.getText().trim();
		        if (u.isEmpty()) {
		            usernameNotice.setText("");
		            return;
		        }

		        boolean available = new MemberServiceImpl().isUsernameAvailable(u);

		        if (available) {
		            usernameNotice.setForeground(new Color(0, 128, 0));
		            usernameNotice.setText("此帳號可使用");
		        } else {
		            usernameNotice.setForeground(Color.RED);
		            usernameNotice.setText("此帳號已被註冊過，請使用其他帳號註冊");
		        }
		    }
		});
		
		username.setColumns(10);
		username.setBounds(240, 118, 178, 37);
		contentPane.add(username);
		
		password = new JTextField();
		password.setColumns(10);
		password.setBounds(240, 176, 178, 37);
		contentPane.add(password);
		
		address = new JTextField();
		address.setColumns(10);
		address.setBounds(240, 303, 178, 37);
		contentPane.add(address);
		
		phone = new JTextField();
		phone.setColumns(10);
		phone.setBounds(240, 361, 178, 37);
		contentPane.add(phone);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(120, 183, 240));
		panel_1.setBounds(0, 36, 595, 58);
		contentPane.add(panel_1);
		
		JLabel lblMonaMuseum_1 = new JLabel("Mona Museum 註冊會員");
		lblMonaMuseum_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonaMuseum_1.setForeground(new Color(249, 255, 228));
		lblMonaMuseum_1.setFont(new Font("Dialog", Font.BOLD, 20));
		lblMonaMuseum_1.setBackground(new Color(120, 183, 240));
		lblMonaMuseum_1.setBounds(6, 6, 583, 46);
		panel_1.add(lblMonaMuseum_1);
		
		
		//BUTTON//
		JButton AddMemberButton = new JButton("確認");
		AddMemberButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String Name=name.getText();
				String Username=username.getText();
				String Password=password.getText();
				String Address=address.getText();
				String Phone=phone.getText();
				Member member=new Member(Name,Username,Password,Address,Phone);
				
				if(new MemberServiceImpl().addMember(member))
				{
					Session.setCurrentMember(member);
					AddMemberSuccess addMembersuccess=new AddMemberSuccess();
					addMembersuccess.setVisible(true);
					dispose();
				}else { 
					Dialogs.error(
							AddMember.this,
							"此帳號已被使用，請使用其他帳號"
							);
						name.setText("");
						username.setText("");
						password.setText("");
						address.setText("");
						phone.setText("");
					
				}
			}
		});
		AddMemberButton.setBackground(new Color(228, 181, 159));
		AddMemberButton.setFont(new Font("Dialog", Font.BOLD, 16));
		AddMemberButton.setBounds(455, 414, 134, 37);
		contentPane.add(AddMemberButton);
		
		
		
		

	}
}
