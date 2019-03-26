package nts.uk.ctx.at.record.infra.repository.standardtime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.standardtime.AgreementMonthSetting;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementMonthSettingRepository;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgreementMonthSet;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgreementMonthSetPK;

@Stateless
public class JpaAgreementMonthSettingRepository extends JpaRepository implements AgreementMonthSettingRepository {

	private static final String FIND;

	private static final String FIND_BY_KEY;

	private static final String FIND_BY_KEYS;

	private static final String DEL_BY_KEY;

	private static final String IS_EXIST_DATA;
	
	private static final String FIND_BY_ID;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgreementMonthSet a ");
		builderString.append("WHERE a.kmkmtAgreementMonthSetPK.employeeId = :employeeId ");
		builderString.append("ORDER BY a.kmkmtAgreementMonthSetPK.yearmonthValue DESC ");
		FIND = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgreementMonthSet a ");
		builderString.append("WHERE a.kmkmtAgreementMonthSetPK.employeeId = :employeeId ");
		builderString.append("AND a.kmkmtAgreementMonthSetPK.yearmonthValue = :yearmonthValue ");
		FIND_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgreementMonthSet a ");
		builderString.append("WHERE a.kmkmtAgreementMonthSetPK.employeeId IN :employeeIds ");
		builderString.append("AND a.kmkmtAgreementMonthSetPK.yearmonthValue IN :yearmonthValues ");
		FIND_BY_KEYS = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KmkmtAgreementMonthSet a ");
		builderString.append("WHERE a.kmkmtAgreementMonthSetPK.employeeId = :employeeId ");
		builderString.append("AND a.kmkmtAgreementMonthSetPK.yearmonthValue = :yearmonthValue ");
		DEL_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM KmkmtAgreementMonthSet a ");
		builderString.append("WHERE a.kmkmtAgreementMonthSetPK.employeeId = :employeeId ");
		builderString.append("AND a.kmkmtAgreementMonthSetPK.yearmonthValue = :yearmonthValue ");
		IS_EXIST_DATA = builderString.toString();
		
		// fix bug 100605
		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgreementMonthSet a ");
		builderString.append("WHERE a.kmkmtAgreementMonthSetPK.employeeId = :employeeId ");
		builderString.append("AND a.kmkmtAgreementMonthSetPK.yearmonthValue = :yearmonthValue ");
		builderString.append("ORDER BY a.kmkmtAgreementMonthSetPK.yearmonthValue DESC ");
		FIND_BY_ID = builderString.toString();
	}

	@Override
	public List<AgreementMonthSetting> find(String employeeId) {
		return this.queryProxy().query(FIND, KmkmtAgreementMonthSet.class).setParameter("employeeId", employeeId)
				.getList(f -> toDomain(f));
	}

	@Override
	public Optional<AgreementMonthSetting> findByKey(String employeeId, YearMonth yearMonth) {
		return this.queryProxy().query(FIND_BY_KEY, KmkmtAgreementMonthSet.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearmonthValue", yearMonth.v())
				.getSingle(f -> toDomain(f));
	}

    @Override
    public List<AgreementMonthSetting> findByKey(List<String> employeeIds, List<YearMonth> yearMonths) {
		if (employeeIds == null || employeeIds.isEmpty())
			return Collections.emptyList();
		if (yearMonths == null || yearMonths.isEmpty())
			return Collections.emptyList();
		List<Integer> yearmonthValues = yearMonths.stream().map(x -> x.v()).collect(Collectors.toList());
		List<AgreementMonthSetting> result = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			result.addAll(this.queryProxy().query(FIND_BY_KEYS, KmkmtAgreementMonthSet.class)
					.setParameter("employeeIds", splitData).setParameter("yearmonthValues", yearmonthValues)
					.getList(f -> toDomain(f)));
		});
		return result;
    }
	
	@Override
	public void add(AgreementMonthSetting agreementMonthSetting) {
		this.commandProxy().insert(toEntity(agreementMonthSetting));
	}

	@Override
	public void delete(String employeeId, BigDecimal yearMonthValue) {
		this.getEntityManager().createQuery(DEL_BY_KEY).setParameter("employeeId", employeeId)
				.setParameter("yearmonthValue", yearMonthValue).executeUpdate();
	}

	@Override
	public void update(AgreementMonthSetting agreementMonthSetting) {

		Optional<KmkmtAgreementMonthSet> entity = this.queryProxy().query(FIND_BY_KEY, KmkmtAgreementMonthSet.class)
				.setParameter("employeeId", agreementMonthSetting.getEmployeeId())
				.setParameter("yearmonthValue", agreementMonthSetting.getYearMonthValue().v()).getSingle();
		
		if (entity.isPresent()) {
			KmkmtAgreementMonthSet data = entity.get();
			
			data.alarmOneMonth = new BigDecimal(agreementMonthSetting.getAlarmOneMonth().valueAsMinutes());
			data.errorOneMonth = new BigDecimal(agreementMonthSetting.getErrorOneMonth().valueAsMinutes());
			
			this.commandProxy().update(data);
		}

	}
	
	// fix bug 100605
	@Override
	public void updateById(AgreementMonthSetting agreementMonthSetting, Integer yearMonthValueOld) {

		Optional<KmkmtAgreementMonthSet> entity = this.queryProxy().query(FIND_BY_ID, KmkmtAgreementMonthSet.class)
				.setParameter("employeeId", agreementMonthSetting.getEmployeeId())
				.setParameter("yearmonthValue", yearMonthValueOld).getSingle();
		if (entity.isPresent()) {
			KmkmtAgreementMonthSet data = entity.get();
			this.delete(data.kmkmtAgreementMonthSetPK.employeeId, new BigDecimal(yearMonthValueOld));
			this.add(agreementMonthSetting);
		}

	}

	@Override
	public boolean checkExistData(String employeeId, BigDecimal yearMonthValue) {
		return this.queryProxy().query(IS_EXIST_DATA, long.class).setParameter("employeeId", employeeId)
				.setParameter("yearmonthValue", yearMonthValue).getSingle().get() > 0;
	}

	private static AgreementMonthSetting toDomain(KmkmtAgreementMonthSet kmkmtAgreementMonthSet) {
		AgreementMonthSetting agreementMonthSetting = AgreementMonthSetting.createFromJavaType(
				kmkmtAgreementMonthSet.kmkmtAgreementMonthSetPK.employeeId,
				kmkmtAgreementMonthSet.kmkmtAgreementMonthSetPK.yearmonthValue,
				kmkmtAgreementMonthSet.errorOneMonth.intValue(), kmkmtAgreementMonthSet.alarmOneMonth.intValue());
		return agreementMonthSetting;
	}

	private KmkmtAgreementMonthSet toEntity(AgreementMonthSetting agreementMonthSetting) {
		val entity = new KmkmtAgreementMonthSet();

		entity.kmkmtAgreementMonthSetPK = new KmkmtAgreementMonthSetPK();
		entity.kmkmtAgreementMonthSetPK.employeeId = agreementMonthSetting.getEmployeeId();
		entity.kmkmtAgreementMonthSetPK.yearmonthValue = new BigDecimal(agreementMonthSetting.getYearMonthValue().v());
		entity.alarmOneMonth = new BigDecimal(agreementMonthSetting.getAlarmOneMonth().v());
		entity.errorOneMonth = new BigDecimal(agreementMonthSetting.getErrorOneMonth().v());

		return entity;
	}
}
