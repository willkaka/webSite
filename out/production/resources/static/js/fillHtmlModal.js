/**
 * 显示编辑模态框
 * @param recordMap 点选的记录内容
 **/
function showEditModal(eventInfo){
    let recordMap = eventInfo.recordMap;
    //弹窗标题和关闭按钮
    clearChildren("swHeader");
    let header = document.getElementById("swHeader");
    let header_span = document.createElement("span");
    header_span.innerHTML = "编辑";//标题
    header.appendChild(header_span);
    let header_close = document.createElement("div");
    header_close.setAttribute("class","subWidowHeaderCloseBtn");
    header_close.addEventListener("click",hide.bind(this,"swBackGround"),false);
    header_close.innerHTML = "X";//标题
    header.appendChild(header_close);

    clearChildren("swBody");
    let body = document.getElementById("swBody");
    let index = 0;
    for(let field in recordMap){
        let groupDiv = document.createElement("div");
        groupDiv.setAttribute("class","inputArea_div_grp_inline");

        let label = document.createElement("label");
        label.setAttribute("class","inputArea_sub_label");
        label.innerHTML = field;//名称
        groupDiv.appendChild(label);

        let input = document.createElement("input");
        input.setAttribute("id","modal"+field);
        input.setAttribute("class","inputArea_sub_input");
        input.setAttribute("value",recordMap[field]);
        groupDiv.appendChild(input);

        body.appendChild(groupDiv);

        index = index + 1;
        //putInputList("input","modal"+field);
        //putInputList("modal"+field,index,curMenuId,"modalArea","input"); //id,seq,fun,area,type
    }

    let footerDiv = document.getElementById("swFooter");
    clearChildren("swFooter");

    if(formatInfoMap.formatInfoList != null){
        let formatInfoList = formatInfoMap.formatInfoList;
        for (let i=0;i<formatInfoList.length;i++){
            if(formatInfoList[i].area == "modalArea"){
                writeWebElementRoute(footerDiv,formatInfoList[i]);
            }
        }
    }

    display("swBackGround");
}

/**
 * 显示编辑模态框
 * @param recordMap 点选的记录内容
 **/
function showAddModal(eventInfo){
    let recordMap = eventInfo.recordMap;
    //弹窗标题和关闭按钮
    clearChildren("swHeader");
    let header = document.getElementById("swHeader");
    let header_span = document.createElement("span");
    header_span.innerHTML = "新增记录";//标题
    header.appendChild(header_span);
    let header_close = document.createElement("div");
    header_close.setAttribute("class","subWidowHeaderCloseBtn");
    header_close.addEventListener("click",hide.bind(this,"swBackGround"),false);
    header_close.innerHTML = "X";//标题
    header.appendChild(header_close);

    clearChildren("swBody");
    let body = document.getElementById("swBody");
    let index = 0;
    //for(let field in recordMap){
    for (let i=0;i<recordMap.length;i++){
        let groupDiv = document.createElement("div");
        groupDiv.setAttribute("class","inputArea_div_grp_inline");

        let label = document.createElement("label");
        label.setAttribute("class","inputArea_sub_label");
        label.innerHTML = recordMap[i].name;//名称
        groupDiv.appendChild(label);

        let input = document.createElement("input");
        input.setAttribute("id","modal"+recordMap[i].name);
        input.setAttribute("class","inputArea_sub_input");
        //input.setAttribute("value",recordMap[field]);
        groupDiv.appendChild(input);

        let labelType = document.createElement("label");
        labelType.setAttribute("class","inputArea_sub_label");
        labelType.innerHTML = recordMap[i].type + "(" + recordMap[i].length + ")";
        groupDiv.appendChild(labelType);

        body.appendChild(groupDiv);

        index = index + 1;
        //putInputList("input","modal"+recordMap[i].name);
        //putInputList("modal"+recordMap[i].name,index,curMenuId,"modalArea","input"); //id,seq,fun,area,type
    }

    let footerDiv = document.getElementById("swFooter");
    clearChildren("swFooter");

    if(formatInfoMap.formatInfoList != null){
        let formatInfoList = formatInfoMap.formatInfoList;
        for (let i=0;i<formatInfoList.length;i++){
            if(formatInfoList[i].area == "modalArea"){
                writeWebElementRoute(footerDiv,formatInfoList[i]);
            }
        }
    }

    display("swBackGround");
}