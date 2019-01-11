module nts.uk.pr.view.qmm003.d.viewmodel {
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import setShared = nts.uk.ui.windows.setShared;
    import constants = qmm003.share.constants;

    export class ScreenModel {
        
        items2: KnockoutObservableArray<any>;
        selectedCodes: KnockoutObservableArray<string>;
        headers: any;
        listRegions: Array<any> = constants.listRegions;
        listPrefectures: Array<any>;
        listRsdTaxPayeeNodes: Array<Node> = [];

        constructor() {
            let self = this;
            self.items2 = ko.observableArray([]);
            self.selectedCodes = ko.observableArray([]);
            self.headers = ko.observableArray([getText("QMM003_9")]);
            self.listPrefectures = constants.listPrefectures;
        }
        
        startPage(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            block.invisible();
            service.getAllResidentTaxPayee().done((data: Array<any>) => {
                let listNodes = [];
                self.listRegions.forEach(r => {
                    let regionNode = new Node(r.code, r.name, [], 0);
                    let prefectures = self.listPrefectures.filter(pr => {return pr.region == r.code});
                    let prefectureNodes = [];
                    prefectures.forEach(pr => {
                        let prefectureNode = new Node(pr.code < 10 ? "_0" + pr.code : "_" + pr.code, pr.name, [], 1);
                        if (data.length > 0) {
                            let residentTaxPayees = data.filter(d => {return d.prefectures == pr.code});
                            let residentNodes = _.map(residentTaxPayees, rs => {
                                let node = new Node(rs.code, rs.name, [], 2);
                                self.listRsdTaxPayeeNodes.push(node);
                                return node;
                            });
                            prefectureNode.children = residentNodes;
                        }
                        prefectureNodes.push(prefectureNode);
                        self.listRsdTaxPayeeNodes.push(prefectureNode);
                    });
                    regionNode.children = prefectureNodes;
                    listNodes.push(regionNode);
                    self.listRsdTaxPayeeNodes.push(regionNode);
                });
                self.items2(listNodes);
                dfd.resolve();
            }).fail(error => {
                alertError(error);
                dfd.reject();
            }).always(() => {
                block.clear();
            });
            return dfd.promise();
        }

        deleteList() {
            let self = this;
            block.invisible();
            let selecteds = [];
            self.selectedCodes().forEach(c => {
                let node = _.find(self.listRsdTaxPayeeNodes, n => {return n.code == c});
                if (node.level == 2) selecteds.push(c);
                if (node.level == 1) {
                    node.children.forEach(child => {
                        selecteds.push(child.code);
                    });
                }
                if (node.level == 0) {
                    node.children.forEach(child => {
                        child.children.forEach(child2 => {
                            selecteds.push(child2.code);
                        });
                    });
                }
            });
            service.checkBeforeDelete(selecteds).done(() => {
                confirm({ messageId: "Msg_18" }).ifYes(() => {
                    service.removeList(selecteds).done(() => {
                        info({ messageId: "Msg_16" }).then(() => {
                            nts.uk.ui.windows.close();
                        });
                    }).fail(error => {
                        alertError(error);
                    }).always(() => {
                        block.clear();
                    });
                }).ifNo(() => {
                });
            }).fail(error => {
                alertError(error);
            }).always(() => {
                block.clear();
            });
        }

        cancel() {
            setShared("QMM003DCancel", true);
            nts.uk.ui.windows.close();
        }

    }

    class Node {
        code: string;
        name: string;
        nodeText: string;
        children: any;
        level: number; //0: region, 1: prefecture, 2: resident
        
        constructor(code: string, name: string, children: Array<Node>, level?: number) {
            let self = this;
            self.code = code;
            self.name = name;
            self.nodeText = level == 2 ? _.escape(self.code + ' ' + self.name) : _.escape(self.name);
            self.children = children;
            if (level != null) self.level = level;
        }
    }
    
}
