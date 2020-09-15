package nts.uk.ctx.at.record.dom.standardtime.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeOfManagePeriodRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.arc.time.calendar.period.YearMonthPeriod;

/**
 * 実装：管理期間の36協定時間を取得
 * @author shuichi_ishida
 */
@Stateless
public class GetAgreementTimeOfMngPeriodImpl implements GetAgreementTimeOfMngPeriod {

	/** 36協定運用設定の取得 */
	@Inject
	public AgreementOperationSettingRepository agreementOperationSet;
	/** 管理期間の36協定時間 */
	@Inject
	private AgreementTimeOfManagePeriodRepository agreementTimeOfMngPrdRepo;
	
	/** 管理期間の36協定時間を取得 */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<AgreementTimeOfManagePeriod> algorithm(String employeeId, Year year) {

		// ログインしている会社ID　取得
		LoginUserContext loginUserContext = AppContexts.user();
		String companyId = loginUserContext.companyId();
		
		// 36協定運用設定を取得
		val agreementOperationSetOpt = this.agreementOperationSet.find(companyId);
		if (!agreementOperationSetOpt.isPresent()) {
			return Collections.emptyList();
		}
		val agreementOperationSet = agreementOperationSetOpt.get();
		
		// 取得する年月の期間を計算
		List<YearMonth> ymRange = new ArrayList<>();
		YearMonth startYm = YearMonth.of(year.v().intValue(), agreementOperationSet.getStartingMonth().value + 1);
		YearMonth endYm = startYm.addMonths(11);
		while (startYm.lessThanOrEqualTo(endYm)){
			ymRange.add(startYm);
			startYm = startYm.addMonths(1);
		}
		
		// 「管理期間の36協定時間」を取得
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		val agreementTimeList = this.agreementTimeOfMngPrdRepo.findBySidsAndYearMonths(employeeIds, ymRange);
		
		return agreementTimeList.stream().map(c -> agreementOperationSet.setYearDto(c)).collect(Collectors.toList());
	}
	
	
	/** 管理期間の36協定時間を取得 */
	@Override
	public Map<String,List<AgreementTimeOfManagePeriod>> algorithm(List<String> employeeIds, Year year) {

		// ログインしている会社ID　取得
		LoginUserContext loginUserContext = AppContexts.user();
		String companyId = loginUserContext.companyId();
		
		// 36協定運用設定を取得
		val agreementOperationSetOpt = this.agreementOperationSet.find(companyId);
		if (!agreementOperationSetOpt.isPresent()) {
			return new HashMap<>();
		}
		val agreementOperationSet = agreementOperationSetOpt.get();
		
		// 取得する年月の期間を計算
		List<YearMonth> ymRange = new ArrayList<>();
		YearMonth startYm = YearMonth.of(year.v().intValue(), agreementOperationSet.getStartingMonth().value + 1);
		YearMonth endYm = startYm.addMonths(11);
		while (startYm.lessThanOrEqualTo(endYm)){
			ymRange.add(startYm);
			startYm = startYm.addMonths(1);
		}
		
		// 「管理期間の36協定時間」を取得
		Map<String,List<AgreementTimeOfManagePeriod>> agreementTimeList = this.agreementTimeOfMngPrdRepo.findBySidsAndYearMonths(employeeIds, ymRange).stream().collect(Collectors.groupingBy(AgreementTimeOfManagePeriod::getEmployeeId));
		
		agreementTimeList.entrySet().forEach(i-> {
			i.getValue().stream().map(c -> agreementOperationSet.setYearDto(c)).collect(Collectors.toList());
		});
		
		return agreementTimeList;
	}


	@Override
	public List<AgreementTimeOfManagePeriod> getAgreementTimeByMonths(List<String> employeeIDLst, YearMonthPeriod yearMonthPeriod) {
		List<YearMonth> yearMonthLst = new ArrayList<>();
		YearMonth loopYearMonth = yearMonthPeriod.start();
		while(loopYearMonth.lessThanOrEqualTo(yearMonthPeriod.end())) {
			yearMonthLst.add(loopYearMonth);
			loopYearMonth = loopYearMonth.addMonths(1);
		}
		return agreementTimeOfMngPrdRepo.findBySidsAndYearMonths(employeeIDLst, yearMonthLst);
	}
}
