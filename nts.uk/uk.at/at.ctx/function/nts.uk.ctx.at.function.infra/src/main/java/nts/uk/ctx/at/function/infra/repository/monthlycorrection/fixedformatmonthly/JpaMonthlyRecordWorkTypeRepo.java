package nts.uk.ctx.at.function.infra.repository.monthlycorrection.fixedformatmonthly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.primitivevalue.DailyPerformanceFormatName;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.DisplayTimeItem;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonthlyActualResults;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonthlyRecordWorkType;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonthlyRecordWorkTypeRepository;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.SheetCorrectedMonthly;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KrcmtDisplayTimeItemRC;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KrcmtMonthlyActualResultRC;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KrcmtMonthlyActualResultRCPK;
import nts.uk.ctx.at.function.infra.entity.monthlycorrection.fixedformatmonthly.KrcmtMonthlyRecordWorkType;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.primitivevalue.BusinessTypeCode;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class JpaMonthlyRecordWorkTypeRepo extends JpaRepository implements MonthlyRecordWorkTypeRepository {

	private static final String GET_ALL_MON = "SELECT a FROM KrcmtMonthlyRecordWorkType a " + " WHERE a.krcmtMonthlyRecordWorkTypePK.companyID = :companyID ";

	private static final String GET_MON_BY_CODE = GET_ALL_MON + " AND a.krcmtMonthlyRecordWorkTypePK.businessTypeCode = :businessTypeCode";
	private static final String GET_MON_BY_LIST_CODE = GET_ALL_MON + " AND a.krcmtMonthlyRecordWorkTypePK.businessTypeCode IN :businessTypeCode";

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
	public List<MonthlyRecordWorkType> getMonthlyRecordWorkTypeByListCode(String companyID,
			List<String> businessTypeCodes) {
		// get KRCMP_BUS_MON_FORM
		String sqlBusMonth = "SELECT CID, BUSINESS_TYPE_CODE FROM KRCMT_BUS_MON_FORM" 
				+ " WHERE CID = @companyId"
				+ " AND BUSINESS_TYPE_CODE in @codes";

		List<MonthlyRecordWorkType> busMonths = new NtsStatement(sqlBusMonth, this.jdbcProxy())
				.paramString("companyId", companyID).paramString("codes", businessTypeCodes).getList(m -> {
					return new MonthlyRecordWorkType(m.getString("CID"),
							new BusinessTypeCode(m.getString("BUSINESS_TYPE_CODE")), null);
				});
		// get KRCMT_BUS_MON_FORM_SHEET
		String sqlBusSheet = "SELECT CID, BUSINESS_TYPE_CODE, SHEET_NO, SHEET_NAME FROM KRCMT_BUS_MON_FORM_SHEET "
				+ " WHERE CID = @companyId" 
				+ " AND BUSINESS_TYPE_CODE in @codes";

		List<SheetCorrectedMonthlyTemp> busSheets = new NtsStatement(sqlBusSheet, this.jdbcProxy())
				.paramString("companyId", companyID).paramString("codes", businessTypeCodes).getList(s -> {
					SheetCorrectedMonthly dom = new SheetCorrectedMonthly(s.getInt("SHEET_NO"),
							new DailyPerformanceFormatName(s.getString("SHEET_NAME")), null);
					return new SheetCorrectedMonthlyTemp(s.getString("CID"), s.getString("BUSINESS_TYPE_CODE"), dom);
				});

		// get KRCMT_BUS_MON_FORM_SHEET
		String sqlBusItem = "SELECT CID, BUSINESS_TYPE_CODE, SHEET_NO, DISPLAY_ORDER, ATTENDANCE_ITEM_ID, COLUMN_WIDTH FROM KRCMT_BUS_MON_FORM_ITEM "
				+ " WHERE CID = @companyId" 
				+ " AND BUSINESS_TYPE_CODE in @codes" 
				+ " AND SHEET_NO in @sheets";
		
		List<Integer> sheetsNos = busSheets.stream().map(s -> s.getItem().getSheetNo()).collect(Collectors.toList());
		List<DisplayTimeItemTemp> busItems = new NtsStatement(sqlBusItem, this.jdbcProxy())
				.paramString("companyId", companyID).paramString("codes", businessTypeCodes)
				.paramInt("sheets", sheetsNos)
				.getList(i -> {
					DisplayTimeItem dom = new DisplayTimeItem(i.getInt("DISPLAY_ORDER"), i.getInt("ATTENDANCE_ITEM_ID"),
							i.getInt("COLUMN_WIDTH"));
					return new DisplayTimeItemTemp(i.getString("CID"), i.getString("BUSINESS_TYPE_CODE"),
							i.getInt("SHEET_NO"), dom);
				});

		busSheets = busSheets.stream().map(s -> {
			SheetCorrectedMonthly dom = s.getItem();
			dom.setListDisplayTimeItem(
					busItems.stream()
							.filter(i -> i.getCid().equals(s.getCid()) && i.getBusCode().equals(s.getBusCode())
									&& i.getSheetNo() == dom.getSheetNo())
							.map(i -> i.getItem()).collect(Collectors.toList()));
			s.setItem(dom);
			return s;
		}).collect(Collectors.toList());

		for (MonthlyRecordWorkType busMonth : busMonths) {
			busMonth.setDisplayItem(new MonthlyActualResults(busSheets.stream()
					.filter(s -> s.getCid().equals(busMonth.getCompanyID())
							&& s.getBusCode().equals(busMonth.getBusinessTypeCode().v()))
					.map(s -> s.getItem()).collect(Collectors.toList())));
		}

		return busMonths;
	}

	@Override
	public void addMonthlyRecordWorkType(MonthlyRecordWorkType monthlyRecordWorkType) {
		KrcmtMonthlyRecordWorkType newEntity = KrcmtMonthlyRecordWorkType.toEntity(monthlyRecordWorkType);
		this.commandProxy().insert(newEntity);
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void updateMonthlyRecordWorkType(MonthlyRecordWorkType monthlyRecordWorkType) {
		KrcmtMonthlyRecordWorkType newEntity = KrcmtMonthlyRecordWorkType.toEntity(monthlyRecordWorkType);

		KrcmtMonthlyRecordWorkType updateEntity = this.queryProxy().query(GET_MON_BY_CODE, KrcmtMonthlyRecordWorkType.class).setParameter("companyID", monthlyRecordWorkType.getCompanyID())
				.setParameter("businessTypeCode", monthlyRecordWorkType.getBusinessTypeCode().v()).getSingle().get();

		List<KrcmtMonthlyActualResultRC> toInsertM = new ArrayList<>();
		List<KrcmtMonthlyActualResultRC> toUpdateC = new ArrayList<>();
		newEntity.listKrcmtMonthlyActualResultRC.stream().forEach(nE -> {
			boolean isExist = updateEntity.listKrcmtMonthlyActualResultRC.stream().filter(oE -> {
				return oE.krcmtMonthlyActualResultRCPK.businessTypeCode.equals(nE.krcmtMonthlyActualResultRCPK.businessTypeCode) && oE.krcmtMonthlyActualResultRCPK.companyID.equals(nE.krcmtMonthlyActualResultRCPK.companyID)
						&& oE.krcmtMonthlyActualResultRCPK.sheetNo == nE.krcmtMonthlyActualResultRCPK.sheetNo;
			}).findFirst().isPresent();
			if (isExist) {
				toUpdateC.add(nE);
			} else {
				toInsertM.add(nE);
			}
		});

		if (!toInsertM.isEmpty()) {
			commandProxy().insertAll(toInsertM);
			this.commandProxy().update(updateEntity);
		}

		if (!toUpdateC.isEmpty()) {
			if (toUpdateC.get(0).listKrcmtDisplayTimeItemRC.size() == 0) {
				this.commandProxy().remove(KrcmtMonthlyActualResultRC.class,new KrcmtMonthlyActualResultRCPK(
						toUpdateC.get(0).krcmtMonthlyActualResultRCPK.companyID,
						toUpdateC.get(0).krcmtMonthlyActualResultRCPK.businessTypeCode,
						toUpdateC.get(0).krcmtMonthlyActualResultRCPK.sheetNo
						));
			} else {
				toUpdateC.stream().forEach(nE -> {
					nE.sheetName = newEntity.listKrcmtMonthlyActualResultRC.stream().filter(oE -> {
						return oE.krcmtMonthlyActualResultRCPK.businessTypeCode.equals(nE.krcmtMonthlyActualResultRCPK.businessTypeCode) && oE.krcmtMonthlyActualResultRCPK.companyID.equals(nE.krcmtMonthlyActualResultRCPK.companyID)
								&& oE.krcmtMonthlyActualResultRCPK.sheetNo == nE.krcmtMonthlyActualResultRCPK.sheetNo;
					}).findFirst().get().sheetName;
				});

				// update list Item
				List<KrcmtDisplayTimeItemRC> newItem = toUpdateC.stream().map(c -> c.listKrcmtDisplayTimeItemRC).flatMap(List::stream).collect(Collectors.toList());
				List<KrcmtDisplayTimeItemRC> updateItem = updateEntity.listKrcmtMonthlyActualResultRC.stream().filter(c -> {
					return toUpdateC.stream().filter(n -> {
						return n.krcmtMonthlyActualResultRCPK.businessTypeCode.equals(c.krcmtMonthlyActualResultRCPK.businessTypeCode) && n.krcmtMonthlyActualResultRCPK.companyID.equals(c.krcmtMonthlyActualResultRCPK.companyID)
								&& n.krcmtMonthlyActualResultRCPK.sheetNo == c.krcmtMonthlyActualResultRCPK.sheetNo;
					}).findFirst().isPresent();
				}).map(c -> c.listKrcmtDisplayTimeItemRC).flatMap(List::stream).collect(Collectors.toList());

				List<KrcmtDisplayTimeItemRC> toRemove = new ArrayList<>();
				List<KrcmtDisplayTimeItemRC> toUpdate = new ArrayList<>();
				List<KrcmtDisplayTimeItemRC> toAdd = new ArrayList<>();

				// check add and update
				for (KrcmtDisplayTimeItemRC nItem : newItem) {
					boolean checkItem = false;
					for (KrcmtDisplayTimeItemRC uItem : updateItem) {
						if (nItem.krcmtDisplayTimeItemRCPK.businessTypeCode.equals(uItem.krcmtDisplayTimeItemRCPK.businessTypeCode) && nItem.krcmtDisplayTimeItemRCPK.companyID.equals(uItem.krcmtDisplayTimeItemRCPK.companyID)
								&& nItem.krcmtDisplayTimeItemRCPK.sheetNo == uItem.krcmtDisplayTimeItemRCPK.sheetNo && nItem.krcmtDisplayTimeItemRCPK.itemDisplay == uItem.krcmtDisplayTimeItemRCPK.itemDisplay) {
							checkItem = true;
							toUpdate.add(nItem);
						}
					}
					if (!checkItem) {
						toAdd.add(nItem);
					}

				}
				// check remove
				for (KrcmtDisplayTimeItemRC uItem : updateItem) {
					boolean checkItem = false;
					for (KrcmtDisplayTimeItemRC nItem : newItem) {
						if (nItem.krcmtDisplayTimeItemRCPK.businessTypeCode.equals(uItem.krcmtDisplayTimeItemRCPK.businessTypeCode) && nItem.krcmtDisplayTimeItemRCPK.companyID.equals(uItem.krcmtDisplayTimeItemRCPK.companyID)
								&& nItem.krcmtDisplayTimeItemRCPK.sheetNo == uItem.krcmtDisplayTimeItemRCPK.sheetNo && nItem.krcmtDisplayTimeItemRCPK.itemDisplay == uItem.krcmtDisplayTimeItemRCPK.itemDisplay) {
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
	
	@AllArgsConstructor
	@Getter
	class DisplayTimeItemTemp {
		private String cid;
		private String busCode;
		private int sheetNo;
		private DisplayTimeItem item;
	}

	@AllArgsConstructor
	@Data
	class SheetCorrectedMonthlyTemp {
		private String cid;
		private String busCode;
		private SheetCorrectedMonthly item;
	}

}
