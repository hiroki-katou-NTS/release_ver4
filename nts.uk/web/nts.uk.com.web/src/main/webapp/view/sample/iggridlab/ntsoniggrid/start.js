var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var ui;
        (function (ui_1) {
            var gridlist;
            (function (gridlist) {
                __viewContext.ready(function () {
                    class ScreenModel {
                        constructor() {
                            this.modes = ko.observableArray([
                                { code: '1', name: '四捨五入' },
                                { code: '2', name: '切り上げ' },
                                { code: '3', name: '切り捨て' }
                            ]);
                            this.flagTemplate = '<div class="nts-binding" data-bind="ntsCheckBox: { checked: flag }">Enable</div>';
                            this.items = (function () {
                                var list = [];
                                for (var i = 0; i < 500; i++) {
                                    list.push(new GridItem(i));
                                }
                                return ko.observableArray(list);
                            })();
                        }
                        rowsRendered(evt, ui) {
                            // 
                            _.defer(() => {
                                $('.nts-binding').not('.nts-binding-done').each(function () {
                                    var $this = $(this).addClass('.nts-binding-done');
                                    var rowIndex = ui_1.ig.grid.getRowIndexFrom($this);
                                    var item = model.items()[rowIndex];
                                    ko.applyBindings(item, $(this).closest('tr')[0]);
                                    $this.one('remove', function (e) {
                                        ko.cleanNode(this);
                                    });
                                });
                            });
                        }
                    }
                    class GridItem {
                        constructor(index) {
                            this.id = index;
                            this.flag = ko.observable(index % 2 == 0);
                            this.ruleCode = ko.observable(String(index % 3 + 1));
                        }
                    }
                    var model = new ScreenModel();
                    this.bind(model);
                });
            })(gridlist = ui_1.gridlist || (ui_1.gridlist = {}));
        })(ui = uk.ui || (uk.ui = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
//# sourceMappingURL=start.js.map
