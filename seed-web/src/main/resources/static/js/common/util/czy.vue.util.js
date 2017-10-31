/**
 * Created by PLC on 2017/6/1.
 */

/**
 *监听登陆过期请求
 */
$.ajaxSetup({
    complete: function (response, status) {
        if (response.status == 401 && response.responseText.indexOf("Unauthorized") > 0) {
            window.location.href = "/login";
        }
    }
});


/**
 * 定义全局工具类
 */
(function () {
    window.czy = {
        /**
         *弹出消息工具方法
         */
        msg: {
            success: function (msg) {
                main_contain.$message({
                    type: 'success',
                    message: msg
                });
            },
            error: function (msg) {
                main_contain.$message({
                    type: 'error',
                    message: msg
                });
            },
            warn: function (msg) {
                main_contain.$message({
                    type: 'warning',
                    message: msg
                });
            },
            info: function (msg) {
                main_contain.$message({
                    type: 'info',
                    message: msg
                });
            },
            /**
             * 自动判断提示类型，与后台方法绑定
             * @param msg
             */
            auto: function (msg) {

            }
        },
        /**
         * 确认弹窗并执行任务——临时，不要使用
         * @param obj
         * @param msg
         * @param title
         * @param url
         * @param params
         * @param callback
         */
        confirm: function (obj, msg, title, url, params, callback) {
            if (params == undefined || params == null) {
                params = {};
            }
            main_contain.$confirm(msg, {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(
                function () {
                    $.post(url, {params: params}, callback(data));
                }).catch(
                function () {
                });
        },
        query: {
            params: function (pageNum, pageSize, params) {
                var _params = {"pageNum": 0, "pageSize": 20, "params": {}}
                _params.params = params == undefined ? params : {};
                _params.pageNum = pageNum == undefined ? 1 : pageNum;
                _params.pageSize = pageSize == undefined ? 1 : pageSize;
                return _params;
            }
        },
        /**
         * 遮罩
         */
        mask: {
            loading: window.ELEMENT.Loading,
            mask: null,  //打开后的遮罩对象
            target: null,//打开遮罩的DOM对象
            settings: {fullscreen: true, text: '拼命加载中...'},  //遮罩设置
            open: function () { //打开遮罩
                this.mask = this.loading.service(this.settings);
            },
            close: function () {//关闭遮罩
                if (this.mask) {
                    this.mask.close();
                }
            }
        },
        /**
         * 带遮罩的ajax请求
         */
        ajax: {
            postJson: function (options) {
                var defaults = {
                    type: "POST",
                    contentType: "application/json;charset=utf-8",
                    dataType: "json"
                };
                var _options = $.extend({}, defaults, options);
                _options.data = JSON.stringify(options.data);
                $.ajax(_options);
            },
            postWithMask: function (options) {
                var defaults = {};
                var callbackOption = {
                    beforeSend: function (XMLHttpRequest) {
                        czy.mask.open();   //打开遮罩
                        if (options.beforeSend && typeof options.beforeSend == 'function') {
                            options.beforeSend(XMLHttpRequest);
                        }
                    },
                    success: function (result) {
                        czy.mask.close();   //取消遮罩
                        if (options.success && typeof options.success == 'function') {
                            options.success(result);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        czy.mask.close();   //取消遮罩
                        if (options.error && typeof options.error == 'function') {
                            options.error(XMLHttpRequest, textStatus, errorThrown);
                        }
                        //跳转登陆页面    toDo
                        //展示错误信息    toDo
                    }
                }
                var _options = $.extend({}, defaults, options);
                var settings = $.extend({}, _options, callbackOption);
                $.ajax(settings);
            },

            /**
             *
             * @param options
             */
            postJsonWithMask: function (options) {
                var defaults = {
                    type: "POST",
                    contentType: "application/json;charset=utf-8",
                    dataType: "json"
                };
                var _options = $.extend({}, defaults, options);
                _options.data = JSON.stringify(options.data);
                czy.ajax.postWithMask(_options);
            }
        },

        /**
         * 加载页面到主显示div内去
         * @param url
         * @param params
         * @param callback
         */
        loadToMainDiv: function (url, params, callback) {
            $(".content-wrapper").load(url, params, function (result) {
                if (typeof callback == 'function') {
                    callback(result);
                }
            })
        },
        operateWin: function (id, url) {
            var operateWin = new Vue({
                el: id,
                data: {formData: {}},
                methods: {
                    save: function () {
                        $.post(url, formData, function (data) {

                        })
                    }
                }
            });
        }
    };
})();