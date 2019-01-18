/**
 * 2:06:01 PM Dec 6, 2017
 */
package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
//import javax.persistence.PrimaryKeyJoinColumn;
//import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCST_ER_AL_JOB_TITLE")
public class KrcstErAlJobTitle extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcstErAlJobTitlePK krcstErAlJobTitlePK;

	@Override
	protected Object getKey() {
		return this.krcstErAlJobTitlePK;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumns({
		@JoinColumn(name = "ERAL_CHECK_ID", referencedColumnName = "ERAL_CHECK_ID", insertable = false, updatable = false),
		@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false) 
	})
	public KrcmtErAlCondition krcmtErAlCondition;

	public KrcstErAlJobTitle(KrcstErAlJobTitlePK krcstErAlJobTitlePK) {
		super();
		this.krcstErAlJobTitlePK = krcstErAlJobTitlePK;
	}
	
}
