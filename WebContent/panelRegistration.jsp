<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" charset="utf-8">
<link rel="stylesheet" href="css/reset.css" type="text/css">
<link rel="stylesheet" href="css/top.css" type="text/css">
<style type="text/css">
.input-file .preview {
  background-image: url(/img/panel/160x120.png);
}
.input-file input[type="file"] {
  opacity: 0;
}
</style>
<script>
//documentと毎回書くのがだるいので$に置き換え
var $ = document; 
var $form = $.querySelector('form');// jQueryの $("form")相当

//jQueryの$(function() { 相当(ただし厳密には違う)
$.addEventListener('DOMContentLoaded', function() {
    //画像ファイルプレビュー表示
    //  jQueryの $('input[type="file"]')相当
    // addEventListenerは on("change", function(e){}) 相当
    $.querySelector('input[type="file"]').addEventListener('change', function(e) {
        var file = e.target.files[0],
               reader = new FileReader(),
               $preview =  $.querySelector(".preview"), // jQueryの $(".preview")相当
               t = this;

        // 画像ファイル以外の場合は何もしない
        if(file.type.indexOf("image") < 0){
          return false;
        }

        reader.onload = (function(file) {
          return function(e) {
             //jQueryの$preview.empty(); 相当
             while ($preview.firstChild) $preview.removeChild($preview.firstChild);

            // imgタグを作成
            var img = document.createElement( 'img' );
            img.setAttribute('src',  e.target.result);
            img.setAttribute('width', '150px');
            img.setAttribute('title',  file.name);
            // imgタグを$previeの中に追加
            $preview.appendChild(img);
          }; 
        })(file);

        reader.readAsDataURL(file);
    }); 
});
</script>
<title>[pane-tori] - パネル登録</title>
</head>
<body>

<div class="page">
	<header>
	</header>
	<div class="wapper">
	
    <form action="regist" method="post" enctype="multipart/form-data">
        <p class="btn_upload">
            画像ファイルを選択してアップロード
        </p>
        
        <input type="file" name="panelImage">
        
        <div class="preview"></div>
        
        パネル名：<input type="text" name="panelName">
        <table border="1">
            <tr>
                <th>表示
                <th>よみ(ひらかな)
            </tr>
            <tr>
                <td><input type="text" name="Disp1">
                <td><input type="text" name="Read1">
            </tr>
            <tr>
                <td><input type="text" name="Disp2">
                <td><input type="text" name="Read2">
            </tr>
            <tr>
                <td><input type="text" name="Disp3">
                <td><input type="text" name="Read3">
            </tr>
            <tr>
                <td><input type="text" name="Disp4">
                <td><input type="text" name="Read4">
            </tr>
            <tr>
                <td><input type="text" name="Disp5">
                <td><input type="text" name="Read5">
            </tr>
            <tr>
                <td><input type="text" name="Disp6">
                <td><input type="text" name="Read6">
            </tr>
            <tr>
                <td><input type="text" name="Disp7">
                <td><input type="text" name="Read7">
            </tr>
            <tr>
                <td><input type="text" name="Disp8">
                <td><input type="text" name="Read8">
            </tr>
        </table>
        <p>
            <input type="submit" value="送信">
        </p>
    </form>
    
    <p><%=request.getAttribute("message")%></p>
	
	<a href="top">トップへ戻る</a>
	
	</div>
	<footer>
	</footer>
</div>

</body>