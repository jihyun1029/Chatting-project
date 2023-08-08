package org.sp.chat.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.sp.chat.client.model.MemberDAO;
import org.sp.chat.client.view.popup.LoginPage;
import org.sp.chat.client.view.popup.PopWin;

import util.DBManager;

public class MyPage extends Page{
	JLabel la_title;
	JLabel la_logout;

	JButton bt_file;
	JButton bt_regist; //수정버튼(등록)
	JPanel p_content;
	
	Image img;


	JLabel la_login;
	JButton bt_profile;
	
	String imgprofile= "res/default.png";
	JTextField t_name;
	JFileChooser chooser;
	JPanel p_south;
	JButton nick;
	JButton unregister;
	JLabel la_icon = new JLabel();
	DBManager dbManager;
	MemberDAO memberDAO;
	
	ChatMain chatMain;
	PopWin popWin;
	MyPage mypage;
	LoginPage loginPage;
	public MyPage(ChatMain chatMain, PopWin popWin) {
		mypage = this;
		this.chatMain=chatMain;
		this.popWin=popWin;
		p_south=new JPanel();
		la_title =new JLabel("마이페이지");
		la_logout =new JLabel("로그아웃");
		la_login = new JLabel("");
		
		
		
		bt_profile = new JButton("프로필 편집");
		t_name = new JTextField("닉네임이름");
		nick =new JButton("");
		unregister=new JButton("회원탈퇴");
		dbManager = new DBManager();
		memberDAO = new MemberDAO(dbManager);


		
		//스타일
		Dimension d = new Dimension(140,40);
		la_title.setPreferredSize(d);
		la_logout.setPreferredSize(d);
		bt_profile.setPreferredSize(new Dimension(20,60));
		

		Font f =new Font("휴먼모음T", Font.PLAIN, 20);
		la_title.setFont(f);
		la_logout.setFont(f);
		bt_profile.setFont(f);
		nick.setFont(f);
		unregister.setFont(new Font("휴먼모음T", Font.PLAIN, 20));
		

		
		la_title.setHorizontalAlignment(JLabel.LEFT);
		la_logout.setHorizontalAlignment(JLabel.RIGHT);
	
		//조립		
		p_north.add(la_title);
		p_north.add(la_logout);
		p_north.add(la_login);
		
		p_center.setLayout(new BorderLayout());
		
		
		p_center.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		imgprofile();
		
		p_center.add(p_south, BorderLayout.CENTER);
		p_south.setPreferredSize(new Dimension(300,20));
		p_center.add(bt_profile, BorderLayout.SOUTH);
		p_south.add(nick);
		p_south.add(unregister);

		
		
		bt_profile.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// 프로필 편집 버튼 클릭 시 profile 연결
				Profile profile = new Profile(mypage);
				profile.setVisible(true);
			
			}
		});
		
	//회원탈퇴 처리
		unregister.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int result=JOptionPane.showConfirmDialog(null, "정말 탈퇴하시겠습니까?","회원탈퇴",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
				if(result==JOptionPane.OK_OPTION) {
					JOptionPane.showMessageDialog(MyPage.this, "탈퇴완료 이용해주셔서 감사합니다");
					deleteMember();//회원탈퇴
					System.out.println("회원탈퇴 완료");
					chatMain.setVisible(false);//현재 메인프레임을 다시 안보이게
					 loginPage.setVisible(true);
				}
			}
		
		});
	
	
	//로그아웃 처리 
			la_logout.addMouseListener(new MouseAdapter() {
				
				public void mouseClicked(MouseEvent e) {
					int result=JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?","로그아웃",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
					if(result==JOptionPane.OK_OPTION) {
						logout();
					}
				}
			});
	}
		

	//관리자가 로그인시 호출될 메서드
		public void login() {
			la_login.setText("로그아웃");
			//프레임의 상단 제목에 로그인 한 관리자의 이름 출력 
			this.setVisible(true);//현재 메인프레임을 안보이게..
			loginPage.setVisible(false);//로그인폼 다시  안보이게
			
		}
		
		//관리자가 로그아웃시 호출될 메서드 
		public void logout() {
			la_login.setText("");
			//this.setTitle("");
			
			//관리자 정보가 들어있었던 DTO를 다시 초기화
			//loginForm.memeber=null;
			
			chatMain.setVisible(false);//현재 메인프레임을 다시 안보이게..
			loginPage.setVisible(true);
		}
		

	
	// 화면을 다시 구성하기
	public void repaint() {
		//System.out.println("동작여부");
		if(ChatMain.member == null) {
			//System.out.println("null 값?");
			return ;
		}
		
		//이미지교체
		//System.out.println("이미지교체" + ChatMain.member.getImg());
		
		
		//la_icon.setIcon(getIcon("res/chatting.png"));
		la_icon.setIcon(getIcon("res/"+ChatMain.member.getImg()));
		
		//이름 변경
		nick.setText(ChatMain.member.getName());
	}
		
	private ImageIcon getIcon(String imageSrc) {
		URL url=ClassLoader.getSystemResource(imageSrc);
			//System.out.println(imageSrc);
		try {
			BufferedImage buffImg = ImageIO.read(url);
			Image image=buffImg;
			image=image.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
			return new ImageIcon(image);
		
		}catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
		
	}
	public void imgprofile() {
			la_icon = new JLabel(getIcon(imgprofile));
			p_south.add(la_icon);

	}
	
	//회원 탈퇴 메서드
	public void deleteMember() {
		int result=memberDAO.delete(chatMain.member.getMember_idx());
	}
	
}

