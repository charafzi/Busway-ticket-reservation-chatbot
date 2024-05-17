package project.sysResTicketbw.exception;

public class ContrainteNbrPlace extends Exception{
	@Override
	public String toString() {
		return "Erreur lors de la modification du bus : nombre de places vides supérieur au nombre de places limite";
	}
	

}
