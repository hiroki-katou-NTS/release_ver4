package nts.uk.ctx.sys.assist.infra.entity.storage;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.uk.ctx.sys.assist.dom.storage.DataStorageSelectionCategory;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
public class SspmtDataStorageSelectionCategory extends UkJpaEntity implements Serializable,
		DataStorageSelectionCategory.MementoGetter, DataStorageSelectionCategory.MementoSetter {
	private static final long serialVersionUID = 1L;

	// column 排他バージョン
	@Version
	@Column(name = "EXCLUS_VER")
	private long version;

	@EmbeddedId
	public SspmtDataStorageSelectionCategoryPk pk;

	@Basic(optional = false)
	@Column(name = "SYSTEM_TYPE")
	public int systemType;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "CONTRACT_CD", referencedColumnName = "CONTRACT_CD", insertable = false, updatable = false),
			@JoinColumn(name = "PATTERN_ATR", referencedColumnName = "PATTERN_ATR", insertable = false, updatable = false),
			@JoinColumn(name = "PATTERN_CD", referencedColumnName = "PATTERN_CD", insertable = false, updatable = false) })
	public SspmtDataStoragePatternSetting patternSetting;

	@Override
	public void setCategoryId(String categoryId) {
		if (pk == null)
			pk = new SspmtDataStorageSelectionCategoryPk();
		pk.categoryId = categoryId;
	}

	@Override
	public void setPatternCode(String patternCode) {
		if (pk == null)
			pk = new SspmtDataStorageSelectionCategoryPk();
		pk.patternCode = patternCode;
	}

	@Override
	public void setPatternClassification(int patternClassification) {
		if (pk == null)
			pk = new SspmtDataStorageSelectionCategoryPk();
		pk.patternClassification = patternClassification;
	}

	@Override
	public void setContractCode(String contractCode) {
		if (pk == null)
			pk = new SspmtDataStorageSelectionCategoryPk();
		pk.contractCode = contractCode;
	}

	@Override
	public String getCategoryId() {
		if (pk != null)
			return pk.categoryId;
		return null;
	}

	@Override
	public String getPatternCode() {
		if (pk != null)
			return pk.contractCode;
		return null;
	}

	@Override
	public int getPatternClassification() {
		if (pk != null)
			return pk.patternClassification;
		return 0;
	}

	@Override
	public String getContractCode() {
		if (pk != null)
			return pk.contractCode;
		return null;
	}

	@Override
	protected Object getKey() {
		return pk;
	}
}
