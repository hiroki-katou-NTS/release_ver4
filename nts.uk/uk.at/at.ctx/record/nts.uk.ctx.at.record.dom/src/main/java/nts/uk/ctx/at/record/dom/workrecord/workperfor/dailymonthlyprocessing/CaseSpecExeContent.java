/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;

/**
 * ケース別実行内容
 * 
 * @author danpv
 *
 */
@Getter
public class CaseSpecExeContent extends AggregateRoot {

	/**
	 * ID (運用ケース)
	 */
	private String caseSpecExeContentID;
	
	/**
	 * 実行実施内容ID
	 */
	private String executionContentID;
	/**
	 * 並び順
	 */
	private int orderNumber;

	/**
	 * 運用ケース名
	 */
	private UseCaseName useCaseName;

	/**
	 * 計算実行設定情報ID
	 */
	//private String calExecutionSetInfoID;
	
	/**
	 * 承認結果反映の設定情報
	 */
	
	@Setter
	private Optional<SetInforReflAprResult> reflectApprovalSetInfo;

	/**
	 * 日別作成の設定情報
	 */
	@Setter
	private Optional<SettingInforForDailyCreation> dailyCreationSetInfo;
	/**
	 * 日別計算の設定情報
	 */
	@Setter
	private Optional<CalExeSettingInfor> dailyCalSetInfo;
	/**
	 * 月別集計の設定情報
	 */
	@Setter
	private Optional<CalExeSettingInfor> monlyAggregationSetInfo;

	public CaseSpecExeContent(String caseSpecExeContentID,String executionContentID, int orderNumber, UseCaseName useCaseName) {
		super();
		this.executionContentID = executionContentID;
		this.caseSpecExeContentID = caseSpecExeContentID;
		this.orderNumber = orderNumber;
		this.useCaseName = useCaseName;
		this.reflectApprovalSetInfo = Optional.empty();
		this.dailyCreationSetInfo =  Optional.empty();
		this.dailyCalSetInfo =  Optional.empty();
		this.monlyAggregationSetInfo =  Optional.empty();
	}

	public static CaseSpecExeContent createFromJavaType(String caseSpecExeContentID,String executionContentID, int orderNumber,
			String useCaseName) {
		return new CaseSpecExeContent(caseSpecExeContentID,executionContentID, orderNumber, new UseCaseName(useCaseName));
	}

}
