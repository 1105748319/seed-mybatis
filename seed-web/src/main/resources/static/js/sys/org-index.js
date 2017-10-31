/**
 * Created by PLC on 2017/5/30.
 */
    //主窗口
var main = new Vue({
        el: '#main',
        data: {
            //组织结构树
            treeData: [],   //机构树数据
            defaultExpandedKeys: [0],
            defaultProps: {
                children: 'children',
                label: 'name'
            },
            //组织机构录入窗口
            sysOrg: {},
            operateDialogShow: false,    //操作窗口默认不显
            formLabelWidth: '70px',      //标题宽度
            //员工信息列表
            tableData: [],
            pageTotal: 1,
            pageCount:0,
            pageSize:20,
            //员工信息录入窗口
            userDialogShow:false,
            sysUser:{},
            currentRow:null,
            searchName:''
        },
        methods: {
            loadTree: function () {
                this.$http.post("/sys/org/selectOrgTree").then(
                    function (response) {
                        this.treeData = response.body;
                    },
                    function (response) {
                        this.$message({
                            message: '获取机构数据失败，请联系管理员',
                            type: 'error',
                            showClose: 'true',
                        });
                    }
                )
            },
            //机构树点击事件
            handleNodeClick: function (node) {
                console.log(node);
            },
            //渲染图标
            renderContent: function (createElement, param) {
                return buildOpeBtn(createElement, param.node, param.data, param.store);
            },
            //获取选中的树节点
            //打开组织机构保存弹窗
            openAddWin: function () {
                // 获取选中的树节点
                var selectedNode = this.$refs.orgTree.getCheckedNodes();
                if (selectedNode.length == 0) {          //未选中
                    this.sysOrg.parentId = 0;           //未选中时默认增加最高级组织机构：parentId = 0
                } else if (selectedNode.length > 1) {    //选中超过一条
                    this.$message({
                        message: '只能指定一个所属机构',
                        type: 'error'
                    });
                    return;
                } else {
                    this.sysOrg.parentId = selectedNode[0].id;
                }
                // 将选中的节点的id值做为新增机构的parentId
                this.operateDialogShow = true;
            },

            // 保存组织机构
            saveOrg: function () {
                this.$http.post("/sys/org/save", this.sysOrg).then(
                    function (success) {
                        this.operateDialogShow = false;  //关闭窗口
                        this.loadTree();
                    },
                    function (failure) {
                        console.log(this.data);
                    }
                )
            },

            // 查询用户信息
            loadUser: function (param) {
                if(param == undefined) {
                    param = czy.query.params()
                }
                param.params.name=this.searchName
                var main = this;
                $.ajax({
                    type:"POST",
                    url:"/sys/user/selectByPage",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(param),
                    success:function(success){
                        main.tableData = success.data.page;
                        main.pageTotal = success.data.total;
                        main.pageCount = success.data.pages;
                    }
                });
            },
            toPage: function (pageNum) {
                var param = {"pageNum":pageNum, "pageSize":this.pageSize, "params":{}}
                this.loadUser(param)
            },
            selectUser:function () {
                var param = {"pageNum":1, "pageSize":this.pageSize, "params":{}};
                this.loadUser(param)
            },
            addUser: function () {
                this.sysUser = {};
                this.userDialogShow = true;
            },
            editUser: function () {
                if(this.currentRow == null) {
                    czy.msg.error("请先选择一条数据");
                }
                this.sysUser = this.currentRow;
                this.userDialogShow = true;
            },
            saveUser:function () {
                this.$http.post("/sys/user/save", this.sysUser).then(
                    function (success) {
                        this.userDialogShow = false;  //关闭窗口
                        var param = {"pageNum":1, "pageSize":this.pageSize, "params":{}}
                        this.loadUser(param);
                    },
                    function (failure) {
                    }
                )
            },
            delUser:function () {
                if(this.currentRow == null) {
                    czy.msg.error("请先选择一条数据");
                }
                this.$confirm('此操作将永久删除该数据, 是否继续?', '提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(
                    function () {
                        $.post("/sys/user/deleteByPrimary/" + main.currentRow.id, function (success, data) {
                            if(success) {
                                czy.msg.info("操作成功");
                                var param = {"pageNum":1, "pageSize":this.pageSize, "params":{}};
                                main.loadUser(param)
                            } else {
                                czy.msg.error(data.msg);
                            }
                        });
                    }).catch(
                    function () {
                    });
            },
            //用户列表选择
            handleCurrentChange:function(selectRow){
                this.currentRow = selectRow
                console.log(this.currentRow)
            }
        },
        created: function () {
            this.loadTree();
            var param = {"pageNum":1, "pageSize":this.pageSize, "params":{}}
            this.loadUser(param);
        }
    });

function buildOpeBtn(createElement, node, data, store) {
    var editBtn = createElement('el-button', {
        attrs: {size: "mini", type: "warning"}, on: {
            click: function (event) {
                event.stopPropagation();    //点击按钮时，树不自动打开
                openEditWin(store, data.id)
            }
        }
    }, "修改");

    var delBtn = createElement('el-button', {
        attrs: {size: "mini", type: "danger"}, on: {
            click: function (event) {
                // store.remove({id: null, name: 'TEST', children: []}, data);
                event.stopPropagation();
                deleteOrg(data.id);
            }
        }
    }, "删除");
    var btns;
    if (data.children.length > 0) {
        btns = [editBtn];
    } else {
        btns = [editBtn, delBtn];
    }
    return createElement('span', [
        createElement('span', node.label),
        createElement(
            'span',
            {attrs: {style: "float: right; margin-right: 20px"}},
            btns
        )
    ])
}

function openEditWin(store, id) {
    Vue.http.get("/sys/org/loadData", {params: {id: id}}).then(
        function (success) {
            main.sysOrg = success.body;
            main.operateDialogShow = true;
        },
        function (failure) {
            alert(error + failure);
        }
    )
};

function deleteOrg(id) {
    main.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(
        function () {
            Vue.http.get("/sys/org/deleteOrg", {params: {id: id}}).then(
                function (success) {
                    if (success.body.code != 200) {
                        main.$message({
                            type: 'error',
                            message: success.body.msg
                        });
                    } else {
                        main.loadTree();
                        main.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                    }
                },
                function (failure) {
                    alert(failure);
                }
            )
        }).catch(
        function () {
            // main.$message({
            //     type: 'info',
            //     message: '已取消删除'
            // });
        });
}