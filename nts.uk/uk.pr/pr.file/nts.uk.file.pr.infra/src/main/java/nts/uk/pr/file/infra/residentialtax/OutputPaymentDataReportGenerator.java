package nts.uk.pr.file.infra.residentialtax;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.ejb.Stateless;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.gul.text.StringUtil;
import nts.uk.file.pr.app.export.residentialtax.OutputPaymentDataGenerator;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentTaxReport;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentTaxTextData;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * Outputting Payment Data (納付データの出力)
 * 
 * @author sonnh
 *
 */
@Stateless
public class OutputPaymentDataReportGenerator extends AsposeCellsReportGenerator implements OutputPaymentDataGenerator {

	@Override
	public void generate(FileGeneratorContext fileContext, ResidentTaxReport reportData) {

		try {
			PrintWriter out;
			out = new PrintWriter(this.createNewFile(fileContext, "住民税納付データ.txt"));
			out.print("1");
			out.print(createspace(reportData.getCommon().getTypeCode(),2));
			out.print("0");
			out.print(createspace(reportData.getCommon().getClientCode(),10));
			out.print(createspace(reportData.getCommon().getDesBranchNumber(),3));
			out.print(createspace(reportData.getCommon().getPaymentDueDate(),6));
			out.print(createspace(reportData.getCommon().getPaymentMonth(),4));
			out.print(createspace(reportData.getCommon().getClientName(),40));
			out.print(createspace(reportData.getCommon().getClientAddress(),50));
			out.println("");
			for (ResidentTaxTextData data : reportData.getData()) {
				out.print("2");
				out.print(createspace(data.getMunicipalCode(),6));
				out.print(createspace(data.getCityName(),15));
				out.print(createspace(data.getDesignatedNumber(),15));
				out.print("0");
				out.print(createspace(data.getDesignatedNumber(),5));
				out.print(createspace(format(data.getSalaryAmount()),9));
				out.print(createspace(data.getNumberRetirees(),5));
				out.print(createspace(format(data.getRetirementAmount()),9));
				out.print(createspace(data.getTotalNumberSalaReti(),5));
				out.print(createspace(format(data.getTotalSalaryRetiAmount()),9));
				out.print(createspace(data.getPersonnel(),3));
				out.print(createspace(format(data.getPayment()),10));
				out.print(createspace(format(data.getCityTownTax()),9));
				out.print(createspace(format(data.getPrefecturalTax()),9));
				out.println("");
			}
			out.print("8");
			out.print(createspace(reportData.getCommon().getTotalNumSalaryMi(),7));
			out.print(createspace(format(reportData.getCommon().getTotalSalaryAmount()),11));
			out.print(createspace(reportData.getCommon().getTotalNumberRetirees(),7));
			out.print(createspace(format(reportData.getCommon().getTotalRetirementAmount()),11));
			out.print(createspace(reportData.getCommon().getTotalNumberSala(),7));
			out.print(createspace(format(reportData.getCommon().getTotalSalaRetiAmount()),11));
			out.println("");
			out.print("9");
			out.close();

		} catch (Exception e) {
			//
		}

	}

	private String format(Double input) {
		DecimalFormat formatter = new DecimalFormat("#0");
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		formatter.setDecimalFormatSymbols(symbols);
		
		return formatter.format(input);
	}

	private String createspace(String text, int size) {
		StringBuffer result = new StringBuffer();
		int count = 0;
		if (StringUtil.isNullOrEmpty(text, true)) {
			count = size;
		} else {
			result.append(text);
			if(text.length() < size ){
				count = size - text.length();
			}
		}	
		
		for (int i = 0; i < count; i++) {
			result.append(" ");
		}
		
		return result.toString();
	}
}
