package com.karat.servlets.admin;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.karat.jpamodel.User;
import com.karat.verify.ErrorMessage;

/**
 * Servlet implementation class Regestration
 */
@WebServlet(urlPatterns={"/admin/registration"}, name="Registration")
public class Registration extends HttpServlet {
	private static final Logger log = Logger.getLogger(Registration.class);
	private static final long serialVersionUID = 1L;
    @EJB
	private com.karat.sessionbeans.UserController userController;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Registration() {
        super();
        System.out.println("-----------Registration Servlet Constructor-----------------------");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/jsp/admin/registration.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User newUser = new User();
		ErrorMessage errMsg = new ErrorMessage();
		
		if ( !request.getParameter("password").equals(request.getParameter("password2")) ) {
			log.info("-------------------password is not eqals---------------------");
			request.setAttribute("ErrorMessage", "Пароли не совпадают");
			request.getRequestDispatcher("/jsp/admin/registration.jsp").forward(request, response);
			return;
		}
		
		log.info(request.getParameter("login") + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		newUser.setName(request.getParameter("name"));
		newUser.setSurname(request.getParameter("surname"));
		newUser.setLogin(request.getParameter("login"));
		newUser.setEmail(request.getParameter("email"));
		newUser.setPassword(request.getParameter("password"));
		newUser.setCompany(request.getParameter("company"));
		
		if ( !userController.regestrationUser(newUser, errMsg)) {
			System.out.println("----------------User registration fail---------------------");
			request.setAttribute("ErrorMessage", errMsg.getMsg());
			request.getRequestDispatcher("/jsp/admin/registration.jsp").forward(request, response);
		}
		
		response.sendRedirect("/admin");
	}

}

