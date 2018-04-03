package org.mymeter.client.echotest;

import java.lang.reflect.Constructor;

public class EchoTestMessageBuilderFactory {

	public EchoTestMessageBuilder createBuilder(EchoTestMessageType messageType) throws Exception {
		StringBuilder className = new StringBuilder("org.mymeter.client.echotest.impl.");
		className.append(EchoTestMessageBuilder.class.getSimpleName()+"__"+messageType.name());
		
		Class<?> clazz = Class.forName(className.toString());
		Constructor<?> constructor = clazz.getConstructor();
		EchoTestMessageBuilder builder = (EchoTestMessageBuilder) constructor.newInstance();

		return builder;
	}

}
