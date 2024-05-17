package project.sysResTicketbw.dto;

import java.util.ArrayList;
import java.util.Collection;

import project.sysResTicketbw.bo.Arret;
import project.sysResTicketbw.bo.Voyage;

public class StationDTO implements Comparable<StationDTO>{
	private String nom;
	private String adresse;
	private double longitude;
	private double latitude;
	private double distance;
	private int endStation;
	private Collection<VoyageDTO> Voayagesdepart = new ArrayList<>();
	private Collection<VoyageDTO> Voayagesarrive = new ArrayList<>();
	private Collection<ArretDTO> arrets = new ArrayList<>();

	
	public StationDTO()
	{
		
	}
	
	public StationDTO(String nom,String adresse,double longitude,double latitude, int endstation)
	{
		this.nom = nom;
		this.adresse = adresse;
		this.longitude = longitude;
		this.latitude = latitude;
		this.endStation = endstation;
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

	public Collection<ArretDTO> getArrets() {
		return arrets;
	}
	public void setArrets(Collection<ArretDTO> arrets) {
		this.arrets = arrets;
	}

	
	public boolean ajouterArret(ArretDTO a)
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
	

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	

	public int getEndStation() {
		return endStation;
	}

	public void setEndStation(int endStation) {
		this.endStation = endStation;
	}

	public boolean equals(StationDTO obj) 
	{
        if(this.nom.equals(obj.getNom()) && this.adresse.equals(obj.getAdresse()))
        	return true;
        return false;
    }

	@Override
	public int compareTo(StationDTO other) {
        return Double.compare(this.distance, other.distance);
    }
}
