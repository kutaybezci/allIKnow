package com.kutaybezci.allIKnow.shell;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.Table;

import com.kutaybezci.allIKnow.bl.PersonBO;
import com.kutaybezci.allIKnow.types.Gender;
import com.kutaybezci.allIKnow.types.Person;


@ShellComponent
public class PersonShell {
	@Autowired
	private PersonBO personBO;
	


	@ShellMethod("create person")
	public String createPerson(String nick,//
			String name, //
			String surname, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String birthDate, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String deathDate, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String gender, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String email, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String phone) throws ParseException {
		Person person = new Person();
		personUpdate(nick, name, surname, birthDate, deathDate, gender, email, phone, person);
		personBO.createPerson(person);
		return String.format("%s is created", person.getNick());
	}

	@ShellMethod("update person")
	public String updatePerson(String nick,//
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String name, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String surname, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String birthDate, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String deathDate, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String gender, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String email, //
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String phone,
			@ShellOption(defaultValue = ShellUtils.DEFAULT_VALUE) String newNick) throws ParseException {
		Person person = personBO.readPersonByNick(nick);
		personUpdate(newNick, name, surname, birthDate, deathDate, gender, email, phone, person);
		personBO.updatePerson(person);
		return String.format("%s updated", person.getNick());
	}
	
	@ShellMethod("display person by nick")
	public String displayPerson(String nick) throws ParseException {
		Person person = personBO.readPersonByNick(nick);
		if(person==null) {
			return String.format("%s not found", nick);
		}
		return person.toString();
	}

	private void personUpdate(String nick, String name, String surname, String birthDate, String deathDate, String gender,
			String email, String phone, Person person) throws ParseException {
		if(ShellUtils.isSet(nick)) {
			person.setNick(nick);
		}
		
		if (ShellUtils.isSet(name)) {
			person.setFirstName(name);
		}
		if (ShellUtils.isSet(surname)) {
			person.setSurname(surname);
		}
		if (ShellUtils.isSet(birthDate)) {
			person.setBirthDate(DateUtils.parseDate(birthDate, ShellUtils.DATE_FORMAT));
		}
		if (ShellUtils.isSet(deathDate)) {
			person.setBirthDate(DateUtils.parseDate(deathDate, ShellUtils.DATE_FORMAT));
		}
		if (ShellUtils.isSet(email)) {
			person.setEmail(email);
		}
		if (ShellUtils.isSet(gender)) {
			gender=gender.toUpperCase();
			if(ArrayUtils.contains(new String[]{"MALE","M","E"}, gender)) {
				person.setGender(Gender.Male);
			}else if(ArrayUtils.contains(new String[]{"FEMALE","F","K"}, gender)){
				person.setGender(Gender.Female);
			}else {
				throw new RuntimeException(String.format("%s is not a gender", gender));
			}
		}
		if (ShellUtils.isSet(phone)) {
			person.setPhone(phone);
		}
	}
	@ShellMethod("display phone book")
	public Table myPhoneBook() {
		List<Person> all=personBO.readAllPerson();
		List<Person> withPhones=new ArrayList<>();
		for(Person p: all) {
			if(!StringUtils.isBlank(p.getPhone())) {
				withPhones.add(p);
			}
		}
		String [][] phoneBook=new String[withPhones.size()][3];
		for(int i=0; i<withPhones.size(); i++) {
			Person p=withPhones.get(i);
			phoneBook[i][0]=p.getNick();
			phoneBook[i][1]=p.getFirstName()+" "+p.getSurname();
			phoneBook[i][2]=p.getPhone();
		}
		return ShellUtils.table(phoneBook);
	}
	
}
