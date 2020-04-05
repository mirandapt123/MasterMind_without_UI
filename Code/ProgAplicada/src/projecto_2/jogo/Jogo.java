package projecto_2.jogo;

import java.util.Vector;

public class Jogo {

	int id;
	int uId;
	String data;
	private Vector <Jogada> listaJogada;
	
	/**
	 * Inicializa o vector de jogadas.
	 */
	public void inicializa() {
		listaJogada = new Vector<Jogada>();
	}
	
	/**
	 * Constructor que recebe todos os dados mandat�rios
	 * @param aId
	 * @param aData
	 */
	public Jogo (int aId, int aUid, String aData) {
		id = aId;
		uId = aUid;
		data = aData;
	}
	
	/**
	 * Constructor que n�o recebe o id
	 * @param aId
	 * @param aData
	 */
	public Jogo (int aUid, String aData) {
		uId = aUid;
		data = aData;
	}
	
	public int getId() {
		return id;
	}

	public int getuId() {
		return uId;
	}

	public String getData() {
		return data;
	}

	/**
	 * Adiciona uma jogada ao vector e devolve um boolean 
	 * a indicar se foi feito com sucesso ou n�o.
	 * @param aJogada
	 * @return boolean
	 */
	public boolean adicionaJogada(Jogada aJogada) {
		if (listaJogada != null && listaJogada.size() >= 0) {
			listaJogada.addElement(aJogada);
			return true;
		}
		return false;
	}

	public String toString() {
		return "Jogo [Id do jogo= " + id + ", Id do utilizador= "+uId+", Data de realiza��o= " + data + "]\n\n";
	}
	
	
	
}
