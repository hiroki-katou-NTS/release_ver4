package nts.uk.ctx.bs.employee.dom.workplace.group.domainservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import mockit.Expectations;
import mockit.Injectable;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.group.AffWorkplaceGroup;
import nts.uk.ctx.bs.employee.dom.workplace.group.domainservice.GetWorkplaceNotWorkgroupService.Require;
import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformation;


public class GetWorkplaceNotWorkgroupServiceTest {
	@Injectable
	private Require require;
	
	String wKPID = "000000000000000000000000000000000011";
	String wKPGRPID = "00000000000001";
	GeneralDate baseDate = GeneralDate.today();
	@Test
	public void get_empty() {
		List<WorkplaceInformation> lstInfoImports = Collections.emptyList();
		new Expectations() {
			{
				require.getAllActiveWorkplace(baseDate);// dummy
				result = lstInfoImports;
			}
		};
		
		List<WorkplaceInformation> imports = GetWorkplaceNotWorkgroupService.getWorkplace(require, baseDate);
		
		assertThat(lstInfoImports.isEmpty()).isTrue();
		assertThat(imports.isEmpty()).isTrue();
	}
	
	@Test
	public void get_success() {
		List<WorkplaceInformation> lstInfoImports = DomainServiceHelper.getLstWpII();
		new Expectations() {
			{
				require.getAllActiveWorkplace(baseDate);// dummy
				result = lstInfoImports;
				
				require.getAll();// dummy
			}
		};
		
		List<WorkplaceInformation> importss = GetWorkplaceNotWorkgroupService.getWorkplace(require, baseDate);
		assertThat(importss).isNotEmpty();
	}
	
	@Test
	public void get_empty_2() {
		List<WorkplaceInformation> lstInfoImports = DomainServiceHelper.getLstWpIISecond();
		new Expectations() {
			{
				require.getAllActiveWorkplace(baseDate);// dummy
				result = lstInfoImports;
			}
		};
		List<AffWorkplaceGroup> workplaceGroup = DomainServiceHelper.getHelper();
		new Expectations() {
			{
				require.getAll();// dummy
				result = workplaceGroup;
			}
		};
		List<WorkplaceInformation> importss = GetWorkplaceNotWorkgroupService.getWorkplace(require, baseDate);
		assertThat(importss).isEmpty();
		
	}
}
