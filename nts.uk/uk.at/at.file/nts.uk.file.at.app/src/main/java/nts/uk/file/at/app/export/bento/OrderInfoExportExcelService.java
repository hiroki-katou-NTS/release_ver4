package nts.uk.file.at.app.export.bento;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.app.command.reservation.bento.BentoMakeOrderCommandHandler;
import nts.uk.ctx.at.record.app.find.reservation.bento.dto.OrderInfoDto;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.ReservationClosingTimeFrame;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class OrderInfoExportExcelService extends ExportService<CreateOrderInfoDataSource> {

    @Inject
    private CreateOrderInfoGenerator generator;

    @Inject
    private CreateOrderInfoFileQuery createOrderInfoFileQuery;

    @Inject
    private BentoMakeOrderCommandHandler commandHandler;

    //private boolean isWorkLocationExport = false;

    //private final OutputExtension OUT_PUT_EXT = OutputExtension.EXCEL;

    @Override
    protected void handle(ExportServiceContext<CreateOrderInfoDataSource> exportServiceContext){
        CreateOrderInfoDataSource dataSource = exportServiceContext.getQuery();
        OrderInfoDto dataGenerator = dataSource.getGeneratorData(createOrderInfoFileQuery, commandHandler);
        boolean isWorkLocationExport;
        if(!CollectionUtil.isEmpty(dataSource.getWorkLocationCodes())){
            isWorkLocationExport = true;
        }else{
            isWorkLocationExport = false;
        }
        ReservationClosingTimeFrame closingTimeFrame = EnumAdaptor.valueOf(dataSource.getReservationClosingTimeFrame(),
                ReservationClosingTimeFrame.class);
        generator.generate(exportServiceContext.getGeneratorContext(),new OrderInfoExportData(dataGenerator,
                dataSource.isBreakPage(), isWorkLocationExport, closingTimeFrame.name, OutputExtension.EXCEL));
    }
}
