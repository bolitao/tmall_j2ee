<%--
  Created by IntelliJ IDEA.
  User: Boli Tao
  Date: 2019/8/25
  Time: 16:06
  To change this template use File | Settings | File Templates.
  TODO:
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<script>
    $(function () {
        $("ul.pagination li.disabled a").click(function () {
            return false;
        });
    });

</script>
<nav>
    <ul class="pagination">
        <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
            <a href="?page.start=0" aria-label="Previous">
                <span aria-hidden="true">«</span>
            </a>
        </li>
        <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
            <a href="?page.start=${page.start-page.count}" aria-label="Previous">
                <span aria-hidden="true">‹</span>
            </a>
        </li>
        <c:forEach begin="0" end="${page.totalPage-1}" varStatus="status">
            <li>
                <a href="?page.start=${status.index*page.count}" class="current">${status.count}</a>
            </li>
        </c:forEach>
        <li>
            <a href="?page.start=${page.start+page.count}" aria-label="Next">
                <span aria-hidden="true">›</span>
            </a>
        </li>
        <li>
            <a href="?page.start=${page.last}" aria-label="Next">
                <span aria-hidden="true">»</span>
            </a>
        </li>
    </ul>
</nav>