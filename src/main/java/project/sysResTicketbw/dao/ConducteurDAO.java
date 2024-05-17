package project.sysResTicketbw.dao;

import java.util.Collection;
import java.util.HashSet;

import org.apache.batik.util.Service;
import org.neo4j.driver.Query;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;
import org.neo4j.driver.types.Node;

import project.sysResTicketbw.bo.Bus;
import project.sysResTicketbw.bo.Conducteur;
import project.sysResTicketbw.bo.Station;
import project.sysResTicketbw.service.CatalogueBus;

public class ConducteurDAO {
	
	public boolean save(Conducteur c)
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
			query = new String("MERGE (c:Conducteur {matricule : $matricule, nom : $nom, prenom : $prenom}) RETURN c;");
			parameters = Values.parameters("matricule",c.getMatricule(),"nom", c.getNom(),"prenom",c.getPrenom());
	        tx.run(query, parameters);
	        
	        for(Bus b:c.getBus())
	        {
	        	 query = new String("MATCH (c:Conducteur {matricule: $matricule}), (b:Bus {numBus: $num}) CREATE (c)-[:Conduit]->(b)\r\n;");
	 			parameters = Values.parameters("matricule",c.getMatricule(),"num",b.getNumBus());
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
	
	public boolean update(Conducteur c)
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
			query = new String("MATCH(c:Conducteur {matricule : $matricule})\r\n"
					+ "set c.nom = $nom,\r\n"
					+ "c.prenom = $prenom\r\n"
					+ "return c;");
			parameters = Values.parameters("matricule",c.getMatricule(),"nom", c.getNom(),"prenom",c.getPrenom());
	        tx.run(query, parameters);
	        
	        query = new String("MATCH (c:Conducteur {matricule: $matricule})-[r]-() DELETE r");
			parameters = Values.parameters("matricule",c.getMatricule());
	        tx.run(query, parameters);
	        
	        for(Bus b:c.getBus())
	        {
	        	System.out.println("****UPDATE**"+b.getNumBus());
	        	 query = new String("MATCH (c:Conducteur {matricule: $matricule}), (b:Bus {numBus: $num}) CREATE (c)-[:Conduit]->(b)\r\n;");
	 			parameters = Values.parameters("matricule",c.getMatricule(),"num",b.getNumBus());
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
	
	public boolean delete(Conducteur c)
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
			query = new String("MATCH(c:Conducteur {matricule : $matricule})\r\n"
					+ "DETACH \r\n"
					+ "DELETE c");
			parameters = Values.parameters("matricule",c.getMatricule());
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

	public Collection<Conducteur> retrieve()
	{
		Transaction tx = null;
		Session session = null;;
		try
		{
			Collection<Conducteur> conducteurs = new HashSet<>();
			Query query = new Query("MATCH (n:Conducteur) RETURN (n)");
			session = Connexion.getDriver().session();
			Result result = session.run(query);
			tx = session.beginTransaction();
			
			while (result.hasNext()) {
	        	 
	        	 
	             org.neo4j.driver.Record record = result.next();
	            
	             Node node = record.get("n").asNode();
	          
	             String matricule = node.get("matricule").asString();
	             String nom = node.get("nom").asString();
	             String prenom = node.get("prenom").asString();
	             Conducteur c = new Conducteur(matricule, nom, prenom);
	             
	             String query2 = "MATCH (c:Conducteur {matricule : $mat})-[:Conduit]->(b:Bus) return b.numBus as numbus";
	             Value parameters = Values.parameters("mat",c.getMatricule());
	            
	 			 Result result2 = tx.run(query2, parameters);
	 			
	 			while (result2.hasNext()) {
	 	        	 
	 	        	 
	 	             org.neo4j.driver.Record record2 = result2.next();
	 	            
	 	             int num = record2.get("numbus").asInt();
	 	          
	 	             c.ajouterBus(new Bus(num));
	 	         }
	             
	             
	             conducteurs.add(c);
	         }
			
			tx.commit();
			session.close();
			return conducteurs;

		}
		catch(Exception e)
		{
			tx.rollback();
			session.close();
			
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void main(String[] args) {
		
		for(Conducteur c: new ConducteurDAO().retrieve())
		{
			System.out.println("Matricule : "+c.getMatricule());
			System.out.println("Nom complet : "+c.getFullName());
			for(Bus b:c.getBus())
			{
				System.out.println(b.getNumBus());
			}
			System.out.println("*********************");
		}
	}

}
