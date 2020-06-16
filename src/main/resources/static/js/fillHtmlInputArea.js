
/**
 * 填充输入区域
 * @param rtnMap
 */
function fillInputArea() {
    clearChildren(inputArea);//清空已有的菜单标签"inputArea"

    let inputList = inputMap.inputList;
    if(inputList==null) return;
    let parentEle = document.getElementById(inputArea);
    for (let i=0;i<inputList.length;i++){
        writeWebElement(parentEle,inputList[i]);
    }
}



//<div class="responsive">
//    <div class="imgClass">
//      <a target="_blank" href="img_lights.jpg">
//        <img class="imgTag" src="//www.runoob.com/wp-content/uploads/2016/04/img_lights.jpg" alt="Northern Lights" width="600" height="400">
//      </a>
//      <div class="desc">Add a description of the image here</div>
//    </div>
//  </div>
//function writeImageDiv(parentEle,elementInfo){
//    let button = document.createElement("button");
//    button.setAttribute("class","inputArea_sub_button");
//    setAttr(button,elementInfo.attrMap); // 属性配置
//    setEventListener(button,elementInfo.eventInfoList); //事件
//
//    let span = document.createElement("span");
//    span.innerHTML = elementInfo.prompt;//名称
//    button.appendChild(span);
//
//    parentEle.appendChild(button);
//}



/**
  * 页面部分内容刷新
  **/
function fillChangedEle(){
    if(changedMap.isChanged){
        let changedEleMap = changedMap.changedEleMap;
        for(let eleId in changedEleMap){
            let ele = document.getElementById(eleId);
            let eleInfo = changedEleMap[eleId];
            clearChildren(eleId); //清空已有的内容
            if(eleInfo.chgType == "option"){
                if (JSON.stringify(eleInfo.dataMap) != JSON.stringify({})){
                    for(let value in eleInfo.dataMap){
                        let option = document.createElement("option");
                        option.setValue = value;
                        option.innerHTML = eleInfo.dataMap[value];
                        ele.appendChild(option);
                    }
                }
            }else if(eleInfo.chgType == "inputDataList"){
                ele.value = "";
                clearChildren("datalist"+eleId); //清空已有的内容
                var dataList = document.getElementById("datalist"+eleId);
                if (JSON.stringify(eleInfo.dataMap) != JSON.stringify({})){
                    for(let value in eleInfo.dataMap){
                        let option = document.createElement("option");
                        option.setAttribute("value",value);
                        dataList.appendChild(option);
                    }
                }
            }
        }
        changedMap.isChanged = false;
    }
}
