package nts.uk.ctx.exio.dom.input.validation.condition.system.helper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import nts.uk.ctx.exio.dom.input.DataItem;
import nts.uk.ctx.exio.dom.input.DataItemList;
import nts.uk.ctx.exio.dom.input.ExecutionContext;
import nts.uk.ctx.exio.dom.input.canonicalize.ImportingMode;
import nts.uk.ctx.exio.dom.input.revise.ItemType;
import nts.uk.ctx.exio.dom.input.revise.reviseddata.RevisedDataRecord;
import nts.uk.ctx.exio.dom.input.validation.ImportableItem;

public class Helper {
	
	public static RevisedDataRecord DUMMY_RECORD(List<DataItem> itemList) {
		return new RevisedDataRecord(0, new DataItemList(itemList)); 
	}
	
	public static List<ImportableItem> DUMMY_ImportableItems(int itemNo){
		return Arrays.asList(
				new ImportableItem(
						Helper.DUMMY.GROUP_ID,
						itemNo,
						Helper.DUMMY.ITEM_TYPE,
						Optional.empty())
			);  
	}
	
	public static List<DataItem> DUMMY_dataItems(int itemNo){
		return Arrays.asList(new DataItem(
				itemNo, 
				Helper.DUMMY.ITEM_TYPE, 
				Helper.DUMMY.VALUE));
	}
	
	public static class DUMMY{
		public static String COMPANY_ID = "company";
		public static String SETTING_CODE = "settingCode";
		public static int GROUP_ID = 999;
		public static int ROW_NO = 999;
		public static Object VALUE = 999;
		public static ItemType ITEM_TYPE = ItemType.INS_TIME;
		public static ImportingMode MODE =  ImportingMode.INSERT_ONLY;
		public static ExecutionContext CONTEXT = new ExecutionContext(COMPANY_ID, SETTING_CODE, GROUP_ID, MODE);
	}
}
