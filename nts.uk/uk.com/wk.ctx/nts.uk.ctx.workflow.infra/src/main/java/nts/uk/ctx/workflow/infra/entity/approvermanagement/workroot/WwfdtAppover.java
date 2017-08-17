package nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * 承認者
 * @author hoatt
 *
 */
@Entity
@Table(name = "WWFDT_APPROVER")
@AllArgsConstructor
@NoArgsConstructor
public class WwfdtAppover extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**主キー*/
	@EmbeddedId
	public WwfdtAppoverPK wwfdtAppoverPK;
	/**職位ID*/
	@Column(name = "JOB_ID")
	public String jobId;
	/**社員ID*/
	@Column(name = "SID")
	public String employeeId;
	/**順序*/
	@Column(name = "ORDER_NUMBER")
	public int orderNumber;
	/**区分*/
	@Column(name = "APPROVAL_ATR")
	public int approvalAtr;
	/**確定者*/
	@Column(name = "CONFIRM_PERSON")
	public int confirmPerson;

	@Override
	protected Object getKey() {
		return wwfdtAppoverPK;
	}
}
