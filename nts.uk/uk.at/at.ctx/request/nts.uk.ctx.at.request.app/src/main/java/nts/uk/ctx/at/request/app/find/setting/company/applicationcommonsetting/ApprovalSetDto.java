package nts.uk.ctx.at.request.app.find.setting.company.applicationcommonsetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationcommonsetting.ApprovalSet;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.AppReflectAfterConfirm;

/**
 * 
 * @author yennth
 *
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ApprovalSetDto {
	/**　スケジュールが確定されている場合 */
	public int scheduleCon;
	/**　実績が確定されている場合 */
	public int achiveCon; 
	
	public static ApprovalSetDto convertToDto(AppReflectAfterConfirm domain){
		return new ApprovalSetDto(domain.getScheduleConfirmedAtr().value, domain.getAchievementConfirmedAtr().value);
	}
}
