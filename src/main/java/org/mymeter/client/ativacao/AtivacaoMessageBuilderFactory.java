package org.mymeter.client.ativacao;

import java.lang.reflect.Constructor;

public class AtivacaoMessageBuilderFactory {

	public AtivacaoMessageBuilder createBuilder(AtivacaoMessageType messageType) throws Exception {
		StringBuilder className = new StringBuilder("org.mymeter.client.ativacao.impl.");
		className.append(AtivacaoMessageBuilder.class.getSimpleName()+"__"+messageType.name());
		
		Class<?> clazz = Class.forName(className.toString());
		Constructor<?> constructor = clazz.getConstructor();
		AtivacaoMessageBuilder builder = (AtivacaoMessageBuilder) constructor.newInstance();

		return builder;
	}

}
