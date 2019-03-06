package nts.uk.file.at.app.export.regisagreetime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.standardtime.AgreementUnitSetting;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementUnitSettingRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.masterlist.annotation.DomainID;
import nts.uk.shr.infra.file.report.masterlist.data.ColumnTextAlign;
import nts.uk.shr.infra.file.report.masterlist.data.MasterData;
import nts.uk.shr.infra.file.report.masterlist.data.MasterHeaderColumn;
import nts.uk.shr.infra.file.report.masterlist.data.MasterListData;
import nts.uk.shr.infra.file.report.masterlist.data.SheetData;
import nts.uk.shr.infra.file.report.masterlist.webservice.MasterListExportQuery;
import nts.uk.shr.infra.file.report.masterlist.webservice.MasterListMode;

@Stateless
@DomainID("RegisterTime")
public class RegisteTimeImpl implements MasterListData {
	
	@Inject
	private RegistTimeRepository registTimeRepository;
	
	@Inject
	private AgreementUnitSettingRepository agreementUnitSettingRepository;
	
	/**
	 *  Sheet1
	 */
	@Override
	public List<MasterHeaderColumn> getHeaderColumns(MasterListExportQuery query) {
		 List <MasterHeaderColumn> columns = new ArrayList<>();

	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_80, TextResource.localize("KMK008_80"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.HEADER_NONE1,"",
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.HEADER_NONE2, "",
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_81, TextResource.localize("KMK008_81"),
	                ColumnTextAlign.LEFT, "", true));
	        return columns;
	}
	
	
	@Override
	public List<MasterData> getMasterDatas(MasterListExportQuery query) {
		return registTimeRepository.getDataExportSheet1();
	}
	
	@Override
	public String mainSheetName() {
		return TextResource.localize("KMK008_70");
	}

	@Override
	public MasterListMode mainSheetMode(){
		return MasterListMode.NONE;
	}
	
	
	/**
	 *  Sheet 2
	 * @return
	 */
	public List<MasterHeaderColumn> getHeaderColumnsSheet2() {
		 List <MasterHeaderColumn> columns = new ArrayList<>();

	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_89, TextResource.localize("KMK008_89"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_90,TextResource.localize("KMK008_90"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_91, TextResource.localize("KMK008_91"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_92, TextResource.localize("KMK008_92"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_125, TextResource.localize("KMK008_125"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_126, TextResource.localize("KMK008_126"),
	                ColumnTextAlign.LEFT, "", true));
	        return columns;
	}
	
	public List<MasterData> getMasterDatasSheet2() {
		return registTimeRepository.getDataExportSheet2();
	}
	
	
	
	/**
	 *  sheet 3
	 * @return
	 */
	public List<MasterHeaderColumn> getHeaderColumnsSheet3() {
		 List <MasterHeaderColumn> columns = new ArrayList<>();

	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_100, TextResource.localize("KMK008_100"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_101,TextResource.localize("KMK008_101"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_89, TextResource.localize("KMK008_89"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_90, TextResource.localize("KMK008_90"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_91, TextResource.localize("KMK008_91"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_92, TextResource.localize("KMK008_92"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_125, TextResource.localize("KMK008_125"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_126, TextResource.localize("KMK008_126"),
	                ColumnTextAlign.LEFT, "", true));
	        return columns;
	}
	
	
	public List<MasterData> getMasterDatasSheet3() {
		return registTimeRepository.getDataExportSheet3();
	}
	
	/**
	 *  sheet4
	 * @return
	 */
	public List<MasterHeaderColumn> getHeaderColumnsSheet4() {
		 List <MasterHeaderColumn> columns = new ArrayList<>();
		 columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_102, TextResource.localize("KMK008_102"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_103,TextResource.localize("KMK008_103"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_89, TextResource.localize("KMK008_89"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_90, TextResource.localize("KMK008_90"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_91, TextResource.localize("KMK008_91"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_92, TextResource.localize("KMK008_92"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_125, TextResource.localize("KMK008_125"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_126, TextResource.localize("KMK008_126"),
	                ColumnTextAlign.LEFT, "", true));
	        return columns;
	}
	
	public List<MasterData> getMasterDatasSheet4() {
		return registTimeRepository.getDataExportSheet4();
	}
	
	/**
	 *  sheet 5
	 * @return
	 */
	public List<MasterHeaderColumn> getHeaderColumnsSheet5() {
		 List <MasterHeaderColumn> columns = new ArrayList<>();

	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_104, TextResource.localize("KMK008_104"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_105,TextResource.localize("KMK008_105"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_89, TextResource.localize("KMK008_89"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_90, TextResource.localize("KMK008_90"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_91, TextResource.localize("KMK008_91"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_92, TextResource.localize("KMK008_92"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_125, TextResource.localize("KMK008_125"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_126, TextResource.localize("KMK008_126"),
	                ColumnTextAlign.LEFT, "", true));
	        return columns;
	}
	
	public List<MasterData> getMasterDatasSheet5() {
		return registTimeRepository.getDataExportSheet5();
	}
	
	/**
	 *  Sheet 6
	 * @return
	 */
	public List<MasterHeaderColumn> getHeaderColumnsSheet6() {
		 List <MasterHeaderColumn> columns = new ArrayList<>();

	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_89, TextResource.localize("KMK008_89"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_90,TextResource.localize("KMK008_90"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_91, TextResource.localize("KMK008_91"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_92, TextResource.localize("KMK008_92"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_125, TextResource.localize("KMK008_125"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_126, TextResource.localize("KMK008_126"),
	                ColumnTextAlign.LEFT, "", true));
	        return columns;
	}
	
	public List<MasterData> getMasterDatasSheet6() {
		return registTimeRepository.getDataExportSheet6();
	}
	
	/**
	 *  sheet 7
	 * @return
	 */
	public List<MasterHeaderColumn> getHeaderColumnsSheet7() {
		 List <MasterHeaderColumn> columns = new ArrayList<>();

	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_100, TextResource.localize("KMK008_100"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_101,TextResource.localize("KMK008_101"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_89, TextResource.localize("KMK008_89"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_90, TextResource.localize("KMK008_90"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_91, TextResource.localize("KMK008_91"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_92, TextResource.localize("KMK008_92"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_125, TextResource.localize("KMK008_125"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_126, TextResource.localize("KMK008_126"),
	                ColumnTextAlign.LEFT, "", true));
	        return columns;
	}
	
	
	public List<MasterData> getMasterDatasSheet7() {
		return registTimeRepository.getDataExportSheet7();
	}
	
	/**
	 *  sheet 8
	 * @return
	 */
	public List<MasterHeaderColumn> getHeaderColumnsSheet8() {
		 List <MasterHeaderColumn> columns = new ArrayList<>();
		 columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_102, TextResource.localize("KMK008_102"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_103,TextResource.localize("KMK008_103"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_89, TextResource.localize("KMK008_89"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_90, TextResource.localize("KMK008_90"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_91, TextResource.localize("KMK008_91"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_92, TextResource.localize("KMK008_92"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_125, TextResource.localize("KMK008_125"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_126, TextResource.localize("KMK008_126"),
	                ColumnTextAlign.LEFT, "", true));
	        return columns;
	}
	
	public List<MasterData> getMasterDatasSheet8() {
		return registTimeRepository.getDataExportSheet8();
	}
	
	
	/**
	 *  sheet 9
	 * @return
	 */
	public List<MasterHeaderColumn> getHeaderColumnsSheet9() {
		 List <MasterHeaderColumn> columns = new ArrayList<>();

	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_104, TextResource.localize("KMK008_104"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_105,TextResource.localize("KMK008_105"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_89, TextResource.localize("KMK008_89"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_90, TextResource.localize("KMK008_90"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_91, TextResource.localize("KMK008_91"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_92, TextResource.localize("KMK008_92"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_125, TextResource.localize("KMK008_125"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_126, TextResource.localize("KMK008_126"),
	                ColumnTextAlign.LEFT, "", true));
	        return columns;
	}
	
	public List<MasterData> getMasterDatasSheet9() {
		return registTimeRepository.getDataExportSheet9();
	}
	
	/**
	 * sheet 10
	 */
	public List<MasterHeaderColumn> getHeaderColumnsSheet10() {
		 List <MasterHeaderColumn> columns = new ArrayList<>();

	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_106, TextResource.localize("KMK008_106"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_107,TextResource.localize("KMK008_107"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_109, TextResource.localize("KMK008_109"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_110, TextResource.localize("KMK008_110"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_111, TextResource.localize("KMK008_111"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_112, TextResource.localize("KMK008_112"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_113, TextResource.localize("KMK008_113"),
	                ColumnTextAlign.LEFT, "", true));
	        columns.add(new MasterHeaderColumn(RegistTimeColumn.KMK008_114, TextResource.localize("KMK008_114"),
	                ColumnTextAlign.LEFT, "", true));
	        return columns;
	}
	
	public List<MasterData> getMasterDatasSheet10(GeneralDate startDate, GeneralDate endDate) {
		return registTimeRepository.getDataExportSheet10(startDate,endDate);
	}
	
	
	@Override
    public List<SheetData> extraSheets(MasterListExportQuery query) {
        List<SheetData> sheetDatas = new ArrayList<>();
        GeneralDate startDate = query.getStartDate();
        GeneralDate endDate = query.getEndDate();
        /**
         *  sheet 2
         */
        SheetData sheetData2 = SheetData.builder()
        		 .mainData(this.getMasterDatasSheet2())
                .mainDataColumns(this.getHeaderColumnsSheet2())
                .sheetName(TextResource.localize("KMK008_71"))
                .mode(MasterListMode.NONE)
                .build();
        sheetDatas.add(sheetData2);
        
        Optional<AgreementUnitSetting> agreementUnitSetting = agreementUnitSettingRepository.find(AppContexts.user().companyId());
        
        
        /**
         *  sheet 3
         */
		if (!agreementUnitSetting.isPresent()
				|| (agreementUnitSetting.isPresent() && agreementUnitSetting.get().getEmploymentUseAtr().value == 1)) {
        	 SheetData sheetData3 = SheetData.builder()
               		 .mainData(this.getMasterDatasSheet3())
                       .mainDataColumns(this.getHeaderColumnsSheet3())
                       .sheetName(TextResource.localize("KMK008_72"))
                       .mode(MasterListMode.NONE)
                       .build();
        	 sheetDatas.add(sheetData3);
        }
        
        /**
         *  sheet 4
         */
        if (!agreementUnitSetting.isPresent()
				|| (agreementUnitSetting.isPresent() && agreementUnitSetting.get().getWorkPlaceUseAtr().value == 1)) {
        	
			SheetData sheetData4 = SheetData.builder().mainData(this.getMasterDatasSheet4())
					.mainDataColumns(this.getHeaderColumnsSheet4()).sheetName(TextResource.localize("KMK008_73"))
	                .mode(MasterListMode.NONE)
					.build();
			sheetDatas.add(sheetData4);
		}
        
        
        /**
         *  sheet 5
         */
		if (!agreementUnitSetting.isPresent()
				|| (agreementUnitSetting.isPresent() && agreementUnitSetting.get().getClassificationUseAtr().value == 1)) {
			SheetData sheetData5 = SheetData.builder().mainData(this.getMasterDatasSheet5())
					.mainDataColumns(this.getHeaderColumnsSheet5()).sheetName(TextResource.localize("KMK008_74"))
	                .mode(MasterListMode.NONE)
					.build();
			sheetDatas.add(sheetData5);
		}
        
        /**
         *  sheet 6
         */
        SheetData sheetData6 = SheetData.builder()
          		 .mainData(this.getMasterDatasSheet6())
                  .mainDataColumns(this.getHeaderColumnsSheet6())
                  .sheetName(TextResource.localize("KMK008_75"))
                  .mode(MasterListMode.NONE)
                  .build();
        sheetDatas.add(sheetData6);
        /**
         *  sheet 7
         */
		if (!agreementUnitSetting.isPresent()
				|| (agreementUnitSetting.isPresent() && agreementUnitSetting.get().getEmploymentUseAtr().value == 1)) {

			SheetData sheetData7 = SheetData.builder().mainData(this.getMasterDatasSheet7())
					.mainDataColumns(this.getHeaderColumnsSheet7()).sheetName(TextResource.localize("KMK008_76"))
	                .mode(MasterListMode.NONE)
					.build();
			sheetDatas.add(sheetData7);
		}
        
		/**
         *  sheet 8
         */
        if (!agreementUnitSetting.isPresent()
				|| (agreementUnitSetting.isPresent() && agreementUnitSetting.get().getWorkPlaceUseAtr().value == 1)) {	
			SheetData sheetData8 = SheetData.builder().mainData(this.getMasterDatasSheet8())
					.mainDataColumns(this.getHeaderColumnsSheet8()).sheetName(TextResource.localize("KMK008_77"))
	                .mode(MasterListMode.NONE)
					.build();
			sheetDatas.add(sheetData8);
		}
        
        /**
         *  sheet 9
         */
		if (!agreementUnitSetting.isPresent()
				|| (agreementUnitSetting.isPresent() && agreementUnitSetting.get().getClassificationUseAtr().value == 1)) {
			SheetData sheetData9 = SheetData.builder()
	          		 .mainData(this.getMasterDatasSheet9())
	                  .mainDataColumns(this.getHeaderColumnsSheet9())
	                  .sheetName(TextResource.localize("KMK008_78"))
	                  .mode(MasterListMode.NONE)
	                  .build();
	        sheetDatas.add(sheetData9);
		}
		
		SheetData sheetData10 = SheetData.builder()
         		 .mainData(this.getMasterDatasSheet10(startDate, endDate))
                 .mainDataColumns(this.getHeaderColumnsSheet10())
                 .sheetName(TextResource.localize("KMK008_79"))
                 .mode(MasterListMode.FISCAL_YEAR_RANGE)
                 .build();
       sheetDatas.add(sheetData10);
		
        return sheetDatas;
    }
	
}
