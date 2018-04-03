package org.mymeter.client.consulta.impl;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.consulta.ConsultaMessageBuilder;

public class ConsultaMessageBuilder__HorarioAtivacao_Preenchido extends ConsultaMessageBuilder {

	@Override
	public void setOtherFields(ISOMsg message) throws ISOException {
		//Horario inicial de ativação
		setISOField_HorarioAtivacao(message, "1731");
	}

}
