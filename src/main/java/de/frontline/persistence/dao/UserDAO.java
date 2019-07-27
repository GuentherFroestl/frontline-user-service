package de.frontline.persistence.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.tom.service.dto.AddressDTO;
import com.tom.service.dto.LandDTO;

import de.frontline.persistence.entities.FlUser;
import de.frontline.persistence.entities.Uservar;
import de.frontline.persistence.entities.UservarPK;

/**
 * Session Bean implementation class UserDAO
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserDAO extends GenericDAOImpl<FlUser, Integer> implements
		UserDAOLocal {

	public static final String standardOrder = " ORDER BY e.email";
	// public static final String entityName = "FlUser";

	public static final String VORNAME_KEY = "billingFirstName";
	public static final String NACHNAME_KEY = "billingLastName";
	public static final String FIRMA_KEY = "firma";
	public static final String STRASSE_KEY = "billingAddress";
	public static final String PLZ_KEY = "billingZipCode";
	public static final String STADT_KEY = "billingCity";
	public static final String LAND_KEY = "billingState";
	public static final String TEL_KEY = "billingTelephone";
	public static final String MOBILE_KEY = "mobilephone";
	public static final String ABTEILUNG_KEY = "abteilung";
	public static final String TITLE_KEY = "title";
	public static final String UUID_KEY = "uuid";
	public static final String MANDANT_KEY = "mandant";
	public static final String KUNDEN_KEY = "Kunden-ID";

	@PersistenceContext(unitName = "frontline.userdb")
	private EntityManager em;

	private final Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Default constructor.
	 */
	public UserDAO() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.onespark.backend.persistence.dao.GenericDAOImpl#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.persistence.dao.GenericDAOImpl#getStandardOrderString
	 * ()
	 */
	@Override
	public String getStandardOrderString() {
		return standardOrder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.onespark.backend.persistence.dao.GenericDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.onespark.backend.persistence.dao.UserDAOLocal#countUservar()
	 */
	@Override
	public Long countUservar() {

		Long anzahl;
		Query query = getEntityManager().createQuery(
				"SELECT COUNT(" + entityPlatzHalter + ") from "
						+ Uservar.class.getSimpleName() + " "
						+ entityPlatzHalter);
		anzahl = (Long) query.getSingleResult();
		return anzahl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.onespark.backend.persistence.dao.UserDAOLocal#countUser()
	 */
	@Override
	public Long countUser() {
		Long anzahl;
		Query query = getEntityManager().createQuery(
				"SELECT COUNT(" + entityPlatzHalter + ") from "
						+ FlUser.class.getSimpleName() + " "
						+ entityPlatzHalter);
		anzahl = (Long) query.getSingleResult();
		return anzahl;
	}

	@Override
	public AddressDTO findById(Integer id) throws DbException {
		FlUser lfu = findById(id, getEntityManager());
		if (lfu != null && lfu.getId() != 0) {
			AddressDTO res = genAddressDTO(lfu);
			return res;
		} else {
			throw new DbException("Objekt mit ID = " + id + " nicht gefunden");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.persistence.dao.UserDAOLocal#findByUuid(java.lang.String)
	 */
	@Override
	public AddressDTO findByUuid(String uuid) throws DbException {
		Query q = getEntityManager()
				.createQuery(
						"SELECT "
								+ "v"
								+ " FROM "
								+ Uservar.class.getSimpleName()
								+ " "
								+ "v"
								+ " WHERE v.value like :uuid and v.primaryKey.name='uuid'");

		q.setParameter("uuid", uuid);
		@SuppressWarnings("unchecked")
		List<Uservar> list = q.getResultList();
		if (list == null || list.size() == 0) {
			throw new DbException(
					"Uservar Objekt mit key uuid und value uuid = " + uuid
							+ " nicht gefunden");
		} else if (list.size() > 1) {
			throw new DbException(
					"mehrere Uservar Objekte key uuid und value uuid = " + uuid
							+ " gefunden");
		}
		// Jetzt Adresse laden
		Integer userId = list.get(0).getPrimaryKey().getId();
		return findById(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.persistence.dao.UserDAOLocal#findUserByString(java.lang.
	 * String)
	 */
	@Override
	public List<FlUser> findUserByString(String search) throws DbException {
		List<FlUser> emailList = findUserByEmail(search);

		Query q = getEntityManager()
				.createQuery(
						"SELECT "
								+ entityPlatzHalter
								+ " FROM "
								+ FlUser.class.getSimpleName()
								+ " "
								+ entityPlatzHalter
								+ ","
								+ Uservar.class.getSimpleName()
								+ " "
								+ "v"
								+ " WHERE "
								+ "(v.value like :value and "
								+ "(v.primaryKey.name='firma' or v.primaryKey.name='name' or v.primaryKey.name='billingFirstName' or v.primaryKey.name='billingLastName' )"
								+ " and v.primaryKey.id=" + entityPlatzHalter
								+ ".id)" + getStandardOrderString());

		q.setParameter("value", search);
		@SuppressWarnings("unchecked")
		List<FlUser> res = q.getResultList();
		if (res != null && emailList != null) {
			res.removeAll(emailList); // Duppletten entfernen
			res.addAll(emailList);
			return res;
		} else {
			return emailList;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.persistence.dao.UserDAOLocal#getUserByEmail(java.
	 * lang.String)
	 */
	@Override
	public List<FlUser> findUserByEmail(String email) {
		Query q = getEntityManager().createQuery(
				"SELECT " + entityPlatzHalter + " FROM "
						+ FlUser.class.getSimpleName() + " "
						+ entityPlatzHalter + " WHERE e.email LIKE :txt" + " "
						+ getStandardOrderString());

		q.setParameter("txt", email);
		@SuppressWarnings("unchecked")
		List<FlUser> res = q.getResultList();
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.persistence.dao.UserDAOLocal#getUserByEmailAndPw(
	 * java.lang.String)
	 */
	@Override
	public List<FlUser> findUserByEmailAndPw(String email, String pw) {
		Query q = getEntityManager().createQuery(
				"SELECT " + entityPlatzHalter + " FROM "
						+ FlUser.class.getSimpleName() + " "
						+ entityPlatzHalter
						+ " WHERE e.email = :txt And e.password = :pw" + " "
						+ getStandardOrderString());

		q.setParameter("txt", email);
		q.setParameter("pw", pw);
		@SuppressWarnings("unchecked")
		List<FlUser> res = q.getResultList();
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.persistence.dao.UserDAOLocal#loadUservars(de.onespark
	 * .backend.persistence.entities.userdb.User)
	 */
	@Override
	public FlUser loadUservars(FlUser user) {
		Query q = getEntityManager().createQuery(
				"SELECT e FROM Uservar e WHERE e.primaryKey.id = :id");
		q.setParameter("id", user.getId());
		@SuppressWarnings("unchecked")
		List<Uservar> res = q.getResultList();
		Map<String, String> userVars = new HashMap<String, String>();
		user.setUserVars(userVars);
		if (res.size() > 0) {
			for (Uservar uv : res) {
				userVars.put(uv.getPrimaryKey().getName(), uv.getValue());
			}

		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.frontline.persistence.dao.UserDAOLocal#deleteUser(de.frontline.backend
	 * .persistence.entities.userdb.FlUser)
	 */
	@Override
	public void deleteUser(AddressDTO adr) throws DbException {
		FlUser userToDelete = null;
		if (adr == null) {
			throw new DbException("Adresse == null");
		}
		if (adr.getUuid() != null && adr.getUuid().length() > 0) {
			AddressDTO ad = findByUuid(adr.getUuid());
			userToDelete = findById(ad.getId(), getEntityManager());
		} else if (adr.getId() > 0) {
			userToDelete = findById(adr.getId(), getEntityManager());
		}
		if (userToDelete != null && userToDelete.getId() > 0) {
			// uservars zuerst löschen
			Query q = getEntityManager().createQuery(
					"DELETE " + " FROM " + "Uservar v"
							+ " WHERE v.primaryKey.id = :id");
			q.setParameter("id", userToDelete.getId());
			int rows = q.executeUpdate();
			logger.info(rows + " Uservars gelöscht für UserId= "
					+ userToDelete.getId());
			this.removeEntity(userToDelete, getEntityManager());
			logger.info("User gelöscht mit UserId= " + userToDelete.getId());
		} else {
			throw new DbException("kein objekt zum löschen gefunden");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.persistence.dao.UserDAOLocal#genAddressDTO(de.onespark
	 * .backend.persistence.entities.userdb.FlUser)
	 */
	@Override
	public AddressDTO genAddressDTO(FlUser flu_in) throws DbException {
		if (flu_in == null || flu_in.getId() == 0) {
			throw new DbException("User oder User.id darf nicht null sein");
		}
		if (flu_in.getUserVars() == null || flu_in.getUserVars().size() == 0) {
			flu_in = this.loadUservars(flu_in);
		}
		AddressDTO adr = new AddressDTO();
		adr.setId(flu_in.getId());

		adr.setEmail(flu_in.getEmail());
		// TODO
		// user.setPasswort(flu_in.getPassword());
		adr.setName(flu_in.getUserVars().containsKey(NACHNAME_KEY) ? flu_in
				.getUserVars().get(NACHNAME_KEY) : null);
		adr.setVorname(flu_in.getUserVars().containsKey(VORNAME_KEY) ? flu_in
				.getUserVars().get(VORNAME_KEY) : null);
		adr.setFirma(flu_in.getUserVars().containsKey(FIRMA_KEY) ? flu_in
				.getUserVars().get(FIRMA_KEY) : null);
		adr.setPlz(flu_in.getUserVars().containsKey(PLZ_KEY) ? flu_in
				.getUserVars().get(PLZ_KEY) : null);
		adr.setStadt(flu_in.getUserVars().containsKey(STADT_KEY) ? flu_in
				.getUserVars().get(STADT_KEY) : null);
		adr.setStrasse(flu_in.getUserVars().containsKey(STRASSE_KEY) ? flu_in
				.getUserVars().get(STRASSE_KEY) : null);
		adr.setTelefon(flu_in.getUserVars().containsKey(TEL_KEY) ? flu_in
				.getUserVars().get(TEL_KEY) : null);
		adr.setMobilTelefon(flu_in.getUserVars().containsKey(MOBILE_KEY) ? flu_in
				.getUserVars().get(MOBILE_KEY) : null);
		adr.setAbteilung(flu_in.getUserVars().containsKey(ABTEILUNG_KEY) ? flu_in
				.getUserVars().get(ABTEILUNG_KEY) : null);
		adr.setTitel(flu_in.getUserVars().containsKey(TITLE_KEY) ? flu_in
				.getUserVars().get(TITLE_KEY) : null);
		if (flu_in.getUserVars().containsKey(LAND_KEY)) {
			LandDTO land = new LandDTO();
			land.setPostCode(flu_in.getUserVars().get(LAND_KEY));
		}

		adr.setMandant(0);
		try {
			adr.setMandant(flu_in.getUserVars().containsKey(MANDANT_KEY) ? Integer
					.parseInt(flu_in.getUserVars().get(MANDANT_KEY)) : 0);
		} catch (NumberFormatException e) {
		}

		// gibt es bereits eine UUID ??
		if (!flu_in.getUserVars().containsKey(UUID_KEY)
				|| flu_in.getUserVars().get(UUID_KEY) == null
				|| flu_in.getUserVars().get(UUID_KEY).length() == 0) {
			String uuid = UUID.randomUUID().toString();
			adr.setUuid(uuid);
			storeUuid(adr);

		} else {
			adr.setUuid(flu_in.getUserVars().get(UUID_KEY));

			logger.info("created UUID for " + adr);
		}

		// user.setKundenId(flu_in.getUserVars().containsKey(KUNDEN_KEY) ?
		// flu_in
		// .getUserVars().get(KUNDEN_KEY) : null);
		return adr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.persistence.dao.UserDAOLocal#createFlUser(de.onespark
	 * .backend.persistence.entities.userdb.FlUser)
	 */
	@Override
	public FlUser createFlUser(FlUser user) throws DbException {
		if (user == null || user.getEmail() == null
				|| user.getPassword() == null) {
			throw new DbException("User oder email == null");
		}
		List<FlUser> userListe = findUserByEmail(user.getEmail());
		if (userListe != null && userListe.size() > 0) {
			throw new DbException("User mit email=" + user.getEmail()
					+ " schon vorhanden");
		}
		getEntityManager().persist(user);
		userListe = findUserByEmail(user.getEmail());
		if (userListe == null || userListe.size() == 0) {
			throw new DbException("User mit email=" + user.getEmail()
					+ " wurde nicht angelegt");
		}
		return userListe.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.persistence.dao.UserDAOLocal#storeUservars(at.cyberlab
	 * .server.rest.dto.AddressDTO)
	 */
	@Override
	public void storeUservars(AddressDTO adr) {
		if (adr == null || adr.getId() == 0) {
			throw new IllegalArgumentException("User == null oder User.Id == 0");
		}

		FlUser flu = findById(adr.getId(), getEntityManager());
		if (flu == null || flu.getId() == 0) {
			throw new IllegalArgumentException("FlUser mit der ID="
					+ adr.getId() + " nicht gefunden");
		}

		Uservar uvName = storeUserVar(flu.getId(), NACHNAME_KEY, adr.getName());
		Uservar uvVorname = storeUserVar(flu.getId(), VORNAME_KEY,
				adr.getVorname());
		Uservar uvFirma = storeUserVar(flu.getId(), FIRMA_KEY, adr.getFirma());
		Uservar uvPlz = storeUserVar(flu.getId(), PLZ_KEY, adr.getPlz());
		Uservar uvStadt = storeUserVar(flu.getId(), STADT_KEY, adr.getStadt());
		Uservar uvLand = storeUserVar(flu.getId(), LAND_KEY, adr.getLand()
				.getPostCode());
		Uservar uvStrasse = storeUserVar(flu.getId(), STRASSE_KEY,
				adr.getStrasse());
		Uservar uvTel = storeUserVar(flu.getId(), TEL_KEY, adr.getTelefon());
		Uservar uvMobile = storeUserVar(flu.getId(), MOBILE_KEY,
				adr.getMobilTelefon());
		Uservar uvAbteilung = storeUserVar(flu.getId(), ABTEILUNG_KEY,
				adr.getAbteilung());
		Uservar uvTitle = storeUserVar(flu.getId(), TITLE_KEY, adr.getTitel());
		Uservar uvUuid = storeUserVar(flu.getId(), UUID_KEY, adr.getUuid());
		String mandant = "0";
		if (adr.getMandant() != null) {
			mandant = adr.getMandant().toString();
		}
		Uservar uman = storeUserVar(flu.getId(), MANDANT_KEY, mandant);
		// TODO
		// Uservar uvKunde = storeUserVar(flu.getId(), KUNDEN_KEY,
		// user.getKundenId());
	}

	private void storeUuid(AddressDTO adr) {
		if (adr == null || adr.getUuid() == null) {
			throw new IllegalArgumentException(
					"Adresse == null oder Adfesse.Uuid == null");
		}
		Uservar uvUuid = storeUserVar(adr.getId(), UUID_KEY, adr.getUuid());
	}

	/**
	 * Helferroutine um die Uservasr in der DB abzuspeichern
	 * 
	 * @param flu
	 *            FlUser
	 */
	private Uservar storeUserVar(int id, String key, String value) {

		// um DB null constrains einzuhalten
		if (id == 0 || key == null | value == null) {
			return null;
		}

		UservarPK pk = new UservarPK(id, key);
		Uservar uv = getEntityManager().find(Uservar.class, pk);
		if (uv == null) {
			uv = new Uservar();
			uv.setPrimaryKey(pk);
			uv.setValue(value);
			getEntityManager().persist(uv);
		} else {
			uv.setValue(value);
		}
		return uv;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.onespark.backend.persistence.dao.UserDAOLocal#updateUser(at.cyberlab
	 * .server.rest.dto.AddressDTO)
	 */
	@Override
	public AddressDTO updateUser(AddressDTO adr) {
		if (adr == null || adr.getId() == 0) {
			throw new IllegalArgumentException("User == null oder User.Id == 0");
		}
		FlUser flu = findById(adr.getId(), getEntityManager());
		if (flu == null) {
			throw new IllegalArgumentException("User mit der id=" + adr.getId()
					+ " nicht gefunden");
		}
		flu.setEmail(adr.getEmail());
		storeUservars(adr);
		return adr;
	}

}
