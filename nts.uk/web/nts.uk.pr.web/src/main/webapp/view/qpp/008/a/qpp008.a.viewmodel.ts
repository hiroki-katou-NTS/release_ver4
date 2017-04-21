module qpp008.a.viewmodel {
    export class ScreenModel {
        paymentDateProcessingList: KnockoutObservableArray<any>;
        selectedPaymentDate: KnockoutObservable<any>;
        singleSelectedCode: KnockoutObservable<string>;
        texteditor1: any;
        texteditor2: any;
        textLbl006: KnockoutObservable<string>;

        /*Multiple selecting GridList*/
        items: KnockoutObservableArray<ItemModel>;
        columns: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<any>;
        currentCodeList: KnockoutObservableArray<any>;

        /*
        
                /*Multiple selecting GridList*/
        itemLeft: KnockoutObservableArray<Employee>;
        columnsLeft: KnockoutObservableArray<any>;
        currentCodeLeft: KnockoutObservable<any>;
        currentCodeListLeft: KnockoutObservableArray<any>;

        /*
                *label
        */

        //constraint: string = 'LayoutCode';
        inline: KnockoutObservable<boolean>;
        required: KnockoutObservable<boolean>;
        enable: KnockoutObservable<boolean>;

        /*
            combobox
        */
        //combobox1
        itemListCbb1: KnockoutObservableArray<ItemModelCbb1>;
        itemNameCbb1: KnockoutObservable<string>;
        currentCodeCbb1: KnockoutObservable<number>
        selectedCodeCbb1: KnockoutObservable<string>;
        isEnableCbb1: KnockoutObservable<boolean>;
        isEditableCbb1: KnockoutObservable<boolean>;

        //combobox2
        itemListCbb2: KnockoutObservableArray<ItemModelCbb2>;
        itemNameCbb2: KnockoutObservable<string>;
        currentCodeCbb2: KnockoutObservable<number>
        selectedCodeCbb2: KnockoutObservable<string>;
        isEnableCbb2: KnockoutObservable<boolean>;
        isEditableCbb2: KnockoutObservable<boolean>;

        //combobox3
        itemListCbb3: KnockoutObservableArray<ItemModelCbb3>;
        itemNameCbb3: KnockoutObservable<string>;
        currentCodeCbb3: KnockoutObservable<number>
        selectedCodeCbb3: KnockoutObservable<string>;
        isEnableCbb3: KnockoutObservable<boolean>;
        isEditableCbb3: KnockoutObservable<boolean>;
        yearmontheditor: any;


        constructor() {
            let self = this;
            self.textLbl006 = ko.observable('2017/11/01');
            self.paymentDateProcessingList = ko.observableArray([]);
            self.selectedPaymentDate = ko.observable(null);

            self.yearmontheditor = {
                value: ko.observable(200001),
                option: ko.mapping.fromJS(new nts.uk.ui.option.TimeEditorOption({
                    inputFormat: 'yearmonth'
                })),
                required: ko.observable(false),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };

            /*
                    GridList
            */
            self.items = ko.observableArray([
                new ItemModel('基本給'),
                new ItemModel('基本給3'),
                new ItemModel('基本給2'),
                new ItemModel('基本給1')
            ]);

            self.itemLeft = ko.observableArray([
                new Employee("A0001", "A", ""),
                new Employee("A0002", "A", ""),
                new Employee("A0003", "A", ""),
                new Employee("A0004", "A", ""),
                new Employee("A0005", "A", "")
            ]);

            self.columns = ko.observableArray([
                { headerText: '印刷内容', prop: 'name', width: 150 }

            ]);
            self.currentCode = ko.observable();
            self.currentCodeList = ko.observableArray([]);

            self.columnsLeft = ko.observableArray([
                { headerText: '印刷内容', prop: 'code', width: 150 },
                { headerText: '印刷内容', prop: 'name', width: 150 },
                { headerText: '印刷内容', prop: 'note', width: 250 }

            ]);
            self.currentCodeLeft = ko.observable();
            self.currentCodeListLeft = ko.observableArray([]);

            /* Label  */
            self.inline = ko.observable(true);
            self.required = ko.observable(true)
            self.enable = ko.observable(true);
            /**
             * combobox
              */
            //combobox1
            self.itemListCbb1 = ko.observableArray([
                new ItemModelCbb1('1', '基本給'),
                new ItemModelCbb1('2', '役職手当'),
                new ItemModelCbb1('3', '基本給')
            ]);

            self.itemNameCbb1 = ko.observable('');
            self.currentCodeCbb1 = ko.observable(3);
            self.selectedCodeCbb1 = ko.observable('0002')
            self.isEnableCbb1 = ko.observable(true);
            self.isEditableCbb1 = ko.observable(true);

            //combobox2
            self.itemListCbb2 = ko.observableArray([
                new ItemModelCbb2('基本給'),
                new ItemModelCbb2('役職手当'),
                new ItemModelCbb2('基本給2')
            ]);

            self.selectedCodeCbb2 = ko.observable('基本給');
            self.isEnableCbb2 = ko.observable(true);
            self.isEditableCbb2 = ko.observable(true);

            //combobox3
            self.itemListCbb3 = ko.observableArray([
                new ItemModelCbb3('基本給1', '基本給'),
                new ItemModelCbb3('基本給2', '役職手当'),
                new ItemModelCbb3('0003', '基本給')
            ]);

            self.itemNameCbb3 = ko.observable('');
            self.currentCodeCbb3 = ko.observable(3);
            self.selectedCodeCbb3 = ko.observable('')
            self.isEnableCbb3 = ko.observable(true);
            self.isEditableCbb3 = ko.observable(true);

            /**
                Textediter
            */
            self.texteditor1 = {
                value: ko.observable(''),
                constraint: 'ResidenceCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "2016/03",
                    width: "50px",
                    textalign: "left"
                })),
                required: ko.observable(true),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };

            self.texteditor2 = {
                value: ko.observable(''),
                constraint: 'ResidenceCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "2016/03",
                    width: "50px",
                    textalign: "left"
                })),
                required: ko.observable(true),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
        }
        /**
         *  Export Data
         */
        exportData(): void {
            let self = this;
            let dfd = $.Deferred<void>();
            service.saveAsPdf().done(function() {
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
            });

        }
        openBDialog() {
            var self = this;
            nts.uk.ui.windows.sub.modal('/view/qpp/008/b/index.xhtml', { title: '印刷設定', dialogClass: 'no-close' }).onClosed(function(): any {
            });
        }
        openCDialog() {
            var self = this;
            nts.uk.ui.windows.sub.modal('/view/qpp/008/c/index.xhtml', { title: '出力項目の設定（共通）', dialogClass: 'no-close' }).onClosed(function(): any {
            });
        }
        /* ItemModelCbb1 of combobox */
    }


    class ItemModelCbb1 {
        codeCbb1: string;
        nameCbb1: string;
        labelCbb1: string;

        constructor(codeCbb1: string, nameCbb1: string) {
            this.codeCbb1 = codeCbb1;
            this.nameCbb1 = nameCbb1;
        }
    }

    class ItemModelCbb2 {
        nameCbb2: string;
        labelCbb2: string;
        constructor(nameCbb2: string) {
            this.nameCbb2 = nameCbb2;
            this.labelCbb2 = nameCbb2;
        }
    }

    class ItemModelCbb3 {
        codeCbb3: string;
        nameCbb3: string;
        labelCbb3: string;
        constructor(codeCbb3: string, nameCbb3: string) {
            this.codeCbb3 = codeCbb3;
            this.nameCbb3 = nameCbb3;
        }
    }

    class ItemModel {
        name: string;

        constructor(name: string) {
            this.name = name;
        }
    }
    class Employee {
        code: string;
        name: string;
        note: string;
        constructor(code: string, name: string, note: string) {
            this.code = code;
            this.name = name;
            this.note = note;
        }

    }

}
