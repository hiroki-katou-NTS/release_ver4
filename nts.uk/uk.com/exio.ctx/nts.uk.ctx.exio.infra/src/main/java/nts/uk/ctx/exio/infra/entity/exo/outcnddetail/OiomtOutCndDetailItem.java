package nts.uk.ctx.exio.infra.entity.exo.outcnddetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 出力条件詳細項目
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OIOMT_OUT_CND_DETAIL_ITEM")
public class OiomtOutCndDetailItem extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@EmbeddedId
	public OiomtOutCndDetailItemPk outCndDetailItemPk;

	/**
	 * 会社ID
	 */
	@Basic(optional = true)
	@Column(name = "CID")
	public String cid;

	/**
	 * ユーザID
	 */
	@Basic(optional = true)
	@Column(name = "USER_ID")
	public String userId;

	/**
	 * 条件記号
	 */
	@Basic(optional = false)
	@Column(name = "CONDITION_SYMBOL")
	public int conditionSymbol;

	/**
	 * 検索数値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_NUM")
	public BigDecimal searchNum;

	/**
	 * 検索数値終了値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_NUM_END_VAL")
	public BigDecimal searchNumEndVal;

	/**
	 * 検索数値開始値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_NUM_START_VAL")
	public BigDecimal searchNumStartVal;

	/**
	 * 検索文字
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_CHAR")
	public String searchChar;

	/**
	 * 検索文字終了値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_CHAR_END_VAL")
	public String searchCharEndVal;

	/**
	 * 検索文字開始値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_CHAR_START_VAL")
	public String searchCharStartVal;

	/**
	 * 検索日付
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_DATE")
	public GeneralDate searchDate;

	/**
	 * 検索日付終了値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_DATE_END")
	public GeneralDate searchDateEnd;

	/**
	 * 検索日付開始値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_DATE_START")
	public GeneralDate searchDateStart;

	/**
	 * 検索時刻
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_CLOCK")
	public Integer searchClock;

	/**
	 * 検索時刻終了値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_CLOCK_END_VAL")
	public Integer searchClockEndVal;

	/**
	 * 検索時刻開始値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_CLOCK_START_VAL")
	public Integer searchClockStartVal;

	/**
	 * 検索時間
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_TIME")
	public Integer searchTime;

	/**
	 * 検索時間終了値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_TIME_END_VAL")
	public Integer searchTimeEndVal;

	/**
	 * 検索時間開始値
	 */
	@Basic(optional = true)
	@Column(name = "SEARCH_TIME_START_VAL")
	public Integer searchTimeStartVal;

	@Override
	protected Object getKey() {
		return outCndDetailItemPk;
	}

//	@OneToMany(targetEntity = OiomtSearchCodeList.class, cascade = CascadeType.ALL, mappedBy = "oiomtOutCndDetailItem", orphanRemoval = true, fetch = FetchType.LAZY)
//	@JoinTable(name = "OIOMT_SEARCH_CODE_LIST")
//	public List<OiomtSearchCodeList> oiomtSearchCodeList;

	public OiomtOutCndDetailItem(String categoryId, int categoryItemNo, String cid, String userId,
			String conditionSettingCd, int conditionSymbol, BigDecimal searchNum, BigDecimal searchNumEndVal,
			BigDecimal searchNumStartVal, String searchChar, String searchCharEndVal, String searchCharStartVal,
			GeneralDate searchDate, GeneralDate searchDateEnd, GeneralDate searchDateStart, Integer searchClock,
			Integer searchClockEndVal, Integer searchClockStartVal, Integer searchTime, Integer searchTimeEndVal,
			Integer searchTimeStartVal) {
		this.outCndDetailItemPk = new OiomtOutCndDetailItemPk(categoryId, categoryItemNo, conditionSettingCd);
		this.cid = cid;
		this.userId = userId;
		this.conditionSymbol = conditionSymbol;
		this.searchNum = searchNum;
		this.searchNumEndVal = searchNumEndVal;
		this.searchNumStartVal = searchNumStartVal;
		this.searchChar = searchChar;
		this.searchCharEndVal = searchCharEndVal;
		this.searchCharStartVal = searchCharStartVal;
		this.searchDate = searchDate;
		this.searchDateEnd = searchDateEnd;
		this.searchDateStart = searchDateStart;
		this.searchClock = searchClock;
		this.searchClockEndVal = searchClockEndVal;
		this.searchClockStartVal = searchClockStartVal;
		this.searchTime = searchTime;
		this.searchTimeEndVal = searchTimeEndVal;
		this.searchTimeStartVal = searchTimeStartVal;
	}

}