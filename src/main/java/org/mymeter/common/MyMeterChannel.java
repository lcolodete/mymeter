package org.mymeter.common;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;

public class MyMeterChannel extends BaseChannel {

	private static final Logger logger = LogManager.getLogger(MyMeterChannel.class);
	
	private int[] trailer = new int[4];

	{
		trailer[0] = 0xBE;
		trailer[1] = 0xD6;
		trailer[2] = 0x17;
		trailer[3] = 0x07;
	}

	public MyMeterChannel() {
		super();
	}

	public MyMeterChannel(String host, int port, ISOPackager p) {
		super(host, port, p);
	}

	public MyMeterChannel(ISOPackager p) throws IOException {
		super(p);
	}

	public MyMeterChannel(ISOPackager p, ServerSocket serverSocket) throws IOException {
		super(p, serverSocket);
	}

	@Override
	protected void sendMessageLength(int len) throws IOException {
		int newMsgLength = len + trailer.length;

		byte[] messageLen = new byte[2];
		messageLen[0] = (byte) ((newMsgLength & 0xFF00) >> 8);
		messageLen[1] = (byte) (newMsgLength & 0x00FF);

		logger.info("<OUTGOING ISO MESSAGE LENGTH = [" + newMsgLength + "]");
		serverOut.write(messageLen);
	}

	@Override
	protected void sendMessageTrailler(ISOMsg m, int len) throws IOException {
		serverOut.writeByte(trailer[0]);
		serverOut.writeByte(trailer[1]);
		serverOut.writeByte(trailer[2]);
		serverOut.writeByte(trailer[3]);
	}

	@Override
	protected int getMessageLength() throws IOException, ISOException {
		int len = 0;
		byte[] b = new byte[2];

		serverIn.readFully(b, 0, 2);

		len = (b[0] & 0xff) * 256 + (b[1] & 0xff);

		logger.info("<INCOMING ISO MESSAGE LENGTH = [" + len + "]");
		return len;
	}

	@Override
	protected int getHeaderLength() {
		return 5; // TPDU ID ( 1 ) + NII DEST ( 2 ) + NII ORIG ( 2 ) = 5
	}
}
