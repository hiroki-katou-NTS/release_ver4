package nts.uk.ctx.exio.infra.entity.input.transfer.conversion.pattern;

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
import nts.uk.cnv.core.dom.conversiontable.pattern.ConversionPattern;
import nts.uk.cnv.core.dom.conversiontable.pattern.TimeWithDayAttrPattern;
import nts.uk.ctx.exio.infra.entity.input.transfer.conversion.ScvmtConversionTable;
import nts.uk.ctx.exio.infra.entity.input.transfer.conversion.ScvmtConversionTablePk;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCVMT_CONVERSION_TYPE_TIME_WITH_DAY_ATTR")
public class ScvmtConversionTypeTimeWithDayAttr extends JpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public ScvmtConversionTablePk pk;

	@Column(name = "TIME_COLUMN_NAME")
	private String timeColumnName;

	@Column(name = "DAY_ATTR_COLUMN_NAME")
	private String dayAttrColumnName;

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

	public TimeWithDayAttrPattern toDomain(Join sourceJoin) {
		return new TimeWithDayAttrPattern(
				sourceJoin,
				this.timeColumnName,
				this.dayAttrColumnName
			);
	}

	public static ScvmtConversionTypeTimeWithDayAttr toEntity(ScvmtConversionTablePk pk, ConversionPattern conversionPattern) {
		if (!(conversionPattern instanceof TimeWithDayAttrPattern)) {
			return null;
		}

		TimeWithDayAttrPattern domain = (TimeWithDayAttrPattern) conversionPattern;

		return new ScvmtConversionTypeTimeWithDayAttr(pk, domain.getTimeColumn(), domain.getDayAttrColumn(), null);
	}

}
