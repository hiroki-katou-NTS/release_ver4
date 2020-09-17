module nts.uk.at.view.ksu001.a {
    let __viewContext: any = window["__viewContext"] || {};
    let KEY = 'USER_INFOR';
    __viewContext.ready(function() {
        __viewContext.viewModel = {
            viewAB: new ksu001.ab.viewmodel.ScreenModel(),
            viewAC: new ksu001.ac.viewmodel.ScreenModel(),
            viewA: new ksu001.a.viewmodel.ScreenModel()
        };
        
        nts.uk.ui.block.grayout();
        __viewContext.viewModel.viewA.startPage().done(() => {
            __viewContext.bind(__viewContext.viewModel);
            
            $(window).resize(function() {
                __viewContext.viewModel.viewA.setPositionButonDownAndHeightGrid();
                __viewContext.viewModel.viewA.setPositionButonToRight();
            });

            nts.uk.ui.block.clear();
        });

        initEvent();
    });

    function initEvent(): void {
        // Fire event.
        $("#multi-list").on('itemDeleted', (function(e: Event) {
            alert("Item is deleted in multi grid is " + e["detail"]["target"]);
        }));

        //A1_10_1 click btn10
        $('#A1_10_1').ntsPopup({
            position: {
                my: 'left top',
                at: 'left bottom+3',
                of: $('#A1_10')
            }
        });

        $('#A1_10').click(function() {
            $('#A1_10_1').ntsPopup("toggle");
        });
        
        //A1_7_1 click btn7
        $('#A1_7_1').ntsPopup({
            position: {
                my: 'left top',
                at: 'left bottom+3',
                of: $('#A1_7')
            }
        });

        $('#A1_7').click(function() {
            $('#A1_7_1').ntsPopup("toggle");
        });
        

        // A1_12_1 click btn12
        $('#A1_12_1').ntsPopup({
            position: {
                my: 'right top',
                at: 'right bottom+3',
                of: $('#A1_12')
            }
        });

        $('#A1_12').click(function() {
            $('#A1_12_1').ntsPopup("toggle");
        });

        $('#A4_1').ntsPopup("init", {
            position: {
                my: 'left top',
                at: 'left bottom+3',
                of: $('#A4')
            },
            dismissible: false
        });
        
        $(window).on("mousedown.popup", function(e) {
            let control = $('#A4_1');
            let combo = $('.nts-combo-column-0');

            if ($(e.target).is(combo[1]) || $(e.target).is(combo[2])) {
                console.log('not hide');
            } else if (!$(e.target).is(control) // Target isn't Popup
                && control.has(e.target).length === 0) { // Target isn't Trigger element
                hide(control);
            }
        });

        function hide(control: JQuery): JQuery {
            control.css({
                visibility: 'hidden',
                top: "-9999px",
                left: "-9999px"
            });
            return control;
        }
        
        $('#A4').click(function() {
            $('#A4_1').ntsPopup("toggle");
        });
        
        
        //click btnA5
        $('#A5_1').ntsPopup({
            position: {
                my: 'left top',
                at: 'left bottom+3',
                of: $('#A5')
            }
        });

        $('#A5').click(function() {
            $('#A5_1').ntsPopup("toggle");
        });

        
        //popup setting grid
        $('#A16').ntsPopup({
            position: {
                my: 'left top',
                at: 'left bottom+3',
                of: $('.settingHeightGrid')
            }
        });

        $('.settingHeightGrid').click(function() {
            $('#A16').ntsPopup("toggle");
        });
    }
}