package nts.uk.ctx.at.function.dom.alarm.alarmlist.execalarmlistprocessing;

import java.util.List;

import nts.arc.time.GeneralDateTime;
/**
 * アラームリスト自動実行処理を実行する
 * @author tutk
 *
 */
public interface ExecAlarmListProcessingService {
	public boolean execAlarmListProcessing(String extraProcessStatusID,String companyId,List<String> workplaceIdList,List<String> listPatternCode,GeneralDateTime dateTime);
}
