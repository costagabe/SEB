package logicas;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Logica {

    String executa(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception;
}
