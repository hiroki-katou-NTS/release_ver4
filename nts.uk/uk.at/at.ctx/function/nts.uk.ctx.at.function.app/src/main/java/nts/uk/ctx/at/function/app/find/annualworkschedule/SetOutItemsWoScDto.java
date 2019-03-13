package nts.uk.ctx.at.function.app.find.annualworkschedule;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.at.function.dom.annualworkschedule.SetOutItemsWoSc;

/**
* 年間勤務表（36チェックリスト）の出力項目設定
*/
@AllArgsConstructor
@Value
public class SetOutItemsWoScDto
{
	/**
	* 会社ID
	*/
	private String cid;

	/**
	* コード
	*/
	private String cd;

	/**
	* 名称
	*/
	private String name;

	/**
	* 36協定時間を超過した月数を出力する
	*/
	private boolean outNumExceedTime36Agr;

	/*
	 * 年間勤務表印刷形式
	 */
	private int printForm;
	
	private boolean multiMonthDisplay;
	
	private Integer monthsInTotalDisplay;
	
	private int totalAverageDisplay;

	public static SetOutItemsWoScDto fromDomain(SetOutItemsWoSc domain) {
		return new SetOutItemsWoScDto(domain.getCid(), domain.getCd().v(), domain.getName().v(),
									  domain.isOutNumExceedTime36Agr(), domain.getPrintForm().value,
									  domain.isMultiMonthDisplay(),domain.getMonthsInTotalDisplay().isPresent()?domain.getMonthsInTotalDisplay().get().value:null,
									  domain.getTotalAverageDisplay().value);
	}
}
