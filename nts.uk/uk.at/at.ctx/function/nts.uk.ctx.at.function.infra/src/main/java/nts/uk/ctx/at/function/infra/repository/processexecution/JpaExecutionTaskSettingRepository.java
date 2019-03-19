package nts.uk.ctx.at.function.infra.repository.processexecution;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskSettingRepository;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.ExecutionTaskSetting;
import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtExecutionTaskSetting;
import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtExecutionTaskSettingPK;

@Stateless
public class JpaExecutionTaskSettingRepository extends JpaRepository
		implements ExecutionTaskSettingRepository {
	/**
	 * Query strings
	 */
	private static final String SELECT_ALL = "SELECT ets FROM KfnmtExecutionTaskSetting ets ";
	private static final String SELECT_All_BY_CID_AND_EXECCD = SELECT_ALL
			+ "WHERE ets.kfnmtExecTaskSettingPK.companyId = :companyId AND ets.kfnmtExecTaskSettingPK.execItemCd = :execItemCd "
			+ "ORDER BY ets.kfnmtExecTaskSettingPK.execItemCd";
	

	/**
	 * get by key
	 */
	@Override
	public Optional<ExecutionTaskSetting> getByCidAndExecCd(String companyId, String execItemCd) {
		return this.queryProxy().query(SELECT_All_BY_CID_AND_EXECCD, KfnmtExecutionTaskSetting.class)
				.setParameter("companyId", companyId)
				.setParameter("execItemCd", execItemCd).getSingle(c -> c.toDomain());
	}

	/**
	 * insert
	 */
	@Override
	public void insert(ExecutionTaskSetting domain) {
		this.commandProxy().insert(KfnmtExecutionTaskSetting.toEntity(domain));
	}

	/**
	 * update
	 */
	@Override
	public void update(ExecutionTaskSetting domain) {
		KfnmtExecutionTaskSetting updateData = KfnmtExecutionTaskSetting.toEntity(domain);
		KfnmtExecutionTaskSetting oldData = this.queryProxy().find(updateData.kfnmtExecTaskSettingPK,
																	KfnmtExecutionTaskSetting.class).get();
		oldData.startDate = updateData.startDate;
		oldData.startTime = updateData.startTime;
		oldData.endTimeCls = updateData.endTimeCls;
		oldData.endTime = updateData.endTime;
		oldData.oneDayRepCls = updateData.oneDayRepCls;
		oldData.oneDayRepInterval = updateData.oneDayRepInterval;
		oldData.repeatCls = updateData.repeatCls;
		oldData.repeatContent = updateData.repeatContent;
		oldData.endDateCls = updateData.endDateCls;
		oldData.endDate = updateData.endDate;
		oldData.enabledSetting = updateData.enabledSetting;
		oldData.nextExecDateTime = updateData.nextExecDateTime;
		oldData.monday = updateData.monday;
		oldData.tuesday = updateData.tuesday;
		oldData.wednesday = updateData.wednesday;
		oldData.thursday = updateData.thursday;
		oldData.friday = updateData.friday;
		oldData.saturday = updateData.saturday;
		oldData.sunday = updateData.sunday;
		oldData.january = updateData.january;
		oldData.february = updateData.february;
		oldData.march = updateData.march;
		oldData.april = updateData.april;
		oldData.may = updateData.may;
		oldData.june = updateData.june;
		oldData.july = updateData.july;
		oldData.august = updateData.august;
		oldData.september = updateData.september;
		oldData.october = updateData.october;
		oldData.november = updateData.november;
		oldData.december = updateData.december;
		oldData.scheduleId = updateData.scheduleId;
		oldData.endScheduleId = updateData.endScheduleId;
		this.commandProxy().update(oldData);
		this.getEntityManager().flush();
	}

	/**
	 * remove
	 */
	@Override
	public void remove(String companyId, String execItemCd) {
		Optional<KfnmtExecutionTaskSetting> entityOpt =
				this.queryProxy().query(SELECT_All_BY_CID_AND_EXECCD, KfnmtExecutionTaskSetting.class)
						.setParameter("companyId", companyId)
						.setParameter("execItemCd", execItemCd).getSingle();
		if (entityOpt.isPresent()) {
			KfnmtExecutionTaskSettingPK kfnmtProcExecPK = new KfnmtExecutionTaskSettingPK(companyId, execItemCd);
			this.commandProxy().remove(KfnmtExecutionTaskSetting.class, kfnmtProcExecPK);
		}
	}

}
