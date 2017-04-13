package com.karat.servlets.admin;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.karat.jpamodel.Category;
import com.karat.jpamodel.Product;
import com.karat.models.RouteManager;
import com.karat.sessionbeans.CategoryController;
import com.karat.sessionbeans.PricePolicyController;
import com.karat.sessionbeans.ProductionController;

/**
 * Servlet implementation class CategoryController
 */
@WebServlet(name="CategoryManagement", urlPatterns={"/admin/category_management/*"})
public class CategoryManagement extends HttpServlet {
	private static final Logger log = Logger.getLogger(CategoryManagement.class);
	
	private static final long serialVersionUID = 1L;
       
	
	@EJB
	private CategoryController categoryController;
	@EJB
	private PricePolicyController pricePolController;
	@EJB
	private ProductionController productController;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoryManagement() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RouteManager routeManager = new RouteManager(request.getRequestURI());
			if (routeManager.getRouteElementsAmount() == 2) {
				List<Category> categories = categoryController.getAllMainCategories();
				request.setAttribute("parent_category_id", null);
				request.setAttribute("categories", categories);
				request.getRequestDispatcher("/jsp/admin/category_management.jsp").forward(request, response);
			} else if ( routeManager.getRouteElementsAmount() == 3 && !routeManager.getRouteElement(3).equals("add-category")) {
				Integer parentCatId = Integer.valueOf(routeManager.getRouteElement(3));
				List<Category> childCategories = categoryController.getChildCategories(parentCatId);
				
				if (childCategories.size() > 0) {
					request.setAttribute("parent_category_id", parentCatId);
					request.setAttribute("categories", childCategories);
					request.getRequestDispatcher("/jsp/admin/category_management.jsp").forward(request, response);
				} else {
					List<Product> products = productController.getProductsByCategoryId(parentCatId);
					if (products != null && products.size() > 0) {
						
						request.setAttribute("category_id", parentCatId);
						request.setAttribute("products", products);
						request.getRequestDispatcher("/jsp/admin/product_management.jsp").forward(request, response);
					} else {
						Category category = categoryController.getCategoryById(parentCatId);
						request.setAttribute("category", category);
						request.getRequestDispatcher("/jsp/admin/create_category_or_product.jsp").forward(request, response);
					}
				}
			} else if ( routeManager.getRouteElement(3).equals("add-category")) {
					Integer categoryId = ( routeManager.getRouteElement(4) == null) ? null : Integer.valueOf(routeManager.getRouteElement(4));
					
					request.setAttribute("category_id", categoryId);
					request.getRequestDispatcher("/jsp/admin/create_category.jsp").forward(request, response);
			}
		} catch (Exception e) {
			log.error(e);
			response.sendRedirect("/admin/category_management");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RouteManager routeManager = new RouteManager(request.getRequestURI());
			if (routeManager.getRouteElementsAmount() == 3) {
				if ( routeManager.getRouteElement(3).equals("change-name")) {
					Integer categoryId = Integer.valueOf(request.getParameter("category_id"));
					String categoryName = request.getParameter("category_name");
					
					Category category = categoryController.getCategoryById(categoryId);
					category.setName(categoryName);
					
					categoryController.updateCategory(category);
					
					Category parentCat = categoryController.getParentCategoryById(categoryId);
					if (parentCat != null)
						response.sendRedirect("/admin/category_management/"+parentCat.getId());
					else
						response.sendRedirect("/admin/category_management/");
				} else if ( routeManager.getRouteElement(3).equals("delete")) {
					Integer categoryId = Integer.valueOf(request.getParameter("category_id"));
					categoryController.removeCategory(categoryId);
					response.sendRedirect("/admin/category_management/");
				} else if (routeManager.getRouteElement(3).equals("create-category")) {
					Integer categoryId = (request.getParameter("category_id").equals("")) ? null : Integer.valueOf(request.getParameter("category_id"));
					String categoryName = request.getParameter("category_name");
					
					Category category = null;
					if (categoryId != null)
						category = categoryController.getCategoryById(categoryId);
					
					if (categoryName == null || categoryName.equals(""))
						throw new Exception("No valid params");
					
					Category newCategory = new Category();
					if (categoryId !=null)
						newCategory.setParentId(categoryId);
					newCategory.setName(categoryName);
					
					categoryController.insertCategory(newCategory);
					
					response.sendRedirect("/admin/category_management/"+categoryId);
				}
			}
		} catch (Exception e) {
			log.error(e);
			response.sendRedirect("/admin/category_management");
		}
	}

}
