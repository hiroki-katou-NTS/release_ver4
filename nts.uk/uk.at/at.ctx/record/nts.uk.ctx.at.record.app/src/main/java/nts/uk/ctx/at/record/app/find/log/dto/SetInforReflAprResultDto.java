package nts.uk.ctx.at.record.app.find.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.SetInforReflAprResult;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetInforReflAprResultDto {
	/**
	 *  0 :通常実行
	 *  1 :再実行
	 */
	private int executionType;
	/**
	 * executionType : name Japan
	 */
	private String executionTypeName;
	
	boolean forciblyReflect;
	
	public static SetInforReflAprResultDto fromDomain(SetInforReflAprResult domain) {
		return new SetInforReflAprResultDto(
				domain.getExecutionType().value,
				domain.getExecutionType().nameId,
				domain.isForciblyReflect()
				);
		
	}
}
