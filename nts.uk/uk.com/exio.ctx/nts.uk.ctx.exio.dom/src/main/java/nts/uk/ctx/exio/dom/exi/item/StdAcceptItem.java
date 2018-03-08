package nts.uk.ctx.exio.dom.exi.item;

import java.util.Optional;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.exio.dom.exi.condset.AcScreenCondSet;
import nts.uk.ctx.exio.dom.exi.condset.AcceptanceConditionCode;
import nts.uk.ctx.exio.dom.exi.condset.SystemType;
import nts.uk.ctx.exio.dom.exi.dataformat.DataFormatSetting;
import nts.uk.ctx.exio.dom.exi.dataformat.ItemType;

/**
 * 受入項目（定型）
 */

@Getter
public class StdAcceptItem extends AggregateRoot {

	/**
	 * 会社ID
	 */
	private String cid;

	/**
	 * 条件設定コード
	 */
	private AcceptanceConditionCode conditionSetCd;

	/**
	 * 受入項目番号
	 */
	private int acceptItemNumber;

	/**
	 * システム種類
	 */
	private SystemType systemType;

	private String categoryId;
	
	/**
	 * CSV項目番号
	 */
	private int csvItemNumber;

	/**
	 * CSV項目名
	 */
	private String csvItemName;

	/**
	 * 項目型
	 */
	private ItemType itemType;

	/**
	 * カテゴリ項目NO
	 */
	private int categoryItemNo;

	/**
	 * 受入選別条件設定
	 */
	private Optional<AcScreenCondSet> acceptScreenConditionSetting;
	
	/**
	 * データ形式設定
	 */
	private DataFormatSetting dataFormatSetting;

	public StdAcceptItem(String cid, int systemType, String conditionSetCd, String categoryId, int acceptItemNumber, int categoryItemNo,
			int csvItemNumber, String csvItemName, int itemType, AcScreenCondSet acceptScreenConditionSetting, DataFormatSetting dataFormatSetting) {
		super();
		this.cid = cid;
		this.systemType = EnumAdaptor.valueOf(systemType, SystemType.class);
		this.conditionSetCd = new AcceptanceConditionCode(conditionSetCd);
		this.categoryId = categoryId;
		this.categoryItemNo = categoryItemNo;
		this.acceptItemNumber = acceptItemNumber;
		this.csvItemNumber = csvItemNumber;
		this.csvItemName = csvItemName;
		this.itemType = EnumAdaptor.valueOf(itemType, ItemType.class);
		this.acceptScreenConditionSetting = Optional.ofNullable(acceptScreenConditionSetting);
		this.dataFormatSetting = dataFormatSetting;
	}

}
