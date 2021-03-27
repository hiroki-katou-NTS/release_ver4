package nts.uk.ctx.at.shared.dom.scherec.application.overtime;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.onemonth.AgreementOneMonthTime;
/**
 * 36協定上限複数月平均時間
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
@Setter
public class Time36AgreeUpperLimitAverageShare extends DomainObject {
	
	/*
	 * 平均時間
	 */
	private List<Time36AgreeUpperLimitPerMonthShare> averageTimeLst; 
	
	/*
	 * 上限時間
	 */
	private AgreementOneMonthTime upperLimitTime;
	
	public Time36AgreeUpperLimitAverageShare(){
		this.averageTimeLst = new ArrayList<>();
		this.upperLimitTime = new AgreementOneMonthTime(0);
	}
}
