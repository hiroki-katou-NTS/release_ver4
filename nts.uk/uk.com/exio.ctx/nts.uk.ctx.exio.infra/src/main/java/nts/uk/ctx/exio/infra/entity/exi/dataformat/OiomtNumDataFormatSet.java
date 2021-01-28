package nts.uk.ctx.exio.infra.entity.exi.dataformat;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.ctx.exio.dom.exi.dataformat.ItemType;
import nts.uk.ctx.exio.dom.exi.dataformat.NumDataFormatSet;
import nts.uk.ctx.exio.dom.exi.item.StdAcceptItem;
import nts.uk.ctx.exio.infra.entity.exi.item.OiomtStdAcceptItem;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 数値型データ形式設定
 */

@NoArgsConstructor
@Entity
@Table(name = "OIOMT_NUM_DATA_FORMAT_SET")
public class OiomtNumDataFormatSet extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@EmbeddedId
	public OiomtNumDataFormatSetPk numDataFormatSetPk;

	/**
	 * 固定値
	 */
	@Basic(optional = false)
	@Column(name = "FIXED_VALUE")
	public int fixedValue;

	/**
	 * 小数区分
	 */
	@Basic(optional = false)
	@Column(name = "DECIMAL_DIVISION")
	public int decimalDivision;

	/**
	 * 有効桁長
	 */
	@Basic(optional = false)
	@Column(name = "EFFECTIVE_DIGIT_LENGTH")
	public int effectiveDigitLength;

	/**
	 * コード変換コード
	 */
	@Basic(optional = true)
	@Column(name = "CD_CONVERT_CD")
	public String cdConvertCd;

	/**
	 * 固定値の値
	 */
	@Basic(optional = true)
	@Column(name = "VALUE_OF_FIXED_VALUE")
	public Double valueOfFixedValue;

	/**
	 * 少数桁数
	 */
	@Basic(optional = true)
	@Column(name = "DECIMAL_DIGIT_NUM")
	public Integer decimalDigitNum;

	/**
	 * 有効桁数開始桁
	 */
	@Basic(optional = true)
	@Column(name = "START_DIGIT")
	public Integer startDigit;

	/**
	 * 有効桁数終了桁
	 */
	@Basic(optional = true)
	@Column(name = "END_DIGIT")
	public Integer endDigit;

	/**
	 * 小数点区分
	 */
	@Basic(optional = true)
	@Column(name = "DECIMAL_POINT_CLS")
	public Integer decimalPointCls;

	/**
	 * 小数端数
	 */
	@Basic(optional = true)
	@Column(name = "DECIMAL_FRACTION")
	public Integer decimalFraction;

	@OneToOne
	@JoinColumns({ @JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
			@JoinColumn(name = "CONDITION_SET_CD", referencedColumnName = "CONDITION_SET_CD", insertable = false, updatable = false),
			@JoinColumn(name = "ACCEPT_ITEM_NUM", referencedColumnName = "ACCEPT_ITEM_NUMBER", insertable = false, updatable = false) })
	public OiomtStdAcceptItem acceptItem;

	@Override
	protected Object getKey() {
		return numDataFormatSetPk;
	}

	public OiomtNumDataFormatSet(String cid, String conditionCode, int acceptItemNum, int fixedValue,
			int decimalDivision, int effectiveDigitLength, String cdConvertCd, Double valueOfFixedValue,
			Integer decimalDigitNum, Integer startDigit, Integer endDigit, Integer decimalPointCls,
			Integer decimalFraction) {
		super();
		this.numDataFormatSetPk = new OiomtNumDataFormatSetPk(cid, conditionCode, acceptItemNum);
		this.fixedValue = fixedValue;
		this.decimalDivision = decimalDivision;
		this.effectiveDigitLength = effectiveDigitLength;
		this.cdConvertCd = cdConvertCd;
		this.valueOfFixedValue = valueOfFixedValue;
		this.decimalDigitNum = decimalDigitNum;
		this.startDigit = startDigit;
		this.endDigit = endDigit;
		this.decimalPointCls = decimalPointCls;
		this.decimalFraction = decimalFraction;
	}

	public static OiomtNumDataFormatSet fromDomain(StdAcceptItem item, NumDataFormatSet domain) {
		return new OiomtNumDataFormatSet(item.getCid(), item.getConditionSetCd().v(),
				item.getAcceptItemNumber(), domain.getFixedValue().value, domain.getDecimalDivision().value,
				domain.getEffectiveDigitLength().value,
				domain.getCdConvertCd().isPresent() ? domain.getCdConvertCd().get().v() : null,
				domain.getValueOfFixedValue().isPresent() ? domain.getValueOfFixedValue().get().v() : null,
				domain.getDecimalDigitNum().isPresent() ? domain.getDecimalDigitNum().get().v() : null,
				domain.getStartDigit().isPresent() ? domain.getStartDigit().get().v() : null,
				domain.getEndDigit().isPresent() ? domain.getEndDigit().get().v() : null,
				domain.getDecimalPointCls().isPresent() ? new Integer(domain.getDecimalPointCls().get().value) : null,
				domain.getDecimalFraction().isPresent() ? new Integer(domain.getDecimalFraction().get().value) : null);
	}

	public NumDataFormatSet toDomain() {
		return new NumDataFormatSet(ItemType.NUMERIC.value, this.fixedValue, this.decimalDivision,
				this.effectiveDigitLength, this.cdConvertCd, this.valueOfFixedValue, this.decimalDigitNum,
				this.startDigit, this.endDigit, this.decimalPointCls, this.decimalFraction);
	}

}
