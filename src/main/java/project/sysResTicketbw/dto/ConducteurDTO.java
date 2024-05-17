package project.sysResTicketbw.dto;

import java.util.ArrayList;
import java.util.Collection;


public class ConducteurDTO {
	private String matricule;
	private String nom;
	private String prenom;
	private Collection<BusDTO> bus = new ArrayList<>();
	
	public ConducteurDTO()
	{
		
	}
	public ConducteurDTO(String matricule, String nom, String prenom) {
		super();
		this.matricule = matricule;
		this.nom = nom;
		this.prenom = prenom;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
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
	
	public String getFullName()
	{
		return nom+" "+prenom;
	}

	public Collection<BusDTO> getBusDTO() {
		return bus;
	}

	public void setBusDTO(Collection<BusDTO> bus) {
		this.bus = bus;
	}
	
	public void ajouterBus(BusDTO b)
	{
		bus.add(b);
	}
}
