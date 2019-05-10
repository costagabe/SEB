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
import java.util.ArrayList;
import java.util.List;
import beans.Multa;
import beans.Reserva;
import beans.Usuario;
import daos.exceptions.IllegalOrphanException;
import daos.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ieieo
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Usuario Login(String usuario, String senha) {
        Query q = getEntityManager().createNamedQuery("Usuario.findByUsuario");
        q.setParameter("usuario", usuario);
        if (!q.getResultList().isEmpty()) {
            Usuario ret = (Usuario) q.getResultList().get(0);
            if (ret.getSenha().equals(senha)) {
                return ret;
            }
        }
        return null;
    }
    public Usuario findByUsuario(String user){
     Query q = getEntityManager().createNamedQuery("Usuario.findByUsuario");
        q.setParameter("usuario", user);
        return (Usuario) q.getSingleResult();
    }
    public boolean exists(String user){
        Query q = getEntityManager().createNamedQuery("Usuario.findByUsuario");
        q.setParameter("usuario", user);
        return !q.getResultList().isEmpty();
    }
    public void create(Usuario usuario) {
        if (usuario.getEmprestimoList() == null) {
            usuario.setEmprestimoList(new ArrayList<Emprestimo>());
        }
        if (usuario.getMultaList() == null) {
            usuario.setMultaList(new ArrayList<Multa>());
        }
        if (usuario.getReservaList() == null) {
            usuario.setReservaList(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Emprestimo> attachedEmprestimoList = new ArrayList<Emprestimo>();
            for (Emprestimo emprestimoListEmprestimoToAttach : usuario.getEmprestimoList()) {
                emprestimoListEmprestimoToAttach = em.getReference(emprestimoListEmprestimoToAttach.getClass(), emprestimoListEmprestimoToAttach.getId());
                attachedEmprestimoList.add(emprestimoListEmprestimoToAttach);
            }
            usuario.setEmprestimoList(attachedEmprestimoList);
            List<Multa> attachedMultaList = new ArrayList<Multa>();
            for (Multa multaListMultaToAttach : usuario.getMultaList()) {
                multaListMultaToAttach = em.getReference(multaListMultaToAttach.getClass(), multaListMultaToAttach.getId());
                attachedMultaList.add(multaListMultaToAttach);
            }
            usuario.setMultaList(attachedMultaList);
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : usuario.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getId());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            usuario.setReservaList(attachedReservaList);
            em.persist(usuario);
            for (Emprestimo emprestimoListEmprestimo : usuario.getEmprestimoList()) {
                Usuario oldUsuarioOfEmprestimoListEmprestimo = emprestimoListEmprestimo.getUsuario();
                emprestimoListEmprestimo.setUsuario(usuario);
                emprestimoListEmprestimo = em.merge(emprestimoListEmprestimo);
                if (oldUsuarioOfEmprestimoListEmprestimo != null) {
                    oldUsuarioOfEmprestimoListEmprestimo.getEmprestimoList().remove(emprestimoListEmprestimo);
                    oldUsuarioOfEmprestimoListEmprestimo = em.merge(oldUsuarioOfEmprestimoListEmprestimo);
                }
            }
            for (Multa multaListMulta : usuario.getMultaList()) {
                Usuario oldUsuarioOfMultaListMulta = multaListMulta.getUsuario();
                multaListMulta.setUsuario(usuario);
                multaListMulta = em.merge(multaListMulta);
                if (oldUsuarioOfMultaListMulta != null) {
                    oldUsuarioOfMultaListMulta.getMultaList().remove(multaListMulta);
                    oldUsuarioOfMultaListMulta = em.merge(oldUsuarioOfMultaListMulta);
                }
            }
            for (Reserva reservaListReserva : usuario.getReservaList()) {
                Usuario oldUsuarioOfReservaListReserva = reservaListReserva.getUsuario();
                reservaListReserva.setUsuario(usuario);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldUsuarioOfReservaListReserva != null) {
                    oldUsuarioOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldUsuarioOfReservaListReserva = em.merge(oldUsuarioOfReservaListReserva);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<Emprestimo> emprestimoListOld = persistentUsuario.getEmprestimoList();
            List<Emprestimo> emprestimoListNew = usuario.getEmprestimoList();
            List<Multa> multaListOld = persistentUsuario.getMultaList();
            List<Multa> multaListNew = usuario.getMultaList();
            List<Reserva> reservaListOld = persistentUsuario.getReservaList();
            List<Reserva> reservaListNew = usuario.getReservaList();
            List<String> illegalOrphanMessages = null;
            for (Emprestimo emprestimoListOldEmprestimo : emprestimoListOld) {
                if (!emprestimoListNew.contains(emprestimoListOldEmprestimo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Emprestimo " + emprestimoListOldEmprestimo + " since its usuario field is not nullable.");
                }
            }
            for (Multa multaListOldMulta : multaListOld) {
                if (!multaListNew.contains(multaListOldMulta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Multa " + multaListOldMulta + " since its usuario field is not nullable.");
                }
            }
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its usuario field is not nullable.");
                }
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
            usuario.setEmprestimoList(emprestimoListNew);
            List<Multa> attachedMultaListNew = new ArrayList<Multa>();
            for (Multa multaListNewMultaToAttach : multaListNew) {
                multaListNewMultaToAttach = em.getReference(multaListNewMultaToAttach.getClass(), multaListNewMultaToAttach.getId());
                attachedMultaListNew.add(multaListNewMultaToAttach);
            }
            multaListNew = attachedMultaListNew;
            usuario.setMultaList(multaListNew);
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getId());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            usuario.setReservaList(reservaListNew);
            usuario = em.merge(usuario);
            for (Emprestimo emprestimoListNewEmprestimo : emprestimoListNew) {
                if (!emprestimoListOld.contains(emprestimoListNewEmprestimo)) {
                    Usuario oldUsuarioOfEmprestimoListNewEmprestimo = emprestimoListNewEmprestimo.getUsuario();
                    emprestimoListNewEmprestimo.setUsuario(usuario);
                    emprestimoListNewEmprestimo = em.merge(emprestimoListNewEmprestimo);
                    if (oldUsuarioOfEmprestimoListNewEmprestimo != null && !oldUsuarioOfEmprestimoListNewEmprestimo.equals(usuario)) {
                        oldUsuarioOfEmprestimoListNewEmprestimo.getEmprestimoList().remove(emprestimoListNewEmprestimo);
                        oldUsuarioOfEmprestimoListNewEmprestimo = em.merge(oldUsuarioOfEmprestimoListNewEmprestimo);
                    }
                }
            }
            for (Multa multaListNewMulta : multaListNew) {
                if (!multaListOld.contains(multaListNewMulta)) {
                    Usuario oldUsuarioOfMultaListNewMulta = multaListNewMulta.getUsuario();
                    multaListNewMulta.setUsuario(usuario);
                    multaListNewMulta = em.merge(multaListNewMulta);
                    if (oldUsuarioOfMultaListNewMulta != null && !oldUsuarioOfMultaListNewMulta.equals(usuario)) {
                        oldUsuarioOfMultaListNewMulta.getMultaList().remove(multaListNewMulta);
                        oldUsuarioOfMultaListNewMulta = em.merge(oldUsuarioOfMultaListNewMulta);
                    }
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Usuario oldUsuarioOfReservaListNewReserva = reservaListNewReserva.getUsuario();
                    reservaListNewReserva.setUsuario(usuario);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldUsuarioOfReservaListNewReserva != null && !oldUsuarioOfReservaListNewReserva.equals(usuario)) {
                        oldUsuarioOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldUsuarioOfReservaListNewReserva = em.merge(oldUsuarioOfReservaListNewReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Emprestimo> emprestimoListOrphanCheck = usuario.getEmprestimoList();
            for (Emprestimo emprestimoListOrphanCheckEmprestimo : emprestimoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Emprestimo " + emprestimoListOrphanCheckEmprestimo + " in its emprestimoList field has a non-nullable usuario field.");
            }
            List<Multa> multaListOrphanCheck = usuario.getMultaList();
            for (Multa multaListOrphanCheckMulta : multaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Multa " + multaListOrphanCheckMulta + " in its multaList field has a non-nullable usuario field.");
            }
            List<Reserva> reservaListOrphanCheck = usuario.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
