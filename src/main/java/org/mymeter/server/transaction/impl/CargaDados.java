package org.mymeter.server.transaction.impl;

import java.util.Calendar;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.mymeter.common.TransactionType;
import org.mymeter.server.transaction.Transaction;

public class CargaDados extends Transaction {

	public CargaDados() {
		super(TransactionType.CARGA_DADOS);
	}

	private final Boolean GENERATE_INVALID_RESPONSE = false;
	
	private final Boolean APPROVE_TRANSACTION = true;

	@Override
	public void processTransaction(ISOMsg msg) throws ISOException {

		if (APPROVE_TRANSACTION) {
			super.processApproved(msg);
			
//			msg.set(12, "20160801173118");
			msg.set(12, formatField12(4)); //AAAAMMDDHHMMSS
			
			//TODO setar campo 60 com as tabelas
			//msg.set(60, "");
		} else {
			//CARGA DENIED
			
//			String responseCode = "31";
			String responseCode = "32";
			
			super.processDeclined(msg, responseCode, "");
//			msg.set(63, "FAILURE");
//			msg.set(63, "UNAUTHORIZED");
		}

	}

	private String formatField12(int minutesToAdd) throws ISOException {
		Calendar c = Calendar.getInstance();
		
		if (minutesToAdd > 0) {
			c.add(Calendar.MINUTE, minutesToAdd);
		}
		
		int month = c.get(Calendar.MONTH) + 1; //adds 1 because it's zero-based
		
		StringBuilder f12Builder = new StringBuilder();
		f12Builder.append(c.get(Calendar.YEAR))
				  .append(ISOUtil.padleft(month+"", 2, '0'))
				  .append(ISOUtil.padleft(c.get(Calendar.DAY_OF_MONTH)+"", 2, '0'))
				  .append(ISOUtil.padleft(c.get(Calendar.HOUR_OF_DAY)+"", 2, '0'))
				  .append(ISOUtil.padleft(c.get(Calendar.MINUTE)+"", 2, '0'))
				  .append(ISOUtil.padleft(c.get(Calendar.SECOND)+"", 2, '0'));
		return f12Builder.toString();
	}
	
	@Override
	public void addResponseFields(ISOMsg msg) throws ISOException {
		super.addResponseFields(msg);
		msg.unset(new int[] {42});
		
		if (GENERATE_INVALID_RESPONSE == true) {
			msg.unset(new int[] {3});
		}
	}

	public static void main(String[] args) throws ISOException {
		CargaDados carga = new CargaDados();
		System.out.println(carga.formatField12(7));
	}

}
