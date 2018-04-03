package org.mymeter.client.ativacao.impl;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.TestData;
import org.mymeter.client.ativacao.AtivacaoMessageBuilder;
import org.mymeter.common.TipoVeiculo;

public class AtivacaoMessageBuilder__AreasEspeciais_1CAD2H_Caminhao extends AtivacaoMessageBuilder {

	@Override
	public void setOtherFields(ISOMsg message) throws ISOException {
		//Areas Especiais
		setISOField_CodigoRegra(message, TestData.COD_AREAS_ESPECIAIS);
		//1 CAD 2H
		setISOField_CodigoTarifa(message, TestData.COD_TARIFA_AREAS_ESPECIAIS_1CAD2H);
		//Caminhao
		setISOField_TipoVeiculo(message, TipoVeiculo.CAMINHAO);
	}

}
