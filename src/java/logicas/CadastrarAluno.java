package logicas;

import beans.Usuario;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import daos.UsuarioJpaController;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CadastrarAluno implements Logica{

    @Override
    public String executa(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
       return "cadastrar-aluno.jsp";
    }
    
    public String cadastrar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Usuario u = new Gson().fromJson(req.getParameter("dados"), Usuario.class);
        u.setNivel(1);
        UsuarioJpaController userDao = new UsuarioJpaController(emf);
        
        JsonObject obj = new JsonObject();
        
        if(userDao.exists(u.getUsuario())){
            obj.addProperty("header", "<p style='color:red'>Informação do sistema</p>");
            obj.addProperty("text", "Usuário já cadastrado no sistema!");
        }else{
            userDao.create(u);
            obj.addProperty("header", "<p style='color:green'>Informação do sistema!</p>");
            obj.addProperty("text", "Usuário cadastrado com sucesso!");
        }
        
        req.setAttribute("json", obj);
        
       return "Json";
    }    
}
