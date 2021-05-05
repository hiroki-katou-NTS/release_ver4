package nts.uk.ctx.at.shared.dom.scherec.taskmanagement.domainservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.taskframe.TaskFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.taskframe.TaskFrameUsageSetting;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.taskmaster.Task;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.taskmaster.TaskCode;

/**
 * 
 * @author chungnt
 *
 */

@RunWith(JMockit.class)
public class GetWorkAvailableToEmployeesServiceTest {

	@Injectable
	private GetWorkAvailableToEmployeesService.Require require;

	private String companyID = "companyID";
	private String employeeID = "employeeID";
	private GeneralDate date = GeneralDate.today();
	private TaskFrameNo taskFrameNo = new TaskFrameNo(1);

	private TaskFrameUsageSetting taskFrameUsageSetting = GetWorkAvailableToEmployeesServiceHelper.getTask();
	private Task task = GetWorkAvailableToEmployeesServiceHelper.getTaskDefault();

	// $作業枠利用設定 isNull
	@Test
	public void test_1() {

		new Expectations() {
			{
				require.getTask();
			}
		};

		List<Task> result = GetWorkAvailableToEmployeesService.get(require, companyID, employeeID, date, taskFrameNo,
				Optional.empty());
		assertThat(result.isEmpty()).isTrue();
	}

	// $作業枠利用設定 isNotNull
	// 作業枠NO <> 1 AND 上位枠作業コード.isPresent
	//if $職場別作業の絞込.isNotPresent()
	//$親作業 isnotPresent
	@Test
	public void test_2() {

		new Expectations() {
			{
				require.getTask();
				result = taskFrameUsageSetting;
				
				require.getOptionalTask(taskFrameNo, new TaskCode("DUMMY"));
			}
		};

		List<Task> result = GetWorkAvailableToEmployeesService.get(require, companyID, employeeID, date, new TaskFrameNo(2),
				Optional.of(new TaskCode("DUMMY")));
		assertThat(result.isEmpty()).isTrue();
	}

	//if $子作業.isEmpty AND $絞込作業.isEmpty
	@Test
	public void test_3() {

		new Expectations() {
			{
				require.getTask();
				result = taskFrameUsageSetting;
				
				require.getOptionalTask(taskFrameNo, new TaskCode("DUMMY"));
				result = Optional.of(task);
			}
		};

		List<Task> result = GetWorkAvailableToEmployeesService.get(require, companyID, employeeID, date, new TaskFrameNo(2),
				Optional.of(new TaskCode("DUMMY")));
		assertThat(result.isEmpty()).isTrue();
	}
}
