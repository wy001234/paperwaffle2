// 각 메뉴 클릭시 서브메뉴 펼치기
$(function(){

    var gnb = $('#gnb > li > a');

    gnb.click(function(e){
        e.preventDefault();

        var isOpen = $(this).next().is(':visible');
        console.log(isOpen);

        if(isOpen){
            $(this).next().slideUp(200);
        }else{
            $(this).next().slideDown(200);
        }

    });

});