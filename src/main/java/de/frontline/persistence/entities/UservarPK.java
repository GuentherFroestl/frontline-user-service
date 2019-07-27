/**
 * 
 */
package de.frontline.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Hilskonstrukt um den PK für Uservar zu implementieren
 * @author gfr
 *
 */
@Embeddable
public class UservarPK {
	
	/**
	 * default Konstruktor
	 */
	public UservarPK(){
		
	}
	

	/**
	 * Konstruktor
	 * @param id UserId
	 * @param name Key
	 */
	public UservarPK(int id,String name){
		this.id = id;
		this.name = name;
	}
	
	/**
	 * FK auf User
	 */
	@Column(name="user_id")
	private int id;
	/**
	 * Name oder Key der Eigenschaft
	 */
	@Column(name="name")
	private String name;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UservarPK other = (UservarPK) obj;
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UservarPK [id=" + id + ", name=" + name + "]";
	}

}
