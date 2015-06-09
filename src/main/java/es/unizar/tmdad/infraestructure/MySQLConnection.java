package es.unizar.tmdad.infraestructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
	private final static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	static String APP_NAME = System.getenv("OPENSHIFT_APP_NAME");
	static String DB_HOST = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
	static String DB_PORT = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
	static String DB_USER = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
	static String DB_PASSWD = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");

	private final static String DRIVER_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + APP_NAME;

	static {

		try {
			Class.forName(DRIVER_CLASS_NAME);
		} catch (ClassNotFoundException e) {
			e.printStackTrace(System.err);
		}

	}

	private MySQLConnection() {}

	public final static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DRIVER_URL, DB_USER, DB_PASSWD);
	}
}
