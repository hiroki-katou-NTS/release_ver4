package nts.uk.ctx.basic.app.command.organization.department;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.organization.department.Department;
import nts.uk.ctx.basic.dom.organization.department.DepartmentCode;
import nts.uk.ctx.basic.dom.organization.department.DepartmentGenericName;
import nts.uk.ctx.basic.dom.organization.department.DepartmentMemo;
import nts.uk.ctx.basic.dom.organization.department.DepartmentName;
import nts.uk.ctx.basic.dom.organization.department.DepartmentRepository;
import nts.uk.ctx.basic.dom.organization.shr.HierarchyCode;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class UpdateDepartmentCommandHandler extends CommandHandler<List<UpdateDepartmentCommand>> {

	@Inject
	private DepartmentRepository departmentRepository;

	@Override
	protected void handle(CommandHandlerContext<List<UpdateDepartmentCommand>> context) {
		String companyCode = AppContexts.user().companyCode();
		List<Department> listDep = new ArrayList<Department>();
		List<Department> listDep1 = new ArrayList<Department>();
		Date startDate1 = new Date();
		Date endDate1 = new Date();
		if ((context.getCommand().size() == 1)
				&& (context.getCommand().get(0).getMemo().compareTo("addhistoryfromlatest") != 0)) {
			// update department when click register button
			if (!departmentRepository.isExistDepartment(companyCode, context.getCommand().get(0).getHistoryId(),
					new DepartmentCode(context.getCommand().get(0).getDepartmentCode()))) {
				throw new BusinessException("ER06");
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			try {
				startDate1 = formatter.parse(context.getCommand().get(0).getStartDate());
				endDate1 = formatter.parse(context.getCommand().get(0).getEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			GeneralDate startDate = GeneralDate.legacyDate(startDate1);
			GeneralDate endDate = GeneralDate.legacyDate(endDate1);
			Department department = new Department(companyCode,
					new DepartmentCode(context.getCommand().get(0).getDepartmentCode()),
					context.getCommand().get(0).getHistoryId(), endDate,
					new DepartmentCode(context.getCommand().get(0).getExternalCode() != null
							? context.getCommand().get(0).getExternalCode() : ""),
					new DepartmentGenericName(context.getCommand().get(0).getFullName()),
					new HierarchyCode(context.getCommand().get(0).getHierarchyCode()),
					new DepartmentName(context.getCommand().get(0).getName()), startDate);
			String historyId = department.getHistoryId();
			departmentRepository.update(department);
			updateMemo(companyCode, historyId, context.getCommand().get(0).getMemo());
		} else if (context.getCommand().size() > 1) {
			// update hierachy of list Department when insert 1 item to tree
			for (int i = 0; i < context.getCommand().size(); i++) {
				if (!departmentRepository.isExistDepartment(companyCode, context.getCommand().get(0).getHistoryId(),
						new DepartmentCode(context.getCommand().get(i).getDepartmentCode()))) {
					throw new BusinessException("ER06");
				}
			}
			for (int i = 0; i < context.getCommand().size(); i++) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
				try {
					startDate1 = formatter.parse(context.getCommand().get(i).getStartDate());
					endDate1 = formatter.parse(context.getCommand().get(i).getEndDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				GeneralDate startDate = GeneralDate.legacyDate(startDate1);
				GeneralDate endDate = GeneralDate.legacyDate(endDate1);
				Department department = new Department(companyCode,
						new DepartmentCode(context.getCommand().get(i).getDepartmentCode()),
						context.getCommand().get(i).getHistoryId(), endDate,
						new DepartmentCode(context.getCommand().get(i).getExternalCode() != null
								? context.getCommand().get(i).getExternalCode() : ""),
						new DepartmentGenericName(context.getCommand().get(i).getFullName()),
						new HierarchyCode(context.getCommand().get(i).getHierarchyCode()),
						new DepartmentName(context.getCommand().get(i).getName()), startDate);

				Department department1 = new Department(companyCode,
						new DepartmentCode(context.getCommand().get(i).getDepartmentCode()),
						context.getCommand().get(i).getHistoryId(), endDate,
						new DepartmentCode(context.getCommand().get(i).getExternalCode() != null
								? context.getCommand().get(i).getExternalCode() : ""),
						new DepartmentGenericName(context.getCommand().get(i).getFullName()),
						new HierarchyCode(context.getCommand().get(i).getHierarchyCode() + i + ""),
						new DepartmentName(context.getCommand().get(i).getName()), startDate);
				listDep.add(department);
				listDep1.add(department1);
			}
			String historyId = context.getCommand().get(0).getHistoryId();
			update(listDep1);
			update(listDep);
			updateMemo(companyCode, historyId, context.getCommand().get(0).getMemo());
		} else if ((context.getCommand().size() == 1)
				&& (context.getCommand().get(0).getMemo().compareTo("addhistoryfromlatest") == 0)) {
			// update endDate when insert 1 history.
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			try {
				endDate1 = formatter.parse(context.getCommand().get(0).getEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			GeneralDate endDate = GeneralDate.legacyDate(endDate1);
			String historyId = context.getCommand().get(0).getHistoryId();

			departmentRepository.updateEnddate(companyCode, historyId, endDate);

		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void update(List<Department> listDep) {
		departmentRepository.updateAll(listDep);
	}

	void updateMemo(String companyCode, String historyId, String memo) {
		DepartmentMemo departmentMemo = new DepartmentMemo(companyCode, historyId, new Memo(memo));
		departmentRepository.updateMemo(departmentMemo);
	}

}
