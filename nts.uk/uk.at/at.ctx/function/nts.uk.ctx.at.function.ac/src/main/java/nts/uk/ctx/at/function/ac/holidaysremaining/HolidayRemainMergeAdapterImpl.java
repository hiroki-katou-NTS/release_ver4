package nts.uk.ctx.at.function.ac.holidaysremaining;

import java.util.*;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.adapter.holidaysremaining.*;
import nts.uk.ctx.at.function.dom.adapter.periodofspecialleave.SpecialHolidayImported;
import nts.uk.ctx.at.function.dom.adapter.reserveleave.ReserveHolidayImported;
import nts.uk.ctx.at.function.dom.adapter.reserveleave.ReservedYearHolidayImported;
import nts.uk.ctx.at.function.dom.adapter.reserveleave.RsvLeaUsedCurrentMonImported;
import nts.uk.ctx.at.function.dom.adapter.vacation.CurrentHolidayImported;
import nts.uk.ctx.at.function.dom.adapter.vacation.StatusHolidayImported;
import nts.uk.ctx.at.function.dom.holidaysremaining.report.PublicHolidayPastSituation;
import nts.uk.ctx.at.function.dom.holidaysremaining.report.SpecialVacationPastSituation;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.export.AbsenceleaveCurrentMonthOfEmployee;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.export.MonthlyAbsenceleaveRemainExport;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.export.DayoffCurrentMonthOfEmployee;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.export.MonthlyDayoffRemainExport;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.export.SpecialHolidayRemainDataOutput;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.export.SpecialHolidayRemainDataSevice;
import nts.uk.ctx.at.record.pub.monthly.vacation.annualleave.AnnualLeaveUsageExport;
import nts.uk.ctx.at.record.pub.monthly.vacation.annualleave.GetConfirmedAnnualLeave;
import nts.uk.ctx.at.record.pub.monthly.vacation.reserveleave.GetConfirmedReserveLeave;
import nts.uk.ctx.at.record.pub.monthly.vacation.reserveleave.ReserveLeaveUsageExport;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.AggrResultOfAnnualLeaveEachMonth;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.AnnLeaveOfThisMonth;
import nts.uk.ctx.at.record.pub.remainnumber.holiday.CheckCallRQ;
import nts.uk.ctx.at.record.pub.remainnumber.holiday.HdRemainDetailMer;
import nts.uk.ctx.at.record.pub.remainnumber.holiday.HdRemainDetailMerPub;
import nts.uk.ctx.at.record.pub.remainnumber.reserveleave.ReserveLeaveNowExport;
import nts.uk.ctx.at.record.pub.remainnumber.reserveleave.RsvLeaUsedCurrentMonExport;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.InterimRemainAggregateOutputData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.remainmerge.RemainMerge;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.remainmerge.RemainMergeRepository;
import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.publicholiday.PublicHolidayRemNumEachMonth;

@Stateless
public class HolidayRemainMergeAdapterImpl implements HolidayRemainMergeAdapter{

	@Inject
	private RemainMergeRepository repoRemainMer;
	@Inject
	private GetConfirmedAnnualLeave a;
	@Inject
	private GetConfirmedReserveLeave b;
	@Inject
	private MonthlyDayoffRemainExport c;
	@Inject
	private MonthlyAbsenceleaveRemainExport d;
	@Inject
	private SpecialHolidayRemainDataSevice e;
	@Inject
	private HdRemainDetailMerPub hdMerPub;

	//////////////////////////////////////
	// ????????????????????????????????????
	//////////////////////////////////////
	@Override
	public HolidayRemainMerEx getRemainMer(String employeeId, YearMonthPeriod period) {
		val lstYrMon = ConvertHelper.yearMonthsBetween(period);
		//////////////////////////////////////
		// ????????????????????????????????????
		//////////////////////////////////////
		Map<YearMonth, List<RemainMerge>> mapRemainMer = repoRemainMer.findBySidsAndYrMons(employeeId, lstYrMon);

		//////////////////////////////////////
		// RQ255?????????
		//////////////////////////////////////
		List<AnnualLeaveUsageExport> lstAnn = a.getYearHdMonthlyVer4(employeeId, period, mapRemainMer);
		List<AnnualLeaveUsageImported> result255 = new ArrayList<>();
		for (AnnualLeaveUsageExport ann : lstAnn) {
			AnnualLeaveUsageImported HolidayRemainData = new AnnualLeaveUsageImported(
					ann.getYearMonth(),
					ann.getUsedDays().v(),
					ann.getUsedTime().map(i -> i.v()).orElse(null),
					ann.getRemainingDays().v(),
					ann.getRemainingTime().map(i -> i.v()).orElse(null),
					ann.getNumOfuses().isPresent()?ann.getNumOfuses().get().v():null,
					ann.getNumOfRemain().isPresent()?ann.getNumOfRemain().get().v():null,
					ann.getMonthlyRemainTime().isPresent()?ann.getMonthlyRemainTime().get().v():null
			);
			result255.add(HolidayRemainData);
		}

		//////////////////////////////////////
		// RQ258???????????????
		//////////////////////////////////////
		List<ReserveLeaveUsageExport> lstRsv = b.getYearRsvMonthlyVer2(employeeId, period, mapRemainMer);
		List<ReservedYearHolidayImported> result258 = new ArrayList<>();
		for (ReserveLeaveUsageExport rsv : lstRsv) {
			result258.add(new ReservedYearHolidayImported(rsv.getYearMonth(),
					rsv.getUsedDays().v(), rsv.getRemainingDays().v()));
		}

		//////////////////////////////////////
		// RQ259?????????
		//////////////////////////////////////
		List<DayoffCurrentMonthOfEmployee> lstDayCur = c.lstDayoffCurrentMonthOfEmpVer2(employeeId, period, mapRemainMer);
		List<StatusHolidayImported> result259 = new ArrayList<>();
		for (DayoffCurrentMonthOfEmployee day : lstDayCur) {
			StatusHolidayImported statusHoliday = new StatusHolidayImported(day.getYm(), day.getOccurrenceDays(),
					day.getOccurrenceTimes(), day.getUseDays(), day.getUseTimes(), day.getUnUsedDays(),
					day.getUnUsedTimes(), day.getRemainingDays(), day.getRemainingTimes());
			result259.add(statusHoliday);
		}

		//////////////////////////////////////
		// RQ260?????????
		//////////////////////////////////////
		List<AbsenceleaveCurrentMonthOfEmployee> lstAbs = d.getDataCurrMonOfEmpVer2(employeeId, period, mapRemainMer);
		List<StatusOfHolidayImported> result260 = new ArrayList<>();
		for (AbsenceleaveCurrentMonthOfEmployee abs : lstAbs) {
			StatusOfHolidayImported sttOfHd = new StatusOfHolidayImported(abs.getYm(), abs.getOccurredDay(),
					abs.getUsedDays(), abs.getUnUsedDays(), abs.getRemainingDays());
			result260.add(sttOfHd);
		}

		//
		//////////////////////////////////////
		// RQ263???????????????                    // 2022.02.01 #120673 ?????? ??????
		//////////////////////////////////////
		List<SpecialHolidayRemainDataOutput> lstSpeHd = e.getSpeHdOfConfMonVer2(employeeId, period, mapRemainMer);
		// 2021.12.24 - 3S - chinh.hm  - issues #122037 - ?????? START
		//List<SpecialHolidayImported> result263 = new ArrayList<>();
		//for (SpecialHolidayRemainDataOutput speHd : lstSpeHd) {
		//	SpecialHolidayImported specialHoliday = new SpecialHolidayImported(speHd.getYm(), speHd.getUseDays(),
		//			speHd.getUseTimes(), speHd.getRemainDays(), speHd.getRemainTimes());
		//	result263.add(specialHoliday);
		//}
		// 2021.12.24 - 3S - chinh.hm  - issues #122037 - ?????? END
		List<SpecialVacationPastSituation> result263 = lstSpeHd.stream().map(e -> new SpecialVacationPastSituation(
				e.getSid(),
				e.getYm(),
				e.getSpecialHolidayCd(),
				////////////////////////////////////////////////////////////////////////////////
				// 2022.02.01 #120673 ?????? ?????? START
				//e.getUseDays(),
				//e.getUseTimes(),
				//e.getAfterRemainDays()  == 0 ? e.getBeforeRemainDays() :e.getAfterRemainDays(),
				//e.getAfterRemainTimes() == 0 ? e.getBeforeRemainTimes():e.getAfterRemainTimes()
				e.getFactUseDays(),
				e.getFactUseTimes(),
				e.isOptAfterFactRemain() ?  e.getAfterFactRemainDays()  : e.getBeforeFactRemainDays() ,
				e.isOptAfterFactRemain() ?  e.getAfterFactRemainTimes() : e.getBeforeFactRemainTimes()
				// 2022.02.01 #120673 ?????? ?????? END
				////////////////////////////////////////////////////////////////////////////////
		)).collect(Collectors.toList());
		//

		// 2022.01.24 - 3S - chinh.hm  - issues #122620  - ?????? START
		////////////////////////////////////////////////
		// RQ262 - ???????????????????????????(?????????)???????????????
		////////////////////////////////////////////////
		List<PublicHolidayPastSituation> result262  = getListPublicHolidayPastSituation(mapRemainMer);
		// 2022.01.24 - 3S - chinh.hm  - issues #122620  - ?????? END

		// 2022.01.24 - 3S - chinh.hm  - issues #122620  - ?????? START
		//return new HolidayRemainMerEx(result255, result258, result259, result260, result263);
		return new HolidayRemainMerEx(result255, result258, result259, result260, result263, result262);
		// 2022.01.24 - 3S - chinh.hm  - issues #122620  - ?????? END
	}

	@Override
	public HdRemainDetailMerEx getRemainDetailMer(String employeeId, YearMonth currentMonth, GeneralDate baseDate,
			DatePeriod period, CheckCallRequest checkCall) {
		CheckCallRQ check = new CheckCallRQ(checkCall.isCall265(), checkCall.isCall268(), checkCall.isCall269(), checkCall.isCall363(), checkCall.isCall364(), checkCall.isCall369());
		HdRemainDetailMer data = hdMerPub.getHdRemainDetailMer(employeeId, currentMonth, baseDate, period, check);
		//265
		AnnLeaveOfThisMonth annLeave = data.getResult265();
		AnnLeaveOfThisMonthImported result265 = annLeave == null ? null : new AnnLeaveOfThisMonthImported(annLeave.getGrantDate(), annLeave.getGrantDays(),
				annLeave.getFirstMonthRemNumDays(), annLeave.getFirstMonthRemNumMinutes(), annLeave.getUsedDays().v(),
				annLeave.getUsedMinutes(), annLeave.getRemainDays().v(), annLeave.getRemainMinutes());
		//268
		ReserveLeaveNowExport reserveLeave = data.getResult268();
		ReserveHolidayImported result268 = reserveLeave == null ? null : new ReserveHolidayImported(reserveLeave.getStartMonthRemain().v(),
				reserveLeave.getGrantNumber().v(), reserveLeave.getUsedNumber().v(), reserveLeave.getRemainNumber().v(),
				reserveLeave.getUndigestNumber().v());
		//269
		List<InterimRemainAggregateOutputData> lst269 = data.getResult269();
		List<CurrentHolidayImported> result269 = lst269.stream()
				.map(c -> new CurrentHolidayImported(c.getYm(), c.getMonthStartRemain(), c.getMonthOccurrence(),
						c.getMonthUse(), c.getMonthExtinction(), c.getMonthEndRemain()))
				.collect(Collectors.toList());
		//363
		List<AggrResultOfAnnualLeaveEachMonth> lst363 = data.getResult363();
		List<AnnLeaveUsageStatusOfThisMonthImported> result363 = lst363.stream().map(c ->
			new AnnLeaveUsageStatusOfThisMonthImported(c.getYearMonth(),
					c.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus()
							.getUsedNumberInfo().getUsedNumber().getUsedDays().map(x -> x.v()).orElse(0d),
					c.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus()
							.getUsedNumberInfo().getUsedNumber().getUsedTime().map(x -> x.valueAsMinutes()),
					c.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus()
							.getRemainingNumberInfo().getRemainingNumber().getTotalRemainingDays().v(),
					c.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus()
							.getRemainingNumberInfo().getRemainingNumber().getTotalRemainingTime().isPresent()
									? Optional.of(c.getAggrResultOfAnnualLeave().getAsOfPeriodEnd()
											.getRemainingNumber().getAnnualLeaveWithMinus()
											.getRemainingNumberInfo().getRemainingNumber()
											.getTotalRemainingTime().get().v())
									: Optional.empty())
		).collect(Collectors.toList());
		//364
		List<RsvLeaUsedCurrentMonExport> lst364 = data.getResult364();
		List<RsvLeaUsedCurrentMonImported> result364 = lst364.stream().map(c -> new RsvLeaUsedCurrentMonImported(
				c.getYearMonth(), c.getUsedNumber().v(), c.getRemainNumber().v())).collect(Collectors.toList());
		//369
		Optional<GeneralDate> result369 = data.getResult369() == null ? Optional.empty() :
					Optional.of(data.getResult369().getGrantDate());
		return new HdRemainDetailMerEx(result265, result268, result269, result363, result364, result369);
	}

	@Override
	public List<AggrResultOfAnnualLeaveEachMonthKdr> getRs363(String employeeId, YearMonth currentMonth, GeneralDate baseDate, DatePeriod period,
															  boolean checkCall363) {
		CheckCallRQ check = new CheckCallRQ(false, false, false, checkCall363, false, false);
		HdRemainDetailMer data = hdMerPub.getHdRemainDetailMer(employeeId, currentMonth, baseDate, period, check);
		List<AggrResultOfAnnualLeaveEachMonth> lst363 = data.getResult363();
		List<AggrResultOfAnnualLeaveEachMonthKdr> lsRs = lst363.stream().map(e->
        {
            val aggrResultOfAnnualLeaveAsOfStart =  e.getAggrResultOfAnnualLeave().getAsOfGrant();
            val aggrResultOfAnnualLeaveAsLased =  e.getAggrResultOfAnnualLeave().getLapsed();
            List<AnnualLeaveInfoKdr> asOfGrant = Collections.emptyList();
            List<AnnualLeaveInfoKdr> lapsed = Collections.emptyList();
            if(aggrResultOfAnnualLeaveAsOfStart.isPresent()){
                asOfGrant=   e.getAggrResultOfAnnualLeave().getAsOfGrant().get().stream().map(
                        i -> new AnnualLeaveInfoKdr(
                                i.getYmd(),
                                new AnnualLeaveRemainingKdr(
                                        i.getRemainingNumber().getAnnualLeaveNoMinus(),
                                        i.getRemainingNumber().getAnnualLeaveWithMinus(),
                                        i.getRemainingNumber().getHalfDayAnnualLeaveNoMinus(),
                                        i.getRemainingNumber().getHalfDayAnnualLeaveWithMinus(),
                                        i.getRemainingNumber().getTimeAnnualLeaveNoMinus(),
                                        i.getRemainingNumber().getTimeAnnualLeaveWithMinus(),
                                        i.getRemainingNumber().getAnnualLeaveUndigestNumber()
                                ),
                                i.getGrantRemainingDataList(),
                                i.getMaxData(),
                                i.getGrantInfo(),
                                i.getUsedDays(),
                                i.getUsedTime(),
                                i.getAnnualPaidLeaveSet().get()
                        )
                ).collect(Collectors.toList());
            }
            if(aggrResultOfAnnualLeaveAsLased.isPresent()){
                lapsed =  aggrResultOfAnnualLeaveAsLased.get().stream().map(
                        i -> new AnnualLeaveInfoKdr(
                                i.getYmd(),
                                new AnnualLeaveRemainingKdr(
                                        i.getRemainingNumber().getAnnualLeaveNoMinus(),
                                        i.getRemainingNumber().getAnnualLeaveWithMinus(),
                                        i.getRemainingNumber().getHalfDayAnnualLeaveNoMinus(),
                                        i.getRemainingNumber().getHalfDayAnnualLeaveWithMinus(),
                                        i.getRemainingNumber().getTimeAnnualLeaveNoMinus(),
                                        i.getRemainingNumber().getTimeAnnualLeaveWithMinus(),
                                        i.getRemainingNumber().getAnnualLeaveUndigestNumber()
                                ),
                                i.getGrantRemainingDataList(),
                                i.getMaxData(),
                                i.getGrantInfo(),
                                i.getUsedDays(),
                                i.getUsedTime(),
                                i.getAnnualPaidLeaveSet().get()
                        )
                ).collect(Collectors.toList());
            }
            return new AggrResultOfAnnualLeaveEachMonthKdr(
                    e.getYearMonth(),
                    new AggrResultOfAnnualLeaveKdr(
                            new AnnualLeaveInfoKdr(
                                    e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getYmd(),
                                    new AnnualLeaveRemainingKdr(
                                            e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveNoMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getHalfDayAnnualLeaveNoMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getHalfDayAnnualLeaveWithMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getTimeAnnualLeaveNoMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getTimeAnnualLeaveWithMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveUndigestNumber()
                                    ),
                                    e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getGrantRemainingDataList(),
                                    e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getMaxData(),
                                    e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getGrantInfo(),
                                    e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getUsedDays(),
                                    e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getUsedTime(),
                                    e.getAggrResultOfAnnualLeave().getAsOfPeriodEnd().getAnnualPaidLeaveSet().get()
                            ),
                            new AnnualLeaveInfoKdr(
                                    e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getYmd(),
                                    new AnnualLeaveRemainingKdr(
                                            e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getRemainingNumber().getAnnualLeaveNoMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getRemainingNumber().getHalfDayAnnualLeaveNoMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getRemainingNumber().getHalfDayAnnualLeaveWithMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getRemainingNumber().getTimeAnnualLeaveNoMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getRemainingNumber().getTimeAnnualLeaveWithMinus(),
                                            e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getRemainingNumber().getAnnualLeaveUndigestNumber()
                                    ),
                                    e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getGrantRemainingDataList(),
                                    e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getMaxData(),
                                    e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getGrantInfo(),
                                    e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getUsedDays(),
                                    e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getUsedTime(),
                                    e.getAggrResultOfAnnualLeave().getAsOfStartNextDayOfPeriodEnd().getAnnualPaidLeaveSet().get()


                            ),
                            asOfGrant,
                            lapsed
                    )
            );
        })
				.collect(Collectors.toList());
		return lsRs;
	}

	// 2022.01.24 - 3S - chinh.hm  - issues #122620  - ?????? START
	////////////////////////////////////////////////
	// RQ262 - ???????????????????????????(?????????)???????????????
	////////////////////////////////////////////////
	private List<PublicHolidayPastSituation> getListPublicHolidayPastSituation(Map<YearMonth, List<RemainMerge>> mapRemainMer){
		List<PublicHolidayPastSituation> listOuput = new ArrayList<>();

		//=======================================
		// ???????????????
		//=======================================
		for (Map.Entry<YearMonth, List<RemainMerge>> entry : mapRemainMer.entrySet()) {
			List<RemainMerge> remainMergeList =
					new ArrayList<>(entry.getValue());
			YearMonth ym = entry.getKey();
			val output = new PublicHolidayPastSituation(
					entry.getKey(),
					null,
					null,
					null,
					null
			);

			//=======================================
			// ????????????????????????????????????
			//=======================================
			// 2022.01.25 - AMID - ?????? - issues #122587 - ?????? START
			int i_begin = 0;								// ??????index
			int i_end   = remainMergeList.size() - 1;		// ??????index
			// 2022.01.25 - AMID - ?????? - issues #122587 - ?????? END

			for (int i = 0; i < remainMergeList.size(); i++) {
				RemainMerge rmm = remainMergeList.get(i);
				// ???????????????????????????
				PublicHolidayRemNumEachMonth publicHolidayRemNumEachMonth = rmm.getMonPublicHoliday();

				// 2022.01.25 - AMID - ?????? - issues #122587 - ?????? START
				// AnnLeaRemNumEachMonth annLeaRemNumEachMonth = rmm.getAnnLeaRemNumEachMonth();
				// DatePeriod closurePeriod = annLeaRemNumEachMonth.getClosurePeriod();
				// 2022.01.25 - AMID - ?????? - issues #122587 - ?????? START

				// ???????????????????????????????????????.?????????     - numberOfCarryforwards
				// ???????????????????????????????????????.????????????   - numberOfGrants
				// ???????????????????????????????????????.?????????     - numberOfUse
				// ???????????????????????????????????????.??????????????? - numberOfRemaining
				val numberOfCarryforwards = publicHolidayRemNumEachMonth.getCarryForwardNumber();
				val numberOfGrants        = publicHolidayRemNumEachMonth.getPublicHolidayday();
				val numberOfUse           = publicHolidayRemNumEachMonth.getNumberOfAcquisitions();
				val numberOfRemaining     = publicHolidayRemNumEachMonth.getNumberCarriedOverToTheNextMonth();

				// 2022.01.25 - AMID - ?????? - issues #122587 - ?????? START
				/**    ??????????????? */
				// GeneralDate endDate = closurePeriod.end();
				// GeneralDate endDateRemainingMax = GeneralDate.ymd(ym.year(), ym.month(), 1);
				// 2022.01.25 - AMID - ?????? - issues #122587 - ?????? START

				// ?????????????????????
				output.setNumberOfGrants( (output.getNumberOfGrants() == null ? 0 : output.getNumberOfGrants()) + (numberOfGrants == null ? 0 : numberOfGrants.v()) );
				output.setNumberOfUse   ( (output.getNumberOfUse()    == null ? 0 : output.getNumberOfUse()   ) + (numberOfUse    == null ? 0 : numberOfUse.v())    );

				// 2022.01.25 - AMID - ?????? - issues #122587 - ?????? START
				// // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				// if (endDate.afterOrEquals(endDateRemainingMax)) {
				//     output.setNumberOfRemaining(numberOfRemaining == null ? null : numberOfRemaining.v());
				// }else {
				//     //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				//     output.setNumberOfCarryforwards(numberOfCarryforwards == null ? null: numberOfCarryforwards.v());
				//}
				//
				// ??????????????????
				if(i == i_begin){
					// ?????????????????????(????????????)?????????????????????????????????????????????????????????????????????????????????????????????
					output.setNumberOfCarryforwards(numberOfCarryforwards == null ? null: numberOfCarryforwards.v());
				}
				// ??????????????????
				if(i == i_end){
					// ??????????????????(????????????)???????????????????????????????????????????????????????????????????????????????????????
					output.setNumberOfRemaining    (numberOfRemaining     == null ? null : numberOfRemaining.v());
				}
				// 2022.01.25 - AMID - ?????? - issues #122587 - ?????? END
			}
			listOuput.add(output);
		}
		return listOuput;
	}
	// 2022.01.24 - 3S - chinh.hm  - issues #122620  - ?????? END
}

