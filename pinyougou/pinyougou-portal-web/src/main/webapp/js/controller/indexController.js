//定义处理器
app.controller("indexController", function ($scope, $http, contentService) {

    $scope.contentList =[];

    $scope.findContentListByCategoryId = function (categoryId) {
        contentService.findContentListByCategoryId(categoryId).success(function (response) {
            $scope.contentList[categoryId] = response;
        })
    }

});