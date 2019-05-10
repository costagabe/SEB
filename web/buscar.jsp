<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach items="#{livros}" var="livro">
    <div class="row">
        <div class="col s10 offset-s1 book-container">
            <div class="row">
                <h4 style="text-align: center;">${livro.titulo}</h4>

            </div>
            <div class="row valign-center">
                <i class="large material-icons center-icon">library_books </i>
                <span style="text-align: justify; margin-left: 10px; margin-right: 15px;    ">${livro.descricao}</span>
            </div>
            <div class="row">
                <div class="col s10 offset-s1">
                    <div class="row">
                        <div class="col s3" >Id: ${livro.id} </div>
                        <div class="col s3">Autor: ${livro.autor} </div>
                        <div class="col s3">Editora: ${livro.editora} </div>
                        <div class="col s3">Disponível: ${livro.qtdEstoque}</div> 
                    </div>
                </div>

            </div>
            <div class="row">
                <div class="col s4"><a class="waves-effect waves-teal btn-flat hvr-back-pulse fill-width center-text">Reservar</a></div>
                <div class="col s4"><a class="waves-effect waves-teal btn-flat hvr-back-pulse fill-width center-text">Realizar Empréstimo</a></div>
                <div class="col s4"><a class="waves-effect waves-teal btn-flat hvr-back-pulse fill-width center-text" onclick="alterarLivro(${livro.id})">Alterar Dados</a></div>

            </div>
        </div>
    </div>
</c:forEach>
<c:if test="${vazio == 'true'}">
    <h1 style="text-align: center">Nenhum resultado encontrado</h1>
</c:if>