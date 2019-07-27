/**
 * 
 */
package de.frontline.backend.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import com.tom.service.dto.AddressDTO;
import com.tom.service.dto.LoginRequest;
import com.tom.service.dto.LoginResponse;
import com.tom.service.dto.PasswordResetRequest;
import com.tom.service.dto.PasswordResetResponse;
import com.tom.service.dto.RegisterAddressRequest;
import com.tom.service.facade.TomException;
import com.tom.service.util.EmailAddressValidator;

import de.frontline.persistence.entities.FlUser;
import de.frontline.persistence.dao.DbException;
import de.frontline.persistence.dao.UserDAOLocal;
import de.frontline.rest.restservice.IllegalRestClientArgumentException;

/**
 * @author gfr
 * 
 */
@Stateless
public class AddressService implements AddressServiceLocal {

	private final Logger logger = Logger.getLogger(AddressService.class);
	public final String CHAR_SET = "UTF-8";

	@EJB
	UserDAOLocal userDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.backend.bl.AddressServiceLocal#searchAddresses(java.lang
	 * .String)
	 */
	@Override
	public List<AddressDTO> searchAddresses(String seachString)
			throws TomException {
		if (seachString == null) {
			throw new TomException("Seachargument == null");
		}
		List<FlUser> uL = null;
		try {
			uL = userDao.findUserByString("%" + seachString + "%");
		} catch (DbException e) {
			throw new TomException(e.getLocalizedMessage());
		}
		List<AddressDTO> res = getAddressList(uL);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.backend.bl.AddressServiceLocal#searchAddressesByEmail(java
	 * .lang.String)
	 */
	@Override
	public List<AddressDTO> searchAddressesByEmail(String email)
			throws TomException {
		if (email == null || email.length() == 0) {
			logger.error("email == null oder email.length == 0");
			throw new IllegalRestClientArgumentException(
					"email == null oder email.length == 0");
		}
		try {
			List<FlUser> uL = userDao.findUserByEmail(email);
			List<AddressDTO> res = getAddressList(uL);
			return res;
		} catch (Exception e) {
			logger.error("getUserByEmail: " + e.getMessage(), e);
			throw new TomException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.backend.bl.AddressServiceLocal#loadAddressById(java.lang
	 * .String)
	 */
	@Override
	public AddressDTO loadAddressById(String id) throws TomException {
		if (id == null || id.length() == 0) {
			throw new TomException("id == null oder id==0");
		}
		Integer i;
		try {
			i = Integer.parseInt(id);
			return userDao.findById(i);
		} catch (NumberFormatException e) {
			throw new TomException("id unzulässig " + e.getMessage());
		} catch (DbException e) {
			throw new TomException(e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.backend.bl.AddressServiceLocal#loadAddressByUid(java.lang
	 * .String)
	 */
	@Override
	public AddressDTO loadAddressByUid(String uuid) throws TomException {
		if (uuid == null || uuid.length() == 0) {
			throw new TomException("uuid == null oder uuid ist leer");
		}
		AddressDTO res;
		try {
			res = userDao.findByUuid(uuid);
			return res;
		} catch (DbException e) {
			throw new TomException(e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.backend.bl.AddressServiceLocal#createAddress(com.tom.dto
	 * .AddressDTO)
	 */
	@Override
	public AddressDTO createAddress(AddressDTO adr) throws TomException {
		// wegen email&&passwort nicht unterstützt
		throw new TomException(
				"Opertion nicht unterstützt, registerAddress verwenden");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.backend.bl.AddressServiceLocal#updateAddress(com.tom.dto
	 * .AddressDTO)
	 */
	@Override
	public AddressDTO updateAddress(AddressDTO adr) throws TomException {
		try {
			AddressDTO res = userDao.updateUser(adr);
			return res;
		} catch (DbException e) {
			throw new TomException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.backend.bl.AddressServiceLocal#deleteAddress(com.tom.dto
	 * .AddressDTO)
	 */
	@Override
	public void deleteAddress(AddressDTO adr) throws TomException {
		try {
			userDao.deleteUser(adr);
		} catch (DbException e) {
			throw new TomException(e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.backend.bl.AddressServiceLocal#doLogin(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public LoginResponse doLogin(LoginRequest req) throws TomException {
		LoginResponse resp = new LoginResponse();
		if (req.getUserName() == null || req.getPw() == null) {
			logger.error("Adresse, Email aoder Passwort nicht angegeben");
			throw new TomException(
					"Adresse, Email aoder Passwort nicht angegeben");
		}
		try {
			List<FlUser> ul = userDao.findUserByEmailAndPw(req.getUserName(),
					req.getPw());
			if (ul == null || ul.size() == 0) {
				resp.setHasErrors(true);
				resp.setErrorMessage("Email & Passwort nicht gefunden");

			} else {
				if (ul.size() > 1) {
					resp.setHasErrors(true);
					resp.setErrorMessage("Mehr als eine Email & Passwort Kombination gefunden");
				}
				AddressDTO adr = userDao.genAddressDTO(ul.get(0));
				resp.setAddress(adr);
			}
		} catch (DbException e) {
			throw new TomException(e.getMessage());
		}

		return resp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.backend.bl.AddressServiceLocal#registerAddress(com.tom.dto
	 * .RegisterAddressRequest)
	 */
	@Override
	public AddressDTO registerAddress(RegisterAddressRequest req)
			throws TomException {
		if (req == null || req.getAddress() == null
				|| req.getAddress().getEmail() == null || req.getPw() == null) {
			logger.error("Adresse, Email aoder Passwort nicht angegeben");
			throw new TomException(
					"Adresse, Email aoder Passwort nicht angegeben");
		}
		if (!EmailAddressValidator.isValid(req.getAddress().getEmail())) {
			logger.error("email nicht zulässig = "
					+ req.getAddress().getEmail());
			throw new IllegalRestClientArgumentException(
					"email nicht zulässig = " + req.getAddress().getEmail());
		}
		if (req.getPw().length() < 5) {
			logger.error("Passwort nicht zulässig, mehr als 5 Zeichen erforderlich");
			throw new TomException(
					"Passwort nicht zulässig, mehr als 5 Zeichen erforderlich");
		}

		AddressDTO adr = req.getAddress();
		String password = req.getPw();

		FlUser flu = new FlUser();
		flu.setEmail(adr.getEmail());
		flu.setPassword(password);
		if (adr.getUuid() == null || adr.getUuid().length() == 0) {
			adr.setUuid(UUID.randomUUID().toString());
			logger.info("create UUID for Address=" + adr);
		}
		try {
			flu = userDao.createFlUser(flu);
		} catch (Exception e) {
			logger.error("Fehler=", e);
			throw new TomException(e.getMessage());
		}
		logger.info("create FlUser=" + flu);

		adr.setId(flu.getId());
		try {
			userDao.storeUservars(adr);
		} catch (Exception e) {
			logger.error("Fehler=", e);
			throw new TomException(e.getMessage());
		}

		return adr;

	}

	@Override
	public PasswordResetResponse resetPassword(PasswordResetRequest req)
			throws TomException {
		if (req == null) {
			throw new TomException("request == null");
		}
		if (!EmailAddressValidator.isValid(req.getEmail())) {
			logger.error("email Adresse nicht zulässig = " + req.getEmail());
			throw new TomException(
					"Die angegebene Email-Adresse nicht zulässig = "
							+ req.getEmail());
		}

		List<FlUser> uL = null;
		AddressDTO adr = null;
		String newPw = RandomStringUtils.randomAlphanumeric(8);
		try {
			uL = userDao.findUserByEmail(req.getEmail());
			FlUser user = uL.get(0);

			if (uL == null || uL.size() == 0) {
				throw new TomException(""); // um catch Block zu aktivieren
			}
			adr = userDao.genAddressDTO(user);
			user.setPassword(newPw);

		} catch (DbException e) {
			logger.error("email Adresse nicht gefunden = " + req.getEmail()
					+ ", " + e.getMessage(), e);
			throw new TomException("email Adresse nicht gefunden = "
					+ req.getEmail());
		}

		// email new password
		try {
			SimpleEmail email = new SimpleEmail();
			email.setCharset(CHAR_SET);
			email.setHostName("mail.cyberlab.at");
			email.addTo(adr.getEmail(), adr.getVorname() + " " + adr.getName());
			email.setFrom("iorder@cyberlab.at", "cyberlab order service");
			email.setSubject("Ihre neuen Zugangsdaten für die cyberlab online Services");

			email.setMsg("Sehr geehrter Kunde, \nSehr geehrte Kundin,\n\n"
					+ "hier ist Ihr neues Passwort für die cyberlab online services:\n\n"
					+ newPw + "\n\n\n\n" + "Mit freundlichen Grüßen\n\n"
					+ "cyberlab online services\n" + "http://www.cyberlab.at");
			email.setAuthentication("iorder@cyberlab.at", "iorder1070");
			email.send();
		} catch (EmailException e) {
			logger.error("Fehler beim senden der Email an = " + req.getEmail()
					+ ", " + e.getMessage(), e);
			throw new TomException("Fehler beim senden der Email an = "
					+ req.getEmail());
		}
		logger.info("neues Password für folgenden user gesendet " + adr);
		PasswordResetResponse res = new PasswordResetResponse();
		return res;
	}

	/**
	 * Mapper für FlUser >> AddressDTO
	 * 
	 * @param List
	 *            <FlUser>
	 * @return List<AddressDTO>
	 * @throws TomException
	 */
	private List<AddressDTO> getAddressList(List<FlUser> uL)
			throws TomException {
		List<AddressDTO> res = new ArrayList<AddressDTO>();
		if (uL != null && uL.size() > 0) {
			for (FlUser fu : uL) {
				AddressDTO a = null;
				try {
					a = userDao.genAddressDTO(fu);
				} catch (DbException e) {
					throw new TomException(e.getMessage());
				}
				if (a != null) {
					res.add(a);
				}
			}
		}
		return res;
	}

}
