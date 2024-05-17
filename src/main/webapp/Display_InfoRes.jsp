<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page  import="project.sysResTicketbw.bo.Voyage,java.util.Collection" %>
<%@ page import="java.util.*" %>
<%@ page import="project.sysResTicketbw.dto.*" %>
<%@ page import="project.sysResTicketbw.service.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultats de la recherche</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Bradley+Hand&display=swap" rel="stylesheet">
    <%@ include file="resources/css.jsp" %>
    <style>
        body {
            font-family: Bradley Hand, cursive;
            background-color: #f0f0f0;
        }

        .navbar {
            background-color: #090C9B !important;
        }

        .card-header {
            background-color: #FFFFFF;
            color: #000000;
            border-radius: 10px;
            margin-top: 20px;
            padding: 20px;
        }

        .form-control {
            border-radius: 5px;
            background-color: #FFFFFF;
        }

        .btn-primary {
            background-color: #FF0000;
            border-color: #FF0000;
            border-radius: 5px;
        }

        .btn-primary:hover {
            background-color: #FFFFFF;
            border-color: #FF0000;
            color: #FF0000;
        }

        .btn-success {
            background-color: #FF0000;
            border-color: #FF0000;
            border-radius: 5px;
        }

        .btn-success:hover {
            background-color: #FFFFFF;
            border-color: #FF0000;
            color: #FF0000;
        }

        .alert-success {
            background-color: #32CD32;
            border-color: #32CD32;
            color: #FFFFFF;
        }

        .alert-danger {
            background-color: #B22222;
            border-color: #B22222;
            color: #FFFFFF;
        }

        .center-div {
		    margin: 0 auto; /* Centrer horizontalement */
		    width: 20%; /* Largeur du formulaire */
		    height:auto;
		    border-radius: 15px;
		    margin-top: 15px;
}
         .container{
           width:90%;
         }
        
    </style>
</head>
<body>
<%@ include file="resources/navbaradmin.jsp" %>

<div class="container mt-4">

    <div class="card">
        <div class="card-header">
            <h5 class="card-title">Informations de réservation</h5>
  <!-- <form action="AfficherInfoVoy" method="post">
    <input type="hidden" name="action" value="Afficher">
    <button type="submit">Afficher les voyages</button>
</form>
 -->
           
        </div>
        <div class="card-body">
            <%-- Récupérer la liste des bus depuis l'attribut de la requête --%>
            <%// Collection<Voyage> busResults = (Collection<Voyage>) request.getAttribute("Voyages"); %>
            <%   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); 
            Collection<VoyageDTO> busResults = (Collection<VoyageDTO>) new VoyageService().retrieve();%>
			<%  if(busResults == null) System.out.println("]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");%>
            <%-- Vérifier si la liste des bus n'est pas nulle et n'est pas vide --%>
            <% if (busResults != null && !busResults.isEmpty()) { 
	            
	            %>
            

                <table class="table">
                    <thead>
                        <tr>
                            <th>Voyage ID</th>
                            <th>Numéro du Bus</th>
                            <th>Nombre de places limite</th>
                            <th> Conducteur</th>
                            <th> Station Départ</th>
                            <th> Stations Arrêt</th>
                            <th> Station Arrivé</th>
                            <th> Réservations</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- Itérer à travers la liste des bus et afficher chaque bus dans une ligne du tableau --%>
                        <% for (VoyageDTO res : busResults) { %>
                            <tr>
                               <td><%= res.getIdVoyage()%></td>
                               
                               <td><%= res.getBus().getNumBus() %></td>
                               
                               <td><%= res.getBus().getNbPlacesLimite() %></td>
                               
                               <td><%= res.getBus().getConducteur().getFullName() %></td>
                               
                               <td> <%= res.getDepart().getAdresse() %> </td>
                               <td>
								    <select class="form-control">
								        <% for(ArretDTO arr: res.getArrets()) { %>
								            <option><%= arr.getArrêt().getAdresse() %></option>
								        <% } %>
								    </select>
								</td>

                               <td> <%= res.getArrive().getAdresse() %></td>
                               <td>
                               <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal<%=res.getIdVoyage()%>">
								  Afficher les réservations
								</button>
								                               <!-- Modal -->
								<div class="modal" id="myModal<%=res.getIdVoyage()%>">
								  <div class="modal-dialog modal-lg">
								    <div class="modal-content">
								
								      <!-- Modal Header -->
								      <div class="modal-header">
								        <h4 class="modal-title">Listes des réservations dans le bus <%= res.getBus().getNumBus() %></h4>
								        <button type="button" class="close" data-dismiss="modal">&times;</button>
								      </div>
								
								      <!-- Modal Body -->
								      <div class="modal-body">
								        <div class="container">
								          <!-- First Row: Labels -->
								          <div class="row">
								            <div class="col">
								              <div class="form-group">
								                <label for="passengerName">Nom et prénom du passager :</label>
								              </div>
								            </div>
								            <div class="col">
								              <div class="form-group">
								                <label for="reservationDate">Date réservation :</label>
								              </div>
								            </div>
								            <div class="col">
								              <div class="form-group">
								                <label for="departure">Départ :</label>
								              </div>
								            </div>
								            <div class="col">
								              <div class="form-group">
								                <label for="destination">Destination :</label>
								              </div>
								            </div>
								          </div>
								          <%
								          for(ReservationDTO r: res.getReservations()) { %>
								          <div class="row">
								            <div class="col">
								              <div class="form-group">
								                <input type="text" class="form-control" id="passengerName" value="<%= r.getPassager().getNom()+" "+r.getPassager().getPrenom() %>">
								              </div>
								            </div>
								            <div class="col">
								              <div class="form-group">
								                <input type="text" class="form-control" id="reservationDate" value="<%= r.getDateReserv().format(formatter) %>">
								              </div>
								            </div>
								            <div class="col">
								              <div class="form-group">
								                <input type="text" class="form-control" id="departure" value="<%= r.getFrom().getAdresse()%>">
								              </div>
								            </div>
								            <div class="col">
								              <div class="form-group">
								                <input type="text" class="form-control" id="destination" value="<%= r.getTo().getAdresse()%>">
								              </div>
								            </div>
								          </div>
								          <%} %>
								  
								
								      <!-- Modal Footer -->
								      <div class="modal-footer">
								        <button type="button" class="btn btn-secondary" data-dismiss="modal">Fermer</button>
								      </div>
								
								    </div>
								  </div>
								</div>
							</td>
							

                            </tr>
                        <% } %>
                    </tbody>
                </table>

            <% } else { %>

                <p>Aucune reservation n est disponible pour la recherche.</p>

            <% } %>
        </div>
    </div>
</div>





</body>
</html>
