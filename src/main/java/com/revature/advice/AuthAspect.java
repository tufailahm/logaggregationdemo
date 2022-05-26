package com.revature.advice;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.annotations.Authorized;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.NotLoggedInException;
import com.revature.models.Role;
import com.revature.models.User;

@Aspect
@Component
public class AuthAspect {

	@Autowired // This will autowire a proxy object, since the HttpServletRequest uses
	// one of the Web-Aware bean scopes
	// Specifically, the Request Scope
	// This injection occurs when this AuthAspect is instantiated
	// And since this class has bean scope Singleton (default)
	// This AuthAspect class will be instantiated BEFORE we receive ANY requests
	// Spring will inject a proxy Request object
	// And when used, this proxy object refers back to the relevant actual request object
	private HttpServletRequest req;
	
	// There are several kinds of advice: Before, After, and Around
	// After also has 2 subtypes: AfterReturning & AfterThrowing
	// Each of these has an annotation
	// Around advice is considered the most powerful, but it is also the most complex
	
	@Around("@annotation(authorized)") // PointCut
	// PointCut = Expression for a collection of methods that we will target for injection
	// We will inject advice around any method with the @Authorized annotation
	// Some popular PointCut Expressions: @annotation, within, execute
	public Object authenticate(ProceedingJoinPoint pjp, Authorized authorized) throws Throwable {
		
		HttpSession session = req.getSession(false);
		// Get the session associated with this request
		// Don't create one if there wasn't any
		
		// If there is no session OR if a session exists, but no one is logged in
		if(session == null || session.getAttribute("currentUser") == null) {
			throw new NotLoggedInException("Must be logged in to perform this action");
		}
		
		// After this point, this means there is a User that is logged in
		
		User currentUser = (User) session.getAttribute("currentUser");
		// This cast is potentially dangerous
		// Make sure that anything you put in the currentUser attribute is of type User
		Role currentRole = currentUser.getRole();
		
		// Get the list of allowed roles from the annotation
		List<Role> allowedRoles = Arrays.asList(authorized.allowedRoles());
		
		if(!allowedRoles.contains(currentRole)) {
			// The User is logged in, but their Role does not match
			// the list of allowed roles
			
			throw new NotAuthorizedException("You are not authorized to perform this action");
		}
		
		// After this point, the User is logged in AND they are authorized to perform this action
		
		return pjp.proceed(pjp.getArgs());
	}
}
