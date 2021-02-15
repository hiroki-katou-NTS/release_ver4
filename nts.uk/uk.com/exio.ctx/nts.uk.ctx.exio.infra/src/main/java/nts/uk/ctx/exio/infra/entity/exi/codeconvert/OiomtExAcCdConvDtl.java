package nts.uk.ctx.exio.infra.entity.exi.codeconvert;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * コード変換詳細
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OIOMT_EX_AC_CD_CONV_DTL")
public class OiomtExAcCdConvDtl extends ContractUkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@EmbeddedId
	public OiomtCdConvertDetailsPk cdConvertDetailsPk;
	/**	契約コード */
	@Basic(optional = false)
	@Column(name = "CONTRACT_CD")
	public String contractCd;
	/**
	 * 出力項目
	 */
	@Basic(optional = false)
	@Column(name = "OUTPUT_ITEM")
	public String outputItem;

	/**
	 * 本システムのコード
	 */
	@Basic(optional = false)
	@Column(name = "SYSTEM_CD")
	public String systemCd;

	@ManyToOne
	@PrimaryKeyJoinColumns({ @PrimaryKeyJoinColumn(name = "CID", referencedColumnName = "CID"),
			@PrimaryKeyJoinColumn(name = "CONVERT_CD", referencedColumnName = "CONVERT_CD") })
	private OiomtExAcCdConv oiomtAcceptCdConvert;

	@Override
	protected Object getKey() {
		return cdConvertDetailsPk;
	}
}
