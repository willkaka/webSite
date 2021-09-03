
//全局变量，保存页面信息
var titleInfoMap = {"isChanged":false,"webName":""}; /* 网点名称 */
var menuMap = {"isChanged":false,"menuList":{}}; /* 菜单 */
var navMap = {"isChanged":false,"navList":{}}; /* 导航内容 */
var formatInfoMap = {"isChanged":false,"formatInfoList":{}}; /* 菜单输入输出格式信息，eg.输出区域记录编辑按钮，可加在此处*/
var inputMap = {"isChanged":false,"inputList":{}}; /* 输入区域内容，eg.查询条件及查询按钮 */
var outputMap = {"isChanged":false,"outputList":{}}; /* 输出区域内容，eg.查询返回的信息显示在表格 */
var changedMap ={"isChanged":false,"changedEleMap":{}}; /* 部分刷新内容，eg.刷新下拉菜单内容等 */
var nextOprMap ={}; /* 请求完成后，页面的操作内容，eg.自动刷新/提示操作成功等 */

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
    sendJsonByAjax("initPageInfo",requestJsonStr,sucFreshAll);//刷新输出区域
}


/*请求成功后，刷新所有页面信息*/
function sucFreshAll(ReturnDto){
    let isChanged = false;
    if(ReturnDto.titleInfoMap != null){
        if(ReturnDto.titleInfoMap.isChanged){
            titleInfoMap = ReturnDto.titleInfoMap;
            isChanged = true;
        }
    }
    if(ReturnDto.menuMap != null){
        if(ReturnDto.menuMap.isChanged){
            menuMap = ReturnDto.menuMap;
            isChanged = true;
        }
    }
    if(ReturnDto.navMap != null){
        if(ReturnDto.navMap.isChanged){
            navMap = ReturnDto.navMap;
            isChanged = true;
        }
    }
    if(ReturnDto.formatInfoMap != null){
        if(ReturnDto.formatInfoMap.isChanged){
            formatInfoMap = ReturnDto.formatInfoMap;
            isChanged = true;
        }
    }
    if(ReturnDto.inputMap != null){
        if(ReturnDto.inputMap.isChanged){
            inputMap = ReturnDto.inputMap;
            isChanged = true;
        }
    }
    if(ReturnDto.outputMap != null){
        if(ReturnDto.outputMap.isChanged){
            outputMap = ReturnDto.outputMap;
            isChanged = true;
        }
        if(ReturnDto.outputMap.isClear) clearChildren(outputArea);//清空已有的输出区域标签
    }

    //部分刷新返回数据包
    if(ReturnDto.changedMap != null){
        if(ReturnDto.changedMap.isChanged){
            changedMap = ReturnDto.changedMap;
            isChanged = true;
        }
    }

    if(isChanged){
        fillPageInfo();
    }

    //退出弹窗
    if(ReturnDto.webNextOpr != null){
        afterRequest(ReturnDto);
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

    if(changedMap != null) {
        fillChangedEle();
        changedMap.isChanged = false;
    }

    if(getCookie(userNameKey) != null && getCookie(userNameKey) != ""){
        let loginA = document.getElementById("loginA");
        loginA.innerHTML = getCookie(userNameKey);
    }
}




