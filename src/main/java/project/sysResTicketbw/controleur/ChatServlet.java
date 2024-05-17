package project.sysResTicketbw.controleur;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicBooleans;

import project.sysResTicketbw.service.CatalogueStation;

public class ChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final boolean TRACE_MODE=false;
    private Bot bot;
    private Chat chatSession;

    public void init() {
        try {
            String resourcePath = getServletContext().getRealPath("/")+"resources";
            MagicBooleans.trace_mode = TRACE_MODE;
            bot = new Bot("super", resourcePath);
            chatSession = new Chat(bot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String user = request.getParameter("userInput");
        String bot = "";
        if(user != null && !user.isEmpty()) {
            bot = processInput(user);
        }
        
        HttpSession session = (HttpSession) request.getSession(true);
        //pour stocker l'historique du chat
        List<String> chatHistory = (List<String>) session.getAttribute("chatHistory");
        if (chatHistory == null) {
            chatHistory = new ArrayList<>();
        }
        
        chatHistory.add("You: " + user);
        chatHistory.add("Bot: " + bot);
        // Update chat history in session
        session.setAttribute("chatHistory", chatHistory);

        request.getRequestDispatcher("ChatUI.jsp").forward(request, response);
    }

    private String processInput(String userInput) {
        String response = "";
        try {
            response = chatSession.multisentenceRespond(userInput);
            String[] action = response.split(",");
            if (action.length > 1) {
                if (action[0].equals("BUSDISPO")) {
                    String dep = CatalogueStation.getInstance().findStationByAdresse(action[1]).getNom();
                    String arr = CatalogueStation.getInstance().findStationByAdresse(action[2]).getNom();
                    String buses = Controleur.voyagesDisponibleString(dep, arr, LocalTime.now());
                    
                    response = "Les numeros des bus diponibles sont " + buses;
                } else if (action[0].equals("RESERVER")) {
                    String[] nom = action[1].split(" ");
                    String dep = CatalogueStation.getInstance().findStationByAdresse(action[2]).getNom();
                    String arr = CatalogueStation.getInstance().findStationByAdresse(action[3]).getNom();
                    boolean res = new Controleur().reservation(Integer.parseInt(action[4].strip()), nom[0], nom[1], dep, arr, LocalTime.of(6, 0));
                    response = res ? "Reservation bien effectué !" : "Un erreur est survenue s'il vous plaît répéter les étapes précedentes à nouveau";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    private static String getpath()
	{
		File currd=new File(".");
		String path=currd.getAbsolutePath();
		String resourcepath=File.separator +"src" + File.separator +"main" +File.separator +"resources";
		return resourcepath;
	}
}
