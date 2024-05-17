package project.sysResTicketbw.bo;

import java.time.LocalTime;

import project.sysResTicketbw.service.CatalogueStation;
import project.sysResTicketbw.exception.*;

public class Arret {
	private LocalTime heureArret;
	private Voyage voyage;
	private Station arrêt;
	private int nbrarrive;
	private int nbrdepart;

	public Arret()
	{
		
	}

	public LocalTime getHeureArret() {
		return heureArret;
	}


	public void setHeureArret(LocalTime heureArret) {
		this.heureArret = heureArret;
	}


	public Voyage getVoyage() {
		return voyage;
	}


	public void setVoyage(Voyage voyage) {
		this.voyage = voyage;
	}


	public Station getArrêt() {
		return arrêt;
	}


	public void setArrêt(Station arrêt) {
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
