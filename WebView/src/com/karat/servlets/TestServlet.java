package com.karat.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.karat.jpamodel.User;
import com.karat.sessionbeans.TestConnection;
import com.karat.verify.ErrorMessage;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/test_db")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB
    private TestConnection testDbConnect;
	@EJB
	private com.karat.sessionbeans.UserController controllerLocal;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		String[] res  = testDbConnect.testSelectFrom();
		PrintWriter pw = response.getWriter();
		pw.print("<html>");
		
		if (res != null) {
			for (String el : res)
				pw.print(el + "<br/>");
		}
		pw.println("---------------------------------<br/>");
		ErrorMessage em = new ErrorMessage();
		if ( !controllerLocal.verify(new User(), em) ) {
			pw.print(em.getMsg()+ "<br/>");
		} else {
			pw.print("Ok!" + "<br/>");
		}
		pw.print("</html>");
		pw.close();
	}

}
