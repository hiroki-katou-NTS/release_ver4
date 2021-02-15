package nts.uk.ctx.at.schedule.infra.entity.shift.businesscalendar.specificdate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Table(name = "KSMST_SPECIFIC_DATE_ITEM")
public class KsmstSpecificDateItem extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public KsmstSpecificDateItemPK ksmstSpecificDateItemPK;

	@Column(name = "USE_ATR")
	public Integer useAtr;

	@Column(name = "NAME")
	public String name;
	
	@Override
	protected Object getKey() {
		return ksmstSpecificDateItemPK;
	}
}
