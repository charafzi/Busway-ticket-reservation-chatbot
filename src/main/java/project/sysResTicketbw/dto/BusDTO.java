package project.sysResTicketbw.dto;

import java.util.ArrayList;
import java.util.Collection;

import project.sysResTicketbw.bo.Conducteur;
import project.sysResTicketbw.bo.Voyage;
import project.sysResTicketbw.exception.ConducteurNotFound;
import project.sysResTicketbw.service.CatalogueConducteur;

public class BusDTO {
	private int numBus;
	private  int nbPlacesLimite;
	private int nbPlacesVides;
	private ConducteurDTO conducteur;
	private Collection<VoyageDTO> voyages = new ArrayList<>();
	
	
	public BusDTO()
	{
		
	}
	public BusDTO(int num)
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

	
	
	public ConducteurDTO getConducteur() {
		return conducteur;
	}

	public void setConducteur(ConducteurDTO conducteur) {
		this.conducteur = conducteur;
	}

	public Collection<VoyageDTO> getVoyages() {
		return voyages;
	}
	public void setVoyages(Collection<VoyageDTO> voyages) {
		this.voyages = voyages;
	}
	
	public boolean ajouterVoyage(VoyageDTO v)
	{
		return voyages.add(v);	
	}
	
	public void setInfoBus(VoyageDTO voyage,String nomC,int nbPlaces) throws ConducteurNotFound
	{
		ConducteurDTO c = CatalogueConducteur.getInstance().findByNom(nomC);
		if(c==null)
			throw new ConducteurNotFound();
		
		c.ajouterBus(this);
		this.setConducteur(c);
		this.ajouterVoyage(voyage);
		this.setNbPlacesLimite(nbPlaces);
	}

	@Override
	public String toString() {
		return "Bus [numBus=" + numBus + ", nbPlacesLimite=" + nbPlacesLimite + ", nbPlacesVides=" + nbPlacesVides+"]";
	}

}
