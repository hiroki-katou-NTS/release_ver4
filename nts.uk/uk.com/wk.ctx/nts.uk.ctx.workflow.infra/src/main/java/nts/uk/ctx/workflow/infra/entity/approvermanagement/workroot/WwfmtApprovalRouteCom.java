package nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;
/**
 * 会社別承認ルート
 * @author hoatt
 *
 */
@Setter
@Entity
@Table(name = "WWFMT_APPROVAL_ROUTE_COM")
@AllArgsConstructor
@NoArgsConstructor
public class WwfmtApprovalRouteCom extends ContractUkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**主キー*/
	@EmbeddedId
	public WwfmtComApprovalRootPK wwfmtComApprovalRootPK;
	
	/**システム区分*/
	@Column(name = "SYSTEM_ATR")
	public int sysAtr;
	/**開始日*/
	@Column(name = "START_DATE")
	public GeneralDate startDate;
	/**終了日*/
	@Column(name = "END_DATE")
	public GeneralDate endDate;
	/**承認ルート区分*/
	@Column(name = "EMPLOYMENT_ROOT_ATR")
	public int employmentRootAtr;
	/**申請種類*/
	@Column(name = "APP_TYPE")
	public Integer applicationType;
	/**確認ルート種類*/
	@Column(name = "CONFIRMATION_ROOT_TYPE")
	public Integer confirmationRootType;
	/**届出ID*/
	@Column(name = "NOTICE_ID")
	public Integer noticeId;
	/**各業務エベントID*/
	@Column(name = "BUS_EVENT_ID")
	public String busEventId;

	@Override
	protected Object getKey() {
		return wwfmtComApprovalRootPK;
	}
}
