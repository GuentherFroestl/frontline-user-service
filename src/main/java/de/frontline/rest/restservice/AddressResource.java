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
import com.tom.service.dto.AddressResponse;
import com.tom.service.dto.BaseResponse;
import com.tom.service.dto.LoginRequest;
import com.tom.service.dto.LoginResponse;
import com.tom.service.dto.PasswordResetRequest;
import com.tom.service.dto.PasswordResetResponse;
import com.tom.service.dto.RegisterAddressRequest;
import com.tom.service.dto.SearchByStringRequest;
import com.tom.service.facade.TomException;
import com.tom.service.rest.AdressAndUserPathes;
import com.tom.service.util.EmailAddressValidator;

import de.frontline.backend.bf.AddressAndUserSFLocal;
import de.frontline.backend.bf.UserBFLocal;

/**
 * RestService für TomAgency Adressen und User
 */
@Path(AdressAndUserPathes.PATH)
@Stateless
public class AddressResource extends AbstractResource {

	private final Logger logger = Logger.getLogger(AddressResource.class);

	/**
	 * for address queries
	 */
	@EJB
	AddressAndUserSFLocal adrSf;
	/**
	 * User Business facade
	 */
	@EJB
	UserBFLocal userBf;

	private static final String CHARSET = ";charset=UTF-8";

	/**
	 * Search addresse by String, Method processing HTTP GET requests, producing
	 * "text/plain" MIME media type.
	 * 
	 * @return AddressSearchResponse
	 */
	@GET
	@Path(AdressAndUserPathes.SEARCH)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON + CHARSET)
	public AddressListResponse searchAddresses(
			@QueryParam("searchstring") String searchString,
			@QueryParam("limit") String limit, @QueryParam("start") String start) {
		try {
			int limitValue = Integer.parseInt(limit);
			int startValue = Integer.parseInt(start);
      SearchByStringRequest req = new SearchByStringRequest();
      req.setSearchString(searchString);
      req.setStartRecord(startValue);
      req.setLimit(limitValue);
			List<AddressDTO> li = adrSf.searchAddresses(req);
			AddressListResponse res = new AddressListResponse(li);
			return res;
		} catch (TomException e) {
			AddressListResponse res = new AddressListResponse();
			convertAndlogErrorSetBaseResponse(e, res, "searchAddresses()", 1,
					logger);
			return res;
		}
	}

	/**
	 * load Address by ID, Method processing HTTP GET requests, producing
	 * "text/plain" MIME media type.
	 * 
	 * @return AddressResponse
	 */
	@GET
	@Path(AdressAndUserPathes.LOAD_BY_ID)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON + CHARSET)
	public AddressResponse loadAddressById(@QueryParam("id") String id) {
		try {
			AddressDTO adr = adrSf.loadAddressById(id);
			AddressResponse res = new AddressResponse(adr);
			return res;
		} catch (TomException e) {
			AddressResponse res = new AddressResponse();
			convertAndlogErrorSetBaseResponse(e, res, "loadAddressById()", 1,
					logger);
			return res;
		}
	}

	/**
	 * load Address by UUID, Method processing HTTP GET requests, producing
	 * "text/plain" MIME media type.
	 * 
	 * @return AddressResponse
	 */
	@GET
	@Path(AdressAndUserPathes.LOAD_BY_UUID)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON + CHARSET)
	public AddressResponse loadAddressByUuid(@QueryParam("uuid") String uuid) {
		try {
			AddressDTO adr = adrSf.loadAddressByUuid(uuid);
			AddressResponse res = new AddressResponse(adr);
			return res;
		} catch (TomException e) {
			logger.error("Fehler bei searchAddresses() " + e.getMessage(), e);
			AddressResponse res = new AddressResponse();
			convertAndlogErrorSetBaseResponse(e, res, "loadAddressByUuid()", 1,
					logger);
			return res;
		}
	}

	/**
	 * Creates a Address, Method processing HTTP POST requests, producing
	 * "application/json" type.
	 * 
	 * @param adr
	 *            AddressDTO
	 * @return AddressDTO
	 */
	@POST
	@Path(AdressAndUserPathes.CREATE)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON + CHARSET)
	public AddressResponse createAddress(AddressDTO adr) {

		try {
			AddressDTO resAdr = adrSf.createAddress(adr);
			AddressResponse res = new AddressResponse(resAdr);
			return res;
		} catch (TomException e) {
			AddressResponse res = new AddressResponse();
			convertAndlogErrorSetBaseResponse(e, res, "createAddress()", 1,
					logger);
			return res;
		}
	}

	/**
	 * Creates a Address, Method processing HTTP POST requests, producing
	 * "application/json" type.
	 * 
	 * @param adr
	 *            AddressDTO
	 * @return AddressDTO
	 */
	@POST
	@Path(AdressAndUserPathes.UPDATE)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON + CHARSET)
	public AddressResponse updateAddress(AddressDTO adr) {

		try {
			AddressDTO resAdr = adrSf.updateAddress(adr);
			AddressResponse res = new AddressResponse(resAdr);
			return res;
		} catch (TomException e) {
			AddressResponse res = new AddressResponse();
			convertAndlogErrorSetBaseResponse(e, res, "updateAddress()", 1,
					logger);
			return res;
		}
	}

	/**
	 * Deletes a Address, Method processing HTTP POST requests, producing
	 * "application/json" type.
	 * 
	 * @param adr
	 *            AddressDTO
	 * @return AddressDTO
	 */
	@POST
	@Path(AdressAndUserPathes.DELETE)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON + CHARSET)
	public BaseResponse deleteAddress(AddressDTO adr) {

		try {
			adrSf.deleteAddress(adr);
			return new BaseResponse();
		} catch (TomException e) {
			BaseResponse res = new BaseResponse();
			convertAndlogErrorSetBaseResponse(e, res, "deleteAddress()", 1,
					logger);
			return res;
		}
	}

	/**
	 * Creates a Address, Method processing HTTP POST requests, producing
	 * "application/json" type.
	 * 
	 * @param adr
	 *            AddressDTO
	 * @return AddressDTO
	 */
	@POST
	@Path(AdressAndUserPathes.LOGIN)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON + CHARSET)
	public LoginResponse doLogin(LoginRequest req) {

		try {
			LoginResponse resp = adrSf.doLogin(req);
			return resp;

		} catch (Exception e) {
			LoginResponse resp = new LoginResponse();
			convertAndlogErrorSetBaseResponse(e, resp, "doLogin()", 10, logger);
			return resp;
		}

	}

	/**
	 * registerAddress: Creates a Address in the DB andreturns the AddressDTO
	 * with generated ID Method processing HTTP POST requests, producing
	 * "text/xml" or "application/json" type.
	 * 
	 * @return String that will be send back as a response of type "text/xml" or
	 *         "application/json".
	 */
	@POST
	@Path(AdressAndUserPathes.REGISTER_ADDRESS)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON)
	public AddressResponse registerAddress(RegisterAddressRequest req) {
		try {
			AddressDTO res = adrSf.registerAddress(req);
			return new AddressResponse(res);
		} catch (Exception e) {
			AddressResponse res = new AddressResponse();
			convertAndlogErrorSetBaseResponse(e, res, "registerAddress()", 11,
					logger);
			return res;
		}
	}

	@GET
	@Path(AdressAndUserPathes.RESET_PW)
	@Produces(MediaType.APPLICATION_JSON)
	public PasswordResetResponse resetPassword(@QueryParam("email") String email) {
		try {
			PasswordResetRequest req = new PasswordResetRequest();
			req.setEmail(email);
			PasswordResetResponse res = adrSf.resetPassword(req);
			return res;
		} catch (Exception e) {
			PasswordResetResponse res = new PasswordResetResponse();
			convertAndlogErrorSetBaseResponse(e, res, "resetPassword()", 12,
					logger);

			return res;
		}
	}

	/**
	 * Creates a User Entity in the DB and returns the AddressDTO with generated
	 * ID Method processing HTTP POST requests, producing "text/xml" or
	 * "application/json" type.
	 * 
	 * @return String that will be send back as a response of type "text/xml" or
	 *         "application/json".
	 */
	@Deprecated
	@POST
	@Path(AdressAndUserPathes.CREATE_USER)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.APPLICATION_JSON)
	public AddressDTO createUser(RegisterAddressRequest req) {
		if (req == null || req.getAddress() == null
				|| req.getAddress().getEmail() == null || req.getPw() == null) {
			logger.error("Adresse, Email aoder Passwort nicht angegeben");
			throw new IllegalRestClientArgumentException(
					"Adresse, Email aoder Passwort nicht angegeben");
		}
		if (!EmailAddressValidator.isValid(req.getAddress().getEmail())) {
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

}
