package nts.uk.cnv.dom.cnv.conversiontable.pattern;

import lombok.Getter;
import nts.uk.cnv.dom.cnv.conversionsql.ConversionSQL;
import nts.uk.cnv.dom.cnv.conversionsql.Join;
import nts.uk.cnv.dom.cnv.conversionsql.SelectSentence;
import nts.uk.cnv.dom.cnv.conversiontable.pattern.manager.ParentJoinPatternManager;
import nts.uk.cnv.dom.cnv.service.ConversionInfo;

@Getter
public class ParentJoinPattern extends ConversionPattern {

	private Join sourceJoin;

	private Join parentJoin;

	private String parentColumn;

	private String targetTable;

	public ParentJoinPattern(ConversionInfo info, Join sourceJoin, Join parentJoin, String parentColumn) {
		super(info);
		this.sourceJoin = sourceJoin;
		this.parentJoin = parentJoin;
		this.parentColumn = parentColumn;
		this.targetTable = "";
	}

	public ParentJoinPattern(ConversionInfo info, Join sourceJoin, Join parentJoin, String parentColumn, String targetTable) {
		super(info);
		this.sourceJoin = sourceJoin;
		this.parentJoin = parentJoin;
		this.parentColumn = parentColumn;
		this.targetTable = targetTable;
	}

	@Override
	public ConversionSQL apply(ConversionSQL conversionSql) {
		conversionSql.getFrom().addJoin(sourceJoin);
		conversionSql.getFrom().addJoin(parentJoin);

		conversionSql.getSelect().add(
				SelectSentence.createNotFormat(
						parentJoin.tableName.getAlias(),
						ParentJoinPatternManager.parentValueColumnName
				));

		return conversionSql;
	}

}
