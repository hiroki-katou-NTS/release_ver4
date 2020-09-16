package nts.uk.cnv.dom.pattern;

import java.util.Arrays;

import nts.uk.cnv.dom.constants.Constants;
import nts.uk.cnv.dom.conversionsql.ColumnName;
import nts.uk.cnv.dom.conversionsql.ConversionSQL;
import nts.uk.cnv.dom.conversionsql.Join;
import nts.uk.cnv.dom.conversionsql.JoinAtr;
import nts.uk.cnv.dom.conversionsql.OnSentence;
import nts.uk.cnv.dom.conversionsql.SelectSentence;
import nts.uk.cnv.dom.conversionsql.TableName;
import nts.uk.cnv.dom.service.ConversionInfo;

public class PasswordPattern extends ConversionPattern {

	private Join sourceJoin;

	private String sourceColumnName;

	public PasswordPattern(ConversionInfo info, Join sourceJoin, String sourceColumnName) {
		super(info);
		this.sourceJoin = sourceJoin;
		this.sourceColumnName = sourceColumnName;
	}

	@Override
	public ConversionSQL apply(ConversionSQL conversionSql) {
		conversionSql.getFrom().addJoin(sourceJoin);

		Join mappingTableJoin = new Join(
				new TableName(info.getTargetDatabaseName(), info.getTargetSchema(), Constants.EncryptionTableName, Constants.EncryptionTableAlias),
				JoinAtr.InnerJoin,
				Arrays.asList(new OnSentence(
						new ColumnName(
							this.sourceJoin.tableName.getAlias(),
							this.sourceColumnName
						),
						new ColumnName(
							Constants.EncryptionTableAlias,
							Constants.kojin_id
						)
					))
			);

		conversionSql.getFrom().addJoin(mappingTableJoin);

		conversionSql.getSelect().add(
				SelectSentence.createNotFormat(Constants.EncryptionTableAlias, Constants.EncryptionColumnName));

		return conversionSql;
	}

}
