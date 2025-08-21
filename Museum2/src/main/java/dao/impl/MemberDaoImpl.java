package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.MemberDao;
import model.Member;
import util.DbConnection;

public class MemberDaoImpl implements MemberDao{

	public static void main(String[] args) {
		/*Member member = new Member();
		 member.setName("Allen");
		 member.setUsername("allen01");
		 member.setPassword("2222");
		 member.setAddress("台北市");
		 member.setPhone("0923318276");
		 
		 new MemberDaoImpl().add(member);*/
		
		System.out.println(new MemberDaoImpl().select("admin01"));

	}
	
	private static Connection conn=DbConnection.getDb();

	@Override
	public void add(Member member) {
		String sql="insert into member(name,username,password,address,phone)"+"values(?,?,?,?,?)";
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1,member.getName());
			ps.setString(2,member.getUsername());
			ps.setString(3,member.getPassword());
			ps.setString(4,member.getAddress());
			ps.setString(5,member.getPhone());
			
			ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public Member select(String username, String password) {
		Member member=null;
		String sql="select*from member where username=? and password=?";
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1,username);
			ps.setString(2,password);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				member=new Member();
				member.setId(rs.getInt("id"));
				member.setName(rs.getString("name"));
				member.setUsername(rs.getString("username"));
				member.setPassword(rs.getString("password"));
				member.setAddress(rs.getString("address"));
				member.setPhone(rs.getString("phone"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return member;
	}


	@Override
	public Member select(String username) {
		
		Member member=null;
		String sql="select*from member where username=?";
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1,username);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				member=new Member();
				member.setId(rs.getInt("id"));
				member.setName(rs.getString("name"));
				member.setUsername(rs.getString("username"));
				member.setPassword(rs.getString("password"));
				member.setAddress(rs.getString("address"));
				member.setPhone(rs.getString("phone"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return member;
	}

}
