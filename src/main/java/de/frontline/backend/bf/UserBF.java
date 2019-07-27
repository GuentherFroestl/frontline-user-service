package de.frontline.backend.bf;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.tom.service.dto.AddressDTO;

import de.frontline.persistence.entities.FlUser;
import de.frontline.persistence.dao.DbException;
import de.frontline.persistence.dao.UserDAOLocal;
import de.frontline.rest.restservice.IllegalRestClientArgumentException;

/**
 * Session Bean implementation class UserBF
 */
@Stateless
public class UserBF implements UserBFRemote, UserBFLocal {

	/**
	 * Default constructor.
	 */
	public UserBF() {
	}

	private final static Logger LOGGER = Logger.getLogger(UserBF.class
			.getName());

	// @EJB(name = "userdao", beanName = "UserDAO")
	@EJB
	UserDAOLocal userDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.bf.UserBFLocal#createUser(at.cyberlab.server.rest
	 * .dto.AddressDTO)
	 */
	@Override
	public AddressDTO createUser(AddressDTO adr, String password) {
		FlUser flu = new FlUser();
		flu.setEmail(adr.getEmail());
		flu.setPassword(password);
		try {
			flu = userDao.createFlUser(flu);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fehler=", e);
			throw new IllegalRestClientArgumentException(e.getMessage());
		}
		LOGGER.info("create FlUser=" + flu);

		adr.setId(flu.getId());
		try {
			userDao.storeUservars(adr);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fehler=", e);
			throw new IllegalRestClientArgumentException(e.getMessage());
		}
		return adr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.bf.UserBFLocal#updateUser(at.cyberlab.server.rest
	 * .dto.AddressDTO)
	 */
	@Override
	public AddressDTO updateUser(AddressDTO user) throws DbException {
		user = userDao.updateUser(user);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.onespark.backend.bf.UserBFLocal#getUserByEmail(java.lang.String)
	 */
	@Override
	public List<AddressDTO> getUserByEmail(String email) throws DbException {
		List<FlUser> ul = userDao.findUserByEmail(email);
		List<AddressDTO> rl = new ArrayList<AddressDTO>();
		if (ul != null && ul.size() > 0) {
			for (FlUser fl : ul) {
				AddressDTO AddressDTO = userDao.genAddressDTO(fl);
				rl.add(AddressDTO);
			}
		}
		return rl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.bf.UserBFLocal#getUserByEmailAndPassword(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public List<AddressDTO> getUserByEmailAndPassword(String email,
			String password) throws DbException {
		List<FlUser> ul = userDao.findUserByEmailAndPw(email, password);
		List<AddressDTO> rl = new ArrayList<AddressDTO>();
		if (ul != null && ul.size() > 0) {
			for (FlUser fl : ul) {
				AddressDTO AddressDTO = userDao.genAddressDTO(fl);
				rl.add(AddressDTO);
			}
		}
		return rl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.onespark.backend.bf.UserBFLocal#countUsers()
	 */
	@Override
	public Long countUsers() {
		Long count = userDao.countUser();
		return count;
	}

}
