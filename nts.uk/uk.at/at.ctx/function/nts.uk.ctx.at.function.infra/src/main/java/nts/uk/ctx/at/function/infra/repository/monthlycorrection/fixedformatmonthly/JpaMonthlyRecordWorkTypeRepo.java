package nts.uk.ctx.at.function.infra.repository.monthlycorrection.fixedformatmonthly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonthlyRecordWorkType;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonthlyRecordWorkTypeRepository;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KrcmtDisplayTimeItemRC;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KrcmtMonthlyActualResultRC;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KrcmtMonthlyRecordWorkType;

@Stateless
public class JpaMonthlyRecordWorkTypeRepo extends JpaRepository implements MonthlyRecordWorkTypeRepository {

	private static final String GET_ALL_MON = "SELECT a FROM KrcmtMonthlyRecordWorkType a " + " WHERE a.krcmtMonthlyRecordWorkTypePK.companyID = :companyID ";

	private static final String GET_MON_BY_CODE = GET_ALL_MON + " AND a.krcmtMonthlyRecordWorkTypePK.businessTypeCode = :businessTypeCode";

	@Override
	public List<MonthlyRecordWorkType> getAllMonthlyRecordWorkType(String companyID) {
		List<MonthlyRecordWorkType> data = this.queryProxy().query(GET_ALL_MON, KrcmtMonthlyRecordWorkType.class).setParameter("companyID", companyID).getList(c -> c.toDomain());
		return data;
	}

	@Override
	public Optional<MonthlyRecordWorkType> getMonthlyRecordWorkTypeByCode(String companyID, String businessTypeCode) {
		Optional<MonthlyRecordWorkType> data = this.queryProxy().query(GET_MON_BY_CODE, KrcmtMonthlyRecordWorkType.class).setParameter("companyID", companyID).setParameter("businessTypeCode", businessTypeCode).getSingle(c -> c.toDomain());
		return data;
	}

	@Override
	public void addMonthlyRecordWorkType(MonthlyRecordWorkType monthlyRecordWorkType) {
		KrcmtMonthlyRecordWorkType newEntity = KrcmtMonthlyRecordWorkType.toEntity(monthlyRecordWorkType);
		this.commandProxy().insert(newEntity);
	}

	@Override
	public void updateMonthlyRecordWorkType(MonthlyRecordWorkType monthlyRecordWorkType) {
		KrcmtMonthlyRecordWorkType newEntity = KrcmtMonthlyRecordWorkType.toEntity(monthlyRecordWorkType);

		KrcmtMonthlyRecordWorkType updateEntity = this.queryProxy().query(GET_MON_BY_CODE, KrcmtMonthlyRecordWorkType.class).setParameter("companyID", monthlyRecordWorkType.getCompanyID())
				.setParameter("businessTypeCode", monthlyRecordWorkType.getBusinessTypeCode().v()).getSingle().get();

		
		List<KrcmtMonthlyActualResultRC> toInsertM = new ArrayList<>();
		List<KrcmtMonthlyActualResultRC> toUpdateC = new ArrayList<>();
		newEntity.listKrcmtMonthlyActualResultRC.stream().forEach(nE-> {
			boolean isExist = updateEntity.listKrcmtMonthlyActualResultRC.stream().filter(oE -> {
					return  oE.krcmtMonthlyActualResultRCPK.businessTypeCode.equals(nE.krcmtMonthlyActualResultRCPK.businessTypeCode)
						&& oE.krcmtMonthlyActualResultRCPK.companyID.equals(nE.krcmtMonthlyActualResultRCPK.companyID)
						&& oE.krcmtMonthlyActualResultRCPK.sheetNo == nE.krcmtMonthlyActualResultRCPK.sheetNo;
			}).findFirst().isPresent();
			if(isExist) {
				toUpdateC.add(nE);
			} else {
				toInsertM.add(nE);
			}
		});
		
		if(!toInsertM.isEmpty()) {
			commandProxy().insertAll(toInsertM);
			this.commandProxy().update(updateEntity);
		}
		
		if(!toUpdateC.isEmpty()) {
			toUpdateC.stream().forEach(nE -> {
				nE.sheetName = newEntity.listKrcmtMonthlyActualResultRC.stream().filter(oE -> {
						return  oE.krcmtMonthlyActualResultRCPK.businessTypeCode.equals(nE.krcmtMonthlyActualResultRCPK.businessTypeCode)
							&& oE.krcmtMonthlyActualResultRCPK.companyID.equals(nE.krcmtMonthlyActualResultRCPK.companyID)
							&& oE.krcmtMonthlyActualResultRCPK.sheetNo == nE.krcmtMonthlyActualResultRCPK.sheetNo;
				}).findFirst().get().sheetName;
			});
			
			//update list Item
			List<KrcmtDisplayTimeItemRC> newItem = toUpdateC.stream().map(c -> c.listKrcmtDisplayTimeItemRC).flatMap(List::stream).collect(Collectors.toList());
			List<KrcmtDisplayTimeItemRC> updateItem = updateEntity.listKrcmtMonthlyActualResultRC.stream().filter(c -> {
				return toUpdateC.stream().filter(n -> {
					return n.krcmtMonthlyActualResultRCPK.businessTypeCode.equals(c.krcmtMonthlyActualResultRCPK.businessTypeCode)
							&& n.krcmtMonthlyActualResultRCPK.companyID.equals(c.krcmtMonthlyActualResultRCPK.companyID)
							&& n.krcmtMonthlyActualResultRCPK.sheetNo == c.krcmtMonthlyActualResultRCPK.sheetNo;
				}).findFirst().isPresent();
			}).map(c -> c.listKrcmtDisplayTimeItemRC).flatMap(List::stream).collect(Collectors.toList());

			List<KrcmtDisplayTimeItemRC> toRemove = new ArrayList<>();
			List<KrcmtDisplayTimeItemRC> toUpdate = new ArrayList<>();
			List<KrcmtDisplayTimeItemRC> toAdd = new ArrayList<>();
			
			//check add and update
			for(KrcmtDisplayTimeItemRC nItem : newItem) {
				boolean checkItem = false;
				for(KrcmtDisplayTimeItemRC uItem : updateItem) {
					if(nItem.krcmtDisplayTimeItemRCPK.businessTypeCode.equals(uItem.krcmtDisplayTimeItemRCPK.businessTypeCode)
							&& nItem.krcmtDisplayTimeItemRCPK.companyID.equals(uItem.krcmtDisplayTimeItemRCPK.companyID)
							&& nItem.krcmtDisplayTimeItemRCPK.sheetNo == uItem.krcmtDisplayTimeItemRCPK.sheetNo
							&& nItem.krcmtDisplayTimeItemRCPK.itemDisplay == uItem.krcmtDisplayTimeItemRCPK.itemDisplay) {
						checkItem = true;
						toUpdate.add(nItem);	
					}
				}
				if(!checkItem) {
					toAdd.add(nItem);
				}
					
			}
			//check remove
			for(KrcmtDisplayTimeItemRC uItem : updateItem ) {
				boolean checkItem = false;
				for(KrcmtDisplayTimeItemRC nItem : newItem) {
					if(nItem.krcmtDisplayTimeItemRCPK.businessTypeCode.equals(uItem.krcmtDisplayTimeItemRCPK.businessTypeCode)
							&& nItem.krcmtDisplayTimeItemRCPK.companyID.equals(uItem.krcmtDisplayTimeItemRCPK.companyID)
							&& nItem.krcmtDisplayTimeItemRCPK.sheetNo == uItem.krcmtDisplayTimeItemRCPK.sheetNo
							&& nItem.krcmtDisplayTimeItemRCPK.itemDisplay == uItem.krcmtDisplayTimeItemRCPK.itemDisplay) {
						checkItem = true;
						break;
					}
				}
				if(!checkItem) {
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
