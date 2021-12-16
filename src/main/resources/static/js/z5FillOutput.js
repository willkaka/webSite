
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
    let webTableInfo = outputMap.webTableInfo;
    let parent_ele = document.getElementById("outputArea");  //<table class="table" id="dataTable">
    writeTable(parent_ele,webTableInfo);
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
