package nts.uk.ctx.at.record.app.find.anyperiod;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.app.find.anyperiod.dto.AttendanceTimeOfAnyPeriodDto;
import nts.uk.ctx.at.record.app.find.monthly.root.*;
import nts.uk.ctx.at.record.app.find.monthly.root.common.MonthlyItemCommon;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.SpecialHolidayRemainDataDtoWrap;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemConverterCommonService;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.scherec.byperiod.AttendanceTimeOfAnyPeriod;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ConvertibleAttendanceItem;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.TimeMonthWithCalculationAndMinus;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.actualworkingtime.RegularAndIrregularTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.flex.FlexTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.flex.FlexTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.excessoutside.ExcessOutsideWork;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.excessoutside.ExcessOutsideWorkOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.excessoutside.SuperHD60HConTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.information.care.MonCareHdRemain;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.information.childnursing.MonChildHdRemain;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.ouen.OuenTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.remarks.RemarksMonthlyRecord;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.absenceleave.AbsenceLeaveRemainData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.dayoff.MonthlyDayoffRemainData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialHolidayRemainData;
import nts.uk.ctx.at.shared.dom.scherec.optitem.OptionalItemRepository;
import nts.uk.ctx.at.shared.dom.scherec.anyperiod.attendancetime.converter.AnyPeriodRecordToAttendanceItemConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnyPeriodRecordToAttendanceItemConverterImpl extends AttendanceItemConverterCommonService
        implements AnyPeriodRecordToAttendanceItemConverter {

    private String employeeId;

    private AnyPeriodRecordToAttendanceItemConverterImpl(OptionalItemRepository optionalItem) {
        super(new HashMap<>(), optionalItem);
    }

    public static AnyPeriodRecordToAttendanceItemConverterImpl builder(OptionalItemRepository optionalItem) {
        return new AnyPeriodRecordToAttendanceItemConverterImpl(optionalItem);
    }

    @Override
    public AnyPeriodRecordToAttendanceItemConverter completed() {
        return this;
    }

    @Override
    public AnyPeriodRecordToAttendanceItemConverter withBase(String employeeId) {
        this.employeeId = (employeeId);
        return this;
    }

    @Override
    public AnyPeriodRecordToAttendanceItemConverter withAttendanceTime(AttendanceTimeOfAnyPeriod domain) {
        if (domain != null) {
            this.withBase(domain.getEmployeeId());
        }

        this.domainSource.put(ItemConst.ANY_PERIOD_ATTENDANCE_TIME_NAME, domain);
        this.dtoSource.put(ItemConst.ANY_PERIOD_ATTENDANCE_TIME_NAME, null);
        this.itemValues.put(ItemConst.ANY_PERIOD_ATTENDANCE_TIME_NAME, null);

        return this;
    }

    @Override
    protected boolean isMonthly() {
        return false;
    }

    @Override
    protected boolean isAnyPeriod() {
        return true;
    }

    @Override
    protected Object correctOptionalItem(Object dto) {
        return dto;
    }

    @Override
    protected boolean isOpyionalItem(String type) {
        return false;
//        return type.equals(ItemConst.OPTIONAL_ITEM);
    }

    @Override
    protected void convertDomainToDto(String type) {
        switch (type) {
            case ItemConst.ANY_PERIOD_ATTENDANCE_TIME_NAME:
                processOnDomain(type, c -> AttendanceTimeOfAnyPeriodDto.fromDomain((AttendanceTimeOfAnyPeriod) c));
                break;
            default:
                break;
        }
    }

    @Override
    protected Object toDomain(ConvertibleAttendanceItem dto) {
        return dto.toDomain(employeeId, null);
    }

    @Override
    public Optional<AttendanceTimeOfAnyPeriod> toAttendanceTime() {
        return Optional.ofNullable((AttendanceTimeOfAnyPeriod) getDomain(ItemConst.ANY_PERIOD_ATTENDANCE_TIME_NAME));
    }

}
