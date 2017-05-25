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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.jurbunic.ejb.eb.Dnevnik;
import org.foi.nwtis.jurbunic.ejb.eb.Promjene;
import org.foi.nwtis.jurbunic.ejb.eb.Uredaji;
import org.foi.nwtis.jurbunic.ejb.sb.DnevnikFacade;
import org.foi.nwtis.jurbunic.ejb.sb.MeteoIoTKlijent;
import org.foi.nwtis.jurbunic.ejb.sb.PromjeneFacade;
import org.foi.nwtis.jurbunic.ejb.sb.UredajiFacade;
import org.foi.nwtis.jurbunic.rest.klijenti.GMKlijent;
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
    private PromjeneFacade promjeneFacade;

    @EJB
    private DnevnikFacade dnevnikFacade;

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
    private Uredaji uredajZaAzuriranje;
    private List<Izbornik> raspoloziviIoT = new ArrayList<>();
    private List<Izbornik> odabraniIoT = new ArrayList<>();
    private List<String> popisRaspoloziviIoT = new ArrayList<>();
    private List<String> popisOdabraniIoT = new ArrayList<>();
    private boolean azuriranje = false;
    private boolean prognoze = false;
    private String gumbPregledPrognoza = "Pregled prognoza";
    private List<MeteoPrognoza> meteoPronoze = new ArrayList<>();
    private List<Uredaji> raspIoT;
    private List<Uredaji> odabIoT = new ArrayList<>();

    private boolean prikazTablica = false;
    private boolean prikazAzuriraj = false;
    private boolean prikazGumb = false;

    private String dnevnikIp;
    private String dnevnikUrl;

    /**
     * Creates a new instance of OdabirIoTPrognoza
     */
    public OdabirIoTPrognoza() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        dnevnikIp = httpServletRequest.getRemoteAddr();
        dnevnikUrl = httpServletRequest.getRequestURI();
    }

    /**
     * Metoda dodaje novi uređaj u entitet Uredaji. Zapisuje se novi IoT uređaj
     * sa vrijednostima koje je korisnik unio na formi za unos novog uređaja.
     *
     * @return
     */
    public String dodajIoTUredaj() {
        long pocetak = System.currentTimeMillis();
        Lokacija lok = meteoIoTKlijent.dajLokaciju(noviAdresa);
        Uredaji uredaj = new Uredaji(Integer.parseInt(noviId), noviNaziv, Float.parseFloat(lok.getLatitude()), Float.parseFloat(lok.getLongitude()), 0, new Date(), new Date());
        uredajiFacade.create(uredaj);
        preuzmiRaspoloziveIoT();
        long ukupno = System.currentTimeMillis() - pocetak;
        Integer trajanje = (int) ukupno;
        Dnevnik d = new Dnevnik(null, "localhost", dnevnikUrl, dnevnikIp, trajanje, 1);
        d.setVrijeme(new Date());
        dnevnikFacade.create(d);
        return "";
    }

    /**
     * Metoda zapisuje promjenu koja je načinjena nad nekim uređajem. Promjena
     * se zapisuje u entitet Promjene.
     *
     * @param promjenjeniUredaj uređaj nad kojim je promjenjen
     */
    public void zapisiPromjenu(Uredaji promjenjeniUredaj) {
        Promjene p = new Promjene(null, promjenjeniUredaj.getId(),
                promjenjeniUredaj.getNaziv(), promjenjeniUredaj.getLatitude(),
                promjenjeniUredaj.getLongitude(), promjenjeniUredaj.getStatus(),
                promjenjeniUredaj.getVrijemePromjene(), promjenjeniUredaj.getVrijemeKreiranja());
        promjeneFacade.create(p);
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

    /**
     * Metoda ažurira uređaj koji je odabran i prikazan na formi. Metoda radi na
     * način da uređaj koji se ažurira prvo izbriše iz entiteta Uređaji te se
     * ponovno zapiše sa promijenjenim vrijednostima. Promjena se zapisuje i u
     * entitet Promjene i u entitet Dnevnik.
     */
    public void azurirajUredaj() {
        long pocetak = System.currentTimeMillis();
        GMKlijent gmk = new GMKlijent();
        Lokacija l = gmk.getGeoLocation(azurirajAdresa);
        Uredaji azuriraniUredaj = new Uredaji(Integer.valueOf(azurirajId), azurirajNaziv, Float.valueOf(l.getLatitude()), Float.valueOf(l.getLongitude()), 1, new Date(), uredajZaAzuriranje.getVrijemeKreiranja());
        uredajiFacade.remove(uredajZaAzuriranje);
        uredajiFacade.create(azuriraniUredaj);
        preuzmiRaspoloziveIoT();
        long ukupno = System.currentTimeMillis() - pocetak;
        zapisiUDnevnik(ukupno);
        zapisiPromjenu(azuriraniUredaj);
    }

    /**
     * Metoda služi za prikaz forme za ažuriranje. Ako nije odabran nijedan
     * uređaj iz raspoloživih uređaja tada se forma ne pokazuje, inače se
     * zapisuje vrijednosti odabranog uređaja u varijable za inputText na formi.
     */
    public void prikaziFormuZaAzuriranje() {
        if (prikazAzuriraj) {
            prikazAzuriraj = false;
        } else {
            prikazAzuriraj = true;
            uredajZaAzuriranje = null;
            Integer id = Integer.valueOf(popisRaspoloziviIoT.get(0));
            for (int i = 0; i < raspoloziviIoT.size(); i++) {
                if (raspoloziviIoT.get(i).getVrijednost().compareTo(popisRaspoloziviIoT.get(0)) == 0) {
                    uredajZaAzuriranje = raspIoT.get(i);
                    break;
                }
            }
            azurirajId = uredajZaAzuriranje.getId().toString();
            azurirajNaziv = uredajZaAzuriranje.getNaziv();
            azurirajAdresa = reverznoGeokodiranje(String.valueOf(uredajZaAzuriranje.getLatitude()), String.valueOf(uredajZaAzuriranje.getLongitude()));
        }
    }

    /**
     * Metoda preuzima sve raspoložive uređaje iz entiteta Uređaji pa ih
     * zapisuje u listu raspoloživih uređaja. Akcija se zapisuje u dnevnik
     */
    private void preuzmiRaspoloziveIoT() {
        long pocetak = System.currentTimeMillis();
        raspIoT = uredajiFacade.findAll();
        raspoloziviIoT.clear();
        for (Uredaji u : raspIoT) {
            raspoloziviIoT.add(new Izbornik(u.getNaziv(), u.getId().toString()));
        }
        long ukupno = System.currentTimeMillis() - pocetak;
        zapisiUDnevnik(ukupno);
    }

    /**
     * Metoda sluzi za prebacivanje iz uređaja iz odabranih uređaja za praćenje
     * u raspoložive uređaje. Kada je prebačeno onoliko uređaja koliko je
     * odabrano tada se prekida petlja da se smanji broj iteracija petlje
     */
    public void vratiUredaje() {
        long pocetak = System.currentTimeMillis();
        int ukupno = popisOdabraniIoT.size();
        for (int i = 0; i < popisOdabraniIoT.size(); i++) {
            for (int j = 0; j < odabraniIoT.size(); j++) {
                if (odabraniIoT.get(j).getVrijednost().compareTo(popisOdabraniIoT.get(i)) == 0) {
                    int k = 0;
                    raspoloziviIoT.add(odabraniIoT.get(j));
                    raspIoT.add(odabIoT.get(j));
                    odabIoT.remove(j);
                    odabraniIoT.remove(j);
                    k++;
                    if (ukupno == k) {
                        if (odabraniIoT.isEmpty()) {
                            prikazTablica = false;
                            prikazGumb = false;
                        }
                        long kraj = System.currentTimeMillis() - pocetak;
                        zapisiUDnevnik(kraj);
                        return;
                    }
                }
            }
        }
        long kraj = System.currentTimeMillis() - pocetak;
        zapisiUDnevnik(kraj);
    }

    /**
     * Metoda sluzi za prebacivanje uređaja iz raspoloživih uređaja u listu
     * uređaja za praćenje. Kada je prebačeno onoliko uređaja koliko je odabrano
     * tada se prekida petlja da se smanji broj iteracija petlje
     */
    public void odaberiUredajeZaPracenje() {
        long pocetak = System.currentTimeMillis();
        prikazTablica = true;
        prikazGumb = true;
        int ukupno = popisRaspoloziviIoT.size();
        for (int i = 0; i < popisRaspoloziviIoT.size(); i++) {
            for (int j = 0; j < raspoloziviIoT.size(); j++) {
                if (raspoloziviIoT.get(j).getVrijednost().compareTo(popisRaspoloziviIoT.get(i)) == 0) {
                    odabraniIoT.add(raspoloziviIoT.get(j));
                    odabIoT.add(raspIoT.get(j));
                    raspIoT.remove(j);
                    raspoloziviIoT.remove(j);
                    if (ukupno == odabraniIoT.size()) {
                        long kraj = System.currentTimeMillis() - pocetak;
                        zapisiUDnevnik(kraj);
                        return;
                    }
                }
            }
        }
        long kraj = System.currentTimeMillis() - pocetak;
        zapisiUDnevnik(kraj);
    }

    /**
     * Metoda dohvaća prognozu za sve uređaje u listi odabranih uređaja za
     * praćenje. Adresa se dobiva reverznim geokodiranjem. Dobivene prognoze se
     * spremaju u polje meteo prognoza
     */
    public void dohvatiPrognozuZaOdabraneIoT() {
        long pocetak = System.currentTimeMillis();
        if(gumbPregledPrognoza.compareTo("Zatvori prognoze")==0){
            gumbPregledPrognoza = "Pregled prognoza";
            prikazTablica = false;
            return;
        }
        prikazTablica = true;
        meteoPronoze.clear();
        odabIoT.size();
        for (int i = 0; i < odabIoT.size(); i++) {
            String adresa = reverznoGeokodiranje(String.valueOf(odabIoT.get(0).getLatitude()), String.valueOf(odabIoT.get(0).getLongitude()));
            MeteoPrognoza[] mp = meteoIoTKlijent.dajMeteoPrognoze(Integer.valueOf(odabIoT.get(i).getId()), adresa);
            meteoPronoze.addAll(Arrays.asList(mp));
        }
        gumbPregledPrognoza = "Zatvori prognoze";
        long ukupno = System.currentTimeMillis() - pocetak;
        zapisiUDnevnik(ukupno);
    }

    /**
     * Metoda služi za pretvaranje longitude i latitude u adresu.
     *
     * @param lat
     * @param log
     * @return adresa sa zadanim lat i log
     */
    private String reverznoGeokodiranje(String lat, String log) {
        long pocetak = System.currentTimeMillis();
        GMKlijent gmk = new GMKlijent();
        long ukupno = System.currentTimeMillis() - pocetak;
        zapisiUDnevnik(ukupno);
        return gmk.reverseGeocoding(lat, log);
    }

    // ------- Getteri & Setteri -----------
    public boolean isPrikazTablica() {
        return prikazTablica;
    }

    public void setPrikazTablica(boolean prikazTablica) {
        this.prikazTablica = prikazTablica;
    }

    public boolean isPrikazAzuriraj() {
        return prikazAzuriraj;
    }

    public void setPrikazAzuriraj(boolean prikazAzuriraj) {
        this.prikazAzuriraj = prikazAzuriraj;
    }

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

    public boolean isPrikazGumb() {
        return prikazGumb;
    }

    public void setPrikazGumb(boolean prikazGumb) {
        this.prikazGumb = prikazGumb;
    }
    
}
