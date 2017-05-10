/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jurbunic.ejb.sb;

import javax.ejb.embeddable.EJBContainer;
import org.foi.nwtis.jurbunic.web.podaci.Lokacija;
import org.foi.nwtis.jurbunic.web.podaci.MeteoPrognoza;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author jurbunic
 */
public class MeteoIoTKlijentTest {
    
    public MeteoIoTKlijentTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of postaviKorisnickePodatke method, of class MeteoIoTKlijent.
     */
    @Ignore
    @Test
    public void testPostaviKorisnickePodatke() throws Exception {
        System.out.println("postaviKorisnickePodatke");
        String apiKey = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        MeteoIoTKlijent instance = (MeteoIoTKlijent)container.getContext().lookup("java:global/classes/MeteoIoTKlijent");
        instance.postaviKorisnickePodatke(apiKey);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dajLokaciju method, of class MeteoIoTKlijent.
     */
    @Ignore
    @Test
    public void testDajLokaciju() throws Exception {
        System.out.println("dajLokaciju");
        String adresa = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        MeteoIoTKlijent instance = (MeteoIoTKlijent)container.getContext().lookup("java:global/classes/MeteoIoTKlijent");
        Lokacija expResult = null;
        Lokacija result = instance.dajLokaciju(adresa);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dajMeteoPrognoze method, of class MeteoIoTKlijent.
     */
    @Test
    public void testDajMeteoPrognoze() throws Exception {
        System.out.println("dajMeteoPrognoze");
        int id = 0;
        String adresa = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        MeteoIoTKlijent instance = (MeteoIoTKlijent)container.getContext().lookup("java:global/classes/MeteoIoTKlijent");
        MeteoPrognoza[] expResult = null;
        MeteoPrognoza[] result = instance.dajMeteoPrognoze(id, adresa);
        assertArrayEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
