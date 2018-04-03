package org.mymeter.server;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public interface IMessageChecker {

	boolean checkIncomingMsg(ISOMsg msg) throws ISOException;
}
