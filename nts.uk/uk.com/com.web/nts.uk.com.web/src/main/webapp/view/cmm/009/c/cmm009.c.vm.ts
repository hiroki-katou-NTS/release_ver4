module cmm009.c.viewmodel {
    import option = nts.uk.ui.option;
    export class ScreenModel {
        selectStartYm: KnockoutObservable<string>;
        C_INP_MEMO: KnockoutObservable<string>;
        valueSel001: KnockoutObservable<string>;
        startYmHis: KnockoutObservable<number>;
        //timeEditorOption: KnockoutObservable<any>;

        //---radio        
        isRadioCheck: KnockoutObservable<number>;
        itemsRadio: KnockoutObservableArray<any>;
        enable: KnockoutObservable<boolean>;
        // -- check data ban đầu
        data: any;
        object: KnockoutObservable<viewmodel.Object>;
        startDateofHisFromScreenatoString: any;

        // time editor
        yearmonthdayeditor: any;
        /**
         * Init screen model.
         */
        constructor() {
            var self = this;
            self.selectStartYm = ko.observable("");
            self.yearmonthdayeditor = {
                option: ko.mapping.fromJS(new nts.uk.ui.option.TimeEditorOption({
                    inputFormat: 'date'
                })),
            };
            self.C_INP_MEMO = ko.observable(null);
            self.valueSel001 = ko.observable("");
            self.startYmHis = ko.observable(null);
            self.object = ko.observable(null);
            //self.timeEditorOption = ko.mapping.fromJS(new option.TimeEditorOption({ inputFormat: "date"}));
            self.data = nts.uk.ui.windows.getShared('datanull');
            var startDateofHisFromScreena = nts.uk.ui.windows.getShared('startDateOfHis');
            startDateofHisFromScreena = new Date(startDateofHisFromScreena);
            var year = startDateofHisFromScreena.getFullYear();
            var month = startDateofHisFromScreena.getMonth() + 1;
            if (month < 10) month = "0" + month;
            var day = startDateofHisFromScreena.getDate();
            if (day < 10) day = "0" + day;
            self.startDateofHisFromScreenatoString = year + "" + month + "" + day;

            //---radio
            if (self.data == "datanull") {
                self.isRadioCheck = ko.observable(2);
                self.enable = ko.observable(false);
                self.itemsRadio = ko.observableArray([
                    { value: 1, text: ko.observable('最新の履歴から引き継ぐ') },
                    { value: 2, text: ko.observable('初めから作成する') }
                ]);
            } else {
                self.enable = ko.observable(true);
                //self.selectStartYm(nts.uk.ui.windows.getShared('startDateOfHis'));
                self.isRadioCheck = ko.observable(1);
                self.itemsRadio = ko.observableArray([
                    { value: 1, text: ko.observable('最新の履歴（' + nts.uk.ui.windows.getShared('startDateOfHis') + '）から引き継ぐ') },
                    { value: 2, text: ko.observable('初めから作成する') }
                ]);
            }


        }

        createHistory(): any {
            var self = this;
            var inputYm = $('#INP_STARTYMD').val();
            //check YM
            if (self.selectStartYm() == "") {
                alert("開始年月日 が入力されていません。");
                $("#INP_STARTYMD").focus();
                return false;
            }
            if (!nts.uk.time.parseYearMonthDate(inputYm).success) {
                alert(nts.uk.time.parseYearMonthDate(inputYm).msg);
                return false;
            }
            var selectYm = self.startYmHis();
            var inputYm2 = inputYm.replace('/', '');
            inputYm2 = inputYm2.replace('/', '');
            if (+inputYm2 < +self.startDateofHisFromScreenatoString
                || +inputYm2 == +self.startDateofHisFromScreenatoString) {
                alert('履歴の期間が正しくありません。');
                $("#INP_STARTYMD").focus();
                return false;
            }
            else {
                self.createData();
                nts.uk.ui.windows.close();
            }
        }

        createData(): any {
            var self = this;
            var startYearMonthDay = $('#INP_STARTYMD').val();
            var checked:any = null;
            if (self.isRadioCheck() === 1 && self.enable() === true) {
                checked = true;
            } else {
                checked = false;
            }
            var memo = self.C_INP_MEMO();
            var obj = new Object(startYearMonthDay, checked, memo);
            self.object(obj);
            nts.uk.ui.windows.setShared('itemHistory', self.object());
        }

        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            dfd.resolve();
            // Return.
            return dfd.promise();
        }

        closeDialog(): any {
            nts.uk.ui.windows.close();
        }
    }
    export class Object {
        startYearMonth: string;
        checked: boolean;
        memo: string;

        constructor(startYearMonth: string, checked: boolean, memo: string) {
            this.startYearMonth = startYearMonth;
            this.memo = memo;
            this.checked = checked;

        }
    }
}
/*

*/