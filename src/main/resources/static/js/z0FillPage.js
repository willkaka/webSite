/**
 * 进入index.html自动执行initPageInfo()
 **/

//全局变量，保存页面信息
var titleInfoMap = {"isChanged":false,"webName":""}; /* 网点名称 */
var menuMap = {"isChanged":false,"menuList":{}}; /* 菜单 */
var navMap = {"isChanged":false,"navList":{}}; /* 导航内容 */
var formatInfoMap = {"isChanged":false,"formatInfoList":{}}; /* 菜单输入输出格式信息，eg.输出区域记录编辑按钮，可加在此处*/
var inputMap = {"isChanged":false,"inputList":{}}; /* 输入区域内容，eg.查询条件及查询按钮 */
var outputMap = {"isChanged":false,"outputList":{}}; /* 输出区域内容，eg.查询返回的信息显示在表格 */
var changedMap ={"isChanged":false,"changedEleMap":{}}; /* 部分刷新内容，eg.刷新下拉菜单内容等 */
var nextOprMap ={}; /* 请求完成后，页面的操作内容，eg.自动刷新/提示操作成功等 */
var modalMap ={"isChanged":false,"modalDataInfo":{}}; /* 请求完成后，页面的操作内容，eg.自动刷新/提示操作成功等 */

var menuArea = "menuArea";      //菜单
var navArea = "navArea";        //导航
var inputArea = "inputArea";    //输入
var outputArea = "outputArea";  //输出
var modalArea = "swBackGround"; //弹窗

var curMenuId = "";
var userNameKey="userName";

/**
 * 初始化页面内容
 */
function initPageInfo(){
    let requestParm = '{"reqParm":{}}';
    let requestObj = JSON.parse(requestParm);  //string -> obj
    requestObj.reqParm = new Map();
    let requestJsonStr = JSON.stringify(requestObj); // obj -> string
    sendJsonByAjax("initPageInfo","post",requestJsonStr,"application/json;charset=utf-8",null,sucFreshAll);//刷新输出区域
}
function initPageInfo2(){
    let requestParm = '{"reqParm":{}}';
    let requestObj = JSON.parse(requestParm);  //string -> obj
    requestObj.reqParm = new Map();
    let requestJsonStr = JSON.stringify(requestObj); // obj -> string
    sendJsonByAjax("initPageInfo2","post",requestJsonStr,"application/json;charset=utf-8",null,sucFreshAll);//刷新输出区域
}


/*请求成功后，刷新所有页面信息*/
function sucFreshAll(returnDto){
    let isChanged = false;
    if(returnDto.titleInfoMap != null){
        if(returnDto.titleInfoMap.isChanged){
            titleInfoMap = returnDto.titleInfoMap;
            isChanged = true;
        }
    }
    if(returnDto.menuMap != null){
        if(returnDto.menuMap.isChanged){
            menuMap = returnDto.menuMap;
            isChanged = true;
        }
    }
    if(returnDto.navMap != null){
        if(returnDto.navMap.isChanged){
            navMap = returnDto.navMap;
            isChanged = true;
        }
    }
    if(returnDto.formatInfoMap != null){
        if(returnDto.formatInfoMap.isChanged){
            formatInfoMap = returnDto.formatInfoMap;
            isChanged = true;
        }
    }
    if(returnDto.inputMap != null){
        if(returnDto.inputMap.isChanged){
            inputMap = returnDto.inputMap;
            isChanged = true;
        }
    }
    if(returnDto.outputMap != null){
        if(returnDto.outputMap.isChanged){
            outputMap = returnDto.outputMap;
            isChanged = true;
        }
        if(returnDto.outputMap.isClear) clearChildren(outputArea);//清空已有的输出区域标签
    }

    //部分刷新返回数据包
    if(returnDto.changedMap != null){
        if(returnDto.changedMap.isChanged){
            changedMap = returnDto.changedMap;
            isChanged = true;
        }
    }

    if(isChanged){
        fillPageInfo();
    }

    //退出弹窗
    if(returnDto.webNextOpr != null){
        afterRequest(returnDto);
    }
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
    if(modalMap.isChanged) {
        showModal();
        modalMap.isChanged = false;
    }

    if(changedMap != null) {
        fillChangedEle();
        changedMap.isChanged = false;
    }

    if(getCookie(userNameKey) != null && getCookie(userNameKey) != ""){
        let loginA = document.getElementById("loginA");
        loginA.innerHTML = getCookie(userNameKey);
    }
}




