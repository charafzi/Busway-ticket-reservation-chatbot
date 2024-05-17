package project.sysResTicketbw.controleur;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.batik.util.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpSession;
import project.sysResTicketbw.dto.*;
import project.sysResTicketbw.service.BusService;
import project.sysResTicketbw.service.CatalogueBus;
import project.sysResTicketbw.service.CatalogueConducteur;
import project.sysResTicketbw.exception.*;
import project.sysResTicketbw.exception.metier.*;

/**
 * Servlet implementation class AllBuss
 */
@WebServlet("/AllBuss")
public class AllBuss extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Controleur c = new Controleur();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    public AllBuss(){

	
	  
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// TODO Auto-generated method stub
		 	HttpSession session = request.getSession(true);
			String action = request.getParameter("action");
			RequestDispatcher dispatcher ;
			
			Collection<BusDTO> lesbus = CatalogueBus.getInstance().getBus();
			request.setAttribute("lesbus", lesbus);
			request.getRequestDispatcher("/Disp_AllBus.jsp").forward(request, response);
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 HttpSession session = request.getSession(true);
			String action = request.getParameter("action");
			System.out.println(action);
			RequestDispatcher dispatcher = null ;
			
			Collection<BusDTO> lesbus = CatalogueBus.getInstance().getBus();
			request.setAttribute("lesbus", lesbus);
			Collection<ConducteurDTO> lesconducteurs = CatalogueConducteur.getInstance().getConducteurs();
			request.setAttribute("les_conducteurs",lesconducteurs);
			
			if(action != null)
			{
				switch(action)
				{
					case "Afficher":
						request.setAttribute("lesbus",lesbus);
						dispatcher = request.getRequestDispatcher("/Disp_AllBus.jsp");
	    		        dispatcher.forward(request, response);
						break;
					case "Ajouter":
						 	String numBus = request.getParameter("numBus");
					        String nbrPlaceLimite = request.getParameter("nbrplacelimite");
					        String conducteurName = request.getParameter("conducteurName");
					       // String[] listeVoyage = request.getParameterValues("voyage");
					        
					        /*HashSet<Integer> voynums = new HashSet<Integer>();
			    	        
			    	        if (listeVoyage != null) {
			    	            System.out.println("Bus dynamiques :");
			    	            for (String v : listeVoyage) {
			    	            	voynums.add(Integer.parseInt(v));
			    	            }
			    	        }
			    	        */
			    	        
					        
			    	        
					        
					        
					       
					        
					        

					        System.out.println("Numero de bus: " + numBus);
					        System.out.println("Nombre de place limite: " + nbrPlaceLimite);
					        System.out.println("Nom du conducteur: " + conducteurName);
					       // System.out.println("Liste de voyages: " + listeVoyage);

					       try
					       {
					    	   ConducteurDTO conducteur_Ajoute = CatalogueConducteur.getInstance().findByNom(conducteurName);
						        BusDTO bus = new BusDTO();
						        bus.setConducteur(conducteur_Ajoute);
						        bus.setNumBus(Integer.parseInt(numBus));
						        bus.setNbPlacesLimite(Integer.parseInt(nbrPlaceLimite));
						        bus.setNbPlacesVides(Integer.parseInt(nbrPlaceLimite));
						        
						        /*Collection<VoyageDTO> les_voyages = c.getVoyages();
						        List<VoyageDTO> voyages = new ArrayList<>();

						        for(Integer v:voynums)
						        {
						        	for(VoyageDTO vdto:les_voyages)
						        	{
						        		if(vdto.getIdVoyage()==v)
						        		{
						        			voyages.add(vdto);
						        			break;
						        		}
						        	}
						        }
						        bus.setVoyages(voyages);*/
						        
					    	   if(new BusService().save(bus))
					    	   {
					    		   CatalogueBus.getInstance().getBus().add(bus);
					    		   session.setAttribute("MessageR", "Insertion est bien effectué !");
					    	   }
						        else
							        session.setAttribute("MessageF", "Erreur lors de la création !");
					    	   
					       }
					       catch(org.neo4j.driver.exceptions.ClientException e1)
			    	        {
			    	        	session.setAttribute("Message", "Bus avec le numéro "+numBus+" déjà existe !");
			    	        }
					        

					     
							request.setAttribute("lesbus", lesbus); 
					        dispatcher = request.getRequestDispatcher("/Disp_AllBus.jsp");
		    		        dispatcher.forward(request, response);
					        
						break;
					case "Modifier":
						String numbus3 = request.getParameter("numBus");
						BusDTO  busAmodifier = CatalogueBus.getInstance().findBusByNum(Integer.parseInt(numbus3));
						request.setAttribute("busAmodifier",busAmodifier );
						request.setAttribute("les_conducteurs",lesconducteurs);
						dispatcher = request.getRequestDispatcher("/Modifier_Bus.jsp");
	    		        dispatcher.forward(request, response);
						break;
						
					case "ModificationT":
						
						String numB1 = request.getParameter("NumBus");
						String NBplcLimite1 = request.getParameter("NBplcLimite");
						//String NBplcVide1 =  request.getParameter("NBplcVide");
						//String NomConducteur1 = request.getParameter("conducteurName");
						//String[] listevoyage = request.getParameterValues("bus");
						//int[] tabVoyage = new int[0];

						System.out.println("Numero de Bus: " + numB1);
						System.out.println("Nombre place limie de Bus: " + NBplcLimite1);
						//System.out.println("Numero place vide: " + NBplcVide1);
						//System.out.println("Numero de Conducteur: " + NomConducteur1);

						int numbusmodifT=0;
						int nbrplacelimitemodifT=0;
						int nbrplacevidemodifT=0;
						BusDTO b = CatalogueBus.getInstance().findBusByNum(Integer.parseInt(numB1));
						b.setNbPlacesLimite(Integer.parseInt(NBplcLimite1));
						
						if(new BusService().update(b))
							session.setAttribute("MessageR", "Bus bien modifié !");
						else
							session.setAttribute("MessageF", "Erreur lors de la modification !");
							

						/*try {
						    numbusmodifT = Integer.parseInt(numB1);
						    nbrplacelimitemodifT = Integer.parseInt(NBplcLimite1);
						    nbrplacevidemodifT = Integer.parseInt(NBplcVide1);
						    if(listevoyage != null ) {
						        tabVoyage = new int[listevoyage.length];
						        for(int i=0; i<listevoyage.length; i++) {
						            tabVoyage[i] = Integer.parseInt(listevoyage[i]);    
						        }   
						    }
						    
						    conducteurModif = CatalogueConducteur.getInstance().findByNom(NomConducteur1);

						    if(nbrplacevidemodifT > nbrplacelimitemodifT) {
						        throw new ContrainteNbrPlace();
						    }
						    
						    // If everything is valid, proceed with modifying the bus
						    BusDTO lebusModif = CatalogueBus.getInstance().findBusByNum(numbusmodifT);  
						    Collection<VoyageDTO> LES_voyage = c.getVoyages();
						    Collection<VoyageDTO> VOyages = new ArrayList<>();

						   
						    
						    ConducteurDTO conducteurModifierT = CatalogueConducteur.getInstance().findByNom(NomConducteur1);
						    lebusModif.setNumBus(numbusmodifT);
						    lebusModif.setNbPlacesVides(nbrplacevidemodifT);
						    lebusModif.setConducteur(conducteurModifierT);
						    lebusModif.setNbPlacesLimite(nbrplacelimitemodifT);
						    if(listevoyage != null) {
						        lebusModif.setVoyages(VOyages);
						    }
						    
						    // Set an attribute to indicate successful modification
						    request.setAttribute("MessageInsertionReussite", "Modification du bus et reussite");
						} catch(NumberFormatException e) {
						    // Handle NumberFormatException
						    request.setAttribute("MessageFormat", e.toString());
						} catch(ContrainteNbrPlace e2) {
						    // Handle ContrainteNbrPlace exception
						    request.setAttribute("MessageFormat2", e2.toString());
						}*/

						// Forward the request
						 dispatcher = request.getRequestDispatcher("/Disp_AllBus.jsp");
						dispatcher.forward(request, response);

						break;
					case "Supprimer":
						System.out.println("***************************");
		    		      String numbBus = request.getParameter("numBusSupp");
		    		      int numberBus = Integer.parseInt(numbBus);
		    		      System.out.println("le bus a supprimé est : "+numberBus);
		    		      BusDTO lebus = CatalogueBus.getInstance().findBusByNum(numberBus);
		    		      for(VoyageDTO vy:lebus.getVoyages())
		    		    	  vy.setDisponible(false);
		    		      // suppression du catalogue
		    		       
		    		      

		    		       if(new BusService().delete(lebus))
		    		       {
		    		    	   CatalogueBus.getInstance().getBus().remove(lebus);
		    		    	   session.setAttribute("MessageR", "Bus bien supprimé !");
		    		       }
		    		    	   
							else
								session.setAttribute("MessageF", "Erreur lors de la suppression!");
		    		    	   
		    		       dispatcher = request.getRequestDispatcher("/Disp_AllBus.jsp");
		    		       dispatcher.forward(request, response);   
		    		        
						break;
				}
				

			}
			
					
		
		
	}
	
	public VoyageDTO chercherVoyage(int id,Collection<VoyageDTO> voyages)
	{
		for(VoyageDTO vy:voyages) {
			if(vy.getIdVoyage()==id)
				return vy;
		}
		return null;
	}

}
