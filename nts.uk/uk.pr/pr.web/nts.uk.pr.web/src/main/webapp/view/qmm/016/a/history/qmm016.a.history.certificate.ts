module nts.uk.pr.view.qmm016.a.history {
    /**
     * For one demension view.
     */
    export class CertificateViewModel extends base.BaseHistoryViewModel {
        igGrid: any;
        igGridDataSource: KnockoutObservableArray<ItemViewModel>;
        certificateGroupModel: model.CertifyGroupOutModel;
        certToGroupMap: {[index: string]: model.CertifyGroupDto};
        
        constructor(history: model.WageTableHistoryDto) {
            super('history/certificate.xhtml', history);
            var self = this;
            self.igGridDataSource = ko.observableArray<ItemViewModel>([]);
            self.certToGroupMap = <any>[];
        }

        /**
         * On load processing.
         */
        onLoad(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();

            // Load certificate info.
            service.instance.loadCertificate().done(res => {
                self.certificateGroupModel = res;
                _.forEach(self.certificateGroupModel.certifyGroups, (group) => {
                    _.forEach(group.certifyItems, (cert) => {
                        self.certToGroupMap[cert.code] = group;
                    })
                })

                // Already have data.
                var vmList = new Array<ItemViewModel>();
                if (self.history.valueItems && self.history.valueItems.length > 0) {
                    // Map uuid to item.
                    var codeToItemMap:{[index: string] : model.ItemDto} = <any>[];
                    var uuidToValueMap:{[index: string] : model.CellItemDto} = <any>[];
                    _.forEach(self.elementSettings[0].itemList, (item) => {
                        codeToItemMap[item.referenceCode] = item;
                    })
                    _.forEach(self.history.valueItems, (vi) => {
                        uuidToValueMap[vi.element1Id] = vi;
                    })
    
                    // Map values item. 
                    _.forEach(self.certificateGroupModel.certifyGroups, (group) => {
                        _.forEach(group.certifyItems, (cert) => {
                            var item = codeToItemMap[cert.code];
                            var value = uuidToValueMap[item.uuid];
                            var vm = new ItemViewModel(group, item);
                            vm.amount(value.amount);
                            vmList.push(vm);
                        })
                    })
                }

                // Init ig girid.
                dfd.resolve();
                self.initIgGrid(vmList);
            })

            // Ret.
            return dfd.promise();
        }

        /**
         * Init ig grid.
         */
        initIgGrid(data: Array<ItemViewModel>): void {
            var self = this;

            // Clean node.
            ko.cleanNode($('#dataTable').get(0));

            // IgGrid
            self.igGridDataSource(data);
            self.igGrid = ko.observable({
                dataSource: self.igGridDataSource,
                width: '100%',
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
                                columnKey: 'groupName',
                                readOnly: true
                            },
                            {
                                columnKey: 'name',
                                readOnly: true
                            },
                            {
                                columnKey: 'amount',
                                readOnly: false,
                                editorProvider: new (<any>$.ig).NtsNumberEditor(),
                                editorOptions: {
                                    constraint: 'WtValue',
                                    option: {
                                    },
                                    required: true
                                }
                            },
                            {
                                columnKey: 'groupCalTypeText',
                                readOnly: true
                            }
                        ],
                    } /*
                    {
                        name: 'CellMerging',
                        initialState: 'merged',
                        cellsMerged: (evt: any, ui: any) => {
                            $('.halign-right', ui.row).removeClass('ui-iggrid-mergedcellstop ui-iggrid-mergedcell');
                        }
                        
                    }*/
                ],
                autoCommit: true,
                columns: [
                    { headerText: 'UUID', dataType: 'string', key: 'uuid', hidden: true, columnCssClass: 'bgIgCol'},
                    { headerText: 'グルップ', dataType: 'string', key: 'groupName', width: '20%', columnCssClass: 'bgIgCol'},
                    { headerText: '資格名称', dataType: 'string', key: 'name', width: '20%', columnCssClass: 'bgIgCol'},
                    { headerText: '値', dataType: 'number', key: 'amount', width: '20%', columnCssClass: 'halign-right'},
                    { headerText: '同一グループ内で複数の資格を取得している場合の支給方法', dataType: 'string', key: 'groupCalTypeText', width: '20%'}
                ]

                /**
                 * Rows rendered.
                 * Clear no need merged cell.
                 */
               /* rendered: (evt: any, ui: any) => {
                    $('.halign-right', this).removeClass('ui-iggrid-mergedcellstop ui-iggrid-mergedcell');
                }*/
            });

            // Bind.
            if (data && data.length > 0) {
                ko.applyBindingsToNode($('#dataTable').get(0), { igGrid: self.igGrid });
            }
        }

        /**
         * On refresh element.
         */
        onRefreshElement(): void {
            var self = this;

            // Map uuid to item.
            var codeToItemMap:{[index: string] : model.ItemDto} = <any>[];
            _.forEach(self.elementSettings[0].itemList, (item) => {
                codeToItemMap[item.referenceCode] = item;
            })

            // Map values item.
            var vmList = new Array<ItemViewModel>(); 
            _.forEach(self.certificateGroupModel.certifyGroups, (group) => {
                _.forEach(group.certifyItems, (cert) => {
                    var item = codeToItemMap[cert.code];
                    var vm = new ItemViewModel(group, item);
                    vmList.push(vm);
                })
            })

            // Update source
            self.initIgGrid(vmList);
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
        groupName: string;
        name: string;
        amount: KnockoutObservable<number>;
        groupCalTypeText: string;

        /**
         * Constructor.
         */
        constructor(group: model.CertifyGroupDto, item: model.ItemDto) {
            var self = this;
            self.uuid = item.uuid;
            self.groupName = group.name;
            self.name = item.displayName;
            self.amount = ko.observable(0);
            self.groupCalTypeText = group.multiApplySet == 0 ? '一番高い手当を1つだけ支給する' : '複数該当した金額を加算する';
        }
    }
}