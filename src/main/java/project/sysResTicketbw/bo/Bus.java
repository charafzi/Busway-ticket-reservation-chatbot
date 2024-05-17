package project.sysResTicketbw.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import project.sysResTicketbw.service.CatalogueConducteur;
import project.sysResTicketbw.exception.*;


public class Bus {
	private int numBus;
	private  int nbPlacesLimite;
	private int nbPlacesVides;
	private Conducteur conducteur;
	private Collection<Voyage> voyages = new ArrayList<>();
	
	
	public Bus()
	{
		
	}
	public Bus(int num)
	{
		this.numBus = num;	
	}
	
	public int getNumBus() {
		return numBus;
	}
	public void setNumBus(int numBus) {
		this.numBus = numBus;
	}
	public int getNbPlacesLimite() {
		return nbPlacesLimite;
	}
	
	
	public int getNbPlacesVides() {
		return nbPlacesVides;
	}

	public void setNbPlacesVides(int nbPlacesVides) {
		this.nbPlacesVides = nbPlacesVides;
	}

	public void setNbPlacesLimite(int nbPlacesLimite) 
	{
		this.nbPlacesLimite = nbPlacesLimite;
	}

	
	
	public Conducteur getConducteur() {
		return conducteur;
	}

	public void setConducteur(Conducteur conducteur) {
		this.conducteur = conducteur;
	}

	public Collection<Voyage> getVoyages() {
		return voyages;
	}
	public void setVoyages(Collection<Voyage> voyages) {
		this.voyages = voyages;
	}
	
	public boolean ajouterVoyage(Voyage v)
	{
		return voyages.add(v);	
	}
	
	
	
}
