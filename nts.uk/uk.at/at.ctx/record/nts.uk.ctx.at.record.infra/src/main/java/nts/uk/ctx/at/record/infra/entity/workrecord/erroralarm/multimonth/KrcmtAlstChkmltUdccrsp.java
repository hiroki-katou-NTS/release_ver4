package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.multimonth;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.multimonth.MulMonthCheckCondCosp;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcmtEralstCndgrp;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

//複数月のチェック条件(該当月数)
@NoArgsConstructor
@Entity
@Table(name = "KRCMT_ALST_CHKMLT_UDCCRSP")
public class KrcmtAlstChkmltUdccrsp extends ContractUkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "ERAL_CHECK_ID")	
	public String errorAlarmCheckID;
	
	//使用区分
	@Basic(optional = false)
	@Column(name = "IS_USE_FLG")
	public int isUseFlg;
	
	// 回数
	@Basic(optional = false)
	@Column(name = "TIMES")
	public int times;

	// 比較演算子
	@Basic(optional = false)
	@Column(name = "COMPARE_OPERATOR")
	public int compareOperator;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy="krcmtMulMonCondCosp", orphanRemoval=true)
	public KrcmtEralstCndgrp krcmtErAlAtdItemCon;
	
	@OneToOne
	@JoinColumns({ @JoinColumn(name = "ERAL_CHECK_ID", referencedColumnName = "ERAL_CHECK_ID", insertable = false, updatable = false) })
	public KrcmtAlstChkmltUd krcmtMulMonAlarmCheck;
	
	@Override
	protected Object getKey() {
		return this.errorAlarmCheckID;
	}
	
	public static KrcmtAlstChkmltUdccrsp toEntity(MulMonthCheckCondCosp domain) {
		return new KrcmtAlstChkmltUdccrsp(domain.getErrorAlarmCheckID(), 
				domain.isUsedFlg() == true ? 1 : 0,
				domain.getTimes(),
				domain.getCompareOperator(),
				KrcmtEralstCndgrp.toEntity(domain.getErrorAlarmCheckID(), 
						domain.getErAlAttendanceItemCondition(), true));
	}

	public MulMonthCheckCondCosp toDomain() {
		return new MulMonthCheckCondCosp(this.errorAlarmCheckID, 
				this.isUseFlg == 1 ? true : false,
				this.times,
				this.compareOperator,
				this.krcmtErAlAtdItemCon.toDomain(this.krcmtErAlAtdItemCon, null, null));
	}

	public KrcmtAlstChkmltUdccrsp(String errorAlarmCheckID, int isUseFlg, int times, int compareOperator,
			KrcmtEralstCndgrp krcmtErAlAtdItemCon) {
		super();
		this.errorAlarmCheckID = errorAlarmCheckID;
		this.isUseFlg = isUseFlg;
		this.times = times;
		this.compareOperator = compareOperator;
		this.krcmtErAlAtdItemCon = krcmtErAlAtdItemCon;
	}
	
}
