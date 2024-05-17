package project.sysResTicketbw.service;
import java.util.List;

import project.sysResTicketbw.bo.Station;
import project.sysResTicketbw.dto.StationDTO;

public interface StationServiceInterface {
	List<StationDTO> retrieve();
	public boolean save(StationDTO sdto);
	public boolean update(StationDTO sdto);
	public boolean delete(StationDTO sdto);
	int nombrePassagersDepart(String nom,int idVoyage);
	int nombrePassagersArrive(String nom,int idVoyage);

}
