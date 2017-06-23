module nts.uk.sys.view.ccg013.b.viewmodel {
    import windows = nts.uk.ui.windows;

    export class ScreenModel {
        //Text edittor
        nameMenuBar: KnockoutObservable<string>;
        //Combobox
        listSystemSelect: KnockoutObservableArray<any>;
        selectedCodeSystemSelect: KnockoutObservable<string>;
        //colorpicker
        letterColor: KnockoutObservable<string>;
        backgroundColor: KnockoutObservable<string>;
        //Radio button
        itemRadioAtcClass: KnockoutObservableArray<any>;
        selectedRadioAtcClass: KnockoutObservable<number>;
        //GridList
        listStandardMenu: KnockoutObservableArray<any>;
        columns: KnockoutObservableArray<any>;
        currentListStandardMenu: KnockoutObservableArray<any>;
        selectCodeStandardMenu: KnockoutObservable<string>;
        allPart: KnockoutObservableArray<any>;
        selectedSystemID: KnockoutObservable<string>;
        constructor() {
            var self = this;
            self.nameMenuBar = ko.observable("");
            //Combo box
            self.listSystemSelect = ko.observableArray([]);
            self.selectedCodeSystemSelect = ko.observable('')
            //Radio button
            self.itemRadioAtcClass = ko.observableArray([]);
            self.selectedRadioAtcClass = ko.observable(1);
            //color picker
            self.letterColor = ko.observable('');
            self.backgroundColor = ko.observable('');
            //GridList
            self.allPart = ko.observableArray([]);
            self.listStandardMenu = ko.observableArray([]);
            self.columns = ko.observableArray([
                { headerText: 'コード', prop: 'code', key: 'code', width: 100 },
                { headerText: '名称', prop: 'displayName', key: 'displayName', width: 230 }
            ]);
            self.selectCodeStandardMenu = ko.observable('');
            self.currentListStandardMenu = ko.observableArray([]);
            //Follow SystemSelect
            self.selectedSystemID = ko.observable(null);
            self.selectedCodeSystemSelect.subscribe((value) => { self.changeSystem(value); });
                
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            var data = windows.getShared("CCG013A_StandardMeNu");
            if (data) {
                self.nameMenuBar(data.nameMenuBar);
                self.letterColor(data.pickerLetter);
                self.backgroundColor(data.pickerBackground);
                self.selectedRadioAtcClass(data.radioActlass);

            }

            /** Get EditMenuBar*/
            service.getEditMenuBar().done(function(editMenuBar: any) {
                self.itemRadioAtcClass(editMenuBar.listSelectedAtr);
                self.listSystemSelect(editMenuBar.listSystem);
                console.log(editMenuBar);
                self.allPart(editMenuBar.listStandardMenu);
                let listStandardMenu: Array<any> = _.orderBy((editMenuBar.listStandardMenu, ["code"], ["asc"]));
                self.listStandardMenu(editMenuBar.listStandardMenu);
                dfd.resolve();
            }).fail(function(error) {
                dfd.reject();
                alert(error.message);
            });
            return dfd.promise();
        }

        cancel_Dialog(): any {
            nts.uk.ui.windows.close();
        }

        submit() {
            var self = this;
            var menuBar = new MenuBar(self.nameMenuBar(), self.letterColor(), self.backgroundColor(), self.selectedRadioAtcClass());
            windows.setShared("CCG013B_MenuBar", menuBar);
            self.cancel_Dialog();
        }

        /** Change System */
        private changeSystem(value): void {
            var self = this;
            var standardMenus =  _.chain(self.allPart()).filter(['system', value]).value();
            self.listStandardMenu(standardMenus);
        }


    }

    class MenuBar {
        nameMenuBar: string;
        letterColor: string;
        backgroundColor: string;
        selectedRadioAtcClass: number;

        constructor(nameMenuBar: string, letterColor: string, backgroundColor: string, selectedRadioAtcClass: number) {
            this.nameMenuBar = nameMenuBar;
            this.letterColor = letterColor;
            this.backgroundColor = backgroundColor;
            this.selectedRadioAtcClass = selectedRadioAtcClass;
        }
    }
}