package nts.uk.ctx.at.shared.dom.remainingnumber.interimremain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared.残数管理.暫定残数管理
 * 暫定残数管理データ
 * @author do_dt
 *
 */
@Getter
@Setter
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
	private CreateAtr creatorAtr;
	/**
	 * 残数種類
	 */
	private RemainType remainType;
	/**
	 * 残数分類
	 */
	private RemainAtr remainAtr;
	public InterimRemain(String sID, GeneralDate ymd, String remainManaID) {
		super();
		this.sID = sID;
		this.ymd = ymd;
		this.remainManaID = remainManaID;
	}
	public InterimRemain(String employeeId, GeneralDate end) {
		super();
		this.sID = employeeId;
		this.ymd = end;
	}
	
	
}
