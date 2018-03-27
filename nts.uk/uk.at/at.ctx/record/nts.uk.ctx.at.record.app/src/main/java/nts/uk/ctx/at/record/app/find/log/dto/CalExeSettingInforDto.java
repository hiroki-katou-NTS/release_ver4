package nts.uk.ctx.at.record.app.find.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CalExeSettingInfor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalExeSettingInforDto {

	/**
	 * 0 : 日別作成
	 * 1 : 日別計算
	 * 2 : 承認結果反映
	 * 3 : 月別集計
	 */
	private int executionContent;
	/**
	 *  0 :通常実行
	 *  1 :再実行
	 */
	private int executionType;
	
	/**
	 * executionType : name Japan
	 */
	private String executionTypeName;
	
	private String calExecutionSetInfoID;
	
	public static CalExeSettingInforDto fromDomain(CalExeSettingInfor domain) {
		return new CalExeSettingInforDto(
				domain.getExecutionContent().value,
				domain.getExecutionType().value,
				domain.getExecutionType().nameId,
				domain.getCalExecutionSetInfoID()
				);
		
	}
	
}
