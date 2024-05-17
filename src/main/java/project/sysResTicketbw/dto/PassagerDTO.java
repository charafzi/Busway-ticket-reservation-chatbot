package project.sysResTicketbw.dto;


public class PassagerDTO {
	private String nom;
	private String prenom;
	private ReservationDTO reservation;
	
	public PassagerDTO()
	{
		
	}
	
	
	
	public PassagerDTO(String nom, String prenom) {
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
	public ReservationDTO getReservation() {
		return reservation;
	}
	public void setReservation(ReservationDTO reservation) {
		this.reservation = reservation;
	}
	
	

}
