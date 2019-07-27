package de.frontline.backend.bf;

import javax.ejb.Local;

import com.tom.service.facade.AddressFacade;
import com.tom.service.facade.FlUserFacade;

/**
 * Service Facade für Zugriffe auf die Adressen
 * 
 * @author gfr
 * 
 */
@Local
public interface AddressAndUserSFLocal extends AddressFacade, FlUserFacade {

}
