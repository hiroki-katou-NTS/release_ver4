package nts.uk.cnv.core.dom.conversiontable.pattern;

import java.util.Optional;
import java.util.TreeMap;

import lombok.Getter;
import nts.uk.cnv.core.dom.conversionsql.ColumnExpression;
import nts.uk.cnv.core.dom.conversionsql.ConversionSQL;
import nts.uk.cnv.core.dom.conversionsql.Join;
import nts.uk.cnv.core.dom.conversionsql.SelectSentence;

@Getter
public class TimeWithDayAttrPattern extends ConversionPattern {

	private Join sourceJoin;

	private String timeColumn;

	private String dayAttrColumn;

	public TimeWithDayAttrPattern(Join sourceJoin, String timeColumn, String dayAttrColumn) {
		this.sourceJoin = sourceJoin;
		this.timeColumn = timeColumn;
		this.dayAttrColumn = dayAttrColumn;
	}

	@Override
	public ConversionSQL apply(ConversionSQL conversionSql) {
		conversionSql.getFrom().addJoin(sourceJoin);

		//0:当日　1:翌日　2:翌々日　9:前日
		String caseSentence =
			"CASE " + this.dayAttrColumn
			+ "   WHEN 0 THEN " + this.timeColumn
			+ "   WHEN 1 THEN " + this.timeColumn + " + 1440 "
			+ "   WHEN 2 THEN " + this.timeColumn + " + 2880 "
			+ "   WHEN 9 THEN " + this.timeColumn + " - 1440 "
			+ "   ELSE 0 "
			+ " END";

		conversionSql.getSelect().add(
			new SelectSentence(
				new ColumnExpression(Optional.empty(), caseSentence),
				new TreeMap<>()
			)
		);
		return conversionSql;
	}

}
