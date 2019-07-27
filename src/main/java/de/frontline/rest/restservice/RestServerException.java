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
public class RestServerException extends WebApplicationException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 246822437841743333L;

	/**
	 * Fehlermeldung
	 */
	private String message;
	
	/**
	 * Constructor
	 * @param statusCode HTTP-Status Code
	 * @param message Fehlermeldung
	 */
	public RestServerException(String message){
		super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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
		return "RestServerException [message=" + message+"]";
	}

}
