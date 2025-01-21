@[TO<!-- TOC -->
* [一.实现效果图](#一实现效果图)
* [二.数据集](#二数据集)
* [三.实现思路](#三实现思路)
  * [1.创建数据封装函数](#1创建数据封装函数)
  * [2.将现有的数据处理成json数组](#2将现有的数据处理成json数组)
  * [3.调用closureProcessing()函数,将json数组变为json字符串](#3调用closureprocessing函数将json数组变为json字符串)
  * [4.初始化画布,并设置数据](#4初始化画布并设置数据)
* [四.源码](#四源码)
  * [1.HTML部分源码(即展示树状图用的画布容器)](#1html部分源码即展示树状图用的画布容器)
  * [2.CSS部分的样式代码](#2css部分的样式代码)
  * [3.完整的js代码](#3完整的js代码)
<!-- TOC -->C](基于OrgChart技术流程图[树状图])

# 一.实现效果图

![效果图 ](https://img-blog.csdnimg.cn/20200320121952281.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzIyOTI2NzM5,size_16,color_FFFFFF,t_70)

# 二.数据集

![数据库中返回的数据](https://img-blog.csdnimg.cn/20200320130818913.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzIyOTI2NzM5,size_16,color_FFFFFF,t_70)

经过处理后的数据集:
```json
[
	{"FACTORRISK":"0.6","FACTOREXPOSURE":"0.5","DIMNAME":"主动风险","STYLENMAE":"BARRA中国因子分解"},
	{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"资产选择","STYLENMAE":"主动风险"},
	{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"共同因子","STYLENMAE":"主动风险"},
	{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"风格因子","STYLENMAE":"共同因子"},
	{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"行业因子","STYLENMAE":"共同因子"},
	{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"市场因子","STYLENMAE":"共同因子"},
	{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"协方差*2","STYLENMAE":"共同因子"}
]
```

此时数据已经变成了json数组

# 三.实现思路

其实思路很简单就是将普通数据组装成符合[OrgChart数据格式(点击这里访问OrgChart官方演示文档)](https://www.oschina.net/p/orgchart "OrgChart官方演示")的json数据

## 1.创建数据封装函数

```javascript
function closureProcessing(obj, s, m,isCompany, str) {
        var x1 = m || [];
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].pId == s) {
                x1.push(obj[i]);
                x1.map(function (item, index) {
                    if (item.pId && isCompany) {

                    } else {
                        x1[index].children = [];
                    }
                    if (str) {
                        isCompany = true;
                        for (var i = 0; i < str.length; i++) {
                            if (item.id == str[i].pId && !item.pId) {
                                x1[index].children.push(str[i]);
                            }
                        }
                    }
                    return closureProcessing(obj, item.id, x1[index].children, str);
                })
            }
        }
        return x1;
    }
```

## 2.将现有的数据处理成json数组

```javascript
var data = [
{"FACTORRISK":"0.6","FACTOREXPOSURE":"0.5","DIMNAME":"主动风险","STYLENMAE":"BARRA中国因子分解"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"资产选择","STYLENMAE":"主动风险"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"共同因子","STYLENMAE":"主动风险"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"风格因子","STYLENMAE":"共同因子"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"行业因子","STYLENMAE":"共同因子"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"市场因子","STYLENMAE":"共同因子"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"协方差*2","STYLENMAE":"共同因子"}
];
for (var i = 0; i < data.length; i++) {
                var item = {};
                if (data[i].PID == 0){
                    item ={
                        pId:data[i].PID,
                        id:data[i].ID,
                        name:data[i].DIMNAME,
                        title:'<div class="org_def"> </div><div class="org_def"> </div>'
                    };
                }else {
                    item = {
                        pId: data[i].PID,
                        id: data[i].ID,
                        name: data[i].DIMNAME,
                        title: '<div class="rote_style">' + (isNaN(data[i].FACTOREXPOSURE) ? 'N/A' : data[i].FACTOREXPOSURE) + '</div><div class="radio_style">' + (isNaN(data[i].FACTORRISK) ? 'N/A' : data[i].FACTORRISK) + '</div>'
                    }
                }
                dataSource.push(item);
            }
            /* 处理后的json数组中的每个元素均为
            {
            	pId: "XX"
				id: "XX"
				name: "XX"
				title: "XX"
				children: [{…}]
				relationship: "001"
			}
			并且children中的数据也是这种格式
				*/
            console.log(dataSource);
           
```
## 3.调用closureProcessing()函数,将json数组变为json字符串

```javascript
/**
最终会形成一个下面格式的json数据,children属性里面存放着所有的下级节点
{
	pId: "0"
	id: "1"
	name: "BARRA中国因子分解"
	title: "<div class="org_def"> </div><div class="org_def"> </div>"
	children: [{…}]
	relationship: "001"
}
*/
var treeData = closureProcessing(dataSource,0,[],isCompany)[0];
```
## 4.初始化画布,并设置数据

```javascript
var oc = $('#chart-container').orgchart({
                'data' : treeData,
                'nodeContent': 'title',
                'nodeTitle':'name',
                'toggleSiblingsResp': true,
                // 'direction': 'T2B',
                //   visibleLevel【number】：默认展开几级
                'visibleLevel': 3,
                parentNodeSymbol: null
            });
```
至此,整个树状(组织结构)图就开发完毕了,但是这样实现出来的效果和图中差距还是挺大的,因为这样实现以后没有效果图中的颜色和右上角的图例.所以这里还需要做些设置,但是这些设置都无关紧要了,直接放到源码里面处理.
# 四.源码
## 1.HTML部分源码(即展示树状图用的画布容器)
因为这部分很简单,所以只写画布相关的代码,有兴趣的自己补足运行!

```html
 		<div>
            <div id="legend"></div>
            <div id="chart-container" style="width: 100%"></div>
        </div>
```
## 2.CSS部分的样式代码

```css
<style type="text/css">
    .radio_style{
        background-color: #64c2e5;
        color: black;
        width: auto;
        height: 20px;
        text-align: center;
    }
    .rote_style{
        background-color: #f3ab11;
        color: black;
        width: auto;
        height: 20px;
        text-align: center;
    }
    .org_def{
        width: auto;
        height: 20px;
        text-align: center;
    }
    .orgchart .node .content{
        height: auto;
    }
    .orgchart table{
        border: white;
    }

    .orgchart td.right {
        border-right: 2px solid rgba(217, 83, 79, 0.8);
    }
    .orgchart td.top {
        border-top: 2px solid rgba(217, 83, 79, 0.8);
    }

    .title{
        border-left: 0px;
        margin-bottom: 0px;
    }
    .orgchart{
        background: white;
    }
</style>
```
## 3.完整的js代码

```javascript
function closureProcessing(obj, s, m,isCompany, str) {
        var x1 = m || [];
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].pId == s) {
                x1.push(obj[i]);
                x1.map(function (item, index) {
                    if (item.pId && isCompany) {

                    } else {
                        x1[index].children = [];
                    }
                    if (str) {
                        isCompany = true;
                        for (var i = 0; i < str.length; i++) {
                            if (item.id == str[i].pId && !item.pId) {
                                x1[index].children.push(str[i]);
                            }
                        }
                    }
                    return closureProcessing(obj, item.id, x1[index].children, str);
                })
            }
        }
        return x1;
    };
var contentBody = $("#chart-container");
var _legend = $("#legend");
var isCompany = false;
var dataSource = [];
var treeData = {};
var data = [
{"FACTORRISK":"0.6","FACTOREXPOSURE":"0.5","DIMNAME":"主动风险","STYLENMAE":"BARRA中国因子分解"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"资产选择","STYLENMAE":"主动风险"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"共同因子","STYLENMAE":"主动风险"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"风格因子","STYLENMAE":"共同因子"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"行业因子","STYLENMAE":"共同因子"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"市场因子","STYLENMAE":"共同因子"},
{"FACTORRISK":"0.8","FACTOREXPOSURE":"0.4","DIMNAME":"协方差*2","STYLENMAE":"共同因子"}
];
for (var i = 0; i < data.length; i++) {
     var item = {};
     if (data[i].PID == 0){
         item ={
              pId:data[i].PID,
              id:data[i].ID,
              name:data[i].DIMNAME,
              title:'<div class="org_def"> </div><div class="org_def"> </div>'
         };
     }else {
         item = {
              pId: data[i].PID,
              id: data[i].ID,
              name: data[i].DIMNAME,
              title: '<div class="rote_style">' + (isNaN(data[i].FACTOREXPOSURE) ? 'N/A' : data[i].FACTOREXPOSURE) + '</div><div class="radio_style">' + (isNaN(data[i].FACTORRISK) ? 'N/A' : data[i].FACTORRISK) + '</div>'
         };
     }
     dataSource.push(item);
}
console.log(dataSource);
treeData = closureProcessing(dataSource,0,[],isCompany)[0];
var html = '';
html += '<div>' +
        '<table style="border: 0px;width: 20%;float: right">' +
        '<tr><td class="rote_style">主动风险（波动率）</td></tr>' +
        '<tr><td class="radio_style">风险贡献率（主动风险%）</td></tr>' +
        '</table>'+
        '</div>';
_legend.append(html);
var oc = $('#chart-container').orgchart({
       'data' : treeData,
       'nodeContent': 'title',
       'nodeTitle':'name',
       'toggleSiblingsResp': true,
       // 'direction': 'T2B',
       //   visibleLevel【number】：默认展开几级
       'visibleLevel': 3,
       parentNodeSymbol: null
     });
```
整个代码中最关键的部分就是怎么将数据处理为我们想要的或者说符合OrgChart格式的json对象,只要能搞定这个关键点,那么后面的都是小case了.
_________________________________________________________________________________
<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="知识共享许可协议" style="border-width:5" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a>

本作品采用<a rel="license" href="http://creativecommons.org/licenses/by/4.0/">知识共享署名 4.0 国际许可协议</a>进行许可。
