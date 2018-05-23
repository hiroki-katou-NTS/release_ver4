package nts.uk.ctx.at.function.app.find.attendancerecord.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.attendancerecord.export.setting.ExportSettingCode;
import nts.uk.ctx.at.function.dom.attendancerecord.item.CalculateAttendanceRecord;
import nts.uk.ctx.at.function.dom.attendancerecord.item.CalculateAttendanceRecordRepositoty;
import nts.uk.ctx.at.function.dom.dailyattendanceitem.repository.DailyAttendanceItemNameDomainService;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CalculateAttendanceRecordFinder {

	/** The calculate attendance record repository. */
	@Inject
	private CalculateAttendanceRecordRepositoty calculateAttendanceRecordRepository;
	
	/** The at name. */
	@Inject
	private DailyAttendanceItemNameDomainService atName;



	/**
	 * Gets the calculate attendance record dto.
	 *
	 * @param attendanceRecordKey the attendance record key
	 * @return the calculate attendance record dto
	 */
	public CalculateAttendanceRecordDto getCalculateAttendanceRecordDto(AttendanceRecordKeyDto attendanceRecordKey) {
		// get domain object
		Optional<CalculateAttendanceRecord> optionalCalculateAttendanceRecord = this.calculateAttendanceRecordRepository
				.getCalculateAttendanceRecord(AppContexts.user().companyId(),
						new ExportSettingCode(attendanceRecordKey.getCode()), attendanceRecordKey.getColumnIndex(),
						attendanceRecordKey.getPosition(), attendanceRecordKey.getExportAtr());
		// convert to dto
		CalculateAttendanceRecord calculateAttendanceRecord = optionalCalculateAttendanceRecord.isPresent()
				? optionalCalculateAttendanceRecord.get() : new CalculateAttendanceRecord();
				
		List<Integer> listAddedId = calculateAttendanceRecord.getAddedItem();
		List<AttendanceRecordItemDto> listAttendanceAdded = new ArrayList<>();
		
		List<Integer> listSubtracted = calculateAttendanceRecord.getSubtractedItem();
		List<AttendanceRecordItemDto> listAttendanceSubtracted = new ArrayList<>();
		
		if(listAddedId!=null && !listAddedId.isEmpty()) {
			listAttendanceAdded = findAttendanceItemsById(listAddedId);
		}
		if(listSubtracted!=null && !listSubtracted.isEmpty()) {
			listAttendanceSubtracted = findAttendanceItemsById(listSubtracted);
		}
		CalculateAttendanceRecordDto calculateAttendanceRecordDto = new CalculateAttendanceRecordDto(
				calculateAttendanceRecord.getName().toString(), listAttendanceAdded,
				listAttendanceSubtracted, calculateAttendanceRecord.getAttribute().value);

		return calculateAttendanceRecordDto;
	}
	
	/**
	 * Find attendance items by id.
	 *
	 * @param listAttendanceId the list attendance id
	 * @return the list
	 */
	public List<AttendanceRecordItemDto> findAttendanceItemsById(List<Integer> listAttendanceId){
		List<AttendanceRecordItemDto> result = new ArrayList<>();
		result = atName.getNameOfDailyAttendanceItem(listAttendanceId).stream().map(
				e -> new AttendanceRecordItemDto(e.getAttendanceItemId(), e.getAttendanceItemName(), 0,
						e.getTypeOfAttendanceItem()))
				.collect(Collectors.toList());
		return result;
	}

}
