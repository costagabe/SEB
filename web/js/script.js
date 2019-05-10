var livros;
var el;
var inst;
document.addEventListener('DOMContentLoaded', function () {
    el = document.querySelectorAll('.autocomplete');
    inst = M.Autocomplete.init(el, {minLength: 4, onAutocomplete: buscarLivro});
});


document.addEventListener('DOMContentLoaded', function () {
    var elems = document.querySelectorAll('.modal');
    var instances = M.Modal.init(elems, {});
});
function confirmarRecebimento(){
   var dados = {
        usuario: $("#usuarioAluno").val(),
        idLivro : $("#idLivro").val()
    };
    enviarDados("ConfirmarRecebimento",dados,"receber", showModalObj); 
}
function realizarEmprestimo(){
    var dados = {
        usuario: $("#usuarioAluno").val(),
        idLivro : $("#idLivro").val()
    };
    enviarDados("NovoEmprestimo",dados,"emprestar", showModalObj);
}
function renovarEmprestimo(id){
    var dados = {
        id : id
    };
    enviarDados("Emprestimos",dados,"renovar",showModalObj);
}
function procurarLivros() {
    if (event.keyCode === 13) {
        openPage("Buscar?pesquisa=" + $("#autocomplete-input").val());
    }
    if ($("#autocomplete-input").val().length > 3) {
        $.ajax({
            url: "Buscar?autocomplete=true&pesquisa=" + $("#autocomplete-input").val(),
            data: {insideSystem: true}
        }).done(function (data) {
            var dados = JSON.parse(data);
            inst[0].options.data = dados;
        });

    }
}
function cadastrarLivro() {
    var livro = {
        autor: $("#autorLivro").val(),
        titulo: $("#tituloLivro").val(),
        editora: $("#editoraLivro").val(),
        descricao: $("#descricaoLivro").val(),
        qtdEstoque: $("#qtdLivro").val()
    };
    enviarDados("CadastrarLivro", livro, "cadastrar", finalizarCadastroLivro);
}
function finalizarCadastroLivro(obj) {

    $("#autorLivro").val("");
    $("#tituloLivro").val("");
    $("#editoraLivro").val("");
    $("#descricaoLivro").val("");
    $("#qtdLivro").val("");
    showModalObj(obj);
}
function showModalObj(obj) {
    showModal(obj.header, obj.text);
}

function cadastrarAluno(){
    var aluno = {
        usuario: $("#usuarioAluno").val(),
        nome: $("#nomeAluno").val(),
        senha: $("#senhaAluno").val()
    };
    enviarDados("CadastrarAluno",aluno,"cadastrar",showModalObj);
}
/**
 * 
 * @param {string} pagina pagina da requisição
 * @param {obj} dados obj json com os dados
 * @param {string} acao acção a ser chamada
 * @param {function} whenDone função que vai ser chamada no final
 */

function enviarDados(pagina, dados, acao, whenDone) {
    $.ajax({
        url: pagina,
        method: "POST",
        data: {insideSystem: true, dados: JSON.stringify(dados), acao: acao}
    }).done(function (data) {
        var dados = JSON.parse(data);
        whenDone(dados);
    });


}
function buscarLivro() {
    openPage("Buscar?pesquisa=" + $("#autocomplete-input").val());
}
$(document).ready(function () {
    var paginaRequerida = window.location.href.split('/')[4];
    //console.log(window.location.href.split('/')[4]);
    //console.log(paginaRequerida === "");
    if (paginaRequerida !== "Sistema" && paginaRequerida !== "Logout" && paginaRequerida !== "" && paginaRequerida !== "Login") {
        openPage(paginaRequerida);
    }
    carregaMenus();
});
function alterarLivro(id){
   var dados = {id:id};
    openPage("CadastrarLivro","editarLivro",dados);
}
function carregaMenus() {
    var menus = [
        {pagina: "ConfirmarRecebimento", tituloPagina: "Confirmar Recebimento", icon: "check", textoMenu: "Confirmar Recebimento", nv: 2},
        {pagina: "ConfirmarPagamento", tituloPagina: "Confirmar Pagamento", icon: "attach_money", textoMenu: "Confirmar Pagamento", nv: 2},
        {pagina: "NovoEmprestimo", tituloPagina: "Novo Empréstimo", icon: "library_books", textoMenu: "Novo Empréstimo", nv: 2},
        {pagina: "CadastrarLivro", tituloPagina: "Cadastrar Livro", icon: "book", textoMenu: "Cadastrar Livro", nv: 2},
        {pagina: "CadastrarAluno", tituloPagina: "Cadastrar Aluno", icon: "account_box", textoMenu: "Cadastrar Aluno", nv: 2},
        {pagina: "Emprestimos", tituloPagina: "Meus Empréstimos", icon: "library_books", textoMenu: "Meus Empréstimos", nv: 1},
        {pagina: "Multas", tituloPagina: "Minhas Multas", icon: "attach_money", textoMenu: "Minhas Multas", nv: 1},
        {pagina: "Historico", tituloPagina: "Histórico", icon: "list", textoMenu: "Histórico", nv: 1}

    ];

    var c = "";
    for (var i = 0; i < menus.length; i++) {
        if (menus[i].nv === logado.nivel) {
            c += ` <li><a class="waves-effect botao-menu-lateral hvr-back-pulse" href="#" onclick="return openPage('${menus[i].pagina}')"><div class="valign-center"> <i class="material-icons center-icon">${menus[i].icon} </i> <span> &nbsp; ${menus[i].textoMenu} </span></div></a></li>`;
        }
    }
    $("#menus").html(c + $("#menus").html());
}
function logar() {
    var usuario = $("#usuario").val();
    var senha = $("#senha").val();


    $.ajax({
        url: "Login",
        data: {usuario: usuario, senha: senha}
    }).done(function (data) {
        var dados = JSON.parse(data);

        if (dados.codigo === 1) {
            window.location.href = dados.pagina;
        } else {
            showModal(dados.titulo, dados.texto);
        }
    });
}
function editarLivro(id){
    var livro = {
        autor: $("#autorLivro").val(),
        titulo: $("#tituloLivro").val(),
        editora: $("#editoraLivro").val(),
        descricao: $("#descricaoLivro").val(),
        qtdEstoque: $("#qtdLivro").val(),
        id:id
    };
    enviarDados("CadastrarLivro",livro,"editar",showModalObj);
}
function openPage(page, acao = 'executa', dados) {

    $.ajax({
        url: page,
        data: {insideSystem: true, acao: acao, dados: JSON.stringify(dados)}
    }).done(function (data) {

        $("#conteudo").html(data);
        if (page !== "Buscar" || page !== "") {
            window.history.pushState(page, page, page);
        }
    });

    return false;
}

function showModal(header, text) {
    var instance = M.Modal.getInstance($("#modal1"));
    $("#modal-header").html(header);
    $("#modal-text").html(text);
    instance.open();
}
