package nts.uk.ctx.exio.infra.entity.qmm.specificationitem;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.exio.dom.qmm.specificationitem.SpecificationItemName;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 明細書項目名称
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPBMT_SPEC_ITEM_NAME")
public class QpbmtSpecItemName extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@EmbeddedId
	public QpbmtSpecItemNamePk specItemNamePk;

	/**
	 * 名称
	 */
	@Basic(optional = false)
	@Column(name = "NAME")
	public String name;

	/**
	 * 略名
	 */
	@Basic(optional = false)
	@Column(name = "SHORT_NAME")
	public String shortName;

	/**
	 * その他言語名称
	 */
	@Basic(optional = true)
	@Column(name = "OTHER_LANGUAGE_NAME")
	public String otherLanguageName;

	/**
	 * 英語名称
	 */
	@Basic(optional = true)
	@Column(name = "ENGLISH_NAME")
	public String englishName;

	@Override
	protected Object getKey() {
		return specItemNamePk;
	}

	public SpecificationItemName toDomain() {
		return new SpecificationItemName(this.specItemNamePk.cid, this.specItemNamePk.salaryItemId, this.name,
				this.shortName, this.otherLanguageName, this.englishName);
	}

	public static QpbmtSpecItemName toEntity(SpecificationItemName domain) {
		return new QpbmtSpecItemName(new QpbmtSpecItemNamePk(domain.getCid(), domain.getSalaryItemId()),
				domain.getName().v(), domain.getShortName().v(),
				domain.getOtherLanguageName().map(i -> i.v()).orElse(null),
				domain.getEnglishName().map(i -> i.v()).orElse(null));
	}

}
