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
import nts.gul.text.IdentifierUtil;
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
public class AddWorkPlaceCommandHandler extends CommandHandler<List<AddWorkPlaceCommand>> {

	@Inject
	private WorkPlaceRepository workPlaceRepository;

	@Override
	protected void handle(CommandHandlerContext<List<AddWorkPlaceCommand>> context) {
		Date startDate1 = new Date();
		Date endDate1 = new Date();
		String companyCode = AppContexts.user().companyCode();
		List<WorkPlace> listDep = new ArrayList<WorkPlace>();
		String newhistoryId = IdentifierUtil.randomUniqueId();
		if (context.getCommand().size() == 1) {
			// truong hop add 1 workplace
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			try {
				startDate1 = formatter.parse(context.getCommand().get(0).getStartDate());
				endDate1 = formatter.parse(context.getCommand().get(0).getEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			GeneralDate startDate = GeneralDate.legacyDate(startDate1);
			GeneralDate endDate = GeneralDate.legacyDate(endDate1);

			WorkPlace workPlace = null;
			// truong hop add 1 WorkPLace khi them moi 1 lich su rong.
			if (context.getCommand().get(0).getHistoryId() == null) {
				workPlace = new WorkPlace(companyCode,
						new WorkPlaceCode(context.getCommand().get(0).getWorkPlaceCode()), endDate,
						new WorkPlaceCode(context.getCommand().get(0).getExternalCode() != null
								? context.getCommand().get(0).getExternalCode() : ""),
						new WorkPlaceGenericName(context.getCommand().get(0).getFullName() != null
								? context.getCommand().get(0).getFullName() : ""),
						new HierarchyCode(context.getCommand().get(0).getHierarchyCode()),
						new WorkPlaceName(context.getCommand().get(0).getName() != null
								? context.getCommand().get(0).getName() : ""),
						new ParentChildAttribute(
								new BigDecimal(context.getCommand().get(0).getParentChildAttribute1())),
						new ParentChildAttribute(
								new BigDecimal(context.getCommand().get(0).getParentChildAttribute2())),
						new WorkPlaceCode(context.getCommand().get(0).getParentWorkCode1()),
						new WorkPlaceCode(context.getCommand().get(0).getParentWorkCode2()), startDate);

				String historyId = workPlace.getHistoryId();
				addMemo(companyCode, historyId, context.getCommand().get(0).getMemo());
			} else {
				// truong hop  add 1 workplace khi click nut them moi 1 dep.
				if (workPlaceRepository.isDuplicateWorkPlaceCode(companyCode,
						new WorkPlaceCode(context.getCommand().get(0).getWorkPlaceCode()),
						context.getCommand().get(0).getHistoryId())) {
					throw new BusinessException("ER005");
				}

				workPlace = new WorkPlace(companyCode,
						new WorkPlaceCode(context.getCommand().get(0).getWorkPlaceCode()),
						context.getCommand().get(0).getHistoryId(), endDate,
						new WorkPlaceCode(context.getCommand().get(0).getExternalCode() != null
								? context.getCommand().get(0).getExternalCode() : ""),
						new WorkPlaceGenericName(context.getCommand().get(0).getFullName() != null
								? context.getCommand().get(0).getFullName() : ""),
						new HierarchyCode(context.getCommand().get(0).getHierarchyCode()),
						new WorkPlaceName(context.getCommand().get(0).getName() != null
								? context.getCommand().get(0).getName() : ""),
						new ParentChildAttribute(
								new BigDecimal(context.getCommand().get(0).getParentChildAttribute1())),
						new ParentChildAttribute(
								new BigDecimal(context.getCommand().get(0).getParentChildAttribute2())),
						new WorkPlaceCode(context.getCommand().get(0).getParentWorkCode1()),
						new WorkPlaceCode(context.getCommand().get(0).getParentWorkCode2()), startDate);
				updateMemo(companyCode, context.getCommand().get(0).getHistoryId(),
						context.getCommand().get(0).getMemo());
			}
			workPlaceRepository.add(workPlace);
		} else {
			// truong hop add 1 list workplace khi them moi 1 lich su tu lich su cu.
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

				WorkPlace workPlace = new WorkPlace(companyCode,
						new WorkPlaceCode(context.getCommand().get(i).getWorkPlaceCode()),
						newhistoryId, endDate,
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

				listDep.add(workPlace);
			}
			addListWkp(listDep);
			String memo = context.getCommand().get(0).getMemo();
			addMemo(companyCode, newhistoryId, memo);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	void addListWkp(List<WorkPlace> listDep) {
		workPlaceRepository.addAll(listDep);
	}

	void addMemo(String companyCode, String historyId, String memo) {
		workPlaceRepository.registerMemo(companyCode, historyId, new Memo(memo));
	}

	void updateMemo(String companyCode, String historyId, String memo) {
		workPlaceRepository.updateMemo(new WorkPlaceMemo(companyCode, historyId, new Memo(memo)));
	}

}
