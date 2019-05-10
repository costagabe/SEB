<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <!--Import Google Icon Font-->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <!--Import materialize.css-->
        <link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen"/>
        <link type="text/css" rel="stylesheet" href="css/styles.css"  media="screen"/>

        <!--Let browser know website is optimized for mobile-->
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body >

        <div class="container center-align ">
            <div class="row ">
                <div class="row center-text">
                    <h1 style="color: rgba(38, 166, 154, 0.39)">SEB - Login</h1>
                </div>
                <div class="col s10 m10 offset-s1 offset-m1  l8 offset-l2  login-container ">
                    <div class="row">

                        <div class="input-field col s8 offset-s2" style="position: relative;margin-top: 50px;">
                            <input  id="usuario" type="text" class="validate" autocomplete="false">
                            <label for="usuario">Usuário</label>
                        </div>
                        <div class="input-field col s8 offset-s2" style="position: relative;margin-top: 50px;">
                            <input  id="senha" type="password" class="validate" autocomplete="false" onkeydown="if(event.keyCode ===13){logar()};">
                            <label for="senha">Senha</label>
                        </div>

                        <div class="col s3 offset-s7" style="margin-top: 15px">
                            <a  class="waves-effect waves-light btn" onclick="logar()"><i class="material-icons left">check</i>Login</a>
                        </div>
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


        <script type="text/javascript" src="js/materialize.min.js"></script> 
        
        <script type="text/javascript" src="js/jquery.js"></script> 
        <script type="text/javascript" src="js/script.js"></script> 
    </body>
</html>
