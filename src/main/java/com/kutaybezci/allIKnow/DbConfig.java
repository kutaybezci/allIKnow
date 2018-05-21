package com.kutaybezci.allIKnow;

import java.io.File;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kutaybezci.allIKnow.types.Person;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.evolve.IncompatibleClassException;

@Configuration
public class DbConfig {
	@Value("${allIKnow.dbPath}")
	private String dbPath;
	@Value("${allIKnow.locale}")
	private String locale;

	@Bean
	public PrimaryIndex<Long, Person> personPK(EntityStore entityStore) throws DatabaseException{
		return entityStore.getPrimaryIndex(Long.class, Person.class);
	}
	
	
	@Bean
	public Locale locale() {
		return Locale.forLanguageTag(locale);
	}

	@Bean
	public Environment dbEnv() throws DatabaseException {
		EnvironmentConfig environmentConfig = new EnvironmentConfig();
		environmentConfig.setAllowCreate(true);
		environmentConfig.setTransactional(true);
		File envHome = new File(dbPath);
		if (!envHome.exists()) {
			envHome.mkdirs();
		}
		Environment environment = new Environment(envHome, environmentConfig);
		return environment;
	}

	@Bean
	public EntityStore entityStore(Environment environment) throws IncompatibleClassException, DatabaseException {
		StoreConfig storeConfig = new StoreConfig();
		storeConfig.setAllowCreate(true);
		storeConfig.setTransactional(true);
		EntityStore entityStore = new EntityStore(environment, "allIKnowStore", storeConfig);
		return entityStore;
	}

}
