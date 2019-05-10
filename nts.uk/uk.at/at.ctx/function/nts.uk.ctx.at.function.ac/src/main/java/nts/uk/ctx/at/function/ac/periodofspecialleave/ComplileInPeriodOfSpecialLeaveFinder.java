package nts.uk.ctx.at.function.ac.periodofspecialleave;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.adapter.periodofspecialleave.ComplileInPeriodOfSpecialLeaveAdapter;
import nts.uk.ctx.at.function.dom.adapter.periodofspecialleave.SpecialHolidayImported;
import nts.uk.ctx.at.function.dom.adapter.periodofspecialleave.SpecialVacationImported;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.export.SpecialHolidayRemainDataOutput;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.export.SpecialHolidayRemainDataSevice;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.ComplileInPeriodOfSpecialLeaveParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeave;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveManagementService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ComplileInPeriodOfSpecialLeaveFinder implements ComplileInPeriodOfSpecialLeaveAdapter {

	@Inject
	private SpecialLeaveManagementService specialLeaveManagementService;

	@Inject
	private SpecialHolidayRemainDataSevice specialHolidayRemainDataSevice;

	@Override
	public SpecialVacationImported complileInPeriodOfSpecialLeave(String cid, String sid, DatePeriod complileDate,
			boolean mode, GeneralDate baseDate, int specialLeaveCode, boolean mngAtr) {
		// requestList273
		ComplileInPeriodOfSpecialLeaveParam param = new ComplileInPeriodOfSpecialLeaveParam(cid, sid,
				complileDate, mode, baseDate, specialLeaveCode, mngAtr,
				false, new ArrayList<>(), new ArrayList<>(), Optional.empty());//TODO can them thong tin cho 3 bien nay
		InPeriodOfSpecialLeave specialLeave = specialLeaveManagementService.complileInPeriodOfSpecialLeave(param).getAggSpecialLeaveResult();
		if (specialLeave == null)
			return null;
		return new SpecialVacationImported(
				specialLeave.getRemainDays().getGrantDetailBefore().getGrantDays(), 
				0.0,
				specialLeave.getRemainDays().getGrantDetailAfter().isPresent()
						? specialLeave.getRemainDays().getGrantDetailAfter().get().getUseDays() : specialLeave.getRemainDays().getGrantDetailBefore().getUseDays(),
				specialLeave.getRemainDays().getGrantDetailAfter().isPresent()
						? specialLeave.getRemainDays().getGrantDetailAfter().get().getRemainDays(): specialLeave.getRemainDays().getGrantDetailBefore().getRemainDays(), 
				0.0, 0.0, 0.0, 0.0);
	}

	@Override
	public List<SpecialHolidayImported> getSpeHoliOfConfirmedMonthly(String sid, YearMonth startMonth,
			YearMonth endMonth) {
		// requestList263
		List<SpecialHolidayRemainDataOutput> lstSpeHoliOfConfirmedMonthly = specialHolidayRemainDataSevice
				.getSpeHoliOfConfirmedMonthly(sid, startMonth, endMonth);

		if (lstSpeHoliOfConfirmedMonthly == null)
			return null;
		List<SpecialHolidayImported> lstSpecialHoliday = new ArrayList<>();
		lstSpeHoliOfConfirmedMonthly.forEach(item -> {
			SpecialHolidayImported specialHoliday = new SpecialHolidayImported(item.getYm(), item.getUseDays(),
					item.getUseTimes(), item.getRemainDays(), item.getRemainTimes());
			lstSpecialHoliday.add(specialHoliday);
		});
		return lstSpecialHoliday;
	}

	@Override
	public List<SpecialHolidayImported> getSpeHoliOfConfirmedMonthly(String sid, YearMonth startMonth,
			YearMonth endMonth, List<Integer> listSpeCode) {
		
		// requestList263 with speCode
				List<SpecialHolidayRemainDataOutput> lstSpeHoliOfConfirmedMonthly = specialHolidayRemainDataSevice
						.getSpeHoliOfPeriodAndCodes(sid, startMonth, endMonth, listSpeCode);

				if (lstSpeHoliOfConfirmedMonthly == null)
					return null;
				List<SpecialHolidayImported> lstSpecialHoliday = new ArrayList<>();
				lstSpeHoliOfConfirmedMonthly.forEach(item -> {
					SpecialHolidayImported specialHoliday = new SpecialHolidayImported(item.getYm(), item.getUseDays(),
							item.getUseTimes(), item.getRemainDays(), item.getRemainTimes());
					lstSpecialHoliday.add(specialHoliday);
				});
				return lstSpecialHoliday;
	}
	/**
	 * @author hoatt
	 * Doi ung response KDR001
	 * RequestList263 ver2
	 * @param sid
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	@Override
	public List<SpecialHolidayImported> getSpeHdOfConfMonVer2(String sid, YearMonth startMonth, YearMonth endMonth) {
//		List<SpecialHolidayRemainDataOutput> lstSpeHdOfConfMonthly = specialHolidayRemainDataSevice
//				.getSpeHdOfConfMonVer2(sid, startMonth, endMonth);
//		if (lstSpeHdOfConfMonthly == null)
//			return null;
		List<SpecialHolidayImported> lstSpecHd = new ArrayList<>();
//		lstSpeHdOfConfMonthly.forEach(item -> {
//			SpecialHolidayImported specialHoliday = new SpecialHolidayImported(item.getYm(), item.getUseDays(),
//					item.getUseTimes(), item.getRemainDays(), item.getRemainTimes());
//			lstSpecHd.add(specialHoliday);
//		});
		return lstSpecHd;
	}
	
}
