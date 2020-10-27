/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.outsideot.breakdown;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.infra.entity.outsideot.breakdown.attendance.KshmtOutsideAtd;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * The Class KshmtOutsideDetail.
 */

@Getter
@Setter
@Entity
@Table(name = "KSHMT_OUTSIDE_DETAIL")
public class KshmtOutsideDetail extends ContractUkJpaEntity implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The kshst over time brd PK. */
    @EmbeddedId
    protected KshmtOutsideDetailPK kshmtOutsideDetailPK;
    
    /** The name. */
    @Column(name = "NAME")
    private String name;
    
    /** The use atr. */
    @Column(name = "USE_ATR")
    private int useAtr;
    
    /** The product number. */
    @Column(name = "PRODUCT_NUMBER")
    private int productNumber;
    
    /** The lst outside ot brd aten. */
	@JoinColumns({
			@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = true, updatable = true),
			@JoinColumn(name = "BRD_ITEM_NO", referencedColumnName = "BRD_ITEM_NO", insertable = true, updatable = true) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<KshmtOutsideAtd> lstOutsideOtBrdAten;
    

    /**
     * Instantiates a new kshst over time brd.
     */
    public KshmtOutsideDetail() {
    	super();
    }

    /**
     * Instantiates a new kshst outside ot brd.
     *
     * @param kshmtOutsideDetailPK the kshst outside ot brd PK
     */
    public KshmtOutsideDetail(KshmtOutsideDetailPK kshmtOutsideDetailPK) {
        this.kshmtOutsideDetailPK = kshmtOutsideDetailPK;
    }

    /**
     * Instantiates a new kshst over time brd.
     *
     * @param kshmtOutsideBrdPK the kshst over time brd PK
     * @param exclusVer the exclus ver
     * @param name the name
     * @param useAtr the use atr
     * @param productNumber the product number
     */
	public KshmtOutsideDetail(KshmtOutsideDetailPK kshmtOutsideDetailPK, int exclusVer, String name,
			short useAtr, short productNumber) {
		this.kshmtOutsideDetailPK = kshmtOutsideDetailPK;
		this.name = name;
		this.useAtr = useAtr;
		this.productNumber = productNumber;
	}

    /**
     * Instantiates a new kshst over time brd.
     *
     * @param cid the cid
     * @param brdItemNo the brd item no
     */
    public KshmtOutsideDetail(String cid, short brdItemNo) {
        this.kshmtOutsideDetailPK = new KshmtOutsideDetailPK(cid, brdItemNo);
    }


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (kshmtOutsideDetailPK != null ? kshmtOutsideDetailPK.hashCode() : 0);
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		// not set
		if (!(object instanceof KshmtOutsideDetail)) {
			return false;
		}
		KshmtOutsideDetail other = (KshmtOutsideDetail) object;
		if ((this.kshmtOutsideDetailPK == null && other.kshmtOutsideDetailPK != null)
				|| (this.kshmtOutsideDetailPK != null
						&& !this.kshmtOutsideDetailPK.equals(other.kshmtOutsideDetailPK))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.kshmtOutsideDetailPK;
	}
    
}
