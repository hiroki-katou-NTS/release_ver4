module nts.uk.pr.view.qmm016.a.history {
    /**
     * For two demension view.
     */
    export class ThreeDemensionViewModel extends base.BaseHistoryViewModel {
        igGrid: any;
        igGridDataSource: KnockoutObservableArray<ItemViewModel>;

        // Item 3 information.
        element3Name: KnockoutObservable<string>;
        element3Items: KnockoutObservableArray<model.ItemDto>;
        selectedElement3ItemId: KnockoutObservable<string>;
        datasourceMap: {[index: string]: Array<ItemViewModel>};
        constructor(history: model.WageTableHistoryDto) {
            super('history/threedemension.xhtml', history);
            var self = this;
            self.igGridDataSource = ko.observableArray<ItemViewModel>([]);
            self.element3Name = ko.observable('');
            self.element3Items = ko.observableArray<model.ItemDto>([]);
            self.selectedElement3ItemId = ko.observable(undefined);
            self.datasourceMap = <any>[];

            // On change.
            self.selectedElement3ItemId.subscribe((id) => {
                if (id && self.datasourceMap[id]) {
                    self.initIgGrid(self.datasourceMap[id]);
                }
            })
        }

        /**
         * On load init ig grid.
         */
        onLoad(): JQueryPromise<any> {
            var self = this;

            // Build first data source.
            var history = self.history;
            if (history.valueItems && history.valueItems.length > 0) {
                var element1 = history.elements[0];
                var element2 = history.elements[1];
                var element3 = history.elements[2];

                element3.itemList.forEach((item3) => {
                    var itemVmList = _.map(element1.itemList, (item) => {
                        var vm = new ItemViewModel(element1.type, item);
                        var valueItemMap: {[index: string]: model.CellItemDto} = <any>[];
                        _.filter(history.valueItems, (vi) => { return vi.element1Id == item.uuid && vi.element3Id == item3.uuid })
                            .forEach((vi) => {valueItemMap[vi.element2Id] = vi});

                        element2.itemList.forEach((item2) => {
                            vm[item2.uuid] = ko.observable(valueItemMap[item2.uuid].amount);
                        })
                        return vm;
                    })
                    self.datasourceMap[item3.uuid] = itemVmList;
                })

                // Build element 3 information.
                self.buildElement3Infomation();
            }

            // Ret.
            return $.Deferred().resolve().promise();
        }


        /**
         * On refresh element.
         */
        onRefreshElement(): void {
            var self = this;

            // Build elements 3 data source.
            self.datasourceMap = <any>[];
            self.elementSettings[2].itemList.forEach((item3) => {
                // Update data source.
                var firstEl = self.elementSettings[0];
                var secondEl = self.elementSettings[1];
                var dataSource: Array<ItemViewModel> = [];
                firstEl.itemList.forEach(firstItem => {
                    var vm = new ItemViewModel(firstEl.type, firstItem);
                    secondEl.itemList.forEach(secondItem => {
                        vm[secondItem.uuid] = ko.observable(0);
                    })
                    dataSource.push(vm);
                })
               self.datasourceMap[item3.uuid] = dataSource;
            })

            // Build elements 3 item info.
            self.buildElement3Infomation();
        }

        /**
         * Get setting cell item.
         */
        getCellItem(): Array<model.CellItemDto> {
            var self = this;
            var firstEl = self.elementSettings[0];
            var secondEl = self.elementSettings[1];
            var thirdEl = self.elementSettings[2];
            var result = new Array<model.CellItemDto>();
            thirdEl.itemList.forEach((item3) => {
                if (self.datasourceMap[item3.uuid]) {
                    self.datasourceMap[item3.uuid].forEach((vm) => {
                         secondEl.itemList.forEach((item2) => {
                            var dto = <model.CellItemDto> {};
                            dto.element1Id = vm['uuid']();
                            dto.element2Id = item2.uuid;
                            dto.element3Id = item3.uuid;
                            dto.amount = vm[item2.uuid]();
                            result.push(dto); 
                        })
                    })
                }
            })
            return result;
        }

        /**
         * Paste data from excel.
         */
        pasteFromExcel(): void {
            // Do parsing.
            return;
        }

        /**
         * Build element 3 information.
         */
        private buildElement3Infomation(): void {
            var self = this;
            // Build elements 3 item info.
            self.element3Items(_.map(self.elementSettings[2].itemList, (item) => {
                (<any>item).name = item.displayName;
                return item;
            }));
            self.selectedElement3ItemId(self.elementSettings[2].itemList[0].uuid);
            self.element3Name(self.elementSettings[2].demensionName);
        }
        
         /**
         * Init ig grid.
         */
        private initIgGrid(data: Array<ItemViewModel>): void {
            var self = this;
            ko.cleanNode($('#dataTable').get(0));

            // Regenerate columns and columsn settings.
            var columns: Array<any> = [];
            var columnSettings: Array<any> = [];

            // Fixed part.
            columns.push({ headerText: 'UUID', dataType: 'string', key: 'uuid', width: '100px', hidden: true});
            columns.push({ headerText: self.elementSettings[0].demensionName, dataType: 'string', key: 'name', width: '100px', columnCssClass: "bgIgCol"});
            columnSettings.push({columnKey: 'uuid', readOnly: true});
            columnSettings.push({columnKey: 'name', readOnly: true});

            // Dynamic part.
            var secondDemensionElements = self.elementSettings[1];
            var mergeColumn: {headerText: string, group: Array<any>} = {headerText: secondDemensionElements.demensionName, group: []};
            _.forEach(secondDemensionElements.itemList, (item) => {
                var colName =  item.displayName;
                mergeColumn.group.push({ headerText: colName, dataType: 'number', key: item.uuid, width: '100px', columnCssClass: "halign-right"});
                columnSettings.push({
                    columnKey: item.uuid,
                    readOnly: false,
                    editorProvider: new (<any>$.ig).NtsNumberEditor(),
                    editorOptions: {
                        constraint: 'WtValue',
                        option: {
                        },
                        required: true
                    }
                });
            });
            columns.push(mergeColumn);

            // Set data soruce.
            self.igGridDataSource(data);

            // IgGrid
            self.igGrid = ko.observable({
                dataSource: self.igGridDataSource,
                width: '700px',
                primaryKey: 'uuid',
                height: '250px',
                features: [
                    {
                        name: 'MultiColumnHeaders'
                    },
                    {
                        name: 'ColumnFixing',
                        fixingDirection: 'left',
                        columnSettings: [
                            {
                                columnKey: 'name',
                                isFixed: true
                            }
                        ]
                    },
                    {
                        name: 'Updating',
                        editMode: 'row',
                        enableAddRow: false,
                        excelNavigatorMode: false,
                        enableDeleteRow: false,
                        columnSettings: columnSettings,
                    }
                ],
                autoCommit: true,
                columns: columns
            });

            // Bind.
            if (mergeColumn.group.length > 0) {
                ko.applyBindingsToNode($('#dataTable').get(0), { igGrid: self.igGrid });
            }
        }
    }

    /**
     * Item view model.
     */
    class ItemViewModel {
        [key: string]: KnockoutObservable<any>;

        /**
         * Constructor.
         */
        constructor(type: number, item: model.ItemDto) {
            var self = this;
            self['uuid'] = ko.observable(item.uuid);
            self['name'] = ko.observable(item.displayName);
        }
    }
}