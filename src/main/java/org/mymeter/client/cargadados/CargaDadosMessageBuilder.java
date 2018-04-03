package org.mymeter.client.cargadados;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.IMessageBuilder;
import org.mymeter.common.ISOFormatUtil;
import org.mymeter.common.TransactionType;

public abstract class CargaDadosMessageBuilder implements IMessageBuilder {

	private TransactionType type = TransactionType.CARGA_DADOS;
	
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
			throw new ISOException("Erro ao criar ISOMsg de Carga de Dados", e); 
		}
	}
	
	protected void setCommonFields(ISOMsg message) throws ISOException {
		message.setMTI(type.getClientMti());
		message.set(3, type.getProcessingCode());
		message.set(11, "002209");
		
		// Numero de serie do POS
//		message.set(41, "NSOMA004");
		message.set(41, "NSOMA000");
		
		message.set(42, "000000000000000");
	}
}
