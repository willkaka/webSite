/**
 * 生成页面元素路由
 **/
function writeWebElementRoute(parentEle,elementInfo){
    if(elementInfo.type == "input") writeInput(parentEle,elementInfo);
    if(elementInfo.type == "inputFile") writeInputFile(parentEle,elementInfo);
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
 * 在父元素插入生成的输入框 div label/input
 **/
function writeInputFile(parentEle,elementInfo){
    let groupDiv = document.createElement("div");
    groupDiv.setAttribute("class","inputArea_div_grp");

    let label = document.createElement("label");
    label.setAttribute("class","inputArea_sub_label");
    label.innerHTML = elementInfo.prompt;//名称
    groupDiv.appendChild(label);

    let input = document.createElement("input");
    input.setAttribute("id",elementInfo.id);
    input.setAttribute("type","file");
    input.setAttribute("class","inputArea_sub_input");
    input.setAttribute("placeholder",elementInfo.prompt);
    input.setAttribute("multiple",null);
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
            let triggerInfoList = eventInfo.triggerInfoList;
            if( triggerInfoList != null){
                for (let j=0;i<triggerInfoList.length;j++){
                    element.addEventListener(eventInfo.event,executeEventMethod.bind(this,eventInfo,eventInfo.recordMap),false);
                }
            }else{
                element.addEventListener(eventInfo.event,executeEventMethod.bind(this,eventInfo,eventInfo.recordMap),false);
            }
        }
    }
}
