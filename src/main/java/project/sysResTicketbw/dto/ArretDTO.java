package project.sysResTicketbw.dto;

import java.time.LocalTime;

import project.sysResTicketbw.service.CatalogueStation;
import project.sysResTicketbw.exception.*;

public class ArretDTO {
	private LocalTime heureArret;
	private VoyageDTO voyage;
	private StationDTO arrêt;
	private int nbrarrive;
	private int nbrdepart;

	public ArretDTO()
	{
		
	}
	
	public ArretDTO(VoyageDTO voyage,String station,LocalTime heureArret) throws StationNotFound
	{
		this.heureArret = heureArret;
		this.voyage = voyage;
		
		StationDTO statarr = CatalogueStation.getInstance().findStationByName(station);
		
		if(statarr == null)
			throw new StationNotFound();
		statarr.ajouterArret(this);
		this.arrêt = statarr;
	}


	public LocalTime getHeureArret() {
		return heureArret;
	}


	public void setHeureArret(LocalTime heureArret) {
		this.heureArret = heureArret;
	}


	public VoyageDTO getVoyage() {
		return voyage;
	}


	public void setVoyage(VoyageDTO voyage) {
		this.voyage = voyage;
	}


	public StationDTO getArrêt() {
		return arrêt;
	}


	public void setArrêt(StationDTO arrêt) {
		this.arrêt = arrêt;
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

	
	
	
	
	
}
