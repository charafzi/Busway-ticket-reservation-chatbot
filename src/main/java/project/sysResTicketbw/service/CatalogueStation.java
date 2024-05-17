package project.sysResTicketbw.service;

import java.util.ArrayList;
import java.util.List;

import project.sysResTicketbw.dto.StationDTO;

public class CatalogueStation {
	private static CatalogueStation instance;
	private List<StationDTO> stations = new ArrayList<>();

	private CatalogueStation()
	{
		/*stations.add(new StationDTO("A", "Quartier Errahma", 33.5913, -7.5895,1));
        stations.add(new StationDTO("B", "Bvds boulevards Al Laymoun", 33.5884, -7.6032,0));
        stations.add(new StationDTO("C", "Rond point Oulmès", 33.5919, -7.6198,0));
        stations.add(new StationDTO("D", "Yaacoub El Mansour", 33.5912, -7.6438,0));
        stations.add(new StationDTO("E", "Abdellah Charif", 33.5833, -7.6507,1));
        stations.add(new StationDTO("F", "Oum Rabie", 33.5726, -7.6571,0));
        stations.add(new StationDTO("G", "Ain Sabaa", 33.5687, -7.6676,0));
        stations.add(new StationDTO("H", "Oulfa", 33.5689, -7.6893,0));
        stations.add(new StationDTO("I", "Salmiya", 33.5762, -7.7149,1));
        stations.add(new StationDTO("J", "Salmiya 2", 33.5590, -7.6576,0));*/
		stations = new StationService().retrieve();
		
	}
	
	public static CatalogueStation getInstance()
	{
		if(instance==null)
			instance = new CatalogueStation();
		return instance;
	}
	
	public List<StationDTO> getStations()
	{
		return this.stations;
	}
	
	
	public StationDTO findStationByName(String nomS)
	{
		for(StationDTO s:stations)
		{
			if(s.getNom().equals(nomS))
				return s;		
		}
		return null;
	}
	
	public StationDTO findStationByAdresse(String adresse)
	{
		for(StationDTO s:stations)
		{
			if(s.getAdresse().equalsIgnoreCase(adresse))
				return s;		
		}
		return null;
	}
	
	
}
