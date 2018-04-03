package org.mymeter.server.transaction.impl;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.common.TransactionType;
import org.mymeter.server.transaction.Transaction;

public class ConsultaPreAtivacao extends Transaction {

	public ConsultaPreAtivacao() {
		super(TransactionType.CONSULTA_PRE_ATIVACAO);
	}

	private final Boolean GENERATE_INVALID_RESPONSE = false;
	
	enum ResponseCodeConsulta {
		APPROVED,
		REQUIRES_CONFIRMATION,
		DENIED
	}

	private final ResponseCodeConsulta responseCodeConsulta = ResponseCodeConsulta.APPROVED;
	
	@Override
	public void processTransaction(ISOMsg msg) throws ISOException {
		
		switch (responseCodeConsulta) {
			case APPROVED:
				super.processApproved(msg);

//				msg.set(61, "2016-08-01 17:31:18,2016-08-01 18:31:18");
				msg.set(61, "r39=00 - Activation code");
				break;
			case REQUIRES_CONFIRMATION:
				super.processApproved(msg, "01");
				
				msg.set(61, "r39=01 - Activation code");
				msg.set(63, "Some parking areas do not operate after 7pm. Proceed anyway?");
				break;
			case DENIED:
				super.processDeclined(msg, "02", "");
				
				msg.set(63, "Activation not allowed");
				break;
		}
		
	}

	@Override
	public void addResponseFields(ISOMsg msg) throws ISOException {
		super.addResponseFields(msg);
		msg.unset(new int[] {42, 47, 57, 58, 59});
		
		if (GENERATE_INVALID_RESPONSE == true) {
			msg.unset(new int[] {3});
		}

	}

}
