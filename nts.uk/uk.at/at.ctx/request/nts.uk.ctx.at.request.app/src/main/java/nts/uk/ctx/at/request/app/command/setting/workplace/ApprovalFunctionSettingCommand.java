package nts.uk.ctx.at.request.app.command.setting.workplace;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
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

@Data
@AllArgsConstructor
public class ApprovalFunctionSettingCommand {
	/**申請種類*/
	private int appType;
	/**
	 * 備考
	 */
	private String memo;
	/**
	 * 利用区分
	 */
	private int useAtr;
	/**
	 * 休出時間申請の事前必須設定
	 */
	private int prerequisiteForpauseFlg;
	/**
	 * 残業申請の事前必須設定
	 */
	private int otAppSettingFlg;
	/**
	 * 時間年休申請の時刻計算を利用する
	 */
	private int holidayTimeAppCalFlg;
	/**
	 * 遅刻早退取消申請の実績取消
	 */
	private int lateOrLeaveAppCancelFlg;
	/**
	 * 遅刻早退取消申請の実績取消を申請時に選択
	 */
	private int lateOrLeaveAppSettingFlg; 
		
	/**
	 * 休憩入力欄を表示する
	 */
	private int breakInputFieldDisFlg;
	/**
	 * 休憩時間を表示する
	 */
	private int breakTimeDisFlg;
	/**
	 * 出退勤時刻初期表示区分
	 */
	private int atworkTimeBeginDisFlg;
	/**
	 * 実績から外出を初期表示する
	 */
	private int goOutTimeBeginDisFlg;
	/**
	 * 時刻計算利用区分
	 */
	private int timeCalUseAtr;	
	/**
	 * 時間入力利用区分
	 */
	private int timeInputUseAtr;
	/**
	 * 退勤時刻初期表示区分
	 */
	private int timeEndDispFlg;
	/**
	 * 指示が必須
	 */
	private int requiredInstructionFlg;
	/**
	 * 指示利用設定 - 指示区分
	 */
	private int instructionAtr;
	/**
	 * 指示利用設定 - 備考
	 */
	private String instructionMemo;
	/**
	 * 指示利用設定 - 利用区分
	 */
	private int instructionUseAtr;
	
	public ApprovalFunctionSetting toDomainDetail() {
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
						EnumAdaptor.valueOf(this.appType, ApplicationType.class)), 
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
