package nts.uk.ctx.at.record.infra.entity.log;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.val;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CaseSpecExeContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@Entity
@Table(name = "KRCST_CASE_SPEC_EXE_CONT")
public class KrcstCaseSpecExeContent extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcstCaseSpecExeContentPK krcstCaseSpecExeContentPK;

	@Column(name = "EXECUTION_CONTENT_ID")
	public String executionContentID;
	
	@Column(name = "ORDER_NUMBER")
	public int orderNumber;

	@Column(name = "USE_CASE_NAME")
	public String useCaseName;
	
	@Column(name = "MONTHLY_AGG_SET_INFOR_ID", nullable = true)
	public String monthlyAggSetInforID;
	
	@OneToOne(mappedBy="caseSpecExeContentMonthly", cascade = CascadeType.ALL, optional = true)
	@JoinTable(name = "KRCST_CAL_EXE_SET_INFO", joinColumns = {
		@JoinColumn(name="MONTHLY_AGG_SET_INFOR_ID", referencedColumnName="CAL_EXECUTION_SET_INFO_ID", insertable = false, updatable = false, nullable=true)
	})
	public KrcdtCalExeSetInfor monthlyAggSetInfor;
	
	@Column(name = "REF_APPROVAL_SET_INFO_ID", nullable = true)
	public String refApprovalSetInfoID;
	
	@OneToOne(mappedBy="caseSpecExeContentRef", cascade = CascadeType.ALL, optional = true)
	@JoinTable(name = "KRCST_CAL_EXE_SET_INFO", joinColumns = {
			@JoinColumn(name="REF_APPROVAL_SET_INFO_ID", referencedColumnName="CAL_EXECUTION_SET_INFO_ID", insertable = false, updatable = false, nullable=true)
	})
	public KrcdtCalExeSetInfor refApprovalSetInfo;
	
	@Column(name = "DAILY_CAL_SET_INFO_ID", nullable = true)
	public String dailyCalSetInfoID;
	
	@OneToOne(mappedBy="caseSpecExeContentCal", cascade = CascadeType.ALL, optional = true)
	@JoinTable(name = "KRCST_CAL_EXE_SET_INFO", joinColumns = {
			@JoinColumn(name="DAILY_CAL_SET_INFO_ID", referencedColumnName="CAL_EXECUTION_SET_INFO_ID", insertable = false, updatable = false, nullable=true)
	})
	public KrcdtCalExeSetInfor dailyCalSetInfo;
	
	@Column(name = "DAILY_CREAT_SET_INFO_ID", nullable = true)
	public String dailyCreatSetInforID;
	
	@OneToOne(mappedBy="caseSpecExeContentCreat", cascade = CascadeType.ALL, optional = true)
	@JoinTable(name = "KRCST_CAL_EXE_SET_INFO", joinColumns = {
			@JoinColumn(name="DAILY_CREAT_SET_INFO_ID", referencedColumnName="CAL_EXECUTION_SET_INFO_ID", insertable = false, updatable = false, nullable=true)
	})
	public KrcdtCalExeSetInfor dailyCreatSetInfor;

	public KrcstCaseSpecExeContent(KrcstCaseSpecExeContentPK krcstCaseSpecExeContentPK, int orderNumber,
			String useCaseName, String monthlyAggSetInforID, String refApprovalSetInfoID, String dailyCalSetInfoID,
			String dailyCreatSetInforID) {
		super();
		this.krcstCaseSpecExeContentPK = krcstCaseSpecExeContentPK;
		this.orderNumber = orderNumber;
		this.useCaseName = useCaseName;
		this.monthlyAggSetInforID = monthlyAggSetInforID;
		this.refApprovalSetInfoID = refApprovalSetInfoID;
		this.dailyCalSetInfoID = dailyCalSetInfoID;
		this.dailyCreatSetInforID = dailyCreatSetInforID;
	}

	public CaseSpecExeContent toDomain() {
		val domain = CaseSpecExeContent.createFromJavaType(
				this.krcstCaseSpecExeContentPK.caseSpecExeContentID,
				this.executionContentID,
				this.orderNumber, this.useCaseName);
		if (dailyCreatSetInfor != null)
			domain.setDailyCreationSetInfo(Optional.of(dailyCreatSetInfor.toDomain()));
		else
			domain.setDailyCreationSetInfo(Optional.empty());
		
		if (dailyCalSetInfo != null)
			domain.setDailyCalSetInfo(Optional.of(dailyCalSetInfo.toDomain()));
		else
			domain.setDailyCalSetInfo(Optional.empty());
		
		if (refApprovalSetInfo != null)
			domain.setReflectApprovalSetInfo(Optional.of(refApprovalSetInfo.toDomain()));
		else
			domain.setReflectApprovalSetInfo(Optional.empty());
		
		if (monthlyAggSetInfor != null)
			domain.setMonlyAggregationSetInfo(Optional.of(monthlyAggSetInfor.toDomain()));
		else
			domain.setMonlyAggregationSetInfo(Optional.empty());
		return domain;
	}

	@Override
	protected Object getKey() {
		return this.krcstCaseSpecExeContentPK;
	}

	

}
