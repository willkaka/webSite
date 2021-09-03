
/**
 * 填充菜单项
 * @param rtnMap
 */
function fillMenuArea() {
    let isChanged = menuMap.isChanged;
    if(isChanged){
        let menuList = menuMap.menuList;

        clearChildren(menuArea);//清空已有的菜单标签

        if(menuList==null) return;
        let element = document.getElementById(menuArea);
        for (let i=0;i<menuList.length;i++){
            let element_div = document.createElement("div");
            element_div.setAttribute("class","menuDropDown");

            let element_a = document.createElement("a");
            element_a.setAttribute("class","menuDropButton");
            element_a.setAttribute("id",menuList[i].id);
            element_a.innerHTML = menuList[i].prompt;//菜单名称
            setAttr(element_a,menuList[i].attrMap); // 属性配置
            setEventListener(element_a,menuList[i].eventInfoList); //事件
            element_div.appendChild(element_a);

            if(menuList[i].subElements != null && menuList[i].subElements.length>0){
                let element_sub_div = document.createElement("div");
                element_sub_div.setAttribute("class","menuDropDown-content");

                let subElements = menuList[i].subElements;
                for (let j=0;j<subElements.length;j++){
                    let element_a = document.createElement("a");
                    element_a.setAttribute("id",subElements[j].id);
                    element_a.innerHTML = subElements[j].prompt;//菜单名称
                    setAttr(element_a,subElements[j].attrMap); // 属性配置
                    setEventListener(element_a,subElements[j].eventInfoList); //事件
                    element_sub_div.appendChild(element_a);
                }
                element_div.appendChild(element_sub_div);
            }
            element.appendChild(element_div);
        }
        if(getCookie("userName")){

        }

        menuMap.isChanged = false;
    }
}