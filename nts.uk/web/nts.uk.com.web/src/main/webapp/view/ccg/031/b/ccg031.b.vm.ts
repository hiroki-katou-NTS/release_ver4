module ccg031.b.viewmodel {
    import model = ccg.model;
    import util = nts.uk.util;
    import windows = nts.uk.ui.windows;
    import errors = nts.uk.ui.errors;
    import resource = nts.uk.resource;
    export class ScreenModel {
        // PGType
        pgType: number;
        // Position
        positionRow: KnockoutObservable<number>;
        positionColumn: KnockoutObservable<number>;
        // TopPage Part Type
        listPartType: KnockoutObservableArray<any>;
        selectedPartType: KnockoutObservable<any>;
        //TopPage Part
        allPart: KnockoutObservableArray<model.TopPagePartDto>;
        listPart: KnockoutObservableArray<model.TopPagePartDto>;
        selectedPartID: KnockoutObservable<string>;
        selectedPart: KnockoutObservable<model.TopPagePartDto>;
        listPartColumn: any;
        // External Url
        isExternalUrl: KnockoutObservable<boolean>;
        urlWidth: KnockoutObservable<number>;
        urlHeight: KnockoutObservable<number>;
        url: KnockoutObservable<string>;
        // UI Binding
        instructionText: KnockoutObservable<string>;
        constructor() {
            var self = this;
            // PGType
            self.pgType = 0;
            // Position
            self.positionRow = ko.observable(null);
            self.positionColumn = ko.observable(null);
            // TopPage Part
            self.listPartType = ko.observableArray([]);
            self.selectedPartType = ko.observable(null);
            self.selectedPartType.subscribe((value) => { self.filterPartType(value); });
            self.allPart = ko.observableArray([]);
            self.listPart = ko.observableArray([]);
            self.selectedPartID = ko.observable(null);
            self.selectedPartID.subscribe((value) => { self.changeSelectedPart(value); });
            self.selectedPart = ko.observable(null);
            self.listPartColumn = [
                { headerText: "ID", key: "topPagePartID", dataType: "string", hidden: true },
                { headerText: "コード", key: "code", dataType: "string", width: 50 },
                { headerText: "名称", key: "name", dataType: "string" },
            ];
            // External Url
            self.isExternalUrl = ko.observable(false);
            self.urlWidth = ko.observable(null);
            self.urlHeight = ko.observable(null);
            self.url = ko.observable(null);
            // UI Binding
            self.instructionText = ko.observable('');
        }

        /** Start Page */
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            // Shared
            self.pgType = windows.getShared("pgtype");
            self.positionRow(windows.getShared("size").row);
            self.positionColumn(windows.getShared("size").column);
            // Get Type and Part
            service.findAll(self.pgType).done((data: any) => {
                // Binding TopPage Part Type
                self.listPartType(data.listTopPagePartType);
                // Binding TopPage Part
                self.allPart(data.listTopPagePart);
                // Default value
                self.selectedPartType(0);
                self.selectFirstPart();
                dfd.resolve();
            }).fail((res) => {
                dfd.fail();
            });
            return dfd.promise();
        }

        /** Submit Dialog */
        submitDialog(): void {
            var self = this;
            $(".nts-validate").trigger("validate");
            _.defer(() => {
                //if (!errors.hasError()) {
                if ($(".nts-validate").ntsError("hasError")) {
                    var placement: model.Placement = self.buildReturnPlacement();
                    windows.setShared("placement", placement, false);
                    windows.close();
                }
            });
        }

        /** Close Dialog */
        closeDialog(): void {
            windows.close();
        }
        
        /** Filter by Type */
        private filterPartType(partType: number): void {
            var isExternalUrl: boolean = (partType === 3);
            this.isExternalUrl(isExternalUrl);
            if (isExternalUrl !== true) {
                if (nts.uk.ui._viewModel)
                    errors.clearAll();
                var listPart = _.filter(this.allPart(), ['type', partType]);
                this.listPart(listPart);
                this.isExternalUrl(isExternalUrl);
                this.selectFirstPart();
            }
            // Instruction Text
            if (partType === 0)
                this.instructionText("CCG031_17");
            if (partType === 1)
                this.instructionText("CCG031_24");
            if (partType === 2)
                this.instructionText("CCG031_19");
        }
        
        /** Change Selected Part */
        private changeSelectedPart(partID: string): void {
            var selectedPart: model.TopPagePartDto = _.find(this.allPart(), ['topPagePartID', partID]);
            this.selectedPart(selectedPart);
        }
        
        /** Select first Part */
        private selectFirstPart(): void {
            var firstPart: model.TopPagePartDto = _.head(this.listPart());
            (firstPart !== undefined) ? this.selectedPartID(firstPart.topPagePartID) : this.selectedPartID(null);
        }

        /** Build a return Placement for LayoutSetting */
        private buildReturnPlacement(): model.Placement {
            var self = this;
            // Default is External Url
            var name: string = "";
            var width: number = self.urlWidth();
            var height: number = self.urlHeight();
            var topPagePartID: string = "";
            var topPagePartType: number = null;
            var url: string = self.url();
            
            // In case is TopPagePart
            if (self.selectedPartType() !== 3) {
                name = self.selectedPart().name;
                width = self.selectedPart().width;
                height = self.selectedPart().height;
                topPagePartID = self.selectedPart().topPagePartID;
                topPagePartType = self.selectedPart().type;
                url = "";
            }
            
            var placement: model.Placement = new model.Placement(
                util.randomId(), name,
                self.positionRow(), self.positionColumn(),
                width, height, url, topPagePartID, topPagePartType);
            return placement;
        }
        
    }

}