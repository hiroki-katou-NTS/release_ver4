package nts.uk.ctx.at.request.app.command.application.gobackdirectly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor	
public class InsertGoBackDirectlyCommand {
	/**
	 * 勤務種類
	 */
	String workTypeCD;
	/**
	 * 就業時間帯
	 */
	String siftCD;
	/**
	 * 勤務を変更する
	 */
	int workChangeAtr;
	/**
	 * 勤務直行1
	 */
	int goWorkAtr1;
	/**
	 * 勤務直帰1
	 */
	int backHomeAtr1;
	/**
	 * 勤務時間開始1
	 */
	Integer workTimeStart1;
	/**
	 * 勤務時間終了1
	 */
	Integer workTimeEnd1;
	/**
	 * 勤務場所選択1
	 */
	String workLocationCD1;
	/**
	 * 勤務直行2
	 */
	int goWorkAtr2;
	/**
	 * 勤務直帰2
	 */
	int backHomeAtr2;
	/**
	 * 勤務時間開始2
	 */
	Integer workTimeStart2;
	/**
	 * 勤務時間終了2
	 */
	Integer workTimeEnd2;
	/**
	 * 勤務場所選択２
	 */
	String workLocationCD2;
	
}
