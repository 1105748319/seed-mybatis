/**
 * Created by PLC on 2017/6/3.
 */
var main_panel = new Vue({
        el: '#main-panel',
        data: {
            queryParam: {
                params: {}
            },
            //组织结构树
            treeData: [],   //机构树数据
            defaultExpandedKeys: [0],
            defaultProps: {
                children: 'children',
                label: 'name'
            },
            // 表单数据
            formData: {},
            editDialogShow: false,   //新增、修改表单是否显示
            formLabelWidth: '70px'   //表单标题宽度
        },

        methods: {
            query: function (val) {
                this.$refs.resTree.filter(this.queryParam.name);
            },
            loadData: function () {
                var _this = this;
                $.post("/sys/resource/selectResourceTree", this.queryParam, function (data) {
                    _this.treeData = data.data;
                });
            },
            filterNode: function (value, data) {
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            },
            edit: function (data) {
                this.formData = $.extend({}, data);
                this.editDialogShow = true;
            },
            save: function () {
                var _this = this;
                czy.ajax.postJson({
                    url: "/sys/resource/save",
                    data: _this.formData,
                    success: function (result) {
                        main_panel.loadData();
                        main_panel.editDialogShow = false;
                        czy.msg.success(result.msg);
                    }
                })
            },
            delete: function (data, store) {
                this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(
                    function () {
                        $.post("/sys/resource/deleteByPrimary/" + data.id, function (res) {
                            if (res.code == 200) {
                                czy.msg.info(res.msg);
                                this.loadData();
                            } else {
                                czy.msg.error(res.msg);
                            }
                        });
                    }).catch(function () {
                });
            },
            //渲染图标
            renderContent: function (createElement, param) {
                return this.buildOpeBtn(createElement, param.node, param.data, param.store);
            },
            //翻页
            changePage: function (pageNum) {
                this.queryParam.pageNum = pageNum;
                this.loadData();
            },
            //换选类型时，清空资源路径
            typesChange: function (label) {
                this.formData.url = "";
            },
            buildOpeBtn: function (createElement, node, data, store) {
                var addBtn = createElement('el-button', {
                    attrs: {size: "mini", type: "primary"}, on: {
                        click: function (event) {
                            event.stopPropagation();                //点击按钮时，树不自动打开
                            // main_panel.formData.parentId = data.id  // 将选中的节点的id值做为新增机构的parentId
                            main_panel.formData = {types: 1, parentId: data.id};                     //清空表单数据
                            main_panel.editDialogShow = true;
                        }
                    }
                }, "新增");
                var editBtn = createElement('el-button', {
                    attrs: {size: "mini", type: "warning"}, on: {
                        click: function (event) {
                            event.stopPropagation();    //点击按钮时，树不自动打开
                            main_panel.edit(data);
                        }
                    }
                }, "修改");

                var delBtn = createElement('el-button', {
                    attrs: {size: "mini", type: "danger"}, on: {
                        click: function (event) {
                            main_panel.delete(data, store);
                        }
                    }
                }, "删除");
                var btns = [];
                if (data.id == 0) {  //是虚拟根节点
                    btns = [addBtn]
                } else {    //是数据库中的正常节点
                    if (data.children.length > 0) {    //有子节点，不生成删除按钮
                        if (data.types == 2) {       //是目录，才生成“新增”按钮
                            btns.push(addBtn);
                        }
                        btns.push(editBtn);
                    } else {                        //无子节点，生成删除按钮
                        if (data.types == 2) {       //是目录，才生成“新增”按钮
                            btns.push(addBtn);
                        }
                        btns.push([editBtn, delBtn]);
                        // btns.push(delBtn);
                    }
                }
                return createElement('span', [
                    createElement('span',
                        {attrs: {style: "vertical-align: middle"}},
                        [
                            createElement('li',
                                {
                                    attrs: {
                                        class: data.icon == null ? "el-icon-menu" : data.icon,
                                        style: "font-size:3px;padding-bottom:12px;"
                                    },
                                }), node.label]),
                    // createElement('span', {attrs: {style: "background-color:red; min-width:300px; min-height:60px"}}, data.url),
                    createElement(
                        'span',
                        {attrs: {style: "float: right; margin-right: 20px;"}},
                        btns
                    )
                ])
            }
        },
        created: function () {
            this.loadData();
        }
    })
    ;
