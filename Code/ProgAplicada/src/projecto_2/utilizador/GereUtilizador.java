package projecto_2.utilizador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import projecto_1.utilizadores.Utilizador;

public class GereUtilizador {

	/**
	 * Verifica quantos utilizadores existem na base de dados.
	 * 
	 * @param aSt
	 * @param aComando
	 * @return n�mero de utilizadores na base de dados.
	 */
	public int verificaListaVazia(Statement aSt, String aComando) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(" SELECT count(*) FROM utilizador" + aComando);

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
	 * Verifica se um login existe
	 * 
	 * @param aLogin
	 * @param aSt
	 * @return false se existir, true se n�o.
	 */
	public boolean verificaLogin(String aLogin, Statement aSt) {
		ResultSet rs = null;
		int valor = 0;
		try {

			rs = aSt.executeQuery(" SELECT count(*) FROM utilizador where u_login = '" + aLogin + "';");

			if (rs == null) {
				return false;
			} else {
				while (rs.next()) {
					valor = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			return false;
		}

		if (valor == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Verifica se um email j� existe na base de dados
	 * 
	 * @param aEmail
	 * @param aSt
	 * @return false se j�, true se n�o.
	 */
	public boolean verificaEmail(String aEmail, Statement aSt) {
		ResultSet rs = null;
		int valor = 0;
		try {

			rs = aSt.executeQuery(" SELECT count(*) FROM utilizador where u_email = '" + aEmail + "';");

			if (rs == null) {
				return false;
			} else {
				while (rs.next()) {
					valor = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			return false;
		}

		if (valor == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Verifica a autentica��o de um utilizador
	 * 
	 * @param aLogin
	 * @param aPassword
	 * @param aSt
	 * @return true se bem sucedida, false se n�o.
	 */
	public boolean verificaAutenticacao(String aLogin, String aPassword, Statement aSt) {
		ResultSet rs = null;
		int valor = 0;
		try {

			rs = aSt.executeQuery(" SELECT count(*) FROM utilizador where u_login = '" + aLogin + "' AND u_password ='"
					+ aPassword + "';");

			if (rs == null) {
				return false;
			} else {
				while (rs.next()) {
					valor = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			return false;
		}

		if (valor == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Verifica o estado de reprova��o
	 * 
	 * @param aLogin
	 * @param aSt
	 * @return boolean do estado de reprova��o.
	 */
	public boolean verificaEstadoReprovacao(String aLogin, Statement aSt) {
		ResultSet rs = null;
		boolean valor = true;
		try {

			rs = aSt.executeQuery(" SELECT u_estadoreprovacao FROM utilizador where u_login = '" + aLogin + "';");

			if (rs == null) {
				return true;
			} else {
				while (rs.next()) {
					valor = rs.getBoolean(1);
				}
			}
		} catch (SQLException e) {
			return true;
		}

		return valor;
	}

	/**
	 * Verifica estado de um utilizador
	 * 
	 * @param aLogin
	 * @param aSt
	 * @return boolean do estado do utilizador.
	 */
	public boolean verificaEstado(String aLogin, Statement aSt) {
		ResultSet rs = null;
		boolean valor = false;
		try {

			rs = aSt.executeQuery(" SELECT u_estado FROM utilizador where u_login = '" + aLogin + "';");

			if (rs == null) {
				return false;
			} else {
				while (rs.next()) {
					valor = rs.getBoolean(1);
				}
			}
		} catch (SQLException e) {
			return false;
		}

		return valor;
	}

	/**
	 * Verifica o tipo de utilizador
	 * 
	 * @param aLogin
	 * @param aSt
	 * @return 1 se for do tipo gestor, 2 do tipo t�cnico ou 3 do tipo fabricante.
	 */
	public int verificaTipo(String aLogin, Statement aSt) {
		ResultSet rs = null;
		char valor = 'a';
		try {

			rs = aSt.executeQuery(" SELECT u_tipo FROM utilizador where u_login = '" + aLogin + "';");

			if (rs == null) {
				return 0;
			} else {
				while (rs.next()) {
					valor = rs.getString(1).charAt(0);
				}
			}
		} catch (SQLException e) {
			return 0;
		}

		if (valor == 'A') {
			return 1;
		} else if (valor == 'J') {
			return 2;
		}
		return 0;
	}

	/**
	 * Lista os utilizadores inactivos
	 * 
	 * @param aLimite
	 * @param aSt
	 * @return String com os dados dos utilizadores encontrados.
	 */
	public String listaUserInactivo(String aLimite, Statement aSt) {

		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(
					"select * from utilizador where u_estado = false and u_estadoreprovacao = false limit 10 offset "
							+ aLimite);

			if (rs == null) {
				return "O comando para encontrar mostrar os utilizadores que aguardam aprova��o n�o pode ser executado!";
			} else if (!rs.isBeforeFirst()) {
				return "\nN�o existem utilizadores a aguardar aprova��o!\n";
			} else {
				Vector<Utilizador> listaUtilizador;
				listaUtilizador = new Vector<Utilizador>();
				while (rs.next()) {
					Utilizador aDados = new Utilizador(rs.getInt(1), rs.getString(2), rs.getString(3), "*****",
							rs.getString(7), rs.getBoolean(5), rs.getString(8).charAt(0), rs.getBoolean(6));
					listaUtilizador.addElement(aDados);
				}
				return listaUsers(listaUtilizador);
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao n�vel de SQL, a tabela de utilizador n�o foi encontrada.\n" + e + "\n";
		}
	}

	/**
	 * Aprova um utilizador.
	 * 
	 * @param aLogin
	 * @param aSt
	 * @return String com uma frase informativa.
	 */
	public String aprovaUtilizador(String aLogin, Statement aSt) {
		int valor = 0;
		boolean estado = false, estadoReprovacao = false;
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(
					"select u_estado, u_estadoreprovacao from utilizador where u_login = '" + aLogin + "';");

			if (rs == null) {
				return "\nO comando para aprovar um utilizador n�o pode ser executado!\n";
			} else if (!rs.isBeforeFirst()) {
				return "\nO login que inseriu n�o existe na base de dados!\n";
			} else {
				while (rs.next()) {
					estado = rs.getBoolean(1);
					estadoReprovacao = rs.getBoolean(2);
				}
				if (estado) {
					return "\nO utilizador " + aLogin + " j� est� aprovado!\n";
				} else {
					if (estadoReprovacao) {
						valor = aSt.executeUpdate(
								" UPDATE utilizador SET u_estado = true, u_estadoreprovacao = false where u_login = '"
										+ aLogin + "';");
						if (valor == 0) {
							return "\nOcorreu um erro a aprovar o utilizador " + aLogin + ".\n";
						} else {
							return "\nO utilizador " + aLogin + " estava reprovado e foi aprovado com sucesso!\n";
						}
					} else {
						valor = aSt.executeUpdate(
								" UPDATE utilizador SET u_estado = true where u_login = '" + aLogin + "';");
						if (valor == 0) {
							return "\nOcorreu um erro a aprovar o utilizador " + aLogin + ".\n";
						} else {
							return "\nO utilizador " + aLogin + " foi aprovado com sucesso!\n";
						}
					}
				}
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao n�vel de SQL, a tabela de utilizador n�o foi encontrada.\n" + e + "\n";
		}
	}

	/**
	 * Lista utilizadores reprovados.
	 * 
	 * @param aLimite
	 * @param aSt
	 * @return String com os dados dos utilizadores encontrados.
	 */
	public String listaUtilizadorReprovado(String aLimite, Statement aSt) {

		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(
					"select * from utilizador where u_estadoreprovacao = true limit 10 offset " + aLimite);

			if (rs == null) {
				return "O comando para encontrar mostrar os utilizadores reprovados n�o pode ser executado!";
			} else if (!rs.isBeforeFirst()) {
				return "\nN�o existem utilizadores reprovados!\n";
			} else {
				Vector<Utilizador> listaUtilizador;
				listaUtilizador = new Vector<Utilizador>();
				while (rs.next()) {
					Utilizador aDados = new Utilizador(rs.getInt(1), rs.getString(2), rs.getString(3), "*****",
							rs.getString(7), rs.getBoolean(5), rs.getString(8).charAt(0), rs.getBoolean(6));
					listaUtilizador.addElement(aDados);
				}
				return listaUsers(listaUtilizador);
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao n�vel de SQL, a tabela de utilizador n�o foi encontrada.\n" + e + "\n";
		}
	}

	/**
	 * Lista utilizadores reprovados.
	 * 
	 * @param aLimite
	 * @param aSt
	 * @return String com os dados dos utilizadores encontrados.
	 */
	public String listaUtilizadorReprovado1(String aLimite, Statement aSt) {

		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(
					"select * from utilizador where u_estadoreprovacao != true and u_tipo != 'A' limit 10 offset "
							+ aLimite);

			if (rs == null) {
				return "O comando para encontrar mostrar os utilizadores reprovados n�o pode ser executado!";
			} else if (!rs.isBeforeFirst()) {
				return "\nN�o existem utilizadores reprovados!\n";
			} else {
				Vector<Utilizador> listaUtilizador;
				listaUtilizador = new Vector<Utilizador>();
				while (rs.next()) {
					Utilizador aDados = new Utilizador(rs.getInt(1), rs.getString(2), rs.getString(3), "*****",
							rs.getString(7), rs.getBoolean(5), rs.getString(8).charAt(0), rs.getBoolean(6));
					listaUtilizador.addElement(aDados);
				}
				return listaUsers(listaUtilizador);
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao n�vel de SQL, a tabela de utilizador n�o foi encontrada.\n" + e + "\n";
		}
	}

	/**
	 * Obtem o id do utilizador atrav�s do login.
	 * 
	 * @param aSt
	 * @param aLogin
	 * @return id do utilizador.
	 */
	public int getIdUser(Statement aSt, String aLogin) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(" SELECT u_id FROM utilizador where u_login = '" + aLogin + "';");

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
	 * Lista todos os utilizadores.
	 * 
	 * @param aLimite
	 * @param aComando
	 * @param aSt
	 * @param aMsg
	 * @return String do dados dos utilizadores encontrados.
	 */
	public String listaUserTotais(String aLimite, String aComando, Statement aSt, String aMsg) {

		ResultSet rs = null;

		try {

			rs = aSt.executeQuery("select * from utilizador " + aComando + " limit 10 offset " + aLimite);

			if (rs == null) {
				return "O comando para encontrar mostrar todos os utilizadores n�o pode ser executado!";
			} else if (!rs.isBeforeFirst()) {
				return aMsg;
			} else {
				Vector<Utilizador> listaUtilizador;
				listaUtilizador = new Vector<Utilizador>();
				while (rs.next()) {
					Utilizador aDados = new Utilizador(rs.getInt(1), rs.getString(2), rs.getString(3), "*****",
							rs.getString(7), rs.getBoolean(5), rs.getString(8).charAt(0), rs.getBoolean(6));
					listaUtilizador.addElement(aDados);
				}
				return listaUsers(listaUtilizador);
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao n�vel de SQL, a tabela de utilizador n�o foi encontrada.\n" + e + "\n";
		}
	}

	/**
	 * Reprova um utilizador
	 * 
	 * @param aLogin
	 * @param aSt
	 * @return String com uma mensagem informativa.
	 */
	public String reprovaUtilizador(String aLogin, Statement aSt) {
		int valor = 0;
		boolean estado = false, estadoReprovacao = false;
		char tipo = 'A';
		ResultSet rs = null;

		try {
			rs = aSt.executeQuery(
					"select u_tipo, u_estado, u_estadoreprovacao from utilizador where u_login = '" + aLogin + "';");

			if (rs == null) {
				return "\nO comando para reprovar um utilizador n�o pode ser executado!\n";
			} else if (!rs.isBeforeFirst()) {
				return "\nO login que inseriu n�o existe na base de dados!\n";
			} else {
				while (rs.next()) {
					tipo = rs.getString(1).charAt(0);
					estado = rs.getBoolean(2);
					estadoReprovacao = rs.getBoolean(3);
				}
				if (tipo == 'A') {
					return "\nUm utilizador do tipo Administrador n�o pode ser reprovado!\n";
				} else {
					if (estadoReprovacao) {
						return "\nO utilizador " + aLogin + " j� est� reprovado!\n";
					} else {
						if (estado) {
							valor = aSt.executeUpdate(
									" UPDATE utilizador SET u_estado = false, u_estadoreprovacao = true where u_login = '"
											+ aLogin + "';");
							if (valor == 0) {
								return "\nOcorreu um erro a reprovar o utilizador " + aLogin + ".\n";
							} else {
								return "\nO utilizador " + aLogin + " foi reprovado com sucesso!\n";
							}
						} else {
							valor = aSt
									.executeUpdate(" UPDATE utilizador SET u_estadoreprovacao = true where u_login = '"
											+ aLogin + "';");
							if (valor == 0) {
								return "\nOcorreu um erro a reprovar o utilizador " + aLogin + ".\n";
							} else {
								return "\nO utilizador " + aLogin + " foi reprovado com sucesso!\n";
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			return "\nOcorreu um erro ao n�vel de SQL, a tabela de utilizador n�o foi encontrada.\n" + e + "\n";
		}
	}

	/**
	 * Inactiva uma conta
	 * 
	 * @param aLogin
	 * @param aSt
	 * @return true se correu bem, false se n�o.
	 */
	public boolean inactivaconta(String aLogin, Statement aSt) {
		int valor = 0;

		try {

			valor = aSt.executeUpdate(
					" UPDATE utilizador SET u_estado = false, u_estadoreprovacao = true where u_login = '" + aLogin
							+ "';");
			
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
	 * Verifica quantos utilizadores reprovados existem.
	 * 
	 * @param aSt
	 * @return n�mero de utilizadores reprovados.
	 */
	public int verificaListaReprovado(Statement aSt) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(" SELECT count(*) FROM utilizador where u_estadoreprovacao = true;");

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
	 * Verifica quantos utilizadores reprovados sem ser do tipo G existem.
	 * 
	 * @param aSt
	 * @return n�mero de utilizadores reprovados.
	 */
	public int verificaListaReprovado1(Statement aSt) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(
					" SELECT count(*) FROM utilizador where u_estadoreprovacao != true and u_tipo != 'A';");

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
	 * Verifica quantos utilizadores existem inactivos.
	 * 
	 * @param aSt
	 * @return n�mero de utilizadores inactivos.
	 */
	public int verificaListaInactivo(Statement aSt) {
		ResultSet rs = null;

		try {

			rs = aSt.executeQuery(
					" SELECT count(*) FROM utilizador where u_estado = false and u_estadoreprovacao = false;");

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
	 * @param listaUtilizador
	 * @return String com os dados do vector de utilizadores.
	 */
	private String listaUsers(Vector<Utilizador> listaUtilizador) {
		if (listaUtilizador != null && listaUtilizador.size() > 0) {
			String listagem = "";
			Enumeration<Utilizador> en = listaUtilizador.elements();
			while (en.hasMoreElements()) {
				listagem = listagem + en.nextElement();
			}
			return listagem;
		}
		return null;
	}

	/**
	 * Obtem o nome do utilizador
	 * 
	 * @param aLogin
	 * @param aSt
	 * @return String do nome de utilizador.
	 */
	public String getNomeUtilizador(String aLogin, Statement aSt) {
		ResultSet rs = null;
		String valor = null;
		try {

			rs = aSt.executeQuery(" SELECT u_name FROM utilizador where u_login = '" + aLogin + "';");

			if (rs == null) {
				return "O comando para enviar o nome de utilizador n�o pode ser executado neste momento!";
			} else {
				while (rs.next()) {
					valor = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			return "\nA tabela de utilizadores n�o foi encontrada\n" + e;
		}

		return valor;
	}

}
