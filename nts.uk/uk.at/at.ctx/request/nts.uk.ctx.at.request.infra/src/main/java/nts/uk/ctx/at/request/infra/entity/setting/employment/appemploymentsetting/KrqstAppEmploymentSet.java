package nts.uk.ctx.at.request.infra.entity.setting.employment.appemploymentsetting;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 *
 * @author loivt
 */
@Entity
@Table(name = "KRQST_APP_EMPLOYMENT_SET")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KrqstAppEmploymentSet extends UkJpaEntity implements Serializable {

	@Version
	@Column(name = "EXCLUS_VER")
	private Long version;
	
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected KrqstAppEmploymentSetPK krqstAppEmploymentSetPK;

    @Column(name = "HOLIDAY_TYPE_USE_FLG")
    private Integer holidayTypeUseFlg;
    
    @Column(name = "DISPLAY_FLAG")
    private Integer displayFlag;

    
    @OneToMany(targetEntity=KrqdtAppEmployWorktype.class, cascade = CascadeType.ALL, mappedBy = "KrqstAppEmploymentSet", orphanRemoval = true)
	@JoinTable(name = "KRQDT_APP_EMPLOY_WORKTYPE")
	public List<KrqdtAppEmployWorktype> krqdtAppEmployWorktype;
    
	@Override
	protected Object getKey() {
		return krqstAppEmploymentSetPK;
	}

    
}

