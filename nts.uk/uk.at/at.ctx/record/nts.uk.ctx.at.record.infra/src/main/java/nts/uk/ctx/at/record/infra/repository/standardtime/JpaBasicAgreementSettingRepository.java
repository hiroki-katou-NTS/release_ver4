package nts.uk.ctx.at.record.infra.repository.standardtime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.standardtime.BasicAgreementSetting;
import nts.uk.ctx.at.record.dom.standardtime.repository.BasicAgreementSettingRepository;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtBasicAgreementSetting;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtBasicAgreementSettingPK;

@Stateless
public class JpaBasicAgreementSettingRepository extends JpaRepository implements BasicAgreementSettingRepository {

	private static final String FIND;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtBasicAgreementSetting a ");
		builderString.append("WHERE a.kmkmtBasicAgreementSettingPK.basicSettingId = :basicSettingId ");
		FIND = builderString.toString();
	}

	@Override
	public void add(BasicAgreementSetting basicAgreementSetting) {
		this.commandProxy().insert(toEntity(basicAgreementSetting, true));
	}

	@Override
	public void add2(BasicAgreementSetting basicAgreementSetting) {
		this.commandProxy().insert(toEntity(basicAgreementSetting, false));

	}

	@Override
	public void updateForCompany(BasicAgreementSetting basicAgreementSetting) { 
		Optional<KmkmtBasicAgreementSetting> entity = this.queryProxy().query(FIND, KmkmtBasicAgreementSetting.class)
				.setParameter("basicSettingId", basicAgreementSetting.getBasicSettingId()).getSingle();
		
		if (entity.isPresent()) {
			KmkmtBasicAgreementSetting data = entity.get();
			data.alarmWeek = new BigDecimal(basicAgreementSetting.getAlarmWeek().v());
			data.alarmTwoWeeks = new BigDecimal(basicAgreementSetting.getAlarmTwoWeeks().v());
			data.alarmFourWeeks = new BigDecimal(basicAgreementSetting.getAlarmFourWeeks().v());
			data.alarmOneMonth = new BigDecimal(basicAgreementSetting.getAlarmOneMonth().v());
			data.alarmTwoMonths = new BigDecimal(basicAgreementSetting.getAlarmTwoMonths().v());
			data.alarmThreeMonths = new BigDecimal(basicAgreementSetting.getAlarmThreeMonths().v());
			data.alarmOneYear = new BigDecimal(basicAgreementSetting.getAlarmOneYear().v());
			data.errorWeek = new BigDecimal(basicAgreementSetting.getErrorWeek().v());
			data.errorTwoWeeks = new BigDecimal(basicAgreementSetting.getErrorTwoWeeks().v());
			data.errorFourWeeks = new BigDecimal(basicAgreementSetting.getErrorFourWeeks().v());
			data.errorOneMonth = new BigDecimal(basicAgreementSetting.getErrorOneMonth().v());
			data.errorTwoMonths = new BigDecimal(basicAgreementSetting.getErrorTwoMonths().v());
			data.errorThreeMonths = new BigDecimal(basicAgreementSetting.getErrorThreeMonths().v());
			data.errorOneYear = new BigDecimal(basicAgreementSetting.getErrorOneYear().v());
			data.limitWeek = new BigDecimal(basicAgreementSetting.getLimitWeek().v());
			data.limitTwoWeeks = new BigDecimal(basicAgreementSetting.getLimitTwoWeeks().v());
			data.limitFourWeeks = new BigDecimal(basicAgreementSetting.getLimitFourWeeks().v());
			data.limitOneMonth = new BigDecimal(basicAgreementSetting.getLimitOneMonth().v());
			data.limitTwoMonths = new BigDecimal(basicAgreementSetting.getLimitTwoMonths().v());
			data.limitThreeMonths = new BigDecimal(basicAgreementSetting.getLimitThreeMonths().v());
			data.limitOneYear = new BigDecimal(basicAgreementSetting.getLimitOneYear().v());
			
			this.commandProxy().update(data);
		}
	}

	@Override
	public void update2(BasicAgreementSetting basicAgreementSetting) {
		
		Optional<KmkmtBasicAgreementSetting> entity = this.queryProxy().query(FIND, KmkmtBasicAgreementSetting.class)
				.setParameter("basicSettingId", basicAgreementSetting.getBasicSettingId()).getSingle();
		
		if (entity.isPresent()) {
			KmkmtBasicAgreementSetting data = entity.get();
			data.alarmWeek = new BigDecimal(basicAgreementSetting.getAlarmWeek().v());
			data.alarmTwoWeeks = new BigDecimal(basicAgreementSetting.getAlarmTwoWeeks().v());
			data.alarmFourWeeks = new BigDecimal(basicAgreementSetting.getAlarmFourWeeks().v());
			data.alarmOneMonth = new BigDecimal(basicAgreementSetting.getAlarmOneMonth().v());
			data.alarmTwoMonths = new BigDecimal(basicAgreementSetting.getAlarmTwoMonths().v());
			data.alarmThreeMonths = new BigDecimal(basicAgreementSetting.getAlarmThreeMonths().v());
			data.alarmOneYear = new BigDecimal(basicAgreementSetting.getAlarmOneYear().v());
			data.errorWeek = new BigDecimal(basicAgreementSetting.getErrorWeek().v());
			data.errorTwoWeeks = new BigDecimal(basicAgreementSetting.getErrorTwoWeeks().v());
			data.errorFourWeeks = new BigDecimal(basicAgreementSetting.getErrorFourWeeks().v());
			data.errorOneMonth = new BigDecimal(basicAgreementSetting.getErrorOneMonth().v());
			data.errorTwoMonths = new BigDecimal(basicAgreementSetting.getErrorTwoMonths().v());
			data.errorThreeMonths = new BigDecimal(basicAgreementSetting.getErrorThreeMonths().v());
			data.errorOneYear = new BigDecimal(basicAgreementSetting.getErrorOneYear().v());
			
			this.commandProxy().update(data);
		}
	}

	@Override
	public void remove(String basicSettingId) {
		this.commandProxy().remove(KmkmtBasicAgreementSetting.class, new KmkmtBasicAgreementSettingPK(basicSettingId));
	}

	@Override
	public Optional<BasicAgreementSetting> find(String basicSettingId) {
		return this.queryProxy().query(FIND, KmkmtBasicAgreementSetting.class)
				.setParameter("basicSettingId", basicSettingId).getSingle(f -> toDomain(f));
	}
	
	@Override
	public List<BasicAgreementSetting> find(List<String> basicSettingId) {
		if(basicSettingId.isEmpty()){
			return new ArrayList<>();
		}
		String query = "SELECT a FROM KmkmtBasicAgreementSetting a WHERE a.kmkmtBasicAgreementSettingPK.basicSettingId in :basicSettingId ";
		
		return this.queryProxy().query(query, KmkmtBasicAgreementSetting.class)
				.setParameter("basicSettingId", basicSettingId).getList(f -> toDomain(f));
	}

	private KmkmtBasicAgreementSetting toEntity(BasicAgreementSetting basicAgreementSetting, boolean isCompany) {
		val entity = new KmkmtBasicAgreementSetting();

		entity.kmkmtBasicAgreementSettingPK = new KmkmtBasicAgreementSettingPK();
		entity.kmkmtBasicAgreementSettingPK.basicSettingId = basicAgreementSetting.getBasicSettingId();
		entity.alarmWeek = new BigDecimal(basicAgreementSetting.getAlarmWeek().v());
		entity.alarmTwoWeeks = new BigDecimal(basicAgreementSetting.getAlarmTwoWeeks().v());
		entity.alarmFourWeeks = new BigDecimal(basicAgreementSetting.getAlarmFourWeeks().v());
		entity.alarmOneMonth = new BigDecimal(basicAgreementSetting.getAlarmOneMonth().v());
		entity.alarmTwoMonths = new BigDecimal(basicAgreementSetting.getAlarmTwoMonths().v());
		entity.alarmThreeMonths = new BigDecimal(basicAgreementSetting.getAlarmThreeMonths().v());
		entity.alarmOneYear = new BigDecimal(basicAgreementSetting.getAlarmOneYear().v());
		entity.errorWeek = new BigDecimal(basicAgreementSetting.getErrorWeek().v());
		entity.errorTwoWeeks = new BigDecimal(basicAgreementSetting.getErrorTwoWeeks().v());
		entity.errorFourWeeks = new BigDecimal(basicAgreementSetting.getErrorFourWeeks().v());
		entity.errorOneMonth = new BigDecimal(basicAgreementSetting.getErrorOneMonth().v());
		entity.errorTwoMonths = new BigDecimal(basicAgreementSetting.getErrorTwoMonths().v());
		entity.errorThreeMonths = new BigDecimal(basicAgreementSetting.getErrorThreeMonths().v());
		entity.errorOneYear = new BigDecimal(basicAgreementSetting.getErrorOneYear().v());
		entity.limitWeek = isCompany ? new BigDecimal(basicAgreementSetting.getLimitWeek().v()) : BigDecimal.ZERO;
		entity.limitTwoWeeks = isCompany ? new BigDecimal(basicAgreementSetting.getLimitTwoWeeks().v()) : BigDecimal.ZERO;
		entity.limitFourWeeks = isCompany ? new BigDecimal(basicAgreementSetting.getLimitFourWeeks().v()) : BigDecimal.ZERO;
		entity.limitOneMonth = isCompany ? new BigDecimal(basicAgreementSetting.getLimitOneMonth().v()) : BigDecimal.ZERO;
		entity.limitTwoMonths = isCompany ? new BigDecimal(basicAgreementSetting.getLimitTwoMonths().v()) : BigDecimal.ZERO;
		entity.limitThreeMonths = isCompany ? new BigDecimal(basicAgreementSetting.getLimitThreeMonths().v()) : BigDecimal.ZERO;
		entity.limitOneYear = isCompany ? new BigDecimal(basicAgreementSetting.getLimitOneYear().v()) : BigDecimal.ZERO;

		return entity;
	}

	private static BasicAgreementSetting toDomain(KmkmtBasicAgreementSetting kmkmtBasicAgreementSetting) {
		BasicAgreementSetting basicAgreementSetting = BasicAgreementSetting.createFromJavaType(
				kmkmtBasicAgreementSetting.kmkmtBasicAgreementSettingPK.basicSettingId,
				kmkmtBasicAgreementSetting.alarmWeek.intValue(), kmkmtBasicAgreementSetting.errorWeek.intValue(),
				kmkmtBasicAgreementSetting.limitWeek.intValue(), kmkmtBasicAgreementSetting.alarmTwoWeeks.intValue(),
				kmkmtBasicAgreementSetting.errorTwoWeeks.intValue(), kmkmtBasicAgreementSetting.limitTwoWeeks.intValue(),
				kmkmtBasicAgreementSetting.alarmFourWeeks.intValue(), kmkmtBasicAgreementSetting.errorFourWeeks.intValue(),
				kmkmtBasicAgreementSetting.limitFourWeeks.intValue(), kmkmtBasicAgreementSetting.alarmOneMonth.intValue(),
				kmkmtBasicAgreementSetting.errorOneMonth.intValue(), kmkmtBasicAgreementSetting.limitOneMonth.intValue(),
				kmkmtBasicAgreementSetting.alarmTwoMonths.intValue(), kmkmtBasicAgreementSetting.errorTwoMonths.intValue(),
				kmkmtBasicAgreementSetting.limitTwoMonths.intValue(), kmkmtBasicAgreementSetting.alarmThreeMonths.intValue(),
				kmkmtBasicAgreementSetting.errorThreeMonths.intValue(), kmkmtBasicAgreementSetting.limitThreeMonths.intValue(),
				kmkmtBasicAgreementSetting.alarmOneYear.intValue(), kmkmtBasicAgreementSetting.errorOneYear.intValue(),
				kmkmtBasicAgreementSetting.limitOneYear.intValue());
		return basicAgreementSetting;
	}
}
