try {
    document.getElementById("submitUpdateCart").onclick = function () {
        updateCart()
    };
}
catch (e) {

}

function updateCart() {
    var ids = $(".num-product").map(function(){
        return this.id;
    }).get();
    var postData = ids.map(function (id) {
        var num = $("#" + id).val();
        return {id : id, num : num};
    });
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "/checkout/cart/update",
        data : JSON.stringify(postData),
        dataType : 'json',
        timeout : 1000,
        success : function (data) {
            var code = data.status;
            if (code === 1) {
                swal("Giỏ hàng", "Cập nhật thành công!", "success");
            } else {
                swal("Giỏ hàng", "Cập nhật không thành công, vui lòng tải lại trang web!", "error");
            }
        },
        error : function (e) {
            console.log("ERROR: ", e);
        }
    });
}