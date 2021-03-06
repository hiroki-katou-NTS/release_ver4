package nts.uk.ctx.at.record.pub.remainnumber.reserveleave;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;

/**
 * 暫定積立年休管理データ
 * @author shuichu_ishida
 */
@Getter
@AllArgsConstructor
public class TmpReserveLeaveMngExport {

	/** 年月日 */
	private GeneralDate ymd;
	/** 作成元区分（予定実績区分） */
	private CreateAtr creatorAtr;
	/** 使用日数 */
	private UseDay useDays;
}
