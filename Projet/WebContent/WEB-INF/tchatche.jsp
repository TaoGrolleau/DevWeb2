<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edition Collaborative de Documents</title>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
	<script type="text/javascript" src="fonctions.js"></script>
	<a id=link href="Connexion">Retour à l'accueil</a>
	<jsp:include page="affichage.jsp" />
	<jsp:include page="saisie.html" />
	<jsp:include page="affichageFichier.jsp" />
</body>
</html>