package project.sysResTicketbw.controleur;

import java.io.IOException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import project.sysResTicketbw.dto.VoyageDTO;
import project.sysResTicketbw.exception.*;

/**
 * Servlet implementation class Info_Bus
 */

public class InfoBus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Controleur c= new Controleur();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfoBus() {
        super();
        // TODO Auto-generated constructor stub
    }
    @Override
	public void init() throws ServletException {
    	
    	
    	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Récupérer la liste des voyages (à partir de votre service ou d'une autre source)
		 	Collection<VoyageDTO> lesvoyages = c.getVoyages();
	        request.getSession().setAttribute("Voyages", lesvoyages);

	        RequestDispatcher dispatcher = request.getRequestDispatcher("saisieInfoBus.jsp");
	        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		HttpSession session = request.getSession(true);
		
		
	    String action = request.getParameter("action");
	        
	    if(action != null)
	    {
	    	if ( action.equals("Ajouter")) {
		        
	        	

		        try {
		        	Map<LocalTime,String> map = new TreeMap<>();
			        
		        	String idvoyage = request.getParameter("idvoyage");
			        String depart = request.getParameter("depart");
			        String arrive =  request.getParameter("arrive");
			        String heuredepart = request.getParameter("departTime");
			       String heurearrive =  request.getParameter("arriveTime");
			        String nbrplace = request.getParameter("nbplace");
			        
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			        LocalTime heure_depart = LocalTime.parse(heuredepart, formatter);
			        LocalTime heure_arrive = LocalTime.parse(heurearrive,formatter);
			        String[] arrets = request.getParameterValues("arret");
			       // String[] heures = request.getParameterValues("heure");
			        
			        if (arrets != null /*&& heures != null*/) {
			            for (int i = 0; i < arrets.length; i++) {
			                LocalTime heureArret =  LocalTime.of(i, 0) ;/*LocalTime.parse(heures[i], formatter);*/
			                map.put(heureArret,arrets[i]);
			                System.out.println(arrets[i]);
			            }
			        }

			        // Traitement spécifique à l'ajout d'un bus
			        String idBus = request.getParameter("bus");
			        String nomConducteur = request.getParameter("idConducteur");
			        int id_v = Integer.parseInt(idvoyage);
			        int nbP=Integer.parseInt(nbrplace);
			        int n_Bus = Integer.parseInt(idBus);
			     
				if(c.installerInfoBusway(id_v,n_Bus,nomConducteur,nbP,depart,heure_depart,arrive,heure_arrive,map))
					session.setAttribute("MessageB", "Bien enregistré !");
				
				for (Map.Entry<LocalTime, String> entry : map.entrySet()) {
		            System.out.println(entry.getKey() + " : " + entry.getValue());
		        }
				System.out.println("Heure de départ : " + heure_depart);
				System.out.println("Heure d'arrivée : ");
				System.out.println("Valeur de idvoyage : " + id_v);
				System.out.println("Valeur de nbrplace : " + nbP);
				System.out.println("Valeur de idBus : " + n_Bus);
				
				
		    	}
		        catch (BusNotFound  e) 
		        {
					// TODO Auto-generated catch block
					e.printStackTrace();
					session.setAttribute("Message", "Bus not Found");
				}
		        catch(StationNotFound e1) {
		        	e1.printStackTrace();
		        	session.setAttribute("Message", "Station Not found !");
		        }
		        catch(ConducteurNotFound e2) {
		        	e2.printStackTrace();
		        	session.setAttribute("Message", "Conducteur Not found");
		        }
		        catch (DateTimeParseException e3) {
		            // Gérer l'exception de parsing ici
		            e3.printStackTrace(); 
		            session.setAttribute("Message", "Veillez entrez une date ");
		        }
		       
		        finally {
		        	doGet(request, response);
		        }
		    
			
	    	}
	    	else
	    	{/*
	    		if(action.equals("Afficher"))
	    		{
	    			System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
	    			Collection<Voyage> lesvoyages = c.getVoyages();
	    			for(Voyage vy:lesvoyages)
	    			{
	    				System.out.println("0000000000000000000");
	    			}
	    			request.setAttribute("Voyages", lesvoyages);

	                RequestDispatcher dispatcher = request.getRequestDispatcher("Display_InfoRes.jsp");
	                System.out.println("££££££££££££££££££££££££££££££££££££££££££££££££££££");
	                dispatcher.forward(request, response);
	    		}
	    		*/
	    		
	    	}
	    	

	    }
	    else
	    {
	    	Collection<VoyageDTO> lesvoyages = c.getVoyages();
			for(VoyageDTO vy:lesvoyages)
			{
				System.out.println("0000000000000000000");
			}
			request.getSession().setAttribute("Voyages", lesvoyages);

	        RequestDispatcher dispatcher = request.getRequestDispatcher("Display_InfoRes.jsp");
	        System.out.println("££££££££££££££££££££££££££££££££££££££££££££££££££££");
	        dispatcher.forward(request, response);
    		    		
	    }
  }
}
