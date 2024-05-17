<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="project.sysResTicketbw.dto.*" %>
<%@ page import="project.sysResTicketbw.service.*" %>
<%@ page import="project.sysResTicketbw.controleur.*" %>  

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
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
        width: 60%;
        }
    </style>
</head>
<body>
<%@ include file="resources/navbaradmin.jsp" %>
	<div class="container mt-4">
        <% 
   	String MessageR = (String) session.getAttribute("MessageR");
    String MessageF = (String) session.getAttribute("MessageF");
%>

<% if( MessageR != null && !MessageR.isEmpty()){%>
 <div class="alert alert-success" role="alert">
        <%= MessageR %>
    </div>
    <% } %>
 <% if( MessageF != null && !MessageF.isEmpty()){%>
 <div class="alert alert-danger" role="alert">
        <%= MessageF %>
    </div>
    <% } %>
 


    <div class="card">
        <div class="card-header">
            <h5 class="card-title">Liste des bus :</h5>
        </div>
        <!--  
        <form action="AllBuss" method="post">
        			 <input type="hidden" name="action" value="Afficher">
				    <button type="submit">Afficher les Bus</button>
        </form>
        -->
        <form action="AjouterBus.jsp" method="post">
        			<!--   <input type="hidden" name="action" value="Ajouter"> -->
				    <button class="btn btn-primary" type="submit" style="width: 20%">Insérer un nouveau Bus </button>
        </form>
        
        <div class="card-body">
            <%-- Récupérer la liste des bus depuis l'attribut de la requête --%>
            <% 
            	new VoyageService().retrieve();
           		CatalogueConducteur.getInstance().getConducteurs();
           
                List<BusDTO> les_bus = CatalogueBus.getInstance().getBus();
            %>
            
            
            <% if (les_bus != null && !les_bus.isEmpty()) { %>
            <form action="AllBuss" method="post">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Numéro bus</th>
                            <th>Place Limite</th>
                            <th>Place vide</th>
                            <th>Conducteur</th>
                            <th>Voyages</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for(BusDTO bus : les_bus) { %>
                            <tr>
                            	
                                <td><%= bus.getNumBus() %></td>
                                <td><%= bus.getNbPlacesLimite() %></td>
                                <td><%= bus.getNbPlacesVides() %></td>
                                <% if(bus.getConducteur()!=null){ %>
                                <td><%= bus.getConducteur().getFullName() %></td>
                                <%}else{ %>
                                <td>Aucun conducteur n'est affecte</td>
                                <%} %>
                                <%if(bus.getVoyages()!=null){ %>
                                <td>
                                    <select class="form-control">
                                        <% for(VoyageDTO vy : bus.getVoyages()) { %>
                                            <option><%= vy.getIdVoyage() %></option>
                                        <% } %>
                                    </select>
                                </td>
                                <%}else{ %>
                                <td> Aucun Voyage n'est affecté</td>
                                <%} %>
                                <td>
                                	
                                	<form action="AllBuss" method="post">
                                		<input type="hidden" name="numBus" value="<%= bus.getNumBus() %>">
                                		<button type="submit" class="btn btn-primary" name="action" value="Modifier">Modifier</button>
                                	</form>
                                    <form action="AllBuss" method="post">
                                    	<input type="hidden" name="numBusSupp" value="<%= bus.getNumBus() %>">
                                   		 <button type="submit" class="btn btn-danger" name="action" value="Supprimer">Supprimer</button> 
                                    </form>
                                </td>
                            </tr>
                        <% } %>   
                    </tbody>     
                </table>
                </form>
            <% } else { %>
                <p>Aucun bus disponible</p>
            <% } %>   
        </div>
    </div>
    </div>
</body>
</html>
