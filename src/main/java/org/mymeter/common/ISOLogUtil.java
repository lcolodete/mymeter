package org.mymeter.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

public class ISOLogUtil {
	
	private static final Logger logger = LogManager.getLogger(ISOLogUtil.class);

	public static void logBinaryDump(ISOMsg msgOut, ISOMsg msgIn) {
		try {
			logger.debug("<ISO MESSAGE BINARY DUMP>");

			logger.debug("  <OUTGOING MESSAGE>");
			logger.debug("    <HEADER =[" + ISOUtil.hexString(msgOut.getHeader()) + "]>");
			msgOut.setPackager(new MyMeterPackager());
			logger.debug("    <MESSAGE=[" + ISOUtil.hexString(msgOut.pack()) + "]>");
			logger.debug("  </OUTGOING MESSAGE>");

			logger.debug("  <INCOMING MESSAGE>");

			if (msgIn != null) {
				logger.debug("    <HEADER =[" + ISOUtil.hexString(msgIn.getHeader()) + "]>");
				msgIn.setPackager(new MyMeterPackager());
				logger.debug("    <MESSAGE=[" + ISOUtil.hexString(msgIn.pack()) + "]>");
			} else {
				logger.debug("    <CONNECTION TIMED OUT>");
			}
			logger.debug("  </INCOMING MESSAGE>");

			logger.debug("</ISO MESSAGE BINARY DUMP>");

		} catch (Exception e) {
			logger.debug("  <UNABLE TO DUMP MESSAGES/>");
			logger.debug("</ISO MESSAGE BINARY DUMP>");
		}
	}
	
	public static void printMessage(String pDirection, ISOMsg pMessage) throws ISOException {
		
		logger.debug("> START " + pDirection);
		logger.debug("> Header = " + ISOUtil.hexString(pMessage.getHeader()) );
		logger.debug("> MTI = " + pMessage.getMTI());
		for (int i = 2; i <= 64; i++) {
			if (pMessage.hasField(i)) {
				if (pMessage.getValue(i) instanceof byte[]) {
					logger.debug(
							"> Campo "
									+ i
									+ " bytes = "
									+ ISOUtil.hexString((byte[]) pMessage
											.getValue(i)));
				} else
					logger.debug(
							"> Campo " + i + " valor = "
									+ pMessage.getValue(i));
			}
		}
		logger.debug("> END " + pDirection);
	}



}
