$(document).ready(function() {

    $('#example').DataTable({
        "columnDefs": [
            { "orderable": false, "targets": 5 }
        ],
        "order" : [[0, "desc"]],
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

    $('.btn-edit-bill').each(function () {
        $(this).on('click', function () {
            let modalSelector = $('#editBillStatus');
            let selector = $(this).parent().parent();
            let id = selector.find('.text-center').attr('id');
            modalSelector.find('.btn-submit-edit').attr('id',id);
            let status = selector.find('.bill-status').attr('value');
            modalSelector.find('select#bill-edit-status').val(status);
            modalSelector.modal();
        });
    });
    $('.btn-delete-bill').on('click', function () {
        let selector = $(this).parent().parent();
        let id = selector.find('.text-center').attr('id');
        swal({
            title: "Bạn có muốn xóa đơn #" + id,
            icon: "warning",
            buttons: true,
            dangerMode: true,
        }).then((willDelete) => {
            if (willDelete) {
                $.ajax({
                    type : "POST",
                    contentType : "application/json",
                    url : "/admin/bills/delete",
                    data : JSON.stringify({
                        id : id,
                    }),
                    dataType : 'json',
                    timeout : 100000,
                    success : function(data) {
                        if (data.status === 1) {
                            swal(name + " đã được xóa!", {
                                icon: "success",
                            }).then(() => {
                                window.location.reload(true);
                            });
                        } else {
                            swal("ERROR", "Xóa không thành công, có thể nhiều sản phẩm đang thuộc chuyên mục này", "error");

                        }
                    },
                    error : function(e) {
                        swal("ERROR", "Lỗi không xác định, vui lòng tải lại trang web", "error");
                    }
                });
            }
        });
    });


    $('.btn-submit-edit').on('click', function () {
        let modalSelector = $('#editBillStatus');
        let status = modalSelector.find('option:selected').val();
        let id = $(this).attr('id');
        let formHolder = $('#form-edit-bill-holder');
        console.log(status);
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/admin/bills/edit",
            data : JSON.stringify({
                id : id,
                bill_status : status,
            }),
            dataType : 'json',
            timeout : 100000,
            success : function(data) {
                var i = data.status;
                if (i===1) {
                    window.location.reload(true);
                }
                else {
                    formHolder.prepend("<div class=\"alert alert-danger\" role=\"alert\">" +
                        "                <a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\" title=\"close\">×</a>" +
                        "                Cập nhật chuyên mục thất bại, vui lòng tải lại trang </div>");
                }
            },
            error : function(e) {
                formHolder.prepend("<div class=\"alert alert-danger\" role=\"alert\">" +
                    "                <a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\" title=\"close\">×</a>" +
                    "                Cập nhật chuyên mục thất bại, vui lòng tải lại trang </div>");

                console.log("ERROR: ", e);
            }
        });
    });

    $('#editBillStatus').on('hidden.bs.modal', function (e) {
        $('div.alert-danger').remove();
    });
});
