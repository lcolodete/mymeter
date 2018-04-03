package org.mymeter.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.client.ativacao.AtivacaoMessageBuilderFactory;
import org.mymeter.client.ativacao.AtivacaoMessageType;
import org.mymeter.client.cancelamento.CancelamentoMessageBuilderFactory;
import org.mymeter.client.cancelamento.CancelamentoMessageType;
import org.mymeter.client.cargadados.CargaDadosMessageBuilderFactory;
import org.mymeter.client.cargadados.CargaDadosMessageType;
import org.mymeter.client.consulta.ConsultaMessageBuilderFactory;
import org.mymeter.client.consulta.ConsultaMessageType;
import org.mymeter.client.echotest.EchoTestMessageBuilderFactory;
import org.mymeter.client.echotest.EchoTestMessageType;
import org.mymeter.common.Config;
import org.mymeter.common.ISOLogUtil;
import org.mymeter.common.Log4JUtil;
import org.mymeter.common.MyMeterChannel;
import org.mymeter.common.MyMeterPackager;
import org.mymeter.common.TransactionType;

public class ISOClient {
	
	private static final Logger logger = LogManager.getLogger(ISOClient.class);

	private List<MessageSender> senders = new ArrayList<MessageSender>();
	private List<MessageSender> sendersRunning = new ArrayList<MessageSender>();
	private String serverIp;
	private int serverPort;
	private Integer timeoutMilis;
	private String niiDestino;
	private String niiOrigem;
	private TransactionType transactionType;
	private String transactionMessage;

	public static final int NUM_SENDERS = 1;
	public static final int NUM_MESSAGES_PER_SENDER = 1;

	public static void main(String[] args) throws Exception {

		try {
			
			Config config = Config.getInstance();
			config.loadProperties("/client/isoclient.properties");
			Log4JUtil.startLog4j();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		ISOClient isoClient = new ISOClient();
		isoClient.readConfig();
		isoClient.addSenders();
		isoClient.startSenders();

		System.exit(0);
	}

	private void readConfig() {

		Config config = Config.getInstance();
		
		this.serverIp = config.getServerIp();
		this.serverPort = config.getServerPort();
		// to milliseconds
		this.timeoutMilis = Integer.parseInt(config.getTimeout()) * 1000;
		this.niiDestino = config.getNiiDestino();
		this.niiOrigem = config.getNiiOrigem();
		this.transactionType = TransactionType.valueOf(config.getTransactionType());
		
		//substitutes XXX for the transactionType
		String transactionMessageProperty = Config.TRANSACTION_MESSAGE.replace("XXX", this.transactionType.name());
		this.transactionMessage = config.getProperty(transactionMessageProperty);
		
		System.out.println("=================================");
		System.out.println(this.transactionType+" : "+this.transactionMessage);
		System.out.println("=================================");
	}

	private void addSenders() throws Exception {
		
		IMessageBuilder builder = null;
		
		switch (this.transactionType) {
			case ECHO_TEST:
				EchoTestMessageBuilderFactory echoTestBuilderFactory = new EchoTestMessageBuilderFactory();
				EchoTestMessageType echoTestType = EchoTestMessageType.valueOf(this.transactionMessage);
				builder = echoTestBuilderFactory.createBuilder(echoTestType);
				break;
			case CONSULTA_PRE_ATIVACAO:
				ConsultaMessageBuilderFactory consultaBuilderFactory = new ConsultaMessageBuilderFactory();
				ConsultaMessageType consultaType = ConsultaMessageType.valueOf(this.transactionMessage);
				builder = consultaBuilderFactory.createBuilder(consultaType);
				break;
			case ATIVACAO:
				AtivacaoMessageBuilderFactory ativacaoFactory = new AtivacaoMessageBuilderFactory();
				AtivacaoMessageType ativacaoType = AtivacaoMessageType.valueOf(this.transactionMessage);
				builder = ativacaoFactory.createBuilder(ativacaoType);
				break;
			case CANCELAMENTO:
				CancelamentoMessageBuilderFactory cancelamentoFactory = new CancelamentoMessageBuilderFactory();
				CancelamentoMessageType cancelamentoType = CancelamentoMessageType.valueOf(this.transactionMessage);
				builder = cancelamentoFactory.createBuilder(cancelamentoType);
				break;
			case CARGA_DADOS:
				CargaDadosMessageBuilderFactory cargDadosFactory = new CargaDadosMessageBuilderFactory();
				CargaDadosMessageType cargaDadosType = CargaDadosMessageType.valueOf(this.transactionMessage);
				builder = cargDadosFactory.createBuilder(cargaDadosType);
				break;
		}
		
		
		for (int i = 0; i < ISOClient.NUM_SENDERS; i++) {
			System.out.println("Creating MessageSender " + i);

			MessageSender msgSender = null;
			msgSender = new MessageSender(NUM_MESSAGES_PER_SENDER, "" + i, serverIp, serverPort, timeoutMilis, niiDestino, niiOrigem, builder,
					sendersRunning);

			this.senders.add(msgSender);
		}
	}

	private void startSenders() throws InterruptedException {

		for (MessageSender msgSender : senders) {
			new Thread(msgSender).start();

			synchronized (sendersRunning) {
				sendersRunning.add(msgSender);
			}

			System.out.println("Thread [" + msgSender.getName() + "] started");
		}

		synchronized (sendersRunning) {
			while (!sendersRunning.isEmpty()) {

				System.out.println("ISOClient Thread Main: Senders still executing");
				System.out.println("ISOClient Thread Main: Qty of Senders: " + sendersRunning.size());
				System.out.println("ISOClient Thread Main: Waiting...");

				sendersRunning.wait();
			}

			System.out.println("ISOClient Thread Main: Senders completed.");
			System.out.println("ISOClient Thread Main: Qty of Senders: " + sendersRunning.size());

		}

		System.out.println("End ISOClient");

	}

}

class MessageSender implements Runnable {

	private int numMessages;
	private List<ISOMsg> messageArray;
	private String name;
	private String serverIp;
	private int serverPort;
	private Integer timeout;
	private String niiDestino;
	private String niiOrigem;
	private IMessageBuilder builder;
	private List<MessageSender> sendersRunning;

	public String getName() {
		return name;
	}

	public MessageSender(int pNumMessages, String pName, String strServerIp, int serverPort, Integer timeout, String niiDestino,
			String niiOrigem, IMessageBuilder builder, List<MessageSender> sendersRunning) {

		this.numMessages = pNumMessages;
		this.messageArray = new ArrayList<ISOMsg>();
		this.name = "MessageSender_" + pName;
		this.serverIp = strServerIp;
		this.serverPort = serverPort;
		this.timeout = timeout;
		this.niiDestino = niiDestino;
		this.niiOrigem = niiOrigem;
		this.builder = builder;
		this.sendersRunning = sendersRunning;

		addMessages();
	}

	/**
	 * Creates ISO messages and adds to the message list.  
	 * 
	 */
	private void addMessages() {
		for (int i = 0; i < numMessages; i++) {
			try {
				ISOMsg message = createISOMsg();

				messageArray.add(message);
				System.out.println(name + " -- msg(" + i + ") created");

			} catch (Exception e) {
				System.out.println(name + " -- Error while creating msg(" + i + ")");
				e.printStackTrace();
			}
		}
	}

	protected ISOMsg createISOMsg() throws ISOException {
		ISOMsg message = this.builder.buildMessage();
		return message;
	}

	public void run() {

		System.out.println(name + " -- Sending messages");

		BaseChannel channel = createChannel();

		sendMessages(channel);

		synchronized (sendersRunning) {
			sendersRunning.remove(this);
			sendersRunning.notify();
			System.out.println(name + " -- All messages sent.");
		}

	}

	protected void sendMessages(BaseChannel channel) {
		ISOMsg msgToSend;
		for (int i = 0; i < messageArray.size(); i++) {
			msgToSend = messageArray.get(i);
			transactMessage(channel, msgToSend, i);
		}
	}

	/**
	 * Sends one ISO message through the channel and waits for a response.
	 * 
	 * @param channel
	 * @param msgToSend message to send
	 * @param msgIndex
	 * @return the response message
	 */
	protected ISOMsg transactMessage(BaseChannel channel, ISOMsg msgToSend, Integer msgIndex) {
		ISOMsg response = null;

		try {

			if (!channel.isConnected()) {
				channel.connect();
			}

			// Sets the timeout after the connection is established
			channel.setTimeout(this.timeout);

			msgToSend.dump(System.out, name + " - msg(" + msgIndex + "):");

			System.out.println(name + " -- Sending msg(" + msgIndex + ") ...");
			channel.send(msgToSend);

			response = channel.receive();

		} catch (Exception e) {
			System.out.println(name + " -- Error trying to send msg(" + msgIndex + "): " + e.getMessage());
		} finally {
			try {
				channel.disconnect();
			} catch (IOException e) {
				System.out.println(name + " -- Error disconnecting channel, msg(" + msgIndex + "): " + e.getMessage());
			}

			ISOLogUtil.logBinaryDump(msgToSend, response);
		}

		if (response == null) {
			System.out.println(name + " -- Response msg(" + msgIndex + ") NOT received");
		} else {
			System.out.println(name + " -- Response msg(" + msgIndex + ") received");
			response.dump(System.out, name + " -- Response msg(" + msgIndex + "):");
		}

		return response;
	}

	protected BaseChannel createChannel() {
		BaseChannel channel = new MyMeterChannel(serverIp, serverPort, new MyMeterPackager());

//		Logger logger = new Logger();
//		logger.addListener(new SimpleLogListener(new MyMeterPrintStream(System.out)));
//		logger.setName("ISO_CLIENT");
//		channel.getPackager().setLogger(logger, "Debug");
		return channel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageSender other = (MessageSender) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}