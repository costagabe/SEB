
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col s10 offset-s1">
        <h3 class="center-text">Cadastro de livro</h3>
        <div class="input-field col s8 offset-s2">
            <input id="tituloLivro" type="text" name="tituloLivro" class="validate" value="${livro.titulo}">
            <label for="tituloLivro" class="${active}">Título do livro</label>
        </div>
        <div class="input-field col s8 offset-s2">
            <input id="autorLivro" type="text" name="autorLivro" class="validate"value="${livro.autor}">
            <label for="autorLivro" class="${active}">Autor do livro</label>
        </div>
        <div class="input-field col s8 offset-s2">
            <input id="editoraLivro" type="text" name="editoraLivro" class="validate "value="${livro.editora}">
            <label for="editoraLivro" class="${active}">Editora do livro</label>
        </div>
        <div class="input-field col s8 offset-s2">
          <textarea id="descricaoLivro" class="materialize-textarea " value="">${livro.descricao}</textarea>
          <label for="descricaoLivro" class="${active}">Descrição</label>
        </div>
        <div class="input-field col s8 offset-s2">
            <input id="qtdLivro" type="number" name="qtdLivro" value="${livro.qtdEstoque}" class="validate ">
            <label for="qtdLivro" class="${active}">Quantidade</label>
        </div>
            <c:if test="${active == null}">
        <div class="col s2 offset-s8">
            <a class="waves-effect waves-teal btn  fill-width center-text" onclick="cadastrarLivro()">Cadastrar</a>
        </div>
        </c:if>
        <c:if test="${active != null}">
        <div class="col s2 offset-s8">
            <a class="waves-effect waves-teal btn  fill-width center-text" onclick="editarLivro(${livro.id})">Editar</a>
        </div>
        </c:if>
    </div>