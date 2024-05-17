package project.sysResTicketbw.service;

import java.util.ArrayList;
import java.util.List;

import project.sysResTicketbw.dto.BusDTO;

public class CatalogueBus {
	private static CatalogueBus instance;
	private List<BusDTO> lesbus = new ArrayList<BusDTO>();
	
	private CatalogueBus() {
		/*lesbus.add(new BusDTO(1));
        lesbus.add(new BusDTO(2));
        lesbus.add(new BusDTO(3));
        lesbus.add(new BusDTO(4));*/
		lesbus = new BusService().retrieve();
    }

    public static CatalogueBus getInstance() {
        if (instance == null) 
        {
            instance = new CatalogueBus();
        }
        return instance;
    }
	
	public BusDTO findBusByNum(int numBus)
	{
		for(BusDTO b:lesbus)
			if(b.getNumBus() == numBus)
				return b;
		return null;
	}

	public List<BusDTO> getBus() {
		return lesbus;
	}
	
	
	public static void main(String[] args) {
	}

}
