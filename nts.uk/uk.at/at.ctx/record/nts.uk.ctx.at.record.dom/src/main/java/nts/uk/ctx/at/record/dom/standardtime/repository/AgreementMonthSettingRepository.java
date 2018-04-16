package nts.uk.ctx.at.record.dom.standardtime.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.standardtime.AgreementMonthSetting;

public interface AgreementMonthSettingRepository {

	List<AgreementMonthSetting> find(String employeeId);
	
	Optional<AgreementMonthSetting> findByKey(String employeeId, YearMonth yearMonth);
	
	void add(AgreementMonthSetting agreementMonthSetting);
	
	void update(AgreementMonthSetting agreementMonthSetting);
	
	void delete(String employeeId, BigDecimal yearMonthValue);
	
	boolean checkExistData(String employeeId, BigDecimal yearMonthValue);
	
}
