var qmm012;
(function (qmm012) {
    var h;
    (function (h) {
        var service;
        (function (service) {
            var paths = {
                findItemSalaryPeriod: "pr/core/itemsalaryperiod/find",
                findItemDeductPeriod: "pr/core/itemdeductperiod/find",
            };
            function findItemSalaryPeriod(itemCode) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.findItemSalaryPeriod + "/" + itemCode)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.findItemSalaryPeriod = findItemSalaryPeriod;
            function findItemDeductPeriod(itemCode) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.findItemDeductPeriod + "/" + itemCode)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.findItemDeductPeriod = findItemDeductPeriod;
            function findItemPeriod(ItemMaster) {
                var dfd = $.Deferred();
                let categoryAtr = ItemMaster.categoryAtr;
                let itemCode = ItemMaster.itemCode;
                if (categoryAtr == 0) {
                    service.findItemSalaryPeriod(itemCode).done(function (ItemPeriod) {
                        dfd.resolve(ItemPeriod);
                    }).fail(function (res) {
                        // Alert message
                        dfd.reject(res);
                    });
                }
                if (categoryAtr == 1) {
                    service.findItemDeductPeriod(itemCode).done(function (ItemPeriod) {
                        dfd.resolve(ItemPeriod);
                    }).fail(function (res) {
                        // Alert message
                        dfd.reject(res);
                    });
                }
                return dfd.promise();
            }
            service.findItemPeriod = findItemPeriod;
            function addItemSalaryPeriod(itemPeriod) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.addItemSalaryPeriod, itemPeriod)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function addItemDeductPeriod(itemPeriod) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.addItemDeductPeriod, itemPeriod)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function addItemPeriod(itemPeriod, itemMaster) {
                var dfd = $.Deferred();
                let categoryAtr = itemMaster.categoryAtr;
                let itemCode = itemMaster.itemCode;
                if (categoryAtr == 0) {
                    addItemSalaryPeriod(itemPeriod).done(function (any) {
                        dfd.resolve(any);
                    }).fail(function (res) {
                        // Alert message
                        dfd.reject(res);
                    });
                }
                if (categoryAtr == 1) {
                    addItemDeductPeriod(itemPeriod).done(function (any) {
                        dfd.resolve(any);
                    }).fail(function (res) {
                        // Alert message
                        dfd.reject(res);
                    });
                }
                return dfd.promise();
            }
            service.addItemPeriod = addItemPeriod;
            function updateItemSalaryPeriod(itemPeriod) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.updateItemSalaryPeriod, itemPeriod)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function updateItemDeductPeriod(itemPeriod) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.updateItemDeductPeriod, itemPeriod)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            function updateItemPeriod(itemPeriod, itemMaster) {
                var dfd = $.Deferred();
                let categoryAtr = itemMaster.categoryAtr;
                let itemCode = itemMaster.itemCode;
                if (categoryAtr == 0) {
                    updateItemSalaryPeriod(itemPeriod).done(function (any) {
                        dfd.resolve(any);
                    }).fail(function (res) {
                        // Alert message
                        dfd.reject(res);
                    });
                }
                if (categoryAtr == 1) {
                    updateItemDeductPeriod(itemPeriod).done(function (any) {
                        dfd.resolve(any);
                    }).fail(function (res) {
                        // Alert message
                        dfd.reject(res);
                    });
                }
                return dfd.promise();
            }
            service.updateItemPeriod = updateItemPeriod;
            var model;
            (function (model) {
                class ItemPeriod {
                    constructor(itemCode, periodAtr, strY, endY, cycleAtr, cycle01Atr, cycle02Atr, cycle03Atr, cycle04Atr, cycle05Atr, cycle06Atr, cycle07Atr, cycle08Atr, cycle09Atr, cycle10Atr, cycle11Atr, cycle12Atr) {
                        this.itemCode = itemCode;
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
                model.ItemPeriod = ItemPeriod;
            })(model = service.model || (service.model = {}));
        })(service = h.service || (h.service = {}));
    })(h = qmm012.h || (qmm012.h = {}));
})(qmm012 || (qmm012 = {}));
