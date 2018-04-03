package org.mymeter.server.transaction;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ResponseUtil {

	private static Map<String, String> responseCodes = new LinkedHashMap<String, String>();
	
	private static Map<String, String> responseErrorCodes = new LinkedHashMap<String, String>();

	static {
		
		//Consulta pré-ativação
		responseErrorCodes.put("02", "ACTIVATION NOT ALLOWED");
		
		//Ativação
		responseErrorCodes.put("11", "FAILURE IN ACTIVATION");
		
		//Consulta e Ativação
		responseCodes.put("00", "APPROVED");
		
		//Consulta
		responseCodes.put("01", "CONFIRM ACTIVATION");
		
		responseCodes.putAll(responseErrorCodes);

	}

	public static Set<String> getErrorCodeSet() {
		return responseErrorCodes.keySet();
	}
	
}
