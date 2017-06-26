package nts.uk.ctx.at.record.app.find.divergencetime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DivergenceTimeDto {
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
	
	public static DivergenceTimeDto fromDomain(DivergenceTime domain){
		return new DivergenceTimeDto(domain.getCompanyId(),
					domain.getDivTimeId(),
					domain.getDivTimeName().v(),
					Integer.valueOf(domain.getDivTimeUseSet().value),
					Integer.valueOf(domain.getAlarmTime().toString()),
					Integer.valueOf(domain.getErrTime().toString()),
					InputSet.convertType(domain.getSelectSet().getSelectUseSet().value,domain.getSelectSet().getCancelErrSelReason().value),
					InputSet.convertType(domain.getInputSet().getSelectUseSet().value, domain.getInputSet().getCancelErrSelReason().value));
	}
}
