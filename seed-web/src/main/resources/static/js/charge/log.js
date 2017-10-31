
var pane = new Vue({
    el: '#log',
    data: function () {

        return {
            logData:[],
            logForm:{},
            currentPage: 1,
            total: 0
        }

    },

    methods: {
        //时间转换器
        dateFormat:function(row, column) {
            debugger
            return  new Date(row.date).format("yyyy-MM-dd hh:mm:ss");
        },

        handleCurrentChange:function (pageNum) {
            this.currentPage = pageNum;
            this.queryListByParams();
        },
        queryListByParams: function () {
            var _this = this;
            var params = {};
            params.name = _this.logForm.name;
            params.passenger = _this.logForm.passenger;
            params.pageSize = 8;
            params.pageNum = _this.currentPage;
            $.post("/charge/log/queryList", params, function (data) {
                _this.logData = data.data.logData;
                _this.total = data.data.total;
            });
        }

    },

    created: function () {
        var _this = this;
        var params = {};
        params.pageSize = 8;
        params.pageNum = _this.currentPage;
        $.post("/charge/log/queryList", params, function (data) {
            _this.logData = data.data.logData;
            _this.total = data.data.total;
        });

    }


});