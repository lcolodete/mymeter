package org.mymeter.server;

import org.jpos.iso.ISOMsg;
import org.mymeter.server.exception.InvalidMessageException;

public interface IValidator {

	void validateIncomingMsg(ISOMsg msgRecv) throws InvalidMessageException;

}
