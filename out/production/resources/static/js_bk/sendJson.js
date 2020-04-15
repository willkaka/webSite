/**
 * 同步请求，并返回成功后的返回数据,post方式
 **/
function sendReqByAjaxPost(url,requestObj){
    let returnDataObj;
    $.ajax({
    　　async:false, //使用同步请求，因为异步请求不能将返回值传给全局变量；
    　　type: "post",
    　　url: url,
    　　data: JSON.stringify(requestObj),
        contentType:"application/json;charset=utf-8",
    　　success: function(returnDtoObj){ //返回的是object
            returnDataObj = returnDtoObj;
        },
        error: function(returnDtoJson) { alert("请求失败，返回信息："+returnDtoJson); }
    });
    return returnDataObj;
}


/**
 * 同步请求，并返回成功后的返回数据,get方式
 **/
function sendReqByAjaxGet(url,reqData){
    let returnData;
    $.ajax({
    　　async:false, //使用同步请求，因为异步请求不能将返回值传给全局变量；
    　　type: "get",
    　　url: url,
    　　data: reqData,
        contentType:"application/json;charset=utf-8",
    　　success: function(data){returnData = data;},
        error: function(ReqJsonDto) { alert("请求失败，返回信息："+ReqJsonDto); }
    });
    return returnData;
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

