package nts.uk.ctx.at.request.app.find.setting.workplace;

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

@AllArgsConstructor
@Data
public class ApprovalFunctionSettingDto {
	public int appType;
	public String memo;
	public int useAtr;
	public int prerequisiteForpauseFlg;
	public int otAppSettingFlg;
	public int holidayTimeAppCalFlg;
	public int lateOrLeaveAppCancelFlg;
	public int lateOrLeaveAppSettingFlg;
	public int breakInputFieldDisFlg;
	public int breakTimeDisFlg;
	public int atworkTimeBeginDisFlg;
	public int goOutTimeBeginDisFlg;
	public int timeCalUseAtr;
	public int timeInputUseAtr;
	public int requiredInstructionFlg;
	public int instructionAtr;
	public String instructionRemarks;
	public int instructionUseDivision;

	public static ApprovalFunctionSettingDto convertToDto(ApprovalFunctionSetting domain) {
		if (domain == null)
			return null;
		return new ApprovalFunctionSettingDto(domain.getAppUseSetting().getAppType().value,
				domain.getAppUseSetting().getMemo().v(), domain.getAppUseSetting().getUserAtr().value,
				domain.getPrerequisiteForpause().value, domain.getOvertimeAppSetting().value,
				domain.getHolidayTimeAppCal().value, domain.getLateOrLeaveAppCancelFlg().value,
				domain.getLateOrLeaveAppSettingFlg().value,
				domain.getApplicationDetailSetting().get().getBreakInputFieldDisp() ? 1 : 0,
				domain.getApplicationDetailSetting().get().getBreakTimeDisp() ? 1 : 0,
				domain.getApplicationDetailSetting().get().getAtworkTimeBeginDisp().value,
				domain.getApplicationDetailSetting().get().getGoOutTimeBeginDisp() ? 1 : 0,
				domain.getApplicationDetailSetting().get().getTimeCalUse().value,
				domain.getApplicationDetailSetting().get().getTimeInputUse().value,
				domain.getApplicationDetailSetting().get().getRequiredInstruction() ? 1 : 0,
				domain.getInstructionUseSetting().getInstructionAtr().value,
				domain.getInstructionUseSetting().getInstructionRemarks().v(),
				domain.getInstructionUseSetting().getInstructionUseDivision().value);
	}

	public static ApprovalFunctionSetting createFromJavaType(ApprovalFunctionSettingDto approvalFunctionSettingDto) {
		return new ApprovalFunctionSetting(
				EnumAdaptor.valueOf(approvalFunctionSettingDto.getPrerequisiteForpauseFlg(), SettingFlg.class),
				new InstructionUseSetting(
					EnumAdaptor.valueOf(approvalFunctionSettingDto.getInstructionAtr(), InstructionCategory.class),
					new Memo(approvalFunctionSettingDto.getInstructionRemarks()),
					EnumAdaptor.valueOf(approvalFunctionSettingDto.getInstructionUseDivision(), UseAtr.class)),
				EnumAdaptor.valueOf(approvalFunctionSettingDto.getHolidayTimeAppCalFlg(), SettingFlg.class),
				EnumAdaptor.valueOf(approvalFunctionSettingDto.getOtAppSettingFlg(), SettingFlg.class),
				EnumAdaptor.valueOf(approvalFunctionSettingDto.getLateOrLeaveAppCancelFlg(), SettingFlg.class),
				EnumAdaptor.valueOf(approvalFunctionSettingDto.getLateOrLeaveAppSettingFlg(), SettingFlg.class),
				new ApplicationUseSetting(
					new AppUseSetRemark(approvalFunctionSettingDto.getMemo()),
					EnumAdaptor.valueOf(approvalFunctionSettingDto.getUseAtr(), UseAtr.class),
					EnumAdaptor.valueOf(approvalFunctionSettingDto.getAppType(), ApplicationType.class)),
				Optional.of(new ApplicationDetailSetting(
					new Boolean(approvalFunctionSettingDto.getBreakInputFieldDisFlg() != 0),
					new Boolean(approvalFunctionSettingDto.getBreakTimeDisFlg() != 0),
					EnumAdaptor.valueOf(approvalFunctionSettingDto.getAtworkTimeBeginDisFlg(), AtWorkAtr.class),
					new Boolean(approvalFunctionSettingDto.getGoOutTimeBeginDisFlg() != 0),
					new Boolean(approvalFunctionSettingDto.getRequiredInstructionFlg() != 0),
					EnumAdaptor.valueOf(approvalFunctionSettingDto.getTimeCalUseAtr(), UseAtr.class),
					EnumAdaptor.valueOf(approvalFunctionSettingDto.getTimeInputUseAtr(), UseAtr.class),
					EnumAdaptor.valueOf(approvalFunctionSettingDto.getBreakTimeDisFlg(), DisplayBreakTime.class)))
				);
	}
}
