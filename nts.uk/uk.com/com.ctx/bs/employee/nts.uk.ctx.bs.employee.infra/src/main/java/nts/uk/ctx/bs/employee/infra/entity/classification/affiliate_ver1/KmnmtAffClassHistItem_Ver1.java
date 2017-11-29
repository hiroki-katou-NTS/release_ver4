/**
 * 
 */
package nts.uk.ctx.bs.employee.infra.entity.classification.affiliate_ver1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import nts.arc.layer.infra.data.entity.JpaEntity;

/**
 * @author danpv
 *
 */
@Entity
@Table(name = "KMNMT_AFF_CLASS_HIS_ITEM")
public class KmnmtAffClassHistItem_Ver1 extends JpaEntity {

	@Id
	@Column(name = "HISTORY_ID")
	public String historyId;

	@Column(name = "SID")
	public String sid;

	@Column(name = "CLASSIFICATION_CODE")
	public String classificationCode;

	@Override
	protected Object getKey() {
		return historyId;
	}

}
