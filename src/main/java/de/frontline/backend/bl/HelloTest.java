package de.frontline.backend.bl;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class HelloTest
 */
@Stateless
@LocalBean
public class HelloTest implements  HelloTestLocal {

    /**
     * Default constructor. 
     */
    public HelloTest() {
    }

	@Override
	public String sayHello() {
		return "Hallo, diese Botschaft kommt von einem EJB 3 Session Bean!";
	}

}
