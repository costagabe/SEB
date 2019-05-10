<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <!--Import Google Icon Font-->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <!--Import materialize.css-->
        <link type="text/css" rel="stylesheet" href="css/materialize.min.css" />
        <link type="text/css" rel="stylesheet" href="css/hover-min.css"  />
        <link type="text/css" rel="stylesheet" href="css/styles.css"  />

        <!--Let browser know website is optimized for mobile-->
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>SEB</title>

    </head>
    <body >

        <div class="col s12">
            <div class="row">
                <div class="col s2 menu-lateral">
                    <div class="row header-menu-lateral">
                        <h1>SEB</h1>
                        <h6>Seja bem vindo(a), ${logado.nome}!</h6>
                    </div>
                    <div class="row">

                        <ul id="menus"> 
                            <!--   <li><a class="waves-effect botao-menu-lateral hvr-back-pulse" href="#" onclick="return openPage('Emprestimos', 'Meus Empréstimos')"><div class="valign-center"> <i class="material-icons center-icon">check </i> <span> &nbsp; Confirmar Recebimento </span></div></a></li>
                              <li><a class="waves-effect botao-menu-lateral hvr-back-pulse" href="#" onclick="return openPage('Emprestimos', 'Meus Empréstimos')"><div class="valign-center"> <i class="material-icons center-icon">attach_money </i> <span> &nbsp; Confirmar Pagamento </span></div></a></li>
                               <li><a class="waves-effect botao-menu-lateral hvr-back-pulse" href="#" onclick="return openPage('Emprestimos', 'Meus Empréstimos')"><div class="valign-center"> <i class="material-icons center-icon">library_books </i> <span> &nbsp; Novo Empréstimo </span></div></a></li>
                               <li><a class="waves-effect botao-menu-lateral hvr-back-pulse" href="#" onclick="return openPage('Emprestimos', 'Meus Empréstimos')"><div class="valign-center"> <i class="material-icons center-icon">book </i> <span> &nbsp; Cadastrar Livro </span></div></a></li>
                               <li><a class="waves-effect botao-menu-lateral hvr-back-pulse" href="#" onclick="return openPage('Emprestimos', 'Meus Empréstimos')"><div class="valign-center"> <i class="material-icons center-icon">account_box </i> <span> &nbsp; Cadastrar Aluno </span></div></a></li>
                              <li><a class="waves-effect botao-menu-lateral hvr-back-pulse" href="#" onclick="return openPage('Emprestimos', 'Meus Empréstimos')"><div class="valign-center"> <i class="material-icons center-icon">library_books </i> <span> &nbsp; Meus empréstimos </span></div></a></li>
                              <li><a class="waves-effect botao-menu-lateral hvr-back-pulse" href="#"  onclick="return openPage('Multas','Minhas Multas')"><div class="valign-center"> <i class="material-icons center-icon">attach_money</i> <span> Minhas multas </span></div></a></li>
                              <li><a class="waves-effect botao-menu-lateral hvr-back-pulse" href="#"  onclick="return openPage('Historico', 'Meu Histórico')"><div class="valign-center"> <i class="material-icons center-icon">list</i> <span> &nbsp;Histórico </span></div></a></li> -->
                            <li><a class="waves-effect botao-menu-lateral hvr-back-pulse" href="Logout"><div class="valign-center"> <i class="material-icons center-icon">power_settings_new</i> <span> &nbsp;Sair </span></div></a></li>


                        </ul>
                    </div>
                </div>
                <div class="col s8 ">
                    <div class="row">
                        <div class="row">
                            <div class="col s12">
                                <div class="row">
                                    <div class="input-field col s12">
                                        <i class="material-icons prefix">search</i>
                                        <input type="text" id="autocomplete-input" onkeyup="procurarLivros()" class="autocomplete">
                                        <label for="autocomplete-input">Pesquisar</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                       <!-- <form class="col s12">
                            <div class="row">
                                <div class="input-field col s12">
                                    <i class="material-icons prefix">search</i>
                                    <input id="icon_prefix" type="text" class="" onkeydown="if (event.keyCode === 13) {
                                                return false
                                            }
                                            ;">
                                    <label for="icon_prefix">Pesquisar</label>
                                </div>

                            </div>
                        </form>-->
                    </div>
                    <div class="row" id="conteudo">
                        
                    </div>

                </div>
            </div>


        </div>



        <div id="modal1" class="modal">
            <div class="modal-content">
                <h4 id="modal-header"></h4>
                <p id="modal-text"></p>
            </div>
            <div class="modal-footer">
                <a href="#!" class="modal-close waves-effect waves-green btn-flat">Ok</a>
            </div>
        </div>

        <script>
            var logado = {nivel:${logado.nivel}};
        </script>
        <script type="text/javascript" src="js/materialize.min.js"></script> 
        <script type="text/javascript" src="js/jquery.js"></script> 
        <script type="text/javascript" src="js/script.js"></script> 
    </body>
</html>
