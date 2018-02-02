package nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.withdrawalrequestset;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.UseAtr;

/**
 * * TanLV 振休振出申請設定
 *
 */
@Getter
@Setter
public class WithDrawalReqSet extends AggregateRoot {
	/** * 会社ID */
	private String companyId;

	/** * 勤務時間変更の許可 */
	private AllowAtr permissionDivision;

	/** * 勤務種類矛盾チェック */
	private ContractCheck appliDateContrac;

	/** * 実績の表示 */
	private UseAtr useAtr;

	/** * 法内法外矛盾チェック */
	private CheckUper checkUpLimitHalfDayHD;

	/** * コメント */
	private String pickUpComment;

	/** * 太字 */
	private Weight pickUpBold;

	/** * 文字色 */
	private String pickUpLettleColor;

	/** * コメント */
	private String deferredComment;

	/** * 太字 */
	private Weight deferredBold;

	/** * 文字色 */
	private String deferredLettleColor;

	/** * 就業時間帯選択の利用 */
	private WorkUse deferredWorkTimeSelect;

	/** * 同時申請必須 */
	private AllowAtr simulAppliReq;

	/** * 振休先取許可 */
	private AllowAtr lettleSuperLeave;
	
	/** *同時申請必須 */
	private AllowAtr simutanAppRequired;
	
	/** * 振休先取許可 */
	private AllowAtr lettleSuspensionLeave;

	/**
	 * Contructor
	 * 
	 * @param companyId
	 * @param permissionDivision
	 * @param appliDateContrac
	 * @param useAtr
	 * @param checkUpLimitHalfDayHD
	 * @param pickUpComment
	 * @param pickUpBold
	 * @param pickUpLettleColor
	 * @param deferredComment
	 * @param deferredBold
	 * @param deferredLettleColor
	 * @param deferredWorkTimeSelect
	 * @param simulAppliReq
	 * @param lettleSuperLeave
	 * @param simutanAppRequired
	 * @param lettleSuspensionLeave
	 */
	public WithDrawalReqSet(String companyId, int permissionDivision, int appliDateContrac, int useAtr,
			int checkUpLimitHalfDayHD, String pickUpComment, int pickUpBold, String pickUpLettleColor,
			String deferredComment, int deferredBold, String deferredLettleColor, int deferredWorkTimeSelect,
			int simulAppliReq, int lettleSuperLeave, int simutanAppRequired, int lettleSuspensionLeave) {

		this.companyId = companyId;
		this.permissionDivision = EnumAdaptor.valueOf(permissionDivision, AllowAtr.class);
		this.appliDateContrac = EnumAdaptor.valueOf(appliDateContrac, ContractCheck.class);
		this.useAtr = EnumAdaptor.valueOf(useAtr, UseAtr.class);
		this.checkUpLimitHalfDayHD = EnumAdaptor.valueOf(checkUpLimitHalfDayHD, CheckUper.class);
		this.pickUpComment = pickUpComment;
		this.pickUpBold = EnumAdaptor.valueOf(pickUpBold, Weight.class);
		this.pickUpLettleColor = pickUpLettleColor;
		this.deferredComment = deferredComment;
		this.deferredBold = EnumAdaptor.valueOf(deferredBold, Weight.class);
		this.deferredLettleColor = deferredLettleColor;
		this.deferredWorkTimeSelect = EnumAdaptor.valueOf(deferredWorkTimeSelect, WorkUse.class);
		this.simulAppliReq = EnumAdaptor.valueOf(simulAppliReq, AllowAtr.class);
		this.lettleSuperLeave = EnumAdaptor.valueOf(lettleSuperLeave, AllowAtr.class);
		this.simutanAppRequired = EnumAdaptor.valueOf(simutanAppRequired, AllowAtr.class);
		this.lettleSuspensionLeave = EnumAdaptor.valueOf(lettleSuspensionLeave, AllowAtr.class);
	}

	/**
	 * Create From Java Type
	 * 
	 * @param companyId
	 * @param permissionDivision
	 * @param appliDateContrac
	 * @param useAtr
	 * @param checkUpLimitHalfDayHD
	 * @param pickUpComment
	 * @param pickUpBold
	 * @param pickUpLettleColor
	 * @param deferredComment
	 * @param deferredBold
	 * @param deferredLettleColor
	 * @param deferredWorkTimeSelect
	 * @param simulAppliReq
	 * @param lettleSuperLeave
	 * @param simutanAppRequired
	 * @param lettleSuspensionLeave
	 * @return
	 */
	public static WithDrawalReqSet createFromJavaType(String companyId, int permissionDivision, int appliDateContrac,
			int useAtr, int checkUpLimitHalfDayHD, String pickUpComment, int pickUpBold, String pickUpLettleColor,
			String deferredComment, int deferredBold, String deferredLettleColor, int deferredWorkTimeSelect,
			int simulAppliReq, int lettleSuperLeave, int simutanAppRequired, int lettleSuspensionLeave) {

		return new WithDrawalReqSet(companyId, permissionDivision, appliDateContrac, useAtr, checkUpLimitHalfDayHD,
				pickUpComment, pickUpBold, pickUpLettleColor, deferredComment, deferredBold, deferredLettleColor,
				deferredWorkTimeSelect, simulAppliReq, lettleSuperLeave, simutanAppRequired, lettleSuspensionLeave);
	}
}
