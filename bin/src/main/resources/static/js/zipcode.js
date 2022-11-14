'use strict'

$(function(){
  $('#zipcode_btn').on('click', function(){
    let hostUrl = 'https://zipcloud.ibsnet.co.jp/api/search';
    let zipcode = $('#zipcode').val();
    $.ajax({
      url: hostUrl,
      type: 'post',
      dataType: 'jsonp',
      data: {
        zipcode: zipcode
      }
    }).done(function(data){
      console.log(data);
      if(data.results === null) {
        $('#zipcode_msg').text('住所が見つかりません')
      }
      $('#address').val(data.results[0].address1 + data.results[0].address2 + data.results[0].address3);
    }).fail(function(){
      alert('エラー')
    });
  });
});