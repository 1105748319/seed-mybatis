/**
 * Created by lenovo on 2017/6/26.
 */

var VE = new Vue({

    el: '#rulebind',

    data: function () {
        var checkFs = function (rule, value, callback) {
            var re = new RegExp("^[a-z,A-Z]{6}$");
            if (!re.test(value)) {
                return callback(new Error('flight line is not valid'));
            } else {
                return callback();
            }
        };
        var checkProductName = function (rule, value, callback) {
            var ptype = VE.editForm.productType;

            if (ptype == 'SEAT') {
                var re = new RegExp("^(([1-9][0-9]*)|(([1-9][0-9]*,)+[1-9][0-9]*)|([1-9][0-9]*-([1-9][0-9]*)?))$");
                if (re.test(value)) {
                    if (value.indexOf(',') != -1) {
                        var temp = value.split(',');
                        var tempSet = new Set(temp);
                        if (tempSet.size == temp.length) {
                            return callback();
                        } else {
                            return callback(new Error("format is not valid"));
                        }
                    } else if (value.indexOf('-') != -1) {
                        var temp = value.split('-');
                        if (temp[1] == '' || parseInt(temp[0]) < parseInt(temp[1])) {
                            return callback();
                        } else {
                            return callback(new Error("format is not valid"));
                        }
                    } else {
                        return callback();
                    }
                } else {
                    return callback(new Error("format is not valid"));
                }
            } else  if (ptype == 'BAGGAGE') {
                var re = new RegExp("^((0-[1-9][0-9]*)|([1-9][0-9]*-([1-9][0-9]*)?))$");
                if (re.test(value)) {
                    var temp = value.split('-');
                    if (temp[1]=='') {
                        return callback();
                    } else if (parseInt(temp[0]) > parseInt(temp[1])) {
                        return callback(new Error("format is not valid"));
                    } else {
                        return callback();
                    }
                } else {
                    return callback(new Error("format is not valid"));
                }
            } else {
                return callback();
            }
        };
        var checkNot0StartNum = function (rule, value, callback) {
            var re = new RegExp("^(([1-9][0-9]*(\\.[0-9]+)?)|([0-9](\\.[0-9]+)?))$");
            if (!re.test(value)) {
                return callback(new Error('unit price is not valid'));
            } else {
                return callback();
            }
        };

        return {
            editForm: {
                productNum: '',
                productName: '',
                unitType: '',
                id: '',
                productType: '',
                fs: '',
                rulesName: '',
                rulesVal: '',
                productId: ''
            },
            types: [],
            tableData: [],
            currentPage: 1,
            total: 0,
            rulesProduct: {
                fs: [
                    {required: true, validator: checkFs, trigger: 'blur'}
                ],
                rulesName: [
                    {required: true, validator: checkProductName, trigger: 'blur'}
                ],
                rulesVal: [
                    {required: true, validator: checkNot0StartNum, trigger: 'blur'}
                ]
            }


        }

    },


    created: function () {
        var _this = this;
        var params = {};
        params.pageNum = 1;
        params.pageSize = 8;
        $.post("/charge/rulebind/queryList", params, function (data) {
            _this.tableData = data.data.rules;
        });
        this.queryTotal();

    },

    methods: {
        handleCurrentChange: function (pageNum) {
            VE.currentPage = pageNum;
            VE.queryRuleList();
        },

        queryRuleList: function () {
            var params = {};
            params.pageNum = VE.currentPage;
            params.pageSize = 8;
            $.post("/charge/rulebind/queryList", params, function (data) {
                VE.tableData = data.data.rules;
            })
        },

        queryRule: function (currentRow, oldCurrentRow) {
            this.$refs['ruleForm'].resetFields();
            $.post("/charge/rulebind/queryOne", {productId: currentRow.productId}, function (data) {
                if (data.code == '200') {
                    var pd = data.data.product;
                    VE.editForm.productNum = pd.productNum;
                    VE.editForm.productName = pd.productName;
                    VE.editForm.unitType = pd.unitType;
                    VE.editForm.id = pd.id;
                    VE.editForm.productType = pd.productType;
                    VE.editForm.fs = currentRow.fs;
                    VE.editForm.rulesName = currentRow.rulesName;
                    VE.editForm.rulesVal = currentRow.rulesVal;
                    VE.editForm.id = currentRow.id;
                    VE.editForm.productId = currentRow.productId;
                } else {
                    VE.editForm.productNum = '';
                    VE.editForm.productName = '';
                    VE.editForm.unitType = '';
                    VE.editForm.id = '';
                    VE.editForm.productType = '';
                    VE.editForm.fs = '';
                    VE.editForm.rulesName = '';
                    VE.editForm.rulesVal = '';
                    VE.editForm.productId = '';
                }
            })
        },

        queryTotal: function () {
            $.post("/charge/rulebind/queryTotal", {}, function (data) {
                VE.total = data.data.total;
            })
        },

        deleteRule: function (index) {

            this.$confirm('Are you sure to delete this rule？','delete confirm',{
                confirmButtonText: 'confirm',
                cancelButtonText: 'cancel',
                type: 'warning'
            }).then(function () {
                $.post("/charge/rulebind/deleteRule", {id: VE.tableData[index].id}, function (data) {
                    if (data.code == '200') {
                        VE.queryTotal();
                        VE.queryRuleList();
                        VE.editForm.productNum = '';
                        VE.editForm.productName = '';
                        VE.editForm.productType = '';
                        VE.editForm.fs = '';
                        VE.editForm.rulesName = '';
                        VE.editForm.rulesVal = '';
                        VE.editForm.id = '';
                        VE.$message.success('delete success');
                    } else {
                        VE.$message.error('delete error');
                    }

                })
            }).catch(function () {//空function可去除没有捕捉到取消事件的报错
                
            });

        },

        saveRule: function () {
            var params = {};
            params.id = VE.editForm.id;
            if (params.id == '') {
                this.$message.warning("please check row.");
                return;
            }
            params.rulesName = VE.editForm.rulesName;
            params.rulesVal = VE.editForm.rulesVal;
            params.fs = VE.editForm.fs;
            params.productId = VE.editForm.productId;


            var ptype = VE.editForm.productType;

            //航线判断
            var reFS = new RegExp("^[a-z,A-Z]{6}$");
            if (!reFS.test(params.fs)) {
                VE.$message.warning("please check input");
                return;
            }

            var reVal = new RegExp("^(([1-9][0-9]*(\\.[0-9]+)?)|([0-9](\\.[0-9]+)?))$");
            if (!reVal.test(params.rulesVal)) {
                VE.$message.warning("please check input");
                return;
            }

            var value = params.rulesName;
            //规则名称判断
            debugger
            if (ptype == 'SEAT') {
                var re = new RegExp("^(([1-9][0-9]*)|(([1-9][0-9]*,)+[1-9][0-9]*)|([1-9][0-9]*-([1-9][0-9]*)?))$");
                if (re.test(value)) {
                    if (value.indexOf(',') != -1) {
                        var temp = value.split(',');
                        var tempSet = new Set(temp);
                        if (tempSet.size != temp.length) {
                            VE.$message.warning("please check input");
                            return;
                        }
                    } else if (value.indexOf('-') != -1) {
                        var temp = value.split('-');
                        if (temp[1] == '') {

                        }else if ( parseInt(temp[0]) >= parseInt(temp[1])) {
                            VE.$message.warning("please check input");
                            return;
                        }
                    }
                } else {
                    VE.$message.warning("please check input");
                    return;
                }
            } else if (ptype == 'BAGGAGE') {
                var re = new RegExp("^((0-[1-9][0-9]*)|([1-9][0-9]*-([1-9][0-9]*)?))$");
                if (re.test(params.rulesName)) {
                    var temp = value.split('-');
                    if(temp[1] == '') {

                    }else if (parseInt(temp[0]) > parseInt(temp[1])) {
                        VE.$message.warning("please check input");
                        return;
                    }
                } else {
                    VE.$message.warning("please check input");
                    return;
                }
            }


            this.$confirm('Are you sure to save？','save confirm',{
                confirmButtonText: 'confirm',
                cancelButtonText: 'cancel',
                type: 'info'
            }).then(function () {
                $.post("/charge/rulebind/updateRule", params, function (data) {
                    if ('200' == data.code) {
                        VE.$message.success('save success');
                    } else {
                        VE.$message.error('save error');
                    }
                    VE.queryRuleList();
                });
            }).catch(function () {
                VE.editForm.id = '';
                VE.editForm.rulesName = '';
                VE.editForm.rulesVal = '';
                VE.editForm.fs = '';
                VE.editForm.productNum = '';
                VE.editForm.productName = '';
                VE.editForm.productType = '';
            });
        },

        formatVal: function (row, column) {
            var val = new Number(row.rulesVal);
            return val.toFixed(2);
        }
    }

});

