package org.mymeter.server.transaction.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.common.TransactionType;
import org.mymeter.server.transaction.Transaction;

public class Ativacao extends Transaction {

	public Ativacao() {
		super(TransactionType.ATIVACAO);
	}

	private final Boolean GENERATE_INVALID_RESPONSE = false;
	
	private final Boolean APPROVE_TRANSACTION = true;
	
	private final Boolean SET_F63_IF_APPROVED = true;
	
	@Override
	public void processTransaction(ISOMsg msg) throws ISOException {
		
		if (APPROVE_TRANSACTION) {
			super.processApproved(msg);
			
			//seta campos 15, 16, 17 e 37 somente se aprovada
			msg.set(15, "20160801173118");
			msg.set(16, "20160801183118");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			msg.set(17, sdf.format(new Date()));
			
			msg.set(37, "16162141053333869");
			
			if (SET_F63_IF_APPROVED) {
//				msg.set(63, "Continuation of previous credits");
				msg.set(63, "This activation cancelled credits that#were valid");
			}
		} else {
			//TRANSACAO DENIED
			
			String responseCode = "11";
			
			super.processDeclined(msg, responseCode, "");
			msg.set(63, "ACTIVATION FAILURE");
		}
	}

	@Override
	public void addResponseFields(ISOMsg msg) throws ISOException {
		super.addResponseFields(msg);
		msg.unset(new int[] {42, 47, 48, 58, 59, 62});
		
		if (GENERATE_INVALID_RESPONSE == true) {
			msg.unset(new int[] {3});
		}
	}

}
