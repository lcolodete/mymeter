package org.mymeter.server.transaction;

public class ResponseMessage {
	
	private static final char CHAR_QUEBRA_LINHA = '#';
	
	private StringBuilder msg = new StringBuilder();

	public ResponseMessage() {
	}
	
	/**
	 * Adiciona uma nova linha a mensagem de resposta.
	 * Toda nova linha se inicia com o caracter '#' (indicador de quebra de linha)
	 * 
	 * @param str conteudo da linha a ser adicionada
	 * @return this for method chaining
	 */
	public ResponseMessage addLine(String str) {
		if (str.isEmpty()) {
			msg.append(CHAR_QUEBRA_LINHA);
		} else {
			if (msg.length() > 0) {
				msg.append(CHAR_QUEBRA_LINHA+str);
			} else {
				msg.append(str);
			}
		}
		return this;
	}
	
	/**
	 * Adiciona uma nova linha em branco à mensagem de resposta.
	 * 
	 * @return this for method chaining
	 */
	public ResponseMessage addLine() {
		msg.append(CHAR_QUEBRA_LINHA);
		return this;
	}
	
	@Override
	public String toString() {
		return msg.toString();
	}
}
