package nts.uk.ctx.sys.portal.infra.entity.toppage.widget;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SPTST_WIDGET_DISPLAY")
public class SptstWidgetDisplay extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public SptstWidgetDisplayPK sptstWidgetDisplayPK;

	@Column(name = "USE_ATR")
	public NotUseAtr useAtr;
	
	@Override
	protected Object getKey() {
		return sptstWidgetDisplayPK;
	}

}