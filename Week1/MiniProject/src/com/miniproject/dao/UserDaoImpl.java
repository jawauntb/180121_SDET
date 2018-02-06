package com.miniproject.dao;

import static com.miniproject.util.CloseStreams.close;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.miniproject.users.User;
import com.miniproject.util.Connections;

public class UserDaoImpl implements UserDao {

	@Override
	public void insertUser(User u) {
		CallableStatement stmt = null;
		
		try(Connection conn = Connections.getConnection()){
			stmt = conn.prepareCall("{call insert_into_users(?,?)}");
			stmt.setString(1, u.getUsername());
			stmt.setString(2, u.getPassword());
			stmt.execute();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmt);
		}
	}

	@Override
	public List<User> getAllUsersForAdmin() {
		Statement stmt = null;
		ResultSet rs = null;
		List<User> users = new ArrayList<>();
		
		try(Connection conn = Connections.getConnection()){
			String sql = "SELECT username, account_approved, banned FROM users";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				users.add(new User(rs.getString(1), rs.getInt(2), rs.getInt(3)));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmt);
			close(rs);
		}
		
		return users;
	}

	@Override
	public User getUser(String inUsername, String inPassword) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		User u = null;
		
		try(Connection conn = Connections.getConnection()){
			String sql = "SELECT username, account_approved, currency, login_streak, beverage_id, banned "
					+ "FROM users WHERE username = ? AND p_word = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, inUsername);
			ps.setString(2, inPassword);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				u = new User(rs.getString(1), rs.getInt(2), rs.getDouble(3), 
						rs.getInt(4), rs.getInt(5), rs.getInt(6));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(ps);
			close(rs);
		}
		return u;
	}

	@Override
	public String getUsername(String inUsername) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String s = null;
		
		try(Connection conn = Connections.getConnection()){
			String sql = "SELECT username FROM users WHERE username = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, inUsername);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				s = rs.getString(1);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(ps);
			close(rs);
		}
		return s;
	}
	
	public void approveUser(String inUsername) {
		CallableStatement stmt = null;
		
		try(Connection conn = Connections.getConnection()){
			stmt = conn.prepareCall("{call approve_user(?)}");
			stmt.setString(1, inUsername);
			stmt.execute();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmt);
		}
	}

	@Override
	public void uBanUser(String inUsername, int inOption) {
		CallableStatement stmt = null;
		
		try(Connection conn = Connections.getConnection()){
			stmt = conn.prepareCall("{call ban_un_user(?, ?)}");
			stmt.setString(1, inUsername);
			stmt.setInt(2, inOption);
			stmt.execute();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmt);
		}
	}

	@Override
	public int loginStreak(String inUsername) {
		CallableStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int loginStreak = -1;
		
		try(Connection conn = Connections.getConnection()){
			stmt = conn.prepareCall("{call login_streak	(?)}");
			stmt.setString(1, inUsername);
			stmt.execute();
			
			ps = conn.prepareStatement("SELECT login_streak FROM users WHERE username = ?");
			ps.setString(1, inUsername);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				loginStreak = rs.getInt(1);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(stmt);
			close(ps);
			close(rs);
		}
		
		return loginStreak;
	}

	@Override
	public double getCurrency(String inUsername) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		double d = -1;
		
		try(Connection conn = Connections.getConnection()){
			String sql = "SELECT currency FROM users WHERE username = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, inUsername);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				d = rs.getDouble(1);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(ps);
			close(rs);
		}
		return d;
	}

	@Override
	public void setCurrency(String inUsername, double money) {
		PreparedStatement ps = null;
		
		try(Connection conn = Connections.getConnection()){
			String sql = "UPDATE users SET currency = ? WHERE username = ?";
			ps = conn.prepareStatement(sql);
			ps.setDouble(1, money);
			ps.setString(2, inUsername);
			ps.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(ps);
		}
	}

}
