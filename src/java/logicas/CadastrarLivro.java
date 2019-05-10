package logicas;

import beans.Livro;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import daos.LivroJpaController;
import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.ParameterGetter;

public class CadastrarLivro implements Logica {

    @Override
    public String executa(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {

        return "cadastrar-livro.jsp";
    }

    public String editar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        System.out.println(req.getParameter("dados") + "kkkk");
        Livro l = new Gson().fromJson(req.getParameter("dados"), Livro.class);
        LivroJpaController livroDao = new LivroJpaController(emf);
        Livro old = livroDao.findLivro(l.getId());

        old.setAutor(l.getAutor());
        old.setDescricao(l.getDescricao());
        old.setTitulo(l.getTitulo());
        old.setQtdEstoque(l.getQtdEstoque());
        old.setEditora(l.getEditora());
        if (old.getEmprestimoList() == null) {
            old.setEmprestimoList(new ArrayList<>());
        }
        if (old.getReservaList() == null) {
            old.setReservaList(new ArrayList<>());
        }

        livroDao.edit(old);

        JsonObject obj = new JsonObject();
        obj.addProperty("header", "Edição");
        obj.addProperty("text", "Livro editado com sucesso!");
        req.setAttribute("json", obj);
        return "Json";
    }

    public String editarLivro(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        int id = Integer.parseInt(ParameterGetter.get("id", req));
        Livro l = new LivroJpaController(emf).findLivro(id);
        req.setAttribute("livro", l);
        req.setAttribute("active", "active");
        return "cadastrar-livro.jsp";
    }

    public String cadastrar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        String dados = req.getParameter("dados");
        Livro l = new Gson().fromJson(dados, Livro.class);
        LivroJpaController livroDao = new LivroJpaController(emf);
        JsonObject msg = new JsonObject();

        if (livroDao.existsBookLikeThat(l)) {
            msg.addProperty("header", "Já existe um livro como esse");
            msg.addProperty("text", "Foi encontrado outro livro no sistema que é parecido com esse que deseja cadastrar. Deseja realmente cadastrar esse livro? <a href='#' class='modal-close' onclick=\"( $.ajax({url:'CadastrarLivro',data:{insideSystem:true,dados:`" + dados.replaceAll("\"", "'") + "`,acao:'confirmarCadastro'}}).done(function(data){}))\">SIM</a>");
        } else {
            msg.addProperty("header", "Cadastro Realizado");
            msg.addProperty("text", "Livro adicionado ao estoque!");

            livroDao.create(l);
        }
        req.setAttribute("json", msg.toString());

        return "Json";
    }

    public String confirmarCadastro(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        String dados = req.getParameter("dados");

        Livro l = new Gson().fromJson(dados, Livro.class);
        LivroJpaController livroDao = new LivroJpaController(emf);
        livroDao.create(l);
        return "Json";
    }

}
