package nts.uk.ctx.sys.assist.dom.datarestoration;

import lombok.Value;

@Value
public class DataRecoveryTable {
	
	//List<List<String>> dataRecovery;
	String uploadId;
	String fileNameCsv;
	boolean hasSidInCsv;
	String tableEnglishName;
	String tableJapaneseName;
	Integer tableNo;
	
}
