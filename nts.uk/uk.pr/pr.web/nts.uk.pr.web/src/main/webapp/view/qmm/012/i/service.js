var qmm012;
(function (qmm012) {
    var i;
    (function (i) {
        var service;
        (function (service) {
            var paths = {
                findAllItemSalaryBD: "pr/core/itemsalarybd/find",
                findAllItemDeductBD: "pr/core/itemdeductbd/find",
                deleteItemSalaryBD: "pr/core/itemsalarybd/delete",
                deleteItemDeductBD: "pr/core/itemdeductbd/delete",
                addItemSalaryBD: "pr/core/itemsalarybd/add",
                addItemDeductBD: "pr/core/itemdeductbd/add",
                updateItemSalaryBD: "pr/core/itemsalarybd/update",
                updateItemDeductBD: "pr/core/itemdeductbd/update",
            };
            function findAllItemSalaryBD(itemCode) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.findAllItemSalaryBD + "/" + itemCode)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function findAllItemBD(ItemMaster) {
                var dfd = $.Deferred();
                let categoryAtr = ItemMaster.categoryAtr;
                let itemCode = ItemMaster.itemCode;
                if (categoryAtr == 0) {
                    findAllItemSalaryBD(itemCode).done(function (itemBDs) {
                        dfd.resolve(itemBDs);
                    });
                }
                else {
                    findAllItemDeductBD(itemCode).done(function (itemBDs) {
                        dfd.resolve(itemBDs);
                    });
                }
                return dfd.promise();
            }
            service.findAllItemBD = findAllItemBD;
            function findAllItemDeductBD(itemCode) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.findAllItemDeductBD + "/" + itemCode)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function deleteItemDeductBD(itemBD) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.deleteItemDeductBD, itemBD)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function deleteItemSalaryBD(itemBD) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.deleteItemSalaryBD, itemBD)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function deleteItemBD(itemBD, ItemMaster) {
                var dfd = $.Deferred();
                let categoryAtr = ItemMaster.categoryAtr;
                let itemCode = ItemMaster.itemCode;
                if (categoryAtr == 0) {
                    deleteItemSalaryBD(itemBD).done(function (any) {
                        dfd.resolve(any);
                    });
                }
                else {
                    deleteItemDeductBD(itemBD).done(function (any) {
                        dfd.resolve(any);
                    });
                }
                return dfd.promise();
            }
            service.deleteItemBD = deleteItemBD;
            function addItemDeductBD(itemBD) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.addItemDeductBD, itemBD)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function addItemSalaryBD(itemBD) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.addItemSalaryBD, itemBD)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function addItemBD(itemBD, ItemMaster) {
                var dfd = $.Deferred();
                let categoryAtr = ItemMaster.categoryAtr;
                let itemCode = ItemMaster.itemCode;
                if (categoryAtr == 0) {
                    addItemSalaryBD(itemBD).done(function (any) {
                        dfd.resolve(any);
                    });
                }
                else {
                    addItemDeductBD(itemBD).done(function (any) {
                        dfd.resolve(any);
                    });
                }
                return dfd.promise();
            }
            service.addItemBD = addItemBD;
            function updateItemDeductBD(itemBD) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.updateItemDeductBD, itemBD)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function updateItemSalaryBD(itemBD) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.updateItemSalaryBD, itemBD)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function updateItemBD(itemBD, ItemMaster) {
                var dfd = $.Deferred();
                let categoryAtr = ItemMaster.categoryAtr;
                let itemCode = ItemMaster.itemCode;
                if (categoryAtr == 0) {
                    updateItemSalaryBD(itemBD).done(function (any) {
                        dfd.resolve(any);
                    });
                }
                else {
                    updateItemDeductBD(itemBD).done(function (any) {
                        dfd.resolve(any);
                    });
                }
                return dfd.promise();
            }
            service.updateItemBD = updateItemBD;
            var model;
            (function (model) {
                class ItemBD {
                    constructor(itemCode, itemBreakdownCode, itemBreakdownName, itemBreakdownAbName, uniteCode, zeroDispSet, itemDispAtr, errRangeLowAtr, errRangeLow, errRangeHighAtr, errRangeHigh, alRangeLowAtr, alRangeLow, alRangeHighAtr, alRangeHigh) {
                        this.itemCode = itemCode;
                        this.itemBreakdownCode = itemBreakdownCode;
                        this.itemBreakdownName = itemBreakdownName;
                        this.itemBreakdownAbName = itemBreakdownAbName;
                        this.uniteCode = uniteCode;
                        this.zeroDispSet = zeroDispSet;
                        this.itemDispAtr = itemDispAtr;
                        this.errRangeLowAtr = errRangeLowAtr;
                        this.errRangeLow = errRangeLow;
                        this.errRangeHighAtr = errRangeHighAtr;
                        this.errRangeHigh = errRangeHigh;
                        this.alRangeLowAtr = alRangeLowAtr;
                        this.alRangeLow = alRangeLow;
                        this.alRangeHighAtr = alRangeHighAtr;
                        this.alRangeHigh = alRangeHigh;
                    }
                }
                model.ItemBD = ItemBD;
            })(model = service.model || (service.model = {}));
        })(service = i.service || (i.service = {}));
    })(i = qmm012.i || (qmm012.i = {}));
})(qmm012 || (qmm012 = {}));
