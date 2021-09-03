
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
        writeWebElementRoute(parentEle,inputList[i]);
    }
}

