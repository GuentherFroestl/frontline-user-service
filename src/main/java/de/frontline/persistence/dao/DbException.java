/**
 * 
 */
package de.frontline.persistence.dao;

/**
 * @author gfr
 * 
 */
public class DbException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;

	public DbException(String msg) {
		setMessage(msg);
	}

	/**
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
