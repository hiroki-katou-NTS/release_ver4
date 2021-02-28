package nts.uk.cnv.dom.cnv.conversiontable.pattern;

import lombok.Getter;
import nts.uk.cnv.dom.cnv.conversionsql.ConversionSQL;
import nts.uk.cnv.dom.cnv.conversionsql.Join;
import nts.uk.cnv.dom.cnv.conversionsql.SelectSentence;
import nts.uk.cnv.dom.cnv.service.ConversionInfo;

/**
 * そのまま移送するパターン
 * @author ai_muto
 */
@Getter
public class NotChangePattern extends ConversionPattern {

	private Join join;

	private String sourceColumn;

	public NotChangePattern(ConversionInfo info, Join join, String sourceColumn) {
		super(info);
		this.join = join;
		this.sourceColumn = sourceColumn;
	}

	@Override
	public ConversionSQL apply(ConversionSQL conversionSql) {
		conversionSql.getFrom().addJoin(join);

		conversionSql.getSelect().add(SelectSentence.createNotFormat(join.tableName.getAlias(), sourceColumn));
		return conversionSql;
	}

}
