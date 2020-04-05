package projecto_2.informGerais;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

public class GereNotificacao {

	/**
	 * Verifica a quantidade de notificações.
	 * @param aComando
	 * @param aSt
	 * @return int com a quantidade de notificações.
	 */
	public int verificaQuantidadeNoti(String aComando, Statement aSt) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(" SELECT count(*) FROM notificacao " + aComando + ";");

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
	 * obtem id da ultima notificacao.
	 * @param aSt
	 * @return int com o id da ultima notificacao.
	 */
	public int obtemId(Statement aSt) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(" SELECT n_id FROM notificacao order by n_id desc;");

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
	 * Regista notificações para os administradores
	 * @param aMsg
	 * @param aSt
	 * @return false se ocorreu um erro, true se foi registada com sucesso.
	 */
	public boolean registaNotificacaoAdmin(String aMsg, Statement aSt) {
		ResultSet rs = null;
		int contador = 0, tamanho = 0;
		int idNotificacao = 0;

		try {

			rs = aSt.executeQuery("select count(*) from utilizador where u_tipo = 'A' and u_estado = true");

			if (rs == null) {
				return false;
			} else {
				while (rs.next()) {
					tamanho = rs.getInt(1);
				}
			}

			int[] idUser = new int[tamanho];

			rs = aSt.executeQuery("select * from utilizador where u_tipo = 'A' and u_estado = true");

			if (rs == null) {
				return false;
			} else {
				while (rs.next()) {
					idUser[contador] = rs.getInt(1);
					contador++;
				}
			}

			aSt.execute("Insert INTO notificacao (n_mensagem) Values ('" + aMsg + "')");
			idNotificacao = obtemId(aSt);

			for (int i = 0; i < idUser.length; i++) {
				aSt.execute("Insert INTO utilizador_notificacao (n_id, u_id) Values (" + idNotificacao + ","
						+ idUser[i] + ")");
			}
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}
	
	/**
	 * Elimina notificação que não está em uso.
	 * @param aSt
	 * @return true se eliminar com sucesso, false se não.
	 */
	public boolean eliminaNotificacao(Statement aSt) {

		try {
			aSt.execute("delete from notificacao where n_id not in (select n_id from utilizador_notificacao)");
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	/**
	 * Lista notificações
	 * @param aLimite
	 * @param aComando
	 * @param aSt
	 * @param aMsg
	 * @return String com as notificações.
	 */
	public String listaNotificacao(String aLimite, String aComando, Statement aSt, String aMsg) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery("select * from notificacao " + aComando + " limit 10 offset " + aLimite);

			if (rs == null) {
				return "\nO comando para mostrar as notificações não pode ser executado neste momento!\n";
			} else if (!rs.isBeforeFirst()) {
				return aMsg;
			} else {
				Vector<Notificacao> listaNotificacao;
				listaNotificacao = new Vector<Notificacao>();
				Notificacao aDados;
				while (rs.next()) {
					aDados = new Notificacao(rs.getString(2));
					listaNotificacao.addElement(aDados);
				}
				return listaNoti(listaNotificacao);
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao nível de SQL, a tabela de notificações não foi encontrada.\n" + e + "\n";
		}
	}

	/**
	 * Retira uma notificação.
	 * @param aIdUser
	 * @param aSt
	 * @return false se ocorreu um erro, true se não.
	 */
	public boolean retiraNotificacao(int aIdUser, Statement aSt) {

		try {

			aSt.execute("delete from utilizador_notificacao where u_id = " + aIdUser);

			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Lista o vector com as notificações
	 * @param listaNotificacao
	 * @return String com a lista das notificações.
	 */
	private String listaNoti(Vector<Notificacao> listaNotificacao) {
		if (listaNotificacao != null && listaNotificacao.size() > 0) {
			String listagem = "";
			Enumeration<Notificacao> en = listaNotificacao.elements();
			while (en.hasMoreElements()) {
				listagem = listagem + en.nextElement();
			}
			return listagem;
		}
		return null;
	}

}
