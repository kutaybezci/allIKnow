package com.kutaybezci.allIKnow;

import java.io.File;

import com.kutaybezci.allIKnow.types.Person;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

public class Deneme {

	public static void main(String[] args) throws DatabaseException {
		Environment environment=null;
		Database denemeDb=null;
		EntityStore personStore=null;
		
		try {
			EnvironmentConfig environmentConfig = new EnvironmentConfig();
			environmentConfig.setAllowCreate(true);
			File envHome = new File("/home/kutay/development/database/deneme");
			environment = new Environment(envHome, environmentConfig);
			/*DatabaseConfig dbConfig=new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			denemeDb = environment.openDatabase(null, "denemeDb", dbConfig);*/
			Person person=new Person();
			person.setFirstName("kutay");
			person.setSurname("bezci");
			person.setNick("kutay.bezci");
			StoreConfig storeConfig=new StoreConfig();
			storeConfig.setAllowCreate(true);
			personStore=new EntityStore(environment, "personStore", storeConfig);
			PrimaryIndex<String, Person> pk=personStore.getPrimaryIndex(String.class, Person.class);
			System.out.println("1-)"+pk.count());
			pk.put(person);
			System.out.println("2-)"+pk.count());
			pk.delete(person.getNick());
			System.out.println("3-)"+pk.count());
			
		} catch (Throwable ex) {
			ex.printStackTrace();
		}finally {
			if(personStore!=null) {
				personStore.close();
			}
			if(denemeDb!=null) {
				denemeDb.close();
			}
			if(environment!=null) {
				environment.close();
			}

		}

	}

}
