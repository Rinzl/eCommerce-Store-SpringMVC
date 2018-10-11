$('.block2-btn-addcart').each(function(){
        var nameProduct = $(this).parent().parent().parent().find('.block2-name').html();
        var idProduct = $(this).attr('id');
        $(this).on('click', function(){
            console.log("Product with id : " + idProduct + " selected");
            $.ajax({
                type : "POST",
                contentType : "application/json",
                url : "/checkout/cart/add",
                data : JSON.stringify({
                    id : idProduct
                }),
                dataType : 'json',
                timeout : 100000,
                success : function(data) {
                    var i = data.status;
                    if (i===1) {
                        swal(nameProduct, "đã được thêm vào giỏ hàng !", "success");
                        var current = $("#cartSize").html();
                        current++;
                        $("#cartSize").html(current);
                        $("#cartSizeMobile").html(current);
                    }
                    else if (i===0) swal(nameProduct, "chưa được thêm vào giỏ hàng !", "error");
                    else swal("ERROR", "Có lỗi bất thường xẩy ra, vui lòng reload lại trang web", "error");
                },
                error : function(e) {
                    swal("ERROR", "Có lỗi bất thường xẩy ra, vui lòng reload lại trang web", "error");
                    console.log("ERROR: ", e);
                }
            });
        });
    });

$('.block2-btn-addwishlist').each(function(){
    var nameProduct = $(this).parent().parent().parent().find('.block2-name').html();
    $(this).on('click', function(){
        swal(nameProduct, "is added to wishlist !", "success");
    });
});
