module nts.uk.pr.view.qmm011.d {
    import HistoryInsuranceRateDto = nts.uk.pr.view.qmm011.a.service.model.HistoryInsuranceRateDto;
    import MonthRange = nts.uk.pr.view.qmm011.a.service.model.MonthRange;
    export module service {
        var paths: any = {
            addUnemployeeInsuranceRate: "pr/insurance/labor/unemployeerate/add"
            
        };

        export function addHistoryInfoUnemployeeInsurance(historyInfo: model.HistoryInfoDto): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            var data = { historyInfoDto: historyInfo, comanyCode: "CC0001" };
            nts.uk.request.ajax(paths.addUnemployeeInsuranceRate, data)
                .done(function(res: any) {
                    dfd.resolve(res);
                    //xyz
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();

        }
        

        export module model {
            export class HistoryInfoDto extends HistoryInsuranceRateDto {
                takeover: boolean;
                constructor(historyId: string, companyCode: string, monthRage: MonthRange, startMonthRage: string,
                    endMonthRage: string, takeover: boolean) {
                    super(historyId, companyCode, monthRage, startMonthRage,
                        endMonthRage);
                    this.takeover = takeover;
                }
            }
        }
    }
}