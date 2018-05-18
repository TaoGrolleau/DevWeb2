package clientleger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Connexion
 */
@WebServlet("/Connexion")
public class Connexion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Connexion() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String pseudo = (String) session.getAttribute("pseudo");

		if ((boolean) session.getAttribute("auth") == true) {
			/*
			 * L'utilisateur a un compte enregistré sur la base de données et les infos
			 * correspondent. Il faut le rediriger vers l'accueil utilisateur.
			 */

			ArrayList<String[]> files = new ArrayList<String[]>();
			try {
				files = basededonnees.Request.user_file(pseudo);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			ArrayList<ArrayList<String>> permissions = new ArrayList<ArrayList<String>>();
			
			for(int i=0; i < files.size(); i++) {
				try {
					permissions.add(basededonnees.Request.list_user(Integer.parseInt(files.get(i)[0])));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			getServletContext().setAttribute("liste", files);
			getServletContext().setAttribute("permissions", permissions);

			request.getServletContext().getRequestDispatcher("/WEB-INF/accueilUser.jsp").forward(request, response);

		} else {
			response.sendRedirect("index.html");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pseudo = request.getParameter("pseudo");
		String mdp = request.getParameter("mdp");

		try {
			boolean check_user = basededonnees.Request.check_user(pseudo, mdp);

			if (check_user) {
				/*
				 * L'utilisateur a un compte enregistré sur la base de données et les infos
				 * correspondent. Il faut le rediriger vers l'accueil utilisateur.
				 */

				HttpSession session = request.getSession();
				session.setAttribute("pseudo", pseudo);
				session.setAttribute("auth", true);

				ArrayList<String[]> files = basededonnees.Request.user_file(pseudo);
				
				ArrayList<ArrayList<String>> permissions = new ArrayList<ArrayList<String>>();
				
				for(int i=0; i < files.size(); i++) {
					permissions.add(basededonnees.Request.list_user(Integer.parseInt(files.get(i)[0])));
				}

				getServletContext().setAttribute("liste", files);
				getServletContext().setAttribute("permissions", permissions);

				request.getServletContext().getRequestDispatcher("/WEB-INF/accueilUser.jsp").forward(request, response);

			} else {
				response.sendRedirect("index.html");
				/*
				 * L'utilisateur n'a pas de données correspondantes dans la base de données. Il
				 * est donc redirigé vers une page d'inscription.
				 */
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
