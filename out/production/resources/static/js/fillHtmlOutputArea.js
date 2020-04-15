
//var outputMap = {"isChanged":true,"outputList":{}};

/**
 * 填充输出区域
 * @param rtnMap
 */
function fillOutputArea() {
    let isChanged = outputMap.isChanged;


    if(isChanged){
        let isClear = outputMap.isClear;
        if(isClear) clearChildren(outputArea);//清空已有的输出区域标签

        let showType = outputMap.showType;
        if("table"===showType){
            fillOutPutAreaWithTable();
        }
        if("div_image"===showType){
            fillOutPutAreaWithImgDiv();
        }
    }
}

/**
 * 填充输出区域_平铺
 * @param rtnMap
 */
function fillOutPutAreaWithImgDiv() {
    let elementList = outputMap.elementList;
    if(elementList==null) return;

    let parent_ele = document.getElementById("outputArea");
    for (let i=0;i<elementList.length;i++){
        let resp_div = document.createElement("div");
        resp_div.setAttribute("class","responsive");

        let img_div = document.createElement("div");
        img_div.setAttribute("class","imgDiv");
        resp_div.appendChild(img_div);

        let img_a = document.createElement("a");
        img_div.appendChild(img_a);

        let img_img = document.createElement("img");
        img_img.setAttribute("src","data:image/jpeg;base64,"+elementList[i].prompt);
        img_img.setAttribute("class","imgTag");
        img_a.appendChild(img_img);

        let desc_div = document.createElement("div");
        desc_div.setAttribute("class","imgDesc");
        desc_div.innerHTML = elementList[i].id;//名称
        img_div.appendChild(desc_div);

        parent_ele.appendChild(resp_div);
    }
}

/**
 * 填充输出区域_表格
 * @param rtnMap
 */
function fillOutPutAreaWithTable() {
    let tableColList = outputMap.tableColList;
    if(tableColList==null) return;

    let parent_ele = document.getElementById("outputArea");  //<table class="table" id="dataTable">
    let element_table = document.createElement("table");
    element_table.setAttribute("class","output_table");
    parent_ele.appendChild(element_table);

    let element_thead = document.createElement("thead");
    element_thead.setAttribute("class","output_table_thead");
    element_table.appendChild(element_thead);
    let element_thead_tr = document.createElement("tr");
    element_thead.appendChild(element_thead_tr);

    let element_thead_th = document.createElement("th");
    element_thead_th.setAttribute("class","output_table_th");
    element_thead_th.innerHTML = "序号";//名称
    element_thead_tr.appendChild(element_thead_th);
    for (let i=0;i<tableColList.length;i++){
        let element_thead_th = document.createElement("th");
        element_thead_th.setAttribute("class","output_table_th");
        element_thead_th.innerHTML = tableColList[i];//菜单名称
        element_thead_tr.appendChild(element_thead_th);
    }

    if(formatInfoMap.formatInfoList != null){
        let formatInfoList = formatInfoMap.formatInfoList;
        for (let i=0;i<formatInfoList.length;i++){
            if(formatInfoList[i].area == "outputArea"){
                let element_thead_th = document.createElement("th");
                element_thead_th.setAttribute("class","output_table_th");
                element_thead_th.innerHTML = formatInfoList[i].prompt;//字段名
                element_thead_tr.appendChild(element_thead_th);
            }
        }
    }
    let tableRecordList = outputMap.tableRecordList;
    if(tableRecordList==null) return;

    let element_tbody = document.createElement("tbody");
    element_table.appendChild(element_tbody);

    for (let i=0;i<tableRecordList.length;i++){
        let recordMap = tableRecordList[i];//记录
        let element_table_tr = document.createElement("tr");
        element_tbody.appendChild(element_table_tr);

        //序号
        let element_table_td = document.createElement("td");
        element_table_td.setAttribute("class","output_table_td_0");
        element_table_td.innerHTML = i+1;//字段名
        element_table_tr.appendChild(element_table_td);

        for (let j=0;j<tableColList.length;j++){
            let element_table_td = document.createElement("td");
            if(i%2==0){
                element_table_td.setAttribute("class","output_table_td_0");
            }else{
                element_table_td.setAttribute("class","output_table_td_1");
            }
            element_table_td.innerHTML = recordMap[tableColList[j]];//字段名
            element_table_tr.appendChild(element_table_td);
        }

        if(formatInfoMap.formatInfoList != null){
            let formatInfoList = formatInfoMap.formatInfoList;
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
                    writeWebElement(element_table_td,formatInfoList[i]);
                    element_table_tr.appendChild(element_table_td);
                }
            }
        }
    }
}