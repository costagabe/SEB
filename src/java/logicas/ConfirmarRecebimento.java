package logicas;

import beans.Emprestimo;
import beans.Livro;
import beans.Multa;
import beans.Usuario;
import com.google.gson.JsonObject;
import daos.EmprestimoJpaController;
import daos.LivroJpaController;
import daos.MultaJpaController;
import daos.UsuarioJpaController;
import java.util.Calendar;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.ParameterGetter;

public class ConfirmarRecebimento implements Logica {

    @Override
    public String executa(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        return "confirmar-recebimento.jsp";
    }

    public String receber(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        JsonObject obj = new JsonObject();
        int idLivro = Integer.parseInt(ParameterGetter.get("idLivro", req));
        String usuario = ParameterGetter.get("usuario", req);

        LivroJpaController livroDao = new LivroJpaController(emf);
        UsuarioJpaController userDao = new UsuarioJpaController(emf);
        EmprestimoJpaController emprestimoDao = new EmprestimoJpaController(emf);

        obj.addProperty("header", "Informação do sistema");
        Livro l = livroDao.findLivro(idLivro);

        if (l != null) {
            if (userDao.exists(usuario)) {
                Usuario u = userDao.findByUsuario(usuario);
                Emprestimo e = emprestimoDao.findByUserAndLivro(u,l);
                if(e != null && e.getDataEntrega() == null){
                    e.setDataEntrega(Calendar.getInstance().getTime());
                    emprestimoDao.edit(e);
                    
                    l.setQtdEstoque(l.getQtdEstoque()+1);
                    livroDao.edit(l);
                    
                    if(e.getDataEntrega().after(e.getDataFim())){
                        int valorMulta = -1* e.daysBetween(e.getDataEntrega(), e.getDataFim()) * 2;
                        Multa m = new Multa();
                        m.setSituacao(0);
                        m.setUsuario(u);
                        m.setValor(valorMulta);
                        new MultaJpaController(emf).create(m);
                        obj.addProperty("text", "Devolução realizada com sucesso, porém foi gerada uma multa para o usuário de R$ "+ valorMulta + ",00.");
                    }else{
                        obj.addProperty("text", "Devolução realizada com sucesso!");
                    }
                }else{
                    obj.addProperty("text", "Empréstimo não localizado no sistema");
                }
            } else {
                obj.addProperty("text", "Usuário não encontrado no sistema");
            }
        } else {
            obj.addProperty("text", "Livro não encontrado no sistema");
        }

        req.setAttribute("json", obj);
        return "Json";
    }

}
