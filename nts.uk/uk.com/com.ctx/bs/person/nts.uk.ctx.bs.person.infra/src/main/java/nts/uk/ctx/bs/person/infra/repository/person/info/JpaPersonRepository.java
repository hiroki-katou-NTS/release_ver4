/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.person.infra.repository.person.info;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.person.dom.person.info.Person;
import nts.uk.ctx.bs.person.dom.person.info.PersonRepository;
import nts.uk.ctx.bs.person.infra.entity.person.info.BpsmtPerson;
import nts.uk.ctx.bs.person.infra.entity.person.info.BpsmtPersonPk;

/**
 * The Class JpaPersonRepository.
 */
@Stateless
public class JpaPersonRepository extends JpaRepository implements PersonRepository {
	public static final String SELECT_NO_WHERE = "SELECT c FROM BpsmtPerson c";

	public static final String SELECT_BY_PERSON_IDS = SELECT_NO_WHERE + " WHERE c.bpsmtPersonPk.pId IN :pids";

	public static final String GET_LAST_CARD_NO = "SELECT c.cardNumberLetter FROM BpsstUserSetting c "

			+ " WHERE c.companyId = :companyId AND c.cardNumberLetter LIKE CONCAT(:cardNo, '%')"
			+ " ORDER BY  c.cardNumberLetter DESC";

	private static Person toDomain(BpsmtPerson entity) {
		if (entity.gender == 0) {
			entity.gender = 1;
		}

		Person domain = Person.createFromJavaType(entity.bpsmtPersonPk.pId, entity.birthday, entity.bloodType,
				entity.gender, entity.businessName, entity.personName, entity.personNameKana);
		return domain;
	}
	
	private static Person toFullPersonDomain(BpsmtPerson entity) {
		if (entity.gender == 0) {
			entity.gender = 1;
		}

		Person domain = Person.createFromJavaType(entity.birthday, entity.bloodType, entity.gender,
				entity.bpsmtPersonPk.pId, entity.businessName, entity.businessNameKana, entity.personName,
				entity.personNameKana, entity.businessOtherName, entity.businessEnglishName, entity.personRomanji,
				entity.personRomanjiKana, entity.todokedeFullName, entity.todokedeFullNameKana, entity.oldName,
				entity.oldNameKana, entity.perNameMultilLang, entity.perNameMultilLangKana);
		return domain;
	}

	private BpsmtPerson toEntity(Person domain) {
		BpsmtPerson entity = new BpsmtPerson();
		entity.bpsmtPersonPk = new BpsmtPersonPk(domain.getPersonId());
		entity.birthday = domain.getBirthDate();
		entity.bloodType = domain.getBloodType() == null ? null : domain.getBloodType().value;
		entity.gender = domain.getGender() == null ? 0 : domain.getGender().value;

		entity.personName = domain.getPersonNameGroup().getPersonName().getFullName().v();

		entity.personNameKana = domain.getPersonNameGroup().getPersonName().getFullNameKana().v();

		entity.businessEnglishName = domain.getPersonNameGroup().getBusinessEnglishName() == null ? null
				: domain.getPersonNameGroup().getBusinessEnglishName().v();
		entity.businessOtherName = domain.getPersonNameGroup().getBusinessOtherName() == null ? null
				: domain.getPersonNameGroup().getBusinessOtherName().v();

		entity.businessName = domain.getPersonNameGroup().getBusinessName().v();

		entity.businessNameKana = domain.getPersonNameGroup().getBusinessNameKana() == null ? null
				: domain.getPersonNameGroup().getBusinessNameKana().v();

		entity.oldName = domain.getPersonNameGroup().getOldName() == null ? null
				: domain.getPersonNameGroup().getOldName().getFullName().v();

		entity.oldNameKana = domain.getPersonNameGroup().getOldName() == null ? null
				: domain.getPersonNameGroup().getOldName().getFullNameKana().v();

		entity.personRomanji = domain.getPersonNameGroup().getPersonRomanji() == null ? null
				: domain.getPersonNameGroup().getPersonRomanji().getFullName().v();

		entity.personRomanjiKana = domain.getPersonNameGroup().getPersonRomanji() == null ? null
				: domain.getPersonNameGroup().getPersonRomanji().getFullNameKana().v();

		entity.todokedeFullName = domain.getPersonNameGroup().getTodokedeFullName() == null ? null
				: domain.getPersonNameGroup().getTodokedeFullName().getFullName().v();
		entity.todokedeFullNameKana = domain.getPersonNameGroup().getTodokedeFullName() == null ? null
				: domain.getPersonNameGroup().getTodokedeFullName().getFullNameKana().v();

		entity.perNameMultilLang = domain.getPersonNameGroup().getPersonalNameMultilingual() == null ? null
				: domain.getPersonNameGroup().getPersonalNameMultilingual().getFullName().v();

		entity.perNameMultilLangKana = domain.getPersonNameGroup().getPersonalNameMultilingual() == null ? null
				: domain.getPersonNameGroup().getPersonalNameMultilingual().getFullNameKana().v();
		return entity;
	}

	/**
	 * update entity
	 * 
	 * @param domain
	 * @param entity
	 * @return
	 */
	private void updateEntity(Person domain, BpsmtPerson entity) {
		entity.birthday = domain.getBirthDate();
		entity.bloodType = domain.getBloodType() == null ? null :domain.getBloodType().value;
		entity.gender = domain.getGender() == null ? 0 : domain.getGender().value;
		if(domain.getPersonNameGroup().getPersonName().getFullName().v() != "") {
			entity.personName = domain.getPersonNameGroup().getPersonName().getFullName().v();
		}
		entity.personNameKana = domain.getPersonNameGroup().getPersonName().getFullNameKana().v();
		
		entity.businessEnglishName = domain.getPersonNameGroup().getBusinessEnglishName() == null ? null
				: domain.getPersonNameGroup().getBusinessEnglishName().v();
		
		entity.businessOtherName = domain.getPersonNameGroup().getBusinessOtherName() == null ? null
				: domain.getPersonNameGroup().getBusinessOtherName().v();
		
		entity.businessName = domain.getPersonNameGroup().getBusinessName().v();
		
		entity.businessNameKana = domain.getPersonNameGroup().getBusinessNameKana() == null ? null
				: domain.getPersonNameGroup().getBusinessNameKana().v();
		
		entity.oldName = domain.getPersonNameGroup().getOldName() == null ? null
				: domain.getPersonNameGroup().getOldName().getFullName().v();
		
		entity.oldNameKana = domain.getPersonNameGroup().getOldName() == null ? null
				: domain.getPersonNameGroup().getOldName().getFullNameKana().v();
		
		entity.personRomanji = domain.getPersonNameGroup().getPersonRomanji() == null ? null
				: domain.getPersonNameGroup().getPersonRomanji().getFullName().v();
		
		entity.personRomanjiKana = domain.getPersonNameGroup().getPersonRomanji() == null ? null
				: domain.getPersonNameGroup().getPersonRomanji().getFullNameKana().v();
		
		entity.todokedeFullName = domain.getPersonNameGroup().getTodokedeFullName() == null ? null
				: domain.getPersonNameGroup().getTodokedeFullName().getFullName().v();
		
		entity.todokedeFullNameKana = domain.getPersonNameGroup().getTodokedeFullName() == null ? null
				: domain.getPersonNameGroup().getTodokedeFullName().getFullNameKana().v();
		
		entity.perNameMultilLang = domain.getPersonNameGroup().getPersonalNameMultilingual() == null ? null
				: domain.getPersonNameGroup().getPersonalNameMultilingual().getFullName().v();
		
		entity.perNameMultilLangKana = domain.getPersonNameGroup().getPersonalNameMultilingual() == null ? null
				: domain.getPersonNameGroup().getPersonalNameMultilingual().getFullNameKana().v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.basic.dom.person.PersonRepository#getPersonByPersonId(java.
	 * util.List)
	 */
	@Override
	public List<Person> getPersonByPersonIds(List<String> personIds) {

		// check exist input
		if (CollectionUtil.isEmpty(personIds)) {
			return new ArrayList<>();
		}

		List<Person> lstPerson = new ArrayList<>();
		CollectionUtil.split(personIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
			lstPerson.addAll(this.queryProxy().query(SELECT_BY_PERSON_IDS, BpsmtPerson.class)
				.setParameter("pids", subIdList).getList(f -> toDomain(f)));
		});
		return lstPerson;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.basic.dom.person.PersonRepository#getByPersonId(java.lang.
	 * String)
	 */
	@Override
	@SneakyThrows
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<Person> getByPersonId(String personId) {
		String sql = "select * from BPSMT_PERSON"
				+ " where PID = ?";
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
			stmt.setString(1, personId);
			return new NtsResultSet(stmt.executeQuery()).getSingle(r -> {
				BpsmtPerson e = new BpsmtPerson();
				e.bpsmtPersonPk = new BpsmtPersonPk(personId);
				e.birthday = r.getGeneralDate("BIRTHDAY");
				e.bloodType = r.getInt("BLOOD_TYPE");
				e.gender = r.getInt("GENDER");
				e.personName = r.getString("PERSON_NAME");
				e.personNameKana = r.getString("PERSON_NAME_KANA");
				e.businessName = r.getString("BUSINESS_NAME");
				e.businessNameKana = r.getString("BUSINESS_NAME_KANA");
				e.businessEnglishName = r.getString("BUSINESS_ENGLISH_NAME");
				e.businessOtherName = r.getString("BUSINESS_OTHER_NAME");
				e.personRomanji = r.getString("P_ROMANJI_FNAME");
				e.personRomanjiKana = r.getString("P_ROMANJI_FNAME_KANA");
				e.todokedeFullName = r.getString("TODOKEDE_FNAME");
				e.todokedeFullNameKana = r.getString("TODOKEDE_FNAME_KANA");
				e.oldName = r.getString("OLDNAME_FNAME");
				e.oldNameKana = r.getString("OLDNAME_FNAME_KANA");
				e.perNameMultilLang = r.getString("PERSON_NAME_MULTIL_LANG");
				e.perNameMultilLangKana = r.getString("PERSON_NAME_MULTIL_LANG_KANA");
				return toFullPersonDomain(e);
			});
		}
	}

	@Override
	public String getLastCardNo(String companyId, String startCardNoLetter) {
		
		startCardNoLetter = startCardNoLetter != null ? "" : startCardNoLetter;
		
		List<Object[]> lst = this.queryProxy().query(GET_LAST_CARD_NO).setParameter("companyId", companyId)
				.setParameter("cardNo", startCardNoLetter).getList();
		if (lst.isEmpty()) {
			return "";
		}
		return lst.get(0).toString();
	}

	/**
	 * Update person 取得した「個人」を更新する
	 * 
	 * @param person
	 */
	@Override
	public void update(Person person) {
		// Get entity
		Optional<BpsmtPerson> existItem = this.queryProxy().find(new BpsmtPersonPk(person.getPersonId()),
				BpsmtPerson.class);
		if (!existItem.isPresent()) {
			throw new RuntimeException("invalid Person");
		}
		// Update entity
		updateEntity(person, existItem.get());
		// Update person table
		this.commandProxy().update(existItem.get());
	}

	// sonnlb code start
	@Override
	public void addNewPerson(Person domain) {
		this.commandProxy().insert(toEntity(domain));
		this.getEntityManager().flush();

	}
	// sonnlb code end

}
