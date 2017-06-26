package nts.uk.ctx.basic.app.command.organization.department;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
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
public class AddDepartmentCommandHandler extends CommandHandler<List<AddDepartmentCommand>> {

	@Inject
	private DepartmentRepository departmentRepository;

	@Override
	protected void handle(CommandHandlerContext<List<AddDepartmentCommand>> context) {
		Date startDate1 = new Date();
		Date endDate1 = new Date();
		List<Department> listDep = new ArrayList<Department>();
		String companyCode = AppContexts.user().companyCode();
		String newhistoryId = IdentifierUtil.randomUniqueId();
		if (context.getCommand().size() == 1) {
			// truong hop add 1 department.
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			try {
				startDate1 = formatter.parse(context.getCommand().get(0).getStartDate());
				endDate1 = formatter.parse(context.getCommand().get(0).getEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			GeneralDate startDate = GeneralDate.legacyDate(startDate1);
			GeneralDate endDate = GeneralDate.legacyDate(endDate1);

			Department department = null;
			// truong hop add 1 Dep khi them moi 1 lich su rong.
			if (context.getCommand().get(0).getHistoryId() == null) {
				department = new Department(companyCode,
						new DepartmentCode(context.getCommand().get(0).getDepartmentCode()), endDate,
						new DepartmentCode(context.getCommand().get(0).getExternalCode() != null
								? context.getCommand().get(0).getExternalCode() : ""),
						new DepartmentGenericName(context.getCommand().get(0).getFullName()),
						new HierarchyCode(context.getCommand().get(0).getHierarchyCode()),
						new DepartmentName(context.getCommand().get(0).getName()), startDate);
				String historyId = department.getHistoryId();
				addMemo(companyCode, historyId, context.getCommand().get(0).getMemo());
			} else {
				// truong hop  add 1 dep khi click nut them moi 1 dep.
				if (departmentRepository.isDuplicateDepartmentCode(companyCode,
						context.getCommand().get(0).getHistoryId(),
						new DepartmentCode(context.getCommand().get(0).getDepartmentCode()))) {
					throw new BusinessException("ER005");
				}

				department = new Department(companyCode,
						new DepartmentCode(context.getCommand().get(0).getDepartmentCode()),
						context.getCommand().get(0).getHistoryId(), endDate,
						new DepartmentCode(context.getCommand().get(0).getExternalCode() != null
								? context.getCommand().get(0).getExternalCode() : ""),
						new DepartmentGenericName(context.getCommand().get(0).getFullName()),
						new HierarchyCode(context.getCommand().get(0).getHierarchyCode()),
						new DepartmentName(context.getCommand().get(0).getName()), startDate);
				updateMemo(companyCode, context.getCommand().get(0).getHistoryId(),
						context.getCommand().get(0).getMemo());
			}
			departmentRepository.add(department);

		} else {
			// truong hop add 1 list dep khi them moi 1 lich su tu lich su cu.
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
						new DepartmentCode(context.getCommand().get(i).getDepartmentCode()), newhistoryId, endDate,
						new DepartmentCode(context.getCommand().get(i).getExternalCode() != null
								? context.getCommand().get(i).getExternalCode() : ""),
						new DepartmentGenericName(context.getCommand().get(i).getFullName()),
						new HierarchyCode(context.getCommand().get(i).getHierarchyCode()),
						new DepartmentName(context.getCommand().get(i).getName()), startDate);

				listDep.add(department);
			}
			addListDep(listDep);
			String memo = context.getCommand().get(0).getMemo();
			addMemo(companyCode, newhistoryId, memo);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	void addListDep(List<Department> listDep) {
		departmentRepository.addeAll(listDep);
	}

	void addMemo(String companyCode, String historyId, String memo) {
		departmentRepository.registerMemo(companyCode, historyId, new Memo(memo));
	}

	void updateMemo(String companyCode, String historyId, String memo) {
		departmentRepository.updateMemo(new DepartmentMemo(companyCode, historyId, new Memo(memo)));
	}

}
