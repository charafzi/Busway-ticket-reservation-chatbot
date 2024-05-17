package project.sysResTicketbw.service;

import project.sysResTicketbw.bo.Passager;
import project.sysResTicketbw.dto.PassagerDTO;

public class ServicePassager {
	
	Passager toPassager(PassagerDTO pdto)
	{
		Passager p = new Passager();
		p.setNom(pdto.getNom());
		p.setPrenom(pdto.getPrenom());
		return p;
	}
	
	PassagerDTO fromPassager(Passager p)
	{
		PassagerDTO pdto = new PassagerDTO();
		pdto.setNom(p.getNom());
		pdto.setPrenom(p.getPrenom());
		return pdto;
	}

}
