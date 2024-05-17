<%@page import="project.sysResTicketbw.service.CatalogueBus"%>
<%@page import="project.sysResTicketbw.dto.*"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Saisie Info Conducteur</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Red+Hat+Text:ital,wght@0,400;0,427;0,500;0,600;0,667;0,700;1,400;1,427;1,500;1,600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" integrity="sha512-d2K0gq9hX7jEDYRRBknQs8l4RbxgPkXUJ98F1vrz1rUqCrlVxS7+AljLQ2r/l91NY+wAa6Rto3WUO29Vgb6Jpw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <style>
        body {
            font-family: Bradley Hand, cursive;
            background-color: #f0f0f0;
            margin: 0;
            padding: 0;
        }

        .navbar {
            background-color: #090C9B !important;
        }

        .container {
            margin-top: 50px;
            max-width: 600px;
            background-color: #fff;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            margin: 0 auto; /* Centrer horizontalement */
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            font-weight: bold;
        }

        input[type="text"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 10px;
            box-sizing: border-box;
        }

        button[type="button"],
        button[type="submit"] {
            background-color: #FF0000;
            border: none;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
        }

        button[type="button"]:hover,
        button[type="submit"]:hover {
            background-color: #FFFFFF;
            border-color: #FF0000;
            color: #FF0000;
        }

        .btn-add-bus {
            margin-left: 10px;
        }

        /* Placeholder style */
        input[type="text"]::placeholder {
            color: #999;
        }
        .center-div {
            margin: 0 auto; /* Centrer horizontalement */ 
            width: 37%; /* Largeur du formulaire */
            border-radius:15px;
            border: 2px solid white;
            margin-top:15px;
        }
    </style>
</head>
<body>
<%@ include file="resources/navbaradmin.jsp" %>
<% List<BusDTO> lesbus = (List<BusDTO> ) CatalogueBus.getInstance().getBus(); %>
<% String erreur = (String) session.getAttribute("Message"); 
	String success= (String) session.getAttribute("MessageZ"); 
if(erreur != null){
%>
<div class="alert alert-danger">
    <%= erreur %>
<% session.setAttribute("erreur", null); %>
</div>

<%} 
if(success != null){
%>
<div class="alert alert-success">
    <%= success %>
<% session.setAttribute("success", null); %>
</div>

<%} %>


<div class="center-div">
<div class="container">
    <h5 class="text-center mb-4">Saisir Informations Conducteur</h5>

    <form action="Info_Conducteur" method="post">
        <div class="form-group">
            <label for="matr">Matricule:</label>
            <input type="text" id="matr" name="matr" placeholder="Entrez le matricule" required>
        </div>

        <div class="form-group">
            <label for="nom">Nom Conducteur:</label>
            <input type="text" id="nom" name="nom" placeholder="Entrez le nom" required>
        </div>

        <div class="form-group">
            <label for="prenom">Prénom Conducteur:</label>
            <input type="text" id="prenom" name="prenom" placeholder="Entrez le prénom" required>
        </div>

        <div class="form-group">
            <label for="staticBus">Affecter bus:</label>
            <select name="bus" class="form-control" required>
            <%for(BusDTO b:lesbus){ %>
            <option value="<%= b.getNumBus() %>"><%= b.getNumBus() %></option>
            <%} %>
            </select>
            <br>
            <div id="busContainer">
			  <!-- Le select sera ajouté ici -->
			</div>
			<br>
            <button type="button" class="btn-add-bus" onclick="addBusField()">+</button>
        </div>

        <button type="submit" name="action" value="Ajouter">Enregistrer</button>
    </form>
</div>
</div>
<script>
  function addBusField() {
    // Créer un nouvel élément select
    var select = document.createElement("select");
    select.className = "form-control";
    select.name = "bus";

    // Ajouter les options de bus
    <% for(BusDTO b:lesbus){ %>
      var option = document.createElement("option");
      option.value = "<%= b.getNumBus() %>";
      option.text = "<%= b.getNumBus() %>";
      select.appendChild(option);
    <% } %>

    // Trouver le conteneur où ajouter le select
    var container = document.getElementById("busContainer");

    // Ajouter le select au conteneur
    container.appendChild(select);
  }
</script>

</body>
</html>
    