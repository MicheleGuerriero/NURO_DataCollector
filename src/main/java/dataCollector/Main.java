package dataCollector;

import config.EnvironmentReader;
import exception.ConfigurationException;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
          NuroApplicationDC dc=new NuroApplicationDC();

    	  try {
    			EnvironmentReader config = EnvironmentReader.getInstance();

			dc.startMonitor(config);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

  
		
    
}
