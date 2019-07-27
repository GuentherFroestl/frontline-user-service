package de.frontline.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import com.tom.service.dto.AddressDTO;

import de.frontline.persistence.entities.FlUser;

/**
 * @author gfr UserDAO handelt den Zugriff auf Userobjekte
 */
@Local
public interface UserDAOLocal {

	/**
	 * Liste aller User
	 * 
	 * @return List<User>
	 */
	public Long countUser();

	/**
	 * Anzahl der uservars insgesamt
	 * 
	 * @return Long #Uservars insgesamt
	 */
	public Long countUservar();

	/**
	 * User mit der Emailadresse finden
	 * 
	 * @param email
	 * @return List<FlUser>
	 */
	public List<FlUser> findUserByString(String search) throws DbException;

	/**
	 * User mit der Emailadresse finden
	 * 
	 * @param id
	 *            Integer
	 * @return AddressDTO
	 */
	public AddressDTO findById(Integer id) throws DbException;

	/**
	 * User mit der Emailadresse finden
	 * 
	 * @param uuid
	 *            String
	 * @return AddressDTO
	 */
	public AddressDTO findByUuid(String uuid) throws DbException;

	/**
	 * User mit der Emailadresse finden
	 * 
	 * @param email
	 * @return List<FlUser>
	 */
	public List<FlUser> findUserByEmail(String email) throws DbException;

	/**
	 * User mit der Emailadresse finden
	 * 
	 * @param email
	 * @param pw
	 * @return List<FlUser>
	 */
	public List<FlUser> findUserByEmailAndPw(String email, String pw)
			throws DbException;

	/**
	 * Die Uservars werden geladen
	 * 
	 * @param user
	 * @return user mit geladenen Uservars
	 */
	public FlUser loadUservars(FlUser user) throws DbException;

	/**
	 * Erzeugt einen AddressDTO aus dem FlUser wenn die UserVars noch null sind
	 * werden sie automatisch aus der DB geladen
	 * 
	 * @param user
	 * @return FlUser
	 */
	public AddressDTO genAddressDTO(FlUser user) throws DbException;

	/**
	 * Ein Frontline User wird neu angelegt
	 * 
	 * @param user
	 * @return FlUser
	 */
	public FlUser createFlUser(FlUser user) throws DbException;

	/**
	 * Die Eigenschaften eines Users werden als Uservars in der DB abgespeichert
	 * 
	 * @param user
	 *            AddressDTO
	 */
	public void storeUservars(AddressDTO user) throws DbException;

	/**
	 * Ein existierender user wird upgedated
	 * 
	 * @param adr
	 *            AddressDTO
	 * @return AddressDTO
	 */
	public AddressDTO updateUser(AddressDTO adr) throws DbException;

	/**
	 * Eine existierender FlUser wird mit den uservars gelöscht
	 * 
	 * @param adr
	 *            AddressDTO
	 * @throws DbException
	 */
	public void deleteUser(AddressDTO adr) throws DbException;

}
