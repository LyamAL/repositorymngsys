var baseUrl = "http://localhost:8080";


var User = function () {
    return {
        deleteUser: function (phone, idx) {
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
            if (index === 0) {
                //切换为添加用户
                $('#headline').text('添加用户');
                this.showRegForm();
            } else if (index === 1) {
                $('#headline').text('更改用户');
                this.showUpdateForm();
            } else if (index === 2) {
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
        fillInForm: function (user) {
            $('#update-username').attr('value', user.username);
            $('#update-phone').attr('value', user.phone);
            $('#old-phone-hidden').attr('value', user.phone);

            const titles = user.titles;
            const roles = titles.split(',');
            for (let i = 0; i < roles.length; i++) {
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

var Repo = function () {

    return {
        init: function () {
            $("#btn-update-repo").on("click", function () {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                $.ajax({
                    url: baseUrl + "/repo/update",
                    type: 'put',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    data: $('#update-repo-form').serialize(),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        if (data.responseText === "success") {
                            window.location.reload()
                        }
                    },
                    clearForm: false,//禁止清楚表单
                    resetForm: false //禁止重置表单
                });
            });
            $("#btn-submit-repo").on("click", function () {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                $.ajax({
                    url: baseUrl + "/repo/add",
                    type: 'post',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    data: $('#add-repo-form').serialize(),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        if (data.responseText === "success") {
                            window.location.reload()
                        }
                    },
                    clearForm: false,//禁止清楚表单
                    resetForm: false //禁止重置表单
                });
            });
            var kv = $('#data-repos').val();
            var re = /[(][^)]*[)]/g;

            var usedArr = [];
            var arr;
            var start = 0;
            var end = 0;
            var posArr = [];
            var emptyArr = [];
            var i = 0;
            while ((arr = re.exec(kv)) != null) {
                if (start === 0) {
                    start = arr.index + 1
                } else {
                    end = arr.index - 7;
                    var repoStr = kv.substring(start, end);
                    var idEnd = repoStr.search(/, ca/i);
                    var capEnd = repoStr.search(/, us/i);
                    var usedEnd = repoStr.search(/, pos/i);
                    idx = repoStr.search(/, us/i);
                    var capacity = repoStr.substring(idEnd + 11, capEnd);
                    var used = repoStr.substring(capEnd + 7, usedEnd);
                    usedArr[i] = parseInt(used);
                    posArr[i] = repoStr.substring(usedEnd + 11);
                    emptyArr[i] = parseInt(capacity) - parseInt(used);
                    i += 1;
                    start = arr.index + 1;
                }

            }

            var barChart = new Chart($('#barChart'), {
                type: 'bar',
                data: {
                    labels: posArr,
                    datasets: [
                        {
                            label: "已用容量",
                            backgroundColor: [
                                'rgba(51, 179, 90, 0.6)',
                                'rgba(51, 179, 90, 0.6)',
                                'rgba(51, 179, 90, 0.6)',
                                'rgba(51, 179, 90, 0.6)',
                                'rgba(51, 179, 90, 0.6)',
                                'rgba(51, 179, 90, 0.6)',
                                'rgba(51, 179, 90, 0.6)'
                            ],
                            borderColor: [
                                'rgba(51, 179, 90, 1)',
                                'rgba(51, 179, 90, 1)',
                                'rgba(51, 179, 90, 1)',
                                'rgba(51, 179, 90, 1)',
                                'rgba(51, 179, 90, 1)',
                                'rgba(51, 179, 90, 1)',
                                'rgba(51, 179, 90, 1)'
                            ],
                            borderWidth: 1,
                            data: usedArr,
                        },
                        {
                            label: "空闲容量",
                            backgroundColor: [
                                'rgba(203, 203, 203, 0.6)',
                                'rgba(203, 203, 203, 0.6)',
                                'rgba(203, 203, 203, 0.6)',
                                'rgba(203, 203, 203, 0.6)',
                                'rgba(203, 203, 203, 0.6)',
                                'rgba(203, 203, 203, 0.6)',
                                'rgba(203, 203, 203, 0.6)'
                            ],
                            borderColor: [
                                'rgba(203, 203, 203, 1)',
                                'rgba(203, 203, 203, 1)',
                                'rgba(203, 203, 203, 1)',
                                'rgba(203, 203, 203, 1)',
                                'rgba(203, 203, 203, 1)',
                                'rgba(203, 203, 203, 1)',
                                'rgba(203, 203, 203, 1)'
                            ],
                            borderWidth: 1,
                            data: emptyArr,
                        }
                    ]
                }
            });

        },
        switchFrame: function (index) {
            if (index === 0) {
                //切换为添加仓库
                $('#headline').text('添加仓库');
                this.showRepoForm();
            } else if (index === 1) {
                $('#headline').text('修改仓库');
                this.showUpdateRepoForm();
            } else if (index === 2) {
                $('#headline').text('所有仓库');
                this.showRepoTable();
            }
        },
        showUpdateRepoForm: function () {
            $('#add-repo-form').css('display', 'none');
            $('#repos-table').css('display', 'none');
            $('#chart').css('display', 'none');
            $('#update-repo-form').css('display', 'block');
        },
        showRepoForm: function () {
            $('#add-repo-form').css('display', 'block');
            $('#chart').css('display', 'none');
            $('#repos-table').css('display', 'none');
            $('#update-repo-form').css('display', 'none');
        },
        showRepoTable: function () {
            $('#add-repo-form').css('display', 'none');
            $('#repos-table').css('display', 'block');
            $('#update-repo-form').css('display', 'none');
            $('#chart').css('display', 'block');
        },
        fillInForm: function (repo) {
            $('#update-position').attr('value', repo.position);
            $('#update-capacity').attr('value', repo.capacity);
            $('#update-used').attr('value', repo.used);
            $('#update-id').attr('value', repo.id);
        },
        deleteRepo: function (id) {

            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            $.ajax({
                url: baseUrl + "/repo/delete?id=" + id,
                type: "delete",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                async: true,
                contentType: "application/json",
                success: function (data) {
                    if (data === "success") {
                        parent.location.reload()
                    } else {
                        alert('fail');
                    }
                }
            })
        },
    }
}();

var RepoStorage = function () {

    return {
        init: function () {
            $("#submit-position").on("click", function () {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                var optionId = $('#select').get(0).options[$('#select').get(0).selectedIndex].id;
                var position = $('#select').get(0).options[$('#select').get(0).selectedIndex].value;
                var id = parseInt(optionId.substring(3));
                $.ajax({
                    url: baseUrl + "/storage/repo/select?id=" + id + "&position=" + position,
                    type: 'get',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        window.location.href = baseUrl + data.responseText
                    }
                });
            });
        },
        updateStorage(gid, repoId, idx, count) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");

            var storage = {};
            storage.gid = gid;
            if (count === 1) {
                storage.qualified = $("input[name='qualified']").val();
                storage.count = $("input[name='count']").val();
            } else {
                storage.qualified = $("input[name='qualified']").eq(idx - 1).val();
                storage.count = $("input[name='count']").eq(idx - 1).val();
            }
            storage.repoId = repoId;
            $.ajax({
                url: baseUrl + "/storage/repo/good/update",
                type: 'put',
                contentType: 'application/json',
                dataType: 'json',
                async: false,
                data: JSON.stringify(storage),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                complete: function (data) {
                    window.location.reload();
                },
                clearForm: false,//禁止清楚表单
                resetForm: false //禁止重置表单
            });
        },
        deleteGoodInRepo(gid, repoId) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");

            $.ajax({
                url: baseUrl + "/storage/repo/good/delete?id=" + gid + "&repoId=" + repoId,
                type: 'delete',
                contentType: 'application/x-www-form-urlencoded',
                dataType: 'text',
                async: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                complete: function (data) {
                    window.location.reload()
                    // if (data.responseText !== "fail") {
                    //     window.location.href = baseUrl + data.responseText;
                    // }
                },
                clearForm: false,//禁止清楚表单
                resetForm: false //禁止重置表单
            });
        }
    }
}();

var GoodStorage = function () {

    return {
        init: function () {
            $("#submit-position").on("click", function () {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                var optionId = $('#select').get(0).options[$('#select').get(0).selectedIndex].id;
                var name = $('#select').get(0).options[$('#select').get(0).selectedIndex].value;
                var id = parseInt(optionId.substring(3));
                $.ajax({
                    url: baseUrl + "/storage/goods/select?id=" + id + "&name=" + name,
                    type: 'get',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        window.location.href = baseUrl + data.responseText
                    }
                });
            });
        },

        updateStorage(gid, repoId, idx, count) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");

            var storage = {};
            storage.gid = gid;
            if (count === 1) {
                storage.qualified = $("input[name='qualified']").val();
                storage.count = $("input[name='count']").val();
            } else {
                storage.qualified = $("input[name='qualified']").eq(idx - 1).val();
                storage.count = $("input[name='count']").eq(idx - 1).val();
            }
            storage.repoId = repoId;
            $.ajax({
                url: baseUrl + "/storage/goods/good/update",
                type: 'put',
                contentType: 'application/json',
                dataType: 'json',
                async: false,
                data: JSON.stringify(storage),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                complete: function (data) {
                    window.location.reload();
                },
                clearForm: false,//禁止清楚表单
                resetForm: false //禁止重置表单
            });
        },
        deleteGoodInRepo(gid, repoId) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");

            $.ajax({
                url: baseUrl + "/storage/goods/good/delete?id=" + gid + "&repoId=" + repoId,
                type: 'delete',
                contentType: 'application/x-www-form-urlencoded',
                dataType: 'text',
                async: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                complete: function (data) {
                    window.location.reload()

                },
                clearForm: false,//禁止清楚表单
                resetForm: false //禁止重置表单
            });
        }
    }
}();
var Good = function () {
    return {
        deleteGood: function (id) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            $.ajax({
                url: baseUrl + "/good/delete?id=" + id,
                type: "delete",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                async: true,
                contentType: "application/json",
                success: function (data) {
                    if (data === "success") {
                        parent.location.reload()
                    }
                }
            })
        },
        showUpdateForm: function () {
            $('#good-form').css('display', 'none');
            $('#goods-table').css('display', 'none');
            $('#goods-sale-chart').css('display', 'block');
            $('#update-good-form').css('display', 'block');

        },
        showAddForm: function () {
            $('#good-form').css('display', 'block');
            $('#goods-sale-chart').css('display', 'none');
            $('#goods-table').css('display', 'none');
            $('#update-good-form').css('display', 'none');
        },
        showTable: function () {
            $('#good-form').css('display', 'none');
            $('#goods-sale-chart').css('display', 'none');
            $('#goods-table').css('display', 'block');
            $('#update-good-form').css('display', 'none');
        },
        switchFrame: function (index) {
            if (index === 0) {
                //add
                $('#headline').text('添加货品');
                this.showAddForm();
            } else if (index === 1) {
                $('#headline').text('修改货品');
                this.showUpdateForm();
            } else {
                $('#headline').text('所有货品');
                this.showTable();
            }
        },
        init: function () {
            $("#btn-submit-change").on("click", function () {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                $.ajax({
                    url: baseUrl + "/good/update",
                    type: 'put',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    data: $('#update-good-form').serialize(),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        if (data.responseText === "success") {
                            window.location.reload()
                        }
                    },
                    clearForm: false,//禁止清楚表单
                    resetForm: false //禁止重置表单
                });
            });
            $("#btn-submit").on("click", function () {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                $.ajax({
                    url: baseUrl + "/good/add",
                    type: 'post',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    data: $('#good-form').serialize(),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        if (data.responseText === "success") {
                            window.location.reload()
                        }
                    },
                    clearForm: false,//禁止清楚表单
                    resetForm: false //禁止重置表单
                });
            });
        },
        fillInForm: function (good) {
            $('#update-name').attr('value', good.name);
            $('#update-origin').attr('value', good.origin);
            $('#update-price').attr('value', good.price);
            $('#update-unit').attr('value', good.unit);
            $('#update-id').attr('value', good.id);

            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");

            var returnedObj = [];

            $.ajax({
                url: baseUrl + "/good/soat?id=" + good.id,
                type: "get",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                async: false,
                contentType: "application/json",
                complete: function (data) {
                    returnedObj = JSON.parse(data.responseText);
                },
            });

            var sales = [];
            var months = [];
            var prices = [];
            for (let i = 0; i < returnedObj.length; i++) {
                sales[i] = returnedObj[i].count;
                months[i] = returnedObj[i].date;
                prices[i] = returnedObj[i].sum;
            }

            var brandPrimary = 'rgba(51, 179, 90, 1)';
            var mySaleChart = new Chart($('#goods-sale-chart'), {
                type: 'line',
                data: {
                    labels: months,
                    datasets: [
                        {
                            label: '销售量',
                            fill: true,
                            lineTension: 0.3,
                            backgroundColor: "rgba(51, 179, 90, 0.38)",
                            borderColor: brandPrimary,
                            borderCapStyle: 'butt',
                            borderDash: [],
                            borderDashOffset: 0.0,
                            borderJoinStyle: 'miter',
                            borderWidth: 1,
                            pointBorderColor: brandPrimary,
                            scaleShowLabels: true,
                            pointBackgroundColor: "#fff",
                            pointBorderWidth: 1,
                            pointHoverRadius: 5,
                            pointHoverBackgroundColor: brandPrimary,
                            pointHoverBorderColor: "rgba(220,220,220,1)",
                            pointHoverBorderWidth: 2,
                            pointRadius: 1,
                            pointHitRadius: 10,
                            data: sales,
                            spanGaps: false,
                        },
                        {
                            label: '销售额',
                            fill: true,
                            lineTension: 0.3,
                            backgroundColor: "rgba(75,192,192,0.4)",
                            borderColor: "rgba(75,192,192,1)",
                            borderCapStyle: 'butt',
                            borderDash: [],
                            borderDashOffset: 0.0,
                            borderJoinStyle: 'miter',
                            borderWidth: 1,
                            pointBorderColor: "rgba(75,192,192,1)",
                            pointBackgroundColor: "#fff",
                            pointBorderWidth: 1,
                            pointHoverRadius: 5,
                            pointHoverBackgroundColor: "rgba(75,192,192,1)",
                            pointHoverBorderColor: "rgba(220,220,220,1)",
                            pointHoverBorderWidth: 2,
                            pointRadius: 1,
                            pointHitRadius: 10,
                            data: prices,
                            spanGaps: false,
                        }
                    ],
                },
                options: {
                    responsive: true,
                    // 标题
                    title: {
                        display: false,
                        text: ['销售量统计', '(以月份为单位)'], // 标题及子标题
                        fontColor: ['#000000'],
                        fontSize: 15
                    },
                    // 图例
                    legend: {
                        position: 'bottom',
                        labels: {
                            fontSize: 10,
                            boxWidth: 9
                        }
                    },

                    // 坐标
                    scales: {
                        xAxes: [{
                            display: true,
                            scaleLabel: {
                                display: true,
                                labelString: '月份'
                            }
                        }],
                        yAxes: [{
                            display: true,
                            scaleLabel: {
                                display: true,
                                labelString: '销售量/销售价格'
                            }
                        }]
                    },
                }

            });
        }
    }
}();

var Dlvr = function () {
    return {
        deleteSheet: function (sheetId) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            $.ajax({
                url: baseUrl + "/sheet/dlvr/delete?id=" + sheetId,
                type: "delete",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                async: true,
                contentType: "application/json",
                success: function (data) {
                    if (data === "success") {
                        parent.location.reload()
                    }
                }
            })
        },
        init: function () {
            $("#submit-add-dlvr").on("click", function () {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                $.ajax({
                    url: baseUrl + "/sheet/dlvr/add",
                    type: 'post',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    data: $('.form-horizontal').serialize(),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        if (data.responseText !== "fail") {
                            window.location.href = baseUrl + data.responseText;
                        }
                    },
                    clearForm: false,//禁止清楚表单
                    resetForm: false //禁止重置表单
                });
            });
        }
    }
}();
var DlvrGoods = function () {
    return {
        init: function () {
            $("#submit-add-dlvr-good").on("click", function () {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                $.ajax({
                    url: baseUrl + "/sheet/dlvr/addGoods",
                    type: 'post',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    data: $('.form-horizontal').serialize(),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        if (data.responseText !== "fail") {
                            window.location.href = baseUrl + data.responseText;
                        }
                    },
                    clearForm: false,//禁止清楚表单
                    resetForm: false //禁止重置表单
                });
            });
        },
        deleteGoodsOnSheet(gid, sheetId) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");

            $.ajax({
                url: baseUrl + "/sheet/dlvr/goods/delete?id=" + gid + "&sheetId=" + sheetId,
                type: 'delete',
                contentType: 'application/x-www-form-urlencoded',
                dataType: 'text',
                async: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                complete: function (data) {
                    window.location.reload();
                },
                clearForm: false,//禁止清楚表单
                resetForm: false //禁止重置表单
            });
        },
        updateGoodsOnSheet(gid, deliveryId) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");

            var goods = {};
            goods.gid = gid;
            goods.price = $("input[name='price']").val();
            goods.count = $("input[name='count']").val();
            goods.sum = $("input[name='sum']").val();
            goods.deliveryId = deliveryId;
            goods.note = $("#update-goods-note").val();
            $.ajax({
                url: baseUrl + "/sheet/dlvr/goods/update",
                type: 'put',
                contentType: 'application/json',
                dataType: 'json',
                async: false,
                data: JSON.stringify(goods),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                complete: function (data) {
                    window.location.reload();
                },
                clearForm: false,//禁止清楚表单
                resetForm: false //禁止重置表单
            });
        },
        updateSheet(sheetId) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            var sheet = {};
            sheet.id = sheetId;
            sheet.destination = $("input[name='destination']").val();
            sheet.deliveryStatus = $("input[name='deliveryStatus']").val();
            sheet.note = $("#update-sheet-note").val();
            sheet.contact = $("select[name='contact']").val();
            sheet.closeTime = $("input[name='closeTime']").val();

            $.ajax({
                url: baseUrl + "/sheet/dlvr/update/sub",
                type: 'put',
                contentType: 'application/json',
                dataType: 'json',
                async: false,
                data: JSON.stringify(sheet),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                complete: function (data) {
                    window.location.reload();
                },
                clearForm: false,//禁止清楚表单
                resetForm: false //禁止重置表单
            });
        }
    }
}();

var Entr = function () {
    return {
        deleteSheet: function (sheetId) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            $.ajax({
                url: baseUrl + "/sheet/entr/delete?id=" + sheetId,
                type: "delete",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                async: true,
                contentType: "application/json",
                success: function (data) {
                    if (data === "success") {
                        parent.location.reload()
                    }
                }
            })
        },
        init: function () {
            $("#submit-add-entr").on("click", function () {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                $.ajax({
                    url: baseUrl + "/sheet/entr/add",
                    type: 'post',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    data: $('.form-horizontal').serialize(),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        if (data.responseText !== "fail") {
                            window.location.href = baseUrl + data.responseText;
                        }
                    },
                    clearForm: false,//禁止清楚表单
                    resetForm: false //禁止重置表单
                });
            });
        }
    }
}();


var EntrGoods = function () {
    return {
        init: function () {
            //添加入库货品
            $("#submit-add-entr-good").on("click", function () {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");

                $.ajax({
                    url: baseUrl + "/sheet/entr/addGoods",
                    type: 'post',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: 'text',
                    async: false,
                    data: $('.form-horizontal').serialize(),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    complete: function (data) {
                        if (data.responseText !== "fail") {
                            window.location.href = baseUrl + data.responseText;
                        }
                    },
                    clearForm: false,//禁止清楚表单
                    resetForm: false //禁止重置表单
                });
            });
        },
        deleteGoodsOnSheet(gid, sheetId) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");

            $.ajax({
                url: baseUrl + "/sheet/entr/goods/delete?id=" + gid + "&sheetId=" + sheetId,
                type: 'delete',
                contentType: 'application/x-www-form-urlencoded',
                dataType: 'text',
                async: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                complete: function (data) {
                    window.location.reload();
                },
                clearForm: false,//禁止清楚表单
                resetForm: false //禁止重置表单
            });
        },
        updateGoodsOnSheet(gid, entrId) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");

            var goods = {};
            goods.gid = gid;
            goods.price = $("input[name='price']").val();
            goods.count = $("input[name='count']").val();
            goods.sum = $("input[name='sum']").val();
            goods.qualified = $("input[name='qualified']").val();
            goods.entryId = entrId;
            $.ajax({
                url: baseUrl + "/sheet/entr/goods/update",
                type: 'put',
                contentType: 'application/json',
                dataType: 'json',
                async: false,
                data: JSON.stringify(goods),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                complete: function (data) {
                    window.location.reload();
                },
                clearForm: false,//禁止清楚表单
                resetForm: false //禁止重置表单
            });
        },
        updateSheet(sheetId) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            var sheet = {};
            sheet.id = sheetId;
            sheet.producer = $("input[name='producer']").val();
            sheet.status = $("input[name='status']").val();
            sheet.note = $("#update-sheet-note").val();
            sheet.contact = $("select[name='contact']").val();
            sheet.closeTime = $("input[name='closeTime']").val();

            $.ajax({
                url: baseUrl + "/sheet/entr/update/sub",
                type: 'put',
                contentType: 'application/json',
                dataType: 'json',
                async: false,
                data: JSON.stringify(sheet),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                complete: function (data) {
                    window.location.reload();
                },
                clearForm: false,//禁止清楚表单
                resetForm: false //禁止重置表单
            });
        }
    }
}();