package project.sysResTicketbw.bo;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import project.sysResTicketbw.service.CatalogueStation;
import project.sysResTicketbw.dto.StationDTO;
import project.sysResTicketbw.exception.*;
import project.sysResTicketbw.exception.metier.*;

public class Voyage {
	private int idVoyage;
	private LocalTime heureDepart;
	private LocalTime heureArrive;
	private Station depart;
	private Station arrive;
	private int nbrarrive;
	private int nbrdepart;
	private Bus bus;
	private boolean disponible;
	private Collection<Arret> arrets = new ArrayList<>();
	private Collection<Reservation> reservations = new ArrayList<>();

	public Voyage()
	{
		
	}

	
	
	public int getIdVoyage() {
		return idVoyage;
	}



	public void setIdVoyage(int idVoyage) {
		this.idVoyage = idVoyage;
	}



	public LocalTime getHeureDepart() {
		return heureDepart;
	}



	public void setHeureDepart(LocalTime heureDepart) {
		this.heureDepart = heureDepart;
	}



	public LocalTime getHeureArrive() {
		return heureArrive;
	}



	public void setHeureArrive(LocalTime heureArrive) {
		this.heureArrive = heureArrive;
	}



	public Station getDepart() {
		return depart;
	}



	public void setDepart(Station depart) {
		this.depart = depart;
	}



	public Station getArrive() {
		return arrive;
	}



	public void setArrive(Station arrive) {
		this.arrive = arrive;
	}



	public int getNbrarrive() {
		return nbrarrive;
	}



	public void setNbrarrive(int nbrarrive) {
		this.nbrarrive = nbrarrive;
	}



	public int getNbrdepart() {
		return nbrdepart;
	}



	public void setNbrdepart(int nbrdepart) {
		this.nbrdepart = nbrdepart;
	}



	public Bus getBus() {
		return bus;
	}



	public void setBus(Bus bus) {
		this.bus = bus;
	}



	public Collection<Arret> getArrets() {
		return arrets;
	}



	public void setArrets(Collection<Arret> arrets) {
		this.arrets = arrets;
	}



	public Collection<Reservation> getReservations() {
		return reservations;
	}



	public void setReservations(Collection<Reservation> reservations) {
		this.reservations = reservations;
	}


	
	public boolean isDisponible() {
		return disponible;
	}



	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}



	
	
	
	public void effectuerReservation(String nom,String prenom,Station from,Station to) throws BusIsFull
	{
		if(verifieReservation(from,to) == false)
			throw new BusIsFull();
		
		Reservation reservation = new Reservation(nom,prenom,this,from,to);
		reservations.add(reservation);
	}
	
	public int nbPlacesRemplie(Station station)
	{	
		int nb;
		//si le station est le départ
		if(station.equals(this.getDepart()))
		{
			//nombre de places remples est celui le nombre qui vont monté
			nb = this.getNbrdepart();
		}
		/*
		 * sinon calculer pour chaque station avant le "To" le nb qui vont monté et qui vont descendre
		 */
		else 
		{
			boolean parcourue = true;
			nb = this.getNbrdepart();
			
			for(Arret arr:this.arrets)
			{
				 nb = nb -arr.getNbrarrive() + arr.getNbrdepart();
				 //si on atteint le To
				 if(arr.getArrêt().equals(station))
				 {
					 parcourue = false;
					 break;	
				 }
			}
			if(parcourue==true)
			 {
				 nb += this.getNbrarrive();
			 }
		}
		return nb;
	}
	
	public boolean verifieReservation(Station from,Station to)
	{
		boolean parcourue = true;
		//calculer le nombre de places de départ
		int nbPlacesRemplies = nbPlacesRemplie(this.getDepart());
		
		// si le From est le départ
		if(from.equals(this.getDepart())) 
		{
			//tester possibilité de réserver
			if(nbPlacesRemplies < this.bus.getNbPlacesLimite())
			{
				this.setNbrdepart(this.getNbrdepart()+ 1); /// icrémenter nb départ de from
				
				//chercher le To dans les arrêts
				for(Arret arrive:arrets)
    			 {
    				 if(arrive.getArrêt().equals(to))
    				 {
    					 arrive.setNbrarrive(arrive.getNbrarrive()+1); ///incrémenter nb arrivée de to
    					 parcourue = false;
    					 break;
    				 }
    			 }
				//si on a parcourue tout les arrêts
				if(parcourue == true)
				{
					//tester le To est l'arrivée du voyage
					if(to.equals(this.getArrive()))
						this.setNbrarrive(this.getNbrarrive()+1); ///incrémenter nb arrivée de to
				}
				return true;
			}
			//sinon raise BusIsFull Exception (places vides = 0)
			else
   			 	return false;
		}
		//sinon si arret
		else
		{ 
			//chercher l'arrêt
			 for(Arret arr:arrets)
		     {
				 //si l'arrêt c'est le From
		    	 if(from.equals(arr.getArrêt()))
		    	 {
		    		 //calculer places vides
		    		 int placeVide = this.bus.getNbPlacesLimite() - nbPlacesRemplie(arr.getArrêt());
		    		 
		    		 if( 1 <= placeVide)
		    		 {
		    			 arr.setNbrdepart(arr.getNbrdepart()+1); /// icrémenter nb départ de from
		    			 for(Arret arrive:arrets)
		    			 {
		    				 if(arrive.getArrêt().equals(to))
		    				 {
		    					 arrive.setNbrarrive(arrive.getNbrarrive()+1); ///incrémenter nb arrivée de to
		    					 parcourue = false;
		    					 break;
		    				 }
		    			 }
		 				if(parcourue == true)
		 				{
		 					//tester le To est l'arrivée du voyage
							if(to.equals(this.getArrive()))
								this.setNbrarrive(this.getNbrarrive()+1); ///incrémenter nb arrivée de to
		 				}	
		    			return true ;
		    		 }
		    		//sinon raise BusIsFull Exception (places vides = 0)
		    		 else
		    			 return false;
		    			 
		    	 } 
		     }
		}
		return false;	    
	}

	@Override
	public String toString() 
	{
		int count =0;
		int placesVides = this.getBus().getNbPlacesLimite() - this.nbPlacesRemplie(this.getDepart());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String str = "**************************************************************\n";
		str+="Voyage \t[Id : "+this.getIdVoyage()+"\t] :\n";
		str+="Numero Bus\t: "+bus.getNumBus()+",\t Nombre places Vides\t: "+placesVides+",\t Nombre places limites\t: "+bus.getNbPlacesLimite()+"\n";
		str+="Conducteur du bus\t: "+this.bus.getConducteur().getFullName()+"\n";
		str+="\n";
		str+="Station Départ\t: "+this.depart.getNom()+" , Heure Départ\t=\t"+this.heureDepart.format(formatter)+" ,adresse : "+this.depart.getAdresse()+"\n";
		str+="Station Arrêts\t:\n";
		for(Arret arr:this.arrets)
		{
			count++;
			str+="Station arrêt "+count+"\t: "+arr.getArrêt().getNom()+" ,Heure arrêt\t=\t"+arr.getHeureArret().format(formatter)+",adresse : "+arr.getArrêt().getAdresse()+"\n";
		}
		str+="Station Arrivée\t: "+this.arrive.getNom()+" , Heure Arivée\t=\t"+this.heureArrive.format(formatter)+", adresse : "+this.arrive.getAdresse()+"\n";
		str+="Réservations :\n";
		for(Reservation r:this.reservations)
		{
			str+=r.toString()+"\n";
		}
		str += "**************************************************************\n";
		return str;
	}
	
}
