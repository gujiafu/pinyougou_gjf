app.controller("goodsController", function ($scope, $controller, goodsService, uploadService,
                                            itemCatService,typeTemplateService,$location) {

    //加载baseController控制器并传入1个作用域，与angularJs运行时作用域相同.
    $controller("baseController",{$scope:$scope});

    //加载列表数据
    $scope.findAll = function(){
        goodsService.findAll().success(function (response) {
            $scope.list = response;
        });
    };

    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };

    $scope.save = function () {
        var object;
        $scope.entity.goodsDesc.introduction = editor.html();
        if($scope.entity.goods.id != null){//更新
            object = goodsService.update($scope.entity);
        } else {//新增
            object = goodsService.add($scope.entity);
        }
        object.success(function (response) {
            if(response.success){
                alert(response.message);
                $scope.entity= {};
                editor.html("");
                specItems =[];
                itemArray =[];
            } else {
                alert(response.message);
            }
        });
    };


    $scope.delete = function () {
        if($scope.selectedIds.length < 1){
            alert("请先选择要删除的记录");
            return;
        }
        if(confirm("确定要删除已选择的记录吗")){
            goodsService.delete($scope.selectedIds).success(function (response) {
                if(response.success){
                    $scope.reloadList();
                    $scope.selectedIds = [];
                } else {
                    alert(response.message);
                }
            });
        }
    };

    $scope.searchEntity = {};//初始为空
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };

    // 图片上传
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {
            if(response.success){
                $scope.image_entity.url = response.message;
            }else{
                alter(response.message);
            }
        }).error(function () {
            alert(" 上传图片失败");
        })
    }

    // 定义商品
    $scope.entity = {"goods":{},"goodsDesc":{"itemImages":[]}};
    // 图片保存
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
        $scope.image_entity = {};
    }
    $scope.delete_image_entity = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    // 初始化selectItemCat1List顶级选项
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCatList1 = response;
        })
    }
    // 监控select1值变化后触发select2加载
    $scope.$watch("entity.goods.category1Id",function (newValue, oldValue) {
        if(newValue!=undefined){
            itemCatService.findByParentId(newValue).success(function (response) {
                $scope.itemCatList2 = response;
                $scope.itemCatList3 = {};
                $scope.entity.goods.typeTemplateId = null;
            })
        }
    })
    // 监控select2值变化后触发select3加载
    $scope.$watch("entity.goods.category2Id",function (newValue, oldValue) {
        if(newValue!=undefined){
            itemCatService.findByParentId(newValue).success(function (response) {
                $scope.itemCatList3 = response;
                $scope.entity.goods.typeTemplateId =null;
            })
        }
    })

    // 监控select3值变化后触发模板id,并获取品牌
    $scope.$watch("entity.goods.category3Id",function (newValue, oldValue) {
        if(newValue!=undefined){

            // 根据itemCat.id 查找itemCat.typeId
            itemCatService.findOne(newValue).success(function (response) {

                // 根据select3获取模板id
                $scope.entity.goods.typeTemplateId = response.typeId;

                typeTemplateService.findOne(response.typeId).success(function (response) {
                    // 根据模板获取品牌
                    $scope.brandIds = JSON.parse(response.brandIds);

                    // 根据模板获取扩展属性
                    if($location.search()["id"]==null){
                        $scope.entity.goodsDesc.customAttributeItems = JSON.parse(response.customAttributeItems);
                    }

                    // 根据模板id获取规格列表
                    typeTemplateService.findSpecList(response.id).success(function (response) {
                        $scope.specList = response;
                    })
                })
            })
        }
    })

    // 勾选规格选项,添加到$scope.entity.goodsDesc.specificationItems
    $scope.entity = {goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
    var specItems = [];
    $scope.checkOption = function (checkOptionValue, attrName, attrValue) {
        // 勾选
        if(checkOptionValue){
            var flag = false;
            for (var i = 0; i < specItems.length; i++) {
                // 1.如果勾选中有规格,就添加属性
                if(specItems[i].attributeName==attrName){
                    flag = true;
                    specItems[i].attributeValue.push(attrValue);
                    break;
                }
            }
            // 2.如果勾选中没有规格,就添加规格和属性
            if(!flag){
                specItems.push({attributeName:attrName,attributeValue:[attrValue]});
            }
        }else{
            // 取消勾选
            // 删除属性
            for (var i = 0; i < specItems.length; i++) {
                if(specItems[i].attributeName==attrName){
                    var index = specItems[i].attributeValue.indexOf(attrValue);
                    specItems[i].attributeValue.splice(index,1);
                    // 删除规格和属性
                    if( specItems[i].attributeValue.length==0){
                        specItems.splice(i,1);
                    }
                }
            }
        }
        $scope.entity.goodsDesc.specificationItems = specItems;

        // 显示规格选项 {spec:[{name1:"v1"},{name2:"v2"}],price:12,stockCount:900,isDefault:0,isEnableSpec:0}

        var itemArray = [];
        for (var i = 0; i < specItems.length; i++) {
            var array = new Array();
            var name = specItems[i].attributeName;
            for (var j = 0; j < specItems[i].attributeValue.length; j++) {
                var value =  specItems[i].attributeValue[j];
                var map = new Map(name,value);
                array.push(map);
            }
            itemArray.push(array);
        }

        // 遍历itemArray组成规格选项
        var itemList = [];
        var newArray =[];
        if(itemArray.length>0){
            $scope.entity.itemList = getItemList(itemArray,0,0,itemList,newArray);
        }else{
            $scope.entity.itemList = [];
        }

    }

    // 点击是否启用规格
    $scope.isEnableSpec = function () {
        specItems =[];
        itemArray =[];
        $scope.entity.itemList=[];
    }

    // 定义商品状态数组
    $scope.auditStatus =["未申请","申请中","审核通过","审核未通过","关闭"];

    //根据itemCat的id查找name
    $scope.itemCarList ={};
    $scope.findItemCatList = function () {
        itemCatService.findAll().success(function (response) {
            for (var i = 0; i < response.length; i++) {
                $scope.itemCarList[response[i].id] = response[i].name;
            }
        })
    }

    // 点击修改商品,回显商品数据数据
    $scope.findOne = function () {
        var id = $location.search()["id"];
        if(id==null){
            return;
        }
        goodsService.findOne(id).success(function (response) {
            $scope.entity = response;

            // 回显富文本
            editor.html($scope.entity.goodsDesc.introduction);

            // 回显图片
            $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);

            // 回显扩展属性
            $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);

            // 回显选中规格选项
            $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
            $scope.checkAttributeValue = function (specName, specOptionName) {
                var specificationItems = $scope.entity.goodsDesc.specificationItems;
                for (var i = 0; i < specificationItems.length; i++) {
                    var attributeName = specificationItems[i].attributeName;
                    if(specName==attributeName){
                        // for (var j = 0; j < specificationItems[i][attributeValue].length; j++) {
                        //     var attributeValue = specificationItems[i][attributeValue][j];
                        //     if(attributeValue==specOptionName){
                        //         return true;
                        //     }
                        // }
                        if(specificationItems[i].attributeValue.indexOf(specOptionName)>-1){
                            return true;
                        }
                    }
                }
                return false;
            }

            // 商品sku回显
            if($scope.entity.itemList.length>0){
                for (var i = 0; i < $scope.entity.itemList.length; i++) {
                    $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
                }
            }
        });
    };

    // 修改spu商品状态
    $scope.updateStatus = function (status) {
        if($scope.selectedIds.length<1){
            alert("请选择记录修改");
        }else{
            if(confirm("你确定要修改?")){
                var ids = $scope.selectedIds;
                goodsService.updateStatus(ids,status).success(function (response) {
                    if(response.success){
                        $scope.reloadList();
                        $scope.selectedIds = [];
                    } else {
                        alert(response.message);
                    }
                })
            }
        }

    }


});

// 定义map
var Map= function (key, value) {
    var map = {};
    map[key] = value;
    return map;
}

// 二维数组的笛卡尔集
var getItemList = function (oriArray, oneIndex, twoIndex, itemList, array) {
    // 遍历二维数组
    for (var i = 0; i < oriArray[oneIndex].length; i++) {
        array[twoIndex] = oriArray[oneIndex][i];
        // 判断该数组是否最后一个数组,如果不是就到下一个数组
        if(oneIndex < oriArray.length-1){
            getItemList(oriArray,oneIndex+1, twoIndex+1, itemList, array);
        }else{
            // 一维数组拷贝放到二维数组中
            var spec = JSON.parse(JSON.stringify(array));
            var specStr ="";
            for (var j = 0; j < spec.length; j++) {
                var obj = spec[j];
                obj = JSON.stringify(obj);
                specStr+=obj;
            }
            specStr = specStr.replace(/\}\{/,",");
            specStr = JSON.parse(specStr);
            itemList.push({spec:specStr,price:12,stockCount:900,isDefault:0,status:0});
        }
    }
    return itemList;
}