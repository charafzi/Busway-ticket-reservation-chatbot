package project.sysResTicketbw.dao;

import java.time.format.DateTimeFormatter;

import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import project.sysResTicketbw.bo.Reservation;
public class ReservationDAO {
	
	public boolean save(Reservation res)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		Session session = null;
		Transaction tx = null;
		String query;
		Value parameters;
		
		try
		{
			session = Connexion.getDriver().session();
			tx = session.beginTransaction();
			
			query = new String("MATCH (b:Bus {numBus: $num})\r\n"
					+ "WITH b\r\n"
					+ "CREATE (p:Passager {nom: $nom, prenom: $prenom})-[:Reservation {dateReserv: $date, From : $from, To : $to}]->(b)\r\n");
			
			parameters = Values.parameters("nom", res.getPassager().getNom(),"prenom",res.getPassager().getPrenom(),
					"date",res.getDateReserv().format(formatter),"from",res.getFrom().getNom(),"to",res.getTo().getNom(),"num",res.getVoyage().getBus().getNumBus());
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
	
	public static void main(String[] args) {
		
		
	}

}
