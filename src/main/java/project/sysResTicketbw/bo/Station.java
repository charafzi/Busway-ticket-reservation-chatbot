package project.sysResTicketbw.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class Station{
	private String nom;
	private String adresse;
	private double longitude;
	private double latitude;
	private int endStation;
	private Collection<Voyage> Voyagesdepart = new ArrayList<>();
	private Collection<Voyage> Voyagesarrive = new ArrayList<>();
	private Collection<Arret> arrets = new ArrayList<>();

	
	public Station()
	{
		
	}
	public Station(String nom,String adresse,double longitude,double latitude,int endStation)
	{
		this.nom = nom;
		this.adresse = adresse;
		this.longitude = longitude;
		this.latitude = latitude;
		this.endStation = endStation;
	}
	
	public Station(String nom)
	{
		this.nom = nom;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Collection<Arret> getArrets() {
		return arrets;
	}
	public void setArrets(Collection<Arret> arrets) {
		this.arrets = arrets;
	}

	
	public boolean ajouterArret(Arret a)
	{
		return arrets.add(a);
	}
	
	
	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	
	

	public int getEndStation() {
		return endStation;
	}
	public void setEndStation(int endStation) {
		this.endStation = endStation;
	}

}
