$(document).ready(function() {
    var table = $('#example').DataTable({
        "columnDefs": [
            { "orderable": false, "targets": 6 }
        ],
        "language": {
            "sProcessing":   "Đang xử lý...",
            "sLengthMenu":   "Xem _MENU_ mục",
            "sZeroRecords":  "Không tìm thấy dòng nào phù hợp",
            "sInfo":         "Đang xem _START_ đến _END_ trong tổng số _TOTAL_ mục",
            "sInfoEmpty":    "Đang xem 0 đến 0 trong tổng số 0 mục",
            "sInfoFiltered": "(được lọc từ _MAX_ mục)",
            "sInfoPostFix":  "",
            "sSearch":       "Tìm:",
            "sUrl":          "",
            "oPaginate": {
                "sFirst":    "Đầu",
                "sPrevious": "Trước",
                "sNext":     "Tiếp",
                "sLast":     "Cuối"
            }
        }
    });
    $('.btn-delete-product').on('click', function () {
        let selector = $(this).parent().parent();
        let id = selector.find('.text-center').attr('id');
        let name = selector.find('.product-name').html();
        swal({
            title: "Bạn có muốn xóa " + name,
            icon: "warning",
            buttons: true,
            dangerMode: true,
        }).then((willDelete) => {
            if (willDelete) {
                $.ajax({
                    type : "POST",
                    contentType : "application/json",
                    url : "/admin/products/delete",
                    data : JSON.stringify({
                        id : id,
                    }),
                    dataType : 'json',
                    timeout : 100000,
                    success : function(data) {
                        var i = data.status;
                        if (data.status === 1) {
                            swal(name + " đã được xóa!", {
                                icon: "success",
                            }).then(() => {
                                window.location.reload(true);
                            });
                        } else {
                            swal("ERROR", "Xóa không thành công", "error");

                        }
                    },
                    error : function(e) {
                        swal("ERROR", "Lỗi không xác định, vui lòng tải lại trang web", "error");
                    }
                });
            }
        });
    });

    table.on('draw', function () {
        $('.btn-delete-product').on('click', function () {
            let selector = $(this).parent().parent();
            let id = selector.find('.text-center').attr('id');
            let name = selector.find('.product-name').html();
            swal({
                title: "Bạn có muốn xóa " + name,
                icon: "warning",
                buttons: true,
                dangerMode: true,
            }).then((willDelete) => {
                if (willDelete) {
                    $.ajax({
                        type : "POST",
                        contentType : "application/json",
                        url : "/admin/products/delete",
                        data : JSON.stringify({
                            id : id,
                        }),
                        dataType : 'json',
                        timeout : 100000,
                        success : function(data) {
                            var i = data.status;
                            if (data.status === 1) {
                                swal(name + " đã được xóa!", {
                                    icon: "success",
                                }).then(() => {
                                    window.location.reload(true);
                                });
                            } else {
                                swal("ERROR", "Xóa không thành công", "error");

                            }
                        },
                        error : function(e) {
                            swal("ERROR", "Lỗi không xác định, vui lòng tải lại trang web", "error");
                        }
                    });
                }
            });
        });
    });
});