package org.mymeter.client.ativacao.impl;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.TestData;
import org.mymeter.client.ativacao.AtivacaoMessageBuilder;
import org.mymeter.common.TipoVeiculo;

public class AtivacaoMessageBuilder__RegraGeral_2CAD_Carro extends AtivacaoMessageBuilder {

	@Override
	public void setOtherFields(ISOMsg message) throws ISOException {
		//Regra geral
		setISOField_CodigoRegra(message, TestData.COD_REGRA_GERAL);
		
		//2CAD
		setISOField_CodigoTarifa(message, TestData.COD_TARIFA_REGRA_GERAL_2CAD);
		
		//Carro
		setISOField_TipoVeiculo(message, TipoVeiculo.CARRO);
	}

}
