package org.sp.chat.client.view.popup;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.sp.chat.client.domain.Member;
import org.sp.chat.client.model.MemberDAO;
import org.sp.chat.client.view.ChatMain;
import org.sp.chat.client.view.ChattingPage;
import org.sp.chat.client.view.FriendPage;

import util.DBManager;
import util.ImageUtil;

public class LoginPage extends PopupPage {


	ChatMain chatMain;
	JTextField t_id;
	JPasswordField t_pass;
	JButton bt_login;
	JButton bt_join;
	DBManager dbManager;
	
	// DAO를 이용하여 db관련 업
	MemberDAO memberDAO;
	Member member;

	JLabel la_icon;
	
	
	public LoginPage(PopWin popWin,ChatMain chatMain) {
		super(popWin);
		this.chatMain = chatMain; // 메인 프레임 넘겨받기
		
		t_id = new JTextField();
		t_pass = new JPasswordField();

		bt_login = new JButton("로그인");
		bt_join = new JButton("가입");
		dbManager = new DBManager();
		memberDAO = new MemberDAO(dbManager);

		bt_login = new JButton("Login");
		bt_join = new JButton("Join");
		dbManager = new DBManager();
		memberDAO = new MemberDAO(dbManager);
		la_icon=new JLabel(new ImageIcon(ImageUtil.getImage("Logoo.png", 300, 300)));

		
		// 스타일
		setPreferredSize(new Dimension(380, 600));
		
		Dimension d = new Dimension(340, 45);
		t_id.setPreferredSize(d);
		t_pass.setPreferredSize(d);
		
		
		// 조립
		add(la_icon);
		add(t_id);
		add(t_pass);
		add(bt_login);
		add(bt_join);
		
		//bt_login.addActionListener(this);
		//bt_join.addActionListener(this);
		
		// 로그인 들었을때
		bt_login.addActionListener((e) -> {
			loginCheck();
		});

		
		// 회원가입 들었을때
		bt_join.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				popWin.showHide(PopWin.JOINPAGE);
			}
		});

		setVisible(true);

	}

	// 로그인이 성공되면, 관리자 메인 프레임 보이게 처리
	public void loginCheck() {
		// 사용자가 입력한 아이디와 패스워드를 채워넣을 빈(empty) 상태의 DTO 생성
		String id = t_id.getText();
		String pass=  new String(t_pass.getPassword());
		
		member = new Member(); // empty
		member.setId(id); // 아이디 대입
		member.setPass(pass); // 비번 대입
		
		//memberDAO.login(로그인 정보가 채워진 DTO를 원함..)
		// login() 메서드 호출 후 반환되는 DTO의 값이 null 이 아니라면, 로그인 성공으로 판단
		Member memberDTO = memberDAO.login(member);
		
		if(memberDTO == null) { // 로그인 실패
			JOptionPane.showConfirmDialog(this, "로그인 정보가 올바르지 않습니다");
		} else { // 로그인 성공
			JOptionPane.showConfirmDialog(this, "로그인 성공");

			//로그인 성공시 회원의 정보를 보관해둔다 
			chatMain.member=memberDTO;

			// 메인 프레임 보여지게..
			chatMain.setVisible(true);
			chatMain.setTitle(member.getId()+"로그인 중");

			
			//this.setVisible(false);// 나는 안 보이게..

			FriendPage friendPage=(FriendPage)chatMain.pages[ChatMain.FRIEND];
			friendPage.showFriendList();
			ChattingPage chattingPage=(ChattingPage)chatMain.pages[ChatMain.CHATTING];
			chattingPage.showRoomList();
			chatMain.repaint();

			this.setVisible(false);// 나는 안 보이게..
			
		}
	}
}