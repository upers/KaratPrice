package com.karat.filters;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.jboss.logging.Logger;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter(urlPatterns="*", filterName="EncodingFilter")
public class EncodingFilter implements Filter {
	private static final Logger log = Logger.getLogger(EncodingFilter.class);

    /**
     * Default constructor. 
     */
    public EncodingFilter() {
    	log.info("---------constructor   EncodingFilter ------------");
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		log.info("---------destroy   EncodingFilter ------------");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		log.info("Encoding set:" + request.getCharacterEncoding());
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		log.info("---------init   EncodingFilter ------------");
	}

}
