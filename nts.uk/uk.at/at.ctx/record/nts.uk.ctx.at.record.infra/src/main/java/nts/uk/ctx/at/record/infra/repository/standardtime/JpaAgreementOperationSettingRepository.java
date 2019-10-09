package nts.uk.ctx.at.record.infra.repository.standardtime;

import java.math.BigDecimal;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.standardtime.AgreementOperationSetting;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementOperationSetting;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementOperationSettingPK;

@Stateless
public class JpaAgreementOperationSettingRepository extends JpaRepository
		implements AgreementOperationSettingRepository {

	private static final String FIND;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgeementOperationSetting a ");
		builderString.append("WHERE a.kmkmtAgeementOperationSettingPK.companyId = :companyId ");
		FIND = builderString.toString();

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<AgreementOperationSetting> find(String companyId) {
		return this.queryProxy().query(FIND, KmkmtAgeementOperationSetting.class).setParameter("companyId", companyId)
				.getSingle(f -> toDomain(f));
	}

	@Override
	public void add(AgreementOperationSetting agreementOperationSetting) {
		this.commandProxy().insert(toEntity(agreementOperationSetting));
	}

	@Override
	public void update(AgreementOperationSetting agreementOperationSetting) {

		Optional<KmkmtAgeementOperationSetting> entity = this.queryProxy()
				.query(FIND, KmkmtAgeementOperationSetting.class)
				.setParameter("companyId", agreementOperationSetting.getCompanyId()).getSingle();
		
		if (entity.isPresent()) {
			KmkmtAgeementOperationSetting data = entity.get();
			data.alarmListAtr = new BigDecimal(agreementOperationSetting.getAlarmListAtr().value);
			data.closingDateAtr = new BigDecimal(agreementOperationSetting.getClosingDateAtr().value);
			data.closingDateType = new BigDecimal(agreementOperationSetting.getClosingDateType().value);
			data.numberTimesOverLimitType = new BigDecimal(agreementOperationSetting.getNumberTimesOverLimitType().value);
			data.startingMonthType = new BigDecimal(agreementOperationSetting.getStartingMonth().value);
			data.yearlyWorkTableAtr = new BigDecimal(agreementOperationSetting.getYearlyWorkTableAtr().value);
			
			this.commandProxy().update(data);
		}
	}

	private static AgreementOperationSetting toDomain(KmkmtAgeementOperationSetting kmkmtAgeementOperationSetting) {
		AgreementOperationSetting agreementOperationSetting = AgreementOperationSetting.createFromJavaType(
				kmkmtAgeementOperationSetting.kmkmtAgeementOperationSettingPK.companyId,
				kmkmtAgeementOperationSetting.startingMonthType.intValue(),
				kmkmtAgeementOperationSetting.numberTimesOverLimitType.intValue(),
				kmkmtAgeementOperationSetting.closingDateType.intValue(),
				kmkmtAgeementOperationSetting.closingDateAtr.intValue(),
				kmkmtAgeementOperationSetting.yearlyWorkTableAtr.intValue(),
				kmkmtAgeementOperationSetting.alarmListAtr.intValue());

		return agreementOperationSetting;
	}

	private KmkmtAgeementOperationSetting toEntity(AgreementOperationSetting agreementOperationSetting) {
		val entity = new KmkmtAgeementOperationSetting();

		entity.kmkmtAgeementOperationSettingPK = new KmkmtAgeementOperationSettingPK();
		entity.kmkmtAgeementOperationSettingPK.companyId = agreementOperationSetting.getCompanyId();
		entity.alarmListAtr = new BigDecimal(agreementOperationSetting.getAlarmListAtr().value);
		entity.closingDateAtr = new BigDecimal(agreementOperationSetting.getClosingDateAtr().value);
		entity.closingDateType = new BigDecimal(agreementOperationSetting.getClosingDateType().value);
		entity.numberTimesOverLimitType = new BigDecimal(agreementOperationSetting.getNumberTimesOverLimitType().value);
		entity.startingMonthType = new BigDecimal(agreementOperationSetting.getStartingMonth().value);
		entity.yearlyWorkTableAtr = new BigDecimal(agreementOperationSetting.getYearlyWorkTableAtr().value);

		return entity;
	}

}
