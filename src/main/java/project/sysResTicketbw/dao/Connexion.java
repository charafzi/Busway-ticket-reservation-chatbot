package project.sysResTicketbw.dao;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Connexion {
	private static Connexion instance;
	private static Driver driver;
	
	private Connexion()
	{
		
	}
	
	public static Connexion getInstance()
	{
		if(instance==null)
			instance = new Connexion();
		return instance;
	}
	
	public static Driver getDriver()
	{
		if(driver==null)
		{
			try
			{
				driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4j123"));
				driver.verifyConnectivity();
				return driver;
		    }
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}  
		    
		}
		return driver;
	    
	}
	
	public static void main(String[] args) {
		 System.out.println(Connexion.getInstance().getDriver().session());
		 

         
		
	}

}
