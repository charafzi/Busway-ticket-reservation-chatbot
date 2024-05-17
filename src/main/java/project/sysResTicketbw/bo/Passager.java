package project.sysResTicketbw.bo;

public class Passager {
	private String nom;
	private String prenom;
	private Reservation reservation;
	
	
	public Passager() {
	}
	public Passager(String nom, String prenom) {
		super();
		this.nom = nom;
		this.prenom = prenom;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public Reservation getReservation() {
		return reservation;
	}


	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	
	public String getFullName()
	{
		return nom+" "+prenom;
	}
	
	
	
	

}
