package nts.uk.ctx.at.record.app.find.log.dto;


import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.workrecord.log.CalExeSettingInfor;
import nts.uk.ctx.at.record.dom.workrecord.log.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.log.ExecutionTime;
import nts.uk.ctx.at.record.dom.workrecord.log.ObjectPeriod;
import nts.uk.ctx.at.record.dom.workrecord.log.SetInforReflAprResult;
import nts.uk.ctx.at.record.dom.workrecord.log.SettingInforForDailyCreation;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionLogDto {
	

	
	/**
	 * 就業計算と集計実行ログID
	 */
	private String empCalAndSumExecLogID;

	/**
	 * 実行内容
	 */
	private int executionContent;

	/**
	 * executionContent : Japan name
	 */
	private String executionContentName;
	
	/**
	 * エラーの有無
	 */
	private int existenceError;
	
	/**
	 * 実行日時
	 * start time - end time
	 */
	private ExecutionTime executionTime;
	
	/**
	 * 処理状況
	 */
	private int processStatus;
	
	/**
	 * 対象期間
	 * start date - end date
	 */
	private ObjectPeriod objectPeriod;
	
	/**
	 * 計算実行設定情報ID
	 */
	private String calExecutionSetInfoID;
	
	
	private SetInforReflAprResultDto reflectApprovalSetInfo;
	
	/**
	 * 日別作成の設定情報
	 */

	private SettingInforForDailyCreationDto dailyCreationSetInfo;
	/**
	 * 日別計算の設定情報
	 */

	private CalExeSettingInforDto dailyCalSetInfo;
	/**
	 * 月別集計の設定情報
	 */

	private CalExeSettingInforDto  monlyAggregationSetInfo;
	
	
	
	public static ExecutionLogDto fromDomain(ExecutionLog domain) {
		return new ExecutionLogDto(
				domain.getEmpCalAndSumExecLogID(), 
				domain.getExecutionContent().value,
				domain.getExecutionContent().nameId, //NAME japan
				domain.getExistenceError().value, 
				new ExecutionTime(
						domain.getExecutionTime().getStartTime(), 
						domain.getExecutionTime().getEndTime()),
				domain.getProcessStatus().value,
				new ObjectPeriod(
						domain.getObjectPeriod().getStartDate(), 
						domain.getObjectPeriod().getEndDate()),
				domain.getCalExecutionSetInfoID(),
				domain.getReflectApprovalSetInfo().isPresent()?SetInforReflAprResultDto.fromDomain(domain.getReflectApprovalSetInfo().get()):null,
				domain.getDailyCreationSetInfo().isPresent()?SettingInforForDailyCreationDto.fromDomain(domain.getDailyCreationSetInfo().get()):null,
				domain.getDailyCalSetInfo().isPresent() ?CalExeSettingInforDto.fromDomain(domain.getDailyCalSetInfo().get()):null,
				domain.getMonlyAggregationSetInfo().isPresent()?CalExeSettingInforDto.fromDomain(domain.getMonlyAggregationSetInfo().get()):null
				); 
	}
	
}
