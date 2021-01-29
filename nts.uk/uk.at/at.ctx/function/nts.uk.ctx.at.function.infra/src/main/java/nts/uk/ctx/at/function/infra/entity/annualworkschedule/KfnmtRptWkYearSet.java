package nts.uk.ctx.at.function.infra.entity.annualworkschedule;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.uk.ctx.at.function.dom.annualworkschedule.ItemsOutputToBookTable;
import nts.uk.ctx.at.function.dom.annualworkschedule.SettingOutputItemOfAnnualWorkSchedule;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 年間勤務表の出力項目設定
 * 
 * @author LienPTK
 *
 */
@Data
@Entity
@Table(name = "KFNMT_RPT_WK_YEAR_SET")
@EqualsAndHashCode(callSuper = true)
public class KfnmtRptWkYearSet extends UkJpaEntity implements Serializable
															, SettingOutputItemOfAnnualWorkSchedule.MementoSetter
															, SettingOutputItemOfAnnualWorkSchedule.MementoGetter {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 項目設定ID */
	@Id
	@Column(name = "LAYOUT_ID")
	public String layoutId;
	
	/** 社員ID */
	@Column(name = "SID")
	private String sid;
	
	/** 会社ID */
	@Column(name = "CID")
	private String cid;

	/** 契約コード */
	@Column(name = "CONTRACT_CD")
	private String contractCd;
	
	/** 排他バージョン */
	@Version
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** 自由定形区分 */
	@Column(name = "SETTING_TYPE")
	private int settingType;

	/** コード */
	@Basic(optional = false)
	@Column(name = "CD")
	private String cd;

	/** 名称 */
	@Basic(optional = false)
	@Column(name = "NAME")
	private String name;
	
	/** 表示形式(未使用かも) */
	@Basic(optional = false)
	@Column(name = "DISPLAY_FORMAT")
	private int displayFormat;
	
	/** 年間勤務表印刷形式 */
	@Basic(optional = false)
	@Column(name = "PRINT_FORM")
	private int printForm;	

	/** 36協定時間を超過した月数を出力する */
	@Basic(optional = false)
	@Column(name = "OUT_NUM_EXCEED_TIME_36_AGR")
	private boolean outNumExceedTime36Agr;
	
	/** 複数月表示 */
	@Basic(optional = false)
	@Column(name = "MULTI_MON_DISP")
	private boolean multiMonthDisplay;
	
	/** 合計表示の月数 */
	@Basic(optional = false)
	@Column(name = "SUM_DISP_MONTHS")
	private Integer monthsInTotalDisplay;
	
	/** 合計平均表示 */
	@Basic(optional = false)
	@Column(name = "SUM_AVG_DISP")
	private Integer totalAverageDisplay;

	@OneToMany(mappedBy = "kfnmtRptWkYearSet", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<KfnmtRptWkYearItem> lstKfnmtRptWkYearItems;

	@Override
	protected Object getKey() {
		return this.layoutId;
	}

	@Override
	public List<ItemsOutputToBookTable> getListItemsOutput() {
		return this.lstKfnmtRptWkYearItems.stream().map(t -> {
			return ItemsOutputToBookTable.createFromMemento(t);
		}).collect(Collectors.toList());
	}

	@Override
	public void setListItemsOutput(List<ItemsOutputToBookTable> listItemsOutput) {
		this.lstKfnmtRptWkYearItems = listItemsOutput.stream().map(t -> {
			KfnmtRptWkYearItem kfnmtRptWkYearItem = new KfnmtRptWkYearItem();
			t.setMemento(kfnmtRptWkYearItem);
			kfnmtRptWkYearItem.setContractCd(AppContexts.user().contractCode());
			return kfnmtRptWkYearItem;
		}).collect(Collectors.toList());
	}
}
