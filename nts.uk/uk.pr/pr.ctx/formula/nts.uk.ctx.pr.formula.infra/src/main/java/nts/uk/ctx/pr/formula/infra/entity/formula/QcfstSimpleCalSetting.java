package nts.uk.ctx.pr.formula.infra.entity.formula;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="QCFST_SIMPLE_CAL_SETTING")
public class QcfstSimpleCalSetting extends UkJpaEntity implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    public QcfstSimpleCalSettingPK qcfstSimpleCalSettingPK;	
	
	@Column(name = "ITEM_NAME")
	public String itemName;
	
	@Column(name = "REFERENCE_DATE_ATR")
	public BigDecimal referenceDateAtr;
	
	@Column(name = "CODE_NAME_ITEM_CTG")
	public BigDecimal codeNameItemCtg;
	
	@Column(name = "FROM_TABLE")
	public String fromTable;
	
	@Column(name = "WHERE_CONDITION")
	public String whereCondition;

	@Override
	protected Object getKey() {
		return this.qcfstSimpleCalSettingPK;
	}
}
