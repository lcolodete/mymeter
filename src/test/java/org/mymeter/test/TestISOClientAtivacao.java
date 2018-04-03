package org.mymeter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.junit.Test;
import org.mymeter.client.TestData;
import org.mymeter.client.ativacao.AtivacaoMessageBuilder;
import org.mymeter.client.ativacao.AtivacaoMessageBuilderFactory;
import org.mymeter.client.ativacao.AtivacaoMessageType;
import org.mymeter.common.TipoVeiculo;
import org.mymeter.common.TransactionType;

public class TestISOClientAtivacao {

	private TransactionType type = TransactionType.ATIVACAO;
	
	private AtivacaoMessageBuilderFactory builderFactory = new AtivacaoMessageBuilderFactory();
	
	/*******************************************************
	 * 
	 * REGRA GERAL
	 * 
	 *******************************************************/

	private void assertCamposObrigatorios(ISOMsg message) throws ISOException {
		
		//MTI
//		  <field id="0" value="0200"/>
		assertEquals(type.getClientMti(), message.getMTI());
		
		//Campo 3
//		  <field id="3" value="002000"/>
		assertEquals(type.getProcessingCode(), message.getValue(3));
		
		//Campo 11
//		  <field id="11" value="002209"/>
		assertNotNull(message.getValue(11));
		
		//Campo 41
//		  <field id="41" value="NSOMA004"/>
		assertNotNull(message.getValue(41));
		
		//Campo 42
//		  <field id="42" value="000000000000000"/>
		String codEstabelecimento = (String) message.getValue(42);
		assertTrue("codigo do estabelecimento deve ter tamanho 15", codEstabelecimento.length()==15);

		//Placa - 47
//		  <field id="47" value="KRP9506"/>
		String placa = (String)message.getValue(47);
		assertTrue("tamanho da placa deve ser 7", placa.length()==7);

		//62 - string recebida na chamada de pré-ativação
//		  <field id="62" value="2016-08-01 17:31:18,2016-08-01 18:31:18"/>
		assertNotNull(message.getValue(62));
	}
	
	//Assert Regra
	
	private void assert_Regra_RegraGeral(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_REGRA_GERAL, message.getValue(58));
	}
	
	private void assert_Regra_BolsaoCaminhao(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_BOLSAO_CAMINHAO, message.getValue(58));
	}
	
	private void assert_Regra_AreasEspeciais(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_AREAS_ESPECIAIS, message.getValue(58));
	}
	
	//Assert Tarifa
	
	private void assert_Tarifa_RegraGeral_1CAD(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_TARIFA_REGRA_GERAL_1CAD, message.getValue(59));
	}

	private void assert_Tarifa_RegraGeral_2CAD(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_TARIFA_REGRA_GERAL_2CAD, message.getValue(59));
	}
	
	private void assert_Tarifa_BolsaoCaminhao_1CAD(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_TARIFA_BOLSAO_CAMINHAO_1CAD, message.getValue(59));
	}
	
	private void assert_Tarifa_BolsaoCaminhao_2CAD(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_TARIFA_BOLSAO_CAMINHAO_2CAD, message.getValue(59));
	}
	
	private void assert_Tarifa_AreasEspeciais_1CAD2H(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_TARIFA_AREAS_ESPECIAIS_1CAD2H, message.getValue(59));
	}
	
	private void assert_Tarifa_AreasEspeciais_1CAD3H(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_TARIFA_AREAS_ESPECIAIS_1CAD3H, message.getValue(59));
	}
	
	private void assert_Tarifa_AreasEspeciais_2CAD4H(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_TARIFA_AREAS_ESPECIAIS_2CAD4H, message.getValue(59));
	}
	
	private void assert_Tarifa_AreasEspeciais_2CAD6H(ISOMsg message) throws ISOException {
		assertEquals(TestData.COD_TARIFA_AREAS_ESPECIAIS_2CAD6H, message.getValue(59));
	}
	
	//Assert Tipo de Veículo
	
	private void assert_TipoVeiculo_Carro(ISOMsg message) throws ISOException {
		assertEquals(TipoVeiculo.CARRO.getCodigo(), Integer.valueOf((String)message.getValue(48)));
	}
	
	private void assert_TipoVeiculo_Moto(ISOMsg message) throws ISOException {
		assertEquals(TipoVeiculo.MOTO.getCodigo(), Integer.valueOf((String)message.getValue(48)));
	}
	
	private void assert_TipoVeiculo_Caminhao(ISOMsg message) throws ISOException {
		assertEquals(TipoVeiculo.CAMINHAO.getCodigo(), Integer.valueOf((String)message.getValue(48)));
	}
	
	private void logMessage(ISOMsg message, AtivacaoMessageType messageType) {
		System.out.println();
		System.out.println(messageType.name());
		message.dump(System.out, "");
	}
	
	@Test
	public void buildMessage__RegraGeral_1CAD_Carro__MessageBuiltAccordingly() throws Exception {

		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.RegraGeral_1CAD_Carro;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);
		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);

		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		
		//Campos obrigatórios em qualquer mensagem de Ativacao
		assertCamposObrigatorios(message);
		
		//Assert - Campos relativos a este teste
		//regra geral - 58
//		  <field id="58" value="67"/>
		assert_Regra_RegraGeral(message);
		
		//1 CAD - 59
//		  <field id="59" value="273"/>
		assert_Tarifa_RegraGeral_1CAD(message);
		
		//Carro - 48
//		  <field id="48" value="0"/>
		assert_TipoVeiculo_Carro(message);
	}
	
	@Test
	public void buildMessage__RegraGeral_2CAD_Carro__MessageBuiltAccordingly() throws Exception {
		
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.RegraGeral_2CAD_Carro;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);
		
		//Regra geral
		assert_Regra_RegraGeral(message);
		
		//2CAD
		assert_Tarifa_RegraGeral_2CAD(message);
		
		//Carro
		assert_TipoVeiculo_Carro(message);
	}
	
	@Test
	public void buildMessage__RegraGeral_1CAD_Moto__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.RegraGeral_1CAD_Moto;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);
		
		//Regra geral
		assert_Regra_RegraGeral(message);
		
		//1CAD
		assert_Tarifa_RegraGeral_1CAD(message);
		
		//Moto
		assert_TipoVeiculo_Moto(message);
	}
	
	@Test
	public void buildMessage__RegraGeral_2CAD_Moto__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.RegraGeral_2CAD_Moto;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		
		assertCamposObrigatorios(message);
		
		//Regra geral
		assert_Regra_RegraGeral(message);
		
		//2CAD
		assert_Tarifa_RegraGeral_2CAD(message);
		
		//Moto
		assert_TipoVeiculo_Moto(message);
	}
	
	@Test
	public void buildMessage__RegraGeral_1CAD_Caminhao__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.RegraGeral_1CAD_Caminhao;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);
		
		//Regra geral
		assert_Regra_RegraGeral(message);
		
		//1CAD
		assert_Tarifa_RegraGeral_1CAD(message);
		
		//Caminhao
		assert_TipoVeiculo_Caminhao(message);
	}
	
	@Test
	public void buildMessage__RegraGeral_2CAD_Caminhao__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.RegraGeral_2CAD_Caminhao;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);
		
		//Regra geral
		assert_Regra_RegraGeral(message);
		
		//2 CAD
		assert_Tarifa_RegraGeral_2CAD(message);
		
		//Caminhao
		assert_TipoVeiculo_Caminhao(message);
	}

	/*******************************************************
	 * 
	 * BOLSAO CAMINHAO
	 * 
	 *******************************************************/

	@Test
	public void buildMessage__BolsaoCaminhao_1CAD_Carro__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.BolsaoCaminhao_1CAD_Carro;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_BolsaoCaminhao(message);
		assert_Tarifa_BolsaoCaminhao_1CAD(message);
		assert_TipoVeiculo_Carro(message);
	}
	
	@Test
	public void buildMessage__BolsaoCaminhao_2CAD_Carro__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.BolsaoCaminhao_2CAD_Carro;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);
		
		assert_Regra_BolsaoCaminhao(message);
		assert_Tarifa_BolsaoCaminhao_2CAD(message);
		assert_TipoVeiculo_Carro(message);
	}
	
	@Test
	public void buildMessage__BolsaoCaminhao_1CAD_Moto__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.BolsaoCaminhao_1CAD_Moto;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_BolsaoCaminhao(message);
		assert_Tarifa_BolsaoCaminhao_1CAD(message);
		assert_TipoVeiculo_Moto(message);
	}
	
	@Test
	public void buildMessage__BolsaoCaminhao_2CAD_Moto__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.BolsaoCaminhao_2CAD_Moto;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_BolsaoCaminhao(message);
		assert_Tarifa_BolsaoCaminhao_2CAD(message);
		assert_TipoVeiculo_Moto(message);
	}
	
	@Test
	public void buildMessage__BolsaoCaminhao_1CAD_Caminhao__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.BolsaoCaminhao_1CAD_Caminhao;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_BolsaoCaminhao(message);
		assert_Tarifa_BolsaoCaminhao_1CAD(message);
		assert_TipoVeiculo_Caminhao(message);
	}
	
	@Test
	public void buildMessage__BolsaoCaminhao_2CAD_Caminhao__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.BolsaoCaminhao_2CAD_Caminhao;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_BolsaoCaminhao(message);
		assert_Tarifa_BolsaoCaminhao_2CAD(message);
		assert_TipoVeiculo_Caminhao(message);
	}
	
	/*******************************************************
	 * 
	 * AREAS ESPECIAIS
	 * 
	 *******************************************************/
	
	@Test
	public void buildMessage__AreasEspeciais_1CAD2H_Carro__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_1CAD2H_Carro;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_1CAD2H(message);
		assert_TipoVeiculo_Carro(message);
	}
	@Test
	public void buildMessage__AreasEspeciais_1CAD3H_Carro__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_1CAD3H_Carro;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_1CAD3H(message);
		assert_TipoVeiculo_Carro(message);
	}
	
	@Test
	public void buildMessage__AreasEspeciais_2CAD4H_Carro__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_2CAD4H_Carro;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_2CAD4H(message);
		assert_TipoVeiculo_Carro(message);
	}
	
	@Test
	public void buildMessage__AreasEspeciais_2CAD6H_Carro__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_2CAD6H_Carro;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_2CAD6H(message);
		assert_TipoVeiculo_Carro(message);
	}
	
	@Test
	public void buildMessage__AreasEspeciais_1CAD2H_Moto__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_1CAD2H_Moto;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_1CAD2H(message);
		assert_TipoVeiculo_Moto(message);
	}
	@Test
	public void buildMessage__AreasEspeciais_1CAD3H_Moto__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_1CAD3H_Moto;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_1CAD3H(message);
		assert_TipoVeiculo_Moto(message);
	}
	
	@Test
	public void buildMessage__AreasEspeciais_2CAD4H_Moto__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_2CAD4H_Moto;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_2CAD4H(message);
		assert_TipoVeiculo_Moto(message);
	}
	@Test
	public void buildMessage__AreasEspeciais_2CAD6H_Moto__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_2CAD6H_Moto;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_2CAD6H(message);
		assert_TipoVeiculo_Moto(message);
	}
	
	@Test
	public void buildMessage__AreasEspeciais_1CAD2H_Caminhao__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_1CAD2H_Caminhao;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_1CAD2H(message);
		assert_TipoVeiculo_Caminhao(message);
	}
	@Test
	public void buildMessage__AreasEspeciais_1CAD3H_Caminhao__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_1CAD3H_Caminhao;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_1CAD3H(message);
		assert_TipoVeiculo_Caminhao(message);
	}
	
	@Test
	public void buildMessage__AreasEspeciais_2CAD4H_Caminhao__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_2CAD4H_Caminhao;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_2CAD4H(message);
		assert_TipoVeiculo_Caminhao(message);
	}
	@Test
	public void buildMessage__AreasEspeciais_2CAD6H_Caminhao__MessageBuiltAccordingly() throws Exception {
		/////////////////////////////////////////
		//
		//Arrange
		//
		/////////////////////////////////////////
		AtivacaoMessageType messageType = AtivacaoMessageType.AreasEspeciais_2CAD6H_Caminhao;
		AtivacaoMessageBuilder builder = this.builderFactory.createBuilder(messageType);

		
		/////////////////////////////////////////
		//
		//Act
		//
		/////////////////////////////////////////
		
		ISOMsg message = builder.buildMessage();
		logMessage(message, messageType);
		
		
		/////////////////////////////////////////
		//
		//Assert
		//
		/////////////////////////////////////////
		assertCamposObrigatorios(message);

		assert_Regra_AreasEspeciais(message);
		assert_Tarifa_AreasEspeciais_2CAD6H(message);
		assert_TipoVeiculo_Caminhao(message);
	}

}
