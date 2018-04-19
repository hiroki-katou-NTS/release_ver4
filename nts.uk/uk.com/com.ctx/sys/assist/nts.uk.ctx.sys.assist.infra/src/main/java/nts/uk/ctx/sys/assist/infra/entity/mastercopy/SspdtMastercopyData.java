package nts.uk.ctx.sys.assist.infra.entity.mastercopy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class SspdtMastercopyData.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="SSPDT_MASTERCOPY_DATA")
public class SspdtMastercopyData extends UkJpaEntity implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The master copy id. */
	@Id
	@Column(name="MASTER_COPY_ID")
	private String masterCopyId;

	/** The master copy target. */
	@Column(name="MASTER_COPY_TARGET")
	private Object masterCopyTarget;

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.masterCopyId;
	}

}