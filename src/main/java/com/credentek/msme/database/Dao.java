package com.credentek.msme.database;

import java.sql.Connection;

public interface Dao {
	public Connection getConnection(String methodName) throws Exception;

}
