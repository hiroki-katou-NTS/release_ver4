package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkadminunverified;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.FixedConditionDataRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkadminunverified.checkbossconfirmed.CheckBossConfirmedService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.checkconfirm.StateConfirm;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class CheckAdminUnverifiedDefault implements CheckAdminUnverifiedService {

	@Inject
	private CheckBossConfirmedService checkBossConfirmedService;
	
	@Inject
	private FixedConditionDataRepository fixedConditionDataRepository;
	
	@Override
	public List<ValueExtractAlarmWR> checkAdminUnverified(String workplaceID, String employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		List<ValueExtractAlarmWR> listValueExtractAlarmWR = new ArrayList<>();
		//管理者の確認が完了しているかチェックする
		List<StateConfirm> listState =	checkBossConfirmedService.checkBossConfirmed(employeeID, startDate, endDate);
		
		//返り値をもとにアラーム値メッセージを生成する
		//勤務実績のアラームデータを生成する
		String message = fixedConditionDataRepository.getAllFixedConditionData().get(3).getMessage().v();
		for(StateConfirm stateConfirm : listState) {
			if(!stateConfirm.isState()) {
				listValueExtractAlarmWR.add(
					new ValueExtractAlarmWR(
							workplaceID,
							employeeID,
							stateConfirm.getDate(),
							TextResource.localize("KAL010_1"),
							TextResource.localize("KAL010_44"),
							TextResource.localize("KAL010_45"),
							message
							));
			}
			
		}
		
		return listValueExtractAlarmWR;
	}

}
