package de.frontline.backend.bl;

import javax.ejb.Local;

/**
 * @author gfr
 * Nur zum testen
 *
 */
@Local
public interface HelloTestLocal {
	
	/**
	 * Testmethode
	 * @return String
	 */
	public String sayHello();

}
