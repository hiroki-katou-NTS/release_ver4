package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.otkcustomize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import nts.uk.ctx.at.record.dom.workrecord.erroralarm.otkcustomize.ContinuousHolCheckSet;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ContinuousVacationDays;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.DisplayMessage;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;


/**
 * The persistent class for the KRCMT_OTK_HD_CK database table.
 * 
 */
@Entity
@Table(name="KRCMT_OTK_HD_CK")
//@NamedQuery(name="KrcmtOtkHdCk.findAll", query="SELECT k FROM KrcmtOtkHdCk k")
public class KrcmtOtkHdCk extends ContractUkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CID")
	private String cid;

	@Column(name="USE_ATR")
	public int useAtr;

	@Column(name="CONTINUOUS_DAYS")
	public int continuousDays;

	@Column(name="MESSAGE_DISPLAY")
	public String messageDisplay;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcmtOtkHdCk")
	public List<KrcmtOtkHdCkWktpTgt> krcmtOtkHdCkWktpTgt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcmtOtkHdCk")
	public List<KrcctOtkWtNonTarget> krcctOtkWtNonTarget;
	
	public KrcmtOtkHdCk() {
	}

	public KrcmtOtkHdCk(String cid, int useAtr, int continuousDays, String messageDisplay) {
		super();
		this.cid = cid;
		this.useAtr = useAtr;
		this.continuousDays = continuousDays;
		this.messageDisplay = messageDisplay;
	}

	@Override
	protected Object getKey() {
		return this.cid;
	}

	public ContinuousHolCheckSet toDomain(){
		return new ContinuousHolCheckSet(cid, 
						krcmtOtkHdCkWktpTgt == null ? new ArrayList<>() : krcmtOtkHdCkWktpTgt.stream()
								.map(c -> new WorkTypeCode(c.id.worktypeCd)).collect(Collectors.toList()), 
						krcctOtkWtNonTarget == null ? new ArrayList<>() : krcctOtkWtNonTarget.stream()
								.map(c -> new WorkTypeCode(c.id.worktypeCd)).collect(Collectors.toList()), 
						useAtr == 1 ? true : false, new DisplayMessage(messageDisplay), new ContinuousVacationDays(continuousDays));
	}
	
	public static KrcmtOtkHdCk fromDomain(ContinuousHolCheckSet setting){
		return new KrcmtOtkHdCk(setting.getCompanyId(), setting.isUseAtr() ? 1 : 0, setting.getMaxContinuousDays().v(),
				setting.getDisplayMessege() == null ? null : setting.getDisplayMessege().v());
	}
}