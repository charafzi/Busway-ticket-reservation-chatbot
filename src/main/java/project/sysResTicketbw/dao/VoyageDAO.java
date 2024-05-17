package project.sysResTicketbw.dao;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.neo4j.driver.Query;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import project.sysResTicketbw.bo.Arret;
import project.sysResTicketbw.bo.Bus;
import project.sysResTicketbw.bo.Conducteur;
import project.sysResTicketbw.bo.Passager;
import project.sysResTicketbw.bo.Reservation;
import project.sysResTicketbw.bo.Station;
import project.sysResTicketbw.bo.Voyage;
import project.sysResTicketbw.service.CatalogueBus;
import project.sysResTicketbw.service.CatalogueConducteur;
import project.sysResTicketbw.service.CatalogueStation;

public class VoyageDAO {

	public boolean save(Voyage v)
	{
		Transaction tx = null;
		String query;
		Value parameters;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		
		try
		{
			Session session = Connexion.getDriver().session();
			tx = session.beginTransaction();
			
			query = new String("MATCH(b:Bus {numBus : $num}), (c:Conducteur {matricule : $matricule})\r\n"
					+"set b.nbPlacesLimite = $nb "
					+ "MERGE (c)-[:Conduit]->(b)\r\n"
					+ "WITH b\r\n"
					+ "Match (dep:Station {nom : $stationdep}) "
					+ "MERGE (b)-[:Depart {idVoyage : $id, heureDepart : $heuredepart}]->(dep)\r\n");
			parameters = Values.parameters("num", v.getBus().getNumBus(),"nb",v.getBus().getNbPlacesLimite(),
					"matricule",v.getBus().getConducteur().getMatricule(),
					"id",v.getIdVoyage(),"heuredepart",v.getHeureDepart().format(formatter),"stationdep",v.getDepart().getNom());
					
	        tx.run(query, parameters);
			
			LocalTime preced = v.getHeureDepart();
			for(Arret a:v.getArrets())
			{
				Session session2 = Connexion.getDriver().session();
				 String query2 = "match(s2:Station { nom : $nom}) with s2 match(s1:Station)-[rel:NEXT]->(s2) return rel.heureParcours as nb";
	             parameters = Values.parameters("nom",a.getArrêt().getNom());
	             Result result2 = session2.run(query2,parameters);
	                	                
	                if(result2.hasNext()) 
	                {
	                    org.neo4j.driver.Record record2 = result2.next();
	                    double heureparcours = record2.get("nb").asDouble();
	                    LocalTime heure = preced.plusMinutes((long) heureparcours);
	                    a.setHeureArret(heure);
	                    preced = heure;
	                      
	                }
	             session2.close();
				
				query = new String("MATCH(b:Bus {numBus : $num}), (s:Station {nom : $stationarr}) "
						+ "WITH b,s "
						+ "MERGE (b)-[:Arrete {idVoyage : $id, heureArret: $heurearret}]->(s)");
				parameters = Values.parameters("num",v.getBus().getNumBus(),"id",v.getIdVoyage(),
						"heurearret",a.getHeureArret().format(formatter),"stationarr",a.getArrêt().getNom());
		        tx.run(query, parameters);
			}
			
			Session session2 = Connexion.getDriver().session();
			String query2 = "match(s2:Station { nom : $nom}) with s2 match(s1:Station)-[rel:NEXT]->(s2) return rel.heureParcours as nb";
            parameters = Values.parameters("nom",v.getArrive().getNom());
            Result result2 = session2.run(query2,parameters);
               	                
               if(result2.hasNext()) 
               {
                   org.neo4j.driver.Record record2 = result2.next();
                   double heureparcours = record2.get("nb").asDouble();
                   LocalTime heure = preced.plusMinutes((long) heureparcours);
                   v.setHeureArrive(heure);
                     
               }
               
               session2.close();
               
			query = new String("MATCH(b:Bus {numBus : $num})\r\n"
					+ "WITH b\r\n"
					+ "Match (arr:Station {nom : $stationarr}) "
					+ "MERGE (b)-[:Arrive {idVoyage : $id, heureArrive: $heurearrive}]->(arr)");
			parameters = Values.parameters("num", v.getBus().getNumBus(),"id",v.getIdVoyage(),"heurearrive",
					v.getHeureArrive().format(formatter),"stationarr",v.getArrive().getNom());
	        
			tx.run(query, parameters);
	        tx.commit();
			session.close();
			return true;
			 

		}
		catch(Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean update(Voyage v)
	{
		Transaction tx = null;
		String query;
		Value parameters;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		
		try
		{
			Session session =Connexion.getDriver().session();
			tx = session.beginTransaction();
			//supprimer les anciennes relations
			query = new String("MATCH (c:Conducteur)-[r:Conduit]-(b:Bus)-[:Depart {idVoyage : $id}]->(:Station)\r\n"
					+ "delete r");
			parameters = Values.parameters("id",v.getIdVoyage());
			tx.run(query, parameters);
			
			query = new String("MATCH(b:Bus {numBus : $num}), (b)-[r]->(:Station)\r\n"
					+"WHERE r.idVoyage = $id "
					+ "Delete r");
			parameters = Values.parameters("id",v.getIdVoyage(),"num",v.getBus().getNumBus());
					
	        tx.run(query, parameters);
			
	        query = new String("MATCH(b:Bus {numBus : $num}), (c:Conducteur {matricule : $matricule})\r\n"
					+"set b.nbPlacesLimite = $nb "
					+ "MERGE (c)-[:Conduit]->(b)\r\n"
					+ "WITH b\r\n"
					+ "Match (dep:Station {nom : $stationdep}), (arr:Station {nom : $stationarrive}) "
					+ "MERGE (b)-[:Depart {idVoyage : $id, heureDepart : $heuredepart}]->(dep)\r\n"
					+ "MERGE (b)-[:Arrive {idVoyage : $id, heureArrive: $heurearrive}]->(arr)");
			parameters = Values.parameters("num", v.getBus().getNumBus(),"nb",v.getBus().getNbPlacesLimite(),
					"matricule",v.getBus().getConducteur().getMatricule(),
					"id",v.getIdVoyage(),"heuredepart",v.getHeureDepart().format(formatter),"stationdep",v.getDepart().getNom(),
					"stationarrive",v.getArrive().getNom(),"heurearrive",v.getHeureArrive().format(formatter));
					
	        tx.run(query, parameters);
			
			
			for(Arret a:v.getArrets())
			{
				query = new String("MATCH(b:Bus {numBus : $num}), (s:Station {nom : $stationarr}) "
						+ "WITH b,s "
						+ "MERGE (b)-[:Arrete {idVoyage : $id, heureArret: $heurearret}]->(s)");
				parameters = Values.parameters("num",v.getBus().getNumBus(),"id",v.getIdVoyage(),
						"heurearret",a.getHeureArret().format(formatter),"stationarr",a.getArrêt().getNom());
		        tx.run(query, parameters);
			}
			
	        tx.commit();
			session.close();
			return true;
			 

		}
		catch(Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete(Voyage v)
	{
		Transaction tx = null;
		String query;
		Value parameters;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		
		try
		{
			Session session =Connexion.getDriver().session();
			tx = session.beginTransaction();
			//supprimer les anciennes relations
			query = new String("MATCH (c:Conducteur)-[r:Conduit]-(b:Bus)-[:Depart {idVoyage : $id}]->(:Station)\r\n"
					+ "delete r");
			parameters = Values.parameters("id",v.getIdVoyage());
			tx.run(query, parameters);
			
			query = new String("MATCH (b:Bus {numBus : $num}), (b)-[r]->(:Station)\r\n"
					+"WHERE r.idVoyage = $id "
					+ "Delete r");
			parameters = Values.parameters("num",v.getBus().getNumBus(),"id",v.getIdVoyage());
					
	        tx.run(query, parameters);
	        
	        tx.commit();
			session.close();
			return true;
			 

		}
		catch(Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return false;
		}
	}
	
	public Collection<Voyage> retrieve()
	{
	    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


		Collection<Voyage> voyages = new ArrayList<>();
		Query query;
		Value parameters;
		
		try
		{
			Session session =Connexion.getDriver().session();
			//recuperer les voyages
			query = new Query( "MATCH ()-[r:Depart]->() RETURN DISTINCT r.idVoyage AS idVoyage");
			Result result = session.run(query);
			
            while (result.hasNext()) {
            	String query2;
            	Result result2;
            	Voyage voyage = new Voyage();
                org.neo4j.driver.Record record = result.next();
                int idVoyage = record.get("idVoyage").asInt();
                voyage.setIdVoyage(idVoyage);
                
                //depart
                query2 = "MATCH ()-[r:Depart {idVoyage : $id}]->(s:Station) RETURN r.heureDepart AS heuredep, s.nom as station";
                parameters = Values.parameters("id",idVoyage);
                result2 = session.run(query2,parameters);
                
                // Parcourir les résultats
                if (result2.hasNext()) 
                {
                    org.neo4j.driver.Record record2 = result2.next();
                    String heuredep = record2.get("heuredep").asString();
                    String station = record2.get("station").asString();
                    Station s = new Station();
                    s.setNom(station);
                    voyage.setDepart(s);
                    voyage.setHeureDepart(LocalTime.parse(heuredep));     
                }
                
                query2 = "MATCH ()-[r:Arrive {idVoyage : $id}]->(s:Station) RETURN r.heureArrive AS heurearr, s.nom as station\r\n";
                parameters = Values.parameters("id",idVoyage);
                result2 = session.run(query2,parameters);
                
                // arrive
                if (result2.hasNext()) 
                {
                    org.neo4j.driver.Record record2 = result2.next();
                    String heurearr = record2.get("heurearr").asString();
                    String station = record2.get("station").asString();
                    Station s = new Station();
                    s.setNom(station);
                    voyage.setArrive(s);
                    voyage.setHeureArrive(LocalTime.parse(heurearr));     
                }
                
                //arrets
                query2 = "MATCH ()-[r:Arrete {idVoyage : $id}]->(s:Station) RETURN r.heureArret AS heurearret, s.nom as station\r\n";
                parameters = Values.parameters("id",idVoyage);
                result2 = session.run(query2,parameters);
                
                parameters = Values.parameters("id",idVoyage);
                result2 = session.run(query2,parameters);
                
                while (result2.hasNext()) 
                {
                    org.neo4j.driver.Record record2 = result2.next();
                    String heurearr = record2.get("heurearret").asString();
                    String station = record2.get("station").asString();
                    Arret a = new Arret();
                    a.setArrêt(new Station(station));
                    a.setHeureArret(LocalTime.parse(heurearr));
                    a.setVoyage(voyage);
                    voyage.getArrets().add(a);
                      
                }
                voyage.setDisponible(true);
                int numBus = 0;
                
                query2 = ( "MATCH (c:Conducteur)-[r:Conduit]-(b:Bus)-[:Depart {idVoyage : $id}]->(:Station) return c.nom as nomC, c.prenom as prenomC, "
                		+ "b.numBus as numbus, b.nbPlacesLimite as nbplaces");
                parameters = Values.parameters("id", idVoyage);
                result2 = session.run(query2,parameters);
                
                if (result2.hasNext()) 
                {
                    org.neo4j.driver.Record record2 = result2.next();
                    String nom = record2.get("nomC").asString();
                    String prenom = record2.get("prenomC").asString();
                    int num = numBus =record2.get("numbus").asInt();
                    int nbplaces = record2.get("nbplaces").asInt();
                    Conducteur c =new Conducteur();
                    c.setNom(nom);
                    c.setPrenom(prenom);
                    Bus b = new Bus();
                    b.setNbPlacesLimite(nbplaces);
                    b.setNumBus(num);
                    b.setConducteur(c);
                    voyage.setBus(b);
                }
                
              //reservation
                query2 = "match(b:Bus{ numBus : $num}),(p:Passager)-[r:Reservation]->(b) return p.nom as nom, p.prenom as prenom, r.dateReserv as date, r.From as from, r.To as to";
                parameters = Values.parameters("num",numBus);
                result2 = session.run(query2,parameters);
                
                
                while (result2.hasNext()) 
                {
                    org.neo4j.driver.Record record2 = result2.next();
                    String nom = record2.get("nom").asString();
                    String prenom = record2.get("prenom").asString();
                    String date= record2.get("date").asString();
                    String from = record2.get("from").asString();
                    String to = record2.get("to").asString();
                    
                    Reservation r = new Reservation();
                    r.setPassager(new Passager(nom, prenom));
                    r.setDateReserv(LocalDateTime.parse(date, formatter));
                    r.setFrom(new Station(from));
                    r.setTo(new Station(to));
                    voyage.getReservations().add(r);
                      
                }
                
                voyages.add(voyage);
            }
            session.close();
            return voyages;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	public ArrayList<Integer> getIdvoyagesPassePar(String stat1,String stat2,LocalTime heure)
	{
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
		ArrayList<Integer> idvoyages1 = new ArrayList<Integer>();
		ArrayList<Integer> idvoyages2 = new ArrayList<Integer>();
		

		Collection<Voyage> voyages = new ArrayList<>();
		String query;
		Value parameters;
		Result result;
		
		
		try
		{
			Session session =Connexion.getDriver().session();
			//voyages qui font depart de stat1
			query = "MATCH (start:Station {nom: $stat1})-[:NEXT*]->(end:Station {nom: $stat2})\r\n"
					+ "MATCH (:Bus)-[dep:Depart]->(start)\r\n"
					+ "WHERE dep.heureDepart >= $heure \r\n"
					+ "RETURN DISTINCT dep.idVoyage AS depIdVoyage";
			parameters = Values.parameters("stat1", stat1,"stat2",stat2,"heure",heure.format(format));
			result = session.run(query,parameters);
			
            while (result.hasNext()) {
                org.neo4j.driver.Record record = result.next();
                int id = record.get("depIdVoyage").asInt();
                idvoyages1.add(id);
                
                
                }
            //voyages qui font arret à stat1 et heurearret superieur à heure
            query = "MATCH (start:Station {nom: $stat1})-[:NEXT*]->(end:Station {nom: $stat2})\r\n"
            		+ "MATCH (:Bus)-[arr:Arrete]->(start)\r\n"
            		+ "WHERE arr.heureArret >= $heure \r\n"
            		+ "RETURN DISTINCT arr.idVoyage AS arrIdVoyage";
			parameters = Values.parameters("stat1", stat1,"stat2",stat2,"heure",heure.format(format));
			result = session.run(query,parameters);
			
            while (result.hasNext()) {
                org.neo4j.driver.Record record = result.next();
                int id = record.get("arrIdVoyage").asInt();
                idvoyages1.add(id);
                
                
                }
            
            //voyages qui font arret à stat2
            query = "MATCH (start:Station {nom: $stat1})-[:NEXT*]->(end:Station {nom: $stat2})\r\n"
            		+ "MATCH (:Bus)-[arr2:Arrete]->(end)\r\n"
            		+ "RETURN DISTINCT arr2.idVoyage AS arr2IdVoyage";
			parameters = Values.parameters("stat1", stat1,"stat2",stat2);
			result = session.run(query,parameters);
			
            while (result.hasNext()) {
                org.neo4j.driver.Record record = result.next();
                int id = record.get("arr2IdVoyage").asInt();
                idvoyages2.add(id);
                
                
                }
            
            //voyages qui arrive à stat2
            query = "MATCH (start:Station {nom: $stat1})-[:NEXT*]->(end:Station {nom: $stat2})\r\n"
            		+ "MATCH (:Bus)-[arrive:Arrive]-(end)\r\n"
            		+ "RETURN DISTINCT arrive.idVoyage AS arriveIdVoyage";
			parameters = Values.parameters("stat1", stat1,"stat2",stat2);
			result = session.run(query,parameters);
			
            while (result.hasNext()) {
                org.neo4j.driver.Record record = result.next();
                int id = record.get("arriveIdVoyage").asInt();
                idvoyages2.add(id);
                
                
                }
              
            //intersection
            idvoyages1.retainAll(idvoyages2);
            return idvoyages1;
               
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	public int getNombrePassagerMonte(String station, int numBus) {
		int nb = 0;
		
		String query;
		Value parameters;
		Result result;
		try
		{
			Session session =Connexion.getDriver().session();
			//voyages qui font depart de stat1
			query = "MATCH(s:Station {nom: $station })\r\n"
					+ "WITH s\r\n"
					+ "MATCH (p:Passager)-[:Monter {numBus: $nb}]->(s)\r\n"
					+ "RETURN COUNT(p) AS Nombre";
			parameters = Values.parameters("station", station,"nb",numBus);
			result = session.run(query,parameters);
			
            if (result.hasNext()) {
                org.neo4j.driver.Record record = result.next();
                nb = record.get("Nombre").asInt();                
                }
            return nb;
               
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getNombrePassagerDescendre(String station, int numBus) {
		int nb = 0;
		
		String query;
		Value parameters;
		Result result;
		try
		{
			Session session =Connexion.getDriver().session();
			//voyages qui font depart de stat1
			query = "MATCH(s:Station {nom: $station })\r\n"
					+ "WITH s\r\n"
					+ "MATCH (p:Passager)-[:Descendre {numBus: $nb}]->(s)\r\n"
					+ "RETURN COUNT(p) AS Nombre";
			parameters = Values.parameters("station", station,"nb",numBus);
			result = session.run(query,parameters);
			
            if (result.hasNext()) {
                org.neo4j.driver.Record record = result.next();
                nb = record.get("Nombre").asInt();                
                }
            return nb;
               
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getNombrePassagerParTrajet(String from, String to,int numBus) {
		int nb = 0;
		
		String query;
		Value parameters;
		Result result;
		try
		{
			Session session =Connexion.getDriver().session();
			//voyages qui font depart de stat1
			query = "MATCH (b:Bus {numBus : $num}),(p:Passager)-[r:Reservation]->(b)\r\n"
					+ "WHERE r.From = $from AND r.To = $to\r\n"
					+ "RETURN COUNT(r) AS count";
			parameters = Values.parameters("num", numBus,"from",from,"to",to);
			result = session.run(query,parameters);
			
            if (result.hasNext()) {
                org.neo4j.driver.Record record = result.next();
                nb = record.get("count").asInt();                
                }
            return nb;
               
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	
	
	public static void main(String[] args) {
		System.out.println(new VoyageDAO().getNombrePassagerParTrajet("A", "B", 1));
		
	}
}
