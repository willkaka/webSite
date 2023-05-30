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
    setEventListener(input,elementInfo.eventInfoList); //事件
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
 * 在父元素插入表格
 **/
function writeTable(parentEle,webTableInfo){
    //第一条记录的字段名称作为表头
    let tableRecordList = webTableInfo.recordList;
    if(tableRecordList==null) return;

    //分页按钮
    if(webTableInfo.withPage){
        for (let i=0;i<inputMap.inputList.length;i++){
            let events = inputMap.inputList[i].eventInfoList;
            for(let j=0;j<events.length;j++){
                if(events[j].withPage){
                    writePageButton(parentEle,events[j],webTableInfo);//分页按钮
                }
            }
        }
    }

    let element_table = document.createElement("table");
    element_table.setAttribute("class","output_table");
    parentEle.appendChild(element_table);

    //表头
    let element_thead = document.createElement("thead");
    element_thead.setAttribute("class","output_table_thead");
    element_table.appendChild(element_thead);
    let element_thead_tr = document.createElement("tr");
    element_thead.appendChild(element_thead_tr);

    let element_thead_th = document.createElement("th");
    element_thead_th.setAttribute("class","output_table_th");
    element_thead_th.innerHTML = "序号";//名称
    element_thead_tr.appendChild(element_thead_th);

    //取第一条记录的字段名称作为表头
    let colList;
    let colCnt=0;
    let colMap = tableRecordList[0];//记录
    for(let fieldName in colMap){
        let fieldAttrCol = colMap[fieldName];
        let element_thead_th = document.createElement("th");
        element_thead_th.setAttribute("class","output_table_th");
        if( fieldAttrCol.remarks != null && fieldAttrCol.remarks != "" && fieldAttrCol.remarks != "null"){
            element_thead_th.innerHTML = fieldAttrCol.remarks;
        }else{
            element_thead_th.innerHTML = fieldName;
        }
        element_thead_tr.appendChild(element_thead_th);
    }
    //是否有记录操作按钮
    let formatInfoList = webTableInfo.formatInfoList;
    if(formatInfoList != null){
        for (let i=0;i<formatInfoList.length;i++){
            if(formatInfoList[i].area == "outputArea"){
                let element_thead_th = document.createElement("th");
                element_thead_th.setAttribute("class","output_table_th");
                element_thead_th.innerHTML = formatInfoList[i].prompt;//字段名
                element_thead_tr.appendChild(element_thead_th);
            }
        }
    }

    //处理表格记录
    let element_tbody = document.createElement("tbody");
    element_tbody.setAttribute("class","output_table_tbody");
    element_table.appendChild(element_tbody);

    //遍历返回的表记录
    let begSeq = 0;//分页时记录序号累加前页记录数
    if(webTableInfo.withPage){
        begSeq = webTableInfo.pageSize * (webTableInfo.pageNow - 1);
    }
    for (let i=0;i<tableRecordList.length;i++){
        let recordMap = tableRecordList[i];//记录
        let element_table_tr = document.createElement("tr");
        element_tbody.appendChild(element_table_tr);

        //第1列，固定为”序号“
        let element_table_td = document.createElement("td");
        element_table_td.setAttribute("class","output_table_td_0");
        element_table_td.innerHTML = begSeq+i+1;//字段名
        element_table_tr.appendChild(element_table_td);
        for(let fieldName in recordMap){
            let fieldAttr = recordMap[fieldName];
            let element_table_td = document.createElement("td");
            if(i%2==0){
                element_table_td.setAttribute("class","output_table_td_0"); //td样式
            }else{
                element_table_td.setAttribute("class","output_table_td_1");
            }
            element_table_td.innerHTML = fieldAttr.value;//字段显示值
            element_table_tr.appendChild(element_table_td);
        }

        //是否有记录操作按钮
        if(formatInfoList != null){
            for (let i=0;i<formatInfoList.length;i++){
                if(formatInfoList[i].area == "outputArea"){
                    let element_table_td = document.createElement("td");
                    if(i%2==0){
                        element_table_td.setAttribute("class","output_table_td_0");
                    }else{
                        element_table_td.setAttribute("class","output_table_td_1");
                    }

                    if(formatInfoList[i].eventInfoList != null){
                        for(let j=0;j<formatInfoList[i].eventInfoList.length;j++){
                            let eventInfo = formatInfoList[i].eventInfoList[j];
                            eventInfo.recordMap = recordMap;
                        }
                    }
                    writeWebElementRoute(element_table_td,formatInfoList[i]);
                    element_table_tr.appendChild(element_table_td);
                }
            }
        }
    }
}

//分页按钮
function writePageButton(parentEle,eventInfo,webTableInfo){
    let events =new Array();
    events[0] = eventInfo;
    let page_div = document.createElement("div");
    page_div.setAttribute("class","page-box");

    let totalPage = parseInt((webTableInfo.totalCount-1)/webTableInfo.pageSize)+1;
    let pageNow = webTableInfo.pageNow;
    let page_pre_a = document.createElement("a");
    page_pre_a.setAttribute("class","page-button");
    page_pre_a.innerHTML = "上一页";
    setEventListener(page_pre_a,events); //事件
    page_div.appendChild(page_pre_a);

    let totalShowNum = 9;//必须为大于4的整奇数
    let lrShowNum = Math.floor((totalShowNum - 4) / 2); //向下取整数 2

    let pageList = new Array();
    pageList[0] = 1;
    if(totalPage <= totalShowNum){
        for(let i=0,page=1;page <= totalPage;page++,i++){
            pageList[i] = page;
        }
    }else if(pageNow <= totalShowNum-2-lrShowNum){
        for(let i=1,page=2;page<=totalShowNum-2;page++,i++){
            pageList[i] = page;
        }
        pageList[totalShowNum-2]="...";
        pageList[totalShowNum-1]=totalPage;
    }else if(pageNow <= totalPage - (totalShowNum-4)){
        pageList[1]="...";
        for(let i=2,page=pageNow-lrShowNum;page<=pageNow+lrShowNum;page++,i++){
            pageList[i] = page;
        }
        pageList[totalShowNum-2]="...";
        pageList[totalShowNum-1]=totalPage;
    }else if(pageNow > totalPage - (totalShowNum-4)){
        pageList[1]="...";
        for(let i=2,page=totalPage - (totalShowNum-4)-1;page<=totalPage;page++,i++){
            pageList[i] = page;
        }
    }
    for(let i = 0; i < pageList.length; i++) {
        if(pageList[i]=="..."){
            writePageA(page_div,"...",pageNow,null);
        }else{
            writePageA(page_div,pageList[i],pageNow,events);
        }
    }
    let page_next_a = document.createElement("a");
    page_next_a.setAttribute("class","page-button");
    page_next_a.innerHTML = "下一页";
    setEventListener(page_next_a,events); //事件
    page_div.appendChild(page_next_a);
    parentEle.appendChild(page_div);
}

function writePageA(page_div,pageNum,pageNow,events){
    if(pageNum==pageNow){
        let page_strong = document.createElement("strong");
        page_strong.setAttribute("class","page-button-strong");
        page_strong.innerHTML = String(pageNum);
        page_div.appendChild(page_strong);
    }else{
        let page_a = document.createElement("a");
        page_a.setAttribute("class","page-button");
        page_a.innerHTML = String(pageNum);
        setEventListener(page_a,events); //事件
        page_div.appendChild(page_a);
    }
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
            //由于bind的特性，需要copy对象绑定。
            element.addEventListener(eventInfo.event,executeEventMethod.bind(this,copy(eventInfo),element),false);
        }
    }
}
