<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class=" col s11 offset-s1">
    <table class="striped">
        <thead>
        <th>Valor da multa</th>
        <th>Situacao</th>
        </thead>
        <tbody>
            <c:forEach items="#{logado.multaList}" var="m">
            <tr>
                <td>R$ ${m.valor},00</td>
                <td>${m.situacaoString}</td>

            </tr>
            </c:forEach>
        </tbody>

    </table>
</div>