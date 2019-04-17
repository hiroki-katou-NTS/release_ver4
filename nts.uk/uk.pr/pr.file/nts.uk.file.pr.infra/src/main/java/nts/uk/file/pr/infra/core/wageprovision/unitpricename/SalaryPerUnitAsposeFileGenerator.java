package nts.uk.file.pr.infra.core.wageprovision.unitpricename;

import com.aspose.cells.*;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.ctx.pr.file.app.core.wageprovision.unitpricename.SalaryPerUnitFileGenerator;
import nts.uk.ctx.pr.file.app.core.wageprovision.unitpricename.SalaryPerUnitSetExportData;
import nts.uk.shr.com.company.CompanyAdapter;
import nts.uk.shr.com.company.CompanyInfor;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Stateless
public class SalaryPerUnitAsposeFileGenerator extends AsposeCellsReportGenerator
        implements SalaryPerUnitFileGenerator {

    private static final String TEMPLATE_FILE = "report/QMM013.xlsx";

    private static final String REPORT_FILE_NAME = "QMM013単価名の登録.pdf";

    private static final int COLUMN_START = 1;
    @Inject
    private CompanyAdapter company;

    @Override
    public void generate(FileGeneratorContext fileContext, List<SalaryPerUnitSetExportData> exportData) {

        try (AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE)) {

            int rowStart = 3;
            Workbook wb = reportContext.getWorkbook();
            WorksheetCollection wsc = wb.getWorksheets();
            Worksheet ws = wsc.get(0);
            ws.setName(TextResource.localize("QMM013_40"));
            //set headler
            // Company name
            String companyName = this.company.getCurrentCompany().map(CompanyInfor::getCompanyName).orElse("");
            PageSetup pageSetup = ws.getPageSetup();
            pageSetup.setHeader(0, "&8&\"MS ゴシック\"" + companyName);

            // Output item name
            pageSetup.setHeader(1, "&16&\"MS ゴシック\"" + "単価名の登録");

            // Set header date
            DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.JAPAN);
            pageSetup.setHeader(2, "&8&\"MS ゴシック\" " + LocalDateTime.now().format(fullDateTimeFormatter) + "\npage &P ");


            //break page
            HorizontalPageBreakCollection pageBreaks = ws.getHorizontalPageBreaks();

//           boolean isFirst = (page == 1 ) && page%39 == 0 ? true : false;
            Cells cells = ws.getCells();
            // fill data           
            int page = exportData.size() / 37;
            if(exportData.size() % 37 != 0){
                page++;
            }
            for (int i = 0; i < exportData.size(); i++) {
                if (i >= 1 && i <= page-1 && page != 1) {
                    wsc.get(wsc.addCopy(0)).setName("sheetName" + i);
                }
                if (i % 37 == 0 && i != 0) {
                    Worksheet sheet = wsc.get("sheetName" + i / 37);
                    cells = sheet.getCells();
                    rowStart = 3;
                }

                SalaryPerUnitSetExportData e = exportData.get(i);
                cells.get(rowStart, COLUMN_START).setValue(e.getCode());
                cells.get(rowStart, COLUMN_START + 1).setValue(e.getName());
                cells.get(rowStart, COLUMN_START + 2).setValue(e.getAbolition().value ==  1 ? "○" : "");
                cells.get(rowStart, COLUMN_START + 3).setValue(e.getShortName());
                cells.get(rowStart, COLUMN_START + 4).setValue(e.getEveryoneEqualSet().isPresent() ? e.getEveryoneEqualSet().get().getTargetClassification().nameId : "-");
                cells.get(rowStart, COLUMN_START + 5).setValue(e.getPerSalaryContractType().isPresent() ? e.getPerSalaryContractType().get().getMonthlySalary().nameId : "-");
                cells.get(rowStart, COLUMN_START + 6).setValue(e.getPerSalaryContractType().isPresent() ? e.getPerSalaryContractType().get().getMonthlySalaryPerday().nameId : "-");
                cells.get(rowStart, COLUMN_START + 7).setValue(e.getPerSalaryContractType().isPresent() ? e.getPerSalaryContractType().get().getDayPayee().nameId : "-");
                cells.get(rowStart, COLUMN_START + 8).setValue(e.getPerSalaryContractType().isPresent() ? e.getPerSalaryContractType().get().getHourlyPay().nameId : "-");
                cells.get(rowStart, COLUMN_START + 9).setValue(e.getUnitPriceType().nameId);
                rowStart++;
            }
            reportContext.processDesigner();
            reportContext.saveAsPdf(this.createNewFile(fileContext, this.getReportName(REPORT_FILE_NAME)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
