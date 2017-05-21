/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jurbunic.ejb.sb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.foi.nwtis.jurbunic.ejb.eb.Dnevnik;
import org.foi.nwtis.jurbunic.ejb.eb.Promjene;

/**
 *
 * @author grupa_1
 */
@Stateless
public class PromjeneFacade extends AbstractFacade<Promjene> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PromjeneFacade() {
        super(Promjene.class);
    }
    
    public List<Promjene> filtriranje(Integer id, String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Promjene> cq = cb.createQuery(Promjene.class);
        Root<Promjene> promjene = cq.from(Promjene.class);
        List<Predicate> parametri = new ArrayList<>();
        if (id != null) {
            parametri.add(cb.equal(promjene.get("id"), id));
        }
        if (!naziv.isEmpty()) {
            parametri.add(cb.equal(promjene.get("naziv"), naziv));
        }

        cq.where(parametri.toArray(new Predicate[]{}));
        Query q = em.createQuery(cq);
        List<Promjene> l = q.getResultList();
        return l;
    }
    
}
