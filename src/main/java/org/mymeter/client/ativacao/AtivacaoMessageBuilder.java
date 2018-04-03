package org.mymeter.client.ativacao;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.IMessageBuilder;
import org.mymeter.common.ISOFormatUtil;
import org.mymeter.common.TipoVeiculo;
import org.mymeter.common.TransactionType;

public abstract class AtivacaoMessageBuilder implements IMessageBuilder {

	private TransactionType type = TransactionType.ATIVACAO;
	
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
			throw new ISOException("Erro ao criar ISOMsg de Ativacao", e); 
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
		message.set(47, "KRP9506");

		//string de controle recebida na pré-ativação
		message.set(62, "2016-08-01 17:31:18,2016-08-01 18:31:18");
	}
	
	protected void setISOField_CodigoRegra(ISOMsg message, String codigoRegra) throws ISOException {
		message.set(58, codigoRegra);
	}
	
	protected void setISOField_CodigoTarifa(ISOMsg message, String codigoTarifa) throws ISOException {
		message.set(59, codigoTarifa);
	}
	
	protected void setISOField_TipoVeiculo(ISOMsg message, TipoVeiculo tipoVeiculo) throws ISOException {
		message.set(48, tipoVeiculo.getCodigo().toString());
	}
}
