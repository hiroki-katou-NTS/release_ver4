module qmm012.e {
    export module service {
        var paths: any = {
            findItemAttend: "pr/core/itemattend/find",
        }
        export function findItemAttend(itemCode): JQueryPromise<model.ItemAttend> {
            var dfd = $.Deferred<model.ItemAttend>();
            nts.uk.request.ajax(paths.findItemAttend + "/" + itemCode)
                .done(function(res: model.ItemAttend) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res.message);
                })
            return dfd.promise();
        }

        export module model {
            export class ItemAttend {
                avePayAtr: number;
                itemAtr: number;
                errRangeLowAtr: number;
                errRangeLow: number;
                errRangeHighAtr: number;
                errRangeHigh: number;
                alRangeLowAtr: number;
                alRangeLow: number;
                alRangeHighAtr: number;
                alRangeHigh: number;
                workDaysScopeAtr: number;
                memo: string;
                constructor(
                    avePayAtr: number,
                    itemAtr: number,
                    errRangeLowAtr: number,
                    errRangeLow: number,
                    errRangeHighAtr: number,
                    errRangeHigh: number,
                    alRangeLowAtr: number,
                    alRangeLow: number,
                    alRangeHighAtr: number,
                    alRangeHigh: number,
                    workDaysScopeAtr: number,
                    memo: string) {
                    this.avePayAtr = avePayAtr;
                    this.itemAtr = itemAtr;
                    this.errRangeLowAtr = errRangeLowAtr;
                    this.errRangeLow = errRangeLow;
                    this.errRangeHighAtr = errRangeHighAtr;
                    this.errRangeHigh = errRangeHigh;
                    this.alRangeLowAtr = alRangeLowAtr;
                    this.alRangeLow = alRangeLow;
                    this.alRangeHighAtr = alRangeHighAtr;
                    this.alRangeHigh = alRangeHigh;
                    this.workDaysScopeAtr = workDaysScopeAtr;
                    this.memo = memo;

                }
            }
        }
    }
}


