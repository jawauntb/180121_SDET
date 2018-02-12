package com.trms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connections {
	public static Connection getConnection() throws SQLException {
		String[] props = System.getenv("PODBPROPS").split(";");
		try {
			Class.forName(props[0]);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return DriverManager.getConnection(
				props[1],
				props[2],
				props[3]
		);
	}
}