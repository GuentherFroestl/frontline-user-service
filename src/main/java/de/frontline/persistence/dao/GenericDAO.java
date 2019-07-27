/**
 * 
 */
package de.frontline.persistence.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * Allgemeines (Generic) I/F für alle DAO
 * 
 * @author gfr
 * 
 */
public interface GenericDAO<T, ID extends Serializable> {

	public enum SEARCHTYPE {
		EXACT, STARTSWITH, CONTAINS, PATTERN
	}

	// public List<T> findByString(String suchbegriff, SEARCHTYPE type,int
	// index, int max,EntityManager em);
	public List<T> findAll(int index, int max, EntityManager em);

	public T findById(ID id, EntityManager em);

	public Long countEntities(EntityManager em);

	public void persistEntity(T entity, EntityManager em);

	public T mergeEntity(T entity, EntityManager em);

	public void removeEntity(T entity, EntityManager em);

	public EntityManager getEntityManager();
}
