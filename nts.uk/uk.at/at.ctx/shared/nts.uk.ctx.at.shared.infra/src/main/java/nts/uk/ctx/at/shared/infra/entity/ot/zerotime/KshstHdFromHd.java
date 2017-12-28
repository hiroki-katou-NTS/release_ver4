package nts.uk.ctx.at.shared.infra.entity.ot.zerotime;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.ot.zerotime.HdFromHd;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * @author phongtq
 * 休日から休日への0時跨ぎ設定
 */
@NoArgsConstructor
@Entity
@Table(name = "KSHST_HD_FROM_HD ")
public class KshstHdFromHd extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主キー */
	@EmbeddedId
	public KshstHdFromHdPK kshstOverDayHdSetPK;

	/** 変更後の法定内休出NO */
	@Column(name = "LEGAL_HD_NO")
	public int legalHdNo;

	/** 変更後の法定外休出NO */
	@Column(name = "NON_LEGAL_HD_NO")
	public int nonLegalHdNo;

	/** 変更後の祝日休出NO */
	@Column(name = "NON_LEGAL_PUBLIC_HD_NO")
	public int nonLegalPublicHdNo;
	
	@ManyToOne
	@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false)
	public KshstZeroTimeSet overDayCalcSet;

	@Override
	protected Object getKey() {
		return kshstOverDayHdSetPK;
	}

	public KshstHdFromHd(KshstHdFromHdPK kshstOverDayHdSetPK, int legalHdNo, int nonLegalHdNo,
			int nonLegalPublicHdNo) {
		super();
		this.kshstOverDayHdSetPK = kshstOverDayHdSetPK;
		this.legalHdNo = legalHdNo;
		this.nonLegalHdNo = nonLegalHdNo;
		this.nonLegalPublicHdNo = nonLegalPublicHdNo;
	}
	
	public HdFromHd toDomain() {
		return HdFromHd.createFromJavaType(this.kshstOverDayHdSetPK.companyId, this.kshstOverDayHdSetPK.breakFrameNo, this.legalHdNo, this.nonLegalHdNo, this.nonLegalPublicHdNo);
	}
	
	public static KshstHdFromHd toEntity(HdFromHd domain){
		return new KshstHdFromHd(
				new KshstHdFromHdPK(domain.getCompanyId(),domain.getBreakFrameNo().v()),
				domain.getLegalHdNo().v(),
				domain.getNonLegalHdNo().v(), 
				domain.getNonLegalPublicHdNo().v());
	}

}
