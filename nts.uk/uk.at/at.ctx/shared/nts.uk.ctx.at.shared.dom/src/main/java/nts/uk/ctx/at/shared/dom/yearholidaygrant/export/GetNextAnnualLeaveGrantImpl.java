package nts.uk.ctx.at.shared.dom.yearholidaygrant.export;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTblSet;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantYearHolidayRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceTbl;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.YearHolidayRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：次回年休付与を取得する
 * @author shuichi_ishida
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
	/** 次回年休付与を取得する(複数社員用) */
	@Inject
	private GetNextAnnualLeaveGrantProcKdm002 getNextAnnualLeaveGrantProcMulti;
	
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
				this.grantYearHolidayRepo,
				this.getNextAnnualLeaveGrantProcMulti);
		return proc.algorithm(companyId, grantTableCode, entryDate, criteriaDate, period, isSingleDay);
	}
	
	/** 次回年休付与を取得する */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<NextAnnualLeaveGrant> algorithm(String companyId, String grantTableCode, GeneralDate entryDate,
			GeneralDate criteriaDate, DatePeriod period, boolean isSingleDay, Optional<GrantHdTblSet> grantHdTblSet,
			Optional<List<LengthServiceTbl>> lengthServiceTbls) {
		
		GetNextAnnualLeaveGrantProc proc = new GetNextAnnualLeaveGrantProc(
				this.yearHolidayRepo,
				this.lengthServiceRepo,
				this.grantYearHolidayRepo,
				this.getNextAnnualLeaveGrantProcMulti);
		return proc.algorithm(companyId, grantTableCode, entryDate, criteriaDate, period, isSingleDay,
				grantHdTblSet, lengthServiceTbls);
	}
}
