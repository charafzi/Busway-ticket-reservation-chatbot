package project.sysResTicketbw.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import project.sysResTicketbw.bo.Arret;
import project.sysResTicketbw.bo.Bus;
import project.sysResTicketbw.bo.Reservation;
import project.sysResTicketbw.bo.Station;
import project.sysResTicketbw.exception.ConducteurNotFound;
import project.sysResTicketbw.exception.StationNotFound;
import project.sysResTicketbw.exception.metier.BusIsFull;
import project.sysResTicketbw.service.CatalogueStation;
import project.sysResTicketbw.service.ReservationService;
import project.sysResTicketbw.service.VoyageService;

public class VoyageDTO {
	private int idVoyage;
	private LocalTime heureDepart;
	private LocalTime heureArrive;
	private StationDTO depart;
	private StationDTO arrive;
	private int nbrarrive;
	private int nbrdepart;
	private BusDTO bus;
	private boolean disponible;
	private Collection<ArretDTO> arrets = new ArrayList<>();
	private Collection<ReservationDTO> reservations = new ArrayList<>();

	
	
	
	

	public VoyageDTO()
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



	public StationDTO getDepart() {
		return depart;
	}



	public void setDepart(StationDTO depart) {
		this.depart = depart;
	}



	public StationDTO getArrive() {
		return arrive;
	}



	public void setArrive(StationDTO arrive) {
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



	public BusDTO getBus() {
		return bus;
	}



	public void setBus(BusDTO bus) {
		this.bus = bus;
	}



	public Collection<ArretDTO> getArrets() {
		return arrets;
	}



	public void setArrets(Collection<ArretDTO> arrets) {
		this.arrets = arrets;
	}



	public Collection<ReservationDTO> getReservations() {
		return reservations;
	}



	public void setReservations(Collection<ReservationDTO> reservations) {
		this.reservations = reservations;
	}


	
	public boolean isDisponible() {
		return disponible;
	}



	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}



	public VoyageDTO(int idvoyage,BusDTO bus,String nomConducteur,int nbPlacesLimites,
			String statDep,LocalTime heureDepart,String statArv,LocalTime heureArrive,
			Map<LocalTime,String> statArr) throws StationNotFound, ConducteurNotFound
	{
		bus.setInfoBus(this,nomConducteur, nbPlacesLimites);
		this.idVoyage = idvoyage;
		this.heureArrive = heureArrive;
		this.heureDepart = heureDepart;
		this.bus = bus;
		
		StationDTO SD = CatalogueStation.getInstance().findStationByName(statDep);
		StationDTO SA = CatalogueStation.getInstance().findStationByName(statArv);
		if(SD == null || SA ==null)
			throw new StationNotFound();
		this.depart = SD;
		this.arrive = SA;

		//création des arrêts
		for(Map.Entry<LocalTime,String> entry : statArr.entrySet())
		{
			ArretDTO arret = new ArretDTO(this,entry.getValue(),entry.getKey());
			arrets.add(arret);
		}
	}
	
	
	public void effectuerReservation(String nom,String prenom,StationDTO from,StationDTO to) throws BusIsFull
	{
		if(verifieReservation(from,to) == false)
			throw new BusIsFull();
		
		ReservationDTO reservation = new ReservationDTO(nom,prenom,this,from,to);
		reservations.add(reservation);
		new ReservationService().save(reservation);
	}
	
	
	public boolean verifieReservation(StationDTO from,StationDTO to)
	{
		VoyageService vs = new VoyageService();
		int nbPassagers = vs.getNombrePassagerParLigne(this, from.getNom(), to.getNom()); 
		
		if((this.getBus().getNbPlacesLimite()-nbPassagers)> 0)
			return true;
		else 
			return false;
	}

	@Override
	public String toString() 
	{
		int count =0;
		int placesVides = 0; //this.getBus().getNbPlacesLimite() - this.nbPlacesRemplie(this.getDepart());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String str = "**************************************************************\n";
		str+="Voyage \t[Id : "+this.getIdVoyage()+"\t] :\n";
		str+="Numero Bus\t: "+bus.getNumBus()+",\t Nombre places Vides\t: "+placesVides+",\t Nombre places limites\t: "+bus.getNbPlacesLimite()+"\n";
		str+="Conducteur du bus\t: "+this.bus.getConducteur().getFullName()+"\n";
		str+="\n";
		str+="Station Départ\t: "+this.depart.getNom()+" , Heure Départ\t=\t"+this.heureDepart.format(formatter)+" ,adresse : "+this.depart.getAdresse()+"\n";
		str+="Station Arrêts\t:\n";
		for(ArretDTO arr:this.arrets)
		{
			count++;
			str+="Station arrêt "+count+"\t: "+arr.getArrêt().getNom()+" ,Heure arrêt\t=\t"+arr.getHeureArret().format(formatter)+",adresse : "+arr.getArrêt().getAdresse()+"\n";
		}
		str+="Station Arrivée\t: "+this.arrive.getNom()+" , Heure Arivée\t=\t"+this.heureArrive.format(formatter)+", adresse : "+this.arrive.getAdresse()+"\n";
		str+="Réservations :\n";
		for(ReservationDTO r:this.reservations)
		{
			str+=r.toString()+"\n";
		}
		str += "**************************************************************\n";
		return str;
	}

}
