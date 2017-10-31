/**
 * Created by PLC on 2017/6/4.
 */
var pageGrid = Vue.extend({
    props: {
        url: '',
        autoInit: true,
        pageBar: null,
        pageBarSmall: false,
        tableHeight: ''    //设置表格高度百分比
    },

    data: function () {
        return {
            tableStyle: '',  //表格样式
            maxHeight: '100%',
            loading: true,
            pageData: null,
            queryParam: {
                pageSize: 10,
                pageNum: 1,
                param: {}
            },
            total: 0,
            currentPageNum: 1,
            _url: this.url,
            _pageBar: this.pageBar,
            _pageBarSmall: this.pageBarSmall
        };
    },
    template: [
        '<el-row>',
        '<el-table v-loading="loading" :data="pageData" :stripe="true" :border="true" :style="tableStyle" :max-height=maxHeight ',
        ' small="pageBarSmall" :page-size="queryParam.pageSize" @current-change="rowSelect" :highlight-current-row="true">',
        '<slot></slot>',
        '</el-table>',
        '<el-pagination :layout="pageBar != null ? pageBar : \'total, sizes, prev, pager, next, jumper\'"',
        ':total="total" @size-change="sizeChange"',
        ' @current-change="turnPage" :page-size="queryParam.pageSize"',
        ' :current-page="queryParam.pageNum"></el-pagination>',
        '</el-row>'
    ].join(''),
    methods: {
        sizeChange: function (size) {
            this.queryParam.pageSize = size;
            // var autoTableHeight = 40 * (this.queryParam.pageSize + 1);
            // this.tableStyle = 'width:100%;height:' + autoTableHeight + 'px';
            this.loadData();
        },
        reload: function (params) {
            for (var key in params) {
                this.queryParam[key] = params[key];
            }
            this.queryParam.pageNum = 1;
            this.loadData();
        },
        refresh: function (params) {
            for (var key in params) {
                this.queryParam[key] = params[key];
            }
            this.queryParam.pageNum = this.currentPageNum;
            this.loadData();
        },
        getSelectedRows: function () {
            return this.selectedRow;
        },
        rowSelect: function (selectedRow) {
            this.selectedRow = selectedRow;
            if (selectedRow) {   //有选中数据时才触发该事件
                this.$emit('current-change', selectedRow);
            }
        },
        turnPage: function (pageNum) {
            this.currentPageNum = pageNum;
            this.queryParam.pageNum = pageNum;
            this.loadData();
        },
        loadData: function () {
            var _this = this;
            _this.loading = true;
            czy.ajax.postJson({
                url: this.url + "/" + this.queryParam.pageNum + "/" + this.queryParam.pageSize,
                data: this.queryParam,
                success: function (response) {
                    _this.loading = false;
                    _this.pageData = response.data.page;
                    _this.total = response.data.total;
                },
                error: function (response) {
                    czy.msg.error("未知异常，请联系管理员");
                    _this.loading = false;
                }
            })
        }
    },
    created: function () {
        //动态计算表格高度
        var tableHeight = this.tableHeight == undefined ? '100%' : this.tableHeight;
        var percent = tableHeight.replace("%", "") / 100;
        // var autoTableHeight = document.body.scrollHeight * percent;
        // this.maxHeight = autoTableHeight;
        // var autoTableHeight = 40 * (this.queryParam.pageSize + 1);
        // this.tableStyle = 'width:100%;height:' + autoTableHeight + 'px';

        if (this.autoInit == undefined || this.autoInit == true) {
            this.loadData();
        } else {
            this.loading = false;
            this.pageData = [];
            this.total = 0;
        }
    }
});

Vue.component("czy-page-grid", pageGrid)