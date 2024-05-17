package project.sysResTicketbw.service;

import project.sysResTicketbw.bo.Passager;
import project.sysResTicketbw.bo.Reservation;
import project.sysResTicketbw.dao.ReservationDAO;
import project.sysResTicketbw.dto.PassagerDTO;
import project.sysResTicketbw.dto.ReservationDTO;

public class ReservationService implements ReservationServiceInterface{

	@Override
	public boolean save(ReservationDTO rdto) {
		return new ReservationDAO().save(this.toReservation(rdto));
	}
	
	public Reservation toReservation(ReservationDTO rdto)
	{
		StationService ss = new StationService();
		VoyageService vs = new VoyageService();
		ServicePassager sp = new ServicePassager();
		Reservation r = new Reservation();
		r.setDateReserv(rdto.getDateReserv());
		r.setFrom(ss.toStation(rdto.getFrom()));
		r.setTo(ss.toStation(rdto.getTo()));
		r.setVoyage(vs.toVoyage(rdto.getVoyage()));
		r.setPassager(sp.toPassager(rdto.getPassager()));
		return r;
	}
	
	public ReservationDTO fromReservation(Reservation r)
	{
		ReservationDTO rdto = new ReservationDTO();
		PassagerDTO p = new PassagerDTO();
		p.setNom(r.getPassager().getNom());
		p.setPrenom(r.getPassager().getPrenom());
		rdto.setPassager(p);
		rdto.setFrom(CatalogueStation.getInstance().findStationByName(r.getFrom().getNom()));
		rdto.setTo(CatalogueStation.getInstance().findStationByName(r.getTo().getNom()));
		rdto.setDateReserv(r.getDateReserv());
		return rdto;
	}

}
