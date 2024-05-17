<%@page import="project.sysResTicketbw.service.CatalogueStation"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.reflect.Array"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page  import="project.sysResTicketbw.bo.Voyage,java.util.Collection" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="project.sysResTicketbw.dto.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultats de la recherche</title>
    <%@ include file="resources/css.jsp" %>
    <link rel="stylesheet" href="resources/index.css">
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
<%@ include file="resources/navbar.jsp" %>
<body>
<%
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	String dep ="";
	String departProche = (String) session.getAttribute("departProche");
	String depart = (String)session.getAttribute("depart");
	String destination =(String) session.getAttribute("destination");
	String heure =(String) session.getAttribute("heure");
	if(departProche != null)
		dep=departProche;
	else
		dep=depart;
	
	String adressedep = (String) CatalogueStation.getInstance().findStationByName(dep).getAdresse();
	String adressearr = (String) CatalogueStation.getInstance().findStationByName(destination).getAdresse();
	
%>

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

    <div class="card" >
        <div class="card-header text-white" id="card-header" >
            <h5 class="card-title">Les bus disponibles de la station 
            <span id="outline"><%= adressedep %></span>
            vers la station <span id="outline"><%= adressearr%></span> 
            à partir de <span id="outline"><%=heure %></span>
            </h5>
        </div>
        <div class="card-body">
            <%-- Récupérer la liste des bus depuis l'attribut de la requête --%>
            <% List<VoyageDTO> busResults = (List<VoyageDTO>) session.getAttribute("voyagesDispo"); %>

                <table class="table">
                    <thead>
                        <tr>
                            <th width="15%" >Numéro du Bus</th>
                            <th width="50%"></th>
                            <th width="20%">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%-- Vérifier si la liste des bus n'est pas nulle et n'est pas vide --%>
		            <% if (busResults != null && !busResults.isEmpty()) { 
		            %>
                        <%-- Itérer à travers la liste des bus et afficher chaque bus dans une ligne du tableau --%>
                        <% for (VoyageDTO v : busResults) { %>
                            <tr>
							    <td><i class="fa-solid fa-bus"></i> <span style="font-weight: bold"><%= v.getBus().getNumBus() %></span></td>
							    <td align="left">
							    <ol class="steplist circle-big">
							    <%			
							
								ArrayList<ArretDTO> arrets = new ArrayList<>(v.getArrets());
							    String heuredep ="";
								String heureArr ="";
								String nomarret ="";
								boolean deptrouve = false;
						    	boolean arrtourve= false;
						    	int j=0;
						    	
						    	if(v.getDepart().getNom().equals(dep))
						    	{ 
								    deptrouve = true;
								    j=0;
								    heuredep = v.getHeureDepart().format(formatter);
							    } 
							    
							    if(!deptrouve)
							    {
							    	for(int i=0; i<arrets.size();i++)
							    	{
							    		ArretDTO a = arrets.get(i);
							    		if(a.getArrêt().getNom().equals(dep))
							    		{
							    			heuredep = a.getHeureArret().format(formatter);
							    			deptrouve = true;
							    			j=i+1;
							    			break;
							    			
							    		}
							    	}
							    }
							    %>
							    <li>
    								<span id="station"><%= adressedep  %></span><br /><span id="heure"><%= heuredep %></span>
								</li>
							   <%
							   for(int i=j; i<arrets.size();i++)
						    	{
						    		ArretDTO a = arrets.get(i);
						    		if(a.getArrêt().getNom().equals(destination))
						    		{
						    			heureArr = a.getHeureArret().format(formatter);
						    			nomarret = a.getArrêt().getAdresse();
						    			arrtourve = true;
						    			break;	
						    		}
						    		else
						    		{
						    	%>
						    	 <li>
    								<span id="station"><%= a.getArrêt().getAdresse() %></span><br /><span id="heure"><%= a.getHeureArret().format(formatter) %></span>
								</li>
						    			
						    	<%
						    		}
						    	}
							   
							   if(!arrtourve)
							   {
								%>
								<li>
    								<span id="station"><%= v.getArrive().getAdresse() %></span><br /><span id="heure"><%= v.getHeureArrive().format(formatter) %></span>
								</li>
								<%
							   }
							   else{%>
							  <li>
    								<span id="station"><%= nomarret %></span><br /><span id="heure"><%= heureArr %></span>
							 </li>
							   <%
							   }
							   	%>
							    </ol>
							    </td>
							    <td>
							        <form id="reservationForm<%= v.getBus().getNumBus() %>" method="post" action="Bus_Dis">
							            <div class="modal fade" id="exampleModalCenter<%= v.getBus().getNumBus() %>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
							                <div class="modal-dialog modal-dialog-centered" role="document">
							                    <div class="modal-content">
							                        <div class="modal-header">
							                            <h5 class="modal-title" id="exampleModalLongTitle">Entrer vos informations :</h5>
							                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
							                                <span aria-hidden="true">&times;</span>
							                            </button>
							                        </div>
							                        <div class="modal-body">
							                            <div class="form-group">
							                                <label for="cardNumber">Nom :</label>
							                                <input type="text" class="form-control" name="nom" placeholder="Votre nom" required="required">
							                            </div>
							                            <div class="form-group">
							                                <label for="cardExpiration">Prénom :</label>
							                                <input type="text" class="form-control" name="prenom" placeholder="Votre prénom" required="required">
							                            </div>
							                            <div class="modal-footer">
							                                <button type="reset" class="btn btn-secondary" data-dismiss="modal">Fermer</button>
							                                <button type="submit" class="btn submit-btn" id="button-res"  name="action" value="reserver_ticket">Confirmer</button>
							                            </div>
							                        </div>
							                    </div>
							                </div>
							            </div>
							            <input type="hidden" name="idbus" value="<%= v.getBus().getNumBus() %>">
							            <button class="btn btn-xs reserve-btn" id="button-res" data-toggle="modal" data-target="#exampleModalCenter<%= v.getBus().getNumBus() %>">Réserver <i class="fa-solid fa-ticket"></i></button>
							        </form>
							    </td>
							</tr>
                            
                        <% } %>

            	<% } else { %>
							<tr>
								<td class='text-center' colspan="3">Aucun bus est disponible</td>
							</tr>

           		 <% } %>
           		 </tbody>
            </table>
        </div>
    </div>
</div>


<script>
    $(document).ready(function () {
        $('.reserve-btn').click(function () {
            $('#exampleModalCenter').modal('show');
        });
    });
    
</script>

</body>
</html>
