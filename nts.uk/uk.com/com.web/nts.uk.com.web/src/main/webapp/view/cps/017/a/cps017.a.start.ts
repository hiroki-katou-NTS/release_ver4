module nts.uk.com.view.cps017.a {
    let __viewContext: any = window["__viewContext"] || {};

    __viewContext.ready(function() {

        __viewContext["viewModel"] = new viewmodel.ViewModel();

        init();

        __viewContext.bind(__viewContext["viewModel"]);

    });

}

function init() {

    $("#item_register_grid").ntsGrid({
        width: '300px',
        height: '400px',
        dataSource: __viewContext["viewModel"].items,
        primaryKey: 'id',
        virtualization: true,
        virtualizationMode: 'continuous',
        columns: [
            { headerText: '', key: 'id', dataType: 'number', width: '30px', ntsControl: 'Label' },
            { headerText: nts.uk.resource.getText('CPS017_18'), key: 'init_sel', dataType: 'boolean', width: '50px', ntsControl: 'Checkbox' },
            { headerText: nts.uk.resource.getText('CPS017_16'), key: 'sel_cd', dataType: 'string', width: '110px', ntsControl: 'Label' },
            { headerText: nts.uk.resource.getText('CPS017_17'), key: 'sel_name', dataType: 'string', width: '70px', ntsControl: 'Label' },
        ],
        features: [{ name: 'Resizing' }],
        ntsFeatures: [{ name: 'CopyPaste' }],
        ntsControls: [
            {
                name: 'Checkbox', options: { value: 1, text: "" }, optionsValue: 'value',
                optionsText: 'text', controlType: 'CheckBox', enable: true
            }
        ]
    });
}
