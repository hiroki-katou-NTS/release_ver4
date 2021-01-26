package nts.uk.ctx.at.schedule.pub.schedule.workschedule;

import java.util.Optional;

import nts.arc.time.GeneralDate;
/**
 * 
 * @author tutk
 *
 */
public interface WorkSchedulePub {
	public Optional<WorkScheduleExport> get(String employeeID , GeneralDate ymd);
	
	/**
	 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務予定.勤務予定.勤務予定.Export.日別勤務予定を取得する.社員IDリスト、基準日から勤務予定を取得する
	 * [1] 取得する
	 * 
	 * @param sid      社員ID
	 * @param baseDate 基準日
	 * @return 勤務種類コード
	 */
	public Optional<String> getWorkTypeCode(String sid, GeneralDate baseDate);
}
