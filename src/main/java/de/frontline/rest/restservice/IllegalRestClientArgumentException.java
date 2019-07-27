/**
 * 
 */
package de.frontline.rest.restservice;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author gfr
 *
 */
public class IllegalRestClientArgumentException extends WebApplicationException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2468224378417430937L;

	/**
	 * Fehlermeldung
	 */
	private String message;
	
	/**
	 * Constructor
	 * @param statusCode HTTP-Status Code
	 * @param message Fehlermeldung
	 */
	public IllegalRestClientArgumentException(String message){
		super(Response.status(Response.Status.BAD_REQUEST)
	             .entity(message).type(MediaType.TEXT_PLAIN).build());

		this.message = message;
		
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IllegalRestClientArgumentException [message=" + message+"]";
	}

}
