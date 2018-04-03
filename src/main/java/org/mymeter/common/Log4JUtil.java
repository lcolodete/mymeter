package org.mymeter.common;

import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class Log4JUtil {

    public static void startLog4j() {

    	Config config = Config.getInstance();
    	
        String log4JPath = config.getLog4JPropertiesFile();

        URL resource = Log4JUtil.class.getResource(log4JPath);

        if (resource != null) {
            PropertyConfigurator.configure(resource);
        } else {
            System.err.println("Log4j: Cannot find file: " + log4JPath);
            BasicConfigurator.configure();
        }
    }

}
