package de.frontline.rest.restservice;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.tom.service.dto.AddressDTO;
import com.tom.service.dto.AddressListResponse;
import com.tom.service.dto.RegisterAddressRequest;

import de.frontline.backend.bf.UserBFLocal;

/**
 * <h1>User Resource</h1>
 * <b>path: /us </b><br/>
 * 
 * User Rest service, supplies various service for the user management
 * @author gfr
 * 
 */
@Path("/us")
@Stateless
public class UserResource {

	/**
	 * User Business facade
	 */
	@EJB
	UserBFLocal userBf;

	private final Logger logger = Logger
			.getLogger(UserResource.class.getName());

	/**
   * 
   * <h1>create user service</h1><br/>
   * <b>Path: /create</b><br/>
   * 
	 * Creates a User Entity in the DB and returns the AddressDTO wit generated
	 * ID Method processing HTTP POST requests, producing "text/xml" or
	 * "application/json" type.

   * @param req RegisterAddressRequest
	 * @return AddressDTO the created AddressDTO
   * 
	 */
	@POST
	@Path("/create")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON)
	public AddressDTO createUser(RegisterAddressRequest req) {
		if (req == null || req.getAddress() == null
				|| req.getAddress().getEmail() == null || req.getPw() == null) {
			logger.error("Adresse, Email aoder Passwort nicht angegeben");
			throw new IllegalRestClientArgumentException(
					"Adresse, Email aoder Passwort nicht angegeben");
		}
		if (req.getAddress().getEmail().length() < 5) {
			logger.error("Emailadresse nicht zulässig");
			throw new IllegalRestClientArgumentException(
					"Emailadresse nicht zulässig");
		}
		if (req.getPw().length() < 5) {
			logger.error("Passwort nicht zulässig, mehr als 5 Zeichen erforderlich");
			throw new IllegalRestClientArgumentException(
					"Passwort nicht zulässig, mehr als 5 Zeichen erforderlich");
		}

		try {
			AddressDTO res = userBf.createUser(req.getAddress(), req.getPw());
			return res;
		} catch (Exception e) {
			logger.error("createUser: " + e.getMessage(), e);
			throw new RestServerException(e.getMessage());
		}
	}

	/**
   * <h1>update user service</h1><br/>
   * <b>Path: /update</b><br/>
   * 
	 * Updates a AddressDTO Method processing HTTP POST requests, producing
	 * "text/xml" or "application/json" type.

	 * @param AddressDTO the address to be updated
	 * @return AddressDTO the updated address
	 */
	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON)
	public AddressDTO updateUser(AddressDTO user) throws RestServerException {
		if (user == null || user.getId() == 0) {
			logger.error("User == null oder User.Id == 0");
			throw new IllegalRestClientArgumentException(
					"User == null oder User.Id == 0");
		}
		try {
			AddressDTO res = userBf.updateUser(user);
			return res;
		} catch (Exception e) {
			logger.error("updateUser: " + e.getMessage(), e);
			throw new RestServerException(e.getMessage());
		}
	}

	/**
   * <h1>get user by email service</h1><br/>
   * <b>Path: /getbyemail</b><br/>
   * 
	 * Query user by emailadr Method processing HTTP GET requests, producing
	 * "text/xml" or "application/json" type.
	 * 
	 * @param email String Email
	 * @return AddressListResponse alist of Addresses
	 * @throws RestServerException
	 */
	@GET
	@Path("/getbyemail")
	@Produces(MediaType.APPLICATION_JSON)
	public AddressListResponse getUserByEmail(@QueryParam("email") String email) {
		if (email == null || email.length() == 0) {
			logger.error("email == null oder email.length == 0");
			throw new IllegalRestClientArgumentException(
					"email == null oder email.length == 0");
		}
		try {
			List<AddressDTO> res = userBf.getUserByEmail(email);
			return new AddressListResponse(res);
		} catch (Exception e) {
			logger.error("getUserByEmail: " + e.getMessage(), e);
			throw new RestServerException(e.getMessage());
		}
	}

	/**
   * <h1>get user by email abd password service</h1><br/>
   * <b>Path: /getbyemailandpw</b><br/>
   * 
	 * Query user by emailadr and Pw Method processing HTTP GET requests,
	 * producing "text/xml" or "application/json" type.
   * 
	 * @param email String email adr
	 * @param pw String password
	 * @return AddressListResponse alist of Addresses
	 * @throws RestServerException
	 */
	@GET
	@Path("/getbyemailandpw")
	@Produces(MediaType.APPLICATION_JSON)
	public AddressListResponse getUserByEmailAndPw(
			@QueryParam("email") String email, @QueryParam("pw") String pw) {
		if (email == null || email.length() == 0) {
			logger.error("email == null oder email.length == 0");
			throw new IllegalRestClientArgumentException(
					"email == null oder email.length == 0");
		}
		try {
			List<AddressDTO> res = userBf.getUserByEmailAndPassword(email, pw);
			return new AddressListResponse(res);
		} catch (Exception e) {
			logger.error("getUserByEmailAndPw: " + e.getMessage(), e);
			throw new RestServerException(e.getMessage());
		}
	}

	/**
   * <h1>count user service</h1><br/>
   * <b>Path: /countusers</b><br/>
   * 
	 * Returns the userCount in the database Method processing HTTP GET
	 * requests, producing "text/xml" or "application/json" type.
   * 
	 * @return Long the actual user count
	 */
	@GET
	@Path("/countusers")
	@Produces(MediaType.APPLICATION_JSON)
	public Long countUsers() {
		try {
			Long count = userBf.countUsers();
			return count;
		} catch (Exception e) {
			logger.error("countUsers: " + e.getMessage(), e);
			throw new RestServerException(e.getMessage());
		}

	}

}
