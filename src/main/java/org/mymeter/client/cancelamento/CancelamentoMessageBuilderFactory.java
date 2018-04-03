package org.mymeter.client.cancelamento;

import java.lang.reflect.Constructor;

public class CancelamentoMessageBuilderFactory {

	public CancelamentoMessageBuilder createBuilder(CancelamentoMessageType messageType) throws Exception {
		StringBuilder className = new StringBuilder("org.mymeter.client.cancelamento.impl.");
		className.append(CancelamentoMessageBuilder.class.getSimpleName()+"__"+messageType.name());
		
		Class<?> clazz = Class.forName(className.toString());
		Constructor<?> constructor = clazz.getConstructor();
		CancelamentoMessageBuilder builder = (CancelamentoMessageBuilder) constructor.newInstance();

		return builder;
	}
}
