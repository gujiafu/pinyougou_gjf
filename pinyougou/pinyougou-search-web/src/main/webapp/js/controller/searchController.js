app.controller("searchController",function ($scope, searchService) {


    $scope.search = function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;
        })
    }

    // 搜索对象
    $scope.searchMap = {keywords:"",category:"",brand:"",spec:{}};
    // 点击添加搜索条件
    $scope.addSearchItem = function (key, value) {
        if(key=="category" || key=="brand"){
            $scope.searchMap[key] = value;
        }else{
            $scope.searchMap.spec[key] = value;
        }
        //重新搜索
        $scope.search();
    }
    
    $scope.deleteItemSearch = function (key) {
        if(key=="category" || key=="brand"){
            $scope.searchMap[key] = "";
        }else{
            delete $scope.searchMap.spec[key];
        }
        //重新搜索
        $scope.search();
    }

})