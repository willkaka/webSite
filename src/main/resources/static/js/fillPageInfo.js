// 目标：页面元素完全配置，新增功能只需开发后台实现功能，前台页面配置即可实现。
//一个页面需要的所有信息 pageInfo：
//1.logo
//2.菜单栏
//3.导航栏
//4.输入区域信息
//5.输出区域信息

// 统一所有事件处理方式
// 所有事件都把当前页面信息、请求类型发送到后台，后台输出内容更新到 pageInfo
// 由单一的方法处理改变的 pageInfo，并显示在页面上



//全局变量，保存页面信息
var titleInfoMap = {"isChanged":false,"webName":""}; /*  */
var menuMap = {"isChanged":false,"menuList":{}}; /*  */
var navMap = {"isChanged":false,"navList":{}}; /*  */
var formatInfoMap = {"isChanged":false,"formatInfoList":{}}; /* 菜单输入输出格式信息 */
var inputMap = {"isChanged":false,"inputList":{}}; /*  */
var outputMap = {"isChanged":false,"outputList":{}}; /* 输出区域内容 */
var changedMap ={"isChanged":false,"changedEleMap":{}}; /* 部分刷新内容 */

var menuArea = "menuArea";
var navArea = "navArea";
var inputArea = "inputArea";
var outputArea = "outputArea";

var curMenuId = "";

/**
 * 初始化页面内容
 */
function initPageInfo(){
    let requestParm = '{"reqParm":{}}';
    let requestObj = JSON.parse(requestParm);  //string -> obj
    requestObj.reqParm = new Map();
    let requestJsonStr = JSON.stringify(requestObj); // obj -> string
    sendJsonByAjax("initPageInfo",requestJsonStr,sucFreshAll);//刷新输出区域
}


function fillPageInfo() {
    if(titleInfoMap.isChanged) {
        fillWebTitle();
        titleInfoMap.isChanged = false;
    }
    if(menuMap.isChanged) {
        fillMenuArea();
        menuMap.isChanged = false;
    }
    if(navMap.isChanged) {
        fillNavArea();
        navMap.isChanged = false;
    }
    if(inputMap.isChanged) {
        fillInputArea();
        inputMap.isChanged = false;
    }
    if(outputMap.isChanged) {
        fillOutputArea();
        outputMap.isChanged = false;
    }

    if(changedMap != null) {
        fillChangedEle();
        changedMap.isChanged = false;
    }
}

/**
 * 填充菜单项
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
            element_div.setAttribute("class","dropdown");

            let element_a = document.createElement("a");
            element_a.setAttribute("class","dropbtn");
            element_a.setAttribute("id",menuList[i].id);
            element_a.innerHTML = menuList[i].prompt;//菜单名称

            setAttr(element_a,menuList[i].attrMap); // 属性配置
            setEventListener(element_a,menuList[i].eventInfoList); //事件
            element_div.appendChild(element_a);

            if(menuList[i].subElements != null && menuList[i].subElements.length>0){
                let element_sub_div = document.createElement("div");
                element_sub_div.setAttribute("class","dropdown-content");

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
        menuMap.isChanged = false;
    }
}

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

