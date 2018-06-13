package nts.uk.ctx.at.shared.dom.yearholidaygrant.export;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantYearHolidayRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.YearHolidayRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：次回年休付与を取得する
 * @author shuichu_ishida
 */
@Stateless
public class GetNextAnnualLeaveGrantImpl implements GetNextAnnualLeaveGrant {

	/** 年休付与テーブル設定 */
	@Inject
	private YearHolidayRepository yearHolidayRepo;
	/** 勤続年数テーブル */
	@Inject
	private LengthServiceRepository lengthServiceRepo;
	/** 年休付与テーブル */
	@Inject
	private GrantYearHolidayRepository grantYearHolidayRepo;
	
	/** 次回年休付与を取得する */
	@Override
	public List<NextAnnualLeaveGrant> algorithm(
			String companyId,
			String grantTableCode,
			GeneralDate entryDate,
			GeneralDate criteriaDate,
			DatePeriod period,
			boolean isSingleDay){

		GetNextAnnualLeaveGrantProc proc = new GetNextAnnualLeaveGrantProc(
				this.yearHolidayRepo,
				this.lengthServiceRepo,
				this.grantYearHolidayRepo);
		return proc.algorithm(companyId, grantTableCode, entryDate, criteriaDate, period, isSingleDay);
	}
}
