/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jurbunic.ejb.sb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.jurbunic.rest.klijenti.GMKlijent;
import org.foi.nwtis.jurbunic.rest.klijenti.OWMKlijent;
import org.foi.nwtis.jurbunic.web.podaci.Lokacija;
import org.foi.nwtis.jurbunic.web.podaci.MeteoPrognoza;

/**
 *
 * @author grupa_1
 */
@Stateless
@LocalBean
public class MeteoIoTKlijent {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private String apiKey = "8a137f80e58000b8bd42ee309da78b11";

    public void postaviKorisnickePodatke(String apiKey) {
        this.apiKey = apiKey;
    }

    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent();
        return gmk.getGeoLocation(adresa);
    }

    public MeteoPrognoza[] dajMeteoPrognoze(int id, String adresa) {
        Lokacija l = dajLokaciju(adresa);
        OWMKlijent owm = new OWMKlijent(apiKey);
        return owm.getWeatherForecast(id, l.getLatitude(), l.getLongitude());
    }
    
}
