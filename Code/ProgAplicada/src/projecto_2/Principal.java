package projecto_2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import projecto_2.informGerais.GereLog;
import projecto_2.informGerais.GereNotificacao;
import projecto_2.informGerais.Log;
import projecto_2.jogo.GereJogada;
import projecto_2.jogo.GereJogo;
import projecto_2.jogo.Jogada;
import projecto_2.jogo.Jogo;
import projecto_2.utilizador.GereAdministrador;
import projecto_2.utilizador.GereJogador;
import projecto_2.utilizador.GereUtilizador;
import projecto_2.utilizador.Jogador;
import projecto_2.utilizador.Utilizador;

public class Principal {

	private static GereLog aLog = new GereLog();
	private static GereNotificacao aNotificacao = new GereNotificacao();
	private static GereUtilizador aUtilizador = new GereUtilizador();
	private static GereJogo aJogo = new GereJogo();
	private static GereJogada aJogada = new GereJogada();
	private static GereBaseDados aBaseDados = new GereBaseDados();
	private static GereJogador aJogador = new GereJogador();
	private static GereAdministrador aAdministrador = new GereAdministrador();
	private static Scanner teclado = new Scanner(System.in);
	private static String nomeUtilizador = null, nomeAdversario = "", loginAdversario = "", nomeLogin = null;
	private static String dbname = "", dbuser = "", dbpassword = "", dburl = "", dbporto = "";
	private static Statement st = null;
	private static String[] cor = new String[4];
	private static String matrizJogoRede[][] = new String[10][8], matrizAdversario[][] = new String[10][8];
	private static int minhaJogada = 0, jogadaAdversario = 0, idJogo, tipoInicio = 0;
	private static long tempoTotalJogada = 0;
	private static boolean ganhou = false, resposta = false;

	/**
	 * Main do programa
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		menu();
	}

	/**
	 * Mensagem de entrada.
	 */
	private static void msgEntrada() {

		try {
			System.out.println(
					" ____                              __                                                         __          __     \r\n"
							+ "/\\  _`\\            __             /\\ \\                                         __            /\\ \\        /\\ \\    \r\n"
							+ "\\ \\,\\L\\_\\     __  /\\_\\     __     \\ \\ \\____    __    ___ ___            __  __/\\_\\    ___    \\_\\ \\    ___\\ \\ \\   \r\n"
							+ " \\/_\\__ \\   /'__`\\\\/\\ \\  /'__`\\    \\ \\ '__`\\ /'__`\\/' __` __`\\  _______/\\ \\/\\ \\/\\ \\ /' _ `\\  /'_` \\  / __`\\ \\ \\  \r\n"
							+ "   /\\ \\L\\ \\/\\  __/ \\ \\ \\/\\ \\L\\.\\_   \\ \\ \\L\\ /\\  __//\\ \\/\\ \\/\\ \\/\\______\\ \\ \\_/ \\ \\ \\/\\ \\/\\ \\/\\ \\L\\ \\/\\ \\L\\ \\ \\_\\ \r\n"
							+ "   \\ `\\____\\ \\____\\_\\ \\ \\ \\__/.\\_\\   \\ \\_,__\\ \\____\\ \\_\\ \\_\\ \\_\\/______/\\ \\___/ \\ \\_\\ \\_\\ \\_\\ \\___,_\\ \\____/\\/\\_\\\r\n"
							+ "    \\/_____/\\/____/\\ \\_\\ \\/__/\\/_/    \\/___/ \\/____/\\/_/\\/_/\\/_/         \\/__/   \\/_/\\/_/\\/_/\\/__,_ /\\/___/  \\/_/\r\n"
							+ "                  \\ \\____/                                                                                       \r\n"
							+ "                   \\/___/                                                                      ");
			Thread.sleep(600);
			System.out.println(
					" ____                    ___                           __                                          \r\n"
							+ "/\\  _`\\                 /\\_ \\   __                    /\\ \\                                         \r\n"
							+ "\\ \\ \\L\\ \\     __     __ \\//\\ \\ /\\_\\  ____      __     \\_\\ \\    ___       _____    ___   _ __  __   \r\n"
							+ " \\ \\ ,  /   /'__`\\ /'__`\\ \\ \\ \\\\/\\ \\/\\_ ,`\\  /'__`\\   /'_` \\  / __`\\    /\\ '__`\\ / __`\\/\\`'__/\\_\\  \r\n"
							+ "  \\ \\ \\\\ \\ /\\  __//\\ \\L\\.\\_\\_\\ \\\\ \\ \\/_/  /_/\\ \\L\\.\\_/\\ \\L\\ \\/\\ \\L\\ \\   \\ \\ \\L\\ /\\ \\L\\ \\ \\ \\/\\/_/_ \r\n"
							+ "   \\ \\_\\ \\_\\ \\____\\ \\__/.\\_/\\____\\ \\_\\/\\____\\ \\__/.\\_\\ \\___,_\\ \\____/    \\ \\ ,__\\ \\____/\\ \\_\\  /\\_\\\r\n"
							+ "    \\/_/\\/ /\\/____/\\/__/\\/_\\/____/\\/_/\\/____/\\/__/\\/_/\\/__,_ /\\/___/      \\ \\ \\/ \\/___/  \\/_/  \\/_/\r\n"
							+ "                                                                           \\ \\_\\                   \r\n"
							+ "                                                                            \\/_/                   ");
			Thread.sleep(600);
			System.out.println(" _____                                          __              \r\n"
					+ "/\\___ \\    /'\\_/`\\  __                         /\\ \\             \r\n"
					+ "\\/__/\\ \\  /\\      \\/\\_\\  _ __   __      ___    \\_\\ \\     __     \r\n"
					+ "   _\\ \\ \\ \\ \\ \\__\\ \\/\\ \\/\\`'__/'__`\\  /' _ `\\  /'_` \\  /'__`\\   \r\n"
					+ "  /\\ \\_\\ \\_\\ \\ \\_/\\ \\ \\ \\ \\ \\/\\ \\L\\.\\_/\\ \\/\\ \\/\\ \\L\\ \\/\\ \\L\\.\\_ \r\n"
					+ "  \\ \\____/\\_\\ \\_\\\\ \\_\\ \\_\\ \\_\\ \\__/.\\_\\ \\_\\ \\_\\ \\___,_\\ \\__/.\\_\\\r\n"
					+ "   \\/___/\\/_/\\/_/ \\/_/\\/_/\\/_/\\/__/\\/_/\\/_/\\/_/\\/__,_ /\\/__/\\/_/\r\n"
					+ "                                                                \r\n"
					+ "                                                                ");
			Thread.sleep(600);
			System.out.println("\n");
		} catch (Exception e) {

		}

	}

	/**
	 * Coloca os dados alterados de acesso à base de dados.
	 */
	private static void colocaDadosAlterados() {
		Properties prop = new Properties();

		prop.setProperty("dbname", dbname);
		prop.setProperty("dburl", dburl);
		prop.setProperty("dbuser", dbuser);
		prop.setProperty("dbpassword", dbpassword);
		prop.setProperty("dbporto", dbporto);

		try {
			prop.store(new FileOutputStream("config.properties"), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Se não for encontrado o ficheiro com os dados de acesso à base de dados, são
	 * colocados uns dados genéricos.
	 */
	private static void colocaDados() {
		Properties prop = new Properties();

		prop.setProperty("dbname", "db");
		prop.setProperty("dburl", "localhost");
		prop.setProperty("dbuser", "root");
		prop.setProperty("dbpassword", "root");
		prop.setProperty("dbporto", "3306");

		dbname = "db";
		dbuser = "root";
		dburl = "localhost";
		dbpassword = "root";
		dbporto = "3306";

		try {
			prop.store(new FileOutputStream("config.properties"), null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Verifica se o ficheiro que contem os dados de acesso à base de dados existe,
	 * se não existir os dados serão os dados genéricos que constam no manual de
	 * utilizador.
	 */
	private static void arranqueDados() {

		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream("config.properties"));
			dbname = prop.getProperty("dbname");
			dbuser = prop.getProperty("dbuser");
			dburl = prop.getProperty("dburl");
			dbpassword = prop.getProperty("dbpassword");
			dbporto = prop.getProperty("dbporto");
		} catch (FileNotFoundException fnde) {
			System.out.println(
					"O ficheiro com os dados de acesso à base de dados não foi encontrado e, por isso, os dados de acesso serão os de origem.");
			colocaDados();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Menu de arranque da aplicação.
	 */
	private static boolean arranque() {
		int opccao;
		do {

			System.out.println(
					"\n1- Ver/Alterar parâmetros de acesso à base de dados.\n2- Continuar para o próximo menu.\n3- Terminar o programa.");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {

			case 1:
				menuDadosAcesso();
				return true;
			case 2:
				return true;
			case 3:
				opccao = 3;
				break;
			default:
				System.out.println("\nInseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 3);
		return false;
	}

	/**
	 * Mostra os dados de acesso à base de dados.
	 */
	private static void mostraDadosDB() {
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (FileNotFoundException fnde) {
			fnde.printStackTrace();
			colocaDadosAlterados();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\nDados de acesso à base de dados:");
		System.out.println("Nome da base de dados: " + prop.getProperty("dbname"));
		System.out.println("Url da base de dados: " + prop.getProperty("dburl"));
		System.out.println("Login da base de dados: " + prop.getProperty("dbuser"));
		System.out.println("Password da base de dados: " + prop.getProperty("dbpassword"));
		System.out.println("Porto da base de dados: " + prop.getProperty("dbporto"));

	}

	/**
	 * Menu de dados de acesso à base de dados.
	 */
	private static void menuDadosAcesso() {
		int opccao;
		do {

			System.out.println(
					"\n1- Ver parâmetros de acesso à base de dados.\n2- Alterar parâmetros de acesso à base de dados.\n3- Sair e continuar com o programa.");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {

			case 1:
				mostraDadosDB();
				break;
			case 2:
				menuAlterarDadosAcesso();
				break;
			case 3:
				opccao = 3;
				break;
			default:
				System.out.println("\nInseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 3);
	}

	/**
	 * Altera os dados de acesso à base de dados.
	 */
	private static void menuAlterarDadosAcesso() {
		int opccao;

		do {
			mostraDadosDB();
			System.out.println(
					"\n1- Alterar nome da base de dados.\n2- Alterar url da base de dados.\n3- Alterar login.\n4- Alterar password.\n5- Alterar porto da base de dados.\n6- Voltar ao menu anterior.");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {

			case 1:
				dbname = leString("Introduza o novo nome da base de dados: ");
				colocaDadosAlterados();
				System.out.println("\nDados alterados com sucesso!\n");
				break;
			case 2:
				dburl = leString("Introduza o novo url da base de dados: ");
				colocaDadosAlterados();
				System.out.println("\nDados alterados com sucesso!\n");
				break;
			case 3:
				dbuser = leString("Introduza o novo login da base de dados: ");
				colocaDadosAlterados();
				System.out.println("\nDados alterados com sucesso!\n");
				break;
			case 4:
				dbpassword = leString("Introduza a nova password da base de dados: ");
				colocaDadosAlterados();
				System.out.println("\nDados alterados com sucesso!\n");
				break;
			case 5:
				dbporto = leString("Introduza o novo porto da base de dados: ");
				colocaDadosAlterados();
				System.out.println("\nDados alterados com sucesso!\n");
				break;
			case 6:
				opccao = 6;
				break;
			default:
				System.out.println("\nInseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 6);
	}

	/**
	 * Lista notificações.
	 * 
	 * @param aComando
	 * @param aComando2
	 * @param aMsg
	 */
	private static void listaNotificacao(String aComando, String aComando2, String aMsg) {
		int numTotal = aNotificacao.verificaQuantidadeNoti(aComando2, st), numVezes = 0;

		if (numTotal > 10) {
			System.out.println("\nForam encontradas " + numTotal + " notificações.");
			numVezes = (int) Math.ceil(numTotal / 10.0);
			for (int i = 0; i < numVezes; i++) {
				System.out.println("\n" + aNotificacao.listaNotificacao(i * 10 + ";", aComando, st, aMsg));
				if (i < (numVezes - 1)) {
					leString("Digite qualquer coisa para ver a próxima página de resultados.");
				}
			}
		} else {
			if (numTotal == 1) {
				System.out.println("\nFoi encontrada 1 notificação.");
			} else {
				System.out.println("\nForam encontradas " + numTotal + " notificações.");
			}
			System.out.println("\n" + aNotificacao.listaNotificacao("0;", aComando, st, aMsg));
		}
		registaLog("Foram mostradas as notificações.");
	}

	/**
	 * Menu principal do programa.
	 */
	private static void menu() {
		msgEntrada();
		int opccao = 1;
		long tempoInicial = (new Date()).getTime();
		arranqueDados();
		if (arranque()) {
			st = aBaseDados.iniciaBaseDados(dbporto, dburl, dbuser, dbpassword, dbname);
			if (st != null) {

				if (aUtilizador.verificaListaVazia(st, ";") == 0) {
					do {
					} while (!registaPrimeiroUser());
				}

				do {
					System.out.println("\n1- Registar utilizador.\n2- Logar no programa.\n3- Sair do programa.");
					opccao = leInt("Introduza a sua opcção:");

					switch (opccao) {

					case 1:
						registaUser(false);
						break;
					case 2:
						if (logaUser()) {
							registaLog("Entrou no programa.");
							if (tipoInicio == 1) {
								int idUser = aUtilizador.getIdUser(st, nomeLogin);
								listaNotificacao(
										"n, utilizador_notificacao nu where n.N_ID = nu.N_ID and nu.u_id = " + idUser,
										"n, utilizador_notificacao nu where n.N_ID = nu.N_ID and nu.u_id = " + idUser,
										"\nNão existem notificações novas!\n");
								aNotificacao.retiraNotificacao(idUser, st);
								aNotificacao.eliminaNotificacao(st);
							}
							opccao = menu2();
							if (opccao == 3) {
								registaLog("Terminou o programa.");
								msgDespedida();
								calculaTempoDecorrido(tempoInicial);
								aBaseDados.fechaBaseDados();
								opccao = 3;
							} else {
								msgDespedida();
								registaLog("Fez 'logout' no programa.");
								nomeUtilizador = null;
								System.out.println("Desconectado com sucesso!");
							}
						}
						break;
					case 3:
						msgDespedida();
						registaLog("Terminou o programa.");
						calculaTempoDecorrido(tempoInicial);
						aBaseDados.fechaBaseDados();
						opccao = 3;
						break;
					default:
						System.out.println("\nInseriu uma opcção inválida.");
						break;
					}

				} while (opccao != 3);
			} else {
				calculaTempoDecorrido(tempoInicial);
				aBaseDados.fechaBaseDados();
			}
		} else {
			msgDespedida();
			calculaTempoDecorrido(tempoInicial);
		}
	}

	/**
	 * Menu principal, sendo que o mesmo varia conforme o tipo de utilizador.
	 * 
	 * @return opccao, se for 3 o programa termina, se for 6 o programa faz logout
	 *         do utilizador.
	 */
	private static int menu2() {
		int opccao = 0;
		if (tipoInicio == 1) {
			do {
				System.out.println(
						"\n1- Gestão de utilizadores.\n2- Ver log(s).\n3- Sair do programa.\n4- Fazer 'logout'.\n");
				opccao = leInt("Introduza a sua opcção:");

				switch (opccao) {
				case 1:
					menuGestaoUtilizadorAdmin();
					break;
				case 2:
					menuLog();
					break;
				case 3:
					opccao = 3;
					break;
				case 4:
					break;
				default:
					System.out.println("Inseriu uma opcção inválida.");
					break;
				}

			} while (opccao != 4 && opccao != 3);

			return opccao;
		}

		if (tipoInicio == 2) {

			do {
				System.out.println(
						"\n1- Menu jogador.\n2- Gestão de utilizadores.\n3- Sair do programa.\n4- Fazer 'logout'.\n");
				opccao = leInt("Introduza a sua opcção:");

				switch (opccao) {
				case 1:
					menuJogador();
					break;
				case 2:
					menuGestaoUtilizadorJogador();
					break;
				case 3:
					opccao = 3;
					break;
				case 4:
					break;
				default:
					System.out.println("Inseriu uma opcção inválida.");
					break;
				}

			} while (opccao != 4 && opccao != 3);
			return opccao;
		}
		return 0;
	}

	/**
	 * Regista os logs.
	 * 
	 * @param aAccao
	 */
	private static void registaLog(String aAccao) {

		String data = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		String hora = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

		if (nomeLogin == null) {
			Log log = new Log(data, hora, "convidado", aAccao);
			aLog.criaLog(log, st);
		} else {
			Log log = new Log(data, hora, nomeLogin, aAccao);
			aLog.criaLog(log, st);
		}

	}

	/**
	 * Lista logs.
	 * 
	 * @param aComando
	 * @param aComando2
	 * @param aMsg
	 */
	private static void listaLogs(String aComando, String aComando2, String aMsg) {
		int numTotal = aLog.verificaQuantidadeLog(aComando2, st), numVezes = 0;

		if (numTotal > 10) {
			System.out.println("\nForam encontrados " + numTotal + " resultados.");
			numVezes = (int) Math.ceil(numTotal / 10.0);
			for (int i = 0; i < numVezes; i++) {
				System.out.println("\n" + aLog.listaLog(i * 10 + ";", aComando, st, aMsg));
				if (i < (numVezes - 1)) {
					leString("Digite qualquer coisa para ver a próxima página de resultados.");
				}
			}
		} else {
			if (numTotal == 1) {
				System.out.println("\nFoi encontrado 1 resultado.");
			} else {
				System.out.println("\nForam encontrados " + numTotal + " resultados.");
			}
			System.out.println("\n" + aLog.listaLog("0;", aComando, st, aMsg));
		}
	}

	/**
	 * Menu que gere os logs.
	 */
	private static void menuLog() {
		int opccao = 0;
		do {
			System.out.println(
					"\n1- Listar todos os logs por ordem asc.\n2- Listar todos os logs por ordem desc.\n3- Pesquisar logs de um determinado utilizador.\n4- Voltar ao menu anterior.\n");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				registaLog("Listou todos os logs.");
				listaLogs(" order by l_data_log, l_hora", "", "\nNão existem logs na base de dados.\n");
				break;
			case 2:
				registaLog("Listou todos os logs.");
				listaLogs(" order by l_data_log desc, l_hora desc", "", "\nNão existem logs na base de dados.\n");
				break;
			case 3:
				String loginPesquisa = leString("\nIntroduza o login do utilizador que deseja pesquisar os logs: ");
				registaLog("Pesquisou os logs do utilizador: " + loginPesquisa + ".");
				listaLogs(" where l_utilizador like '%" + loginPesquisa + "%' order by l_data_log, l_hora",
						"where l_utilizador like '%" + loginPesquisa + "%' order by l_data_log, l_hora",
						"\nNão foram encontrados logs desse utilizador na base de dados.\n");
				break;
			case 4:
				break;
			default:
				System.out.println("\nInseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 4);
	}

	/**
	 * Menu jogador.
	 */
	private static void menuJogador() {
		int opccao;

		do {
			System.out.println("\n1- Menu de jogos.\n2- Gerir jogo(s).\n3- Voltar ao menu anterior.");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				menuJogar();
				break;
			case 2:
				menuGestaoJogo();
				break;
			case 3:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 3);
	}

	/**
	 * Lista jogo.
	 * 
	 * @param aComando
	 * @param aComando2
	 * @param aMsg
	 */
	private static void listaJogo(String aComando, String aComando2) {
		int numTotal = aJogo.verificaQuantidadeJogo(aComando2, st), numVezes = 0;

		if (numTotal > 10) {
			System.out.println("\nForam encontrados " + numTotal + " resultados.");
			numVezes = (int) Math.ceil(numTotal / 10.0);
			for (int i = 0; i < numVezes; i++) {
				System.out.println("\n" + aJogo.listaJogo(i * 10 + ";", aComando, st));
				if (i < (numVezes - 1)) {
					leString("Digite qualquer coisa para ver a próxima página de resultados.");
				}
			}
		} else {
			if (numTotal == 1) {
				System.out.println("\nFoi encontrado 1 resultado.");
			} else {
				System.out.println("\nForam encontrados " + numTotal + " resultados.");
			}
			System.out.println("\n" + aJogo.listaJogo("0;", aComando, st));
		}
	}

	/**
	 * Menu gestão de jogos.
	 */
	private static void menuGestaoJogo() {
		int opccao;

		do {
			System.out.println(
					"\n1- Listar todos os meus jogos por data asc.\n2- Listar todos os meus jogos por data desc.\n3- Simular um determinado jogo.\n4- Voltar ao menu anterior.");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				listaJogo("where u_id = " + aUtilizador.getIdUser(st, nomeLogin) + " order by jo_data ",
						"where u_id = " + aUtilizador.getIdUser(st, nomeLogin));
				registaLog("Listou todos os seus jogos.");
				break;
			case 2:
				listaJogo("where u_id = " + aUtilizador.getIdUser(st, nomeLogin) + " order by jo_data desc ",
						"where u_id = " + aUtilizador.getIdUser(st, nomeLogin));
				registaLog("Listou todos os seus jogos.");
				break;
			case 3:
				if (aJogo.verificaQuantidadeJogo("", st) > 0) {
					int idUser = aUtilizador.getIdUser(st, nomeLogin);
					listaJogo("where u_id = " + aUtilizador.getIdUser(st, nomeLogin) + " order by jo_data ",
							"where u_id = " + idUser);
					int idJogo = leInt("\nIntroduza o id do jogo que deseja simular: ");
					if (aJogo.verificaQuantidadeJogo("where jo_id = " + idJogo + " and u_id = " + idUser, st) == 1) {
						simulaJogo(idJogo);
						registaLog("Simulou o jogo com id: " + idJogo + ".");
					} else {
						System.out.println("\nNão tem nenhum jogo com esse id!\n");
						registaLog("Tentou simular um jogo mas inseriu um id inválido.");
					}
				} else {
					System.out.println("\nNão tem nenhum jogo registado na base de dados!\n");
					registaLog("Tentou simular um jogo mas não tem nenhum jogo na base de dados.");
				}
				break;
			case 4:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 4);
	}

	/**
	 * Função que simula um jogo.
	 * 
	 * @param aIdJogo
	 */
	private static void simulaJogo(int aIdJogo) {
		Vector<Jogada> listaJogada = aJogada.ListaJogada(aIdJogo, st);
		int contador = 0;
		if (listaJogada != null && listaJogada.size() > 0) {
			Enumeration<Jogada> en = listaJogada.elements();
			while (en.hasMoreElements()) {
				String[] splited = en.nextElement().getAccao().split("\\s+");
				if (splited[1].equals("<1>") && splited[2].equals("<jogopc>;")) {
					contador = 1;
				}
			}
		}

		if (contador == 1) {
			int numLinha = 0;
			if (listaJogada != null && listaJogada.size() > 0) {
				Enumeration<Jogada> en = listaJogada.elements();
				while (en.hasMoreElements()) {
					String[] splited = en.nextElement().getAccao().split("\\s+");
					if (splited[1].equals("<linhas>")) {
						numLinha = Integer.parseInt(splited[2]);
					}
				}
			}
			minhaJogada = 0;
			System.out.println("\nEstá a ver o jogo entre si e o computador.\n");
			String matrizJogo[][] = new String[numLinha][8];
			for (int i = 0; i < numLinha; i++) {
				for (int j = 0; j < 8; j++) {
					matrizJogo[i][j] = " ";
				}
			}
			mostraMatriz(matrizJogo, numLinha);
			leString("\nPressione 'enter' para a próxima jogada/resultado aparecer.");
			if (listaJogada != null && listaJogada.size() > 0) {
				Enumeration<Jogada> en = listaJogada.elements();
				while (en.hasMoreElements()) {
					matrizJogo = refazJogadaComputador(en.nextElement().getAccao(), matrizJogo, numLinha);
				}
			}
		} else {
			if (listaJogada != null && listaJogada.size() > 0) {
				Enumeration<Jogada> en = listaJogada.elements();
				while (en.hasMoreElements()) {
					refazJogadaRede(en.nextElement().getAccao());
				}
			}
		}

	}

	/**
	 * Refaz uma jogada contra o computador.
	 * 
	 * @param aMensagem
	 * @param aMatriz
	 * @param aNumLinha
	 */
	private static String[][] refazJogadaComputador(String aMensagem, String aMatriz[][], int aNumLinha) {
		String[] splited = aMensagem.split("\\s+");

		if (splited[1].equals("<play>")) {
			aMatriz[minhaJogada][0] = "" + splited[2].charAt(1);
			aMatriz[minhaJogada][1] = "" + splited[2].charAt(3);
			aMatriz[minhaJogada][2] = "" + splited[2].charAt(5);
			aMatriz[minhaJogada][3] = "" + splited[2].charAt(7);
			minhaJogada++;
			return aMatriz;
		}

		if (splited[1].equals("<result>")) {
			aMatriz[minhaJogada - 1][4] = "" + splited[2].charAt(1);
			aMatriz[minhaJogada - 1][5] = "" + splited[2].charAt(3);
			aMatriz[minhaJogada - 1][6] = "" + splited[2].charAt(5);
			aMatriz[minhaJogada - 1][7] = "" + splited[2].charAt(7);
			mostraMatriz(aMatriz, aNumLinha);
			leString("\nPressione 'enter' para a próxima jogada/resultado aparecer.");
			return aMatriz;
		}

		if (splited[2].equals("<defeat>;")) {
			System.out.println("\nVocê perdeu este encontro contra o computador!\n");
		}

		if (splited[2].equals("<win>;")) {
			System.out.println("\nVocê ganhou este encontro contra o computador!!!\n");
		}

		if (splited[2].equals("<desistiu>;")) {
			System.out.println("\nVocê desistiu e por isso perdeu este jogo contra o computador!\n");
		}
		return aMatriz;
	}

	/**
	 * Refaz uma jogada em rede
	 * 
	 * @param aMensagem
	 */
	private static void refazJogadaRede(String aMensagem) {
		String[] splited = aMensagem.split("\\s+");

		if (splited[1].equals("<nomeAdv>")) {
			nomeAdversario = splited[2];
			System.out.println("\nEstá a ver o jogo entre <" + nomeUtilizador + "> e " + nomeAdversario + "\n");
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 8; j++) {
					matrizAdversario[i][j] = " ";
					matrizJogoRede[i][j] = " ";
				}
			}
			minhaJogada = 0;
			jogadaAdversario = 0;
			mostraMatrizes();
			leString("\nPressione 'enter' para a próxima jogada/resultado aparecer.");
		}

		if (splited[1].equals("<play>")) {
			matrizJogoRede[minhaJogada][0] = "" + splited[2].charAt(1);
			matrizJogoRede[minhaJogada][1] = "" + splited[2].charAt(3);
			matrizJogoRede[minhaJogada][2] = "" + splited[2].charAt(5);
			matrizJogoRede[minhaJogada][3] = "" + splited[2].charAt(7);
			minhaJogada++;
		}

		if (splited[1].equals("<playadv>")) {
			matrizAdversario[jogadaAdversario][0] = "" + splited[2].charAt(1);
			matrizAdversario[jogadaAdversario][1] = "" + splited[2].charAt(3);
			matrizAdversario[jogadaAdversario][2] = "" + splited[2].charAt(5);
			matrizAdversario[jogadaAdversario][3] = "" + splited[2].charAt(7);
			jogadaAdversario++;
		}

		if (splited[1].equals("<result>")) {
			matrizJogoRede[minhaJogada - 1][4] = "" + splited[2].charAt(1);
			matrizJogoRede[minhaJogada - 1][5] = "" + splited[2].charAt(3);
			matrizJogoRede[minhaJogada - 1][6] = "" + splited[2].charAt(5);
			matrizJogoRede[minhaJogada - 1][7] = "" + splited[2].charAt(7);
			mostraMatrizes();
			leString("\nPressione 'enter' para a próxima jogada/resultado aparecer.");
		}

		if (splited[1].equals("<resultadv>")) {
			matrizAdversario[jogadaAdversario - 1][4] = "" + splited[2].charAt(1);
			matrizAdversario[jogadaAdversario - 1][5] = "" + splited[2].charAt(3);
			matrizAdversario[jogadaAdversario - 1][6] = "" + splited[2].charAt(5);
			matrizAdversario[jogadaAdversario - 1][7] = "" + splited[2].charAt(7);
			mostraMatrizes();
			leString("\nPressione 'enter' para a próxima jogada/resultado aparecer.");
		}

		if (splited[2].equals("<draw>;")) {
			System.out.println("\nEste jogo terminou empatado!\n");
		}

		if (splited[2].equals("<defeat>;")) {
			System.out.println("\nVocê perdeu este encontro!\n");
		}

		if (splited[2].equals("<defeatD>;")) {
			System.out.println("\nVocê perdeu este encontro porque desistiu do mesmo!\n");
		}

		if (splited[2].equals("<win>;")) {
			System.out.println("\nVocê ganhou este jogo!!!\n");
		}

		if (splited[2].equals("<winD>;")) {
			System.out.println("\nVocê ganhou este jogo porque o seu adversário demorou muito tempo a responder!!!\n");
		}

		if (splited[2].equals("<winDesistiu>;")) {
			System.out.println("\nVocê ganhou este jogo porque o seu adversário desistiu!!!\n");
		}

		if (splited[2].equals("<erro>;")) {
			System.out.println(
					"\nOcorreu um erro neste jogo! O erro pode ter sido por ter demorado muito tempo a responder.\n");
		}

	}

	/**
	 * Menu que serve para o jogador escolher o tipo de jogo que quer fazer.
	 */
	private static void menuJogar() {
		int opccao;

		do {
			System.out.println(
					"\n1- Jogar contra o computador.\n2- Jogar contra um jogador em rede (servidor).\n3- Jogar contra um jogador em rede (cliente).\n4- Voltar ao menu anterior.");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				jogaComputador();
				registaLog("Fez um jogo contra o computador.");
				break;
			case 2:
				jogaRedeServidor();
				break;
			case 3:
				jogaRedeCliente();
				break;
			case 4:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 4);
	}

	/**
	 * Função que inicia/trata da ligação por parte do cliente ao servidor.
	 */
	private static void jogaRedeCliente() {
		String serverIP = leString("\nIntroduza o ip do servidor: ");
		int serverPort = leInt("\nIntroduza o porto em que o servidor está à escuta: ");
		Socket socket = null;
		try {
			socket = new Socket(serverIP, serverPort); // socket connection
			System.out.println("\nO servidor aceitou o seu pedido!\n");
			socket.setSoTimeout(50000);
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 8; j++) {
					matrizJogoRede[i][j] = " ";
					matrizAdversario[i][j] = " ";
				}
			}
			minhaJogada = 0;
			tempoTotalJogada = 0;
			jogadaAdversario = 0;
			ganhou = false;
			boolean estado = false;
			int contador = 0;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String inputLine = "", outputLine = "";
			System.out.println("\nEstá ligado ao ip: " + socket.getInetAddress() + "\n");
			System.out.println("\nEscolha, por favor, as suas cores:\n");
			cor = escolheCores();
			System.out.println(
					"\nEscolheu a combinação: " + cor[0] + ", " + cor[1] + ", " + cor[2] + ", " + cor[3] + ".");
			String tempoInicioJogo = "";
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.equals("<" + loginAdversario + "> <bye>;")) {
					break;
				}
				if (inputLine.equals("<" + loginAdversario + "> <withdraw>;")) {
					System.out.println("\nO seu adversário desistiu! Você ganhou o jogo devido a isso!\n");
					msgParabens();
					registaLog("Ganhou um jogo em rede sendo o cliente contra o adversário com login: "
							+ loginAdversario + ".");
					String loginvit = aJogador.jogadorComMaisVit(st);
					if (aJogador.actualizaNumeroJogos(nomeLogin, st)
							&& aJogador.actualizaNumeroVitorias(nomeLogin, st)) {
						System.out.println(
								"\nO seu número de jogos e número de vitórias no jogo foram actualizados (como o adversário desistiu, o seu tempo irá continuar igual)!\n");
						int numVitorias = aJogador.obtemNumVitorias(nomeLogin, st);
						if (numVitorias % 10 == 0) {
							System.out.println("\nParabéns! Atingiu as " + numVitorias + " vitórias.\n");
							aNotificacao.registaNotificacaoAdmin("O utilizador com o login: " + nomeLogin
									+ " atingiu as " + numVitorias + " vitórias.", st);
						}
						if (!loginvit.equals(nomeLogin) && aJogador.jogadorComMaisVit(st).equals(nomeLogin)) {
							System.out.println("\nParabéns! Subiu ao primeiro lugar em número de vitórias!\n");
							SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
							aNotificacao.registaNotificacaoAdmin(
									"O utilizador com o login: " + nomeLogin + " é neste momento ("
											+ format.format(new Date()) + ") o jogador com mais vitórias ("
											+ numVitorias + " vitórias) na lista de jogadores.",
									st);
						}
					} else {
						System.out.println(
								"\nOcorreu um erro a actualizar o número de jogos, número de vitórias e tempo total acumulado no jogo!\n");
					}
					Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <winDesistiu>;");
					aJogada.registaJogada(jogada, st);
					out.println("<" + loginAdversario + "> <withdraw> <ack>;");
				} else {
					if (contador == 0) {
						tempoInicioJogo = obtemTempoInicio();
						estado = trata1Mensagem(inputLine);
						Jogo jogo1 = new Jogo(aUtilizador.getIdUser(st, nomeLogin), tempoInicioJogo);
						if (aJogo.registaJogo(jogo1, st)) {
							idJogo = aJogo.verificaQuantidadeJogo("", st);
							Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <linhas> 10;");
							aJogada.registaJogada(jogada, st);
						} else {
							idJogo = -1;
							System.out.println(
									"\nOcorreu um erro a gravar o jogo e, por isso, o mesmo não poderá ser revisto.\n");
						}
						if (estado) {
							out.println("<" + nomeLogin + "> <hello> <" + nomeUtilizador + ">;");
							contador++;
						}
					} else {
						outputLine = trataMensagemCliente(tempoInicioJogo, inputLine);
						if (outputLine.equals("<" + nomeLogin + "> <withdraw>;")) {
							if (aJogador.actualizaNumeroJogos(nomeLogin, st) && aJogador
									.actualizaTempoJogador(calculaTempoCert(tempoInicioJogo), nomeLogin, st)) {
								System.out.println(
										"\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
							} else {
								System.out.println(
										"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
							}
							Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <defeatD>;");
							aJogada.registaJogada(jogada, st);
						}
						out.println(outputLine);
					}
				}
			}
		} catch (SocketTimeoutException ste) {
			System.out.println("\nO adversário excedeu o tempo de resposta! Você ganhou o jogo devido a isso!\n");
			msgParabens();
			registaLog(
					"Ganhou um jogo em rede sendo o cliente contra o adversário com login: " + loginAdversario + ".");
			String loginvit = aJogador.jogadorComMaisVit(st);
			if (aJogador.actualizaNumeroJogos(nomeLogin, st) && aJogador.actualizaNumeroVitorias(nomeLogin, st)) {
				System.out.println(
						"\nO seu número de jogos e número de vitórias no jogo foram actualizados (como o adversário deixou de responder, o seu tempo irá continuar igual)!\n");
				int numVitorias = aJogador.obtemNumVitorias(nomeLogin, st);
				if (numVitorias % 10 == 0) {
					System.out.println("\nParabéns! Atingiu as " + numVitorias + " vitórias.\n");
					aNotificacao.registaNotificacaoAdmin(
							"O utilizador com o login: " + nomeLogin + " atingiu as " + numVitorias + " vitórias.", st);
				}
				if (!loginvit.equals(nomeLogin) && aJogador.jogadorComMaisVit(st).equals(nomeLogin)) {
					System.out.println("\nParabéns! Subiu ao primeiro lugar em número de vitórias!\n");
					SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					aNotificacao.registaNotificacaoAdmin("O utilizador com o login: " + nomeLogin + " é neste momento ("
							+ format.format(new Date()) + ") o jogador com mais vitórias (" + numVitorias
							+ " vitórias) na lista de jogadores.", st);
				}
			} else {
				System.out.println(
						"\nOcorreu um erro a actualizar o número de jogos, número de vitórias e tempo total acumulado no jogo!\n");
			}
			Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <winD>;");
			aJogada.registaJogada(jogada, st);
		} catch (SocketException se) {
			System.out.println(
					"\nOcorreu um erro! Se o mesmo tiver sido por causa de ter excedido o tempo, será creditada a vitória ao adversário!\n");
			Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <erro>;");
			aJogada.registaJogada(jogada, st);
			registaLog("Ocorreu um erro no jogo em rede sendo o cliente contra o adversário com login: "
					+ loginAdversario + ".");
		} catch (IOException ioe) {
			Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <erro>;");
			aJogada.registaJogada(jogada, st);
			registaLog("Ocorreu um erro no jogo em rede sendo o cliente contra o adversário com login: "
					+ loginAdversario + ".");
			ioe.printStackTrace();
		} catch (Exception e) {
			Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <erro>;");
			aJogada.registaJogada(jogada, st);
			registaLog("Ocorreu um erro no jogo em rede sendo o cliente contra o adversário com login: "
					+ loginAdversario + ".");
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Função que trata das mensagens recebidas por parte do servidor
	 * 
	 * @param aTempoInicioJogo
	 * @param aMensagem
	 * @return mensagem para o servidor.
	 */
	private static String trataMensagemCliente(String aTempoInicioJogo, String aMensagem) {
		String[] splited = aMensagem.split("\\s+");
		if (splited[1].equals("<start>")) {
			if (splited[2].equals("<1>;")) {
				mostraMatrizes();
				registaLog(
						"Fez um jogo em rede sendo o cliente contra o adversário com login: " + loginAdversario + ".");
				System.out.println("\nO seu adversário [" + nomeAdversario
						+ "] será o primeiro a jogar! Tem um limite de 50 segundos para fazer uma jogada, se demorar mais tempo irá perder o jogo!\n");
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <nomeAdv> <" + nomeAdversario + ">;");
				aJogada.registaJogada(jogada, st);
				return "<" + nomeLogin + "> <start> <ok>;";
			} else {
				mostraMatrizes();
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <nomeAdv> <" + nomeAdversario + ">;");
				aJogada.registaJogada(jogada, st);
				System.out.println(
						"\nVocê é o primeiro a jogar! Tem 50 segundos para efectuar a jogada, se passar 50 segundos e não tiver efectuado a jogada, irá perder o jogo!\n");
				return jogaJogada(idJogo);
			}
		}
		if (splited[1].equals("<play>")) {
			matrizAdversario[jogadaAdversario][0] = "" + splited[2].charAt(1);
			matrizAdversario[jogadaAdversario][1] = "" + splited[2].charAt(3);
			matrizAdversario[jogadaAdversario][2] = "" + splited[2].charAt(5);
			matrizAdversario[jogadaAdversario][3] = "" + splited[2].charAt(7);
			Jogada jogada = new Jogada(idJogo,
					"<" + nomeLogin + "> <playadv> <" + matrizAdversario[jogadaAdversario][0] + ","
							+ matrizAdversario[jogadaAdversario][1] + "," + matrizAdversario[jogadaAdversario][2] + ","
							+ matrizAdversario[jogadaAdversario][3] + ">;");
			aJogada.registaJogada(jogada, st);
			if (introduzResultado(matrizAdversario, jogadaAdversario, cor)) {
				ganhou = true;
				matrizAdversario[jogadaAdversario][4] = "P";
				matrizAdversario[jogadaAdversario][5] = "P";
				matrizAdversario[jogadaAdversario][6] = "P";
				matrizAdversario[jogadaAdversario][7] = "P";
				jogadaAdversario++;
				if (minhaJogada == jogadaAdversario) {
					if (matrizJogoRede[minhaJogada - 1][4].equals("P") && matrizJogoRede[minhaJogada - 1][5].equals("P")
							&& matrizJogoRede[minhaJogada - 1][6].equals("P")
							&& matrizJogoRede[minhaJogada - 1][7].equals("P")) {
						ganhou = false;
						mostraMatrizes();
						System.out.println("\nOcorreu um empate!\n");
						registaLog("Empatou um jogo em rede sendo o cliente contra o adversário com login: "
								+ loginAdversario + ".");
						System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
						if (aJogador.actualizaNumeroJogos(nomeLogin, st)
								&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
							System.out.println(
									"\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
						} else {
							System.out.println(
									"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
						}
						jogada = new Jogada(idJogo,
								"<" + nomeLogin + "> <resultadv> <" + matrizAdversario[jogadaAdversario - 1][4] + ","
										+ matrizAdversario[jogadaAdversario - 1][5] + ","
										+ matrizAdversario[jogadaAdversario - 1][6] + ","
										+ matrizAdversario[jogadaAdversario - 1][7] + ">;");
						aJogada.registaJogada(jogada, st);
						jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <draw>;");
						aJogada.registaJogada(jogada, st);
						return "<" + nomeLogin + "> <status> <draw>;";
					}
					ganhou = false;
					mostraMatrizes();
					System.out.println("\nPerdeu o jogo!\n");
					registaLog("Perdeu um jogo em rede sendo o cliente contra o adversário com login: "
							+ loginAdversario + ".");
					System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
					if (aJogador.actualizaNumeroJogos(nomeLogin, st)
							&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
						System.out.println(
								"\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
					} else {
						System.out.println(
								"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
					}
					jogada = new Jogada(idJogo,
							"<" + nomeLogin + "> <resultadv> <" + matrizAdversario[jogadaAdversario - 1][4] + ","
									+ matrizAdversario[jogadaAdversario - 1][5] + ","
									+ matrizAdversario[jogadaAdversario - 1][6] + ","
									+ matrizAdversario[jogadaAdversario - 1][7] + ">;");
					aJogada.registaJogada(jogada, st);
					jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <defeat>;");
					aJogada.registaJogada(jogada, st);
					return "<" + nomeLogin + "> <status> <win>;";
				}
			} else {
				jogadaAdversario++;
			}
			jogada = new Jogada(idJogo, "<" + nomeLogin + "> <resultadv> <" + matrizAdversario[jogadaAdversario - 1][4]
					+ "," + matrizAdversario[jogadaAdversario - 1][5] + "," + matrizAdversario[jogadaAdversario - 1][6]
					+ "," + matrizAdversario[jogadaAdversario - 1][7] + ">;");
			aJogada.registaJogada(jogada, st);
			return "<" + nomeLogin + "> <result> <" + matrizAdversario[jogadaAdversario - 1][4] + ","
					+ matrizAdversario[jogadaAdversario - 1][5] + "," + matrizAdversario[jogadaAdversario - 1][6] + ","
					+ matrizAdversario[jogadaAdversario - 1][7] + ">;";
		}
		if (splited[1].equals("<result>") && !splited[2].equals("<ack>;")) {
			matrizJogoRede[minhaJogada - 1][4] = "" + splited[2].charAt(1);
			matrizJogoRede[minhaJogada - 1][5] = "" + splited[2].charAt(3);
			matrizJogoRede[minhaJogada - 1][6] = "" + splited[2].charAt(5);
			matrizJogoRede[minhaJogada - 1][7] = "" + splited[2].charAt(7);

			Jogada jogada = new Jogada(idJogo,
					"<" + nomeLogin + "> <result> <" + matrizJogoRede[minhaJogada - 1][4] + ","
							+ matrizJogoRede[minhaJogada - 1][5] + "," + matrizJogoRede[minhaJogada - 1][6] + ","
							+ matrizJogoRede[minhaJogada - 1][7] + ">;");
			aJogada.registaJogada(jogada, st);
			mostraMatrizes();
			if (ganhou) {
				if (splited[2].equals("<P,P,P,P>;")) {
					ganhou = false;
					System.out.println("\nOcorreu um empate!\n");
					registaLog("Empatou um jogo em rede sendo o cliente contra o adversário com login: "
							+ loginAdversario + ".");
					System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
					if (aJogador.actualizaNumeroJogos(nomeLogin, st)
							&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
						System.out.println(
								"\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
					} else {
						System.out.println(
								"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
					}
					jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <draw>;");
					aJogada.registaJogada(jogada, st);
					return "<" + nomeLogin + "> <status> <draw>;";
				}
				ganhou = false;
				System.out.println("\nPerdeu o jogo!\n");
				registaLog("Perdeu um jogo em rede sendo o cliente contra o adversário com login: " + loginAdversario
						+ ".");
				if (aJogador.actualizaNumeroJogos(nomeLogin, st)
						&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
					System.out.println("\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
				} else {
					System.out.println(
							"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
				}
				jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <defeat>;");
				aJogada.registaJogada(jogada, st);
				return "<" + nomeLogin + "> <status> <win>;";
			}
			System.out.println("\nÉ a vez do adversário [" + nomeAdversario + "]!\n");
			return "<" + nomeLogin + "> <result> <ack>;";
		}
		if (splited[1].equals("<result>") && splited[2].equals("<ack>;")) {
			mostraMatrizes();
			if (jogadaAdversario == 10 && minhaJogada == 10) {
				System.out.println("\nOcorreu um empate!\n");
				registaLog("Empatou um jogo em rede sendo o cliente contra o adversário com login: " + loginAdversario
						+ ".");
				System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
				if (aJogador.actualizaNumeroJogos(nomeLogin, st)
						&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
					System.out.println("\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
				} else {
					System.out.println(
							"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
				}
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <draw>;");
				aJogada.registaJogada(jogada, st);
				return "<" + nomeLogin + "> <status> <draw>;";
			} else {
				System.out.println("\nÉ a sua vez!\n");
				return jogaJogada(idJogo);
			}
		}
		if (splited[1].equals("<withdraw>") && splited[2].equals("<ack>;")) {
			return "<" + nomeLogin + "> <ready>;";
		}
		if (splited[1].equals("<status>") && splited[2].equals("<win>;")) {
			matrizJogoRede[minhaJogada - 1][4] = "P";
			matrizJogoRede[minhaJogada - 1][5] = "P";
			matrizJogoRede[minhaJogada - 1][6] = "P";
			matrizJogoRede[minhaJogada - 1][7] = "P";
			Jogada jogada = new Jogada(idJogo,
					"<" + nomeLogin + "> <result> <" + matrizJogoRede[minhaJogada - 1][4] + ","
							+ matrizJogoRede[minhaJogada - 1][5] + "," + matrizJogoRede[minhaJogada - 1][6] + ","
							+ matrizJogoRede[minhaJogada - 1][7] + ">;");
			aJogada.registaJogada(jogada, st);
			mostraMatrizes();
			System.out.println("\nParabéns!!! Ganhou o encontro!\n");
			registaLog(
					"Ganhou um jogo em rede sendo o cliente contra o adversário com login: " + loginAdversario + ".");
			msgParabens();
			System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
			String loginvit = aJogador.jogadorComMaisVit(st);
			if (aJogador.actualizaNumeroJogos(nomeLogin, st) && aJogador.actualizaNumeroVitorias(nomeLogin, st)
					&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
				System.out.println(
						"\nO seu número de jogos, número de vitórias e tempo total acumulado no jogo foram actualizados!\n");
				int numVitorias = aJogador.obtemNumVitorias(nomeLogin, st);
				if (numVitorias % 10 == 0) {
					System.out.println("\nParabéns! Atingiu as " + numVitorias + " vitórias.\n");
					aNotificacao.registaNotificacaoAdmin(
							"O utilizador com o login: " + nomeLogin + " atingiu as " + numVitorias + " vitórias.", st);
				}
				if (!loginvit.equals(nomeLogin) && aJogador.jogadorComMaisVit(st).equals(nomeLogin)) {
					System.out.println("\nParabéns! Subiu ao primeiro lugar em número de vitórias!\n");
					SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					aNotificacao.registaNotificacaoAdmin("O utilizador com o login: " + nomeLogin + " é neste momento ("
							+ format.format(new Date()) + ") o jogador com mais vitórias (" + numVitorias
							+ " vitórias) na lista de jogadores.", st);
				}
			} else {
				System.out.println(
						"\nOcorreu um erro a actualizar o número de jogos, número de vitórias e tempo total acumulado no jogo!\n");
			}
			jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <win>;");
			aJogada.registaJogada(jogada, st);
			return "<" + nomeLogin + "> <status> <ok>;";
		}
		if (splited[1].equals("<status>") && splited[2].equals("<draw>;")) {
			if (minhaJogada != 10 && jogadaAdversario != 10) {
				matrizJogoRede[minhaJogada - 1][4] = "P";
				matrizJogoRede[minhaJogada - 1][5] = "P";
				matrizJogoRede[minhaJogada - 1][6] = "P";
				matrizJogoRede[minhaJogada - 1][7] = "P";
				Jogada jogada = new Jogada(idJogo,
						"<" + nomeLogin + "> <result> <" + matrizJogoRede[minhaJogada - 1][4] + ","
								+ matrizJogoRede[minhaJogada - 1][5] + "," + matrizJogoRede[minhaJogada - 1][6] + ","
								+ matrizJogoRede[minhaJogada - 1][7] + ">;");
				aJogada.registaJogada(jogada, st);
				mostraMatrizes();
			}
			System.out.println("\nOcorreu um empate!\n");
			registaLog(
					"Empatou um jogo em rede sendo o cliente contra o adversário com login: " + loginAdversario + ".");
			System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
			if (aJogador.actualizaNumeroJogos(nomeLogin, st)
					&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
				System.out.println("\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
			} else {
				System.out
						.println("\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
			}
			Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <draw>;");
			aJogada.registaJogada(jogada, st);
			return "<" + nomeLogin + "> <status> <ok>;";
		}
		if (splited[1].equals("<status>") && splited[2].equals("<ok>;")) {
			return "<" + nomeLogin + "> <status> <ack>;";
		}
		if (splited[1].equals("<new>") && splited[2].equals("<?>;")) {
			System.out.println("\nQuer fazer um novo jogo? (0 = Não | 1 = Sim)");
			if (leNovoJogo("Introduza a sua opcção: ") == 0) {
				System.out.println("\nAté breve!\n");
				ganhou = false;
				resposta = false;
				return "<" + nomeLogin + "> <new> <n>;";
			} else {
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 8; j++) {
						matrizJogoRede[i][j] = " ";
						matrizAdversario[i][j] = " ";
					}
				}
				minhaJogada = 0;
				jogadaAdversario = 0;
				tempoTotalJogada = 0;
				resposta = false;
				ganhou = false;
				System.out.println("\nEscolha de novo as suas cores:\n");
				cor = escolheCores();
				aTempoInicioJogo = obtemTempoInicio();
				Jogo jogo1 = new Jogo(aUtilizador.getIdUser(st, nomeLogin), aTempoInicioJogo);
				if (aJogo.registaJogo(jogo1, st)) {
					idJogo = aJogo.verificaQuantidadeJogo("", st);
					Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <linhas> 10;");
					aJogada.registaJogada(jogada, st);
				} else {
					idJogo = -1;
					System.out.println(
							"\nOcorreu um erro a gravar o jogo e, por isso, o mesmo não poderá ser revisto.\n");
				}
				registaLog("Jogou um jogo em rede sendo o cliente contra o adversário com login: " + loginAdversario
						+ ".");
				return "<" + nomeLogin + "> <new> <y>;";
			}
		}
		return null;
	}

	/**
	 * Função que trata das mensagens recebidas por parte do cliente.
	 * 
	 * @param aTempoInicioJogo
	 * @param aMensagem
	 * @return mesangem para o servidor.
	 */
	private static String trataMensagemServidor(String aTempoInicioJogo, String aMensagem) {
		String[] splited = aMensagem.split("\\s+");
		if (splited[1].equals("<start>")) {
			if (splited[2].equals("<ok>;")) {
				mostraMatrizes();
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <nomeAdv> <" + nomeAdversario + ">;");
				aJogada.registaJogada(jogada, st);
				System.out.println(
						"\nVocê é o primeiro a jogar! Tem 50 segundos para efectuar a jogada, se passar 50 segundos e não tiver efectuado a jogada, irá perder o jogo!\n");
				return jogaJogada(idJogo);
			}
		}
		if (splited[1].equals("<play>")) {
			matrizAdversario[jogadaAdversario][0] = "" + splited[2].charAt(1);
			matrizAdversario[jogadaAdversario][1] = "" + splited[2].charAt(3);
			matrizAdversario[jogadaAdversario][2] = "" + splited[2].charAt(5);
			matrizAdversario[jogadaAdversario][3] = "" + splited[2].charAt(7);
			Jogada jogada = new Jogada(idJogo,
					"<" + nomeLogin + "> <playadv> <" + matrizAdversario[jogadaAdversario][0] + ","
							+ matrizAdversario[jogadaAdversario][1] + "," + matrizAdversario[jogadaAdversario][2] + ","
							+ matrizAdversario[jogadaAdversario][3] + ">;");
			aJogada.registaJogada(jogada, st);
			if (introduzResultado(matrizAdversario, jogadaAdversario, cor)) {
				ganhou = true;
				matrizAdversario[jogadaAdversario][4] = "P";
				matrizAdversario[jogadaAdversario][5] = "P";
				matrizAdversario[jogadaAdversario][6] = "P";
				matrizAdversario[jogadaAdversario][7] = "P";
				jogadaAdversario++;
				if (minhaJogada == jogadaAdversario) {
					if (matrizJogoRede[minhaJogada - 1][4].equals("P") && matrizJogoRede[minhaJogada - 1][5].equals("P")
							&& matrizJogoRede[minhaJogada - 1][6].equals("P")
							&& matrizJogoRede[minhaJogada - 1][7].equals("P")) {
						ganhou = false;
						mostraMatrizes();
						System.out.println("\nOcorreu um empate!\n");
						registaLog("Empatou um jogo em rede sendo o servidor contra o adversário com login: "
								+ loginAdversario + ".");
						System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
						if (aJogador.actualizaNumeroJogos(nomeLogin, st)
								&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
							System.out.println(
									"\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
						} else {
							System.out.println(
									"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
						}
						jogada = new Jogada(idJogo,
								"<" + nomeLogin + "> <resultadv> <" + matrizAdversario[jogadaAdversario - 1][4] + ","
										+ matrizAdversario[jogadaAdversario - 1][5] + ","
										+ matrizAdversario[jogadaAdversario - 1][6] + ","
										+ matrizAdversario[jogadaAdversario - 1][7] + ">;");
						aJogada.registaJogada(jogada, st);
						jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <draw>;");
						aJogada.registaJogada(jogada, st);
						return "<" + nomeLogin + "> <status> <draw>;";
					}
					ganhou = false;
					mostraMatrizes();
					System.out.println("\nPerdeu o jogo!\n");
					registaLog("Perdeu um jogo em rede sendo o servidor contra o adversário com login: "
							+ loginAdversario + ".");
					System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
					if (aJogador.actualizaNumeroJogos(nomeLogin, st)
							&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
						System.out.println(
								"\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
					} else {
						System.out.println(
								"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
					}
					jogada = new Jogada(idJogo,
							"<" + nomeLogin + "> <resultadv> <" + matrizAdversario[jogadaAdversario - 1][4] + ","
									+ matrizAdversario[jogadaAdversario - 1][5] + ","
									+ matrizAdversario[jogadaAdversario - 1][6] + ","
									+ matrizAdversario[jogadaAdversario - 1][7] + ">;");
					aJogada.registaJogada(jogada, st);
					jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <defeat>;");
					aJogada.registaJogada(jogada, st);
					return "<" + nomeLogin + "> <status> <win>;";
				}
			} else {
				jogadaAdversario++;
			}
			jogada = new Jogada(idJogo, "<" + nomeLogin + "> <resultadv> <" + matrizAdversario[jogadaAdversario - 1][4]
					+ "," + matrizAdversario[jogadaAdversario - 1][5] + "," + matrizAdversario[jogadaAdversario - 1][6]
					+ "," + matrizAdversario[jogadaAdversario - 1][7] + ">;");
			aJogada.registaJogada(jogada, st);
			return "<" + nomeLogin + "> <result> <" + matrizAdversario[jogadaAdversario - 1][4] + ","
					+ matrizAdversario[jogadaAdversario - 1][5] + "," + matrizAdversario[jogadaAdversario - 1][6] + ","
					+ matrizAdversario[jogadaAdversario - 1][7] + ">;";
		}
		if (splited[1].equals("<result>") && !splited[2].equals("<ack>;")) {
			matrizJogoRede[minhaJogada - 1][4] = "" + splited[2].charAt(1);
			matrizJogoRede[minhaJogada - 1][5] = "" + splited[2].charAt(3);
			matrizJogoRede[minhaJogada - 1][6] = "" + splited[2].charAt(5);
			matrizJogoRede[minhaJogada - 1][7] = "" + splited[2].charAt(7);

			Jogada jogada = new Jogada(idJogo,
					"<" + nomeLogin + "> <result> <" + matrizJogoRede[minhaJogada - 1][4] + ","
							+ matrizJogoRede[minhaJogada - 1][5] + "," + matrizJogoRede[minhaJogada - 1][6] + ","
							+ matrizJogoRede[minhaJogada - 1][7] + ">;");
			aJogada.registaJogada(jogada, st);

			mostraMatrizes();
			if (ganhou) {
				if (splited[2].equals("<P,P,P,P>;")) {
					ganhou = false;
					System.out.println("\nOcorreu um empate!\n");
					registaLog("Empatou um jogo em rede sendo o servidor contra o adversário com login: "
							+ loginAdversario + ".");
					System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
					if (aJogador.actualizaNumeroJogos(nomeLogin, st)
							&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
						System.out.println(
								"\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
					} else {
						System.out.println(
								"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
					}
					jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <draw>;");
					aJogada.registaJogada(jogada, st);
					return "<" + nomeLogin + "> <status> <draw>;";
				}
				ganhou = false;
				System.out.println("\nPerdeu o jogo!\n");
				registaLog("Perdeu um jogo em rede sendo o servidor contra o adversário com login: " + loginAdversario
						+ ".");
				System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
				if (aJogador.actualizaNumeroJogos(nomeLogin, st)
						&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
					System.out.println("\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
				} else {
					System.out.println(
							"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
				}
				jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <defeat>;");
				aJogada.registaJogada(jogada, st);
				return "<" + nomeLogin + "> <status> <win>;";
			}
			System.out.println("\nÉ a vez do adversário [" + nomeAdversario + "]!\n");
			return "<" + nomeLogin + "> <result> <ack>;";
		}
		if (splited[1].equals("<result>") && splited[2].equals("<ack>;")) {
			mostraMatrizes();
			if (jogadaAdversario == 10 && minhaJogada == 10) {
				System.out.println("\nOcorreu um empate!\n");
				registaLog("Empatou um jogo em rede sendo o servidor contra o adversário com login: " + loginAdversario
						+ ".");
				System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
				if (aJogador.actualizaNumeroJogos(nomeLogin, st)
						&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
					System.out.println("\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
				} else {
					System.out.println(
							"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
				}
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <draw>;");
				aJogada.registaJogada(jogada, st);
				return "<" + nomeLogin + "> <status> <draw>;";
			} else {
				System.out.println("\nÉ a sua vez!\n");
				return jogaJogada(idJogo);
			}
		}
		if (splited[1].equals("<status>") && splited[2].equals("<win>;")) {
			matrizJogoRede[minhaJogada - 1][4] = "P";
			matrizJogoRede[minhaJogada - 1][5] = "P";
			matrizJogoRede[minhaJogada - 1][6] = "P";
			matrizJogoRede[minhaJogada - 1][7] = "P";
			Jogada jogada = new Jogada(idJogo,
					"<" + nomeLogin + "> <result> <" + matrizJogoRede[minhaJogada - 1][4] + ","
							+ matrizJogoRede[minhaJogada - 1][5] + "," + matrizJogoRede[minhaJogada - 1][6] + ","
							+ matrizJogoRede[minhaJogada - 1][7] + ">;");
			aJogada.registaJogada(jogada, st);
			mostraMatrizes();
			System.out.println("\nParabéns!!! Ganhou o encontro!\n");
			registaLog(
					"Ganhou um jogo em rede sendo o servidor contra o adversário com login: " + loginAdversario + ".");
			msgParabens();
			System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
			String loginvit = aJogador.jogadorComMaisVit(st);
			if (aJogador.actualizaNumeroJogos(nomeLogin, st) && aJogador.actualizaNumeroVitorias(nomeLogin, st)
					&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
				System.out.println(
						"\nO seu número de jogos, número de vitórias e tempo total acumulado no jogo foram actualizados!\n");
				int numVitorias = aJogador.obtemNumVitorias(nomeLogin, st);
				if (numVitorias % 10 == 0) {
					System.out.println("\nParabéns! Atingiu as " + numVitorias + " vitórias.\n");
					aNotificacao.registaNotificacaoAdmin(
							"O utilizador com o login: " + nomeLogin + " atingiu as " + numVitorias + " vitórias.", st);
				}
				if (!loginvit.equals(nomeLogin) && aJogador.jogadorComMaisVit(st).equals(nomeLogin)) {
					System.out.println("\nParabéns! Subiu ao primeiro lugar em número de vitórias!\n");
					SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					aNotificacao.registaNotificacaoAdmin("O utilizador com o login: " + nomeLogin + " é neste momento ("
							+ format.format(new Date()) + ") o jogador com mais vitórias (" + numVitorias
							+ " vitórias) na lista de jogadores.", st);
				}
			} else {
				System.out.println(
						"\nOcorreu um erro a actualizar o número de jogos, número de vitórias e tempo total acumulado no jogo!\n");
			}
			jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <win>;");
			aJogada.registaJogada(jogada, st);
			return "<" + nomeLogin + "> <status> <ok>;";
		}
		if (splited[1].equals("<status>") && splited[2].equals("<draw>;")) {
			if (minhaJogada != 10 && jogadaAdversario != 10) {
				matrizJogoRede[minhaJogada - 1][4] = "P";
				matrizJogoRede[minhaJogada - 1][5] = "P";
				matrizJogoRede[minhaJogada - 1][6] = "P";
				matrizJogoRede[minhaJogada - 1][7] = "P";
				Jogada jogada = new Jogada(idJogo,
						"<" + nomeLogin + "> <result> <" + matrizJogoRede[minhaJogada - 1][4] + ","
								+ matrizJogoRede[minhaJogada - 1][5] + "," + matrizJogoRede[minhaJogada - 1][6] + ","
								+ matrizJogoRede[minhaJogada - 1][7] + ">;");
				aJogada.registaJogada(jogada, st);
				matrizJogoRede[minhaJogada - 1][7] = "P";
				mostraMatrizes();
			}
			System.out.println("\nOcorreu um empate!\n");
			registaLog(
					"Empatou um jogo em rede sendo o servidor contra o adversário com login: " + loginAdversario + ".");
			System.out.println(calculaTempoJogada(aTempoInicioJogo, "\nO tempo total de jogo foi: "));
			if (aJogador.actualizaNumeroJogos(nomeLogin, st)
					&& aJogador.actualizaTempoJogador(calculaTempoCert(aTempoInicioJogo), nomeLogin, st)) {
				System.out.println("\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
			} else {
				System.out
						.println("\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
			}
			Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <draw>;");
			aJogada.registaJogada(jogada, st);
			return "<" + nomeLogin + "> <status> <ok>;";
		}
		if (splited[1].equals("<status>") && splited[2].equals("<ok>;")) {
			resposta = true;
			System.out.println("\nA perguntar ao cliente se quer fazer um novo jogo...\n");
			return "<" + nomeLogin + "> <new> <?>;";
		}
		if (splited[1].equals("<withdraw>") && splited[2].equals("<ack>;")) {
			resposta = true;
			System.out.println("\nA perguntar ao cliente se quer fazer um novo jogo...\n");
			return "<" + nomeLogin + "> <new> <?>;";
		}
		if (splited[1].equals("<status>") && splited[2].equals("<ack>;")) {
			resposta = true;
			System.out.println("\nA perguntar ao cliente se quer fazer um novo jogo...\n");
			return "<" + nomeLogin + "> <new> <?>;";
		}
		if (splited[1].equals("<new>") && splited[2].equals("<n>;")) {
			ganhou = false;
			resposta = false;
			System.out.println("\nO cliente não quer jogar outra vez consigo! :(\n");
			return "<" + nomeLogin + "> <bye>;";
		}
		if (splited[1].equals("<new>") && splited[2].equals("<y>;")) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 8; j++) {
					matrizJogoRede[i][j] = " ";
					matrizAdversario[i][j] = " ";
				}
			}
			minhaJogada = 0;
			jogadaAdversario = 0;
			tempoTotalJogada = 0;
			resposta = false;
			ganhou = false;
			System.out.println("\nO cliente quer jogar outra vez, escolha de novo as suas cores:\n");
			cor = escolheCores();
			aTempoInicioJogo = obtemTempoInicio();
			Jogo jogo1 = new Jogo(aUtilizador.getIdUser(st, nomeLogin), aTempoInicioJogo);
			if (aJogo.registaJogo(jogo1, st)) {
				idJogo = aJogo.verificaQuantidadeJogo("", st);
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <linhas> 10;");
				aJogada.registaJogada(jogada, st);
			} else {
				idJogo = -1;
				System.out.println("\nOcorreu um erro a gravar o jogo e, por isso, o mesmo não poderá ser revisto.\n");
			}
			int[] moeda = { 0, 1 };
			int escolha = moeda[new Random().nextInt(moeda.length)];
			if (escolha == 0) {
				mostraMatrizes();
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <nomeAdv> <" + nomeAdversario + ">;");
				aJogada.registaJogada(jogada, st);
				System.out.println("\nO seu adversário [" + nomeAdversario
						+ "] será o primeiro a jogar! Tem um limite de 50 segundos para fazer uma jogada, se demorar mais tempo irá perder o jogo!\n");
			}
			registaLog(
					"Jogou um jogo em rede sendo o servidor contra o adversário com login: " + loginAdversario + ".");
			return "<" + nomeLogin + "> <start> <" + escolha + ">;";
		}
		return null;
	}

	private static String jogaJogada(int idJogo) {
		int desistir = 0;
		String tempoInicioJogada = obtemTempoInicio();
		for (int j = 0; j < 4; j++) {
			int numTotal = 4 - j;
			System.out.println(
					"\n1- Vermelho, 2- Azul, 3- Rosa, 4- Castanho, 5- Laranja, 6- Magenta, 7- Desistir  | Já selecionou: "
							+ j + " cor(es). Falta selecionar: " + numTotal + " cor(es).");
			desistir = introduzCharRede(matrizJogoRede, minhaJogada, j);
			if (desistir == 1) {
				System.out.println("\nFraquinho, desistir é para os fracos!");
				registaLog("Desistiu do jogo em rede contra o adversário com login: " + loginAdversario + ".");
				return "<" + nomeLogin + "> <withdraw>;";
			}
		}
		minhaJogada++;
		tempoTotalJogada += devolveTempoMs(tempoInicioJogada);
		System.out.println("\nJá jogou " + minhaJogada + " jogada(s).");
		System.out.println(calculaTempoJogada(tempoInicioJogada, "Demorou este tempo a efectuar a jogada: "));
		System.out.println(calculaTempoTotalJogada(tempoTotalJogada, "O tempo total das jogadas é: "));
		System.out
				.println(calculaTempoTotalJogada(tempoTotalJogada / minhaJogada, "O tempo médio das suas jogadas é: "));
		Jogada jogada = new Jogada(idJogo,
				"<" + nomeLogin + "> <play> <" + matrizJogoRede[minhaJogada - 1][0] + ","
						+ matrizJogoRede[minhaJogada - 1][1] + "," + matrizJogoRede[minhaJogada - 1][2] + ","
						+ matrizJogoRede[minhaJogada - 1][3] + ">;");
		aJogada.registaJogada(jogada, st);
		return "<" + nomeLogin + "> <play> <" + matrizJogoRede[minhaJogada - 1][0] + ","
				+ matrizJogoRede[minhaJogada - 1][1] + "," + matrizJogoRede[minhaJogada - 1][2] + ","
				+ matrizJogoRede[minhaJogada - 1][3] + ">;";
	}

	/**
	 * Função que inicia/trata da ligação por parte do servidor.
	 */
	private static void jogaRedeServidor() {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		int porto = leInt("\nIntroduza o porto em que quer estar à escuta: ");
		boolean aceitou = false;
		try {
			serverSocket = new ServerSocket(porto);
			System.out.println("\nA aguardar a conexão de um adversário...\n");
			serverSocket.setSoTimeout(150000);
			clientSocket = serverSocket.accept();
			aceitou = true;
			System.out.println("\nO pedido do cliente foi aceite!\n");
			clientSocket.setSoTimeout(50000);
		} catch (SocketTimeoutException ste) {
			System.out.println(
					"\nPassaram 150 segundos e não houve pedido do cliente, o programa vai voltar ao menu anterior.\n");
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (aceitou) {
			PrintWriter out = null;
			BufferedReader in = null;
			try {
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 8; j++) {
						matrizJogoRede[i][j] = " ";
						matrizAdversario[i][j] = " ";
					}
				}
				minhaJogada = 0;
				jogadaAdversario = 0;
				ganhou = false;
				int contador = 0;
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String inputLine, outputLine;
				System.out.println("\nEstá ligado ao ip: " + clientSocket.getInetAddress() + "\n");
				System.out.println("\nEscolha, por favor, as suas cores:\n");
				cor = escolheCores();
				System.out.println(
						"\nEscolheu a combinação: " + cor[0] + ", " + cor[1] + ", " + cor[2] + ", " + cor[3] + ".");
				String tempoInicioJogo = obtemTempoInicio();
				Jogo jogo1 = new Jogo(aUtilizador.getIdUser(st, nomeLogin), tempoInicioJogo);
				if (aJogo.registaJogo(jogo1, st)) {
					idJogo = aJogo.verificaQuantidadeJogo("", st);
					Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <linhas> 10;");
					aJogada.registaJogada(jogada, st);
				} else {
					idJogo = -1;
					System.out.println(
							"\nOcorreu um erro a gravar o jogo e, por isso, o mesmo não poderá ser revisto.\n");
				}
				out.println("<" + nomeLogin + "> <hello> <" + nomeUtilizador + ">;");
				while ((inputLine = in.readLine()) != null) { // read
					if (inputLine.equals("<" + loginAdversario + "> <bye>;")) {
						break;
					}
					if (inputLine.equals("<" + loginAdversario + "> <withdraw>;")) {
						System.out.println("\nO seu adversário desistiu! Você ganhou o jogo devido a isso!\n");
						registaLog("Ganhou um jogo em rede sendo o servidor contra o adversário com login: "
								+ loginAdversario + ".");
						msgParabens();
						String loginvit = aJogador.jogadorComMaisVit(st);
						if (aJogador.actualizaNumeroJogos(nomeLogin, st)
								&& aJogador.actualizaNumeroVitorias(nomeLogin, st)) {
							System.out.println(
									"\nO seu número de jogos e número de vitórias no jogo foram actualizados (como o adversário desistiu, o seu tempo irá continuar igual)!\n");
							int numVitorias = aJogador.obtemNumVitorias(nomeLogin, st);
							if (numVitorias % 10 == 0) {
								System.out.println("\nParabéns! Atingiu as " + numVitorias + " vitórias.\n");
								aNotificacao.registaNotificacaoAdmin("O utilizador com o login: " + nomeLogin
										+ " atingiu as " + numVitorias + " vitórias.", st);
							}
							if (!loginvit.equals(nomeLogin) && aJogador.jogadorComMaisVit(st).equals(nomeLogin)) {
								System.out.println("\nParabéns! Subiu ao primeiro lugar em número de vitórias!\n");
								SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
								aNotificacao.registaNotificacaoAdmin(
										"O utilizador com o login: " + nomeLogin + " é neste momento ("
												+ format.format(new Date()) + ") o jogador com mais vitórias ("
												+ numVitorias + " vitórias) na lista de jogadores.",
										st);
							}
						} else {
							System.out.println(
									"\nOcorreu um erro a actualizar o número de jogos, número de vitórias e tempo total acumulado no jogo!\n");
						}
						Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <winDesistiu>;");
						aJogada.registaJogada(jogada, st);
						out.println("<" + loginAdversario + "> <withdraw> <ack>;");
					} else if (inputLine.equals("<" + loginAdversario + "> <ready>;")) {
						resposta = true;
						System.out.println("\nA perguntar ao cliente se quer fazer um novo jogo...\n");
						out.println("<" + nomeLogin + "> <new> <?>;");
					} else {
						if (contador == 0) {
							if (!trata1Mensagem(inputLine)) {
								out.println("<" + nomeLogin + "> <hello> <" + nomeUtilizador + ">;");
							} else {
								contador++;
								int[] moeda = { 0, 1 };
								int escolha = moeda[new Random().nextInt(moeda.length)];
								if (escolha == 0) {
									mostraMatrizes();
									Jogada jogada = new Jogada(idJogo,
											"<" + nomeLogin + "> <nomeAdv> <" + nomeAdversario + ">;");
									aJogada.registaJogada(jogada, st);
									registaLog("Jogou um jogo em rede sendo o servidor contra o adversário com login: "
											+ loginAdversario + ".");
									System.out.println("\nO seu adversário [" + nomeAdversario
											+ "] será o primeiro a jogar! Tem um limite de 50 segundos para fazer uma jogada, se demorar mais tempo irá perder o jogo!\n");
								}
								out.println("<" + nomeLogin + "> <start> <" + escolha + ">;");
							}
						} else {
							outputLine = trataMensagemServidor(tempoInicioJogo, inputLine);
							if (outputLine.equals("<" + nomeLogin + "> <withdraw>;")) {
								if (aJogador.actualizaNumeroJogos(nomeLogin, st) && aJogador
										.actualizaTempoJogador(calculaTempoCert(tempoInicioJogo), nomeLogin, st)) {
									System.out.println(
											"\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
								} else {
									System.out.println(
											"\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
								}
								Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <defeatD>;");
								aJogada.registaJogada(jogada, st);
							}
							if (outputLine.equals("<" + nomeLogin + "> <status> <ok>;")) {
								out.println(outputLine);
							} else {
								out.println(outputLine);
							}
						}
					}
				}
			} catch (SocketTimeoutException ste) {
				if (!resposta) {
					System.out
							.println("\nO adversário excedeu o tempo de resposta! Você ganhou o jogo devido a isso!\n");
					msgParabens();
					registaLog("Ganhou um jogo em rede sendo o cliente contra o adversário com login: "
							+ loginAdversario + ".");
					String loginvit = aJogador.jogadorComMaisVit(st);
					if (aJogador.actualizaNumeroJogos(nomeLogin, st)
							&& aJogador.actualizaNumeroVitorias(nomeLogin, st)) {
						System.out.println(
								"\nO seu número de jogos e número de vitórias no jogo foram actualizados (como o adversário deixou de responder, o seu tempo irá continuar igual)!\n");
						int numVitorias = aJogador.obtemNumVitorias(nomeLogin, st);
						if (numVitorias % 10 == 0) {
							System.out.println("\nParabéns! Atingiu as " + numVitorias + " vitórias.\n");
							aNotificacao.registaNotificacaoAdmin("O utilizador com o login: " + nomeLogin
									+ " atingiu as " + numVitorias + " vitórias.", st);
						}
						if (!loginvit.equals(nomeLogin) && aJogador.jogadorComMaisVit(st).equals(nomeLogin)) {
							System.out.println("\nParabéns! Subiu ao primeiro lugar em número de vitórias!\n");
							SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
							aNotificacao.registaNotificacaoAdmin(
									"O utilizador com o login: " + nomeLogin + " é neste momento ("
											+ format.format(new Date()) + ") o jogador com mais vitórias ("
											+ numVitorias + " vitórias) na lista de jogadores.",
									st);
						}
					} else {
						System.out.println(
								"\nOcorreu um erro a actualizar o número de jogos, número de vitórias e tempo total acumulado no jogo!\n");
					}
					Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <winD>;");
					aJogada.registaJogada(jogada, st);
				} else {
					System.out.println(
							"\nO adversário excedeu o tempo para responder à sua pergunta, o novo jogo será cancelado!\n");
				}
			} catch (SocketException se) {
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <erro>;");
				aJogada.registaJogada(jogada, st);
				registaLog("Ocorreu um erro no jogo em rede sendo o servidor contra o adversário com login: "
						+ loginAdversario + ".");
				System.out.println(
						"\nOcorreu um erro! Se o mesmo tiver sido por causa de ter excedido o tempo, será creditada a vitória ao adversário!\n");
			} catch (IOException ioe) {
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <erro>;");
				aJogada.registaJogada(jogada, st);
				registaLog("Ocorreu um erro no jogo em rede sendo o servidor contra o adversário com login: "
						+ loginAdversario + ".");
				ioe.printStackTrace();
			} catch (Exception e) {
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <erro>;");
				aJogada.registaJogada(jogada, st);
				registaLog("Ocorreu um erro no jogo em rede sendo o servidor contra o adversário com login: "
						+ loginAdversario + ".");
				e.printStackTrace();
			} finally {
				try {
					in.close();
					out.close();
					clientSocket.close();
					serverSocket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Trata da primeira mensagem
	 * 
	 * @param aMensagem
	 * @return boolean a indicar se recebeu a primeira mensagem.
	 */
	private static boolean trata1Mensagem(String aMensagem) {
		String[] splited = aMensagem.split("\\s+");
		if (splited[1].equals("<hello>")) {
			nomeAdversario = "";
			loginAdversario = "";
			for (int i = 1; i < splited[0].length() - 1; i++) {
				loginAdversario += splited[0].charAt(i);
			}
			for (int i = 1; i < (splited[2].length() - 2); i++) {
				nomeAdversario += splited[2].charAt(i);
			}
			System.out.print("Você [" + nomeUtilizador + "] está a jogar contra o jogador: " + nomeAdversario + ".\n");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Escolhe as cores do jogador
	 * 
	 * @return array com as cores escolhidas pelo jogador
	 */
	private static String[] escolheCores() {
		int[] cores = new int[4];
		int contador = 0, contador1 = 0;
		boolean seleccao = false;
		String[] corString = new String[4];
		while (!seleccao) {
			System.out.println("\n1- Vermelho, 2- Azul, 3- Rosa, 4- Castanho, 5- Laranja, 6- Magenta:");
			cores[contador] = leIntCor("\nEscolha as suas cores (não podem ser repetidas): ");
			if (contador > 0) {
				for (int i = 0; i < contador; i++) {
					if (cores[i] == cores[contador]) {
						System.out.println("\nNão pode escolher cores repetidas!\n");
						contador1++;
					}
				}
				if (contador1 == 0) {
					contador++;
				} else {
					contador1 = 0;
				}
			} else {
				contador++;
			}
			if (contador == 4) {
				seleccao = true;
			}
		}

		for (int i = 0; i < contador; i++) {
			switch (cores[i]) {
			case 1:
				corString[i] = "V";
				break;
			case 2:
				corString[i] = "A";
				break;
			case 3:
				corString[i] = "R";
				break;
			case 4:
				corString[i] = "C";
				break;
			case 5:
				corString[i] = "L";
				break;
			case 6:
				corString[i] = "M";
				break;
			}
		}
		return corString;
	}

	/**
	 * calcula o tempo que o jogador esteve em jogo
	 * 
	 * @param aIdCert
	 * @return tempo que o jogador esteve em jogo
	 */
	private static long calculaTempoCert(String aTempo) {
		String dataInicial = aTempo;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dataFinal = format.format(new Date());

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(dataInicial);
			d2 = format.parse(dataFinal);

			long diff = d2.getTime() - d1.getTime();

			return diff;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * calcula o tempo que decorreu uma jogada
	 * 
	 * @param aDataInicial
	 * @param aMsg
	 * @return tempo que decorreu a jogada
	 */
	private static String calculaTempoJogada(String aDataInicial, String aMsg) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dataFinal = format.format(new Date());

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(aDataInicial);
			d2 = format.parse(dataFinal);

			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;

			return aMsg + diffHours + "h:" + diffMinutes + "m:" + diffSeconds + "s.";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * calcula o tempo total das jogadas
	 * 
	 * @param aTempoTotalMs
	 * @param aMsg
	 * @return tempo total das jogadas
	 */
	private static String calculaTempoTotalJogada(long aTempoTotalMs, String aMsg) {

		try {

			long diffSeconds = aTempoTotalMs / 1000 % 60;
			long diffMinutes = aTempoTotalMs / (60 * 1000) % 60;
			long diffHours = aTempoTotalMs / (60 * 60 * 1000) % 24;

			return aMsg + diffHours + "h:" + diffMinutes + "m:" + diffSeconds + "s.";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * calcula o tempo total das jogadas
	 * 
	 * @param aTempoTotalMs
	 * @param aDataInicial
	 * @param aMsg
	 * @return tempo total das jogadas
	 */
	private static long devolveTempoMs(String aDataInicial) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dataFinal = format.format(new Date());

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(aDataInicial);
			d2 = format.parse(dataFinal);

			long diff = d2.getTime() - d1.getTime();

			return diff;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Vê horas de inicio do jogo
	 * 
	 * @return tempo de inicial do jogo
	 */
	private static String obtemTempoInicio() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * Menu de jogo contra o computador.
	 */
	private static void jogaComputador() {
		int numLinha = leIntLinha("Introduza o número de linhas que deseja (entre 10 e 20):"), idJogo = -1;
		long tempoTotalJogada = 0;
		String matrizJogo[][] = new String[numLinha][8];
		String tempoInicioJogo = obtemTempoInicio();

		for (int i = 0; i < numLinha; i++) {
			for (int j = 0; j < 8; j++) {
				matrizJogo[i][j] = " ";
			}
		}
		String coresEscolhidas[] = escolheRand();
		boolean jogo = true;
		int desistir = 0;
		int contadorLinha = 0;
		mostraMatriz(matrizJogo, numLinha);
		Jogo jogo1 = new Jogo(aUtilizador.getIdUser(st, nomeLogin), tempoInicioJogo);
		if (aJogo.registaJogo(jogo1, st)) {
			idJogo = aJogo.verificaQuantidadeJogo("", st);
			Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <linhas> " + numLinha);
			aJogada.registaJogada(jogada, st);
			jogada = new Jogada(idJogo, "<" + nomeLogin + "> <1> <jogopc>;");
			aJogada.registaJogada(jogada, st);
		} else {
			System.out.println("\nOcorreu um erro a gravar o jogo e, por isso, o mesmo não poderá ser revisto.\n");
		}
		while (jogo) {
			String tempoInicioJogada = obtemTempoInicio();
			for (int j = 0; j < 4; j++) {
				if (desistir != 1 && desistir != 2) {
					System.out.println(
							"\n1- Vermelho, 2- Azul, 3- Rosa, 4- Castanho, 5- Laranja, 6- Magenta, 7- Desistir, 8- Pedir dica. (Posição: "+j+")");
					desistir = introduzChar(matrizJogo, contadorLinha, j);
					if (desistir == 1) {
						registaLog("Desistiu contra o computador.");
						System.out.println("\nFraquinho, desistir é para os fracos!");
						if (idJogo != -1) {
							Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <desistiu>;");
							aJogada.registaJogada(jogada, st);
						}
						jogo = false;
						contadorLinha = numLinha;
					}
					if (desistir == 2) {
						matrizJogo = daDica(matrizJogo, contadorLinha, coresEscolhidas);
					}
				}
			}
			if (desistir != 1) {
				desistir = 0;
				introduzResultado(matrizJogo, contadorLinha, coresEscolhidas);
				mostraMatriz(matrizJogo, numLinha);
				if (idJogo != -1) {
					Jogada jogada = new Jogada(idJogo,
							"<" + nomeLogin + "> <play> <" + matrizJogo[contadorLinha][0] + ","
									+ matrizJogo[contadorLinha][1] + "," + matrizJogo[contadorLinha][2] + ","
									+ matrizJogo[contadorLinha][3] + ">;");
					aJogada.registaJogada(jogada, st);
					jogada = new Jogada(idJogo,
							"<" + nomeLogin + "> <result> <" + matrizJogo[contadorLinha][4] + ","
									+ matrizJogo[contadorLinha][5] + "," + matrizJogo[contadorLinha][6] + ","
									+ matrizJogo[contadorLinha][7] + ">;");
					aJogada.registaJogada(jogada, st);
				}
				jogo = verificaJogo(matrizJogo, contadorLinha);
			}
			if (jogo) {
				contadorLinha++;
			} else if (!jogo && desistir != 1) {
				contadorLinha++;
			}

			if (contadorLinha > 0 && desistir == 0) {
				tempoTotalJogada += devolveTempoMs(tempoInicioJogada);
				if (jogo) {
					System.out.println("\nJá jogou " + contadorLinha + " jogada(s).");
					System.out
							.println(calculaTempoJogada(tempoInicioJogada, "Demorou este tempo a efectuar a jogada: "));
					System.out.println(calculaTempoTotalJogada(tempoTotalJogada, "O tempo total das jogadas é: "));
					System.out.println(calculaTempoTotalJogada(tempoTotalJogada / contadorLinha,
							"O tempo médio das suas jogadas é: "));
				} else {
					System.out.println(
							calculaTempoJogada(tempoInicioJogada, "\nDemorou este tempo a efectuar a última jogada: "));
					System.out.println(calculaTempoTotalJogada(tempoTotalJogada, "O tempo total das jogadas foi: "));
					System.out.println(calculaTempoTotalJogada(tempoTotalJogada / contadorLinha,
							"O tempo médio das suas jogadas foi: "));
					System.out.println("\n\nParabéns!!! Ganhou o jogo!!\n");
					msgParabens();
					registaLog("Ganhou um jogo contra o computador.");
					if (idJogo != -1) {
						Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <win>;");
						aJogada.registaJogada(jogada, st);
					}
					System.out.println("\nPrecisou de " + contadorLinha + " jogada(s) para vencer o computador!");
					System.out.println(calculaTempoJogada(tempoInicioJogo, "\nO tempo total de jogo foi: "));
					contadorLinha--;
				}
			}
			if (contadorLinha == numLinha) {
				jogo = false;
			}
		}
		if (contadorLinha < numLinha) {
			String loginvit = aJogador.jogadorComMaisVit(st);
			if (aJogador.actualizaNumeroJogos(nomeLogin, st) && aJogador.actualizaNumeroVitorias(nomeLogin, st)
					&& aJogador.actualizaTempoJogador(calculaTempoCert(tempoInicioJogo), nomeLogin, st)) {
				System.out.println(
						"\nO seu número de jogos, número de vitórias e tempo total acumulado no jogo foram actualizados!\n");
				int numVitorias = aJogador.obtemNumVitorias(nomeLogin, st);
				if (numVitorias % 10 == 0) {
					System.out.println("\nParabéns! Atingiu as " + numVitorias + " vitórias.\n");
					aNotificacao.registaNotificacaoAdmin(
							"O utilizador com o login: " + nomeLogin + " atingiu as " + numVitorias + " vitórias.", st);
				}
				if (!loginvit.equals(nomeLogin) && aJogador.jogadorComMaisVit(st).equals(nomeLogin)) {
					System.out.println("\nParabéns! Subiu ao primeiro lugar em número de vitórias!\n");
					SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					aNotificacao.registaNotificacaoAdmin("O utilizador com o login: " + nomeLogin + " é neste momento ("
							+ format.format(new Date()) + ") o jogador com mais vitórias (" + numVitorias
							+ " vitórias) na lista de jogadores.", st);
				}
			} else {
				System.out.println(
						"\nOcorreu um erro a actualizar o número de jogos, número de vitórias e tempo total acumulado no jogo!\n");
			}
		} else if (desistir == 1) {
			System.out.println("\nA combinação era:  " + coresEscolhidas[0] + ", " + coresEscolhidas[1] + ", "
					+ coresEscolhidas[2] + ", " + coresEscolhidas[3] + " .\n");
			if (aJogador.actualizaNumeroJogos(nomeLogin, st)
					&& aJogador.actualizaTempoJogador(calculaTempoCert(tempoInicioJogo), nomeLogin, st)) {
				System.out.println("\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
			} else {
				System.out
						.println("\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
			}
		} else {
			if (idJogo != -1) {
				Jogada jogada = new Jogada(idJogo, "<" + nomeLogin + "> <status> <defeat>;");
				aJogada.registaJogada(jogada, st);
			}
			System.out.println("\nPerdeu o jogo contra o computador!!\n");
			registaLog("Perdeu um jogo contra o computador.");
			System.out.println("A combinação era:  " + coresEscolhidas[0] + ", " + coresEscolhidas[1] + ", "
					+ coresEscolhidas[2] + ", " + coresEscolhidas[3] + " .\n");
			if (aJogador.actualizaNumeroJogos(nomeLogin, st)
					&& aJogador.actualizaTempoJogador(calculaTempoCert(tempoInicioJogo), nomeLogin, st)) {
				System.out.println("\nO seu número de jogos e tempo total acumulado no jogo foram actualizados!\n");
			} else {
				System.out
						.println("\nOcorreu um erro a actualizar o número de jogos e tempo total acumulado no jogo!\n");
			}
		}
	}

	/**
	 * Dá uma dica.
	 * 
	 * @param matrizJogo
	 * @param numLinha
	 * @param coresEscolhidas
	 * @return String[][] matriz.
	 */
	private static String[][] daDica(String matrizJogo[][], int numLinha, String coresEscolhidas[]) {
		String dicaMatriz[] = new String[4];
		int contadorCor = 0;
		boolean dica = false;

		while (!dica) {
			for (int j = 0; j < 4; j++) {
				int rnd = new Random().nextInt(coresEscolhidas.length);
				dicaMatriz[j] = coresEscolhidas[rnd];
			}
			for (int i = 0; i < 4; i++) {
				if (dicaMatriz[i].equals(coresEscolhidas[i])) {
					contadorCor++;
				}
			}
			if (contadorCor < 3) {
				dica = true;
			} else {
				contadorCor = 0;
			}
		}
		for (int i = 0; i < 4; i++) {
			matrizJogo[numLinha][i] = dicaMatriz[i];
		}
		System.out.println("\nDica dada com sucesso!\n");
		return matrizJogo;
	}

	/**
	 * Verifica se ganhou o jogo.
	 * 
	 * @param matrizJogo
	 * @param numLinha
	 * @return false se ganhou, true se não.
	 */
	private static boolean verificaJogo(String matrizJogo[][], int numLinha) {
		int contador = 0;

		for (int i = 4; i < 8; i++) {
			if (matrizJogo[numLinha][i].equals("P")) {
				contador++;
			}
		}

		if (contador == 4) {
			numLinha++;
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Mostra a matriz no ecrã.
	 * 
	 * @param matrizJogo
	 * @param numLinha
	 */
	private static void mostraMatriz(String matrizJogo[][], int numLinha) {
		int contador = 0;
		System.out.println("\n Jogada     Resultado");
		for (int i = numLinha - 1; i >= 0; i--) {
			System.out.println("_________   _________");
			System.out.print("|");
			for (int j = 0; j < 8; j++) {
				if (contador == 4) {
					System.out.print("   |" + matrizJogo[i][j] + "|");
				} else {
					System.out.print(matrizJogo[i][j] + "|");
				}
				contador++;
			}
			contador = 0;
			System.out.println("");
		}
	}

	/**
	 * Mostra jogo em rede no ecrã.
	 */
	private static void mostraMatrizes() {
		int contador = 0;
		System.out.println("\n Minha Tabela    Resultado |  Tabela Adv.   Resultado");
		for (int i = 10 - 1; i >= 0; i--) {
			System.out.println("_________      _________        _________     _________");
			System.out.print("|");
			for (int j = 0; j < 8; j++) {
				if (contador == 4) {
					System.out.print("      |" + matrizJogoRede[i][j] + "|");
				} else {
					System.out.print(matrizJogoRede[i][j] + "|");
				}
				contador++;
			}
			contador = 0;
			System.out.print("        |");
			for (int j = 0; j < 8; j++) {
				if (contador == 4) {
					System.out.print("     |" + matrizAdversario[i][j] + "|");
				} else {
					System.out.print(matrizAdversario[i][j] + "|");
				}
				contador++;
			}
			contador = 0;
			System.out.println("");
		}
	}

	/**
	 * Escolhe as cores do computador alteatoriamente
	 * 
	 * @return String [] cores.
	 */
	private static String[] escolheRand() {
		int repeticao = 0, contadorCor = 0;
		String[] cores = { "V", "A", "R", "C", "L", "M" };
		String coresEscolhidas[] = new String[4];

		while (contadorCor <= 3) {
			int rnd = new Random().nextInt(cores.length);

			for (int j = 0; j < coresEscolhidas.length; j++) {
				if (coresEscolhidas[j] != null) {
					if (coresEscolhidas[j].equals(cores[rnd])) {
						repeticao++;
					}
				}
			}
			if (repeticao != 0) {
				repeticao = 0;
			} else {
				coresEscolhidas[contadorCor] = cores[rnd];
				contadorCor++;
			}
		}

		return coresEscolhidas;
	}

	/**
	 * Escolhe o número de linhas da matriz
	 * 
	 * @param aMsg
	 * @return número linhas da matriz.
	 */
	private static int leIntLinha(String aMsg) {

		do {
			String leitura = leString(aMsg);
			try {
				int a = Integer.parseInt(leitura);
				if (a >= 10 && a <= 20) {
					return a;
				} else {
					System.out.println("\nTem de introduzir um número inteiro entre 10 e 20!\n");
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Tem de digitar um inteiro!");
			}

		} while (true);
	}

	/**
	 * Introduz um char na matriz
	 * 
	 * @param matrizJogo
	 * @param numLinha
	 * @param numJogada
	 * @return
	 */
	private static int introduzChar(String matrizJogo[][], int numLinha, int numJogada) {

		int numLinha1 = numLinha + 1;
		int charEscolhido = leIntChar("Introduza a opcção (Jogada nº" + numLinha1 + "):");

		switch (charEscolhido) {
		case 1:
			matrizJogo[numLinha][numJogada] = "V";
			break;
		case 2:
			matrizJogo[numLinha][numJogada] = "A";
			break;
		case 3:
			matrizJogo[numLinha][numJogada] = "R";
			break;
		case 4:
			matrizJogo[numLinha][numJogada] = "C";
			break;
		case 5:
			matrizJogo[numLinha][numJogada] = "L";
			break;
		case 6:
			matrizJogo[numLinha][numJogada] = "M";
			break;
		case 7:
			return 1;
		case 8:
			return 2;
		default:
			System.out.println("\nNúmero inválido!\n");
			break;
		}
		return 0;
	}

	/**
	 * Introduz um char na matriz a jogar em rede
	 * 
	 * @param matrizJogo
	 * @param numLinha
	 * @param numJogada
	 * @return
	 */
	private static int introduzCharRede(String matrizJogo[][], int numLinha, int numJogada) {

		int charEscolhido = leIntCharRede("Introduza a opcção:");

		switch (charEscolhido) {
		case 1:
			matrizJogo[numLinha][numJogada] = "V";
			break;
		case 2:
			matrizJogo[numLinha][numJogada] = "A";
			break;
		case 3:
			matrizJogo[numLinha][numJogada] = "R";
			break;
		case 4:
			matrizJogo[numLinha][numJogada] = "C";
			break;
		case 5:
			matrizJogo[numLinha][numJogada] = "L";
			break;
		case 6:
			matrizJogo[numLinha][numJogada] = "M";
			break;
		case 7:
			return 1;
		default:
			System.out.println("\nIntroduziu um número invalido!\n");
			break;
		}
		return 0;
	}

	/**
	 * Mensagem de parabéns.
	 */
	private static void msgParabens() {
		System.out.println(" __      __                                     __     \r\n"
				+ "/\\ \\  __/\\ \\  __                               /\\ \\    \r\n"
				+ "\\ \\ \\/\\ \\ \\ \\/\\_\\    ___     ___      __   _ __\\ \\ \\   \r\n"
				+ " \\ \\ \\ \\ \\ \\ \\/\\ \\ /' _ `\\ /' _ `\\  /'__`\\/\\`'__\\ \\ \\  \r\n"
				+ "  \\ \\ \\_/ \\_\\ \\ \\ \\/\\ \\/\\ \\/\\ \\/\\ \\/\\  __/\\ \\ \\/ \\ \\_\\ \r\n"
				+ "   \\ `\\___x___/\\ \\_\\ \\_\\ \\_\\ \\_\\ \\_\\ \\____\\\\ \\_\\  \\/\\_\\\r\n"
				+ "    '\\/__//__/  \\/_/\\/_/\\/_/\\/_/\\/_/\\/____/ \\/_/   \\/_/\r\n"
				+ "                                                       \r\n"
				+ "                                                       ");
	}

	/**
	 * Introduz o resultado na matriz
	 * 
	 * @param matrizJogo
	 * @param numLinha
	 * @param escolha
	 */
	private static boolean introduzResultado(String matrizJogo[][], int numLinha, String escolha[]) {
		int contador = 0, contador1 = 4, caracter[] = new int[4], contadorP = 0;
		String resultado[] = new String[4];

		for (int i = 0; i < caracter.length; i++) {
			caracter[i] = -1;
		}

		for (int i = 0; i < 4; i++) {
			if (matrizJogo[numLinha][i].equals(escolha[i])) {
				resultado[contador] = "P";
				contador++;
				caracter[i]++;
			}
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < escolha.length; j++) {
				if (i != j && contador < 4) {
					if (matrizJogo[numLinha][i].equals(escolha[j]) && caracter[j] == -1) {
						resultado[contador] = "B";
						caracter[j]++;
						contador++;
					}
				}
			}
		}

		if (contador < 4) {
			for (int i = contador; i < 4; i++) {
				resultado[contador] = "X";
				contador++;
			}
		}

		for (int i = 0; i < 4; i++) {
			if (resultado[i].equals("P")) {
				matrizJogo[numLinha][contador1] = "P";
				contador1++;
				contadorP++;
			}
		}

		for (int i = 0; i < 4; i++) {
			if (resultado[i].equals("B")) {
				matrizJogo[numLinha][contador1] = "B";
				contador1++;
			}
		}

		for (int i = 0; i < 4; i++) {
			if (resultado[i].equals("X")) {
				matrizJogo[numLinha][contador1] = "X";
				contador1++;
			}
		}

		if (contadorP == 4) {
			return true;
		}

		return false;
	}

	/**
	 * Verifica as cores
	 * 
	 * @param aMsg
	 * @return opcção
	 */
	private static int leIntCor(String aMsg) {

		do {
			String leitura = leString(aMsg);
			try {
				int a = Integer.parseInt(leitura);
				if (a > 0 && a <= 6) {
					return a;
				} else {
					System.out.println("\nTem de introduzir um número inteiro entre 1 e 6!\n");
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Tem de digitar um inteiro!");
			}

		} while (true);
	}

	/**
	 * Verifica opcções do jogador
	 * 
	 * @param aMsg
	 * @return opcção
	 */
	private static int leIntChar(String aMsg) {

		do {
			String leitura = leString(aMsg);
			try {
				int a = Integer.parseInt(leitura);
				if (a > 0 && a <= 8) {
					return a;
				} else {
					System.out.println("\nTem de introduzir um número inteiro entre 1 e 8!\n");
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Tem de digitar um inteiro!");
			}

		} while (true);
	}

	/**
	 * Verifica opcções do jogador
	 * 
	 * @param aMsg
	 * @return opcção
	 */
	private static int leNovoJogo(String aMsg) {

		do {
			String leitura = leString(aMsg);
			try {
				int a = Integer.parseInt(leitura);
				if (a >= 0 && a <= 1) {
					return a;
				} else {
					System.out.println("\nSó pode introduzir 0 ou 1!\n");
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Tem de digitar um inteiro!");
			}

		} while (true);
	}

	/**
	 * Verifica opcções do jogador em rede
	 * 
	 * @param aMsg
	 * @return opcção
	 */
	private static int leIntCharRede(String aMsg) {

		do {
			String leitura = leString(aMsg);
			try {
				int a = Integer.parseInt(leitura);
				if (a > 0 && a <= 7) {
					return a;
				} else {
					System.out.println("\nTem de introduzir um número inteiro entre 1 e 7!\n");
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Tem de digitar um inteiro!");
			}

		} while (true);
	}

	/**
	 * Menu gestão utilizador jogador.
	 */
	private static void menuGestaoUtilizadorJogador() {
		int opccao;

		do {
			System.out.println(
					"\n1- Listar os meus dados de utilizador.\n2- Alterar os meus dados de utilizador.\n3- Listar jogadores.\n4- Inactivar a minha conta.\n5- Voltar ao menu anterior.");
			opccao = leInt("\nIntroduza a sua opcção:");

			switch (opccao) {
			case 1:
				System.out.println("\nA listar os seus dados de utilizador:\n");
				System.out.println(aJogador.listaDadosJogador(nomeLogin, st));
				registaLog("Listou os seus dados de utilizador.");
				break;
			case 2:
				alteraDadosJogador(nomeLogin);
				break;
			case 3:
				menuListagemJogador1();
				break;
			case 4:
				if (leNovoJogo("\nTem a certeza que deseja inactivar a sua conta (0 = Não | 1 = Sim)?") == 1) {
					if (aUtilizador.inactivaconta(nomeLogin, st)) {
						aNotificacao.registaNotificacaoAdmin(
								"O utilizador com o login: " + nomeLogin + " inactivou a sua conta.", st);
						registaLog("Inactivou a sua conta.");
						System.out.println(
								"\nA sua conta foi inactiva com sucesso! Verá as alterações depois de efectuar o 'logout'/sair do programa.\n");
					} else {
						System.out.println("\nOcorreu um erro a inactivar a sua conta, por favor tente novamente!\n");
						registaLog("Tentou inactivar a sua conta, mas ocorreu um erro.");
					}
				} else {
					System.out.println("\nOperação abortada com sucesso!\n");
				}
				break;
			case 5:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 5);
	}

	/**
	 * Lista utilizadores que aguardam aprovação, 10 a 10.
	 */
	private static void listaUser10Aprovacao() {
		int numTotal = aUtilizador.verificaListaInactivo(st), numVezes = 0;

		if (numTotal > 10) {
			System.out.println("\nForam encontrados " + numTotal + " resultados.");
			numVezes = (int) Math.ceil(numTotal / 10.0);
			for (int i = 0; i < numVezes; i++) {
				System.out.println("\n" + aUtilizador.listaUserInactivo(i * 10 + ";", st));
				if (i < (numVezes - 1)) {
					leString("Digite qualquer coisa para ver a próxima página de resultados.");
				}
			}
		} else {
			if (numTotal == 1) {
				System.out.println("\nFoi encontrado 1 resultado.");
			} else {
				System.out.println("\nForam encontrados " + numTotal + " resultados.");
			}
			System.out.println("\n" + aUtilizador.listaUserInactivo("0;", st));
		}
	}

	/**
	 * Menu gestão utilizador gestor.
	 */
	private static void menuGestaoUtilizadorAdmin() {
		int opccao;

		do {
			System.out.println(
					"\n1- Listar utilizador(es) que aguarda(m) aprovação.\n2- Listar os meus dados de utilizador.\n3- Aprovar utilizador/ Reprovar utilizador.\n"
							+ "4- Alterar dados de utilizador.\n5- Criar um novo utilizador.\n6- Listar jogadores.\n7- Pesquisar jogador(es) por nome.\n8- Listar todos os utilizadores.\n9- Voltar ao menu anterior.\n");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				System.out.println("\nLista de utilizadores a aguardar aprovação:");
				registaLog("Listou utilizadores que aguardam aprovação.");
				listaUser10Aprovacao();
				break;
			case 2:
				System.out.println("\nA listar os seus dados de utilizador:\n");
				System.out.println(aAdministrador.listaDadosAdmin(nomeLogin, st));
				registaLog("Listou os seus dados de utilizador.");
				break;
			case 3:
				menuAceitaReprovaUser();
				break;
			case 4:
				menuAlteraDadosAdmin();
				break;
			case 5:
				registaUser(true);
				break;
			case 6:
				menuListagemJogador();
				break;
			case 7:
				String nomePesquisa = (leString("Introduza o nome do jogador que deseja pesquisar: "));
				listaUser10TotalJog("where u.u_id = j.u_id and (u.u_name like '%" + nomePesquisa + "%' )",
						"j, utilizador u where u.u_id = j.u_id and (u.u_name like '%" + nomePesquisa + "%' )",
						"\nNão foi encontrado nenhum jogador com esse nome!\n");
				registaLog("Pesquisou um jogador por nome (" + nomePesquisa + ").");
				break;
			case 8:
				menuListagemUtilizadoresTotal();
				break;
			case 9:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 9);
	}

	/**
	 * Menu listagem de utilizadores.
	 */
	private static void menuListagemUtilizadoresTotal() {
		int opccao;

		do {
			System.out.println(
					"\n1- Listar todos os utilizadores.\n2- Listar todos os utilizadores por login asc.\n3- Listar todos os utilizadores por login desc.\n"
							+ "4- Listar todos os utilizadores por nome asc.\n5- Listar todos os utilizadores por nome desc.\n9- Voltar ao menu anterior.\n");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				listaUser10Total("", ";", "\nNão existem utilizadores na base de dados!\n");
				registaLog("Listou todos os utilizadores.");
				break;
			case 2:
				listaUser10Total("order by u_login", ";", "\nNão existem utilizadores na base de dados!\n");
				registaLog("Listou todos os utilizadores.");
				break;
			case 3:
				listaUser10Total("order by u_login desc", ";", "\nNão existem utilizadores na base de dados!\n");
				registaLog("Listou todos os utilizadores.");
				break;
			case 4:
				listaUser10Total("order by u_name", ";", "\nNão existem utilizadores na base de dados!\n");
				registaLog("Listou todos os utilizadores.");
				break;
			case 5:
				listaUser10Total("order by u_name desc", ";", "\nNão existem utilizadores na base de dados!\n");
				registaLog("Listou todos os utilizadores.");
				break;
			case 9:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 9);
	}

	/**
	 * Lista utilizadores.
	 * 
	 * @param aComando
	 * @param aComando2
	 * @param aMsg
	 */
	private static void listaUser10Total(String aComando, String aComando2, String aMsg) {
		int numTotal = aUtilizador.verificaListaVazia(st, aComando2), numVezes = 0;

		if (numTotal > 10) {
			System.out.println("\nForam encontrados " + numTotal + " resultados.");
			numVezes = (int) Math.ceil(numTotal / 10.0);
			for (int i = 0; i < numVezes; i++) {
				System.out.println("\n" + aUtilizador.listaUserTotais(i * 10 + ";", aComando, st, aMsg));
				if (i < (numVezes - 1)) {
					leString("Digite qualquer coisa para ver a próxima página de resultados.");
				}
			}
		} else {
			if (numTotal == 1) {
				System.out.println("\nFoi encontrado 1 resultado.");
			} else {
				System.out.println("\nForam encontrados " + numTotal + " resultados.");
			}
			System.out.println("\n" + aUtilizador.listaUserTotais("0;", aComando, st, aMsg));
		}
	}

	/**
	 * Menu de listagem de jogadores por parte do administrador.
	 */
	private static void menuListagemJogador() {
		int opccao;

		do {
			System.out.println(
					"\n1- Listar todos os jogadores por ordem alfabética asc.\n2- Listar todos os jogadores por ordem alfabética desc.\n3- Listar todos os jogadores por número de jogos asc.\n"
							+ "4- Listar todos os jogadores por número de jogos desc.\n5- Listar todos os jogadores por número de vitórias asc.\n6- Listar todos os jogadores por número de vitórias desc."
							+ "\n9- Voltar ao menu anterior.\n");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				listaUser10TotalJog("where u.u_id = j.u_id order by u_name", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 2:
				listaUser10TotalJog("where u.u_id = j.u_id order by u_name desc", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				;
				break;
			case 3:
				listaUser10TotalJog("where u.u_id = j.u_id order by j_numjogos ", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 4:
				listaUser10TotalJog("where u.u_id = j.u_id order by j_numjogos desc", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 5:
				listaUser10TotalJog("where u.u_id = j.u_id order by j_numvitorias ", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 6:
				listaUser10TotalJog("where u.u_id = j.u_id order by j_numvitorias desc", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 9:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 9);
	}

	/**
	 * Menu de listagem de jogadores por parte do jogador.
	 */
	private static void menuListagemJogador1() {
		int opccao;

		do {
			System.out.println(
					"\n1- Listar todos os jogadores por ordem alfabética asc.\n2- Listar todos os jogadores por ordem alfabética desc.\n3- Listar todos os jogadores por número de jogos asc.\n"
							+ "4- Listar todos os jogadores por número de jogos desc.\n5- Listar todos os jogadores por número de vitórias asc.\n6- Listar todos os jogadores por número de vitórias desc."
							+ "\n9- Voltar ao menu anterior.\n");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				listaUser10TotalJog1("where u.u_id = j.u_id order by u_name", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 2:
				listaUser10TotalJog1("where u.u_id = j.u_id order by u_name desc", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 3:
				listaUser10TotalJog1("where u.u_id = j.u_id order by j_numjogos ", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 4:
				listaUser10TotalJog1("where u.u_id = j.u_id order by j_numjogos desc", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 5:
				listaUser10TotalJog1("where u.u_id = j.u_id order by j_numvitorias ", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 6:
				listaUser10TotalJog1("where u.u_id = j.u_id order by j_numvitorias desc", ";",
						"\nNão existem jogadores na base de dados!\n");
				registaLog("Listou todos os jogadores.");
				break;
			case 9:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 9);
	}

	/**
	 * Lista/pesquisa os jogadores por parte do administrador.
	 * 
	 * @param aComando
	 * @param aComando2
	 * @param aMsg
	 */
	private static void listaUser10TotalJog(String aComando, String aComando2, String aMsg) {
		int numTotal = aJogador.verificaListaVazia(st, aComando2), numVezes = 0;

		if (numTotal > 10) {
			System.out.println("\nForam encontrados " + numTotal + " resultados.");
			numVezes = (int) Math.ceil(numTotal / 10.0);
			for (int i = 0; i < numVezes; i++) {
				System.out.println("\n" + aJogador.listaJogTotais(i * 10 + ";", aComando, st, aMsg));
				if (i < (numVezes - 1)) {
					leString("Digite qualquer coisa para ver a próxima página de resultados.");
				}
			}
		} else {
			if (numTotal == 1) {
				System.out.println("\nFoi encontrado 1 resultado.");
			} else {
				System.out.println("\nForam encontrados " + numTotal + " resultados.");
			}
			System.out.println("\n" + aJogador.listaJogTotais("0;", aComando, st, aMsg));
		}
	}

	/**
	 * Lista/pesquisa os jogadores por parte dos jogadores.
	 * 
	 * @param aComando
	 * @param aComando2
	 * @param aMsg
	 */
	private static void listaUser10TotalJog1(String aComando, String aComando2, String aMsg) {
		int numTotal = aJogador.verificaListaVazia(st, aComando2), numVezes = 0;

		if (numTotal > 10) {
			System.out.println("\nForam encontrados " + numTotal + " resultados.");
			numVezes = (int) Math.ceil(numTotal / 10.0);
			for (int i = 0; i < numVezes; i++) {
				System.out.println("\n" + aJogador.listaJogTotais1(i * 10 + ";", aComando, st, aMsg));
				if (i < (numVezes - 1)) {
					leString("Digite qualquer coisa para ver a próxima página de resultados.");
				}
			}
		} else {
			if (numTotal == 1) {
				System.out.println("\nFoi encontrado 1 resultado.");
			} else {
				System.out.println("\nForam encontrados " + numTotal + " resultados.");
			}
			System.out.println("\n" + aJogador.listaJogTotais1("0;", aComando, st, aMsg));
		}
	}

	/**
	 * Menu altera dados do administrador.
	 */
	private static void menuAlteraDadosAdmin() {
		int opccao;

		do {
			System.out.println(
					"\n1- Alterar os meus dados.\n2- Alterar dados de outros utilizadores.\n9- Voltar ao menu anterior.\n");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				alteraDadosAdmin(nomeLogin);
				break;
			case 2:
				String login = leString("Introduza o login do utilizador que deseja alterar:");
				if (login.equalsIgnoreCase(nomeLogin)) {
					alteraDadosAdmin(nomeLogin);
				} else {
					if (!aUtilizador.verificaLogin(login, st)) {
						int tipo = aUtilizador.verificaTipo(login, st);
						if (tipo == 1) {
							System.out.println("\nNão pode alterar dados de um utilizador do tipo Gestor!\n");
						} else if (tipo == 2) {
							alteraDadosJogadorAdmin(login);
						}
					} else {
						System.out.println("Esse login não existe na base de dados");
					}
				}
				break;
			case 9:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 9);
	}

	/**
	 * Altera dados do jogador pelo administrador.
	 * 
	 * @param aLogin
	 */
	private static void alteraDadosJogadorAdmin(String aLogin) {
		int opccao;

		do {
			System.out.println("\n1- Alterar login.\n2- Alterar nome.\n3- Alterar email.\n4- Alterar password.\n"
					+ "5- Alterar tipo.\n" + "9- Voltar ao menu anterior.\n");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				String loginNovo = leString("Introduza o novo login: ");
				if (!aUtilizador.verificaLogin(loginNovo, st)) {
					System.out.println("\nEsse login já existe na base de dados!");
				} else {
					System.out.println(aAdministrador.actualizaDadosAdmin("u_login = '" + loginNovo + "'", aLogin, st));
					registaLog("Alterou um login de " + aLogin + " para " + loginNovo + ".");
					if (nomeLogin.equals(aLogin)) {
						aLogin = loginNovo;
						nomeLogin = aLogin;
					} else {
						aLogin = loginNovo;
					}
				}
				break;
			case 2:
				String nome = leString("Introduza o novo nome: ");
				System.out.println(aAdministrador.actualizaDadosAdmin("u_nome = '" + nome + "'", aLogin, st));
				registaLog("Alterou o nome do jogador com login: " + aLogin + " para " + nome + ".");
				if (nomeLogin.equals(aLogin)) {
					nomeUtilizador = nome;
				}
				break;
			case 3:
				String emailNovo = verificaEmail();
				if (!aUtilizador.verificaEmail(emailNovo, st)) {
					System.out.println("\nEsse email já existe na base de dados!");
				} else {
					System.out.println(aAdministrador.actualizaDadosAdmin("u_email = '" + emailNovo + "'", aLogin, st));
					registaLog("Alterou o email do jogador com login: " + aLogin + " para " + emailNovo + ".");
				}
				break;
			case 4:
				System.out.println(aAdministrador.actualizaDadosAdmin(
						"u_password = '" + leString("Introduza a nova password: ") + "'", aLogin, st));
				registaLog("Alterou a password do jogador com login: " + aLogin + ".");
				break;
			case 5:
				if (nomeLogin.equals(aLogin)) {
					System.out.println("\nNão tem permissões para alterar o seu tipo, contacte um Administrador.");
				} else {
					char tipo = verificaTipo();
					if (tipo == 'A') {
						System.out.println(aAdministrador.actualizaDadosAdmin("u_tipo = '" + tipo + "'", aLogin, st));
						registaLog("Alterou o tipo do jogador com login: " + aLogin + " para Administrador.");
					} else if (tipo == 'J') {
						System.out.println(aAdministrador.actualizaDadosAdmin("u_tipo = '" + tipo + "'", aLogin, st));
					}
				}
				break;
			case 9:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 9);
	}

	/**
	 * Altera dados do jogador.
	 * 
	 * @param aLogin
	 */
	private static void alteraDadosJogador(String aLogin) {
		int opccao;

		do {
			System.out.println("\nA listar os seus dados de utilizador:\n");
			System.out.println(aJogador.listaDadosJogador(nomeLogin, st));
			System.out.println("\n1- Alterar login.\n2- Alterar nome.\n3- Alterar email.\n4- Alterar password.\n"
					+ "5- Voltar ao menu anterior.\n");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				String loginNovo = leString("Introduza o novo login: ");
				if (!aUtilizador.verificaLogin(loginNovo, st)) {
					System.out.println("\nEsse login já existe na base de dados!");
				} else {
					System.out.println(aAdministrador.actualizaDadosAdmin("u_login = '" + loginNovo + "'", aLogin, st));
					if (nomeLogin.equals(aLogin)) {
						aLogin = loginNovo;
						registaLog("Alterou o login para " + loginNovo + ".");
						nomeLogin = aLogin;
					} else {
						aLogin = loginNovo;
					}
				}
				break;
			case 2:
				String nome = leString("Introduza o novo nome: ");
				System.out.println(aAdministrador.actualizaDadosAdmin("u_nome = '" + nome + "'", aLogin, st));
				if (nomeLogin.equals(aLogin)) {
					registaLog("Alterou o nome para " + nome + ".");
					nomeUtilizador = nome;
				}
				break;
			case 3:
				String emailNovo = verificaEmail();
				if (!aUtilizador.verificaEmail(emailNovo, st)) {
					System.out.println("\nEsse email já existe na base de dados!");
				} else {
					System.out.println(aAdministrador.actualizaDadosAdmin("u_email = '" + emailNovo + "'", aLogin, st));
					registaLog("Alterou o email para " + emailNovo + ".");
				}
				break;
			case 4:
				System.out.println(aAdministrador.actualizaDadosAdmin(
						"u_password = '" + leString("Introduza a nova password: ") + "'", aLogin, st));
				registaLog("Alterou a password.");
				break;
			case 5:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 5);
	}

	/**
	 * Altera dados do administrador.
	 * 
	 * @param aLogin
	 */
	private static void alteraDadosAdmin(String aLogin) {
		int opccao;
		String resposta = "";

		do {
			System.out.println("\nA listar os seus dados de utilizador:\n");
			System.out.println(aAdministrador.listaDadosAdmin(nomeLogin, st));
			System.out.println(
					"\n1- Alterar login.\n2- Alterar nome.\n3- Alterar email\n4- Alterar password\n5- Alterar tipo\n9- Voltar ao menu anterior.\n");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				String loginNovo = leString("Introduza o novo login: ");
				if (!aUtilizador.verificaLogin(loginNovo, st)) {
					System.out.println("\nEsse login já existe na base de dados!");
				} else {
					resposta = aAdministrador.actualizaDadosAdmin("u_login = '" + loginNovo + "'", nomeLogin, st);
					if (resposta.equals("\nDados actualizados com sucesso.")) {
						registaLog("Alterou o login para " + loginNovo + ".");
						nomeLogin = loginNovo;
						System.out.println("\nDados actualizados com sucesso.");
					} else {
						System.out.println("\nOcorreu um erro a actualizar os dados.");
					}
				}
				break;
			case 2:
				String nomeNovo = leString("Introduza o novo nome: ");
				resposta = aAdministrador.actualizaDadosAdmin("u_nome = '" + nomeNovo + "'", nomeLogin, st);
				if (resposta.equals("\nDados actualizados com sucesso.")) {
					registaLog("Alterou o nome para " + nomeNovo + ".");
					nomeUtilizador = nomeNovo;
					System.out.println("\nDados actualizados com sucesso.");
				} else {
					System.out.println("\nOcorreu um erro a actualizar os dados.");
				}
				break;
			case 3:
				String emailNovo = verificaEmail();
				if (!aUtilizador.verificaEmail(emailNovo, st)) {
					System.out.println("\nEsse email já existe na base de dados!");
				} else {
					System.out.println(
							aAdministrador.actualizaDadosAdmin("u_email = '" + emailNovo + "'", nomeLogin, st));
					registaLog("Alterou o email para " + emailNovo + ".");
				}
				break;
			case 4:
				System.out.println(aAdministrador.actualizaDadosAdmin(
						"u_password = '" + leString("Introduza a nova password: ") + "'", nomeLogin, st));
				registaLog("Alterou a password.");
				break;
			case 5:
				char tipo = verificaTipo();
				if (tipo == 'A') {
					System.out.println(aAdministrador.actualizaDadosAdmin("u_tipo = '" + tipo + "'", nomeLogin, st));
				} else if (tipo == 'J') {
					System.out.println(aAdministrador.actualizaDadosAdmin("u_tipo = '" + tipo + "'", nomeLogin, st));
					registaLog("Mudou o tipo para Jogador.");
					System.out.println("As mudanças só irão surgir depois de fazer 'logout'.");
				}
				break;
			case 9:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 9);
	}

	/**
	 * Menu de aprovação/reprovação de utilizadores.
	 */
	private static void menuAceitaReprovaUser() {
		int opccao;

		do {
			System.out.println(
					"\n1- Aprovar um utilizador.\n2- Listar todos os utilizadores reprovados.\n3- Reprovar Utilizador\n"
							+ "4- Voltar ao menu anterior.");
			opccao = leInt("Introduza a sua opcção:");

			switch (opccao) {
			case 1:
				int contador = 0;
				if (aUtilizador.verificaListaInactivo(st) == 0) {
					System.out.println("\nNão existem utilizadores a aguardar aprovação.");
				} else {
					System.out.println("\nLista de utilizadores a aguardar aprovação:");
					listaUser10Aprovacao();
					contador++;
				}
				if (aUtilizador.verificaListaReprovado(st) == 0) {
					System.out.println("\nNão existem utilizadores reprovados.\n");
				} else {
					System.out.println("\nLista de utilizadores reprovados:");
					listaUser10Reprovacao();
					contador++;
				}
				if (contador > 0) {
					String loginApr = leString("Introduza o login do utilizador que deseja aprovar: ");
					System.out.println(aUtilizador.aprovaUtilizador(loginApr, st));
					registaLog("Aprovou o utilizador com login: " + loginApr + ".");
				} else {
					System.out.println("\nNão existe nenhum utilizadores para aprovar.\n");
				}
				break;
			case 2:
				System.out.println("\nLista de utilizadores reprovados:");
				listaUser10Reprovacao();
				registaLog("Listou os utilizadores reprovados.");
				break;
			case 3:
				if (aUtilizador.verificaListaReprovado1(st) == 0) {
					System.out.println("\nNão existem utilizadores não reprovados!");
				} else {
					System.out.println("\nLista de utilizadores não reprovados:");
					listaUser10Reprovacao1();
					String loginRepr = leString("Introduza o login do utilizador que deseja reprovar: ");
					System.out.println(aUtilizador.reprovaUtilizador(loginRepr, st));
					registaLog("Reprovou o utilizador com login: " + loginRepr + ".");
				}
				break;
			case 4:
				break;
			default:
				System.out.println("Inseriu uma opcção inválida.");
				break;
			}

		} while (opccao != 4);
	}

	/**
	 * Lista utilizadores que podem ser reprovados.
	 */
	private static void listaUser10Reprovacao1() {
		int numTotal = aUtilizador.verificaListaReprovado1(st), numVezes = 0;

		if (numTotal > 10) {
			System.out.println("\nForam encontrados " + numTotal + " resultados.");
			numVezes = (int) Math.ceil(numTotal / 10.0);
			for (int i = 0; i < numVezes; i++) {
				System.out.println("\n" + aUtilizador.listaUtilizadorReprovado1(i * 10 + ";", st));
				if (i < (numVezes - 1)) {
					leString("Digite qualquer coisa para ver a próxima página de resultados.");
				}
			}
		} else {
			if (numTotal == 1) {
				System.out.println("\nFoi encontrado 1 resultado.");
			} else {
				System.out.println("\nForam encontrados " + numTotal + " resultados.");
			}
			System.out.println("\n" + aUtilizador.listaUtilizadorReprovado1("0;", st));
		}
	}

	/**
	 * Lista utilizadores reprovados, 10 a 10.
	 */
	private static void listaUser10Reprovacao() {
		int numTotal = aUtilizador.verificaListaReprovado(st), numVezes = 0;

		if (numTotal > 10) {
			System.out.println("\nForam encontrados " + numTotal + " resultados.");
			numVezes = (int) Math.ceil(numTotal / 10.0);
			for (int i = 0; i < numVezes; i++) {
				System.out.println("\n" + aUtilizador.listaUtilizadorReprovado(i * 10 + ";", st));
				if (i < (numVezes - 1)) {
					leString("Digite qualquer coisa para ver a próxima página de resultados.");
				}
			}
		} else {
			if (numTotal == 1) {
				System.out.println("\nFoi encontrado 1 resultado.");
			} else {
				System.out.println("\nForam encontrados " + numTotal + " resultados.");
			}
			System.out.println("\n" + aUtilizador.listaUtilizadorReprovado("0;", st));
		}
	}

	/**
	 * Efectua o login à base de dados.
	 * 
	 * @return falso se o login não existir na base de dados, se a conta não se
	 *         encontrar aprovada ou se a password não corresponder à password da
	 *         base de dados.
	 */
	private static boolean logaUser() {
		String login = leString("Introduza o login: ");
		if (aUtilizador.verificaLogin(login, st)) {
			System.out.println("\nEsse login não consta na base de dados!");
			return false;
		}
		String password = leString("Introduza a sua password: ");
		if (aUtilizador.verificaAutenticacao(login, password, st)) {
			if (!aUtilizador.verificaEstadoReprovacao(login, st)) {
				if (aUtilizador.verificaEstado(login, st)) {
					tipoInicio = aUtilizador.verificaTipo(login, st);
					nomeLogin = login;
					nomeUtilizador = aUtilizador.getNomeUtilizador(login, st);
					System.out.println("\nBem-vindo [" + nomeUtilizador + "].");
				} else {
					System.out.println("\nO administrador ainda não activou a sua conta!");
					return false;
				}
			} else {
				System.out.println("\nA sua conta foi rejeitada/desactivada, contacte o administrador!");
				return false;
			}
		} else {
			System.out.println("\nA password que introduziu está incorrecta!");
			return false;
		}
		return true;
	}

	/**
	 * Registar o utilizador no programa.
	 * 
	 * @param aEstado
	 */
	private static void registaUser(boolean aEstado) {
		System.out.println("\nPor favor, introduza os dados pedidos:");
		String nome = leString("Introduza o nome: ");
		String login = verificaLogin();
		String password = leString("Introduza a password: ");
		String email = verificaEmail();
		boolean estado = aEstado;
		char tipo = verificaTipo();

		if (tipo == 'A') {
			Utilizador utilizador = new Utilizador(nome, login, password, email, estado, tipo, false);
			if (aAdministrador.registaAdministrador(utilizador, st)) {
				if (!estado) {
					aNotificacao.registaNotificacaoAdmin(
							"O utilizador com o login: " + login + " criou uma conta do tipo Administrador.", st);
				}
				registaLog("Registou um administrador com login: " + login + ".");
				System.out.println("\nAdministrador registado com sucesso!\n");
			} else {
				System.out.println("\nOcorreu um erro a registar o Administrador.\n");
			}
		} else if (tipo == 'J') {
			Jogador jogador = new Jogador(nome, login, password, estado, email, false, 0, 0, "00:00:00");
			if (aJogador.registaJogador(jogador, st)) {
				if (!estado) {
					aNotificacao.registaNotificacaoAdmin(
							"O utilizador com o login: " + login + " criou uma conta do tipo Jogador.", st);
				}
				registaLog("Registou um jogador com login: " + login + ".");
				System.out.println("\nJogador registado com sucesso!\n");
			} else {
				System.out.println("\nOcorreu um erro a registar o Jogador.\n");
			}
		}
	}

	/**
	 * Verifica o tipo de utilizador.
	 * 
	 * @return 'A' se o tipo for administrador e 'A' se for jogador
	 */
	private static char verificaTipo() {
		boolean tipoCorrecto = false;
		int opccao = 0, contador = 0;
		while (!tipoCorrecto) {
			if (contador > 0) {
				System.out.println("\nIntroduziu dados incorrectos, só pode introduzir 1 ou 2!");
			}
			opccao = leInt("Introduza o tipo de utilizador (1- Administrador, 2- Jogador): ");
			if (opccao == 1 || opccao == 2) {
				tipoCorrecto = true;
			}
			contador++;
		}

		switch (opccao) {

		case 1:
			return 'A';
		case 2:
			return 'J';
		}
		return 'a';
	}

	/**
	 * Verifica se o login já existe.
	 * 
	 * @return login
	 */
	private static String verificaLogin() {
		boolean loginVerificado = false;
		String login = "";
		int contador = 0;
		while (!loginVerificado) {
			if (contador > 0) {
				System.out.println("\nEsse login já existe na base de dados, por favor introduza outro.");
			}
			login = leString("Introduza o login:");
			if (aUtilizador.verificaLogin(login, st)) {
				loginVerificado = true;
			}
			contador++;
		}
		return login;
	}

	/**
	 * Regista o primeiro utilizador do programa
	 * 
	 * @return true se for registado com sucesso ou false se ocorrer um erro.
	 */
	private static boolean registaPrimeiroUser() {
		System.out.println("\nNão foram encontrados utilizadores registados... Por favor introduza os dados pedidos:");
		String nome = leString("Introduza o seu nome: ");
		String login = leString("Introduza o seu login: ");
		String password = leString("Introduza a sua password: ");
		String email = verificaEmail();
		boolean estado = true;
		char tipo = 'A';

		Utilizador utilizador = new Utilizador(nome, login, password, email, estado, tipo, false);

		if (aAdministrador.registaAdministrador(utilizador, st)) {
			registaLog("Registou um administrador com login: " + login + " (primeiro utilizador do programa).");
			System.out.println("\nAdministrador registado com sucesso!\n");
			return true;
		} else {
			System.out.println("\nOcorreu um erro a registar o administrador.");
			System.out.println("\nVolte a introduzir os dados:");
			return false;
		}
	}

	/**
	 * Verifica o email.
	 * 
	 * @return Email.
	 */
	private static String verificaEmail() {
		boolean EmailCorrecto = false, arroba = false, ponto = false;
		int iarroba = 0, contador = 0;
		String email = "";
		while (!EmailCorrecto) {
			if (contador > 0) {
				System.out.println("\nIntroduziu um email com um formato incorrecto!\n");
			}
			email = leString("Introduza o email: ");
			for (int i = 0; i < email.length(); i++) {
				if (arroba) {
					if (email.charAt(i) == '.' && (i - iarroba >= 2) && (email.length() - i >= 2)) {
						ponto = true;
					}
				}
				if (email.charAt(i) == '@' && i >= 1) {
					arroba = true;
					iarroba = i;
				}
			}
			contador++;
			if (arroba && ponto) {
				if (aUtilizador.verificaEmail(email, st)) {
					EmailCorrecto = true;
				} else {
					System.out.println("\nEsse email já existe na base de dados!\n");
					contador = 0;
				}
			}
		}
		return email;
	}

	/**
	 * Insere a mensagem de despedida.
	 */
	private static void msgDespedida() {
		if (nomeUtilizador != null) {
			System.out.println("\nAdeus [" + nomeUtilizador + "]!");
		} else {
			System.out.println("\nAdeus [convidado]!");
		}
	}

	/**
	 * Calcula tempo decorrido entre o início do programa e o fim.
	 * 
	 * @param tempoInicial
	 */
	private static void calculaTempoDecorrido(long tempoInicial) {
		Date dataInicio = new Date(tempoInicial);
		System.out.println("\nInício do processo: " + dataInicio);
		long tempoFinal = (new Date()).getTime();
		Date dataFim = new Date(tempoFinal);
		long tempoTotal = tempoFinal - tempoInicial;
		long tempoTotalSeg = tempoTotal / 1000;
		long tempoTotalMin = tempoTotalSeg / 60;
		long tempoTotalHora = tempoTotalMin / 60;
		System.out.println("Fim do processo: " + dataFim);
		System.out.println("Tempo de execução: " + tempoTotal + " Milissegundos (" + tempoTotalSeg + " Segundo(s); "
				+ tempoTotalMin + " Minuto(s); " + tempoTotalHora + " Hora(s) )");
	}

	/**
	 * Lê a string introduzida.
	 * 
	 * @param aMsg
	 * @return String digitada
	 */
	private static String leString(String aMsg) {
		System.out.println(aMsg);
		return teclado.nextLine();
	}

	/**
	 * Lê o int introduzido.
	 * 
	 * @param aMsg
	 * @return int introduzido.
	 */
	private static int leInt(String aMsg) {

		do {
			String leitura = leString(aMsg);
			try {
				return Integer.parseInt(leitura);
			} catch (NumberFormatException nfe) {
				System.out.println("Tem de digitar um inteiro!");
			}

		} while (true);
	}

}