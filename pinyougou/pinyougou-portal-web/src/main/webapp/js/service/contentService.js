//定义业务服务
app.service("contentService",function ($http) {
    //加载列表数据

    this.findContentListByCategoryId = function (categoryId) {
        return $http.get("content/findContentListByCategoryId.do?categoryId="+categoryId);
    };

});