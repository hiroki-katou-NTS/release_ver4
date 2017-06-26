module qpp009.a {
    export module service {
        var servicePath = {
            printService: "/screen/pr/qpp009/generate",
        };

        export function printService(data: viewmodel.ScreenModel): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var reportQuery = {
                yearMonth: data.yearMonth(),
//                detailItemsSetting: ko.toJS(data.detailItemsSetting()),
//                printSetting: ko.toJS(data.printSetting())
                
                selectedDivision: data.selectedDivision(),
                isPrintDetailItem: data.detailItemsSetting().isPrintDetailItem(),
                isPrintTotalOfDepartment: data.detailItemsSetting().isPrintTotalOfDepartment(),
                isPrintDepHierarchy: data.detailItemsSetting().isPrintDepHierarchy(),
                selectedLevels: data.detailItemsSetting().selectedLevels(),
                isCalculateTotal: data.detailItemsSetting().isCalculateTotal(),
                selectedBreakPageCode: data.printSetting().selectedBreakPageCode(),
                
                // isBreakPageByEmployee: data.printSetting().
                //isBreakPageByDepartment
                selectedUse2000yen: data.printSetting().selectedUse2000yen(),
                selectedBreakPageHierarchyCode: data.printSetting().selectedBreakPageHierarchyCode(),
                isBreakPageByAccumulated: data.printSetting().isBreakPageByAccumulated()
                }
            nts.uk.request.exportFile(servicePath.printService, reportQuery)
                .done(function() {
                    dfd.resolve();
                }).fail(function(res) {
                    dfd.reject(res);
                });

            return dfd.promise();
        }

        export interface SalaryChartResultDto {
            empCode: string;
            empName: string;
            paymentAmount: number;
            empDesignation: string;
            depDesignation: string;
            totalDesignation: string;
            depCode: string;
            depName: string;
            depPath: string;
        }
    }
}

