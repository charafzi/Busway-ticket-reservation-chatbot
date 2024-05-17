package project.sysResTicketbw.dao;
import java.util.Collection;
import java.util.HashSet;

import org.neo4j.driver.Query;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;
import org.neo4j.driver.types.Node;

import project.sysResTicketbw.bo.Arret;
import project.sysResTicketbw.bo.Station;
import project.sysResTicketbw.service.CatalogueStation;


public class StationDAO {
	
	public boolean save(Station s)
	{
		Transaction tx = null;
		String query;
		Value parameters;
		
		try
		{
			Session session = Connexion.getDriver().session();
			tx = session.beginTransaction();
			
			//// construire le noeud voyage 
			//merge : si le noeud existe pas il ne le crée pas , sinon il le créee
			query = new String("MERGE (s:Station {nom : $nom, adresse : $adresse, longitude : $longitude, latitude : $latitude, endStation : $end})"
					+ "RETURN s;");
			parameters = Values.parameters("nom", s.getNom(),"adresse",s.getAdresse(),
					"longitude",s.getLongitude(),"latitude",s.getLatitude(),"end",s.getEndStation());
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
	
	public boolean update(Station s)
	{
		Transaction tx = null;
		String query;
		Value parameters;
		
		try
		{
			Session session = Connexion.getDriver().session();
			tx = session.beginTransaction();
			//merge : si le noeud existe pas il ne le crée pas , sinon il le créee
			query = new String("MATCH(s:Station {nom : $nom})\r\n"
					+ "set s.adresse = $adresse,\r\n"
					+ "s.longitude = $longitude,\r\n"
					+ "s.latitude = $latitude,\r\n"
					+ "s.endStation = $end"
					+ "return s");
			parameters = Values.parameters("nom", s.getNom(),"adresse",s.getAdresse(),
					"longitude",s.getLongitude(),"latitude",s.getLatitude(),
					"end",s.getEndStation());
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
	
	public boolean delete(Station s)
	{
		Transaction tx = null;
		String query;
		Value parameters;
		
		try
		{
			Session session = Connexion.getDriver().session();
			tx = session.beginTransaction();
			
			//merge : si le noeud existe pas il ne le crée pas , sinon il le créee
			query = new String("MATCH(s:Station {nom : $nom})\r\n"
					+ "DETACH \r\n"
					+ "DELETE s");
			parameters = Values.parameters("nom", s.getNom());
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
	
	public Collection<Station> retrieve()
	{
		Session session = Connexion.getDriver().session();
		try
		{
			Collection<Station> stations = new HashSet<>();
			Query query = new Query("MATCH (s:Station) RETURN (s)");
			Result result = session.run(query);
			
			while (result.hasNext()) {
	        	 
	        	 
	             org.neo4j.driver.Record record = result.next();
	            
	             Node node = record.get("s").asNode();
	          
	             String nom = node.get("nom").asString();
	             String adresse = node.get("adresse").asString();
	             double longitude = node.get("longitude").asDouble();
	             double latitude = node.get("latitude").asDouble();
	             int end = node.get("endStation").asInt();
	             Station station = new Station(nom, adresse, longitude, latitude,end);
	             stations.add(station);
	         }
			session.close();
			 return stations;
		}
		catch(Exception e)
		{
			session.close();
			e.printStackTrace();
			return null;
		}
		
	}
	
	public int nombrePassagersDepart(String nom, int idVoyage)
	{
		int nbDepart = 0;
		Session session = null;
		try
		{
			session = Connexion.getDriver().session();
			Query query = new Query("MATCH(s:Station{ nom : \""+nom+"\"})\r\n"
					+ "WITH s "
					+ "MATCH(:Reservation {fromStation : s.nom})-[r:CONCERNE]-(v:Voyage { idVoyage : "+idVoyage+"}) "
					+ "RETURN  COUNT(r) as nbDeparts");
			
			
			Result result = session.run(query);
			
			if(result.hasNext()) {
	        	 
	             org.neo4j.driver.Record record = result.next();
	             if (record.containsKey("nbDeparts")) 
	             {
	                 nbDepart = record.get("nbDeparts").asInt();
	             } else 
	             {
	                 nbDepart = -1;
	             }
	         }
			session.close();
			return nbDepart;

		}
		catch(Exception e)
		{
			session.close();
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public int nombrePassagersArrive(String nom, int idVoyage)
	{
		int nbArrive = 0;
		Session session = null;
		try
		{
			session = Connexion.getDriver().session();
			Query query = new Query("MATCH(s:Station{ nom : \""+nom+"\"})\r\n"
					+ "WITH s "
					+ "MATCH(:Reservation {toStation : s.nom})-[r:CONCERNE]-(v:Voyage { idVoyage : "+idVoyage+"}) "
					+ "RETURN  COUNT(r) as nbDeparts");
			
			
			Result result = session.run(query);
			
			if(result.hasNext()) {
	        	 
	             org.neo4j.driver.Record record = result.next();
	             if (record.containsKey("nbDeparts")) 
	             {
	            	 nbArrive = record.get("nbDeparts").asInt();
	             } else 
	             {
	            	 nbArrive = -1;
	             }
	         }
			session.close();
			return nbArrive;

		}
		catch(Exception e)
		{
			session.close();
			e.printStackTrace();
			return -1;
		}
		
	}
	public static void main(String[] args) {
		
		
	}
	
	

}
