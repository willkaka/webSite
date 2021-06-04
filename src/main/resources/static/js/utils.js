/**
 * 生成页面元素路由
 **/
function writeWebElementRoute(parentEle,elementInfo){
    if(elementInfo.type == "input") writeInput(parentEle,elementInfo);
    if(elementInfo.type == "dropDown") writeDropDown(parentEle,elementInfo);
    if(elementInfo.type == "inputDataList") writeInputDataList(parentEle,elementInfo);
    if(elementInfo.type == "selectOption") writeSelectOption(parentEle,elementInfo);
    if(elementInfo.type == "multipleSelect") writeMultipleSelect(parentEle,elementInfo);
    if(elementInfo.type == "button") writeButton(parentEle,elementInfo);
}

/**
 * 在父元素插入生成的输入框 div label/input
 **/
function writeInput(parentEle,elementInfo){
    let groupDiv = document.createElement("div");
    groupDiv.setAttribute("class","inputArea_div_grp");

    let label = document.createElement("label");
    label.setAttribute("class","inputArea_sub_label");
    label.innerHTML = elementInfo.prompt;//名称
    groupDiv.appendChild(label);

    let input = document.createElement("input");
    input.setAttribute("id",elementInfo.id);
    input.setAttribute("class","inputArea_sub_input");
    input.setAttribute("placeholder",elementInfo.prompt);
    if(null != elementInfo.defaultValue){
        input.setAttribute("value",elementInfo.defaultValue);
    }
    if(null != elementInfo.attrMap){
        setAttr(input,elementInfo.attrMap);
    }
    groupDiv.appendChild(input);

    parentEle.appendChild(groupDiv);
}

/**
 * 在父元素插入生成的下拉选择框 div label input/option
 **/
function writeInputDataList(parentEle,elementInfo){
    let groupDiv = document.createElement("div");
    groupDiv.setAttribute("class","inputArea_div_grp");

    let label = document.createElement("label");
    label.setAttribute("class","inputArea_sub_label");
    label.innerHTML = elementInfo.prompt;//名称
    groupDiv.appendChild(label);

    let input = document.createElement("input");
    input.setAttribute("id",elementInfo.id);
    input.setAttribute("list","datalist"+elementInfo.id);
    input.setAttribute("class","inputArea_sub_select");
    setEventListener(input,elementInfo.eventInfoList); //事件
    if(null != elementInfo.attrMap){
        setAttr(input,elementInfo.attrMap);
    }
    let dataList = document.createElement("datalist");
    dataList.setAttribute("id","datalist"+elementInfo.id);

    let dataMap = elementInfo.dataMap;
    for(let value in dataMap){
        let option = document.createElement("option");
        option.setAttribute("value",value);
        option.setAttribute("name",dataMap[value]);
        //option.innerHTML = dataMap[value];
        dataList.appendChild(option);
    }
    groupDiv.appendChild(input);
    groupDiv.appendChild(dataList);
    parentEle.appendChild(groupDiv);
}

function writeMultipleSelect(parentEle,elementInfo){

    let groupDiv = document.createElement("div");
    groupDiv.setAttribute("class","inputArea_div_grp");

    let label = document.createElement("label");
    label.setAttribute("class","inputArea_sub_label");
    label.innerHTML = elementInfo.prompt;//名称
    groupDiv.appendChild(label);

    let select = document.createElement("select");
    select.setAttribute("id",elementInfo.id);
    select.setAttribute("multiple","multiple");
    setEventListener(select,elementInfo.eventInfoList); //事件
    if(null != elementInfo.attrMap){
        setAttr(select,elementInfo.attrMap);
    }
    groupDiv.appendChild(select);
    parentEle.appendChild(groupDiv);

    let selectOptions = [];
    let dataMap = elementInfo.dataMap;
    for(let mapKey in dataMap){
        selectOptions.push({label: dataMap[mapKey], title: dataMap[mapKey], value: mapKey});
    }
    let multipleSelect2 = $("#"+elementInfo.id);
    multipleSelect2.multiselect({
                                enableFiltering: true,
                                includeSelectAllOption: true,
                                nonSelectedText: '请选择',
                                numberDisplayed: 1,
                                nSelectedText: '个已选!',
                                selectAllText: '全选',
                                allSelectedText: '已全选',
                                selectedClass: 'active multiselect-selected',
                                optionClass: function(element) {
                                    var value = $(element).val();
                                    return value%2 == 0?'even':'odd'; }
                           });
    multipleSelect2.multiselect('dataprovider', selectOptions);
}

/**
 * 在父元素插入生成的下拉选择框 div label select/option
 <select>
   <option value="1">Volvo</option>
   <option value="2">Saab</option>
 </select>
 **/
function writeSelectOption(parentEle,elementInfo){
    let groupDiv = document.createElement("div");
    groupDiv.setAttribute("class","inputArea_div_grp");

    let label = document.createElement("label");
    label.setAttribute("class","inputArea_sub_label");
    label.innerHTML = elementInfo.prompt;//名称
    groupDiv.appendChild(label);

    let select = document.createElement("select");
    select.setAttribute("id",elementInfo.id);
    select.setAttribute("class","inputArea_sub_select");
    setEventListener(select,elementInfo.eventInfoList); //事件
    if(null != elementInfo.attrMap){
        setAttr(select,elementInfo.attrMap);
    }

    let dataMap = elementInfo.dataMap;
    for(let value in dataMap){
        let option = document.createElement("option");
        option.setAttribute("value",value);
        option.innerHTML = dataMap[value];
        select.appendChild(option);
    }
    groupDiv.appendChild(select);
    parentEle.appendChild(groupDiv);
}

/**
 * 在父元素插入生成的下拉选择框 div label select/option
 **/
function writeDropDown(parentEle,elementInfo){
    let groupDiv = document.createElement("div");
    groupDiv.setAttribute("class","inputArea_div_grp");

    let label = document.createElement("label");
    label.setAttribute("class","inputArea_sub_label");
    label.innerHTML = elementInfo.prompt;//名称
    groupDiv.appendChild(label);

    let select = document.createElement("select");
    select.setAttribute("id",elementInfo.id);
    select.setAttribute("class","inputArea_sub_select");
    //setAttr(select,elementInfo.attrMap); // 属性配置
    setEventListener(select,elementInfo.eventInfoList); //事件
    if(null != elementInfo.attrMap){
        setAttr(select,elementInfo.attrMap);
    }

    let dataMap = elementInfo.dataMap;
    for(let value in dataMap){
        let option = document.createElement("option");
        option.setAttribute("value",value);
        option.innerHTML = dataMap[value];
        select.appendChild(option);
    }
    groupDiv.appendChild(select);
    parentEle.appendChild(groupDiv);
}

/**
 * 在父元素插入生成的按钮
 **/
function writeButton(parentEle,elementInfo){
    let button = document.createElement("button");
    button.setAttribute("class","inputArea_sub_button");
    setEventListener(button,elementInfo.eventInfoList); //事件
    if(null != elementInfo.attrMap){
        setAttr(button,elementInfo.attrMap); // 属性配置
    }

    let span = document.createElement("span");
    span.innerHTML = elementInfo.prompt;//名称
    button.appendChild(span);

    parentEle.appendChild(button);
}

/**
 * 设置元素属性
 **/
function setAttr(element,attrMap){
    //属性
    if(attrMap != null){
        for(let attrName in attrMap){ //key:attrName,value:attrMap[attrName]
            element.setAttribute(attrName,attrMap[attrName]);
        }
    }
}

/**
 * 设置元素事件
 **/
function setEventListener(element,eventInfoList){
    //事件
    if(eventInfoList != null && eventInfoList.length > 0 ){
        for (let i=0;i<eventInfoList.length;i++){
            let eventInfo = eventInfoList[i];
            element.addEventListener(eventInfo.event,setEventPrcMethod.bind(this,eventInfo,eventInfo.recordMap),false);
        }
    }
}

/**
 * 设置点击事件处理方法
 * @param eventInfo
 * @param recordMap
 */
function setEventPrcMethod(eventInfo,recordMap) {
    //需要取出当前页面的所有数据传给后台：
    //1.选择的菜单/导航
    //2.输入信息
    //target：触发事件的元bai素。currentTarget：事件绑定的元素。
    var event = window.event || arguments[0];
    var eventEle = event.currentTarget;
    eventInfo.reqPage = 0;
    if(eventEle.tagName == "INPUT"){
        if(eventEle.value == undefined || eventEle.value == "" || eventEle.value == null){
            return;
        }
        eventInfo.selectedValue = eventEle.value;
    }else if(eventEle.tagName == "A" && eventInfo.withPage){ //分页按钮请求
        if("上一页" == eventEle.innerHTML){
            eventInfo.reqPage = outputMap.pageNow - 1 < 0?0:outputMap.pageNow - 1;
        }else if("下一页" == eventEle.innerHTML){
            let totalPage = parseInt((outputMap.totalCount-1)/outputMap.pageSize)+1;
            eventInfo.reqPage = outputMap.pageNow + 1 > totalPage?totalPage:outputMap.pageNow + 1;
        }else{
            eventInfo.reqPage = eventEle.innerHTML;
        }
    }else{
        if(eventInfo.type == "menuReq"){
            curMenuId = eventInfo.id;
            document.getElementById("navSpan").innerHTML= eventInfo.id;
        } else if(eventInfo.type == "webDataReq"){
            var selectedValue = $('select  option:selected').val();
            eventInfo.selectedValue = selectedValue;
        }
    }

    //仅页面处理即可，不需要请求主机
    if(eventInfo.type == "webButtonShowModal"){
        eventInfo.recordMap = recordMap;
        showEditModal(eventInfo);
    }else{
        let requestParm = '{"eventId":"","reqParm":{}}';
        let requestObj = JSON.parse(requestParm);  //string -> obj
        requestObj.eventId = eventInfo.id;
        requestObj.curMenu = curMenuId;
        let param = getCurPageInfo();
        requestObj.reqParm = param;
        eventInfo = putChangeValue(param,eventInfo);
        requestObj.eventInfo = eventInfo; //事件信息
        let requestJsonStr = JSON.stringify(requestObj); // obj -> string
        sendJsonByAjax(eventInfo.type+'/'+eventInfo.id,requestJsonStr,sucFreshAll);//刷新输出区域
    }
}

function putChangeValue(param,eventInfo){
    let inputValueMap = param["inputValue"];
    let recordMap = eventInfo.recordMap;  //recordMap[field].value
    if(inputValueMap != null && recordMap != null){
        for(let modalFieldName in inputValueMap){ //key:attrName,value:attrMap[attrName]
            if("modal" === modalFieldName.substring(0,5) ) {
                let fieldName = modalFieldName.substring(5);
                recordMap[fieldName].curValue = inputValueMap[modalFieldName];
            }
        }
    }
    return eventInfo;
}

/**
 * 用于自动刷新时调取的查询功能
 */
function callMethod(eventInfo) {
    //target：触发事件的元bai素。currentTarget：事件绑定的元素。
    let event = window.event || arguments[0];
    let eventEle = event.currentTarget;
    if(eventEle.tagName == "INPUT"){
        if(eventEle.value == undefined || eventEle.value == "" || eventEle.value == null){
            return;
        }
        eventInfo.selectedValue = eventEle.value;
    }else{
        if(eventInfo.type == "menuReq"){
            curMenuId = eventInfo.id;
            document.getElementById("navSpan").innerHTML= eventInfo.id;
        } else if(eventInfo.type == "webDataReq"){
            var selectedValue = $('select  option:selected').val();
            eventInfo.selectedValue = selectedValue;
        }
    }

    //仅页面处理即可，不需要请求主机
    if(eventInfo.type == "webButtonShowModal"){
        //eventInfo.recordMap = recordMap;
        showAddModal(eventInfo);
    }else{
        let requestParm = '{"eventId":"","reqParm":{}}';
        let requestObj = JSON.parse(requestParm);  //string -> obj
        requestObj.eventId = eventInfo.id;
        requestObj.curMenu = curMenuId;
        requestObj.userName = getCookie(userNameKey);
        let param = getCurPageInfo();
        requestObj.reqParm = param;
        requestObj.eventInfo = eventInfo; //事件信息
        let requestJsonStr = JSON.stringify(requestObj); // obj -> string
        sendJsonByAjax(eventInfo.type+'/'+eventInfo.id,requestJsonStr,sucFreshAll);//刷新输出区域
    }
}

/**
 * 发送json报文到后台
 * @param requestUrl
 * @param requestParam
 * @param sucfn
 */
function sendJsonByAjax(requestUrl,requestParam,sucFun) {
    $.ajax({
        type: "post",
        url: requestUrl,
        data: requestParam,//ReqJsonDto
        contentType:"application/json;charset=utf-8",
        success:function (ReturnDto) {
            if (ReturnDto.rtnCode === '0000') {
                sucFun(ReturnDto);
            } else {
                alert("请求成功，但后台处理失败!\n返 回 码:"+ReturnDto.rtnCode + "\n失败信息:" + ReturnDto.rtnMsg);
            }
        },
        error:function (ReturnDto) {
            alert("请求失败，返回信息："+ReturnDto);
        }
    });
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
        let webNextOprMap = ReturnDto.webNextOpr;

        if(webNextOprMap["type"] == "hide"){
            if(webNextOprMap["alert"] == "true"){
                if(ReturnDto.rtnCode == "0000"){
                    alert(webNextOprMap["sucMsg"]);
                    hide(webNextOprMap["hideEle"]); //swBackGround
                }else{
                    alert("处理失败，返回码:"+ReturnDto.rtnCode+",返回信息:"+ReturnDto.rtnMsg);
                }
            }
        }
        if(webNextOprMap["callEven"] != null){
            callMethod(webNextOprMap["callEven"]); //调用某事件，实现刷新功能。
        }
        if(webNextOprMap["execFun"] != null && webNextOprMap["execFun"] != ""){
            if(webNextOprMap["param"] != null){
                //webNextOprMap["execFun"].call(webNextOprMap["param"]);
                eval(webNextOprMap["execFun"]+'("'+webNextOprMap["param"]+'")');
            }else{
                eval(webNextOprMap["execFun"]+'()');
            }
        }
    }
}

/**
 * 取页面数值
 * @return Map
 **/
function getCurPageInfo(){
    let pageInfoMap = {"curMenuId":curMenuId,"inputValue":getInputValueMap()};
    return pageInfoMap;
}

/**
 * 取输入区域取值
 * @return Map
 **/
function getInputValueMap(){
    let nodeValueMap = {};
    let map = getNodeValueMap("input");
    for(let value in map){
        nodeValueMap[value] = map[value];
    }
    map = getNodeValueMap("select");
    for(let value in map){
        nodeValueMap[value] = map[value];
    }
    return nodeValueMap;
}

/**
 * 取页面指定标签的当前值
 **/
function getNodeValueMap(nodeTag){
    let nodeValueMap = {};
    var nodeList = document.querySelectorAll(nodeTag);
    for (let i=0;i<nodeList.length;i++){
        let nodeEle = nodeList[i];
        if("select"===nodeTag){
            //判断select/option是否为多选
            let isMultipleSelect = jQuery("#"+nodeEle.id).attr("multiple");
            if("multiple" == isMultipleSelect){
                let selectedValueMap = [];
                for(optionIndex=0;optionIndex<nodeEle.length;optionIndex++){
                    if(nodeEle.options[optionIndex].selected){
                        selectedValueMap.push(nodeEle.options[optionIndex].value);
                    }
                }
                nodeValueMap[nodeEle.id] = selectedValueMap;
            }else{
                let index = nodeEle.selectedIndex; // 选中索引
                if(index >= 0){
                    let text = nodeEle.options[index].text; // 选中文本
                    let value = nodeEle.options[index].value; // 选中值
                    nodeValueMap[nodeEle.id] = value;
                }else{
                    nodeValueMap[nodeEle.id] = "";
                }
            }
        }else if("input"===nodeTag){
            nodeValueMap[nodeEle.id] = nodeEle.value;
        }
    }
    return nodeValueMap;
}

/**
 * 删除传入标签ID下所有子标签
 * @param parentId 父标签ID
 */
function clearChildren(parentId) {
    let element = document.getElementById(parentId);
    if(element!=null){
        let menuElements = element.childNodes;
        for(let i=menuElements.length-1;i>=0;i--){
            element.removeChild(menuElements[i]);
        }
    }
}

function clearChildrenExcept(element,tagName) {
    if(element!=null){
        let menuElements = element.childNodes;
        for(let i=menuElements.length-1;i>=0;i--){
            if(tagName == menuElements[i].tagName || menuElements[i].tagName == "LABEL"){ continue; }
            element.removeChild(menuElements[i]);
        }
    }
}

/**
 * 不显示指定标签
 * @param eleId
 */
function hide(eleId){
    let ele = document.getElementById(eleId);
    ele.style.display="none";
}

/**
 * 显示指定标签
 * @param eleId
 */
function display(eleId){
    let ele = document.getElementById(eleId);
    ele.style.display="";
}

function setCookie(cname,cvalue,exdays){
    let d = new Date();
    d.setTime(d.getTime()+(exdays*24*60*60*1000));
    let expires = "expires="+d.toGMTString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function delCookie(cname){
    let expires = "expires=Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = cname + "=" + getCookie(cname) + "; " + expires;
}

function getCookie(cname){
    let name = cname + "=";
    let ca = document.cookie.split(';');
    for(let i=0; i<ca.length; i++){
        let c = ca[i].trim();
        if (c.indexOf(name)==0) return c.substring(name.length,c.length);
    }
    return "";
}

function showLoginWindow(){
    if(getCookie(userNameKey) != null && getCookie(userNameKey) != ""){
        alert("您 " + getCookie(userNameKey) + " 已登录！");
        return;
    }

    showLoginModal();
}

function setUserNameIntoCookie(cvalue){
    setCookie(userNameKey,cvalue,1)
}