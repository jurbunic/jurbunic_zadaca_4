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
import java.util.stream.Collectors;
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
    private List<Dnevnik> listaDnevnik;
    private List<Dnevnik> prikazDnevnik;
    private String filterKorisnik;
    private String filterIpadresa;
    private String filterTrajanje;
    private String filterStatus;
    private boolean isChanged = true;
    private int brojUnosa=0;

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
    }

    private void dohvatiDnevnik() {
        isChanged = false;
        listaDnevnik = dnevnikFacade.findAll();
        brojUnosa = listaDnevnik.size();
        prikazDnevnik = new ArrayList<>();
        prikazDnevnik.addAll(listaDnevnik);
        

    }

    public void filtriraj() {
        prikazDnevnik.clear();
        prikazDnevnik.addAll(listaDnevnik);
        if (!filterKorisnik.isEmpty() && filterKorisnik != null) {
            prikazDnevnik = prikazDnevnik.stream().filter(p -> p.getKorisnik().compareTo(filterKorisnik) == 0).collect(Collectors.toList());
        }if(!filterIpadresa.isEmpty() && filterIpadresa != null) {
            prikazDnevnik = prikazDnevnik.stream().filter(p -> p.getIpadresa().compareTo(filterIpadresa) == 0).collect(Collectors.toList());
        }if(!filterStatus.isEmpty() && filterStatus != null){
            prikazDnevnik = prikazDnevnik.stream().filter(p -> p.getStatus() == Integer.valueOf(filterStatus)).collect(Collectors.toList());
        }if(!filterTrajanje.isEmpty() && filterTrajanje != null){
            prikazDnevnik = prikazDnevnik.stream().filter(p -> p.getTrajanje() == Integer.valueOf(filterTrajanje)).collect(Collectors.toList());
        }
    }
    //----------- Getter & Setter -------------

    public List<Dnevnik> getPrikazDnevnik() {
        if(brojUnosa != dnevnikFacade.count()){
            isChanged=true;
        }
        if(isChanged){
            dohvatiDnevnik();
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
