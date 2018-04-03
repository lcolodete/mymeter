package org.mymeter.server;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public interface IResponder {

	public void addResponseFields(ISOMsg msg) throws ISOException;
}
