package project.sysResTicketbw.service;

import project.sysResTicketbw.bo.Arret;
import project.sysResTicketbw.dto.ArretDTO;

public class ArretService {
	
	Arret toArret(ArretDTO adto)
	{
		StationService ss = new StationService();
		Arret a = new Arret();
		a.setHeureArret(adto.getHeureArret());
		a.setArrêt(ss.toStation(adto.getArrêt()));
		return a;
	}
	
	ArretDTO fromArret(Arret a)
	{
		ArretDTO adto = new ArretDTO();
		adto.setHeureArret(a.getHeureArret());
		adto.setArrêt(CatalogueStation.getInstance().findStationByName(a.getArrêt().getNom()));
		return adto;
	}

}
