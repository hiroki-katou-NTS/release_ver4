package nts.uk.ctx.exio.dom.input.revise.reviseddata;

import java.util.Optional;

import lombok.Value;
import nts.uk.ctx.exio.dom.input.DataItem;
import nts.uk.ctx.exio.dom.input.DataItemList;

/**
 * 編集済みの1行分のデータ
 */
@Value
public class RevisedDataRecord {
	
	/** 受入カテゴリID */
	int categoryId;

	DataItemList items;
	
	public Optional<DataItem> getItemByNo(int itemNo) {
		return items.getItemByNo(itemNo);
	}
}
