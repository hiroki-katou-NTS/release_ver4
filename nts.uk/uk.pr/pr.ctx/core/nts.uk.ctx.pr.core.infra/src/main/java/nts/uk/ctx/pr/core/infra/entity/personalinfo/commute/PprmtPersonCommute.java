package nts.uk.ctx.pr.core.infra.entity.personalinfo.commute;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="PPRMT_PERSON_COMMUTE")
public class PprmtPersonCommute {

	@EmbeddedId
    public PprmtPersonCommutePK pprmtPersonCommutePK;
	
	@Column(name ="END_YM")
	public int endYm;
	
	@Column(name ="COMMU_NOTAX_LIMIT_PUB_NO")
	public String commuNotaxLimitPubNo;
	
	@Column(name ="COMMU_NOTAX_LIMIT_PRI_NO")
	public String commuNotaxLimitPriNo;
	
	@Column(name ="COMMU_MEANS_NAME")
	public String commuMeansName;
	
	@Column(name ="COMMU_DISTANCE_KM")
	public BigDecimal commuDistanceKm;
	
	@Column(name ="COMMU_APPLICATION_REASON")
	public String commuApplicationReason;
	
	@Column(name ="COMMU_FROM_ALLWAY")
	public String commuFromAllway;
	
	@Column(name ="COMMU_TO_ALLWAY")
	public String commuToAllway;
	
	@Column(name ="COMMU_ALLOWANCE_TOTAL")
	public BigDecimal commuToAllowanceTotal;
	
	//UseOrNot1
	@Column(name ="USE_OR_NOT1")
	public int useOrNot1;
	
	@Column(name ="COMMU_CYCLE1")
	public int commuteCycle1;
	
	@Column(name ="PAY_START_YM1")
	public int payStartYm1;
	
	@Column(name ="COMMU_ALLOWANCE1")
	public BigDecimal commuAllowance1;
	
	@Column(name ="COMMU_MEANS_ATR1")
	public int commuMeansAtr1;
	
	@Column(name ="COMMU_ALLOWANCE_1DAY1")
	public BigDecimal commuAllowance1Day1;
	
	@Column(name ="COMMU_ALLOWANCE_PASS1")
	public BigDecimal commuAllowancePass1;
	
	@Column(name ="COMMU_ALLOWANCE_PUB1")
	public BigDecimal commuAllowancePub1;
	
	@Column(name ="COMMU_FROM1")
	public String commuFrom1;
	
	@Column(name ="COMMU_TO1")
	public String commuTo1;
	
	@Column(name ="COMMU_MEANS1")
	public String commuMeans1;
	
	//UseOrNot2
	@Column(name ="USE_OR_NOT2")
	public int useOrNot2;
	
	@Column(name ="COMMU_CYCLE2")
	public int commuteCycle2;
	
	@Column(name ="PAY_START_YM2")
	public int payStartYm2;
	
	@Column(name ="COMMU_ALLOWANCE2")
	public BigDecimal commuAllowance2;
	
	@Column(name ="COMMU_MEANS_ATR2")
	public int commuMeansAtr2;
	
	@Column(name ="COMMU_ALLOWANCE_1DAY2")
	public BigDecimal commuAllowance1Day2;
	
	@Column(name ="COMMU_ALLOWANCE_PASS2")
	public BigDecimal commuAllowancePass2;
	
	@Column(name ="COMMU_ALLOWANCE_PUB2")
	public BigDecimal commuAllowancePub2;
	
	@Column(name ="COMMU_FROM2")
	public String commuFrom2;
	
	@Column(name ="COMMU_TO2")
	public String commuTo2;
	
	@Column(name ="COMMU_MEANS2")
	public String commuMeans2;
	
	//UseOrNot3
	@Column(name ="USE_OR_NOT3")
	public int useOrNot3;
	
	@Column(name ="COMMU_CYCLE3")
	public int commuteCycle3;
	
	@Column(name ="PAY_START_YM3")
	public int payStartYm3;
	
	@Column(name ="COMMU_ALLOWANCE3")
	public BigDecimal commuAllowance3;
	
	@Column(name ="COMMU_MEANS_ATR3")
	public int commuMeansAtr3;
	
	@Column(name ="COMMU_ALLOWANCE_1DAY3")
	public BigDecimal commuAllowance1Day3;
	
	@Column(name ="COMMU_ALLOWANCE_PASS3")
	public BigDecimal commuAllowancePass3;
	
	@Column(name ="COMMU_ALLOWANCE_PUB3")
	public BigDecimal commuAllowancePub3;
	
	@Column(name ="COMMU_FROM3")
	public String commuFrom3;
	
	@Column(name ="COMMU_TO3")
	public String commuTo3;
	
	@Column(name ="COMMU_MEANS3")
	public String commuMeans3;
	
	//UseOrNot4
	@Column(name ="USE_OR_NOT4")
	public int useOrNot4;
	
	@Column(name ="COMMU_CYCLE4")
	public int commuteCycle4;
	
	@Column(name ="PAY_START_YM4")
	public int payStartYm4;
	
	@Column(name ="COMMU_ALLOWANCE4")
	public BigDecimal commuAllowance4;
	
	@Column(name ="COMMU_MEANS_ATR4")
	public int commuMeansAtr4;
	
	@Column(name ="COMMU_ALLOWANCE_1DAY4")
	public BigDecimal commuAllowance1Day4;
	
	@Column(name ="COMMU_ALLOWANCE_PASS4")
	public BigDecimal commuAllowancePass4;
	
	@Column(name ="COMMU_ALLOWANCE_PUB4")
	public BigDecimal commuAllowancePub4;
	
	@Column(name ="COMMU_FROM4")
	public String commuFrom4;
	
	@Column(name ="COMMU_TO4")
	public String commuTo4;
	
	@Column(name ="COMMU_MEANS4")
	public String commuMeans4;
	
	//UseOrNot5
	@Column(name ="USE_OR_NOT5")
	public int useOrNot5;
	
	@Column(name ="COMMU_CYCLE5")
	public int commuteCycle5;
	
	@Column(name ="PAY_START_YM5")
	public int payStartYm5;
	
	@Column(name ="COMMU_ALLOWANCE5")
	public BigDecimal commuAllowance5;
	
	@Column(name ="COMMU_MEANS_ATR5")
	public int commuMeansAtr5;
	
	@Column(name ="COMMU_ALLOWANCE_1DAY5")
	public BigDecimal commuAllowance1Day5;
	
	@Column(name ="COMMU_ALLOWANCE_PASS5")
	public BigDecimal commuAllowancePass5;
	
	@Column(name ="COMMU_ALLOWANCE_PUB5")
	public BigDecimal commuAllowancePub5;
	
	@Column(name ="COMMU_FROM5")
	public String commuFrom5;
	
	@Column(name ="COMMU_TO5")
	public String commuTo5;
	
	@Column(name ="COMMU_MEANS5")
	public String commuMeans5;
}
