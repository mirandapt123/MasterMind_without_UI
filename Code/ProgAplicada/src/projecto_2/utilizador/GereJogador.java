package projecto_2.utilizador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Vector;

public class GereJogador {

	/**
	 * @param aJogador
	 * @param aSt
	 * @return true se o jogador foi adicionada com sucesso, se ocorreu um erro
	 *         retorna false.
	 */
	public boolean registaJogador(Jogador aJogador, Statement aSt) {

		if (enviaDadosJogador(aJogador, aSt)) {
			return true;
		}

		return false;
	}

	/**
	 * Insere um jogador na base de dados.
	 * 
	 * @param aJogador
	 * @param aSt
	 * @return true se os dados forem inseridos, false se não forem.
	 */
	private boolean enviaDadosJogador(Jogador aJogador, Statement aSt) {
		ResultSet rs = null;
		int idUser = 0;

		try {
			aSt.execute(
					"Insert INTO utilizador (u_login, u_name, U_EMAIL, U_PASSWORD, U_ESTADO, U_ESTADOREPROVACAO, U_TIPO)"
							+ "VALUES ('" + aJogador.getLogin() + "','" + aJogador.getNome() + "','"
							+ aJogador.getEmail() + "','" + aJogador.getPassword() + "'," + aJogador.getEstado() + ","
							+ aJogador.getEstadoReprovacao() + ",'" + aJogador.getTipo() + "');");

			rs = aSt.executeQuery(
					"SELECT u_id FROM utilizador where u_login = '" + aJogador.getLogin() + "';");

			if (rs == null) {
				System.out.println("O comando para encontrar os dados genéricos da aplicação não pode ser executado!");
			} else {
				while (rs.next()) {
					idUser = rs.getInt(1);
				}
			}

			String s = aJogador.getTempoTotal();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			long ms = sdf.parse(s).getTime();
			Time t = new Time(ms);

			aSt.execute("Insert INTO jogador (u_id, j_numjogos, j_numvitorias, j_tempototal)" + "VALUES (" + idUser
					+ "," + aJogador.getNumJogos() + "," + aJogador.getNumVitorias() + ",'"+t+"');");

			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}  catch (ParseException pe) {
			return false;
		}
	}
	
	/**
	 * Lista todos os jogadores.
	 * @param aLimite
	 * @param aComando
	 * @param aSt
	 * @param aMsg
	 * @return String com lista de jogadores
	 */
	public String listaJogTotais(String aLimite, String aComando, Statement aSt, String aMsg) {

		ResultSet rs = null;

		try {

			rs = aSt.executeQuery("select * from utilizador u, jogador j " + aComando + " limit 10 offset " + aLimite);

			if (rs == null) {
				return "O comando para mostrar todos os jogadores não pode ser executado!";
			} else if (!rs.isBeforeFirst()) {
				return aMsg;
			} else {
				Vector<Jogador> listaJogador;
				listaJogador = new Vector<Jogador>();
				while (rs.next()) {
					Jogador aDados = new Jogador(rs.getInt(1), rs.getString(2), rs.getString(3), "*****",
							rs.getBoolean(5), rs.getString(7), rs.getBoolean(6),rs.getInt(10),rs.getInt(11),rs.getString(12));
					listaJogador.addElement(aDados);
				}
				return listaJog(listaJogador);
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao nível de SQL, a tabela de utilizador não foi encontrada.\n" + e + "\n";
		}
	}
	
	/**
	 * Lista todos os jogadores.
	 * @param aLimite
	 * @param aComando
	 * @param aSt
	 * @param aMsg
	 * @return String com lista de jogadores
	 */
	public String listaJogTotais1(String aLimite, String aComando, Statement aSt, String aMsg) {

		ResultSet rs = null;

		try {

			rs = aSt.executeQuery("select * from utilizador u, jogador j " + aComando + " limit 10 offset " + aLimite);

			if (rs == null) {
				return "O comando para mostrar todos os jogadores não pode ser executado!";
			} else if (!rs.isBeforeFirst()) {
				return aMsg;
			} else {
				Vector<Jogador> listaJogador;
				listaJogador = new Vector<Jogador>();
				while (rs.next()) {
					Jogador aDados = new Jogador(rs.getInt(1), rs.getString(2), "*****", "*****",
							rs.getBoolean(5), "*****", rs.getBoolean(6),rs.getInt(10),rs.getInt(11),"*****");
					listaJogador.addElement(aDados);
				}
				return listaJog(listaJogador);
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao nível de SQL, a tabela de utilizador não foi encontrada.\n" + e + "\n";
		}
	}
	
	/**
	 * Mostra os dados do jogador
	 * @param aLogin
	 * @param aSt
	 * @return String com os dados de um jogador
	 */
	public String listaDadosJogador(String aLogin, Statement aSt) {

		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(
					" SELECT * FROM utilizador u, jogador j where u.u_id = j.u_id and u_login = '"
							+ aLogin + "';");

			if (rs == null) {
				return "O comando para encontrar mostrar os seus dados não pode ser executado!";
			} else {
				while (rs.next()) {
					Jogador aDados = new Jogador(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getBoolean(5), rs.getString(7), rs.getBoolean(6),rs.getInt(10),rs.getInt(11),rs.getString(12));

					return listaJog(aDados);
				}
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao nível de SQL, a tabela de utilizador não foi encontrada.\n" + e + "\n";
		}
		return null;
	}
	
	/**
	 * Lista um objecto do tipo jogador.
	 * @param aJogador
	 * @return String com dados do objecto.
	 */
	private String listaJog(Jogador aJogador) {
		String listagem = "";
		listagem += aJogador;
		return listagem;
	}

	/**
	 * Lista um vector do tipo técnico.
	 * @param listaTecnico
	 * @return String com os dados do vector.
	 */
	private String listaJog(Vector<Jogador> listaJogador) {
		if (listaJogador != null && listaJogador.size() > 0) {
			String listagem = "";
			Enumeration<Jogador> en = listaJogador.elements();
			while (en.hasMoreElements()) {
				listagem = listagem + en.nextElement();
			}
			return listagem;
		}
		return null;
	}
	
	/**
	 * Obtem o número de jogadores na base de dados.
	 * @param aSt
	 * @param aComando
	 * @return número de jogadores.
	 */
	public int verificaListaVazia(Statement aSt, String aComando) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(" SELECT count(*) FROM jogador " + aComando);

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
	 * Faz update aos dados do jogador
	 * @param aComando
	 * @param aLogin
	 * @param aSt
	 * @return String com mensagem.
	 */
	public String actualizaDadosJogador(String aComando, String aLogin, Statement aSt) {
		int valor = 0, id = 0;

		try {

			ResultSet rs = null;

			rs = aSt.executeQuery(
					"select j.u_id from utilizador u, jogador j where u.u_id=j.u_id and u_login = '"
							+ aLogin + "'");

			if (rs == null) {
				return "O comando para o id do jogador não pode ser executado!";
			} else {
				while (rs.next()) {
					id = rs.getInt(1);
				}
			}

			valor = aSt.executeUpdate(" UPDATE jogador SET " + aComando + " where u_id = " + id + ";");

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
	 * Actualiza o tempo total do jogador
	 * @param aTempo
	 * @param aLogin
	 * @param aSt
	 * @return String com mensagem.
	 */
	public boolean actualizaTempoJogador(long aTempo, String aLogin, Statement aSt) {
		int valor = 0, id = 0;

		try {

			ResultSet rs = null;
			rs = aSt.executeQuery(
					"select j.u_id from utilizador u, jogador j where u.u_id=j.u_id and u_login = '"
							+ aLogin + "'");

			if (rs == null) {
				return false;
			} else {
				while (rs.next()) {
					id = rs.getInt(1);
				}
			}
			
			aTempo = aTempo / 1000;
			
			valor = aSt.executeUpdate(" UPDATE jogador SET j_tempototal = date_add(j_tempototal, interval "+aTempo+" second) where u_id = " + id + ";");

			if (valor == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			return false;
		} 
	}
	
	/**
	 * Actualiza o número de jogos do jogador
	 * @param aLogin
	 * @param aSt
	 * @return String com mensagem.
	 */
	public boolean actualizaNumeroJogos(String aLogin, Statement aSt) {
		int valor = 0, id = 0;

		try {

			ResultSet rs = null;

			rs = aSt.executeQuery(
					"select j.u_id from utilizador u, jogador j where u.u_id=j.u_id and u_login = '"
							+ aLogin + "'");

			if (rs == null) {
				return false;
			} else {
				while (rs.next()) {
					id = rs.getInt(1);
				}
			}

			valor = aSt.executeUpdate(" UPDATE jogador SET j_numjogos = j_numjogos + 1 where u_id = " + id + ";");

			if (valor == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Actualiza o número de vitórias do jogador
	 * @param aLogin
	 * @param aSt
	 * @return String com mensagem.
	 */
	public boolean actualizaNumeroVitorias(String aLogin, Statement aSt) {
		int valor = 0, id = 0;

		try {

			ResultSet rs = null;

			rs = aSt.executeQuery(
					"select j.u_id from utilizador u, jogador j where u.u_id=j.u_id and u_login = '"
							+ aLogin + "'");

			if (rs == null) {
				return false;
			} else {
				while (rs.next()) {
					id = rs.getInt(1);
				}
			}

			valor = aSt.executeUpdate(" UPDATE jogador SET j_numvitorias = j_numvitorias + 1 where u_id = " + id + ";");

			if (valor == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Envia tempo total acumulado do jogador
	 * @param aLogin
	 * @param aSt
	 * @return String com mensagem.
	 */
	public String obtemTempoAcumulado(String aLogin, Statement aSt) {

		try {

			ResultSet rs = null;

			rs = aSt.executeQuery(
					"select j.j_tempototal from utilizador u, jogador j where u.u_id=j.u_id and u_login = '"
							+ aLogin + "'");

			if (rs == null) {
				return null;
			} else {
				while (rs.next()) {
					String tempo = rs.getString(1);
					return tempo;
				}
			}
		} catch (SQLException e) {
			return null;
		}
		return null;
	}
	
	/**
	 * Envia número de vitórias do jogador
	 * @param aLogin
	 * @param aSt
	 * @return String com mensagem.
	 */
	public int obtemNumVitorias(String aLogin, Statement aSt) {

		try {

			ResultSet rs = null;

			rs = aSt.executeQuery(
					"select j.j_numvitorias from utilizador u, jogador j where u.u_id=j.u_id and u_login = '"
							+ aLogin + "'");

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
	 * Envia o login do jogador com mais vitorias
	 * @param aSt
	 * @return String com mensagem.
	 */
	public String jogadorComMaisVit(Statement aSt) {

		try {

			ResultSet rs = null;

			rs = aSt.executeQuery(
					"select u.u_login from utilizador u, jogador j where u.u_id=j.u_id order by j.j_numvitorias desc;");

			if (rs == null) {
				return "";
			} else {
				while (rs.next()) {
					return rs.getString(1);
				}
			}
		} catch (SQLException e) {
			return "";
		}
		return "";
	}

}
