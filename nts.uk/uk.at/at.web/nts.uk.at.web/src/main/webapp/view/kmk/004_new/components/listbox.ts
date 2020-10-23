/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kmk004_new{
    
    interface Params {
    }

    const template = `
        <div class="listbox">
            <div id="list-box" data-bind="ntsListBox: {
                options: itemList,
                optionsValue: 'value',
                optionsText: 'value',
                multiple: false,
                value: selectedYear,
                enable: false,
                rows: 5,
                columns: [
                    { key: 'statusValue', length: 1 },
                    { key: 'value', length: 4 }
                ]}"></div>
        </div>
        <style type="text/css" rel="stylesheet">
        </style>
        <style type="text/css" rel="stylesheet" data-bind="html: $component.style"></style>
    `;

    @component({
        name: 'listbox',
        template
    })

    class ListBox extends ko.ViewModel {

        public itemList: KnockoutObservableArray<IListYear> = ko.observableArray([]);
        public selectedYear: KnockoutObservable<string> = ko.observable('');

        created() {
            const vm = this;
            const faceData: IListYear[] = [{status: true, statusValue : '*', value: '2017'},
            {status: false, statusValue : '', value: '2016'}
            ,{status: true, statusValue : '*', value: '2018'}
            ,{status: true, statusValue : '*', value: '2020'}];

            vm.itemList(_.orderBy(faceData, ['value'],['desc']));
            
            console.log(vm.selectedYear);
        }

    }

    export interface IListYear {
        status: boolean;
        statusValue: string;
        value: string;
    }

    // export class ListYear {
    //     status: KnockoutObservable<boolean> = ko.observable(true);
    //     statusValue: KnockoutObservable<string> = ko.observable('');
    //     value: KnockoutObservable<string> = ko.observable('');

    //     public create(params?: IListYear) {
    //         const self = this;

    //         if (params) {
    //             self.status(params.status);
    //             self.value(params.value);
    //             if(params.status) {
    //                 self.statusValue('*');
    //             }
    //         }
    //     }
    // }
}
