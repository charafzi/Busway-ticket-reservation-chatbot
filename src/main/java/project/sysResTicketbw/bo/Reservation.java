package project.sysResTicketbw.bo;

import java.time.LocalDateTime;

public class Reservation {
	private LocalDateTime dateReserv;
	private Voyage voyage;
	private Passager passager;
	private Station From;
	private Station To;
	
	
	public Reservation()
	{
		
	}
	
	 public Reservation(String nom,String prenom, Voyage voyage,Station from, Station to) 
	 {
	     this.dateReserv = LocalDateTime.now();
	     this.voyage = voyage;
	     
	     this.From = from;
	     this.To = to;
	     Passager p = new Passager(nom, prenom);
		 p.setReservation(this);
		 this.passager = p;
	        
	 }
	
	public Station getTo() {
		return To;
	}
	public void setTo(Station to) {
		To = to;
	}
	public Station getFrom() {
		return From;
	}

	public void setFrom(Station from) {
		From = from;
	}

	public LocalDateTime getDateReserv() {
		return dateReserv;
	}

	public void setDateReserv(LocalDateTime dateReserv) {
		this.dateReserv = dateReserv;
	}

	public Voyage getVoyage() {
		return voyage;
	}

	public void setVoyage(Voyage voyage) {
		this.voyage = voyage;
	}

	public Passager getPassager() {
		return passager;
	}

	public void setPassager(Passager passager) {
		this.passager = passager;
	}

	@Override
	public String toString() {
		return "Reservation [Date Reservation =" + dateReserv.toLocalDate() + " "+dateReserv.toLocalTime()+", passager=" + passager.getNom()+" "+passager.getPrenom() + ", From=" + From.getNom() + ", To=" + To.getNom()
				+ "]";
	}
	
	
}
