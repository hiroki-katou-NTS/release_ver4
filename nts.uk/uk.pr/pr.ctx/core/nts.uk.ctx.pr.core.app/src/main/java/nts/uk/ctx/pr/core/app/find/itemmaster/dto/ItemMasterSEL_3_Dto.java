package nts.uk.ctx.pr.core.app.find.itemmaster.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMaster;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemMasterSEL_3_Dto {
	/** 項目コード */
	private String itemCode;
	private String itemAbName;

	public static ItemMasterSEL_3_Dto fromDomain(ItemMaster domain) {
		return new ItemMasterSEL_3_Dto(domain.getItemCode().v(),domain.getItemAbName().v());
	}
}
