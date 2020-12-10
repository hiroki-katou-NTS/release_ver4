package nts.uk.screen.at.app.kmk004.j;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.calcmethod.calcmethod.flex.sha.ShaFlexMonthActCalSet;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.calcmethod.calcmethod.flex.sha.ShaFlexMonthActCalSetRepo;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author sonnlb
 *
 *         UKDesign.UniversalK.就業.KDW_日別実績.KMK_計算マスタ.KMK004_法定労働時間の登録（New）.J：社員別法定労働時間の登録（フレックス勤務）.メニュー別OCD.社員別基本設定（フレックス勤務）を表示する.社員別基本設定（フレックス勤務）を表示する
 */
@Stateless
public class DisplayFlexBasicSettingByEmployee {

	@Inject
	private ShaFlexMonthActCalSetRepo shaFlexMonthActCalSetRepo;

	public ShaFlexMonthActCalSetDto displayFlexBasicSettingByEmployee(String sId) {

		// 社員別フレックス勤務集計方法

		Optional<ShaFlexMonthActCalSet> ShaFlexOpt = this.shaFlexMonthActCalSetRepo.find(AppContexts.user().companyId(),
				sId);

		if (ShaFlexOpt.isPresent()) {
			return ShaFlexMonthActCalSetDto.fromDomain(ShaFlexOpt.get());
		}

		return null;

	}
}
