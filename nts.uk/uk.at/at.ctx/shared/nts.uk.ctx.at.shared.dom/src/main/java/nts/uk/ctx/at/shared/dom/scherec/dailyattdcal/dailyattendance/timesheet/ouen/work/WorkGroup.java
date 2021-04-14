package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen.work;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.layer.dom.objecttype.DomainObject;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.taskframe.TaskFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.taskframe.TaskFrameSetting;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.taskframe.TaskFrameUsageSetting;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.taskmaster.Task;

@Getter
/** 作業グループ */
public class WorkGroup implements DomainObject {

	/** 作業CD1: int */
	private WorkCode workCD1;
	
	/** 作業CD2: int */
	private Optional<WorkCode> workCD2;
	
	/** 作業CD3: int */
	private Optional<WorkCode> workCD3;
	
	/** 作業CD4: int */
	private Optional<WorkCode> workCD4;
	
	/** 作業CD5: int */
	private Optional<WorkCode> workCD5;

	//[C-0] 作業グループ(作業CD1,作業CD2,作業CD3,作業CD4,作業CD5)
	private WorkGroup(WorkCode workCD1, Optional<WorkCode> workCD2, 
			Optional<WorkCode> workCD3, Optional<WorkCode> workCD4, Optional<WorkCode> workCD5) {
		super();
		this.workCD1 = workCD1;
		this.workCD2 = workCD2;
		this.workCD3 = workCD3;
		this.workCD4 = workCD4;
		this.workCD5 = workCD5;
	}
	
	public static WorkGroup create(WorkCode workCD1, Optional<WorkCode> workCD2, 
			Optional<WorkCode> workCD3, Optional<WorkCode> workCD4, Optional<WorkCode> workCD5){
		
		return new WorkGroup(workCD1, workCD2, workCD3, workCD4, workCD5);
	}
	
	public static WorkGroup create(String workCd1, String workCd2, String workCd3, String workCd4, String workCd5){
		
		return new WorkGroup(
				new WorkCode(workCd1), 
				Optional.of(workCode(workCd2)), 
				Optional.of(workCode(workCd3)),
				Optional.of(workCode(workCd4)),
				Optional.of(workCode(workCd5)));
	}

	private static WorkCode workCode(String workCd2) {
		return workCd2 == null ? null : new WorkCode(workCd2);
	}
	
//■Public
	/**
	 * @name [1] 作業内容を確認する										
	 * @Description 説明:作業グループに設定されている作業コードが利用可能か確認する。利用できないコードがあればfalseを返す
	 * @input require
	 * @output boolean
	 */
	public boolean checkWorkContents(Require require) {
		//$作業枠利用設定 = require.作業枠利用設定を取得する()
		TaskFrameUsageSetting taskFrameUsageSetting = require.getTaskFrameUsageSetting();
		//map<Int,作業コード> 作業内容 = {1-@作業CD1、																					
									//2-＠作業CD2、　　																				
									//3-＠作業CD3、　　																				
									//4-＠作業CD4、　　																				
									//5-＠作業CD5}
		Map<Integer, Optional<WorkCode>> workContents = new HashMap<Integer, Optional<WorkCode>>();
									workContents.put(1, Optional.of(this.workCD1));
									workContents.put(2, this.workCD2);
									workContents.put(3, this.workCD3);
									workContents.put(4, this.workCD4);
									workContents.put(5, this.workCD5);
		//$利用作業 = 作業内容：																											
				//filter [prv-1] 利用できる作業コードかをチェックする(require,$作業枠利用設定,$.key,$.value) == false				
				//first
		Optional<Map.Entry<Integer, Optional<WorkCode>>> workContent = workContents.entrySet().stream()
				.filter(c->this.checkWorkCode(require, taskFrameUsageSetting, new TaskFrameNo(c.getKey()), c.getValue()) == false)
				.findFirst();
		//if $利用作業.isPresent return false	
		if(workContent.isPresent()) return false;
		//	return true	
		return true;
	}
	/**
	 * @name [2] 作業内容の有効期限を確認する
	 * @Description 説明:作業グループに設定されている作業コードの有効期限をチェックする。
	 * @input require
	 * @input date	年月日
	 * @output 
	 */
	public void checkExpirationDate(Require require, GeneralDate date) {
		//map<Int,作業コード> 作業内容 = {1-@作業CD1、																					
									//2-＠作業CD2、　　																				
									//3-＠作業CD3、　　																				
									//4-＠作業CD4、　　																				
									//5-＠作業CD5}
		Map<Integer, Optional<WorkCode>> workContent = new HashMap<Integer, Optional<WorkCode>>();
									workContent.put(1, Optional.of(this.workCD1));
									workContent.put(2, this.workCD2);
									workContent.put(3, this.workCD3);
									workContent.put(4, this.workCD4);
									workContent.put(5, this.workCD5);
		//作業内容：作業の有効期限をチェックする#確認する(require,年月日,$.key,$.value)
		for (Map.Entry<Integer,Optional<WorkCode>> element : workContent.entrySet()) {
			CheckWorkExpirationDateService.check(require, date, new TaskFrameNo(element.getKey()), element.getValue());
		}
	}
	
//■Private
	/**
	 * @name [prv-1] 利用できる作業コードかをチェックする
	 * @Description 説明：作業枠が利用されてない、または作業マスタが削除されているか各する。問題ない場合trueを返す
	 * @input require
	 * @input taskFrameUsageSetting 作業枠利用設定
	 * @input taskFrameNo 作業枠NO
	 * @input code 作業コード
	 * @output boolean
	 */
	private boolean checkWorkCode(Require require, TaskFrameUsageSetting taskFrameUsageSetting, TaskFrameNo taskFrameNo, Optional<WorkCode> code) {
		//if $作業コード.isEmpty	return true	
		if(!code.isPresent()) return true;
		//$作業枠 = 作業枠利用設定.枠設定:																									
				//filter $.利用区分 = する																							
				//filter $.作業枠NO = 作業枠NO		
		List<TaskFrameSetting> workFrame = taskFrameUsageSetting.getFrameSettingList().stream()
				.filter(c->c.getUseAtr().isUse())
				.filter(c->c.getTaskFrameNo().v() == taskFrameNo.v())
				.collect(Collectors.toList());
		//if $作業枠.isEmpty		return false
		if(workFrame.isEmpty()) return false;
		//$作業 = require.作業を取得する(作業枠NO,作業コード)
		Optional<Task> task = require.getTask(taskFrameNo, code.get());
		//if $作業.isEmpty	 return false
		if(!task.isPresent()) return false;
		//return true			
		return true;
	}
	
//■Require
	public static interface Require extends CheckWorkExpirationDateService.Require{}
}
