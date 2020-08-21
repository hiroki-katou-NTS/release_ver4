package nts.uk.ctx.at.schedule.dom.employeeinfo.scheduleteam.domainservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.uk.ctx.at.schedule.dom.employeeinfo.scheduleteam.BelongScheduleTeam;
import nts.uk.ctx.at.schedule.dom.employeeinfo.scheduleteam.ScheduleTeam;
import nts.uk.ctx.at.schedule.dom.employeeinfo.scheduleteam.ScheduleTeamCd;
import nts.uk.ctx.at.schedule.dom.employeeinfo.scheduleteam.ScheduleTeamName;
import nts.uk.ctx.at.schedule.dom.employeeinfo.scheduleteam.domainservice.GetScheduleTeamInfoService.Require;
@RunWith(JMockit.class)
public class GetScheduleTeamInfoServiceTest {

	@Injectable
	private Require require;
	
	/**
	 * require.所属スケジュールチームを取得する(社員リスト) is empty
	 * require.スケジュールチームを取得する($社員たちの職場グループIDリスト) is empty
	 */
	@Test
	public void testGet() {
		List<String> lstEmpId = Arrays.asList("emp1","emp2");
		List<String> listWorkplaceGroupID = new ArrayList<>();
		new Expectations() {
			{
				require.get(lstEmpId);
				
				require.getAllSchedule(listWorkplaceGroupID);
				
			}
		};
		
		List<EmpTeamInfor> result = GetScheduleTeamInfoService.get(require, lstEmpId)
				.stream().sorted((x, y) -> x.getEmployeeID().compareTo(y.getEmployeeID())).collect(Collectors.toList());
		assertFalse(result.isEmpty());
		
		assertThat(result)
		.extracting(d->d.getEmployeeID(),d->d.getOptScheduleTeamCd(),d->d.getOptScheduleTeamName())
		.containsExactly(
				tuple("emp1",Optional.empty(),Optional.empty()),
				tuple("emp2",Optional.empty(),Optional.empty()));
		
	}
	
	/**
	 * require.所属スケジュールチームを取得する(社員リスト) not empty
	 * require.スケジュールチームを取得する($社員たちの職場グループIDリスト) is empty
	 */
	@Test
	public void testGet_1() {
		List<String> lstEmpId = Arrays.asList("emp1","emp2");
		List<String> listWorkplaceGroupID = Arrays.asList("wkp1","wkp2");
		List<BelongScheduleTeam> listBelongScheduleTeam = Arrays.asList(
				new BelongScheduleTeam("emp1", "wkp1",new ScheduleTeamCd("ScheduleTeamCd1")),
				new BelongScheduleTeam("emp2", "wkp2",new ScheduleTeamCd("ScheduleTeamCd2"))
				);
		new Expectations() {
			{
				require.get(lstEmpId);
				result = listBelongScheduleTeam;
				
				require.getAllSchedule(listWorkplaceGroupID);
				
			}
		};
		
		List<EmpTeamInfor> result = GetScheduleTeamInfoService.get(require, lstEmpId)
				.stream().sorted((x, y) -> x.getEmployeeID().compareTo(y.getEmployeeID())).collect(Collectors.toList());
		assertFalse(result.isEmpty());
		
		assertThat(result)
		.extracting(d->d.getEmployeeID(),d->d.getOptScheduleTeamCd(),d->d.getOptScheduleTeamName())
		.containsExactly(
				tuple("emp1",Optional.of(new ScheduleTeamCd("ScheduleTeamCd1")),Optional.empty()),
				tuple("emp2",Optional.of(new ScheduleTeamCd("ScheduleTeamCd2")),Optional.empty()));
		
	}
	
	/**
	 * require.所属スケジュールチームを取得する(社員リスト) not empty
	 * require.スケジュールチームを取得する($社員たちの職場グループIDリスト) not empty
	 */
	@Test
	public void testGet_2() {
		List<String> lstEmpId = Arrays.asList("emp1","emp2", "emp3","emp4","emp5", "emp6");
		List<String> listWorkplaceGroupID = Arrays.asList("wkp1","wkp2","wkp3","wkp4","wkp5","wkp6");
		List<BelongScheduleTeam> listBelongScheduleTeam = Arrays.asList(
				new BelongScheduleTeam("emp1", "wkp1",new ScheduleTeamCd("ScheduleTeamCd1")),
				new BelongScheduleTeam("emp2", "wkp2",new ScheduleTeamCd("ScheduleTeamCd2")),
				new BelongScheduleTeam("emp3", "wkp3",new ScheduleTeamCd("ScheduleTeamCd3")),
				new BelongScheduleTeam("emp4", "wkp4",new ScheduleTeamCd("ScheduleTeamCd4")),
				new BelongScheduleTeam("emp5", "wkp5",new ScheduleTeamCd("ScheduleTeamCd5")),
				new BelongScheduleTeam("emp6", "wkp6",new ScheduleTeamCd("ScheduleTeamCd6"))
				);
		
		List<ScheduleTeam> listScheduleTeam = Arrays.asList(
				new ScheduleTeam("wkp1", new ScheduleTeamCd("ScheduleTeamCd1"),new ScheduleTeamName("ScheduleTeamName1"), Optional.empty()),
				new ScheduleTeam("wkp2", new ScheduleTeamCd("ScheduleTeamCd2"),new ScheduleTeamName("ScheduleTeamName2"), Optional.empty()),
				new ScheduleTeam("wkp3", new ScheduleTeamCd("ScheduleTeamCd3"),new ScheduleTeamName("ScheduleTeamName3"), Optional.empty()),
				new ScheduleTeam("wkp4", new ScheduleTeamCd("ScheduleTeamCd4"),new ScheduleTeamName("ScheduleTeamName4"), Optional.empty()),
				new ScheduleTeam("wkp5", new ScheduleTeamCd("ScheduleTeamCd5"),new ScheduleTeamName("ScheduleTeamName5"), Optional.empty()),
				new ScheduleTeam("wkp6", new ScheduleTeamCd("ScheduleTeamCd6"),new ScheduleTeamName("ScheduleTeamName6"), Optional.empty())
				);
		new Expectations() {
			{
				require.get(lstEmpId);
				result = listBelongScheduleTeam;
				
				require.getAllSchedule(listWorkplaceGroupID);
				result = listScheduleTeam;
			}
		};
		
		List<EmpTeamInfor> result = GetScheduleTeamInfoService.get(require, lstEmpId)
				.stream().sorted((x, y) -> x.getEmployeeID().compareTo(y.getEmployeeID())).collect(Collectors.toList());
		assertFalse(result.isEmpty());
		
		assertThat(result)
		.extracting(d->d.getEmployeeID(),d->d.getOptScheduleTeamCd(),d->d.getOptScheduleTeamName())
		.containsExactly(
				tuple("emp1",Optional.of(new ScheduleTeamCd("ScheduleTeamCd1")),Optional.of(new ScheduleTeamName("ScheduleTeamName1"))),
				tuple("emp2",Optional.of(new ScheduleTeamCd("ScheduleTeamCd2")),Optional.of(new ScheduleTeamName("ScheduleTeamName2"))),
				tuple("emp3",Optional.of(new ScheduleTeamCd("ScheduleTeamCd3")),Optional.of(new ScheduleTeamName("ScheduleTeamName3"))),
				tuple("emp4",Optional.of(new ScheduleTeamCd("ScheduleTeamCd4")),Optional.of(new ScheduleTeamName("ScheduleTeamName4"))),
				tuple("emp5",Optional.of(new ScheduleTeamCd("ScheduleTeamCd5")),Optional.of(new ScheduleTeamName("ScheduleTeamName5"))),
				tuple("emp6",Optional.of(new ScheduleTeamCd("ScheduleTeamCd6")),Optional.of(new ScheduleTeamName("ScheduleTeamName6"))));
		
	}

}
