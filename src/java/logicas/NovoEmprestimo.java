package logicas;

import beans.Emprestimo;
import beans.Livro;
import beans.Usuario;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import daos.EmprestimoJpaController;
import daos.LivroJpaController;
import daos.UsuarioJpaController;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.ParameterGetter;

public class NovoEmprestimo implements Logica {

    @Override
    public String executa(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        return "novo-emprestimo.jsp";
    }

    public String emprestar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
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

                if (u.getEmprestimoAtivoList().size() < 3) {
                    if (!jaTemLivro(u.getEmprestimoList(), l)) {
                        if (l.getQtdEstoque() > 0) {
                            if (!u.temMulta()) {
                                Emprestimo e = new Emprestimo();

                                e.setLivro(l);
                                e.setUsuario(u);
                                e.setDataInicio(Calendar.getInstance().getTime());

                                Calendar dataFim = Calendar.getInstance();
                                dataFim.add(Calendar.DAY_OF_MONTH, 5);
                                e.setDataFim(dataFim.getTime());

                                emprestimoDao.create(e);

                                l.setQtdEstoque(l.getQtdEstoque() - 1);
                                livroDao.edit(l);

                                obj.addProperty("text", "Empréstimo realizado com sucesso!");
                            } else {
                                obj.addProperty("text", "O usuário possui multas para pagar!");
                            }
                        } else {
                            obj.addProperty("text", "Não há livros para emprestar!");
                        }

                    } else {
                        obj.addProperty("text", "Este usuário já emprestou esse livro!");
                    }
                } else {
                    obj.addProperty("text", "Limite de empréstimos atingido!");
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

    private boolean jaTemLivro(List<Emprestimo> emprestimoList, Livro l) {
        for (Emprestimo e : emprestimoList) {
            if (e.getLivro().equals(l) && e.getDataEntrega() == null) {
                return true;
            }
        }
        return false;
    }

}
