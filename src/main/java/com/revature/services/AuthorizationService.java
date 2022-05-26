package com.revature.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.NotLoggedInException;
import com.revature.models.Role;
import com.revature.models.User;

@Service
public class AuthorizationService {

	@Autowired
	private HttpServletRequest req;
	
	// The ID that we pass in represents the ID of the User resource that is being manipulated
	public void guardByUserId(int userId) {
		
		HttpSession session = req.getSession(false);
		// Get the session associated with this request
		// Don't create one if there wasn't any
		
		// If there is no session OR if a session exists, but no one is logged in
		if(session == null || session.getAttribute("currentUser") == null) {
			throw new NotLoggedInException("Must be logged in to perform this action");
		}
		
		// After this point, this means there is a User that is logged in
		
		User currentUser = (User) session.getAttribute("currentUser");
		
		if(userId != currentUser.getId()) {
			// The resource that is being manipulated does not belong to the currently
			// logged in User
			
			// Maybe perform extra logic to still allow Admins to manipulate other User's resources
			if(currentUser.getRole() != Role.ADMIN) {
				throw new NotAuthorizedException("You are not permitted to perform this action on this resource");
			}
		}
	}
}
