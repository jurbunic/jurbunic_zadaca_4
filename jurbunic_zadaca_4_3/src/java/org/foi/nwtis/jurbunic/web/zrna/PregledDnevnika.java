/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jurbunic.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.foi.nwtis.jurbunic.ejb.eb.Dnevnik;
import org.foi.nwtis.jurbunic.ejb.sb.DnevnikFacade;

/**
 *
 * @author jurbunic
 */
@Named(value = "pregledDnevnika")
@SessionScoped
public class PregledDnevnika implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;
    List<Dnevnik> listaDnevnik;
    
    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
    }
    
    private void dohvatiDnevnik(){
        listaDnevnik = dnevnikFacade.findAll();
        
    }
    
    //----------- Getter & Setter -------------

    public List<Dnevnik> getListaDnevnik() {
        dohvatiDnevnik();
        return listaDnevnik;
    }

    public void setListaDnevnik(List<Dnevnik> listaDnevnik) {
        this.listaDnevnik = listaDnevnik;
    }
    
    
    
}
