package nts.uk.ctx.at.function.app.command.processexecution;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import nts.arc.time.GeneralDateTime;
import nts.gul.web.communicate.typedapi.FailureCause;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskSettingRepository;
import nts.uk.shr.com.communicate.PathToWebApi;
import nts.uk.shr.com.communicate.batch.BatchServer;
//import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.uk.shr.com.task.schedule.ExecutionContext;
import nts.uk.shr.com.task.schedule.UkJobScheduler;
import nts.uk.shr.com.task.schedule.UkScheduledJob;

@Stateless
@Slf4j
public class SortingProcessScheduleJob extends UkScheduledJob {

	@Inject
	private SortingProcessCommandHandler sortingProcessCommandHandler;

	@Inject
	private ExecutionTaskSettingRepository execSettingRepo;
	
	@Inject
	private UkJobScheduler scheduler;

	@Inject
	private BatchServer batchServer;

	@Override
	protected void execute(ExecutionContext context) {

		String companyId = context.scheduletimeData().getString("companyId");
		String execItemCd = context.scheduletimeData().getString("execItemCd");
		ScheduleExecuteCommand s = new ScheduleExecuteCommand();
		s.setCompanyId(companyId);
		s.setExecItemCd(execItemCd);
		
		// scheduler.getNextFireTime(id)は、Quartzから実行されたサーバ上でなければ値が返らない。
		// そのため、ここであらかじめ取得してから渡すようにする。
		GeneralDateTime nextFireTime = this.execSettingRepo.getByCidAndExecCd(companyId, execItemCd)
				.map(e -> e.getScheduleId())
				.flatMap(id -> this.scheduler.getNextFireTime(id))
				.orElse(null);
		s.setNextDate(nextFireTime);

		if (this.batchServer.exists()) {
			log.info("Sent a request SortingProcessCommandHandler to Batch Server");
			val webApi = this.batchServer.webApi(PathToWebApi.at("/batch/sorting/process"),
					ScheduleExecuteCommand.class);
			this.batchServer.request(
					webApi,
					c -> c.entity(s).failed(f -> requestError(f)));
		} else {
			log.info("SortingProcessCommandHandler is executed on AP Server");
			this.sortingProcessCommandHandler.handle(s);
		}

	}
	
	private static void requestError(FailureCause failure) {
		String message = "request error: " + failure.getCauseType();
		if (failure.getException() == null) {
			log.error(message);
		} else {
			log.error(message, failure.getException());
		}
	}

}
