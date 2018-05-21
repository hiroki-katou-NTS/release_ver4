package nts.uk.ctx.at.shared.dom.reamainmana.interimremain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.reamainmana.interimremain.primitive.CreaterAtr;
import nts.uk.ctx.at.shared.dom.reamainmana.interimremain.primitive.RemainAtr;
import nts.uk.ctx.at.shared.dom.reamainmana.interimremain.primitive.RemainType;
/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared.残数管理.暫定残数管理
 * 暫定残数管理データ
 * @author do_dt
 *
 */
@Getter
@AllArgsConstructor
public class InterimRemain extends AggregateRoot{
	/**
	 * 残数管理データID
	 */
	private String remainManaID;
	/**
	 * 社員ID
	 */
	private String sID;
	/**
	 * 対象日
	 */
	private GeneralDate ymd;
	/**
	 * 作成元区分
	 */
	private CreaterAtr creatorAtr;
	/**
	 * 残数種類
	 */
	private RemainType remainType;
	/**
	 * 残数分類
	 */
	private RemainAtr remainAtr;
	

}
