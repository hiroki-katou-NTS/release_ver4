package nts.uk.shr.sample.task.deadlock;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.shr.com.system.config.internal.CisctSystemConfig;

@Stateless
public class SampleDeadLockTask extends JpaRepository {

	@SneakyThrows
	public void task1() {
		taskAt();
		Thread.sleep(4000);
		taskPr();
	}
	
	@SneakyThrows
	public void task2() {
		taskPr();
		Thread.sleep(2000);
		taskAt();
	}
	
	private void taskAt() {
		val at = this.queryProxy().find("VersionAT", CisctSystemConfig.class).get();
		at.value = Integer.toString(Integer.parseInt(at.value) + 1);
		this.getEntityManager().flush();
	}
	
	private void taskPr() {
		val pr = this.queryProxy().find("VersionPR", CisctSystemConfig.class).get();
		pr.value = Integer.toString(Integer.parseInt(pr.value) + 1);
		this.getEntityManager().flush();
	}
}
