module nts.uk.pr.view.qpp007.b {
    export module service {
        /**
         *  Service paths
         */
        var paths: any = {
            saveSalaryPrintSetting: "ctx/pr/report/salarydetail/printsetting/save",
            findSalaryPrintSetting: "ctx/pr/report/salarydetail/printsetting/find"
        };

        /**
         *  Save SalaryPrintSetting.
         */
        export function saveSalaryPrintSetting(data: model.SalaryPrintSettingDto): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.saveSalaryPrintSetting, data);
        }

        /**
         *  Load SalaryPrintSetting.
         */
        export function findSalaryPrintSetting(): JQueryPromise<model.SalaryPrintSettingDto> {
            return nts.uk.request.ajax(paths.findSalaryPrintSetting);
        }

        /**
        * Model namespace.
        */
        export module model {

            export interface SalaryPrintSettingDto {
                showPayment: boolean;
                sumPersonSet: boolean;
                sumMonthPersonSet: boolean;
                sumEachDeprtSet: boolean;
                sumMonthDeprtSet: boolean;
                sumDepHrchyIndexSet: boolean;
                sumMonthDepHrchySet: boolean;
                hrchyIndex1: boolean;
                hrchyIndex2: boolean;
                hrchyIndex3: boolean;
                hrchyIndex4: boolean;
                hrchyIndex5: boolean;
                hrchyIndex6: boolean;
                hrchyIndex7: boolean;
                hrchyIndex8: boolean;
                hrchyIndex9: boolean;
                totalSet: boolean;
                monthTotalSet: boolean;
            }

        }
    }
}