/**
 * 
 */
package de.frontline.rest.restservice;

import org.apache.log4j.Logger;

import com.tom.service.dto.BaseResponse;

/**
 * has some common methodes
 * 
 * @author gfr
 * 
 */
public class AbstractResource {

	public Throwable getRootCause(Throwable throwable) {
		if (throwable.getCause() == null) {
			return throwable;
		}
		return getRootCause(throwable.getCause());
	}

	/**
	 * findet den root cause und loggt sowie setzt die Fehlermeldung in den
	 * Response
	 * 
	 * @param e
	 *            Exception
	 * @param resp
	 *            BaseResponse
	 * @param methodeHint
	 *            name of calling methode
	 * @param errorCode
	 *            int Fehlernummer
	 * @param logger
	 *            Logger
	 */
	public void convertAndlogErrorSetBaseResponse(Exception e,
			BaseResponse resp, String methodeHint, int errorCode, Logger logger) {
		Throwable rootCause = getRootCause(e);
		logger.error(methodeHint + ": " + rootCause.getMessage(), rootCause);
		resp.setErrorCode(errorCode);
		resp.setHasErrors(true);
		resp.setErrorMessage(rootCause.getMessage());

	}

}
