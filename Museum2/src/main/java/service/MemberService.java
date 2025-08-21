package service;

import model.Member;

public interface MemberService {

		//create
		boolean addMember(Member member);
		boolean isUsernameAvailable(String username);
		
		//read
		Member login(String username,String password);//登入時判斷帳密是否正確
		Member select(String username);

}
