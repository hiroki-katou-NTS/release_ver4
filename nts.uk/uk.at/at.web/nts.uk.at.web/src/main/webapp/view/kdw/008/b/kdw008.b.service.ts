module nts.uk.at.view.kdw008.b {
    export module service {
        export class Service {
            paths = {
                addDailyDetail: "at/record/businesstype/addBusTypeFormat",
                updateDailyDetail: "at/record/businesstype/updateBusTypeFormat",
//                addMonthlyDetail: "at/record/businesstype/addBusinessTypeMonthlyDetail",
//                updateMonthlyDetail: "at/record/businesstype/updateBusinessTypeMonthlyDetail",
                
                
                getBusinessType: "at/record/businesstype/findAll",
                getDailyPerformance: "at/record/businesstype/find/businessTypeDetail/{0}/{1}",
                
                //monthly
                getListMonthlyAttdItem:"at/record/attendanceitem/monthly/findall",
                
                // monthly tab3
                getListMonthRight: "at/function/monthlycorrection/findbycode/{0}"
            }

            constructor() {

            }

            addDailyDetail(AddBusTypeCommand: any): JQueryPromise<any> {
                return nts.uk.request.ajax("at", this.paths.addDailyDetail, AddBusTypeCommand);
            };

            updateDailyDetail(UpdateBusTypeCommand: any): JQueryPromise<any> {
                return nts.uk.request.ajax("at", this.paths.updateDailyDetail, UpdateBusTypeCommand);
            };

//            addMonthlyDetail(AddBusinessTypeMonthlyCommand: any): JQueryPromise<any> {
//                return nts.uk.request.ajax("at", this.paths.addMonthlyDetail, AddBusinessTypeMonthlyCommand);
//            };

//            updateMonthlyDetail(UpdateBusinessTypeMonthlyCommand: any): JQueryPromise<any> {
//                return nts.uk.request.ajax("at", this.paths.updateMonthlyDetail, UpdateBusinessTypeMonthlyCommand);
//            };

            getBusinessType(): JQueryPromise<any> {
                let _path = nts.uk.text.format(this.paths.getBusinessType);
                return nts.uk.request.ajax("at", _path);
            };

            getDailyPerformance(businessTypeCode: string, sheetNo: number): JQueryPromise<any> {
                let _path = nts.uk.text.format(this.paths.getDailyPerformance, businessTypeCode, sheetNo);
                return nts.uk.request.ajax("at", _path);
            };
            
            getListMonthRight(businessTypeCode: string): JQueryPromise<any> {
                let _path = nts.uk.text.format(this.paths.getListMonthRight, businessTypeCode);
                return nts.uk.request.ajax("at", _path);
            };
            
             // monthly
            getListMonthlyAttdItem(): JQueryPromise<any> {
                return nts.uk.request.ajax("at",nts.uk.text.format(this.paths.getListMonthlyAttdItem));
            };


        }
    }
}
