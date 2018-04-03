package org.mymeter.server.transaction.impl;


import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.common.TransactionType;
import org.mymeter.server.transaction.Transaction;

public class EchoTest extends Transaction {

	public EchoTest() {
		super(TransactionType.ECHO_TEST);
	}

	@Override
	public void processTransaction(ISOMsg msg) throws ISOException {
	}

	@Override
	public void addResponseFields(ISOMsg msg) throws ISOException {
		super.addResponseFields(msg);
	}

}
