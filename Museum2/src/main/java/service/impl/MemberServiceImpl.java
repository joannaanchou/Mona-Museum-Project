package service.impl;

import dao.impl.MemberDaoImpl;
import model.Member;
import service.MemberService;

public class MemberServiceImpl implements MemberService{

	public static void main(String[] args) {
		/*Member m=new Member("Guest02", "guest02", "3333","台南市","0911763665");
		System.out.println(new MemberServiceImpl().addMember(m));*/
		
		System.out.println(new MemberServiceImpl().login("joanna01","1111"));

	}

	private static MemberDaoImpl mdi=new MemberDaoImpl();
	
	
	@Override
	public boolean addMember(Member member) {
		boolean isAdded=false;
		Member existing=mdi.select(member.getUsername());
		
		if(existing==null)
		{
			mdi.add(member);
			isAdded=true;
		}
		
		return isAdded;
	}
	
	// 即時判斷新註冊帳號是否可使用
	@Override
	public boolean isUsernameAvailable(String username) {
		boolean isAvailable=false;
		String u=username;
		
		if(u!=null)
		{
			u.trim();
			if(!u.isEmpty())
			{
				Member existing=mdi.select(u);
				if(existing==null)
				{
					isAvailable=true;
				}
			}
		}
		return isAvailable;
	}

	@Override
	public Member login(String username, String password) {
		
		return mdi.select(username,password);
	}

	@Override
	public Member select(String username) {
		if (username == null) return null;
        return mdi.select(username.trim());
	}

	

}
