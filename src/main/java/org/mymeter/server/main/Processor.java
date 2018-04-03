package org.mymeter.server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ServerChannel;
import org.mymeter.common.Config;
import org.mymeter.common.ISOFormatUtil;
import org.mymeter.common.ISOLogUtil;
import org.mymeter.common.Log4JUtil;
import org.mymeter.common.MyMeterChannel;
import org.mymeter.common.MyMeterPackager;
import org.mymeter.server.EntryPoint;
import org.mymeter.server.exception.InvalidMessageException;
import org.mymeter.server.exception.TransactionNotSupportedException;
import org.mymeter.server.transaction.TransactionFactory;

public class Processor implements Runnable {

	private static final Logger logger = LogManager.getLogger(Processor.class);

	protected class Session implements Runnable {
		private ServerChannel channel;

		protected Session(ServerChannel channel) {
			this.channel = channel;
		}

		private ISOMsg createResponseMsgForException(ISOMsg msgRecv, String reason) throws ISOException {
			ISOMsg messageException = new ISOMsg();
			
			messageException.setHeader(msgRecv.getISOHeader());
			ISOFormatUtil.formatHeaderSwapDirection(messageException);

			messageException.setMTI(msgRecv.getMTI().substring(0, 2)+"10");
			
			// para indicar que nao existem mais mensagens
			messageException.set(3, "990000");
			
			if (msgRecv.getValue(11) != null) {
				messageException.set(11, msgRecv.getValue(11).toString());
			}
			messageException.set(24, msgRecv.getValue(24).toString());
			messageException.set(39, "05");
			messageException.set(41, msgRecv.getValue(41).toString());

			return messageException;
		}
		
		public void run() {
			logger.info("SessionStart");

			try {

				for (;;) {
					boolean errorProcessMsg = false;
					String reasonError = "";
					
					ISOMsg isoMessage = channel.receive();
					
					ISOLogUtil.printMessage("INCOMING", isoMessage);

					EntryPoint entryPoint = EntryPoint.getInstance();
					try {
						entryPoint.processMsg(isoMessage);
					} catch (Exception e) {
						logger.error("Error while processing received message", e);

						errorProcessMsg = true;
						
						if (e instanceof TransactionNotSupportedException) {
							reasonError = "TRANSACTION NOT SUPPORTED";
						} else if (e instanceof InvalidMessageException) {
							reasonError = "INVALID MESSAGE";
						} else {
							reasonError = "SERVER ERROR";
						}
					}

					//Ocorreu alguma excecao ao processar a mensagem ISO recebida
					//	Cria uma nova mensagem para comunicar ao cliente a situacao de erro
					if (errorProcessMsg) {
						ISOMsg messageException = createResponseMsgForException(isoMessage, reasonError);
						isoMessage = messageException;
					}
					
					DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, new Locale("pt","BR"));
					String currentDateTime = df.format(new Date());

					ISOLogUtil.printMessage("OUTGOING", isoMessage);
					
					System.out.println("OUTGOING MSG: ["
										+ currentDateTime
										+ " - bit3:"
										+ getLastPositionBit3(isoMessage.getValue(3).toString())
										+ " - "
										+ ((isoMessage.hasField(39)) ? "r39=(" + isoMessage.getValue(39) + ")" : "Field 39 missing") + "]");

					channel.send(isoMessage);
				}
			} catch (Exception e) {
				logger.error("SessionError", e);
			}
			logger.info("SessionEnd");
		}

		private String getLastPositionBit3(String bit3) {
			return bit3.substring(bit3.length() - 1, bit3.length());
		}

	}

	public void run() {

		try {
			Config serverConfig = Config.getInstance();
			int port = serverConfig.getServerPort();
			ServerSocket serverSocket = new ServerSocket(port);
			logger.info("listening on port " + port);

			for (;;) {

				ServerChannel channel = (ServerChannel) new MyMeterChannel("localhost", port, (ISOPackager) new MyMeterPackager());
				channel.accept(serverSocket);
//				channel.getPackager().setLogger(logger, "Debug");

				Thread t = new Thread(new Session(channel));
				t.setDaemon(true);
				t.start();
			}
		} catch (IOException e) {
			logger.error("I/O error during server execution", e);
		}
	}

	public static void main(String args[]) {

		try {
			Config config = Config.getInstance();
			config.loadProperties("/server/isoserver.properties");
			Log4JUtil.startLog4j();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		new Thread(new Processor()).start();

		System.out.println("================================");
		System.out.println("== mymeter TestServer started ==");
		System.out.println("================================");
		
		TransactionFactory.printSupportedTransactions();
	}

}
