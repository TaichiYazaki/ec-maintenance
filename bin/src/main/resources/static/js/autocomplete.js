'use strict'

$(function(){ 
  let token = $("meta[name='_csrf']").attr("content");
  let header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function(e, xhr, options) { 
    xhr.setRequestHeader(header, token);
  });
  $.ajax({
    url: 'http://localhost:8080/ec-202201b/item/autocomplete',
    type: 'post',
    dataType: 'json',
    async: true
  }).done((data)=>{
    console.log(data.items)
    $('#autocomplete').autocomplete({
      source: data.items
    });
  }).fail(function(){
    console.log("エラー")
  });
});