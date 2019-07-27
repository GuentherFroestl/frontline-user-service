package de.frontline.persistence.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Basisklasse für alle DataAccessObjects die mit JPA arbeiten Wichtig !!!! Alle
 * abgeleiteten Klassen müssen "e" als Platzhalter für das Entity in den Queries
 * verwenden also: SELECT e FROM EntityName e .......
 * 
 * @author gfr
 * 
 * @param <T>
 *            Klasse
 * @param <ID>
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements
		GenericDAO<T, ID> {

	/**
	 * 
	 * @return String Entity Short-Name, zb. UserDO (Entity)
	 */
	abstract public String getEntityName();

	/**
	 * 
	 * @return String, ORDER BY String z.b. "ORDER BY e.name" e ist Platzhalter
	 *         für Entity
	 */
	abstract public String getStandardOrderString();

	/**
	 * Alle abgeleiteten Klassen müssen entityPlatzHalter als Platzhalter für
	 * das Entity in den Queries verwenden
	 */
	public static final String entityPlatzHalter = "e";

	@Override
	public T findById(ID id, EntityManager em) {

		@SuppressWarnings("unchecked")
		T entity = em.find((Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0], id);
		return entity;
	}

	@Override
	public List<T> findAll(int index, int max, EntityManager em) {

		Query q = em.createQuery("SELECT " + entityPlatzHalter + " FROM "
				+ getEntityName() + " " + entityPlatzHalter + " "
				+ getStandardOrderString());

		if (max > 0)
			q.setMaxResults(max);
		if (index > 0)
			q.setFirstResult(index);

		return q.getResultList();
	}

	@Override
	public Long countEntities(EntityManager em) {
		Long anzahl = new Long(0);
		try {
			Query query = em.createQuery("SELECT COUNT(" + entityPlatzHalter
					+ ") from " + getEntityName() + " " + entityPlatzHalter);
			anzahl = (Long) query.getSingleResult();
		} catch (Exception e) {
		}
		return anzahl;
	}

	@Override
	public void persistEntity(T entity, EntityManager em) {
		em.persist(entity);
	}

	@Override
	public T mergeEntity(T entity, EntityManager em) {
		return em.merge(entity);
	}

	@Override
	public void removeEntity(T entity, EntityManager em) {
		em.remove(entity);
	}

	/*
	 * @see de.gammadata.persistence.GenericDAO#findByString(java.lang.String,
	 * de.gammadata.persistence.GenericDAO.SEARCHTYPE, int, int,
	 * javax.persistence.EntityManager) sollte von Unterklassen überschrieben
	 * werden
	 */

	public String getSearchString(String suchbegriff, SEARCHTYPE type) {
		String sq = suchbegriff;
		if (type.compareTo(SEARCHTYPE.STARTSWITH) == 0)
			sq = suchbegriff + "%";
		else if (type.compareTo(SEARCHTYPE.CONTAINS) == 0)
			sq = "%" + suchbegriff + "%";

		return sq;
	}

}