package de.frontline.backend.bl;

import java.util.List;

import javax.ejb.Local;

import com.tom.service.dto.AddressDTO;
import com.tom.service.dto.LoginRequest;
import com.tom.service.dto.LoginResponse;
import com.tom.service.dto.PasswordResetRequest;
import com.tom.service.dto.PasswordResetResponse;
import com.tom.service.dto.RegisterAddressRequest;
import com.tom.service.facade.TomException;

@Local
public interface AddressServiceLocal {

	/**
	 * Sucht Adressen aufgrund eines Suchbegriffes
	 * 
	 * @param seachString
	 * @return List of AddressDTO
	 * @throws TomException
	 */
	public List<AddressDTO> searchAddresses(String seachString)
			throws TomException;

	/**
	 * Sucht Adressen aufgrund eines Email-Suchbegriffes
	 * 
	 * @param seachString
	 * @return List of AddressDTO
	 * @throws TomException
	 */
	public List<AddressDTO> searchAddressesByEmail(String seachString)
			throws TomException;

	/**
	 * Lädt eine Addresse durch die ID
	 * 
	 * @param id
	 *            String
	 * @return AddressDTO
	 */
	public AddressDTO loadAddressById(String id) throws TomException;

	/**
	 * Lädt eine Addresse durch die ID
	 * 
	 * @param uid
	 *            String
	 * @return AddressDTO
	 */
	public AddressDTO loadAddressByUid(String uid) throws TomException;

	/**
	 * Speichert eine neue Adresse in der Datenbank
	 * 
	 * @param adr
	 *            AddressDTO
	 * @return AddressDTO mit erzeugter ID und UID
	 */
	public AddressDTO createAddress(AddressDTO adr) throws TomException;

	/**
	 * Speichert/überschreibt eine Adresse in der Datenbank
	 * 
	 * @param adr
	 *            AddressDTO
	 * @return AddressDTO mit erzeugter ID
	 */
	public AddressDTO updateAddress(AddressDTO adr) throws TomException;

	/**
	 * Löscht eine Adresse aus der Datenbank
	 * 
	 * @param adr
	 *            AddressDTO mit id oder uuid gesetzt
	 * @throws TomException
	 */
	public void deleteAddress(AddressDTO adr) throws TomException;

	/**
	 * Login Routine
	 * 
	 * @param userName
	 * @param pw
	 * @return LoginResultDTO
	 */
	public LoginResponse doLogin(LoginRequest req) throws TomException;

	/**
	 * Register eine Adresse mit Passwort
	 * 
	 * @param req
	 * @return AddressDTO
	 * @throws TomException
	 */
	public AddressDTO registerAddress(RegisterAddressRequest req)
			throws TomException;

	/**
	 * Service zum Rücksetzen des Password für den spezifizierten user
	 * 
	 * @param req
	 *            PasswordResetRequest
	 * @return PasswordResetResponse
	 * @throws TomException
	 */
	public PasswordResetResponse resetPassword(PasswordResetRequest req)
			throws TomException;

}
