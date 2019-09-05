var app = new Vue({
    el:"#app",
    data: {
        username:"",
        //支付日志或者交易编号
        outTradeNo: "",
        //支付总金额
        totalFee:0
    },
    methods: {
        //生成支付二维码
        createNative: function () {
            //1、接收参数
            this.outTradeNo = getParameterByName("outTradeNo");
            //2、发送请求
            axios.get("pay/createNative.do?outTradeNo=" + this.outTradeNo + "&r=" + Math.random()).then(function (response) {
                if ("SUCCESS" == response.data.result_code) {
                    //下单成功
                    app.totalFee = (response.data.totalFee/100).toFixed(2);
                    //3、生成二维码
                    var qr = new QRious({
                        //要渲染生成二维码图片的元素
                        element: document.getElementById("qrious"),
                        //大小
                        size: 250,
                        //级别
                        level: "Q",
                        //值
                        value: response.data.code_url
                    });
                } else {
                    alert("生成二维码失败！");
                }
            });


        },
        //获取用户名
        getUsername: function () {
            axios.get("cart/getUsername.do").then(function (response) {
                app.username = response.data.username;
            });
        },
        //根据参数名字获取参数
        getParameterByName: function (name) {
            return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
        }
    },
    created(){
        this.getUsername();
        //生成支付二维码
        this.createNative();
    }
});