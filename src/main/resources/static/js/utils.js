
/**
 * 删除传入标签ID下所有子标签
 * @param parentId 父标签ID
 */
function clearChildren(parentId) {
    let element = document.getElementById(parentId);
    if(element!=null){
        let menuElements = element.childNodes;
        for(let i=menuElements.length-1;i>=0;i--){
            element.removeChild(menuElements[i]);
        }
    }
}

function clearChildrenExcept(element,tagName) {
    if(element!=null){
        let menuElements = element.childNodes;
        for(let i=menuElements.length-1;i>=0;i--){
            if(tagName == menuElements[i].tagName || menuElements[i].tagName == "LABEL"){ continue; }
            element.removeChild(menuElements[i]);
        }
    }
}

/**
 * 不显示指定标签
 * @param eleId
 */
function hide(eleId){
    let ele = document.getElementById(eleId);
    ele.style.display="none";
}

/**
 * 显示指定标签
 * @param eleId
 */
function display(eleId){
    let ele = document.getElementById(eleId);
    ele.style.display="";
}

function setCookie(cname,cvalue,exdays){
    let d = new Date();
    d.setTime(d.getTime()+(exdays*24*60*60*1000));
    let expires = "expires="+d.toGMTString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function delCookie(cname){
    let expires = "expires=Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = cname + "=" + getCookie(cname) + "; " + expires;
}

function getCookie(cname){
    let name = cname + "=";
    let ca = document.cookie.split(';');
    for(let i=0; i<ca.length; i++){
        let c = ca[i].trim();
        if (c.indexOf(name)==0) return c.substring(name.length,c.length);
    }
    return "";
}

function showLoginWindow(){
    if(getCookie(userNameKey) != null && getCookie(userNameKey) != ""){
        alert("您 " + getCookie(userNameKey) + " 已登录！");
        return;
    }

    showLoginModal();
}

function setUserNameIntoCookie(cvalue){
    setCookie(userNameKey,cvalue,1)
}

function copy(obj){
    return Object.assign({},obj);
}

/**
 * 获取表格某一行列数
 * @param  Int id    表格id
 * @param  Int index 行数
 * @return Int
 */
function getTableRowCellsLength(tableId, rowNo){
    var table = document.getElementById(tableId);
    if(rowNo<table.rows.length){
        return table.rows[rowNo].cells.length;
    }else{
        return 0;
    }
}

/**
 * 遍历表格内容返回数组
 * @param  Int   id 表格id
 * @return Array
 */
function getTableContent(tableId){
    var table = document.getElementById(tableId);
    var data = [];
    for(var i=0,rows=table.rows.length; i<rows; i++){
        for(var j=0,cells=table.rows[i].cells.length; j<cells; j++){
            if(!data[i]){
                data[i] = new Array();
            }
            data[i][j] = table.rows[i].cells[j].innerHTML;
        }
    }
    return data;
}

function getTableInfo(tableId){

}