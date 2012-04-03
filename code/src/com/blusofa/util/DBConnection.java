package com.blusofa.util;


import java.sql.*;
import java.util.ArrayList;

public abstract class DBConnection {	
	protected static String account = "ccs108elui";
	protected static String password = "uweidoob";
	protected static String server = "jdbc:mysql://mysql-user-master.stanford.edu";
	protected static String database = "c_cs108_elui"; 
//	protected static String account = "ccs108hspinks";
//	protected static String password = "oshahque";
//	protected static String server = "jdbc:mysql://mysql-user-master.stanford.edu";
//	protected static String database = "c_cs108_hspinks"; 
	
	protected Connection con;
	protected Statement stmt;
	protected String table_name;
	protected String id_col_name;
	protected String input_col_names;
	protected int id = 0;
	//protected static final String QUIZ_ID_COL = "quiz_id";
	//protected static final String INPUT_COLS = "(quiz_name, creator_id, description, question_ids, genre_ids, num_times_taken, date_created)";
	
	public static boolean isNotEmpty(ResultSet rs) throws SQLException {
		if(rs.first()) {
			rs.beforeFirst();
			return true;
		}
		return false;
	}
	
	public void test(){
		if (stmt == null){
			System.out.println("stmt is null");
		}
	}
	
	private DBConnection() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(server, account, password);
			stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
		} catch (SQLException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	protected DBConnection(String table_name, String id_col_name, String input_col_names) {
		this();
		this.table_name = table_name;
		this.id_col_name = id_col_name;
		this.input_col_names = input_col_names;
	}
	
	/*
	 * replaces special characters in a string to SQL friendly escape sequences
	 */
	protected static String convertToSQLAcceptable(String str){
		if(str == null) return null;
		str = str.replace("'", "");
		str = str.replace("\n", "");
		str = str.replace("\b", "");
		str = str.replace("\r", "");
		str = str.replace("\t", "");
		//str = str.replace("'", "\'");
		str = str.replace("\"", "");
		str = str.replace("\n", "");
		str = str.replace("\b", "");
		str = str.replace("\r", "");
		str = str.replace("\t", "");
		str = str.replace("\\", "");
		//str = str.replace("%", "");
		//str = str.replace("_", "");
//		str = str.replace("'", "\\'");
//		str = str.replace("\n", "\\n");
//		str = str.replace("\b", "\\b");
//		str = str.replace("\r", "\\r");
//		str = str.replace("\t", "\\t");
//		//str = str.replace("'", "\'");
//		str = str.replace("\"", "\\\"");
//		str = str.replace("\n", "\n");
//		str = str.replace("\b", "\b");
//		str = str.replace("\r", "\r");
//		str = str.replace("\t", "\t");
//		str = str.replace("\\", "\\\\");
//		str = str.replace("%", "\\%");
//		str = str.replace("_", "\\_");
		return str;
	}
	
	/**
	 * 
	 * @param id
	 * @param col_name
	 * @param value
	 * @throws SQLException
	 */

	public void setAttribute(String id, String col_name, String value) throws SQLException {
		value = convertToSQLAcceptable(value);
		System.out.println("UPDATE " + table_name + " SET " + col_name + " = '" + value + "' WHERE " + id_col_name + " = " + id);
		stmt.executeUpdate("UPDATE " + table_name + " SET " + col_name + " = '" + value + "' WHERE " + id_col_name + " = " + id);
	}
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public void close() throws SQLException {
		stmt.close();
		con.close();
	}
	
	/**
	 * 
	 * @param id
	 * @return a result set
	 * @throws SQLException
	 */
	public ResultSet getRowInfo(String id) throws SQLException {
		System.out.println("SELECT * FROM " + table_name + " WHERE " + id_col_name + " = " + id);
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE " + id_col_name + " = " + id);
	}
	
	/**
	 * @return id just added
	 * @param attributes
	 * @throws SQLException 
	 */
	public String addEntry(String attributes) throws SQLException {
		System.out.println("INSERT INTO " + table_name + " " + input_col_names + " VALUES (" + attributes+")");
		int insert_id = stmt.executeUpdate("INSERT INTO " + table_name + " " + input_col_names + " VALUES (" + attributes+")",Statement.RETURN_GENERATED_KEYS);
		ResultSet res = stmt.executeQuery("SELECT * FROM  "+table_name);
		String lastLine ="";
		while (res.next())
        {
                
                lastLine = res.getString(id_col_name);
                //System.out.println(lastLine);
        }


		//System.out.println(insert_id +" asad");
		//return Integer.toString(insert_id);
		return  lastLine;
	}
	
	/**
	 * 
	 * @param id
	 * @param col_name
	 * @return attribute
	 * @throws SQLException
	 */
	public String getAttribute(String id, String col_name) throws SQLException {
		ResultSet rs = stmt.executeQuery("SELECT * FROM " + table_name + " WHERE " + id_col_name + " = '" + id + "'");
		if(rs.next()) {
			return rs.getString(col_name);
		}
		return null;
	}
	
	/**
	 * 
	 * @param id
	 * @throws SQLException 
	 */
	public void removeItem(String id) throws SQLException{
		stmt.executeUpdate("DELETE FROM " + table_name + " WHERE " + id_col_name + " = " + id);
	}
	
	public void executeUpdate(String instruction) {
		try {
			stmt.executeUpdate(instruction);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public ResultSet executeQuery(String instruction) throws SQLException  {
			return stmt.executeQuery(instruction);	
	}
	
	public int generateId() {
		id++;
		return id;
	}
	
	
	
	


}
