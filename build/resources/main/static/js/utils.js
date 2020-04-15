/**
 * js生成缩略图
 * 传递三个参数 url（连接），width（宽度），height（高度）
 */
function(url, width, height) {

    if (!url) { return '';}
    if (!width || !height) { return url; }

    const urlArr = url.split('.');
    const len = urlArr.length;
    const suffixArr = ['jpg', 'jpeg', 'png', 'gif'];
    let newUrl = '';

    // 判断是否是图片链接
    if (suffixArr.indexOf(urlArr[len - 1]) === -1) { return url; }

    // 新的图片链接拼接
    for (const i in urlArr) {
        if (Number(i) === (len - 1) && suffixArr.indexOf(urlArr[i].toLowerCase()) > -1) {
          newUrl += '_' + width + 'x' + height + '.' + urlArr[i];
        } else if (Number(i) === 0) {
          newUrl += urlArr[i];
        } else {
          newUrl += '.' + urlArr[i];
        }
    }
  return newUrl;
}