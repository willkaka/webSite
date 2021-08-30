
/**
 * 填充导航栏
 * @param rtnMap
 */
function fillNavArea(navList) {
    clearChildren(navArea);//清空已有的菜单标签

    let element = document.getElementById(navArea);
    element.setAttribute("class","breadcrumb");

    if(navList != null){
        for (let i=0;i<navList.length;i++){
            let nav_li = document.createElement("li");
            let nav_a = document.createElement("a");
            nav_a.addEventListener("click",navClick.bind(this,navList[i].elementId,navList[i].elementName),false);
            let node = document.createTextNode(navList[i].elementName);
            nav_a.appendChild(node);
            nav_li.appendChild(nav_a);
            element.appendChild(nav_li);
        }
    }
}

function navClick(menuId,menuText) {
    //alert(menuId);
    curMenuId = menuId;
    curMenuText = menuText;
    fillMenuArea(getMenuList());
    fillNavArea(getNavList(curMenuId));
    fillInputArea(getInputList(curMenuId));
}