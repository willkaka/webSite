
/**
 * 显示modal后，将显示的字段保存在inputList中，使请求时可以一并取到modal上的字段值。
 **/
function putInputList(id,seq,fun,area,type){
    //var inputMap = {"isChanged":false,"inputList":{}}; /*  */
    let inputList = inputMap.inputList;
    let found = false;
    for (let i=0;i<inputList.length;i++){
        if(inputList[i].id == id){
            inputList[i].seq = seq;
            inputList[i].function = fun;
            inputList[i].area = area;
            inputList[i].type = type;
            inputList[i].prompt = prompt;

            found = true;
        }
    }
    if(!found){
        let newElement = {"id":id,"seq":seq,"function":fun,"area":area,"type":type,"prompt":prompt};
        inputList.push(newElement);
    }
}

function writeWebElement(parentEle,elementInfo){
    if(elementInfo.type == "input") writeInput(parentEle,elementInfo);
    if(elementInfo.type == "dropDown") writeDropDown(parentEle,elementInfo);
    if(elementInfo.type == "inputDataList") writeInputDataList(parentEle,elementInfo);
    if(elementInfo.type == "button") writeButton(parentEle,elementInfo);
}

//<div class="inputArea_div_grp">
//    <label class="inputArea_sub_label">输入1</label>
//    <input class="inputArea_sub_input" placeholder="输入1...">
//</div>
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
    groupDiv.appendChild(input);

    parentEle.appendChild(groupDiv);
}


/**
 * 
 * @param {*} parentEle 
 * @param {*} elementInfo 
 */
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
    let dataList = document.createElement("datalist");
    dataList.setAttribute("id","datalist"+elementInfo.id);

    let dataMap = elementInfo.dataMap;
    for(let value in dataMap){
        let option = document.createElement("option");
        option.setAttribute("value",value);
        dataList.appendChild(option);
    }
    groupDiv.appendChild(input);
    groupDiv.appendChild(dataList);
    parentEle.appendChild(groupDiv);
}

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

    let dataMap = elementInfo.dataMap;
    for(let value in dataMap){
        let option = document.createElement("option");
        //option.setValue = value;
        option.setAttribute("value",value);
        option.innerHTML = dataMap[value];
        select.appendChild(option);
    }
    groupDiv.appendChild(select);
    parentEle.appendChild(groupDiv);
}

//function func(){
// //获取被选中的option标签
// var vs = $('select  option:selected').val();
//}

//<button class="inputArea_sub_button"><span>按钮1</span></button>
function writeButton(parentEle,elementInfo){
    let button = document.createElement("button");
    button.setAttribute("class","inputArea_sub_button");
    setAttr(button,elementInfo.attrMap); // 属性配置
    setEventListener(button,elementInfo.eventInfoList); //事件

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
            element.addEventListener(eventInfo.event,eventProcessMethod.bind(this,eventInfo,eventInfo.recordMap),false);
        }
    }
}

/**
 * 点击事件处理方法
 * @param menuId
 */
function eventProcessMethod(eventInfo,recordMap) {
    //target：触发事件的元bai素。currentTarget：事件绑定的元素。
    var event = window.event || arguments[0];
    var eventEle = event.currentTarget;
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
        eventInfo.recordMap = recordMap;
        showEditModal(eventInfo);
    }else{
        let requestParm = '{"eventId":"","reqParm":{}}';
        let requestObj = JSON.parse(requestParm);  //string -> obj
        requestObj.eventId = eventInfo.id;
        requestObj.curMenu = curMenuId;
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
}


function getCurPageInfo(){
    let pageInfoMap = {"curMenuId":curMenuId,"inputValue":getInputValueMap()};
    return pageInfoMap;
}


/**
 * 取输入区域取值
 * @return Map
 **/
function getInputValueMap(){
    let curInputList = inputMap.inputList;
    let inputValue = {};
    for (let i=0;i<curInputList.length;i++){
        let inputEle = document.getElementById(curInputList[i].id);
        if("dropDown"===curInputList[i].type){
            let index = inputEle.selectedIndex; // 选中索引
            if(index >= 0){
                let text = inputEle.options[index].text; // 选中文本
                let value = inputEle.options[index].value; // 选中值
                inputValue[curInputList[i].id] = value;
            }else{
                inputValue[curInputList[i].id] = "";
            }
        }else if("input"===curInputList[i].type){
            inputValue[curInputList[i].id] = inputEle.value;
        }else if("inputDataList"===curInputList[i].type){
            inputValue[curInputList[i].id] = inputEle.value;
        }
    }
    return inputValue;
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

function hidder(eleId){
    let ele = document.getElementById(eleId);
    ele.style.display="none";
}

function display(eleId){
    let ele = document.getElementById(eleId);
    ele.style.display="";
}