module nts.uk.at.view.kmk011.a {
    import serviceScreenH = nts.uk.at.view.kmk011.h.service;
    import setShared = nts.uk.ui.windows.setShared;

    export module viewmodel {

        export class ScreenModel {
            constructor() {
                var self = this;
            }

            /**
             * open DivergenceTimeSetting (init screen b)
             */
            public openDivergenceTimeSetting(): void {
                nts.uk.request.jump("/view/kmk/011/b/index.xhtml");
            }

            /**
             * open Creating divergence reference time page (init screen d)
             */
            public openCreatingDivergenceRefTimePage(): void {
                nts.uk.request.jump("/view/kmk/011/d/index.xhtml");
            }

            /**
             * open dialog H
             */
            public openUsageUnitSettingDialog(): void {
                serviceScreenH.find().done((value) => {
                    setShared("selectWorkTypeCheck", value.workTypeUseSet);
                    nts.uk.ui.windows.sub.modal("/view/kmk/011/h/index.xhtml").onClosed(function() { });
                })

            }

            public exportExcel(): void {
                var self = this;
                nts.uk.ui.block.grayout();

                serviceScreenH.saveAsExcel().done(function() {
                }).fail(function(error) {
                    nts.uk.ui.dialog.alertError({ messageId: error.messageId });
                }).always(function() {
                    nts.uk.ui.block.clear();
                });
            }

        }

    }
}