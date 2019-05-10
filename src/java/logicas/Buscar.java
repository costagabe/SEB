package logicas;

import beans.Livro;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import daos.LivroJpaController;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Buscar implements Logica{

    @Override
    public String executa(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        String pesquisa = req.getParameter("pesquisa");
        LivroJpaController livroDao = new LivroJpaController(emf);
        List<Livro> livros = livroDao. findByPesquisa(pesquisa);
        System.out.println(pesquisa);
        req.setAttribute("livros", livros);
        
        if(req.getParameter("autocomplete") != null){
            String livrosJsonObj = generateJson(livros);
            req.setAttribute("json",livrosJsonObj);
            return "Json";
        }
        if(livros.isEmpty()){
            req.setAttribute("vazio", "true");
        }
        return "buscar.jsp";
    }

    private String generateJson(List<Livro> livros) {
        JsonObject obj = new JsonObject();
        livros.forEach((l) -> {
            obj.addProperty(l.getTitulo(), "img/book.png");
        });
       return obj.toString();
    }
    
}
