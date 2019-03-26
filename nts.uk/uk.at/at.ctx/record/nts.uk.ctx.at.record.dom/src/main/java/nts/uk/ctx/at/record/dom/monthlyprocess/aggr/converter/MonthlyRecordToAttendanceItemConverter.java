package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.converter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.information.care.MonCareHdRemain;
import nts.uk.ctx.at.record.dom.monthly.information.childnursing.MonChildHdRemain;
import nts.uk.ctx.at.record.dom.monthly.remarks.RemarksMonthlyRecord;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.MonthlyDayoffRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainData;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 月別実績と勤怠項目の相互変換
 * @author shuichu_ishida
 */
public interface MonthlyRecordToAttendanceItemConverter {

	Optional<ItemValue> convert(int attendanceItemId);

	List<ItemValue> convert(Collection<Integer> attendanceItemIds);
	
	void merge(ItemValue value);
	
	void merge(Collection<ItemValue> values);

	IntegrationOfMonthly toDomain();
	
	MonthlyRecordToAttendanceItemConverter setData(IntegrationOfMonthly domain);

	MonthlyRecordToAttendanceItemConverter withBase(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate);
	
	MonthlyRecordToAttendanceItemConverter withAffiliation(AffiliationInfoOfMonthly domain);

	MonthlyRecordToAttendanceItemConverter withAttendanceTime(AttendanceTimeOfMonthly domain);

	MonthlyRecordToAttendanceItemConverter withAnyItem(List<AnyItemOfMonthly> domains);

	MonthlyRecordToAttendanceItemConverter withAnnLeave(AnnLeaRemNumEachMonth domain);

	MonthlyRecordToAttendanceItemConverter withRsvLeave(RsvLeaRemNumEachMonth domain);

	MonthlyRecordToAttendanceItemConverter withDayOff(MonthlyDayoffRemainData domains);

	MonthlyRecordToAttendanceItemConverter withSpecialLeave(List<SpecialHolidayRemainData> domain);

	MonthlyRecordToAttendanceItemConverter withAbsenceLeave(AbsenceLeaveRemainData domain);

	MonthlyRecordToAttendanceItemConverter withMonChildHd(MonChildHdRemain monChildHdRemain);

	MonthlyRecordToAttendanceItemConverter withMonCareHd(MonCareHdRemain monCareHdRemain);

	MonthlyRecordToAttendanceItemConverter withRemarks(List<RemarksMonthlyRecord> domain);

	MonthlyRecordToAttendanceItemConverter completed();
	
	Optional<AffiliationInfoOfMonthly> toAffiliation();
	
	Optional<AttendanceTimeOfMonthly> toAttendanceTime();
	
	List<AnyItemOfMonthly> toAnyItems();
	
	Optional<AnnLeaRemNumEachMonth> toAnnLeave();
	
	Optional<MonCareHdRemain> toMonCareHd();
	
	Optional<MonChildHdRemain> toMonChildHd();
	
	Optional<RsvLeaRemNumEachMonth> toRsvLeave();
	
	Optional<MonthlyDayoffRemainData> toDayOff();
	
	List<SpecialHolidayRemainData> toSpecialHoliday();
	
	Optional<AbsenceLeaveRemainData> toAbsenceLeave();
	
	List<RemarksMonthlyRecord> toRemarks();
}
