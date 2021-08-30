
/**
 * 在执行完请求后，按返回信息执行后续动作
 *   例:后台成功新增记录后，关闭对应的弹窗；
 *      弹窗提示，成功/失败信息等。
 */
function afterRequest(ReturnDto){
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