
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
        if("textArea"===showType){
            fillOutPutAreaWithText();
        }
        if("mapData_labelShow"===showType){
            fillOutPutLabelShow();
        }
    }
}

function fillOutPutLabelShow() {
    let parent_ele = document.getElementById("outputArea");

    let mapDataList = outputMap.mapData;  // List<WebDivDto>
    for (let i=0;i<mapDataList.length;i++){
        let webDivDto = mapDataList[i];  // WebDivDto

        let resp_div = document.createElement("div");
        resp_div.setAttribute("class","responsive");

        let divSubElementList = webDivDto.subElements; //List<WebElementDto>
        for (let eleIndex=0;i<divSubElementList.length;eleIndex++){
            let webElementDto = divSubElementList[eleIndex];

            writeWebElementRoute(resp_div,webElementDto);
        }
        parent_ele.appendChild(resp_div);
    }
}

/**
 * 填充输出区域_平铺
 * @param rtnMap
 */
function fillOutPutAreaWithText() {
    let text = outputMap.textAreaValue;
    let parent_ele = document.getElementById("outputArea");

    let textArea = document.createElement("textarea");
    textArea.setAttribute("class","output_textArea");
    textArea.innerHTML = text;

    parent_ele.appendChild(textArea);
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

        if(elementList[i].type.indexOf("video") != -1 || elementList[i].type.indexOf("realmedia") != -1 ){
            img_img.addEventListener("click",playVideo.bind(this,elementList[i],getCurPageInfo()),false);
        }

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
    let tableRecordList = outputMap.tableRecordList;
    if(tableRecordList==null) return;

    let parent_ele = document.getElementById("outputArea");  //<table class="table" id="dataTable">

    //分页按钮
    if(outputMap.withPage){
        for (let i=0;i<inputMap.inputList.length;i++){
            let events = inputMap.inputList[i].eventInfoList;
            for(let j=0;j<events.length;j++){
                if(events[j].withPage){
                    writePageButton(parent_ele,events[j],outputMap);//分页按钮
                }
            }
        }
    }

    let element_table = document.createElement("table");
    element_table.setAttribute("class","output_table");
    parent_ele.appendChild(element_table);

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

    //处理表格记录
    let element_tbody = document.createElement("tbody");
    element_tbody.setAttribute("class","output_table_tbody");
    element_table.appendChild(element_tbody);

    //遍历返回的表记录
    let begSeq = 0;//分页时记录序号累加前页记录数
    if(outputMap.withPage){
        begSeq = outputMap.pageSize * (outputMap.pageNow - 1);
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
                    writeWebElementRoute(element_table_td,formatInfoList[i]);
                    element_table_tr.appendChild(element_table_td);
                }
            }
        }
    }
}

//分页按钮
function writePageButton(parent_ele,eventInfo,outputMap){
    let events =new Array();
    events[0] = eventInfo;
    let page_div = document.createElement("div");
    page_div.setAttribute("class","page-box");

    let totalPage = parseInt((outputMap.totalCount-1)/outputMap.pageSize)+1;
    let pageNow = outputMap.pageNow;
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
    parent_ele.appendChild(page_div);
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

function playVideo(element,pageInfoMap){

    if(elementList[i].type.indexOf("video") != -1){
        setVideoTag(element,pageInfoMap);
    }

    if(elementList[i].type.indexOf("realmedia") != -1 ){
        setRealPlayTag(element,pageInfoMap);
    }
}

function setVideoTag(element,pageInfoMap){
    //弹窗标题和关闭按钮
    clearChildren("swHeader");
    let header = document.getElementById("swHeader");
    let header_span = document.createElement("span");
    header_span.innerHTML = "播放视频";//标题
    header.appendChild(header_span);
    let header_close = document.createElement("div");
    header_close.setAttribute("class","subWidowHeaderCloseBtn");
    header_close.addEventListener("click",hide.bind(this,"swBackGround"),false);
    header_close.innerHTML = "X";//标题
    header.appendChild(header_close);

    clearChildren("swBody");
    let body = document.getElementById("swBody");
//<!--    <video width="1120" height="540" controls="controls" id="video" preload="auto">-->
//<!--        <source src="video?path=mov_bbb.mp4">-->
//<!--    </video>-->
    let htmlVideo = document.createElement("video");
    htmlVideo.setAttribute("controls","controls");
    htmlVideo.setAttribute("id","playVideo");
    htmlVideo.setAttribute("preload","auto");
    htmlVideo.setAttribute("width","1120px");
    htmlVideo.setAttribute("height","540px");
    body.appendChild(htmlVideo);

    let inputEle = document.getElementById("localAddress");
    let index = inputEle.selectedIndex; // 选中索引
    let value ="";
    if(index >= 0){
        let text = inputEle.options[index].text; // 选中文本
        value = inputEle.options[index].value; // 选中值
    }

    let htmlSource = document.createElement("source");
    htmlSource.setAttribute("src","video?path="+element.id);
    htmlVideo.appendChild(htmlSource);

    let footerDiv = document.getElementById("swFooter");
    clearChildren("swFooter");

    display("swBackGround");
}

//<!--    <object classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" height="515" id="RAOCX" name="rmplay" width="700">-->
//<!--        <param name="SRC" value="video/mov_bbb.mp4" />-->
//<!--        <param name="_ExtentX" value="9313" />-->
//<!--        <param name="_ExtentY" value="7620" />-->
//<!--        <param name="AUTOSTART" value="1" />-->
//<!--        <param name="SHUFFLE" value="0" />-->
//<!--        <param name="PREFETCH" value="0" />-->
//<!--        <param name="NOLABELS" value="0" />-->
//<!--        <param name="CONTROLS" value="ImageWindow,ControlPanel" />-->
//<!--        <param name="CONSOLE" value="Clip2" />-->
//<!--        <param name="LOOP" value="0" />-->
//<!--        <param name="NUMLOOP" value="0" />-->
//<!--        <param name="CENTER" value="0" />-->
//
//<!--        <param name="MAINTAINASPECT" value="0" />-->
//<!--        <param name="BACKGROUNDCOLOR" value="#000000" />-->
//<!--        <embed src="video/mov_bbb.mp4" type="audio/x-pn-realaudio-plugin" console="Clip1" controls="ImageWindow,ControlPanel" height="515" width="700" autostart="true" />-->
//<!--    </object>-->
function setRealPlayTag(element,pageInfoMap){
    //弹窗标题和关闭按钮
    clearChildren("swHeader");
    let header = document.getElementById("swHeader");
    let header_span = document.createElement("span");
    header_span.innerHTML = "播放视频";//标题
    header.appendChild(header_span);
    let header_close = document.createElement("div");
    header_close.setAttribute("class","subWidowHeaderCloseBtn");
    header_close.addEventListener("click",hide.bind(this,"swBackGround"),false);
    header_close.innerHTML = "X";//标题
    header.appendChild(header_close);

    clearChildren("swBody");
    let body = document.getElementById("swBody");
    let htmlObject = document.createElement("object");
    htmlObject.setAttribute("controls","controls");
}
