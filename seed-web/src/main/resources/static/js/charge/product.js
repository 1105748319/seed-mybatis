/**
 * Created by lenovo on 2017/6/26.
 */
/*$.ajaxSetup({
    async: false
});*/
var PM = new Vue({

    el: '#productManager',

    data: function () {

        var checkProductName = function (rule, value, callback) {
            var ptype = PM.addProductRuleForm.productType;

            if (ptype == 'SEAT') {
                debugger
                var re = new RegExp("^(([1-9][0-9]*)|(([1-9][0-9]*,)+[1-9][0-9]*)|([1-9][0-9]*-([1-9][0-9]*)?))$");
                if (re.test(value)) {
                    if (value.indexOf(',') != -1) {
                        var temp = value.split(',');
                        var tempSet = new Set(temp);
                        if (tempSet.size == temp.length) {
                            return callback();
                        } else {
                            return callback(new Error("Format is not valid"));
                        }
                    } else if (value.indexOf('-') != -1) {
                        var temp = value.split('-');
                        if (temp[1] == '' || parseInt(temp[0]) < parseInt(temp[1])) {
                            return callback();
                        } else {
                            return callback(new Error("Format is not valid"));
                        }
                    } else {
                        return callback();
                    }
                } else {
                    return callback(new Error("Format is not valid"));
                }
            } else {
                var re = new RegExp("^((0-[1-9][0-9]*)|([1-9][0-9]*-([1-9][0-9]*)?))$");
                if (re.test(value)) {
                    var temp = value.split('-');
                    if (parseInt(temp[0]) > parseInt(temp[1])) {
                        return callback(new Error("Format is not valid"));
                    } else {
                        return callback();
                    }
                } else {
                    return callback(new Error("Format is not valid"));
                }
            }
        };
        var checkNum = function (rule, value, callback) {
            var re = new RegExp("^\\d+$");
            if (!re.test(value)) {
                return callback(new Error('rules number is only number'));
            } else {
                return callback();
            }
        };
        var checkNot0StartNum = function (rule, value, callback) {
            var re = new RegExp("^(([1-9][0-9]*(\\.[0-9]+)?)|([0-9](\\.[0-9]+)?))$");
            if (!re.test(value)) {
                return callback(new Error('Unit price is not valid'));
            } else {
                return callback();
            }
        };
        var checkFs = function (rule, value, callback) {
            var re = new RegExp("^[a-z,A-Z]{6}$");
            if (!re.test(value)) {
                return callback(new Error('flight line is not valid'));
            } else {
                return callback();
            }
        };

        return {
            form: {
                productNum: '',
                productName: '',
                unitType: '',
                id: '',
                productType: ''
            },
            options: [],
            tableData: [],
            currentPage: 1,
            total: 0,

            productForm: {
                id: '',
                productNum: '',
                productName: '',
                unitType: '',
                productType: 'BAGGAGE',
                productUnitPrice: ''
            },
            dialogFormVisible: false,
            formLabelWidth: '150px',
            updateProductForm: {
                id: '',
                productNum: '',
                productName: '',
                unitType: '',
                productType: '',
                productUnitPrice: ''
            },
            rulesUpdateProduct: {
                productNum: [
                    {required: true, validator: checkNum, trigger: 'blur'}
                ],
                productName: [
                    {required: true, message: "name is not empty", trigger: 'blur'}
                ],
                // productType: [
                //     {required: true, message: "单价不能为空"}
                // ],
                productUnitPrice: [
                    {required: true, validator: checkNot0StartNum, trigger: 'blur'}
                ],
                unitType: [
                    {required: true, message: "unit is not empty", trigger: 'blur'}
                ]
            },
            dialogUpdateProduct: false,

            addProductRuleForm: {
                productNum: '',
                productName: '',
                productType: '',
                fs: '',
                rulesName: '',
                rulesVal: ''
            },

            rulesR: {
                fs: [
                    {required: true, validator: checkFs, trigger: 'blur'}
                ],
                rulesName: [
                    {required: true, validator: checkProductName, trigger: 'blur'}
                ],
                rulesVal: [
                    {required: true, validator: checkNot0StartNum, trigger: 'blur'}
                ]
            },
            dialogAddProductRule: false,
            productId: '',
            productTypes: [],
            rulesProduct: {
                productNum: [
                    {required: true, validator: checkNum, trigger: 'blur'}
                ],
                productName: [
                    {required: true, message: 'product name is not empty', trigger: 'blur'}
                ],
                // productType: [
                //     {required: true, message: '产品类型不能为空'}
                // ],
                productUnitPrice: [
                    {required: true, validator: checkNot0StartNum, trigger: 'blur'}

                ],
                unitType: [
                    {required: true, message: 'price unit is not empty', trigger: 'blur'}
                ]
            }
        }

    },


    created: function () {
        // PM.currentPage = 1;
        var _this = this;
        var params = {};

        params.pageSize = 8;
        params.pageNum = _this.currentPage;
        $.post("/charge/product/queryList", params, function (data) {
            _this.tableData = data.data.pageInfo;
        });

        this.readTotal();
        this.queryProductTypes();
    },

    methods: {
        queryProductTypes: function () {
            var _this = this;
            $.post("/charge/product/queryProductType", {}, function (data) {
                _this.options = data.data;
                _this.othersType = data.data[3];
                _this.productTypes = data.data.slice(1, 5);
            })

        },
        queryListByParams: function () {
            var _this = this;
            var params = {};
            params.productName = _this.form.productName;
            params.productNum = _this.form.productNum;
            params.productType = _this.form.productType;
            params.pageSize = 8;
            params.pageNum = _this.currentPage;
            $.post("/charge/product/queryList", params, function (data) {
                _this.tableData = data.data.pageInfo;
            });
            this.readTotal();
        },

        handleCurrentChange: function (pageNum) {
            this.currentPage = pageNum;
            this.queryListByParams();
        },
        readTotal: function () {
            var params = {};
            params.productName = this.form.productName;
            params.productNum = this.form.productNum;
            params.productType = this.form.productType;
            $.post("/charge/product/queryTotal", params, function (data) {
                PM.total = data.data.total;
            })
        },
        addOneProduct: function () {
            var _this = this;
            var params = {};
            params.productNum = this.productForm.productNum;
            params.productName = this.productForm.productName;
            params.productType = this.productForm.productType;
            params.productUnitPrice = this.productForm.productUnitPrice;
            params.unitType = this.productForm.unitType;

            var re = new RegExp("^\\d+$");
            if (!re.test(params.productNum)) {
                this.$message.warning("please check input");
                return;
            }
            if (params.productName == '') {
                this.$message.warning("please check input");
                return;
            }
            if (params.productName == '') {
                this.$message.warning("please check input");
                return;
            }
            var rem = new RegExp("^(([1-9][0-9]*(\\.[0-9]+)?)|([0-9](\\.[0-9]+)?))$");
            if (!rem.test(params.productUnitPrice)) {
                this.$message.warning("please check input");
                return;
            }
            if (params.unitType == '') {
                this.$message.warning("please check input");
                return;
            }

            $.post("/charge/product/addOneProduct", params, function (data) {
                if (data.code == '200') {
                    _this.dialogFormVisible = false;
                    _this.$message.success("add product success");
                    _this.queryListByParams();
                    _this.$refs['productForm'].resetFields();
                    _this.dialogFormVisible = false;
                } else {
                    if (data.msg == '1') {
                        _this.$message.error("add product error,here already have this product No.");
                    } else {
                        _this.$message.error("add product error");
                    }
                }
            });
        },
        updateProduct: function (index) {
            this.updateProductForm.id = this.tableData[index].id;
            this.updateProductForm.productNum = this.tableData[index].productNum;
            this.updateProductForm.productName = this.tableData[index].productName;
            this.updateProductForm.productType = this.tableData[index].productType;
            this.updateProductForm.productUnitPrice = '' + this.tableData[index].productUnitPrice;
            this.updateProductForm.unitType = this.tableData[index].unitType;
            this.dialogUpdateProduct = true;
        },
        deleteProduct: function (index) {
            var _this = this;
            var productNum = this.tableData[index].productNum;
            var id = this.tableData[index].id;
            this.$confirm('Are you sure to delete the product？if you sure that all rules will delete on the product', 'delete confirm', {
                confirmButtonText: 'confirm',
                cancelButtonText: 'cancel',
                type: 'warning'
            }).then(function () {
                $.post("/charge/product/deleteProduct", {id: id}, function (data) {
                    if (data.code == '200') {
                        _this.$message.success('delete product success');
                    } else {
                        _this.$message.success('delete product error');
                    }
                    _this.queryListByParams();
                })
            }).catch(function () {//空function可去除没有捕捉到取消事件的报错

            });

        },
        sureUpdateProduct: function () {
            var _this = this;
            var params = {};

            params.id = this.updateProductForm.id;
            params.productNum = this.updateProductForm.productNum;
            params.productName = this.updateProductForm.productName;
            params.productType = this.updateProductForm.productType;
            params.productUnitPrice = this.updateProductForm.productUnitPrice;
            params.unitType = this.updateProductForm.unitType;

            var re = new RegExp("^\\d+$");
            if (!re.test(params.productNum)) {
                this.$message.warning("please check input");
                return;
            }
            if (params.productName == '') {
                this.$message.warning("please check input");
                return;
            }
            if (params.productName == '') {
                this.$message.warning("please check input");
                return;
            }
            var rem = new RegExp("^(([1-9][0-9]*(\\.[0-9]+)?)|([0-9](\\.[0-9]+)?))$");
            if (!rem.test(params.productUnitPrice)) {
                this.$message.warning("please check input");
                return;
            }
            if (params.unitType == '') {
                this.$message.warning("please check input");
                return;
            }


            $.post("/charge/product/updateProduct", params, function (data) {
                if (data.code == '200') {
                    _this.dialogUpdateProduct = false;
                    _this.$message.success('update product success');
                    _this.queryListByParams();
                } else {
                    if (data.msg == "1") {
                        _this.$message.error('update product error,here already have this product No.');
                    } else {
                        _this.$message.error('update product error');
                    }
                }

            });

        },
        addProductRule: function (index) {
            this.addProductRuleForm.fs = '';
            this.addProductRuleForm.rulesName = '';
            this.addProductRuleForm.rulesVal = '';

            this.addProductRuleForm.productNum = this.tableData[index].productNum;
            this.addProductRuleForm.productName = this.tableData[index].productName;
            this.addProductRuleForm.productType = this.tableData[index].productType;
            this.productId = this.tableData[index].id;

            this.dialogAddProductRule = true;
        },
        sureAddProductRule: function () {
            var T = true;
            var params = {};
            params.productId = this.productId;
            params.fs = this.addProductRuleForm.fs;
            params.rulesName = this.addProductRuleForm.rulesName;
            params.rulesVal = this.addProductRuleForm.rulesVal;
            params.productNum = this.addProductRuleForm.productNum;


            var ptype = PM.addProductRuleForm.productType;

            debugger
            //航线判断
            var reFS = new RegExp("^[a-z,A-Z]{6}$");
            if (!reFS.test(params.fs)) {
                PM.$message.warning("please check input");
                return;
            }

            var reVal = new RegExp("^(([1-9][0-9]*(\\.[0-9]+)?)|([0-9](\\.[0-9]+)?))$");
            if (!reVal.test(params.rulesVal)) {
                PM.$message.warning("please check input");
                return;
            }

            var value = params.rulesName;
            //规则名称判断
            if (ptype == 'SEAT') {
                var tempFF;
                var msg = "";
                var re = new RegExp("^(([1-9][0-9]*)|(([1-9][0-9]*,)+[1-9][0-9]*)|([1-9][0-9]*-([1-9][0-9]*)?))$");
                if (re.test(value)) {
                    if (value.indexOf(',') != -1) {

                        var temp = value.split(',');
                        tempFF = value.split(',')
                        var tempSet = new Set(temp);
                        if (tempSet.size != temp.length) {
                            PM.$message.warning("please check input");
                            return;
                        }
                        debugger
                        for (var i = 0; i < tempFF.length; i++) {
                            if (Number(tempFF[i + 1]) - Number(tempFF[i]) > 0) {
                                msg = msg + (Number(tempFF[i]) + 1) + "-" + (Number(tempFF[i + 1]) - 1) + ";"
                            }

                        }

                    } else if (value.indexOf('-') != -1) {
                        var temp = value.split('-');
                        if (temp[1] == '') {

                        } else if (parseInt(temp[0]) >= parseInt(temp[1])) {
                            PM.$message.warning("please check input");
                            return;
                        }
                    }
                } else {
                    PM.$message.warning("please check input");
                    return;
                }
                $.post("/charge/rulebind/seleteRule", params, function (data) {
                    if (data.code == '200') {
                        var rulesNameNew;//添加的坐位规则名称
                        var rulesNameOld;//添加的坐位规则名称
                        var seatwin = false;
                        debugger

                        if (params.rulesName.indexOf("-") >= 0) {
                            rulesNameNew = params.rulesName.split('-')[0];
                        } else if (params.rulesName.indexOf(",") >= 0) {
                            rulesNameNew = params.rulesName.split(',')[0];
                        } else {
                            rulesNameNew = params.rulesName;
                        }

                        if (data.data.productRule) {
                            if (data.data.productRule.rulesName.indexOf("-") >= 0) {
                                rulesNameOld = data.data.productRule.rulesName.split('-')[1];
                            } else if (data.data.productRule.rulesName.indexOf(",") >= 0) {
                                rulesNameOld = data.data.productRule.rulesName.split(',')[data.data.productRule.rulesName.split(',').length - 1];
                            } else {
                                rulesNameOld = data.data.productRule.rulesName;
                            }


                            if (Number(rulesNameOld) + 1 != rulesNameNew || msg != "") {
                                PM.$confirm((msg + ";" + Number(rulesNameOld) + 1) + ' row seat rules information not added, whether to continue?', 'add rules', {
                                    confirmButtonText: 'confirm',
                                    cancelButtonText: 'cancel',
                                    type: 'warning'
                                }).then(function () {
                                    debugger

                                    $.post("/charge/rulebind/addRule", params, function (data) {
                                        if (data.code == '200') {
                                            PM.dialogAddProductRule = false;
                                            PM.$message.success('bind rule success');
                                        } else {
                                            PM.$message.error('bind rule error');
                                        }
                                    })
                                    return;
                                }).catch(function () {//空function可去除没有捕捉到取消事件的报错
                                    return;
                                });

                            } else {
                                $.post("/charge/rulebind/addRule", params, function (data) {
                                    if (data.code == '200') {
                                        PM.dialogAddProductRule = false;
                                        PM.$message.success('bind rule success');
                                    } else {
                                        PM.$message.error('bind rule error');
                                    }
                                })
                            }

                        } else {

                            if (msg != "") {
                                PM.$confirm((msg) + ' row seat rules information not added, whether to continue?', 'add rules', {
                                    confirmButtonText: 'confirm',
                                    cancelButtonText: 'cancel',
                                    type: 'warning'
                                }).then(function () {
                                    debugger

                                    $.post("/charge/rulebind/addRule", params, function (data) {
                                        if (data.code == '200') {
                                            PM.dialogAddProductRule = false;
                                            PM.$message.success('bind rule success');
                                        } else {
                                            PM.$message.error('bind rule error');
                                        }
                                    })
                                    return;
                                }).catch(function () {//空function可去除没有捕捉到取消事件的报错
                                    return;
                                });

                            } else {
                                $.post("/charge/rulebind/addRule", params, function (data) {
                                    if (data.code == '200') {
                                        PM.dialogAddProductRule = false;
                                        PM.$message.success('bind rule success');
                                    } else {
                                        PM.$message.error('bind rule error');
                                    }
                                })

                            }


                        }
                    } else {
                        PM.$message.error('bind rule error');
                        return;
                    }
                })


            } else if (ptype == 'BAGGAGE') {
                var re = new RegExp("^((0-[1-9][0-9]*)|([1-9][0-9]*-([1-9][0-9]*)?))$");
                if (re.test(params.rulesName)) {
                    var temp = value.split('-');
                    if (parseInt(temp[0]) > parseInt(temp[1])) {
                        PM.$message.warning("please check input");
                        return;
                    }
                } else {
                    PM.$message.warning("please check input");
                    return;
                }
                $.post("/charge/rulebind/seleteRule", params, function (data) {
                    if (data.code == '200') {
                        debugger
                        if (data.data.productRule) {
                            var temp = data.data.productRule.rulesName.split('-');
                            if (temp[1] != params.rulesName.split('-')[0]) {
                                PM.$message.error(temp[1] + "-?" + 'baggage rules information not added');

                            } else {
                                $.post("/charge/rulebind/addRule", params, function (data) {
                                    if (data.code == '200') {
                                        PM.dialogAddProductRule = false;
                                        PM.$message.success('bind rule success');
                                    } else {
                                        PM.$message.error('bind rule error');
                                    }
                                })
                            }

                        } else {
                            $.post("/charge/rulebind/addRule", params, function (data) {
                                if (data.code == '200') {
                                    PM.dialogAddProductRule = false;
                                    PM.$message.success('bind rule success');
                                } else {
                                    PM.$message.error('bind rule error');
                                }
                            })
                        }
                    } else {
                        PM.$message.error('bind rule error');
                        return;
                    }
                })

            } else {
                $.post("/charge/rulebind/addRule", params, function (data) {
                    if (data.code == '200') {
                        PM.dialogAddProductRule = false;
                        PM.$message.success('bind rule success');
                    } else {
                        PM.$message.error('bind rule error');
                    }
                })
            }

        },

        addRule: function (params) {

        },


        closeProductForm: function () {
            this.$refs['productForm'].resetFields();
            this.dialogFormVisible = false;
        },
        beforeCloseProductForm: function (done) {
            this.closeProductForm();
        },
        closeUpdateForm: function () {
            this.$refs['updateForm'].resetFields();
            this.dialogUpdateProduct = false;
        },
        beforeCloseUpdateForm: function (done) {
            this.closeUpdateForm();
        },
        closeRuleForm: function () {
            this.$refs['ruleForm'].resetFields();
            this.dialogAddProductRule = false;
        },
        beforeCloseRuleForm: function (done) {
            this.closeRuleForm();
        },

        formatPrice: function (row, column) {
            var price = new Number(row.productUnitPrice);
            return price.toFixed(2);
        }

    }


});