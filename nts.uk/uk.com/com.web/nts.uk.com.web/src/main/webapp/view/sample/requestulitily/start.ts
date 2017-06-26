__viewContext.ready(function () {
    class ScreenModel {
        exportFile() {
            nts.uk.request.exportFile('/sample/report/generate', { value: 'abc' }).done(() => {
                console.log('DONE!!');
            });
        }
    }

    
    this.bind(new ScreenModel());
    
});