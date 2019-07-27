/**
 * 
 */
package de.frontline.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity für die uservariablen in der UserDB
 * Werden über die user_id als key/value Paar dem user zugeordnet
 * @author gfr
 *
 */
@Entity
@Table(name="uservar")
public class Uservar  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/**
	 * Value/Wert der Eigenschaft
	 */
	@Column(name="value")
	private String value;
	
	
	/**
	 * Hilfskonstrukt um den fehlenden PK in der UserBD.uservar zu ersetzen
	 */
	@EmbeddedId
	private UservarPK primaryKey;

	/**
	 * @return the primaryKey
	 */
	public UservarPK getPrimaryKey() {
		return primaryKey;
	}



	/**
	 * @param primaryKey the primaryKey to set
	 */
	public void setPrimaryKey(UservarPK primaryKey) {
		this.primaryKey = primaryKey;
	}



	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}



	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Uservar [value=" + value + ", primaryKey=" + primaryKey + "]";
	}




	

}
