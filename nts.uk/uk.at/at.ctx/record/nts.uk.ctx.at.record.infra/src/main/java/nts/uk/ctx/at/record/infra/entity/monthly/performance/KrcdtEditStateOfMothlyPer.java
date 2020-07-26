package nts.uk.ctx.at.record.infra.entity.monthly.performance;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.performance.EditStateOfMonthlyPerformance;
import nts.uk.ctx.at.record.dom.monthly.performance.enums.StateOfEditMonthly;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * エンティティ：月別実績の編集状態
 * @author shuichu_ishida
 */
@Entity
@Table(name = "KRCDT_EDIT_STATE_MONTH")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KrcdtEditStateOfMothlyPer extends UkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** プライマリキー */
	@EmbeddedId
	public KrcdtEditStateOfMothlyPerPK krcdtEditStateOfMothlyPerPK;
	
	/** 編集状態 **/
	@Column(name = "STATE_OF_EDIT")
	public Integer stateOfEdit;
	
	/** 期間 - start */
	@Column(name = "STR_YMD")
	public GeneralDate startYmd;
	
	/** 期間 - end */
	@Column(name = "END_YMD")
	public GeneralDate endYmd;
	
	@Override
	protected Object getKey() {
		return this.krcdtEditStateOfMothlyPerPK;
	}
	
	public static final JpaEntityMapper<KrcdtEditStateOfMothlyPer> MAPPER = new JpaEntityMapper<>(KrcdtEditStateOfMothlyPer.class);

	/**
	 * ドメインに変換
	 * @return 月別実績の編集状態
	 */
	public EditStateOfMonthlyPerformance toDomain(){
		
		return new EditStateOfMonthlyPerformance(
				this.krcdtEditStateOfMothlyPerPK.employeeID,
				new YearMonth(this.krcdtEditStateOfMothlyPerPK.processDate),
				new DatePeriod(
						this.startYmd,
						this.endYmd),
				this.krcdtEditStateOfMothlyPerPK.attendanceItemID,
				EnumAdaptor.valueOf(this.krcdtEditStateOfMothlyPerPK.closureID, ClosureId.class),
				new ClosureDate(
						this.krcdtEditStateOfMothlyPerPK.closeDay,
						(this.krcdtEditStateOfMothlyPerPK.isLastDay == 1)),
				EnumAdaptor.valueOf(this.stateOfEdit, StateOfEditMonthly.class));
	}
}
