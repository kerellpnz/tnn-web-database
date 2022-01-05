// // for adding a loader
// $(window).load(function(){
//     setTimeout(function() {
//         $(".se-pre-con").fadeOut("slow");
//     }, 500);
// });


//For handling CSRF token
var token = $('meta[name="_csrf"]').attr('content');
var header = $('meta[name="_csrf_header"]').attr('content');
if (token.length > 0 && header.length > 0) {
    $(document).ajaxSend(function (e,xhr,options) {
        xhr.setRequestHeader(header,token);
    })
}

//Selectize

$(document).ready(function () {
    $('.selectize').selectize({
        sortField: 'text'
    });
});


//DATE PICKER

$(function() {
    $('.date-picker').datepicker();
});


//AUTO HEIGHT TEXTAREA

$(function() {
    $('.form-textarea').each(function() {
        $(this).height($(this).prop('scrollHeight'));
    });
});

$('#operations a').click(function(e){
    e.preventDefault();
    $(this).tab('show');
    $(function() {
        $('.form-textarea').each(function() {
            $(this).height($(this).prop('scrollHeight'));
        });
    });
});

//TABLES

$(document).ready(function () {

    //dismissing the alert after 3 second
    var $alert = $('.alert');
    if($alert.length) {
        setTimeout(function () {
            $alert.fadeOut('slow');
        }, 3000)
    }

    //TCP TABLE

    var $TCPTable = $('#tcp-table');

    if($TCPTable.length) {
        //console.log('Inside the table!');
        var jsonUrl = '';
        jsonUrl = window.contextRoot + '/tcp/' + window.tcpClass + '/json/getAll';
        var myModal = $('#newTCPModal');

        $TCPTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить пункт',
                    action: function ( e, dt, node, config ) {
                        $('#newTCPModal #id').val('0');
                        $('#newTCPModal #productType').val('');
                        $('#newTCPModal #operationType').val('');
                        $('#newTCPModal #point').val('');
                        $('#newTCPModal #shortDescription').val('');
                        $('#newTCPModal #description').val('');
                        $('#newTCPModal #placeOfControl').val('');
                        $('#newTCPModal #document').val('');

                        myModal.modal({
                            show : true
                        });
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'productType'
                },
                {
                    className: 'view-data',
                    data: 'operationType'
                },
                {
                    className: 'view-data',
                    data: 'point'
                },
                {
                    data: 'shortDescription'
                },
                {
                    className: 'tcp-description',
                    data: 'description'
                },
                {
                    className: 'view-data',
                    data: 'placeOfControl'
                },
                {
                    className: 'view-data',
                    data: 'document'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a href="' + window.contextRoot + '/tcp/' + window.tcpClass
                            + '/delete/' + data + '" class="btn btn-danger" ' +
                            'onclick="if (!(confirm(\'Удалить этот пункт ПТК?\'))) return false">' +
                            '<span class="glyphicon glyphicon-remove"></span></a>';
                        return str;
                    }
                }
            ]
        });
        $('#tcp-table tbody').on('dblclick', 'td', function () {
            var data = $TCPTable.DataTable().row( $(this).parents('tr') ).data();
            $('#newTCPModal #id').val(data['id']);
            $('#newTCPModal #productType').val(data['productType']);
            $('#newTCPModal #operationType').val(data['operationType']);
            $('#newTCPModal #point').val(data['point']);
            $('#newTCPModal #shortDescription').val(data['shortDescription']);
            $('#newTCPModal #description').val(data['description']);
            $('#newTCPModal #placeOfControl').val(data['placeOfControl']);
            $('#newTCPModal #document').val(data['document']);

            myModal.modal({
                show : true
            });
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $TCPTable.DataTable().row( this ).index();
            $( $TCPTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $TCPTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on('touchstart','td', function(e){
            var rowIdx = $TCPTable.DataTable().row( $(this).parents('tr') ).index();
            $( $TCPTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $TCPTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $TCPTable.DataTable().row( $(this).parents('tr') ).data();
                $('#newTCPModal #id').val(data['id']);
                $('#newTCPModal #productType').val(data['productType']);
                $('#newTCPModal #operationType').val(data['operationType']);
                $('#newTCPModal #point').val(data['point']);
                $('#newTCPModal #description').val(data['description']);
                $('#newTCPModal #placeOfControl').val(data['placeOfControl']);
                $('#newTCPModal #document').val(data['document']);

                myModal.modal({
                    show : true
                });
                e.preventDefault()
            }
        });
    }



    //validation code for TCP
    var $tcpForm = $('#tcpForm');

    if ($tcpForm.length) {
        $tcpForm.validate({
            rules: {
                productType: {
                    required: true
                },
                operationType: {
                    required: true
                },
                point: {
                    required: true
                },
                shortDescription: {
                    required: true
                },
                description: {
                    required: true
                },
                placeOfControl: {
                    required: true
                },
                document: {
                    required: true
                }
            },
            messages: {
                productType: {
                    required: 'Выберете тип продукта!'
                },
                operationType: {
                    required: 'Выберете тип операции!'
                },
                point: {
                    required: 'Введите номер пункта!'
                },
                shortDescription: {
                    required: 'Введите краткое описание!'
                },
                description: {
                    required: 'Введите описание!'
                },
                placeOfControl: {
                    required: 'Выберете место контроля!'
                },
                document: {
                    required: 'Выберете документацию!'
                }
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }

    //validation for LOGIN PAGE
    var $loginForm = $('#loginForm');

    if ($loginForm.length) {
        $loginForm.validate({
            rules: {
                username: {
                    required: true
                },
                password: {
                    required: true
                }
            },
            messages: {
                username: {
                    required: 'Введите логин!'
                },
                password: {
                    required: 'Введите пароль!'
                }
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }

    //CUSTOMER TABLE

    var $CustomerTable = $('#customer-table');

    if($CustomerTable.length) {
        var jsonUrl = '';
        jsonUrl = window.contextRoot + '/tcp/Customers/json/getAll';
        var myModal = $('#newCustomerModal');

        $CustomerTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить заказчика',
                    action: function ( e, dt, node, config ) {
                        $('#newCustomerModal #id').val('0');
                        $('#newCustomerModal #name').val('');
                        $('#newCustomerModal #shortName').val('');

                        myModal.modal({
                            show : true
                        });
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'name'
                },
                {
                    className: 'view-data',
                    data: 'shortName'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a href="' + window.contextRoot + '/tcp/Customers/delete/' + data + '" class="btn btn-danger" ' +
                            'onclick="if (!(confirm(\'Удалить этого заказчика?\'))) return false">' +
                            '<span class="glyphicon glyphicon-remove"></span></a>';
                        return str;
                    }
                }
            ]
        });
        $('#customer-table tbody').on('dblclick', 'td', function () {
            var data = $CustomerTable.DataTable().row( $(this).parents('tr') ).data();
            $('#newCustomerModal #id').val(data['id']);
            $('#newCustomerModal #name').val(data['name']);
            $('#newCustomerModal #shortName').val(data['shortName']);

            myModal.modal({
                show : true
            });
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $CustomerTable.DataTable().row( this ).index();
            $( $CustomerTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $CustomerTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on('touchstart','td', function(e){
            var rowIdx = $CustomerTable.DataTable().row( $(this).parents('tr') ).index();
            $( $CustomerTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $CustomerTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $CustomerTable.DataTable().row( $(this).parents('tr') ).data();
                $('#newCustomerModal #id').val(data['id']);
                $('#newCustomerModal #name').val(data['name']);
                $('#newCustomerModal #shortName').val(data['shortName']);

                myModal.modal({
                    show : true
                });
                e.preventDefault()
            }
        });
    }

    //validation code for Customer
    var $customerForm = $('#customerForm');

    if ($customerForm.length) {
        $customerForm.validate({
            rules: {
                name: {
                    required: true
                },
                shortName: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: 'Введите наиманование!'
                },
                shortName: {
                    required: 'Введите аббревиатуру!'
                }
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }

    //JOURNAL TABLE

    var $JournalNumberTable = $('#journal-number-table');

    if($JournalNumberTable.length) {
        //console.log('Inside the table!');
        var jsonUrl = '';
        jsonUrl = window.contextRoot + '/entity/JournalNumbers/json/getAll';
        var myModal = $('#newJournalNumberModal');
        var changeJournalModal = $('#changeJournal');

        $JournalNumberTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить новый журнал',
                    action: function ( e, dt, node, config ) {
                        $('#newJournalNumberModal #id').val('0');
                        $('#newJournalNumberModal #number').val('');

                        myModal.modal({
                            show : true
                        });
                    }
                },
                {
                    text: 'Сменить активный журнал',
                    action: function ( e, dt, node, config ) {
                        changeJournalModal.modal({
                            show : true
                        });
                    }
                },
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: -1,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'number'
                },
                {
                    className: 'view-data',
                    data: 'closed',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<label class="switch">';
                        if (data) {
                            str += '<input type="checkbox" checked="checked" value="' + row.id + '"/>';
                        }
                        else {
                            str += '<input type="checkbox"  value="' + row.id + '"/>';
                        }
                        str += '<div class="slider"></div></label>';
                        return str;
                    }
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/JournalNumbers/delete/' + data + '" class="btn btn-danger delete" ' +
                                'onclick="if (!(confirm(\'Удалить этот журнал\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                var api = this.api();
                api.$('.switch input[type="checkbox"]').on('change', function () {
                    var checkbox = $(this);
                    var checked = checkbox.prop('checked');
                    var dMsg = (checked)? 'Закрыть этот журнал?':
                                            'Открыть этот журнал?';
                    var value = checkbox.prop('value');
                    bootbox.confirm({
                        size: 'medium',
                        title: 'Подтверждение действия',
                        message: dMsg,
                        locale: 'ru',
                        callback: function (confirmed) {
                            if(confirmed) {
                                if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                                    $.ajax({
                                        type: 'GET',
                                        url: window.contextRoot + '/entity/JournalNumbers/journal/' + value + '/activation'
                                    });
                                }
                                else {
                                    checkbox.prop('checked', !checked);
                                    bootbox.alert('НЕТ ДОСТУПА!')
                                }
                            }
                            else {
                                checkbox.prop('checked', !checked);
                            }
                        }
                    });
                });
            }
        });
        $('#journal-number-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $JournalNumberTable.DataTable().row( $(this).parents('tr') ).data();
            $('#newJournalNumberModal #id').val(data['id']);
            $('#newJournalNumberModal #number').val(data['number']);
            $('#newJournalNumberModal #isClosed').val(data['closed']);

            myModal.modal({
                show : true
            });
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $JournalNumberTable.DataTable().row( this ).index();
            $( $JournalNumberTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $JournalNumberTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $JournalNumberTable.DataTable().row( $(this).parents('tr') ).index();
            $( $JournalNumberTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $JournalNumberTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $JournalNumberTable.DataTable().row( $(this).parents('tr') ).data();
                $('#newJournalNumberModal #id').val(data['id']);
                $('#newJournalNumberModal #number').val(data['number']);
                $('#newJournalNumberModal #isClosed').val(data['closed']);

                myModal.modal({
                    show : true
                });
                e.preventDefault()
            }
        });
    }

    var $changeJournalForm = $('#changeJournalForm');

    if ($changeJournalForm.length) {
        $changeJournalForm.validate({
            rules: {
                number: {
                    required: true
                }
            },
            messages: {
                number: {
                    required: 'Журнал не может быть не выбран!'
                }
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }

    //SPECIFICATION TABLE

    function format ( d ) {
        // `d` is the original data object for the row
        var output = '';
        for(var i = 0; i < d.pids.length; i++){
            output += '<tr><td class="inner-pid-column">' + d.pids[i].number + '</td><td class="inner-pid-column">' + d.pids[i].designation + '</td><td class="inner-pid-column">' +
                '<a href="' + window.contextRoot + '/Specifications/showFormForUpdate/' + d.pids[i].id + '" class="btn btn-primary edit" >' +
            'Изменить PID</span></a> &#160;';
            if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                output += '<a href="' + window.contextRoot + '/Specifications/delete/pid/' + d.pids[i].id + '" class="btn btn-danger delete" ' +
                            'onclick="if (!(confirm(\'Удалить этот PID\'))) return false">' +
                            'Удалить PID</a>'
            }
            output += '</td><td></td></tr>'
        }
        return '<table class="table table-border table-inner-pid">' + output + '</table>';
    }

    var $SpecificationTable = $('#specification-table');

    if($SpecificationTable.length) {
        //console.log('Inside the table!');
        var jsonUrl = window.contextRoot + '/Specifications/json/getAll';
        var myModal = $('#newSpecificationModal');
        var findPIDModal = $('#findPID');

        $SpecificationTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить новую спецификацию',
                    action: function ( e, dt, node, config ) {
                        $('#newSpecificationModal #id').val('0');
                        $('#newSpecificationModal #number').val('');
                        $('#newSpecificationModal #program').val('');
                        $('#newSpecificationModal #customer').val('0');
                        $('#newSpecificationModal #facility').val('');

                        myModal.modal({
                            show : true
                        });
                    }
                },
                {
                    text: 'Найти PID',
                    action: function ( e, dt, node, config ) {
                        findPIDModal.modal({
                            show : true
                        });
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className:      'details-control view-data',
                    orderable:      false,
                    bSortable: false,
                    data:           null,
                    defaultContent: ''
                },
                {
                    className:      'view-data',
                    data: 'id'
                },
                {
                    className:      'view-data',
                    data: 'number'
                },
                {
                    className:      'view-data',
                    data: 'program'
                },
                {
                    className:      'view-data',
                    data: 'customer',
                    mRender: function (data, type, row) {
                        if (data === null) {
                            return data;
                        }
                        else {
                            return data.shortName;
                        }
                    }
                },
                {
                    className:      'view-data',
                    data: 'facility',
                    bSortable: false
                },
                {
                    className:      'control-data',
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';

                        str += '<a href="' + window.contextRoot + '/Specifications/showFormForAdd/' + data + '" class="btn btn-success add" >' +
                            'Добавить PID</a> &#160;';

                        str += '<a class="btn btn-primary edit" >' +
                            'Изменить</a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/Specifications/delete/' + data + '" class="btn btn-danger delete" ' +
                                'onclick="if (!(confirm(\'Удалить эту спецификацию\'))) return false">' +
                                'Удалить</span></a>';
                        }
                        return str;
                    }
                },
                {
                    data: 'pids',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        return "";
                    }
                },
            ],
            initComplete: function () {
                $SpecificationTable.DataTable().page('last').draw('page');
            }
        });
        $('#specification-table tbody').on( 'click', 'a.edit', function () {
            var data = $SpecificationTable.DataTable().row( $(this).parents('tr') ).data();
            $('#newSpecificationModal #id').val(data['id']);
            $('#newSpecificationModal #number').val(data['number']);
            $('#newSpecificationModal #program').val(data['program']);
            var customer = data['customer'];
            $('#newSpecificationModal #customer').val(customer['id']);
            $('#newSpecificationModal #facility').val(data['facility']);

            myModal.modal({
                show : true
            });
        } );
        $('#specification-table tbody').on('click', 'td.view-data', function () {
            var tr = $(this).closest('tr');
            var row = $SpecificationTable.DataTable().row( tr );

            if ( row.child.isShown() ) {
                // This row is already open - close it
                row.child.hide();
                tr.removeClass('shown');
            }
            else {
                // Open this row
                row.child( format(row.data()) ).show();
                tr.addClass('shown');
            }
        } );
        $('#specification-table tbody')
            .on( 'mouseenter', 'tr', function () {
                var rowIdx = $SpecificationTable.DataTable().row( this ).index();
                $( $SpecificationTable.DataTable().rows().nodes() ).removeClass( 'highlight' );
                if (rowIdx !== undefined) {
                    $( $SpecificationTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight' );
                }
            } );
    }

    //validation code for SPECIFICATION
    var $specificationForm = $('#specificationForm');

    if ($specificationForm.length) {
        $specificationForm.validate({
            rules: {
                number: {
                    required: true
                },
                program: {
                    required: true
                },
                customer: {
                    required: true
                },
                facility: {
                    required: true
                }
            },
            messages: {
                number: {
                    required: 'Введите номер спецификации!'
                },
                program: {
                    required: 'Введите программу!'
                },
                customer: {
                    required: 'Выберете заказчика!'
                },
                facility: {
                    required: 'Введите объект!'
                }
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }

    var $findPIDForm = $('#findPIDForm');

    if ($findPIDForm.length) {
        $findPIDForm.validate({
            rules: {
                PIDNumber: {
                    required: true
                }
            },
            messages: {
                PIDNumber: {
                    required: 'Номер не может быть пустым!'
                }
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }

    //GATEVALVE TABLE

    var $SheetGateValveTable = $('#gate-valve-table');

    if($SheetGateValveTable.length) {
        var jsonUrl = window.contextRoot + '/entity/SheetGateValves/json/getAll';
        // var myModal = $('#singleCopyModal');
        // var myModal2 = $('#multiCopyModal');

        $SheetGateValveTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить новую ЗШ',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/SheetGateValves/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'number',
                    mRender: function (data, type, row) {
                        return 'О' + data;
                    }
                },
                {
                    className: 'view-data',
                    data: 'pid',
                    mRender: function (data, type, row) {
                        if (data === null) {
                            return '';
                        }
                        else {
                            return data.designation;
                        }
                    }
                },
                {
                    className: 'view-data',
                    data: 'pid',
                    mRender: function (data, type, row) {
                        if (data === null) {
                            return '';
                        }
                        else {
                            return data.number;
                        }
                    }
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        // str += '<a class="btn btn-primary copy" >' +
                        //     '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        // str += '<a class="btn btn-primary multi-copy" >' +
                        //     '<span class="glyphicon glyphicon-list"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/SheetGateValves/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить эту ЗШ?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                $SheetGateValveTable.DataTable().page('last').draw('page');
            }
        });
        $('#gate-valve-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $SheetGateValveTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/SheetGateValves/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $SheetGateValveTable.DataTable().row( this ).index();
            $( $SheetGateValveTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $SheetGateValveTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.multi-copy', function () {
            var data = $SheetGateValveTable.DataTable().row( $(this).parents('tr') ).data();
            $('#multiCopyModal #id').val(data['id']);

            myModal2.modal({
                show : true
            });
        }).on( 'click', 'a.copy', function () {
            var data = $SheetGateValveTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $SheetGateValveTable.DataTable().row( $(this).parents('tr') ).index();
            $( $SheetGateValveTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $SheetGateValveTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $SheetGateValveTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/SheetGateValves/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //SCREW TABLE (SCREWSTUD, SCREWNUT)

    var $ScrewTable = $('#screw-table');

    if($ScrewTable.length) {
        var jsonUrl = window.contextRoot + '/entity/' + window.screwClass + '/json/getAll';
        var myModal = $('#singleCopyModal');
        var tapped = false;
        $ScrewTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить новый крепеж (' + window.title + ')',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/' + window.screwClass + '/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'dn'
                },
                {
                    className: 'view-data',
                    data: 'certificate'
                },
                {
                    className: 'view-data',
                    data: 'batch'
                },
                {
                    className: 'view-data',
                    data: 'melt'
                },
                {
                    className: 'view-data',
                    data: 'drawing'
                },
                {
                    className: 'view-data',
                    data: 'material'
                },
                {
                    className: 'view-data',
                    data: 'amount'
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/' + window.screwClass
                                + '/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить этот крепеж?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                $ScrewTable.DataTable().page('last').draw('page');
            }
        });
        $('#screw-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $ScrewTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/' + window.screwClass + '/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $ScrewTable.DataTable().row( this ).index();
            $( $ScrewTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $ScrewTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.copy', function () {
            var data = $ScrewTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $ScrewTable.DataTable().row( $(this).parents('tr') ).index();
            $( $ScrewTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $ScrewTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $ScrewTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/' + window.screwClass + '/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //SHEAR PIN TABLE

    var $ShearPinTable = $('#shear-pin-table');

    if($ShearPinTable.length) {
        var jsonUrl = window.contextRoot + '/entity/ShearPins/json/getAll';
        var myModal = $('#shearPinCopyModal');
        var tapped = false;
        $ShearPinTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить деталь (' + window.title + ')',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/ShearPins/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'number'
                },
                {
                    className: 'view-data',
                    data: 'diameter'
                },
                {
                    className: 'view-data',
                    data: 'tensileStrength'
                },
                {
                    className: 'view-data',
                    data: 'pull'
                },
                {
                    className: 'view-data',
                    data: 'drawing'
                },
                {
                    className: 'view-data',
                    data: 'certificate'
                },
                {
                    className: 'view-data',
                    data: 'melt'
                },
                {
                    className: 'view-data',
                    data: 'material'
                },
                {
                    className: 'view-data',
                    data: 'amount'
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/ShearPins'
                                + '/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить этот штифт?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                $ShearPinTable.DataTable().page('last').draw('page');
            }
        });
        $('#shear-pin-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $ShearPinTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/ShearPins/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $ShearPinTable.DataTable().row( this ).index();
            $( $ShearPinTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $ShearPinTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.copy', function () {
            var data = $ShearPinTable.DataTable().row( $(this).parents('tr') ).data();
            $('#shearPinCopyModal #id').val(data['id']);
            $('#shearPinCopyModal #number').val(data['number']);
            $('#shearPinCopyModal #diameter').val(data['diameter']);
            $('#shearPinCopyModal #tensileStrength').val(data['tensileStrength']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $ShearPinTable.DataTable().row( $(this).parents('tr') ).index();
            $( $ShearPinTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $ShearPinTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $ShearPinTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/ShearPins/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //METAL MATERIAL TABLE (SHEETMATERIAL, ROLLEDMATERIAL)

    var $MetalMaterialTable = $('#metal-material-table');

    if($MetalMaterialTable.length) {
        var jsonUrl = window.contextRoot + '/entity/' + window.materialClass + '/json/getAll';
        var myModal = $('#singleCopyModal');
        var tapped = false;
        $MetalMaterialTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить (' + window.title + ')',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/' + window.materialClass + '/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'certificate'
                },
                {
                    className: 'view-data',
                    data: 'melt'
                },
                {
                    className: 'view-data',
                    data: 'number'
                },
                {
                    className: 'view-data',
                    data: 'batch'
                },
                {
                    className: 'view-data',
                    data: 'material'
                },
                {
                    className: 'view-data',
                    data: 'thirdSize',
                    mRender: function (data, type, row) {
                        if (window.materialClass === 'RolledMaterials') {
                            return 'Ф'+data;
                        }
                        else
                            return data;
                    }
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/' + window.materialClass
                                + '/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить этот прокат?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                if (window.materialClass === 'RolledMaterials') {
                    $MetalMaterialTable.DataTable().column( 3 ).visible( false );
                }
                $MetalMaterialTable.DataTable().page('last').draw('page');
            }
        });
        $('#metal-material-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $MetalMaterialTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/' + window.materialClass + '/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $MetalMaterialTable.DataTable().row( this ).index();
            $( $MetalMaterialTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $MetalMaterialTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.copy', function () {
            var data = $MetalMaterialTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $MetalMaterialTable.DataTable().row( $(this).parents('tr') ).index();
            $( $MetalMaterialTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $MetalMaterialTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $MetalMaterialTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/' + window.materialClass + '/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //COATING TABLE (ABOVEGROUNDCOATING, ABRASIVEMATERIAL, UNDERCOAT, UNDERGROUNDCOATING)

    var $CoatingTable = $('#coating-table');

    if($CoatingTable.length) {
        var jsonUrl = window.contextRoot + '/entity/' + window.coatingClass + '/json/getAll';
        var myModal = $('#singleCopyModal');
        var tapped = false;
        $CoatingTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить покрытие (' + window.title + ')',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/' + window.coatingClass + '/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'name'
                },
                {
                    className: 'view-data',
                    data: 'certificate'
                },
                {
                    className: 'view-data',
                    data: 'batch'
                },
                {
                    className: 'view-data',
                    data: 'inputControlDate'
                },
                {
                    className: 'view-data',
                    data: 'expirationDate'
                },
                {
                    className: 'view-data',
                    data: 'amount'
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/' + window.coatingClass
                                + '/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить это АКП?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                $CoatingTable.DataTable().page('last').draw('page');
            },
            columnDefs:[{targets:[4,5], render:function(data){
                    return moment(data).format('DD.MM.yyyy');
                }}]
        });
        $('#coating-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $CoatingTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/' + window.coatingClass + '/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $CoatingTable.DataTable().row( this ).index();
            $( $CoatingTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $CoatingTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.copy', function () {
            var data = $CoatingTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $CoatingTable.DataTable().row( $(this).parents('tr') ).index();
            $( $CoatingTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $CoatingTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $CoatingTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/' + window.coatingClass + '/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //COMMON TABLE (CASEBOTTOM, FLANGE, COVERSLEEVE008)

    var $CommonDetailTable = $('#common-detail-table');

    if($CommonDetailTable.length) {
        var jsonUrl = window.contextRoot + '/entity/' + window.commonDetailClass + '/json/getAll';
        var myModal = $('#singleCopyModal');
        var myModal2 = $('#multiCopyModal');

        $CommonDetailTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить деталь (' + window.title + ')',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/' + window.commonDetailClass + '/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'dn'
                },
                {
                    className: 'view-data',
                    data: 'zk'
                },
                {
                    className: 'view-data',
                    data: 'number'
                },
                {
                    className: 'view-data',
                    data: 'melt'
                },
                {
                    className: 'view-data',
                    data: 'certificate'
                },
                {
                    className: 'view-data',
                    data: 'drawing'
                },
                {
                    className: 'view-data',
                    data: 'material'
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        str += '<a class="btn btn-primary multi-copy" >' +
                            '<span class="glyphicon glyphicon-list"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/' + window.commonDetailClass + '/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить эту деталь?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                if (window.commonDetailClass !== 'CoverSleeves008') {
                    $CommonDetailTable.DataTable().column( 2 ).visible( false );
                }
                $CommonDetailTable.DataTable().page('last').draw('page');
            }
        });
        $('#common-detail-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $CommonDetailTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/' + window.commonDetailClass + '/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $CommonDetailTable.DataTable().row( this ).index();
            $( $CommonDetailTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $CommonDetailTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.copy', function () {
            var data = $CommonDetailTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on( 'click', 'a.multi-copy', function () {
            var data = $CommonDetailTable.DataTable().row( $(this).parents('tr') ).data();
            $('#multiCopyModal #id').val(data['id']);

            myModal2.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $CommonDetailTable.DataTable().row( $(this).parents('tr') ).index();
            $( $CommonDetailTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $CommonDetailTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $CommonDetailTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/' + window.commonDetailClass + '/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //validating ENTITY FORM

    var $shearPinCopyForm = $('#shearPinCopyForm');

    if ($shearPinCopyForm.length) {
        $shearPinCopyForm.validate({
            rules: {
                number: {
                    required: true
                },
                diameter: {
                    required: true
                },
                tensileStrength: {
                    required: true
                },
                pull: {
                    required: true
                },
                amount: {
                    required: true,
                    number: true
                },
            },
            messages: {
                number: {
                    required: 'Номер не может быть пустым!'
                },
                diameter: {
                    required: 'Поле не может быть пустым'
                },
                tensileStrength: {
                    required: 'Поле не может быть пустым'
                },
                pull: {
                    required: 'Поле не может быть пустым'
                },
                amount: {
                    required: 'Поле не может быть пустым',
                    number: 'Введите число!'
                },
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }

    var $entityForm = $('#entityIdForm');

    if ($entityForm.length) {
        $entityForm.validate({
            rules: {
                number: {
                    required: true
                }
            },
            messages: {
                number: {
                    required: 'Номер не может быть пустым!'
                }
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }

    var $entityForm2 = $('#entityIdForm2');

    if ($entityForm2.length) {
        $entityForm2.validate({
            rules: {
                quantity: {
                    required: true,
                    range: [1, 20]
                }
            },
            messages: {
                quantity: {
                    required: 'Поле не может быть пустым!',
                    range: 'Введите число от 1 до 20'
                }
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }

    var $SheetGateValveForm = $('#sheetGateValveForm');

    if ($SheetGateValveForm.length) {
        $SheetGateValveForm.validate({
            rules: {
                amountShear: {
                    required: true,
                    number: true
                },
                amountStud: {
                    required: true,
                    number: true
                },
                amountNut: {
                    required: true,
                    number: true
                },
                amountSpring: {
                    required: true,
                    number: true
                }
            },
            messages: {
                amountShear: {
                    required: 'Поле не может быть пустым!',
                    number: 'Введите число!'
                },
                amountStud: {
                    required: 'Поле не может быть пустым!',
                    number: 'Введите число!'
                },
                amountNut: {
                    required: 'Поле не может быть пустым!',
                    number: 'Введите число!'
                },
                amountSpring: {
                    required: 'Поле не может быть пустым!',
                    number: 'Введите число!'
                }
            },
            errorElement: 'em',
            errorPlacement: function (error, element) {
                error.addClass('help-block');
                error.insertAfter(element);
            }
        });
    }

    // ASSEMBLIES TABLE (SHEETGATEVALVECASE, SHEETGATEVALVECOVER)

    var $AssembliesTable = $('#assemblies-table');

    if($AssembliesTable.length) {
        var jsonUrl = window.contextRoot + '/entity/' + window.assemblyClass + '/json/getAll';
        var myModal = $('#singleCopyModal');

        $AssembliesTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить деталь (' + window.title + ')',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/' + window.assemblyClass + '/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'dn'
                },
                {
                    className: 'view-data',
                    data: 'pn'
                },
                {
                    className: 'view-data',
                    data: 'number'
                },
                {
                    className: 'view-data',
                    data: 'melt'
                },
                {
                    className: 'view-data',
                    data: 'material'
                },
                {
                    className: 'view-data',
                    data: 'drawing'
                },
                {
                    className: 'view-data',
                    data: 'pid',
                    mRender: function (data, type, row) {
                        if (data === null) {
                            return '';
                        }
                        else {
                            return data.number;
                        }
                    }
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/' + window.assemblyClass + '/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить эту деталь?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                if (window.assemblyClass === 'SheetGateValveCovers') {
                    $AssembliesTable.DataTable().column( 5 ).visible( false );
                }
                $AssembliesTable.DataTable().page('last').draw('page');
            }
        });
        $('#assemblies-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $AssembliesTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/' + window.assemblyClass + '/showFormForUpdate/' + data['id'];
        }).on( 'click', 'a.copy', function () {
            var data = $AssembliesTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        } ).on( 'mouseenter', 'tr', function () {
            var rowIdx = $AssembliesTable.DataTable().row( this ).index();
            $( $AssembliesTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $AssembliesTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $AssembliesTable.DataTable().row( $(this).parents('tr') ).index();
            $( $AssembliesTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $AssembliesTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $AssembliesTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/' + window.assemblyClass + '/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //ASSEMBLY DETAIL TABLE (SADDLE, NOZZLE)

    var $AssemblyDetailTable = $('#assembly-detail-table');

    if($AssemblyDetailTable.length) {
        var jsonUrl = window.contextRoot + '/entity/' + window.assemblyDetail + '/json/getAll';
        var myModal = $('#singleCopyModal');
        var myModal2 = $('#multiCopyModal');

        $AssemblyDetailTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить деталь (' + window.title + ')',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/' + window.assemblyDetail + '/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'dn'
                },
                {
                    className: 'view-data',
                    data: 'pn'
                },
                {
                    className: 'view-data',
                    data: 'zk'
                },
                {
                    className: 'view-data',
                    data: 'number'
                },
                {
                    className: 'view-data',
                    data: 'melt'
                },
                {
                    className: 'view-data',
                    data: 'tensileStrength'
                },
                {
                    className: 'view-data',
                    data: 'grooving'
                },
                {
                    className: 'view-data',
                    data: 'material'
                },
                {
                    className: 'view-data',
                    data: 'certificate'
                },
                {
                    className: 'view-data',
                    data: 'drawing'
                },
                {
                    className: 'view-data',
                    data: 'pid',
                    mRender: function (data, type, row) {
                        if (data === null) {
                            return '';
                        }
                        else {
                            return data.number;
                        }
                    }
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        str += '<a class="btn btn-primary multi-copy" >' +
                            '<span class="glyphicon glyphicon-list"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/' + window.assemblyDetail + '/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить эту деталь?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                if (window.assemblyDetail === 'Saddles') {
                    $AssemblyDetailTable.DataTable().column( 6 ).visible( false );
                    $AssemblyDetailTable.DataTable().column( 7 ).visible( false );
                }
                $AssemblyDetailTable.DataTable().page('last').draw('page');
            }
        });
        $('#assembly-detail-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $AssemblyDetailTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/' + window.assemblyDetail + '/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $AssemblyDetailTable.DataTable().row( this ).index();
            $( $AssemblyDetailTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $AssemblyDetailTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.multi-copy', function () {
            var data = $AssemblyDetailTable.DataTable().row( $(this).parents('tr') ).data();
            $('#multiCopyModal #id').val(data['id']);

            myModal2.modal({
                show : true
            });
        }).on( 'click', 'a.copy', function () {
            var data = $AssemblyDetailTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $AssemblyDetailTable.DataTable().row( $(this).parents('tr') ).index();
            $( $AssemblyDetailTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $AssemblyDetailTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $AssemblyDetailTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/' + window.assemblyDetail + '/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //PERIODICAL TABLE

    var $PeriodicalTable = $('#periodical-table');

    if($PeriodicalTable.length) {
        var jsonUrl = window.contextRoot + '/entity/' + window.periodicalClass + '/json/getAll';

        $PeriodicalTable.DataTable( {
            dom: 'flrtip',
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'name'
                },
                {
                    className: 'view-data',
                    data: 'lastControl'
                },
                {
                    className: 'view-data',
                    data: 'nextControl'
                }
            ],
            columnDefs:[{targets:[2,3], render:function(data){
                    return moment(data).format('DD.MM.yyyy');
                }}]
        });
        $('#periodical-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $PeriodicalTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/' + window.periodicalClass + '/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $PeriodicalTable.DataTable().row( this ).index();
            $( $PeriodicalTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $PeriodicalTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $PeriodicalTable.DataTable().row( $(this).parents('tr') ).index();
            $( $PeriodicalTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $PeriodicalTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $PeriodicalTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/' + window.periodicalClass + '/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //CONTROL WELD TABLE

    var $ControlWeldsTable = $('#control-weld-table');

    if($ControlWeldsTable.length) {
        var jsonUrl = window.contextRoot + '/entity/ControlWelds/json/getAll';
        var myModal = $('#singleCopyModal');

        $ControlWeldsTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить КСС',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/ControlWelds/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'number'
                },
                {
                    className: 'view-data',
                    data: 'weldingMethod'
                },
                {
                    className: 'view-data',
                    data: 'mechanicalPropertiesReport'
                },
                {
                    className: 'view-data',
                    data: 'metallographicPropertiesReport'
                },
                {
                    className: 'view-data',
                    data: 'expiryDate'
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        // str += '<a class="btn btn-primary copy" >' +
                        //     '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/ControlWelds/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить этот КСС?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            columnDefs:[{targets:[5], render:function(data){
                    return moment(data).format('DD.MM.yyyy');
                }}],
            initComplete: function () {
                $ControlWeldsTable.DataTable().page('last').draw('page');
            }
        });
        $('#control-weld-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $ControlWeldsTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/ControlWelds/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $ControlWeldsTable.DataTable().row( this ).index();
            $( $ControlWeldsTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $ControlWeldsTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.copy', function () {
            var data = $ControlWeldsTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $ControlWeldsTable.DataTable().row( $(this).parents('tr') ).index();
            $( $ControlWeldsTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $ControlWeldsTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $ControlWeldsTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/ControlWelds/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //WELDING MATERIAL TABLE

    var $WeldingMaterialsTable = $('#welding-materials-table');

    if($WeldingMaterialsTable.length) {
        var jsonUrl = window.contextRoot + '/entity/WeldingMaterials/json/getAll';
        var myModal = $('#singleCopyModal');

        $WeldingMaterialsTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Новый сварочный материал',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/WeldingMaterials/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'name'
                },
                {
                    className: 'view-data',
                    data: 'batch'
                },
                {
                    className: 'view-data',
                    data: 'certificate'
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/WeldingMaterials/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить этот материал?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                $WeldingMaterialsTable.DataTable().page('last').draw('page');
            }
        });
        $('#welding-materials-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $WeldingMaterialsTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/WeldingMaterials/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $WeldingMaterialsTable.DataTable().row( this ).index();
            $( $WeldingMaterialsTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $WeldingMaterialsTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.copy', function () {
            var data = $WeldingMaterialsTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $WeldingMaterialsTable.DataTable().row( $(this).parents('tr') ).index();
            $( $WeldingMaterialsTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $WeldingMaterialsTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $WeldingMaterialsTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/WeldingMaterials/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //DETAIL TABLE WITH ZK (COVERSLEEVE, SPINDLE, GATE)

    var $DetailTable = $('#detail-table');

    if($DetailTable.length) {
        var jsonUrl = window.contextRoot + '/entity/' + window.detailClass + '/json/getAll';
        var myModal = $('#singleCopyModal');
        var myModal2 = $('#multiCopyModal');

        $DetailTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить деталь (' + window.title + ')',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/' + window.detailClass + '/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'dn'
                },
                {
                    className: 'view-data',
                    data: 'zk'
                },
                {
                    className: 'view-data',
                    data: 'number'
                },
                {
                    className: 'view-data',
                    data: 'melt'
                },
                {
                    className: 'view-data',
                    data: 'drawing'
                },
                {
                    className: 'view-data',
                    data: 'material'
                },
                {
                    className: 'view-data',
                    data: 'certificate'
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        str += '<a class="btn btn-primary multi-copy" >' +
                            '<span class="glyphicon glyphicon-list"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/' + window.detailClass + '/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить эту деталь?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                $DetailTable.DataTable().page('last').draw('page');
            }
        });
        $('#detail-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $DetailTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/' + window.detailClass + '/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $DetailTable.DataTable().row( this ).index();
            $( $DetailTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $DetailTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.multi-copy', function () {
            var data = $DetailTable.DataTable().row( $(this).parents('tr') ).data();
            $('#multiCopyModal #id').val(data['id']);

            myModal2.modal({
                show : true
            });
        }).on( 'click', 'a.copy', function () {
            var data = $DetailTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $DetailTable.DataTable().row( $(this).parents('tr') ).index();
            $( $DetailTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $DetailTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $DetailTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/' + window.detailClass + '/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }

    //BUGEL TABLE (COLUMN, RUNNINGSLEEVE)

    var $BugelTable = $('#bugel-table');

    if($BugelTable.length) {
        var jsonUrl = window.contextRoot + '/entity/' + window.bugelClass + '/json/getAll';
        var myModal = $('#singleCopyModal');
        var myModal2 = $('#multiCopyModal');

        $BugelTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить деталь (' + window.title + ')',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/' + window.bugelClass + '/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'dn'
                },
                {
                    className: 'view-data',
                    data: 'zk'
                },
                {
                    className: 'view-data',
                    data: 'number'
                },
                {
                    className: 'view-data',
                    data: 'drawing'
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        if (window.bugelClass === 'RunningSleeves') {
                            str += '<a class="btn btn-primary multi-copy" >' +
                                '<span class="glyphicon glyphicon-list"></span></a> &#160;';
                        }
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/' + window.bugelClass + '/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить эту деталь?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                if (window.bugelClass === 'Columns') {
                    $BugelTable.DataTable().column( 2 ).visible( false );
                }
                $BugelTable.DataTable().page('last').draw('page');
            }
        });
        $('#bugel-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $BugelTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/' + window.bugelClass + '/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $BugelTable.DataTable().row( this ).index();
            $( $BugelTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $BugelTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.multi-copy', function () {
            var data = $BugelTable.DataTable().row( $(this).parents('tr') ).data();
            $('#multiCopyModal #id').val(data['id']);

            myModal2.modal({
                show : true
            });
        }).on( 'click', 'a.copy', function () {
            var data = $BugelTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $BugelTable.DataTable().row( $(this).parents('tr') ).index();
            $( $BugelTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $BugelTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $BugelTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/' + window.bugelClass + '/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }


    //SEALING TABLE (FRONTALSADDLE, MAINFLANGE, SPRING)

    var $SealingTable = $('#sealing-table');

    if($SealingTable.length) {
        var jsonUrl = window.contextRoot + '/entity/' + window.sealingClass + '/json/getAll';
        var myModal = $('#singleCopyModal');

        $SealingTable.DataTable( {
            dom: 'lBfrtip',
            buttons: [
                {
                    text: 'Добавить (' + window.title + ')',
                    action: function ( e, dt, node, config ) {
                        window.location.href= window.contextRoot + '/entity/' + window.sealingClass + '/showFormForAdd';
                    }
                }
            ],
            lengthMenu: [[5,10,20,-1], ['5','10','20','Все']],
            pageLength: 10,
            ajax: {
                url: jsonUrl,
                dataSrc: ''
            },
            columns: [
                {
                    className: 'view-data',
                    data: 'id'
                },
                {
                    className: 'view-data',
                    data: 'name'
                },
                {
                    className: 'view-data',
                    data: 'batch'
                },
                {
                    className: 'view-data',
                    data: 'certificate'
                },
                {
                    className: 'view-data',
                    data: 'material'
                },
                {
                    className: 'view-data',
                    data: 'drawing'
                },
                {
                    className: 'view-data',
                    data: 'amount'
                },
                {
                    className: 'view-data',
                    data: 'status'
                },
                {
                    data: 'id',
                    bSortable: false,
                    mRender: function (data, type, row) {
                        var str = '';
                        str += '<a class="btn btn-primary copy" >' +
                            '<span class="glyphicon glyphicon-duplicate"></span></a> &#160;';
                        if(userRole === 'ADMIN' || userRole === 'MANAGER') {
                            str += '<a href="' + window.contextRoot + '/entity/' + window.sealingClass
                                + '/delete/' + data + '" class="btn btn-danger" ' +
                                'onclick="if (!(confirm(\'Удалить эту деталь?\'))) return false">' +
                                '<span class="glyphicon glyphicon-remove"></span></a>';
                        }
                        return str;
                    }
                }
            ],
            initComplete: function () {
                $SealingTable.DataTable().page('last').draw('page');
            }
        });
        $('#sealing-table tbody').on('dblclick', 'td.view-data', function () {
            var data = $SealingTable.DataTable().row( $(this).parents('tr') ).data();
            window.location.href= window.contextRoot + '/entity/' + window.sealingClass + '/showFormForUpdate/' + data['id'];
        }).on( 'mouseenter', 'tr', function () {
            var rowIdx = $SealingTable.DataTable().row( this ).index();
            $( $SealingTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $SealingTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
        }).on( 'click', 'a.copy', function () {
            var data = $SealingTable.DataTable().row( $(this).parents('tr') ).data();
            $('#singleCopyModal #id').val(data['id']);

            myModal.modal({
                show : true
            });
        }).on('touchstart','td.view-data', function(e){
            var rowIdx = $SealingTable.DataTable().row( $(this).parents('tr') ).index();
            $( $SealingTable.DataTable().rows().nodes() ).removeClass( 'highlight-entity' );
            if (rowIdx !== undefined) {
                $( $SealingTable.DataTable().row(rowIdx).nodes() ).addClass( 'highlight-entity' );
            }
            if(!tapped){ //if tap is not set, set up single tap
                tapped = setTimeout(function(){
                    tapped = null //insert things you want to do when single tapped
                },300);   //wait 300ms then run single click code
            } else {    //tapped within 300ms of last tap. double tap
                clearTimeout(tapped); //stop single tap callback
                tapped = null
                var data = $SealingTable.DataTable().row( $(this).parents('tr') ).data();
                window.location.href= window.contextRoot + '/entity/' + window.sealingClass + '/showFormForUpdate/' + data['id'];
                e.preventDefault()
            }
        });
    }
    
});




