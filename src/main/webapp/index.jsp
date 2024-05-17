<%@ page import="project.sysResTicketbw.dto.*" %>
<%@ page import="java.util.Collection" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% if(request.getAttribute("plusProche") == null) 
	{
%>
<script>
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var latitude = position.coords.latitude;
            var longitude = position.coords.longitude;
            window.location.href = 'Bus_Dis?longitude=' + longitude + '&latitude=' + latitude;
        }, function(error) {
            switch (error.code) {
                case error.PERMISSION_DENIED:
                    alert("Utilisateur a refusé d'accepter d'accéder à sa localisation.");
                    break;
                case error.POSITION_UNAVAILABLE:
                    alert("Impossible d'avoir les informations de localisation");
                    break;
                case error.TIMEOUT:
                    alert("Temps dépassé."); 
                    break;
                case error.UNKNOWN_ERROR:
                    alert("Erreur.");
                    break;
            }
        });
    } else {
        alert("Geolocation est non supporté par le navigateur !");
    }
</script>
<% 
	}
else
{
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bus Informations</title>
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

<%@ include file="resources/navbar.jsp" %>

<div class="container center-div mt-4">
<div class="container">
  <div class="container mt-4">
		<% String erreur = (String) session.getAttribute("erreur"); 
			String success= (String) session.getAttribute("success"); 
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
        <%
        	StationDTO plusproche = (StationDTO) request.getAttribute("plusProche");
        	Collection<StationDTO> stations = (Collection<StationDTO>) request.getAttribute("stations");
        
        %>
    <div class="card">
        <div class="card-title text-center">
            <h5 class="card-title text-black">Réserver votre ticket :</h5>
        </div>
        <div class="card-body">
            <form action="Bus_Dis" method="post" class="form-horizontal">
                <div class="form-group row">
                    <label for="departProche" class="col-sm-3 col-form-label">Station Départ </br>(le plus proche) :</label>
                    <div class="col-sm-9">
                        <select class="form-control" id="departProche" name="departProche" required>
                            <option value="<%= plusproche.getNom()%>"> <%= plusproche.getAdresse() %> </option>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="depart" class="col-sm-3 col-form-label">Choisir une autre station :</label>
                    <div class="col-sm-9">
                        <select class="form-control" id="depart" name="depart">
                            <option value="">---- Sélectionner une station ----</option>
                            <% for(StationDTO s:stations){ %>
                            <option value="<%= s.getNom()%>"> <%= s.getAdresse() %> </option>
                            <%} %>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="destination" class="col-sm-3 col-form-label">Station Destination :</label>
                    <div class="col-sm-9">
                        <select class="form-control" id="destination" name="destination" required>
                            <% for(StationDTO s:stations){ %>
                            <option value="<%= s.getNom()%>"> <%= s.getAdresse() %> </option>
                            <%} %>
                        </select>
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-3"> <!-- Offset pour aligner la bordure avec les autres champs -->
                    </div>
                    <div class="col-sm-9">
                        <button type="submit" class="btn btn-success" name="action" value="afficher">Afficher les bus disponibles </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<br>
</div>
</div>

<% 	String qrcode = (String) session.getAttribute("qrcode");
%>
<% 
	if(qrcode != null){
	%>
	<div id="myModal" class="modal fade">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Votre ticket pour monter en bus :</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <div class="form-group text-center">
	                <img id="image" src="qrcode.png" class="img-fluid" alt="QR code" width="250px" height="250px"/>
	            </div>
            </div>
        </div>
    </div>
	</div>
<%
session.setAttribute("qrcode", null);
	} %>

<script>
    $(document).ready(function(){
        $("#myModal").modal('show');
    });
</script>

</body>
</html>
<%}%>