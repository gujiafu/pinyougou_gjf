app.controller("searchController",function ($scope, $location, searchService) {

    $scope.search = function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;
        })
    }

    // 搜索对象
    $scope.searchMap = {keywords: "", category: "", brand: "", spec: {}, price:"",sort:"",sortField:""};
    // 点击添加搜索条件
    $scope.addSearchItem = function (key, value) {
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
        //重新搜索
        $scope.search();
    }

    $scope.deleteItemSearch = function (key) {
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = "";
        } else {
            delete $scope.searchMap.spec[key];
        }
        //重新搜索
        $scope.search();
    }

    // 排序查询
    $scope.sortSearch = function (sortField, sort) {
        $scope.searchMap["sortField"] = sortField;
        $scope.searchMap["sort"] = sort;
        //重新搜索
        $scope.search();
    }

    // 如果关键字中包含品牌字符串那么就隐藏品牌
    $scope.keywordsIsBrand = function () {
        var brandList = $scope.resultMap.brandList;
        for (var i = 0; i < brandList.length; i++) {
            var brand = brandList[i];
            if($scope.searchMap.keywords.indexOf(brand.text)>=0){
                return true;
            }
        }
        return false;
    }

    // 由门户首页跳转到搜索系统查询
    $scope.toSearch = function () {
        $scope.searchMap.keywords = $location.search()["keywords"];
        //重新搜索
        $scope.search();
    }

})
