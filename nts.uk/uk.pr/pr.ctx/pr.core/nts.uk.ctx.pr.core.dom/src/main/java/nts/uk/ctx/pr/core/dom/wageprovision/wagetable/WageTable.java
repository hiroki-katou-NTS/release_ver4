package nts.uk.ctx.pr.core.dom.wageprovision.wagetable;

import java.util.Optional;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;

/**
 * 賃金テーブル
 */
@Getter
public class WageTable extends AggregateRoot {

	/**
	 * 会社ID
	 */
	private String cid;

	/**
	 * 賃金テーブルコード
	 */
	private WageTableCode wageTableCode;

	/**
	 * 賃金テーブル名
	 */
	private WageTableName wageTableName;

	/**
	 * 要素設定
	 */
	private ElementSetting elementSetting;

	/**
	 * 備考情報
	 */
	private Optional<Memo> remarkInformation;

	/**
	 * 要素情報
	 */
	private ElementInformation elementInformation;

	public WageTable(String cid, String wageTableCode, String wageTableName, Integer oneMasterNumericClassification,
			String oneFixedElement, String oneOptionalAdditionalElement, Integer twoMasterNumericClassification,
			String twoFixedElement, String twoOptionalAdditionalElement, Integer threeMasterNumericClassification,
			String threeFixedElement, String threeOptionalAdditionalElement, int elementSetting,
			String remarkInformation) {
		this.cid = cid;
		this.wageTableCode = new WageTableCode(wageTableCode);
		this.wageTableName = new WageTableName(wageTableName);
		this.elementInformation = new ElementInformation(oneMasterNumericClassification, oneFixedElement,
				oneOptionalAdditionalElement, twoMasterNumericClassification, twoFixedElement,
				twoOptionalAdditionalElement, threeMasterNumericClassification, threeFixedElement,
				threeOptionalAdditionalElement);
		this.elementSetting = EnumAdaptor.valueOf(elementSetting, ElementSetting.class);
		this.remarkInformation = remarkInformation == null ? Optional.empty()
				: Optional.of(new Memo(remarkInformation));
	}

	public WageTable(String cid, String wageTableCode, String wageTableName, Integer elementSetting,
			String remarkInformation, ElementInformation elementInformation) {
		this.cid = cid;
		this.wageTableCode = new WageTableCode(wageTableCode);
		this.wageTableName = new WageTableName(wageTableName);
		this.elementSetting = EnumAdaptor.valueOf(elementSetting, ElementSetting.class);
		this.remarkInformation = remarkInformation == null ? Optional.empty()
				: Optional.of(new Memo(remarkInformation));
		this.elementInformation = elementInformation;
	}

}
