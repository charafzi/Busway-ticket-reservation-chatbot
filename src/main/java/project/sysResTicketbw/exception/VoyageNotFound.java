package project.sysResTicketbw.exception;


public class VoyageNotFound extends Exception{
	
	@Override
	public String toString() {
		return "Le voyage n existe pas";
	}

}
