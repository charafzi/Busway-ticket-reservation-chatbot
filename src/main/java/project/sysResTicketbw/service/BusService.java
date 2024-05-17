package project.sysResTicketbw.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import project.sysResTicketbw.bo.Bus;
import project.sysResTicketbw.dao.BusDAO;
import project.sysResTicketbw.dto.BusDTO;

public class BusService implements BusServiceInterface {
	private Collection<Bus> bus = new ArrayList<>();

	@Override
	public boolean save(BusDTO bdto) {
		// TODO Auto-generated method stub
		return (new BusDAO().save(this.toBus(bdto)));
	}

	@Override
	public boolean update(BusDTO bdto) {
		// TODO Auto-generated method stub
		return (new BusDAO().update(this.toBus(bdto)));
	}

	@Override
	public boolean delete(BusDTO bdto) {
		// TODO Auto-generated method stub
		return (new BusDAO().delete(this.toBus(bdto)));
	}
	
	@Override
	public List<BusDTO>retrieve() {
		ArrayList<BusDTO> lesbusdto = new ArrayList<>();
		
		for(Bus b:new BusDAO().retrieve())
		{
			lesbusdto.add(this.fromBus(b));
		}
		return lesbusdto;
	}
	
	Bus toBus(BusDTO bdto)
	{
		Bus b = new Bus();
		b.setNumBus(bdto.getNumBus());
		b.setNbPlacesLimite(bdto.getNbPlacesLimite());
		return b;
	}
	
	BusDTO fromBus(Bus b)
	{
		BusDTO bdto = new BusDTO();
		bdto.setNumBus(b.getNumBus());
		bdto.setNbPlacesLimite(b.getNbPlacesLimite());
		return bdto;
	}
	
	
	public static void main(String[] args) {
		
	}

	
	

}
