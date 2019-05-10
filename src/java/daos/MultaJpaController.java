/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import beans.Multa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import beans.Usuario;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ieieo
 */
public class MultaJpaController implements Serializable {

    public MultaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Multa multa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = multa.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                multa.setUsuario(usuario);
            }
            em.persist(multa);
            if (usuario != null) {
                usuario.getMultaList().add(multa);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Multa multa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Multa persistentMulta = em.find(Multa.class, multa.getId());
            Usuario usuarioOld = persistentMulta.getUsuario();
            Usuario usuarioNew = multa.getUsuario();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                multa.setUsuario(usuarioNew);
            }
            multa = em.merge(multa);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getMultaList().remove(multa);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getMultaList().add(multa);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = multa.getId();
                if (findMulta(id) == null) {
                    throw new NonexistentEntityException("The multa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Multa multa;
            try {
                multa = em.getReference(Multa.class, id);
                multa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The multa with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario = multa.getUsuario();
            if (usuario != null) {
                usuario.getMultaList().remove(multa);
                usuario = em.merge(usuario);
            }
            em.remove(multa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Multa> findMultaEntities() {
        return findMultaEntities(true, -1, -1);
    }

    public List<Multa> findMultaEntities(int maxResults, int firstResult) {
        return findMultaEntities(false, maxResults, firstResult);
    }

    private List<Multa> findMultaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Multa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Multa findMulta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Multa.class, id);
        } finally {
            em.close();
        }
    }

    public int getMultaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Multa> rt = cq.from(Multa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
