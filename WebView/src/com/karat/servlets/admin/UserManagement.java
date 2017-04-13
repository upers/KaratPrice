package com.karat.servlets.admin;

import java.io.IOException;
import java.util.List;
import java.util.logging.LogManager;

import javax.ejb.EJB;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.karat.jpamodel.Category;
import com.karat.jpamodel.PricePolicy;
import com.karat.jpamodel.Product;
import com.karat.jpamodel.User;
import com.karat.models.CategoryMarkup;
import com.karat.models.ProductMarkup;
import com.karat.models.RouteManager;
import com.karat.sessionbeans.CategoryController;
import com.karat.sessionbeans.PricePolicyController;
import com.karat.sessionbeans.ProductionController;

/**
 * Servlet implementation class UserManagement
 */
@WebServlet(name="UserManagement", urlPatterns={"/admin/user_management/*"})
public class UserManagement extends HttpServlet {
	private static final Logger log = Logger.getLogger(UserManagement.class);
	
	private static final long serialVersionUID = 1L;
	@EJB
    private com.karat.sessionbeans.UserController userControllerLocal;
	@EJB
	private CategoryController categoryController;
	@EJB
	private PricePolicyController pricePolController;
	@EJB
	private ProductionController productController;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserManagement() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RouteManager routeManager = new RouteManager(request.getRequestURI());
		
		if (routeManager.getRouteElementsAmount() == 2) {
			List<User> allUsers = userControllerLocal.getAllUsers();
			request.setAttribute("all_users", allUsers);
			request.getRequestDispatcher("/jsp/admin/select_user.jsp").forward(request, response);
		} else if (routeManager.getRouteElementsAmount() == 3) {
			String strIdVal = routeManager.getRouteElement(3);
			
			try {
				int userId = Integer.valueOf(strIdVal);
				User user = userControllerLocal.getUserByIdWithDependencies(userId);
				if (user == null) 
					throw new Exception("No such user with id: " + userId);
				
				List<CategoryMarkup> categories = categoryController.getMainCategoriesMarkupByUserId(userId);
				
				request.setAttribute("user", user);
				request.setAttribute("categories", categories);
				request.getRequestDispatcher("/jsp/admin/user_management.jsp").forward(request, response);
			} catch (Exception e) {
				log.error(e);
				response.sendRedirect("/admin/user_management");
			}
		} else if (routeManager.getRouteElementsAmount() == 4) {
			String strIdVal = routeManager.getRouteElement(3);
			String strCatIdVal = routeManager.getRouteElement(4);
			try {
				Integer userId = Integer.valueOf(strIdVal);
				User user = userControllerLocal.getUserByIdWithDependencies(userId);
				if (user == null) 
					throw new Exception("No such user with id: " + userId);
				
				Integer categoryId = Integer.valueOf(strCatIdVal);
				if ( categoryController.getChildCategories(categoryId).size() > 0 ) {
					List<CategoryMarkup> categories = categoryController.getChildCategoriesMarkup(categoryId, userId);
					request.setAttribute("user", user);
					request.setAttribute("categories", categories);
					request.getRequestDispatcher("/jsp/admin/user_management.jsp").forward(request, response);
				} else {
					List<ProductMarkup> products = productController.getProductsWithMurkupByUserAndCategId(userId, categoryId);
					
					request.setAttribute("user", user);
					request.setAttribute("products", products);
					request.getRequestDispatcher("/jsp/admin/user_management_production.jsp").forward(request, response);
				}
			} catch (Exception e) {
				log.error(e);
				response.sendRedirect("/admin/user_management");
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("-------POST---- USERMANAGER----method---------------");
		RouteManager routeManager = new RouteManager(request.getRequestURI());
		if (routeManager.getRouteElementsAmount() < 3) {
			response.sendRedirect("/admin/user_management");
			return;
		}
		try {
			if (routeManager.getRouteElement(3).equals("category")) {
				Integer userId = Integer.valueOf(request.getParameter("user_id"));
				Integer categoryId = Integer.valueOf(request.getParameter("category_id"));
				Double categoryMarkup = Double.valueOf(request.getParameter("category_markup"));
				if (categoryMarkup < 0) {
					response.sendRedirect("/admin/user_management");
					return;
				}
				
				PricePolicy pp = pricePolController.getPricePolByCategUserId(userId, categoryId);
				if ( pp == null ) {
					pp = new PricePolicy();
					User user = userControllerLocal.getUserById(userId);
					Category category = categoryController.getCategoryById(categoryId);
					if (user == null || category == null) {
						response.sendRedirect("/admin/user_management");
						return;
					}
					
					pp.setUser(user);
					pp.setCategory(category);
				}
				pp.setCategoryMurkupPercent(categoryMarkup);
				
				pricePolController.mergePricePol(pp);
				response.sendRedirect("/admin/user_management/" + userId);
			} else if (routeManager.getRouteElement(3).equals("product")) { 
				Integer userId = Integer.valueOf(request.getParameter("user_id"));
				Integer productId = Integer.valueOf(request.getParameter("product_id"));
				Double productMarkup = Double.valueOf(request.getParameter("product_markup"));
				if (productMarkup < 0) {
					response.sendRedirect("/admin/user_management");
					return;
				}
				
				PricePolicy pp = pricePolController.getPricePolByProductId(userId, productId);
				if ( pp == null ) {
					pp = new PricePolicy();
					User user = userControllerLocal.getUserById(userId);
					Product product  = productController.getProductById(productId);
					if (user == null || product == null) {
						response.sendRedirect("/admin/user_management");
						return;
					}
					
					pp.setUser(user);
					pp.setProduct(product);
				}
				pp.setProductMarkup__productMarckupPercent(productMarkup);
				pricePolController.mergePricePol(pp);
				
				Category prodCategory = productController.getCategoryByProductId(productId);
				response.sendRedirect("/admin/user_management/" + userId + "/" + prodCategory.getId());
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

}
