package org.mymeter.server;

import java.util.List;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.server.exception.InvalidMessageException;
import org.mymeter.server.exception.TransactionNotSupportedException;
import org.mymeter.server.transaction.Transaction;
import org.mymeter.server.transaction.TransactionFactory;

public class EntryPoint {

	private static EntryPoint instance;
	
	private List<Transaction> transacoes;
	
	private EntryPoint() {
		try {
			transacoes = TransactionFactory.createAll();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static synchronized EntryPoint getInstance() {
		if (instance == null) {
			instance = new EntryPoint();
		}
		return instance;
	}
	
	public void processMsg(ISOMsg msgRecv) throws TransactionNotSupportedException, InvalidMessageException, ISOException {
		boolean transactionRecognized = false;
		Transaction transactionFound = null;
		
		for (Transaction t : transacoes) {
			IMessageChecker checador = (IMessageChecker) t;
			transactionRecognized = checador.checkIncomingMsg(msgRecv);

			if (transactionRecognized) {
				transactionFound = t;
				logTransacaoIdentificada(transactionFound, msgRecv);
				break;
			}

		}
		
		if (!transactionRecognized) {
			logTransacaoNaoIdentificada(msgRecv);
			throw new TransactionNotSupportedException();
		}
		
		IValidator validador = (IValidator) transactionFound;
		validador.validateIncomingMsg(msgRecv);
		
		transactionFound.processTransaction(msgRecv);
		
		IResponder responder = (IResponder) transactionFound;
		responder.addResponseFields(msgRecv);
		
	}

	private void logTransacaoNaoIdentificada(ISOMsg msgRecv) throws ISOException {
		System.out.println("Transaction UNKNOWN: " + Transaction.getBasicISOInfo(msgRecv));
	}
	
	private void logTransacaoIdentificada(Transaction t, ISOMsg msgRecv) throws ISOException {
		System.out.println("Transaction identified: " + t.getTransactionName() + " => " + t.getMsgInfo(msgRecv));
	}
}
