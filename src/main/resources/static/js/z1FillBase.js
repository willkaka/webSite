/**
 * 填充站点名称
 * @param rtnMap
 */
function fillWebTitle() {
    let isChanged = titleInfoMap.isChanged;
    if(isChanged){
//        let webName = titleInfoMap.webName;
//        if(webName != null){
//            let element = document.getElementById("webTitle");
//            element.innerHTML = webName;
//        }
        titleInfoMap.isChanged = false;
    }
}