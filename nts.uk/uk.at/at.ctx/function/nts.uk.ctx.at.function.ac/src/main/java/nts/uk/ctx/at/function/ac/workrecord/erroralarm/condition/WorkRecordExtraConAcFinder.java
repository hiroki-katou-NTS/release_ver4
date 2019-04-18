package nts.uk.ctx.at.function.ac.workrecord.erroralarm.condition;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.WorkRecordExtraConAdapter;
import nts.uk.ctx.at.function.dom.adapter.WorkRecordExtraConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.AlarmCheckTargetConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.AttendanceItemConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlAtdItemConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErAlConAttendanceItemAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.ErrorAlarmConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.MessageWRExtraConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.WorkTimeConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.WorkTypeConAdapterDto;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.MessageWRExtraConPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.WorkRecordExtraConPub;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.WorkRecordExtraConPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.AlarmCheckTargetConditionPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.AttendanceItemConditionPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.ErAlAtdItemConditionPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.ErAlConditionsAttendanceItemPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.ErrorAlarmConditionPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.WorkTimeConditionPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.WorkTypeConditionPubExport;
/**
 * 
 * @author tutk
 *
 */
@Stateless
public class WorkRecordExtraConAcFinder implements WorkRecordExtraConAdapter {

	@Inject
	private WorkRecordExtraConPub repo;
	
	@Override
	public List<WorkRecordExtraConAdapterDto> getAllWorkRecordExtraConByListID(List<String> listErrorAlarmID) {
		List<WorkRecordExtraConAdapterDto> data = repo.getAllWorkRecordExtraConByListID(listErrorAlarmID)
				.stream().map(c->convertToImport(c)).collect(Collectors.toList());
		return data;
	}

	@Override
	public WorkRecordExtraConAdapterDto getWorkRecordExtraConById(String errorAlarmCheckID) {
		WorkRecordExtraConAdapterDto data = convertToImport(repo.getWorkRecordExtraConById(errorAlarmCheckID));
		return data;
	}
	//convert : Export -> Import
	private WorkRecordExtraConAdapterDto convertToImport( WorkRecordExtraConPubExport export ) {
		return new WorkRecordExtraConAdapterDto(
				export.getErrorAlarmCheckID(),
				export.getCheckItem(),
				export.isMessageBold(),
				export.getMessageColor(),
				export.getSortOrderBy(),
				export.isUseAtr(),
				export.getNameWKRecord(),
				export.getErrorAlarmCondition() == null ? null : convertToErrorAlarmCon(export.getErrorAlarmCondition())
				);
	}
	private ErrorAlarmConAdapterDto convertToErrorAlarmCon(ErrorAlarmConditionPubExport export) {
		return new ErrorAlarmConAdapterDto(
				export.getErrorAlarmCheckID(),
				convertToAlarmCheckTargetCon(export.getAlarmCheckTargetCondition()),
				convertToWorkTypeCon(export.getWorkTypeCondition()),
				convertToAttendanceItemCon(export.getAttendanceItemCondition()),
				convertToWorkTimeCon(export.getWorkTimeCondition()),
				export.getDisplayMessage(),
				export.getContinuousPeriod()
				);
	}
	
	private AlarmCheckTargetConAdapterDto convertToAlarmCheckTargetCon (AlarmCheckTargetConditionPubExport export) {
		return new AlarmCheckTargetConAdapterDto(
				export.isFilterByBusinessType(),
				export.isFilterByJobTitle(),
				export.isFilterByEmployment(),
				export.isFilterByClassification(),
				export.getLstBusinessType(),
				export.getLstJobTitle(),
				export.getLstEmployment(),
				export.getLstClassification()
				);
	}
	
	private WorkTypeConAdapterDto convertToWorkTypeCon (WorkTypeConditionPubExport export) {
		return new WorkTypeConAdapterDto(
				export.isUseAtr(),
				export.getComparePlanAndActual(),
				export.isPlanFilterAtr(),
				export.getPlanLstWorkType(),
				export.isActualFilterAtr(),
				export.getActualLstWorkType()
				);
	}
	private WorkTimeConAdapterDto convertToWorkTimeCon(WorkTimeConditionPubExport export) {
		return new WorkTimeConAdapterDto(
				export.isUseAtr(),
				export.getComparePlanAndActual(),
				export.isPlanFilterAtr(),
				export.getPlanLstWorkTime(),
				export.isActualFilterAtr(),
				export.getActualLstWorkTime()
				);
	}
	
	public static AttendanceItemConAdapterDto convertToAttendanceItemCon (AttendanceItemConditionPubExport export) {
		return new AttendanceItemConAdapterDto(
				export.getOperatorBetweenGroups(),
				convertToErAlConAttendanceItem(export.getGroup1()),
				export.getGroup2()==null?null:convertToErAlConAttendanceItem(export.getGroup2()),
				export.isGroup2UseAtr()
				);
	}
	
	public static ErAlConAttendanceItemAdapterDto convertToErAlConAttendanceItem(ErAlConditionsAttendanceItemPubExport export) {
		return new  ErAlConAttendanceItemAdapterDto(
				export.getAtdItemConGroupId(),
				export.getConditionOperator(),
				export.getLstErAlAtdItemCon().stream().map(c->convertToErAlAtdItemCon(c)).collect(Collectors.toList())
				);
	}
	public static ErAlAtdItemConAdapterDto convertToErAlAtdItemCon(ErAlAtdItemConditionPubExport export) {
		return new ErAlAtdItemConAdapterDto(export.getTargetNO(), export.getConditionAtr(), export.isUseAtr(),
				export.getUncountableAtdItem(), export.getCountableAddAtdItems(), export.getCountableSubAtdItems(),
				export.getConditionType(), export.getCompareOperator(), export.getSingleAtdItem(),
				export.getCompareStartValue(), export.getCompareEndValue(), export.getInputCheckCondition());
	}
	
	//convert : Import -> Export
	
	private WorkRecordExtraConPubExport convertToExport( WorkRecordExtraConAdapterDto dto ) {
		return new WorkRecordExtraConPubExport(
				dto.getErrorAlarmCheckID(),
				dto.getCheckItem(),
				dto.isMessageBold(),
				dto.getMessageColor(),
				dto.getSortOrderBy(),
				dto.isUseAtr(),
				dto.getNameWKRecord(),
				convertToErrorAlarmConEx(dto.getErrorAlarmCondition())
				);
	}
	
	private ErrorAlarmConditionPubExport convertToErrorAlarmConEx(ErrorAlarmConAdapterDto dto) {
		return new ErrorAlarmConditionPubExport(
				dto.getErrorAlarmCheckID(),
				convertToAlarmCheckTargetConEx(dto.getAlCheckTargetCondition()),
				convertToWorkTypeConEx(dto.getWorkTypeCondition()),
				convertToAttendanceItemConEx(dto.getAtdItemCondition()),
				convertToWorkTimeConEx(dto.getWorkTimeCondition()),
				dto.getDisplayMessage(),
				dto.getContinuousPeriod()
				);
	}
	
	private AlarmCheckTargetConditionPubExport convertToAlarmCheckTargetConEx(AlarmCheckTargetConAdapterDto dto) {
		return new AlarmCheckTargetConditionPubExport(
				dto.isFilterByBusinessType(),
				dto.isFilterByJobTitle(),
				dto.isFilterByEmployment(),
				dto.isFilterByClassification(),
				dto.getLstBusinessTypeCode(),
				dto.getLstJobTitleId(),
				dto.getLstEmploymentCode(),
				dto.getLstClassificationCode()
				);
	}
	
	private WorkTypeConditionPubExport convertToWorkTypeConEx(WorkTypeConAdapterDto dto) {
		return new WorkTypeConditionPubExport(
				dto.isUseAtr(),
				dto.getComparePlanAndActual(),
				dto.isPlanFilterAtr(),
				dto.getPlanLstWorkType(),
				dto.isActualFilterAtr(),
				dto.getActualLstWorkType()
				);
	}
	private WorkTimeConditionPubExport convertToWorkTimeConEx(WorkTimeConAdapterDto dto) {
		return new WorkTimeConditionPubExport(
				dto.isUseAtr(),
				dto.getComparePlanAndActual(),
				dto.isPlanFilterAtr(),
				dto.getPlanLstWorkTime(),
				dto.isActualFilterAtr(),
				dto.getActualLstWorkTime()
				);
	}
	
	private AttendanceItemConditionPubExport convertToAttendanceItemConEx(AttendanceItemConAdapterDto dto) {
		return new AttendanceItemConditionPubExport(
				dto.getOperatorBetweenGroups(),
				convertToErAlConAttendanceItemEx(dto.getGroup1()),
				convertToErAlConAttendanceItemEx(dto.getGroup2()),
				dto.isGroup2UseAtr()
				);
	}
	private ErAlConditionsAttendanceItemPubExport convertToErAlConAttendanceItemEx(ErAlConAttendanceItemAdapterDto dto) {
		return new  ErAlConditionsAttendanceItemPubExport(
				dto.getAtdItemConGroupId(),
				dto.getConditionOperator(),
				dto.getLstErAlAtdItemCon().stream().map(c->convertToErAlAtdItemConEx(c)).collect(Collectors.toList())
				);
	}
	private ErAlAtdItemConditionPubExport convertToErAlAtdItemConEx(ErAlAtdItemConAdapterDto dto) {
		return new ErAlAtdItemConditionPubExport(dto.getTargetNO(), dto.getConditionAtr(), dto.isUseAtr(),
				dto.getUncountableAtdItem(), dto.getCountableAddAtdItems(), dto.getCountableSubAtdItems(),
				dto.getConditionType(), dto.getCompareOperator(), dto.getSingleAtdItem(), dto.getCompareStartValue(),
				dto.getCompareEndValue(), dto.getInputCheckCondition());
	}
	//  
	
	@Override
	public void addWorkRecordExtraConPub(WorkRecordExtraConAdapterDto workRecordExtraConPubExport) {
		this.repo.addWorkRecordExtraConPub(convertToExport(workRecordExtraConPubExport));
		
	}

	@Override
	public void updateWorkRecordExtraConPub(WorkRecordExtraConAdapterDto workRecordExtraConPubExport) {
		this.repo.updateWorkRecordExtraConPub(convertToExport(workRecordExtraConPubExport));
		
	}

	@Override
	public void deleteWorkRecordExtraConPub(List<String> listErrorAlarmID) {
		this.repo.deleteWorkRecordExtraConPub(listErrorAlarmID);
		
	}

	@Override
	public List<String> checkUpdateListErAl(List<String> listErrorAlarmCheckID,List<WorkRecordExtraConAdapterDto> listErroAlarm) {
		return this.repo.checkUpdateListErAl(
				listErrorAlarmCheckID,
				listErroAlarm.stream().map(c->convertToExport(c)).collect(Collectors.toList())
				);
	}


	@Override
	public  List<String> addNewListErAl(List<WorkRecordExtraConAdapterDto> listErroAlarm) {
		return this.repo.addNewListErAl(
				listErroAlarm.stream().map(c->convertToExport(c)).collect(Collectors.toList())
				);
	}

	@Override
	public List<MessageWRExtraConAdapterDto> getMessageWRExtraConByListID(List<String> listErrorAlarmID) {
		return repo.getMessageWRExtraConByListID(listErrorAlarmID)
				.stream().map(c->convertToMessageEx(c)).collect(Collectors.toList());
	}
	
	private MessageWRExtraConAdapterDto convertToMessageEx(MessageWRExtraConPubExport export) {
		return new MessageWRExtraConAdapterDto(
				export.getErrorAlarmCheckID(),
				export.getDisplayMessage()
				);
	}

}
