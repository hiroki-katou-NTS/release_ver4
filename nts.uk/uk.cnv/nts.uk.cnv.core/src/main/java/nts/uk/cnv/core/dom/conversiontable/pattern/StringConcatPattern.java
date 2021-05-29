package nts.uk.cnv.core.dom.conversiontable.pattern;

import java.util.Optional;

import lombok.Getter;
import nemunoki.oruta.shr.tabledefinetype.DatabaseSpec;
import nts.uk.cnv.core.dom.conversionsql.ConversionSQL;
import nts.uk.cnv.core.dom.conversionsql.Join;
import nts.uk.cnv.core.dom.conversionsql.SelectSentence;

@Getter
public class StringConcatPattern extends ConversionPattern {
	private DatabaseSpec spec;

	private Join sourceJoin;

	private String column1;

	private String column2;

	private Optional<String> delimiter;


	public StringConcatPattern(DatabaseSpec spec, Join sourceJoin, String column1, String column2, Optional<String> delimiter) {
		this.spec = spec;
		this.sourceJoin = sourceJoin;
		this.column1 = column1;
		this.column2 = column2;
		this.delimiter = delimiter;
	}

	@Override
	public ConversionSQL apply(ConversionSQL conversionSql) {
		conversionSql.getFrom().addJoin(sourceJoin);

		String source1 = sourceJoin.tableName.getAlias() + "." + column1;
		String source2 = sourceJoin.tableName.getAlias() + "." + column2;

		if(delimiter.isPresent() && !delimiter.get().isEmpty()) {
			source1 = spec.concat(source1, "'" + delimiter.get() + "'");
		}

		String concatString = spec.concat(source1, source2);


		conversionSql.getSelect().add(SelectSentence.createNotFormat("", concatString));

		return conversionSql;
	}

}
