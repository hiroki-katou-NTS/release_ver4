package nts.uk.ctx.exio.dom.input.canonicalize.methods;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import lombok.Value;
import lombok.val;
import nts.uk.ctx.exio.dom.input.DataItem;
import nts.uk.ctx.exio.dom.input.DataItemList;
import nts.uk.ctx.exio.dom.input.ExecutionContext;
import nts.uk.ctx.exio.dom.input.canonicalize.CanonicalizedDataRecord;

/**
 * 正準化の中間結果
 * 履歴の開始日のように、その値自体が正準化によって変更される場合もあり、その場合はbeforeとafterに同じ項目Noで変更前後の値が入る
 */
@Value
public class IntermediateResult {
	
	/** 正準化したデータ */
	DataItemList itemsAfterCanonicalize;
	
	/** 正準化対象の正準化前データ */
	DataItemList itemsBeforeCanonicalize;
	
	/** 正準化対象ではなかったデータ */
	DataItemList itemsNotCanonicalize;
	
	/**
	 * 作る
	 * @param source 全部入り
	 * @param canonicalizedItems 正準化された項目のリスト
	 * @param targetItemNos 正準化対象となる項目Noのリスト
	 * @return
	 */
	public static IntermediateResult create(
			DataItemList source,
			DataItem canonicalizedItem,
			Integer... targetItemNos) {
		
		return create(source, new DataItemList(Arrays.asList(canonicalizedItem)), targetItemNos);
	}
	
	/**
	 * 新しくインスタンスを生成する
	 * @param source 全部入り
	 * @param canonicalizedItems 正準化された項目のリスト
	 * @param targetItemNos 正準化対象となる項目Noのリスト
	 * @return
	 */
	public static IntermediateResult create(
			DataItemList source,
			DataItemList canonicalizedItems,
			Integer... targetItemNos) {
		
		val before = new DataItemList();
		val not = new DataItemList();
		separate(source, before, not, targetItemNos);
		
		return new IntermediateResult(canonicalizedItems, before, not);
	}
	
	/**
	 * 現在の保持内容に対して新たに正準化した分を追加する
	 * @param canonicalizedItems
	 * @param targetItemNos
	 * @return
	 */
	public IntermediateResult addCanonicalized(
			DataItemList canonicalizedItems,
			Integer... targetItemNos) {

		val after = new DataItemList();
		after.addAll(itemsAfterCanonicalize);
		after.addAll(canonicalizedItems);
		
		val before = new DataItemList();
		val not = new DataItemList();
		separate(itemsNotCanonicalize, before, not, targetItemNos);
		
		return new IntermediateResult(after, before, not);
	}
	
	/**
	 * 指定した項目のデータを取り出す
	 * afterとbeforeの両方に入っている場合は、afterの値が優先
	 * @param itemNo
	 * @return
	 */
	public Optional<DataItem> getItemByNo(int itemNo) {
		
		return itemsAfterCanonicalize.getItemByNo(itemNo)
				.map(item -> Optional.of(item))
				.orElseGet(() -> itemsBeforeCanonicalize.getItemByNo(itemNo))
				.map(item -> Optional.of(item))
				.orElseGet(() -> itemsNotCanonicalize.getItemByNo(itemNo));
	}

	/**
	 * sourceの内容をtargetItemNosに指定されたものとそうでないもの仕分ける
	 * @param source
	 * @param targetList
	 * @param notTargetList
	 * @param targetItemNos
	 */
	private static void separate(
			DataItemList source,
			DataItemList targetList,
			DataItemList notTargetList,
			Integer... targetItemNos) {
		
		val targetItemNoSet = new HashSet<>(Arrays.asList(targetItemNos));
		
		for (DataItem item : source) {
			if (targetItemNoSet.contains(item.getItemNo())) {
				targetList.add(item);
			} else {
				notTargetList.add(item);
			}
		}
	}
	
	public CanonicalizedDataRecord complete(ExecutionContext context) {
		return new CanonicalizedDataRecord(
				context,
				itemsAfterCanonicalize,
				itemsBeforeCanonicalize,
				itemsNotCanonicalize);
	}
}
