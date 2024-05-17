<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chatbot Interface</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
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
         
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 50px;
        }
        .chat-container {
            background-color: #FFFFFF;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 20px;
            overflow-y: auto; /* Enable vertical scrolling */
            max-height: 400px; /* Set maximum height for the chat container */
        }
        .chat-message {
            margin-bottom: 20px;
        }
        .card {
            margin-bottom: 10px;
        }
        .card-body {
            padding: 10px;
        }
        .user-message {
            color: black; /* Change color for user messages */
        }
        .bot-message {
            color: #090C9B ; /* Change color for bot messages */
        }
    </style>
</head>
<%@ include file="resources/navbar.jsp" %>

<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">            
                <h1 class="text-center mb-4">BusWay Chatbot</h1>
                <div class="chat-container" id="chat-container">
                    <% 
                    String[] mes;
                    session = request.getSession();
                    List<String> chatHistory = (List<String>) session.getAttribute("chatHistory");
                    if (chatHistory != null) {
                        for (String message : chatHistory) {
                    %>
                        <div class="card">
                            <div class="card-body">
                                <%  mes = message.split(":");
                                if (mes[0].startsWith("You")) { %>
                                    <span class="user-message"><%= mes[1] %></span>
                                <% } else { 
                                if(mes[1].equalsIgnoreCase(" Reservation bien effectué !")){
                                %>
                                <script>
				                    $(document).ready(function() {
				                        $('#myModal').modal('show');
				                    });
				                </script>
                                <%} %>
                                	
                                    <span class="bot-message"><%= mes[1] %></span>
                                <% } %>
                            </div>
                        </div>
                    <% } }%>
                    <form method="post" action="ChatServlet">
                        <div class="form-group">
                            <label for="userInput">Saisir votre message :</label>
                            <input type="text" class="form-control" id="userInput" name="userInput" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Envoyer</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <div id="myModal" class="modal fade">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Votre billet pour monter en bus :</h5>
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
    <!-- Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>

<script>
var objDiv = document.getElementById("chat-container");
objDiv.scrollTop = objDiv.scrollHeight;
</script>
</html>
