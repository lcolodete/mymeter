package org.mymeter.client.ativacao.impl;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.TestData;
import org.mymeter.client.ativacao.AtivacaoMessageBuilder;
import org.mymeter.common.TipoVeiculo;

public class AtivacaoMessageBuilder__AreasEspeciais_2CAD4H_Moto extends AtivacaoMessageBuilder {

	@Override
	public void setOtherFields(ISOMsg message) throws ISOException {
		//Areas Especiais
		setISOField_CodigoRegra(message, TestData.COD_AREAS_ESPECIAIS);
		//2 CAD 4H
		setISOField_CodigoTarifa(message, TestData.COD_TARIFA_AREAS_ESPECIAIS_2CAD4H);
		//Moto
		setISOField_TipoVeiculo(message, TipoVeiculo.MOTO);
	}

}
