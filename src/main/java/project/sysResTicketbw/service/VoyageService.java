package project.sysResTicketbw.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import project.sysResTicketbw.bo.Arret;
import project.sysResTicketbw.bo.Reservation;
import project.sysResTicketbw.bo.Voyage;
import project.sysResTicketbw.dao.VoyageDAO;
import project.sysResTicketbw.dto.ArretDTO;
import project.sysResTicketbw.dto.BusDTO;
import project.sysResTicketbw.dto.ConducteurDTO;
import project.sysResTicketbw.dto.ReservationDTO;
import project.sysResTicketbw.dto.VoyageDTO;

public class VoyageService implements VoyageServiceInterface{

	@Override
	public boolean save(VoyageDTO v) {
		return (new VoyageDAO().save(this.toVoyage(v)));
	}

	@Override
	public List<VoyageDTO> retrieve() {
		Collection<Voyage> voyages = new VoyageDAO().retrieve();
		ArrayList<VoyageDTO> voyagesdao = new ArrayList<VoyageDTO>();
		
		for(Voyage v:voyages)
		{
			
			voyagesdao.add(this.fromVoyage(v));
		}
		
		return voyagesdao;
	}
	
	@Override
	public boolean update(VoyageDTO v) {
		// TODO Auto-generated method stub
		return (new VoyageDAO().update(this.toVoyage(v)));
	}

	@Override
	public boolean delete(VoyageDTO v) {
		// TODO Auto-generated method stub
		return (new VoyageDAO().delete(this.toVoyage(v)));
	}
	
	public Voyage toVoyage(VoyageDTO vdto)
	{
		ConducteurService cs = new ConducteurService();
		StationService ss = new StationService();
		ArretService as = new ArretService();
		BusService bs = new BusService();
		
		Voyage v = new Voyage();
		v.setIdVoyage(vdto.getIdVoyage());
		v.setHeureDepart(vdto.getHeureDepart());
		v.setDepart(ss.toStation(vdto.getDepart()));
		v.setHeureArrive(vdto.getHeureArrive());
		v.setArrive(ss.toStation(vdto.getArrive()));
		v.setBus(bs.toBus(vdto.getBus()));
		v.getBus().setConducteur(cs.toConducteur(vdto.getBus().getConducteur()));
		
		for(ArretDTO adto:vdto.getArrets())
		{
			v.getArrets().add(as.toArret(adto));
		}
		
		return v;	
	}
	
	public VoyageDTO fromVoyage(Voyage v)
	{
		StationService ss = new StationService();
		ArretService as = new ArretService();
		VoyageDTO vdto = new VoyageDTO();
		vdto.setIdVoyage(v.getIdVoyage());
		vdto.setHeureDepart(v.getHeureDepart());
		vdto.setDepart(CatalogueStation.getInstance().findStationByName(v.getDepart().getNom()));
		vdto.setHeureArrive(v.getHeureArrive());
		vdto.setArrive(CatalogueStation.getInstance().findStationByName(v.getArrive().getNom()));
		BusDTO bdto = CatalogueBus.getInstance().findBusByNum(v.getBus().getNumBus());
		bdto.setNbPlacesLimite(v.getBus().getNbPlacesLimite());
		ConducteurDTO cdto = CatalogueConducteur.getInstance().findByNom(v.getBus().getConducteur().getFullName());
		cdto.ajouterBus(bdto);
		bdto.setConducteur(cdto);
		vdto.setBus(bdto);
		bdto.getVoyages().add(vdto);
		vdto.setDisponible(true);
		
		
		for(Arret a:v.getArrets())
		{
			ArretDTO adto = as.fromArret(a);
			adto.setVoyage(vdto);
			vdto.getArrets().add(adto);
		}
		
		ReservationService rs = new ReservationService();
		for(Reservation r:v.getReservations())
		{
			ReservationDTO rdto = rs.fromReservation(r);
			rdto.setVoyage(vdto);
			vdto.getReservations().add(rdto);
		}
		return vdto;
		
	}

	@Override
	public List<Integer> getIdvoyagesPassePar(String stat1, String stat2, LocalTime heure) {
		return (new VoyageDAO().getIdvoyagesPassePar(stat1, stat2, heure));
	}

	@Override
	public int getNombrePassagerMonte(String station, int numbus) {
		return (new VoyageDAO().getNombrePassagerMonte(station, numbus));
	}

	@Override
	public int getNombrePassagerDescendre(String station, int numbus) {
		// TODO Auto-generated method stub
		return (new VoyageDAO().getNombrePassagerDescendre(station, numbus));
	}

	@Override
	public int getNombrePassagerParLigne(VoyageDTO v, String from, String to) {
		ArrayList<ArretDTO> arrets = new ArrayList<ArretDTO>(v.getArrets());
		String dep = v.getDepart().getNom();
		String arrive =v.getArrive().getNom();
		int count = 0;
		int j=0;
		int k=arrets.size()-1;
		boolean toisArrive = true;
		boolean fromisDep = true;
		
		if(from.equals(to))
			return 0;
		
		if(!v.getDepart().getNom().equals(from))
		{
			//chercher depart dans arret
			for(int i=0; i<arrets.size();i++)
			{
				if(arrets.get(i).getArrêt().getNom().equals(from))
				{
					fromisDep = false;
					dep = arrets.get(i).getArrêt().getNom();
					j=i+1;
					break;
				}
				
			}
		}
		else
			dep = v.getDepart().getNom();
		
		
		if(!v.getArrive().getNom().equals(to))
		{
			//chercher arrive dans arret
			for(int i=0; i<arrets.size();i++)
			{
				if(arrets.get(i).getArrêt().getNom().equals(to))
				{
					arrive = arrets.get(i).getArrêt().getNom();
					toisArrive = false;
					k=i;
					break;
				}
				
			}
			
			
		}
		else
			arrive = v.getArrive().getNom();

		
		VoyageDAO vdao = new VoyageDAO();
		int numbus = v.getBus().getNumBus();
		
		while(!dep.equals(arrive))
		{
			System.out.println("Dep :"+dep);
			System.out.println("Arrive :"+arrive);
			for(int i=j; i<=k ;i++)
			{
				System.out.println(dep+" ----> +"+arrets.get(i).getArrêt().getNom());
				count+= vdao.getNombrePassagerParTrajet(dep, arrets.get(i).getArrêt().getNom(),numbus );
			}
			
			System.out.println();
			if(j==arrets.size() && toisArrive)
			{
				System.out.println(arrets.get(j-1).getArrêt().getNom()+" ----> +"+arrive);
				count+= vdao.getNombrePassagerParTrajet(arrets.get(j-1).getArrêt().getNom(), arrive,numbus );
				break;
			}
			if(toisArrive)
			{
				System.out.println(dep+" ----> +"+arrive);
				count+= vdao.getNombrePassagerParTrajet(dep, arrive,numbus );
			}
				
			
			
			
			dep = arrets.get(j).getArrêt().getNom();
			j++;
				
		}
		return count;
		
	}
	
	

	
}
