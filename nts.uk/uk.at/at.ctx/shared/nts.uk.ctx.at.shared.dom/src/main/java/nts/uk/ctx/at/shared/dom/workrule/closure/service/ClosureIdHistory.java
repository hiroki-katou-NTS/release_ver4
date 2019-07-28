package nts.uk.ctx.at.shared.dom.workrule.closure.service;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 締めID履歴
 * @author shuichi_ishida
 */
@Getter
public class ClosureIdHistory {

	/** 締めID */
	private ClosureId closureId;
	/** 期間 */
	@Setter
	private DatePeriod period;
	
	/**
	 * コンストラクタ
	 */
	public ClosureIdHistory(){
		
		this.closureId = ClosureId.RegularEmployee;
		this.period = new DatePeriod(GeneralDate.today(), GeneralDate.today());
	}
	
	/**
	 * ファクトリー
	 * @param closureId 締めID
	 * @param period 期間
	 * @return 締めID履歴
	 */
	public static ClosureIdHistory of(int closureId, DatePeriod period){
		
		ClosureIdHistory domain = new ClosureIdHistory();
		domain.closureId = EnumAdaptor.valueOf(closureId, ClosureId.class);
		domain.period = period;
		return domain;
	}
}
