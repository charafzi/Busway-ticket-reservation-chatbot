package project.sysResTicketbw.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import project.sysResTicketbw.bo.Passager;
import project.sysResTicketbw.bo.Station;
import project.sysResTicketbw.bo.Voyage;

public class ReservationDTO {
	private LocalDateTime dateReserv;
	private VoyageDTO voyage;
	private PassagerDTO passager;
	private StationDTO From;
	private StationDTO To;
	
	
	
	public ReservationDTO()
	{
		
	}
	
	 public ReservationDTO(String nom,String prenom, VoyageDTO voyage,StationDTO from, StationDTO to) 
	 {
	     this.dateReserv = LocalDateTime.now();
	     this.voyage = voyage;
	     
	     this.From = from;
	     this.To = to;
	     PassagerDTO p = new PassagerDTO(nom, prenom);
		 p.setReservation(this);
		 this.passager = p;
	        
	 }

	public LocalDateTime getDateReserv() {
		return dateReserv;
	}

	public void setDateReserv(LocalDateTime dateReserv) {
		this.dateReserv = dateReserv;
	}

	public VoyageDTO getVoyage() {
		return voyage;
	}

	public void setVoyage(VoyageDTO voyage) {
		this.voyage = voyage;
	}

	public PassagerDTO getPassager() {
		return passager;
	}

	public void setPassager(PassagerDTO passager) {
		this.passager = passager;
	}

	public StationDTO getFrom() {
		return From;
	}


	public void setFrom(StationDTO from) {
		From = from;
	}

	public StationDTO getTo() {
		return To;
	}

	public void setTo(StationDTO to) {
		To = to;
	}
	
	public String afficher()
	{
		String str="";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		str+="Date : "+this.getDateReserv().format(formatter)+", Départ : "+this.getFrom().getAdresse()+", Destination : "+this.getTo().getAdresse();
		return str;
	}

	@Override
	public String toString() {
		return "ReservationDTO [dateReserv=" + dateReserv.toString() + ", voyage=" + voyage.getIdVoyage() + ", From=" + From.getNom() + ", To=" + To.getNom()
				+ "]";
	}
	
	
	
	

}
