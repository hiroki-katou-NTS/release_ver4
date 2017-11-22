/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.person.dom.person.info;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.person.dom.person.info.fullnameset.FullNameSet;
import nts.uk.ctx.bs.person.dom.person.info.personnamegroup.BusinessEnglishName;
import nts.uk.ctx.bs.person.dom.person.info.personnamegroup.BusinessName;
import nts.uk.ctx.bs.person.dom.person.info.personnamegroup.BusinessOtherName;
import nts.uk.ctx.bs.person.dom.person.info.personnamegroup.PersonName;
import nts.uk.ctx.bs.person.dom.person.info.personnamegroup.PersonNameGroup;
import nts.uk.ctx.bs.person.dom.person.info.personnamegroup.BusinessNameKana;

/**
 * The Class Person.
 */
// 個人
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person extends AggregateRoot {

	/** The Birthday */
	// 生年月日
	private GeneralDate birthDate;

	/** The BloodType */
	// 血液型
	private BloodType bloodType;

	/** The Gender - 性別 */
	private GenderPerson gender;

	/** The person id - 個人ID */
	private String personId;


	/** The PersonNameGroup - 個人名グループ*/
	private PersonNameGroup personNameGroup;

//	public static Person createFromJavaType(String pId, String personName) {
//		return new Person(pId, new PersonNameGroup(new PersonName(personName)));
//	}
	
	//for required field
	public static Person createFromJavaType(String personId, GeneralDate birthDate, int bloodType, int gender, String businessName, String personName, String personNameKana) {
		return new Person(personId, birthDate, EnumAdaptor.valueOf(bloodType, BloodType.class),
				EnumAdaptor.valueOf(gender, GenderPerson.class), new BusinessName(businessName), new FullNameSet(personName, personNameKana));
	}
	public Person(String personId, PersonNameGroup personNameGroup) {
		super();
		this.personId = personId;
		this.personNameGroup = personNameGroup;
	}
	
	//constructor for required field
	public Person(String personId, GeneralDate birthDate, BloodType bloodType, GenderPerson gender, BusinessName businessName, FullNameSet personName){
		super();
		this.personId = personId;
		this.birthDate = birthDate;
		this.bloodType = bloodType;
		this.gender = gender;
		this.personNameGroup = new PersonNameGroup(personName, businessName);
	}
	
	// sonnlb code start

	public static Person createFromJavaType(GeneralDate birthDate, int bloodType, int gender, String personId,
			String businessName,String businessNameKana, String personName, String personNameKana, String businessOtherName,
			String businessEnglishName, String personRomanji, String personRomanjiKana,
			String todokedeFullName, String todokedeFullNameKana, String oldName, String oldNameKana,
			String PersonalNameMultilingual, String PersonalNameMultilingualKana) {
		FullNameSet personNameSet = new FullNameSet(personName, personNameKana);
		FullNameSet personRomanjiSet = new FullNameSet(personRomanji, personRomanjiKana);
		FullNameSet todokedeFullNameSet = new FullNameSet(todokedeFullName, todokedeFullNameKana);
		FullNameSet oldNameSet = new FullNameSet(oldName, oldNameKana);
		FullNameSet PersonalNameMultilingualSet = new FullNameSet(PersonalNameMultilingual, PersonalNameMultilingualKana);

		PersonNameGroup personNameGroup = new PersonNameGroup(new BusinessName(businessName), new BusinessNameKana(businessNameKana),
				new BusinessOtherName(businessOtherName),
				new BusinessEnglishName(businessEnglishName), personNameSet,PersonalNameMultilingualSet,  personRomanjiSet,
				todokedeFullNameSet, oldNameSet);

		return new Person(birthDate, EnumAdaptor.valueOf(bloodType, BloodType.class),
				EnumAdaptor.valueOf(gender, GenderPerson.class), personId, personNameGroup);
	}

	// sonnlb code end

}
