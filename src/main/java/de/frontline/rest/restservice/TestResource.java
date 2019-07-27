package de.frontline.rest.restservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.tom.service.dto.AddressDTO;

import de.frontline.backend.bl.HelloTestLocal;
import de.frontline.persistence.entities.FlUser;
import de.frontline.persistence.dao.DbException;
import de.frontline.persistence.dao.UserDAOLocal;

/**
 * Example resource class hosted at the URI path "/test"
 */

@Path("/test1")
@Stateless
public class TestResource {

	/**
	 * Vom Container iniziert
	 */
	// @EJB(name="test",beanName="HelloTest")
	@EJB
	HelloTestLocal helloTestLocal;
	// @EJB(name="userdao",beanName="UserDAO")
	@EJB
	UserDAOLocal userDao;

	private final static Logger LOGGER = Logger.getLogger(TestResource.class
			.getName());

	/**
	 * Method processing HTTP GET requests, producing MIME media type.
	 * 
	 * @return String that will be send back as a response of type
	 *         text/plain","text/xml","application/json.
	 */
	@GET
	@Path("/hello")
	@Produces({ MediaType.TEXT_PLAIN})
	public String getIt() {
		return "Hallo Welt mit statischen Text";
	}

	/**
	 * Method processing HTTP GET requests, producing MIME media type.
	 * 
	 * @return String that will be send back as a response of type
	 *         text/plain","text/xml","application/json.
	 */
	@GET
	@Path("/ejbhello")
	@Produces({ MediaType.TEXT_PLAIN })
	public String sayhello() {
		return helloTestLocal.sayHello();
	}

	/**
	 * Method processing HTTP GET requests, producing "text/xml" or
	 * "application/json" type.
	 * 
	 * @return String that will be send back as a response of type "text/xml" or
	 *         "application/json".
	 */
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public AddressDTO getAdresse() {
		AddressDTO adr = new AddressDTO();
		adr.setId(2);
		adr.setPlz("82110");
		adr.setStadt("Germering");
		return adr;
	}

	/**
	 * Method processing HTTP GET requests, producing "text/xml" or
	 * "application/json" type.
	 * 
	 * @return String that will be send back as a response of type "text/xml" or
	 *         "application/json".
	 */
	@POST
	@Path("/create")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON)
	public AddressDTO createUser(AddressDTO user) {
		if (user == null) {
			LOGGER.log(Level.SEVERE, "User  == null");
			throw new IllegalRestClientArgumentException("User  == null");
		}

		FlUser flu = new FlUser();
		flu.setEmail(user.getEmail());
		// TODO
		// flu.setPassword(user.getPasswort());
		try {
			flu = userDao.createFlUser(flu);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fehler=", e);
			throw new IllegalRestClientArgumentException(e.getMessage());
		}
		LOGGER.info("create FlUser=" + flu);

		user.setId(flu.getId());
		try {
			userDao.storeUservars(user);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fehler=", e);
			throw new IllegalRestClientArgumentException(e.getMessage());
		}
		return user;
	}

	/**
	 * Method processing HTTP GET requests, producing "text/xml" or
	 * "application/json" type.
	 * 
	 * @return String that will be send back as a response of type "text/xml" or
	 *         "application/json".
	 */
	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON)
	public AddressDTO updateUser(AddressDTO user) {
		if (user == null || user.getId() == 0) {
			LOGGER.log(Level.SEVERE, "User == null oder User.Id == 0");
			throw new IllegalRestClientArgumentException(
					"User == null oder User.Id == 0");
		}
		try {
			user = userDao.updateUser(user);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * Method processing HTTP GET requests, producing "text/xml" or
	 * "application/json" type.
	 * 
	 * @return String that will be send back as a response of type "text/xml" or
	 *         "application/json".
	 */
	@GET
	@Path("/jpa")
	@Produces(MediaType.APPLICATION_JSON)
	public String testJPA() {
		Long count = userDao.countUser();
		Long countVar = userDao.countUservar();
		return "Hallo es gibt " + Long.toString(count)
				+ " User Datensätze und " + Long.toString(countVar)
				+ " Uservar Datensätze";

	}

	/**
	 * Method processing HTTP GET requests, producing "text/xml" or
	 * "application/json" type.
	 * 
	 * @return String that will be send back as a response of type "text/xml" or
	 *         "application/json".
	 */
	@GET
	@Path("/getbyemail")
	@Produces(MediaType.APPLICATION_JSON)
	public AddressDTO getUserByEmail(@QueryParam("email") String email) {
		List<FlUser> res = null;
		try {
			res = userDao.findUserByEmail(email);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res.size() == 0) {
			throw new IllegalRestClientArgumentException(email
					+ " nicht gefunden");
		}

		FlUser user = res.get(0);
		LOGGER.info("User=" + user);
		AddressDTO adr = null;
		try {
			adr = userDao.genAddressDTO(user);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("User mit FlUser=" + adr);
		return adr;
	}

}
