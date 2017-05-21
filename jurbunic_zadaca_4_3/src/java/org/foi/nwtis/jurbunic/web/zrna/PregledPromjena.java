/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jurbunic.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.jurbunic.ejb.eb.Promjene;
import org.foi.nwtis.jurbunic.ejb.sb.PromjeneFacade;

/**
 *
 * @author jurbunic
 */
@Named(value = "pregledPromjena")
@SessionScoped
public class PregledPromjena implements Serializable {

    @EJB
    private PromjeneFacade promjeneFacade;

    private List<Promjene> listaPromjene;
    private Integer filterId;
    private String filterNaziv;

    private String dnevnikIp;
    private String dnevnikKorisnik;

    private FacesContext ctx;

    /**
     * Creates a new instance of PregledPromjena
     */
    public PregledPromjena() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        dnevnikIp = httpServletRequest.getRemoteAddr();
    }

    public void filtriraj() {
        listaPromjene = promjeneFacade.filtriranje(filterId, filterNaziv);
    }

    // -------- Getter & Setter ----------------
    public List<Promjene> getListaPromjene() {
        if (listaPromjene == null) {
            listaPromjene = promjeneFacade.findAll();
        }

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
