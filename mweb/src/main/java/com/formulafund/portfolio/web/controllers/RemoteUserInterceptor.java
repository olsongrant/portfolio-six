package com.formulafund.portfolio.web.controllers;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoteUserInterceptor implements HandlerInterceptor {
	
	private static final Class expectedPrincipalClass = UsernamePasswordAuthenticationToken.class;
	
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler)
     throws Exception {
		log.info("RemoteUserInterceptor::preHandle invoked");
		Principal principal = request.getUserPrincipal();
		if (principal != null) {
			log.info("Principal class: " + principal.getClass().getName());
		}
		return true;
	}
	
	@Override
	public void postHandle(
	  HttpServletRequest req, 
	  HttpServletResponse res,
	  Object o, 
	  ModelAndView model) throws Exception {
	    log.info("RemoteUserInterceptor::postHandle invoked");
	    if (model != null && !isRedirectView(model)) {
	        if (isUserLogged(req)) {
	        addToModelUserDetails(model);
	    }
	    }
	}

	public static boolean isRedirectView(ModelAndView mv) {
	    String viewName = mv.getViewName();
	    if (viewName.startsWith("redirect:/")) {
	        return true;
	    }
	    View view = mv.getView();
	    return (view != null && view instanceof SmartView
	      && ((SmartView) view).isRedirectView());
	}
	
	public static boolean isUserLogged(HttpServletRequest request) {
	    try {
			Principal principal = request.getUserPrincipal();
			if (principal == null) return false;
	    	boolean isAnonymousUser = !SecurityContextHolder.getContext().getAuthentication()
	  	          .getName().equals("anonymousUser");
	    	boolean isRightClass = false;
			log.info("Principal class: " + principal.getClass().getName());
			isRightClass = RemoteUserInterceptor.expectedPrincipalClass.equals(principal.getClass());
	        return (isAnonymousUser && isRightClass);
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	private void addToModelUserDetails(HttpSession session) {
	    log.info("=============== addToModelUserDetails =========================");
	    
	    String loggedUsername 
	      = SecurityContextHolder.getContext().getAuthentication().getName();
	    session.setAttribute("username", loggedUsername);
	    
	    log.info("user(" + loggedUsername + ") session : " + session);
	    log.info("=============== addToModelUserDetails =========================");
	}
	
	private void addToModelUserDetails(ModelAndView model) {
	    log.info("=============== addToModelUserDetails =========================");
	    
	    String loggedUsername = SecurityContextHolder.getContext()
	      .getAuthentication().getName();
	    log.info("logged in username: " + loggedUsername);
	    model.addObject("username", loggedUsername);
	    
	    log.trace("session : " + model.getModel());
	    log.info("=============== addToModelUserDetails =========================");
	}
}
