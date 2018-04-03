package org.mymeter.server.transaction.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.common.TransactionType;
import org.mymeter.server.transaction.Transaction;

public class Cancelamento extends Transaction {

	public Cancelamento() {
		super(TransactionType.CANCELAMENTO);
	}
	
	private final Boolean GENERATE_INVALID_RESPONSE = false;
	
	private final Boolean APPROVE_TRANSACTION = true;

	
	@Override
	public void processTransaction(ISOMsg msg) throws ISOException {

		if (APPROVE_TRANSACTION) {
			super.processApproved(msg);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			msg.set(12, sdf.format(new Date()));
			msg.set(37, "16162141053333869");
		} else {
			//DENIED
			
			msg.unset(new int[] {37});
			
			String responseCode = "41";
			
			super.processDeclined(msg, responseCode, "");
			msg.set(63, "CANCELLATION FAILED");
		}

	}

	@Override
	public void addResponseFields(ISOMsg msg) throws ISOException {
		super.addResponseFields(msg);
		msg.unset(new int[] {42, 47});
		
		if (GENERATE_INVALID_RESPONSE == true) {
			msg.unset(new int[] {37});
		}
	}

}
