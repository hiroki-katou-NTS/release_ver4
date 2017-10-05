package nts.uk.ctx.at.schedule.app.find.executionlog.dto;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.executionlog.CreateMethodAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ImplementAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ProcessExecutionAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ReCreateAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleCreateContentSetMemento;

/**
 * The Class ExecutionContentDto.
 */
public class ScheduleCreateContentDto implements ScheduleCreateContentSetMemento {

	/** The copy start date. */
	// コピー開始日
	public GeneralDate copyStartDate;

	// 作成方法区分
	public CreateMethodAtr createMethodAtr;

	/** The confirm. */
	// 作成時に確定済みにする
	public Boolean confirm;

	/** The implement atr. */
	// 実施区分
	public ImplementAtr implementAtr;

	/** The process execution atr. */
	// 処理実行区分
	public ProcessExecutionAtr processExecutionAtr;

	/** The re create atr. */
	// 再作成区分
	public ReCreateAtr reCreateAtr;

	/** The reset master info. */
	// マスタ情報再設定
	public Boolean resetMasterInfo;

	/** The reset absent holiday business. */
	// 休職休業再設定
	public Boolean resetAbsentHolidayBusines;

	/** The reset working hours. */
	// 就業時間帯再設定
	public Boolean resetWorkingHours;

	/** The reset time assignment. */
	// 申し送り時間再設定
	public Boolean resetTimeAssignment;

	/** The reset direct line bounce. */
	// 直行直帰再設定
	public Boolean resetDirectLineBounce;

	/** The reset time child care. */
	// 育児介護時間再設定
	public Boolean resetTimeChildCare;

	@Override
	public void setExecutionId(String executionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCopyStartDate(GeneralDate copyStartDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCreateMethodAtr(CreateMethodAtr createMethodAtr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConfirm(Boolean confirm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImplementAtr(ImplementAtr implementAtr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProcessExecutionAtr(ProcessExecutionAtr processExecutionAtr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReCreateAtr(ReCreateAtr reCreateAtr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResetMasterInfo(Boolean resetMasterInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResetAbsentHolidayBusines(Boolean resetAbsentHolidayBusines) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResetWorkingHours(Boolean resetWorkingHours) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResetTimeAssignment(Boolean resetTimeAssignment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResetDirectLineBounce(Boolean resetDirectLineBounce) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResetTimeChildCare(Boolean resetTimeChildCare) {
		// TODO Auto-generated method stub
		
	}

}
