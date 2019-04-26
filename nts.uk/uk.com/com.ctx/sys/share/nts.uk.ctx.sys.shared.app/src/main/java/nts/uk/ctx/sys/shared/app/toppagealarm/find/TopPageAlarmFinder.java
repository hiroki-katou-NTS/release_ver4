package nts.uk.ctx.sys.shared.app.toppagealarm.find;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.shared.dom.toppagealarm.TopPageAlarm;
import nts.uk.ctx.sys.shared.dom.toppagealarm.TopPageAlarmRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class TopPageAlarmFinder {
	@Inject
	private TopPageAlarmRepository toppageRep;
	// find toppage by rogerflag and month
	public List<TopPageAlarmDto> findKTG031(int rogerFlag, int month) {
		List<TopPageAlarmDto> listDto = new ArrayList<>();
		TopPageAlarmDto dto = new TopPageAlarmDto();
		Integer size = 0;
		String companyId = AppContexts.user().companyId();
		String employeeId = AppContexts.user().employeeId();
		List<TopPageAlarm> listDomain = this.toppageRep.findToppage(companyId, employeeId, rogerFlag, month);
		for (TopPageAlarm item : listDomain) {
			size = this.toppageRep.findDetail(item.getExecutionLogId()).size();
			// set 「処理名」
			switch (item.getExecutionContent()) {
			case CREATE_SCHEDULE:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1247"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case CREATE_DAILY_REPORT:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1248"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case CALCULATION_DAILY_REPORT:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1249"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case REFLECT_APPROVAL_RESULT:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1250"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case AGGREGATE_RESULT_MONTH:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1251"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case ALARM_LIST_PERSONAL:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1326"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case APPROVAL_DAILY:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1404"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case APPROVAL_MONTHLY:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1405"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			default:
				break;
			}
		}
		return listDto;
	}
	
	// find all toppage
	public List<TopPageAlarmDto> findAll(int month) {
		List<TopPageAlarmDto> listDto = new ArrayList<>();
		TopPageAlarmDto dto = new TopPageAlarmDto();
		Integer size = 0;
		String companyId = AppContexts.user().companyId();
		String employeeId = AppContexts.user().employeeId();
		List<TopPageAlarm> listDomain = this.toppageRep.findAllToppage(companyId, employeeId, month);
		for (TopPageAlarm item : listDomain) {
			size = this.toppageRep.findDetail(item.getExecutionLogId()).size();
			// set 「処理名」
			switch (item.getExecutionContent()) {
			case CREATE_SCHEDULE:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1247"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case CREATE_DAILY_REPORT:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1248"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case CALCULATION_DAILY_REPORT:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1249"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case REFLECT_APPROVAL_RESULT:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1250"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case AGGREGATE_RESULT_MONTH:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1251"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case ALARM_LIST_PERSONAL:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1326"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case APPROVAL_DAILY:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1404"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			case APPROVAL_MONTHLY:
				dto = new TopPageAlarmDto(item.getExecutionLogId(), item.getFinishDateTime(),
						item.getExecutionContent().value, item.getExistenceError().value == 1 ? true : false, item.getRogerFlag().value ==  1 ? true : false,
						TextResource.localize("Msg_1405"), 
						item.getExistenceError().value == 1 ? TextResource.localize("Msg_1252", size.toString()) : TextResource.localize("Msg_1253"));
				listDto.add(dto);
				break;
			default:
				break;
			}
		}
		return listDto;
	}
}
