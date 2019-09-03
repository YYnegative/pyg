var app = new Vue({
    el:"#app",
    data: {
        username:"",
        //购物车列表
        cartList:[]
    },
    methods: {
        //查询购物车数据
        findCartList:function(){
          axios.get("cart/findCartList.do").then(function (response) {
              app.cartList = response.data;
          });
        },
        //获取用户名
        getUsername: function () {
            axios.get("cart/getUsername.do").then(function (response) {
                app.username = response.data.username;
            });
        }
    },
    created(){
        this.getUsername();
        //查询购物车列表
        this.findCartList();
    }
});