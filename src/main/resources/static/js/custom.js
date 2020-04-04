$(function () {
    $.ajaxSetup({
        headers:
            {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')}
    });

    var _document = $(document),
        removeIcon = '<div class="col-md-1 remove-prod">' +
            '<label></label>' +
            '<button class="btn btn-danger" type="button">' +
            '<i class="glyphicon glyphicon-remove"></i> ' +
            '</button>' +
            '</div>';

    var swalDeleteConf = {
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes!'
        },
        retrieveFromForm = function (formDataArray) {
            var data = {};

            formDataArray.forEach(function (form) {
                if (form.hasOwnProperty('name') && form.hasOwnProperty('value')) {
                    data[form['name']] = form['value'];
                }
            });
            return data;
        },
        processPromiseResponse = function (message, timeout, _callback) {
            var _timeout = timeout || 2000;
            return new Promise(function (resolve) {
                if (_callback) {
                    _callback();
                }
                toastr.success(message);
                setTimeout(function () {
                    resolve();
                }, _timeout);
            })
        },
        parseInteger = function (value) {
            var _val = parseInt(value);
            if (isNaN(_val)) {
                return null;
            }
            return _val;
        };

    _document.on('click', '.add-instance', function () {
        var _this = $(this),
            modalId = _this.attr('data-modal-id'),
            modal = $('#' + modalId);
        if (!modal.length) {
            return false;
        }
        modal.modal('show');
    })

        .on('click', '.save-modal-data', function (e, eventData) {
            var _this = $(this),
                modal = _this.closest('.modal'),
                form = modal.find('form'),
                formData = form.serializeArray(),
                href = eventData && eventData.hasOwnProperty('setHref') ? eventData['setHref'] : _this.attr('data-href');
            if (!href) {
                return toastr.error("Wrong url!");
            }
            var data = eventData && eventData.hasOwnProperty('setData') ? eventData['setData'] : retrieveFromForm(formData);
            $.ajax({
                url: href,
                method: 'POST',
                contentType: 'application/json',
                mimeType: 'application/json',
                data: JSON.stringify(data),
                success: function (data) {
                    processPromiseResponse(data['message'], 2000, function () {
                        modal.modal('hide');
                    }).then(function () {
                        return location.reload();
                    });
                },
                error: function (xhr) {
                    toastr.error(xhr['responseJSON']['message']);
                }
            });
        })

        .on('change', '#user-assign', function () {
            var _this = $(this),
                tableId = _this.attr('data-table-id'),
                userId = _this.val();

            if (!userId) {
                return toastr.error('Please Select user');
            }

            if (!tableId) {
                return toastr.error('Something goes wrong!');
            }
            _this.attr('disable', true);
            $.ajax({
                url: '/tables/manager/assign/' + tableId + '/' + userId,
                method: 'POST',
                success: function (data) {
                    processPromiseResponse(data['message'], 2000).then(function () {
                        return location.reload();
                    });
                },
                error: function (xhr) {
                    return toastr.error(xhr['responseJSON']['message']);
                }
            }).done(function () {
                _this.removeAttr('disable');
            });
        })

        .on('click', '.instance-delete', function () {
            var _this = $(this),
                url = _this.attr('data-href');
            if (!url) {
                return toastr.error('Wrong url');
            }
            Swal.fire(swalDeleteConf).then((result) => {
                if (result.value) {
                    $.ajax({
                        url: url,
                        method: 'DELETE',
                        success: function (data) {
                            processPromiseResponse(data['message'], 2000).then(function () {
                                return location.reload();
                            });
                        },
                        error: function (xhr) {
                            return toastr.error(xhr['responseJSON']['message']);
                        }
                    });
                }
            });
        })

        .on('click', '.edit-instance', function (e, eventData) {
            var _this = $(this),
                url = eventData && eventData.hasOwnProperty('setHref') ? eventData['setHref'] : _this.attr('data-href'),
                data = {},
                elements = _this.closest('tr').find('input');

            elements.each(function (index, input) {
                var _input = $(input);
                data[_input.attr('name')] = _input.val();
            });
            if (eventData && eventData.hasOwnProperty('setData')) {
                data = eventData['setData'];
            }
            if (!url) {
                return toastr.error("Wrong url!");
            }
            Swal.fire(swalDeleteConf).then((result) => {
                if (result.value) {
                    $.ajax({
                        url: url,
                        method: 'PUT',
                        contentType: 'application/json',
                        mimeType: 'application/json',
                        data: JSON.stringify(data),
                        success: function (data) {
                            processPromiseResponse(data['message'], 2000).then(function () {
                                return location.reload();
                            });
                        },
                        error: function (xhr) {
                            return toastr.error(xhr['responseJSON']['message']);
                        }
                    });
                }
            });
        })

        .on('click', '#product-cloner', function () {
            var form = $(this).closest('.modal-body').find('form'),
                productRow = form.find('.product-row');

            var cloned = productRow.eq(0).clone();
            cloned.find('[data-type="input"]').each(function (index, item) {
                var _it = $(item),
                    namePart = 'order[' + productRow.length + ']',
                    name = _it.attr('data-name');

                if ('amount' === _it.attr('data-name')) {
                    _it.attr('name', namePart + '[' + name + ']');
                    _it.val(1);
                }

                if ('productId' === _it.attr('data-name')) {
                    _it.attr('name', namePart + '[' + name + ']');
                    _it.val(_it.find('option').eq(0).val());
                }
            });
            cloned.append(removeIcon);
            form.append(cloned);
        })

        .on('click', '.remove-prod', function () {
            $(this).closest('.product-row').remove();
        })

        .on('click', '.save-order', function () {
            var _this = $(this),
                form = _this
                    .closest('.modal-dialog')
                    .find('form'),
                prodRow = form.find('.product-row'),
                dataArray = [];

            prodRow.each(function (index, row) {
                var _row = $(row);
                dataArray.push({
                    productId: parseInteger(_row.find('[data-name="productId"]').val()),
                    amount: parseInteger(_row.find('[data-name="amount"]').val())
                });
            });
            var data = {
                setData: {products: dataArray},
                setHref: _this.attr('data-href')
            };
            $('.save-modal-data').trigger('click', [data]);
        })

        .on('change', '.order-status', function () {
            var _this = $(this),
                href = _this.attr('data-href'),
                status = _this.val(),
                data = {
                    setData: {
                        status: status
                    },
                    setHref: href
                };
            $('.edit-instance').trigger('click', [data]);
        });
});