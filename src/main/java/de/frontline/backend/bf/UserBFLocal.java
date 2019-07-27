package de.frontline.backend.bf;

import java.util.List;

import javax.ejb.Local;

import com.tom.service.dto.AddressDTO;

import de.frontline.persistence.dao.DbException;

/**
 * @author gfr Business Facade for User Repository
 * 
 */
@Local
public interface UserBFLocal {

	/**
	 * Creates a new user
	 * 
	 * @param user
	 *            AddressDTO
	 * @return AddressDTO
	 */
	public AddressDTO createUser(AddressDTO user, String password)
			throws DbException;

	/**
	 * Updates an extisting User
	 * 
	 * @param user
	 *            AddressDTO
	 * @return AddressDTO
	 * @throws DbException
	 */
	public AddressDTO updateUser(AddressDTO user) throws DbException;

	/**
	 * Searches an User by Email Address
	 * 
	 * @param email
	 *            String
	 * @return List<AddressDTO>
	 */
	public List<AddressDTO> getUserByEmail(String email) throws DbException;

	/**
	 * Searches an User by Email Address and Password
	 * 
	 * @param email
	 *            String
	 * @param password
	 *            String
	 * @return List<AddressDTO>
	 */
	public List<AddressDTO> getUserByEmailAndPassword(String email,
			String password) throws DbException;

	/**
	 * Returns the count of existing users in the databas
	 * 
	 * @return userCount Long
	 */
	public Long countUsers() throws DbException;

}
