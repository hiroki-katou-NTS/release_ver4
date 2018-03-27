module nts.uk.at.view.kmk011.h {

    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    import DivergenceReferenceTimeUsageUnitDto = nts.uk.at.view.kmk011.h.model.DivergenceReferenceTimeUsageUnit;
    export module viewmodel {
        export class ScreenModel {
            selectWorkTypeCheck: KnockoutObservable<boolean>;

            constructor() {
                let _self = this;
                _self.selectWorkTypeCheck = ko.observable(true);
            }

            public start_page(): JQueryPromise<any> {
                let _self = this;
                var dfd = $.Deferred<any>();
                
                service.find().done(function(value: any) {
                    if (value != null) {
                        _self.selectWorkTypeCheck(value.workTypeUseSet)
                    }
                });
                dfd.resolve();
                return dfd.promise();
            }

            private closeSaveDialog(): JQueryPromise<any> {
                let _self = this;
                var dfd = $.Deferred<any>();
                var workTypeUseSet = _self.convertBoolToNum(_self.selectWorkTypeCheck())
                var data = new DivergenceReferenceTimeUsageUnitDto(workTypeUseSet);

                service.save(data).done(() => {
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                        dfd.resolve();
                        nts.uk.ui.windows.close();
                    });
                });

                return dfd.promise();
            }

            private closeDialog(): void {
                nts.uk.ui.windows.close();
            }
            private convertBoolToNum(value: boolean) {
                if (value) return 1;
                return 0;
            }

        }
    }
}
