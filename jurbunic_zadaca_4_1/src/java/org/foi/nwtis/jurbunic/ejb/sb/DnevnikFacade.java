/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jurbunic.ejb.sb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.foi.nwtis.jurbunic.ejb.eb.Dnevnik;

/**
 *
 * @author grupa_1
 */
@Stateless
public class DnevnikFacade extends AbstractFacade<Dnevnik> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DnevnikFacade() {
        super(Dnevnik.class);
    }

    public List<Dnevnik> filtriranje(String korisnik, String adresa, String trajanje, String status, Date odDatuma, Date doDatuma) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Dnevnik> cq = cb.createQuery(Dnevnik.class);
        Root<Dnevnik> dnevnik = cq.from(Dnevnik.class);
        List<Predicate> parametri = new ArrayList<Predicate>();
        if (!korisnik.isEmpty()) {
            parametri.add(cb.equal(dnevnik.get("korisnik"), korisnik));
        }
        if (!adresa.isEmpty()) {
            parametri.add(cb.equal(dnevnik.get("ipadresa"), adresa));
        }
        if (!trajanje.isEmpty()) {
            parametri.add(cb.equal(dnevnik.get("trajanje"), trajanje));
        }
        if (!status.isEmpty()) {
            parametri.add(cb.equal(dnevnik.get("status"), status));
        }
        if (odDatuma != null){
            parametri.add(cb.greaterThanOrEqualTo(dnevnik.<Date>get("vrijeme"), odDatuma));
        }
        if (doDatuma != null){
            parametri.add(cb.lessThanOrEqualTo(dnevnik.<Date>get("vrijeme"), doDatuma));
        }
        cq.where(parametri.toArray(new Predicate[]{}));
        Query q = em.createQuery(cq);
        List<Dnevnik> l = q.getResultList();
        return l;
    }

}
