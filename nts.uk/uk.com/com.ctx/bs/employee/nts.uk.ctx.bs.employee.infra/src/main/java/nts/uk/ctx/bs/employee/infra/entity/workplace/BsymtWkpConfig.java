/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.entity.workplace;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class BsymtWkpConfig.
 */
@Getter
@Setter
@Entity
@Table(name = "BSYMT_WKP_CONFIG")
public class BsymtWkpConfig extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bsymt wkp config PK. */
	@EmbeddedId
	protected BsymtWkpConfigPK bsymtWkpConfigPK;

	/** The str D. */
	@Column(name = "STR_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate strD;

	/** The end D. */
	@Column(name = "END_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate endD;

	/** The bsymt wkp config info. */
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumns({
			@PrimaryKeyJoinColumn(name = "HISTORY_ID", referencedColumnName = "HISTORY_ID") })
	public BsymtWkpConfigInfo bsymtWkpConfig;

	/**
	 * Instantiates a new bsymt wkp config.
	 */
	public BsymtWkpConfig() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (bsymtWkpConfigPK != null ? bsymtWkpConfigPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof BsymtWkpConfig)) {
			return false;
		}
		BsymtWkpConfig other = (BsymtWkpConfig) object;
		if ((this.bsymtWkpConfigPK == null && other.bsymtWkpConfigPK != null)
				|| (this.bsymtWkpConfigPK != null
						&& !this.bsymtWkpConfigPK.equals(other.bsymtWkpConfigPK))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.bsymtWkpConfigPK;
	}

}
