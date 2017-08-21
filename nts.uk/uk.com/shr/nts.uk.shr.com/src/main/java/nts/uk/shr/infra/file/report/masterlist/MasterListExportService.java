package nts.uk.shr.infra.file.report.masterlist;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import nts.arc.i18n.custom.ISessionLocale;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDateTime;
import nts.uk.shr.com.company.CompanyAdapter;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.infra.file.report.masterlist.annotation.NamedAnnotation;
import nts.uk.shr.infra.file.report.masterlist.data.MasterData;
import nts.uk.shr.infra.file.report.masterlist.data.MasterHeaderColumn;
import nts.uk.shr.infra.file.report.masterlist.data.MasterListData;
import nts.uk.shr.infra.file.report.masterlist.generator.MasterListExportSource;
import nts.uk.shr.infra.file.report.masterlist.generator.MasterListReportGenerator;
import nts.uk.shr.infra.file.report.masterlist.webservice.MasterListExportQuery;

@Stateless
public class MasterListExportService extends ExportService<MasterListExportQuery>{
	
	@Inject
	private MasterListReportGenerator generator;
	
	@Inject
	private ISessionLocale currentLanguage;
	
//	@Inject
//	private CompanyAdapter company;

	@Override
	protected void handle(ExportServiceContext<MasterListExportQuery> context) {
		
		MasterListExportQuery query = context.getQuery();
		
		try {
			MasterListData domainData = CDI.current().select(MasterListData.class, new NamedAnnotation(query.getDomainId())).get();
			
			List<MasterHeaderColumn> columns = domainData.getHeaderColumns();
			List<MasterData> datas = domainData.getMasterDatas();
			Map<String, String> headers = this.getHeaderInfor(query);
			
			this.generator.generate(context.getGeneratorContext(), new MasterListExportSource(headers, columns, datas));
		} catch (UnsatisfiedResolutionException ex) {
			throw new RuntimeException(ex);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	private Map<String, String> getHeaderInfor(MasterListExportQuery query) {
		Map<String, String> headers = new LinkedHashMap<>(); 
		
		LoginUserContext context = AppContexts.user();
		String companyCode = context.companyCode();
		String companyname = "";//company.getCompanyByCode(companyCode)
//				.orElseThrow(() -> new RuntimeException("Company is not found!!!!")).getCompanyName();
		
		String language = currentLanguage.getSessionLocale().getDisplayLanguage(); 
		
		String createReportDate = GeneralDateTime.now().localDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss"));
		
		headers.put("【会社】", companyCode + " " + companyname);
		headers.put("【種類】", query.getDomainType());
		headers.put("【日時】", createReportDate);
		headers.put("【選択言語】 ", language);
		
		return headers;
	}

}
