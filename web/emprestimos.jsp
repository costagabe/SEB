<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class=" col s11 offset-s1">
    <table class="striped">
        <thead>
        <th>Dia Empréstimo</th>
        <th>Data para entregar</th>
        <th>Data que foi entregue</th>
        <th>ID Livro</th>
        <th>Título Livro</th>
        <th>Dias atrasados</th>
        <th>Renovações</th>
        </thead>
        <tbody>
            <c:forEach items="#{logado.emprestimoList}" var="e">
            <tr>
                <td>${e.dataDoInicio}</td>
                <td>${e.dataDaEntrega}</td>
                <td>${e.dataQueEntregue}</td>
                <td>${e.livro.id}</td>
                <td>${e.livro.titulo}</td>
                <td>${e.diasAtrasados}</td>
                <td>${e.renovacoes} <a href="#" onclick="renovarEmprestimo(${e.id})">(renovar)</a></td>
            </tr>
            </c:forEach>
        </tbody>

    </table>
</div>