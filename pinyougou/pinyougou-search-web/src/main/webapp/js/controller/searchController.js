app.controller("searchController",function ($scope, searchService) {

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

})
