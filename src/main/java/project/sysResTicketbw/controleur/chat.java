package project.sysResTicketbw.controleur;
import java.io.File;
import java.time.LocalTime;

import org.alicebot.ab.*;
import org.alicebot.ab.utils.*;

import project.sysResTicketbw.dto.StationDTO;
import project.sysResTicketbw.service.CatalogueStation;
public class chat {
	
	
	private static final boolean TRACE_MODE=false;
	
	public static void main(String args[])
	{
		try {
			String resourcepath=getpath();
			MagicBooleans.trace_mode=TRACE_MODE;
			Bot b=new Bot("super",resourcepath);
			Chat chatsession=new Chat(b);
			String textline="";
			
			
			while(true)
			{
				System.out.println("YOU : ");
				textline=IOUtils.readInputTextLine();
				
				if(textline==null || textline.length()<1)
				{
					textline=MagicStrings.null_input;
					
				}
				else if(textline.equals("q"))
				{
					System.exit(0);
				}
				else if(textline.equals("wq"))
				{
					b.writeQuit();
					System.exit(0);
				}
				else
				{
					String request=textline;
					String response=chatsession.multisentenceRespond(request);
					
					String[] action = response.split(",");
					if(action.length > 1)
					{
						if(action[0].equals("BUSDISPO"))
						{
							System.out.println("-->" + action[1]);
							System.out.println("-->");
						
							String dep = CatalogueStation.getInstance().findStationByAdresse(action[1]).getNom();
							String arr = CatalogueStation.getInstance().findStationByAdresse(action[2]).getNom();
							
							String buses = Controleur.voyagesDisponibleString(dep, arr, LocalTime.of(6, 0));
							response ="Les numeros des bus diponibles : "+ buses;
						}
						else if(action[0].equals("RESERVER"))
						{
							String[] nom = action[1].split(" ");
							
							System.out.println(nom[0]+" "+nom[1]);
							System.out.println(Integer.parseInt(action[4].strip()));
							System.out.println(action[2]);
							System.out.println(action[3]);
							String dep = CatalogueStation.getInstance().findStationByAdresse(action[2]).getNom();
							String arr = CatalogueStation.getInstance().findStationByAdresse(action[3]).getNom();
							boolean res = new Controleur().reservation(Integer.parseInt(action[4].strip()),nom[0],nom[1],dep,arr,LocalTime.of(6, 0));
							if(res==true)
								response = "Reservation bien effectué !";
							else
								response="Un erreur est sourvenue s'il vous plaît répeter les étapes précedentes à nouveau";
						}
						
					}
					System.out.println("BOT :"+response);
				}
	
			}
		}
		catch (Exception e){
			
		}
	}
	private static String getpath()
	{
		File currd=new File(".");
		String path=currd.getAbsolutePath();
		String resourcepath=path + File.separator +"src" + File.separator +"main" +File.separator +"resources";
		return resourcepath;
	}
}