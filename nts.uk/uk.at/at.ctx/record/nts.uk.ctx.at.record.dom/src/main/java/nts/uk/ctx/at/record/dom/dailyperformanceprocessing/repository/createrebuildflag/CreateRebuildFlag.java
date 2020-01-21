package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.createrebuildflag;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.RecreateFlag;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectWorkInforDomainService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectWorkInforDomainServiceImpl;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectWorkInforDomainServiceImpl.ExitStatus;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;

/**
 * 再作成フラグの作成
 * 
 * @author tutk
 *
 */
@Stateless
public class CreateRebuildFlag {
	
	@Inject
	private ReflectWorkInforDomainService reflectWorkInforDomainServiceImpl;
	
	@Inject
	private EditStateOfDailyPerformanceRepository editStateOfDailyPerformanceRepository;

	/**
	 * 
	 * @param employeeId 社員ID
	 * @param ymd 年月日
	 * @param executionType 実行種別：enum<実行種別>
	 * @param recreateByWorkTypeChange 勤務種別変更時に再作成：boolean
	 * @param reCreateWorkPlace 異動時に再作成：boolean
	 * @param empCalAndSumExecLogId optional(就業計算と集計実行ログID)
	 * @param workInfoOfDailyPerformance optional(勤務情報：日別実績の勤務情報)
	 * 
	 * return  RecreateFlag  再作成フラグ
	 */
	public RecreateFlag createRebuildFlag(String employeeId, GeneralDate ymd, ExecutionType executionType,
			boolean recreateByWorkTypeChange, boolean reCreateWorkPlace, Optional<String> empCalAndSumExecLogId,
			Optional<WorkInfoOfDailyPerformance> workInfoOfDailyPerformance) {
		//「再作成フラグ」を作成する - 再作成フラグ　=　しない
		RecreateFlag recreateFlag = RecreateFlag.DO_NOT;
		
		//INPUT．実行種別を確認
		//再実行
		if(executionType == ExecutionType.RERUN) {
			//再作成フラグ　=　する（日別作成）
			recreateFlag = RecreateFlag.CREATE_DAILY;
			return recreateFlag;
		}
		//通常実行
		//INPUT．日別実績が存在するか確認
		if(!workInfoOfDailyPerformance.isPresent()) {
			return recreateFlag;
		}
		//勤務種別変更時に再作成するかどうかを判断
		ExitStatus exitStatus = reflectWorkInforDomainServiceImpl.reCreateWorkType(employeeId, ymd,
				empCalAndSumExecLogId.isPresent() ? empCalAndSumExecLogId.get() : null, recreateByWorkTypeChange,
				reCreateWorkPlace);
		if (exitStatus == ExitStatus.DO_NOT_RECREATE) {
			return recreateFlag;
		}
		// 編集状態を取得
		List<EditStateOfDailyPerformance> editStateOfDailyPerformances = this.editStateOfDailyPerformanceRepository.findByKey(employeeId, ymd);
		if(editStateOfDailyPerformances.isEmpty()) {
			//再作成フラグ　=　する（日別作成）
			recreateFlag = RecreateFlag.CREATE_DAILY;
		}else {
			//再作成フラグ　=　する（更新自動実行）
			recreateFlag = RecreateFlag.UPDATE_AUTO_EXECUTION;
		}
		return recreateFlag;	
	}

}
