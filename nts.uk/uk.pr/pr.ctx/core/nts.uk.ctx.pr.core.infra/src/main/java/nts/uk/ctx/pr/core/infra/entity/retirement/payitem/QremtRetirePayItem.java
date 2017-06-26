package nts.uk.ctx.pr.core.infra.entity.retirement.payitem;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="QREMT_RETIRE_PAY_ITEM")
public class QremtRetirePayItem extends UkJpaEntity {
	@EmbeddedId
	public QremtRetirePayItemPK qremtRetirePayItemPK;
	
	@Column(name="ITEM_NAME")
	public String itemName;
	
	@Column(name="ITEM_AB_NAME")
	public String printName;
	
	@Column(name="ITEM_AB_NAME_E")
	public String englishName;
	
	@Column(name="ITEM_AB_NAME_O")
	public String fullName;

	@Column(name="MEMO")
	public String memo;

	@Override
	protected Object getKey() {
		return qremtRetirePayItemPK;
	}
}
