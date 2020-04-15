//全局变量，保存页面全局信息
var curMenuId="";//当前被选择的菜单项id
var curMenuText="";//当前被选择的菜单项名称
var curInputList;

/**
 * 初始化页面内容
 */
function getPageInfo() {
    var requestParm = '{"reqName":"window","reqParm":{"x11":"a11","x12":"a12"},"rtnCode":"0000","rtnMsg":""}';
    sendJsonByAjax("getPageInfo",requestParm,fillPageInfo);
}

/**
 * 点击菜单请求
 * @param curMenuId
 */
function menuReq(curMenuId) {
    var requestParm = '{"reqName":"test001","reqParm":{"x11":"a11","x12":"a12"},"rtnCode":"0000","rtnMsg":""}';
    var requestObj = JSON.parse(requestParm);  //string -> obj
    requestObj.reqName=curMenuId;
    var requestJsonStr = JSON.stringify(requestObj); // obj -> string
    sendJsonByAjax("getPageInfo",requestJsonStr,fillPageInfo);
}

/**
 * 发送json报文到后台
 * @param requestUrl
 * @param requestParam
 * @param sucfn
 */
function sendJsonByAjax(requestUrl,requestParam,sucfn) {
    $.ajax({
        type: "post",
        url: requestUrl,
        data: requestParam,//ReqJsonDto
        contentType:"application/json;charset=utf-8",
        success:function (ReqJsonDto) {
            if (ReqJsonDto.rtnCode === '0000') {
                sucfn(ReqJsonDto.rtnMap);
            } else {
                alert("请求成功，处理失败，返回码"+ReqJsonDto.rtnCode + ":" + ReqJsonDto.rtnMsg);
            }
        },
        error:function (ReqJsonDto) {
            alert("请求失败，返回信息："+ReqJsonDto);
        }
    });
}

/**
 * 填充页面信息
 * @param rtnMap
 */
function fillPageInfo(rtnMap) {
    fillMenuArea(rtnMap);
    fillNavArea(rtnMap);
    fillInputArea(rtnMap);
    fillOutPutArea(rtnMap);
}

/**
 * 填充菜单项
 * @param rtnMap
 */
function fillMenuArea(rtnMap) {
    clearChildren("menuList");//清空已有的菜单标签

    var menuList = rtnMap.menuList;
    if(menuList==null) return;
    var element = document.getElementById("menuList");
    for (let i=0;i<menuList.length;i++){
        var element_li = document.createElement("li");
        if(menuList[i].elementId===curMenuId){
            element_li.setAttribute("class","active");
        }
        var element_a = document.createElement("a");
        var node = document.createTextNode(menuList[i].elementName);//菜单名称
        element_a.appendChild(node);
        element_a.setAttribute("id",menuList[i].elementId);
        element_a.setAttribute("class",menuList[i].elementClass);//鼠标移动到则改变鼠标样式
        element_a.addEventListener("click",menuClick.bind(this,menuList[i].elementId,menuList[i].elementName),false);
        element_li.appendChild(element_a);
        element.appendChild(element_li);
    }
}

/**
 * 菜单点击事件处理方法
 * @param menuId
 */
function menuClick(menuId,menuText) {
    //alert(menuId);
    curMenuId = menuId;
    curMenuText = menuText;
    menuReq(curMenuId);
}

/**
 * 填充输入区域
 * @param rtnMap
 */
function fillInputArea(rtnMap) {
    clearChildren("inputArea");//清空已有的菜单标签
    var inputList = rtnMap.inputList;
    curInputList = inputList;
    if(inputList==null) return;
    var element = document.getElementById("inputArea");
    for (let i=0;i<inputList.length;i++){
        if("dropDown"===inputList[i].elementType){
            addDropDown(element,inputList[i]);
        }else if("input"===inputList[i].elementType){
            addInput(element,inputList[i],inputList);
        }else if("button"===inputList[i].elementType){
            addButton(element,inputList[i],inputList);
        }else if("btn_user"===inputList[i].elementType){ //自定义按钮
            addUserDefinedButton(element,inputList[i],inputList);
        }
    }
}

/**
 * 输入区域-自定义按钮
 */
function addUserDefinedButton(parentEle,inputElement,inputList) {
    let button = document.createElement("button");
    button.setAttribute("class","btn btn-info");
    button.addEventListener("click",getFields.bind(this,inputList,inputElement.elementReqName),false);
    let node = document.createTextNode(inputElement.elementPrompt);
    button.appendChild(node);
    parentEle.appendChild(button);
}

function getFields(inputList,reqId){
    var reqParm = {};
    for (let i=0;i<inputList.length;i++){
        if("dropDown"===inputList[i].elementType){
            let inputEle = document.getElementById(inputList[i].elementName);
            reqParm[inputList[i].elementName] = inputEle.options[inputEle.selectedIndex].text;
        }else if("input"===inputList[i].elementType){
            let inputEle = document.getElementById(inputList[i].elementName);
            reqParm[inputList[i].elementName] = inputEle.value;
        }
    }
    var requestParm = '{"reqMapping":"","reqName":"","reqType":"","curMenu":"","reqParm":{},"rtnCode":"0000","rtnMsg":""}';
    var requestObj = JSON.parse(requestParm);  //string -> obj
    requestObj.reqMapping = "clickReq";
    requestObj.reqName = "queryTable";
    requestObj.reqType = "webReq";
    requestObj.curMenu = "M002";
    requestObj.reqParm=reqParm;
    var requestJsonStr = JSON.stringify(requestObj); // obj -> string
    //sendJsonByAjax("getPageInfo",requestJsonStr,fillPageInfo);
    sendJsonByAjax(requestObj.reqMapping,requestJsonStr,addNewRecord);//刷新输出区域
}

function addNewRecord(rtnMap) {
    let element = document.getElementById("editModalBody");
    clearChildren("editModalBody");

    let tableRecordList = rtnMap.tableRecordList;
    if(tableRecordList==null) return;
    let recordMap = tableRecordList[0];//记录

    for (let i=0;i<tableRecordList.length;i++){
    //for(let field in recordMap){
        //console.log("属性：" + key + ",值：" + map[key]);
        let element_div = document.createElement("div");
        element_div.setAttribute("class","input-group mb-3");

        let element_span = document.createElement("span");
        element_span.setAttribute("class","input-group-addon");
        let span_node = document.createTextNode(tableRecordList[i].name);
        element_span.appendChild(span_node);
        element_div.appendChild(element_span);

        let element_input = document.createElement("input");
        element_input.setAttribute("id","modal"+tableRecordList[i].name);
        element_input.setAttribute("type","text");
        element_input.setAttribute("class","form-control");
        //element_input.value=recordMap[tableRecordList[i].name];
        element_div.appendChild(element_input);

        element.appendChild(element_div);
    }

    //<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    //<button type="button" class="btn btn-default" id="dele-submit-button">删除</button>
    //<button type="button" class="btn btn-primary" id="edit-submit-button">更改</button>

    let footerDiv = document.getElementById("editModalFooter");
    clearChildren("editModalFooter");

    let element_btn_del = document.createElement("button");
    element_btn_del.setAttribute("class","btn btn-primary");
    element_btn_del.addEventListener("click",editModalAddBtnOnClick.bind(this,tableRecordList),false);
    let btn_del_node = document.createTextNode("提交");
    element_btn_del.appendChild(btn_del_node);
    footerDiv.appendChild(element_btn_del);

    let element_btn_close = document.createElement("button");
    element_btn_close.setAttribute("class","btn btn-default");
    element_btn_close.setAttribute("data-dismiss","modal");
    let btn_close_node = document.createTextNode("关闭");
    element_btn_close.appendChild(btn_close_node);
    footerDiv.appendChild(element_btn_close);

    $('#editModal').modal('show'); //显示弹出窗
}

/**
 * 输入区域-按钮
 */
function addButton(parentEle,inputElement,inputList) {
    //<button class="pull-right btn btn-info" onclick="fun">查询</button>
    let button = document.createElement("button");
    //button.setAttribute("class","pull-right btn btn-info");
    button.setAttribute("class","btn btn-info");
    button.addEventListener("click",buttonClick.bind(this,inputList,inputElement.elementReqName),false);
    let node = document.createTextNode(inputElement.elementPrompt);
    button.appendChild(node);
    parentEle.appendChild(button);
}

/**
 * 输入区域-按钮执行方法
 * @param inputList
 */
function buttonClick(inputList,reqId) {
    var reqParm = {};
    for (let i=0;i<inputList.length;i++){
        if("dropDown"===inputList[i].elementType){
            let inputEle = document.getElementById(inputList[i].elementName);
            reqParm[inputList[i].elementName] = inputEle.options[inputEle.selectedIndex].text;
        }else if("input"===inputList[i].elementType){
            let inputEle = document.getElementById(inputList[i].elementName);
            reqParm[inputList[i].elementName] = inputEle.value;
        }
    }
    var requestParm = '{"reqMapping":"","reqName":"","reqType":"","curMenu":"","reqParm":{},"rtnCode":"0000","rtnMsg":""}';
    var requestObj = JSON.parse(requestParm);  //string -> obj
    requestObj.reqMapping = "clickReq";
    requestObj.reqName = reqId;
    requestObj.reqType = "webReq";
    requestObj.curMenu = curMenuId;
    requestObj.reqParm=reqParm;
    var requestJsonStr = JSON.stringify(requestObj); // obj -> string
    //sendJsonByAjax("getPageInfo",requestJsonStr,fillPageInfo);
    sendJsonByAjax(requestObj.reqMapping,requestJsonStr,fillOutPutArea);//刷新输出区域
}

/**
 * 输入区域-下拉输入框
 */
function addDropDown(parentEle,inputElement) {
    let element_div = document.createElement("div");
    element_div.setAttribute("class","input-group mb-3");

    let element_div2 = document.createElement("div");
    element_div2.setAttribute("class","input-group-prepend");
    let element_label = document.createElement("label");
    element_label.setAttribute("class","input-group-text");
    element_label.setAttribute("for",inputElement.elementName);
    //element_label.innerText("Options");
    let node = document.createTextNode(inputElement.elementPrompt);
    element_label.appendChild(node);
    element_div2.appendChild(element_label);
    element_div.appendChild(element_div2);

    let element_select = document.createElement("select");
    element_select.setAttribute("id",inputElement.elementName);
    element_select.setAttribute("class","custom-select");

    let valueMap = inputElement.elementEnumValue;
    for(let mapKey in valueMap){
        //console.log("属性：" + key + ",值：" + map[key]);
        let element_option = document.createElement("option");
        element_option.setAttribute("value",mapKey);
        let node2 = document.createTextNode(valueMap[mapKey]);
        element_option.appendChild(node2);
        element_select.appendChild(element_option);
    }
    element_div.appendChild(element_select);
    parentEle.appendChild(element_div);

    let split_span = document.createElement("span");
    split_span.setAttribute("style","white-space:pre;");
    let span_node = document.createTextNode("   ");
    split_span.appendChild(span_node);
    parentEle.appendChild(split_span);
}


/**
 * 输入区域-输入框
 */
function addInput(parentEle,inputElement) {
//<div class="input-group">
//   <span class="input-group-addon">@</span>
//   <input type="text" class="form-control" placeholder="twitterhandle">
//</div>
    let element_div = document.createElement("div");
    element_div.setAttribute("class","input-group mb-3");

    let element_span = document.createElement("span");
    element_span.setAttribute("class","input-group-addon");
    let span_node = document.createTextNode(inputElement.elementPrompt);
    element_span.appendChild(span_node);
    element_div.appendChild(element_span);

    let element_input = document.createElement("input");
    element_input.setAttribute("id",inputElement.elementName);
    element_input.setAttribute("type","text");
    element_input.setAttribute("class","form-control");
    if(typeof inputElement.elementWidth === 'number' && !isNaN(inputElement.elementWidth) ){
        if(inputElement.elementWidth > 0){
            element_input.style.width=inputElement.elementWidth+"px";
        }
    }
    if(typeof inputElement.elementHeight === 'number' && !isNaN(inputElement.elementHeight) ){
        if(inputElement.elementHeight > 0){
            element_input.style.height=inputElement.elementHeight+"px";
        }
    }
    element_div.appendChild(element_input);

    parentEle.appendChild(element_div);
}


/**
 * 填充输出区域
 * @param rtnMap
 */
function fillOutPutArea(rtnMap) {
    clearChildren("outputArea");//清空已有的输出区域标签
    let showType = rtnMap.showType;

    if("table"===showType){ fillOutPutAreaWithTable(rtnMap,false); }
    if("table-edit"===showType){ fillOutPutAreaWithTable(rtnMap,true); }
    if("div"===showType){ fillOutPutAreaWithDiv(rtnMap); }
}

/**
 * 填充输出区域_平铺
 * @param rtnMap
 */
function fillOutPutAreaWithDiv(rtnMap) {
    var elementList = rtnMap.elementList;
    if(elementList==null) return;

    var parent_ele = document.getElementById("outputArea");  //<table class="table" id="dataTable">
    let element_div = document.createElement("div"); // 弹窗显示图片
    element_div.style.display="none";
    let element_ul = document.createElement("ul");
    element_ul.setAttribute("id","viewerImages");
    element_div.appendChild(element_ul);
    for (let i=0;i<elementList.length;i++){
        //let element_div = document.createElement("div");
        //element_div.setAttribute("class","row");

        let element_img = document.createElement("img");
        //element_img.setAttribute("class","img-responsive center-block");
        element_img.setAttribute("class","img-thumbnail center-block");
        element_img.setAttribute("src","data:image/jpeg;base64,"+elementList[i].elementPrompt);
        element_img.setAttribute("width","100");//width="304" height="236"
        element_img.setAttribute("height","100");
        element_img.setAttribute("height","100");
        //element_div.appendChild(element_img);

//        let element_a = document.createElement("p");
//        //let note = document.createTextNode(elementList[i].elementName);
//        let note = document.createTextNode("  ");
//        element_a.appendChild(note);
//        element_div.appendChild(element_a);

        parent_ele.appendChild(element_img);

        let element_li = document.createElement("li");
        let element_img2 = document.createElement("img");
        element_img2.setAttribute("src","data:image/jpeg;base64,"+elementList[i].elementPrompt);
        element_img2.setAttribute("alt",elementList[i].elementName);
        element_li.appendChild(element_img2);
        element_ul.appendChild(element_li);
    }
    parent_ele.appendChild(element_div);

    var viewer = new Viewer(document.getElementById('viewerImages'));
}

/**
 * 填充输出区域_表格
 * @param rtnMap
 */
function fillOutPutAreaWithTable(rtnMap,isEdit) {
    var tableColList = rtnMap.tableColList;
    if(tableColList==null) return;

    var parent_ele = document.getElementById("outputArea");  //<table class="table" id="dataTable">
    var element_table = document.createElement("table");
    element_table.setAttribute("class","table");
    parent_ele.appendChild(element_table);

    var element_thead = document.createElement("thead");
    element_table.appendChild(element_thead);
    var element_thead_tr = document.createElement("tr");
    element_thead.appendChild(element_thead_tr);

    for (let i=0;i<tableColList.length;i++){
        let element_thead_th = document.createElement("th");
        let node = document.createTextNode(tableColList[i]);//字段名
        element_thead_th.appendChild(node);
        element_thead_tr.appendChild(element_thead_th);
    }

    if(isEdit){
        let element_thead_th = document.createElement("th");
        let node = document.createTextNode("edit");//字段名
        element_thead_th.appendChild(node);
        element_thead_tr.appendChild(element_thead_th);
    }

    let tableRecordList = rtnMap.tableRecordList;
    if(tableRecordList==null) return;

    let element_tbody = document.createElement("tbody");
    element_table.appendChild(element_tbody);
    for (let i=0;i<tableRecordList.length;i++){
        let recordMap = tableRecordList[i];//记录
        let element_table_tr = document.createElement("tr");
        element_tbody.appendChild(element_table_tr);

        for (let i=0;i<tableColList.length;i++){
            let element_table_td = document.createElement("td");
            let node = document.createTextNode(recordMap[tableColList[i]]);
            element_table_td.appendChild(node);
            element_table_tr.appendChild(element_table_td);
        }
        if(isEdit){
            let element_table_td = document.createElement("td");
            let button = document.createElement("button");
            button.setAttribute("class","btn btn-info");
            //button.setAttribute("data-toggle","modal");//data-toggle="modal" data-toggle="modal" data-target="#myModal"
            //button.setAttribute("data-target","#editModal");//data-toggle="modal" data-toggle="modal" data-target="#myModal"
            button.addEventListener("click",showEditModal.bind(this,recordMap),false);
            let node = document.createTextNode("edit");
            button.appendChild(node);
            element_table_td.appendChild(button);
            element_table_tr.appendChild(element_table_td);
        }
    }
}

/**
 * 填充导航栏
 * @param rtnMap
 */
function fillNavArea(rtnMap) {
    clearChildren("navList");//清空已有的菜单标签
    let element = document.getElementById("navList");
    element.setAttribute("class","breadcrumb");
    let nav_li = document.createElement("li");
    let nav_a = document.createElement("a");
    let node = document.createTextNode(curMenuText);
    nav_a.appendChild(node);
    nav_li.appendChild(nav_a);
    element.appendChild(nav_li);
}

function navClick(menuId) {
    alert(menuId);
}

/**
 * 删除传入标签ID下所有子标签
 * @param parentId 父标签ID
 */
function clearChildren(parentId) {
    let element = document.getElementById(parentId);
    let menuElements = element.childNodes;
    for(let i=menuElements.length-1;i>=0;i--){
        element.removeChild(menuElements[i]);
    }
}

/**
 * 显示编辑模态框
 * @param recordMap 点选的记录内容
 **/
function showEditModal(recordMap){
    let element = document.getElementById("editModalBody");
    clearChildren("editModalBody");

    for(let field in recordMap){
        //console.log("属性：" + key + ",值：" + map[key]);
        let element_div = document.createElement("div");
        element_div.setAttribute("class","input-group mb-3");

        let element_span = document.createElement("span");
        element_span.setAttribute("class","input-group-addon");
        let span_node = document.createTextNode(field);
        element_span.appendChild(span_node);
        element_div.appendChild(element_span);

        let element_input = document.createElement("input");
        element_input.setAttribute("id","modal"+field);
        element_input.setAttribute("type","text");
        element_input.setAttribute("class","form-control");
        element_input.value=recordMap[field];
        element_div.appendChild(element_input);

        element.appendChild(element_div);
    }

    //<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    //<button type="button" class="btn btn-default" id="dele-submit-button">删除</button>
    //<button type="button" class="btn btn-primary" id="edit-submit-button">更改</button>

    let footerDiv = document.getElementById("editModalFooter");
    clearChildren("editModalFooter");

    let element_btn_del = document.createElement("button");
    element_btn_del.setAttribute("class","btn btn-primary");
    element_btn_del.addEventListener("click",editModalDelBtnOnClick.bind(this,recordMap),false);
    let btn_del_node = document.createTextNode("删除");
    element_btn_del.appendChild(btn_del_node);
    footerDiv.appendChild(element_btn_del);

    let element_btn_upd = document.createElement("button");
    element_btn_upd.setAttribute("class","btn btn-primary");
    element_btn_upd.addEventListener("click",editModalUpdBtnOnClick.bind(this,recordMap),false);
    let btn_upd_node = document.createTextNode("更新");
    element_btn_upd.appendChild(btn_upd_node);
    footerDiv.appendChild(element_btn_upd);

    let element_btn_close = document.createElement("button");
    element_btn_close.setAttribute("class","btn btn-default");
    element_btn_close.setAttribute("data-dismiss","modal");
    let btn_close_node = document.createTextNode("关闭");
    element_btn_close.appendChild(btn_close_node);
    footerDiv.appendChild(element_btn_close);

    $('#editModal').modal('show'); //显示弹出窗
}

/**
 * 编辑模态框-删除记录按钮事件
 * @param recordMap 点选的记录内容
 **/
function editModalDelBtnOnClick(recordMap){
    if(window.confirm("确认是否删除该记录！")){

        let reqParm = {};
        let inputValue = getInputDivValue();
        reqParm["inputValue"]=inputValue;
        reqParm["originalRecordMap"]=recordMap;

//        let requestParm = '{"reqMapping":"","reqName":"","reqType":"","curMenu":"","reqParm":{},"rtnCode":"0000","rtnMsg":""}';
//        let requestObj = JSON.parse(requestParm);  //string -> obj
//        requestObj.reqMapping = "clickReq";
//        requestObj.reqName = "deleteRecord";
//        requestObj.reqType = "modalReq";
//        requestObj.curMenu = curMenuId;
//        requestObj.reqParm = reqParm;
//
//        let requestJsonStr = JSON.stringify(requestObj); // obj -> string
//        sendJsonByAjax("clickReq",requestJsonStr,delRecordSucFun);//刷新输出区域
        sendReq("clickReq","deleteRecord","modalReq",reqParm,delRecordSucFun);

        $('#editModal').modal('hide'); //隐藏弹出窗
    }
}

function delRecordSucFun(){
    alert("已成功删除该记录！");
}


/**
 * 编辑模态框-更新记录按钮事件
 * @param recordMap 点选的记录内容
 **/
function editModalUpdBtnOnClick(recordMap){
    let reqParm = {};

    let inputValue = getInputDivValue();

    let originalRecordMap = {};
    for(let field in recordMap){
        originalRecordMap[field] = recordMap[field];
    }

    let updatedRecordMap = {};
    for(let field in recordMap){
        let input = document.getElementById("modal"+field);
        updatedRecordMap[field] = input.value;
    }
//    for (let i=0;i<tableRecordList.length;i++){
//        let input = document.getElementById("modal"+tableRecordList[i].name);
//        updatedRecordMap[tableRecordList[i].name] = input.value;
//    }

    reqParm["inputValue"]=inputValue;
    reqParm["originalRecordMap"]=originalRecordMap;
    reqParm["updatedRecordMap"]=updatedRecordMap;

    sendReq("clickReq","updateRecord","modalReq",reqParm,updRecordSucFun);

    $('#editModal').modal('hide'); //隐藏弹出窗
}

function updRecordSucFun(){
    alert("已成功更新该记录！");
}

/**
 * 编辑模态框-删除记录按钮事件
 * @param recordMap 点选的记录内容
 **/
function editModalAddBtnOnClick(tableRecordList){
    let reqParm = {};

    let inputValue = getInputDivValue();

    let newRecordMap = {};
    for (let i=0;i<tableRecordList.length;i++){
        let input = document.getElementById("modal"+tableRecordList[i].name);
        newRecordMap[tableRecordList[i].name] = input.value;
    }

    reqParm["inputValue"]=inputValue;
    reqParm["newRecordMap"]=newRecordMap;

    sendReq("clickReq","insertRecord","modalReq",reqParm,addRecordSucFun);

    $('#editModal').modal('hide'); //隐藏弹出窗
}

function addRecordSucFun(){
    alert("已成功新增该记录！");
}

function sendReq(mapping,reqName,reqType,reqParm,sucFun){
    let requestParm = '{"reqMapping":"","reqName":"","reqType":"","curMenu":"","reqParm":{},"rtnCode":"0000","rtnMsg":""}';
    let requestObj = JSON.parse(requestParm);  //string -> obj
    requestObj.reqMapping = mapping;
    requestObj.reqName = reqName;
    requestObj.reqType = reqType;
    requestObj.curMenu = curMenuId;
    requestObj.reqParm = reqParm;
    let requestJsonStr = JSON.stringify(requestObj); // obj -> string
    sendJsonByAjax(mapping,requestJsonStr,sucFun);//刷新输出区域
}


/**
 * 取输入区域取值
 **/
function getInputDivValue(){
    let inputValue = {};
    for (let i=0;i<curInputList.length;i++){
        if("dropDown"===curInputList[i].elementType){
            let inputEle = document.getElementById(curInputList[i].elementName);
            inputValue[curInputList[i].elementName] = inputEle.options[inputEle.selectedIndex].text;
        }else if("input"===curInputList[i].elementType){
            let inputEle = document.getElementById(curInputList[i].elementName);
            inputValue[curInputList[i].elementName] = inputEle.value;
        }
    }
    return inputValue;
}