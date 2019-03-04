import text = nts.uk.resource.getText;
ko.components.register('kaf022-b', {
    viewModel: {
        createViewModel: function(params, componentInfo) {
            let vm = {
                itemListB4: ko.observableArray([
                    { code: 1, name: text('KAF022_420')},
                    { code: 0, name: text('KAF022_421')}
                ]),
                itemListB6: ko.observableArray([
                    { code: 2, name: text('KAF022_137') },
                    { code: 1, name: text('KAF022_136') },
                    { code: 0, name: text('KAF022_37') },
                    
                    
                ]),
                itemListB8: ko.observableArray([
                    { code: 1, name: text('KAF022_36') },
                    { code: 0, name: text('KAF022_37') }
                   
                ]),
                itemListB24: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 1, name: text('KAF022_174') },
                ]),
                itemListB30: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 1, name: text('KAF022_175') },
                ]),    
                itemListB26: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 1, name: text('KAF022_174') },
                    { code: 2, name: text('KAF022_175') },
                ]),
                itemListB28: ko.observableArray([
                    { code: 1, name: text('KAF022_139') },
                    { code: 0, name: text('KAF022_140') },
                ]),
            };

            _.extend(vm, {
                selectedIdB4: params.selectedIdB4,
                selectedIdB8: params.selectedIdB8,
                selectedIdB10: params.selectedIdB10,
                selectedIdB12: params.selectedIdB12,
                selectedIdB6: params.selectedIdB6,
                
                selectedIdB15: params.selectedIdB15,
                selectedIdB17: params.selectedIdB17,
                selectedIdB19: params.selectedIdB19,
                selectedIdB21: params.selectedIdB21,
                
                selectedIdB24: params.selectedIdB24,
                selectedIdB26: params.selectedIdB26,
                selectedIdB28: params.selectedIdB28,
                selectedIdB30: params.selectedIdB30,
                selectedIdB32: params.selectedIdB32,
            });
            return vm;
        }
    },
    template: `
    <!-- ko let: { $bv: $data, text: nts.uk.resource.getText } -->
    <div class="fixed-table-container" style="width: 900px">
        <table id="fixed-table-b1">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind = "text: text('KAF022_119')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="b3" data-bind = "text: text('KAF022_419')"></td>
                    <td style="width: 650px">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB4,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB4,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="b5" data-bind = "text: text('KAF022_418')"></td>
                    <td>
                        <div id="b6" tabindex="2" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB6,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB6,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="b7" data-bind = "text: text('KAF022_427')"></td>
                    <td>
                        <div class="blockrow" id="b8" tabindex="3" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB8,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB8,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="b9" data-bind = "text: text('KAF022_428')"></td>
                    <td>
                        <div class="blockrow" id="b10" tabindex="4" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB8,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB10,
                            enable: false }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="b11" data-bind = "text: text('KAF022_429')"></td>
                    <td>
                        <div class="blockrow" id="b12" tabindex="5" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB8,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB12,
                            enable: false }">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-b2">
            <thead>
                <tr>
                    <th class="color-header" id="b13" colspan="2" data-bind = "text: text('KAF022_142')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="b14" data-bind = "text: text('KAF022_424')"></td>
                    <td style="width: 650px">
                        <div class="blockrow" id="b15" tabindex="6" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB8,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB15,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="b16" data-bind = "text: text('KAF022_425')"></td>
                    <td>
                        <div class="blockrow" id="b17" tabindex="7" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB8,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB17,
                            enable: true}">
                        </div>
                    </td>
                </tr>`+
//                <tr>
//                    <td class="color-header" id="b18" data-bind = "text: text('KAF022_426')"></td>
//                    <td>
//                        <div class="blockrow" id="b19" tabindex="8" data-bind="ntsRadioBoxGroup: {
//                            options: $bv.itemListB8,
//                            optionsValue: 'code',
//                            optionsText: 'name',
//                            value: $bv.selectedIdB19,
//                            enable: true}">
//                        </div>
//                    </td>
//                </tr>
                `
                <tr>
                    <td class="color-header" id="b20" data-bind = "text: text('KAF022_430')"></td>
                    <td>
                        <div class="blockrow" id="b21" tabindex="9" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB8,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB21,
                            enable: true}">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-b3">
            <thead>
                <tr>
                    <th class="color-header" id="b22" colspan="2" data-bind = "text: text('KAF022_143')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="b23" data-bind = "text: text('KAF022_422')"></td>  
                    <td style="width: 650px">
                        <div class="blockrow" id="b24" tabindex="10" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB24,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB24,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="b25" data-bind = "text: text('KAF022_423')"></td>
                    <td>
                        <div class="radio3Box" id="b26" tabindex="11" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB26,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB26,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="b27" data-bind = "text: text('KAF022_121')"></td>
                    <td>
                        <div id="b28" tabindex="12" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB28,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB28,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="b29" data-bind = "text: text('KAF022_431')"></td>
                    <td>
                        <div class="blockrow" id="b30" tabindex="13" data-bind="ntsRadioBoxGroup: {
                            options: $bv.itemListB30,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB30,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="b31" data-bind = "text: text('KAF022_394')"></td>
                    <td>
                        <div id="b32" tabindex="14" data-bind="ntsRadioBoxGroup: {  
                            options: $bv.itemListB26,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $bv.selectedIdB32,
                            enable: false}">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <!-- /ko -->
    `
});