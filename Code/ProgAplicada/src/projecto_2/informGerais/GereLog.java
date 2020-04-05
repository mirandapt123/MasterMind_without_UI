package projecto_2.informGerais;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Vector;

public class GereLog {
	
	/**
	 * Verifica a quantidade de logs existentes na base de dados.
	 * @param aComando
	 * @param aSt
	 * @return número de logs existentes na base de dados.
	 */
	public int verificaQuantidadeLog(String aComando, Statement aSt) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(" SELECT count(*) FROM logs " + aComando + ";");

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
	 * Regista um log na base de dados.
	 * @param aLog
	 * @param aSt
	 * @return true se não ocorrer um erro, false se ocorrer.
	 */
	public boolean criaLog(Log aLog, Statement aSt) {

		try {
			String startDate = aLog.getData();
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date = sdf1.parse(startDate);
			java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());

			aSt.execute(
					"Insert INTO logs (l_data_log, l_hora, l_utilizador, l_accao)"
							+ " VALUES ('" + sqlStartDate + "','" + aLog.getHora() + "','"
							+ aLog.getUtilizador() + "','" + aLog.getAccao() + "');");

			return true;
		} catch (SQLException e) {
			return false;	
		} catch (ParseException pe) {
			return false;
		}
	}
	
	/**
	 * Lista os logs.
	 * @param aLimite
	 * @param aComando
	 * @param aSt
	 * @param aMsg
	 * @return String com os dados dos logs.
	 */
	public String listaLog(String aLimite, String aComando, Statement aSt, String aMsg) {

		ResultSet rs = null;

		try {

			rs = aSt.executeQuery("select * from logs " + aComando + " limit 10 offset " + aLimite);

			if (rs == null) {
				return "O comando para encontrar mostrar os logs não pode ser executado!";
			} else if (!rs.isBeforeFirst()) {
				return aMsg;
			} else {
				Vector<Log> listaLog;
				listaLog = new Vector<Log>();
				while (rs.next()) {
					Log log = new Log(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
					listaLog.addElement(log);
				}
				return listaLog(listaLog);
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao nível de SQL, a tabela de logs não foi encontrada.\n" + e + "\n";
		}
	}
	
	/**
	 * @param listaLog
	 * @return String com os dados do vector dos logs.
	 */
	private String listaLog(Vector<Log> listaLog) {
		if (listaLog != null && listaLog.size() > 0) {
			String listagem = "";
			Enumeration<Log> en = listaLog.elements();
			while (en.hasMoreElements()) {
				listagem = listagem + en.nextElement();
			}
			return listagem;
		}
		return null;
	}
	
}
