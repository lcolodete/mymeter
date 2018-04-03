package org.mymeter.server.transaction;

import java.util.Calendar;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.mymeter.common.ISOFormatUtil;
import org.mymeter.common.TransactionType;
import org.mymeter.server.IMessageChecker;
import org.mymeter.server.IResponder;
import org.mymeter.server.IValidator;
import org.mymeter.server.exception.InvalidMessageException;

public abstract class Transaction implements IMessageChecker, IValidator, IResponder {

	private String clientMti;
	private String serverMti;
	private String processingCode;
	
	private TransactionType type;
	private String transactioNameSpanish;
	private String transactionNameEnglish;
	
	public Transaction(TransactionType transactionType) {
		this.type = transactionType;
		this.clientMti = type.getClientMti();
		this.serverMti = type.getServerMti();
		this.processingCode = type.getProcessingCode();
	}
	
	public Transaction(TransactionType transactionType, String nameInEnglish, String nameInSpanish) {
		this(transactionType);
		
		this.transactionNameEnglish = nameInEnglish;
		this.transactioNameSpanish = nameInSpanish;
	}

	@Override
	public void validateIncomingMsg(ISOMsg msg) throws InvalidMessageException {
		
		StringBuilder errorMsgs = new StringBuilder();
		boolean hasError = false;
		hasError = validateStandardIncomingFields(msg, errorMsgs);
		
		if (hasError) {
			throw new InvalidMessageException(errorMsgs.toString());
		}
	}

	/**
	 * Valida os campos padrao de entrada: 3, 41
	 * 
	 * @param msg mensagem de entrada que sera validada
	 * @param errorMsgs buffer onde serao adicionadas mensagens de erro
	 * @param hasError 
	 * @return true caso a mensagem nao passe nos testes de validacao
	 */
	private boolean validateStandardIncomingFields(ISOMsg msg, StringBuilder errorMsgs) {
		boolean hasError = false;
		String sep = ", ";
		
		if (!msg.hasField(3)) {
			errorMsgs.append("Field 3 missing");
			hasError = true;
		}
		
		if (!msg.hasField(11)) {
			if (errorMsgs.length() > 0) {
				errorMsgs.append(sep);
			}
			errorMsgs.append("Field 11 missing");
			hasError = true;
		}
		
		if (!msg.hasField(41)) {
			if (errorMsgs.length() > 0) {
				errorMsgs.append(sep);
			}
			errorMsgs.append("Field 41 missing");
			hasError = true;
		}
		
		return hasError;
	}

	@Override
	public boolean checkIncomingMsg(ISOMsg msg) throws ISOException {
		
		boolean isThisTransaction = false;
		
		if (msg.getMTI().equals(clientMti) && msg.getValue(3).toString().substring(0, 5).equals(processingCode.substring(0, 5))) {
			isThisTransaction = true;
		}
			
		return isThisTransaction;
	}

	/**
	 * Adiciona os campos de resposta que sempre estao em 
	 * qualquer resposta de transacao e sao independentes do 
	 * status da transacao (aprovada ou negada).
	 * 
	 * @param msg
	 * @throws ISOException 
	 */
	@Override
	public void addResponseFields(ISOMsg msg) throws ISOException {
		addStandardResponseFields(msg);
	}
	
	private void addStandardResponseFields(ISOMsg msg) throws ISOException {
		ISOFormatUtil.formatHeaderSwapDirection(msg);
		
		msg.setMTI(this.serverMti);
	}

	/**
	 * extrai dados dos campos e executa regra para aprovação ou negação da transação.
	 * 
	 * @param msg
	 * @throws ISOException 
	 */
	public abstract void processTransaction(ISOMsg msg) throws ISOException;

	/**
	 * Processa a transacao como aprovada.
	 * 
	 * @param isoMsg
	 * @throws ISOException
	 */
	protected void processApproved(ISOMsg isoMsg) throws ISOException {
		setISOField_39(isoMsg, "00");
	}
	
	/**
	 * Processa a transacao como aprovada.
	 * 
	 * @throws ISOException
	 */
	protected void processApproved(ISOMsg isoMsg, String responseCode) throws ISOException {
		setISOField_39(isoMsg, responseCode);
	}
	
	/**
	 * Processa a transacao como aprovada. Envia para o POS a mensagem contida no objeto
	 * ResponseMessage passado por parametro.
	 * 
	 * @param isoMsg
	 * @param resp Objeto que contem a mensagem a ser enviada para o POS
	 * @throws ISOException
	 */
	protected void processApproved(ISOMsg isoMsg, ResponseMessage resp) throws ISOException {
		setISOField_39(isoMsg, "00", resp.toString());
	}

	/**
	 * Processa a transacao como Negada. 
	 * Usa um codigo de negacao (responseCode) padrao. 
	 * Envia uma mensagem padrao para o POS.
	 * 
	 * @param isoMsg
	 * @throws ISOException
	 */
	protected void processDeclined(ISOMsg isoMsg) throws ISOException {
		processDeclined(isoMsg, "INVALID TRANSACTION");
	}
	
	/**
	 * Processa a transacao como negada. 
	 * Usa um codigo de negacao (responseCode) padrao.
	 * Envia como mensagem para o POS a String passada por parametro.
	 * 
	 * @param isoMsg
	 * @param reason String contendo a mensagem de transacao negada a ser enviada para o POS
	 * @throws ISOException
	 */
	protected void processDeclined(ISOMsg isoMsg, String reason) throws ISOException {
		processDeclined(isoMsg, "12", reason);
	}
	
	/**
	 * Processa a transacao como negada.
	 * A mensagem de resposta para o POS conterá no campo 39 o 
	 * codigo de resposta informado.
	 *  
	 * @param isoMsg
	 * @param responseCode código de resposta que sera enviado no campo 39
	 * @param reason
	 * @throws ISOException
	 */
	protected void processDeclined(ISOMsg isoMsg, String responseCode, String reason) throws ISOException {
		setISOField_39(isoMsg, responseCode, reason);
	}
	
	/**
	 * Processa a transacao como negada. O Objeto ResponseMessage passado por parametro
	 * contem a mensagem a ser enviada para o POS.
	 * 
	 * @param isoMsg
	 * @param resp Objeto contendo a mensagem a ser enviada para o POS
	 * @throws ISOException
	 */
	protected void processDeclined(ISOMsg isoMsg, ResponseMessage resp) throws ISOException {
		processDeclined(isoMsg, resp.toString());
	}
	
	protected void setResponseCode(ISOMsg isoMsg, String responseCode, ResponseMessage resp) throws ISOException {
		setISOField_39(isoMsg, responseCode, resp.toString());
	}
	
	protected void setISOField_39(ISOMsg isoMsg, String responseCode, String reason) throws ISOException {
		isoMsg.set(39, responseCode);
	}
	
	protected void setISOField_39(ISOMsg isoMsg, String responseCode) throws ISOException {
		isoMsg.set(39, responseCode);
	}
	
	/**
	 * Pega os campos mais importantes da mensagem passada, concatena e 
	 * retorna a String resultante.
	 * 
	 * @return
	 * @throws ISOException 
	 */
	public String getMsgInfo(ISOMsg msg) throws ISOException {
		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append(Transaction.getBasicISOInfo(msg));
		
		String additionalInfo = getAdditionalMessageInfo(msg);
		
		if (!additionalInfo.isEmpty()) {
			strBuilder.append(", additionalInfo=[")
					  .append(additionalInfo + "]");
		}

		return strBuilder.toString();
	}

	public static String getBasicISOInfo(ISOMsg msg) throws ISOException {
		StringBuilder strBuilder = new StringBuilder();

		//" => MTI=["+mti+"], f11=["+f11+"], f41=["+f41+"]"
		strBuilder.append("MTI=[")
				  .append(msg.getMTI() + "]");

		strBuilder.append(", PROCESSING_CODE=[")
		  .append(msg.getValue(3) + "]");

		if (msg.hasField(11)) {
			strBuilder.append(", f11=[")
					  .append(msg.getValue(11) + "]");
		}
				  
		strBuilder.append(", f41=[")
				  .append(msg.getValue(41))
				  .append("]");

		if (msg.hasField(62)) {
			strBuilder.append(", f62=[")
					  .append(msg.getValue(62) + "]");
		}
		
		return strBuilder.toString();
	}
	
	public String getTransactionName() {
		String name = null;
		StringBuilder nameSb = new StringBuilder();
		
		if (this.transactioNameSpanish != null && 
			!"".equals(this.transactioNameSpanish)) {
			nameSb.append(this.transactioNameSpanish);
		}
		
		if(this.transactionNameEnglish != null &&
		   !"".equals(this.transactionNameEnglish)) {
			if (nameSb.length() > 0) {
				nameSb.append("/");
			}
			nameSb.append(this.transactionNameEnglish);
		}
		
		if (nameSb.length() > 0) {
			name = nameSb.toString();
		} else {
			name = type.name();
		}
		
		return name;
	}

	protected void setFields12_13(ISOMsg msg) throws ISOException {
		Calendar c = Calendar.getInstance();
		msg.set(12, ISOFormatUtil.formatField12(c)); //HHMMSS
		msg.set(13, ISOFormatUtil.formatField13(c)); //MMDD
	}
	
	protected String getAdditionalMessageInfo(ISOMsg msg) throws ISOException {
		return "";
	}
	
	public String getClientMti() {
		return clientMti;
	}

	public String getServerMti() {
		return serverMti;
	}

	public String getProcessingCode() {
		return processingCode;
	}


}
