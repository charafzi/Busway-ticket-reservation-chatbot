<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="project.sysResTicketbw.dto.*" %>
<%@ page import="java.util.*" %>
<%@ page import="project.sysResTicketbw.bo.*" %>
<%@ page import="project.sysResTicketbw.service.*" %>
<%@ page import="project.sysResTicketbw.controleur.*" %> 
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifier Bus</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
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
<% BusDTO busAmodifier = (BusDTO) request.getAttribute("busAmodifier"); 
Collection<ConducteurDTO> lesConducteurs = (Collection<ConducteurDTO>) request.getAttribute("les_conducteurs");
%>


<div class="container mt-4">
    <div class="card">
        <div class="card-header">
            <h5 class="card-title">Modifier Bus</h5>
        </div>

        <div class="card-body">
            <form action="AllBuss" method="post">
                
                <%-- Affichez les informations du conducteur dans les champs du formulaire --%>
                <div class="form-group">
                    <label for="NumBus">Numero de Bus :</label>
                    <input type="text" class="form-control" value="<%= busAmodifier.getNumBus()%>" disabled>
                    <input type="hidden" id="NumBus" name="NumBus" value="<%= busAmodifier.getNumBus()%>" > 
                    
                </div>
                <div class="form-group">
                    <label for="NBplcLimite">Nombre de place Limite :</label>
                    <input type="text" class="form-control" id="NBplcLimite" name="NBplcLimite" value="<%= busAmodifier.getNbPlacesLimite() %>" required>
                </div>
                
                <button type="submit" class="btn btn-primary" name="action" value="ModificationT">Enregistrer les modifications</button>
            </form>
        </div>
    </div>
</div>
<script> 
function AjouterNouvelleListe() {
    // Créer un nouvel index en fonction du nombre de champs de bus existants
    var index = document.querySelectorAll(".bus-field").length + 1;

    // Créer un nouvel élément de groupe de formulaire
    var formGroup = document.createElement("div");
    formGroup.className = "form-group";

    // Créer un élément de div pour le champ de bus
    var busDiv = document.createElement("div");

    // Créer une étiquette pour le champ de bus
    var busLabel = document.createElement("label");
    busLabel.textContent = "Bus " + index + " :";
    busLabel.className = "col-form-label";

    // Créer un champ de texte pour le bus
    var busField = document.createElement("input");
    busField.type = "text";
    busField.className = "form-control bus-field";
    busField.name = "bus" ; // Nom unique pour chaque champ de bus
    busField.required = true;

    // Ajouter l'étiquette et le champ de texte à la div du bus
    busDiv.appendChild(busLabel);
    busDiv.appendChild(busField);
    
    // Ajouter la div du bus au groupe de formulaire
    formGroup.appendChild(busDiv);

    // Trouver le formulaire et ajouter le groupe de formulaire au formulaire
    var form = document.querySelector("form");
    form.appendChild(formGroup);
}
</script>
</body>
</html>
