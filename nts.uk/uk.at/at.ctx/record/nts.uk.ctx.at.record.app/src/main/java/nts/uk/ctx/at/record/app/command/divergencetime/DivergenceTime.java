package nts.uk.ctx.at.record.app.command.divergencetime;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.app.find.divergencetime.InputSet;
@Getter
@Setter
public class DivergenceTime {
	/*会社ID*/
	private String companyId;
	/*乖離時間ID*/
	private int divTimeId;
	/*乖離時間名称*/
	private String divTimeName;
	/*乖離時間使用設定*/
	private int divTimeUseSet;
	/*アラーム時間*/
	private int alarmTime;
	/*エラー時間*/
	private int errTime;
	/*選択使用設定*/	
	private InputSet selectSet;
	/*入力使用設定*/
	private InputSet inputSet;
}
