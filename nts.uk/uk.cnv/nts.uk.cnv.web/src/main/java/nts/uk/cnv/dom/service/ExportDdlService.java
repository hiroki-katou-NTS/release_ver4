package nts.uk.cnv.dom.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.time.GeneralDate;
import nts.uk.cnv.dom.tabledefinetype.databasetype.DatabaseType;
import nts.uk.cnv.dom.tabledesign.TableDesign;

@Stateless
public class ExportDdlService {

	public String exportDdlAll(Require require, String type, boolean withComment, String branch, GeneralDate date) {
		List<TableDesign> tableDesigns = require.findAll(branch, date);

		List<String> sql = tableDesigns.stream()
				.map(td -> exportDdl(require, td, type, withComment).getDdl())
				.collect(Collectors.toList());

		return String.join("\r\n", sql);
	}

	public List<ExportDdlServiceResult>  exportDdl(Require require, String tableId, String type, boolean withComment, String branch, GeneralDate date) {
		List<TableDesign> tableDesign = require.find(tableId, branch, date);
		if(tableDesign.size() == 0) {
			throw new BusinessException(new RawErrorMessage("定義が見つかりません：" + tableId));
		}

		return tableDesign.stream()
			.map(td -> exportDdl(require, td, type, withComment))
			.collect(Collectors.toList());
	}

	private ExportDdlServiceResult exportDdl(Require require, TableDesign tableDesign, String type, boolean withComment) {

		if("uk".equals(type)) {
			return new ExportDdlServiceResult(tableDesign.createTableSql(),
					tableDesign.getVer().get().getBranch(),
					tableDesign.getVer().get().getDate().toString());
		}

		DatabaseType dbtype = DatabaseType.valueOf(type);

		String createTableSql;
		if(withComment) {
			createTableSql = tableDesign.createFullTableSql(dbtype.spec());
		}
		else {
			createTableSql = tableDesign.createSimpleTableSql(dbtype.spec());
		}

		return new ExportDdlServiceResult(createTableSql,
				tableDesign.getVer().get().getBranch(),
				tableDesign.getVer().get().getDate().toString());
	}

	public interface Require {
		List<TableDesign> findAll(String branch, GeneralDate date);
		List<TableDesign> find(String tablename, String branch, GeneralDate date);

	}
}
