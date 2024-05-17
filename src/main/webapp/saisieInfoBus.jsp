<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.*" %>
<%@ page import="project.sysResTicketbw.dto.*" %>
<%@ page import="project.sysResTicketbw.service.*" %>
<%@ page import="project.sysResTicketbw.controleur.*" %>
<html>
<head>
<meta charset="UTF-8"> 
<title>Installer informations Busway</title>
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
    </style>


</head>
<body>
<%@include file="resources/navbaradmin.jsp" %>
<%
    List<BusDTO> listeBus = new ArrayList<BusDTO>();
	List<StationDTO> stations = CatalogueStation.getInstance().getStations();
    // Initialisez et ajoutez des éléments à votre liste listeBus
    listeBus = (List<BusDTO>) CatalogueBus.getInstance().getBus();
    request.setAttribute("listeBus", listeBus);

    // Initialisation de la liste des conducteurs
    List<ConducteurDTO> listeConducteur = new ArrayList<ConducteurDTO>();
    listeConducteur = (List<ConducteurDTO>) CatalogueConducteur.getInstance().getConducteurs();
 
    String MessageB = (String) session.getAttribute("MessageB");
	String Message = (String) session.getAttribute("Message");
%>

<% if( MessageB != null && !MessageB.isEmpty()){%>
 <div class="alert alert-success" role="alert">
        <%= MessageB %>
    </div>
    <% } %>
 <% if( Message != null && !Message.isEmpty()){%>
 <div class="alert alert-danger" role="alert">
        <%= Message %>
    </div>
    <% } %> 
    
<div class="center-div">
    
<div class="card-header">
   <h5 class="card-title text-center">Installer informations du Busway</h5>

    <form action="InfoBus" method="post">
        <div class="form-group">
        	<div class="row">
		       <div class="col">
		       		<label for="departure">Station Depart </label>
		           <!-- <input type="text" class="form-control"  name="depart"  class="form-control"required> --> 
		            <select id="depart" name="depart"  class="form-control" required>
		            <%for(StationDTO s:stations){ %>
		            <option value="<%= s.getNom() %>"><%= s.getAdresse() %></option>
		            <%} %>
		            </select>
		       </div>    
		       <div class="col">
		       		 <label for="departureTime">Heure départ :</label>
		            <input type="text" class="form-control" id="departTime" name="departTime" pattern="^([01]?[0-9]|2[0-3]):[0-5][0-9]$" required>
		            <small class="form-text text-muted">Format: HH:mm</small>
		       </div>     
		           
        		
        	</div>
	    </div>

        <div class="form-group">
        	<div class="row">
        		<div class="col">
        			<label for="arrival">Station Arrivée</label>
		            <!--  <input type="text" class="form-control" id="arrive" name="arrive" required>-->
		            <select id="arrive" name="arrive" class="form-control" required>
		            <%for(StationDTO s:stations){ %>
		            <option value="<%= s.getNom()%>"><%= s.getAdresse() %></option>
		            <%} %>
		            </select>
        		</div>
        		  <div class="col">
        			<label for="departureTime">Heure Arrive :</label>
		            <input type="text" class="form-control" id="arriveTime" name="arriveTime" pattern="^([01]?[0-9]|2[0-3]):[0-5][0-9]$" required>
		            <small class="form-text text-muted">Format: HH:mm</small>
        		</div>
        	</div>
		</div>

        <div class="form-group">
        	<div class="row">
        		<div class="col">
        			<label for="listConducteur">Choisissez un Conducteur :</label>
		            <select class="form-control" id="listConducteur" name="idConducteur">
		                <% for(ConducteurDTO c:listeConducteur){ %>
		                <option value="<%= c.getNom()+" "+ c.getPrenom()%>"> <%= c.getNom() + " "+ c.getPrenom() %></option>
		                <% } %>
		            </select>
        		</div>
        		<div class="col">
        			  <label for="listeBus">Choisissez un bus :</label>
		            <select  class="form-control" name="bus">
		                <% for (BusDTO bus : listeBus) { %>
		                <option value="<%= bus.getNumBus()%>">  <%= bus.getNumBus() %>    </option>
		                <% } %>
		            </select>
        		</div>
        	</div>     
        </div>

        <div class="form-group">
        	<div class="row">
        			<div class="col">
        					<label for="departure">Id voyage </label>
		            <input type="text" class="form-control" id="depart" name="idvoyage" required> 
        			</div>
        			<div class="col">
        				<label for="departureTime">Nombre de places limite :</label>
		            <input type="text" class="form-control" id="nbplace" name="nbplace"  required>
        			</div>
        	</div>          
        </div>
        <div class="form-group">
        	<div class="row">
        			<div class="col">
        			<label> Stations arrêts : </label>
		            </div>
        	</div>          
        </div>
        
        <div id="statContainer">
						  <!-- Le select sera ajouté ici -->
		</div>
		    
		<div class="form-group">
		    <div class="row">
		    	<div class="col">
		    	</div>
		    	
		        <div class="col">
		            <div class="station-field">
		                <button type="button" class="btn btn-primary" onclick="addStationField()">+</button>
		            </div>
		        </div>
		        <div class="col">
		            <button type="submit" name="action" value="Ajouter" class="btn btn-success">Enregistrer</button>
		        </div>
		    </div>
		</div>
		    </form>
		</div>
		</div>
<script>
function addStationField() {
    // Créer un nouveau groupe de stations
    var stationGroup = document.createElement("div");
    stationGroup.className = "form-group"; // Ajoutez la classe form-group

    // Créer un div pour la rangée
    var rowDiv = document.createElement("div");
    rowDiv.className = "row"; // Ajoutez la classe row

    // Créer un div pour la colonne
    var colDiv = document.createElement("div");
    colDiv.className = "col"; // Ajoutez la classe col

    // Créer un nouvel élément select pour les stations
    var select = document.createElement("select");
    select.className = "form-control";
    select.name = "arret";

    // Ajouter les options de station
    <% for(StationDTO s:stations){ %>
      var option = document.createElement("option");
      option.value = "<%= s.getNom() %>";
      option.text = "<%= s.getAdresse() %>";
      select.appendChild(option);
    <% } %>

    // Ajouter le select à la colonne
    colDiv.appendChild(select);

    // Ajouter la colonne à la rangée
    rowDiv.appendChild(colDiv);

    // Ajouter la rangée au groupe de stations
    stationGroup.appendChild(rowDiv);

    // Trouver le conteneur où ajouter le groupe de stations
    var container = document.getElementById("statContainer");

    // Ajouter le groupe de stations au conteneur
    container.appendChild(stationGroup);
}
</script>


</body>
</html>
