package org.mymeter.client;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public interface IMessageBuilder {
	public ISOMsg buildMessage() throws ISOException;
}
