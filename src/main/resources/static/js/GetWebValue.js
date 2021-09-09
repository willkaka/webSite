
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

    //取标签为 input 的页面元素
    let map = getNodeValueMap("input");
    for(let value in map){
        nodeValueMap[value] = map[value];
    }

    //取标签为 select 的页面元素
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

//    let webValueList = new Array;
//    let webElementValue = new Object; // menu,area,type,value

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
            if("file"===nodeEle.type){
                nodeValueMap[nodeEle.id] = nodeEle.files;//支持多文件上传
            }else{
                nodeValueMap[nodeEle.id] = nodeEle.value;
            }
        }
    }
    return nodeValueMap;
}