var app = new Vue({
    el:"#app",
    data: {
        username:""
    },
    methods: {
        //获取用户名
        getUsername: function () {
            axios.get("cart/getUsername.do").then(function (response) {
                app.username = response.data.username;
            });
        }
    },
    created(){
        this.getUsername();
    }
});