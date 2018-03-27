package nts.uk.ctx.at.record.dom.dailyperformanceprocessing;


import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;


/**
 * 日別実績の所属情報を作成するために発生したエラー＆
 * 作成した日別実績の所属情報を保持する
 *
 * @author keisuke_hoshina
 *
 */
@Getter
public class AffiliationInforState {

	List<ErrMessageInfo> errMesInfos;
	
	Optional<AffiliationInforOfDailyPerfor> affiliationInforOfDailyPerfor;

	public AffiliationInforState(List<ErrMessageInfo> errMesInfos,
			Optional<AffiliationInforOfDailyPerfor> affiliationInforOfDailyPerfor) {
		super();
		this.errMesInfos = errMesInfos;
		this.affiliationInforOfDailyPerfor = affiliationInforOfDailyPerfor;
	}
	
	
}
