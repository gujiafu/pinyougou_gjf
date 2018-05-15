app.controller("itemController", function ($scope, $http) {

    $scope.itemNum =3;
    $scope.addItem = function () {
        $scope.itemNum = $scope.itemNum -1;
        if($scope.itemNum<0){
            $scope.itemNum =0;
        }

    }
    $scope.deleteItem = function () {
        $scope.itemNum = $scope.itemNum +1;
        if($scope.itemNum>3){
            $scope.itemNum =3;
        }
    }
    //当前选择了的那些规格
    $scope.specificationItems = {};

    //选择具体的规格选项
    $scope.selectSpecification = function (specName, optionName) {
        $scope.specificationItems[specName] = optionName;

        //获取到当前选择了的规格对应的sku
        searchSku();
    };

    //判断当前的选项是否已经选择了
    $scope.isSelected = function (specName, optionName) {
        if ($scope.specificationItems[specName] == optionName) {
            return true;
        }
        return false;
    };

    //当前选择了的sku
    $scope.sku = {};

    //加载默认的sku
    $scope.loadSku = function () {
        //将sku列表中的第一个作为默认的sku；进入页面的时候初始化显示的
        $scope.sku = skuList[0];

        //将当前的sku的规格选项设置为选择了的规格选项
        $scope.specificationItems = JSON.parse(JSON.stringify($scope.sku.spec));
    };

    searchSku = function () {
        //根据当前选择的规格从skuList中查询规格与其一致的sku;将当前的sku置为找到的那个sku
        for (var j = 0; j < skuList.length; j++) {
            var sku = skuList[j];
            if(matchObject($scope.specificationItems, sku.spec)){
                $scope.sku = sku;
                return;
            }
        }

        //如果在sku列表中查找不到具体的sku
        $scope.sku = {"id":0, "title":"------------", "price":0, "spec":{}};

    };

    //比较两个js对象是否一致
    matchObject = function (map1, map2) {
        for(var j in map1){
            if (map1[j] != map2[j]) {
                return false;
            }
        }

        for(var k in map2){
            if (map1[k] != map2[k]) {
                return false;
            }
        }

        return true;
    };

    //加入购物车
    $scope.addCart = function () {
        alert("skuId = " + $scope.sku.id);

    };
});