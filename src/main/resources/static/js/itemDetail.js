'use strict';



$(function() {
	var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });
	calc_price();
	$(".size").on("change", function() {

		calc_price();
	});

	$(".toppingCount").on("change", function() {

		calc_price();
	});

	$("#quantity").on("change", function() {

		calc_price();
	});

	function removeComma(number) {
		var removed = number.replace(/,/g, '');
		return parseInt(removed, 10);
	}
	function calc_price() {
		let size = $(".size:checked").val();
		let topping_count = $("#topping input:checkbox:checked").length;
		let quantity = $("#quantity").val();
		let size_price = 0;
		let topping_price = 0;
		if (size === "M") {
			size_price = removeComma($("#priceM").val());
			topping_price = 200 * topping_count;
		} else {
			size_price = removeComma($("#priceL").val());
			topping_price = 300 * topping_count;
		}
		let price = (size_price + topping_price) * quantity;
		$("#total-price").text(price.toLocaleString());
	}

	$("#cartForm").on('submit', function(e){
		e.preventDefault();
	
		console.log("処理開始");
		
		let toppingList = [];
		$('[class="toppingCount"]:checked').each(function() {
			toppingList.push(($(this).val()))
		});
		



		$.ajax({
			url: $(this).attr("action"),
			type: 'post',
			dataType: 'text',
			data: {
				priceM: $('#priceM').val(),
				priceL: $('#priceL').val(),
				itemId: $('#itemNumber').val(),
				size: $('input:radio[name="size"]:checked').val(),
				toppingId: toppingList.join(),
				quantity: $('#quantity').val(),
			},

			async: true,
		}).done(function() {
			console.log("処理成功");
			$('.popup').addClass('show').fadeIn();
		}).fail(function(XMLHttpRequest, textStatus, errorThrown) {
			alert("正しい結果をえられませんでした");
			console.log("XMLHttpRequest : " + XMLHttpRequest.status);
			console.log("textStatus     : " + textStatus);
			console.log("errorThrown    : " + errorThrown.message);
		})
	return false;

	});
	$('#close').on('click', function() {
		$('.popup').removeClass('show').fadeOut();
	});


});

