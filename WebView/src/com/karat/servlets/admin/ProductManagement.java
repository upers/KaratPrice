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
 * Servlet implementation class ProductManagement
 */
@WebServlet(name="ProductManagement", urlPatterns={"/admin/product_management/*"})
public class ProductManagement extends HttpServlet {
	private static final Logger log = Logger.getLogger(ProductManagement.class);
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
    public ProductManagement() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RouteManager routeManager = new RouteManager(request.getRequestURI());
			log.info(routeManager.getRouteElementsAmount() + "<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			if (routeManager.getRouteElementsAmount() == 4) {
				 if (routeManager.getRouteElement(3).equals("add-product")) {
					 Integer categoryId = Integer.valueOf(routeManager.getRouteElement(4));
					 
					request.setAttribute("category_id", categoryId);
					request.getRequestDispatcher("/jsp/admin/create_product.jsp").forward(request, response);
				 }
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
					Integer productId = Integer.valueOf(request.getParameter("product_id"));
					String productName = request.getParameter("product_name");
					Double productPrice = Double.valueOf(request.getParameter("product_price"));
					
					Product product = productController.getProductById(productId);
					product.setName(productName);
					product.setPrice(productPrice);
					
					productController.updateProduct(product);
					Category parentCat = productController.getCategoryByProductId(productId);
					if (parentCat != null)
						response.sendRedirect("/admin/category_management/"+parentCat.getId());
					else
						response.sendRedirect("/admin/category_management/");
				} else if ( routeManager.getRouteElement(3).equals("delete")) {
					Integer productId = Integer.valueOf(request.getParameter("product_id"));
					productController.removeProduct(productId);
					
					Category parentCat = productController.getCategoryByProductId(productId);
					if (parentCat != null)
						response.sendRedirect("/admin/product_management/"+parentCat.getId());
					else
						response.sendRedirect("/admin/category_management/");
				} else if (routeManager.getRouteElement(3).equals("create-product")) {
					Integer categoryId = Integer.valueOf(request.getParameter("category_id"));
					String productName = request.getParameter("product_name");
					Double productPrice = Double.valueOf(request.getParameter("product_price"));
					
					Category category = categoryController.getCategoryById(categoryId);
					
					if (category == null || productName == null || productName.equals(""))
						throw new Exception("No valid params");
					
					Product newProduct = new Product();
					newProduct.setCategory(category);
					newProduct.setName(productName);
					newProduct.setPrice(productPrice);
					
					productController.mergeProduct(newProduct);
					
					response.sendRedirect("/admin/category_management/"+categoryId);
				}
			}
		} catch (Exception e) {
			log.error(e);
			response.sendRedirect("/admin/category_management");
		}
	}

}
