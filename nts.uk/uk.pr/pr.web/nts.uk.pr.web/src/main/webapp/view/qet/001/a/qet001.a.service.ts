module qet001.a {
    export module service {

        // Service paths.
        var servicePath = {
            findOutputSettings: 'ctx/pr/report/wageledger/outputsetting/findAll',
            printReport: 'screen/pr/qet001/print'
        }
        
        /**
         * Find all output setting services.
         */
        export function findOutputSettings(): JQueryPromise<model.WageLedgerOutputSetting[]>{
            return nts.uk.request.ajax(servicePath.findOutputSettings);
        }
        
        /**
         * Print report service.
         */
        export function printReport(data: viewmodel.ScreenModel) : JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var dataJson = {
                targetYear: data.targetYear(),
                isAggreatePreliminaryMonth: data.isAggreatePreliminaryMonth(),
                layoutType: data.layoutSelected(),
                isPageBreakIndicator: data.isPageBreakIndicator(),
                outputType: data.outputTypeSelected(),
                outputSettingCode: data.outputSettingSelectedCode()
                // TODO: need employee code list.
            }
            nts.uk.request.exportFile(servicePath.printReport, dataJson).done(function() {
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise();
        }
        
        /**
        * Model namespace.
        */
        export module model {
            export class WageLedgerOutputSetting {
                code: string;
                name: string;
                isOnceSheetPerPerson: boolean;
                categorySettings: WageledgerCategorySetting[];
            }
            
            export class WageledgerCategorySetting {
                category: string;
                paymentType: string;
                outputItems: WageLedgerSettingItem[];
            }
            
            export interface WageLedgerSettingItem {
                code: string;
                name: string;
                isAggregateItem: boolean;
                orderNumber?: number;
                itemKey: string;
            }
        }
    }
}
