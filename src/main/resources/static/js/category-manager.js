$(document).ready(function() {

    $('#example').DataTable({
        "columnDefs": [
            { "orderable": false, "targets": 2 }
        ],
        "paging":   false,
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

    $('.btn-edit-category').each(function () {
        $(this).on('click', function () {
            let modalSelector = $('#editCateModal');
            modalSelector.modal();
            let selector = $(this).parent().parent();
            let id = selector.find('.text-center').attr('id');
            modalSelector.find('.btn-submit-edit').attr('id',id);
            let cateName = selector.find('.cate-name').html();
            let cateDesc = selector.find('.cate-desc').html();
            modalSelector.find('input#cate-edit-name').val(cateName);
            modalSelector.find('input#cate-edit-describe').val(cateDesc);
        });
    });
    $('.btn-delete-category').on('click', function () {
        let selector = $(this).parent().parent();
        let id = selector.find('.text-center').attr('id');
        let cateName = selector.find('.cate-name').html();
        let cateDesc = selector.find('.cate-desc').html();
        swal({
            title: "Bạn có muốn xóa " + cateName,
            icon: "warning",
            buttons: true,
            dangerMode: true,
        }).then((willDelete) => {
            if (willDelete) {
                $.ajax({
                    type : "POST",
                    contentType : "application/json",
                    url : "/admin/products/category/delete",
                    data : JSON.stringify({
                        id : id,
                        cateName : cateName,
                        cateDesc : cateDesc
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

    $('#btn-add-category').on('click', function () {
        let select = $('#form-add-category');
        let formHolder = $('#form-add-cate-holder');
        let cateName = select.find('input#cate-name').val();
        let cateDesc = select.find('input#cate-describe').val();
        console.log(cateName + " " + cateDesc);
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/admin/products/category/add",
            data : JSON.stringify({
                cateName : cateName,
                cateDesc : cateDesc
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
                        "                Thêm chuyên mục thất bại, vui lòng tải lại trang </div>");
                }
            },
            error : function(e) {
                formHolder.prepend("<div class=\"alert alert-danger\" role=\"alert\">" +
                    "                <a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\" title=\"close\">×</a>" +
                    "                Thêm chuyên mục thất bại, vui lòng tải lại trang </div>");

                console.log("ERROR: ", e);
            }
        });
    });

    $('.btn-submit-edit').on('click', function () {
        let modalSelector = $('#editCateModal');
        let name = modalSelector.find('input#cate-edit-name').val();
        let desc = modalSelector.find('input#cate-edit-describe').val();
        let formHolder = $('#form-edit-cate-holder');
        let id = $(this).attr('id');
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/admin/products/category/edit",
            data : JSON.stringify({
                id : id,
                cateName : name,
                cateDesc : desc
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

    $('#exampleModal').on('hidden.bs.modal', function (e) {
        $('div.alert-danger').remove();
    });
    $('#editCateModal').on('hidden.bs.modal', function (e) {
        $('div.alert-danger').remove();
    });
});
