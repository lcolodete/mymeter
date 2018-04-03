package org.mymeter.client.cargadados;

import java.lang.reflect.Constructor;

public class CargaDadosMessageBuilderFactory {

	public CargaDadosMessageBuilder createBuilder(CargaDadosMessageType messageType) throws Exception {
		StringBuilder className = new StringBuilder("org.mymeter.client.cargadados.impl.");
		className.append(CargaDadosMessageBuilder.class.getSimpleName()+"__"+messageType.name());
		
		Class<?> clazz = Class.forName(className.toString());
		Constructor<?> constructor = clazz.getConstructor();
		CargaDadosMessageBuilder builder = (CargaDadosMessageBuilder) constructor.newInstance();

		return builder;
	}
}
