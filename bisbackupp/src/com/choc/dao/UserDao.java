package com.choc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.choc.exceptions.ObjectNotFoundException;
import com.choc.exceptions.UserAlreadyExistsException;
import com.choc.model.User;
import com.choc.model.UserAddress;
import com.choc.util.DBConnectionPool;
import com.choc.util.DbUtils;
import static com.choc.dao.DbSchema.*;

/**
 * Data Access Object(DAO) singleton class for CRUD operations on User.<br>
 * All the operations on User data is done via this class.
 * @author Maharshi
 */
public class UserDao {
	/**
	 * The static singleton instance of UserDAO
	 */
	private static UserDao dao;
	
	/**
	 * MySQL connection object
	 */
	private DBConnectionPool connectionPool;
	
	private UserDao() {
		connectionPool = DbUtils.getConnectionPool();
	}
	
	public static UserDao getInstance() {
		if(dao == null) {
			dao = new UserDao();
		}
		return dao;
	}
	
	/**
	 * This Method is use to get the email_id of the User whose user_id is provided.
	 * @param id: user_id of the user
	 * @return email_id of the user if it exists else null
	 * @throws UserIDNotFoundException
	 */
	public String getEmailByUserID(String id) throws ObjectNotFoundException {
		
		String email = null;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try{
			String query = "select " + user_email + " from " + table_users + " where " + user_id + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, id);
			rs = statement.executeQuery();
			if(rs.next()) {
				email = rs.getString(user_email);
			}
			else {
				throw new ObjectNotFoundException(user_id + " " + id + " does not exist");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		
		return email;
	}
	
	/**
	 * This Method is use to get the user_id of the User whose email_id is provided.
	 * @param email : email_id of the user
	 * @return user_id of the User if it exists else null
	 * @throws UserEmailNotFoundException
	 */
	public String getUserIDByEmail(String email) throws ObjectNotFoundException {
		String id = null;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try{
			String query = "select " + user_id + " from " + table_users + " where " + user_email + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, email);
			rs = statement.executeQuery();
			if(rs.next()) {
				id = rs.getString(user_id);
			}
			else {
				throw new Exception();
			}
		} catch (Exception e){
			throw new ObjectNotFoundException(user_id + "\"" + id + "\" does not exist");
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return id;
	}
	
	public String getUserHash(String emailID) {
		String hashcode = null;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String query = "select " + user_hashcode + " from " + table_users + " where " + user_email + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, emailID);
			rs = statement.executeQuery();
			if(rs.next()) {
				hashcode = rs.getString(user_hashcode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return hashcode;
	}
	
	public Map<String,Object> getUserAttributes(String userID, List<String> attributes) {
		HashMap<String,Object> map = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			String colums = "";
			for(String str : attributes) {
				colums += " ," + str;
			}
			String query = "select " + user_id + colums + " from " + table_users + " where " + user_id + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userID);
			rs = statement.executeQuery();
			if(rs.next()) {
				map = new HashMap<String, Object>();
				for(String str : attributes) {
					map.put(str, rs.getObject(str));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return map;
	}
	
	public User getUserByUserID(String userID) {
		User user = null;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String querry = "select * from  " + table_users + " where " + user_id + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			statement.setString(1, userID);
			rs = statement.executeQuery();
			
			if(rs.next()) {
				user = new User();
				user.setUserID(rs.getString(user_id));
				user.setEmail(rs.getString(user_email));
				user.setFirstName(rs.getString(user_fname));
				user.setLastName(rs.getString(user_lname));
				user.setValid(rs.getBoolean(user_verify_bit));
				user.setLogin(rs.getBoolean(user_login_bit));
				user.setPassword(rs.getString(user_password));
				user.setContact(rs.getString(user_contact));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return user;
	}
	
	public User getUserByEmailId(String emailId) {
		User user = null;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String querry = "select * from  " + table_users + " where " + user_email + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			statement.setString(1, emailId);
			rs = statement.executeQuery();
			
			if(rs.next()) {
				user = new User();
				user.setUserID(rs.getString(user_id));
				user.setEmail(rs.getString(user_email));
				user.setFirstName(rs.getString(user_fname));
				user.setLastName(rs.getString(user_lname));
				user.setValid(rs.getBoolean(user_verify_bit));
				user.setLogin(rs.getBoolean(user_login_bit));
				user.setPassword(rs.getString(user_password));
				user.setContact(rs.getString(user_contact));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return user;
	}
	
	public User getUserByHashCode(String hashcode) {
		User user = null;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String querry = "select * from  " + table_users + " where " + user_hashcode + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			statement.setString(1, hashcode);
			rs = statement.executeQuery();
			
			if(rs.next()) {
				user = new User();
				user.setUserID(rs.getString(user_id));
				user.setEmail(rs.getString(user_email));
				user.setFirstName(rs.getString(user_fname));
				user.setLastName(rs.getString(user_lname));
				user.setValid(rs.getBoolean(user_verify_bit));
				user.setLogin(rs.getBoolean(user_login_bit));
				user.setPassword(rs.getString(user_password));
				user.setContact(rs.getString(user_contact));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return user;
	}
	
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String querry = "select * from  " + table_users;
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			rs = statement.executeQuery();
			
			while(rs.next()) {
				User user = new User();
				user.setUserID(rs.getString(user_id));
				user.setEmail(rs.getString(user_email));
				user.setFirstName(rs.getString(user_fname));
				user.setLastName(rs.getString(user_lname));
				user.setValid(rs.getBoolean(user_verify_bit));
				user.setLogin(rs.getBoolean(user_login_bit));
				user.setPassword(rs.getString(user_password));
				user.setContact(rs.getString(user_contact));
				users.add(user);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return users;
	}
	
	
	public boolean userExists(String email) {
		
		boolean exists = false;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String querry = "select exists(select * from " + table_users + " where " + user_email + " = ?)";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			statement.setString(1, email);
			rs = statement.executeQuery();
			if(rs.next()) {
				exists = (rs.getInt(1) == 1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return exists;
	}
	
	
	public boolean verifyUser(String hashCode) throws ObjectNotFoundException {
		boolean success  = false;
		
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			String query = "update "  +table_users + " set "  +user_verify_bit + " = ? " + " where hashcode = ?";
			
			conn = connectionPool.getConnection();
			
			statement = conn.prepareStatement(query);
			statement.setBoolean(1, true);
			statement.setString(2, hashCode);
			int count = statement.executeUpdate();
			if(count == 1) {
				success = true;
			}
			
		} catch(SQLException e) {
			throw new ObjectNotFoundException("undeclared hash value : " + hashCode);
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return success;
	}
	
	
	public boolean authenticateUser(String email, String password) {
		boolean ret = false;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
		if(userExists(email)) {
			String querry = "select count(" + user_email + ") from " + table_users + " where " + 
							user_email + " = ? and " + 
							user_password + " = ? and " + 
							user_verify_bit + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			statement.setString(1, email);
			statement.setString(2, password);
			statement.setBoolean(3, true);
			System.out.println(statement);
			rs = statement.executeQuery();
			if(rs.next()) {
				if(rs.getInt(1) == 1)
					ret = true;
			}
		}
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return ret;
	}
	
	public boolean loginUser(String email, String password) {
		boolean status = false;
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			String querry = "update " + table_users + " set " + user_login_bit + " = true where " + 
					user_email + " = ? and " + 
					user_password + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			statement.setString(1,email);
			statement.setString(2, password);
			int count = statement.executeUpdate();
			if(count == 1)
				status = true;
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return status;
	}
	
	public boolean logoutUser(String email) {
		boolean status = true;
		PreparedStatement statement = null;
		Connection conn = null;
		try {
			String querry = "update " + table_users + " set " + user_login_bit
					+ " = false where " + user_email + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			statement.setString(1, email);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	public boolean insertUser(User user) throws UserAlreadyExistsException {
		boolean success = false;
		
		Connection conn = null;
		PreparedStatement statement = null;
		
		try {
			String querry = "insert into " + table_users + "("
					+ user_email + ", " 
					+ user_password + ", " 
					+ user_fname + ", " 
					+ user_lname + ", "
					+ user_contact + ", "
					+ user_hashcode
					+ ") values (?, ?, ?, ?, ?, ?)";
			String hashcode = UUID.randomUUID().toString();
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			statement.setString(1, user.getEmailID());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getFirstname());
			statement.setString(4, user.getLastname());
			statement.setString(5, user.getContact());
			statement.setString(6, hashcode);
			int count = statement.executeUpdate();
			if(count == 1) {
				success = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserAlreadyExistsException("User with email_id " + user.getEmailID() + " already exists");
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return success;
	}
	
	public boolean updateUser(User user) {
		boolean success = false;
		
		Connection conn = null;
		PreparedStatement statement = null;
		
        try {
        	conn = connectionPool.getConnection();
            statement = conn
                    .prepareStatement("update users set "+user_fname+"=?, "+user_lname+"=?, "+user_email+"=?" +
                            "where "+ user_id+"=?");
            statement.setString(1, user.getFirstname());
            statement.setString(2, user.getLastname());
            statement.setString(3, user.getEmailID());
            statement.setString(4, user.getUserID());
            int count = statement.executeUpdate();
            if(count == 1) {
            	success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	DbUtils.close(statement);
        	DbUtils.close(conn);
        }
        return success;
    }
	
	public boolean changeUserPassword(String email, String oldPassword, String newPassword) {
		boolean success  = false;
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			String querry = "update " +table_users + " set " + user_password + " = ? where " + user_email + " = ? and " + user_password + " = ?";
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			statement.setString(1, newPassword);
			statement.setString(2, email);
			statement.setString(2, oldPassword);
			int count = statement.executeUpdate();
			if(count == 1) {
				success = true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return success;
	}
	
	public UserAddress getUserAddressByAddressID(String addressID) {
		UserAddress addr = null;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from " + table_users_addr + " where " + addr_address_id + " = ?";
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, addressID);
			rs = statement.executeQuery();
			
			if(rs.next()) {
				addr = new UserAddress();
				addr.setUserAddrID(rs.getString(addr_address_id));
				addr.setUserID(rs.getString(addr_user_id));
				addr.setName(rs.getString(addr_name));
				addr.setStreet1(rs.getString(addr_street1));
				addr.setStreet2(rs.getString(addr_street2));
				addr.setCity(rs.getString(addr_city));
				addr.setState(rs.getString(addr_state));
				addr.setCountry(rs.getString(addr_country));
				addr.setPincode(rs.getString(addr_pincode));
				addr.setContactNo(rs.getString(addr_contact));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return addr;
	}
	
	public List<UserAddress> getUserAddresses(String userID) {
		List<UserAddress> addresses = new ArrayList<UserAddress>();
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from "  +table_users_addr + " where " + addr_user_id + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userID);
			rs = statement.executeQuery();
			
			UserAddress addr = new UserAddress();
			while(rs.next()) {
				addr.setUserAddrID(rs.getString(addr_address_id));
				addr.setUserID(rs.getString(addr_user_id));
				addr.setName(rs.getString(addr_name));
				addr.setStreet1(rs.getString(addr_street1));
				addr.setStreet2(rs.getString(addr_street2));
				addr.setCity(rs.getString(addr_city));
				addr.setState(rs.getString(addr_state));
				addr.setCountry(rs.getString(addr_country));
				addr.setPincode(rs.getString(addr_pincode));
				addr.setContactNo(rs.getString(addr_contact));
				addresses.add(addr);
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return addresses;
	}
	
	public boolean insertUserAddress(String userID, UserAddress address) {
		boolean success = false;
		Connection conn = null;
		PreparedStatement statement = null;
		
		try {
			String query = "insert into " + table_users_addr + "("
					+ addr_user_id + ", "
					+ addr_name + ", " 
					+ addr_street1 + ", " 
					+ addr_street2 + ", " 
					+ addr_city + ", "
					+ addr_state + ", "
					+ addr_country + ", "
					+ addr_pincode + ", "
					+ addr_contact
					+ ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, address.getName());
			statement.setString(3, address.getStreet1());
			statement.setString(4, address.getStreet2());
			statement.setString(5, address.getCity());
			statement.setString(6, address.getState());
			statement.setString(7, address.getCountry());
			statement.setString(8, address.getPincode());
			statement.setString(9, address.getContactNo());
			int count = statement.executeUpdate();
			if(count == 1) {
				success = true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		
		
		return success;
	}
}
