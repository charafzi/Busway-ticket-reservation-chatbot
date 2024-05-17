<%@page import="project.sysResTicketbw.dto.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.*" %>
<%@ page import="project.sysResTicketbw.bo.*" %>
<%@ page import="project.sysResTicketbw.service.*" %>
<%@ page import="project.sysResTicketbw.controleur.*" %>  
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">


<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <style>
        body {
            font-family: Bradley Hand, cursive;
            background-color: #f0f0f0;
            padding-top: 20px;
        }

        .navbar {
            background-color: #090C9B !important;
            margin-top: -20px;
        }

        .form-container {
            background-color: #FFFFFF;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            margin-top: 20px;
            width: 50%;
            margin-left: auto;
            margin-right: auto;
        }

        .form-title {
            text-align: center;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            font-weight: bold;
        }

        input[type="text"],
        input[type="number"] {
            width: 100%;
            padding: 10px;
            border-radius: 5px;
            border: 1px solid #ced4da;
        }

        button[type="submit"] {
            width: 30%;
            padding: 10px;
            border-radius: 9px;
            background-color: #FF0000;
            border-color: #FF0000;
            color: #FFFFFF;
            cursor: pointer;
            transition: background-color 0.3s, border-color 0.3s, color 0.3s;
        }

        button[type="submit"]:hover {
            background-color: #FFFFFF;
            border-color: #FF0000;
            color: #FF0000;
        }
    </style>
</head>
<%@include file="resources/navbaradmin.jsp" %>
<body>
<div class="form-container">
    <h2 class="form-title">Ajouter un Bus</h2>
    <form action="AllBuss" method="post" >
        <div class="form-group">
            <label for="BusNum">Numero de bus :</label>
            <input type="text" class="form-control" id="numBus" name="numBus" required>
           
            <label for="busplceLimite">Nombre de places limite :</label>
            <input type="number" min="1" class="form-control" id="nbrplacelimite" name="nbrplacelimite" required>
        
        </div>
        <div class="form-group">
            <button type="submit" name="action" value="Ajouter" class="btn btn-success">Ajouter</button>
        </div>
    </form>
</div>

  
</body>
</html>
