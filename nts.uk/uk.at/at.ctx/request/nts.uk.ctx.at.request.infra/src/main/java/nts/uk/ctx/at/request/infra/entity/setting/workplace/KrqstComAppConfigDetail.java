package nts.uk.ctx.at.request.infra.entity.setting.workplace;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.InstructionCategory;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.setting.workplace.AppUseSetRemark;
import nts.uk.ctx.at.request.dom.setting.workplace.ApplicationDetailSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.ApplicationUseSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.AtWorkAtr;
import nts.uk.ctx.at.request.dom.setting.workplace.DisplayBreakTime;
import nts.uk.ctx.at.request.dom.setting.workplace.InstructionUseSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.Memo;
import nts.uk.ctx.at.request.dom.setting.workplace.SettingFlg;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 *  会社-申請承認機能設定
 * @author Doan Duy Hung
 *
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "KRQST_COM_APP_CF_DETAIL")
@Builder
public class KrqstComAppConfigDetail extends UkJpaEntity implements Serializable{
	
	@EmbeddedId
	public KrqstComAppConfigDetailPK krqstWpAppConfigDetailPK;
	/**
	 * 備考
	 */
	@Column(name = "MEMO")
	public String memo;
	/**
	 * 利用区分
	 */
	@Column(name = "USE_ATR")
	public int useAtr;
	/**
	 * 休出時間申請の事前必須設定
	 */
	@Column(name = "PREREQUISITE_FORPAUSE_FLG")
	public int prerequisiteForpauseFlg;
	/**
	 * 残業申請の事前必須設定
	 */
	@Column(name = "OT_APP_SETTING_FLG")
	public int otAppSettingFlg;
	/**
	 * 時間年休申請の時刻計算を利用する
	 */
	@Column(name = "HOLIDAY_TIME_APP_CAL_FLG")
	public int holidayTimeAppCalFlg;
	/**
	 * 遅刻早退取消申請の実績取消
	 */
	@Column(name = "LATE_OR_LEAVE_APP_CANCEL_FLG")
	public int lateOrLeaveAppCancelFlg;
	/**
	 * 遅刻早退取消申請の実績取消を申請時に選択
	 */
	@Column(name = "LATE_OR_LEAVE_APP_SETTING_FLG")	
	public int lateOrLeaveAppSettingFlg; 
		
	/**
	 * 休憩入力欄を表示する
	 */
	@Column(name = "BREAK_INPUTFIELD_DIS_FLG")
	public int breakInputFieldDisFlg;
	/**
	 * 休憩時間を表示する
	 */
	@Column(name = "BREAK_TIME_DISPLAY_FLG")
	public int breakTimeDisFlg;
	/**
	 * 出退勤時刻初期表示区分
	 */
	@Column(name = "ATWORK_TIME_BEGIN_DIS_FLG")
	public int atworkTimeBeginDisFlg;
	/**
	 * 実績から外出を初期表示する
	 */
	@Column(name = "GOOUT_TIME_BEGIN_DIS_FLG")
	public int goOutTimeBeginDisFlg;
	/**
	 * 時刻計算利用区分
	 */
	@Column(name = "TIME_CAL_USE_ATR")
	public int timeCalUseAtr;	
	/**
	 * 時間入力利用区分
	 */
	@Column(name = "TIME_INPUT_USE_ATR")
	public int timeInputUseAtr;
	
	/**
	 * 退勤時刻初期表示区分
	 */
	@Column(name = "TIME_END_DIS_FLG")
	public int timeEndDispFlg;

	/**
	 * 指示が必須
	 */
	@Column(name = "REQUIRED_INSTRUCTION_FLG")
	public int requiredInstructionFlg;
	/**
	 * 指示利用設定 - 指示区分
	 */
	@Column(name = "INSTRUCTION_ATR")
	public int instructionAtr;
	/**
	 * 指示利用設定 - 備考
	 */
	@Column(name = "INSTRUCTION_MEMO")
	public String instructionMemo;
	/**
	 * 指示利用設定 - 利用区分
	 */
	@Column(name ="INSTRUCTION_USE_ATR")
	public int instructionUseAtr;
	
	@ManyToOne
	@PrimaryKeyJoinColumns({
		@PrimaryKeyJoinColumn(name="CID",referencedColumnName="CID")
	})
	private KrqstComAppConfig krqstComAppConfig;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Object getKey() {
		return krqstWpAppConfigDetailPK;
	}

	public static KrqstComAppConfigDetail fromDomain(ApprovalFunctionSetting approvalFunctionSetting, String companyID){
		return KrqstComAppConfigDetail.builder()
				.krqstWpAppConfigDetailPK(new KrqstComAppConfigDetailPK(
						companyID, 
						approvalFunctionSetting.getAppUseSetting().getAppType().value))
				.memo(approvalFunctionSetting.getAppUseSetting().getMemo().v())
				.useAtr(approvalFunctionSetting.getAppUseSetting().getUserAtr().value) 
				.prerequisiteForpauseFlg(approvalFunctionSetting.getPrerequisiteForpause().value) 
				.otAppSettingFlg(approvalFunctionSetting.getOvertimeAppSetting().value) 
				.holidayTimeAppCalFlg(approvalFunctionSetting.getHolidayTimeAppCal().value) 
				.lateOrLeaveAppCancelFlg(approvalFunctionSetting.getLateOrLeaveAppCancelFlg().value) 
				.lateOrLeaveAppSettingFlg(approvalFunctionSetting.getLateOrLeaveAppSettingFlg().value) 
				.breakInputFieldDisFlg(approvalFunctionSetting.getApplicationDetailSetting().map(x -> x.getBreakInputFieldDisp() ? 1 : 0).orElse(0)) 
				.breakTimeDisFlg(approvalFunctionSetting.getApplicationDetailSetting().map(x -> x.getBreakTimeDisp() ? 1 : 0).orElse(0))
				.atworkTimeBeginDisFlg(approvalFunctionSetting.getApplicationDetailSetting().map(x -> x.getAtworkTimeBeginDisp().value).orElse(0))
				.goOutTimeBeginDisFlg(approvalFunctionSetting.getApplicationDetailSetting().map(x -> x.getGoOutTimeBeginDisp() ? 1 : 0).orElse(0)) 
				.timeCalUseAtr(approvalFunctionSetting.getApplicationDetailSetting().map(x -> x.getTimeCalUse().value).orElse(0))
				.timeInputUseAtr(approvalFunctionSetting.getApplicationDetailSetting().map(x -> x.getTimeInputUse().value).orElse(0))
				.timeEndDispFlg(approvalFunctionSetting.getApplicationDetailSetting().map(x -> x.getTimeEndDispFlg().value).orElse(0))
				.requiredInstructionFlg(approvalFunctionSetting.getApplicationDetailSetting().map(x -> x.getRequiredInstruction() ? 1 : 0).orElse(0))
				.instructionAtr(approvalFunctionSetting.getInstructionUseSetting().instructionAtr.value) 
				.instructionMemo(approvalFunctionSetting.getInstructionUseSetting().instructionRemarks.v()) 
				.instructionUseAtr(approvalFunctionSetting.getInstructionUseSetting().instructionUseDivision.value)
				.build();
	}
	
	public ApprovalFunctionSetting toDomain(){
		return new ApprovalFunctionSetting(
				SettingFlg.toEnum(this.prerequisiteForpauseFlg), 
				new InstructionUseSetting(
						InstructionCategory.toEnum(this.instructionAtr), 
						new Memo(this.instructionMemo), 
						UseAtr.toEnum(this.instructionUseAtr)), 
				SettingFlg.toEnum(this.holidayTimeAppCalFlg), 
				SettingFlg.toEnum(this.otAppSettingFlg), 
				SettingFlg.toEnum(this.lateOrLeaveAppCancelFlg), 
				SettingFlg.toEnum(this.lateOrLeaveAppSettingFlg), 
				new ApplicationUseSetting(
						new AppUseSetRemark(this.memo), 
						UseAtr.toEnum(this.useAtr), 
						EnumAdaptor.valueOf(this.krqstWpAppConfigDetailPK.appType, ApplicationType.class)), 
				Optional.of(new ApplicationDetailSetting(
						this.breakInputFieldDisFlg == 1? true : false, 
						this.breakTimeDisFlg == 1? true : false, 
						EnumAdaptor.valueOf(this.atworkTimeBeginDisFlg, AtWorkAtr.class), 
						this.goOutTimeBeginDisFlg == 1 ? true : false, 
						this.requiredInstructionFlg == 1 ? true : false, 
						UseAtr.toEnum(this.timeCalUseAtr), 
						UseAtr.toEnum(this.timeInputUseAtr), 
						EnumAdaptor.valueOf(this.timeEndDispFlg, DisplayBreakTime.class))));
	}
	
}
