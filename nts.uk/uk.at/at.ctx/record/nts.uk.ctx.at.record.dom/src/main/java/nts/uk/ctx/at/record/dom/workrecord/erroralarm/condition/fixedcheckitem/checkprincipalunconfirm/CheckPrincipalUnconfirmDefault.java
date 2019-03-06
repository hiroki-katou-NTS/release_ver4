package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.FixedConditionDataRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.checkconfirm.CheckConfirmService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.checkconfirm.StateConfirm;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class CheckPrincipalUnconfirmDefault implements CheckPrincipalUnconfirmService  {
	@Inject
	private CheckConfirmService checkConfirmService;
	
	@Inject
	private FixedConditionDataRepository fixedConditionDataRepository;

	@Override
	public List<ValueExtractAlarmWR> checkPrincipalUnconfirm(String workplaceID,String employeeID, GeneralDate startDate, GeneralDate endDate) {
		List<ValueExtractAlarmWR> listValueExtractAlarmWR = new ArrayList<>();
		//本人が確認しているかチェックする
		List<StateConfirm> listState = checkConfirmService.checkConfirm(employeeID, startDate, endDate);
		//返り値をもとにアラーム値メッセージを生成する
		//勤務実績のアラームデータを生成する
		String comment = fixedConditionDataRepository.getFixedByNO(3).get().getMessage().v();
		for(StateConfirm stateConfirm : listState) {
			if(!stateConfirm.isState()) {
				listValueExtractAlarmWR.add(
					new ValueExtractAlarmWR(
							workplaceID,
							employeeID,
							stateConfirm.getDate(),
							TextResource.localize("KAL010_1"),
							TextResource.localize("KAL010_42"),
							TextResource.localize("KAL010_43"),
							comment
							));
			}
			
		}
		
		return listValueExtractAlarmWR;
	}
	
	public Map<String, List<GeneralDate>> checkPrincipalConfirmed(List<String> employeeID, GeneralDate startDate,
			GeneralDate endDate){
		return checkConfirmService.checkConfirm(employeeID, startDate, endDate);
	}

}
