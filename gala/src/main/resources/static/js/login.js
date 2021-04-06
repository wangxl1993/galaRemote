function loginSub() {
    var un = document.getElementById("username").value;
    var pw = document.getElementById("password").value;
    $.ajax({
        type:"POST",
        dataType:"json",
        url:"/gala/login/login",
        data:{"username":un,"password":pw},
        success: function (result) {
            console.log(result);
            if (result.code == 200){
                window.location.href = 'http://localhost:1993/gala/login/build';
            }
        },
        error:function () {
            console.log("ajax调用后台登陆接口异常");
        }

    });
}
