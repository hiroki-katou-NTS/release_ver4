package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;

/**
 *
 * @author masaaki_jinno
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubtractUseDaysFromMngDataOut {

	// 要修正 jinno
//		/**
//		 * 特別休暇の集計結果
//		 */
//		private InPeriodOfSpecialLeaveResultInfor speLeaveResult;

	/**
	 *
	 */
	private List<SpecialLeaveGrantRemainingData> lstSpeRemainData;
}
