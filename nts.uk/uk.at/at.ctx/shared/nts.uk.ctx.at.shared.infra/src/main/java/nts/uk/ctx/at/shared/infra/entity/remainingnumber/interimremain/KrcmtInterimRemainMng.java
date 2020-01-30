package nts.uk.ctx.at.shared.infra.entity.remainingnumber.interimremain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 暫定残数管理データ
 * 
 * @author do_dt
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCMT_INTERIM_REMAIN_MNG")
public class KrcmtInterimRemainMng extends UkJpaEntity implements Serializable {
	/** 残数管理データID */
	@Id
	@Column(name = "REMAIN_MNG_ID")
	public String remainMngId;
	/** 社員ID */
	@Column(name = "SID")
	public String sId;
	/** 対象日 */
	@Column(name = "YMD")
	public GeneralDate ymd;
	/** 作成元区分 */
	@Column(name = "CREATOR_ATR")
	public int createrAtr;
	/** 残数種類 */
	@Column(name = "REMAIN_TYPE")
	public int remainType;
	/** 残数分類 */
	@Column(name = "REMAIN_ATR")
	public int remainAtr;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return remainMngId;
	}
	
}
