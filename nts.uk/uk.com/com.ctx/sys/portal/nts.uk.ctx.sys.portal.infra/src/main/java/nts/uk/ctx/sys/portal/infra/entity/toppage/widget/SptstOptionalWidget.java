package nts.uk.ctx.sys.portal.infra.entity.toppage.widget;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SPTST_OPTIONAL_WIDGET")
public class SptstOptionalWidget  extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public SptstOptionalWidgetPK sptstOptionalWidgetPK;

	@Override
	protected Object getKey() {
		return sptstOptionalWidgetPK;
	}

}
