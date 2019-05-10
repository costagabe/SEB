
package utils;

import java.util.HashMap;
import java.util.Map;


public  class LevelAccess {
    private static final Map<String, Integer> ACCESS = new HashMap<>();
    
    
    public static boolean canEnter(String page, int level){
        if(ACCESS.isEmpty()){
            fillKeys();
        }
        
        return ACCESS.get(page) == level || ACCESS.get(page) == 0;
    }

    
    private static void fillKeys() {
        ACCESS.put("Buscar", 0);
        ACCESS.put("ConfirmarRecebimento", 2);
        ACCESS.put("ConfirmarPagamento", 2);
        ACCESS.put("NovoEmprestimo", 2);
        ACCESS.put("CadastrarLivro", 2);
        ACCESS.put("CadastrarAluno", 2);
        ACCESS.put("Emprestimos", 1);
        ACCESS.put("Multas", 1);
        ACCESS.put("Historico", 1);
        ACCESS.put("Sistema", 0);
        ACCESS.put("Login", 0);
        ACCESS.put("Logout", 0);
        
    }
    
}
