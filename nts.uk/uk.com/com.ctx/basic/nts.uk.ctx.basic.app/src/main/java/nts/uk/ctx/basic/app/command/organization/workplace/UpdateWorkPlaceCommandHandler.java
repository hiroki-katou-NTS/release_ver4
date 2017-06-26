package nts.uk.ctx.basic.app.command.organization.workplace;

import java.math.BigDecimal;
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
import nts.uk.ctx.basic.dom.organization.shr.HierarchyCode;
import nts.uk.ctx.basic.dom.organization.workplace.ParentChildAttribute;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlace;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceCode;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceGenericName;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceMemo;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceName;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class UpdateWorkPlaceCommandHandler extends CommandHandler<List<UpdateWorkPlaceCommand>> {

	@Inject
	private WorkPlaceRepository workPlaceRepository;

	@Override
	protected void handle(CommandHandlerContext<List<UpdateWorkPlaceCommand>> context) {
		String companyCode = AppContexts.user().companyCode();
		List<WorkPlace> listDep = new ArrayList<WorkPlace>();
		List<WorkPlace> listDep1 = new ArrayList<WorkPlace>();
		Date startDate1 = new Date();
		Date endDate1 = new Date();
		//  update workplace when click register button
		if ((context.getCommand().size() == 1)
				&& (context.getCommand().get(0).getMemo().compareTo("addhistoryfromlatest") != 0)) {

			if (!workPlaceRepository.isExistWorkPace(companyCode, context.getCommand().get(0).getHistoryId(),
					new WorkPlaceCode(context.getCommand().get(0).getWorkPlaceCode()))) {
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

			WorkPlace workPlace = new WorkPlace(companyCode,
					new WorkPlaceCode(context.getCommand().get(0).getWorkPlaceCode()),
					context.getCommand().get(0).getHistoryId(), endDate,
					new WorkPlaceCode(context.getCommand().get(0).getExternalCode()),
					new WorkPlaceGenericName(context.getCommand().get(0).getFullName()),
					new HierarchyCode(context.getCommand().get(0).getHierarchyCode()),
					new WorkPlaceName(context.getCommand().get(0).getName()),
					new ParentChildAttribute(new BigDecimal(context.getCommand().get(0).getParentChildAttribute1())),
					new ParentChildAttribute(new BigDecimal(context.getCommand().get(0).getParentChildAttribute2())),
					new WorkPlaceCode(context.getCommand().get(0).getParentWorkCode1()),
					new WorkPlaceCode(context.getCommand().get(0).getParentWorkCode2()), startDate);

			String historyId = workPlace.getHistoryId();
			workPlaceRepository.update(workPlace);
			updateMemo(companyCode, historyId, context.getCommand().get(0).getMemo());
		} else if (context.getCommand().size() > 1) {
			// update hierachy of list workplace when insert 1 item to tree
			for (int i = 0; i < context.getCommand().size(); i++) {
				if (!workPlaceRepository.isExistWorkPace(companyCode, context.getCommand().get(0).getHistoryId(),
						new WorkPlaceCode(context.getCommand().get(i).getWorkPlaceCode()))) {
					throw new BusinessException("ER005");
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

				WorkPlace workPlace1 = new WorkPlace(companyCode,
						new WorkPlaceCode(context.getCommand().get(i).getWorkPlaceCode()),
						context.getCommand().get(i).getHistoryId(), endDate,
						new WorkPlaceCode(context.getCommand().get(i).getExternalCode() != null
								? context.getCommand().get(i).getExternalCode() : ""),
						new WorkPlaceGenericName(context.getCommand().get(i).getFullName() != null
								? context.getCommand().get(i).getFullName() : ""),
						new HierarchyCode(context.getCommand().get(i).getHierarchyCode()),
						new WorkPlaceName(context.getCommand().get(i).getName() != null
								? context.getCommand().get(i).getName() : ""),
						new ParentChildAttribute(
								new BigDecimal(context.getCommand().get(i).getParentChildAttribute1())),
						new ParentChildAttribute(
								new BigDecimal(context.getCommand().get(i).getParentChildAttribute2())),
						new WorkPlaceCode(context.getCommand().get(i).getParentWorkCode1()),
						new WorkPlaceCode(context.getCommand().get(i).getParentWorkCode2()), startDate);

				WorkPlace workPlace2 = new WorkPlace(companyCode,
						new WorkPlaceCode(context.getCommand().get(i).getWorkPlaceCode()),
						context.getCommand().get(i).getHistoryId(), endDate,
						new WorkPlaceCode(context.getCommand().get(i).getExternalCode() != null
								? context.getCommand().get(i).getExternalCode() : ""),
						new WorkPlaceGenericName(context.getCommand().get(i).getFullName() != null
								? context.getCommand().get(i).getFullName() : ""),
						new HierarchyCode("099" + i + ""),
						new WorkPlaceName(context.getCommand().get(i).getName() != null
								? context.getCommand().get(i).getName() : ""),
						new ParentChildAttribute(
								new BigDecimal(context.getCommand().get(i).getParentChildAttribute1())),
						new ParentChildAttribute(
								new BigDecimal(context.getCommand().get(i).getParentChildAttribute2())),
						new WorkPlaceCode(context.getCommand().get(i).getParentWorkCode1()),
						new WorkPlaceCode(context.getCommand().get(i).getParentWorkCode2()), startDate);
				listDep.add(workPlace1);
				listDep1.add(workPlace2);
			}
			String historyId = context.getCommand().get(0).getHistoryId();
			update(listDep1);
			update(listDep);
			updateMemo(companyCode, historyId, context.getCommand().get(0).getMemo());
		} else if ((context.getCommand().size() == 1)
				&& (context.getCommand().get(0).getMemo().compareTo("addhistoryfromlatest") == 0)) {
			//  update endDate when insert 1 history.
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			try {
				endDate1 = formatter.parse(context.getCommand().get(0).getEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			GeneralDate endDate = GeneralDate.legacyDate(endDate1);
			String historyId = context.getCommand().get(0).getHistoryId();

			workPlaceRepository.updateEnddate(companyCode, historyId, endDate);

		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void update(List<WorkPlace> listDep) {
		workPlaceRepository.updateAll(listDep);
	}

	void updateMemo(String companyCode, String historyId, String memo) {
		WorkPlaceMemo departmentMemo = new WorkPlaceMemo(companyCode, historyId, new Memo(memo));
		workPlaceRepository.updateMemo(departmentMemo);
	}
}
