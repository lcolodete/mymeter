package org.mymeter.common;

public enum TransactionType {

	ECHO_TEST(MTI.M_0800, MTI.M_0810, "990000"),
	CARGA_DADOS(MTI.M_0800, MTI.M_0810, "930000"),
	CONSULTA_PRE_ATIVACAO(MTI.M_0100, MTI.M_0110, "001000"),
	ATIVACAO(MTI.M_0200, MTI.M_0210, "002000"),
	CANCELAMENTO(MTI.M_0400, MTI.M_0410, "002000");

	/**
	 * MTI enviado pelo cliente (CLIENTE -> SERVIDOR)
	 */
	private String clientMti;
	
	/**
	 * MTI enviado pelo servidor (SERVIDOR -> CLIENTE)
	 */
	private String serverMti;
	
	/**
	 * Codigo de processamento da transacao
	 */
	private String processingCode;

	TransactionType (MTI clientMti, MTI serverMti, String processingCode) {
		this.clientMti = clientMti.toString();
		this.serverMti = serverMti.toString();
		this.processingCode = processingCode;
	}
	
	public String getClientMti() {
		return clientMti;
	}

	public String getServerMti() {
		return serverMti;
	}

	public String getProcessingCode() {
		return processingCode;
	}
	
}

