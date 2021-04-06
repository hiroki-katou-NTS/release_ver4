package nts.uk.cnv.dom.td.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.uk.cnv.dom.td.schema.snapshot.TableListSnapshot;
import nts.uk.cnv.dom.td.schema.snapshot.TableSnapshot;
import nts.uk.cnv.dom.td.schema.tabledesign.TableDesign;
import nts.uk.cnv.dom.td.tabledefinetype.TableDefineType;
import nts.uk.cnv.dom.td.tabledefinetype.UkDataType;
import nts.uk.cnv.dom.td.tabledefinetype.databasetype.DatabaseType;

@Stateless
public class ExportDdlService {

	public String exportDdlAll(Require require, String type, boolean withComment) {
		TableListSnapshot tableListSnapshot = require.getTableList();

		List<String> sql = tableListSnapshot.getList().stream()
				.map(tableIdentity -> require.getTable(tableListSnapshot.getSnapshotId(), tableIdentity.getTableId()))
				.filter(td -> td.isPresent())
				.map(td -> exportDdl(require, td.get(), type, withComment))
				.collect(Collectors.toList());

		return String.join("\r\n", sql);
	}

	public String exportAllConstraintsDdl(Require require, String type) {
		TableDefineType tableDefineType;
		if("uk".equals(type)) {
			tableDefineType = new UkDataType();
		}
		else {
			tableDefineType = DatabaseType.valueOf(type).spec();
		}
		TableListSnapshot tableListSnapshot = require.getTableList();

		List<String> sql = tableListSnapshot.getList().stream()
				.map(tableIdentity -> require.getTable(tableListSnapshot.getSnapshotId(), tableIdentity.getTableId()))
				.filter(td -> td.isPresent())
				.map(td -> exportOnlyConstraintDdl(td.get(), tableDefineType))
				.filter(ddl -> !ddl.isEmpty())
				.collect(Collectors.toList());

		return String.join("", sql);
	}

	public String exportDdl(Require require, String tableName, String type, boolean withComment) {
		Optional<TableSnapshot> tss = require.getLatestTableSnapshot(tableName);
		if(!tss.isPresent()) {
			throw new BusinessException(new RawErrorMessage("定義が見つかりません：" + tableName));
		}

		return exportDdl(require, tss.get(), type, withComment);
	}

	private String exportDdl(Require require, TableDesign tableDesign, String type, boolean withComment) {

		TableDefineType tableDefineType;
		if("uk".equals(type)) {
			tableDefineType = new UkDataType();
		}
		else {
			tableDefineType = DatabaseType.valueOf(type).spec();
		}

		if(withComment) {
			return tableDesign.createFullTableSql(tableDefineType);
		}
		else {
			return tableDesign.createSimpleTableSql(tableDefineType);
		}
	}

	private String exportOnlyConstraintDdl(TableDesign tableDesign, TableDefineType tableDefineType) {
		return tableDesign.createConstraintSql(tableDefineType);
	}

	public interface Require {
		TableListSnapshot getTableList();
		Optional<TableSnapshot> getTable(String snapshotId, String tableId);
		Optional<TableSnapshot> getLatestTableSnapshot(String tableName);
	}
}
