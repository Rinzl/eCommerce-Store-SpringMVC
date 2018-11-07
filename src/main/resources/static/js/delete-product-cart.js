$('.cart-img-product').each(function () {
    var selector = $(this).parent().parent();
    var id = selector.find("input").attr("id");
    var name = selector.find(".column-2").html();
    $(this).on('click', function () {
        console.log("delete called on id : " + id);
        swal({
            title: "Bạn có muốn xóa " + name + " khỏi giỏ hàng?",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        }).then((willDelete) => {
            if (willDelete) {
                $.ajax({
                    type : "POST",
                    contentType : "application/json",
                    url : "/checkout/cart/remove",
                    data : JSON.stringify({
                        id : id
                    }),
                    dataType : "json",
                    timeout : 1000,
                    success : function (data) {
                        if (data.status === 1) {
                            swal(name + " đã được xóa khỏi giỏ hàng!", {
                                icon: "success",
                            }).then(() => {
                                window.location.reload(true);
                            });
                        } else {
                            swal("ERROR", "Lỗi không xác định, vui lòng tải lại trang web", "error");

                        }
                    },
                    error : function (e) {
                        swal("ERROR", "Có Lỗi không xác định, vui lòng tải lại trang web", "error");
                        console.log("ERROR: ", e);
                    }
                });
            }
        });
    });
});