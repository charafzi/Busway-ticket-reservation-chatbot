package project.sysResTicketbw.bo;

import java.util.ArrayList;
import java.util.Collection;

public class Conducteur {
	private String matricule;
	private String nom;
	private String prenom;
	private Collection<Bus> bus = new ArrayList<>();
	
	
	public Conducteur()
	{
	}
	public Conducteur(String matricule, String nom, String prenom) {
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

	public Collection<Bus> getBus() {
		return bus;
	}

	public void setBus(Collection<Bus> bus) {
		this.bus = bus;
	}
	
	public void ajouterBus(Bus b)
	{
		bus.add(b);
	}
	
	
	
}
