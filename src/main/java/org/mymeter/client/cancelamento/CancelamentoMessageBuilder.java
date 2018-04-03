package org.mymeter.client.cancelamento;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.IMessageBuilder;
import org.mymeter.common.ISOFormatUtil;
import org.mymeter.common.TransactionType;

public abstract class CancelamentoMessageBuilder implements IMessageBuilder {

	private TransactionType type = TransactionType.CANCELAMENTO;
	
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
			throw new ISOException("Erro ao criar ISOMsg de Cancelamento", e); 
		}
	}
	
	protected void setCommonFields(ISOMsg message) throws ISOException {
		message.setMTI(type.getClientMti());
		message.set(3, type.getProcessingCode());
		message.set(11, "002209");
		
		//Autorizador da Ativação
		message.set(37, "16162141053333869");
		
		// Numero de serie do POS
		message.set(41, "NSOMA004");
		
		message.set(42, "000000000000000");
		
		//placa
//		message.set(47, "AAA0001");
		message.set(47, "AAA0000");
	}
}
