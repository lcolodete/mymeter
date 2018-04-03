package org.mymeter.client.ativacao.impl;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.TestData;
import org.mymeter.client.ativacao.AtivacaoMessageBuilder;
import org.mymeter.common.TipoVeiculo;

public class AtivacaoMessageBuilder__BolsaoCaminhao_2CAD_Moto extends AtivacaoMessageBuilder {

	@Override
	public void setOtherFields(ISOMsg message) throws ISOException {
		//Bolsao Caminhao
		setISOField_CodigoRegra(message, TestData.COD_BOLSAO_CAMINHAO);
		//2 CADs
		setISOField_CodigoTarifa(message, TestData.COD_TARIFA_BOLSAO_CAMINHAO_2CAD);
		//Moto
		setISOField_TipoVeiculo(message, TipoVeiculo.MOTO);
	}

}
