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
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
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
    private List<Dnevnik> prikazDnevnik = new ArrayList<>();
    private String filterKorisnik;
    private String filterIpadresa;
    private String filterTrajanje;
    private String filterStatus;
    private int brojUnosa=0;
    private String dnevnikIp;
    private String dnevnikUrl;
    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();  
        dnevnikIp = httpServletRequest.getRemoteAddr(); 
        dnevnikUrl = httpServletRequest.getRequestURI();
    }

    /**
     * Metoda poziva filtriranje rezultata i zapisuje u listu za prikaz dnevnika, filtrirane rezultate
     */
    public void filtriraj() {
        long pocetak = System.currentTimeMillis();
        prikazDnevnik = dnevnikFacade.filtriranje(filterKorisnik, filterIpadresa, filterTrajanje, filterStatus);
        long ukupno = System.currentTimeMillis()- pocetak;
        zapisiUDnevnik(ukupno);
    }
    
     /**
     * Metoda zapisuje u aktivnosti korisnika u dnevnik. Potrebno je izračunati
     * vrijeme trajanja aktivnosti prije nego se poziva metoda. Korisnička
     * akcija se potom zapisuje u entitet Dnevnik.
     *
     * @param ukupnoTrajanje trajanje korisničke aktivnosti
     */
    public void zapisiUDnevnik(long ukupnoTrajanje) {
        Integer trajanje = (int) ukupnoTrajanje;
        Dnevnik d = new Dnevnik(null, "jurbunic", dnevnikUrl, dnevnikIp, trajanje, 1);
        d.setVrijeme(new Date());
        dnevnikFacade.create(d);
    }
    //----------- Getter & Setter -------------

    public List<Dnevnik> getPrikazDnevnik() {
        brojUnosa = dnevnikFacade.count();
        if(brojUnosa != prikazDnevnik.size()){
            prikazDnevnik = dnevnikFacade.findAll();
        }
        return prikazDnevnik;
    }

    public void setPrikazDnevnik(List<Dnevnik> prikazDnevnik) {     
        this.prikazDnevnik = prikazDnevnik;
    }

    public String getFilterKorisnik() {
        return filterKorisnik;
    }

    public void setFilterKorisnik(String filterKorisnik) {
        this.filterKorisnik = filterKorisnik;
    }

    public String getFilterIpadresa() {
        return filterIpadresa;
    }

    public void setFilterIpadresa(String filterIpadresa) {
        this.filterIpadresa = filterIpadresa;
    }

    public String getFilterTrajanje() {
        return filterTrajanje;
    }

    public void setFilterTrajanje(String filterTrajanje) {
        this.filterTrajanje = filterTrajanje;
    }

    public String getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(String filterStatus) {
        this.filterStatus = filterStatus;
    }

    

}
