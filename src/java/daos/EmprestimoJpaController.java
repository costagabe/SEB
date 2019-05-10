/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import beans.Emprestimo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import beans.Livro;
import beans.Usuario;
import daos.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ieieo
 */
public class EmprestimoJpaController implements Serializable {

    public EmprestimoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Emprestimo emprestimo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Livro livro = emprestimo.getLivro();
            if (livro != null) {
                livro = em.getReference(livro.getClass(), livro.getId());
                emprestimo.setLivro(livro);
            }
            Usuario usuario = emprestimo.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                emprestimo.setUsuario(usuario);
            }
            em.persist(emprestimo);
            if (livro != null) {
                livro.getEmprestimoList().add(emprestimo);
                livro = em.merge(livro);
            }
            if (usuario != null) {
                usuario.getEmprestimoList().add(emprestimo);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Emprestimo emprestimo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Emprestimo persistentEmprestimo = em.find(Emprestimo.class, emprestimo.getId());
            Livro livroOld = persistentEmprestimo.getLivro();
            Livro livroNew = emprestimo.getLivro();
            Usuario usuarioOld = persistentEmprestimo.getUsuario();
            Usuario usuarioNew = emprestimo.getUsuario();
            if (livroNew != null) {
                livroNew = em.getReference(livroNew.getClass(), livroNew.getId());
                emprestimo.setLivro(livroNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                emprestimo.setUsuario(usuarioNew);
            }
            emprestimo = em.merge(emprestimo);
            if (livroOld != null && !livroOld.equals(livroNew)) {
                livroOld.getEmprestimoList().remove(emprestimo);
                livroOld = em.merge(livroOld);
            }
            if (livroNew != null && !livroNew.equals(livroOld)) {
                livroNew.getEmprestimoList().add(emprestimo);
                livroNew = em.merge(livroNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getEmprestimoList().remove(emprestimo);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getEmprestimoList().add(emprestimo);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = emprestimo.getId();
                if (findEmprestimo(id) == null) {
                    throw new NonexistentEntityException("The emprestimo with id " + id + " no longer exists.");
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
            Emprestimo emprestimo;
            try {
                emprestimo = em.getReference(Emprestimo.class, id);
                emprestimo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The emprestimo with id " + id + " no longer exists.", enfe);
            }
            Livro livro = emprestimo.getLivro();
            if (livro != null) {
                livro.getEmprestimoList().remove(emprestimo);
                livro = em.merge(livro);
            }
            Usuario usuario = emprestimo.getUsuario();
            if (usuario != null) {
                usuario.getEmprestimoList().remove(emprestimo);
                usuario = em.merge(usuario);
            }
            em.remove(emprestimo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Emprestimo> findEmprestimoEntities() {
        return findEmprestimoEntities(true, -1, -1);
    }

    public List<Emprestimo> findEmprestimoEntities(int maxResults, int firstResult) {
        return findEmprestimoEntities(false, maxResults, firstResult);
    }

    private List<Emprestimo> findEmprestimoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Emprestimo.class));
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

    public Emprestimo findEmprestimo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Emprestimo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmprestimoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Emprestimo> rt = cq.from(Emprestimo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Emprestimo findByUserAndLivro(Usuario u, Livro l) {
        Query q = getEntityManager().createQuery("SELECT e FROM Emprestimo e WHERE e.usuario = :usuario and e.livro = :livro");
        q.setParameter("usuario", u);
        q.setParameter("livro", l);
        return (Emprestimo) q.getSingleResult();
    }
    
}
