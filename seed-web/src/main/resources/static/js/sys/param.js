/**
 * Created by PLC on 2017/6/3.
 */
var main_panel = new Vue({
    el: '#main-panel',
    data: {
        selectedRow: null,      //列表选中行
        queryParam: {           //查询参数
            pageNum: 1,
            pageSize: 10
        },
        pageData: null,         //分页数据
        total: 0,               //数据总量
        // 表单数据
        formData: {},            //表单数据
        editDialogShow: false,   //新增、修改表单是否显示
        formLabelWidth: '70px'   //表单标题宽度
    },

    methods: {
        query: function () {
            this.queryParam.pageNum = 1;
            this.loadData();
        },
        loadData: function () {
            var _this = this;
            $.post("/sys/param/selectPageByParams", this.queryParam, function (data) {
                _this.pageData = data.data.page;
                _this.total = data.data.total;
            });
        },
        add: function () {
            this.formData = {};
            this.editDialogShow = true;
        },
        edit: function () {
            if (this.selectedRow == null) {
                czy.msg.error("请选择要操作的数据");
                return;
            }
            this.formData = this.selectedRow;
            this.editDialogShow = true;
        },
        save: function () {
            czy.ajax.postJson({
                utl:"/sys/param/save",
                data:this.formData,
                success:function (result) {
                main_panel.editDialogShow = false;
                main_panel.loadData();
            }});
        },
        gridSelect: function (selectedRow) {
            this.selectedRow = selectedRow;
        },
        changePage: function (pageNum) {
            this.queryParam.pageNum = pageNum;
            this.loadData();
        }
    },
    created: function () {
        this.loadData();
    }

});