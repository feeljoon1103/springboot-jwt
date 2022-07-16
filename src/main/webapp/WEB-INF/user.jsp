<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
    $(document).ready(function() {
        $("#send").click(function () {
            $("#frm").attr("action", "/api/user"); // attribute setting
            $("#frm").submit();
        });
    });
</script>
<body>

<h2>SUCCESS</h2>



</body>
</html>