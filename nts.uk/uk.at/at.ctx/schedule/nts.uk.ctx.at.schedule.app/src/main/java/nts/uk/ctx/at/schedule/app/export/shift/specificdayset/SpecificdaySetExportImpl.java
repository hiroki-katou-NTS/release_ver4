package nts.uk.ctx.at.schedule.app.export.shift.specificdayset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.app.find.pattern.monthly.setting.Period;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.file.report.masterlist.annotation.DomainID;
import nts.uk.shr.infra.file.report.masterlist.data.ColumnTextAlign;
import nts.uk.shr.infra.file.report.masterlist.data.MasterCellData;
import nts.uk.shr.infra.file.report.masterlist.data.MasterCellStyle;
import nts.uk.shr.infra.file.report.masterlist.data.MasterData;
import nts.uk.shr.infra.file.report.masterlist.data.MasterHeaderColumn;
import nts.uk.shr.infra.file.report.masterlist.data.MasterListData;
import nts.uk.shr.infra.file.report.masterlist.data.SheetData;
import nts.uk.shr.infra.file.report.masterlist.webservice.MasterListExportQuery;

/**
 *
 * SpecificDay Setting export implements
 * 
 * @author HiepTH
 *
 */
@Stateless
@DomainID(value = "SpecificdaySet")
public class SpecificdaySetExportImpl implements MasterListData {

	@Inject
	private SpecificdaySetReportRepository specificdaySetReportRepository;
	
	 @Override
	 public List<SheetData> extraSheets(MasterListExportQuery query) {
		 List<SheetData> sheetDatas = new ArrayList<>();
		 //add the work place sheet
		 SheetData sheetWorkplaceData = new SheetData(getMasterDatasForWorkplace(query), getHeaderColumnsForWorkplace(query),null, null, "職場");
		 sheetDatas.add(sheetWorkplaceData);
		 return sheetDatas;
	 }

	@Override
	public List<MasterHeaderColumn> getHeaderColumns(MasterListExportQuery query) {
		List<MasterHeaderColumn> columns = new ArrayList<>();

		columns.add(new MasterHeaderColumn("年月", "年月", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("1日", "1日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("2日", "2日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("3日", "3日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("4日", "4日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("5日", "5日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("6日", "6日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("7日", "7日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("8日", "8日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("9日", "9日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("10日", "10日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("11日", "11日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("12日", "12日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("13日", "13日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("14日", "14日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("15日", "15日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("16日", "16日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("17日", "17日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("18日", "18日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("19日", "19日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("20日", "20日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("21日", "21日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("22日", "22日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("23日", "23日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("24日", "24日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("25日", "25日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("26日", "26日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("27日", "27日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("28日", "28日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("29日", "29日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("30日", "30日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("31日", "31日", ColumnTextAlign.LEFT, "", true));

		return columns;
	}

	@Override
	public List<MasterData> getMasterDatas(MasterListExportQuery query) {
		String companyId = AppContexts.user().companyId();
		List<MasterData> datas = new ArrayList<>();
		
		Period period = specificdaySetReportRepository.getBaseDateByCompany(companyId, query.getStartDate(), query.getEndDate());
		Optional<Map<String, List<SpecificdaySetCompanyReportData>>> mapSetReportDatas = specificdaySetReportRepository
				.findAllSpecificdaySetCompany(companyId, period.getStartDate(), period.getEndDate());

		if (mapSetReportDatas.isPresent()) {
			mapSetReportDatas.get().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(x -> {
				Optional<MasterData> row = newCompanyMasterData(x.getKey(), Optional.ofNullable(x.getValue()));
				if (row.isPresent()) {
					datas.add(row.get());
				}
			});

		}

		return datas;
	}
	
	
	@Override
	public String mainSheetName() {
		return "会社";
	}

	private Optional<MasterData> newCompanyMasterData(String yearMonth,
			Optional<List<SpecificdaySetCompanyReportData>> specificdaySetReportDatas) {
		Map<String, Object> data = new HashMap<>();
		if (specificdaySetReportDatas.isPresent()) {
			data.put("年月", yearMonth.substring(0, 4) + "/" + yearMonth.substring(4, yearMonth.length()));
			putEmptyToColumsCompany(data);
			specificdaySetReportDatas.get().stream().sorted(Comparator.comparing(SpecificdaySetCompanyReportData::getDay))
					.forEachOrdered(x -> {
						putDataToColumns(data, x);
					});

			MasterData rowMaster = new MasterData(data, null, "");
			alignDataCompany(rowMaster.getRowData());
			return Optional.of(rowMaster);
		}
		return Optional.empty();
	}
	
	/**
	 * 
	 * @param rowData
	 */
	private void alignDataCompany(Map<String, MasterCellData> rowData) {
		rowData.get("年月").setStyle(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT));
		for (int i = 1; i <= 31; i++) {
			String key = i + "日";
			rowData.get(key).setStyle(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT));
		}
	}

	private void putEmptyToColumsCompany(Map<String, Object> data) {
		
		for (int i = 1; i <= 31; i++) {
			String key = i + "日";
			data.put(key, "");
		}
	}

	private void putDataToColumns(Map<String, Object> data, SpecificdaySetCompanyReportData setReportData) {
		int day = setReportData.getDay();
		for (int i = 1; i <= 31; i++) {
			String key = i + "日";
			if (day == i) {
				String value = (String) data.get(key);
				if (value != null && !value.isEmpty()) {
					value += "," + setReportData.getSpecificDateItemName().v();
				}
				else if (value != null && value.isEmpty()){
					value += setReportData.getSpecificDateItemName().v();
				}
				data.put(key, value);
			}
		}
	}
	
	/**
	 * get header for WorkPlace Sheet
	 * @param query
	 * @return
	 */
	private List<MasterHeaderColumn> getHeaderColumnsForWorkplace(MasterListExportQuery query) {
		List<MasterHeaderColumn> columns = new ArrayList<>();

		columns.add(new MasterHeaderColumn("コード", "コード", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("名称", "名称", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("年月", "年月", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("1日", "1日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("2日", "2日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("3日", "3日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("4日", "4日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("5日", "5日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("6日", "6日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("7日", "7日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("8日", "8日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("9日", "9日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("10日", "10日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("11日", "11日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("12日", "12日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("13日", "13日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("14日", "14日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("15日", "15日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("16日", "16日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("17日", "17日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("18日", "18日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("19日", "19日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("20日", "20日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("21日", "21日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("22日", "22日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("23日", "23日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("24日", "24日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("25日", "25日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("26日", "26日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("27日", "27日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("28日", "28日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("29日", "29日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("30日", "30日", ColumnTextAlign.LEFT, "", true));
		columns.add(new MasterHeaderColumn("31日", "31日", ColumnTextAlign.LEFT, "", true));

		return columns;
	}
	
	
	
	/**
	 * get data for WorkPlace Sheet
	 * @param query
	 * @return
	 */
	private List<MasterData> getMasterDatasForWorkplace(MasterListExportQuery query) {
		String companyId = AppContexts.user().companyId();
		List<MasterData> datas = new ArrayList<>();
		Period period = specificdaySetReportRepository.getBaseDateByCompany(companyId, query.getStartDate(), query.getEndDate());
		Optional<Map<String, List<SpecificdaySetWorkplaceReportData>>> mapSetReportDatas = specificdaySetReportRepository
				.findAllSpecificdaySetWorkplace(companyId, period.getStartDate(), period.getEndDate());

		if (mapSetReportDatas.isPresent()) {
			mapSetReportDatas.get().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(x -> {
				Optional<List<SpecificdaySetWorkplaceReportData>> listDataPerOneWp = Optional.ofNullable(x.getValue());
				if (listDataPerOneWp.isPresent()) {
					Map<String, List<SpecificdaySetWorkplaceReportData>> mapDataByYearMonth = 
					listDataPerOneWp.get().stream().collect(Collectors.groupingBy(SpecificdaySetWorkplaceReportData::getYearMonth));
					List<String> yearMonthKeys = mapDataByYearMonth.keySet().stream().sorted().collect(Collectors.toList());
					for(int i = 0; i < yearMonthKeys.size(); i++) {
						String yearMonth = yearMonthKeys.get(i);
						List<SpecificdaySetWorkplaceReportData> listDataPerOneRow = mapDataByYearMonth.get(yearMonth);
						Optional<MasterData> row = newWorkplaceMasterData(i, yearMonth, Optional.ofNullable(listDataPerOneRow));
						if (row.isPresent()) {
							datas.add(row.get());
						}
					}
				}
			});

		}

		return datas;
	}
	
	/**
	 * create row data for WorkPlace sheet
	 * @param yearMonth
	 * @param specificdaySetReportDatas
	 * @return
	 */
	private Optional<MasterData> newWorkplaceMasterData(Integer index, String yearMonth,
			Optional<List<SpecificdaySetWorkplaceReportData>> specificdaySetReportDatas) {
		Map<String, Object> data = new HashMap<>();
		if (specificdaySetReportDatas.isPresent()) {
			//put empty to columns
			putEmptyToColumWorkplace(data);
			if (index == 0) {
				SpecificdaySetWorkplaceReportData setWorkplaceReportData = specificdaySetReportDatas.get().get(0);
				data.put("コード", setWorkplaceReportData.getWorkplaceCode());
				data.put("名称", setWorkplaceReportData.getWorkplaceName());
			}
			data.put("年月", yearMonth.substring(0, 4) + "/" + yearMonth.substring(4, yearMonth.length()));
			
			specificdaySetReportDatas.get().stream().sorted(Comparator.comparing(SpecificdaySetWorkplaceReportData::getDay))
					.forEachOrdered(x -> {
						putDataToColumnsWorkplace(data, x);
					});

			MasterData masterData = new MasterData(data, null, "");
			alignDataWorkspace(masterData.getRowData());
			return Optional.of(masterData);
		}
		return Optional.empty();
	}
	
	/**
	 * 
	 * @param rowData
	 */
	private void alignDataWorkspace(Map<String, MasterCellData> rowData) {
		rowData.get("コード").setStyle(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT));
		rowData.get("名称").setStyle(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT));
		rowData.get("年月").setStyle(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT));
		for (int i = 1; i <= 31; i++) {
			String key = i + "日";
			rowData.get(key).setStyle(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT));
		}
	}
	
	/**
	 * 
	 * @param data
	 */
	private void putEmptyToColumWorkplace(Map<String, Object> data) {
		data.put("コード", "");
		data.put("名称", "");
		for (int i = 1; i <= 31; i++) {
			String key = i + "日";
			data.put(key, "");
		}
	}

	/**
	 * 
	 * @param data
	 * @param setReportData
	 */
	private void putDataToColumnsWorkplace(Map<String, Object> data, SpecificdaySetWorkplaceReportData setReportData) {
		int day = setReportData.getDay();
		for (int i = 1; i <= 31; i++) {
			String key = i + "日";
			if (day == i) {
				String value = (String) data.get(key);
				if (value != null && !value.isEmpty()) {
					value += "," + setReportData.getSpecificDateItemName().v();
				}
				else if (value != null && value.isEmpty()){
					value += setReportData.getSpecificDateItemName().v();
				}
				data.put(key, value);
			}

		}
	}
}
