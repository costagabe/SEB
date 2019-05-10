/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import beans.Emprestimo;
import beans.Livro;
import java.util.ArrayList;
import java.util.List;
import beans.Reserva;
import daos.exceptions.IllegalOrphanException;
import daos.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ieieo
 */
public class LivroJpaController implements Serializable {

    public LivroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
       }
    public List<Livro> findByPesquisa(String pesquisa){
        Query q = getEntityManager().createQuery("SELECT l FROM Livro l WHERE    l.autor like :pesq or l.titulo like :pesq or l.editora like :pesq ");
        q.setParameter("pesq", "%"+pesquisa+"%");
        
        return q.getResultList();
    }
    public boolean existsBookLikeThat(Livro l){
        Query q = getEntityManager().createQuery("SELECT l FROM Livro l WHERE l.autor like :autor and l.titulo like :titulo and l.editora like :editora ");
        q.setParameter("autor", "%"+l.getAutor()+"%");
        q.setParameter("titulo", "%"+l.getTitulo()+"%");
        q.setParameter("editora", "%"+l.getEditora()+"%");
        return !q.getResultList().isEmpty();
    }
    public void create(Livro livro) {
        if (livro.getEmprestimoList() == null) {
            livro.setEmprestimoList(new ArrayList<>());
        }
        if (livro.getReservaList() == null) {
            livro.setReservaList(new ArrayList<>());
        }
        EntityManager em = null;
        
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Emprestimo> attachedEmprestimoList = new ArrayList<>();
            for (Emprestimo emprestimoListEmprestimoToAttach : livro.getEmprestimoList()) {
                emprestimoListEmprestimoToAttach = em.getReference(emprestimoListEmprestimoToAttach.getClass(), emprestimoListEmprestimoToAttach.getId());
                attachedEmprestimoList.add(emprestimoListEmprestimoToAttach);
            }
            livro.setEmprestimoList(attachedEmprestimoList);
            List<Reserva> attachedReservaList = new ArrayList<>();
            for (Reserva reservaListReservaToAttach : livro.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getId());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            livro.setReservaList(attachedReservaList);
            em.persist(livro);
            for (Emprestimo emprestimoListEmprestimo : livro.getEmprestimoList()) {
                Livro oldLivroOfEmprestimoListEmprestimo = emprestimoListEmprestimo.getLivro();
                emprestimoListEmprestimo.setLivro(livro);
                emprestimoListEmprestimo = em.merge(emprestimoListEmprestimo);
                if (oldLivroOfEmprestimoListEmprestimo != null) {
                    oldLivroOfEmprestimoListEmprestimo.getEmprestimoList().remove(emprestimoListEmprestimo);
                    oldLivroOfEmprestimoListEmprestimo = em.merge(oldLivroOfEmprestimoListEmprestimo);
                }
            }
            for (Reserva reservaListReserva : livro.getReservaList()) {
                Livro oldLivroOfReservaListReserva = reservaListReserva.getLivro();
                reservaListReserva.setLivro(livro);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldLivroOfReservaListReserva != null) {
                    oldLivroOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldLivroOfReservaListReserva = em.merge(oldLivroOfReservaListReserva);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Livro livro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Livro persistentLivro = em.find(Livro.class, livro.getId());
            List<Emprestimo> emprestimoListOld = persistentLivro.getEmprestimoList();
            List<Emprestimo> emprestimoListNew = livro.getEmprestimoList();
            List<Reserva> reservaListOld = persistentLivro.getReservaList();
            List<Reserva> reservaListNew = livro.getReservaList();
            List<String> illegalOrphanMessages = null;
            try{
            for (Emprestimo emprestimoListOldEmprestimo : emprestimoListOld) {
                if (!emprestimoListNew.contains(emprestimoListOldEmprestimo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Emprestimo " + emprestimoListOldEmprestimo + " since its livro field is not nullable.");
                }
            }
            }catch(Exception e){
                
            }
            try{
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its livro field is not nullable.");
                }
            }
            }catch(Exception e){
                
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Emprestimo> attachedEmprestimoListNew = new ArrayList<Emprestimo>();
            for (Emprestimo emprestimoListNewEmprestimoToAttach : emprestimoListNew) {
                emprestimoListNewEmprestimoToAttach = em.getReference(emprestimoListNewEmprestimoToAttach.getClass(), emprestimoListNewEmprestimoToAttach.getId());
                attachedEmprestimoListNew.add(emprestimoListNewEmprestimoToAttach);
            }
            emprestimoListNew = attachedEmprestimoListNew;
            livro.setEmprestimoList(emprestimoListNew);
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getId());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            livro.setReservaList(reservaListNew);
            livro = em.merge(livro);
            for (Emprestimo emprestimoListNewEmprestimo : emprestimoListNew) {
                if (!emprestimoListOld.contains(emprestimoListNewEmprestimo)) {
                    Livro oldLivroOfEmprestimoListNewEmprestimo = emprestimoListNewEmprestimo.getLivro();
                    emprestimoListNewEmprestimo.setLivro(livro);
                    emprestimoListNewEmprestimo = em.merge(emprestimoListNewEmprestimo);
                    if (oldLivroOfEmprestimoListNewEmprestimo != null && !oldLivroOfEmprestimoListNewEmprestimo.equals(livro)) {
                        oldLivroOfEmprestimoListNewEmprestimo.getEmprestimoList().remove(emprestimoListNewEmprestimo);
                        oldLivroOfEmprestimoListNewEmprestimo = em.merge(oldLivroOfEmprestimoListNewEmprestimo);
                    }
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Livro oldLivroOfReservaListNewReserva = reservaListNewReserva.getLivro();
                    reservaListNewReserva.setLivro(livro);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldLivroOfReservaListNewReserva != null && !oldLivroOfReservaListNewReserva.equals(livro)) {
                        oldLivroOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldLivroOfReservaListNewReserva = em.merge(oldLivroOfReservaListNewReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = livro.getId();
                if (findLivro(id) == null) {
                    throw new NonexistentEntityException("The livro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Livro livro;
            try {
                livro = em.getReference(Livro.class, id);
                livro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The livro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Emprestimo> emprestimoListOrphanCheck = livro.getEmprestimoList();
            for (Emprestimo emprestimoListOrphanCheckEmprestimo : emprestimoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Livro (" + livro + ") cannot be destroyed since the Emprestimo " + emprestimoListOrphanCheckEmprestimo + " in its emprestimoList field has a non-nullable livro field.");
            }
            List<Reserva> reservaListOrphanCheck = livro.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Livro (" + livro + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable livro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(livro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Livro> findLivroEntities() {
        return findLivroEntities(true, -1, -1);
    }

    public List<Livro> findLivroEntities(int maxResults, int firstResult) {
        return findLivroEntities(false, maxResults, firstResult);
    }

    private List<Livro> findLivroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Livro.class));
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

    public Livro findLivro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Livro.class, id);
        } finally {
            em.close();
        }
    }

    public int getLivroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Livro> rt = cq.from(Livro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
