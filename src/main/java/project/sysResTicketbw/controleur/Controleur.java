package project.sysResTicketbw.controleur;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import net.glxn.qrgen.javase.QRCode;
import project.sysResTicketbw.dto.ArretDTO;
import project.sysResTicketbw.dto.BusDTO;
import project.sysResTicketbw.dto.ConducteurDTO;
import project.sysResTicketbw.dto.StationDTO;
import project.sysResTicketbw.dto.VoyageDTO;
import project.sysResTicketbw.exception.metier.*;
import project.sysResTicketbw.exception.BusNotFound;
import project.sysResTicketbw.exception.ConducteurNotFound;
import project.sysResTicketbw.exception.SameStation;
import project.sysResTicketbw.exception.StationNotFound;
import project.sysResTicketbw.service.*;


//******* update delete Voyage
//******* create update delete Bus
//******* create update delete conducteur
//******* create update delete Station

public class Controleur {
	private Collection<VoyageDTO> voyages;
	private static Collection<VoyageDTO> voyages1 = new ArrayList<VoyageDTO>(new VoyageService().retrieve());
	
	
	Collection<VoyageDTO> getVoyages()
	{
		if(voyages == null)
		{
			voyages = new ArrayList<VoyageDTO>();
			voyages = new VoyageService().retrieve();
			
		}
		return voyages;
	}
	
	public boolean installerInfoBusway(int idvoyage,int numBus,String nomConducteur,int nbPlacesLimites,
									String statDep,LocalTime heureDepart,String statArv,LocalTime heureArrive,
									Map<LocalTime,String> statArr) throws ConducteurNotFound,StationNotFound , BusNotFound 
	{
		BusDTO bus = CatalogueBus.getInstance().findBusByNum(numBus);
		if(bus == null)
			
			throw new BusNotFound();
		
		VoyageDTO voyage;
		voyage = new VoyageDTO(idvoyage,bus,nomConducteur,nbPlacesLimites,statDep,heureDepart,
							statArv,heureArrive,statArr);

		voyage.setDisponible(true);
		this.getVoyages().add(voyage);
		return (new VoyageService().save(voyage));
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
	
	public boolean reservation(int numBus,String nom,String prenom,String from,String to,LocalTime heure) throws BusIsFull,StationNotFound,SameStation
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		Collection<VoyageDTO> voyagesDispo = this.voyagesDisponible(from, to, heure);
		
		System.out.println("Numéro de bus : " + numBus);
	    System.out.println("Nom : " + nom);
	    System.out.println("Prénom : " + prenom);
	    System.out.println("Départ : " + from);
	    System.out.println("Destination : " + to);
	    System.out.println("Heure de départ : " + heure);
		
		
		VoyageDTO v = this.getVoyageBynumBus(voyagesDispo, numBus);
		
		//erreur de saisie
		if(v == null)
		{
			System.err.println("Aucun voyage disponible de "+from+" à "+to+" à "+heure.toString()+" dans le bus de numéro "+numBus);
			return false;
		}
		
		StationDTO fromStation = CatalogueStation.getInstance().findStationByName(from);
		StationDTO toStation = CatalogueStation.getInstance().findStationByName(to);
		if(fromStation.equals(toStation))
			throw new SameStation();

		if(fromStation == null || toStation == null)
			throw new StationNotFound();
		
		v.effectuerReservation(nom,prenom, fromStation, toStation);
		this.saveQrCode(nom+" "+prenom+"_"+"_From_"+from+"_to_"+to+"_at_"+LocalDateTime.now().format(formatter)+"_",nom+" "+prenom);
		System.out.println(nom+" "+prenom+" a bien reservé !");
		return true;
		
	}
	
	public static boolean reservation1(int numBus,String nom,String prenom,String from,String to,LocalTime heure) throws BusIsFull,StationNotFound,SameStation
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		Collection<VoyageDTO> voyagesDispo = new Controleur().voyagesDisponible(from, to, heure);
		
		
		
		VoyageDTO v = new Controleur().getVoyageBynumBus(voyagesDispo, numBus);
		
		//erreur de saisie
		if(v == null)
		{
			System.err.println("Aucun voyage disponible de "+from+" à "+to+" à "+heure.toString()+" dans le bus de numéro "+numBus);
			return false;
		}
		
		StationDTO fromStation = CatalogueStation.getInstance().findStationByName(from);
		StationDTO toStation = CatalogueStation.getInstance().findStationByName(to);
		if(fromStation.equals(toStation))
			throw new SameStation();

		if(fromStation == null || toStation == null)
			throw new StationNotFound();
		
		v.effectuerReservation(nom,prenom, fromStation, toStation);
		new Controleur().saveQrCode(nom+" "+prenom+"_"+"_From_"+from+"_to_"+to+"_at_"+LocalDateTime.now().format(formatter)+"_",nom+" "+prenom);
		System.out.println(nom+" "+prenom+" a bien reservé !");
		return true;
		
	}
	
	/*public boolean reservation(int numBus,String nomPassager,String from,String to,LocalTime heure) 
	{
		try
		{
			Collection<Voyage> voyagesDispo = this.voyagesDisponible(from, to, heure);
			
			
			Voyage v = this.getVoyageBynumBus(voyagesDispo, numBus);
			
			if(v == null)
			{
				System.err.println("Aucun voyage disponible de "+from+" à "+to+" à "+heure.toString()+" dans le bus de numéro "+numBus);
				return false;
			}
			
			Station fromStation = CatalogueStation.getInstance().chercherStation(from);
			Station toStation = CatalogueStation.getInstance().chercherStation(to);
			if(fromStation.equals(toStation))
				throw new SameStation();

			if(fromStation == null || toStation == null)
				throw new StationNotFound();
			
			v.effectuerReservation(nomPassager, fromStation, toStation);
			
			this.saveQrCode(nomPassager+"_"+"_From_"+from+"_to_"+to+"_"+LocalDate.now().toString()+"_at_"+LocalTime.now()+"_",nomPassager);
			System.out.println(nomPassager+" a bien reservé !");
			return true;
		}
		catch(BusIsFull e1) {
			System.err.println(e1.toString());
			return false;
			
		}
		catch(StationNotFound e2)
		{
			System.err.println(e2.toString());
			return false;
		}
		catch(SameStation e3)
		{
			System.err.println(e3.toString());
			return false;
		}	
		
	}*/
	
	
	
	public void afficherInfoReservation()
	{
		for(VoyageDTO v:this.voyages)
		{
			System.out.println(v);
		}	
	}
	
	public Collection<VoyageDTO> voyagesParTrajet(StationDTO fromStation,StationDTO toStation,LocalTime heure)
	{
		Collection<VoyageDTO> voyagedisponible = new Vector<VoyageDTO>();
		
		Collection<VoyageDTO> lesvoyages = this.getVoyages();
		

		 for (int i = 0; i < lesvoyages.size(); i++) 
		 {
	            VoyageDTO voyage = (VoyageDTO) lesvoyages.toArray()[i];
	            
		 
	            	if(voyage.getDepart().equals(fromStation))
	            	{
	            		
	            		 boolean Arrivetrouve = false;
	           	
	            		if(voyage.getHeureDepart().compareTo(heure)>=0 )
	            		{
	            			for(int j=0;j<voyage.getArrets().size();j++)
	            			{
	            				ArretDTO arret = (ArretDTO) voyage.getArrets().toArray()[j]; 
	            	            
	            	            StationDTO stationArrive = arret.getArrêt();
	            			
	            	            if(stationArrive.equals(toStation))
	            	            {
	            	            	voyagedisponible.add(voyage);
	            	            	Arrivetrouve =true;
	            	            	break;
	            	            }
	            	            
	            			}
	            			if((!Arrivetrouve) && voyage.getArrive().equals(toStation))
	            			{
	            				voyagedisponible.add(voyage);
	            				
	            			}
	            		}
	            	}
	            	else
	            	{   
	            	    boolean Arrivetrouve = false;
	            	    for(int k=0;k < voyage.getArrets().size();k++)
	            	    {	
	            	        ArretDTO arret = (ArretDTO) voyage.getArrets().toArray()[k]; 
	            	        
	            	        if(arret.getHeureArret().compareTo(heure)>=0 )
	            	        {
	            	        	if(arret.getArrêt().equals(fromStation))
	            	        	{
	            	        		for(int l = k ; l < voyage.getArrets().size() ; l++)    
	            	                {	
	            	                    ArretDTO nextArret = (ArretDTO) voyage.getArrets().toArray()[l]; 

	            	                    if (nextArret.getArrêt().equals(toStation)) 
	            	                    {
	            	                        voyagedisponible.add(voyage);
	            	                        Arrivetrouve=true;
	            	                        break; 
	            	                    }
	            	                }
	            	                if((!Arrivetrouve))
	            	                {
	            	                    if(voyage.getArrive().equals(toStation))
	            	                    {
	            	                    	
	            	                    	voyagedisponible.add(voyage);
	            	                    }
	            	                    
	            	                }
	            	                    
	            	                else
	            	                {
	            	                    break;
	            	                }  
	            	        	}
	            	        	
	            	        }
	            	    }
	            	}
		 }
		
		 
		return voyagedisponible;
	}
	
	public static List<VoyageDTO> voyagesParTrajet1(String fromStation,String toStation,LocalTime heure)
	{
		List<VoyageDTO> voyagedisponible = new ArrayList<VoyageDTO>();
		
		for(int id:new VoyageService().getIdvoyagesPassePar(fromStation, toStation, heure))
		{
			for(VoyageDTO v:voyages1)
			{
				if(id == v.getIdVoyage())
					voyagedisponible.add(v);
			}
		}	 
		return voyagedisponible;
	}
	
	public Collection<VoyageDTO> voyagesDisponiblesOnly(Collection<VoyageDTO> lesvoyages)
	{
		Collection<VoyageDTO> voyagedisponible = new Vector<VoyageDTO>();
		
		for(VoyageDTO v:lesvoyages)
		{
			if(v.isDisponible() == true)
			{
				
				voyagedisponible.add(v);
			}
		}
		return voyagedisponible;

	}
	
	public static Collection<VoyageDTO> voyagesDisponiblesOnly1(Collection<VoyageDTO> lesvoyages)
	{
		Collection<VoyageDTO> voyagedisponible = new Vector<VoyageDTO>();
		
		for(VoyageDTO v:lesvoyages)
		{
			if(v.isDisponible() == true)
			{
				
				voyagedisponible.add(v);
			}
		}
		return voyagedisponible;

	}
	
	public Collection<VoyageDTO> voyagesDisponible(String From,String To,LocalTime heure) 
	{
		try
		{
			
			StationDTO fromStation = CatalogueStation.getInstance().findStationByName(From);
			StationDTO toStation = CatalogueStation.getInstance().findStationByName(To);
			
			
			if(fromStation == null || toStation == null)
				throw new StationNotFound();
			
			if(fromStation.equals(toStation))
				throw new SameStation();

					
		
			Collection<VoyageDTO> voyages_from_to = voyagesParTrajet(fromStation,toStation,heure);

			Collection<VoyageDTO> voyages_disponibles = voyagesDisponiblesOnly(voyages_from_to);
			
			
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
	
	public static String voyagesDisponibleString(String From,String To,LocalTime heure) throws StationNotFound,SameStation
	{
		try
		{
			
			StationDTO fromStation = CatalogueStation.getInstance().findStationByName(From);
			StationDTO toStation = CatalogueStation.getInstance().findStationByName(To);
			
			
			if(fromStation == null || toStation == null)
				throw new StationNotFound();
			
			if(fromStation.equals(toStation))
				throw new SameStation();

					
		
			Collection<VoyageDTO> voyages_from_to = voyagesParTrajet1(From,To,heure);

			Collection<VoyageDTO> voyages_disponibles = voyagesDisponiblesOnly1(voyages_from_to);
			
			
			ArrayList<VoyageDTO> voyagess = new ArrayList<>(voyages_disponibles);
			String numBuses = "[";
			
			for(int i=0;i<voyagess.size();i++)
			{
				
				numBuses+=""+voyagess.get(i).getBus().getNumBus();
				if(i!=voyagess.size()-1)
				{
					numBuses+=", ";
				}
			}
			numBuses+="]";
			return numBuses;
		}
		
		catch(StationNotFound stnf)
		{
			System.err.println(stnf.toString());
			return "Un des stations choisis est introuvé.";
		}
		catch(SameStation sameS)
		{
			System.err.println(sameS.toString());
			return "Le station départ est celui d'arrivée";
		}
		
			
	}
	
	public void afficherbusDisponibles(String From,String To,LocalTime heure) 
	{
		Collection<VoyageDTO> lesvoyages = this.voyagesDisponible(From, To, heure);
		
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
		
		//String filePath = "C:\\Users\\Achra\\Documents\\ILISI2\\Atelier POO\\QRcode\\"+name+".png"; // Replace this with your desired path

		String filePath = "C:\\Users\\Achra\\Documents\\ILISI2\\JEE\\Workspace\\Busway_Online_V2.0\\src\\main\\webapp\\qrcode.png";
        // Write the file to the specified path
		
		File existingFile = new File(filePath);

	    if (existingFile.exists()) {
	        existingFile.delete();
	    }

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
		
	
	
	public static void main(String[] args)
	{
		Controleur c = new Controleur();
		
		/*StationService ss = new StationService();
		//List<StationDTO> stations = ss.retrieve();
		
		for(StationDTO s:CatalogueStation.getInstance().getStations())
		{
			ss.save(s);
		}
		
		
		ConducteurService cs = new ConducteurService();

		for(ConducteurDTO cond:CatalogueConducteur.getInstance().getConducteurs())
		{
			cs.save(cond);
		}
		
		BusService bs = new BusService();
		
		for(BusDTO b:CatalogueBus.getInstance().getBus())
		{
			bs.save(b);
		}
		
		System.out.println("DONE !");*/
		
		
		
		VoyageService vs = new VoyageService();
		
		
	     
	    try {
	    	 
	    	// Enregistrer le 1ere bus
	 		LocalTime heureDep = LocalTime.of(18, 0);
	 		LocalTime heurearrive = LocalTime.of(12, 0);
	 		
	 	     Map<LocalTime,String> map = new TreeMap<>();///pour que l'insertion des arrêts soit d'un manière ordonnée
	 	  
	 	     LocalTime heure = LocalTime.of(9, 0);
	 	     map.put(heure,"B");
	 	     
	 	     heure = LocalTime.of(10, 0);
	 	     map.put(heure,"C");
	 	     
	 	     heure = LocalTime.of(11, 0);
	 	     map.put(heure,"D");
			c.installerInfoBusway(100,1,"HACHIMI Ahmed",5,"A",heureDep,"E",heurearrive,map);
			
			 // Enregistrer le deuxième bus
		     LocalTime heureDep1 = LocalTime.of(18, 30);
		     Map<LocalTime, String> map1 = new TreeMap<>();
		     map1.put(LocalTime.of(9, 30), "J");
		     LocalTime arrive1 = LocalTime.of(10, 30);
		     c.installerInfoBusway(102,2,"HACHIMI Ahmed",25,"C",heureDep1,"I",arrive1,map1);
		     
			
			// Enregistrer le troisième bus
		     LocalTime heureDep3 = LocalTime.of(17, 0);
		     Map<LocalTime, String> map3 = new TreeMap<>();
		     
		     heure = LocalTime.of(10, 30);
		     map3.put(heure,"F");
		     
		     heure = LocalTime.of(11, 0);
		     map3.put(heure,"G");
		     
		     heure = LocalTime.of(11, 30);
		     map3.put(heure,"H");
		     LocalTime arrive3 = LocalTime.of(17, 30);
		     c.installerInfoBusway(104, 3, "DADAS Ali", 4, "C", heureDep3, "I", arrive3, map3);
		     
		     
		  // Enregistrer le 4eme bus
		     LocalTime heureDep4 = LocalTime.of(19, 0);
		     Map<LocalTime, String> map4 = new TreeMap<>();
		     heure = LocalTime.of(11, 30);
		     map4.put(heure,"B");
		     
		     heure = LocalTime.of(12, 30);
		     map4.put(heure,"C");
		     
		     heure = LocalTime.of(12, 45);
		     map4.put(heure,"F");
		     
		     heure = LocalTime.of(13, 0);
		     map4.put(heure,"G");
		     
		     heure = LocalTime.of(13, 30);
		     map4.put(heure,"H");
		     LocalTime arrive4 = LocalTime.of(15, 0);
		     c.installerInfoBusway(105, 4, "DADAS Ali", 8, "A", heureDep4, "I", arrive4, map4);
		} catch (ConducteurNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StationNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BusNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	
	   

	    
	     
	     for(VoyageDTO v:c.getVoyages())
			{
	    	vs.save(v);
			}
	     
		
		/*StationDTO s = CatalogueStation.getInstance().findStationByName("A");
		
		s.setAdresse("xxxxxxxxxxxxxx");
		System.out.println(s.getAdresse());
		s.setLongitude(10.00);
		ss.delete(s);*/
		
		

		/*ConducteurService cs = new ConducteurService();

		for(ConducteurDTO cond:CatalogueConducteur.getInstance().getConducteurs())
		{
			cs.save(cond);
		}
	  
		
		for(ConducteurDTO cond:cs.retrieve())
		{
			System.out.println(cond.getFullName());
		}
		
		/*ConducteurDTO cond = CatalogueConducteur.getInstance().chercherConducteur("DADAS Ali");
		
		cond.setNom("RAFIK");
		cs.delete(cond);*/
		
		/*BusService bs = new BusService();
		
		for(BusDTO b:CatalogueBus.getInstance().getBus())
		{
			bs.save(b);
		}*/
		
		/*BusDTO b= CatalogueBus.getInstance().chercherBus(1);
		b.setNbPlacesLimite(500);
		bs.delete(b);
		
			  
		for(BusDTO b:CatalogueBus.getInstance().getBus())
		{
			System.out.println(b);
		}
		
		

	    c.voyages = new VoyageService().retrieve();

		for(VoyageDTO v:c.voyages)
		{
			System.out.println(v);
		}
		
		for(BusDTO b:CatalogueBus.getInstance().getBus())
		{
			System.out.println(b);
		}
		*/
	     
		VoyageDTO voyage = null;
	    for(VoyageDTO v:c.getVoyages())
	    {
	    	System.out.println(v);
	    	if(v.getIdVoyage() == 100)
	    		voyage = v;
	    }
	    
	     
	    System.out.println(vs.getNombrePassagerParLigne(voyage, "A", "C"));
		System.out.println("done!");
		
		
		
		
	
		
		
		
	    
	     
	}

}
