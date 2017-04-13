package com.karat.servlets.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.jboss.logging.Logger;

import com.karat.models.RouteManager;
import com.karat.sessionbeans.ExcelController;

/**
 * Servlet implementation class ExcelManagement
 */
@WebServlet(name="ExcelManagement", urlPatterns="/admin/excel/*")
@MultipartConfig(location="/tmp", fileSizeThreshold=1024*1024, 
maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class ExcelManagement extends HttpServlet {
	private static final Logger log = Logger.getLogger(ExcelManagement.class);
	private static final long serialVersionUID = 1L;
	private static final int BYTE_BUFFER = 1024;
	@EJB
	private ExcelController excelController;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExcelManagement() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RouteManager routeManager = new RouteManager(request.getRequestURI());
    	if (routeManager.getRouteElement(3).equals("update_product_price")) {
    		request.setAttribute("header", "Загрузка цен из Excel документа");
    		request.setAttribute("link", "/admin/excel/update_product_price");
    		request.getRequestDispatcher("/jsp/admin/excel_upload.jsp").forward(request, response);
    	} else {
    		response.sendRedirect("/user_controller");
    	}
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RouteManager routeManager = new RouteManager(request.getRequestURI());
		if (routeManager.getRouteElement(3).equals("update_product_price")) {
			Part part = request.getPart("excel_price");
			InputStream in = part.getInputStream();
			byte[] buffer = new byte[BYTE_BUFFER];
			File tempExcel = File.createTempFile("temp", Long.toString(System.nanoTime()));
			log.info("temp excel file: " + tempExcel.getAbsolutePath());
			FileOutputStream out = new FileOutputStream(tempExcel);
			while (in.read(buffer) != -1) {
				out.write(buffer);
			}
			out.close();
			
			Future<Boolean> future = excelController.updateProductsPrice(tempExcel);
			try {
				future.get(10000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				log.error(e);
			} catch (ExecutionException e) {
				log.error(e);
			} catch (TimeoutException e) {
				log.error(e);
			}
		} else {
			response.sendRedirect("/admin");
		}
	}

}
