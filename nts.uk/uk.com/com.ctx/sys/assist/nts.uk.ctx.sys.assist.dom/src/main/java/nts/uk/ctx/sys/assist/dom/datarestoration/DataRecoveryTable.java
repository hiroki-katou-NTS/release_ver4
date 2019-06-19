package nts.uk.ctx.sys.assist.dom.datarestoration;

import lombok.Value;
import nts.uk.shr.com.enumcommon.NotUseAtr;

@Value
public class DataRecoveryTable {
	
	//List<List<String>> dataRecovery;
	String uploadId;
	String fileNameCsv;
	boolean hasSidInCsv;
	String tableEnglishName;
	String tableJapaneseName;
	Integer tableNo;
	NotUseAtr hasParentTblFlg;
	
}
