package nts.uk.ctx.at.function.ac.workrecord.alarmlist.fourweekfourdayoff;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.worklocation.RecordWorkInfoFunAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.workrecord.alarmlist.fourweekfourdayoff.W4D4CheckAdapter;
import nts.uk.ctx.at.function.dom.adapter.workschedule.WorkScheduleBasicInforFunctionImport;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.ErAlConstant;
import nts.uk.ctx.at.record.pub.workinformation.InfoCheckNotRegisterPubExport;
import nts.uk.ctx.at.record.pub.workrecord.alarmlist.fourweekfourdayoff.AlarmExtractionValue4W4DExport;
import nts.uk.ctx.at.record.pub.workrecord.alarmlist.fourweekfourdayoff.W4D4CheckPub;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.i18n.TextResource;
import nts.arc.time.calendar.period.DatePeriod;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class W4D4CheckAcAdapter implements W4D4CheckAdapter {

	@Inject
	private W4D4CheckPub w4D4CheckPub;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public Optional<ValueExtractAlarm> checkHoliday(String workplaceID, String employeeID, DatePeriod period,List<String> listHolidayWorkTypeCode,List<RecordWorkInfoFunAdapterDto> listWorkInfoOfDailyPerByID) {

		Optional<AlarmExtractionValue4W4DExport> optAlarmExport = w4D4CheckPub.checkHoliday(workplaceID, employeeID,
				period,listHolidayWorkTypeCode,listWorkInfoOfDailyPerByID.stream().map(c->convertToExport(c)).collect(Collectors.toList()) );
		if (optAlarmExport.isPresent()) {			
			AlarmExtractionValue4W4DExport alarmExport = optAlarmExport.get();
			ValueExtractAlarm alarmImport = new ValueExtractAlarm(workplaceID, employeeID, 
					TextResource.localize("KAL010_908",period.start().toString(ErAlConstant.DATE_FORMAT),period.end().toString(ErAlConstant.DATE_FORMAT)),
					alarmExport.getClassification(), alarmExport.getAlarmItem(), alarmExport.getAlarmValueMessage(),
					alarmExport.getComment(),alarmExport.getCheckedValue());
			return Optional.of(alarmImport);
		} else {

			return Optional.empty();
		}

	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public List<ValueExtractAlarm> checkHolidayWithSchedule(String workplaceID, String employeeID, DatePeriod period,
			List<WorkType> legalHolidayWorkTypeCodes, List<WorkType> illegalHolidayWorkTypeCodes,
			List<RecordWorkInfoFunAdapterDto> listWorkInfoOfDailyPerByID,
			List<WorkScheduleBasicInforFunctionImport> basicSchedules) {

		/*List<AlarmExtractionValue4W4DExport> alarmExports = w4D4CheckPub.checkHolidayWithSchedule(workplaceID, employeeID,
				period, legalHolidayWorkTypeCodes, illegalHolidayWorkTypeCodes, 
				listWorkInfoOfDailyPerByID.stream().map(c->convertToExport(c)).collect(Collectors.toList()),
				basicSchedules.stream().map(c -> new InfoCheckNotRegisterPubExport(c.getEmployeeID(), 
						c.getWorkTimeCd(), c.getWorkTypeCd(), c.getYmd()))
					.collect(Collectors.toList()));*/
		List<ValueExtractAlarm> alarms = new ArrayList<>();
		/*if (!alarmExports.isEmpty()) {			
			alarmExports.stream().forEach(a -> {
				ValueExtractAlarm alarmImport = new ValueExtractAlarm(workplaceID, employeeID, 
						TextResource.localize("KAL010_908",period.start().toString(ErAlConstant.DATE_FORMAT),period.end().toString(ErAlConstant.DATE_FORMAT)),
						a.getClassification(), a.getAlarmItem(), a.getAlarmValueMessage(),
						a.getComment(), a.getCheckedValue());
				alarms.add(alarmImport);
			});
		}*/
		
		return alarms;

	}
	
	private String toDateString(DatePeriod period) {
		return period.start().toString(ErAlConstant.DATE_FORMAT) + ErAlConstant.PERIOD_SEPERATOR + period.end().toString(ErAlConstant.DATE_FORMAT); 
	}
	
	private InfoCheckNotRegisterPubExport convertToExport(RecordWorkInfoFunAdapterDto dto ) {
		return new  InfoCheckNotRegisterPubExport(
				dto.getEmployeeId(),
				dto.getWorkTimeCode(),
				dto.getWorkTypeCode(),
				dto.getWorkingDate());
	}

}
