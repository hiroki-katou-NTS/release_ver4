module nts.uk.at.view.kdw004.a {
    __viewContext.ready(function() {
        let screenModel = new viewmodel.ScreenModelKDW004A();
        let value = __viewContext.transferred.value,
            param = null;
        if (_.isNil(value) || typeof(value) != 'object') {
            param = {
                closureIdParam: null,
                startDateParam: null,
                endDateParam: null
            };
        } else {
            param = {
                closureIdParam: value.closureId,
                startDateParam: moment(value.startDateKDW004).utc().toISOString(),
                endDateParam: moment(value.endDateKDW004).utc().toISOString(),
            };
        }
        
        screenModel.startPage(param).done(function() {
            __viewContext.bind(screenModel);
            screenModel.setHeadersColor();
            let img = document.createElement("span");
            img.className = "windows-img";
            let colorBtn = document.getElementById("colorBtn");
            colorBtn.innerHTML = "";
            colorBtn.appendChild(img);
            let txt = document.createElement("span");
            txt.innerHTML = nts.uk.resource.getText("KDW004_7");
            colorBtn.appendChild(txt);
            //disable tabIndex button date range
            document.getElementsByClassName("ntsDateRangeButton")[0].tabIndex = -1;
            document.getElementsByClassName("ntsDateRangeButton")[1].tabIndex = -1;
        }).fail((messageId) => {
            nts.uk.ui.dialog.alert({ messageId: messageId }).then(() => {
                 nts.uk.request.jump("com", "view/ccg/008/a/index.xhtml");
            });
        });
    });
}