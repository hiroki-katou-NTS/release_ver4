package nts.uk.ctx.at.record.app.find.monthly;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.AbsenceLeaveRemainDataDto;
import nts.uk.ctx.at.record.app.find.monthly.root.AffiliationInfoOfMonthlyDto;
import nts.uk.ctx.at.record.app.find.monthly.root.AnnLeaRemNumEachMonthDto;
import nts.uk.ctx.at.record.app.find.monthly.root.AnyItemOfMonthlyDto;
import nts.uk.ctx.at.record.app.find.monthly.root.AttendanceTimeOfMonthlyDto;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyCareHdRemainDto;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyChildCareHdRemainDto;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyDayoffRemainDataDto;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRemarksDto;
import nts.uk.ctx.at.record.app.find.monthly.root.RsvLeaRemNumEachMonthDto;
import nts.uk.ctx.at.record.app.find.monthly.root.SpecialHolidayRemainDataDto;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
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
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.converter.MonthlyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

public class MonthlyRecordToAttendanceItemConverterImpl implements MonthlyRecordToAttendanceItemConverter {

	private final MonthlyRecordWorkDto monthlyRecord;
	
	private final OptionalItemRepository optionalItem;
	
	private final String compId = AppContexts.user().companyId(); 
	
	private final Map<Integer, OptionalItemAtr> optionalAttr;
	
	private MonthlyRecordToAttendanceItemConverterImpl(OptionalItemRepository optionalItem){
		this.monthlyRecord = new MonthlyRecordWorkDto();
		this.optionalItem = optionalItem;
		this.optionalAttr = this.optionalItem.findAll(compId).stream()
				.collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c.getOptionalItemAtr()));
	}
	
	public static MonthlyRecordToAttendanceItemConverter builder(OptionalItemRepository optionalItem) {
		return new MonthlyRecordToAttendanceItemConverterImpl(optionalItem);
	}
	
	@Override
	public Optional<ItemValue> convert(int attendanceItemId) {
		List<ItemValue> items = AttendanceItemUtil.toItemValues(
				this.monthlyRecord, Arrays.asList(attendanceItemId), AttendanceItemType.MONTHLY_ITEM);
		return items.isEmpty() ? Optional.empty() : Optional.of(items.get(0));
	}
	
	@Override
	public List<ItemValue> convert(Collection<Integer> attendanceItemIds) {
		return AttendanceItemUtil.toItemValues(this.monthlyRecord, attendanceItemIds, AttendanceItemType.MONTHLY_ITEM);
	}
	
	@Override
	public void merge(ItemValue value) {
		AttendanceItemUtil.fromItemValues(this.monthlyRecord, Arrays.asList(value), AttendanceItemType.MONTHLY_ITEM);
	}
	
	@Override
	public void merge(Collection<ItemValue> values) {
		AttendanceItemUtil.fromItemValues(this.monthlyRecord, values, AttendanceItemType.MONTHLY_ITEM);
	}
	
	@Override
	public IntegrationOfMonthly toDomain() {
		return this.monthlyRecord.toDomain(
				this.monthlyRecord.employeeId(),
				this.monthlyRecord.getYearMonth(),
				this.monthlyRecord.getClosureID(),
				this.monthlyRecord.getClosureDate());
	}
	
	@Override
	public MonthlyRecordToAttendanceItemConverter setData(IntegrationOfMonthly domain) {
		if (domain == null) return this;
		if (!domain.getAttendanceTime().isPresent()) return this;
		
		this.withAttendanceTime(domain.getAttendanceTime().get());
		this.withAffiliation(domain.getAffiliationInfo().orElse(null));
		this.withAnyItem(domain.getAnyItemList());
		this.withAnnLeave(domain.getAnnualLeaveRemain().orElse(null));
		this.withRsvLeave(domain.getReserveLeaveRemain().orElse(null));
		this.withSpecialLeave(domain.getSpecialLeaveRemainList());
		this.withDayOff(domain.getMonthlyDayoffRemain().orElse(null));
		this.withAbsenceLeave(domain.getAbsenceLeaveRemain().orElse(null));
		this.withRemarks(domain.getRemarks());
		return this;
	}
	
	@Override
	public MonthlyRecordToAttendanceItemConverter withAffiliation(AffiliationInfoOfMonthly domain) {
		this.monthlyRecord.withAffiliation(AffiliationInfoOfMonthlyDto.from(domain));
		return this;
	}
	
	@Override
	public MonthlyRecordToAttendanceItemConverter withAttendanceTime(AttendanceTimeOfMonthly domain) {
		if (domain != null) {
			this.withBase(domain.getEmployeeId(), domain.getYearMonth(), domain.getClosureId(), domain.getClosureDate());
		}
		this.monthlyRecord.withAttendanceTime(AttendanceTimeOfMonthlyDto.from(domain));
		return this;
	}
	
	@Override
	public MonthlyRecordToAttendanceItemConverter withBase(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate){
		this.monthlyRecord.employeeId(employeeId);
		this.monthlyRecord.yearMonth(yearMonth);
		this.monthlyRecord.closureID(closureId.value);
		this.monthlyRecord.closureDate(ClosureDateDto.from(closureDate));
		return this;
	}
	
	@Override
	public MonthlyRecordToAttendanceItemConverter withAnyItem(List<AnyItemOfMonthly> domains) {
		this.monthlyRecord.withAnyItem(AnyItemOfMonthlyDto.fromWith(domains, optionalAttr));
		return this;
	}
	
	@Override
	public MonthlyRecordToAttendanceItemConverter withAnnLeave(AnnLeaRemNumEachMonth domain) {
		this.monthlyRecord.withAnnLeave(AnnLeaRemNumEachMonthDto.from(domain));
		return this;
	}
	
	@Override
	public MonthlyRecordToAttendanceItemConverter withRsvLeave(RsvLeaRemNumEachMonth domain) {
		this.monthlyRecord.withRsvLeave(RsvLeaRemNumEachMonthDto.from(domain));
		return this;
	}

	@Override
	public MonthlyRecordToAttendanceItemConverter withDayOff(MonthlyDayoffRemainData domain) {
		this.monthlyRecord.withDayOff(MonthlyDayoffRemainDataDto.from(domain));
		return this;
	}

	@Override
	public MonthlyRecordToAttendanceItemConverter withSpecialLeave(List<SpecialHolidayRemainData> domain) {
		this.monthlyRecord.withSpecialHoliday(domain.stream().map(d -> SpecialHolidayRemainDataDto.from(d)).collect(Collectors.toList()));
		return this;
	}

	@Override
	public MonthlyRecordToAttendanceItemConverter withAbsenceLeave(AbsenceLeaveRemainData domain) {
		this.monthlyRecord.withAbsenceLeave(AbsenceLeaveRemainDataDto.from(domain));
		return this;
	}
	
	@Override
	public MonthlyRecordToAttendanceItemConverter completed() {
		return this;
	}
	
	@Override
	public Optional<AffiliationInfoOfMonthly> toAffiliation() {
		return Optional.ofNullable(this.monthlyRecord.toAffiliation());
	}
	
	@Override
	public Optional<AttendanceTimeOfMonthly> toAttendanceTime() {
		return Optional.ofNullable(this.monthlyRecord.toAttendanceTime());
	}
	
	@Override
	public List<AnyItemOfMonthly> toAnyItems() {
		return this.monthlyRecord.toAnyItems();
	}
	
	@Override
	public Optional<AnnLeaRemNumEachMonth> toAnnLeave() {
		return Optional.ofNullable(this.monthlyRecord.toAnnLeave());
	}
	
	@Override
	public Optional<RsvLeaRemNumEachMonth> toRsvLeave() {
		return Optional.ofNullable(this.monthlyRecord.toRsvLeave());
	}

	@Override
	public Optional<MonthlyDayoffRemainData> toDayOff() {
		return Optional.ofNullable(this.monthlyRecord.toDayOff());
	}

	@Override
	public List<SpecialHolidayRemainData> toSpecialHoliday() {
		return this.monthlyRecord.toSpecialHoliday();
	}

	@Override
	public Optional<AbsenceLeaveRemainData> toAbsenceLeave() {
		return Optional.ofNullable(this.monthlyRecord.toAbsenceLeave());
	}

	@Override
	public MonthlyRecordToAttendanceItemConverter withRemarks(List<RemarksMonthlyRecord> domain) {
		this.monthlyRecord.withRemarks(domain.stream().map(d -> MonthlyRemarksDto.from(d)).collect(Collectors.toList()));
		return this;
	}

	@Override
	public List<RemarksMonthlyRecord> toRemarks() {
		return this.monthlyRecord.toRemarks();
	}

	@Override
	public MonthlyRecordToAttendanceItemConverter withMonChildHd(MonChildHdRemain monChildHdRemain) {
		this.monthlyRecord.withChildCare(MonthlyChildCareHdRemainDto.from(monChildHdRemain));
		return this;
	}

	@Override
	public MonthlyRecordToAttendanceItemConverter withMonCareHd(MonCareHdRemain monCareHdRemain) {
		this.monthlyRecord.withCare(MonthlyCareHdRemainDto.from(monCareHdRemain));
		return this;
	}

	@Override
	public Optional<MonCareHdRemain> toMonCareHd() {
		
		return Optional.ofNullable(this.monthlyRecord.toMonCare());
	}

	@Override
	public Optional<MonChildHdRemain> toMonChildHd() {
		return Optional.ofNullable(this.monthlyRecord.toMonChildCare());
	}
}
