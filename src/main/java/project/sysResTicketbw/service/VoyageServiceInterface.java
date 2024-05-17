package project.sysResTicketbw.service;

import java.time.LocalTime;
import java.util.List;

import project.sysResTicketbw.dto.VoyageDTO;

public interface VoyageServiceInterface {
	public boolean save(VoyageDTO v);
	public boolean update(VoyageDTO v);
	public boolean delete(VoyageDTO c);
	public List<VoyageDTO> retrieve();
	public List<Integer> getIdvoyagesPassePar(String stat1,String stat2,LocalTime heure);
	public int getNombrePassagerMonte(String station,int numbus);
	public int getNombrePassagerDescendre(String station,int numbus);
	public int getNombrePassagerParLigne(VoyageDTO v,String from,String to);

}
