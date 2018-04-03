package org.mymeter.client.consulta;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.IMessageBuilder;
import org.mymeter.client.TestData;
import org.mymeter.common.ISOFormatUtil;
import org.mymeter.common.TransactionType;

public abstract class ConsultaMessageBuilder implements IMessageBuilder {

	private TransactionType type = TransactionType.CONSULTA_PRE_ATIVACAO;
	
	public abstract void setOtherFields(ISOMsg message) throws ISOException;
	
	@Override
	public ISOMsg buildMessage() throws ISOException {
		try {
			ISOMsg message = new ISOMsg();
			setCommonFields(message);
			setOtherFields(message);
			
			ISOFormatUtil.formatHeader("0001", "0000", message);

			return message;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ISOException("Erro ao criar ISOMsg de Consulta", e); 
		}
	}
	
	protected void setCommonFields(ISOMsg message) throws ISOException {
		message.setMTI(type.getClientMti());
		message.set(3, type.getProcessingCode());
		message.set(11, "002209");
		
		// Numero de serie do POS
		message.set(41, "NSOMA004");
		
		message.set(42, "000000000000000");
		
		//placa
//		message.set(47, "AAA0000");
//		message.set(47, "AAA0001");
		message.set(47, "KRP9560");
		
		//regra
		setISOField_CodigoRegra(message, TestData.COD_REGRA_GERAL);
		
		//tarifa
		setISOField_CodigoTarifa(message, TestData.COD_TARIFA_REGRA_GERAL_1CAD);
	}
	
	protected void setISOField_HorarioAtivacao(ISOMsg message, String horarioAtivacao) throws ISOException {
		message.set(57, horarioAtivacao);
	}

	protected void setISOField_CodigoRegra(ISOMsg message, String codigoRegra) throws ISOException {
		message.set(58, codigoRegra);
	}
	
	protected void setISOField_CodigoTarifa(ISOMsg message, String codigoTarifa) throws ISOException {
		message.set(59, codigoTarifa);
	}
	
}
