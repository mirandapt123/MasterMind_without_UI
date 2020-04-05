package projecto_2.utilizador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GereAdministrador {

	/**
	 * Lista dados de um utilizador do tipo administrador.
	 * 
	 * @param aLogin
	 * @param aSt
	 * @return String com os dados do administrador
	 */
	public String listaDadosAdmin(String aLogin, Statement aSt) {

		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(" SELECT * FROM utilizador where u_login = '" + aLogin + "';");

			if (rs == null) {
				System.out.println("O comando para encontrar mostrar os seus dados não pode ser executado!");
			} else {
				while (rs.next()) {
					Utilizador aDados = new Utilizador(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(7), rs.getBoolean(5), rs.getString(8).charAt(0), rs.getBoolean(6));

					return listaAdmin(aDados);
				}
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao nível de SQL, a tabela de utilizador não foi encontrada.\n" + e + "\n";
		}
		return null;
	}
	
	/**
	 * Faz um 'update' aos dados do administrador na base de dados.
	 * @param aComando
	 * @param aLogin
	 * @param aSt
	 * @return String com mensagem personalizada que dá uma informação ao utilizador.
	 */
	public String actualizaDadosAdmin(String aComando, String aLogin, Statement aSt) {
		int valor = 0;

		try {

			valor = aSt.executeUpdate(
					" UPDATE utilizador SET " + aComando + " where u_login = '" + aLogin + "';");

			if (valor == 0) {
				return "\nOcorreu um erro a actualizar os dados.";
			} else {
				return "\nDados actualizados com sucesso.";
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao nível de SQL, a tabela de utilizadores não foi encontrada.\n" + e + "\n";
		}
	}

	/**
	 * Lista um administrador
	 * 
	 * @param aAdmin
	 * @return String com os dados do objecto administrador.
	 */
	private String listaAdmin(Utilizador aAdmin) {
		String listagem = "";
		listagem += aAdmin;
		return listagem;
	}

	/**
	 * Verifica se existem gestores na base de dados.
	 * 
	 * @param aSt
	 * @param aComando
	 * @return número de gestores na base de dados.
	 */
	public int verificaListaVazia(Statement aSt, String aComando) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(" SELECT count(*) FROM utilizador where u_tipo='A' " + aComando);

			if (rs == null) {
				return 0;
			} else {
				while (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			return 0;
		}
		return 0;
	}

	/**
	 * Regista um administrador na base de dados
	 * 
	 * @param aAdministrador
	 * @param aSt
	 * @return true se for bem sucedido, false se não.
	 */
	public boolean registaAdministrador(Utilizador aAdministrador, Statement aSt) {

		if (enviaDadosAdmin(aAdministrador, aSt)) {
			return true;
		}
		return false;
	}

	/**
	 * Introduz um administrador na base de dados.
	 * @param aGestor
	 * @param aSt
	 * @return true se for bem sucedido, false se não.
	 */
	private boolean enviaDadosAdmin(Utilizador aAdministrador, Statement aSt) {

		try {
			aSt.execute(
					"Insert INTO utilizador (U_login, u_name, U_EMAIL, U_PASSWORD, U_ESTADO, U_ESTADOREPROVACAO, U_TIPO)"
							+ "VALUES ('" + aAdministrador.getLogin() + "','" + aAdministrador.getNome() + "','"
							+ aAdministrador.getEmail() + "','" + aAdministrador.getPassword() + "',"
							+ aAdministrador.getEstado() + "," + aAdministrador.getEstadoReprovacao() + ",'"
							+ aAdministrador.getTipo() + "');");
			return true;
		} catch (SQLException e) {
			System.out.println("!! SQL Exception !!\n" + e + "\n");
			return false;
		}
	}

}
