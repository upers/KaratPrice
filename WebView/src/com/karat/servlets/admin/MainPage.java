package com.karat.servlets.admin;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.karat.jpamodel.User;
import com.karat.sessionbeans.UserController;

/**
 * Servlet implementation class MainPage
 */
@WebServlet("/admin")
public class MainPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB
    private UserController userController;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("admin main page");
		List<User> allUsers = userController.getAllUsers();
		request.setAttribute("all_users", allUsers);
		request.getRequestDispatcher("/jsp/admin/select_user.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
