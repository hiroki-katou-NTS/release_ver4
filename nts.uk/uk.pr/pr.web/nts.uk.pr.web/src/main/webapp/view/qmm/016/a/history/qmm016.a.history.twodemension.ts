module nts.uk.pr.view.qmm016.a.history {
    /**
     * For two demension view.
     */
    export class TwoDemensionViewModel extends base.BaseHistoryViewModel {
        igGrid: any;
        igGridDataSource: KnockoutObservableArray<ItemViewModel>;

        constructor(history: model.WageTableHistoryDto) {
            super('history/twodemension.xhtml', history);
            this.igGridDataSource = ko.observableArray<ItemViewModel>([]);
        }

        /**
         * On load init ig grid.
         */
        onLoad(): JQueryPromise<any> {
            var self = this;

            // Build first data source.
            var history = self.history;
            if (history.valueItems && history.valueItems.length > 0) {
                var element = history.elements[0];
                var secondElement = history.elements[1];
                var itemVmList = _.map(element.itemList, (item) => {
                    var vm = new ItemViewModel(element.type, item);
                    
                    var valueItemMap: {[index: string]: model.CellItemDto} = <any>[];
                    _.filter(history.valueItems, (vi) => { return vi.element1Id == item.uuid; })
                        .forEach((vi) => {valueItemMap[vi.element2Id] = vi});
                    secondElement.itemList.forEach((item2) => {
                        vm[item2.uuid] = ko.observable(valueItemMap[item2.uuid].amount);
                    })
                    return vm;
                })
            }

            // Build grid.
            self.initIgGrid(itemVmList);

            // Ret.
            return $.Deferred().resolve().promise();
        }

        /**
         * On refresh element.
         */
        onRefreshElement(): void {
            var self = this;

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

            // Recreate ig grid.
            self.initIgGrid(dataSource);

        }

        /**
         * Get setting cell item.
         */
        getCellItem(): Array<model.CellItemDto> {
            var self = this;
            var firstEl = self.elementSettings[0];
            var secondEl = self.elementSettings[1];
            var result = new Array<model.CellItemDto>();
            self.igGridDataSource().forEach((data) => {
                secondEl.itemList.forEach((item) => {
                    var dto = <model.CellItemDto> {};
                    dto.element1Id = data['uuid']();
                    dto.element2Id = item.uuid;
                    dto.amount = data[item.uuid]();
                    result.push(dto); 
                })
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
         * Init ig grid.
         */
        private initIgGrid(data:  Array<ItemViewModel>): void {
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

            // IgGrid
            self.igGridDataSource(data);
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