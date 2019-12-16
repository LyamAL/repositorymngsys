var baseUrl = "http://localhost:8080";


var User = function () {
    return {
        deleteUser: function (phone, idx) {
            alert(phone)

            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            $.ajax({
                url: baseUrl + "/user/delete?phone=" + phone,
                type: "delete",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                async: true,
                contentType: "application/json",
                success: function (data) {
                    if (data === "success") {
                        $("#user" + idx).remove();//主要就是这个删除成功后移除这行数据
                        parent.location.reload()
                    }
                }
            })
        },
        switchFrame: function (index) {
            if (index === 0){
                //切换为添加用户
                $('#headline').text('添加用户');
                this.showRegForm();
            }else if (index === 1 ){
                $('#headline').text('更改用户');
                this.showUpdateForm();
            }else if (index === 2 ){
                $('#headline').text('所有用户');
                this.showTable();
            }
        },
        showUpdateForm: function () {
            $('#user-form').css('display', 'none');
            $('#users-table').css('display', 'none');
            $('#update-user-form').css('display', 'block');
        },
        showRegForm: function () {
            $('#user-form').css('display', 'block');
            $('#users-table').css('display', 'none');
            $('#update-user-form').css('display', 'none');
        },
        showTable: function () {
            $('#user-form').css('display', 'none');
            $('#users-table').css('display', 'block');
            $('#update-user-form').css('display', 'none');
        },
        init: function () {
            $("#btn-submit-change").on("click", function () {
                $('#update-failure').css('display', 'none');

                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                $.ajax({
                    url: baseUrl + "/user/update",
                    type: 'put',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    data: $('#update-user-form').serialize(),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        if (data.responseText === "success") {
                            window.location.reload()
                        } else {
                            $('#update-failure').css('display', 'block');
                            $('#update-failure').text(data.responseText);
                        }
                    },
                    clearForm: false,//禁止清楚表单
                    resetForm: false //禁止重置表单
                });
            });
            $("#btn-submit").on("click", function () {
                $('#dup-phone-hint').css('display', 'none');

                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                $.ajax({
                    url: baseUrl + "/user/add",
                    type: 'post',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    data: $('#user-form').serialize(),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        alert(JSON.stringify(data));
                        if (data.responseText === "success") {
                            window.location.reload()
                        } else {
                            $('#dup-phone-hint').css('display', 'block');
                            $('#dup-phone-hint').text(data.responseText);
                        }
                    },
                    clearForm: false,//禁止清楚表单
                    resetForm: false //禁止重置表单
                });
            });
        },
        fillInForm: function(user) {
            $('#update-username').attr('value', user.username);
            $('#update-phone').attr('value', user.phone);
            $('#old-phone-hidden').attr('value', user.phone);

            const titles = user.titles;
            const roles = titles.split(',');
            for (let i = 0; i < roles.length; i++) {
                alert(roles[i]);
                switch (roles[i]) {
                    case '系统管理员':
                        $('#chk-sysAdmin').attr('checked', "");
                        break;
                    case '采购员':
                        $('#chk-purchaser').attr('checked', "");
                        break;
                    case '发货员':
                        $('#chk-consignor').attr('checked', "");
                        break;
                    case '仓库管理员':
                        $('#chk-repoAdmin').attr('checked', "");
                        break;
                    case '普通用户':
                        $('#chk-user').attr('checked', "");
                        break;
                }
            }
        }
    }
}();