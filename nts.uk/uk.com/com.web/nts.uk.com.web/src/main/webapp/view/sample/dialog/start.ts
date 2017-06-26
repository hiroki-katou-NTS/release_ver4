__viewContext.ready(function() {
    class ScreenModel {

        constructor() {
            var self = this;
        }

        Alert() {
            nts.uk.ui.dialog.alert({ messageId: "Msg_3" });
        }

        AlertInfor() {
            nts.uk.ui.dialog.info({ messageId: "Msg_3" });
        }
        AlertError() {
            nts.uk.ui.dialog.alertError({messageId: "Msg_175", messageParams: ["1", "2"]})
        }
        Confirm() {
            nts.uk.ui.dialog.confirm({ messageId: "Msg_3" });
        }
    }

    this.bind(new ScreenModel());

});