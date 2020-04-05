package projecto_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GereBaseDados {

	private Statement st = null;
	private Connection conn = null;

	/**
	 * Inicia a ligacao da base de dados
	 * @param aDBPorto
	 * @param aDBUrl
	 * @param aDBUser
	 * @param aDBPassword
	 * @param aDBName
	 * @return null se ocorrer um erro, statement se n�o. 
	 */
	public Statement iniciaBaseDados(String aDBPorto, String aDBUrl, String aDBUser, String aDBPassword, String aDBName) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://" + aDBUrl + ":" + aDBPorto + "/" + aDBName+"?useSSL=false", aDBUser,
					aDBPassword);
			st = conn.createStatement();

			return st;

		} catch (SQLException e) {
			System.out.println("\nOcorreu o seguinte problema a n�vel de SQL:\n" + e);
			System.out.println("\n DEVIDO � OCORR�NCIA DO PROBLEMA INDICADO EM CIMA, O PROGRAMA IR� TERMINAR!\n");
			return null;
		} catch (ClassNotFoundException e) {
			System.out.println("!! Class Not Found. Unable to load Database Drive !!\n" + e);
			System.out.println("\n DEVIDO � OCORR�NCIA DO PROBLEMA INDICADO EM CIMA, O PROGRAMA IR� TERMINAR!\n");
			return null;
		} catch (IllegalAccessException e) {
			System.out.println("!! Illegal Access !!\n" + e);
			System.out.println("\n DEVIDO � OCORR�NCIA DO PROBLEMA INDICADO EM CIMA, O PROGRAMA IR� TERMINAR!\n");
			return null;
		} catch (InstantiationException e) {
			System.out.println("!! Class Not Instanciaded !!\n" + e);
			System.out.println("\n DEVIDO � OCORR�NCIA DO PROBLEMA INDICADO EM CIMA, O PROGRAMA IR� TERMINAR!\n");
			return null;
		}
	}

	/**
	 * Fecha a liga��o � base de dados.
	 */
	public void fechaBaseDados() {
		if (st != null) {
			try {
				st.close();
			} catch (Exception e) {
				System.out.println("!! Exception returning statement !!\n" + e);
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				System.out.println("!! Exception closing DB connection !!\n" + e);
			}
		}
	}

}