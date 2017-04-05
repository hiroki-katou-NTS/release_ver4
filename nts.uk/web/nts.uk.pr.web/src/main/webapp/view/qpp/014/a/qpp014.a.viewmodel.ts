// TreeGrid Node
module qpp014.a.viewmodel {
    export class ScreenModel {
        viewmodelb = new qpp014.b.viewmodel.ScreenModel();
        viewmodeld = new qpp014.d.viewmodel.ScreenModel();
        viewmodelg = new qpp014.g.viewmodel.ScreenModel();
        viewmodelh = new qpp014.h.viewmodel.ScreenModel();

        //viewmodel A
        a_SEL_001_items: KnockoutObservableArray<shr.viewmodelbase.PayDayProcessing>;
        a_SEL_001_itemSelected: KnockoutObservable<any>;

        constructor() {
            var self = this;
            $('.func-btn').css('visibility', 'hidden');
            $('#screenB').css('display', 'none');

            //viewmodel A
            self.a_SEL_001_items = ko.observableArray([]);
            self.a_SEL_001_itemSelected = ko.observable();
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.findAll().done(function() {
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise();
        }

        /**
         * get data from table PAYDAY_PROCESSING
         */
        findAll(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            //get data with BONUS_ATR(PAY_BONUS_ATR) = 0 
            qpp014.a.service.findAll(0)
                .done(function(data) {
                    if (data.length > 0) {
                        self.a_SEL_001_items(data);
                        self.a_SEL_001_itemSelected(self.a_SEL_001_items()[0]);
                    } else {
                        nts.uk.ui.dialog.alert("対象データがありません。");//ER010
                    }
                    dfd.resolve();
                }).fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }

        /**
         * go to next screen
         */
        nextScreen(): void {
            $("#screenA").css("display", "none");
            $("#screenB").css("display", "");
            $("#screenB").ready(function() {
                $(".func-btn").css("visibility", "visible");
            });
        }

        /**
         * back to previous screen
         */
        backScreen(): void {
            $("#screenB").css("display", "none");
            $("#screenA").css("display", "");
            $(".func-btn").css("visibility", "hidden");
        }

        /**
         * go to screen J
         */
        goToScreenJ(): void {
            nts.uk.ui.windows.sub.modal("/view/qpp/014/j/index.xhtml", { title: "振込チェックリスト", dialogClass: "no-close" }).onClosed(function() {
            });
        }
    }
};
