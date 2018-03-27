package nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 日別計算用のエラーチェック
 * @author keisuke_hoshina
 *
 */
@Stateless
public class CalculationErrorCheckServiceImpl implements CalculationErrorCheckService{

	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository; 
	@Override
	public IntegrationOfDaily errorCheck(IntegrationOfDaily integrationOfDaily) {
		
		val errorItemList = errorAlarmWorkRecordRepository.getListErrorAlarmWorkRecord(AppContexts.user().companyId());
		//勤務実績のエラーアラーム数分ループ
		for(ErrorAlarmWorkRecord errorItem : errorItemList) {
			//使用しない
			if(!errorItem.getUseAtr()) continue;
//			val addItem = (errorItem.getFixedAtr())?/*システム固定エラーチェック*/:/*ユーザエラーチェック(Tinさんの処理呼ぶ)*/;
//			integrationOfDaily.getEmployeeError().add(addItem);
		}
		return integrationOfDaily;
	}

}
