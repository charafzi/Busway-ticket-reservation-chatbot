package project.sysResTicketbw.service;

import java.util.List;

import project.sysResTicketbw.dto.BusDTO;

public interface BusServiceInterface {
	List<BusDTO> retrieve();
	public boolean save(BusDTO bdto);
	public boolean update(BusDTO bdto);
	public boolean delete(BusDTO bdto);

}
