package nts.uk.ctx.exio.dom.input.transfer;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.val;
import nts.arc.task.tran.AtomTask;
import nts.uk.cnv.core.dom.conversionsql.ColumnExpression;
import nts.uk.cnv.core.dom.conversionsql.ConversionSQL;
import nts.uk.cnv.core.dom.conversionsql.WhereSentence;
import nts.uk.cnv.core.dom.conversiontable.ConversionCodeType;
import nts.uk.cnv.core.dom.conversiontable.ConversionSource;
import nts.uk.cnv.core.dom.conversiontable.ConversionTable;
import nts.uk.cnv.core.dom.conversiontable.pattern.NotChangePattern;
import nts.uk.ctx.exio.dom.input.ExecutionContext;
import nts.uk.ctx.exio.dom.input.domain.ImportingDomain;
import nts.uk.ctx.exio.dom.input.domain.ImportingDomainId;
import nts.uk.ctx.exio.dom.input.meta.ImportingDataMeta;
import nts.uk.ctx.exio.dom.input.workspace.WorkspaceTableName;

/**
 * 正準化データを移送する
 * @author ai_muto
 */
public class TransferCanonicalData {
	
	/**
	 * 全データを一括で移送する
	 * @param require
	 * @param context
	 * @return
	 */
	public static AtomTask transferAll(Require require, ExecutionContext context) {
		return transfer(require, context, Collections.emptyList());
	}
	
	/**
	 * データを社員ごとに移送する
	 * @param require
	 * @param context
	 * @param employeeId
	 * @return
	 */
	public static AtomTask transferByEmployee(Require require, ExecutionContext context, String employeeId) {
		
		val where = WhereSentence.equal("SID", ColumnExpression.stringLiteral(employeeId));
		
		return transfer(require, context, Arrays.asList(where));
	}
	
	private static AtomTask transfer(Require require, ExecutionContext context, List<WhereSentence> whereList) {
		
		val importingGroup = require.getImportingGroup(context.getGroupId());
		
		val conversionTables = require.getConversionTable(
				getConversionSource(require, context),
				importingGroup.getName(),
				context.getMode().getType());

		val itemNames = getImportingItemNames(require, context);
		val sqls = conversionTables.stream()
				.map(t -> createConversionSql(context, whereList, itemNames, t))
				.collect(toList());

		// 移送処理の実行
		return AtomTask.of(() -> {
			require.execute(sqls);
		});
	}

	private static List<String> getImportingItemNames(Require require, ExecutionContext context) {
		
		List<String> itemNames = new ArrayList<>();
		itemNames.addAll(require.getImportingDataMeta(context).getItemNames());
		itemNames.add("CONTRACT_CD");
		itemNames.add("CID");
		
		return itemNames;
	}

	private static ConversionSQL createConversionSql(
			ExecutionContext context,
			List<WhereSentence> whereList,
			List<String> importingItemNames,
			ConversionTable conversionTable) {
		
		conversionTable.getWhereList().addAll(whereList);
		
		// 受入項目の列名リストを元に移送する列をフィルタ
		ConversionTable filteredConversionTable = new ConversionTable(
				conversionTable.getSpec(),
				conversionTable.getTargetTableName(),
				conversionTable.getDateColumnName(),
				conversionTable.getStartDateColumnName(),
				conversionTable.getEndDateColumnName(),
				conversionTable.getWhereList(),
				conversionTable.getConversionMap().stream()
					.filter(m -> importingItemNames.contains(((NotChangePattern) m.getPattern()).getSourceColumn()))
					.collect(toList())
				);
		
		// TODO: Insert & Update両方のモードは未対応
		if(context.getMode().getType() == ConversionCodeType.INSERT) {
			return filteredConversionTable.createConversionSql();
		}
		else {
			return filteredConversionTable.createUpdateConversionSql();
		}
	}
	
	private static ConversionSource getConversionSource(Require require, ExecutionContext context) {
		
		val importingGroup = require.getImportingGroup(context.getGroupId());
		val base = require.getConversionSource(importingGroup.getName());
		
		val tableName = new WorkspaceTableName(context, importingGroup.getName());
		
		// Source側のテーブル名を一時テーブルの名前に変更する必要がある
		return new ConversionSource(
				base.getSourceId(),
				base.getCategory(),
				tableName.asCanonicalized(),
				base.getCondition(),
				base.getMemo(),
				base.getDateColumnName(),
				base.getStartDateColumnName(),
				base.getEndDateColumnName(),
				base.getDateType(),
				base.getPkColumns()
			);
	}

	public interface Require{
		ImportingDataMeta getImportingDataMeta(ExecutionContext context);
		ImportingDomain getImportingGroup(ImportingDomainId groupId);
		ConversionSource getConversionSource(String groupName);
		List<ConversionTable> getConversionTable(ConversionSource source, String groupName, ConversionCodeType cct);
		int execute(List<ConversionSQL> conversionSqls);
	}
	
}
