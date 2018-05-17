package nts.uk.ctx.at.function.infra.repository.monthlycorrection.fixedformatmonthly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonPfmCorrectionFormat;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonPfmCorrectionFormatRepository;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KfnmtDisplayTimeItemPfm;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KfnmtMonthlyActualResultPrm;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KfnmtMonthlyActualResultPrmPK;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KrcmtMonPfmCorrectionFormat;

@Stateless
public class JpaMonPfmCorrectionFormatRepo extends JpaRepository implements MonPfmCorrectionFormatRepository {

	private static final String GET_ALL_MON_PFM = "SELECT c FROM KrcmtMonPfmCorrectionFormat c"
			+ " WHERE c.krcmtMonPfmCorrectionFormatPK.companyID = :companyID";
	
	private static final String GET_MON_PRM_BY_CODE = GET_ALL_MON_PFM
			+ " AND c.krcmtMonPfmCorrectionFormatPK.monthlyPfmFormatCode = :monthlyPfmFormatCode";
	
	private static final String GET_MON_PRM_BY_CODE_LIST = GET_ALL_MON_PFM
			+ " AND c.krcmtMonPfmCorrectionFormatPK.monthlyPfmFormatCode IN :monthlyPfmFormatCodes";
	
	
	@Override
	public List<MonPfmCorrectionFormat> getAllMonPfm(String companyID) {
		List<MonPfmCorrectionFormat> data =this.queryProxy().query(GET_ALL_MON_PFM,KrcmtMonPfmCorrectionFormat.class)
				.setParameter("companyID", companyID)
				.getList(c->c.toDomain());
		return data;
	}

	@Override
	public Optional<MonPfmCorrectionFormat> getMonPfmCorrectionFormat(String companyID, String monthlyPfmFormatCode) {
		Optional<MonPfmCorrectionFormat> data =this.queryProxy().query(GET_MON_PRM_BY_CODE,KrcmtMonPfmCorrectionFormat.class)
				.setParameter("companyID", companyID)
				.setParameter("monthlyPfmFormatCode", monthlyPfmFormatCode)
				.getSingle(c->c.toDomain());
		return data;
	}
	@Override
	public List<MonPfmCorrectionFormat> getMonPfmCorrectionFormat(String companyID, List<String> monthlyPfmFormatCodes) {
		List<MonPfmCorrectionFormat> data =this.queryProxy().query(GET_MON_PRM_BY_CODE_LIST,KrcmtMonPfmCorrectionFormat.class)
				.setParameter("companyID", companyID)
				.setParameter("monthlyPfmFormatCodes", monthlyPfmFormatCodes)
				.getList(c->c.toDomain());
		return data;
	}

	@Override
	public void addMonPfmCorrectionFormat(MonPfmCorrectionFormat monPfmCorrectionFormat) {
		KrcmtMonPfmCorrectionFormat newEntity = KrcmtMonPfmCorrectionFormat.toEntity(monPfmCorrectionFormat);
		this.commandProxy().insert(newEntity);
		
	}

	@Override
	public void updateMonPfmCorrectionFormat(MonPfmCorrectionFormat monPfmCorrectionFormat) {
		KrcmtMonPfmCorrectionFormat newEntity = KrcmtMonPfmCorrectionFormat.toEntity(monPfmCorrectionFormat);

		KrcmtMonPfmCorrectionFormat updateEntity = this.queryProxy().query(GET_MON_PRM_BY_CODE, KrcmtMonPfmCorrectionFormat.class).setParameter("companyID", monPfmCorrectionFormat.getCompanyID())
				.setParameter("monthlyPfmFormatCode", monPfmCorrectionFormat.getMonthlyPfmFormatCode().v()).getSingle().get();

		List<KfnmtMonthlyActualResultPrm> toInsertM = new ArrayList<>();
		List<KfnmtMonthlyActualResultPrm> toUpdateC = new ArrayList<>();
		newEntity.listKrcmtMonthlyActualResultPfm.stream().forEach(nE -> {
			boolean isExist = updateEntity.listKrcmtMonthlyActualResultPfm.stream().filter(oE -> {
				return oE.kfnmtMonthlyActualResultPrmPK.monthlyPfmFormatCode.equals(nE.kfnmtMonthlyActualResultPrmPK.monthlyPfmFormatCode) && oE.kfnmtMonthlyActualResultPrmPK.companyID.equals(nE.kfnmtMonthlyActualResultPrmPK.companyID)
						&& oE.kfnmtMonthlyActualResultPrmPK.sheetNo == nE.kfnmtMonthlyActualResultPrmPK.sheetNo;
			}).findFirst().isPresent();
			if (isExist) {
				toUpdateC.add(nE);
			} else {
				toInsertM.add(nE);
			}
		});

		if (!toInsertM.isEmpty()) {
			commandProxy().insertAll(toInsertM);
			updateEntity.sheetName = newEntity.sheetName;
			this.commandProxy().update(updateEntity);
		}

		if (!toUpdateC.isEmpty()) {
			if (toUpdateC.get(0).listKrcmtDisplayTimeItemPfm.size() == 0) {
				this.commandProxy().remove(KfnmtMonthlyActualResultPrm.class,new KfnmtMonthlyActualResultPrmPK(
						toUpdateC.get(0).kfnmtMonthlyActualResultPrmPK.companyID,
						toUpdateC.get(0).kfnmtMonthlyActualResultPrmPK.monthlyPfmFormatCode,
						toUpdateC.get(0).kfnmtMonthlyActualResultPrmPK.sheetNo
						));
			} else {
				toUpdateC.stream().forEach(nE -> {
					nE.sheetName = newEntity.listKrcmtMonthlyActualResultPfm.stream().filter(oE -> {
						return oE.kfnmtMonthlyActualResultPrmPK.monthlyPfmFormatCode.equals(nE.kfnmtMonthlyActualResultPrmPK.monthlyPfmFormatCode)
								&& oE.kfnmtMonthlyActualResultPrmPK.companyID.equals(nE.kfnmtMonthlyActualResultPrmPK.companyID) && oE.kfnmtMonthlyActualResultPrmPK.sheetNo == nE.kfnmtMonthlyActualResultPrmPK.sheetNo;
					}).findFirst().get().sheetName;
				});
				List<KfnmtDisplayTimeItemPfm> newItem = toUpdateC.stream().map(c -> c.listKrcmtDisplayTimeItemPfm).flatMap(List::stream).collect(Collectors.toList());
				List<KfnmtDisplayTimeItemPfm> updateItem = updateEntity.listKrcmtMonthlyActualResultPfm.stream().filter(c -> {
					return toUpdateC.stream().filter(n -> {
						return n.kfnmtMonthlyActualResultPrmPK.monthlyPfmFormatCode.equals(c.kfnmtMonthlyActualResultPrmPK.monthlyPfmFormatCode) && n.kfnmtMonthlyActualResultPrmPK.companyID.equals(c.kfnmtMonthlyActualResultPrmPK.companyID)
								&& n.kfnmtMonthlyActualResultPrmPK.sheetNo == c.kfnmtMonthlyActualResultPrmPK.sheetNo;
					}).findFirst().isPresent();
				}).map(c -> c.listKrcmtDisplayTimeItemPfm).flatMap(List::stream).collect(Collectors.toList());
				List<KfnmtDisplayTimeItemPfm> toRemove = new ArrayList<>();
				List<KfnmtDisplayTimeItemPfm> toUpdate = new ArrayList<>();
				List<KfnmtDisplayTimeItemPfm> toAdd = new ArrayList<>();

				// check add and update
				for (KfnmtDisplayTimeItemPfm nItem : newItem) {
					boolean checkItem = false;
					for (KfnmtDisplayTimeItemPfm uItem : updateItem) {
						if (nItem.kfnmtDisplayTimeItemPfmPK.monthlyPfmFormatCode.equals(uItem.kfnmtDisplayTimeItemPfmPK.monthlyPfmFormatCode) && nItem.kfnmtDisplayTimeItemPfmPK.companyID.equals(uItem.kfnmtDisplayTimeItemPfmPK.companyID)
								&& nItem.kfnmtDisplayTimeItemPfmPK.sheetNo == uItem.kfnmtDisplayTimeItemPfmPK.sheetNo && nItem.kfnmtDisplayTimeItemPfmPK.itemDisplay == uItem.kfnmtDisplayTimeItemPfmPK.itemDisplay) {
							checkItem = true;
							toUpdate.add(nItem);
						}
					}
					if (!checkItem) {
						toAdd.add(nItem);
					}

				}
				// check remove
				for (KfnmtDisplayTimeItemPfm uItem : updateItem) {
					boolean checkItem = false;
					for (KfnmtDisplayTimeItemPfm nItem : newItem) {
						if (nItem.kfnmtDisplayTimeItemPfmPK.monthlyPfmFormatCode.equals(uItem.kfnmtDisplayTimeItemPfmPK.monthlyPfmFormatCode) && nItem.kfnmtDisplayTimeItemPfmPK.companyID.equals(uItem.kfnmtDisplayTimeItemPfmPK.companyID)
								&& nItem.kfnmtDisplayTimeItemPfmPK.sheetNo == uItem.kfnmtDisplayTimeItemPfmPK.sheetNo && nItem.kfnmtDisplayTimeItemPfmPK.itemDisplay == uItem.kfnmtDisplayTimeItemPfmPK.itemDisplay) {
							checkItem = true;
							break;
						}
					}
					if (!checkItem) {
						toRemove.add(uItem);
					}

				}

				this.commandProxy().updateAll(toUpdateC);
				this.commandProxy().insertAll(toAdd);
				this.commandProxy().updateAll(toUpdate);
				this.commandProxy().removeAll(toRemove);
			}

		}
		
	}

	@Override
	public void deleteMonPfmCorrectionFormat(String companyID, String monthlyPfmFormatCode) {
		KrcmtMonPfmCorrectionFormat newEntity = this.queryProxy().query(GET_MON_PRM_BY_CODE, KrcmtMonPfmCorrectionFormat.class).setParameter("companyID", companyID).setParameter("monthlyPfmFormatCode", monthlyPfmFormatCode).getSingle()
				.get();
		this.commandProxy().remove(newEntity);
		
	}

}
