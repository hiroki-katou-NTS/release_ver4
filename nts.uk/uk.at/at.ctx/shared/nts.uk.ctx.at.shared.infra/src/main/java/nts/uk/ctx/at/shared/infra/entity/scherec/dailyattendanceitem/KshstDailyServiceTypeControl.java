package nts.uk.ctx.at.shared.infra.entity.scherec.dailyattendanceitem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.DisplayAndInputControl;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.InputControlOfAttendanceItem;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@NoArgsConstructor
@Table(name = "KSHST_DAI_SER_TYPE_CTR")
public class KshstDailyServiceTypeControl extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KshstDailyServiceTypeControlPK kshstDailyServiceTypeControlPK;
	
	@Column(name = "USE_ATR")
	public int toUse;
	
	@Column(name = "CHANGED_BY_OTHERS")
	public int canBeChangedByOthers;
	
	@Column(name = "CHANGED_BY_YOU")
	public int youCanChangeIt;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
		@JoinColumn(name = "AUTHORITY_DAILY_ID", referencedColumnName = "AUTHORITY_DAILY_ID", insertable = false, updatable = false)
	})
	public KshstDailyAttdItemAuth dailyattditemauth;
	
	@Override
	protected Object getKey() {
		return kshstDailyServiceTypeControlPK;
	}
	
	public KshstDailyServiceTypeControl(KshstDailyServiceTypeControlPK kshstDailyServiceTypeControlPK, int toUse, int canBeChangedByOthers, int youCanChangeIt) {
		super();
		this.kshstDailyServiceTypeControlPK = kshstDailyServiceTypeControlPK;
		this.toUse = toUse;
		this.canBeChangedByOthers = canBeChangedByOthers;
		this.youCanChangeIt = youCanChangeIt;
	}
	
	public static KshstDailyServiceTypeControl toEntity(String companyID,String authorityDailyID, DisplayAndInputControl domain) {
		return new KshstDailyServiceTypeControl(
				new KshstDailyServiceTypeControlPK(companyID, authorityDailyID,domain.getItemDailyID()),
				domain.isToUse()?1:0,
				domain.getInputControl().isCanBeChangedByOthers()?1:0,
				domain.getInputControl().isYouCanChangeIt()?1:0
				); 
	}
	
	public DisplayAndInputControl  toDomain() {
		return new DisplayAndInputControl(
				this.kshstDailyServiceTypeControlPK.itemDailyID,
				this.toUse==1?true:false,
				new InputControlOfAttendanceItem(
						this.youCanChangeIt==1?true:false, 
						this.canBeChangedByOthers==1?true:false)
				); 
	}
	

}
