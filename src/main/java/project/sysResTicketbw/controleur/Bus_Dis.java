package project.sysResTicketbw.controleur;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.glxn.qrgen.javase.QRCode;
import project.sysResTicketbw.dto.BusDTO;
import project.sysResTicketbw.dto.StationDTO;
import project.sysResTicketbw.dto.VoyageDTO;
import project.sysResTicketbw.service.CatalogueBus;
import project.sysResTicketbw.service.CatalogueStation;
import project.sysResTicketbw.service.VoyageService;
import project.sysResTicketbw.exception.*;
import project.sysResTicketbw.exception.metier.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class Bus_Dis
 */
public class Bus_Dis extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Collection<VoyageDTO> voyages;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Bus_Dis() {
        super();
        // TODO Auto-generated constructor stub
    }
   
	@Override
	public void init() throws ServletException {
		voyages = new ArrayList<VoyageDTO>();
		voyages = new VoyageService().retrieve();
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String latitude = request.getParameter("latitude");
		String longitude = request.getParameter("longitude");
		
		StationDTO statPlusProche = this.getStationPlusProches(Double.parseDouble(longitude),Double.parseDouble(latitude)).get(0);	
		System.out.println("plus priche :"+statPlusProche.getNom());
		Collection<StationDTO> stations = CatalogueStation.getInstance().getStations();
		request.setAttribute("stations", stations);
		request.setAttribute("plusProche",statPlusProche);
		
		RequestDispatcher disp = this.getServletContext().getRequestDispatcher("/index.jsp");
		disp.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérez les paramètres du formulaire
		
		String action = request.getParameter("action");
		System.out.println("action : "+action);
		
		if(action != null)
		{
			if(action.equals("afficher"))
			{
				System.out.println("*******************V***************");
        		for(VoyageDTO v:this.voyages)
        		{
        			System.out.println(v.getIdVoyage());
        		}
        		System.out.println("************************************");
				HttpSession session = request.getSession(true);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

				String departProche = request.getParameter("departProche");
		        String depart = request.getParameter("depart");
		        String destination = request.getParameter("destination");
		        LocalTime heure = LocalTime.now();
		        System.out.println("date "+heure.toString());
		        //LocalTime heure = LocalTime.of(6, 30);
		        System.out.println(departProche);
		        System.out.println(depart);
		        System.out.println("heure de reservation: "+heure.format(formatter));
		        session.setAttribute("heure", heure.format(formatter));
		        session.setAttribute("destination", destination);
		        

		        
		        if((departProche==null || departProche.equals("")) && depart.equals(""))
		        {
		        	
		        	session.setAttribute("erreur", "Veuillez saisir une station départ");
		        	RequestDispatcher disp = this.getServletContext().getRequestDispatcher("/index.jsp");
		        	disp.forward(request, response);
		        }
		        if(depart.equals(""))
		        {
		        	List<VoyageDTO> lesvoyagesdispo =this.voyagesDisponible(departProche, destination, heure);
		        	if(lesvoyagesdispo != null && lesvoyagesdispo.size()!=0)
		        	{
		        		System.out.println("************************************");
		        		System.out.println("ggggggggggggg");
		        		for(VoyageDTO v:lesvoyagesdispo)
		        		{
		        			System.out.println(v.getIdVoyage());
		        		}
		        		System.out.println("************************************");
		        		session.setAttribute("voyagesDispo", lesvoyagesdispo);
		        		session.setAttribute("departProche", departProche);
		        		session.setAttribute("depart", null);
		        		RequestDispatcher dispatcher = request.getRequestDispatcher("/Display_Busdis.jsp");
			            dispatcher.forward(request, response);
		        	}
		        	else
		        	{
		        		session.setAttribute("erreur", "Aucun voyage est disponible pour la ligne sélectionnée !");
		        		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
			            dispatcher.forward(request, response);		
		        	}	
		        }
		        else
		        {
		        	List<VoyageDTO> lesvoyagesdispo =this.voyagesDisponible(depart, destination, heure);
		        	if(lesvoyagesdispo != null && lesvoyagesdispo.size()!=0)
		        	{
		        		System.out.println("************************************");
		        		System.out.println("ggggggggggggg");
		        		for(VoyageDTO v:lesvoyagesdispo)
		        		{
		        			System.out.println(v.getIdVoyage());
		        		}
		        		System.out.println("************************************");
		        		session.setAttribute("voyagesDispo", lesvoyagesdispo);
		        		session.setAttribute("depart", depart);
		        		session.setAttribute("departProche", null);
		        		RequestDispatcher dispatcher = request.getRequestDispatcher("/Display_Busdis.jsp");
			            dispatcher.forward(request, response);
		        		
		        	}
		        	else
		        	{
		        		session.setAttribute("erreur", "Aucun voyage est disponible pour la ligne sélectionnée !");
		        		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
			            dispatcher.forward(request, response);	
		        	}
		        }				
			}
			else if(action.equals("reserver_ticket"))
			{				
				HttpSession session = request.getSession();
				
				String nom = request.getParameter("nom");
				String prenom = request.getParameter("prenom");
				int numbus = Integer.parseInt(request.getParameter("idbus"));
				String heure = (String) session.getAttribute("heure");
				String from;
		        String to;
				
		        String departProche = (String) session.getAttribute("departProche");
		        String depart = (String) session.getAttribute("depart");
		        String destination = (String) session.getAttribute("destination");
		        
		        if(depart == null)
		        {
		        	from = departProche;
		        }
		        else
		        {
		        	from = depart;
		        }
	
		        to = destination;
				
				System.out.println("depart :"+from);
				System.out.println("arrive :"+to);
				System.out.println("heure :"+heure);
				System.out.println("nom + prenom :"+nom+" "+prenom);
				System.out.println("numbus :"+numbus);

				
		        try 
		        {
					if(this.reservation(numbus,nom,prenom ,from,to,LocalTime.parse(heure)))
					{
						session.setAttribute("success", "Bien reservé !");
						session.setAttribute("qrcode",nom+" "+prenom);
						RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
				        dispatcher.forward(request, response);
					}
					else
					{
						session.setAttribute("erreur", "Erreur lors de reservation !");
						RequestDispatcher dispatcher = request.getRequestDispatcher("Display_Busdis.jsp");
				        dispatcher.forward(request, response);
					}
					
				} 
		        catch(BusIsFull e1)
		        {
		        	e1.printStackTrace();
		        	session.setAttribute("erreur", "Le bus de numéro "+numbus+" est plein pour le moment !");
					RequestDispatcher dispatcher = request.getRequestDispatcher("Display_Busdis.jsp");
			        dispatcher.forward(request, response);
		        }
		        catch (NumberFormatException| StationNotFound | SameStation e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
					session.setAttribute("erreur", "Erreur lors de la reservation !");
					RequestDispatcher dispatcher = request.getRequestDispatcher("Display_Busdis.jsp");
			        dispatcher.forward(request, response);
				} 
				
				
			}
			
		}
		/*else
		{
			HttpSession session = request.getSession(true);

			String from = request.getParameter("departure");
	        String to = request.getParameter("arrival");
	        String heureStr = request.getParameter("departureTime");
	        LocalTime heure = LocalTime.parse(heureStr);
	        
	        session.setAttribute("departure", from);
	        session.setAttribute("arrival", to);
	        session.setAttribute("departureTime", heureStr);

	        System.out.println("---->"+from+" "+to+" "+heureStr);

	        

	        // recuperer les voyages diponibles
	        // Après avoir obtenu les résultats
	        //Collection<Voyage> htmlResult = this.voyagesDisponible(from, to, heure);
	        Collection<Voyage> lesvoyagesdispo =this.voyagesDisponible(from, to, heure);
	        session.setAttribute("voyagesDispo", lesvoyagesdispo);
	        

	        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$Voyages dispo");
	        if(lesvoyagesdispo != null)
	        {
	        	for(Voyage v:lesvoyagesdispo)
	            {
	            	System.out.println(v.toString());
	            }
	        	// Ajouter les résultats comme attribut à la requête
	            //request.setAttribute("busResults", htmlResult);

	            // Dispatcher vers la page JSP
	            RequestDispatcher dispatcher = request.getRequestDispatcher("/Display_Busdis.jsp");
	            dispatcher.forward(request, response);
	        }
	        else
	        {
	        	request.setAttribute("Erreur", "Aucun voyage est disponible !");
	        	RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
	            dispatcher.forward(request, response);
	        }

		}*/
			

        
		
        

    }
	

	//***Fonctionnalité 1
	public boolean installerInfoBusway(int idvoyage,int numBus,String nomConducteur,int nbPlacesLimites,
			String statDep,LocalTime heureDepart,String statArv,LocalTime heureArrive,
			Map<LocalTime,String> statArr)
	{
		try 
		{
			BusDTO bus = CatalogueBus.getInstance().findBusByNum(numBus);
			if(bus == null)
			
			throw new BusNotFound();
			
			VoyageDTO voyage;
			voyage = new VoyageDTO(idvoyage,bus,nomConducteur,nbPlacesLimites,statDep,heureDepart,
						statArv,heureArrive,statArr);
			
			voyage.setDisponible(true);
			this.voyages.add(voyage);
			return true;
			//return (new VoyageService().save(voyage));
		
		} 
		catch (ConducteurNotFound | StationNotFound | BusNotFound e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	
	public VoyageDTO getVoyageBynumBus(Collection<VoyageDTO> lesvoyages,int numBus)
	{
		for(VoyageDTO v:lesvoyages)
		{
			System.out.println("--------------->"+v.getBus().getNumBus());
			if(v.getBus().getNumBus()==numBus)
				return v;
		}
		return null;
	}
	
	//***Fonctionnalité 2
	public boolean reservation(int numBus,String nom,String prenom,String from,String to,LocalTime heure) throws BusIsFull,StationNotFound,SameStation
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		Collection<VoyageDTO> voyagesDispo = this.voyagesDisponible(from, to, heure);
		
		
		VoyageDTO v = this.getVoyageBynumBus(voyagesDispo, numBus);
		
		StationDTO fromStation = CatalogueStation.getInstance().findStationByName(from);
		StationDTO toStation = CatalogueStation.getInstance().findStationByName(to);
		if(fromStation.equals(toStation))
			throw new SameStation();

		if(fromStation == null || toStation == null)
			throw new StationNotFound();
		
		v.effectuerReservation(nom,prenom, fromStation, toStation);
		this.saveQrCode(nom+" "+prenom+"_BUS:"+numBus+"_"+from+"_To_"+to+"_at_"+LocalDateTime.now().format(formatter),nom+" "+prenom);
		System.out.println(nom+" "+prenom+" a bien reservé !");
		return true;
		
	}
	
	
	//***Fonctionnalité 3
	
	public void afficherInfoReservation()
	{
		for(VoyageDTO v:this.voyages)
		{
			System.out.println(v);
		}	
	}
	
	public List<VoyageDTO> voyagesParTrajet(String fromStation,String toStation,LocalTime heure)
	{
		List<VoyageDTO> voyagedisponible = new ArrayList<VoyageDTO>();
		
		for(int id:new VoyageService().getIdvoyagesPassePar(fromStation, toStation, heure))
		{
			for(VoyageDTO v:this.voyages)
			{
				if(id == v.getIdVoyage())
					voyagedisponible.add(v);
			}
		}	 
		return voyagedisponible;
	}
	
	public List<VoyageDTO> voyagesDisponiblesOnly(List<VoyageDTO> lesvoyages)
	{
		List<VoyageDTO> voyagedisponible = new ArrayList<VoyageDTO>();
		
		for(VoyageDTO v:lesvoyages)
		{
			if(v.isDisponible() == true)
			{
				voyagedisponible.add(v);
			}
		}
		return voyagedisponible;

	}
	
	public List<VoyageDTO> voyagesDisponible(String From,String To,LocalTime heure) 
	{
		try
		{
			
			StationDTO fromStation = CatalogueStation.getInstance().findStationByName(From);
			StationDTO toStation = CatalogueStation.getInstance().findStationByName(To);
			
			
			if(fromStation == null || toStation == null)
				throw new StationNotFound();
			
			if(fromStation.equals(toStation))
				throw new SameStation();
					
		
			List<VoyageDTO> voyages_from_to = voyagesParTrajet(From,To,heure);

			List<VoyageDTO> voyages_disponibles = voyagesDisponiblesOnly(voyages_from_to);
			
			
			return voyages_disponibles;
		}
		
		catch(StationNotFound stnf)
		{
			System.err.println(stnf.toString());
			return null;
		}
		catch(SameStation sameS)
		{
			System.err.println(sameS.toString());
			return null;
		}
		
			
	}
	
	//***Fonctionnalité 4
	public void afficherbusDisponibles(String From,String To,LocalTime heure) 
	{
		List<VoyageDTO> lesvoyages = this.voyagesDisponible(From, To, heure);
		
		System.out.println("\tLes bus diponibles\t:");
		System.out.println("-----------------------------------------------");
		if(lesvoyages != null)
		{
			for (VoyageDTO voyage : lesvoyages) 
		    {
		        System.out.println("Voyage Id :"+voyage.getIdVoyage()+",Numéro du bus : " + voyage.getBus().getNumBus()); // Affiche le numéro du bus
		    }
		}
	    
	}
		
		public void saveQrCode(String text,String name)
		{
			File file = QRCode.from(text).file();
			
			String filePath = "C:\\Users\\Achra\\Documents\\ILISI2\\JEE\\Workspace\\Busway_Online_V2.0\\src\\main\\webapp\\qrcode.png"; // Replace this with your desired path
			
			File existingFile = new File(filePath);

		    if (existingFile.exists()) {
		        existingFile.delete();
		    }

	        // Write the file to the specified path
	        try
	        {
	            file.renameTo(new File(filePath));
	            System.out.println("Qr code bien crée :" + filePath);
	        } 
	        catch (Exception e) 
	        {
	            System.err.println("Error: " + e.getMessage());
	        }
		}
		
		public List<StationDTO> getStationPlusProches(double longitude,double latitude)
		{
			List<StationDTO> stationspp = new ArrayList<StationDTO>();
			double deltaX;
			double deltaY;
			double res;
			for(StationDTO s:CatalogueStation.getInstance().getStations())
			{
				deltaX = longitude - s.getLongitude();
		        deltaY = latitude - s.getLatitude();
		        res = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
				s.setDistance(res);
				stationspp.add(s);
			}
			Collections.sort(stationspp);
			return stationspp;
		}
}
		
	
	
	

		

