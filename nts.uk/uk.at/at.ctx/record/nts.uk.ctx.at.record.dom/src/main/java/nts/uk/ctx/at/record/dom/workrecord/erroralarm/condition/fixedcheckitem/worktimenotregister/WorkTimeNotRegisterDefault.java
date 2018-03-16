package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.worktimenotregister;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.worktime.worktimeset.CheckExistWorkTimeAdapter;
import nts.uk.ctx.at.record.dom.workrecord.daily.erroralarm.createerrorforemployee.CreateErrorForEmployeeService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.FixedConditionDataRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class WorkTimeNotRegisterDefault implements WorkTimeNotRegisterService {

	@Inject
	private CheckExistWorkTimeAdapter checkExistWorkTimeAdapter;
	
	@Inject
	private CreateErrorForEmployeeService createErrorForEmployeeService;
	
	@Inject
	private FixedConditionDataRepository fixedConditionDataRepository;
	
	@Override
	public ValueExtractAlarmWR checkWorkTimeNotRegister( String workplaceID,String employeeID, GeneralDate date, String workTimeCD) {
		String companyID = AppContexts.user().companyId();
		//就業時間帯CDがドメインモデル「就業時間帯の設定」に存在するかをチェックする
		boolean check = checkExistWorkTimeAdapter.checkExistWorkTimeAdapter(workTimeCD);
		//ドメインに存在する場合
		if(check)
			return null;
		String errorCode = "S024";
		List<Integer> listTimeItemID = new ArrayList<>();
		listTimeItemID.add(13);
		
		String comment = fixedConditionDataRepository.getAllFixedConditionData().get(1).getMessage().v();
		
		ValueExtractAlarmWR valueExtractAlarmWR = createErrorForEmployeeService.createErrorForEmployeeService(workplaceID,companyID, employeeID, date, errorCode, listTimeItemID);
		Optional<ValueExtractAlarmWR> data = Optional.ofNullable(valueExtractAlarmWR);
		if(data.isPresent()) {
			valueExtractAlarmWR.setAlarmItem(TextResource.localize("KAL010_8"));
			valueExtractAlarmWR.setAlarmValueMessage(TextResource.localize("KAL010_9",errorCode));
			valueExtractAlarmWR.setComment(comment);
			return valueExtractAlarmWR;
		}
		return null;
	}

}
