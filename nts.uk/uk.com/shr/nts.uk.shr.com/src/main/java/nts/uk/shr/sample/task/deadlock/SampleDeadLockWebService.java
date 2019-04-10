package nts.uk.shr.sample.task.deadlock;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * task1とtask2を別クライアントから同時に呼ぶと、デッドロックになる
 * @author m_kitahira
 *
 */
@Path("/sample/deadlock")
public class SampleDeadLockWebService {

	@Inject
	private SampleDeadLockTask task;

	@POST
	@Path("task1")
	public void task1() {
		this.task.task1();
	}
	
	@POST
	@Path("task2")
	public void task2() {
		this.task.task2();
	}
}
