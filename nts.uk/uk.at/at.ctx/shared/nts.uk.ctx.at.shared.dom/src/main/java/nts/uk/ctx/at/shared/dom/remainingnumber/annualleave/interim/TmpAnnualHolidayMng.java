package nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared.残数管理.年休管理.暫定年休管理データ.暫定年休管理データ
 * @author do_dt
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TmpAnnualHolidayMng extends AggregateRoot{
	/**
	 * 暫定年休管理データID
	 */
	private String annualId;
	/**
	 * 勤務種類
	 */
	private String workTypeCode;
	/**
	 * 勤務種類
	 */
	private UseDay useDays;
	public TmpAnnualHolidayMng(String annualId) {
		super();
		this.annualId = annualId;
	}
}
