package nts.uk.cnv.core.infra.entity.conversiontable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.JpaEntity;
import nts.uk.cnv.core.dom.constants.Constants;
import nts.uk.cnv.core.dom.conversionsql.ColumnExpression;
import nts.uk.cnv.core.dom.conversionsql.ColumnName;
import nts.uk.cnv.core.dom.conversionsql.Join;
import nts.uk.cnv.core.dom.conversionsql.RelationalOperator;
import nts.uk.cnv.core.dom.conversionsql.WhereSentence;
import nts.uk.cnv.core.dom.conversiontable.ConversionInfo;
import nts.uk.cnv.core.dom.conversiontable.ConversionSource;
import nts.uk.cnv.core.dom.conversiontable.ConversionTable;
import nts.uk.cnv.core.dom.conversiontable.OneColumnConversion;
import nts.uk.cnv.core.dom.conversiontable.pattern.ConversionPattern;
import nts.uk.cnv.core.dom.conversiontable.pattern.ConversionType;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeCodeToCode;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeCodeToId;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeDateTimeMerge;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeFileId;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeFixedValue;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeFixedValueWithCondition;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeGuid;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeNone;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeParent;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypePassword;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeStringConcat;
import nts.uk.cnv.core.infra.entity.conversiontable.pattern.ScvmtConversionTypeTimeWithDayAttr;

/**
 * コンバート表
 * @author ai_muto
 *
 */
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCVMT_CONVERSION_TABLE")
public class ScvmtConversionTable extends JpaEntity implements Serializable  {
	private static final long serialVersionUID = 1L;

	/* 主キー */
	@EmbeddedId
	public ScvmtConversionTablePk pk;

	@Column(name = "CONVERSION_TYPE")
	private String conversionType;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeNone typeNone;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeCodeToId typeCodeToId;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeCodeToCode typeCodeToCode;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeFixedValue typeFixedValue;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeFixedValueWithCondition typeFixedValueWithCondition;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeParent typeParent;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeStringConcat typeStringConcat;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeTimeWithDayAttr typeTimeWithDayAttr;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeDateTimeMerge typeDateTimeMerge;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeGuid typeGuid;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypePassword typePassword;

	@OneToOne(optional=true, mappedBy="conversionTable", cascade=CascadeType.ALL)
	public ScvmtConversionTypeFileId typeFileId;


	@Override
	protected Object getKey() {
		return pk;
	}

	public static ConversionTable toDomain(String tagetTableName, ConversionInfo info, List<OneColumnConversion> columns, ConversionSource source) {
		List<WhereSentence> where = createWhereSentence(tagetTableName, info, source.getCondition());

		return new ConversionTable(
					info.getDatebaseType().spec(),
					info.getTargetTable(tagetTableName),
					source.getDateColumnName(),
					source.getStartDateColumnName(),
					source.getEndDateColumnName(),
					where,
					columns
				);
	}

	public OneColumnConversion toDomain(ConversionInfo info, Join sourceJoin) {
		return new OneColumnConversion(
				this.pk.getTargetColumnName(),
				this.conversionType,
				this.createConversionPattern(info, sourceJoin)
			);
	}

	public static ScvmtConversionTable toEntity(String category, String table, int recordNo, OneColumnConversion domain) {
		ScvmtConversionTablePk pk = new ScvmtConversionTablePk(category, table, recordNo, domain.getTargetColumn());
		ConversionPattern conversionPattern = domain.getPattern();

		return ScvmtConversionTable.builder()
			.pk(pk)
			.conversionType(domain.getConversionType())
			.typeNone(ScvmtConversionTypeNone.toEntity(pk, conversionPattern))
			.typeCodeToId(ScvmtConversionTypeCodeToId.toEntity(pk, conversionPattern))
			.typeCodeToCode(ScvmtConversionTypeCodeToCode.toEntity(pk, conversionPattern))
			.typeFixedValue(ScvmtConversionTypeFixedValue.toEntity(pk, conversionPattern))
			.typeFixedValueWithCondition(ScvmtConversionTypeFixedValueWithCondition.toEntity(pk, conversionPattern))
			.typeParent(ScvmtConversionTypeParent.toEntity(pk, conversionPattern))
			.typeStringConcat(ScvmtConversionTypeStringConcat.toEntity(pk, conversionPattern))
			.typeTimeWithDayAttr(ScvmtConversionTypeTimeWithDayAttr.toEntity(pk, conversionPattern))
			.typeDateTimeMerge(ScvmtConversionTypeDateTimeMerge.toEntity(pk, conversionPattern))
			.typeGuid(ScvmtConversionTypeGuid.toEntity(pk, conversionPattern))
			.typePassword(ScvmtConversionTypePassword.toEntity(pk, conversionPattern))
			.typeFileId(ScvmtConversionTypeFileId.toEntity(pk, conversionPattern))
			.build();
	}

	private ConversionPattern createConversionPattern(ConversionInfo info, Join sourceJoin) {
		ConversionType type = ConversionType.parse(this.conversionType);

		switch(type) {
			case None:
				return typeNone.toDomain(info, sourceJoin);
			case CodeToId:
				return typeCodeToId.toDomain(info, sourceJoin);
			case CodeToCode:
				return typeCodeToCode.toDomain(info, sourceJoin);
			case FixedValue:
				return typeFixedValue.toDomain(info, sourceJoin);
			case FixedValueWithCondition:
				return typeFixedValueWithCondition.toDomain(info.getDatebaseType().spec(), sourceJoin);
			case Parent:
				return typeParent.toDomain(info, sourceJoin);
			case StringConcat:
				return typeStringConcat.toDomain(info.getDatebaseType().spec(), sourceJoin);
			case TimeWithDayAttr:
				return typeTimeWithDayAttr.toDomain(sourceJoin);
			case DateTimeMerge:
				return typeDateTimeMerge.toDomain(info, sourceJoin);
			case Guid:
				return typeGuid.toDomain(info.getDatebaseType().spec());
			case Password:
				return typePassword.toDomain(info, sourceJoin);
			case FileId:
				return typeFileId.toDomain(info, sourceJoin);
		}

		throw new RuntimeException("ConversionPatternが不正です");
	}

	private static List<WhereSentence> createWhereSentence(String tagetTableName, ConversionInfo info, String sourceCondition) {

		List<WhereSentence> where = new ArrayList<>();

		if (sourceCondition == null || sourceCondition.isEmpty()) return where;

		String[] conditions = sourceCondition.toUpperCase().split("AND");

		for (String condition : conditions) {
			RelationalOperator operator = null;
			for (RelationalOperator oreratorValue : RelationalOperator.values()) {
				if(condition.contains(oreratorValue.getSign())) {
					operator = oreratorValue;
					break;
				}
			}

			if(operator == null) throw new RuntimeException();

			String[] expressions = condition.split(operator.getSign());

			if(expressions.length < 2) throw new RuntimeException();

			where.add(new WhereSentence(
					new ColumnName(Constants.BaseTableAlias, expressions[0]),
					operator,
					Optional.of(new ColumnExpression(expressions[1]))
				));
		}

		return where;
	}

}
