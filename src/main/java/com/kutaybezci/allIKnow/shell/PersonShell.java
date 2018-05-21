package com.kutaybezci.allIKnow.shell;

import java.text.ParseException;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.kutaybezci.allIKnow.bl.PersonBO;
import com.kutaybezci.allIKnow.types.Gender;
import com.kutaybezci.allIKnow.types.Person;


@ShellComponent
public class PersonShell {
	@Autowired
	private PersonBO personBO;
	
	private static final String DATE_FORMAT[] = { "dd.MM.yyyy" };
	private static final String DEFAULT_VALUE = "NOT_SET";

	public static boolean isSet(String input) {
		return !StringUtils.equals(DEFAULT_VALUE, input);
	}

	@ShellMethod("create person")
	public String createPerson(String nick,//
			String name, //
			String surname, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String birthDate, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String deathDate, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String gender, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String email, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String phone) throws ParseException {
		Person person = new Person();
		personUpdate(nick, name, surname, birthDate, deathDate, gender, email, phone, person);
		personBO.createPerson(person);
		return String.format("%s is created", person.getNick());
	}

	@ShellMethod("update person")
	public String updatePerson(String nick,//
			@ShellOption(defaultValue = DEFAULT_VALUE) String name, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String surname, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String birthDate, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String deathDate, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String gender, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String email, //
			@ShellOption(defaultValue = DEFAULT_VALUE) String phone,
			@ShellOption(defaultValue = DEFAULT_VALUE) String newNick) throws ParseException {
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
		if(isSet(nick)) {
			person.setNick(nick);
		}
		
		if (isSet(name)) {
			person.setFirstName(name);
		}
		if (isSet(surname)) {
			person.setSurname(surname);
		}
		if (isSet(birthDate)) {
			person.setBirthDate(DateUtils.parseDate(birthDate, DATE_FORMAT));
		}
		if (isSet(deathDate)) {
			person.setBirthDate(DateUtils.parseDate(deathDate, DATE_FORMAT));
		}
		if (isSet(email)) {
			person.setEmail(email);
		}
		if (isSet(gender)) {
			gender=gender.toUpperCase();
			if(ArrayUtils.contains(new String[]{"MALE","M","E"}, gender)) {
				person.setGender(Gender.Male);
			}else if(ArrayUtils.contains(new String[]{"FEMALE","F","K"}, gender)){
				person.setGender(Gender.Female);
			}else {
				throw new RuntimeException(String.format("%s is not a gender", gender));
			}
		}
		if (isSet(phone)) {
			person.setPhone(phone);
		}
	}
}
