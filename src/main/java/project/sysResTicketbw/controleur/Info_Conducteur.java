package project.sysResTicketbw.controleur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import project.sysResTicketbw.dto.BusDTO;
import project.sysResTicketbw.dto.ConducteurDTO;
import project.sysResTicketbw.dto.VoyageDTO;
import project.sysResTicketbw.exception.*;
import project.sysResTicketbw.service.CatalogueBus;
import project.sysResTicketbw.service.CatalogueConducteur;
import project.sysResTicketbw.service.ConducteurService;

/**
 * Servlet implementation class Info_Conducteur
 */

public class Info_Conducteur extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @throws IOException 
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public Info_Conducteur() throws ServletException, IOException {
        super();
   
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		HttpSession session = request.getSession(true);
		String action = request.getParameter("action");
		RequestDispatcher dispatcher;
		
	    if(action!=null)
	    {

	    	switch(action)
	    	{
	    		case "Ajouter":
	    			ConducteurDTO cdto = new ConducteurDTO();
	    			String nom = request.getParameter("nom");
	    	        String prenom = request.getParameter("prenom");
	    	        String matr = request.getParameter("matr");
	    	        String[] buses = request.getParameterValues("bus");
	    	        
	    	        cdto.setNom(nom);
	    	        cdto.setPrenom(prenom);
	    	        cdto.setMatricule(matr);
	    	        
	    	        HashSet<Integer> busnums = new HashSet<Integer>();
	    	        
	    	        if (buses != null) {
	    	            System.out.println("Bus dynamiques :");
	    	            for (String bus : buses) {
	    	                busnums.add(Integer.parseInt(bus));
	    	            }
	    	        }

	    	        List<BusDTO> lesbus = new ArrayList<BusDTO>(); 

	    	        System.out.println("Nom du conducteur : " + nom);
	    	        System.out.println("Prénom du conducteur : " + prenom);
	    	        
	    	        
	    	       try
	    	        {
	    	        	BusDTO b;
	    	        	
		    	        List<Integer> numDesBus = new ArrayList<Integer>();
		    	        if(buses != null) {
		    	        	for(Integer num:busnums) {
		    	        		b = CatalogueBus.getInstance().findBusByNum(num.intValue());
		    	        		b.setConducteur(cdto);
		    	        		cdto.ajouterBus(b);
		    	        		lesbus.add(b);
		    	        		//numDesBus.add(Integer.parseInt(buses[i]));
		    	        	}
		    	        }
		    	        
		    	        if(new ConducteurService().save(cdto))
		    	        {
		    	        	session.setAttribute("MessageZ", "Conducteur  bien ajouté !");
		    	        	CatalogueConducteur.getInstance().getConducteurs().add(cdto);
		    	        }
		    	        else
		    	        	 session.setAttribute("Message", "Erreur lors de l'enregistrement !");
	    	        }
	    	        catch(NumberFormatException e) {
	    	        	//e.printStackTrace();
	    	        	 session.setAttribute("Message", "Les numéros des bus sont des entiers !");
	    	        	 
	    	        }
	    	        catch(org.neo4j.driver.exceptions.ClientException e1)
	    	        {
	    	        	session.setAttribute("Message", "Conducteur avec matricule "+matr+" déjà existe !");
	    	        }
	    	       
	    	        dispatcher = request.getRequestDispatcher("/Disp_conducteur.jsp");
			        dispatcher.forward(request, response);
	    			break;
	    		case "Afficher":
	    			 		System.out.println("-------------------------");
	    			 		Collection<ConducteurDTO> conducteurs = CatalogueConducteur.getInstance().getConducteurs();
	    			    request.setAttribute("Conducteurs", conducteurs);
	    			     dispatcher = request.getRequestDispatcher("/Disp_conducteur.jsp");
	    		        dispatcher.forward(request, response);
	    			break;
	    			
	    		case "Supprimer":
	    			String fullnameS = request.getParameter("cond_supp");
	    			ConducteurDTO cond = CatalogueConducteur.getInstance().findByNom(fullnameS);
	    			Collection<BusDTO> lesbus_conduit = cond.getBusDTO();
	    			for(BusDTO b:lesbus_conduit) {
	    				for(VoyageDTO vy:b.getVoyages()) {
	    					vy.setDisponible(false);
	    				}
	    			}
	    			CatalogueConducteur.getInstance().SupprimerConducteur(cond);
	    			if(new ConducteurService().delete(cond))
	    			{
	    				CatalogueConducteur.getInstance().getConducteurs().remove(cond);
	    				session.setAttribute("MessageModifReussite", "Suppression bien effectuée !");
	    			}
		    		else
		    			session.setAttribute("MessageModif", "Erreur lors de la suppression !");
	    			dispatcher = request.getRequestDispatcher("/Disp_conducteur.jsp");
	    		    dispatcher.forward(request, response);
	    		        
	    			
	    			
	    			break;
	    		case "Modifier":
	    			System.out.println("***********************************");
	    			String fullname = request.getParameter("cond_modif");
	    			
	    					ConducteurDTO conducteurAModifier = CatalogueConducteur.getInstance().findByNom(fullname);
	    					request.setAttribute("conducteurAModifier", conducteurAModifier);
	    					request.getRequestDispatcher("Modifier_Conducteur.jsp").forward(request, response);
	    			break;
	    		case "ModificationT":
	    			String nom_MT = request.getParameter("nom");
	    			String prenom_MT = request.getParameter("prenom");

	    		
	    			
	    			Collection<BusDTO> busModif = new ArrayList<BusDTO>();
	    		    String[] nouveauxBus1 = request.getParameterValues("bus");
	    		    
	    		    
	    		    if(nouveauxBus1 != null && nouveauxBus1.length != 0)
	    		    {
	    		    	for(int i=0;i<nouveauxBus1.length;i++)
		    		    {
		    		    	  try { 
		    		    	System.out.println(nouveauxBus1[i]);
		    		    	BusDTO bus = CatalogueBus.getInstance().findBusByNum(Integer.parseInt(nouveauxBus1[i]));
		    		    	if(bus==null) throw new BusNotFound();
		    		    	
		    		    	busModif.add(bus);
		    		    	  }
		      		    	catch(BusNotFound e)
		      		    	{
		      		    		e.printStackTrace();
		      		    		session.setAttribute("MessageModif", e.toString());
		      		    		dispatcher = request.getRequestDispatcher("Modifier_Conducteur.jsp");
			    		        dispatcher.forward(request, response);
		      		    	}
		      		    	catch(NumberFormatException e1)
		      		    	{
		      		    		e1.printStackTrace();
		      		    		session.setAttribute("MessageModif", "Les numéros des bus sont des entiers !");
		      		    		dispatcher = request.getRequestDispatcher("Modifier_Conducteur.jsp");
			    		        dispatcher.forward(request, response);
		      		    	}
		    		    
		    		    }
	    		    }

			    	ConducteurDTO cd = CatalogueConducteur.getInstance().findByNom(request.getParameter("lastname"));
	    		    
	    		    cd.setNom(nom_MT);
	    		    cd.setPrenom(prenom_MT);
	    		    cd.setBusDTO(busModif);
	    		    if(new ConducteurService().update(cd))
	    		    	session.setAttribute("MessageModifReussite", "Modification  bien enregistrée !");
	    		    else
	    		    	session.setAttribute("MessageModif", "Erreur lors de la mise à jour !");
	    		    
	    		    dispatcher = request.getRequestDispatcher("/Disp_conducteur.jsp");
    		        dispatcher.forward(request, response);
	    			
	    			break;
	    	
	    	
	    	
	    	}
	    	
			
	    }		
	}

}
