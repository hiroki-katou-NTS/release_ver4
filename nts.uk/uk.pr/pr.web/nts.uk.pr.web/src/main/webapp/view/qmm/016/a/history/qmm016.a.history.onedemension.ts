module nts.uk.pr.view.qmm016.a.history {
    /**
     * For one demension view.
     */
    export class OneDemensionViewModel extends base.BaseHistoryViewModel {
        igGrid: any;
        igGridDataSource: KnockoutObservableArray<ItemViewModel>;
        elements: KnockoutObservable<model.ElementSettingDto>;

        constructor(history: model.WageTableHistoryDto) {
            super('history/onedemension.xhtml', history);
            this.igGridDataSource = ko.observableArray<ItemViewModel>([]);

            if (history.valueItems && history.valueItems.length > 0) {
                var element = history.elements[0];
                var itemVmList = _.map(element.itemList, (item) => {
                    var vm = new ItemViewModel(viewmodel.getElementTypeByValue(element.type), item);
                    // Filter value.
                    vm.amount(_.filter(history.valueItems, (vi) => {
                        return vi.element1Id == item.uuid;
                    })[0].amount);

                    return vm;
                })
                this.igGridDataSource(itemVmList);
            }
        }

        /**
         * On load processing.
         */
        onLoad(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            dfd.resolve();
            self.initIgGrid();
            return dfd.promise();
        }

        /**
         * Init ig grid.
         */
        initIgGrid(): void {
            var self = this;

            // IgGrid
            self.igGrid = ko.observable({
                dataSource: self.igGridDataSource,
                width: '60%',
                primaryKey: 'uuid',
                height: '250px',
                features: [
                    {
                        name: 'Updating',
                        editMode: 'row',
                        enableAddRow: false,
                        excelNavigatorMode: false,
                        enableDeleteRow: false,
                        columnSettings: [
                            {
                                columnKey: 'uuid',
                                readOnly: true
                            },
                            {
                                columnKey: 'name',
                                readOnly: true
                            },
                            {
                                columnKey: 'amount',
                                editorProvider: new (<any>$.ig).NtsNumberEditor(),
                                editorOptions: {
                                    constraint: 'WtValue',
                                    option: {
                                    },
                                    required: true
                                },
                                readOnly: false
                            }
                        ],
                    }
                ],
                autoCommit: true,
                columns: [
                    { headerText: 'Element Name', dataType: 'string', key: 'uuid', hidden: true},
                    { headerText: self.elementSettings[0].demensionName, dataType: 'string', key: 'name', width: '50%', columnCssClass: "bgIgCol"},
                    { headerText: '値', dataType: 'number', key: 'amount', width: '50%', columnCssClass: "halign-right"}
                ]
            });
        }

        /**
         * On refresh element.
         */
        onRefreshElement(): void {
            var self = this;

            // First element.
            var element = self.elementSettings[0];
            var itemVmList = _.map(element.itemList, (item) => {
                return new ItemViewModel(viewmodel.getElementTypeByValue(element.type), item);
            });

            // Update source
            self.igGridDataSource(itemVmList);
        }

        /**
         * Get setting cell item.
         */
        getCellItem(): Array<model.CellItemDto> {
            return _.map(this.igGridDataSource(), item => {
                var dto = <model.CellItemDto> {};
                dto.element1Id = item.uuid;
                dto.amount = item.amount();
                return dto;
            });
        }

        /**
         * Paste data from excel.
         */
        pasteFromExcel(): void {
            // Do parsing.
            return;
        }
    }

    /**
     * Item view model.
     */
    class ItemViewModel {
        uuid: string;
        name: string;
        amount: KnockoutObservable<number>;

        /**
         * Constructor.
         */
        constructor(type: model.ElementTypeDto, item: model.ItemDto) {
            var self = this;
            self.uuid = item.uuid;
            self.name = item.displayName;
            self.amount = ko.observable(0);
        }
    }
}