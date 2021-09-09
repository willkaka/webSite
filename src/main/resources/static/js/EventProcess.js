
/**
 * 设置点击事件处理方法
 * @param eventInfo
 * @param recordMap
 */
function executeEventMethod(eventInfo,recordMap) {
    //需要取出当前页面的所有数据传给后台：
    //1.选择的菜单/导航
    //2.输入信息
    //target：触发事件的元素。currentTarget：事件绑定的元素。
    var event = window.event || arguments[0];
    var eventEle = event.currentTarget;
    eventInfo.reqPage = 0;

    // 取事件源信息，并保存在eventInfo中
    eventInfo = getSourceElementInfo(eventEle, eventInfo);

    //仅页面处理即可，不需要请求主机
    if(eventInfo.type == "webButtonShowModal"){
        if(recordMap != null){
            eventInfo.recordMap = recordMap;
        }
        if( eventInfo.id == "addNewRecord"){
            showAddModal(eventInfo);
        }else{
            showEditModal(eventInfo);
        }
    }else{
//        eventInfo.type == "buttonReq";

        let data;        //请求数据
        let contentType;
        let processData;

        let requestParm = '{"eventId":"","reqParm":{}}';
        let requestObj = JSON.parse(requestParm);  //string -> obj
        requestObj.eventId = eventInfo.id;
        requestObj.curMenu = curMenuId;
        requestObj.userName = getCookie(userNameKey);
        let param = getCurPageInfo();
        requestObj.reqParm = param;
        // 把原值保存在 eventInfo.recordMap.curValue
        eventInfo = putChangeValue(param,eventInfo);
        requestObj.eventInfo = eventInfo; //事件信息
        let requestJsonStr = JSON.stringify(requestObj); // obj -> string

        if(eventInfo.type == "fileReq"){
            let formData = new FormData();
            let files = param.inputValue["file"];
            for(let j=0;j<files.length;j++){
                formData.append('fileList',files[j]);
            }
            formData.append('requestDto', requestJsonStr);
            data = formData;
            contentType = false;
            processData = false;
        }else{
            data = requestJsonStr;
            contentType = "application/json;charset=utf-8";
            processData = null;
        }
        sendJsonByAjax(eventInfo.type+'/'+eventInfo.id,'post', data,contentType,processData,sucFreshAll);
    }
}

/**
 * 取事件源信息，并保存在eventInfo中
 */
function getSourceElementInfo(eventEle, eventInfo){
    // input 输入框，change等事件
    if(eventEle.tagName == "INPUT"){
        // 当没有值时不
        if(eventEle.value == undefined || eventEle.value == "" || eventEle.value == null){
            return;
        }
        // 有值时，返回当前值
        eventInfo.selectedValue = eventEle.value;

    // 分页按钮，需要返回请求的页码 reqPage
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
    return eventInfo;
}

/**
 * 把记录的原值保存在 eventInfo.recordMap.curValue
 */
function putChangeValue(param,eventInfo){
    let inputValueMap = param["inputValue"]; //输入区域值
    let recordMap = eventInfo.recordMap;     //recordMap[field].value,原记录值
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
