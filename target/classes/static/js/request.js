var Request = function () {
    var baseUrl = "http://locahost:8080";
    return {
        register: function (user) {
            $.ajax({
                type:'post',
                url:baseUrl + '/user/register',
                contentType:'application/json;charset=utf-8',
                dataType:'json',
                //数据格式是json串
                data:JSON.stringify(user),
                success:function(data){   //测试能不能返回数据
                    alert(data);
                }
            });
        }
    }
}();