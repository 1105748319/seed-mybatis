var pane = new Vue({
    el: '#orderS',
    data: function () {

        return {
            chargeNum: '',//订单号
            updatedBy: '',//操作人
            ConfirmShow: false,//收费确认弹出框
            CancelShow: false,//收费取消弹出框
            RefundShow: false,//收费退款
            chargeNumShow: true,
            ONOFF: true,
            radio: '',
            chargeMap: '',
            tableData: [],
            totalNum: 0,//数量合计
            formS: {
                type: '',

            }
        }

    },

    methods: {

        getSummaries: function (param) {//收费统计
            var columns = param.columns;
            var data = param.data;
            var sums = [];
            var num = 0;
            var money = 0;
            for (var i = 0; i < data.length; i++) {
                if (data[i].productType == this.chargeMap.baggage) {
                    num += 1;
                    money += Number(data[i].money);
                } else if (data[i].productType == this.chargeMap.seat) {
                    num += 1;
                    money += Number(data[i].money);
                } else if (data[i].productType == this.chargeMap.baggage_others) {
                    num += Number(data[i].unit);
                    money += Number(data[i].money);
                } else if (data[i].productType == this.chargeMap.other) {
                    num += Number(data[i].unit);
                    money += Number(data[i].money);
                }
            }
            this.totalNum = num;
            columns.forEach(function (column, index) {
                if (index === 3) {
                    sums[index] = 'Total';
                    return;
                }
                if (index === 4) {

                    sums[index] = num;
                    return;
                }
                if (index === 5) {
                    sums[index] = money;
                    return;
                }
            })

            return sums;

        },


        selectOrder: function () { //录入订单号，查询对应对收费信息
            var _this = this;
            $.post({
                url: "/charge/order/orderSelect",
                data: {"orderNum": _this.chargeNum, "updatedBy": _this.updatedBy},
                success: function (result) {
                    if (result.data.OrderList.length > 0) {
                        _this.formS = result.data.OrderList[0];
                        _this.tableData = result.data.OrderList[0].tblOrderDetailList;
                        _this.radio = result.data.OrderList[0].paymentType;
                        _this.chargeMap = result.data.chargeMap;
                        _this.$message.success('success');
                        _this.chargeNumShow = false;
                        _this.ONOFF = true;
                    } else {
                        _this.$message.error('There is no query to the data！');
                    }
                }
            });


        },
        successCharge: function () { //收费确认，修改收费状态
            var _this = this;
            var re = new RegExp("^\\d+(\\.\\d{0,2})?$");
            if (re.test(_this.formS.actualAmount) && Number(_this.formS.actualAmount) > 0 && _this.radio != null && _this.radio != "") {
                _this.ConfirmShow = true;

                setTimeout(function () {
                    try {
                        $(".ConfirmShow").focus();
                        $(".ConfirmShow").select();
                    } catch (e) {
                    }
                }, 200);
                _this.ONOFF = false;
            } else {
                _this.$message.error('Please enter the charging amount and method of payment！');
            }

        },
        successChargeBAK: function () {
            var _this = this;
            _this.ONOFF = false;
            if (_this.formS.type == "N") {
                $.post("/charge/order/successCharge", {
                    "type": "Y",
                    "id": _this.formS.id,
                    "actualAmount": _this.formS.actualAmount,
                    "radio": _this.radio,
                    "updatedBy": _this.updatedBy,
                    "name": _this.formS.name
                }, function (data) {
                    _this.formS.type = "Y";
                    _this.$message.success('success');
                })
            } else {
                _this.$message.error('wrong operation！');
            }


            _this.ConfirmShow = false;
            setTimeout(function () {
                try {
                    _this.ONOFF = true;
                } catch (e) {
                }
            }, 200);

        },

        cancelCharge: function () {//收费取消
            var _this = this;

            if (_this.formS.type == "N") {
                _this.CancelShow = true;
                _this.ONOFF = false;
                setTimeout(function () {
                    try {
                        $(".CancelShow").focus();
                        $(".CancelShow").select();
                    } catch (e) {
                    }
                }, 200);
                _this.ONOFF = false;

            } else {
                _this.$message.error('wrong operation！');
            }
        },
        cancelChargeBAK: function () {
            var _this = this;
            _this.ONOFF = false;
            $.post("/charge/order/cancelCharge", {
                "type": "C",
                "id": _this.formS.id,
                "name": _this.formS.name,
                "updatedBy": _this.updatedBy
            }, function (data) {
                _this.$message.success('success');
                _this.formS.type = "C";
            })
            _this.CancelShow = false;
            setTimeout(function () {
                try {
                    _this.ONOFF = true;
                } catch (e) {
                }
            }, 200);
        },

        refundCharge: function () {//退款
            var _this = this;
            if (_this.formS.type == "Y") {
                _this.RefundShow = true;
                _this.ONOFF = false;
                setTimeout(function () {
                    try {
                        $(".RefundShow").focus();
                        $(".RefundShow").select();
                    } catch (e) {
                    }
                }, 200);
                _this.ONOFF = false;
            } else {
                _this.$message.error('wrong operation！');
            }

        },
        refundChargeBak: function () {
            var _this = this;
            _this.ONOFF = false;
            $.post("/charge/order/refundCharge", {
                "type": "Refund",
                "id": _this.formS.id,
                "name": _this.formS.name,
                "updatedBy": _this.updatedBy
            }, function (data) {
                _this.$message.success('success');
                _this.formS.type = "Refund";
            })
            _this.RefundShow = false;
            setTimeout(function () {
                try {
                    _this.ONOFF = true;
                } catch (e) {
                }
            }, 200);

        },
        radioData: function () {
            var _this = this;

            if (_this.radio == null || _this.radio == "") {
                _this.radio = "1";
                return
            }
            if (_this.radio == "1") {
                _this.radio = "2";
                return
            }
            if (_this.radio == "2") {
                _this.radio = "3";
                return
            }
            if (_this.radio == "3") {
                _this.radio = "1";
                return
            }


        },
        //弹出框键盘焦点切换
        keyICR: function (i) {
            setTimeout(function () {
                try {
                    $(i).focus();
                    $(i).select();
                } catch (e) {
                }
            }, 200);

        },



returnInitial: function () {//返回收费确认页面
    this.ONOFF = false;
    this.chargeNumShow = true;
    this.chargeNum = "";

    setTimeout(function () {
        try {
            $("#chargeNumId").focus();
            $("#chargeNumId").select();
        } catch (e) {
        }
    }, 200);
}
,

},


created: function () {
    var _this = this;
    _this.ONOFF = false;
    $("#sp").hide();
    _this.updatedBy = $("#sp").html();
    shortcut.remove("alt+M");
    shortcut.remove("alt+e");
    shortcut.remove("alt+p");
    shortcut.remove("alt+q");
    shortcut.remove("alt+r");
    shortcut.remove("alt+c");
    shortcut.remove("alt+y");
    shortcut.remove("alt+d");

    shortcut.add("alt+r", _this.returnInitial, {});//返回methods
    shortcut.add("alt+c", _this.cancelCharge, {});//取消
    shortcut.add("alt+y", _this.successCharge, {});//确认
    shortcut.add("alt+d", _this.refundCharge, {});//退款



}
,

});

Vue.directive('focus', function (el, option) {
    debugger
    var defClass = 'el-input', defTag = 'input';
    var value = option.value;
    if (typeof value === 'boolean')
        value = {cls: defClass, tag: defTag, foc: value};
    if (el.classList.contains(value.cls) && value.foc)
        el.getElementsByTagName(value.tag)[0].focus();

});
$(function () {
    shortcut.add("esc", windowClose, {});
    setTimeout(function () {
        try {
            $("#chargeNumId").focus();
            $("#chargeNumId").select();
        } catch (e) {
        }
    }, 200);
});

function windowClose() {
    window.opener = null;
    window.open('', '_self');
    window.close();
}

