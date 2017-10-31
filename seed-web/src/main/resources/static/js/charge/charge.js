var pane = new Vue({

    el: '#pane',
    data: {
        detailOnOff: true,//订单明细是否可以删除
        detailDEL: false,//是否允许清空订单明细
        ONOFF: null,
        CKIURL: "",//CKI打印地址
        confirmSum: false,//确认收费信息
        orderdetailDel: false,//订单明细删除
        orderConfirmShow: false,//订单确认弹出框
        tblOrder: {},//CKI接口数据
        chargeMap: {},//收费的项目集合
        baggageRule: [],//首件行李收费规则
        baggage: [],//行李收费规则
        LbaggageForm: {//行李收费
            bagw: 0,
            netBaggage: '',//网上行李额度
            freeLuggageNum: '',//免费行李额度
            needChargeBaggage: '',//需收费行李重量
            firstWeight: '',
            firstMoner: '',
            univalence: '',
            overweight: '',
            gather: '',
            productNum: '',
            productName: '',
            productType: '',
        },
        LotherForm: {
            otherName: '',
            otherUnivalence: '',
            otherNum: '',
            otherMoney: '',
        },

        activeName: 'Lbaggage',//收费切换默认标题栏
        LseatData: [],
        LspBaggageData: [],
        detail: [],//当前订单明细
        detailBack: [],//当前订单明细备用
        queryParams: {},
        number: '',
        moneyALL: 0,//订单明细合计价格
        tableData: [],//订单列表
        LbaggageRule: {//行李收费校验，算了不校验了
        },
        baggageDisabled: true,//行李收费输入框状态
        currentRow: null,//订单列表选中项
        detailRow: null,//订单明细列表选中项


        LotherRule: {//其他收费校验
            otherName: [
                {required: true, message: '请输入物品名称', trigger: 'blur'}
            ],
            otherUnivalence: [
                {required: true, message: '请输入物品单价', trigger: 'blur'}
            ],
            otherNum: [
                {required: true, message: '请输入物品数量', trigger: 'blur'}
            ],
            otherMoney: [
                {required: true, message: '请输入收费金额', trigger: 'blur'}
            ]

        }
    },

    methods: {
        //特殊行李收费信息
        BagTotal: function (index, row) {
            var _this = this;
            _this.LspBaggageData[index].money = Number(row.number) * (_this.LspBaggageData[index].productUnitPrice);
        },
        //其他收费合计
        totalOther: function () {
            debugger
            var _this = this;
            _this.LotherForm.otherMoney = Number(_this.LotherForm.otherNum) * Number(_this.LotherForm.otherUnivalence);
        },

        //关闭窗口
        windowClose: function () {
            window.opener = null;
            window.open('', '_self');
            window.close();
        },


        //收费信息切换控制
        handleClick: function (tab, event) {
            console.log(tab, event);
        },
        //确认按钮altY
        altY: function () {
            var _this = this;
            shortcut.remove("down");
            shortcut.remove("enter");

            $(".success").focus();
            _this.onSubmit();

        },
        //取消按钮altC
        altC: function () {
            var _this = this;
            shortcut.remove("down");
            shortcut.remove("enter");
            $(".warning").focus();

        },
        //快捷键切换行李收费altB
        altB: function () {
            var _this = this;
            if(_this.detailDEL){
                _this.detailOnOff = true;//允许删除单个订单明细
                _this.detail = [];//清空订单明细列表
                _this.detailDEL = false;//不允许清空

            }
            shortcut.remove("down");
            shortcut.remove("enter");
            _this.activeName = 'Lbaggage';
            setTimeout(function () {
                try {
                    var t = document.getElementById('baggage12');
                    t.focus();
                    t.select();
                } catch (e) {
                }
            }, 200);
        },
        //键盘ENTER焦点切换
        keyEnter: function (i) {
            shortcut.remove("down");
            shortcut.remove("enter");
            $(i).focus();
        },
        //键盘up焦点切换
        keyUp: function (i) {
            shortcut.remove("down");
            shortcut.remove("enter");
            $(i).focus();
        },
        //快捷键切换座位收费收费altS
        altS: function () {
            var _this = this;
            if(_this.detailDEL){
                _this.detailOnOff = true;//允许删除单个订单明细
                _this.detail = [];//清空订单明细列表
                _this.detailDEL = false;//不允许清空

            }
            shortcut.remove("down");
            shortcut.remove("enter");
            this.activeName = 'Lseat';
            $("#seatId input")[0].focus();

            setTimeout(function () {
                try {
                    $("#seatId input")[0].focus();
                    $("#seatId input")[0].select();
                } catch (e) {
                }
            }, 200);

        },
        //快捷键切换特殊行李收费altT
        altT: function () {
            var _this = this;
            if(_this.detailDEL){
                _this.detailOnOff = true;//允许删除单个订单明细
                _this.detail = [];//清空订单明细列表
                _this.detailDEL = false;//不允许清空

            }
            shortcut.remove("down");
            shortcut.remove("enter");
            this.activeName = 'LspBaggage';

            setTimeout(function () {
                try {
                    $("#BaggageId input")[0].focus();
                    $("#BaggageId input")[0].select();
                } catch (e) {
                }
            }, 200);

        },
        ctrlAltH: function () {
            var _this = this;
            shortcut.remove("down");
            shortcut.remove("enter");
            _this.tblOrder.seat = "";
            var order = JSON.stringify(_this.tblOrder);
            $.post({
                url: "/charge/order/ctrlAltH",
                data: {"order": order},
                success: function (result) {

                }
            });
            _this.baggageDisabled = false;


        },
        //快捷键切换其他收费altQ
        altQ: function () {
            var _this = this;
            if(_this.detailDEL){
                _this.detailOnOff = true;//允许删除单个订单明细
                _this.detail = [];//清空订单明细列表
                _this.detailDEL = false;//不允许清空

            }
            shortcut.remove("down");
            shortcut.remove("enter");
            this.activeName = 'Lother';
            setTimeout(function () {
                try {
                    $("#other1").focus();
                    $("#other1").select();
                } catch (e) {
                }
            }, 200);

        },
        //ENTER跳转光标

        //快捷键切换订单列表F1
        tableDataF1: function () {
            var _this = this;
            $("input").blur();
            _this.$refs.detailTable.setCurrentRow();
            detailRow = null;
            _this.ONOFF = "F1";
            _this.$refs.singleTable.setCurrentRow(_this.$refs.singleTable.data[0]);
            _this.currentRow = _this.$refs.singleTable.data[0];
            shortcut.remove("down");
            shortcut.remove("enter");
            shortcut.add("down", _this.f1Down, {});//改变输入框状态licenseSelect
            shortcut.add("enter", _this.licenseSelect, {});
            return;

        },
        //快捷键切换订单列表F1然后选择下一个
        f1Down: function () {
            var _this = this;
            for (var i = 0; i < _this.$refs.singleTable.data.length; i++) {
                if (_this.currentRow === _this.$refs.singleTable.data[i]) {
                    if (i < _this.$refs.singleTable.data.length - 1) {
                        _this.$refs.singleTable.setCurrentRow(_this.$refs.singleTable.data[i + 1]);
                        _this.currentRow = _this.$refs.singleTable.data[i + 1];
                        return;
                    } else {
                        _this.$refs.singleTable.setCurrentRow(_this.$refs.singleTable.data[0]);
                        _this.currentRow = _this.$refs.singleTable.data[0];
                        i = 0;
                        return;
                    }

                }
            }


        },


        //快捷键切换订单明细F2
        detailF2: function () {
            var _this = this;
            $("input").blur();
            _this.$refs.singleTable.setCurrentRow();
            currentRow = null;
            _this.ONOFF = "F2";
            _this.$refs.detailTable.setCurrentRow(_this.$refs.detailTable.data[0]);
            _this.detailRow = _this.$refs.detailTable.data[0];
            shortcut.remove("down");
            shortcut.remove("enter");
            shortcut.add("down", _this.f2Down, {});//改变输入框状态licenseSelect
            shortcut.add("enter", _this.detailSelect, {});
            return;


        },
        //F2down快捷键
        f2Down: function () {
            var _this = this;
            for (var i = 0; i < _this.$refs.detailTable.data.length; i++) {
                if (_this.detailRow === _this.$refs.detailTable.data[i]) {
                    if (i < _this.$refs.detailTable.data.length - 1) {
                        _this.$refs.detailTable.setCurrentRow(_this.$refs.detailTable.data[i + 1]);
                        _this.detailRow = _this.$refs.detailTable.data[i + 1];
                        return;
                    } else {
                        _this.$refs.detailTable.setCurrentRow(_this.$refs.detailTable.data[0]);
                        _this.detailRow = _this.$refs.detailTable.data[0];
                        i = 0;
                        return;
                    }

                }
            }


        },

        orderConfirmSum: function () {
            var _this = this;
            var baggageTotal = _this.LbaggageForm.bagw + "/" + _this.LbaggageForm.netBaggage + "/" + _this.LbaggageForm.freeLuggageNum + "/" + _this.LbaggageForm.needChargeBaggage + "/" + _this.LbaggageForm.firstWeight + "/" + _this.LbaggageForm.firstMoner + "/" + _this.LbaggageForm.univalence + "/" + _this.LbaggageForm.overweight + "/" + _this.LbaggageForm.gather;
            if (_this.detail.length > 0) {
                _this.tblOrder.seat = "";
                var order = JSON.stringify(_this.tblOrder);
                var detail = JSON.stringify(_this.detail);

                $.post({
                    url: "/charge/order/addData",
                    data: {"order": order, "detail": detail, "baggageTotal": baggageTotal},
                    success: function (result) {

                        _this.tableData = result.data.OrderList;//订单列表
                        _this.detailOnOff = false;//订单生成不允许删除订单
                        _this.detailDEL = true;//允许清空订单明细
                        var selection;
                        for (var i = 0; i < result.data.OrderList.length; i++) {
                            if (result.data.OrderList[i].id == result.data.orderId) {
                                selection = result.data.OrderList[i];
                            }
                        }
                        debugger
                        printReceipt(selection, _this.chargeMap, _this.CKIURL);
                        _this.$message({
                            message: "success",
                            type: 'success'
                        });
                    }
                });


            } else {
                _this.$message.error('Warning: No information !');
            }
            _this.confirmSum = false;
        },
        //确定按钮提交事件
        onSubmit: function () {
            var _this = this;
            _this.confirmSum = true;

            setTimeout(function () {
                try {
                    $(".confirmSum").focus();
                    $(".confirmSum").select();
                } catch (e) {
                }
            }, 200);


        },
        //取消按钮提交事件
        onCancel: function () {
            var _this = this;


        },
        //初始化方法
        initial: function () {
            var _this = this;
            $.post("/charge/order/orderMessage", "", function (response) {
                _this.CKIURL = response.data.CKIURL;
                _this.tblOrder = response.data.tblOrder;//CKI接口信息
                _this.chargeMap = response.data.chargeMap;//收费项目集合
                _this.tableData = response.data.OrderList;//订单列表
                var seat = new Array();//座位收费信息
                var Baggage = new Array();//特殊行李收费
                var rule = new Array();//首件行李收费规则
                var baggageR = new Array();//行李收费规则
                for (var i = 0; i < response.data.ProductList.length; i++) {
                    if (response.data.ProductList[i].productType == _this.chargeMap.seat) {    //座位收费
                        response.data.ProductList[i].sn = "/";
                        response.data.ProductList[i].money = "/";
                        seat.push(seatGa(response.data.ProductList[i], _this.tblOrder.sn));
                    }
                    if (response.data.ProductList[i].productType == _this.chargeMap.baggage_others) {  //特殊行李收费
                        response.data.ProductList[i].number = 0;
                        response.data.ProductList[i].money = 0;
                        response.data.ProductList[i].productUnitPrice = response.data.ProductList[i].tblProductRulesList[0].rulesVal;
                        Baggage.push(response.data.ProductList[i]);

                    }
                    if (response.data.ProductList[i].productType == _this.chargeMap.baggage) {//行李收费
                        baggageR.push(response.data.ProductList[i]);
                        for (var j = 0; j < response.data.ProductList[i].tblProductRulesList.length; j++) {
                            rule.push(response.data.ProductList[i].tblProductRulesList[j]);
                        }
                    }
                }
                _this.LseatData = seat;//座位收费
                _this.LspBaggageData = Baggage;//特殊行李收费
                _this.baggageRule = rule;//行李首件收费规则
                _this.baggage = baggageR;//行李收费规则
                _this.LbaggageForm.netBaggage = _this.tblOrder.netBaggage;
                if (_this.tblOrder.freeLuggageNum == 5) {//ij如果免费行了额度为5那么就默认免费额度为0
                    _this.LbaggageForm.freeLuggageNum = 0;
                } else {
                    _this.LbaggageForm.freeLuggageNum = _this.tblOrder.freeLuggageNum;
                }

            });

        },
        //行李收费计算
        LbaggageCount: function (i) {//TODO 行李收费重新计算
            var bigWeight = 0;
            var re = new RegExp("^\\d+(\\.\\d{0,1})?$");
            var reS = new RegExp("^\\d*$");
            var _this = this;
            if (_this.baggageDisabled) {
                _this.LbaggageForm.firstWeight = "";
                _this.LbaggageForm.firstMoner = "";

                _this.LbaggageForm.overweight = "";
                if (!re.test(_this.LbaggageForm.bagw) || _this.LbaggageForm.bagw == "") {
                    $("#LbaggageFormbagw").show();
                    return;
                } else {
                    $("#LbaggageFormbagw").hide();
                }

                _this.LbaggageForm.needChargeBaggage = (Number(_this.LbaggageForm.bagw) - Number(_this.LbaggageForm.netBaggage) - Number(_this.LbaggageForm.freeLuggageNum)).toFixed(1);
                if (Number(_this.LbaggageForm.needChargeBaggage) < 0) {
                    _this.LbaggageForm.needChargeBaggage = 0;
                    _this.LbaggageForm.firstWeight = 0;
                    _this.LbaggageForm.firstMoner = 0;
                    _this.LbaggageForm.overweight = 0;
                    _this.LbaggageForm.gather = 0;
                    return;
                }


                for (var a = 0; a < _this.baggageRule.length; a++) {
                    var rule = _this.baggageRule[a].rulesName.split("-");
                    if (Number(rule[0]) != "" && rule[1] == "") {
                        bigWeight = rule[0];
                    }
                }


                var overweightNum = (Number(_this.LbaggageForm.bagw) - Number(_this.LbaggageForm.freeLuggageNum) - Number(bigWeight)) > 0 ? Number(_this.LbaggageForm.bagw) - Number(_this.LbaggageForm.freeLuggageNum) - Number(bigWeight) : 0;//愈重行李重量

                var arr = overweightNum.toString().split(".");


                if (arr.length > 1) {//如果是小数自动先前进一位
                    _this.LbaggageForm.overweight = Number(arr[0]) + 1;
                } else {
                    _this.LbaggageForm.overweight = arr[0];
                }


                for (var a = 0; a < _this.baggageRule.length; a++) {
                    debugger
                    var rule = _this.baggageRule[a].rulesName.split("-");
                    if (Number(rule[0]) != "" && rule[1] == "") {
                        bigWeight = rule[0];
                    }
                    if (Number(rule[0]) < (Number(_this.LbaggageForm.needChargeBaggage) - Number(_this.LbaggageForm.overweight)) && (Number(_this.LbaggageForm.needChargeBaggage) - Number(_this.LbaggageForm.overweight)) <= Number(rule[1])) {
                        _this.LbaggageForm.firstWeight = rule[1];
                        _this.LbaggageForm.firstMoner = _this.baggageRule[a].rulesVal;
                    } else if (Number(rule[0]) != "" && rule[1] == "" && (Number(_this.LbaggageForm.needChargeBaggage) - Number(_this.LbaggageForm.overweight)) > rule[0]) { //行李额度大于最大额度的情况
                        _this.LbaggageForm.firstWeight = rule[0];
                        _this.LbaggageForm.firstMoner = _this.baggageRule[a].rulesVal;
                    } else if (Number(_this.LbaggageForm.bagw) == 0) {
                        _this.LbaggageForm.firstWeight = 0;
                        _this.LbaggageForm.firstMoner = 0;
                    }
                }
                for (var ii = 0; ii < _this.baggage.length; ii++) {//愈重行李收费规则计算
                    _this.LbaggageForm.univalence = _this.baggage[ii].productUnitPrice;//愈重行李单价
                    _this.LbaggageForm.productNum = _this.baggage[ii].productNum;//产品编号
                    _this.LbaggageForm.productName = _this.baggage[ii].productName;//产品名称
                    _this.LbaggageForm.productType = _this.baggage[ii].productType;//产品类型
                }


                _this.LbaggageForm.gather = Number(_this.LbaggageForm.firstMoner) + (Number(_this.LbaggageForm.univalence) * Number(_this.LbaggageForm.overweight));
                var baggageNum = 0;
                for (var i = 0; i < _this.detail.length; i++) {
                    if (_this.detail[i].productType == _this.chargeMap.baggage) {
                        baggageNum += 1;
                    }
                }
                if (baggageNum == 0) {
                    if (Number(_this.LbaggageForm.gather) > 0) {
                        var baggage = {};
                        baggage['productType'] = _this.LbaggageForm.productType;
                        baggage['productName'] = _this.LbaggageForm.productName;
                        baggage['productNum'] = _this.LbaggageForm.productNum;
                        baggage['unit'] = _this.LbaggageForm.bagw;
                        baggage['money'] = _this.LbaggageForm.gather;
                        _this.detail.push(baggage);
                        _this.moneyALL = Number(_this.moneyALL) + Number(_this.LbaggageForm.gather);
                    }

                } else {
                    _this.$message.error('Warning: baggage fee information not repeatable added !');

                }

            } else {
                $(i).focus();
                var op = 0;
                if (!re.test(_this.LbaggageForm.bagw) || _this.LbaggageForm.bagw == "") {
                    $("#LbaggageFormbagw").show();
                    op = 1;
                } else {
                    $("#LbaggageFormbagw").hide();
                }
                if (!reS.test(_this.LbaggageForm.netBaggage) || _this.LbaggageForm.netBaggage == "") {
                    $("#LbaggageFormnetBaggage").show();
                    op = 1;
                } else {
                    $("#LbaggageFormnetBaggage").hide();
                }
                if (!reS.test(_this.LbaggageForm.freeLuggageNum) || _this.LbaggageForm.freeLuggageNum == "") {
                    $("#LbaggageFormfreeLuggageNum").show();
                    op = 1;
                } else {
                    $("#LbaggageFormfreeLuggageNum").hide();
                }
                if (!re.test(_this.LbaggageForm.needChargeBaggage) || _this.LbaggageForm.needChargeBaggage == "") {
                    $("#LbaggageFormneedChargeBaggage").show();
                    op = 1;
                } else {
                    $("#LbaggageFormneedChargeBaggage").hide();
                }
                if (!reS.test(_this.LbaggageForm.firstWeight) || _this.LbaggageForm.firstWeight == "") {
                    $("#LbaggageFormfirstWeight").show();
                    op = 1;
                } else {
                    $("#LbaggageFormfirstWeight").hide();
                }
                if (!re.test(_this.LbaggageForm.firstMoner) || _this.LbaggageForm.firstMoner == "") {
                    $("#LbaggageFormfirstMoner").show();
                    op = 1;
                } else {
                    $("#LbaggageFormfirstMoner").hide();
                }
                if (!re.test(_this.LbaggageForm.univalence) || _this.LbaggageForm.univalence == "") {
                    $("#LbaggageFormunivalence").show();
                    op = 1;
                } else {
                    $("#LbaggageFormunivalence").hide();
                }
                if (!re.test(_this.LbaggageForm.overweight) || _this.LbaggageForm.overweight == "") {
                    $("#LbaggageFormoverweight").show();
                    op = 1;
                } else {
                    $("#LbaggageFormoverweight").hide();
                }
                if (op == 1) {
                    return;
                }
                if ("#baggage6" == i) {
                    _this.LbaggageForm.gather = Number(_this.LbaggageForm.firstMoner) + (Number(_this.LbaggageForm.univalence) * Number(_this.LbaggageForm.overweight));
                    var baggageNum = 0;
                    for (var i = 0; i < _this.detail.length; i++) {
                        if (_this.detail[i].productType == _this.chargeMap.baggage) {
                            baggageNum += 1;
                        }
                    }
                    if (baggageNum == 0) {
                        if (Number(_this.LbaggageForm.gather) > 0) {
                            var baggage = {};
                            baggage['productType'] = _this.chargeMap.baggage;
                            baggage['productName'] = "baggage";
                            baggage['productNum'] = _this.LbaggageForm.productNum;
                            baggage['unit'] = _this.LbaggageForm.bagw;
                            baggage['money'] = _this.LbaggageForm.gather;
                            _this.detail.push(baggage);
                            _this.moneyALL = Number(_this.moneyALL) + Number(_this.LbaggageForm.gather);
                        }

                    } else {
                        _this.$message.error('Warning: baggage fee information not repeatable added !');

                    }
                }

            }

        },
        //座位收费计算
        seatCount: function (index, row) {
            var _this = this;

            var seatNum = 0;
            for (var i = 0; i < _this.detail.length; i++) {
                if (_this.detail[i].productType == _this.chargeMap.seat) {
                    seatNum += 1;
                }
            }
            if (Number(_this.LseatData[index].money) > 0) {
                if (seatNum == 0) {
                    var baggage = {};
                    baggage['productType'] = _this.LseatData[index].productType;
                    baggage['productName'] = _this.LseatData[index].productName;
                    baggage['productNum'] = _this.LseatData[index].productNum;
                    baggage['unit'] = _this.LseatData[index].sn;
                    baggage['money'] = _this.LseatData[index].money;
                    _this.detail.push(baggage);
                    _this.moneyALL = Number(_this.moneyALL) + Number(_this.LseatData[index].money);
                } else {
                    _this.$message.error('Warning: seat fee information not repeatable added !');

                }

            }

            if (index < $("#seatId input").length - 1) {
                $("#seatId input")[index + 1].focus();
            }

        },
        //座位收费UP键切换
        seatUp: function (index) {
            var _this = this;

            if (index > 0) {
                $("#seatId input")[index - 1].focus();
            }

        },
        //其他收费计算
        otherCount: function () {
            var _this = this;
            var reS = new RegExp("^\\d*$");
            var op = 0;
            if (_this.LotherForm.otherName == "") {
                $("#LotherFormotherName").show();
                op = 1;
            } else {
                $("#LotherFormotherName").hide();
            }
            if (!reS.test(_this.LotherForm.otherUnivalence) || _this.LotherForm.otherUnivalence == "") {
                $("#LotherFormotherUnivalence").show();
                op = 1;
            } else {
                $("#LotherFormotherUnivalence").hide();
            }
            if (!reS.test(_this.LotherForm.otherNum) || _this.LotherForm.otherNum == "") {
                $("#LotherFormotherNum").show();
                op = 1;
            } else {
                $("#LotherFormotherNum").hide();
            }
            if (op == 1) {
                return;
            }

            _this.LotherForm.otherMoney = Number(_this.LotherForm.otherNum) * Number(_this.LotherForm.otherUnivalence);
            if (Number(_this.LotherForm.otherMoney) > 0) {
                var baggage = {};
                baggage['productType'] = _this.chargeMap.other;
                baggage['productName'] = _this.LotherForm.otherName;
                baggage['productNum'] = "999";
                baggage['unit'] = _this.LotherForm.otherNum;
                baggage['money'] = _this.LotherForm.otherMoney;
                _this.detail.push(baggage);
                _this.moneyALL = Number(_this.moneyALL) + Number(_this.LotherForm.otherMoney);

                _this.LotherForm = {};
                setTimeout(function () {
                    try {
                        $("#other1").focus();
                        $("#other1").select();
                    } catch (e) {
                    }
                }, 200);
            }

        },
        //特殊行李收费计算
        BaggageCount: function (index, row) {

            var _this = this;
            var re = new RegExp("^\\d*$");
            var _this = this;

            if (!re.test(row.number)) {
                _this.$message.error('please check input !');
                return;
            }
            _this.LspBaggageData[index].money = Number(row.number) * (_this.LspBaggageData[index].productUnitPrice);
            if (Number(_this.LspBaggageData[index].money) > 0) {
                var baggage = {};
                baggage['productType'] = _this.LspBaggageData[index].productType;
                baggage['productName'] = _this.LspBaggageData[index].productName;
                baggage['productNum'] = _this.LspBaggageData[index].productNum;
                baggage['unit'] = _this.LspBaggageData[index].number;
                baggage['money'] = _this.LspBaggageData[index].money;
                _this.detail.push(baggage);
                _this.moneyALL = Number(_this.moneyALL) + Number(_this.LspBaggageData[index].money);
                _this.LspBaggageData[index].money = 0;
                row.number = 0;
            }
            if (index < $("#BaggageId input").length - 1) {
                $("#BaggageId input")[index + 1].focus();
            }


        },
        //特殊行李收费中UP键操作
        bagGageUp: function (index) {
            var _this = this;

            if (index > 0) {
                $("#BaggageId input")[index - 1].focus();
            }
        },
        orderConfirm: function () {
            var _this = this;
            if (_this.currentRow != null) {
                var selection = _this.currentRow;
                if (selection.type == "N" || selection.type == "Y") {
                    //TODO 这里需要调用打印程序
                    _this.$message.success(selection.orderNum + ":Receipts in the print......!");
                    printReceipt(selection, _this.chargeMap, _this.CKIURL);
                } else {
                    _this.$message.warning("The type does not support to print receipts!");
                }
            }
            _this.orderConfirmShow = false;
        },
        //订单列表确认
        licenseSelect: function () {
            var _this = this;
            _this.orderConfirmShow = true;
            shortcut.remove("down");
            shortcut.remove("enter");
            setTimeout(function () {
                try {
                    $(".orderConfirm").focus();
                    $(".orderConfirm").select();
                } catch (e) {
                }
            }, 200);


        },
        helpMSG: function () {
            $("#helpMSG").toggle();
        },
        orderdetail: function () {
            var _this = this;
            if (_this.detailRow != null) {
                var selection = _this.detailRow;
                for (var j = 0; j < _this.detail.length; j++) {
                    if (_this.detail[j] == selection) {
                        _this.detail.splice(j, 1);
                    }
                }
                _this.moneyALL = 0;
                for (var i = 0; i < _this.detail.length; i++) {
                    _this.moneyALL = Number(_this.moneyALL) + Number(_this.detail[i].money);
                }
            }
            _this.orderdetailDel = false;
        },
        //删除订单明细
        detailSelect: function () {
            var _this = this;
            if (_this.detailOnOff) {
                _this.orderdetailDel = true;
                shortcut.remove("down");
                shortcut.remove("enter");
                setTimeout(function () {
                    try {
                        $(".orderdetail").focus();
                        $(".orderdetail").select();
                    } catch (e) {
                    }
                }, 200);
            }

        }

    },
    //页面加载后初始化方法
    created: function () {

        var _this = this;
        setTimeout(function () {
            try {
                var t = document.getElementById('baggage12');
                t.focus();
                t.select();
            } catch (e) {
            }
        }, 200);


        _this.initial();
        shortcut.add("f1", _this.tableDataF1, {});//订单列表
        shortcut.add("f2", _this.detailF2, {});//订单明细
        shortcut.add("alt+b", _this.altB, {});//行李
        shortcut.add("alt+s", _this.altS, {});//座位收费
        shortcut.add("alt+t", _this.altT, {});//特殊行李收费
        shortcut.add("alt+q", _this.altQ, {});//其他收费
        shortcut.add("esc", _this.windowClose, {});//关闭页面
        shortcut.add("alt+y", _this.altY, {});//确定按钮
        shortcut.add("alt+c", _this.altC, {});//取消按钮
        shortcut.add("ctrl+alt+h", _this.ctrlAltH, {});//改变输入框状态
        shortcut.add("alt+p", _this.helpMSG, {});//改变输入框状态
        $("#helpMSG").hide();


    }
});


function seatGa(seat, sn) {//座位收费计算

    for (var i = 0; i < seat.tblProductRulesList.length; i++) {
        if (seat.tblProductRulesList[i].rulesName.indexOf("-") >= 0) {
            var snS = seat.tblProductRulesList[i].rulesName.split("-");
            if (parseInt(snS[0]) <= parseInt(sn) && parseInt(sn) <= parseInt(snS[1])) {// xx-xx情况
                seat.sn = sn;
                seat.money = seat.tblProductRulesList[i].rulesVal;
                return seat;
            }
        }
        if (seat.tblProductRulesList[i].rulesName.indexOf(",") >= 0) {//xx,xx情况
            var snS = seat.tblProductRulesList[i].rulesName.split(",");
            for (var j = 0; j < snS.length; j++) {
                if (parseInt(snS[j]) == parseInt(sn)) {
                    seat.sn = sn;
                    seat.money = seat.tblProductRulesList[i].rulesVal;
                    return seat;
                }
            }

        }
        if (parseInt(seat.tblProductRulesList[i].rulesName) == parseInt(sn)) {
            seat.sn = sn;
            seat.money = seat.tblProductRulesList[i].rulesVal;
            return seat;
        }
    }
    return seat;

}

//打印订单信息
function printReceipt(selection, chargeMap, CKIURL) {
    debugger
    var data = "";
    var bagaWight = "";
    var seat = "";
    var nowDate = new Date();
    var year = nowDate.getFullYear();
    var month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1)
        : nowDate.getMonth() + 1;
    var day = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate
        .getDate();
    var dateStr = year + "-" + month + "-" + day;

    for (var i = 0; i < selection.tblOrderDetailList.length; i++) {
        if (selection.tblOrderDetailList[i].productType == chargeMap.baggage) {
            bagaWight = selection.tblOrderDetailList[i].unit;
        }
        if (selection.tblOrderDetailList[i].productType == chargeMap.seat) {
            seat = selection.tblOrderDetailList[i].unit;
        }
    }
    data += "paymentDate:" + dateStr;
    data += ";receiptNo:" + selection.orderNum;
    data += ";referenceNo:" + selection.rl;
    data += ";flightNo:" + selection.fi;
    data += ";segment:" + selection.fs;
    data += ";bagaWight:" + bagaWight;
    data += ";bagaMoney:" + selection.overweightLuggage;
    data += ";otherbagaMoney:" + selection.specialLuggage;
    data += ";seat:" + seat;
    data += ";seatMoney:" + selection.seat;
    data += ";otherMoney:" + selection.other;
    data += ";totalMoney:" + selection.receivableAmount;
    if (selection.paymentType == null) {
        selection.paymentType = "";
    }
    if (selection.paymentType == 1) {
        selection.paymentType = "Cash";
    }
    if (selection.paymentType == 2) {
        selection.paymentType = "POS";
    }
    if (selection.paymentType == 3) {
        selection.paymentType = "POS(G)";
    }
    data += ";paymentType:" + ";";
    $.post(CKIURL, data, function (dat, status) {


    });

}






