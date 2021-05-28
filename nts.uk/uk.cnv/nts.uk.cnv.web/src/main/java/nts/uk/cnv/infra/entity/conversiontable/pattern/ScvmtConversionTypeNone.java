package nts.uk.cnv.infra.entity.conversiontable.pattern;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.JpaEntity;
import nts.uk.cnv.core.dom.conversionsql.Join;
import nts.uk.cnv.core.dom.conversiontable.ConversionInfo;
import nts.uk.cnv.core.dom.conversiontable.pattern.ConversionPattern;
import nts.uk.cnv.core.dom.conversiontable.pattern.NotChangePattern;
import nts.uk.cnv.infra.entity.conversiontable.ScvmtConversionTable;
import nts.uk.cnv.infra.entity.conversiontable.ScvmtConversionTablePk;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCVMT_CONVERSION_TYPE_NONE")
public class ScvmtConversionTypeNone extends JpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public ScvmtConversionTablePk pk;

	@Column(name = "SOURCE_COLUMN_NAME")
	private String sourceColumnName;

	@OneToOne(optional=true) @PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name="CATEGORY_NAME", referencedColumnName="CATEGORY_NAME"),
        @PrimaryKeyJoinColumn(name="TARGET_TBL_NAME", referencedColumnName="TARGET_TBL_NAME"),
        @PrimaryKeyJoinColumn(name="RECORD_NO", referencedColumnName="RECORD_NO"),
        @PrimaryKeyJoinColumn(name="TARGET_COLUMN_NAME", referencedColumnName="TARGET_COLUMN_NAME")
    })
	private ScvmtConversionTable conversionTable;

	@Override
	protected Object getKey() {
		return pk;
	}

	public NotChangePattern toDomain(ConversionInfo info, Join sourcejoin) {
		return new NotChangePattern(
				info,
				sourcejoin,
				this.sourceColumnName
			);
	}

	public static ScvmtConversionTypeNone toEntity(ScvmtConversionTablePk pk, ConversionPattern conversionPattern) {
		if (!(conversionPattern instanceof NotChangePattern)) {
			return null;
		}

		NotChangePattern domain = (NotChangePattern) conversionPattern;

		return new ScvmtConversionTypeNone(pk, domain.getSourceColumn(), null);
	}

}
