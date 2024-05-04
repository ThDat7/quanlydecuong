<%--
    Document   : index
    Created on : Jul 7, 2023, 1:08:19 PM
    Author     : admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<section class="container">
    <h1 class="text-center text-info mt-1">DANH SÁCH NGƯỜI DÙNG</h1>
    <a href="<c:url value='/users/create' />" class="btn btn-info">Thêm nguười dùng</a>


    <c:if test="${counter > 1}">
        <ul class="pagination mt-1">
            <li class="page-item"><a class="page-link" href="${action}">Tất cả</a></li>
            <c:forEach begin="1" end="${counter}" var="i">
                <c:url value="/" var="pageUrl">
                    <c:param name="page" value="${i}"></c:param>
                </c:url>
                <li class="page-item"><a class="page-link" href="${pageUrl}">${i}</a></li>
            </c:forEach>
        </ul>
    </c:if>

    <table class="table table-hover">
        <thead>
        <tr>
            <th>Id</th>
            <th>Tên</th>
            <th>Chức vụ</th>
            <th>Email</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users}" var="u">
            <tr>
                <td>${u.id}</td>
                <td>${u.lastName} ${u.firstName}</td>
                <td>${u.role}</td>
                <td>${u.email}</td>
                <td>
                    <a href="<c:url value='/users/${u.id}'/>" class="btn btn-success">Cập nhật</a>
                    <a href="<c:url value='/users/delete/${u.id}'/>" class="btn btn-danger">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</section>
