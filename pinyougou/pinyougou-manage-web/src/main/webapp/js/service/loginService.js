//定义一个服务
app.service("loginService", function ($http) {
    this.getUsername = function () {
        return $http.get("../login/getUsername.do");
    }

})