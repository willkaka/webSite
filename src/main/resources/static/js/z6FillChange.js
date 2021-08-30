
/**
 * 页面部分内容刷新
 **/
function fillChangedEle(){
    if(changedMap.isChanged){
        let changedEleMap = changedMap.changedEleMap;
        for(let eleId in changedEleMap){
            let ele = document.getElementById(eleId);
            let eleInfo = changedEleMap[eleId];
            clearChildren(eleId); //清空已有的内容
            if(eleInfo.chgType == "option"){
                if (JSON.stringify(eleInfo.dataMap) != JSON.stringify({})){
                    for(let value in eleInfo.dataMap){
                        let option = document.createElement("option");
                        option.setValue = value;
                        option.innerHTML = eleInfo.dataMap[value];
                        ele.appendChild(option);
                    }
                }
            }else if(eleInfo.chgType == "inputDataList"){
                //ele.value = "";
                clearChildren("datalist"+eleId); //清空已有的内容
                var dataList = document.getElementById("datalist"+eleId);
                if (dataList != null && JSON.stringify(eleInfo.dataMap) != JSON.stringify({})){
                    for(let value in eleInfo.dataMap){
                        let option = document.createElement("option");
                        option.setAttribute("value",value);
                        dataList.appendChild(option);
                    }
                }
            }else if(eleInfo.chgType == "selectOption"){
                clearChildren(eleId); //清空已有的内容
                let selectTag = document.getElementById(eleId);
                if (JSON.stringify(eleInfo.dataMap) != JSON.stringify({})){
                    for(let value in eleInfo.dataMap){
                        let option = document.createElement("option");
                        option.setAttribute("value",value);
                        option.innerHTML = eleInfo.dataMap[value];
                        selectTag.appendChild(option);
                    }
                }
            }else if(eleInfo.chgType == "multipleSelect"){
                let multipleSelect2 = $("#"+eleId);
                let selectOptions = [];
                let selectTag = document.getElementById(eleId);
                if (JSON.stringify(eleInfo.dataMap) != JSON.stringify({})){
                    for(let mapKey in eleInfo.dataMap){
                        selectOptions.push({label: eleInfo.dataMap[mapKey], title: eleInfo.dataMap[mapKey], value: mapKey});
                    }
                }
                multipleSelect2.multiselect('dataprovider',selectOptions);
            }
        }
        changedMap.isChanged = false;
    }
}