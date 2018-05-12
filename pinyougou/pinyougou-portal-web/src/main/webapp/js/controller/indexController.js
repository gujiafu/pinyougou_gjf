//定义处理器
app.controller("indexController", function ($scope, $http, contentService) {

    $scope.contentList =[];

    $scope.findContentListByCategoryId = function (categoryId) {
        contentService.findContentListByCategoryId(categoryId).success(function (response) {
            $scope.contentList[categoryId] = response;
        })
    }

    // 跳转到搜索系统
    $scope.toSearch = function () {
        location.href="http://search.pinyougou.com/search.html#?keywords="+$scope.keywords;
    }

});