function loginSub() {
    var un = document.getElementById("username").value;
    var pw = document.getElementById("password").value;
    $.ajax({
        type:"POST",
        dataType:"json",
        url:"/login/login",
        data:{"username":un,"password":pw},
        success: function (result) {
            console.log(result);
            if (result.code == 200){
                window.location.href = 'http://localhost:1993/login/index';
            }else {
                layer.msg(result.message);
            }
        },
        error:function () {
            console.log("ajax调用后台登陆接口异常");
        }

    });
}
function registerSub() {
    var un = document.getElementById("un").value;
    var pw = document.getElementById("pw").value;
    var email = document.getElementById("email").value;
    var phone = document.getElementById("phone").value;
    console.log(un+"-"+pw+"-"+email+"-"+phone);
    $.ajax({
        type:"POST",
        dataType:"json",
        url:"/login/register",
        data:{"username":un,"password":pw,"email":email,"phone":phone},
        success: function (result) {
            console.log(result);
            if (result.code == 200){
                layer.msg(result.message);
                // window.location.href = 'http://localhost:1993/login/log';
            }else {
                layer.msg(result.message);
            }
        },
        error:function () {
            console.log("ajax调用后台登陆接口异常");
        }

    });
}