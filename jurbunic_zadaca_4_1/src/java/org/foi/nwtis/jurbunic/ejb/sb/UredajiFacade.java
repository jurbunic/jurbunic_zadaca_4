/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jurbunic.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.jurbunic.ejb.eb.Uredaji;

/**
 *
 * @author grupa_1
 */
@Stateless
public class UredajiFacade extends AbstractFacade<Uredaji> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UredajiFacade() {
        super(Uredaji.class);
    }
    
}
