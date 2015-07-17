package it.seaclouds.NURO_dc;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import dataCollector.MetricManager;
import dataCollector.NuroApplicationDC;
import exception.MetricNotAvailableException;



/**
 * Unit test for simple App.
 */
public class NuroDCTest 
{
	
    @Test
    public void testSignificantInstance() {
        NuroApplicationDC dc = new NuroApplicationDC();
        Assert.assertNotNull(dc);
    }
    
    @Test
    public void testMetricRetrieving() {
        NuroApplicationDC dc = new NuroApplicationDC();
        try {
			MetricManager manager=new MetricManager();
			
			for(String metric: manager.getApplicationMetrics()){
				ObjectMapper mapper = new ObjectMapper();
				JsonNode actualObj = mapper.readTree(InputExample.EXAMPLE_INPUT);
				manager.getMetricValue(metric, actualObj);
			}
		} catch (MetricNotAvailableException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Assert.assertNotNull(dc);
    }
  
    
}
