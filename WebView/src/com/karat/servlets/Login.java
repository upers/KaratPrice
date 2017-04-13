package com.karat.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.karat.jpamodel.User;
import com.karat.verify.ErrorMessage;

/**
 * Servlet implementation class Login
 */
@WebServlet(urlPatterns={"/login", "/signin", "/signout"}, name="Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB
    private com.karat.sessionbeans.UserController userControl;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ( request.getRequestURI().equals("/signin") ) {
			return;
		} else if ( request.getRequestURI().equals("/signout")) {
			HttpSession session = ((HttpServletRequest) request).getSession();
			session.setAttribute("user", null);
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		
		User user = new User();
		user.setLogin(request.getParameter("login"));
		user.setPassword(request.getParameter("password"));
		ErrorMessage em = new ErrorMessage();
		
		if ( userControl.verify(user, em) ) {
			session.setAttribute("user", user);
			System.out.println("---------------user was saved in session with login: " + user.getLogin());
			if ( !userControl.isAdmin(user))
				response.sendRedirect("/user_controller");
			else
				response.sendRedirect("/user_controller");
//				response.sendRedirect("/admin/user_management");
		} else {
			request.setAttribute("ErrorMessage", em.getMsg());
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
		}
	}

}
