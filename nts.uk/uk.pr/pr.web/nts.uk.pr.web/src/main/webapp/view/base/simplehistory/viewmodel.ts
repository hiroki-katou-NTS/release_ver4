module nts.uk.pr.view.base.simplehistory {
    export module viewmodel {

        /**
         * Simple history screen options.
         */
        export interface SimpleHistoryScreenOptions<M extends model.MasterModel<H>, H extends model.HistoryModel> {
            functionName: string;
            removeMasterOnLastHistoryRemove?: boolean;
            service: service.Service<M, H>
        }

        /**
         * Base screen model for simple history.
         */
        export abstract class ScreenBaseModel<M extends model.MasterModel<H>, H extends model.HistoryModel> {
            // Service.
            service: service.Service<M, H>;

            // Whether or not in new mode.
            protected isNewMode: KnockoutObservable<boolean>;

            // Master history.
            masterHistoryList: Array<M>;

            // Data source.
            masterHistoryDatasource: KnockoutObservableArray<Node>;

            // Selected history uuid.
            selectedNode: KnockoutObservable<Node>;
            selectedHistoryUuid: KnockoutObservable<string>;
            igGridSelectedHistoryUuid: KnockoutObservable<string>;

            // Flag
            private canUpdateHistory: KnockoutObservable<boolean>;
            private canAddNewHistory: KnockoutObservable<boolean>;
            private options: SimpleHistoryScreenOptions<M, H>;

            isClickHistory: KnockoutObservable<boolean>;
            /**
             * Constructor.
             */
            constructor(options: SimpleHistoryScreenOptions<M, H>) {
                var self = this;

                // Set.
                self.options = options;
                self.service = options.service;

                // Init.
                self.isNewMode = ko.observable(true);
                self.masterHistoryDatasource = ko.observableArray([]);

                // On searched result.
                self.igGridSelectedHistoryUuid = ko.observable(undefined);
                self.selectedHistoryUuid = ko.observable(undefined);
                self.selectedNode = ko.observable(undefined);
                self.isClickHistory = ko.observable(false);

                // Can update history flag.
                self.canUpdateHistory = ko.computed(() => {
                    return self.selectedNode() && self.selectedHistoryUuid() != undefined;
                })
                self.canAddNewHistory = ko.computed(() => {
                    return self.selectedNode() != null;
                })

                self.igGridSelectedHistoryUuid.subscribe(id => {
                    var inlineFunc = () => {
                        // Not select.
                        if (!id) {
                            self.selectedNode(undefined);
                            return;
                        }

                        var selectedNode = self.getNode(id);
                        // History node.
                        if (!selectedNode.isMaster) {
                            self.isNewMode(false);
                            self.selectedHistoryUuid(selectedNode.id);
                            if (nts.uk.ui._viewModel) {
                                self.clearErrors();
                            }
                            self.onSelectHistory(id);
                        } else {
                            // Parent node.
                            self.onSelectMaster(id);
                        }
                        self.selectedNode(selectedNode);
                    };
                    if (self.selectedHistoryUuid() &&
                        id != self.selectedHistoryUuid()) {
                        self.confirmDirtyAndExecute(inlineFunc, () => {
                            self.igGridSelectedHistoryUuid(self.selectedHistoryUuid());
                        })
                    } else {
                        if (!self.selectedHistoryUuid()) {
                            inlineFunc();
                        }
                    }
                })
            }

            /**
             * Start page.
             */
            startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                $.when(self.loadMasterHistory(), self.start()).done((res1, res2) => {
                    if (!self.masterHistoryList || self.masterHistoryList.length == 0) {
                        // Set new mode.
                        self.isNewMode(true);
                        self.onRegistNew();
                    } else {
                        // Not new mode and select first history.
                        self.isNewMode(false);
                        if (self.masterHistoryDatasource()[0].childs &&
                            self.masterHistoryDatasource()[0].childs.length > 0) {
                            self.igGridSelectedHistoryUuid(self.masterHistoryDatasource()[0].childs[0].id);
                        } else {
                            self.igGridSelectedHistoryUuid(self.masterHistoryDatasource()[0].id);
                        }
                    }

                    // resolve.
                    dfd.resolve();
                }).fail(dfd.reject);

                return dfd.promise();
            }

            /**
             * Load master history.
             */
            loadMasterHistory(): JQueryPromise<Array<M>> {
                var self = this;
                var dfd = $.Deferred();

                self.service.loadMasterModelList().done(res => {
                    var nodeList = _.map(res, master => {
                        // Current node.
                        var masterNode: Node = {
                            id: master.code,
                            searchText: master.code + ' ' + master.name,
                            nodeText: master.code + ' ' + master.name,
                            isMaster: true,
                            data: master
                        };

                        // Child node.
                        var masterChild = _.map(master.historyList, history => {
                            var node: Node = {
                                id: history.uuid,
                                searchText: '',
                                nodeText: nts.uk.time.formatYearMonth(history.start) + '~' + nts.uk.time.formatYearMonth(history.end),
                                isMaster: false,
                                parent: masterNode,
                                data: history
                            };
                            return node;
                        })

                        masterNode.childs = masterChild;
                        return masterNode;
                    })

                    self.masterHistoryList = res;
                    self.masterHistoryDatasource(nodeList);
                    dfd.resolve();
                })

                return dfd.promise();
            }

            /**
             * Confirm dirty state and execute function.
             */
            confirmDirtyAndExecute(functionToExecute: () => void, functionToExecuteIfNo?: () => void) {
                var self = this;
                if (self.isDirty()) {
                    nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                        functionToExecute();
                    }).ifNo(function() {
                        if (functionToExecuteIfNo) {
                            functionToExecuteIfNo();
                        }
                    });
                } else {
                    functionToExecute();
                }
            }

            /**
             * Start regist new.
             */
            registBtnClick(): void {
                var self = this;
                self.confirmDirtyAndExecute(() => {
                    self.isNewMode(true);
                    self.onRegistNew();
                    if (nts.uk.ui._viewModel) {
                        self.clearErrors();
                    }

                    // Clear select history uuid.
                    self.igGridSelectedHistoryUuid(undefined);
                });
            }

            /**
             * Confirm dirty check.
             * Override it by your self.
             */
            abstract isDirty(): boolean;

            /**
             * Save current master and history data.
             */
            saveBtnClick(): void {
                var self = this;
                self.onSave().done((uuid) => {
                    self.loadMasterHistory().done(() => {
                        self.igGridSelectedHistoryUuid(uuid);
                    })
                }).fail(() => {
                    // Do nothing.
                })
            }

            /**
             * Open add new history dialog.
             */
            addNewHistoryBtnClick(): void {
                var self = this;
                self.confirmDirtyAndExecute(() => {
                    var currentNode = self.selectedNode();
                    var latestNode = currentNode.isMaster ? _.head(currentNode.childs) : _.head(currentNode.parent.childs);
                    var newHistoryOptions: newhistory.viewmodel.NewHistoryScreenOption = {
                        name: self.options.functionName,
                        master: currentNode.isMaster ? currentNode.data : currentNode.parent.data,
                        lastest: latestNode ? latestNode.data : undefined,

                        // Copy.
                        onCopyCallBack: (data) => {
                            var dfd = $.Deferred<any>();
                            self.service.createHistory(data.masterCode, data.startYearMonth, true)
                                .done(h => {
                                    self.reloadMasterHistory(h.uuid);
                                    dfd.resolve();
                                }).fail(res => {
                                    dfd.reject(res);
                                });
                            return dfd.promise();
                        },

                        // Init.
                        onCreateCallBack: (data) => {
                            var dfd = $.Deferred<any>();
                            self.service.createHistory(data.masterCode, data.startYearMonth, false)
                                .done(h => {
                                    self.reloadMasterHistory(h.uuid);
                                    dfd.resolve();
                                }).fail(res => {
                                    dfd.reject(res);
                                });
                            return dfd.promise();
                        }
                    };
                    nts.uk.ui.windows.setShared('options', newHistoryOptions);
                    var ntsDialogOptions = {
                        title: nts.uk.text.format('{0}の登録 > 履歴の追加', self.options.functionName),
                        dialogClass: 'no-close'
                    };
                    nts.uk.ui.windows.sub.modal('/view/base/simplehistory/newhistory/index.xhtml', ntsDialogOptions);
                });
            }

            /**
             * Open update history btn.
             */
            updateHistoryBtnClick(): void {
                var self = this;
                var currentNode = self.getNode(self.selectedHistoryUuid());
                var newHistoryOptions: updatehistory.viewmodel.UpdateHistoryScreenOption = {
                    name: self.options.functionName,
                    master: currentNode.parent.data,
                    history: currentNode.data,
                    removeMasterOnLastHistoryRemove: self.options.removeMasterOnLastHistoryRemove,

                    // Delete callback.
                    onDeleteCallBack: (data) => {
                        self.service.deleteHistory(data.masterCode, data.historyId).done(() => {
                            self.reloadMasterHistory(null);
                        });
                    },

                    // Update call back.
                    onUpdateCallBack: (data) => {
                        var dfd = $.Deferred();
                        self.service.updateHistoryStart(data.masterCode, data.historyId, data.startYearMonth).done(() => {
                            self.reloadMasterHistory(self.selectedHistoryUuid());
                            dfd.resolve();
                        }).fail(res => {
                            dfd.reject(res);
                        });
                        return dfd.promise();
                    }
                };
                nts.uk.ui.windows.setShared('options', newHistoryOptions);
                var ntsDialogOptions = {
                    title: nts.uk.text.format('{0}の登録 > 履歴の編集', self.options.functionName),
                    dialogClass: 'no-close'
                };
                nts.uk.ui.windows.sub.modal('/view/base/simplehistory/updatehistory/index.xhtml', ntsDialogOptions);
            }

            /**
             * Reload master history then set.
             */
            reloadMasterHistory(uuid: string) {
                var self = this;
                self.loadMasterHistory().done(() => {
                    self.selectedHistoryUuid(undefined);
                    if (uuid) {
                        self.igGridSelectedHistoryUuid(uuid);
                        self.igGridSelectedHistoryUuid.valueHasMutated();
                    } else {
                        if (self.masterHistoryList.length > 0) {
                            if (!_.isEmpty(self.masterHistoryList[0].historyList)) {
                                self.igGridSelectedHistoryUuid(self.masterHistoryList[0].historyList[0].uuid);
                                self.igGridSelectedHistoryUuid.valueHasMutated();
                            } else {
                                self.igGridSelectedHistoryUuid(self.masterHistoryList[0].code);
                                self.igGridSelectedHistoryUuid.valueHasMutated();
                            }
                        }
                    }
                    // Set new mode if masterHistoryList has 0 element.
                    if (!self.masterHistoryList || self.masterHistoryList.length == 0) {
                        self.isNewMode(true);
                        self.selectedNode(null);
                        self.onRegistNew();
                    }
                })
            }

            /**
             * Internal start for each page.
             * Override if you need to do any thing.
             */
            start(): JQueryPromise<any> {
                var dfd = $.Deferred();
                dfd.resolve();
                return dfd.promise();
            }

            /**
             * On select history.
             */
            abstract onSelectHistory(uuid: string): void;

            /**
             * On select master data.
             */
            onSelectMaster(code: string): void {
                // Override your self if need.
            }

            /**
             * Clear errors.
             */
            clearErrors(): void {
                // Override it by yourself.
            }

            /**
             * On regist new.
             */
            abstract onRegistNew(): void;

            /**
             * On save click.
             * Do validate your self.
             * Set error your self.
             * @return resolve with uuid of saved history or failed with any. 
             */
            abstract onSave(): JQueryPromise<string>;

            /**
             * Get node using id.
             */
            private getNode(id: string): Node {
                var self = this;
                var nodeList = _.flatMap(self.masterHistoryDatasource(), (node) => {
                    var newArr = new Array<Node>();
                    newArr.push(node);
                    if (node.childs) {
                        newArr = newArr.concat(node.childs);
                    }
                    return newArr;
                })
                return _.first(_.filter(nodeList, (node) => {
                    return node.id == id;
                }));
            }
        }

        /**
         * Node interface.
         */
        export interface Node {
            id: string;
            searchText: string;
            nodeText: string;
            isMaster: boolean;
            parent?: Node;
            childs?: Node[];
            data?: any;
        }
    }
}