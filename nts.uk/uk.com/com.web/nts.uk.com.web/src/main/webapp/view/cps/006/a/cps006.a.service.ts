module nts.uk.com.view.cps006.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    let paths = {
        getAllCategory: "ctx/bs/person/info/ctg/findAll",
        getAllPerInfoItemDefByCtgId: "ctx/bs/person/info/item/findby/categoryId/{0}",
        getAllPerInfoCtgByRoot: "ctx/bs/person/info/ctg/findAllByRoot",
        updateCtgInfo: "ctx/bs/person/info/ctg/updateCtgInfo",
        updateCategoryOrder: "ctx/bs/person/info/ctg/updateCtgOrder"
    }
    export function getAllCategory(){
        return ajax(paths.getAllCategory);
    }

    export function getAllPerInfoItemDefByCtgId(categoryId: string){
        return ajax(format(paths.getAllPerInfoItemDefByCtgId, categoryId));
    }

    export function getAllPerInfoCtgByRoot() {
        return ajax(paths.getAllPerInfoCtgByRoot);
    }

    export function updateCtgInfo(category: any){
        return ajax(paths.updateCtgInfo, category);
    }
    
    export function updateCtgOrder(categoryLst: any) {
        return ajax(paths.updateCategoryOrder, categoryLst);
    }
}
