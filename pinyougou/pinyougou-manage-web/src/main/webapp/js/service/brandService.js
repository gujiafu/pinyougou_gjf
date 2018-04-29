//定义一个服务
app.service("brandService", function ($http) {
    this.findAll = function () {
        return $http.get("../brand/findAll.do");
    }
    // 分页无条件查询
    this.findPage = function (page,rows) {
        return $http.get("../brand/findPage.do?page=" + page + "&rows=" + rows);
    }
    // 新增
    this.add = function (entity) {
        return $http.post("../brand/add.do", entity);
    };
    // 更新
    this.update = function (entity) {
        return $http.post("../brand/update.do", entity);
    };
    // 根据id查找
    this.findOne = function (id) {
        return $http.get("../brand/findOne.do?id=" + id);
    }
    // 根据id批量删除
    this.delete = function (selectedIds) {
        return $http.get("../brand/delete.do?ids=" + selectedIds);
    }
    // 分页条件查询
    this.search = function (page,rows,searchEntity) {
        return $http.post("../brand/search.do?page=" + page + "&rows=" + rows, searchEntity);
    }
})