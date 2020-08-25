package nts.uk.ctx.at.request.app.find.application.applicationlist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.dom.application.applist.extractcondition.AppListExtractCondition;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppListParamFilter {
	
	/**
	 * 申請 / 承認モード
	 */
	private Integer mode;
	
	/**
	 * 期間（開始日、終了日）
	 */
	private String startDate;
	private String endDate;
	
	/**
	 * 社員ID
	 */
	private String employeeID;
	
	//デバイス：PC = 0 or スマートフォン = 1
	private int device;
	
	/**
	 * 申請種類リスト情報(LIST)(Optional)
	 */
	private List<ListOfAppTypesDto> listOfAppTypes;
	
	/**
	 * SPR連携用パラメータ(Optional）
	 */
	private AppListSprParam sprParam;
	
	//対象申請種類List
	private List<Integer> lstAppType;

//	private AppListExtractConditionDto condition;
//	private boolean spr;
//	private int extractCondition;
//	//デバイス：PC = 0 or スマートフォン = 1
//	private int device;
//	//対象申請種類List
//	private List<Integer> lstAppType;
	
	private AppListExtractCondition appListExtractCondition;
}
