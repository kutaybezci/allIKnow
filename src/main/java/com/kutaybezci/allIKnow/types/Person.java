package com.kutaybezci.allIKnow.types;

import java.util.Date;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

import lombok.Data;

@Data
@Entity
public class Person {
	@PrimaryKey(sequence="personSeq")
	private long id;
	@SecondaryKey(name="nick", relate=Relationship.ONE_TO_ONE)
	private String nick;
	private String firstName;
	@SecondaryKey(name="surname",relate=Relationship.MANY_TO_ONE)
	private String surname;
	private String email;
	private String phone;
	@SecondaryKey(name="birthDate",relate=Relationship.MANY_TO_ONE)
	private Date birthDate;
	@SecondaryKey(name="deathDate",relate=Relationship.MANY_TO_ONE)
	private Date deathDate;
	private Gender gender;
}
/*
 
Relationship	Field type	Key type	Example
Relationship.ONE_TO_ONE	Singular	Unique	A person record with a unique social security number key.
Relationship.MANY_TO_ONE	Singular	Duplicates	A person record with a non-unique employer key.
Relationship.ONE_TO_MANY	Set/Collection/array	Unique	A person record with multiple unique email address keys.
Relationship.MANY_TO_MANY	Set/Collection/array	Duplicates	A person record with multiple non-unique organization keys.

 */
