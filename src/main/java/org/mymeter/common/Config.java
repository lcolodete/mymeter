package org.mymeter.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Singleton that manages the server settings.
 * 
 * @author lcolodete@gmail.com
 *
 */
public class Config {
	
	private static final Logger logger = LogManager.getLogger(Config.class);

	public static final String SERVER_PORT = "server.port";
	
	public static final String LOG4J_PROPERTIES_FILE = "log4j.properties.file";
	
	//Client-only properties
	public static final String SERVER_IP = "server.ip";
	public static final String TIMEOUT = "timeout";
	public static final String NII_DESTINO = "nii.destino";
	public static final String NII_ORIGEM = "nii.origem";
	public static final String TRANSACTION_TYPE = "transaction.type";
	public static final String TRANSACTION_MESSAGE = "transaction.XXX.message";

	
	private static Config config;
	
	private Properties properties;
	
	private Config() {
		this.properties = new Properties();
	}
	
	public static synchronized Config getInstance() {
		if (config == null) {
			config = new Config();
		}
		return config;
	}
	
	public void loadProperties(String propertiesFile) {
		boolean error = false;
		Throwable cause = null;
		
		if (propertiesFile != null && !propertiesFile.isEmpty()) {
			try {
				this.properties.load(Config.getInstance().getClass().getResourceAsStream(propertiesFile));
			} catch (FileNotFoundException e) {
				error = true;
				cause = e;
			} catch (IOException e) {
				error = true;
				cause = e;
			}
			
		} else {
			error = true;
		}
		
		if (error) {
			throw new IllegalStateException("Could not load settings.", cause);
		}
	}
	
    /**
     * Returns the value of a property in the properties file.
     * 
     * @param property property name
     * @return String property value
     */
    public String getProperty(String property) {
        String value = null;
        
        try {
            value = this.properties.getProperty(property);
            if (value == null) {
            	String msg = "property [" + property + "] not found";
             	throw new IllegalArgumentException(msg);
            }
				
        }
        catch (MissingResourceException e) {
            logger.error(e, e);
        }
        
        return value; 
    }
    
	public Integer getServerPort() {
		return Integer.parseInt( this.getProperty(SERVER_PORT) );
	}

	public String getLog4JPropertiesFile() {
		return this.getProperty(LOG4J_PROPERTIES_FILE);
	}
	
	public String getServerIp() {
		return this.getProperty(SERVER_IP);
	}
	
	public String getTimeout() {
		return this.getProperty(TIMEOUT);
	}
	
	public String getNiiDestino() {
		return this.getProperty(NII_DESTINO);
	}
	
	public String getNiiOrigem() {
		return this.getProperty(NII_ORIGEM);
	}
	
	public String getTransactionType() {
		return this.getProperty(TRANSACTION_TYPE);
	}
	
	public String getTransactionMessage() {
		return this.getProperty(TRANSACTION_MESSAGE);
	}
}
