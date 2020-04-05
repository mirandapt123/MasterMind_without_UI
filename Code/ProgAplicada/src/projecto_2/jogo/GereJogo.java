package projecto_2.jogo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;


public class GereJogo {

	/**
	 * Verifica a quantidade de jogos.
	 * 
	 * @param aComando
	 * @param aSt
	 * @return int com a quantidade de jogos.
	 */
	public int verificaQuantidadeJogo(String aComando, Statement aSt) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(" SELECT count(*) FROM jogo " + aComando + ";");

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
	 * Regista um jogo
	 * 
	 * @param aJogo
	 * @param aSt
	 * @return false se ocorreu um erro, true se foi registado com sucesso.
	 */
	public boolean registaJogo(Jogo aJogo, Statement aSt) {

        int idJogo = verificaQuantidadeJogo("", aSt);
        idJogo ++;
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date parsedDate = dateFormat.parse(aJogo.getData());
			Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
			
			aSt.execute("Insert INTO jogo (jo_id, u_id, jo_data) Values ("+idJogo+","+aJogo.getuId()+", '"+timestamp+"')");
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		} catch (ParseException pe) {
			System.out.println(pe);
			return false;
		}
	}
	
	/**
	 * Lista todos os jogos de um utilizador.
	 * @param aLimite
	 * @param aSt
	 * @return String com os jogos.
	 */
	public String listaJogo(String aLimite, String aComando, Statement aSt) {

		ResultSet rs = null;
		
		try {

			rs = aSt.executeQuery(" SELECT * FROM jogo "+aComando+" limit 10 offset " + aLimite);

			if (rs == null) {
				return "\nOcorreu um erro a mostrar os seus jogos!\n";
			} else if (!rs.isBeforeFirst()) {
				return "\nNão tem nenhum jogo na base de dados!\n";
			} else {
				Vector<Jogo> listaJogo;
				listaJogo = new Vector<Jogo>();
				Jogo aDados;
				while (rs != null && rs.next()) {
					aDados = new Jogo(rs.getInt(1), rs.getInt(2), rs.getString(3));
					listaJogo.addElement(aDados);
				}
				return listaJogo(listaJogo);
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro a mostrar as certificações!\n";
		}
	}

	/**
	 * @param listaJogo
	 * @return dados do vector da lista de jogos em string.
	 */
	private String listaJogo(Vector<Jogo> listaJogo) {
		if (listaJogo != null && listaJogo.size() > 0) {
			String listagem = "";
			Enumeration<Jogo> en = listaJogo.elements();
			while (en.hasMoreElements()) {
				listagem = listagem + en.nextElement();
			}
			return listagem;
		}
		return null;
	}
	
	
}
