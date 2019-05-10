package logicas;

import beans.Usuario;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import daos.UsuarioJpaController;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login implements Logica {

    @Override
    public String executa(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        String usuario = req.getParameter("usuario");
        String senha = req.getParameter("senha");

        if (usuario != null) {
            UsuarioJpaController userDao = new UsuarioJpaController(emf);
            Usuario user = userDao.Login(usuario, senha);
            JsonObject obj = new JsonObject();
            if (user != null) {
                req.getSession().setAttribute("logado", user);
                obj.addProperty("codigo", 1);
                obj.addProperty("pagina", "Sistema");
                

            } else {

                obj.addProperty("codigo", 2);
                obj.addProperty("titulo", "Usuário inválido");
                obj.addProperty("texto", "O usuário digitado não está cadastrado no sistema ou a senha está incorreta!");

            }
            req.setAttribute("json", obj);
            return "Json";
        }
        if(req.getSession().getAttribute("logado")!= null){
            return "index.jsp";
        }
        return "login.jsp";
    }

}
