async function crawl(url, depth, concurrency) {

    let objArr = [];
    let linksArr = [url];

    // Цикл по глубине
    for (let i = 0; i < depth; i++) {
        let arr = [];
        let newLinks = [];
        for (let j = 0; j <= linksArr.length; j++) {
            if (arr.length < concurrency && j < linksArr.length) {
                arr.push(linksArr[j]);
                continue;
            }
            //console.log(arr)
            let requests = arr.map(url => fetch(url));
            let responses = await Promise.all(requests);
            //let urls = await Promise.all(responses.map(response => response.url));
            let texts = await Promise.all(responses.map(response => response.text()));
            for (let k = 0; k < texts.length; k++) {
                let currentUrl = arr[k];
                let text = texts[k];
                let links = [];
                let indL = text.indexOf("href=\"");
                let indR = text.indexOf("\"", indL + 6);
                while (indL !== -1) {
                    let link = text.slice(indL + 6, indR);
                    if (!link.startsWith("http")) {
                        link = currentUrl + link;
                    }
                    //console.log(link);
                    links.push(link);
                    newLinks.push(link);
                    indL = text.indexOf("href=\"", indR);
                    indR = text.indexOf("\"", indL + 6);
                }
                objArr.push({
                    url: currentUrl,
                    depth: i,
                    content: text,
                    links: links
                });
            }
            arr = [];
        }
        linksArr = newLinks;
        newLinks = [];
    }
    return objArr;
}

module.exports = crawl;