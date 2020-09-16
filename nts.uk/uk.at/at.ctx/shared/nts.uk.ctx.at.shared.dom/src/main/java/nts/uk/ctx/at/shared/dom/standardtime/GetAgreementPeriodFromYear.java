package nts.uk.ctx.at.shared.dom.standardtime;

import java.util.Optional;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.standardtime.enums.ClosingDateAtr;
import nts.uk.ctx.at.shared.dom.standardtime.enums.ClosingDateType;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 年度から集計期間を取得
 * @author shuichi_ishida
 */
public class GetAgreementPeriodFromYear {

	/**
	 * 年度から集計期間を取得
	 * @param year 年度
	 * @param closure 締め
	 * @return 集計期間
	 */
	public static Optional<DatePeriod> algorithm(RequireM1 require, Year year, Closure closure) {
		// ログインしている会社ID　取得
		LoginUserContext loginUserContext = AppContexts.user();
		String companyId = loginUserContext.companyId();

		// 36協定運用設定を取得
		val agreementOperationSetOpt = require.agreementOperationSetting(companyId);
		
		return algorithm(year, closure, agreementOperationSetOpt);
	}
	
	/**
	 * 年度から集計期間を取得
	 * @param year 年度
	 * @param closure 締め
	 * @param agreementOperationSetOpt ３６協定運用設定
	 * @return 集計期間
	 */
	public static Optional<DatePeriod> algorithm(Year year, Closure closure, 
			Optional<AgreementOperationSetting> agreementOperationSetOpt) {
		// 36協定運用設定を取得
		if (!agreementOperationSetOpt.isPresent()) {
			return Optional.empty();
		}
		val agreementOperationSet = agreementOperationSetOpt.get();
		
		// 「起算月」を取得
		int startMonth = agreementOperationSet.getStartingMonth().value + 1;
		
		// 36協定の「締め日」を確認
		ClosureDate closureDate = new ClosureDate(1, true);
		if (agreementOperationSet.getClosingDateType() != ClosingDateType.LASTDAY){
			closureDate = new ClosureDate(agreementOperationSet.getClosingDateType().value + 1, false);
		}
		
		// 「締め日区分」を取得
		if (agreementOperationSet.getClosingDateAtr() == ClosingDateAtr.SAMEDATE){
			
			// 当月の締め日を取得する
			val closureDateOpt = closure.getClosureDateOfCurrentMonth();
			if (!closureDateOpt.isPresent()) return Optional.empty();
			closureDate = closureDateOpt.get();
		}
		
		// 期間を判断
		YearMonth startYm = YearMonth.of(year.v(), startMonth).addMonths(1);
		YearMonth endYm = YearMonth.of(year.v(), startMonth).addMonths(12);
		// 開始月と終了月の末日
		GeneralDate startYmd = GeneralDate.ymd(startYm.year(), startYm.month(), 1).addDays(-1);
		GeneralDate endYmd = GeneralDate.ymd(endYm.year(), endYm.month(), 1).addDays(-1);
		if (!closureDate.getLastDayOfMonth()){
			// 締め日が末日でない時は、その締め日にする
			if (startYmd.day() > closureDate.getClosureDay().v()){
				startYmd = GeneralDate.ymd(startYmd.year(), startYmd.month(), closureDate.getClosureDay().v());
			}
			if (endYmd.day() > closureDate.getClosureDay().v()){
				endYmd = GeneralDate.ymd(endYmd.year(), endYmd.month(), closureDate.getClosureDay().v());
			}
		}
		
		return Optional.of(new DatePeriod(startYmd, endYmd));
	}
	
	public static interface RequireM1 {
		
		Optional<AgreementOperationSetting> agreementOperationSetting(String companyId);
	}
}
