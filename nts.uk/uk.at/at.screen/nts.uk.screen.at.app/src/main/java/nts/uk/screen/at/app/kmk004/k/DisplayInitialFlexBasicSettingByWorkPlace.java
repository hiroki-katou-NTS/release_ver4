package nts.uk.screen.at.app.kmk004.k;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.calcmethod.calcmethod.flex.wkp.WkpFlexMonthActCalSet;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.calcmethod.calcmethod.flex.wkp.WkpFlexMonthActCalSetRepo;
import nts.uk.screen.at.app.kmk004.h.WkpFlexMonthActCalSetDto;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author sonnlb
 *
 *         UKDesign.UniversalK.就業.KDW_日別実績.KMK_計算マスタ.KMK004_法定労働時間の登録（New）.K：基本設定（フレックス勤務）.メニュー別OCD.社員別基本設定（フレックス勤務）の初期画面を表示する
 */
@Stateless
public class DisplayInitialFlexBasicSettingByWorkPlace {

	@Inject
	private WkpFlexMonthActCalSetRepo wkpFlexMonthActCalSetRepo;

	public WkpFlexMonthActCalSetDto displayInitialFlexBasicSettingByWorkPlace(String wkpId) {

		// 職場別フレックス勤務集計方法

		Optional<WkpFlexMonthActCalSet> wkpFlexOpt = this.wkpFlexMonthActCalSetRepo.find(AppContexts.user().companyId(),
				wkpId);

		if (wkpFlexOpt.isPresent()) {
			return WkpFlexMonthActCalSetDto.fromDomain(wkpFlexOpt.get());

		}

		return null;
	}
}
