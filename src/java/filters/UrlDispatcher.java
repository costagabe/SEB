package filters;

import beans.Usuario;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logicas.Logica;
import utils.LevelAccess;

@WebFilter(filterName = "UrlDispatcher", urlPatterns = {"/*"})
public class UrlDispatcher implements Filter {

    private static final boolean debug = false;
    private FilterConfig filterConfig = null;

    public UrlDispatcher() {
    }

    private boolean contains(String palavra, String[] sequencias) {
        for (String s : sequencias) {
            if (palavra.contains(s)) {
                return true;
            }
        }
        return false;
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("UrlDispatcher:DoBeforeProcessing");
        }

    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
           // log("UrlDispatcher:DoAfterProcessing");
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = ((HttpServletRequest) request);
        if (!contains(req.getRequestURI(), new String[]{"css", "js", "img"})) {
            HttpServletResponse resp = ((HttpServletResponse) response);
            String pageRequest;
            int userLevel = 0;
            try {
                pageRequest = req.getRequestURI().split("/")[2];
            } catch (Exception e) {
                pageRequest = "Login";
            }
            if (req.getSession().getAttribute("logado") == null) {
                pageRequest = "Login";
            } else if (req.getParameter("insideSystem") == null && !pageRequest.equals("Logout")) {
                pageRequest = "Sistema";
            } else if (req.getSession().getAttribute("logado") != null) {
                userLevel = ((Usuario) req.getSession().getAttribute("logado")).getNivel();
            }

            if (!LevelAccess.canEnter(pageRequest, userLevel)) {
                pageRequest = "AccessDenied";
            }
            Class<?> classe;
            Logica logica = null;
            String acao = "";
            try {
                classe = Class.forName("logicas." + pageRequest);

                 logica = (Logica) classe.newInstance();

                EntityManagerFactory emf = Persistence.createEntityManagerFactory("SEBPU");
                acao = req.getParameter("acao");
                String pagina;
                if ( !pageRequest.equals("AccessDenied") && (acao != null || (acao != null && acao.equals("")))) {
                    pagina = (String) (logica.getClass().getMethod(acao, new Class[]{HttpServletRequest.class, HttpServletResponse.class, EntityManagerFactory.class}).invoke(logica,req, resp, emf));
                } else {
                    pagina = logica.executa(req, resp, emf);
                }
                emf.close();

                if (pagina != null) {
                    request.getRequestDispatcher(pagina).forward(request, response);
                }

            } catch (ClassNotFoundException ex) {
                log("Erro ao tentar instanciar a classe " + logica.getClass().getName());
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(UrlDispatcher.class.getName()).log(Level.SEVERE, null, ex);
            }catch(InvocationTargetException ex){
                log("Erro ao invocar método " + acao + " da classe " + logica.getClass().getName()+" o método lançou uma exceção interna") ;
                
            }
            catch (Exception ex) {
                log("Ocorreu uma exceção1");
                ex.printStackTrace();
                
            }
        }
        if (debug) {
            log("UrlDispatcher:doFilter()");
        }

        doBeforeProcessing(request, response);

        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {

            problem = t;
           //  t.printStackTrace();
        }

        doAfterProcessing(request, response);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("UrlDispatcher:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("UrlDispatcher()");
        }
        StringBuffer sb = new StringBuffer("UrlDispatcher(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (IOException ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (IOException ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
