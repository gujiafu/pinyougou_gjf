//定义处理器
app.controller("indexController", function ($scope, $http, loginService) {

    $scope.getUsername =function () {
        loginService.getUsername().success(function (response) {
            $scope.username = response.username;
        })
    }

});