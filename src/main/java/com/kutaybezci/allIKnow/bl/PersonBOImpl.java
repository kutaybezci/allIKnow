package com.kutaybezci.allIKnow.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kutaybezci.allIKnow.types.Person;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

@Service
public class PersonBOImpl implements PersonBO {
	@Autowired
	private EntityStore entityStore;
	@Autowired
	private Environment dbEnv;
	@Autowired
	private Locale locale;
	@Autowired
	private PrimaryIndex<Long, Person> personPK;

	@Override
	public Long createPerson(Person person) {
		Transaction transaction = null;
		try {
			transaction = dbEnv.beginTransaction(null, null);
			person.setFirstName(person.getFirstName().toUpperCase(locale));
			person.setSurname(person.getSurname().toUpperCase(locale));
			person.setNick(person.getNick().toUpperCase(locale));
			personPK.putNoOverwrite(person);
			transaction.commit();
			return person.getId();
		} catch (Exception ex) {
			if (transaction != null) {
				try {
					transaction.abort();
				} catch (DatabaseException e) {
					throw new RuntimeException(ex);
				}
			}
			throw new RuntimeException(ex);
		}
	}

	@Override
	public Person readPersonByNick(String nick) {
		EntityCursor<Person> cursor = null;
		try {
			nick = nick.toUpperCase(locale);
			SecondaryIndex<String, Long, Person> nickIndex = entityStore.getSecondaryIndex(personPK, String.class,
					"nick");
			return nickIndex.get(nick);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (DatabaseException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Override
	public void updatePerson(Person person) {
		Transaction transaction = null;
		try {
			transaction = dbEnv.beginTransaction(null, null);
			person.setFirstName(person.getFirstName().toUpperCase(locale));
			person.setSurname(person.getSurname().toUpperCase(locale));
			person.setNick(person.getNick().toUpperCase(locale));
			personPK.put(person);
			transaction.commit();
		} catch (Exception ex) {
			if (transaction != null) {
				try {
					transaction.abort();
				} catch (DatabaseException e) {
					throw new RuntimeException(e);
				}
			}
			throw new RuntimeException(ex);
		}

	}

	@Override
	public List<Person> readAllPerson() {
		EntityCursor<Person> allPerson = null;
		List<Person> all = new ArrayList<>();
		try {
			allPerson = personPK.entities();
			for (Person p : allPerson) {
				all.add(p);
			}
			return all;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (allPerson != null) {
				try {
					allPerson.close();
				} catch (DatabaseException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}