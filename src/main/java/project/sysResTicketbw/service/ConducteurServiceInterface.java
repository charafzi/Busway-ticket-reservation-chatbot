package project.sysResTicketbw.service;

import java.util.List;

import project.sysResTicketbw.dto.ConducteurDTO;


public interface ConducteurServiceInterface {
	List<ConducteurDTO> retrieve();
	public boolean save(ConducteurDTO cdto);
	public boolean update(ConducteurDTO cdto);
	public boolean delete(ConducteurDTO cdto);

}
