package org.mymeter.client.consulta;

import java.lang.reflect.Constructor;

public class ConsultaMessageBuilderFactory {

	public ConsultaMessageBuilder createBuilder(ConsultaMessageType messageType) throws Exception {
		StringBuilder className = new StringBuilder("org.mymeter.client.consulta.impl.");
		className.append(ConsultaMessageBuilder.class.getSimpleName()+"__"+messageType.name());
		
		Class<?> clazz = Class.forName(className.toString());
		Constructor<?> constructor = clazz.getConstructor();
		ConsultaMessageBuilder builder = (ConsultaMessageBuilder) constructor.newInstance();

		return builder;
	}
}
