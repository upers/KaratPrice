package com.karat.filters;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import com.karat.jpamodel.User;
import com.karat.sessionbeans.UserController;

/**
 * Servlet Filter implementation class DenyNotAuthorizationAccess
 */
@WebFilter(urlPatterns="*", filterName="DenyNotAuthorizationAccess")
public class DenyNotAuthorizationAccess implements Filter {
	private static final Logger log = Logger.getLogger(DenyNotAuthorizationAccess.class);
	@EJB
	UserController userControl;
    /**
     * Default constructor. 
     */
    public DenyNotAuthorizationAccess() {
    	log.info("---------constructor   DenyNotAuthorizationAccess ------------");
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		log.info("---------destroy   DenyNotAuthorizationAccess ------------");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String inUrl = ((HttpServletRequest) request).getRequestURI();
		if ( inUrl.endsWith(".css")) {
			
		} else if ( session.getAttribute("user") == null && !inUrl.equals("/login") &&!inUrl.equals("/signin")) {
			log.info("send redirect");
			((HttpServletResponse) response).sendRedirect("/login");
			return;
		}
		
		if ( inUrl.startsWith("/admin") ) {
			User user = (User)session.getAttribute("user");
			if (  !userControl.isAdmin(user)) {
				((HttpServletResponse) response).sendRedirect("/user_controller");
				return;
			}
		}
			
		if (inUrl.equals("/")) {
			((HttpServletResponse) response).sendRedirect("/user_controller");
			return;
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
