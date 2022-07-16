<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
    $(document).ready(function() {
        $("#send").click(function () {
            $("#frm").attr("action", "/api/login"); // attribute setting
            $("#frm").submit();
        });
    });
</script>

<body>

<form id="frm" method="post">
    <div class="form-group">
        <label for="inputID">ID</label>
        <input type="tel" class="form-control" id="inputID" name="username" placeholder="ID">
    </div>
    <div class="form-group">
        <label for="inputPassword">Password</label>
        <input type="password" class="form-control" id="inputPassword" name="password" placeholder="Password">
    </div>
    <div class="form-group">
        <label class="form-check-label"><input type="checkbox"> Remember me</label>
    </div>
    <button type="button" id="send" class="btn btn-primary">Sign in</button>
</form>

</body>
</html>