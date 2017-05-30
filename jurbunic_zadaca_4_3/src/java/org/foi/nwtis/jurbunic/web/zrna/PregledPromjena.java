/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jurbunic.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.jurbunic.ejb.eb.Dnevnik;
import org.foi.nwtis.jurbunic.ejb.eb.Promjene;
import org.foi.nwtis.jurbunic.ejb.sb.DnevnikFacade;
import org.foi.nwtis.jurbunic.ejb.sb.PromjeneFacade;

/**
 *
 * @author jurbunic
 */
@Named(value = "pregledPromjena")
@SessionScoped
public class PregledPromjena implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;

    @EJB
    private PromjeneFacade promjeneFacade;

    private List<Promjene> listaPromjene;
    private Integer filterId;
    private String filterNaziv;
    private Integer brojZapisa;

    private String dnevnikIp;
    private String dnevnikUrl;

    private long otvoreno = 0;
    private boolean filtrirano = false;

    /**
     * Creates a new instance of PregledPromjena
     */
    public PregledPromjena() {
        long pocetak = System.currentTimeMillis();
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        dnevnikIp = httpServletRequest.getRemoteAddr();
        dnevnikUrl = httpServletRequest.getRequestURI();
        otvoreno = System.currentTimeMillis() - pocetak;
    }

    @PostConstruct
    public void otvorio() {
        zapisiUDnevnik(otvoreno);
    }

    /**
     * Metoda poziva filtriranje rezultata i zapisuje u listu za prikaz
     * promjena, filtrirane rezultate
     */
    public void filtriraj() {
        long pocetak = System.currentTimeMillis();
        listaPromjene = promjeneFacade.filtriranje(filterId, filterNaziv);
        filtrirano = true;
        if (listaPromjene.size() == promjeneFacade.count()) {
            filtrirano = false;
        }
        long ukupno = System.currentTimeMillis() - pocetak;
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

    // -------- Getter & Setter ----------------
    public List<Promjene> getListaPromjene() {
        long pocetak = System.currentTimeMillis();
        if (listaPromjene == null || !filtrirano) {
            listaPromjene = promjeneFacade.findAll();
            brojZapisa = promjeneFacade.count();
        }
        long kraj = System.currentTimeMillis() - pocetak;
        zapisiUDnevnik(kraj);
        return listaPromjene;
    }

    public void setListaPromjene(List<Promjene> listaPromjene) {
        this.listaPromjene = listaPromjene;
    }

    public Integer getFilterId() {
        return filterId;
    }

    public void setFilterId(Integer filterId) {
        this.filterId = filterId;
    }

    public String getFilterNaziv() {
        return filterNaziv;
    }

    public void setFilterNaziv(String filterNaziv) {
        this.filterNaziv = filterNaziv;
    }

}
