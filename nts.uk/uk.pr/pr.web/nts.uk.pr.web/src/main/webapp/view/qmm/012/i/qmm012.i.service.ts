module qmm012.i {
    export module service {
        var paths: any = {
            findAllItemSalaryBD: "pr/core/itemsalarybd/find",
            findAllItemDeductBD: "pr/core/itemdeductbd/find",
            deleteItemSalaryBD: "pr/core/itemsalarybd/delete",
            deleteItemDeductBD: "pr/core/itemdeductbd/delete",
            addItemSalaryBD: "pr/core/itemsalarybd/add",
            addItemDeductBD: "pr/core/itemdeductbd/add",
            updateItemSalaryBD: "pr/core/itemsalarybd/update",
            updateItemDeductBD: "pr/core/itemdeductbd/update",
        }
        function findAllItemSalaryBD(itemCode): JQueryPromise<Array<model.ItemBD>> {
            var dfd = $.Deferred<Array<model.ItemBD>>();
            nts.uk.request.ajax(paths.findAllItemSalaryBD + "/" + itemCode)
                .done(function(res: Array<model.ItemBD>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        export function findAllItemBD(ItemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<Array<model.ItemBD>> {
            var dfd = $.Deferred<Array<model.ItemBD>>();
            let categoryAtr = ItemMaster.categoryAtr;
            let itemCode = ItemMaster.itemCode;
            if (categoryAtr == 0) {
                findAllItemSalaryBD(itemCode).done(function(itemBDs: Array<model.ItemBD>) {
                    dfd.resolve(itemBDs);
                });
            } else {
                findAllItemDeductBD(itemCode).done(function(itemBDs: Array<model.ItemBD>) {
                    dfd.resolve(itemBDs);
                });
            }
            return dfd.promise();
        }
        function findAllItemDeductBD(itemCode): JQueryPromise<Array<model.ItemBD>> {
            var dfd = $.Deferred<Array<model.ItemBD>>();
            nts.uk.request.ajax(paths.findAllItemDeductBD + "/" + itemCode)
                .done(function(res: Array<model.ItemBD>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        function deleteItemDeductBD(itemBD: model.ItemBD) {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.deleteItemDeductBD, itemBD)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        function deleteItemSalaryBD(itemBD: model.ItemBD) {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.deleteItemSalaryBD, itemBD)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        export function deleteItemBD(itemBD: model.ItemBD, ItemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            let categoryAtr = ItemMaster.categoryAtr;
            let itemCode = ItemMaster.itemCode;
            if (categoryAtr == 0) {
                deleteItemSalaryBD(itemBD).done(function(any) {
                    dfd.resolve(any);
                });
            } else {
                deleteItemDeductBD(itemBD).done(function(any) {
                    dfd.resolve(any);
                });
            }
            return dfd.promise();
        }
        function addItemDeductBD(itemBD: model.ItemBD) {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.addItemDeductBD, itemBD)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        function addItemSalaryBD(itemBD: model.ItemBD) {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.addItemSalaryBD, itemBD)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        export function addItemBD(itemBD: model.ItemBD, ItemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            let categoryAtr = ItemMaster.categoryAtr;
            let itemCode = ItemMaster.itemCode;
            if (categoryAtr == 0) {
                addItemSalaryBD(itemBD).done(function(any) {
                    dfd.resolve(any);
                });
            } else {
                addItemDeductBD(itemBD).done(function(any) {
                    dfd.resolve(any);
                });
            }
            return dfd.promise();
        }
        function updateItemDeductBD(itemBD: model.ItemBD) {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.updateItemDeductBD, itemBD)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        function updateItemSalaryBD(itemBD: model.ItemBD) {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.updateItemSalaryBD, itemBD)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        export function updateItemBD(itemBD: model.ItemBD, ItemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            let categoryAtr = ItemMaster.categoryAtr;
            let itemCode = ItemMaster.itemCode;
            if (categoryAtr == 0) {
                updateItemSalaryBD(itemBD).done(function(any) {
                    dfd.resolve(any);
                });
            } else {
                updateItemDeductBD(itemBD).done(function(any) {
                    dfd.resolve(any);
                });
            }
            return dfd.promise();
        }

        export module model {
            export class ItemBD {
                itemCode: string;
                itemBreakdownCode: string;
                itemBreakdownName: string;
                itemBreakdownAbName: string;
                uniteCode: string;
                zeroDispSet: number;
                itemDispAtr: number;
                errRangeLowAtr: number;
                errRangeLow: number;
                errRangeHighAtr: number;
                errRangeHigh: number;
                alRangeLowAtr: number;
                alRangeLow: number;
                alRangeHighAtr: number;
                alRangeHigh: number;
                constructor(
                    itemCode?: string,
                    itemBreakdownCode?: string,
                    itemBreakdownName?: string,
                    itemBreakdownAbName?: string,
                    uniteCode?: string,
                    zeroDispSet?: number,
                    itemDispAtr?: number,
                    errRangeLowAtr?: number,
                    errRangeLow?: number,
                    errRangeHighAtr?: number,
                    errRangeHigh?: number,
                    alRangeLowAtr?: number,
                    alRangeLow?: number,
                    alRangeHighAtr?: number,
                    alRangeHigh?: number
                ) {
                    this.itemCode = itemCode ? itemCode : '';
                    this.itemBreakdownCode = itemBreakdownCode ? itemBreakdownCode : '';
                    this.itemBreakdownName = itemBreakdownName ? itemBreakdownName : '';
                    this.itemBreakdownAbName = itemBreakdownAbName ? itemBreakdownAbName : '';
                    this.uniteCode = uniteCode ? uniteCode : '';
                    this.zeroDispSet = zeroDispSet ? zeroDispSet : 0;
                    this.itemDispAtr = itemDispAtr ? itemDispAtr : 0;
                    this.errRangeLowAtr = errRangeLowAtr ? errRangeLowAtr : 0;
                    this.errRangeLow = errRangeLow ? errRangeLow : 0;
                    this.errRangeHighAtr = errRangeHighAtr ? errRangeHighAtr : 0;
                    this.errRangeHigh = errRangeHigh ? errRangeHigh : 0;
                    this.alRangeLowAtr = alRangeLowAtr ? alRangeLowAtr : 0;
                    this.alRangeLow = alRangeLow ? alRangeLow : 0;
                    this.alRangeHighAtr = alRangeHighAtr ? alRangeHighAtr : 0;
                    this.alRangeHigh = alRangeHigh ? alRangeHigh : 0;

                }
            }
        }

    }
}