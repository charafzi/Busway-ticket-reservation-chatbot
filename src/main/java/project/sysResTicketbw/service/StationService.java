package project.sysResTicketbw.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import project.sysResTicketbw.bo.Station;
import project.sysResTicketbw.dao.StationDAO;
import project.sysResTicketbw.dto.StationDTO;

public class StationService implements StationServiceInterface{

	@Override
	public List<StationDTO> retrieve() {
		ArrayList<StationDTO> stationsdto = new ArrayList<StationDTO>();
		Collection<Station> stations = new StationDAO().retrieve();
		for(Station s:stations)
			stationsdto.add(this.fromStation(s));
		return stationsdto;
	}
	
	@Override
	public boolean save(StationDTO sdto) {
		return (new StationDAO().save(this.toStation(sdto)));
	}

	@Override
	public boolean update(StationDTO sdto) {
		return (new StationDAO().update(this.toStation(sdto)));
	}

	@Override
	public boolean delete(StationDTO sdto) {
		// TODO Auto-generated method stub
		return (new StationDAO().delete(this.toStation(sdto)));

	}

	
	@Override
	public int nombrePassagersDepart(String nom, int idVoyage) {
		return (new StationDAO().nombrePassagersDepart(nom, idVoyage));
	}

	@Override
	public int nombrePassagersArrive(String nom, int idVoyage) {
		return (new StationDAO().nombrePassagersArrive(nom, idVoyage));
	}
	
	Station toStation(StationDTO sdto)
	{
		Station s = new Station();
		s.setNom(sdto.getNom());
		s.setAdresse(sdto.getAdresse());
		s.setLongitude(sdto.getLongitude());
		s.setLatitude(sdto.getLatitude());
		s.setEndStation(sdto.getEndStation());
		return s;
	}
	
	StationDTO fromStation(Station s)
	{
		StationDTO sdto = new StationDTO();
		sdto.setNom(s.getNom());
		sdto.setAdresse(s.getAdresse());
		sdto.setLongitude(s.getLongitude());
		sdto.setLatitude(s.getLatitude());
		sdto.setEndStation(s.getEndStation());
		return sdto;
	}
	
	public static void main(String[] args) {
		
	}

	
	


	
	

}
