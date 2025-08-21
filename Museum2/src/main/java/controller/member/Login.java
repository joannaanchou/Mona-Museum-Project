package controller.member;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import controller.admin.AdminCenter;
import model.Member;
import service.impl.MemberServiceImpl;
import util.Tool;
import util.Session;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPasswordField;
import javax.swing.JToggleButton;


public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 595, 475);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(228, 181, 159));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(120, 183, 240));
		panel.setBounds(0, 33, 595, 58);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome to Mona Museum");
		lblNewLabel.setBackground(new Color(120, 183, 240));
		lblNewLabel.setForeground(new Color(249, 255, 228));
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(6, 6, 583, 46);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("帳號");
		lblNewLabel_1.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(148, 128, 61, 36);
		contentPane.add(lblNewLabel_1);
		
		username = new JTextField();
		username.setBackground(new Color(238, 244, 218));
		username.setBounds(157, 156, 281, 36);
		contentPane.add(username);
		username.setColumns(10);
		
		password = new JPasswordField();
		password.setBackground(new Color(238, 244, 218));
		password.setBounds(157, 220, 281, 36);
		contentPane.add(password);
		
		//預設的遮罩字元
		final char defaultEcho = password.getEchoChar();
		
		JToggleButton eye = new JToggleButton();
		eye.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		eye.setFocusable(false);
		eye.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
		eye.setToolTipText("顯示/隱藏密碼");
		
		// 設定圖示（16x16 可自行調整）
		java.net.URL openUrl=getClass().getResource("/img/eye.png");
		java.net.URL closeUrl=getClass().getResource("/img/eye-off.png");
		
		if (openUrl != null) {
		    java.awt.Image img = new ImageIcon(closeUrl).getImage().getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
		    eye.setIcon(new ImageIcon(img));
		}
		if (closeUrl != null) {
		    java.awt.Image img = new ImageIcon(openUrl).getImage().getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
		    eye.setSelectedIcon(new ImageIcon(img));
		}
		
		
		eye.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (eye.isSelected()) {
			        // 開眼：顯示
			        password.setEchoChar((char) 0);
			        eye.setToolTipText("隱藏密碼");
			    } else {
			        // 關眼：恢復遮罩
			        password.setEchoChar(defaultEcho);
			        eye.setToolTipText("顯示密碼");
			    }
			}
		});
		
		eye.setBounds(415, 220, 79, 34);
		contentPane.add(eye);
		
		
		
		JLabel lblNewLabel_1_1 = new JLabel("密碼");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(148, 195, 61, 36);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("還不是會員？");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_1_1_1.setBounds(167, 327, 123, 36);
		contentPane.add(lblNewLabel_1_1_1);
		
		
		//BUTTON//
		JButton LoginButton = new JButton("登入");
		LoginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String Username=username.getText();
				String Password = new String(password.getPassword());
				Member m=new MemberServiceImpl().login(Username, Password);
				
				
				if (m != null && m.getId() == null) {
				    // 補救：用 username 再查一次把 id 補齊（需要有 select(String username)）
				    Member fresh = new MemberServiceImpl().select(Username);
				    if (fresh != null && fresh.getId() != null) {
				        m = fresh;
				    } else {
				        JOptionPane.showMessageDialog(Login.this, "會員資料缺少ID，請聯絡管理員或重新註冊。");
				        return;
				    }
				}
				
				if(m!=null)
				{
					Tool.saveFile(m,"member.txt");
					
					
					if("admin01".equals(Username)&&"0000".equals(Password))
					{
						AdminCenter adminCenter = new AdminCenter();
						adminCenter.setVisible(true);
					
					}else {
						LoginSuccess loginSuccess = new LoginSuccess();
		                loginSuccess.setVisible(true);
					}
					dispose();
					
				}else {
					
					// 自訂按鈕文字
				    Object[] options = {"重新輸入"};
				    
					JOptionPane.showOptionDialog(
					        Login.this,                   // 確保彈窗定位正確
					        "帳號密碼錯誤！",                // 訊息
					        "登入失敗",                      // 標題
					        JOptionPane.DEFAULT_OPTION,   // 選項型態
					        JOptionPane.ERROR_MESSAGE,    // 圖示：錯誤
					        null,                         // 自訂圖示（null就不自訂）
					        options,                      // 按鈕文字
					        options[0]                    // 預設按鈕
					    );
					
						// 彈窗按下確認後，自動清空帳密欄位
						username.setText("");
						password.setText("");
				}
				
			}
		});
		LoginButton.setForeground(new Color(0, 0, 0));
		LoginButton.setBackground(new Color(120, 183, 240));
		LoginButton.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		LoginButton.setBounds(157, 273, 281, 41);
		contentPane.add(LoginButton);
		
		JButton RegisterButton = new JButton("加入會員");
		RegisterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AddMember addmember=new AddMember();
				addmember.setVisible(true);
				dispose();
			}
		});
		RegisterButton.setForeground(Color.BLACK);
		RegisterButton.setFont(new Font("Dialog", Font.BOLD, 14));
		RegisterButton.setBackground(new Color(120, 183, 240));
		RegisterButton.setBounds(292, 326, 146, 41);
		contentPane.add(RegisterButton);
		

		
		
		
		

	}
}
