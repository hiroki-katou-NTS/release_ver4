package nts.uk.ctx.at.shared.app.command.remainingnumber.nursingcareleave;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.ChildCareLeaveRemainingDataService;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.ChildCareLeaveRemaiDataRepo;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.ChildCareLeaveRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.LeaveForCareData;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.LeaveForCareDataRepo;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.ChildCareLeaveRemInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.ChildCareLeaveRemainingInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.LeaveForCareInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.LeaveForCareInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.UpperLimitSetting;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.command.PeregUpdateListCommandHandler;
@Stateless
@Transactional
public class UpdateCareLeaveListCommandHandler extends CommandHandler<List<UpdateCareLeaveCommand>>
implements PeregUpdateListCommandHandler<UpdateCareLeaveCommand>{
	@Inject
	private ChildCareLeaveRemainingDataService  service;
	
	@Inject
	private LeaveForCareInfoRepository careInfoRepo;

	@Inject
	private ChildCareLeaveRemInfoRepository childCareInfoRepo;

	@Inject
	private ChildCareLeaveRemaiDataRepo childCareDataRepo;
	
	@Inject
	private LeaveForCareDataRepo careDataRepo;
	
	@Override
	public String targetCategoryCd() {
		return "CS00036";
	}

	@Override
	public Class<?> commandClass() {
		return UpdateCareLeaveCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<List<UpdateCareLeaveCommand>> context) {
		List<UpdateCareLeaveCommand> cmd = context.getCommand();
		String cid = AppContexts.user().companyId();		
		List<LeaveForCareData> leaveCareDataInsert = new ArrayList<>();
		List<LeaveForCareData> leaveCareDataUpdate = new ArrayList<>();
		List<LeaveForCareInfo> leaveCareInfoInsert = new ArrayList<>();
		List<LeaveForCareInfo> leaveCareInfoUpdate = new ArrayList<>();
		List<ChildCareLeaveRemainingData> childCareDataInsert = new ArrayList<>();
		List<ChildCareLeaveRemainingData> childCareDataUpdate = new ArrayList<>();
		List<ChildCareLeaveRemainingInfo> childCareLeaveInfoInsert = new ArrayList<>();
		List<ChildCareLeaveRemainingInfo> childCareLeaveInfoUpdate = new ArrayList<>();
		
		createData(cmd, childCareDataInsert, childCareDataUpdate, leaveCareDataInsert, leaveCareDataUpdate,
				childCareLeaveInfoInsert, childCareLeaveInfoUpdate, leaveCareInfoInsert, leaveCareInfoUpdate);
		
		service.addData(cid, childCareDataInsert, leaveCareDataInsert, childCareLeaveInfoInsert, leaveCareInfoInsert);
		
		service.updateData(cid, childCareDataUpdate, leaveCareDataUpdate, childCareLeaveInfoUpdate, leaveCareInfoUpdate);

	}
	
	private void createData(List<UpdateCareLeaveCommand> cmd,
			List<ChildCareLeaveRemainingData> childCareDataInsert, List<ChildCareLeaveRemainingData> childCareDataUpdate,
			List<LeaveForCareData> leaveCareDataInsert, List<LeaveForCareData> leaveCareDataUpdate,
			List<ChildCareLeaveRemainingInfo> childCareLeaveInfoInsert, List<ChildCareLeaveRemainingInfo> childCareLeaveInfoUpdate,
			List<LeaveForCareInfo> leaveCareInfoInsert, List<LeaveForCareInfo> leaveCareInfoUpdate) {
		String cid = AppContexts.user().companyId();
		List<String> sids = cmd.parallelStream().map(c -> c.getSId()).collect(Collectors.toList());
		List<ChildCareLeaveRemainingData> checkChildCareDatailsLst = childCareDataRepo.getChildCareByEmpIds(cid, sids);
		List<ChildCareLeaveRemainingInfo> checkChildCareInfoLst = childCareInfoRepo.getChildCareByEmpIdsAndCid(cid, sids);
		List<LeaveForCareInfo> checkCareInfoLst = careInfoRepo.getCareByEmpIdsAndCid(cid, sids);
		List<LeaveForCareData> checkCareDatailsLst = careDataRepo.getCareByEmpIds(cid, sids);

		cmd.parallelStream().forEach(c ->{
			// child-care-data
			ChildCareLeaveRemainingData childCareData = ChildCareLeaveRemainingData.getChildCareHDRemaining(c.getSId(),
					c.getChildCareUsedDays() == null ? 0.0 : c.getChildCareUsedDays().doubleValue());
			Optional<ChildCareLeaveRemainingData> childCareDetailsOpt= checkChildCareDatailsLst.parallelStream().filter(item -> item.getSId().equals(c.getSId())).findFirst();
			if (childCareDetailsOpt.isPresent()) {
				childCareDataUpdate.add(childCareData);
			} else {
				if (c.getChildCareUsedDays() != null)
					childCareDataInsert.add(childCareData);
			}

			// care-data
			LeaveForCareData careData = LeaveForCareData.getCareHDRemaining(c.getSId(),
					c.getCareUsedDays() == null ? 0.0 : c.getCareUsedDays().doubleValue());
			Optional<LeaveForCareData> careDataOpt = checkCareDatailsLst.parallelStream().filter(item -> item.getSId().equals(c.getSId())).findFirst();

			if (careDataOpt.isPresent()) {
				leaveCareDataUpdate.add(careData);
			} else {
				if (c.getCareUsedDays() != null)
					leaveCareDataInsert.add(careData);
			}

			// child-care-info
			ChildCareLeaveRemainingInfo childCareInfo = ChildCareLeaveRemainingInfo.createChildCareLeaveInfo(c.getSId(),
					c.getChildCareUseArt().intValue(),
					c.getChildCareUpLimSet() == null ? UpperLimitSetting.FAMILY_INFO.value
							: c.getChildCareUpLimSet().intValue(),
					c.getChildCareThisFiscal() == null ? null : c.getChildCareThisFiscal().doubleValue(),
					c.getChildCareNextFiscal() == null ? null : c.getChildCareNextFiscal().doubleValue());
			Optional<ChildCareLeaveRemainingInfo> childCareInfoOpt = checkChildCareInfoLst.parallelStream().filter(item -> item.getSId().equals(c.getSId())).findFirst();
			if (childCareInfoOpt.isPresent()) {
				childCareLeaveInfoUpdate.add(childCareInfo);
			} else {
				childCareLeaveInfoInsert.add(childCareInfo);
			}

			// care-info
			LeaveForCareInfo careInfo = LeaveForCareInfo.createCareLeaveInfo(c.getSId(),
					c.getCareUseArt().intValue(),
					c.getCareUpLimSet() == null ? UpperLimitSetting.FAMILY_INFO.value
							: c.getCareUpLimSet().intValue(),
					c.getCareThisFiscal() == null ? null : c.getCareThisFiscal().doubleValue(),
					c.getCareNextFiscal() == null ? null : c.getCareNextFiscal().doubleValue());
			Optional<LeaveForCareInfo> careInfoOpt = checkCareInfoLst.parallelStream().filter(item -> item.getSId().equals(c.getSId())).findFirst();
			if (careInfoOpt.isPresent()) {
				leaveCareInfoUpdate.add(careInfo);
			} else {
				leaveCareInfoInsert.add(careInfo);
			}
			
		});
	}

}
