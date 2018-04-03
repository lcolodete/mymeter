package org.mymeter.server.transaction;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mymeter.server.transaction.impl.Ativacao;
import org.mymeter.server.transaction.impl.Cancelamento;
import org.mymeter.server.transaction.impl.CargaDados;
import org.mymeter.server.transaction.impl.ConsultaPreAtivacao;
import org.mymeter.server.transaction.impl.EchoTest;


public class TransactionFactory {

	private static List<Class<? extends Transaction>> transactions = new ArrayList<Class<? extends Transaction>>();

	static {
		
		transactions.add(EchoTest.class);
		transactions.add(CargaDados.class);
		transactions.add(ConsultaPreAtivacao.class);
		transactions.add(Ativacao.class);
		transactions.add(Cancelamento.class);
	}
	
	public static List<Transaction> createAll() throws Exception {
		
		List<Transaction> transactionObjects = new ArrayList<Transaction>();
		
		for (Class<? extends Transaction> clazz : transactions) {
			Constructor<? extends Transaction> constructor = clazz.getConstructor(new Class<?>[]{});
			Transaction t = constructor.newInstance(new Object[]{});
			transactionObjects.add(t);
		}
		
		return transactionObjects;
	}
	

	public static void printSupportedTransactions() {
		//Imprime as transações em ordem alfabética
		Set<String> transactionSet = new TreeSet<String>();
		
		System.out.println("Supported transactions:");
		
		for (Class<? extends Transaction> txClass : TransactionFactory.transactions) {
			transactionSet.add(txClass.getSimpleName());
		}
		
		for (String transaction : transactionSet) {
			System.out.println("\t. "+transaction);
		}
		
		System.out.println("");
		
	}
}
