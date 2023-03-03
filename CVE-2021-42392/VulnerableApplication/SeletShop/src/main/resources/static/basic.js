let targetId;

$(document).ready(function () {
    // id 가 query 인 녀석 위에서 엔터를 누르면 execSearch() 함수를 실행하라는 뜻입니다.
    $('#query').on('keypress', function (e) {
        if (e.key == 'Enter') {
            execSearch();
        }
    });
    $('#close').on('click', function () {
        $('#container').removeClass('active');
    })

    $('.nav div.nav-see').on('click', function () {
        $('div.nav-see').addClass('active');
        $('div.nav-search').removeClass('active');

        $('#see-area').show();
        $('#search-area').hide();
    })
    $('.nav div.nav-search').on('click', function () {
        $('div.nav-see').removeClass('active');
        $('div.nav-search').addClass('active');

        $('#see-area').hide();
        $('#search-area').show();
    })

    $('#see-area').show();
    $('#search-area').hide();

    showProduct();
})

function showProduct() {
    var isAdmin = false;
    if ($('#admin').length === 1) {
        isAdmin = true
    }

    var sorting = $("#sorting option:selected").val();
    var isAsc = $(':radio[name="isAsc"]:checked').val();
    console.log(sorting, isAsc);

    $('#product-container').empty();
    $('#search-result-box').empty();
    $('#pagination').pagination({
        dataSource: isAdmin ? `/api/admin/products?sortBy=${sorting}&isAsc=${isAsc}` : `/api/products?sortBy=${sorting}&isAsc=${isAsc}`,
        locator: 'content',
        alias: {
            pageNumber: 'page',
            pageSize: 'size'
        },
        totalNumberLocator: (response) => {
            return response.totalElements;
        },
        pageSize: 10,
        showPrevious: true,
        showNext: true,
        ajax: {
            beforeSend: function() {
                $('#product-container').html('상품 불러오는 중...');
            }
        },
        callback: function(data, pagination) {
            $('#product-container').empty();
            for (let i = 0; i < data.length; i++) {
                let product = data[i];
                let tempHtml = addProductItem(product);
                $('#product-container').append(tempHtml);
            }
        }
    });
}

function addProductItem(product) {
    return `<div class="product-card">
                <div onclick="window.location.href='${product.link}'">
                    <div class="card-header">
                        <img src="${product.image}"
                             alt="">
                    </div>
                    <div class="card-body">
                        <div class="title">
                            ${product.title}
                        </div>
                        <div class="lprice">
                            <span>${numberWithCommas(product.lprice)}</span>원
                        </div>
                        <div class="isgood ${product.lprice > product.myprice ? 'none' : ''}">
                            최저가
                        </div>
                    </div>
                </div>
            </div>`;
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function execSearch() {
    let query = $('#query').val();
    if (query == '') {
        alert('검색어를 입력해주세요');
        $('#query').focus();
        return;
    }
    $.ajax({
        type: 'GET',
        url: `/api/search?query=${query}`,
        success: function (response) {
            $('#search-result-box').empty();
            for (let i = 0; i < response.length; i++) {
                let itemDto = response[i];
                let tempHtml = addHTML(itemDto);
                $('#search-result-box').append(tempHtml);
            }
        }
    })
}

function addHTML(itemDto) {
    return `<div class="search-itemDto">
                <div class="search-itemDto-left">
                    <img src="${itemDto.image}" alt="">
                </div>
                <div class="search-itemDto-center">
                    <div>${itemDto.title}</div>
                    <div class="price">
                        ${numberWithCommas(itemDto.lprice)}
                        <span class="unit">원</span>
                    </div>
                </div>
                <div class="search-itemDto-right">
                    <img src="images/icon-save.png" alt="" onclick='addProduct(${JSON.stringify(itemDto)})'>
                </div>
            </div>`
}

function addProduct(itemDto) {
    $.ajax({
        type: "POST",
        url: '/api/products',
        contentType: "application/json",
        data: JSON.stringify(itemDto),
        success: function (response) {
            $('#container').addClass('active');
            targetId = response.id;
        }
    })
}

function setMyprice() {
    let myprice = $('#myprice').val();
    if (myprice == '') {
        alert('올바른 가격을 입력해주세요');
        return;
    }
    $.ajax({
        type: "PUT",
        url: `/api/products/${targetId}`,
        contentType: "application/json",
        data: JSON.stringify({myprice: myprice}),
        success: function (response) {
            $('#container').removeClass('active');
            alert('성공적으로 등록되었습니다.');
            window.location.reload();
        }
    })
}