module qmm012.c {
    export module service {
        var paths: any = {
            findItemSalary: "pr/core/itemsalary/find",
            getCommuteNoTaxLimit: "core/commutelimit/getCommuteNoTaxLimit"
        }
        export function getCommuteNoTaxLimit(commuNoTaxLimitCode): JQueryPromise<qmm023.a.service.model.CommuteNoTaxLimitDto> {
            var dfd = $.Deferred<qmm023.a.service.model.CommuteNoTaxLimitDto>();
            nts.uk.request.ajax(paths.getCommuteNoTaxLimit + "/" + commuNoTaxLimitCode)
                .done(function(res: qmm023.a.service.model.CommuteNoTaxLimitDto) {
                    dfd.resolve(res);
                }).fail(function(res) {
                    dfd.reject(res.message);
                })
            return dfd.promise();
        }
        export function findItemSalary(itemCode): JQueryPromise<model.ItemSalary> {
            var dfd = $.Deferred<model.ItemSalary>();
            nts.uk.request.ajax(paths.findItemSalary + "/" + itemCode)
                .done(function(res: model.ItemSalary) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res.message);
                })
            return dfd.promise();
        }
        export module model {
            export class ItemSalary {
                taxAtr: number;
                socialInsAtr: number;
                laborInsAtr: number;
                fixPayAtr: number;
                applyForAllEmpFlg: number;
                applyForMonthlyPayEmp: number;
                applyForDaymonthlyPayEmp: number;
                applyForDaylyPayEmp: number;
                applyForHourlyPayEmp: number;
                avePayAtr: number;
                errRangeLowAtr: number;
                errRangeLow: number;
                errRangeHighAtr: number;
                errRangeHigh: number;
                alRangeLowAtr: number;
                alRangeLow: number;
                alRangeHighAtr: number;
                alRangeHigh: number;
                memo: string;
                limitMnyAtr: number;
                limitMnyRefItemCode: string;
                limitMny: number;
                constructor(
                    taxAtr: number,
                    socialInsAtr: number,
                    laborInsAtr: number,
                    fixPayAtr: number,
                    applyForAllEmpFlg: number,
                    applyForMonthlyPayEmp: number,
                    applyForDaymonthlyPayEmp: number,
                    applyForDaylyPayEmp: number,
                    applyForHourlyPayEmp: number,
                    avePayAtr: number,
                    errRangeLowAtr: number,
                    errRangeLow: number,
                    errRangeHighAtr: number,
                    errRangeHigh: number,
                    alRangeLowAtr: number,
                    alRangeLow: number,
                    alRangeHighAtr: number,
                    alRangeHigh: number,
                    memo: string,
                    limitMnyAtr: number,
                    limitMnyRefItemCode: string,
                    limitMny: number

                ) {
                    this.taxAtr = taxAtr;
                    this.socialInsAtr = socialInsAtr;
                    this.laborInsAtr = laborInsAtr;
                    this.fixPayAtr = fixPayAtr;
                    this.applyForAllEmpFlg = applyForAllEmpFlg;
                    this.applyForMonthlyPayEmp = applyForMonthlyPayEmp;
                    this.applyForDaymonthlyPayEmp = applyForDaymonthlyPayEmp;
                    this.applyForDaylyPayEmp = applyForDaylyPayEmp;
                    this.applyForHourlyPayEmp = applyForHourlyPayEmp;
                    this.avePayAtr = avePayAtr;
                    this.errRangeLowAtr = errRangeLowAtr;
                    this.errRangeLow = errRangeLow;
                    this.errRangeHighAtr = errRangeHighAtr;
                    this.errRangeHigh = errRangeHigh;
                    this.alRangeLowAtr = alRangeLowAtr;
                    this.alRangeLow = alRangeLow;
                    this.alRangeHighAtr = alRangeHighAtr;
                    this.alRangeHigh = alRangeHigh;
                    this.memo = memo;
                    this.limitMnyAtr = limitMnyAtr;
                    this.limitMnyRefItemCode = limitMnyRefItemCode;
                    this.limitMny = limitMny;


                };
            }
        }
    }

}



