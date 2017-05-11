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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import org.foi.nwtis.jurbunic.ejb.eb.Uredaji;
import org.foi.nwtis.jurbunic.ejb.sb.MeteoIoTKlijent;
import org.foi.nwtis.jurbunic.ejb.sb.UredajiFacade;
import org.foi.nwtis.jurbunic.web.kontrole.Izbornik;
import org.foi.nwtis.jurbunic.web.podaci.Lokacija;
import org.foi.nwtis.jurbunic.web.podaci.MeteoPrognoza;

/**
 *
 * @author jurbunic
 */
@Named(value = "odabirIoTPrognoza")
@SessionScoped
public class OdabirIoTPrognoza implements Serializable {

    @EJB
    private MeteoIoTKlijent meteoIoTKlijent;

    @EJB
    private UredajiFacade uredajiFacade;

    private String noviId;
    private String noviNaziv;
    private String noviAdresa;
    private String azurirajId;
    private String azurirajNaziv;
    private String azurirajAdresa;
    private List<Izbornik> raspoloziviIoT = new ArrayList<>();
    private List<Izbornik> odabraniIoT = new ArrayList<>();
    private List<String> popisRaspoloziviIoT = new ArrayList<>();
    private List<String> popisOdabraniIoT = new ArrayList<>();
    private boolean azuriranje = false;
    private boolean prognoze = false;
    private String gumbPregledPrognoza = "Pregled prognoza";
    private List<MeteoPrognoza> meteoPronoze = new ArrayList<>();
    private List<Uredaji> raspIoT;

    /**
     * Creates a new instance of OdabirIoTPrognoza
     */
    public OdabirIoTPrognoza() {
    }

    public String dodajIoTUredaj() {
        Lokacija lok = meteoIoTKlijent.dajLokaciju(noviAdresa);
        Uredaji uredaj = new Uredaji(Integer.parseInt(noviId), noviNaziv, Float.parseFloat(lok.getLatitude()), Float.parseFloat(lok.getLongitude()), 0, new Date(), new Date());
        uredajiFacade.create(uredaj);
        preuzmiRaspoloziveIoT();
        return "";
    }

    private void preuzmiRaspoloziveIoT() {
        raspIoT = uredajiFacade.findAll();
        raspoloziviIoT.clear();
        for (Uredaji u : raspIoT) {
            raspoloziviIoT.add(new Izbornik(u.getNaziv(), u.getId().toString()));
        }
    }

    public void odaberiUredajeZaPracenje() {
        int ukupno = popisRaspoloziviIoT.size();
        for (int i = 0; i < popisRaspoloziviIoT.size(); i++) {
            for (int j = 0; j < raspoloziviIoT.size(); j++) {
                if (raspoloziviIoT.get(j).getVrijednost().compareTo(popisRaspoloziviIoT.get(i)) == 0) {
                    odabraniIoT.add(raspoloziviIoT.get(j));
                    raspoloziviIoT.remove(j);
                    if(ukupno == odabraniIoT.size()){
                        return;
                    }
                }
            }
        }
    }
    
    public void dohvatiPrognozuZaOdabraneIoT(){
        MeteoPrognoza[] mp = meteoIoTKlijent.dajMeteoPrognoze(Integer.valueOf(popisOdabraniIoT.get(0)), "Pavlinska 2, Varazdin");
        meteoPronoze = Arrays.asList(mp);
    }

    // ------- Getteri & Setteri -----------
    public String getNoviId() {
        return noviId;
    }

    public void setNoviId(String noviId) {
        this.noviId = noviId;
    }

    public String getNoviNaziv() {
        return noviNaziv;
    }

    public void setNoviNaziv(String noviNaziv) {
        this.noviNaziv = noviNaziv;
    }

    public String getNoviAdresa() {
        return noviAdresa;
    }

    public void setNoviAdresa(String noviAdresa) {
        this.noviAdresa = noviAdresa;
    }

    public String getAzurirajId() {
        return azurirajId;
    }

    public void setAzurirajId(String azurirajId) {
        this.azurirajId = azurirajId;
    }

    public String getAzurirajNaziv() {
        return azurirajNaziv;
    }

    public void setAzurirajNaziv(String azurirajNaziv) {
        this.azurirajNaziv = azurirajNaziv;
    }

    public String getAzurirajAdresa() {
        return azurirajAdresa;
    }

    public void setAzurirajAdresa(String azurirajAdresa) {
        this.azurirajAdresa = azurirajAdresa;
    }

    public List<Izbornik> getRaspoloziviIoT() {
        if (raspoloziviIoT.isEmpty()) {
            preuzmiRaspoloziveIoT();
        }
        return raspoloziviIoT;
    }

    public void setRaspoloziviIoT(List<Izbornik> raspoloziviIoT) {
        this.raspoloziviIoT = raspoloziviIoT;
    }

    public List<Izbornik> getOdabraniIoT() {
        return odabraniIoT;
    }

    public void setOdabraniIoT(List<Izbornik> odabraniIoT) {
        this.odabraniIoT = odabraniIoT;
    }

    public List<String> getPopisRaspoloziviIoT() {
        return popisRaspoloziviIoT;
    }

    public void setPopisRaspoloziviIoT(List<String> popisRaspoloziviIoT) {
        this.popisRaspoloziviIoT = popisRaspoloziviIoT;
    }

    public List<String> getPopisOdabraniIoT() {
        return popisOdabraniIoT;
    }

    public void setPopisOdabraniIoT(List<String> popisOdabraniIoT) {
        this.popisOdabraniIoT = popisOdabraniIoT;
    }

    public boolean isAzuriranje() {
        return azuriranje;
    }

    public void setAzuriranje(boolean azuriranje) {
        this.azuriranje = azuriranje;
    }

    public boolean isPrognoze() {
        return prognoze;
    }

    public void setPrognoze(boolean prognoze) {
        this.prognoze = prognoze;
    }

    public String getGumbPregledPrognoza() {
        return gumbPregledPrognoza;
    }

    public void setGumbPregledPrognoza(String gumbPregledPrognoza) {
        this.gumbPregledPrognoza = gumbPregledPrognoza;
    }

    public List<MeteoPrognoza> getMeteoPronoze() {
        return meteoPronoze;
    }

    public void setMeteoPronoze(List<MeteoPrognoza> meteoPronoze) {
        this.meteoPronoze = meteoPronoze;
    }

}
