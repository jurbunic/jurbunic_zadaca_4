/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jurbunic.ejb.sb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author grupa_1
 */
@Stateless
@LocalBean
public class MeteoIoTKlijent {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private String apiKey = null;

    public void postaviKorisnickePodatke(String apiKey) {
        this.apiKey = apiKey;
    }
    
}
