package nts.uk.ctx.at.shared.infra.entity.worktype.language;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name="KSHMT_WORKTYPE_LANGUAGE")
@AllArgsConstructor
@NoArgsConstructor
public class KshmtWorkTypeLanguage extends UkJpaEntity implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public KshmtWorkTypeLanguagePK  kmnmtWorkTypeLanguagePK;
	/*名称*/
	@Column(name = "NAME")
	public String name;
	/*略名*/
	@Column(name = "ABNAME")
	public String abname;
	
	@Override
	protected Object getKey() {
		return kmnmtWorkTypeLanguagePK;
	}
	
	

}
