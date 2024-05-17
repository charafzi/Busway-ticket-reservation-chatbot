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

import project.sysResTicketbw.bo.Bus;
import project.sysResTicketbw.bo.Conducteur;

public class BusDAO {
	
	public boolean save(Bus b)
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
			query = new String("MERGE (b:Bus {numBus: $num, nbPlacesLimite : $nb}) RETURN b;");
			parameters = Values.parameters("num",b.getNumBus(),"nb", b.getNbPlacesLimite());
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
	
	public boolean update(Bus b)
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
			query = new String("MATCH(b:Bus {numBus : $num})\r\n"
					+ "set b.nbPlacesLimite = $nb\r\n"
					+ "return b;");
			parameters = Values.parameters("num",b.getNumBus(),"nb", b.getNbPlacesLimite());
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
	
	public boolean delete(Bus b)
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
			query = new String("MATCH(b:Bus {numBus: $num})\r\n"
					+ "DETACH \r\n"
					+ "DELETE b;");
			parameters = Values.parameters("num",b.getNumBus());
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
	
	public Collection<Bus> retrieve()
	{
		Session session = null;
		try
		{
			Collection<Bus> lesbus = new HashSet<>();
			Query query = new Query("MATCH (n:Bus) RETURN (n)");
			session = Connexion.getDriver().session();
			Result result = session.run(query);
			
			while (result.hasNext()) {
	        	 
	        	 
	             org.neo4j.driver.Record record = result.next();
	            
	             Node node = record.get("n").asNode();
	          
	             int num = node.get("numBus").asInt();
	             int nbplaceslimites = node.get("nbPlacesLimite").asInt();
	             Bus bus = new Bus();
	             bus.setNumBus(num);
	             bus.setNbPlacesLimite(nbplaceslimites);
	             lesbus.add(bus);
	         }
			 session.close();
			 return lesbus;

		}
		catch(Exception e)
		{
			session.close();
			e.printStackTrace();
			return null;
		}
		
	}

}
