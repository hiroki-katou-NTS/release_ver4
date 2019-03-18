package nts.uk.ctx.sys.assist.dom.tablelist;

import java.util.List;
import java.util.Map;

import nts.arc.layer.infra.file.export.FileGeneratorContext;

public interface TableListRepository {
	
	void add(TableList domain);
	void update(TableList domain);
	List<TableList> getByOffsetAndNumber(String storeProcessingId, int offset, int number);
	List<TableList> getByProcessingId(String storeProcessingId);
	void getDataDynamic(TableList tableList, List<String> targetEmployeesSid, List<String> headerCsv3, FileGeneratorContext generatorContext);
	List<String> getAllColumnName(String tableName);
}
