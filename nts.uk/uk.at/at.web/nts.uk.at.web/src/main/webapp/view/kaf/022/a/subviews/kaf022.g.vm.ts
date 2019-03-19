import text = nts.uk.resource.getText;

ko.components.register('kaf022-g', {
    viewModel: {
        createViewModel: function(params, componentInfo) {
            let vm = {
                itemListG16: ko.observableArray([
                    { code: 0, name: text('KAF022_221') },
                    { code: 1, name: text('KAF022_222') }
                ]),
                itemListG24: ko.observableArray([
                    { code: 3, name: text('KAF022_198') },
                    { code: 2, name: text('KAF022_199') },
                    { code: 1, name: text('KAF022_200') },
                    { code: 0, name: text('KAF022_201') }
                ]),
//                itemListG8: ko.observableArray([
//                    { code: 0, name: text('KAF022_439') },
//                    { code: 1, name: text('KAF022_392') }
//                ]),
                itemListG10: ko.observableArray([
                    { code: 1, name: text('KAF022_36') },
                    { code: 0, name: text('KAF022_37') }
                ]),
                itemListG26: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 1, name: text('KAF022_175') }
                ]),
                itemListG28: ko.observableArray([
                    { code: 1, name: text('KAF022_173') },
                    { code: 0, name: text('KAF022_175') }
                ]),
                itemListG30: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 1, name: text('KAF022_174') }
                ]),
                itemListG32: ko.observableArray([
                    { code: 0, name: text('KAF022_173') },
                    { code: 1, name: text('KAF022_174') },
                    { code: 2, name: text('KAF022_175') },
                ]),
                itemListG34: ko.observableArray([
                    { code: 0, name: text('KAF022_139') },
                    { code: 1, name: text('KAF022_140') }
                ]),
                
            };

            _.extend(vm, {
                selectedIdG4: params.selectedIdG4,
                selectedIdG6: params.selectedIdG6,
                selectedIdG8: params.selectedIdG8,
                selectedIdG10: params.selectedIdG10,
                selectedIdG12: params.selectedIdG12,
                selectedIdG14: params.selectedIdG14,
                
                selectedIdG17: params.selectedIdG17,
                selectedIdG19: params.selectedIdG19,
                selectedIdG21: params.selectedIdG21,
                selectedIdG23: params.selectedIdG23,
                
                selectedIdG26: params.selectedIdG26,
                selectedIdG28: params.selectedIdG28,
                selectedIdG30: params.selectedIdG30,
                selectedIdG32: params.selectedIdG32,
                selectedIdG34: params.selectedIdG34,
                selectedIdG36: params.selectedIdG36,
                selectedIdG38: params.selectedIdG38,
                selectedIdG40: params.selectedIdG40,
            });

            return vm;
        }
    },
    template: `
    <!-- ko let: { $gv: $data, text: nts.uk.resource.getText } -->
    <div class="fixed-table-container" style="width: 900px;">
        <table id="fixed-table-g1">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind="text: text('KAF022_206')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 258px" class="color-header" id="g3" data-bind="text: text('KAF022_220')"></td>
                    <td style="width: 650px">
                        <div class="blockrow" id="g4" tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG16,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG4,
                            enable: false}">
                        </div>
                    </td> 
                </tr>
                <tr>
                    <td  class="color-header"  id="g5" data-bind="text: text('KAF022_197')"></td>
                    <td>
                        <div class = "blockrowg6" id="g6" tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG24,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG6,
                            enable: false}">
                        </div>
                    </td>               
                </tr>`+
//                <tr>
//                    <td class="color-header" id="g7" data-bind="text: text('KAF022_186')"></td>
//                    <td>
//                        <div class = "blockrowg6" id="g8" tabindex="1" data-bind="ntsRadioBoxGroup: {
//                            options: $gv.itemListG8,
//                            optionsValue: 'code',
//                            optionsText: 'name',
//                            value: $gv.selectedIdG8,
//                            enable: true}">
//                        </div>
//                    </td> 
//                </tr>
                `<tr>
                    <td class="color-header" id="g9" data-bind="text: text('KAF022_427')"></td>
                    <td>
                        <div class="blockrow" id="g10" tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG10,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG10,
                            enable: false}">
                        </div>
                    </td> 
                </tr>
                <tr>
                    <td class="color-header" id="g11" data-bind="text: text('KAF022_428')"></td>
                    <td>
                        <div class="blockrow" id="g12" tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG10,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG12,
                            enable: false}">
                        </div>
                    </td> 
                </tr>
                <tr>
                    <td class="color-header" id="g13" data-bind="text: text('KAF022_429')"></td>
                    <td>
                        <div class="blockrow" id="g14" tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG10,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG14,
                            enable: false}">
                        </div>
                    </td> 
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-g2">
            <thead>
                <tr>
                    <th class="color-header" id="g15" colspan="2" data-bind = "text: text('KAF022_142')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="g16" data-bind = "text: text('KAF022_424')"></td>
                    <td style="width: 650px">
                        <div class="blockrow" id="g17" tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG10,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG17,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="g18" data-bind = "text: text('KAF022_425')"></td>
                    <td>
                        <div class="blockrow" id="g19" tabindex="2" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG10,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG19,
                            enable: false}">
                        </div>
                    </td>
                </tr>`+
    //                <tr>
    //                    <td class="color-header" id="g20" data-bind = "text: text('KAF022_426')"></td>
    //                    <td>
    //                        <div class="blockrow" id="g21" tabindex="3" data-bind="ntsRadioBoxGroup: {
    //                            options: $gv.itemListG10,
    //                            optionsValue: 'code',
    //                            optionsText: 'name',
    //                            value: $gv.selectedIdG21,
    //                            enable: true}">
    //                        </div>
    //                    </td>
    //                </tr>
    
                `
                <tr>
                    <td class="color-header" id="g22" data-bind = "text: text('KAF022_430')"></td>
                    <td>
                        <div class="blockrow" id="g23" tabindex="3" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG10,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG23,
                            enable: true}">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-g2">
            <thead>
                <tr>
                    <th class="color-header" id="g15" colspan="2" data-bind = "text: text('KAF022_143')"></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td style="width: 270px" class="color-header" id="g25" data-bind = "text: text('KAF022_432')"></td>
                    <td style="width: 650px">
                        <div class="blockrow" id="g26" tabindex="1" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG26,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG26,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="g27" data-bind = "text: text('KAF022_433')"></td>
                    <td>
                        <div class="blockrow" id="g28" tabindex="2" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG28,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG28,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="g29" data-bind = "text: text('KAF022_422')"></td>
                    <td>
                        <div class="blockrow" id="g30" tabindex="3" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG30,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG30,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="g31" data-bind = "text: text('KAF022_423')"></td>
                    <td>
                        <div class="blockrow" id="g32" tabindex="3" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG32,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG32,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="g33" data-bind = "text: text('KAF022_121')"></td>
                    <td>
                        <div class="blockrow" id="g34" tabindex="3" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG34,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG34,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="g35" data-bind = "text: text('KAF022_431')"></td>
                    <td>
                        <div class="blockrow" id="g36" tabindex="3" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG26,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG36,
                            enable: true}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="g37" data-bind = "text: text('KAF022_394')"></td>
                    <td>
                        <div class="blockrow" id="g38" tabindex="3" data-bind="ntsRadioBoxGroup: {  
                            options: $gv.itemListG32,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG38,
                            enable: false}">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="g39" data-bind = "text: text('KAF022_264')"></td>
                    <td>
                        <div class="blockrow" id="g40" tabindex="3" data-bind="ntsRadioBoxGroup: {
                            options: $gv.itemListG26,
                            optionsValue: 'code',
                            optionsText: 'name',
                            value: $gv.selectedIdG40,
                            enable: true}">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <!-- /ko -->
    `
});