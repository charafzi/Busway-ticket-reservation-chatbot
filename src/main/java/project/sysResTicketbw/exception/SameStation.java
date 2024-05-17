package project.sysResTicketbw.exception;

public class SameStation extends Exception
{
	@Override
	public String toString() {
		return "Impossible Station Départ c'est le Station d'arrivé !!!";
	}
}
