function loginSub() {
    alert("333")
    $.ajax({
        type:"POST",
        dataType:"json",
        url:"/gala/login/login",
        data:$('#form2').serialize(),
        success: function (result) {
            console.log(result);
            if (result.code == 200){
                alert("登录页面通通通");
            }
        },
        error:function () {
            console.log("ajax调用后台登陆接口异常");
        }

    });
}
