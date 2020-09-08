package nts.uk.cnv.infra.entity.conversiontable;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.JpaEntity;
import nts.uk.cnv.dom.conversionsql.TableName;
import nts.uk.cnv.dom.conversionsql.WhereSentence;
import nts.uk.cnv.dom.conversiontable.ConversionTable;
import nts.uk.cnv.dom.conversiontable.OneColumnConversion;
import nts.uk.cnv.dom.service.ConversionInfo;

/**
 * コンバート表
 * @author ai_muto
 *
 */
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCVMT_CONVERSION_TABLE")
public class ScvmtConversionTable extends JpaEntity implements Serializable  {
	private static final long serialVersionUID = 1L;

	/* 主キー */
	@EmbeddedId
	public ScvmtConversionTablePk pk;

	@Column(name = "TARGET_TBL_NAME")
	private String targetTableName;

	@Override
	protected Object getKey() {
		return pk;
	}

	public ConversionTable toDomain(ConversionInfo info, List<WhereSentence> where, List<OneColumnConversion> conversionMap) {
		return new ConversionTable(
					new TableName(info.getTargetDatabaseName(), info.getTargetSchema(), targetTableName, ""),
					where,
					conversionMap
				);
	}

}
