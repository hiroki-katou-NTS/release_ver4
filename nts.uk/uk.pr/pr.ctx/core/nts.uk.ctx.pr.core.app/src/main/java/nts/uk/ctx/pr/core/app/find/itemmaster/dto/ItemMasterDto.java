package nts.uk.ctx.pr.core.app.find.itemmaster.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMaster;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemMasterDto {
	/** 項目コード */
	private String itemCode;
	private String itemName;
	/** カテゴリ区分 */
	private int categoryAtr;
	private String categoryAtrName;
	/** item name */
	private String itemAbName;
	private int fixAtr;

	// sonnlb add
	private String itemAbNameE;
	private String itemAbNameO;
	private int displaySet;
	private String uniteCode;
	private int zeroDisplaySet;
	private int itemDisplayAtr;

	public static ItemMasterDto fromDomain(ItemMaster domain) {
		return new ItemMasterDto(domain.getItemCode().v(), domain.getItemName().v(), domain.getCategoryAtr().value,
				domain.getCategoryAtr().name, domain.getItemAbName().v(), domain.getFixAtr().value,
				domain.getItemAbNameE().v(), domain.getItemAbNameO().v(), domain.getDisplaySet().value,
				domain.getUniteCode().v(), domain.getZeroDisplaySet().value, domain.getItemDisplayAtr().value);
	}
}
