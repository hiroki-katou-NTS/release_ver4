module nts.uk.at.view.kmk012.e {
    import getText = nts.uk.resource.getText;

    //When view context ready.
    __viewContext.ready(function() {

        var model = new viewmodel.ScreenModel();

        //When getClosureEmploy done, set data for dataSource on grid.
        model.getClosureEmploy(model.startDate).done(function() {
            //Init data for combobox.
            var comboItems = model.closureEmployDto.closureCdNameList;
            //Insert comboColumnas with value = 0;
            var closureCdNameDto = new viewmodel.ClosureCdNameDto(-1, '');
            comboItems.unshift(closureCdNameDto);
            var comboColumns = [{ prop: 'closureIdMain', length: 4 },
                { prop: 'closureName', length: 8 }];

            //View list data on grid.
            $("#grid2").ntsGrid({
                width: '390px',
                height: '380px',
                dataSource: model.items,
                primaryKey: 'code',
                virtualization: true,
                virtualizationMode: 'continuous',

                //columns on grid list.
                columns: [
                    { headerText: 'ID', key: 'code', dataType: 'string', width: '50px', hidden: true },
                    { headerText: getText('KMK012_38'), key: 'name', dataType: 'string', width: '150px' },
                    { headerText: getText('KMK012_39'), key: 'closureId', dataType: 'number', width: '180px', ntsControl: 'Combobox' }
                ],
                features: [{ name: 'Sorting', type: 'local' }],

                //Defind combobox and other control
                ntsControls: [
                    { name: 'Combobox', options: comboItems, optionsValue: 'closureIdMain', optionsText: 'closureName', columns: comboColumns, controlType: 'ComboBox', enable: true }]
            });
        });

        //Add function button
        $("#button8").on("click", function() {
            var source = $("#grid2").igGrid("option", "dataSource");
            
            //Add source data on grid to server
            model.insertDelArray(source);
        });
        
        __viewContext.bind(model);

    });
}