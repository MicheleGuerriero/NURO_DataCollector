package it.seaclouds.NURO_dc;

import org.testng.Assert;
import org.testng.annotations.Test;

import dataCollector.NuroApplicationDC;



/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void testSignificantInstance() {
        NuroApplicationDC dc = new NuroApplicationDC();
        Assert.assertNotNull(dc);
    }
}
