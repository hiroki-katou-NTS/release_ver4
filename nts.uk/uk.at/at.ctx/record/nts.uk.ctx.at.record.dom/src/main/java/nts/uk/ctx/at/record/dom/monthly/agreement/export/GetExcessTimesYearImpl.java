package nts.uk.ctx.at.record.dom.monthly.agreement.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.standardtime.export.GetAgreementTimeOfMngPeriod;
import nts.uk.ctx.at.shared.dom.common.Year;

/**
 * 実装：年間超過回数の取得
 * @author shuichi_ishida
 */
@Stateless
public class GetExcessTimesYearImpl implements GetExcessTimesYear {

	/** 管理期間の36協定時間を取得 */
	@Inject
	private GetAgreementTimeOfMngPeriod getAgreementTimeOfMngPeriod;
	
	/** 年間超過回数の取得 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public AgreementExcessInfo algorithm(String employeeId, Year year) {

		List<YearMonth> yearMonths = new ArrayList<>();
		
		// 管理期間の36協定時間を取得
		val agreementTimeList =  this.getAgreementTimeOfMngPeriod.algorithm(employeeId, year);
		
		// 状態が「超過」判定の件数をカウント
		int excessCount = 0;
		for (val agreementTime : agreementTimeList){
			switch (agreementTime.getAgreementTime().getStatus()){
			case EXCESS_LIMIT_ERROR:
			case EXCESS_LIMIT_ERROR_SP:
			case EXCESS_EXCEPTION_LIMIT_ALARM:
			case EXCESS_EXCEPTION_LIMIT_ERROR:
				excessCount++;
				yearMonths.add(agreementTime.getYearMonth());
				break;
			default:
				break;
			}
		}
		
		// 36協定超過情報を返す
		return AgreementExcessInfo.of(excessCount, yearMonths);
	}
	
	/** 年間超過回数の取得 */
	@Override
	public Map<String,AgreementExcessInfo> algorithm(List<String> employeeIds, Year year) {

		List<YearMonth> yearMonths = new ArrayList<>();
		
		// 管理期間の36協定時間を取得
		Map<String,List<AgreementTimeOfManagePeriod>> agreementTimeList = this.getAgreementTimeOfMngPeriod.algorithm(employeeIds, year);
		
		Map<String,AgreementExcessInfo> result = new HashMap<>();
		
		agreementTimeList.entrySet().forEach(i->{
			// 状態が「超過」判定の件数をカウント
			int excessCount = 0;
			for (val agreementTime : i.getValue()){
				switch (agreementTime.getAgreementTime().getStatus()){
				case EXCESS_LIMIT_ERROR:
				case EXCESS_LIMIT_ERROR_SP:
				case EXCESS_EXCEPTION_LIMIT_ALARM:
				case EXCESS_EXCEPTION_LIMIT_ERROR:
					excessCount++;
					yearMonths.add(agreementTime.getYearMonth());
					break;
				default:
					break;
				}
			}
			result.put(i.getKey(), AgreementExcessInfo.of(excessCount, yearMonths));
		});
		
		
		// 36協定超過情報を返す
		return result;
	}
}