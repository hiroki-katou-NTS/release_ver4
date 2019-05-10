package nts.uk.shr.com.task.schedule.internal;

import javax.ejb.Stateless;

import org.quartz.JobKey;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;

@Stateless
public class QuartzSchedulerRepository extends JpaRepository {

	public void deleteTriggers(JobKey jobKey) {

		this.deleteCronTriggers(jobKey.getName(), jobKey.getGroup());
		
	}
	
	private void deleteCronTriggers(String jobName, String jobGroup) {
		
		String sql = "delete c from QRTZ_CRON_TRIGGERS as c"
				+ " inner join QRTZ_TRIGGERS as t"
				+ " on t.TRIGGER_NAME = c.TRIGGER_NAME"
				+ " and t.TRIGGER_GROUP = c.TRIGGER_GROUP"
				+ " where t.JOB_NAME = ?"
				+ " and t.JOB_GROUP = ?";
		
		this.deleteBy(jobName, jobGroup, sql);
	}
	
	private void deleteTriggers(String jobName, String jobGroup) {
		
	}

	@SneakyThrows
	private void deleteBy(String jobName, String jobGroup, String sql) {
		try (val stmt = this.connection().prepareStatement(sql)) {
			stmt.setString(1, jobName);
			stmt.setString(2, jobGroup);
			stmt.executeUpdate();
		}
	}
}
