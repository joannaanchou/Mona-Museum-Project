package dao;

import model.Member;

public interface MemberDao {

	//create
	void add(Member member);
	
	//read
	Member select(String username, String password);
	Member select(String username); 
	
	
}
