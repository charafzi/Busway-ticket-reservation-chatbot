<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="project.sysResTicketbw.dto.*" %>
<%@ page import="java.util.*" %>
<%@ page import="project.sysResTicketbw.bo.*" %>
<%@ page import="project.sysResTicketbw.service.*" %>
<%@ page import="project.sysResTicketbw.controleur.*" %> 
<!DOCTYPE html>
<html>
<head>
<%@ include file="resources/navbaradmin.jsp" %>
    <meta charset="UTF-8">
    <title>Modifier Conducteur</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            font-family: Bradley Hand, cursive; /* Utilisation de la police Arial */
            background-color: #f0f0f0; /* Couleur de fond */
        }

        .navbar {
            background-color: #090C9B !important; /* Couleur de fond de la navbar */
        }

        .card-header {
            background-color: #FFFFFF; /* Couleur de fond de l'en-tête de la carte */
            color: #000000; /* Couleur du texte de l'en-tête de la carte */
            border-radius: 10px; /* Rayon de la bordure */
            margin-top: 20px; /* Marge en haut de l'en-tête de la carte */
            padding: 20px; /* Espacement interne de l'en-tête de la carte */
        }

        .form-control {
            border-radius: 5px; /* Rayon de la bordure des champs de formulaire */
            background-color: #FFFFFF; /* Couleur de fond des champs de formulaire */
        }

        .btn-primary {
            background-color: #FF0000; /* Couleur de fond du bouton principal */
            border-color: #FF0000; /* Couleur de la bordure du bouton principal */
            border-radius: 5px; /* Rayon de la bordure du bouton principal */
        }

        .btn-primary:hover {
            background-color: #FFFFFF; /* Couleur de fond du bouton principal au survol */
            border-color: #FF0000; /* Couleur de la bordure du bouton principal au survol */
            color: #FF0000; /* Couleur du texte du bouton principal au survol */
        }

        .btn-success {
            background-color: #FF0000; /* Couleur de fond du bouton de succès */
            border-color: #FF0000; /* Couleur de la bordure du bouton de succès */
            border-radius: 5px; /* Rayon de la bordure du bouton de succès */
        }

        .btn-success:hover {
            background-color: #FFFFFF; /* Couleur de fond du bouton principal au survol */
            border-color: #FF0000; /* Couleur de la bordure du bouton principal au survol */
            color: #FF0000; /* Couleur du texte du bouton principal au survol */
        }

        .alert-success {
            background-color: #32CD32; /* Couleur de fond de l'alerte de succès */
            border-color: #32CD32; /* Couleur de la bordure de l'alerte de succès */
            color: #FFFFFF; /* Couleur du texte de l'alerte de succès */
        }

        .alert-danger {
            background-color: #B22222; /* Couleur de fond de l'alerte d'erreur */
            border-color: #B22222; /* Couleur de la bordure de l'alerte d'erreur */
            color: #FFFFFF; /* Couleur du texte de l'alerte d'erreur */
            
        }
         .center-div {
            margin: 0 auto; /* Centrer horizontalement */ 
            width: 50%; /* Largeur du formulaire */
            border-radius:15px;
        
            margin-top:15px;
        }
        .btn{
        margin: 5px;
        }
    </style>
</head>
<body>
<% ConducteurDTO conducteurAModifier = (ConducteurDTO) request.getAttribute("conducteurAModifier"); %>
<% //request.setAttribute("conducteurA", conducteurAModifier.getFullName());%>
<script>
    function addBusField() {
        var index = document.getElementsByClassName("bus-field").length + 1;

        var formGroup = document.createElement("div");
        formGroup.className = "form-group";

        var busDiv = document.createElement("div");

        var busLabel = document.createElement("label");
        busLabel.textContent = "Bus " + index + " :";
        busLabel.className = "col-form-label";

        var busField = document.createElement("input");
        busField.type = "text";
        busField.className = "form-control bus-field";
        busField.name = "bus"; 
        busField.required = true;

        busDiv.appendChild(busLabel);
        busDiv.appendChild(busField);
        formGroup.appendChild(busDiv);

        var container = document.getElementById("Container");

        // Ajouter le groupe de stations au conteneur
        container.appendChild(formGroup);
    }

 
</script>
<div class="container mt-4">
    <div class="card">
        <div class="card-header">
            <h5 class="card-title">Modifier Conducteur</h5>
        </div>
        <% 
   	String Message = (String) request.getAttribute("MessageModif");
%>

<% if( Message != null && !Message.isEmpty()){%>
 <div class="alert alert-danger" role="alert">
        <%= Message %>
    </div>
    <% } %>
        <div class="card-body">
            <form action="Info_Conducteur" method="post">
                
                <%-- Affichez les informations du conducteur dans les champs du formulaire --%>
                <div class="form-group">
                    <label for="nom">Nom Conducteur :</label>
                    <input type="text" class="form-control" id="nom" name="nom" value="<%= conducteurAModifier.getNom() %>" required>
                    
                </div>
                <div class="form-group">
                    <label for="prenom">Prénom Conducteur :</label>
                    <input type="text" class="form-control" id="prenom" name="prenom" value="<%= conducteurAModifier.getPrenom() %>" required>
                </div>
                <div class="form-group">
                    <label>Liste des bus :</label>
                    <ul class="list-group">
                        <% Collection<BusDTO> busesDisponibles = conducteurAModifier.getBusDTO(); %>
                        <% for (BusDTO bus : busesDisponibles) { %>
                            <li id="bus-<%= bus.getNumBus() %>" class="list-group-item d-flex justify-content-between align-items-center">
                                <%= bus.getNumBus() %>
                               
                            </li>
                        <% } %>
                    </ul>
                </div>
                <div id="Container">
						  <!-- Le select sera ajouté ici -->
				</div>
                <button type="button" class="btn btn-primary" onclick="addBusField()">Affecter un nouveau bus</button>
                <input type="hidden" name="lastname" value="<%= conducteurAModifier.getFullName() %>">
                <button type="submit" class="btn btn-primary" name="action" value="ModificationT">Enregistrer les modifications</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>
