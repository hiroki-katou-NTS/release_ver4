package nts.uk.cnv.core.dom.conversionsql;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import lombok.Getter;
import nemunoki.oruta.shr.tabledefinetype.DatabaseSpec;

@Getter
public class ConversionUpdateSQL implements ConversionSQL {

	private UpdateSentence update;
	private FromSentence from;
	private List<WhereSentence> where;

	public ConversionUpdateSQL(TableFullName table, List<WhereSentence> where) {
		this.update = new UpdateSentence(table);
		this.from = new FromSentence();
		this.where = where;
	}

	@Override
	public void add(ColumnName column, ColumnExpression value) {
		this.update.add(column, value);
	}

	@Override
	public void add(ColumnName column, ColumnExpression value, TreeMap<FormatType, String> formatTable) {
		this.update.add(column, value);
	}

	@Override
	public void addJoin(Join join) {
		this.from.addJoin(join);
	}

	@Override
	public void addWhere(WhereSentence where) {
		this.where.add(where);
	}

	@Override
	public TableFullName getBaseTable() {
		return this.from.getBaseTable().orElse(null);
	}

	public String build(DatabaseSpec spec) {
		String whereString = (from.getBaseTable().isPresent() && where.size() > 0) ? "\r\n" + WhereSentence.join(where) : "";
		return this.update.sql() + "\r\n" +
				from.sql(spec) +
				whereString;
	}

	public void addOnSentense(ColumnName column, String newExpression) {
		Join source = this.from.getJoinTables().stream().findFirst().get();
		source.onSentences.add(new OnSentence(column, new ColumnName(newExpression), Optional.empty()));
	}
}
