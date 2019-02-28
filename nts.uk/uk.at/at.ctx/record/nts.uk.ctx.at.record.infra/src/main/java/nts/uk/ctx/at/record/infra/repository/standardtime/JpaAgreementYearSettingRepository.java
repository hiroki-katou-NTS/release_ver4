package nts.uk.ctx.at.record.infra.repository.standardtime;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
//import nts.arc.time.YearMonth;
//import nts.uk.ctx.at.record.dom.standardtime.AgreementMonthSetting;
import nts.uk.ctx.at.record.dom.standardtime.AgreementYearSetting;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementYearSettingRepository;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementYearSetting;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementYearSettingPK;
//import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgreementMonthSet;

@Stateless
public class JpaAgreementYearSettingRepository extends JpaRepository implements AgreementYearSettingRepository {

	private static final String FIND;

	private static final String FIND_BY_KEY;
	private static final String DEL_BY_KEY;

	private static final String IS_EXIST_DATA;
	private static final String FIND_BY_ID;
	private static final String FIND_BY_IDS;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgeementYearSetting a ");
		builderString.append("WHERE a.kmkmtAgeementYearSettingPK.employeeId = :employeeId ");
		builderString.append("ORDER BY a.kmkmtAgeementYearSettingPK.yearValue DESC ");
		FIND = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgeementYearSetting a ");
		builderString.append("WHERE a.kmkmtAgeementYearSettingPK.employeeId = :employeeId ");
		builderString.append("AND a.kmkmtAgeementYearSettingPK.yearValue = :yearValue ");
		FIND_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KmkmtAgeementYearSetting a ");
		builderString.append("WHERE a.kmkmtAgeementYearSettingPK.employeeId = :employeeId ");
		builderString.append("AND a.kmkmtAgeementYearSettingPK.yearValue = :yearValue ");
		DEL_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM KmkmtAgeementYearSetting a ");
		builderString.append("WHERE a.kmkmtAgeementYearSettingPK.employeeId = :employeeId ");
		builderString.append("AND a.kmkmtAgeementYearSettingPK.yearValue = :yearValue ");
		IS_EXIST_DATA = builderString.toString();
		
		// fix bug 100605
		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgeementYearSetting a ");
		builderString.append("WHERE a.kmkmtAgeementYearSettingPK.employeeId = :employeeId ");
		builderString.append("AND a.kmkmtAgeementYearSettingPK.yearValue = :yearValue ");
		builderString.append("ORDER BY a.kmkmtAgeementYearSettingPK.yearValue DESC ");
		FIND_BY_ID = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgeementYearSetting a ");
		builderString.append("WHERE a.kmkmtAgeementYearSettingPK.employeeId IN :employeeIds ");
		builderString.append("AND a.kmkmtAgeementYearSettingPK.yearValue = :yearValue ");
		FIND_BY_IDS = builderString.toString();
	}

	@Override
	public List<AgreementYearSetting> find(String employeeId) {
		return this.queryProxy().query(FIND, KmkmtAgeementYearSetting.class).setParameter("employeeId", employeeId)
				.getList(f -> toDomain(f));
	}
	
	@Override
	public Optional<AgreementYearSetting> findByKey(String employeeId, int yearMonth) {
		return this.queryProxy().query(FIND_BY_ID, KmkmtAgeementYearSetting.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearValue", yearMonth)
				.getSingle(f -> toDomain(f));
	}

	@Override
	public List<AgreementYearSetting> findByKey(List<String> employeeIds, int yearMonth) {
		if (employeeIds == null || employeeIds.isEmpty()) return Collections.emptyList();
		return this.queryProxy().query(FIND_BY_IDS, KmkmtAgeementYearSetting.class)
				.setParameter("employeeIds", employeeIds)
				.setParameter("yearValue", yearMonth)
				.getList(f -> toDomain(f));
	}

	@Override
	public void add(AgreementYearSetting agreementYearSetting) {
		this.commandProxy().insert(toEntity(agreementYearSetting));
	}

	@Override
	public void update(AgreementYearSetting agreementYearSetting) {

		Optional<KmkmtAgeementYearSetting> entity = this.queryProxy().query(FIND_BY_KEY, KmkmtAgeementYearSetting.class)
				.setParameter("employeeId", agreementYearSetting.getEmployeeId())
				.setParameter("yearValue", agreementYearSetting.getYearValue()).getSingle();
		
		if (entity.isPresent()) {
			KmkmtAgeementYearSetting data = entity.get();
			data.errorOneYear = new BigDecimal(agreementYearSetting.getErrorOneYear().valueAsMinutes());
			data.alarmOneYear = new BigDecimal(agreementYearSetting.getAlarmOneYear().valueAsMinutes());
			
			this.commandProxy().update(data);
		}

	}
	
	// fix bug 100605
		@Override
		public void updateById(AgreementYearSetting agreementYearSetting, Integer yearMonthValueOld) {

			Optional<KmkmtAgeementYearSetting> entity = this.queryProxy().query(FIND_BY_ID, KmkmtAgeementYearSetting.class)
					.setParameter("employeeId", agreementYearSetting.getEmployeeId())
					.setParameter("yearValue", yearMonthValueOld).getSingle();
			
			if (entity.isPresent()) {
				KmkmtAgeementYearSetting data = entity.get();
				this.delete(data.kmkmtAgeementYearSettingPK.employeeId, yearMonthValueOld);
				this.add(agreementYearSetting);
			}

		}

	@Override
	public void delete(String employeeId, int yearValue) {
		this.getEntityManager().createQuery(DEL_BY_KEY).setParameter("employeeId", employeeId)
				.setParameter("yearValue", yearValue).executeUpdate();
	}

	@Override
	public boolean checkExistData(String employeeId, BigDecimal yearValue) {
		return this.queryProxy().query(IS_EXIST_DATA, long.class).setParameter("employeeId", employeeId)
				.setParameter("yearValue", yearValue).getSingle().get() > 0;
	}

	private static AgreementYearSetting toDomain(KmkmtAgeementYearSetting kmkmtAgeementYearSetting) {
		AgreementYearSetting agreementYearSetting = AgreementYearSetting.createFromJavaType(
				kmkmtAgeementYearSetting.kmkmtAgeementYearSettingPK.employeeId,
				kmkmtAgeementYearSetting.kmkmtAgeementYearSettingPK.yearValue.intValue(),
				kmkmtAgeementYearSetting.errorOneYear.intValue(), kmkmtAgeementYearSetting.alarmOneYear.intValue());
		return agreementYearSetting;
	}

	private KmkmtAgeementYearSetting toEntity(AgreementYearSetting agreementYearSetting) {
		val entity = new KmkmtAgeementYearSetting();

		entity.kmkmtAgeementYearSettingPK = new KmkmtAgeementYearSettingPK();
		entity.kmkmtAgeementYearSettingPK.employeeId = agreementYearSetting.getEmployeeId();
		entity.kmkmtAgeementYearSettingPK.yearValue = new BigDecimal(agreementYearSetting.getYearValue());
		entity.alarmOneYear = new BigDecimal(agreementYearSetting.getAlarmOneYear().v());
		entity.errorOneYear = new BigDecimal(agreementYearSetting.getErrorOneYear().v());

		return entity;
	}
}
