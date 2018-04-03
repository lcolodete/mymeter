package org.mymeter.client.echotest;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.IMessageBuilder;
import org.mymeter.common.ISOFormatUtil;
import org.mymeter.common.TransactionType;

public abstract class EchoTestMessageBuilder implements IMessageBuilder {

	private TransactionType type = TransactionType.ECHO_TEST;
	
	@Override
	public ISOMsg buildMessage() throws ISOException {
		try {
			ISOMsg message = new ISOMsg();
			setCommonFields(message);
			
			ISOFormatUtil.formatHeader("0001", "0000", message);

			return message;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ISOException("Error creating EchoTest message", e); 
		}
	}
	
	protected void setCommonFields(ISOMsg message) throws ISOException {
		message.setMTI(type.getClientMti());
		message.set(3, type.getProcessingCode());
		message.set(11, "000123");
		
		message.set(41, "NSOMA000");
		
		message.set(42, "000000000000000");
	}

}
