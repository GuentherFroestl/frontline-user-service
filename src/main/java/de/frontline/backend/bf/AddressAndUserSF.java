package de.frontline.backend.bf;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.tom.service.dto.AddressDTO;
import com.tom.service.dto.LoginRequest;
import com.tom.service.dto.LoginResponse;
import com.tom.service.dto.PasswordResetRequest;
import com.tom.service.dto.PasswordResetResponse;
import com.tom.service.dto.RegisterAddressRequest;
import com.tom.service.dto.SearchByStringRequest;
import com.tom.service.facade.TomException;

import de.frontline.backend.bl.AddressServiceLocal;

/**
 * Service Facade für Address Funktionen, Session Bean implementation class AdressAndUserSF
 */
@Stateless(mappedName = "frontline/AddressAndUserSF")
public class AddressAndUserSF implements AddressAndUserSFRemote,
        AddressAndUserSFLocal {

  @EJB
  AddressServiceLocal adrSvc;

  /**
   * Default constructor.
   */
  public AddressAndUserSF() {
  }

  /**
   * 
   * @param req SearchByStringRequest
   * @return List of AddressDTO
   * @throws TomException 
   */
  @Override
  public List<AddressDTO> searchAddresses(SearchByStringRequest req) throws TomException {
    if (req == null) {
      return null;
    }
    return adrSvc.searchAddresses(req.getSearchString());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.tom.ejb.facade.AddressAndUserSFLocal#loadAddressById(java.lang.String
   * )
   */
  @Override
  public AddressDTO loadAddressById(String id) throws TomException {
    return adrSvc.loadAddressById(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.tom.ejb.facade.AddressAndUserSFLocal#loadAddressByUuid(java.lang.
   * String)
   */
  @Override
  public AddressDTO loadAddressByUuid(String uid) throws TomException {
    return adrSvc.loadAddressByUid(uid);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.tom.ejb.facade.AddressAndUserSFLocal#createAddress(com.tom.dto.AddressDTO
   * )
   */
  @Override
  public AddressDTO createAddress(AddressDTO adr) throws TomException {
    return adrSvc.createAddress(adr);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.tom.ejb.facade.AddressAndUserSFLocal#updateAddress(com.tom.dto.AddressDTO
   * )
   */
  @Override
  public AddressDTO updateAddress(AddressDTO adr) throws TomException {
    return adrSvc.updateAddress(adr);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.tom.ejb.facade.AddressAndUserSFLocal#deleteAddress(com.tom.dto.AddressDTO
   * )
   */
  @Override
  public void deleteAddress(AddressDTO adr) throws TomException {
    adrSvc.deleteAddress(adr);

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.tom.ejb.facade.AddressAndUserSFLocal#doLogin(java.lang.String,
   * java.lang.String)
   */
  @Override
  public LoginResponse doLogin(LoginRequest req) throws TomException {
    return adrSvc.doLogin(req);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * de.frontline.backend.bf.AddressAndUserSFLocal#registerAddress(com.tom
   * .dto.RegisterAddressRequest)
   */
  @Override
  public AddressDTO registerAddress(RegisterAddressRequest req)
          throws TomException {
    return adrSvc.registerAddress(req);
  }

  @Override
  public PasswordResetResponse resetPassword(PasswordResetRequest req)
          throws TomException {
    return adrSvc.resetPassword(req);
  }
}
