package logicas;

import beans.Emprestimo;
import beans.Usuario;
import com.google.gson.JsonObject;
import daos.EmprestimoJpaController;
import java.util.Calendar;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.ParameterGetter;

public class Emprestimos implements Logica {

    @Override
    public String executa(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {

        return "emprestimos.jsp";
    }

    public String renovar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        int idEmprestimo = Integer.parseInt(ParameterGetter.get("id", req));
        EmprestimoJpaController emprestimoDao = new EmprestimoJpaController(emf);
        JsonObject obj = new JsonObject();
        Emprestimo e = emprestimoDao.findEmprestimo(idEmprestimo);

        obj.addProperty("header", "Informação do sistema");

        if (e.getRenovacoes() < 3) {
            Calendar dataFim = Calendar.getInstance();
            dataFim.add(Calendar.DAY_OF_MONTH, 5);
            e.setDataFim(dataFim.getTime());
            e.setRenovacoes(e.getRenovacoes()+1);
            Usuario logado = (Usuario) req.getSession().getAttribute("logado");
            
            //---------------- Alterar dados no cache da sessão
            logado.getEmprestimoList().set(logado.getEmprestimoList().indexOf(e), e);
          /*  Emprestimo emprestimoUsuarioLogado = logado.getEmprestimoList().get(logado.getEmprestimoList().indexOf(e));
            emprestimoUsuarioLogado.setRenovacoes(emprestimoUsuarioLogado.getRenovacoes()+1);
            emprestimoUsuarioLogado.setDataFim(dataFim.getTime());*/
            // --------------------------------
            emprestimoDao.edit(e);

            obj.addProperty("text", "Renovação realizada com sucesso!");
        } else {
            obj.addProperty("text", "Quantidade máxima de renovações atingida!");
        }

        req.setAttribute("json", obj);

        return "Json";
    }

}
