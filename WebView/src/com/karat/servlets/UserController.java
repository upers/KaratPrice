package com.karat.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.karat.jpamodel.Category;
import com.karat.jpamodel.User;
import com.karat.models.RouteManager;
import com.karat.sessionbeans.CategoryController;
import com.karat.sessionbeans.ProductionController;
import com.karat.verify.ErrorMessage;

/**
 * Servlet implementation class UserController
 */
@WebServlet(name="UserController", urlPatterns={"/user_controller/*"})
public class UserController extends HttpServlet {
	private static final Logger log = Logger.getLogger(UserController.class);
	private static final long serialVersionUID = 1L;
    
	@EJB
	private CategoryController categoryController;
	@EJB
	private ProductionController productController;
	@EJB
	private com.karat.sessionbeans.UserController userController;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info(request.getRequestURI() + ">>>>>>>>>>>>>>>>>>>>");
		RouteManager routeManager = new RouteManager(request.getRequestURI());
		User user = (User)request.getSession().getAttribute("user");
		
		if (routeManager.getRouteElementsAmount() == 1) {
			log.info("amount 1");
			List<Category> categories = categoryController.getAllMainCategories();
			request.setAttribute("parent_category_id", null);
			request.setAttribute("categories", categories);
			request.getRequestDispatcher("/jsp/category.jsp").forward(request, response);
			
		} else if (routeManager.getRouteElementsAmount() == 2) {
			log.info("amount 2");
			Integer parentCatId = Integer.valueOf(routeManager.getRouteElement(2));
			List<Category> childCategories = categoryController.getChildCategories(parentCatId);
			
			if (childCategories.size() > 0) {
				log.info("child present");
				request.setAttribute("parent_category_id", parentCatId);
				request.setAttribute("categories", childCategories);
				request.getRequestDispatcher("/jsp/category.jsp").forward(request, response);
			} else {
				user = userController.getUserByLogin(user.getLogin(), new ErrorMessage());
				log.info(user.getId() + "       " +  parentCatId);
				Map<String, Double> products = productController.getProductsForUser(user.getId(), parentCatId);
				if (products != null && products.size() > 0) {
					log.info("product present ");					
					request.setAttribute("category_id", parentCatId);
					request.setAttribute("products", products);
					request.getRequestDispatcher("/jsp/product.jsp").forward(request, response);
				} else {
					log.info("redirect product not present ");
					response.sendRedirect("/user_controller");
				}
			}
		} else {
			response.sendRedirect("/user_controller");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
