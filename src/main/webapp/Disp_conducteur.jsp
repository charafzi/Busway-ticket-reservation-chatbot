<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
</head>
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
        width: 100%;
        }
    </style>
<body>
<%@ include file="resources/navbaradmin.jsp" %>

	<div class="container mt-4">
<% 
   	String Message = (String) session.getAttribute("MessageSupp");
    String MessageA = (String) session.getAttribute("MessageModifReussite");
%>

<% if( Message != null && !Message.isEmpty()){%>
 <div class="alert alert-danger" role="alert">
        <%= Message %>
    </div>
    <% } %>
 <% if( MessageA != null && !MessageA.isEmpty()){%>
 <div class="alert alert-success" role="alert">
        <%= MessageA %>
    </div>
    <% } %>   
    <div class="card">
        <div class="card-header">
            <h5 class="card-title">Liste des conducteurs </h5>
        </div>
        <form action="saisieConducteurInfo.jsp" method="get">
        			<!--   <input type="hidden" name="action" value="Ajouter"> -->
				    <button class="btn btn-primary" type="submit" style="width: 30%">Insérer un nouveau conducteur</button>
        </form>
        <div class="card-body">
        <!--<form action="Info_Conducteur" method="post">
				    <input type="hidden" name="action" value="Afficher">
				    <button type="submit">Afficher les conducteurs</button>
				</form>  -->
            <%-- Récupérer la liste des bus depuis l'attribut de la requête --%>
            <% 
          
            Collection<ConducteurDTO> lesconducteurs = CatalogueConducteur.getInstance().getConducteurs(); %>

           
            	<% if (lesconducteurs != null && !lesconducteurs.isEmpty()) { 

            	%>
				
                <table class="table">
                    <thead>
                        <tr>
                        	<th>Matricule</th>
                            <th>Nom Conducteur</th>
                            <th>Prénom Conducteur</th>
                            <th>Liste des bus</th>
                            <th align="center">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- Itérer à travers la liste des bus et afficher chaque bus dans une ligne du tableau --%>
                        <% for (ConducteurDTO cond : lesconducteurs) { %>
                            <tr>
                            	<td><%= cond.getMatricule() %></td>
                                <td><%= cond.getNom() %></td>
                                <td><%= cond.getPrenom() %></td>
                               	<td>
								    <select name="listeBus" class="form-control">
								        <% for (BusDTO bus : cond.getBusDTO()) { %>
								            <option value="<%= bus.getNumBus() %>"><%= bus.getNumBus() %></option>
								        <% } %>
								    </select>
								</td>  
								<td>
									<form action="Info_Conducteur" method="post">
										<input type="hidden" name="cond_modif" value="<%= cond.getFullName() %>">
										<button type="submit" name="action" value="Modifier" class="btn btn-primary">Modifier</button>
									</form>
									
									<form action="Info_Conducteur" method="post">
										<input type="hidden" name="cond_supp" value="<%= cond.getFullName() %>">
										<button type="submit" name="action" value="Supprimer" class="btn btn-danger">Supprimer</button>
									</form>
									
								</td>                
                            </tr>
                        <% } %>
                    </tbody>
                </table>

            <% } else { %>

                <p>Liste des conducteurs est vide.</p>

            <% } %>
        </div>
    </div>
</div>
	

	





</body>
</html>