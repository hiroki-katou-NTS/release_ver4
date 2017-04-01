module qmm012.h {
    export module service {
        var paths: any = {
            findItemSalaryPeriod: "pr/core/itemsalaryperiod/find",
            findItemDeductPeriod: "pr/core/itemdeductperiod/find",
            addItemSalaryPeriod: "pr/core/itemsalaryperiod/add",
            addItemDeductPeriod: "pr/core/itemdeductperiod/add",
            updateItemSalaryPeriod: "pr/core/itemsalaryperiod/update",
            updateItemDeductPeriod: "pr/core/itemdeductperiod/update",
        }
        function findItemSalaryPeriod(itemCode): JQueryPromise<model.ItemPeriod> {
            var dfd = $.Deferred<model.ItemPeriod>();
            nts.uk.request.ajax(paths.findItemSalaryPeriod + "/" + itemCode)
                .done(function(res: model.ItemPeriod) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        function findItemDeductPeriod(itemCode): JQueryPromise<model.ItemPeriod> {
            var dfd = $.Deferred<model.ItemPeriod>();
            nts.uk.request.ajax(paths.findItemDeductPeriod + "/" + itemCode)
                .done(function(res: model.ItemPeriod) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        export function findItemPeriod(itemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<model.ItemPeriod> {
            var dfd = $.Deferred<model.ItemPeriod>();
            let categoryAtr = itemMaster.categoryAtr;
            let itemCode = itemMaster.itemCode;
            if (categoryAtr == 0) {
                findItemSalaryPeriod(itemCode).done(function(ItemPeriod: service.model.ItemPeriod) {
                    dfd.resolve(ItemPeriod);
                }).fail(function(res) {
                    // Alert message
                    dfd.reject(res);
                });
            }
            if (categoryAtr == 1) {
                findItemDeductPeriod(itemCode).done(function(ItemPeriod: service.model.ItemPeriod) {
                    dfd.resolve(ItemPeriod);
                }).fail(function(res) {
                    // Alert message
                    dfd.reject(res);
                });
            }
            return dfd.promise();
        }
        function addItemDeductPeriod(itemPeriod: model.ItemPeriod): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.addItemDeductPeriod, itemPeriod)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        function addItemSalaryPeriod(itemPeriod: model.ItemPeriod): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.addItemSalaryPeriod, itemPeriod)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        export function addItemPeriod(itemMaster: qmm012.b.service.model.ItemMaster, itemPeriod: model.ItemPeriod) {
            var dfd = $.Deferred<model.ItemPeriod>();
            let categoryAtr = itemMaster.categoryAtr;
            if (categoryAtr == 0) {
                addItemSalaryPeriod(itemPeriod).done(function(res: any) {
                    dfd.resolve(res);
                }).fail(function(res) {
                    // Alert message
                    dfd.reject(res);
                });
            }
            if (categoryAtr == 1) {
                addItemDeductPeriod(itemPeriod).done(function(res: any) {
                    dfd.resolve(res);
                }).fail(function(res) {
                    // Alert message
                    dfd.reject(res);
                });
            }
            return dfd.promise();
        }
        export function updateItemPeriod(itemMaster: qmm012.b.service.model.ItemMaster, itemPeriod: model.ItemPeriod) {
            var dfd = $.Deferred<model.ItemPeriod>();
            let categoryAtr = itemMaster.categoryAtr;
            if (categoryAtr == 0) {
                updateItemSalaryPeriod(itemPeriod).done(function(res: any) {
                    dfd.resolve(res);
                }).fail(function(res) {
                    // Alert message
                    dfd.reject(res);
                });
            }
            if (categoryAtr == 1) {
                updateItemDeductPeriod(itemPeriod).done(function(res: any) {
                    dfd.resolve(res);
                }).fail(function(res) {
                    // Alert message
                    dfd.reject(res);
                });
            }
            return dfd.promise();
        }
        function updateItemSalaryPeriod(itemPeriod: model.ItemPeriod): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.updateItemSalaryPeriod, itemPeriod)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        function updateItemDeductPeriod(itemPeriod: model.ItemPeriod): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.updateItemDeductPeriod, itemPeriod)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }


        export module model {



            export class ItemPeriod {
                itemCd: string;
                periodAtr: number;
                strY: number;
                endY: number;
                cycleAtr: number;
                cycle01Atr: number;
                cycle02Atr: number;
                cycle03Atr: number;
                cycle04Atr: number;
                cycle05Atr: number;
                cycle06Atr: number;
                cycle07Atr: number;
                cycle08Atr: number;
                cycle09Atr: number;
                cycle10Atr: number;
                cycle11Atr: number;
                cycle12Atr: number;
                constructor(itemCd: string, periodAtr: number, strY: number, endY: number, cycleAtr: number, cycle01Atr: number, cycle02Atr: number,
                    cycle03Atr: number, cycle04Atr: number, cycle05Atr: number, cycle06Atr: number, cycle07Atr: number, cycle08Atr: number, cycle09Atr: number,
                    cycle10Atr: number, cycle11Atr: number, cycle12Atr: number) {
                    this.itemCd = itemCd;
                    this.periodAtr = periodAtr;
                    this.strY = strY;
                    this.endY = endY;
                    this.cycleAtr = cycleAtr;
                    this.cycle01Atr = cycle01Atr;
                    this.cycle02Atr = cycle02Atr;
                    this.cycle03Atr = cycle03Atr;
                    this.cycle04Atr = cycle04Atr;
                    this.cycle05Atr = cycle05Atr;
                    this.cycle06Atr = cycle06Atr;
                    this.cycle07Atr = cycle07Atr;
                    this.cycle08Atr = cycle08Atr;
                    this.cycle09Atr = cycle09Atr;
                    this.cycle10Atr = cycle10Atr;
                    this.cycle11Atr = cycle11Atr;
                    this.cycle12Atr = cycle12Atr;
                }
            }
        }
    }
}