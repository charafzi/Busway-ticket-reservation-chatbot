package project.sysResTicketbw.service;

import java.util.ArrayList;
import java.util.List;

import project.sysResTicketbw.bo.Conducteur;
import project.sysResTicketbw.dto.ConducteurDTO;

public class CatalogueConducteur {
	private static CatalogueConducteur instance;
	private List<ConducteurDTO> conducteurs = new ArrayList<>();
	
	private CatalogueConducteur()
	{
		//conducteurs = new ArrayList<ConducteurDTO>(new ConducteurService().retrieve());
		/*conducteurs.add(new ConducteurDTO("123456789","HACHIMI","Ahmed"));
		conducteurs.add(new ConducteurDTO("123445270","DADAS","Ali"));*/
		conducteurs = new ConducteurService().retrieve();
	}
	
	public static CatalogueConducteur getInstance()
	{
		if(instance==null)
			instance = new CatalogueConducteur();
		return instance;
	}
	
	public ConducteurDTO findByNom(String nomComplet)
	{
		for(ConducteurDTO c:conducteurs)
		{
			if(c.getFullName().equals(nomComplet))
				return c;
		}
		return null;
	}
	
	public List<ConducteurDTO> getConducteurs()
	{
		return this.conducteurs;
	}
	
	public void SupprimerConducteur(ConducteurDTO cd)
	{
		conducteurs.remove(cd);
	}
	
	
	
}
