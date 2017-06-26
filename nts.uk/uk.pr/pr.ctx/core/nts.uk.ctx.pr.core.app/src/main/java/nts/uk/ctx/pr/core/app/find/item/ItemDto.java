package nts.uk.ctx.pr.core.app.find.item;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterV1;
/** Finder DTO of item */
@Value
public class ItemDto {	
	/**会社コード */
	String companyCode;
	/** 項目コード */
	String itemCode;
	/** カテゴリ区分 */
	int categoryAtr;
	/** item name */
	String itemAbName;
	/** item details */
	boolean checkUseHighError;
    int errRangeHigh;
    boolean checkUseLowError;
    int errRangeLow;
    boolean checkUseHighAlam;
    int alamRangeHigh;
    boolean checkUseLowAlam;
    int alamRangeLow;
	int taxAtr;
	
	public static ItemDto fromDomain(ItemMasterV1 domain){
		return new ItemDto(
				domain.getCompanyCode().v(), 
				domain.getItemCode().v(), 
				domain.getCategoryAtr().value,
				domain.getItemAbName().v(),
				(domain.getError().get(0).getIsUseHigh().value == 1),
				domain.getError().get(0).getRange().max().intValue(),
				(domain.getError().get(0).getIsUseLow().value == 1),
				domain.getError().get(0).getRange().min().intValue(),
				(domain.getAlarm().get(0).getIsUseHigh().value == 1),
				domain.getAlarm().get(0).getRange().max().intValue(),
				(domain.getAlarm().get(0).getIsUseLow().value == 1),
				domain.getAlarm().get(0).getRange().min().intValue(),
				domain.getTaxAtr().value
				);	
	}
}
