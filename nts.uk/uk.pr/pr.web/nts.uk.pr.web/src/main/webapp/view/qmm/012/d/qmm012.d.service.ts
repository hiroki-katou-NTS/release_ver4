module qmm012.d {
    export module service {
        var paths: any = {
            findItemDeduct: "pr/core/itemdeduct/find",
        }

        export function findItemDeduct(itemCode): JQueryPromise<model.ItemDeduct> {
            var dfd = $.Deferred<model.ItemDeduct>();
            nts.uk.request.ajax(paths.findItemDeduct + "/" + itemCode)
                .done(function(res: model.ItemDeduct) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res.message);
                })
            return dfd.promise();
        }
        export module model {
            export class ItemDeduct {
                deductAtr: number;
                errRangeLowAtr: number;
                errRangeLow: number;
                errRangeHighAtr: number;
                errRangeHigh: number;
                alRangeLowAtr: number;
                alRangeLow: number;
                alRangeHighAtr: number;
                alRangeHigh: number;
                memo: String;
                constructor(
                    deductAtr: number,
                    errRangeLowAtr: number,
                    errRangeLow: number,
                    errRangeHighAtr: number,
                    errRangeHigh: number,
                    alRangeLowAtr: number,
                    alRangeLow: number,
                    alRangeHighAtr: number,
                    alRangeHigh: number,
                    memo: String
                ) {
                        this.deductAtr = deductAtr,
                        this.errRangeLowAtr = errRangeLowAtr,
                        this.errRangeLow = errRangeLow,
                        this.errRangeHighAtr = errRangeHighAtr,
                        this.errRangeHigh = errRangeHigh,
                        this.alRangeLowAtr = alRangeLowAtr,
                        this.alRangeLow = alRangeLow,
                        this.alRangeHighAtr = alRangeHighAtr,
                        this.alRangeHigh = alRangeHigh,
                        this.memo = memo
                }
            }
        }
    }

}





