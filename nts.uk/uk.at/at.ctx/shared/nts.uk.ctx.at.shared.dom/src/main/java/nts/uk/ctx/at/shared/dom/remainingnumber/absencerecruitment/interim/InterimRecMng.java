package nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimMngCommon;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.HolidayAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.OccurrenceDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnUsedDay;
/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared.残数管理.暫定残数管理
 * 暫定振出管理データ
 * @author do_dt
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class InterimRecMng extends AggregateRoot implements InterimMngCommon{
	/**	暫定振出管理データID */
	private String recruitmentMngId;
	/**	使用期限日 */
	private GeneralDate expirationDate;
	/**	発生日数 */
	private OccurrenceDay occurrenceDays;
	/**	法定内外区分 */
	private HolidayAtr statutoryAtr;
	/**	未使用日数 */
	private UnUsedDay unUsedDays;
	@Override
	public String getId() {
		return recruitmentMngId;
	}

}
