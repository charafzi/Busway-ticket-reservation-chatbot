package project.sysResTicketbw.controleur;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jakarta.servlet.RequestDispatcher;
import project.sysResTicketbw.dto.VoyageDTO;

/**
 * Servlet implementation class AfficherInfoVoy
 */
public class AfficherInfoVoy extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Controleur c= new Controleur();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AfficherInfoVoy() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Collection<VoyageDTO> lesvoyages = c.getVoyages();
        request.getSession().setAttribute("Voyages", lesvoyages);

        javax.servlet.RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("Display_InfoRes.jsp");
        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
