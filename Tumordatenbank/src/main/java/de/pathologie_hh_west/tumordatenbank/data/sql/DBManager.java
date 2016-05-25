package de.pathologie_hh_west.tumordatenbank.data.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Driver;

public class DBManager {

	private static DBManager manager;
	
	private Connection connection;
	private Statement statement;
	
	private DBManager() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/mydb";
		String login = "java";
		String password = "geheim";
//		char[] c = {};
//		password = String.valueOf(c);
		
		DriverManager.registerDriver(new Driver());
		Connection connection = DriverManager.getConnection(url, login, password);
		connection.setAutoCommit(false);
		connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		connection.setReadOnly(true);
		System.out.println("Connection opened!");
		
		this.connection = connection;
		this.statement = this.connection.createStatement();
	}
	
	public static DBManager getInstance() throws SQLException {
		if (manager == null) {
			manager = new DBManager();
		}
		return manager;
	}
	
	public boolean isConnected()  {
		try {
			return this.connection.isValid(2);
		} catch (SQLException e) {
			return false;
		}
	}
	
	Connection getConnection() throws SQLException {
		return this.connection;
	}
	
	void setAutoCommitOff() throws SQLException {
		this.connection.setAutoCommit(false);
	}
	
	void commit() throws SQLException {
		this.getConnection().commit();
	}
	
	void rollback() throws SQLException {
		this.getConnection().rollback();
	}
	
	Statement getStatement() {
		return this.statement;
	}
	
	PreparedStatement prepareStatement(String statement) throws SQLException {
		return this.getConnection().prepareStatement(statement);
	}
	
	public static void closeConnection() throws DataAccessException {
		if (manager != null && manager.connection != null) {
			try {
				manager.connection.setReadOnly(false);
				manager.statement.close();
				manager.connection.close();
				System.out.println("Connection closed!");
			} catch (SQLException e) {
				throw new DataAccessException(e.getMessage());
			}
		}
	}
	
	public static void openConnection() throws DataAccessException {
		try {
			if (getInstance().connection.isClosed()) {
				manager = new DBManager();
			}
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}
	}
	
}
