package com.formulafund.portfolio.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MyErrorController implements ErrorController {

	@RequestMapping(value="/error", method = RequestMethod.GET)
	public String renderErrorPage(HttpServletRequest httpRequest, Model model) {

		String errorMsg = "";
		int httpErrorCode = getErrorCode(httpRequest);
		log.info("ErrorController::renderErrorPage --> error code: " + httpErrorCode);
		switch (httpErrorCode) {
			case 400: {
				errorMsg = "Http Error Code: 400. Bad Request";
				break;
			}
			case 401: {
				errorMsg = "Http Error Code: 401. Unauthorized";
				break;
			}
			case 404: {
				errorMsg = "Http Error Code: 404. Resource not found";
				break;
			}
			case 500: {
				errorMsg = "Http Error Code: 500. Internal Server Error";
				break;
			}
		}
		model.addAttribute("resultmessage", errorMsg);
		return "simple";

	}

	private int getErrorCode(HttpServletRequest httpRequest) {
		return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
