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
        //label.innerHTML = field;//名称
        let fieldName = recordMap[field].columnName;//名称
        if(recordMap[field].remarks != null && recordMap[field].remarks != ''){
            fieldName = fieldName + '(' +recordMap[field].remarks + ')';
        }
        label.innerHTML = fieldName;//名称
        groupDiv.appendChild(label);

        let input = document.createElement("input");
        input.setAttribute("id","modal"+field);
        input.setAttribute("class","inputArea_sub_input");
        input.setAttribute("value",recordMap[field].value);
        groupDiv.appendChild(input);

        let labelType = document.createElement("label");
        labelType.setAttribute("class","inputArea_sub_label");
        let typeStr;
        //sqlite数据库
        if(recordMap[field].columnSize == '2000000000'){
            typeStr = recordMap[field].typeName;
        }else{
            typeStr = recordMap[field].typeName + "(" + recordMap[field].columnSize;
            if('DECIMAL' == recordMap[field].typeName){
                typeStr = typeStr + "," + recordMap[field].decimalDigits;
            }
            typeStr = typeStr + ")";
        }
        if('NO' == recordMap[field].isNullable){ typeStr = typeStr + ',' + 'is not null'}
        if(true == recordMap[field].keyField){ typeStr = typeStr + ',' + 'is key'}
        if('YES'== recordMap[field].isAutoincrement){typeStr = typeStr + ',' + 'isAutoincrement'}
        labelType.innerHTML = typeStr;
        groupDiv.appendChild(labelType);

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
            if(formatInfoList[i].area == "modalArea" && formatInfoList[i].window == "editWindow"){
                let elementInfo = formatInfoList[i];
                let eventInfoList = elementInfo["eventInfoList"];
                for(let j=0;j<eventInfoList.length;j++){
                    eventInfoList[j].recordMap = recordMap;
                }
                writeWebElementRoute(footerDiv,elementInfo);
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
    for(let field in recordMap){
    //for (let i=0;i<recordMap.length;i++){
        let groupDiv = document.createElement("div");
        groupDiv.setAttribute("class","inputArea_div_grp_inline");

        let label = document.createElement("label");
        label.setAttribute("class","inputArea_sub_label");
        let fieldName = recordMap[field].columnName;//名称
        if(recordMap[field].remarks != null && recordMap[field].remarks != ''){
            fieldName = fieldName + '(' +recordMap[field].remarks + ')';
        }
        label.innerHTML = fieldName;//名称
        groupDiv.appendChild(label);

        let input = document.createElement("input");
        input.setAttribute("id","modal"+recordMap[field].columnName);
        input.setAttribute("class","inputArea_sub_input");
        //input.setAttribute("value",recordMap[field]);
        groupDiv.appendChild(input);

        let labelType = document.createElement("label");
        labelType.setAttribute("class","inputArea_sub_label");
        let typeStr;
        //sqlite数据库
        if(recordMap[field].columnSize == '2000000000'){
            typeStr = recordMap[field].typeName;
        }else{
            typeStr = recordMap[field].typeName + "(" + recordMap[field].columnSize;
            if('DECIMAL' == recordMap[field].typeName){
                typeStr = typeStr + "," + recordMap[field].decimalDigits;
            }
            typeStr = typeStr + ")";
        }
        if('NO' == recordMap[field].isNullable){ typeStr = typeStr + ' ' + 'is not null'}
        if(true == recordMap[field].keyField){ typeStr = typeStr + ',' + 'is key'}
        if('YES'== recordMap[field].isAutoincrement){typeStr = typeStr + ',' + 'isAutoincrement'}
        labelType.innerHTML = typeStr;
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
            if(formatInfoList[i].area == "modalArea" && formatInfoList[i].window == "addWindow"){
                let elementInfo = formatInfoList[i];
                let eventInfoList = elementInfo["eventInfoList"];
                for(let j=0;j<eventInfoList.length;j++){
                    eventInfoList[j].recordMap = recordMap;
                }
                writeWebElementRoute(footerDiv,elementInfo);
            }
        }
    }

    display("swBackGround");
}


/**
 * 显示编辑模态框
 * @param recordMap 点选的记录内容
 **/
function showLoginModal(){
    //弹窗标题和关闭按钮
    clearChildren("swHeader");
    let header = document.getElementById("swHeader");
    let header_span = document.createElement("span");
    header_span.innerHTML = "登录";//标题
    header.appendChild(header_span);
    let header_close = document.createElement("div");
    header_close.setAttribute("class","subWidowHeaderCloseBtn");
    header_close.addEventListener("click",hide.bind(this,"swBackGround"),false);
    header_close.innerHTML = "X";//标题
    header.appendChild(header_close);

    clearChildren("swBody");
    let swBorder = document.getElementById("swBorder");
    swBorder.setAttribute("margin","15% 40% auto");

    let body = document.getElementById("swBody");

        let groupDivUser = document.createElement("div");
        groupDivUser.setAttribute("class","inputArea_div_grp_inline");

        let labelUser = document.createElement("label");
        labelUser.setAttribute("class","inputArea_sub_label");
        labelUser.innerHTML = "用户名: ";//名称
        groupDivUser.appendChild(labelUser);

        let inputUser = document.createElement("input");
        inputUser.setAttribute("id","modal"+"userName");
        inputUser.setAttribute("class","inputArea_sub_input");
        groupDivUser.appendChild(inputUser);
        body.appendChild(groupDivUser);

        let groupDivPwd = document.createElement("div");
        groupDivPwd.setAttribute("class","inputArea_div_grp_inline");

        let labelPwd = document.createElement("label");
        labelPwd.setAttribute("class","inputArea_sub_label");
        labelPwd.innerHTML = "密码: ";//名称
        groupDivPwd.appendChild(labelPwd);

        let inputPwd = document.createElement("input");
        inputPwd.setAttribute("id","modal"+"pwd");
        inputPwd.setAttribute("class","inputArea_sub_input");
        inputPwd.setAttribute("type","password");
        groupDivPwd.appendChild(inputPwd);
        body.appendChild(groupDivPwd);

    let footerDiv = document.getElementById("swFooter");
    clearChildren("swFooter");

    let webElement = {
        type:"button",
        prompt:"登录"
    };
    let eventInfo = {
        event: "click",
        type : "buttonReq",
        id   : "login"
    };
    let eventInfos = new Array;
    eventInfos[0]=eventInfo;

    webElement.eventInfoList = eventInfos;
    writeWebElementRoute(footerDiv,webElement);

    display("swBackGround");
}