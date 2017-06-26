module nts.uk.pr.view.qpp020.a.viewmodel {
    export class viewModel {
        showModalDialogB(item, event): void {
            nts.uk.ui.windows.sub.modal("../b/index.xhtml", {
                resizable: false, width: 600, height: 515, title: '給与処理月を翌月へ更新'
            })
                .onClosed(() => {
                })
        }
    }
}