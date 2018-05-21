package com.kutaybezci.allIKnow.bl;

import com.kutaybezci.allIKnow.types.Person;


public interface PersonBO {
	Long createPerson(Person person);
	Person readPersonByNick(String nick);
	void updatePerson(Person person);	
}
