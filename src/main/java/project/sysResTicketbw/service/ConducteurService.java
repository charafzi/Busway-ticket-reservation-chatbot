package project.sysResTicketbw.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import project.sysResTicketbw.bo.Bus;
import project.sysResTicketbw.bo.Conducteur;
import project.sysResTicketbw.bo.Station;
import project.sysResTicketbw.dao.ConducteurDAO;
import project.sysResTicketbw.dto.BusDTO;
import project.sysResTicketbw.dto.ConducteurDTO;

public class ConducteurService implements ConducteurServiceInterface{

	@Override
	public List<ConducteurDTO> retrieve() {
		ArrayList<ConducteurDTO> conducteursdto = new ArrayList<ConducteurDTO>();
		
		for(Conducteur c : new ConducteurDAO().retrieve())
		{
			conducteursdto.add(this.fromConducteur(c));
		}
		return conducteursdto;
	}
	
	@Override
	public boolean save(ConducteurDTO cdto) {
		// TODO Auto-generated method stub
		return (new ConducteurDAO().save(this.toConducteur(cdto)));
	}

	@Override
	public boolean update(ConducteurDTO cdto) {
		// TODO Auto-generated method stub
		return (new ConducteurDAO().update(this.toConducteur(cdto)));
	}

	@Override
	public boolean delete(ConducteurDTO cdto) {
		// TODO Auto-generated method stub
		return (new ConducteurDAO().delete(this.toConducteur(cdto)));
	}
	
	Conducteur toConducteur(ConducteurDTO cdto)
	{
		Conducteur c = new Conducteur();
		c.setMatricule(cdto.getMatricule());
		c.setNom(cdto.getNom());
		c.setPrenom(cdto.getPrenom());
		for(BusDTO bdto :cdto.getBusDTO())
		{
			Bus b = new Bus();
			b.setNumBus(bdto.getNumBus());
			c.ajouterBus(b);
		}
		return c;
	}
	
	ConducteurDTO fromConducteur(Conducteur c)
	{
		ConducteurDTO cdto = new ConducteurDTO();
		cdto.setMatricule(c.getMatricule());
		cdto.setNom(c.getNom());
		cdto.setPrenom(c.getPrenom());
		for(Bus b:c.getBus())
		{
			BusDTO bdto = CatalogueBus.getInstance().findBusByNum(b.getNumBus());
			cdto.ajouterBus(bdto);
			bdto.setConducteur(cdto);
		}
		return cdto;
	}

	

}
